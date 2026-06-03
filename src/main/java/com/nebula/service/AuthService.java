package com.nebula.service;

import com.nebula.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final StringRedisTemplate redisTemplate;

    public String login(String username, String password) {
        // 此处省略密码校验逻辑 (用 passwordEncoder.matches)
        return jwtUtils.generateToken(username);
    }

    public void logout(String token) {
        String jwt = token.replace("Bearer ", "");
        String jti = jwtUtils.extractJti(jwt);
        long remainTime = jwtUtils.extractExpiration(jwt).getTime() - System.currentTimeMillis();

        if (remainTime > 0) {
            // 将 Token 唯一标识加入 Redis 黑名单，过期时间自动销毁
            redisTemplate.opsForValue().set("jwt:blacklist:" + jti, "revoked", remainTime, TimeUnit.MILLISECONDS);
        }
    }
}
