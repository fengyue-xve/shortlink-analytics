package com.example.shortlink.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shortlink.config.JwtInterceptor;
import com.example.shortlink.dto.CreateLinkDTO;
import com.example.shortlink.dto.UpdateLinkDTO;
import com.example.shortlink.service.ShortLinkService;
import com.example.shortlink.vo.ResultVO;
import com.example.shortlink.vo.ShortLinkVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/link")
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    public ShortLinkController(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @PostMapping("/create")
    public ResultVO<ShortLinkVO> create(@Valid @RequestBody CreateLinkDTO dto) {
        return ResultVO.success(shortLinkService.create(JwtInterceptor.currentUserId(), dto));
    }

    @GetMapping("/list")
    public ResultVO<Page<ShortLinkVO>> list(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(required = false) String keyword) {
        return ResultVO.success(shortLinkService.listByUser(JwtInterceptor.currentUserId(), page, size, keyword));
    }

    @PutMapping("/update")
    public ResultVO<Boolean> update(@RequestBody UpdateLinkDTO dto) {
        return ResultVO.success(shortLinkService.updateLink(JwtInterceptor.currentUserId(), dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable Long id) {
        return ResultVO.success(shortLinkService.deleteLink(JwtInterceptor.currentUserId(), id));
    }
}
