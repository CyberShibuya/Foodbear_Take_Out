package com.bear.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bear.common.CustomException;
import com.bear.dto.SetmealDto;
import com.bear.entity.Setmeal;
import com.bear.entity.SetmealDish;
import com.bear.mapper.SetmealMapper;
import com.bear.service.SetmealDishService;
import com.bear.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //save set meal
        this.save(setmealDto);

        //save setMealDish, set setmealId into setmealDish from setmeal
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long setmealId = setmealDto.getId();
        for (SetmealDish setmealDish:setmealDishes){
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void deleteWithDish(List<Long> ids) {
        //check if status is not on sale
        //select * from setmeal where id in (ids) and status = 1
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1);
        Long count = this.count(queryWrapper);
        if(count > 0){
            throw new CustomException("at least one setmeal whitch is not on sale");
        }

        //normally delete setmeal
        this.removeBatchByIds(ids);

        //delete setmealDish using setmealId
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }
}
