package com.example.shortlink.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shortlink.dto.LoginDTO;
import com.example.shortlink.dto.RegisterDTO;
import com.example.shortlink.entity.User;
import com.example.shortlink.exception.BusinessException;
import com.example.shortlink.mapper.UserMapper;
import com.example.shortlink.service.UserService;
import com.example.shortlink.util.BCryptUtil;
import com.example.shortlink.util.JwtUtil;
import com.example.shortlink.vo.LoginVO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final JwtUtil jwtUtil;

    public UserServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        User user = baseMapper.selectByUsername(dto.getUsername());
        if (user == null || !BCryptUtil.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        LoginVO vo = new LoginVO();
        vo.setToken(jwtUtil.generateToken(user.getId(), user.getUsername()));
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        return vo;
    }

    @Override
    public Boolean register(RegisterDTO dto) {
        long count = count(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(BCryptUtil.encode(dto.getPassword()));
        user.setNickname(dto.getNickname() == null || dto.getNickname().isBlank() ? dto.getUsername() : dto.getNickname());
        user.setStatus(1);
        return save(user);
    }
}
