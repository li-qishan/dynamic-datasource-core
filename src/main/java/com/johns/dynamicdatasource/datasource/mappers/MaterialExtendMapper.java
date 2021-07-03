package com.johns.dynamicdatasource.datasource.mappers;

import com.johns.dynamicdatasource.datasource.entities.MaterialExtend;
import com.johns.dynamicdatasource.datasource.entities.MaterialExtendExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialExtendMapper {
    long countByExample(MaterialExtendExample example);

    int deleteByExample(MaterialExtendExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MaterialExtend record);

    int insertSelective(MaterialExtend record);

    List<MaterialExtend> selectByExample(MaterialExtendExample example);

    MaterialExtend selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MaterialExtend record, @Param("example") MaterialExtendExample example);

    int updateByExample(@Param("record") MaterialExtend record, @Param("example") MaterialExtendExample example);

    int updateByPrimaryKeySelective(MaterialExtend record);

    int updateByPrimaryKey(MaterialExtend record);
}