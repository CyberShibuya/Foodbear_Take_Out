package com.bear.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long categoryId;

    private String name;

    private BigDecimal price;

    //0: not sale 1: on sale
    private Integer status;

    private String code;

    private String description;

    private String image;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
