package com.example.shortlink.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShortLinkVO {
    private Long id;
    private Long campaignId;
    private String originalUrl;
    private String shortCode;
    private String shortUrl;
    private String title;
    private Integer status;
    private LocalDateTime expireTime;
    private Long visitCount;
    private LocalDateTime createTime;
}
