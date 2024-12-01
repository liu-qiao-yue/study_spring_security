package com.example.style_spring_security.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.style_spring_security.domain.SecurityHelper.getUserInfo;

/**
 * 自动填充公共字段
 * @author ellie
 */
@Slf4j
@Component
public class AutoFillerMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入操作自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]");
        Long id = getUserInfo().getUser().getId();
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", id);
        metaObject.setValue("updateUser", id);
    }

    /**
     * 更新操作自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", getUserInfo().getUser().getId());
    }
}
