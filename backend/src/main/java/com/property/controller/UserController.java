package com.property.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.common.Result;
import com.property.entity.User;
import com.property.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/page")
    public Result<Page<User>> page(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "8") int size,
                                   @RequestParam(required = false) String role,
                                   @RequestParam(required = false) String keyword) {
        return Result.ok(userService.pageUsers(page, size, role, keyword));
    }

    @PostMapping
    public Result<Void> add(@RequestBody User user) {
        userService.addUser(user);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.ok();
    }

    @PutMapping("/{id}/reset")
    public Result<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.ok();
    }

    @GetMapping("/workers")
    public Result<List<User>> workers() {
        return Result.ok(userService.listWorkers());
    }
}
