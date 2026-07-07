package com.example.shortlink.vo;

import lombok.Data;

import java.util.List;

@Data
public class DashboardVO {
    private Long totalLinks;
    private Long totalVisits;
    private Long todayVisits;
    private List<ShortLinkVO> topLinks;
}
