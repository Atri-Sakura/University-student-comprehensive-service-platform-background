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
import com.ruoyi.platform.domain.UserBankCard;
import com.ruoyi.platform.service.IUserBankCardService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户银行卡绑定Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/user1/card")
public class UserBankCardController extends BaseController
{
    @Autowired
    private IUserBankCardService userBankCardService;

    /**
     * 查询用户银行卡绑定列表
     */
    @PreAuthorize("@ss.hasPermi('user1:card:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserBankCard userBankCard)
    {
        startPage();
        List<UserBankCard> list = userBankCardService.selectUserBankCardList(userBankCard);
        return getDataTable(list);
    }

    /**
     * 导出用户银行卡绑定列表
     */
    @PreAuthorize("@ss.hasPermi('user1:card:export')")
    @Log(title = "用户银行卡绑定", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserBankCard userBankCard)
    {
        List<UserBankCard> list = userBankCardService.selectUserBankCardList(userBankCard);
        ExcelUtil<UserBankCard> util = new ExcelUtil<UserBankCard>(UserBankCard.class);
        util.exportExcel(response, list, "用户银行卡绑定数据");
    }

    /**
     * 获取用户银行卡绑定详细信息
     */
    @PreAuthorize("@ss.hasPermi('user1:card:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userBankCardService.selectUserBankCardById(id));
    }

    /**
     * 新增用户银行卡绑定
     */
    @PreAuthorize("@ss.hasPermi('user1:card:add')")
    @Log(title = "用户银行卡绑定", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserBankCard userBankCard)
    {
        return toAjax(userBankCardService.insertUserBankCard(userBankCard));
    }

    /**
     * 修改用户银行卡绑定
     */
    @PreAuthorize("@ss.hasPermi('user1:card:edit')")
    @Log(title = "用户银行卡绑定", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserBankCard userBankCard)
    {
        return toAjax(userBankCardService.updateUserBankCard(userBankCard));
    }

    /**
     * 删除用户银行卡绑定
     */
    @PreAuthorize("@ss.hasPermi('user1:card:remove')")
    @Log(title = "用户银行卡绑定", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userBankCardService.deleteUserBankCardByIds(ids));
    }
}
