package com.johns.dynamicdatasource.config;

import com.johns.dynamicdatasource.datasource.mappers.DatabaseTenantDetailMapper;
import com.johns.dynamicdatasource.entity.DatabaseDetail;
import com.johns.dynamicdatasource.datasource.mappers.DatabaseDetailMapper;
import com.johns.dynamicdatasource.entity.DatabaseTenantDetail;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据来源
 *
 * @author johns-li
 * @date 2021/06/23
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 缓存当前线程数据源的key（租户id）
     */
    private static final ThreadLocal<String> CURRENT_DATASOURCE_KEY = new ThreadLocal<>();
    /**
     * 缓存租户对应的数据源
     * ConcurrentHashMap<租户id，数据源>
     */
    private ConcurrentHashMap<Object, Object> targetDataSources = new ConcurrentHashMap<>();

    private DatabaseDetailMapper databaseDetailMapper = null;

    private DatabaseTenantDetailMapper databaseTenantDetailMapper = null;

    public DynamicDataSource(DataSource defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
    }

    /**
     * 选择当前线程数据源的key
     */
    @Override
    public Object determineCurrentLookupKey() {
        return CURRENT_DATASOURCE_KEY.get();
    }

    /**
     * 清除当前线程数据源key
     */
    public static void clearCurrentDataSourceKey() {
        CURRENT_DATASOURCE_KEY.remove();
    }

    /**
     * 设置当前线程的数据源
     */
    public void setCurrentThreadDataSource(String dataSourceKey) {
        // 系统内包含非一次请求 租户ID 需进行转换操作
        if(dataSourceKey.getBytes(StandardCharsets.UTF_8).length> 10){
            if (null == databaseTenantDetailMapper) {
                getDatabaseDetailMapper();
            }
            dataSourceKey  = databaseTenantDetailMapper.getTenantCodeByTenantId(Long.valueOf(dataSourceKey)).getCode();
        }
        if (!targetDataSources.containsKey(dataSourceKey)) {
            addNewDataSource(dataSourceKey);
        }
        CURRENT_DATASOURCE_KEY.set(dataSourceKey);
    }

    private synchronized void addNewDataSource(String dataSourceKey) {
        if (targetDataSources.containsKey(dataSourceKey)) {
            return;
        }
        DataSource datasource = createDataSource(dataSourceKey);
        targetDataSources.put(dataSourceKey, datasource);
        super.afterPropertiesSet();
    }


    private DataSource createDataSource(String dataSourceKey) {
        DatabaseTenantDetail dbDetail = getDatabaseDetail(dataSourceKey);
        return DynamicDataSourceConfig.createDataSource(dbDetail);
    }

    // 数据库信息动态获取
    private DatabaseTenantDetail getDatabaseDetail(String dataSourceKey) {
        if (null == databaseTenantDetailMapper) {
            getDatabaseDetailMapper();
        }
        return databaseTenantDetailMapper.selectOneTenantByTenantId(dataSourceKey);
    }

    private synchronized void getDatabaseDetailMapper() {
//        if (null == databaseDetailMapper) {
//            databaseDetailMapper = SpringContextHolder.getBean(DatabaseDetailMapper.class);
//        }
        if (null == databaseTenantDetailMapper) {
            databaseTenantDetailMapper = SpringContextHolder.getBean(DatabaseTenantDetailMapper.class);
        }
    }


}
