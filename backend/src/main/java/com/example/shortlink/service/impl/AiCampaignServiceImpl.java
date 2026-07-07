package com.example.shortlink.service.impl;

import com.example.shortlink.dto.AiCampaignDTO;
import com.example.shortlink.service.AiCampaignService;
import com.example.shortlink.vo.AiCampaignVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AiCampaignServiceImpl implements AiCampaignService {

    private final RestTemplate restTemplate;

    @Value("${ai.deepseek.api-key}")
    private String apiKey;

    @Value("${ai.deepseek.base-url}")
    private String baseUrl;

    @Value("${ai.deepseek.model}")
    private String model;

    public AiCampaignServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public AiCampaignVO generate(AiCampaignDTO dto) {
        String prompt = buildPrompt(dto);
        if (apiKey == null || apiKey.isBlank()) {
            return fallback(dto, prompt);
        }
        try {
            String content = callDeepSeek(prompt);
            AiCampaignVO vo = fallback(dto, prompt);
            vo.setFallback(false);
            vo.setShortIntro(content);
            return vo;
        } catch (Exception e) {
            return fallback(dto, prompt);
        }
    }

    private String callDeepSeek(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "你是短链接投放助手，回答要简洁、可执行，不要夸大宣传。"),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        Map<?, ?> response = restTemplate.postForObject(baseUrl + "/chat/completions", entity, Map.class);
        if (response == null) {
            return "AI 服务暂无响应，请稍后重试。";
        }
        List<?> choices = (List<?>) response.get("choices");
        if (choices == null || choices.isEmpty()) {
            return "AI 服务未返回有效内容。";
        }
        Map<?, ?> first = (Map<?, ?>) choices.get(0);
        Map<?, ?> message = (Map<?, ?>) first.get("message");
        return message == null ? "AI 服务未返回文案。" : String.valueOf(message.get("content"));
    }

    private String buildPrompt(AiCampaignDTO dto) {
        return "请基于以下信息生成短链接投放建议：\n"
                + "目标链接：" + safe(dto.getOriginalUrl()) + "\n"
                + "产品/活动名称：" + safe(dto.getProductName()) + "\n"
                + "目标人群：" + safe(dto.getTargetAudience()) + "\n"
                + "核心卖点：" + safe(dto.getSellingPoint()) + "\n"
                + "计划渠道：" + safe(dto.getChannel()) + "\n\n"
                + "输出内容包括：1个推广标题、3条短文案、3条渠道投放建议、2条风险提示。";
    }

    private AiCampaignVO fallback(AiCampaignDTO dto, String prompt) {
        String product = safe(dto.getProductName()).isBlank() ? "你的活动" : safe(dto.getProductName());
        String audience = safe(dto.getTargetAudience()).isBlank() ? "目标用户" : safe(dto.getTargetAudience());
        String point = safe(dto.getSellingPoint()).isBlank() ? "核心价值" : safe(dto.getSellingPoint());

        AiCampaignVO vo = new AiCampaignVO();
        vo.setTitle(product + "限时推广");
        vo.setShortIntro("面向" + audience + "，突出“" + point + "”，用短链接承接访问并统计投放效果。");
        vo.setChannelSuggestions(List.of(
                "朋友圈/社群：适合冷启动扩散，文案要短，重点突出利益点。",
                "公众号/文章：适合承接详细介绍，可配合短链接统计点击效果。",
                "BOSS/简历项目演示：可用短链展示项目 Demo、GitHub 或在线文档。"
        ));
        List<String> copy = new ArrayList<>();
        copy.add(product + "来了，点击了解：" + dto.getOriginalUrl());
        copy.add("如果你是" + audience + "，这个链接可以快速了解" + point + "。");
        copy.add("用 30 秒看看" + product + "，核心亮点：" + point + "。");
        vo.setCopywritingOptions(copy);
        vo.setRiskTips(List.of(
                "不要夸大承诺，避免使用绝对化宣传用语。",
                "投放前确认目标链接可访问，并通过短链接后台观察点击数据。"
        ));
        vo.setPrompt(prompt);
        vo.setFallback(true);
        return vo;
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
