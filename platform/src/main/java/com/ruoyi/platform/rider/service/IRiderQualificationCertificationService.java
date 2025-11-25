package com.ruoyi.platform.rider.service;

import org.springframework.web.multipart.MultipartFile;

public interface IRiderQualificationCertificationService {

    int upload(MultipartFile frontFile, MultipartFile backFile,Long riderBaseId,Long idCardNumber);
}
