package com.bear.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result<T> {
    private Integer code;  // 1:success 2:fail
    private String msg;
    private T data; //main data
    private Map map = new HashMap();

    public static <T> Result<T> success(T data){
        Result<T> r = new Result<>();
        r.code = 1;
        r.data = data;
        return r;
    }

    public static <T> Result<T> error(String msg){
        Result<T> r = new Result<>();
        r.msg = msg;
        r.code = 2;
        return r;
    }


    public Result<T> add(String msg, String value){
        this.map.put(msg, value);
        return this;
    }


}
