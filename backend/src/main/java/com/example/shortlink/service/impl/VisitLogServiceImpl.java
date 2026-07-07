package com.example.shortlink.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.entity.ShortLink;
import com.example.shortlink.entity.VisitLog;
import com.example.shortlink.mapper.ShortLinkMapper;
import com.example.shortlink.mapper.VisitLogMapper;
import com.example.shortlink.service.VisitLogService;
import com.example.shortlink.util.IpUtil;
import com.example.shortlink.vo.DashboardVO;
import com.example.shortlink.vo.ShortLinkVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper, VisitLog> implements VisitLogService {

    private final ShortLinkMapper shortLinkMapper;

    @Value("${shortlink.domain}")
    private String domain;

    public VisitLogServiceImpl(ShortLinkMapper shortLinkMapper) {
        this.shortLinkMapper = shortLinkMapper;
    }

    @Override
    @Transactional
    public void recordVisit(ShortLink link, HttpServletRequest request) {
        VisitLog log = new VisitLog();
        log.setLinkId(link.getId());
        log.setShortCode(link.getShortCode());
        log.setIpAddress(IpUtil.getClientIp(request));
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setReferer(request.getHeader("Referer"));
        log.setVisitTime(LocalDateTime.now());
        save(log);
        shortLinkMapper.increaseVisitCount(link.getId());
    }

    @Override
    public DashboardVO dashboard(Long userId) {
        DashboardVO vo = new DashboardVO();
        Long totalLinks = shortLinkMapper.selectCount(new LambdaQueryWrapper<ShortLink>().eq(ShortLink::getUserId, userId));
        Long totalVisits = count(new LambdaQueryWrapper<VisitLog>()
                .inSql(VisitLog::getLinkId, "SELECT id FROM short_link WHERE user_id = " + userId));
        Long todayVisits = baseMapper.countTodayVisitsByUser(userId);
        List<ShortLink> top = shortLinkMapper.selectList(new LambdaQueryWrapper<ShortLink>()
                .eq(ShortLink::getUserId, userId)
                .orderByDesc(ShortLink::getVisitCount)
                .last("LIMIT 5"));
        List<ShortLinkVO> topLinks = new ArrayList<>();
        for (ShortLink link : top) {
            ShortLinkVO item = new ShortLinkVO();
            BeanUtils.copyProperties(link, item);
            item.setShortUrl(domain + "/s/" + link.getShortCode());
            topLinks.add(item);
        }
        vo.setTotalLinks(totalLinks);
        vo.setTotalVisits(totalVisits);
        vo.setTodayVisits(todayVisits);
        vo.setTopLinks(topLinks);
        return vo;
    }
}
