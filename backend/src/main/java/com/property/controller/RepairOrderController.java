package com.property.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.common.Result;
import com.property.dto.EvaluateDTO;
import com.property.dto.HandleDTO;
import com.property.dto.RepairDTO;
import com.property.entity.RepairOrder;
import com.property.service.RepairOrderService;
import com.property.vo.OrderDetailVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class RepairOrderController {

    @Resource
    private RepairOrderService repairOrderService;

    @PostMapping
    public Result<Long> create(@RequestBody RepairDTO dto) {
        return Result.ok(repairOrderService.create(dto));
    }

    @GetMapping("/page")
    public Result<Page<RepairOrder>> page(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "8") int size,
                                          @RequestParam(required = false) Integer status,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) Long typeId) {
        return Result.ok(repairOrderService.pageQuery(page, size, status, keyword, typeId));
    }

    @GetMapping("/{id}")
    public Result<OrderDetailVO> detail(@PathVariable Long id) {
        return Result.ok(repairOrderService.detail(id));
    }

    @PutMapping("/{id}/assign")
    public Result<Void> assign(@PathVariable Long id, @RequestParam Long workerId) {
        repairOrderService.assign(id, workerId);
        return Result.ok();
    }

    @PutMapping("/{id}/finish")
    public Result<Void> finish(@PathVariable Long id, @RequestBody HandleDTO dto) {
        repairOrderService.finish(id, dto.getHandleRemark());
        return Result.ok();
    }

    @PutMapping("/{id}/evaluate")
    public Result<Void> evaluate(@PathVariable Long id, @RequestBody EvaluateDTO dto) {
        repairOrderService.evaluate(id, dto.getRating(), dto.getEvaluate());
        return Result.ok();
    }
}
