package com.example.shortlink.config;

import com.example.shortlink.entity.User;
import com.example.shortlink.mapper.UserMapper;
import com.example.shortlink.util.BCryptUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;

    public DataInitializer(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void run(String... args) {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                user.setPassword(BCryptUtil.encode(user.getPassword()));
                userMapper.updateById(user);
            }
        }
    }
}
