package com.bear.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //orderNumber
    private String number;

    //1: Pending Payment 2:Pending Dispatch 3: Dispatched 4:Completed 5:Canceled
    private Integer status;

    private Long userId;

    private Long addressBookId;

    private LocalDateTime orderTime;

    private LocalDateTime checkoutTime;

    private Integer payMethod;

    private BigDecimal amount;

    private String remark;

    private String userName;

    private String email;

    private String address;

    private String consignee;
}
