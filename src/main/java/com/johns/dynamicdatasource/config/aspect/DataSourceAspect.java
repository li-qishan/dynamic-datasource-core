package com.johns.dynamicdatasource.config.aspect;

import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.config.DynamicDataSource;
import com.johns.dynamicdatasource.config.read.HttpHelper;
import com.johns.dynamicdatasource.utils.Tools;
import lombok.SneakyThrows;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * 数据来源切面
 *
 * @author johns-li
 * @date 2021/06/23
 */
@Aspect
@Component
@Order(-1)
public class DataSourceAspect {

    @Autowired
    private DynamicDataSource dynamicDataSource;

    private static final String LOGIN_PATH = "/jshERP-boot/user/login";

    /**
     * 切换数据源应发生在事务之前，否则仍然会使用默认数据源，
     * 故数据源切换放在controller层（默认事务应放到service层）
     * //@Pointcut("@annotation(DynamicSwitchDataSource)")
     */
    //@Pointcut("@annotation(com.johns.dynamicdatasource.config.annotation.DynamicSwitchDataSource)")
    @Pointcut("execution(public * com.johns.dynamicdatasource.controller..*.*(..))")
    public void dataSourcePointCut() {
    }

    @Before("dataSourcePointCut()")
    public void beforeExecute() {
        String tenantId = getTenantIdFromSession();
        if (tenantId != null) {
            dynamicDataSource.setCurrentThreadDataSource(tenantId);
        }
    }

    @After("dataSourcePointCut()")
    public void afterExecute() {
        DynamicDataSource.clearCurrentDataSourceKey();
    }

    //TODO 从请求参数体 或 Headers 中获取 租客id
    @SneakyThrows
    private String getTenantIdFromSession() {
        String tenantId = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = attributes.getRequest();
        // 登录获取租户编码使用
        if (LOGIN_PATH.equals(httpServletRequest.getRequestURI())) {
            // 调用登录接口 /user/login   时从request body中获取租户信息
            tenantId = JSONObject.parseObject(HttpHelper.getBodyString(httpServletRequest)).getString("tenantCode");
        }
        else {
            //调用业务接口 /&&&/**** 时从header中获取租户信息 截取获取租户ID
            String token = httpServletRequest.getHeader("X-Access-Token");
            tenantId = new String(String.valueOf(Tools.getTenantIdByToken(token)));
        }
        return tenantId;
    }



}
