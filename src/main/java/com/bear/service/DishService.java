package com.bear.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bear.dto.DishDto;
import com.bear.entity.Dish;

public interface DishService extends IService<Dish> {
    //add dish with flavor
    void saveWithFlavor(DishDto dishDto);

    //get dish with flavor
    DishDto getWithFlavor(Long id);

    //update dish with flavor
    void updateWithFlavor(DishDto dishDto);
}
