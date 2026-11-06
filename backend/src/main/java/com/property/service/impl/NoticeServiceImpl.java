package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.entity.Notice;
import com.property.mapper.NoticeMapper;
import com.property.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public List<Notice> listLatest(int limit) {
        return this.list(new LambdaQueryWrapper<Notice>()
                .orderByDesc(Notice::getId)
                .last("LIMIT " + limit));
    }
}
