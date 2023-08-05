package com.bear.common;

public class CustomException extends RuntimeException{//as not runtime exception, so not use it directly
    public CustomException(String message){
        super(message);
    }
}
