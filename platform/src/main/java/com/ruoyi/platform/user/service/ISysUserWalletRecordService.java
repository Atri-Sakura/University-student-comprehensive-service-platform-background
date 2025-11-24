package com.ruoyi.platform.user.service;

import com.ruoyi.platform.domain.UserWallet;
import com.ruoyi.platform.user.vo.UserWalletRecordVO;

import java.util.List;

public interface ISysUserWalletRecordService {
    List<UserWalletRecordVO> getUserWalletRecord(Long userId);

    UserWallet getUserWalletBalance(Long userId);

    int addWallet(Long userId);

    int freezeWallet(Long userId);

    int unfreezeWallet(Long userId);

    int setPayPassword(Long userId, String oldPayPassword, String newPayPassword);
}
