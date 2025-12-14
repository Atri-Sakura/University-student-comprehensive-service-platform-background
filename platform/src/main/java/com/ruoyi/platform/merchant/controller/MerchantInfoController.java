package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common. enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.MinioFileUtils;
import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.domain.MerchantAddress;
import com.ruoyi.platform.domain.MerchantWallet;
import com. ruoyi.platform.merchant.service.IMerchantInfoService;
import com.ruoyi.platform.merchant.service. IMerchantAddressInfoService;
import com.ruoyi.platform.service.IMerchantWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind. annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 商家基础信息控制器
 * 支持 merchant_base 和 merchant_address 基础信息的查询与修改
 */
@RestController
@RequestMapping("/merchant/info")
public class MerchantInfoController {

    @Autowired
    private IMerchantInfoService merchantInfoService;

    @Autowired
    private IMerchantAddressInfoService merchantAddressInfoService;

    @Autowired
    private IMerchantWalletService merchantWalletService;

    @Autowired
    private MinioFileUtils minioFileUtils;

    /**
     * 查询商家基础信息
     * @return 商家基础信息
     */
    @GetMapping("/base")
    public AjaxResult getMerchantBaseInfo() {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        MerchantBase merchantBase = merchantInfoService.selectMerchantBaseByMerchantBaseId(merchantBaseId);
        if (merchantBase == null) {
            return AjaxResult.error("未找到该商家基础信息");
        }
        return AjaxResult.success(merchantBase);
    }

    /**
     * 修改商家基础信息
     * @param merchantBase 商家基础信息对象
     * @return 操作结果
     */
    @Log(title = "修改商家基础信息", businessType = BusinessType.UPDATE)
    @PutMapping("/base")
    public AjaxResult updateMerchantBase(@RequestBody MerchantBase merchantBase) {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        if (merchantBase == null || merchantBase.getMerchantBaseId() == null || ! merchantBase.getMerchantBaseId().equals(merchantBaseId)) {
            return AjaxResult.error("无权修改该商家信息");
        }
        int result = merchantInfoService.updateMerchantBase(merchantBase);
        if (result > 0) {
            return AjaxResult.success("商家信息修改成功");
        }
        return AjaxResult.error("商家信息修改失败");
    }

