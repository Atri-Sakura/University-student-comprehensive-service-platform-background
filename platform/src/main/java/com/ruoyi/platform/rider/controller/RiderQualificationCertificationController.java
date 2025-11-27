package com.ruoyi.platform.rider.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.rider.service.IRiderQualificationCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/rider/qualificationCertification")
public class RiderQualificationCertificationController {
    @Autowired
    private IRiderQualificationCertificationService riderQualificationCertificationService;

    @PostMapping("/upload")
    public AjaxResult addIdCardFront(@RequestParam("frontFile") MultipartFile frontFile,@RequestParam("backFile") MultipartFile backFile,@RequestParam("idCardNumber") Long idCardNumber){
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        if (riderBaseId == null){
            return AjaxResult.error("请先登录");
        }
        int rows = riderQualificationCertificationService.upload(frontFile,backFile,riderBaseId,idCardNumber);
        return rows > 0 ? AjaxResult.success("上传成功") : AjaxResult.error("上传失败");
    }

}
