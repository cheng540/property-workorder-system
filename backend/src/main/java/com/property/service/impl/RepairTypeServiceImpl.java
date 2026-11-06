package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.common.BizException;
import com.property.entity.RepairOrder;
import com.property.entity.RepairType;
import com.property.mapper.RepairOrderMapper;
import com.property.mapper.RepairTypeMapper;
import com.property.service.RepairTypeService;
import com.property.util.RoleUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RepairTypeServiceImpl extends ServiceImpl<RepairTypeMapper, RepairType> implements RepairTypeService {

    @Resource
    private RepairOrderMapper repairOrderMapper;

    @Override
    public void saveType(RepairType type) {
        RoleUtil.requireAdmin();
        if (type.getName() == null || type.getName().isBlank()) {
            throw new BizException("请填写类型名称");
        }
        long exists = this.count(new LambdaQueryWrapper<RepairType>()
                .eq(RepairType::getName, type.getName())
                .ne(type.getId() != null, RepairType::getId, type.getId()));
        if (exists > 0) {
            throw new BizException("该报修类型已存在");
        }
        if (type.getId() == null) {
            this.save(type);
        } else {
            this.updateById(type);
        }
    }

    @Override
    public void removeType(Long id) {
        RoleUtil.requireAdmin();
        long used = repairOrderMapper.selectCount(new LambdaQueryWrapper<RepairOrder>()
                .eq(RepairOrder::getTypeId, id));
        if (used > 0) {
            throw new BizException("该类型下已有 " + used + " 条工单，不能删除");
        }
        this.removeById(id);
    }
}
