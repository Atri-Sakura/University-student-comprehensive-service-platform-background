package com.ruoyi.platform.user.mapper;

import com.ruoyi.platform.domain.IndexImgUrl;
import com.ruoyi.platform.domain.UserTimetable;
import com.ruoyi.platform.domain.vo.SecondhandGoodDetailVO;
import com.ruoyi.platform.merchant.vo.MerchantGoodsVO;
import com.ruoyi.platform.user.vo.SecondhandGoodVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserIndexMapper {
    List<MerchantGoodsVO> getTakeoutRecommendations();

    List<SecondhandGoodVO> getSecondhandRecommendations();

    @Select("SELECT * FROM user_timetable WHERE user_base_id = #{userBaseId} AND week_day = #{weekNumber}")
    List<UserTimetable> getTodayCourses(Long userBaseId,Integer weekNumber);

    @Select("SELECT * FROM index_image_url")
    List<IndexImgUrl> getIndexImgUrls();

    @Select("SELECT * FROM user_timetable WHERE user_base_id = #{userBaseId}")
    List<UserTimetable> getUserCourses(Long userBaseId);
}
