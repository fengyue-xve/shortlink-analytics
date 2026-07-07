package com.example.shortlink.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shortlink.config.JwtInterceptor;
import com.example.shortlink.dto.CampaignDTO;
import com.example.shortlink.service.CampaignService;
import com.example.shortlink.vo.CampaignVO;
import com.example.shortlink.vo.ResultVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/campaign")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping("/create")
    public ResultVO<CampaignVO> create(@Valid @RequestBody CampaignDTO dto) {
        return ResultVO.success(campaignService.create(JwtInterceptor.currentUserId(), dto));
    }

    @PutMapping("/update")
    public ResultVO<Boolean> update(@Valid @RequestBody CampaignDTO dto) {
        return ResultVO.success(campaignService.updateCampaign(JwtInterceptor.currentUserId(), dto));
    }

    @GetMapping("/list")
    public ResultVO<Page<CampaignVO>> list(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           @RequestParam(required = false) String keyword) {
        return ResultVO.success(campaignService.pageByUser(JwtInterceptor.currentUserId(), page, size, keyword));
    }

    @GetMapping("/simple")
    public ResultVO<List<CampaignVO>> simple() {
        return ResultVO.success(campaignService.simpleList(JwtInterceptor.currentUserId()));
    }
}
