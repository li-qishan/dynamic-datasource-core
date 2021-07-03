package com.johns.dynamicdatasource.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * spring上下文持有者
 *
 * @author johns-li
 * @date 2021/06/23
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext APP_CONTEXT = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APP_CONTEXT = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return APP_CONTEXT.getBean(requiredType);
    }
}
