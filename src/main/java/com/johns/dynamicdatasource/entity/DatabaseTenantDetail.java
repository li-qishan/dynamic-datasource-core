package com.johns.dynamicdatasource.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class DatabaseTenantDetail {

    private String tenantId;

    private String code;

    private String name;

    private String username;

    private String password;

    private String url;

    private String driverClassName;
}
