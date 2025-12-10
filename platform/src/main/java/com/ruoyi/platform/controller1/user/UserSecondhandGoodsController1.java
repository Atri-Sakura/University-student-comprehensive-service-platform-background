package com.ruoyi.platform.controller1.user;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

import com.ruoyi.platform.domain.dto.SecondhandGoodsSearchDTO;
import com.ruoyi.platform.domain.dto.SecondhandOrderCreatDTO;
import com.ruoyi.platform.domain.vo.SecondhandGoodDetailVO;
import com.ruoyi.platform.domain.vo.SecondhandGoodsListVO;
import com.ruoyi.platform.domain.vo.SecondhandOrderContactDetailVO;
import com.ruoyi.platform.service.IOrderMainService;
import com.ruoyi.platform.service.ISecondhandGoodsImageService;
import com.ruoyi.platform.service.ISecondhandGoodsService;
import com.ruoyi.platform.service.ISecondhandOrderService;
import com.ruoyi.platform.service.impl.SecondhandGoodsPublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static com.ruoyi.common.utils.PageUtils.startPage;

@RestController
@RequestMapping("/api/user/secondhandGoods")
@RequiredArgsConstructor
public class UserSecondhandGoodsController1 extends BaseController {
    /**
     * 获取二手交易订单详情（包含联系方式）
     * @param orderNo 订单号
     * @return 订单详情
     */
    @GetMapping("/order/detail/{orderNo}")
    public AjaxResult getSecondhandOrderDetail(@PathVariable String orderNo) {
        Long currentUserBaseId = SecurityUtils.getUserBaseId();
        if (currentUserBaseId == null) {
            return AjaxResult.error("未检测到登录用户信息");
        }

        SecondhandOrderContactDetailVO detailVO =
                secondhandOrderService.getSecondhandOrderDetail(orderNo, currentUserBaseId);

        if (detailVO == null) {
            return AjaxResult.error("订单不存在或无权查看");
        }
        return AjaxResult.success("获取成功", detailVO);
    }


    /**
     * 商品发布服务
     * 目前是强一致性的实现，后续优化方向是使用MQ优化通过最终一致优化性能
     */
    private final SecondhandGoodsPublishService secondhandGoodsPublishService;
    private final ISecondhandGoodsService secondhandGoodsService;
    private final ISecondhandOrderService secondhandOrderService;


    @GetMapping("/search")
    public TableDataInfo search(SecondhandGoodsSearchDTO dto){
        startPage();
        List<SecondhandGoodsListVO> list = secondhandGoodsService.searchSecondhandGoods(dto);
        return getDataTable(list);
    }
    /**
     * 用户确认二手商品的收货
     * @param orderNo 订单号
     * @return 交易确认
     */
    @PostMapping("/order/confirm/{orderNo}")
    public AjaxResult confirmOrder(@PathVariable String orderNo){
        boolean success = secondhandOrderService.confirmOrder(orderNo);
        if( success){
            return AjaxResult.success("确认成功");
        }else{
            return AjaxResult.error("确认失败");
        }
    }



    /**
     * mock支付
     */
    @PostMapping("/order/pay/{orderNo}")
    public AjaxResult payOrder(@PathVariable String orderNo){
        boolean success = secondhandOrderService.payOrder(orderNo);
        return success ? AjaxResult.success("支付成功") : AjaxResult.error("支付失败");
    }
    /**
     * 创建二手交易订单
     * @return 商品详情
     */
    @PostMapping("/order/create")
    public AjaxResult createSecondhandOrder(@RequestBody SecondhandOrderCreatDTO dto){
        String orderNo = secondhandOrderService.createSecondhandOrder(dto);
        if (orderNo != null){
            return AjaxResult.success("创建成功", orderNo);
        }
        return AjaxResult.error("创建失败");
    }
    /**
     * 获取已发布的商品的详情
     *  secondhandGoodsId 商品ID
     */
    @GetMapping("/detail/{goodsId}")
    public AjaxResult getSecondhandGoodsDetail(@PathVariable Long goodsId){
        SecondhandGoodDetailVO detailVO = secondhandGoodsService.getSecondhandGoodsDetail(goodsId);
        return AjaxResult.success("获取成功", detailVO);
    }

    /**
     * 查询已发布的商品
     * @param category 分类(可选)
     * @param pageSize 页码大小(默认1）
     * @param pageNum 页码(默认10)
     * @return 商品主页信息(商品ID，商品名称，商品价格，商品图片，商品描述，商品分类，商品状态，商品创建时间)
     * 待优化，由于每个用户偏好有区别，所有可以优化使用个性推荐算法实现个性推荐，或者调用ai实现个性推荐
     */
    @GetMapping("/list")
    public TableDataInfo listPublishedSecondhandGoods(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ){
        PageHelper.startPage(pageNum, pageSize);
        List<SecondhandGoodsListVO> list = secondhandGoodsService.selectPublishedSecondhandGoodsListByCategory(category);
        return getDataTable(list);
    }

    /**
     * 发布二手商品
     * @param files 图片文件(支持多张）
     * @param goodsName 商品名称
     * @param price 商品价格
     * @param category 商品分类
     * @param description 商品描述
     * @return AjaxResult
     *
     * notice :使用Apifox 测试接口时由于Apifox的string类型，会自动添加content-type导致BigDecimal转换失败识别为null
     */
    @PostMapping("/publish")
    public AjaxResult publishSecondhandGood(
            @RequestPart("files") MultipartFile[] files,
            @RequestParam("goodsName") String goodsName,
            @RequestParam("price") BigDecimal price,
            @RequestParam("category") String category,
            @RequestParam(value = "description" , required = false) String description
    ){
        try {
            Long userId = SecurityUtils.getUserBaseId();
            if (userId == null) {
                return AjaxResult.error("未检测到登录用户信息");
            }

            secondhandGoodsPublishService.publishGood(userId, goodsName, price, category, description, files);
            return AjaxResult.success("发布成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("发布失败：" + e.getMessage());
        }
//        try {
//            Long userId = SecurityUtils.getUserBaseId();
//            if(userId == null){
//                return AjaxResult.error("未检测到登录用户信息");
//            }
//            // Step 1️⃣：保存商品主信息
//            SecondhandGoods goods = new SecondhandGoods();
//            goods.setUserBaseId(userId);
//            goods.setGoodsName(goodsName);
//            goods.setCategory(category);
//            goods.setPrice(price);
//            goods.setDescription(description);
//            goods.setStatus(1L); // 默认在售中
//
//            secondhandGoodsService.insertSecondhandGoods(goods);
//            // Step 2️⃣：循环上传图片并保存图片表
//            List<SecondhandGoodsImage> imageList = new ArrayList<>();
//            Long sortOrder = 0L;
//            for(MultipartFile file: files){
//                String imageUrl = minioFileUtils.upload(file, "secondhand", userId);
//
//                SecondhandGoodsImage image = new SecondhandGoodsImage();
//                image.setSecondhandGoodsId(goods.getSecondhandGoodsId());
//                image.setImageUrl(imageUrl);
//                image.setIsMain(sortOrder == 0L ? 1L : 0L); // 第一张为主图
//                image.setSortOrder(sortOrder++);
//                imageList.add(image);
//            }
//
//            secondhandGoodsImageService.insertSecondhandGoodsImages(imageList);
//
//            return AjaxResult.success("发布成功", goods);
//        }catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResult.error("发布失败：" + e.getMessage());
//        }
    }

}
