package com.property.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.property.dto.RepairDTO;
import com.property.entity.RepairOrder;
import com.property.vo.OrderDetailVO;

public interface RepairOrderService extends IService<RepairOrder> {

    Long create(RepairDTO dto);

    void assign(Long orderId, Long workerId);

    void finish(Long orderId, String handleRemark);

    void evaluate(Long orderId, Integer rating, String evaluate);

    Page<RepairOrder> pageQuery(int page, int size, Integer status, String keyword, Long typeId);

    OrderDetailVO detail(Long id);
}
