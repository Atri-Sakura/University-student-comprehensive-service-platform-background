package com.ruoyi.platform.rider.controller;

import com.ruoyi.common.utils.file.MinioFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rider/qualificationCertification")
public class RiderQualificationCertificationController {
    @Autowired
    private MinioFileUtils minioFileUtils;


}
