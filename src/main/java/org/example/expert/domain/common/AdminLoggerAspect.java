package org.example.expert.domain.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.expert.domain.common.consts.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminLoggerAspect {
    private final Logger logger = LoggerFactory.getLogger(AdminLoggerAspect.class);
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;
    private final HttpServletResponse response;

    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))")
    private void deleteComment() {}

    @Pointcut("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    private void changeUserRole() {}

    @Around("deleteComment() || changeUserRole()")
    public Object aroundAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        // 요청 사용자 ID, 요청 시각, 요청 URL, 요청 본문, 응답 본문
        Long userId = (Long) request.getAttribute(Const.USERID);
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Const.DATETIME_FORMAT));
        String requestUrl = request.getRequestURI();
        String requestMethod = request.getMethod();
        String requestBody = extractRequestBody(joinPoint);

        logger.info("[Admin Request] "+joinPoint.getSignature().getName());
        logger.info("[Admin Request User] "+userId);
        logger.info("[Admin Request Time] "+now);
        logger.info("[Admin Request URL] "+ requestMethod + " " + requestUrl);
        logger.info("[Admin Request Body] "+ requestBody);
        
        Object result = joinPoint.proceed();

        String responseBody = result != null ? result.toString() : "[void]";
        String status = response != null ? String.valueOf(response.getStatus()) : "No status";
        logger.info("[Admin Response] status={}, body={}", status, responseBody);

        return result;
    }

    private String extractRequestBody(ProceedingJoinPoint joinPoint) {
        try{
            Object[] args = joinPoint.getArgs();
            MethodSignature sig = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = sig.getParameterNames();

            Map<String, Object> body = new LinkedHashMap<>();
            for (int i = 0; i < args.length; i++) {
                String key = (paramNames != null && i < paramNames.length) ? paramNames[i] : "arg" + i;
                body.put(key, args[i]);
            }
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            return "[unserializable request args]";
        }
    }

}
