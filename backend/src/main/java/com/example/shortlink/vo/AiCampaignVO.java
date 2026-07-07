package com.example.shortlink.vo;

import lombok.Data;

import java.util.List;

@Data
public class AiCampaignVO {
    private String title;
    private String shortIntro;
    private List<String> channelSuggestions;
    private List<String> copywritingOptions;
    private List<String> riskTips;
    private String prompt;
    private Boolean fallback;
}
