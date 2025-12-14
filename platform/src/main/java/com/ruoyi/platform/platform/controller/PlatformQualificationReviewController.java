package com.ruoyi.platform.platform.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.platform.service.IPlatformQualificationReviewService;
import com.ruoyi.platform.platform.vo.RiderBaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/platform/qualificationReview")
public class PlatformQualificationReviewController {
    @Autowired
    private IPlatformQualificationReviewService platformQualificationReviewService;

    @GetMapping
    public AjaxResult getAllRiderQualificationStatus(){
        RiderBaseVO riderBaseVO = platformQualificationReviewService.getAllRiderQualificationStatus();
        return AjaxResult.success("查询成功",riderBaseVO);
    }

    @PostMapping
    public AjaxResult setRiderQualificationStatus(@RequestParam Integer status, @RequestParam Integer riderId){
        int result = platformQualificationReviewService.setRiderQualificationStatus(status,riderId);
        return result > 0 ? AjaxResult.success("修改成功") : AjaxResult.error("修改失败");
    }
}
