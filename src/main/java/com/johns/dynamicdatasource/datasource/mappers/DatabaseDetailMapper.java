package com.johns.dynamicdatasource.datasource.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.johns.dynamicdatasource.entity.DatabaseDetail;
import org.apache.ibatis.annotations.Select;


/**
 * 数据库细节映射器
 *
 * @author johns-li
 * @date 2021/06/23
 */
public interface DatabaseDetailMapper extends BaseMapper<DatabaseDetail> {

    @Select("select * from database_detail where tenant_id=#{tenantId} limit 1 ")
    DatabaseDetail selectOneByTenantId(String tenantId);
}
