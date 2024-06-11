package com.fenrir.simplebookdatabasesite.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLoggerInterceptor());
    }

    @Slf4j
    private static class RequestLoggerInterceptor implements HandlerInterceptor {
        private final AtomicLong counter = new AtomicLong();

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            log.info(String.format(
                    "[COUNT: %s]\t REQUEST %s %s",
                    counter.getAndIncrement(),
                    request.getMethod(),
                    request.getRequestURI())
            );

            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
    }
}
