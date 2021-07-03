package com.johns.dynamicdatasource.datasource.mappers;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.johns.dynamicdatasource.entity.Demo;
import com.johns.dynamicdatasource.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface DemoMapper extends BaseMapper<Demo> {

    @Select("select * from DEMO")
    List<Demo> selectAll();

}
