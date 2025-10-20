package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.merchant.dto.*;
import com.ruoyi.platform.merchant.service.MerchantPortalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商家门户服务测试
 *
 * @date 2025-10-20
 */
@SpringBootTest
@ComponentScan("com.ruoyi.platform")
class MerchantPortalServiceTest {

    @Autowired
    private MerchantPortalService merchantPortalService;

    private static final Long TEST_MERCHANT_ID = 1L;

    @Test
    void testGetMerchantInfo() {
        MerchantInfoDTO info = merchantPortalService.getMerchantInfo(TEST_MERCHANT_ID);
        assertNotNull(info);
        assertEquals(TEST_MERCHANT_ID, info.getMerchantBaseId());
        assertNotNull(info.getMerchantName());
    }

    @Test
    void testUpdateBusinessStatus() {
        boolean result = merchantPortalService.updateBusinessStatus(TEST_MERCHANT_ID, 1L);
        assertTrue(result);

        // 验证更新结果
        MerchantInfoDTO info = merchantPortalService.getMerchantInfo(TEST_MERCHANT_ID);
        assertEquals(1L, info.getBusinessStatus());
    }

    @Test
    void testCreateGoods() {
        GoodsCreateRequest request = new GoodsCreateRequest();
        request.setGoodsName("测试商品");
        request.setCategory("美食");
        request.setSubCategory("快餐");
        request.setPrice(new BigDecimal("15.00"));
        request.setOriginalPrice(new BigDecimal("20.00"));
        request.setStock(100L);
        request.setDescription("这是一个测试商品");
        request.setTags(Arrays.asList("FOOD_SPICY", "FAST_FOOD"));
        request.setImageUrls(Arrays.asList("http://example.com/image1.jpg", "http://example.com/image2.jpg"));
        request.setMainImageIndex(0);

        Long goodsId = merchantPortalService.createGoods(TEST_MERCHANT_ID, request);
        assertNotNull(goodsId);
        assertTrue(goodsId > 0);

        // 验证创建结果
        MerchantGoodsDTO goods = merchantPortalService.getGoodsDetail(TEST_MERCHANT_ID, goodsId);
        assertNotNull(goods);
        assertEquals("测试商品", goods.getGoodsName());
        assertEquals(2, goods.getImages().size());
    }

    @Test
    void testListGoods() {
        List<MerchantGoodsDTO> list = merchantPortalService.listGoods(
                TEST_MERCHANT_ID, null, null, 1, 10);
        assertNotNull(list);
    }

    @Test
    void testUpdateGoodsStatus() {
        // 假设商品ID为1
        Long goodsId = 1L;
        boolean result = merchantPortalService.updateGoodsStatus(TEST_MERCHANT_ID, goodsId, 1L);
        assertTrue(result);
    }

    @Test
    void testReplyMerchantEvaluation() {
        // 假设评价ID为1
        Long evaluationId = 1L;
        String reply = "感谢您的评价,我们会继续努力!";
        boolean result = merchantPortalService.replyMerchantEvaluation(
                TEST_MERCHANT_ID, evaluationId, reply);
        assertTrue(result);
    }

    @Test
    void testCreateActivity() {
        ActivityCreateRequest request = new ActivityCreateRequest();
        request.setActivityName("新春大促");
        request.setActivityType("DISCOUNT");
        request.setStartTime(new Date());
        request.setEndTime(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L)); // 7天后
        request.setContent("全场8折优惠");

        Long activityId = merchantPortalService.createActivity(TEST_MERCHANT_ID, request);
        assertNotNull(activityId);
        assertTrue(activityId > 0);
    }
}