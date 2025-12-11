package com.ruoyi.platform.user.mapper;

import com.ruoyi.platform.domain.IndexImgUrl;
import com.ruoyi.platform.domain.UserTimetable;
import com.ruoyi.platform.domain.vo.SecondhandGoodDetailVO;
import com.ruoyi.platform.merchant.vo.MerchantGoodsVO;
import com.ruoyi.platform.user.vo.SecondhandGoodVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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

    @Insert("INSERT INTO user_timetable (user_base_id, course_name, teacher_name, class_room , week_day, start_period, end_period, start_time, end_time, start_date,end_date, create_time,import_source) VALUES (#{userBaseId}, #{courseName}, #{teacherName},#{classRoom},#{weekDay},#{startPeriod},#{endPeriod},#{startTime},#{endTime},#{startDate},#{endDate},#{NOW()},#{importSource})")
    int userCoursesAdd(UserTimetable userTimetable);

    @Delete("DELETE FROM user_timetable WHERE user_timetable_id = #{userTimetableId}")
    int userCoursesDelete(Long userTimetableId);
}
