package com.ruoyi.platform.controller.merchant.controller1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 商家信息Controller测试
 *
 * @author Moli2580
 * @date 2025-10-20
 */
@SpringBootTest
@AutoConfigureMockMvc
class MerchantInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String MERCHANT_ID_HEADER = "X-Merchant-Id";
    private static final String TEST_MERCHANT_ID = "1";

    @Test
    void testGetInfo() throws Exception {
        mockMvc.perform(get("/merchant/info")
                        .header(MERCHANT_ID_HEADER, TEST_MERCHANT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testUpdateBusinessStatus() throws Exception {
        mockMvc.perform(put("/merchant/info/business-status")
                        .header(MERCHANT_ID_HEADER, TEST_MERCHANT_ID)
                        .param("businessStatus", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testUpdateInfoWithoutAuth() throws Exception {
        mockMvc.perform(get("/merchant/info"))
                .andExpect(status().isOk())  // 改为期望 200
                .andExpect(jsonPath("$.code").value(500))  // 检查业务错误码
                .andExpect(jsonPath("$.msg").value("未登录或登录已过期"));
    }
}