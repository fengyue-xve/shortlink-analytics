package com.example.shortlink.controller;

import com.example.shortlink.dto.LoginDTO;
import com.example.shortlink.dto.RegisterDTO;
import com.example.shortlink.service.UserService;
import com.example.shortlink.vo.LoginVO;
import com.example.shortlink.vo.ResultVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResultVO<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return ResultVO.success("登录成功", userService.login(dto));
    }

    @PostMapping("/register")
    public ResultVO<Boolean> register(@Valid @RequestBody RegisterDTO dto) {
        return ResultVO.success(userService.register(dto));
    }
}
