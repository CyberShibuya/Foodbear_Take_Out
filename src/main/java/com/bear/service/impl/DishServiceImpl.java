package com.bear.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bear.dto.DishDto;
import com.bear.entity.Dish;
import com.bear.entity.DishFlavor;
import com.bear.mapper.DishMapper;
import com.bear.service.DishFlavorService;
import com.bear.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional //all database operation success or fail together
    public void saveWithFlavor(DishDto dishDto) {
        //save data into Dish(due to ServiceImpl<DishMapper, Dish>)
        this.save(dishDto);

        //get dishId for FlavorId
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        // add the single flavorID to all flavors
        //method1: flavors.forEach(); or
//        for (DishFlavor dishFlavor: flavors){
//            dishFlavor.setDishId(dishId);
//        }

        // method2: stream lambda
        flavors.stream().map((item) -> {//item is flavor
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //save data into DishFlavors(due to ServiceImpl<DishFlavorMapper, DishFlavor>)
        dishFlavorService.saveBatch(flavors); //saveBatch for multiple data(list)

    }

    @Override
    public DishDto getWithFlavor(Long id) {

        Dish dish = this.getById(id);

        //copy dish into a new dishDto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //get all flavors by id from dish_flavor table
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //update dish, not update()
        this.updateById(dishDto);

        //delete the flavors belonged to the dish ID
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //add new flavors (List<DishFlavor> has no DishId)
        List<DishFlavor> flavors = dishDto.getFlavors();
        Long dishId = dishDto.getId();
        for (DishFlavor dishFlavor: flavors){
            dishFlavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);
    }


}
