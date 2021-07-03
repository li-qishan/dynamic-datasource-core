package com.johns.dynamicdatasource.datasource.mappers;

import com.johns.dynamicdatasource.datasource.entities.TenantEx;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TenantMapperEx {

    List<TenantEx> selectByConditionTenant(
            @Param("loginName") String loginName,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows);

    Long countsByTenant(
            @Param("loginName") String loginName);
}