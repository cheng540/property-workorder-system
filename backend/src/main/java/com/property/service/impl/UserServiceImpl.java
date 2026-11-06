package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.common.BizException;
import com.property.dto.LoginDTO;
import com.property.entity.User;
import com.property.mapper.UserMapper;
import com.property.service.UserService;
import com.property.util.JwtUtil;
import com.property.util.RoleUtil;
import com.property.vo.LoginVO;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoginVO login(LoginDTO dto) {
        if (dto.getUsername() == null || dto.getPassword() == null) {
            throw new BizException("请输入用户名和密码");
        }
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BizException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BizException("该账号已被停用，请联系物业管理员");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }
        LoginVO vo = new LoginVO();
        vo.setToken(jwtUtil.create(user.getId(), user.getUsername(), user.getRole()));
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setRole(user.getRole());
        vo.setAddress(user.getAddress());
        return vo;
    }

    @Override
    public Page<User> pageUsers(int page, int size, String role, String keyword) {
        RoleUtil.requireAdmin();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(User::getRole, "ADMIN");
        if (role != null && !role.isBlank()) {
            wrapper.eq(User::getRole, role);
        }
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getRealName, keyword)
                    .or().like(User::getPhone, keyword));
        }
        wrapper.orderByAsc(User::getId);
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public void addUser(User user) {
        RoleUtil.requireAdmin();
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BizException("请填写账号");
        }
        long exists = this.count(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        if (exists > 0) {
            throw new BizException("该账号已存在");
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("RESIDENT");
        }
        if (!"RESIDENT".equals(user.getRole()) && !"WORKER".equals(user.getRole())) {
            throw new BizException("只能新增住户或维修工账号");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword("123456");
        }
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        this.save(user);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        RoleUtil.requireAdmin();
        User user = this.getById(id);
        if (user == null) {
            throw new BizException("账号不存在");
        }
        if ("ADMIN".equals(user.getRole())) {
            throw new BizException("管理员账号不能停用");
        }
        user.setStatus(status);
        this.updateById(user);
    }

    @Override
    public void resetPassword(Long id) {
        RoleUtil.requireAdmin();
        User user = this.getById(id);
        if (user == null) {
            throw new BizException("账号不存在");
        }
        user.setPassword(passwordEncoder.encode("123456"));
        this.updateById(user);
    }

    @Override
    public List<User> listWorkers() {
        RoleUtil.requireAdmin();
        return this.list(new LambdaQueryWrapper<User>()
                .eq(User::getRole, "WORKER")
                .eq(User::getStatus, 1)
                .orderByAsc(User::getId));
    }
}
