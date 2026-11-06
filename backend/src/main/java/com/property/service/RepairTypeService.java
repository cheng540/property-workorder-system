package com.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.entity.RepairType;

public interface RepairTypeService extends IService<RepairType> {

    void saveType(RepairType type);

    void removeType(Long id);
}
