package com.johns.dynamicdatasource.service;

import com.johns.dynamicdatasource.entity.User;
import com.johns.dynamicdatasource.datasource.mappers.User1Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 用户服务
 *
 * @author johns-li
 * @date 2021/06/23
 */
@Service
public class User1Service {

    @Resource
    private User1Mapper user1Mapper;

    @Transactional(rollbackFor = Exception.class)
    public List<User> selectAll() {
        return user1Mapper.selectAll();
    }

//    @Transactional(rollbackFor = Exception.class)
//    public int insert(User user) throws Exception {
//        int flag = userMapper.insert(user);
//        return flag;
//    }
}
