package com.ruoyi.platform.platform.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.platform.vo.RiderBaseVO;

public interface IPlatformQualificationReviewService {
    RiderBaseVO getAllRiderQualificationStatus();

    int setRiderQualificationStatus(Integer status, Integer riderId);
}
