package com.example.shortlink.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shortlink.dto.CreateLinkDTO;
import com.example.shortlink.dto.UpdateLinkDTO;
import com.example.shortlink.entity.ShortLink;
import com.example.shortlink.vo.ShortLinkVO;

public interface ShortLinkService extends IService<ShortLink> {
    ShortLinkVO create(Long userId, CreateLinkDTO dto);

    Page<ShortLinkVO> listByUser(Long userId, Integer page, Integer size, String keyword);

    Boolean updateLink(Long userId, UpdateLinkDTO dto);

    Boolean deleteLink(Long userId, Long id);

    ShortLink resolve(String shortCode);
}
