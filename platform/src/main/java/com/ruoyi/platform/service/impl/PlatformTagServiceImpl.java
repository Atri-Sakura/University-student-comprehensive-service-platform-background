package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.PlatformTagMapper;
import com.ruoyi.platform.domain.PlatformTag;
import com.ruoyi.platform.service.IPlatformTagService;

/**
 * 平台标签体系（管理用户和商品标签）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class PlatformTagServiceImpl implements IPlatformTagService 
{
    @Autowired
    private PlatformTagMapper platformTagMapper;

    /**
     * 查询平台标签体系（管理用户和商品标签）
     * 
     * @param platformTagId 平台标签体系（管理用户和商品标签）主键
     * @return 平台标签体系（管理用户和商品标签）
     */
    @Override
    public PlatformTag selectPlatformTagByPlatformTagId(Long platformTagId)
    {
        return platformTagMapper.selectPlatformTagByPlatformTagId(platformTagId);
    }

    /**
     * 查询平台标签体系（管理用户和商品标签）列表
     * 
     * @param platformTag 平台标签体系（管理用户和商品标签）
     * @return 平台标签体系（管理用户和商品标签）
     */
    @Override
    public List<PlatformTag> selectPlatformTagList(PlatformTag platformTag)
    {
        return platformTagMapper.selectPlatformTagList(platformTag);
    }

    /**
     * 新增平台标签体系（管理用户和商品标签）
     * 
     * @param platformTag 平台标签体系（管理用户和商品标签）
     * @return 结果
     */
    @Override
    public int insertPlatformTag(PlatformTag platformTag)
    {
        platformTag.setCreateTime(DateUtils.getNowDate());
        return platformTagMapper.insertPlatformTag(platformTag);
    }

    /**
     * 修改平台标签体系（管理用户和商品标签）
     * 
     * @param platformTag 平台标签体系（管理用户和商品标签）
     * @return 结果
     */
    @Override
    public int updatePlatformTag(PlatformTag platformTag)
    {
        platformTag.setUpdateTime(DateUtils.getNowDate());
        return platformTagMapper.updatePlatformTag(platformTag);
    }

    /**
     * 批量删除平台标签体系（管理用户和商品标签）
     * 
     * @param platformTagIds 需要删除的平台标签体系（管理用户和商品标签）主键
     * @return 结果
     */
    @Override
    public int deletePlatformTagByPlatformTagIds(Long[] platformTagIds)
    {
        return platformTagMapper.deletePlatformTagByPlatformTagIds(platformTagIds);
    }

    /**
     * 删除平台标签体系（管理用户和商品标签）信息
     * 
     * @param platformTagId 平台标签体系（管理用户和商品标签）主键
     * @return 结果
     */
    @Override
    public int deletePlatformTagByPlatformTagId(Long platformTagId)
    {
        return platformTagMapper.deletePlatformTagByPlatformTagId(platformTagId);
    }
}
