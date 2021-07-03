package com.johns.dynamicdatasource.controller;

import com.johns.dynamicdatasource.entity.User;
import com.johns.dynamicdatasource.service.User1Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 用户控制器
 *
 * @author johns-li
 * @date 2021/06/23
 */
@RestController
@Api(tags = "多租户管理端--用户模块")
@RequestMapping(value = "/user1")
public class User1Controller {

    @Autowired
    User1Service userService;

    @ApiOperation(value = "获取用户", notes = "获取各租户下所有用户")
    @GetMapping("/user")
    public List<User> getAll() {
        return userService.selectAll();
    }

//    @ApiOperation(value = "新增用户", notes = "新增用户")
//    @PostMapping("/user")
//    public int insertOne(@RequestBody User user) {
//        try {
//            return userService.insert(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
}
