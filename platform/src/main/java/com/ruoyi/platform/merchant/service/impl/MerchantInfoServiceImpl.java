package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.merchant.mapper.MerchantInfoMapper;
import com.ruoyi.platform.merchant.service.IMerchantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 商家基础信息Service实现类
 * 负责商家信息的具体查询和修改逻辑
 */
@Service
public class MerchantInfoServiceImpl implements IMerchantInfoService
{
    @Autowired
    private MerchantInfoMapper merchantInfoMapper;

    /**
     * 根据商家ID查询商家基础信息
     * @param merchantBaseId 商家ID
     * @return 商家基础信息
     */
    @Override
    public MerchantBase selectMerchantBaseByMerchantBaseId(Long merchantBaseId)
    {
        return merchantInfoMapper.selectMerchantBaseByMerchantBaseId(merchantBaseId);
    }

    /**
     * 修改商家基础信息
     * @param merchantBase 商家基础信息
     * @return 修改结果（受影响的行数）
     */
    @Override
    public int updateMerchantBase(MerchantBase merchantBase)
    {
        try {
            merchantBase.setUpdateTime(DateUtils.getNowDate());
            int result = merchantInfoMapper.updateMerchantBase(merchantBase);
            if (result == 0) {
                throw new RuntimeException("修改商家信息失败：未找到对应的商家记录");
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("修改商家信息失败：" + e.getMessage(), e);
        }
    }


}