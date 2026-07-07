package com.example.shortlink.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shortlink.entity.ShortLink;
import com.example.shortlink.entity.VisitLog;
import com.example.shortlink.vo.DashboardVO;
import jakarta.servlet.http.HttpServletRequest;

public interface VisitLogService extends IService<VisitLog> {
    void recordVisit(ShortLink link, HttpServletRequest request);

    DashboardVO dashboard(Long userId);
}
