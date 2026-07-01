package com.nebula.controller;

import com.nebula.annotation.RecordAudit;
import com.nebula.domain.RestBean;
import com.nebula.domain.User;
import com.nebula.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public record LoginReq(
            @NotBlank(message = "用户名不能为空") String username,
            @NotBlank(message = "密码不能为空") String password
    ) {}

    public record RegisterReq(
            @NotBlank(message = "用户名不能为空") String username,
            @NotBlank(message = "密码不能为空") String password,
            @Email(message = "邮箱格式不正确") String email
    ) {}

    @PostMapping("/login")
    @RecordAudit("用户登录操作")
    public RestBean<String> login(@Valid @RequestBody LoginReq req) {
        String token = authService.login(req.username(), req.password());
        return RestBean.success(token);
    }

    @PostMapping("/register")
    @RecordAudit("用户注册操作")
    public RestBean<User> register(@Valid @RequestBody RegisterReq req) {
        User user = authService.register(req.username(), req.password(), req.email());
        return RestBean.success("注册成功", user);
    }

    @PostMapping("/logout")
    @RecordAudit("用户注销操作")
    public RestBean<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            authService.logout(token);
        }
        return RestBean.success(null);
    }
}
