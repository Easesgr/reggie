package com.anyi.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author 安逸i
 * @version 1.0
 */

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public R<String> SQLIntegrityConstraintViolationException(Exception ex){
        ex.printStackTrace();
        String[] message = ex.getCause().getMessage().split(" ");
        String key = message[2];
        return R.error(key + "已经存在！");
    }
    @ExceptionHandler(value = Exception.class)
    public R<String> exceptionHandler(Exception ex){
        ex.printStackTrace();
        return R.error("服务器异常");
    }

}
