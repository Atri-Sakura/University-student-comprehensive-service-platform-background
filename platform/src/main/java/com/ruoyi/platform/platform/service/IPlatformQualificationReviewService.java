package com.ruoyi.platform.platform.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.platform.vo.MerchantBaseVO;
import com.ruoyi.platform.platform.vo.RiderBaseVO;

import java.util.List;

public interface IPlatformQualificationReviewService {
    List<RiderBaseVO> getAllRiderQualificationStatus();

    int setRiderQualificationStatus(Integer status, Integer riderId);

    /**
     * 设置商家凭证状态
     * @param status
     * @param merchantId
     * @return
     */
    int setMerchantQualificationStatus(Integer status, Long merchantId);

    List<MerchantBaseVO> getAllMerchantQualificationStatus();
}
