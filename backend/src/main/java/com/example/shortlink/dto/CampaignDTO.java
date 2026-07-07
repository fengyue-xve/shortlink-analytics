package com.example.shortlink.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CampaignDTO {
    private Long id;
    @NotBlank(message = "活动名称不能为空")
    private String name;
    private String targetAudience;
    private String channel;
    private String description;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
