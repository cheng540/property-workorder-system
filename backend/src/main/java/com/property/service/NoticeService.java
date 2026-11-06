package com.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.entity.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {

    List<Notice> listLatest(int limit);
}
