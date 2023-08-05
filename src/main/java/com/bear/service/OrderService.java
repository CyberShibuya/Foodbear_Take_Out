package com.bear.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bear.entity.Orders;


public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
