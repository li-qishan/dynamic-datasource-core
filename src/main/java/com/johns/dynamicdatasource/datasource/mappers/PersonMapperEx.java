package com.johns.dynamicdatasource.datasource.mappers;

import com.johns.dynamicdatasource.datasource.entities.Person;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonMapperEx {

    List<Person> selectByConditionPerson(
            @Param("name") String name,
            @Param("type") String type,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByPerson(
            @Param("name") String name,
            @Param("type") String type);

    int batchDeletePersonByIds(@Param("ids") String ids[]);
}