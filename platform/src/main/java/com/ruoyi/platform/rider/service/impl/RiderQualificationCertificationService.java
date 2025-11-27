package com.ruoyi.platform.rider.service.impl;

import com.ruoyi.common.utils.file.MinioFileUtils;
import com.ruoyi.platform.rider.mapper.RiderQualificationCertificationMapper;
import com.ruoyi.platform.rider.service.IRiderQualificationCertificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class RiderQualificationCertificationService implements IRiderQualificationCertificationService {
    @Autowired
    private MinioFileUtils minioFileUtils;

    @Autowired
    private RiderQualificationCertificationMapper riderQualificationCertificationMapper;

    @Override
    public int upload(MultipartFile frontFile, MultipartFile backFile,Long riderBaseId,Long idCardNumber) {
        String imgUrl = null;
        String imgUrl1 = null;
        try {
            // 1. 先上传图片到MinIO
            imgUrl = minioFileUtils.upload(frontFile, "riderqualificationcertification", riderBaseId);
            imgUrl1 = minioFileUtils.upload(backFile, "riderqualificationcertification", riderBaseId);

            int affectedRows = 0;
            // 2. 将图片URL保存到数据库
            affectedRows = riderQualificationCertificationMapper.addIdCardImage(imgUrl,imgUrl1,idCardNumber, riderBaseId);




            if (affectedRows == 0) {
                // 数据库插入失败，删除已上传的图片
                minioFileUtils.deleteByUrl(imgUrl);
                minioFileUtils.deleteByUrl(imgUrl1);
                return 0;
            }

            return 1;
        } catch (Exception e) {
            // 发生异常，清理已上传的图片
            if (imgUrl != null) {
                try {
                    minioFileUtils.deleteByUrl(imgUrl);
                } catch (Exception deleteException) {
                    log.error("清理图片失败: {}", imgUrl, deleteException);
                }
            }
            log.error("添加图片失败", e);
            return 0;
        }
    }
}
