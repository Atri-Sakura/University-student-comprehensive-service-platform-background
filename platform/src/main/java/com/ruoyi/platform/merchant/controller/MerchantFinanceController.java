package com.ruoyi.platform.merchant.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.domain.MerchantDailySummary;
import com.ruoyi.platform.domain.MerchantWallet;
import com.ruoyi.platform.domain.MerchantWithdrawAccount;
import com.ruoyi.platform.domain.MerchantWithdrawRecord;
import com.ruoyi.platform.domain.dto.MerchantWithdrawAccountAddDTO;
import com.ruoyi.platform.domain.dto.WithdrawApplyDTO;
import com.ruoyi.platform.domain.vo.*;
import com.ruoyi.platform.merchant.payment.PaymentGatewayClient;
import com.ruoyi.platform.merchant.service.IMerchantWithdrawAccountService;
import com.ruoyi.platform.merchant.service.IMerchantWithdrawRecordService;
import com.ruoyi.platform.service.IMerchantWalletService;
import com.ruoyi.platform.service.IOrderMainService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * 商家财务信息控制器
 */

@RestController
@RequestMapping("/api/merchant/finance")
public class MerchantFinanceController extends BaseController {

    @Autowired
    private IOrderMainService orderMainService;

    @Autowired
    private IMerchantWalletService merchantWalletService;

    @Autowired
    private IMerchantWithdrawRecordService merchantWithdrawRecordService;

    @Autowired
    private IMerchantWithdrawAccountService merchantWithdrawAccountService;
    /**
     * 查询商家今日收入统计
     */
    @GetMapping("/income/today")
    public AjaxResult getTodayIncome() {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null) {
            return AjaxResult.error("商家身份信息缺失，请重新登录");
        }
        //今日起止时间
        LocalDate today = LocalDate.now();
        Date startTime = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(today.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        //查询今日收入与订单统计
        Map<String, Object> incomeMap = orderMainService.selectMerchantTodayIncome(merchantId, startTime, endTime);
        BigDecimal totalIncome = (BigDecimal) incomeMap.get("total_income");
        Integer orderCount = ((Number) incomeMap.get("order_count")).intValue();

        //查询今日退款订单
        BigDecimal refundAmount = orderMainService.selectMerchantTodayRefund(merchantId, startTime, endTime);
        if (refundAmount == null) refundAmount = BigDecimal.ZERO;

        // 净收入 = 收入 - 退款
        BigDecimal netIncome = totalIncome.subtract(refundAmount);

        // 封装VO
        MerchantIncomeTodayVO vo = new MerchantIncomeTodayVO();
        vo.setTotalIncome(totalIncome);
        vo.setOrderCount(orderCount);
        vo.setRefundAmount(refundAmount);
        vo.setNetIncome(netIncome);


        return AjaxResult.success(vo);
    }

    /**
     * 查询商家钱包信息
     */
    @GetMapping("/wallet")
    public AjaxResult getMerchantWallet() {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null) {
            return AjaxResult.error("商家身份信息缺失，请重新登录");
        }
        MerchantWallet wallet = merchantWalletService.getWalletByMerchantId(merchantId);
        if (wallet == null) {
            return AjaxResult.error("商家钱包信息不存在");
        }
        MerchantWalletVO walletVO = new MerchantWalletVO();
        walletVO.setAvailableBalance(wallet.getBalance());
        walletVO.setFreezeAmount(wallet.getFreezeAmount());
        walletVO.setUpdateTime(wallet.getUpdateTime());


