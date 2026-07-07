package com.example.shortlink.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.dto.CampaignDTO;
import com.example.shortlink.entity.Campaign;
import com.example.shortlink.exception.BusinessException;
import com.example.shortlink.mapper.CampaignMapper;
import com.example.shortlink.service.CampaignService;
import com.example.shortlink.vo.CampaignVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CampaignServiceImpl extends ServiceImpl<CampaignMapper, Campaign> implements CampaignService {

    @Override
    public CampaignVO create(Long userId, CampaignDTO dto) {
        Campaign campaign = new Campaign();
        BeanUtils.copyProperties(dto, campaign);
        campaign.setUserId(userId);
        campaign.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        save(campaign);
        return toVO(campaign);
    }

    @Override
    public Boolean updateCampaign(Long userId, CampaignDTO dto) {
        Campaign campaign = getById(dto.getId());
        if (campaign == null || !campaign.getUserId().equals(userId)) {
            throw new BusinessException("活动不存在");
        }
        campaign.setName(dto.getName());
        campaign.setTargetAudience(dto.getTargetAudience());
        campaign.setChannel(dto.getChannel());
        campaign.setDescription(dto.getDescription());
        campaign.setStatus(dto.getStatus());
        campaign.setStartTime(dto.getStartTime());
        campaign.setEndTime(dto.getEndTime());
        return updateById(campaign);
    }

    @Override
    public Page<CampaignVO> pageByUser(Long userId, Integer page, Integer size, String keyword) {
        LambdaQueryWrapper<Campaign> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Campaign::getUserId, userId);
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(Campaign::getName, keyword)
                    .or().like(Campaign::getChannel, keyword)
                    .or().like(Campaign::getTargetAudience, keyword));
        }
        wrapper.orderByDesc(Campaign::getCreateTime);
        Page<Campaign> result = page(new Page<>(page, size), wrapper);
        Page<CampaignVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<CampaignVO> records = new ArrayList<>();
        for (Campaign campaign : result.getRecords()) {
            records.add(toVO(campaign));
        }
        voPage.setRecords(records);
        return voPage;
    }

    @Override
    public List<CampaignVO> simpleList(Long userId) {
        List<Campaign> campaigns = list(new LambdaQueryWrapper<Campaign>()
                .eq(Campaign::getUserId, userId)
                .eq(Campaign::getStatus, 1)
                .orderByDesc(Campaign::getCreateTime));
        List<CampaignVO> result = new ArrayList<>();
        for (Campaign campaign : campaigns) {
            result.add(toVO(campaign));
        }
        return result;
    }

    private CampaignVO toVO(Campaign campaign) {
        CampaignVO vo = new CampaignVO();
        BeanUtils.copyProperties(campaign, vo);
        vo.setLinkCount(baseMapper.countLinks(campaign.getId()));
        vo.setVisitCount(baseMapper.sumVisitCount(campaign.getId()));
        return vo;
    }
}
