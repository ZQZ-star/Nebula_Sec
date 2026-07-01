package com.nebula.service;

import com.nebula.domain.User;
import com.nebula.repository.UserRepository;
import com.nebula.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String login(String username, String password) {
        // 使用 AuthenticationManager 进行完整的认证流程
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return jwtUtils.generateToken(username);
    }

    public User register(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (email != null && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setStatus(true);
        user.setRole("USER");
        return userRepository.save(user);
    }

    public void logout(String token) {
        String jwt = token.replace("Bearer ", "");
        try {
            String jti = jwtUtils.extractJti(jwt);
            long remainTime = jwtUtils.extractExpiration(jwt).getTime() - System.currentTimeMillis();

            if (remainTime > 0) {
                redisTemplate.opsForValue().set(
                        "jwt:blacklist:" + jti, "revoked", remainTime, TimeUnit.MILLISECONDS
                );
            }
        } catch (Exception ignored) {
            // Token 已过期或无效，无需处理
        }
    }
}