        return AjaxResult.success(walletVO);
    }

    /**
     * 查询提现页顶部信息
     */
    @GetMapping("/withdraw/overview")
    public AjaxResult getWithdrawOverview() {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null) {
            return AjaxResult.error("商家身份信息缺失，请重新登录");
        }
        MerchantWithdrawOverviewVO overviewVO = merchantWithdrawRecordService.getWithdrawOverview(merchantId);
        if (overviewVO == null) {
            return AjaxResult.error("商家钱包信息未找到");
        }
        return AjaxResult.success(overviewVO);
    }

    /**
     * 提现预估结果
     */
    @PostMapping("/withdraw/preview")
    public AjaxResult previewWithdraw(@RequestParam("amount") BigDecimal amount) {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null) {
            return AjaxResult.error("商家身份信息缺失，请重新登录");
        }

        // 基础参数校验
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.error("提现金额必须大于0");
        }

        // 固定费率配置（后期可改为配置表）
        BigDecimal feeRate = new BigDecimal("0.005");
        BigDecimal fee = amount.multiply(feeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal actualAmount = amount.subtract(fee).setScale(2, BigDecimal.ROUND_HALF_UP);

        MerchantWithdrawPreviewVO vo = new MerchantWithdrawPreviewVO();
        vo.setAmount(amount);
        vo.setFee(fee);
        vo.setActualAmount(actualAmount);
        return AjaxResult.success(vo);

    }

    /**
     * 添加商家提现账户需要增强逻辑避免重复添加相同的
     */
    @PostMapping("/withdraw/account/add")
    public AjaxResult addWithdrawAccount(@RequestBody MerchantWithdrawAccountAddDTO dto) {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null) {
            return AjaxResult.error("商家身份信息缺失，请重新登录");
        }

        if (dto.getAccountType() == null || dto.getAccountName() == null || dto.getAccountNumber() == null) {
            return AjaxResult.error("账户类型、户名、账号不能为空");
        }

        if ("bank".equalsIgnoreCase(dto.getAccountType())
                && (dto.getBankName() == null || dto.getBankName().trim().isEmpty())) {
            return AjaxResult.error("银行卡账户必须填写银行名称");
        }

        int rows = merchantWithdrawAccountService.addWithdrawAccount(merchantId, dto);
        return rows > 0 ? AjaxResult.success("账户添加成功") : AjaxResult.error("账户添加失败");
    }

    /**
     * 获取商家提现账户列表
     */
    @GetMapping("/withdraw/account/list")
    public AjaxResult getWithdrawAccountList() {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null) {
            return AjaxResult.error("商家身份信息缺失，请重新登录");
        }

        List<MerchantWithdrawAccountVO> list = merchantWithdrawAccountService.getWithdrawAccountList(merchantId);
        return AjaxResult.success(list);

    }

    /**
     * 删除商家提现账户
     */
    @DeleteMapping("/withdraw/account/{accountId}")
    public AjaxResult deleteWithdrawAccount(@PathVariable Long accountId) {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null) {
            return AjaxResult.error("商家身份信息缺失，请重新登录");
        }

        try {
            int rows = merchantWithdrawAccountService.deleteWithdrawAccount(merchantId, accountId);
            return rows > 0 ? AjaxResult.success("账户删除成功") : AjaxResult.error("账户删除失败");
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 设置提现账户为默认账户
     */
    @PutMapping("/withdraw/account/{accountId}/default")
    public AjaxResult setDefaultWithdrawAccount(@PathVariable Long accountId) {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null) {
            return AjaxResult.error("商家身份信息缺失，请重新登录");
        }

        try {
            int rows = merchantWithdrawAccountService.setDefaultWithdrawAccount(merchantId, accountId);
            return rows > 0 ? AjaxResult.success("已设为默认账户") : AjaxResult.error("设置默认账户失败");
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 提现申请
     */
    @PostMapping("/withdraw/apply")
    public AjaxResult applyWithdraw(@RequestBody WithdrawApplyDTO withdrawApplyDTO) {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null) {
            return AjaxResult.error("商家身份信息缺失，请重新登录");
        }
        try {
            WithdrawApplyResultVO vo = merchantWithdrawRecordService.applyWithdraw(merchantId, withdrawApplyDTO);
            return AjaxResult.success(vo);
        } catch (ServiceException e) {
            return AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            return AjaxResult.error("提现申请失败，请稍后重试");
        }
    }

    /**
     * 查询提现状态
     */
    @GetMapping("/withdraw/status/{withdrawId}")
    public AjaxResult getWithdrawStatus(@PathVariable Long withdrawId) {
        MerchantWithdrawRecord record = merchantWithdrawRecordService.selectWithdrawRecordById(withdrawId);
        if (record == null) {
            return AjaxResult.error("未找到提现记录");
        }

        WithdrawStatusVO vo = new WithdrawStatusVO();
        vo.setWithdrawId(record.getWithdrawId());
        vo.setWithdrawStatus(record.getWithdrawStatus());
        vo.setRemark(record.getRemark());
        vo.setProcessTime(record.getProcessTime());
        return AjaxResult.success(vo);
    }

    /**
     * 获取商家提现记录列表
     */
    @GetMapping("/withdraw/records")
    public TableDataInfo getwithdrawRecordList(@RequestParam (required = false) String status) {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null) {
            throw new ServiceException("商家身份信息缺失，请重新登录");
        }
        startPage();
        List<MerchantWithdrawRecordVO> list = merchantWithdrawRecordService.selectWithdrawRecordVOList(merchantId, status);
        return getDataTable(list);
    }
}

