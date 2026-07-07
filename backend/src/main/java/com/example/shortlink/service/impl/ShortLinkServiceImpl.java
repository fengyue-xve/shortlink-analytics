package com.example.shortlink.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.dto.CreateLinkDTO;
import com.example.shortlink.dto.UpdateLinkDTO;
import com.example.shortlink.entity.ShortLink;
import com.example.shortlink.exception.BusinessException;
import com.example.shortlink.mapper.ShortLinkMapper;
import com.example.shortlink.service.ShortLinkService;
import com.example.shortlink.util.Base62Util;
import com.example.shortlink.vo.ShortLinkVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink> implements ShortLinkService {

    @Value("${shortlink.domain}")
    private String domain;

    @Value("${shortlink.cache-seconds}")
    private long cacheSeconds;

    private final StringRedisTemplate redisTemplate;

    public ShortLinkServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    public ShortLinkVO create(Long userId, CreateLinkDTO dto) {
        if (!dto.getOriginalUrl().startsWith("http://") && !dto.getOriginalUrl().startsWith("https://")) {
            throw new BusinessException("链接必须以 http:// 或 https:// 开头");
        }
        ShortLink link = new ShortLink();
        link.setUserId(userId);
        link.setCampaignId(dto.getCampaignId());
        link.setOriginalUrl(dto.getOriginalUrl());
        link.setTitle(dto.getTitle());
        link.setStatus(1);
        link.setExpireTime(dto.getExpireTime());
        link.setVisitCount(0L);
        save(link);

        String shortCode = Base62Util.encode(link.getId() + 10_000);
        link.setShortCode(shortCode);
        updateById(link);
        cacheLink(link);
        return toVO(link);
    }

    @Override
    public Page<ShortLinkVO> listByUser(Long userId, Integer page, Integer size, String keyword) {
        LambdaQueryWrapper<ShortLink> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShortLink::getUserId, userId);
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(ShortLink::getTitle, keyword)
                    .or().like(ShortLink::getOriginalUrl, keyword)
                    .or().like(ShortLink::getShortCode, keyword));
        }
        wrapper.orderByDesc(ShortLink::getCreateTime);
        Page<ShortLink> result = page(new Page<>(page, size), wrapper);
        Page<ShortLinkVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<ShortLinkVO> records = new ArrayList<>();
        for (ShortLink link : result.getRecords()) {
            records.add(toVO(link));
        }
        voPage.setRecords(records);
        return voPage;
    }

    @Override
    public Boolean updateLink(Long userId, UpdateLinkDTO dto) {
        ShortLink link = getById(dto.getId());
        if (link == null || !link.getUserId().equals(userId)) {
            throw new BusinessException("短链接不存在");
        }
        link.setTitle(dto.getTitle());
        link.setStatus(dto.getStatus());
        link.setExpireTime(dto.getExpireTime());
        boolean ok = updateById(link);
        redisTemplate.delete(cacheKey(link.getShortCode()));
        return ok;
    }

    @Override
    public Boolean deleteLink(Long userId, Long id) {
        ShortLink link = getById(id);
        if (link == null || !link.getUserId().equals(userId)) {
            throw new BusinessException("短链接不存在");
        }
        link.setStatus(0);
        boolean ok = updateById(link);
        redisTemplate.delete(cacheKey(link.getShortCode()));
        return ok;
    }

    @Override
    public ShortLink resolve(String shortCode) {
        String cachedUrl = redisTemplate.opsForValue().get(cacheKey(shortCode));
        ShortLink link = baseMapper.selectByCode(shortCode);
        if (link == null) {
            if (cachedUrl != null) {
                redisTemplate.delete(cacheKey(shortCode));
            }
            throw new BusinessException(404, "??????");
        }
        validateResolvable(link);
        if (cachedUrl == null) {
            cacheLink(link);
        }
        return link;
    }

    private void validateResolvable(ShortLink link) {
        if (link.getStatus() == 0) {
            redisTemplate.delete(cacheKey(link.getShortCode()));
            throw new BusinessException(410, "??????");
        }
        if (link.getExpireTime() != null && link.getExpireTime().isBefore(LocalDateTime.now())) {
            redisTemplate.delete(cacheKey(link.getShortCode()));
            throw new BusinessException(410, "??????");
        }
    }

    private void cacheLink(ShortLink link) {
        long ttl = cacheSeconds;
        if (link.getExpireTime() != null) {
            long secondsToExpire = Duration.between(LocalDateTime.now(), link.getExpireTime()).getSeconds();
            if (secondsToExpire <= 0) {
                return;
            }
            ttl = Math.min(cacheSeconds, secondsToExpire);
        }
        redisTemplate.opsForValue().set(cacheKey(link.getShortCode()), link.getOriginalUrl(), ttl, TimeUnit.SECONDS);
    }

    private String cacheKey(String shortCode) {
        return "shortlink:url:" + shortCode;
    }

    private ShortLinkVO toVO(ShortLink link) {
        ShortLinkVO vo = new ShortLinkVO();
        BeanUtils.copyProperties(link, vo);
        vo.setShortUrl(domain + "/s/" + link.getShortCode());
        return vo;
    }
}
