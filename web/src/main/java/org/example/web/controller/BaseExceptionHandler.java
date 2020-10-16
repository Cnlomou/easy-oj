package org.example.web.controller;

import org.example.web.entity.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Cnlomou
 * @create 2020/8/8 22:47
 */

@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(Throwable throwable){
        return Result.excep(throwable.getMessage());
    }
}
