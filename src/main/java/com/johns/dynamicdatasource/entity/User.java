package com.johns.dynamicdatasource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 用户
 *
 * @author johns-li
 * @date 2021/06/23
 */
@Data
@TableName("user")
@KeySequence("seq_user")
public class User {

    @TableId(value = "id",type = IdType.INPUT)
    private Long id;
    private String name;
    private String tenantId;
}
