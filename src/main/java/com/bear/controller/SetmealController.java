package com.bear.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bear.common.Result;
import com.bear.dto.SetmealDto;
import com.bear.entity.Category;
import com.bear.entity.Setmeal;
import com.bear.service.CategoryService;
import com.bear.service.SetmealDishService;
import com.bear.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto){
        log.info("setmealDto:{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return Result.success("save setmeal successfully");
    }

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name){
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>(page, pageSize);

        //create query page for setmeal
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, queryWrapper);

        //copy setmealPage(except record) into idoPage and add categoryName(from Category) to page records
        BeanUtils.copyProperties(setmealPage, dtoPage, "records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> setmealDtoList = new ArrayList<>();
        for (Setmeal setmeal:records){
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            //get the categoryName from Category
            Category category = categoryService.getById(setmeal.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            setmealDtoList.add(setmealDto);
        }
        dtoPage.setRecords(setmealDtoList);
        return Result.success(dtoPage);
    }

    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids){//not "/{ids}" and @PathVariable
        setmealService.deleteWithDish(ids);
        return Result.success("delete successfully");
    }

    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, 1);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> setmealList= setmealService.list(queryWrapper);
        return Result.success(setmealList);
    }
}
