package com.anyi.reggie.config;

/**
 * @author 安逸i
 * @version 1.0
 */

import com.anyi.reggie.common.UserContext;
import com.anyi.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * @author 安逸i
 * @version 1.0
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("createUser",UserContext.getUserId(),metaObject);
        this.setFieldValByName("updateUser",UserContext.getUserId(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("updateUser", UserContext.getUserId(),metaObject );
    }
}