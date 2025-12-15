package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.SecondhandGoods;
import com.ruoyi.platform.domain.dto.SecondhandGoodsSearchDTO;
import com.ruoyi.platform.domain.vo.SecondhandGoodDetailVO;
import com.ruoyi.platform.domain.vo.SecondhandGoodsListVO;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 二手商品(简化版)Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface SecondhandGoodsMapper 
{
    /**
     * 搜索二手商品
     * @param dto
     * @return
     */
    public List<SecondhandGoodsListVO> searchSecondhandGoods(SecondhandGoodsSearchDTO dto);
    /**
     * 更新浏览次数
     * @param goodsId
     */
    public int updateSecondhandGoodViewCount(Long goodsId);
    /**
     * 查询已发布二手商品的详情信息
     * @param goodsId
     */
    public SecondhandGoodDetailVO selectSecondhandGoodsDetailById(Long goodsId);
    /**
     * 查询二手商品(简化版)
     * 
     * @param secondhandGoodsId 二手商品(简化版)主键
     * @return 二手商品(简化版)
     */
    public SecondhandGoods selectSecondhandGoodsBySecondhandGoodsId(Long secondhandGoodsId);

    /**
     * 查询二手商品(简化版)列表
     * 
     * @param secondhandGoods 二手商品(简化版)
     * @return 二手商品(简化版)集合
     */
    public List<SecondhandGoods> selectSecondhandGoodsList(SecondhandGoods secondhandGoods);

    /**
     * 新增二手商品(简化版)
     * 
     * @param secondhandGoods 二手商品(简化版)
     * @return 结果
     */
    public int insertSecondhandGoods(SecondhandGoods secondhandGoods);

    /**
     * 修改二手商品(简化版)
     * 
     * @param secondhandGoods 二手商品(简化版)
     * @return 结果
     */
    public int updateSecondhandGoods(SecondhandGoods secondhandGoods);

    /**
     * 删除二手商品(简化版)
     * 
     * @param secondhandGoodsId 二手商品(简化版)主键
     * @return 结果
     */
    public int deleteSecondhandGoodsBySecondhandGoodsId(Long secondhandGoodsId);

    /**
     * 批量删除二手商品(简化版)
     * 
     * @param secondhandGoodsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSecondhandGoodsBySecondhandGoodsIds(Long[] secondhandGoodsIds);

    /**
     * 根据商品分类查询已发布的二手商品
     * @param category
     * @return
     */
    List<SecondhandGoodsListVO> selectSecondhandGoodsListByCategory(@Param("category") String category);

    /**
     * 查询商品详情时使用行锁
     * @param goodsId
     * @return
     */
    SecondhandGoods selectSecondhandGoodsForUpdate(Long goodsId);

    /**
     * 修改商品状态
     * @param goodsId
     * @param status
     */
    int updateSecondhandGoodsStatus(@Param("goodsId") Long goodsId,@Param("status") Long status);

    @Select("select seller_id from order_secondhand_detail where order_main_id = #{orderMainId}")
    Long selectSellerIdByOrderMainId(Long orderMainId);
}
