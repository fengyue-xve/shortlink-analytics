package com.example.shortlink.controller;

import com.example.shortlink.entity.ShortLink;
import com.example.shortlink.exception.BusinessException;
import com.example.shortlink.service.ShortLinkService;
import com.example.shortlink.service.VisitLogService;
import com.example.shortlink.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
public class RedirectController {

    private final ShortLinkService shortLinkService;
    private final VisitLogService visitLogService;
    private final StringRedisTemplate redisTemplate;

    @Value("${shortlink.rate-limit-per-minute}")
    private long rateLimitPerMinute;

    public RedirectController(ShortLinkService shortLinkService,
                              VisitLogService visitLogService,
                              StringRedisTemplate redisTemplate) {
        this.shortLinkService = shortLinkService;
        this.visitLogService = visitLogService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/s/{shortCode}")
    public void redirect(@PathVariable String shortCode,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        rateLimit(shortCode, request);
        ShortLink link = shortLinkService.resolve(shortCode);
        visitLogService.recordVisit(link, request);
        response.sendRedirect(link.getOriginalUrl());
    }

    private void rateLimit(String shortCode, HttpServletRequest request) {
        String key = "shortlink:rate:" + shortCode + ":" + IpUtil.getClientIp(request);
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, 60, TimeUnit.SECONDS);
        }
        if (count != null && count > rateLimitPerMinute) {
            throw new BusinessException(429, "访问过于频繁，请稍后再试");
        }
    }
}
