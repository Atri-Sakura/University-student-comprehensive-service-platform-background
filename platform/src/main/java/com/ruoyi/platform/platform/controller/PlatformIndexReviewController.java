package com.ruoyi.platform.platform.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.domain.IndexImgUrl;
import com.ruoyi.platform.platform.service.IPlatformIndexReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/platform/index/review")
public class PlatformIndexReviewController {
    @Autowired
    private IPlatformIndexReviewService platformIndexReviewService;

    @GetMapping
    public AjaxResult getUserIndexImgs(){
        List<IndexImgUrl> indexImgUrls = platformIndexReviewService.getUserIndexImgs();
        return AjaxResult.success("查询成功",indexImgUrls);
    }

    @DeleteMapping
    public AjaxResult deleteUserIndexImgs(@RequestBody IndexImgUrl indexImgUrl){
        int result = platformIndexReviewService.deleteUserIndexImgs(indexImgUrl);
        return result > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    @PostMapping
    public AjaxResult addIndexImgUrl(@RequestBody IndexImgUrl indexImgUrl){
        int result = platformIndexReviewService.addIndexImgUrl(indexImgUrl);
        return result > 0 ? AjaxResult.success("添加成功") : AjaxResult.error("添加失败");
    }

}
