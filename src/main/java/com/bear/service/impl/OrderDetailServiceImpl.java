package com.bear.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bear.entity.OrderDetail;
import com.bear.mapper.OrderDetailMapper;
import com.bear.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
