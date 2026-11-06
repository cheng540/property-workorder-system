package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.property.common.Result;
import com.property.entity.RepairType;
import com.property.service.RepairTypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/type")
public class RepairTypeController {

    @Resource
    private RepairTypeService repairTypeService;

    @GetMapping("/list")
    public Result<List<RepairType>> list() {
        return Result.ok(repairTypeService.list(
                new LambdaQueryWrapper<RepairType>().orderByAsc(RepairType::getId)));
    }

    @PostMapping
    public Result<Void> save(@RequestBody RepairType type) {
        repairTypeService.saveType(type);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        repairTypeService.removeType(id);
        return Result.ok();
    }
}
