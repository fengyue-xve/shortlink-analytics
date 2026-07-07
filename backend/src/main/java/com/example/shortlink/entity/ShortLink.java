package com.example.shortlink.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("short_link")
public class ShortLink {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long campaignId;
    private String originalUrl;
    private String shortCode;
    private String title;
    private Integer status;
    private LocalDateTime expireTime;
    private Long visitCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
