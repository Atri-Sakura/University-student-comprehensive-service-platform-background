package com.ruoyi.platform.user.service.impl;

import com.ruoyi.platform.domain.IndexImgUrl;
import com.ruoyi.platform.domain.UserTimetable;
import com.ruoyi.platform.domain.vo.SecondhandGoodDetailVO;
import com.ruoyi.platform.merchant.vo.MerchantGoodsVO;
import com.ruoyi.platform.user.mapper.SysUserIndexMapper;
import com.ruoyi.platform.user.service.ISysUserIndexService;
import com.ruoyi.platform.user.vo.SecondhandGoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class SysUserIndexService implements ISysUserIndexService {
    @Autowired
    private SysUserIndexMapper sysUserIndexMapper;

    @Override
    public List<MerchantGoodsVO> getTakeoutRecommendations() {
        return sysUserIndexMapper.getTakeoutRecommendations();
    }

    @Override
    public List<SecondhandGoodVO> getSecondhandRecommendations() {
        return sysUserIndexMapper.getSecondhandRecommendations();
    }

    @Override
    public List<UserTimetable> getTodayCourses(Long userBaseId) {
        //获取星期几
        // 获取当前日期
        LocalDate today = LocalDate.now();

        // 获取星期几（DayOfWeek枚举）
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        // 转换为数字（1=周一，7=周日）
        int weekNumber = dayOfWeek.getValue();
        return sysUserIndexMapper.getTodayCourses(userBaseId, weekNumber);
    }

    @Override
    public List<IndexImgUrl> getIndexImgUrls() {
        return sysUserIndexMapper.getIndexImgUrls();
    }

    @Override
    public List<UserTimetable> getUserCourses(Long userBaseId) {
        return sysUserIndexMapper.getUserCourses(userBaseId);
    }

    @Override
    public int userCoursesAdd(UserTimetable userTimetable) {
        return sysUserIndexMapper.userCoursesAdd(userTimetable);
    }
}
