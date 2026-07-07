package com.example.shortlink.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shortlink.dto.CampaignDTO;
import com.example.shortlink.entity.Campaign;
import com.example.shortlink.vo.CampaignVO;

import java.util.List;

public interface CampaignService extends IService<Campaign> {
    CampaignVO create(Long userId, CampaignDTO dto);

    Boolean updateCampaign(Long userId, CampaignDTO dto);

    Page<CampaignVO> pageByUser(Long userId, Integer page, Integer size, String keyword);

    List<CampaignVO> simpleList(Long userId);
}
