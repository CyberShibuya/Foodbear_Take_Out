package com.bear.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bear.common.Result;
import com.bear.dto.DishDto;
import com.bear.entity.Category;
import com.bear.entity.Dish;
import com.bear.entity.DishFlavor;
import com.bear.service.CategoryService;
import com.bear.service.DishFlavorService;
import com.bear.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto){
        log.info("received data: {}", dishDto);

        dishService.saveWithFlavor(dishDto);
        return Result.success("add dish successfully");
    }

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //query condition for search name
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null, Dish::getName, name).orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, queryWrapper);

        //copy pageInfo to disDtoPage, and ignore records(dish class + category name)
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        // update records, loop for replacing categoryId with categoryName
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map((dish)->{
            DishDto dishDto = new DishDto();
            //copy dish to dishDto(DishDto extends Dish)
            BeanUtils.copyProperties(dish, dishDto);

            //use categoryId to get category, and then get its name
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDto.setCategoryName(category.getName());

            return dishDto;
        }).collect(Collectors.toList());

        //put new records to dishDtoPage
        dishDtoPage.setRecords(dishDtoList);
        return Result.success(dishDtoPage);
    }

    //query dish and return back to show in UI
    @GetMapping("/{id}")
    public Result<DishDto> get(@PathVariable Long id){
        log.info("id:{}",id);
        DishDto dishDto = dishService.getWithFlavor(id);
        log.info("dishDtoId:{}",dishDto.getId());
        return Result.success(dishDto);
    }

    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return Result.success("update dish with flavor successfully");
    }

    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish){// get mothed has no RequestBody, Spring automatically bind the categoryId query parameter value to the dish parameter's categoryId field.
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishes = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = dishes.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null){
                dishDto.setCategoryName(category.getName());
            }

            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());

        return Result.success(dishDtoList);
    }
}
