package com.johns.dynamicdatasource.datasource.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.johns.dynamicdatasource.entity.DatabaseDetail;
import com.johns.dynamicdatasource.entity.DatabaseTenantDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface DatabaseTenantDetailMapper  extends BaseMapper<DatabaseTenantDetail> {

    @Select("SELECT ct.id AS tenantId,ct.`code`,cdc.username,cdc.`password`,cdc.url,cdc.driver_class_name AS driverClassName FROM c_tenant ct LEFT JOIN c_tenant_datasource_config ctdc ON ct.id=ctdc.tenant_id LEFT JOIN c_datasource_config cdc ON cdc.id=ctdc.datasource_config_id WHERE ct.`code`= #{tenantId} limit 1 ")
    DatabaseTenantDetail selectOneTenantByTenantId(String tenantId);

    @Select("SELECT ct.`code`,cdc.username,cdc.`password`,cdc.url,cdc.driver_class_name AS driverClassName FROM c_tenant ct LEFT JOIN c_tenant_datasource_config ctdc ON ct.id=ctdc.tenant_id LEFT JOIN c_datasource_config cdc ON cdc.id=ctdc.datasource_config_id WHERE ct.id =  #{id} limit 1 ")
    DatabaseTenantDetail getTenantCodeByTenantId(@Param("id") Long id);
}
