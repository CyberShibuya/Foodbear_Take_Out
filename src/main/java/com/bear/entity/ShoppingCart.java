package com.bear.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long userId;

    private Long dishId;

    private Long setmealId;

    private String dishFlavor;

    private Integer number;
    //price
    private BigDecimal amount;

    private String image;

    //@TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //@TableField(fill = FieldFill.INSERT_UPDATE)
    //private LocalDateTime updateTime;
}