    /**
     * 上传商家Logo
     * @param file Logo文件
     * @return 操作结果，包含Logo URL
     */
    @Log(title = "上传商家Logo", businessType = BusinessType.UPDATE)
    @PostMapping("/logo")
    public AjaxResult uploadLogo(@RequestParam("file") MultipartFile file) {
        try {
            // 1. 获取当前登录商家ID
            Long merchantBaseId = SecurityUtils.getMerchantBaseId();
            if (merchantBaseId == null) {
                return AjaxResult. error("获取商家信息失败");
            }

            // 2. 验证文件
            if (file == null || file.isEmpty()) {
                return AjaxResult.error("上传文件不能为空");
            }

            // 3. 验证文件类型（只允许图片）
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return AjaxResult. error("只支持上传图片格式文件");
            }

            // 4. 验证文件大小（限制20MB）
            long maxSize = 20 * 1024 * 1024; // 20MB
            if (file.getSize() > maxSize) {
                return AjaxResult.error("文件大小不能超过20MB");
            }

            // 5. 查询商家原有Logo
            MerchantBase merchantBase = merchantInfoService.selectMerchantBaseByMerchantBaseId(merchantBaseId);
            if (merchantBase == null) {
                return AjaxResult.error("商家信息不存在");
            }
            String oldLogoUrl = merchantBase.getLogo();

            // 6. 上传新Logo到MinIO
            String bucketName = "merchantlogo"; // 商家Logo存储桶
            String logoUrl = minioFileUtils.upload(file, bucketName, merchantBaseId);
            if (logoUrl == null || logoUrl.isEmpty()) {
                return AjaxResult.error("Logo上传失败");
            }

            // 7. 更新数据库中的Logo字段
            merchantBase.setLogo(logoUrl);
            int result = merchantInfoService.updateMerchantBase(merchantBase);
            if (result <= 0) {
                // 如果数据库更新失败，删除已上传的文件
                minioFileUtils. deleteByUrl(logoUrl);
                return AjaxResult.error("Logo更新失败");
            }

            // 8. 删除旧Logo（如果存在且不为空）
            if (oldLogoUrl != null && !oldLogoUrl.isEmpty()) {
                minioFileUtils.safeDeleteByUrl(oldLogoUrl);
            }

            // 9. 返回成功结果
            Map<String, Object> result_data = new HashMap<>();
            result_data.put("logoUrl", logoUrl);
            return AjaxResult.success("Logo上传成功", result_data);

        } catch (Exception e) {
            return AjaxResult.error("Logo上传失败：" + e.getMessage());
        }
    }

    /**
     * 查询商家地址信息
     * @return 商家地址信息
     */
    @GetMapping("/address")
    public AjaxResult getMerchantAddress() {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        MerchantAddress address = merchantAddressInfoService.selectMerchantAddressByMerchantBaseId(merchantBaseId);
        if (address == null) {
            return AjaxResult.error("未找到地址信息");
        }
        return AjaxResult.success(address);
    }

    /**
     * 新建商家地址信息
     * @param merchantAddress 商家地址对象
     * @return 操作结果
     */
    @Log(title = "新建商家地址", businessType = BusinessType.INSERT)
    @PostMapping("/address")
    public AjaxResult createMerchantAddress(@RequestBody @Validated MerchantAddress merchantAddress) {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();

        // 检查该商家是否已有地址（根据业务需求，可能一个商家只能有一个地址）
        MerchantAddress existingAddress = merchantAddressInfoService.selectMerchantAddressByMerchantBaseId(merchantBaseId);
        if (existingAddress != null) {
            return AjaxResult.error("该商家已有地址信息，请使用修改接口");
        }

        merchantAddress.setMerchantBaseId(merchantBaseId);

        // 插入地址
        int result = merchantAddressInfoService.insertMerchantAddress(merchantAddress);
        if (result > 0) {
            return AjaxResult.success("商家地址创建成功", merchantAddress);
        }
        return AjaxResult.error("商家地址创建失败");
    }

    /**
     * 修改商家地址信息
     * @param merchantAddress 商家地址对象
     * @return 操作结果
     */
    @Log(title = "修改商家地址", businessType = BusinessType.UPDATE)
    @PutMapping("/address")
    public AjaxResult updateMerchantAddress(@RequestBody MerchantAddress merchantAddress) {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        MerchantAddress dbAddress = merchantAddressInfoService.selectMerchantAddressByMerchantBaseId(merchantBaseId);
        if (merchantAddress == null || dbAddress == null || ! dbAddress.getMerchantAddressId().equals(merchantAddress.getMerchantAddressId())) {
            return AjaxResult.error("无权修改该商家地址信息");
        }
        int result = merchantAddressInfoService.updateMerchantAddress(merchantAddress);
        if (result > 0) {
            return AjaxResult.success("商家地址修改成功");
        }
        return AjaxResult.error("商家地址修改失败");
    }

    /**
     * 查询当前商家钱包信息
     * @return 钱包信息
     */
    @GetMapping("/wallet")
    public AjaxResult getMerchantWallet() {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        if (merchantBaseId == null) {
            return AjaxResult. error("获取商家信息失败");
        }

        MerchantWallet wallet = merchantWalletService.getWalletByMerchantId(merchantBaseId);
        if (wallet == null) {
            return AjaxResult.error("未找到钱包信息，请先初始化钱包");
        }

        return AjaxResult.success(wallet);
    }

    /**
     * 查询当前商家钱包详细信息（包含统计数据）
     * @return 钱包详细信息
     */
    @GetMapping("/wallet/detail")
    public AjaxResult getMerchantWalletDetail() {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        if (merchantBaseId == null) {
            return AjaxResult.error("获取商家信息失败");
        }

        MerchantWallet wallet = merchantWalletService.getWalletByMerchantId(merchantBaseId);
        if (wallet == null) {
            return AjaxResult.error("未找到钱包信息，请先初始化钱包");
        }

        // 构建详细信息（可选：添加额外统计数据）
        Map<String, Object> detail = new HashMap<>();
        detail.put("merchantWalletId", wallet.getMerchantWalletId());
        detail.put("merchantBaseId", wallet.getMerchantBaseId());
        detail.put("balance", wallet.getBalance());
        detail.put("freezeAmount", wallet.getFreezeAmount());
        detail.put("availableAmount", wallet.getBalance()); // 可用金额 = 余额
        detail.put("totalAmount", wallet.getBalance().add(wallet.getFreezeAmount())); // 总金额 = 余额 + 冻结
        detail.put("createTime", wallet.getCreateTime());
        detail.put("updateTime", wallet.getUpdateTime());

        return AjaxResult.success(detail);
    }

    /**
     * 初始化钱包
     * @return
     */
    @PostMapping("initWallet")
    public AjaxResult initMerchantWallet(@RequestParam(required = false) Long merchantWalletId) {
        // 从Security上下文获取商家ID（更安全）
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        if(merchantBaseId == null){
            return AjaxResult.error("商家ID不能为空");
        }

        // 检查钱包是否已存在，避免重复创建
        MerchantWallet existingWallet = merchantWalletService.getWalletByMerchantId(merchantBaseId);
        if(existingWallet != null){
            return AjaxResult.error("该商家钱包已存在，无需重复创建");
        }

        // 创建钱包对象
        MerchantWallet merchantWallet = new MerchantWallet();
        merchantWallet.setMerchantWalletId(merchantWalletId); // 可选，若使用自增主键可不用设置
        merchantWallet.setMerchantBaseId(merchantBaseId);
        merchantWallet.setBalance(BigDecimal.ZERO); // 初始余额设为0
        merchantWallet.setFreezeAmount(BigDecimal.ZERO); // 初始冻结金额设为0
        merchantWallet.setCreateTime(new Date()); // 设置创建时间
        merchantWallet.setUpdateTime(new Date()); // 设置更新时间

        // 插入数据库
        merchantWalletService.insertMerchantWallet(merchantWallet);

        return AjaxResult.success("钱包初始化成功");
    }
}