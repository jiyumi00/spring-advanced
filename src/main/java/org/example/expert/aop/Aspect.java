package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Slf4j
@org.aspectj.lang.annotation.Aspect
public class Aspect {
    //포인트 컷 -> 범위 지정


    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    private void adminLayer(){

    }

    //어드바이스들
    @Around("adminLayer()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("Before");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Long requestUserId=(Long)request.getAttribute("userId");
        LocalDateTime requestTime=LocalDateTime.now();
        String requestUrl=request.getRequestURI();

        log.info("요청한 사용자의 ID={}, API 요청 시각 = {}, API 요청 URL= {}",requestUserId,requestTime,requestUrl);

        try{
            Object result= joinPoint.proceed();
            log.info("After Returning");
            return result;

        }catch(Exception e){
            log.info("After throwing");
            throw e;
        }finally {

            log.info("After");

        }
    }
}
