package com.nebula.controller;

import com.nebula.annotation.RecordAudit;
import com.nebula.domain.RestBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    @RecordAudit("访问了受保护的测试接口") // 触发我们写的AOP异步日志
    public RestBean<String> hello() {
        // 获取当前登录的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return RestBean.success("Hello, " + username + "! 你的Token校验通过！");
    }
}