package com.job.tracker.system.entity.expression.bean;

import com.system.util.string.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanUtils {

    public static void setBeanFieldValue(Object bean, String fieldName, Object value) {
        Field beanField = ReflectionUtils.findField(bean.getClass(), fieldName);
        beanField.setAccessible(true);
        ReflectionUtils.setField(beanField, bean, value);
    }

    public static void setBeanFieldValueViaMethod(Object bean, String fieldName, Object value) {
        Method beanField = ReflectionUtils.findMethod(bean.getClass(), "set" + StringUtils.capitalize(fieldName));
        try {
            beanField.setAccessible(true);
            beanField.invoke(bean, value);
        } catch (Exception e) {
            e.printStackTrace();
            setBeanFieldValue(bean, fieldName, value);
        }
    }

}
