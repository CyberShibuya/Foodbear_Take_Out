package com.bear.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bear.entity.DishFlavor;
import com.bear.mapper.DishFlavorMapper;
import com.bear.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
