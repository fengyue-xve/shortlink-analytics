package com.example.shortlink.service;

import com.example.shortlink.dto.AiCampaignDTO;
import com.example.shortlink.vo.AiCampaignVO;

public interface AiCampaignService {
    AiCampaignVO generate(AiCampaignDTO dto);
}
