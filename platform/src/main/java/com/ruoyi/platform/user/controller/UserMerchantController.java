package com.ruoyi.platform.user.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.domain.vo.MerchantDetailVO;
import com.ruoyi.platform.domain.vo.MerchantListVO;
import com.ruoyi.platform.service.IMerchantBaseService;

/**
 * 用户端商家查询Controller (精简版)
 */
@RestController
@RequestMapping("/user/merchant")
public class UserMerchantController extends BaseController {

    @Autowired
    private IMerchantBaseService merchantBaseService;

    /**
     * 用户端:查询营业中的商家列表(分页,精简字段)
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String businessScope) {

        MerchantBase query = new MerchantBase();
        query.setAuditStatus(1L);
        query.setBusinessStatus(1L);
        query.setMerchantName(merchantName);
        query.setBusinessScope(businessScope);

        List<MerchantBase> list = merchantBaseService.selectMerchantBaseList(query);

        // 转换为VO对象,只返回需要的字段
        List<MerchantListVO> voList = list.stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return getDataTable(voList);
    }

    /**
     * 用户端:获取商家详细信息
     */
    @GetMapping("/{merchantBaseId}")
    public AjaxResult getInfo(@PathVariable("merchantBaseId") Long merchantBaseId) {

        MerchantBase merchant = merchantBaseService.selectMerchantBaseByMerchantBaseId(merchantBaseId);

        if (merchant == null) {
            return AjaxResult.error("商家不存在");
        }

        if (merchant.getAuditStatus() == null || merchant.getAuditStatus() != 1L) {
            return AjaxResult.error("该商家未通过审核");
        }

        if (merchant.getBusinessStatus() == null || merchant.getBusinessStatus() != 1L) {
            return AjaxResult.error("该商家暂停营业");
        }

        // 转换为详情VO
        MerchantDetailVO vo = convertToDetailVO(merchant);

        return success(vo);
    }

    /**
     * 用户端:搜索商家
     */
    @GetMapping("/search")
    public TableDataInfo search(@RequestParam("keyword") String keyword) {

        MerchantBase query = new MerchantBase();
        query.setAuditStatus(1L);
        query.setBusinessStatus(1L);
        query.setMerchantName(keyword);

        List<MerchantBase> list = merchantBaseService.selectMerchantBaseList(query);

        List<MerchantListVO> voList = list.stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return getDataTable(voList);
    }

    /**
     * 用户端:按经营范围筛选商家
     */
    @GetMapping("/scope/{businessScope}")
    public TableDataInfo listByScope(@PathVariable("businessScope") String businessScope) {

        MerchantBase query = new MerchantBase();
        query.setAuditStatus(1L);
        query.setBusinessStatus(1L);
        query.setBusinessScope(businessScope);

        List<MerchantBase> list = merchantBaseService.selectMerchantBaseList(query);

        List<MerchantListVO> voList = list.stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return getDataTable(voList);
    }

    /**
     * 用户端:热门商家
     */
    @GetMapping("/hot")
    public TableDataInfo listHotMerchants() {

        MerchantBase query = new MerchantBase();
        query.setAuditStatus(1L);
        query.setBusinessStatus(1L);

        List<MerchantBase> list = merchantBaseService.selectMerchantBaseList(query);

        List<MerchantListVO> voList = list.stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return getDataTable(voList);
    }

    /**
     * 转换为列表VO(精简字段)
     */
    private MerchantListVO convertToListVO(MerchantBase merchant) {
        MerchantListVO vo = new MerchantListVO();
        BeanUtils.copyProperties(merchant, vo);
        return vo;
    }

    /**
     * 转换为详情VO(稍微详细)
     */
    private MerchantDetailVO convertToDetailVO(MerchantBase merchant) {
        MerchantDetailVO vo = new MerchantDetailVO();
        BeanUtils.copyProperties(merchant, vo);
        return vo;
    }
}