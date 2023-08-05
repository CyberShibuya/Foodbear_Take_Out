package com.bear.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bear.common.BaseContext;
import com.bear.common.CustomException;
import com.bear.entity.*;
import com.bear.mapper.OrderMapper;
import com.bear.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService{

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public void submit(Orders orders) {
        //get shoppingCart lists by userId in threadLocal
        Long userId = BaseContext.getCurrentID();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(shoppingCartLambdaQueryWrapper);

        //get User
        User user = userService.getById(userId);

        //get Address
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null){
            throw new CustomException("addressBook is wrong");
        }

        //get orderNumber(snow algorithm)
        Long orderNumber = IdWorker.getId();

        //multithread safe
        AtomicInteger amount = new AtomicInteger(0);
        //calculate order amount based on orderDetail's flavor data
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderNumber);
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());

            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());

            return orderDetail;
        }).collect(Collectors.toList());

        //save order into database
        orders.setId(orderNumber);
        orders.setNumber(String.valueOf(orderNumber));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setEmail(addressBook.getEmail());
        orders.setAddress(
                (addressBook.getProvinceName()==null ? "" :addressBook.getProvinceName()) +
                (addressBook.getCityName()==null ? "" :addressBook.getCityName()) +
                (addressBook.getDistrictName() == null ? "":addressBook.getDistrictName())+
                (addressBook.getDetail() == null ? "":addressBook.getDetail())
        );
        orders.setAmount(new BigDecimal(amount.get()));

        this.save(orders);

        orderDetailService.saveBatch(orderDetails);
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
    }
}
