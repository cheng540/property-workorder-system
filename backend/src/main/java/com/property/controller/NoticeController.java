package com.property.controller;

import com.property.common.Result;
import com.property.entity.Notice;
import com.property.service.NoticeService;
import com.property.util.RoleUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @GetMapping("/list")
    public Result<List<Notice>> list() {
        return Result.ok(noticeService.listLatest(10));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Notice notice) {
        RoleUtil.requireAdmin();
        notice.setId(null);
        notice.setCreateTime(LocalDateTime.now());
        noticeService.save(notice);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Notice notice) {
        RoleUtil.requireAdmin();
        noticeService.updateById(notice);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        RoleUtil.requireAdmin();
        noticeService.removeById(id);
        return Result.ok();
    }
}
