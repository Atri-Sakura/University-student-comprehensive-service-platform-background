package com.gzu.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzu.common.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
@Component
public class MyBlockExceptionHandler implements BlockExceptionHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s, BlockException e) throws Exception {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(429);//too many requests

        PrintWriter out = httpServletResponse.getWriter();

        R<T> errorR = R.error(500,s+"被Sentinel限制"+e.getMessage());
        String json = objectMapper.writeValueAsString(errorR);
//        System.out.println(json);
        out.write(json);

        out.flush();
        out.close();
    }
}
