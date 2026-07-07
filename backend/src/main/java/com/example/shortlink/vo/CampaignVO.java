package com.example.shortlink.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CampaignVO {
    private Long id;
    private String name;
    private String targetAudience;
    private String channel;
    private String description;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long linkCount;
    private Long visitCount;
    private LocalDateTime createTime;
}
