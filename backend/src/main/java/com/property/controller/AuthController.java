package com.property.controller;

import com.property.common.Result;
import com.property.dto.LoginDTO;
import com.property.entity.User;
import com.property.service.UserService;
import com.property.util.UserContext;
import com.property.vo.LoginVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO dto) {
        return Result.ok(userService.login(dto));
    }

    @GetMapping("/me")
    public Result<User> me() {
        return Result.ok(userService.getById(UserContext.getUserId()));
    }
}
