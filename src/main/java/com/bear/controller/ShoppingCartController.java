package com.bear.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bear.common.BaseContext;
import com.bear.common.Result;
import com.bear.entity.ShoppingCart;
import com.bear.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("shoppingCart={}", shoppingCart);
        //save userId into shoppingCart
        shoppingCart.setUserId(BaseContext.getCurrentID());

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //check dish or setmeal, and
        if (shoppingCart.getDishId() != null){
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        //check if this dish or setmeal is in the shoppingcart
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
        if (shoppingCart1 != null){// already in shoppingcart, then num 1+
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number + 1);
            shoppingCartService.updateById(shoppingCart1);
        }else { //not exist, then create new one
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCart1 = shoppingCart;
        }
        return Result.success(shoppingCart1);
    }

    @PostMapping("/sub")
    public Result<ShoppingCart> subtract(@RequestBody ShoppingCart shoppingCart){

        //check dish or setmeal
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if (shoppingCart.getDishId() != null){
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
        Integer number = shoppingCart1.getNumber();
        shoppingCart1.setNumber(number -1);
        shoppingCartService.updateById(shoppingCart1);

        return Result.success(shoppingCart1);
    }


    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentID());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        return Result.success(shoppingCartList);
    }

    @DeleteMapping("/clean")
    public Result<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentID());
        shoppingCartService.remove(queryWrapper);

        return Result.success("delete successfully");
    }

}
