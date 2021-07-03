package com.johns.dynamicdatasource.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class Demo {

    private String id;
    private String name;
    private String key_word;
    private Date punch_time;
    private BigDecimal salary_money;
    private Double bonus_money;
    private String sex;
    private int age;
    private Date birthday;
    private String email;
    private String content;
    private String create_by;
    private Date create_time;
    private String update_by;
    private Date update_time;

}
