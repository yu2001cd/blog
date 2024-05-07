package com.minzheng.blog.exception;



/**
 * 自定义异常类
 */
public class ServeException extends RuntimeException {
    public ServeException(String message) {
        super(message);
    }

}
