package com.ruoyi.platform.user.service.impl;

import com.ruoyi.platform.domain.UserWallet;
import com.ruoyi.platform.user.mapper.SysUserWalletRecordMapper;
import com.ruoyi.platform.user.service.ISysUserWalletRecordService;
import com.ruoyi.platform.user.vo.UserWalletRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserWalletRecordService implements ISysUserWalletRecordService {
    @Autowired
    private SysUserWalletRecordMapper userWalletRecordMapper;

    @Override
    public List<UserWalletRecordVO> getUserWalletRecord(Long userId) {
        return userWalletRecordMapper.getUserWalletRecordByUserId(userId);
    }

    @Override
    public UserWallet getUserWalletBalance(Long userId) {
        return userWalletRecordMapper.getUserWalletBalanceByUserId(userId);
    }
}
