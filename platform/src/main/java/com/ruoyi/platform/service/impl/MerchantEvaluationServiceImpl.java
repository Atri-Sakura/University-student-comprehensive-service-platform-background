package com.ruoyi.platform.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.common.utils.file.MinioFileUtils;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.domain.vo.*;
import com.ruoyi.platform.mapper.OrderMainMapper;
import com.ruoyi.platform.mapper.UserBaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.MerchantEvaluationMapper;
import com.ruoyi.platform.domain.MerchantEvaluation;
import com.ruoyi.platform.service.IMerchantEvaluationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 商家评价Service业务层处理
 *
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class MerchantEvaluationServiceImpl implements IMerchantEvaluationService
{
    private static final Logger log = LoggerFactory.getLogger(MerchantEvaluationServiceImpl.class);

    @Autowired
    private MerchantEvaluationMapper merchantEvaluationMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private MinioFileUtils minioFileUtils;

    /**
     * MinIO 存储桶名称（评价图片专用）
     */
    private static final String BUCKET_NAME = "evaluation";

    /**
     * 最大上传图片数量
     */
    private static final int MAX_IMAGE_COUNT = 9;

    /**
     * 单张图片最大大小（5MB）
     */
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;

    /**
     * 生成Long类型的评价ID
     *
     * @return Long类型ID
     */
    private Long generateLongId()
    {
        String uuid = IdUtils.fastSimpleUUID();
        String hexString = uuid.substring(0, 8); // 取前8位16进制
        try {
            // 将8位16进制字符串转换为Long（最大值为 4294967295，10位数字）
            return Long.parseLong(hexString, 16);
        } catch (NumberFormatException e) {
            // 如果转换失败，使用时间戳作为备用方案
            log.warn("ID生成失败，使用时间戳备用方案", e);
            return System.currentTimeMillis();
        }
    }

    /**
     * 用户新增评价（带完整业务逻辑校验 + 图片上传）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUserEvaluation(MerchantEvaluationAddReq req, Long userBaseId) {
        // 1. 基础校验：用户ID必须存在
        if (userBaseId == null) {
            throw new ServiceException("用户身份异常，无法评价");
        }

        // 2. 查询订单完整信息
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(req.getOrderId());

        // 3. 校验订单是否存在
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 4. 【安全校验】订单归属权验证：必须是当前登录用户的订单
        if (!order.getUserId().equals(userBaseId)) {
            throw new ServiceException("无权评价非本人的订单");
        }

        // 5. 【状态校验】订单状态验证：只有已完成(5)的订单可以评价
        // 状态码对照：订单状态：1-商家待接单 2-骑手待接单 3-骑手待取货 4-配送中 5-已完成 6-已取消 7-异常报备
        if (order.getOrderStatus() != 5L) {
            throw new ServiceException("订单未完成，暂无法进行评价");
        }

        // 6. 【防刷校验】检查是否已评价，防止重复提交
        MerchantEvaluation queryEval = new MerchantEvaluation();
        queryEval.setOrderId(req.getOrderId());
        // 只查该用户的
        queryEval.setUserId(userBaseId);
        List<MerchantEvaluation> existingEvals = merchantEvaluationMapper.selectMerchantEvaluationList(queryEval);
        if (existingEvals != null && !existingEvals.isEmpty()) {
            throw new ServiceException("该订单已评价，请勿重复操作");
        }

        // 7. 【图片上传】处理评价图片
        String imgUrls = null;
        if (req.getImages() != null && !req.getImages().isEmpty()) {
            imgUrls = uploadImages(req.getImages(), userBaseId);
        }

        // 8. 构建评价实体 (数据清洗与组装)
        MerchantEvaluation evaluation = new MerchantEvaluation();

        // 【关键修改】生成评价ID
        evaluation. setMerchantEvaluationId(generateLongId());

        // 自动从订单中获取商家ID，确保数据一致性，不信任前端传的商家ID
        evaluation.setMerchantBaseId(order.getMerchantId());

        evaluation.setUserId(userBaseId);
        evaluation.setOrderId(req.getOrderId());
        evaluation.setRating(req.getRating());

        // 可选评分
        evaluation.setTasteScore(req.getTasteScore() != null ? req.getTasteScore() : req.getRating());
        evaluation.setPackageScore(req.getPackageScore() != null ? req.getPackageScore() : req.getRating());

        evaluation.setContent(req.getContent());
        evaluation.setImgUrls(imgUrls); // 使用上传后的图片URL
        evaluation.setCreateTime(DateUtils.getNowDate());
        // 商家回复留空，replyTime留空

        // 9. 执行插入
        return merchantEvaluationMapper.insertMerchantEvaluation(evaluation);
    }

    /**
     * 用户修改评价（带权限校验 + 图片处理）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserEvaluation(MerchantEvaluationUpdateReq req, Long userBaseId) {
        // 1. 基础校验：用户ID必须存在
        if (userBaseId == null) {
            throw new ServiceException("用户身份异常，无法修改评价");
        }

        // 2. 校验评价ID是否存在
        if (req.getMerchantEvaluationId() == null) {
            throw new ServiceException("评价ID不能为空");
        }

        // 3. 查询原评价信息
        MerchantEvaluation existingEval = merchantEvaluationMapper.selectMerchantEvaluationByMerchantEvaluationId(
                req.getMerchantEvaluationId());

        // 4. 校验评价是否存在
        if (existingEval == null) {
            throw new ServiceException("评价不存在");
        }

        // 5. 【安全校验】评价归属权验证：必须是当前登录用户的评价
        if (!existingEval.getUserId().equals(userBaseId)) {
            throw new ServiceException("无权修改他人的评价");
        }

        // 6. 【图片处理】处理修改时的图片
        String finalImgUrls = processUpdateImages(
                existingEval.getImgUrls(),
                req.getKeepImgUrls(),
                req.getNewImages(),
                userBaseId
        );

        // 7. 只允许修改特定字段，防止篡改关键数据
        MerchantEvaluation updateEval = new MerchantEvaluation();
        updateEval.setMerchantEvaluationId(req.getMerchantEvaluationId());
        updateEval.setRating(req.getRating());
        updateEval.setTasteScore(req.getTasteScore());
        updateEval.setPackageScore(req.getPackageScore());
        updateEval. setContent(req.getContent());
        updateEval.setImgUrls(finalImgUrls);
        // 禁止修改：userId, merchantBaseId, orderId, createTime 等

        // 8. 执行更新
        return merchantEvaluationMapper.updateMerchantEvaluation(updateEval);
    }

    /**
     * 用户删除单个评价（带权限校验 + 图片删除）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserEvaluation(Long merchantEvaluationId, Long userBaseId) {
        // 1. 基础校验：用户ID必须存在
        if (userBaseId == null) {
            throw new ServiceException("用户身份异常，无法删除评价");
        }

        // 2. 校验评价ID是否存在
        if (merchantEvaluationId == null) {
            throw new ServiceException("评价ID不能为空");
        }

        // 3. 查询评价信息
        MerchantEvaluation existingEval = merchantEvaluationMapper. selectMerchantEvaluationByMerchantEvaluationId(merchantEvaluationId);

        // 4. 校验评价是否存在
        if (existingEval == null) {
            throw new ServiceException("评价不存在");
        }

        // 5. 【安全校验】评价归属权验证：必须是当前登录用户的评价
        if (!existingEval.getUserId().equals(userBaseId)) {
            throw new ServiceException("无权删除他人的评价");
        }

        // 6. 【删除图片】先删除关联的图片文件
        deleteEvaluationImages(existingEval.getImgUrls());

        // 7. 执行删除
        return merchantEvaluationMapper.deleteMerchantEvaluationByMerchantEvaluationId(merchantEvaluationId);
    }

    /**
     * 用户批量删除评价（带权限校验 + 图片删除）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserEvaluationBatch(Long[] merchantEvaluationIds, Long userBaseId) {
        // 1. 基础校验：用户ID必须存在
        if (userBaseId == null) {
            throw new ServiceException("用户身份异常，无法删除评价");
        }

        // 2. 校验评价ID数组
        if (merchantEvaluationIds == null || merchantEvaluationIds.length == 0) {
            throw new ServiceException("评价ID不能为空");
        }

        // 3. 逐个校验权限（确保每个评价都属于当前用户）
        int successCount = 0;
        StringBuilder errorMsg = new StringBuilder();

        for (Long evalId : merchantEvaluationIds) {
            try {
                // 复用单个删除的逻辑
                deleteUserEvaluation(evalId, userBaseId);
                successCount++;
            } catch (ServiceException e) {
                errorMsg.append("评价ID[").append(evalId).append("]:  ").append(e.getMessage()).append("; ");
            }
        }

        // 4. 如果有失败的，抛出异常（事务回滚）
        if (successCount < merchantEvaluationIds.length) {
            throw new ServiceException("批量删除失败：" + errorMsg.toString());
        }

        return successCount;
    }

    // ================= 图片处理辅助方法 =================

    /**
     * 上传多张图片到MinIO
     *
     * @param images 图片文件列表
     * @param userId 用户ID
     * @return 图片URL字符串（逗号分隔）
     */
    private String uploadImages(List<MultipartFile> images, Long userId) {
        if (images == null || images.isEmpty()) {
            return null;
        }

        // 校验图片数量
        if (images.size() > MAX_IMAGE_COUNT) {
            throw new ServiceException("最多只能上传" + MAX_IMAGE_COUNT + "张图片");
        }

        List<String> uploadedUrls = new ArrayList<>();

        try {
            for (MultipartFile image : images) {
                // 跳过空文件
                if (image. isEmpty()) {
                    continue;
                }

                // 校验文件大小
                if (image.getSize() > MAX_IMAGE_SIZE) {
                    throw new ServiceException("图片大小不能超过5MB");
                }

                // 校验文件类型
                String contentType = image.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new ServiceException("只能上传图片文件");
                }

                // 上传到MinIO
                String imageUrl = minioFileUtils.upload(image, BUCKET_NAME, userId);
                uploadedUrls.add(imageUrl);
            }

            // 返回逗号分隔的URL字符串
            return uploadedUrls.isEmpty() ? null : String.join(",", uploadedUrls);

        } catch (Exception e) {
            // 上传失败，清理已上传的文件
            for (String url : uploadedUrls) {
                minioFileUtils.safeDeleteByUrl(url);
            }
            log.error("图片上传失败：{}", e.getMessage(), e);
            throw new ServiceException("图片上传失败：" + e.getMessage());
        }
    }

    /**
     * 处理修改时的图片（删除旧图 + 上传新图）
     *
     * @param oldImgUrls 原有图片URL
     * @param keepImgUrls 保留的图片URL
     * @param newImages 新增的图片文件
     * @param userId 用户ID
     * @return 最终的图片URL字符串
     */
    private String processUpdateImages(String oldImgUrls, String keepImgUrls,
                                       List<MultipartFile> newImages, Long userId) {
        List<String> finalUrls = new ArrayList<>();

        // 1. 处理保留的图片
        if (StringUtils.isNotEmpty(keepImgUrls)) {
            finalUrls.addAll(Arrays. asList(keepImgUrls.split(",")));
        }

        // 2. 上传新图片
        if (newImages != null && !newImages.isEmpty()) {
            String newUrls = uploadImages(newImages, userId);
            if (StringUtils.isNotEmpty(newUrls)) {
                finalUrls. addAll(Arrays.asList(newUrls.split(",")));
            }
        }

        // 3. 校验总数量
        if (finalUrls.size() > MAX_IMAGE_COUNT) {
            throw new ServiceException("图片总数不能超过" + MAX_IMAGE_COUNT + "张");
        }

        // 4. 删除被移除的旧图片
        if (StringUtils.isNotEmpty(oldImgUrls)) {
            List<String> oldUrlList = Arrays.asList(oldImgUrls.split(","));
            List<String> toDelete = oldUrlList.stream()
                    .filter(url -> !finalUrls.contains(url))
                    .collect(Collectors.toList());

            for (String url : toDelete) {
                minioFileUtils.safeDeleteByUrl(url);
            }
        }

        return finalUrls.isEmpty() ? null : String.join(",", finalUrls);
    }

    /**
     * 删除评价相关的图片
     *
     * @param imgUrls 图片URL字符串（逗号分隔）
     */
    private void deleteEvaluationImages(String imgUrls) {
        if (StringUtils.isEmpty(imgUrls)) {
            return;
        }

        List<String> urlList = Arrays. asList(imgUrls.split(","));
        for (String url : urlList) {
            minioFileUtils.safeDeleteByUrl(url. trim());
        }
    }

    @Override
    public MerchantEvaluation selectMerchantEvaluationByMerchantEvaluationId(Long merchantEvaluationId)
    {
        return merchantEvaluationMapper.selectMerchantEvaluationByMerchantEvaluationId(merchantEvaluationId);
    }

    @Override
    public List<MerchantEvaluation> selectMerchantEvaluationList(MerchantEvaluation merchantEvaluation)
    {
        return merchantEvaluationMapper.selectMerchantEvaluationList(merchantEvaluation);
    }

    @Override
    public int insertMerchantEvaluation(MerchantEvaluation merchantEvaluation)
    {
        merchantEvaluation.setCreateTime(DateUtils.getNowDate());
        return merchantEvaluationMapper.insertMerchantEvaluation(merchantEvaluation);
    }

    @Override
    public int updateMerchantEvaluation(MerchantEvaluation merchantEvaluation)
    {
        return merchantEvaluationMapper.updateMerchantEvaluation(merchantEvaluation);
    }

    @Override
    public int deleteMerchantEvaluationByMerchantEvaluationIds(Long[] merchantEvaluationIds)
    {
        return merchantEvaluationMapper.deleteMerchantEvaluationByMerchantEvaluationIds(merchantEvaluationIds);
    }

    @Override
    public int deleteMerchantEvaluationByMerchantEvaluationId(Long merchantEvaluationId)
    {
        return merchantEvaluationMapper.deleteMerchantEvaluationByMerchantEvaluationId(merchantEvaluationId);
    }

    /**
     * 商家查询评价列表（带高级筛选）
     */
    @Override
    public List<MerchantEvaluationDetailVO> getMerchantEvaluationList(
            MerchantEvaluationQueryReq req, Long merchantBaseId) {

        // 1. 基础校验
        if (merchantBaseId == null) {
            throw new ServiceException("商家身份异常");
        }

        // 2. 查询评价列表
        List<MerchantEvaluation> evaluationList = merchantEvaluationMapper
                .selectMerchantEvaluationListByMerchant(merchantBaseId, req);

        // 3. 转换为VO
        List<MerchantEvaluationDetailVO> result = new ArrayList<>();
        for (MerchantEvaluation evaluation : evaluationList) {
            MerchantEvaluationDetailVO vo = convertToDetailVO(evaluation);
            result. add(vo);
        }

        return result;
    }

    /**
     * 商家查询评价统计信息
     */
    @Override
    public MerchantEvaluationStatisticsVO getEvaluationStatistics(Long merchantBaseId) {
        // 1. 基础校验
        if (merchantBaseId == null) {
            throw new ServiceException("商家身份异常");
        }

        // 2. 查询统计信息
        MerchantEvaluationStatisticsVO statistics = merchantEvaluationMapper
                .selectEvaluationStatistics(merchantBaseId);

        // 3. 处理可能的null值
        if (statistics == null) {
            statistics = new MerchantEvaluationStatisticsVO();
            statistics.setTotalCount(0L);
            statistics.setAvgRating(0.0);
            statistics.setFiveStarCount(0L);
            statistics.setFourStarCount(0L);
            statistics.setThreeStarCount(0L);
            statistics.setTwoStarCount(0L);
            statistics.setOneStarCount(0L);
            statistics. setPendingReplyCount(0L);
            statistics.setWithImageCount(0L);
        }

        return statistics;
    }

    /**
     * 商家回复评价
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int replyEvaluation(MerchantEvaluationReplyReq req, Long merchantBaseId) {
        // 1. 基础校验
        if (merchantBaseId == null) {
            throw new ServiceException("商家身份异常，无法回复评价");
        }

        // 2. 查询评价信息
        MerchantEvaluation existingEval = merchantEvaluationMapper
                .selectMerchantEvaluationByMerchantEvaluationId(req.getMerchantEvaluationId());

        // 3. 校验评价是否存在
        if (existingEval == null) {
            throw new ServiceException("评价不存在");
        }

        // 4. 【安全校验】评价归属权验证：必须是当前商家的评价
        if (! existingEval.getMerchantBaseId().equals(merchantBaseId)) {
            throw new ServiceException("无权回复其他商家的评价");
        }

        // 5. 【业务校验】检查是否已回复
        if (StringUtils.isNotEmpty(existingEval.getMerchantReply())) {
            throw new ServiceException("该评价已回复，如需修改请联系管理员");
        }

        // 6. 构建更新对象
        MerchantEvaluation updateEval = new MerchantEvaluation();
        updateEval.setMerchantEvaluationId(req.getMerchantEvaluationId());
        updateEval.setMerchantReply(req.getMerchantReply());
        updateEval.setReplyTime(DateUtils.getNowDate());

        // 7. 执行更新
        return merchantEvaluationMapper.updateMerchantEvaluation(updateEval);
    }

    /**
     * 商家查询评价详情
     */
    @Override
    public MerchantEvaluationDetailVO getEvaluationDetail(
            Long merchantEvaluationId, Long merchantBaseId) {

        // 1. 基础校验
        if (merchantBaseId == null) {
            throw new ServiceException("商家身份异常");
        }

        // 2. 查询评价信息
        MerchantEvaluation evaluation = merchantEvaluationMapper
                .selectMerchantEvaluationByMerchantEvaluationId(merchantEvaluationId);

        // 3. 校验评价是否存在
        if (evaluation == null) {
            throw new ServiceException("评价不存在");
        }

        // 4. 【安全校验】评价归属权验证
        if (!evaluation.getMerchantBaseId().equals(merchantBaseId)) {
            throw new ServiceException("无权查看其他商家的评价");
        }

        // 5. 转换为VO
        return convertToDetailVO(evaluation);
    }

    // ================= 辅助方法 =================

    /**
     * 将MerchantEvaluation转换为MerchantEvaluationDetailVO
     *
     * @param evaluation 评价实体
     * @return 评价详情VO
     */
    private MerchantEvaluationDetailVO convertToDetailVO(MerchantEvaluation evaluation) {
        MerchantEvaluationDetailVO vo = new MerchantEvaluationDetailVO();

        // 1. 复制基本属性
        BeanUtils.copyProperties(evaluation, vo);

        // 2. 查询用户信息（脱敏）
        try {
            UserBase user = userBaseMapper.selectUserBaseByUserBaseId(evaluation.getUserId());
            if (user != null) {
                // 用户昵称脱敏：只显示第一个字 + ***
                String nickname = user.getNickname();
                if (StringUtils.isNotEmpty(nickname)) {
                    if (nickname.length() == 1) {
                        vo.setUserNickname(nickname + "***");
                    } else {
                        vo.setUserNickname(nickname.substring(0, 1) + "***");
                    }
                } else {
                    vo.setUserNickname("匿名用户");
                }
                vo.setUserAvatar(user.getAvatar());
            } else {
                vo.setUserNickname("匿名用户");
            }
        } catch (Exception e) {
            log.warn("查询用户信息失败，userId={}", evaluation.getUserId(), e);
            vo.setUserNickname("匿名用户");
        }

        // 3. 处理图片列表
        if (StringUtils.isNotEmpty(evaluation. getImgUrls())) {
            List<String> imageList = Arrays.asList(evaluation.getImgUrls().split(","));
            vo.setImageList(imageList);
        }

        // 4. 设置是否已回复标志
        vo.setHasReply(StringUtils.isNotEmpty(evaluation. getMerchantReply()));

        return vo;
    }
}