package com.bear.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bear.common.CustomException;
import com.bear.entity.Category;
import com.bear.entity.Dish;
import com.bear.entity.Setmeal;
import com.bear.mapper.CategoryMapper;
import com.bear.service.CategoryService;
import com.bear.service.DishService;
import com.bear.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setMealService;

    @Override
    public void remove(Long id) {

        //check if associate with dish, if yes, throw an exception
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        Long count_dish = dishService.count(dishLambdaQueryWrapper);

        if(count_dish>0){//associated
            throw new CustomException("associate with dish, cannot delete");
        }
        //check if associate with SetMeal, if yes, throw an exception
        LambdaQueryWrapper<Setmeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        Long count_setMeal = setMealService.count(setMealLambdaQueryWrapper);

        log.info(String.valueOf(count_setMeal));

        if(count_setMeal>0){//associated
            throw new CustomException("associate with setMeal, cannot delete");
        }
        //neither associates, then delete as normal(use IService method)
        super.removeById(id);
    }
}
