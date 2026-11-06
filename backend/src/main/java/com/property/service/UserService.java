package com.property.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.property.dto.LoginDTO;
import com.property.entity.User;
import com.property.vo.LoginVO;

import java.util.List;

public interface UserService extends IService<User> {

    LoginVO login(LoginDTO dto);

    Page<User> pageUsers(int page, int size, String role, String keyword);

    void addUser(User user);

    void updateStatus(Long id, Integer status);

    void resetPassword(Long id);

    List<User> listWorkers();
}
