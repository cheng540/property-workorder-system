package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.property.common.Result;
import com.property.entity.RepairOrder;
import com.property.entity.User;
import com.property.service.RepairOrderService;
import com.property.service.UserService;
import com.property.util.RoleUtil;
import com.property.util.UserContext;
import com.property.vo.StatVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/stat")
public class StatController {

    @Resource
    private RepairOrderService repairOrderService;

    @Resource
    private UserService userService;

    @GetMapping("/overview")
    public Result<StatVO> overview() {
        StatVO vo = new StatVO();
        vo.setPendingCount(repairOrderService.count(scope().eq(RepairOrder::getStatus, 0)));
        vo.setHandlingCount(repairOrderService.count(scope().eq(RepairOrder::getStatus, 1)));
        vo.setFinishedCount(repairOrderService.count(scope().in(RepairOrder::getStatus, 2, 3)));
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        vo.setTodayCount(repairOrderService.count(scope().ge(RepairOrder::getCreateTime, todayStart)));
        vo.setResidentCount(userService.count(new LambdaQueryWrapper<User>().eq(User::getRole, "RESIDENT")));
        vo.setWorkerCount(userService.count(new LambdaQueryWrapper<User>().eq(User::getRole, "WORKER")));
        return Result.ok(vo);
    }

    /**
     * 按当前登录人的角色限定统计范围
     */
    private LambdaQueryWrapper<RepairOrder> scope() {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        if (RoleUtil.isResident()) {
            wrapper.eq(RepairOrder::getResidentId, UserContext.getUserId());
        } else if (RoleUtil.isWorker()) {
            wrapper.eq(RepairOrder::getWorkerId, UserContext.getUserId());
        }
        return wrapper;
    }
}
