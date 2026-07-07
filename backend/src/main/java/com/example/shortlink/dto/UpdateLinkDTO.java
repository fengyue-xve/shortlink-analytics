package com.example.shortlink.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateLinkDTO {
    private Long id;
    private String title;
    private Integer status;
    private LocalDateTime expireTime;
}
