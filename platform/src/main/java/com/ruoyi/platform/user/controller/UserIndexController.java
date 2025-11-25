package com.ruoyi.platform.user.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.IndexImgUrl;
import com.ruoyi.platform.domain.UserTimetable;
import com.ruoyi.platform.domain.vo.SecondhandGoodDetailVO;
import com.ruoyi.platform.merchant.vo.MerchantGoodsVO;
import com.ruoyi.platform.user.service.ISysUserIndexService;
import com.ruoyi.platform.user.vo.SecondhandGoodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/index")
public class UserIndexController {
    @Autowired
    private ISysUserIndexService sysUserIndexService;
    //推荐外卖接口
    @GetMapping("/takeout")
    public AjaxResult getTakeoutRecommendations() {
        Long userBaseId = SecurityUtils.getUserBaseId();
        if (userBaseId == null) {
            return AjaxResult.error("用户未登录");
        }
        List<MerchantGoodsVO> takeoutRecommendations = sysUserIndexService.getTakeoutRecommendations();
        return takeoutRecommendations != null ? AjaxResult.success("获取成功",takeoutRecommendations) : AjaxResult.error("获取失败");
    }
    //推荐二手商品接口
    @GetMapping("/secondhand")
    public AjaxResult getSecondhandRecommendations() {
        Long userBaseId = SecurityUtils.getUserBaseId();
        if (userBaseId == null) {
            return AjaxResult.error("用户未登录");
        }
        List<SecondhandGoodVO> secondhandRecommendations = sysUserIndexService.getSecondhandRecommendations();
        return secondhandRecommendations != null ? AjaxResult.success("获取成功",secondhandRecommendations) : AjaxResult.error("获取失败");
    }
    //今日课程接口
    @GetMapping("/todayCourses")
    public AjaxResult getTodayCourses() {
        Long userBaseId = SecurityUtils.getUserBaseId();
        if (userBaseId == null) {
            return AjaxResult.error("用户未登录");
        }
        List<UserTimetable> todayCourses = sysUserIndexService.getTodayCourses(userBaseId);
        return todayCourses !=null ? AjaxResult.success("获取成功",todayCourses) : AjaxResult.error("获取失败");
    }
    //用户课程列表
    @GetMapping("/userCourses")
    public AjaxResult getUserCourses() {
        Long userBaseId = SecurityUtils.getUserBaseId();
        if (userBaseId == null) {
            return AjaxResult.error("用户未登录");
        }
        List<UserTimetable> userCourses = sysUserIndexService.getUserCourses(userBaseId);
        return userCourses !=null ? AjaxResult.success("获取成功",userCourses) : AjaxResult.error("获取失败");
    }
    //首页轮播图
    @GetMapping("/carousel")
    public AjaxResult getCarousel() {
        List<IndexImgUrl> indexImgUrls = sysUserIndexService.getIndexImgUrls();
        return indexImgUrls != null ? AjaxResult.success("获取成功",indexImgUrls) : AjaxResult.error("获取失败");
    }
}
