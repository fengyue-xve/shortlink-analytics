package com.example.shortlink.controller;

import com.example.shortlink.config.JwtInterceptor;
import com.example.shortlink.service.VisitLogService;
import com.example.shortlink.vo.DashboardVO;
import com.example.shortlink.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final VisitLogService visitLogService;

    public DashboardController(VisitLogService visitLogService) {
        this.visitLogService = visitLogService;
    }

    @GetMapping("/stats")
    public ResultVO<DashboardVO> stats() {
        return ResultVO.success(visitLogService.dashboard(JwtInterceptor.currentUserId()));
    }
}
