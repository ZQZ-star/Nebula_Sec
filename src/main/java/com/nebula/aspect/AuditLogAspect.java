package com.nebula.aspect;

import com.nebula.domain.AuditLog;
import com.nebula.mapper.AuditLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogMapper auditLogMapper;

    @Around("@annotation(com.nebula.annotation.RecordAudit)")
    public Object record(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long time = System.currentTimeMillis() - start;

        String username = "anonymous";
        if(SecurityContextHolder.getContext().getAuthentication() != null){
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        // 调用异步方法写入日志 (在JDK21下由轻量级虚拟线程执行，不阻塞主业务)
        saveLogAsync(username, joinPoint.getSignature().getName(), time);
        return result;
    }

    @Async
    public void saveLogAsync(String username, String method, long time) {
        AuditLog logEntity = new AuditLog();
        logEntity.setUsername(username);
        logEntity.setMethod(method);
        logEntity.setExecutionTimeMs(time);
        logEntity.setCreateTime(LocalDateTime.now());
        auditLogMapper.insert(logEntity);
        log.info("审计日志已异步入库: {}", method);
    }
}