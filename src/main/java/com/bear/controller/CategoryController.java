package com.bear.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bear.common.Result;
import com.bear.entity.Category;
import com.bear.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody Category category){
        log.info("Category: {}", category);
        categoryService.save(category);
        return Result.success("Add Category successfully");
    }

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize){
        // create page constructor
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //query wrapper
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    @DeleteMapping
    public Result<String> delete(Long id){
        log.info("deleted id:{}",id);
        //categoryService.removeById(id);
        categoryService.remove(id);
        return Result.success("delete successful");
    }

    @PutMapping
    public Result<String> update(@RequestBody Category category){
        log.info("edit category:{}",category);
        categoryService.updateById(category);

        return Result.success("category edit successfully");
    }

    @GetMapping("/list")
    public Result<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper= new LambdaQueryWrapper<>();
        //add condition of type (match getRequest)
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //add condition of sort
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }

}
