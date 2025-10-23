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
import com.ruoyi.platform.domain.UserCreditScoreRecord;
import com.ruoyi.platform.service.IUserCreditScoreRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户信用分流水Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/user1/creditScoreRecord")
public class UserCreditScoreRecordController extends BaseController
{
    @Autowired
    private IUserCreditScoreRecordService userCreditScoreRecordService;

    /**
     * 查询用户信用分流水列表
     */
    @PreAuthorize("@ss.hasPermi('user1:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserCreditScoreRecord userCreditScoreRecord)
    {
        startPage();
        List<UserCreditScoreRecord> list = userCreditScoreRecordService.selectUserCreditScoreRecordList(userCreditScoreRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户信用分流水列表
     */
    @PreAuthorize("@ss.hasPermi('user1:record:export')")
    @Log(title = "用户信用分流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserCreditScoreRecord userCreditScoreRecord)
    {
        List<UserCreditScoreRecord> list = userCreditScoreRecordService.selectUserCreditScoreRecordList(userCreditScoreRecord);
        ExcelUtil<UserCreditScoreRecord> util = new ExcelUtil<UserCreditScoreRecord>(UserCreditScoreRecord.class);
        util.exportExcel(response, list, "用户信用分流水数据");
    }

    /**
     * 获取用户信用分流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('user1:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userCreditScoreRecordService.selectUserCreditScoreRecordById(id));
    }

    /**
     * 新增用户信用分流水
     */
    @PreAuthorize("@ss.hasPermi('user1:record:add')")
    @Log(title = "用户信用分流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserCreditScoreRecord userCreditScoreRecord)
    {
        return toAjax(userCreditScoreRecordService.insertUserCreditScoreRecord(userCreditScoreRecord));
    }

    /**
     * 修改用户信用分流水
     */
    @PreAuthorize("@ss.hasPermi('user1:record:edit')")
    @Log(title = "用户信用分流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserCreditScoreRecord userCreditScoreRecord)
    {
        return toAjax(userCreditScoreRecordService.updateUserCreditScoreRecord(userCreditScoreRecord));
    }

    /**
     * 删除用户信用分流水
     */
    @PreAuthorize("@ss.hasPermi('user1:record:remove')")
    @Log(title = "用户信用分流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userCreditScoreRecordService.deleteUserCreditScoreRecordByIds(ids));
    }
}
