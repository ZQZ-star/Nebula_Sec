package com.nebula.controller;

import com.nebula.annotation.RecordAudit;
import com.nebula.domain.RestBean;
import com.nebula.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // JDK21 Record 做参数接收
    public record LoginReq(String username, String password) {}

    @PostMapping("/login")
    @RecordAudit("用户登录操作")
    public RestBean<String> login(@RequestBody LoginReq req) {
        String token = authService.login(req.username(), req.password());
        return RestBean.success(token);
    }

    @PostMapping("/logout")
    public RestBean<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            authService.logout(token);
        }
        return RestBean.success(null);
    }
}
