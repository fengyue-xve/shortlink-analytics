package com.example.shortlink.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shortlink.dto.LoginDTO;
import com.example.shortlink.dto.RegisterDTO;
import com.example.shortlink.entity.User;
import com.example.shortlink.vo.LoginVO;

public interface UserService extends IService<User> {
    LoginVO login(LoginDTO dto);

    Boolean register(RegisterDTO dto);
}
