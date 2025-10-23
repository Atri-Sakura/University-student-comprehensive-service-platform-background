package com.ruoyi.platform.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.platform.domain.UserWallet;
import com.ruoyi.platform.service.IUserWalletService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户钱包Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/user1/wallet")
public class UserWalletController extends BaseController
{
    @Autowired
    private IUserWalletService userWalletService;

    /**
     * 查询用户钱包列表
     */
    @PreAuthorize("@ss.hasPermi('user1:wallet:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserWallet userWallet)
    {
        startPage();
        List<UserWallet> list = userWalletService.selectUserWalletList(userWallet);
        return getDataTable(list);
    }

    /**
     * 导出用户钱包列表
     */
    @PreAuthorize("@ss.hasPermi('user1:wallet:export')")
    @Log(title = "用户钱包", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserWallet userWallet)
    {
        List<UserWallet> list = userWalletService.selectUserWalletList(userWallet);
        ExcelUtil<UserWallet> util = new ExcelUtil<UserWallet>(UserWallet.class);
        util.exportExcel(response, list, "用户钱包数据");
    }

    /**
     * 获取用户钱包详细信息
     */
    @PreAuthorize("@ss.hasPermi('user1:wallet:query')")
    @GetMapping(value = "/{userWalletId}")
    public AjaxResult getInfo(@PathVariable("userWalletId") Long userWalletId)
    {
        return success(userWalletService.selectUserWalletByUserWalletId(userWalletId));
    }

    /**
     * 新增用户钱包
     */
    @PreAuthorize("@ss.hasPermi('user1:wallet:add')")
    @Log(title = "用户钱包", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserWallet userWallet)
    {
        return toAjax(userWalletService.insertUserWallet(userWallet));
    }

    /**
     * 修改用户钱包
     */
    @PreAuthorize("@ss.hasPermi('user1:wallet:edit')")
    @Log(title = "用户钱包", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserWallet userWallet)
    {
        return toAjax(userWalletService.updateUserWallet(userWallet));
    }

    /**
     * 删除用户钱包
     */
    @PreAuthorize("@ss.hasPermi('user1:wallet:remove')")
    @Log(title = "用户钱包", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userWalletIds}")
    public AjaxResult remove(@PathVariable Long[] userWalletIds)
    {
        return toAjax(userWalletService.deleteUserWalletByUserWalletIds(userWalletIds));
    }
}
