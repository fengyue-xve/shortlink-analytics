package com.example.shortlink.controller;

import com.example.shortlink.dto.AiCampaignDTO;
import com.example.shortlink.service.AiCampaignService;
import com.example.shortlink.vo.AiCampaignVO;
import com.example.shortlink.vo.ResultVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiCampaignController {

    private final AiCampaignService aiCampaignService;

    public AiCampaignController(AiCampaignService aiCampaignService) {
        this.aiCampaignService = aiCampaignService;
    }

    @PostMapping("/campaign")
    public ResultVO<AiCampaignVO> campaign(@Valid @RequestBody AiCampaignDTO dto) {
        return ResultVO.success(aiCampaignService.generate(dto));
    }
}
