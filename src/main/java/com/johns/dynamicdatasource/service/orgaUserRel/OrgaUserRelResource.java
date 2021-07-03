package com.johns.dynamicdatasource.service.orgaUserRel;

import com.johns.dynamicdatasource.service.ResourceInfo;

import java.lang.annotation.*;

/**
 * Description
 *  机构用户关系
 * @Author: cjl
 * @Date: 2019/3/11 18:11
 */
@ResourceInfo(value = "orgaUserRel")
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrgaUserRelResource {

}
