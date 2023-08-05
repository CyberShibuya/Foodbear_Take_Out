package com.bear.common;

// a utility class encapsulating ThreadLocal, used to ensure and retrieve the current userID
public class BaseContext {
    //create static ThreadLocal with Long generics
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //set id
    public static void setCurrentID(Long id){
        threadLocal.set(id);
    }

    //get id
    public static Long getCurrentID(){
        return threadLocal.get();
    }
}
