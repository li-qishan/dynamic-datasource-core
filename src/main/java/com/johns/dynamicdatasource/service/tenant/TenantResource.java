package com.johns.dynamicdatasource.service.tenant;

import com.johns.dynamicdatasource.service.ResourceInfo;

import java.lang.annotation.*;

/**
 * @author jishenghua qq752718920  2019-6-27 22:56:56
 */
@ResourceInfo(value = "tenant")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantResource {
}
