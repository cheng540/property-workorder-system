package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.common.BizException;
import com.property.dto.RepairDTO;
import com.property.entity.OrderLog;
import com.property.entity.RepairOrder;
import com.property.entity.RepairType;
import com.property.entity.User;
import com.property.mapper.OrderLogMapper;
import com.property.mapper.RepairOrderMapper;
import com.property.mapper.RepairTypeMapper;
import com.property.mapper.UserMapper;
import com.property.service.RepairOrderService;
import com.property.util.RoleUtil;
import com.property.util.UserContext;
import com.property.vo.OrderDetailVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder>
        implements RepairOrderService {

    @Resource
    private RepairTypeMapper repairTypeMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private OrderLogMapper orderLogMapper;

    @Override
    public Long create(RepairDTO dto) {
        if (!RoleUtil.isResident()) {
            throw new BizException("只有住户账号可以提交报修单");
        }
        if (dto.getTypeId() == null) {
            throw new BizException("请选择报修类型");
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new BizException("请填写故障标题");
        }
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new BizException("请描述故障情况");
        }
        RepairType type = repairTypeMapper.selectById(dto.getTypeId());
        if (type == null) {
            throw new BizException("报修类型不存在");
        }

        RepairOrder order = new RepairOrder();
        order.setOrderNo(nextOrderNo());
        order.setResidentId(UserContext.getUserId());
        order.setTypeId(dto.getTypeId());
        order.setTitle(dto.getTitle());
        order.setContent(dto.getContent());
        order.setAddress(dto.getAddress());
        order.setUrgency(dto.getUrgency() == null ? 1 : dto.getUrgency());
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        this.save(order);

        writeLog(order.getId(), "提交报修", "住户提交报修单，等待物业派单");
        return order.getId();
    }

    private String nextOrderNo() {
        String day = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long todayCount = this.count(new LambdaQueryWrapper<RepairOrder>()
                .likeRight(RepairOrder::getOrderNo, "BX" + day));
        return "BX" + day + String.format("%03d", todayCount + 1);
    }

    @Override
    public void assign(Long orderId, Long workerId) {
        RoleUtil.requireAdmin();
        RepairOrder order = this.getById(orderId);
        if (order == null) {
            throw new BizException("工单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BizException("该工单已派单，不能重复派单");
        }
        User worker = userMapper.selectById(workerId);
        if (worker == null || !"WORKER".equals(worker.getRole())) {
            throw new BizException("请选择维修工账号");
        }
        if (worker.getStatus() != null && worker.getStatus() == 0) {
            throw new BizException("该维修工账号已停用");
        }
        order.setWorkerId(workerId);
        order.setStatus(1);
        order.setAssignTime(LocalDateTime.now());
        this.updateById(order);
        writeLog(orderId, "派单", "指派给维修工 " + worker.getRealName());
    }

    @Override
    public void finish(Long orderId, String handleRemark) {
        RoleUtil.requireWorker();
        RepairOrder order = this.getById(orderId);
        if (order == null) {
            throw new BizException("工单不存在");
        }
        if (!UserContext.getUserId().equals(order.getWorkerId())) {
            throw new BizException(403, "只能处理派给自己的工单");
        }
        if (order.getStatus() != 1) {
            throw new BizException("该工单当前状态不能提交完工");
        }
        if (handleRemark == null || handleRemark.isBlank()) {
            throw new BizException("请填写处理说明");
        }
        order.setHandleRemark(handleRemark);
        order.setStatus(2);
        order.setFinishTime(LocalDateTime.now());
        this.updateById(order);
        writeLog(orderId, "完工", handleRemark);
    }

    @Override
    public void evaluate(Long orderId, Integer rating, String evaluate) {
        if (!RoleUtil.isResident()) {
            throw new BizException("只有住户可以评价工单");
        }
        RepairOrder order = this.getById(orderId);
        if (order == null) {
            throw new BizException("工单不存在");
        }
        if (!UserContext.getUserId().equals(order.getResidentId())) {
            throw new BizException(403, "只能评价本人提交的工单");
        }
        if (order.getStatus() == 3) {
            throw new BizException("该工单已评价，无需重复评价");
        }
        if (order.getStatus() != 2) {
            throw new BizException("该工单尚未完工，不能评价");
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new BizException("请给出 1 到 5 星的评分");
        }
        order.setRating(rating);
        order.setEvaluate(evaluate);
        order.setStatus(3);
        this.updateById(order);
        writeLog(orderId, "评价", rating + " 星评价：" + (evaluate == null ? "无文字说明" : evaluate));
    }

    private void writeLog(Long orderId, String action, String remark) {
        OrderLog log = new OrderLog();
        log.setOrderId(orderId);
        log.setOperatorId(UserContext.getUserId());
        User user = userMapper.selectById(UserContext.getUserId());
        log.setOperatorName(user == null ? UserContext.getRole() : user.getRealName());
        log.setAction(action);
        log.setRemark(remark);
        log.setCreateTime(LocalDateTime.now());
        orderLogMapper.insert(log);
    }

    @Override
    public Page<RepairOrder> pageQuery(int page, int size, Integer status, String keyword, Long typeId) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        if (RoleUtil.isResident()) {
            wrapper.eq(RepairOrder::getResidentId, UserContext.getUserId());
        } else if (RoleUtil.isWorker()) {
            wrapper.eq(RepairOrder::getWorkerId, UserContext.getUserId());
        }
        if (status != null) {
            wrapper.eq(RepairOrder::getStatus, status);
        }
        if (typeId != null) {
            wrapper.eq(RepairOrder::getTypeId, typeId);
        }
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(RepairOrder::getTitle, keyword)
                    .or().like(RepairOrder::getOrderNo, keyword)
                    .or().like(RepairOrder::getAddress, keyword));
        }
        wrapper.orderByDesc(RepairOrder::getId);
        Page<RepairOrder> result = this.page(new Page<>(page, size), wrapper);
        fillNames(result.getRecords());
        return result;
    }

    private void fillNames(List<RepairOrder> records) {
        if (records.isEmpty()) {
            return;
        }
        List<Long> typeIds = records.stream().map(RepairOrder::getTypeId).distinct().toList();
        List<Long> userIds = records.stream()
                .flatMap(o -> java.util.stream.Stream.of(o.getResidentId(), o.getWorkerId()))
                .filter(i -> i != null)
                .distinct()
                .toList();

        Map<Long, String> typeMap = new HashMap<>();
        for (RepairType t : repairTypeMapper.selectBatchIds(typeIds)) {
            typeMap.put(t.getId(), t.getName());
        }
        Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            for (User u : userMapper.selectBatchIds(userIds)) {
                userMap.put(u.getId(), u);
            }
        }
        for (RepairOrder o : records) {
            o.setTypeName(typeMap.getOrDefault(o.getTypeId(), "其他"));
            User resident = userMap.get(o.getResidentId());
            if (resident != null) {
                o.setResidentName(resident.getRealName());
                o.setResidentPhone(resident.getPhone());
            }
            User worker = userMap.get(o.getWorkerId());
            if (worker != null) {
                o.setWorkerName(worker.getRealName());
            }
        }
    }

    @Override
    public OrderDetailVO detail(Long id) {
        RepairOrder order = this.getById(id);
        if (order == null) {
            throw new BizException("工单不存在");
        }
        if (RoleUtil.isResident() && !UserContext.getUserId().equals(order.getResidentId())) {
            throw new BizException(403, "只能查看本人提交的工单");
        }
        if (RoleUtil.isWorker() && !UserContext.getUserId().equals(order.getWorkerId())) {
            throw new BizException(403, "只能查看派给自己的工单");
        }
        fillNames(List.of(order));
        List<OrderLog> logs = orderLogMapper.selectList(new LambdaQueryWrapper<OrderLog>()
                .eq(OrderLog::getOrderId, id)
                .orderByAsc(OrderLog::getId));
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrder(order);
        vo.setLogs(logs);
        return vo;
    }
}
