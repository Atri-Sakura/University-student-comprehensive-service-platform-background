package com.ruoyi.platform.platform.service.impl;

import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.platform.mapper.PlatformQualificationReviewMapper;
import com.ruoyi.platform.platform.service.IPlatformQualificationReviewService;
import com.ruoyi.platform.platform.vo.MerchantBaseVO;
import com.ruoyi.platform.platform.vo.RiderBaseVO;
import com.ruoyi.platform.utils.MaskUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlatformQualificationReviewServiceImpl implements IPlatformQualificationReviewService {
    @Autowired
    private PlatformQualificationReviewMapper platformQualificationReviewMapper;


    @Override
    public List<RiderBaseVO> getAllRiderQualificationStatus() {
        List<RiderBase> riderBase = platformQualificationReviewMapper.getAllRiderQualificationStatus();
        List<RiderBaseVO> riderBaseVOList = new ArrayList<>();
        for (RiderBase riderBase1 : riderBase) {
            RiderBaseVO riderBaseVO = new RiderBaseVO();
            BeanUtils.copyProperties(riderBase1,riderBaseVO);
            riderBaseVOList.add(riderBaseVO);
        }

        return riderBaseVOList;
    }

    @Override
    public int setRiderQualificationStatus(Integer status, Integer riderId) {
        return platformQualificationReviewMapper.setRiderQualificationStatus(status,riderId);
    }

    public int setMerchantQualificationStatus(Integer status, Long merchantId) {
        return platformQualificationReviewMapper.setMerchantQualificationStatus(status,merchantId);
    }

    @Override
    public List<MerchantBaseVO> getAllMerchantQualificationStatus() {
        List<MerchantBase> merchantBase = platformQualificationReviewMapper.getAllMerchantQualificationStatus();
        List<MerchantBaseVO> merchantBaseVOList = new ArrayList<>();
        for (MerchantBase merchantBase1 : merchantBase) {
            MerchantBaseVO merchantBaseVO = new MerchantBaseVO();
            BeanUtils.copyProperties(merchantBase1,merchantBaseVO);
            merchantBaseVOList.add(merchantBaseVO);
        }
        return merchantBaseVOList;
    }
}
