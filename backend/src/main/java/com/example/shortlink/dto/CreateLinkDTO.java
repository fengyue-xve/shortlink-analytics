package com.example.shortlink.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateLinkDTO {
    private Long campaignId;
    @NotBlank(message = "原始链接不能为空")
    private String originalUrl;
    private String title;
    private LocalDateTime expireTime;
}
