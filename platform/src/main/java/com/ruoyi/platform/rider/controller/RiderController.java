package com.ruoyi.platform.rider.controller;


import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.service.IRiderBaseService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.framework.config.FileStorageProperties;
import com.ruoyi.framework.storage.CloudStorageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/rider")
public class RiderController {
    @Autowired
    private IRiderBaseService riderBaseService;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Autowired(required = false)
    private CloudStorageService cloudStorageService;


   /**
     * 获取当前骑手登陆信息
     */
   @GetMapping("/info")
    public AjaxResult getRiderInfo(){
       Long userId = SecurityUtils.getUserId();

       //The principle is SafeCheck
       if(userId ==  null){
           return AjaxResult.error("未检测到登录用户，请先登录或启用");
       }

       RiderBase rider = riderBaseService.selectRiderBaseByRiderBaseId(userId);

       if(rider == null){
           return AjaxResult.error("未找到骑手信息，请检查数据库是否存在该用户ID");
       }
       return AjaxResult.success("查询成功",rider);
   }

   /**
     * 修改骑手个人信息
     */
   @PutMapping("/update")
    public AjaxResult updateRiderInfo(@RequestBody RiderBase riderBase){
       Long userId = SecurityUtils.getUserId();

       if(userId == null){
           return AjaxResult.error("未检测到登录用户，请先登录或启用");
       }

       riderBase.setRiderBaseId(userId);
       int rows = riderBaseService.updateRiderBaseBasicInfo(riderBase);
       if (rows > 0) {
           // 返回修改后的完整信息（包含身份证号）
           //有优化点，很多字段修改个人信息页其实不需要，有点内存影响，但是为了简单，这里返回完整信息，后续优化数据脱敏功能
           RiderBase updated = riderBaseService.selectRiderBaseByRiderBaseId(userId);
           return AjaxResult.success("修改成功", updated);
       }
       return AjaxResult.error("修改失败，未找到目标骑手或无修改内容");
   }
   /**
     * 更换骑手工作状态
    *
    * 可优化实现状态幂等性即网络波动的情况下骑手点击多次但只实现切换一次（常见实现思路就是锁）
     */
   @PutMapping("/status")
   //对吧 @RequestParams
   public AjaxResult updateWorkStatus(@RequestBody RiderBase riderBase){
       Long userId = SecurityUtils.getUserId();

       if (userId == null) {
           return AjaxResult.error("未检测到登录用户，请先登录或启用");
       }
       if (riderBase.getWorkStatus() == null ||
               (riderBase.getWorkStatus() < 0 || riderBase.getWorkStatus() > 2)) {
           return AjaxResult.error("非法的工作状态参数，应为 0(下线)/1(上线)/2(忙碌)");
       }

       // 设置当前骑手ID
       riderBase.setRiderBaseId(userId);

       int rows = riderBaseService.updateRiderWorkStatus(riderBase);
       if (rows == 0) {
           return AjaxResult.success("状态未变化，无需更新");
       }

       if (rows > 0) {
           RiderBase updated = riderBaseService.selectRiderBaseByRiderBaseId(userId);
           Map<String, Object> result = new HashMap<>();
           result.put("riderBaseId", updated.getRiderBaseId());
           result.put("workStatus", updated.getWorkStatus());
           return AjaxResult.success("状态修改成功", result);
       }

       return AjaxResult.error("状态修改失败，请重试");

   }

   /**
    * 骑手身份验证
    * 可增强点，
    * 可能需要增强身份证格式验证
    * 照片格式验证
    * 自动验证图像识别这一块 后续可以考虑 AI图像识别/OCR
    *
    *
    * 注意：
    * - 审核通过/拒绝接口由【平台端】实现（待开发 RiderAuditController）
    */
   @PostMapping("/auth")
   public AjaxResult uploadRiderAuthInfo(
           @RequestParam("realName") String realName,
           @RequestParam("idCard") String idCard,
           @RequestParam("frontImage")MultipartFile frontImage,
           @RequestParam("backImage")MultipartFile backImage){

       Long userId = SecurityUtils.getUserId();
       if (userId == null) {
           return AjaxResult.error("未检测到登录用户，请登录后再试");
       }
       // -------------------- 文件上传逻辑（本地 or 云端） --------------------

       String frontUrl;
       String backUrl;

       try{
           if (fileStorageProperties.getCloud().isEnabled() && cloudStorageService != null) {
               // ☁️ 云端优先上传
               frontUrl = cloudStorageService.upload(frontImage, "rider/idcard/front_" + userId + ".jpg");
               backUrl = cloudStorageService.upload(backImage, "rider/idcard/back_" + userId + ".jpg");
           } else {
               // 💾 本地存储逻辑
               String basePath = RuoYiConfig.getProfile() + "/rider/idcard/";
               File dir = new File(basePath);
               if (!dir.exists()) dir.mkdirs();

               String frontFileName = "front_" + userId + "_" + System.currentTimeMillis() + ".jpg";
               String backFileName = "back_" + userId + "_" + System.currentTimeMillis() + ".jpg";

               frontImage.transferTo(new File(dir, frontFileName));
               backImage.transferTo(new File(dir, backFileName));

               // RuoYi 的静态资源路径映射为 /profile/**
               frontUrl = "/profile/rider/idcard/" + frontFileName;
               backUrl = "/profile/rider/idcard/" + backFileName;
           }
       } catch (Exception e) {
           return AjaxResult.error("文件上传失败: " + e.getMessage());
       }
       // 组装认证信息
       RiderBase rider = new RiderBase();
       rider.setRiderBaseId(userId);
       rider.setRealName(realName);
       rider.setIdCard(idCard); // TODO: 后续加密存储
       rider.setIdCardFront(frontUrl);
       rider.setIdCardBack(backUrl);
       rider.setAuditStatus(0L); // 0 = 待审核

       int rows = riderBaseService.updateRiderAuthInfo(rider);
       return rows > 0 ? AjaxResult.success("身份信息上传成功，待审核")
               : AjaxResult.error("更新骑手认证信息失败");

   }

}
