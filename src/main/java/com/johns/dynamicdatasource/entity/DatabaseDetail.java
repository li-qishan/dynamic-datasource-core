package com.johns.dynamicdatasource.entity;

import lombok.Data;


/**
 * 数据库的细节
 *
 * @author johns-li
 * @date 2021/06/23
 */
@Data
public class DatabaseDetail {
    private long id;
    private String tenantId;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
