package com.ruoyi.framework.security.filter;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 🧪 FakeAuthFilter：开发阶段模拟登录用户
 */
@Component
@Profile("dev")
public class FakeAuthFilter extends OncePerRequestFilter{

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse reponse, FilterChain chain)
            throws ServletException, IOException {

        //仅当用户未登陆时注入用户
        if(SecurityContextHolder.getContext().getAuthentication() == null){

            // 模拟一个系统用户对象（SysUser）
            SysUser sysUser = new SysUser();
            sysUser.setUserId(1L);           // 数据库中存在的用户ID
            sysUser.setUserName("rider001"); // 登录用户名
            sysUser.setPhonenumber("13800000001");
            //后续将LoginUser的信息改为数据库中的目标用户数据即可通过模拟登陆
            LoginUser fakeUser = new LoginUser();
            fakeUser.setUser(sysUser);
            fakeUser.setUserId(sysUser.getUserId());
            fakeUser.setMerchantBaseId(sysUser.getUserId());
            fakeUser.setUserBaseId(2L);
            fakeUser.setRiderBaseId(3318474181L);
//            fakeUser.setUsername("dev_user");
//            fakeUser.setToken("fake-token");
//            fakeUser.setDeptName("测试部");

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(fakeUser, null, null);

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("✅ FakeAuthFilter 已注入模拟用户: userId=1L");
        }

        chain.doFilter(request, reponse);
    }
}
