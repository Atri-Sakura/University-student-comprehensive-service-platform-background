package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.PlatformTag;

/**
 * 平台标签体系（管理用户和商品标签）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface PlatformTagMapper 
{
    /**
     * 查询平台标签体系（管理用户和商品标签）
     * 
     * @param platformTagId 平台标签体系（管理用户和商品标签）主键
     * @return 平台标签体系（管理用户和商品标签）
     */
    public PlatformTag selectPlatformTagByPlatformTagId(Long platformTagId);

    /**
     * 查询平台标签体系（管理用户和商品标签）列表
     * 
     * @param platformTag 平台标签体系（管理用户和商品标签）
     * @return 平台标签体系（管理用户和商品标签）集合
     */
    public List<PlatformTag> selectPlatformTagList(PlatformTag platformTag);

    /**
     * 新增平台标签体系（管理用户和商品标签）
     * 
     * @param platformTag 平台标签体系（管理用户和商品标签）
     * @return 结果
     */
    public int insertPlatformTag(PlatformTag platformTag);

    /**
     * 修改平台标签体系（管理用户和商品标签）
     * 
     * @param platformTag 平台标签体系（管理用户和商品标签）
     * @return 结果
     */
    public int updatePlatformTag(PlatformTag platformTag);

    /**
     * 删除平台标签体系（管理用户和商品标签）
     * 
     * @param platformTagId 平台标签体系（管理用户和商品标签）主键
     * @return 结果
     */
    public int deletePlatformTagByPlatformTagId(Long platformTagId);

    /**
     * 批量删除平台标签体系（管理用户和商品标签）
     * 
     * @param platformTagIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePlatformTagByPlatformTagIds(Long[] platformTagIds);
}
