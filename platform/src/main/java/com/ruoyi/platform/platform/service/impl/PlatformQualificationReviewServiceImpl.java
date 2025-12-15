package com.ruoyi.platform.platform.service.impl;

import com.ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.platform.mapper.PlatformQualificationReviewMapper;
import com.ruoyi.platform.platform.service.IPlatformQualificationReviewService;
import com.ruoyi.platform.platform.vo.RiderBaseVO;
import com.ruoyi.platform.utils.MaskUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformQualificationReviewServiceImpl implements IPlatformQualificationReviewService {
    @Autowired
    private PlatformQualificationReviewMapper platformQualificationReviewMapper;


    @Override
    public RiderBaseVO getAllRiderQualificationStatus() {
        RiderBase riderBase = platformQualificationReviewMapper.getAllRiderQualificationStatus();
        RiderBaseVO riderBaseVO = new RiderBaseVO();
        BeanUtils.copyProperties(riderBase,riderBaseVO);
        riderBaseVO.setIdCard(MaskUtils.maskIdCard(riderBaseVO.getIdCard()));
        riderBaseVO.setPhone(MaskUtils.maskPhone(riderBaseVO.getPhone()));
        return riderBaseVO;
    }

    @Override
    public int setRiderQualificationStatus(Integer status, Integer riderId) {
        return platformQualificationReviewMapper.setRiderQualificationStatus(status,riderId);
    }

    public int setMerchantQualificationStatus(Integer status, Long merchantId) {
        return platformQualificationReviewMapper.setMerchantQualificationStatus(status,merchantId);
    }
}
