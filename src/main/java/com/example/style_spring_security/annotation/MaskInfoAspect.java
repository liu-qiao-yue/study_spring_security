package com.example.style_spring_security.annotation;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.style_spring_security.common.R;
import com.example.style_spring_security.enums.MaskType;
import com.example.style_spring_security.exception.MaskException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 掩码切面，用于对返回的数据进行掩码处理，避免敏感信息泄露
 * {@code @Aspect} 定义切面
 * {@code @Component } 将切面注册为Spring bean
 * @author ellie
 */
@Aspect
@Component
public class MaskInfoAspect {

    /**
     * 定义切点
     */
    @Pointcut("execution(* com.itheima.reggie.controller..*.*(..))")
    public void controllerMethods() {
    }

    /**
     * 定义around通知
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("controllerMethods()")
    public Object maskFields(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        return maskData(result);
    }

    /**
     * 对返回的数据进行掩码处理
     * @param obj
     * @return
     */
    private Object maskData(Object obj) {
        if (obj == null) {
            return obj;
        }

        if (obj instanceof R) {
            R<?> r = (R<?>) obj;
            Object data = r.getData();
            maskData(data);
            try {
                Field dataField = r.getClass().getDeclaredField("data");
                dataField.setAccessible(true);//NOSONAR
                dataField.set(r, data);//NOSONAR
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new MaskException(e.getMessage());
            }
            return r;
        }

        if (obj instanceof Page) {
            return maskPage((Page<?>) obj);
        }

        if (obj instanceof Map) {
            return maskMap((Map<?, ?>) obj);
        } else if (obj instanceof Collection) {
            return maskCollection((Collection<?>) obj);
        } else if (obj.getClass().isArray()) {
            return maskArray(obj);
        } else {
            maskFields(obj);
            return obj;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Page<T> maskPage(Page<T> page) {
        List<T> records = page.getRecords();
        if (records != null) {
            List<T> maskedRecords = new ArrayList<>(records.size());
            for (T data : records) {
                maskedRecords.add((T) maskData(data));
            }
            page.setRecords(maskedRecords);
        }
        return page;
    }

    private void maskFields(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(MaskInfo.class)) {
                // 设置私有字段可访问
                field.setAccessible(true);//NOSONAR
                try {
                    Object value = field.get(obj);
                    if (value instanceof String) {
                        MaskInfo maskInfo = field.getAnnotation(MaskInfo.class);
                        String maskedValue = maskString((String) value, maskInfo.type());
                        field.set(obj, maskedValue);//NOSONAR
                    } else if (value != null && !isBasicTypeOrWrapper(value)) {
                        // 如果字段是一个对象，则递归处理 递归处理非字符串类型的值
                        maskData(value);
                    }
                } catch (IllegalAccessException e) {
                    throw new MaskException(e.getMessage());
                }
            }
        }
    }

    private boolean isBasicTypeOrWrapper(Object value) {
        return value instanceof Integer ||
                value instanceof Long ||
                value instanceof Short ||
                value instanceof Byte ||
                value instanceof Double ||
                value instanceof Float ||
                value instanceof Boolean ||
                value instanceof Character ||
                value instanceof String;
    }

    private Object maskMap(Map<?, ?> map) {
        Map<Object, Object> newMap = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            maskData(value);
            newMap.put(key, value);
        }
        return newMap;
    }

    private Object maskCollection(Collection<?> collection) {
        Collection<Object> newList = new ArrayList<>(collection.size());
        for (Object item : collection) {
            maskData(item);
            newList.add(item);
        }
        return newList;
    }

    private Object maskArray(Object array) {
        Object newArray = Array.newInstance(array.getClass().getComponentType(), Array.getLength(array));
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(array, i);
            maskData(item);
            Array.set(newArray, i, item);
        }
        return newArray;
    }

    /**
     * 对字符串进行掩码处理。
     * @param str
     * @param type
     * @return
     */
    private String maskString(String str, MaskType type) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return switch (type) {
            case FULL -> maskFull(str);
            case PARTIAL -> maskPartial(str);
            default ->
                // 不做处理
                    str;
        };
    }

    private String maskFull(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            sb.append('*');
        }
        return sb.toString();
    }

    private String maskPartial(String str) {
        int length = str.length();
        if (length <= 7) {
            // 字符串太短，无需掩码
            return str;
        }
        String frontPart = str.substring(0, 3);
        String backPart = str.substring(length - 4);
        StringBuilder middlePart = new StringBuilder(length - 7);
        for (int i = 0; i < length - 7; i++) {
            middlePart.append('*');
        }
        return frontPart + middlePart.toString() + backPart;
    }
}