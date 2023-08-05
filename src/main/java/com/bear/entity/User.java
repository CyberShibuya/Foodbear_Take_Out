package com.bear.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    private String name;


    private String email;

    //0: woman 1: man
    private String sex;

    private String idNumber;

    private String avatar;


    //0:disabled
    private Integer status;
}
