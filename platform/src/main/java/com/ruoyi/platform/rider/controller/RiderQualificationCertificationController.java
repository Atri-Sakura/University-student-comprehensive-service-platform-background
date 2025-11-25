package com.ruoyi.platform.rider.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.MinioFileUtils;
import com.ruoyi.platform.rider.service.IRiderQualificationCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/rider/qualificationCertification")
public class RiderQualificationCertificationController {
    @Autowired
    private IRiderQualificationCertificationService riderQualificationCertificationService;

    @PostMapping("/upload")
    public AjaxResult addIdCardFront(@RequestParam("file") MultipartFile file,@RequestParam("type") Integer type){
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        if (riderBaseId == null){
            return AjaxResult.error("请先登录");
        }
        //type 1 身份证正面国徽面 2 身份证反面
        int rows = riderQualificationCertificationService.upload(file,type,riderBaseId);
        return rows > 0 ? AjaxResult.success("上传成功") : AjaxResult.error("上传失败");
    }

}
