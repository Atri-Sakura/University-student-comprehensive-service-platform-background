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

    @Override
    public int addWallet(Long userId,Long userWalletId) {
        return userWalletRecordMapper.addWallet(userId,userWalletId);
    }

    @Override
    public int freezeWallet(Long userId) {
        return userWalletRecordMapper.freezeWallet(userId);
    }

    @Override
    public int unfreezeWallet(Long userId) {
        return userWalletRecordMapper.unfreezeWallet(userId);
    }

    @Override
    public int setPayPassword(Long userId, String oldPayPassword, String newPayPassword) {
        String oldPassword = userWalletRecordMapper.getPayPasswordByUserId(userId);
        if (oldPassword == null || !oldPassword.equals(oldPayPassword)) {
            throw new RuntimeException("旧密码错误");
        }
        return userWalletRecordMapper.setPayPassword(userId, newPayPassword);
    }
}
