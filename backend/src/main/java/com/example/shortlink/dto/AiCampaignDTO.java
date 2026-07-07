package com.example.shortlink.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AiCampaignDTO {
    @NotBlank(message = "目标链接不能为空")
    private String originalUrl;
    private String productName;
    private String targetAudience;
    private String sellingPoint;
    private String channel;
}
