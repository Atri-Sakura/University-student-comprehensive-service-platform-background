package com.ruoyi.platform.user.service;

import com.ruoyi.platform.domain.IndexImgUrl;
import com.ruoyi.platform.domain.UserTimetable;
import com.ruoyi.platform.domain.vo.SecondhandGoodDetailVO;
import com.ruoyi.platform.merchant.vo.MerchantGoodsVO;
import com.ruoyi.platform.user.vo.SecondhandGoodVO;

import java.util.List;

public interface ISysUserIndexService {
    List<MerchantGoodsVO> getTakeoutRecommendations();

    List<SecondhandGoodVO> getSecondhandRecommendations();

    List<UserTimetable> getTodayCourses(Long userBaseId);

    List<IndexImgUrl> getIndexImgUrls();

    List<UserTimetable> getUserCourses(Long userBaseId);

    int userCoursesAdd(UserTimetable userTimetable);
}
