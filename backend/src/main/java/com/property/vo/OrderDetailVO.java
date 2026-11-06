package com.property.vo;

import com.property.entity.OrderLog;
import com.property.entity.RepairOrder;

import java.util.List;

/**
 * 工单详情，带上流转日志
 */
public class OrderDetailVO {

    private RepairOrder order;

    private List<OrderLog> logs;

    public RepairOrder getOrder() {
        return order;
    }

    public void setOrder(RepairOrder order) {
        this.order = order;
    }

    public List<OrderLog> getLogs() {
        return logs;
    }

    public void setLogs(List<OrderLog> logs) {
        this.logs = logs;
    }
}
