package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.MerchantBaseMapper;
import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.service.IMerchantBaseService;

/**
 * 商家基础信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class MerchantBaseServiceImpl implements IMerchantBaseService 
{
    @Autowired
    private MerchantBaseMapper merchantBaseMapper;

    /**
     * 查询商家基础信息
     * 
     * @param merchantBaseId 商家基础信息主键
     * @return 商家基础信息
     */
    @Override
    public MerchantBase selectMerchantBaseByMerchantBaseId(Long merchantBaseId)
    {
        return merchantBaseMapper.selectMerchantBaseByMerchantBaseId(merchantBaseId);
    }

    /**
     * 查询商家基础信息列表
     * 
     * @param merchantBase 商家基础信息
     * @return 商家基础信息
     */
    @Override
    public List<MerchantBase> selectMerchantBaseList(MerchantBase merchantBase)
    {
        return merchantBaseMapper.selectMerchantBaseList(merchantBase);
    }

    /**
     * 新增商家基础信息
     * 
     * @param merchantBase 商家基础信息
     * @return 结果
     */
    @Override
    public int insertMerchantBase(MerchantBase merchantBase)
    {
        merchantBase.setCreateTime(DateUtils.getNowDate());
        return merchantBaseMapper.insertMerchantBase(merchantBase);
    }

    /**
     * 修改商家基础信息
     * 
     * @param merchantBase 商家基础信息
     * @return 结果
     */
    @Override
    public int updateMerchantBase(MerchantBase merchantBase)
    {
        merchantBase.setUpdateTime(DateUtils.getNowDate());
        return merchantBaseMapper.updateMerchantBase(merchantBase);
    }

    /**
     * 批量删除商家基础信息
     * 
     * @param merchantBaseIds 需要删除的商家基础信息主键
     * @return 结果
     */
    @Override
    public int deleteMerchantBaseByMerchantBaseIds(Long[] merchantBaseIds)
    {
        return merchantBaseMapper.deleteMerchantBaseByMerchantBaseIds(merchantBaseIds);
    }

    /**
     * 删除商家基础信息信息
     * 
     * @param merchantBaseId 商家基础信息主键
     * @return 结果
     */
    @Override
    public int deleteMerchantBaseByMerchantBaseId(Long merchantBaseId)
    {
        return merchantBaseMapper.deleteMerchantBaseByMerchantBaseId(merchantBaseId);
    }

    /**
     * 根据商家ID获取商家名称
     *
     * @param merchantBaseId 商家ID
     * @return 商家名称
     */
    @Override
    public String getNameById(Long merchantBaseId) {
        if (merchantBaseId == null) {
            return null;
        }
        MerchantBase merchantBase = merchantBaseMapper.selectMerchantBaseByMerchantBaseId(merchantBaseId);
        return merchantBase != null ? merchantBase.getMerchantName() : null;
    }

    /**
     * 根据手机号获取商家名称
     *
     * @param phone 手机号
     * @return 商家名称
     */
    @Override
    public String getNameByPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return null;
        }
        MerchantBase merchantBase = merchantBaseMapper.selectMerchantBaseByPhone(phone);
        return merchantBase != null ? merchantBase.getMerchantName() : null;
    }
}
