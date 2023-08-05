package com.bear.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bear.dto.SetmealDto;
import com.bear.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
    void deleteWithDish(List<Long> ids);
}
