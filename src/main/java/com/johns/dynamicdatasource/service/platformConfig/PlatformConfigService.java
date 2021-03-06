package com.johns.dynamicdatasource.service.platformConfig;

import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.datasource.entities.PlatformConfig;
import com.johns.dynamicdatasource.datasource.entities.PlatformConfigExample;
import com.johns.dynamicdatasource.datasource.mappers.PlatformConfigMapper;
import com.johns.dynamicdatasource.datasource.mappers.PlatformConfigMapperEx;
import com.johns.dynamicdatasource.exception.JshException;
import com.johns.dynamicdatasource.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class PlatformConfigService {
    private Logger logger = LoggerFactory.getLogger(PlatformConfigService.class);

    @Resource
    private PlatformConfigMapper platformConfigMapper;

    @Resource
    private PlatformConfigMapperEx platformConfigMapperEx;

    public PlatformConfig getPlatformConfig(long id)throws Exception {
        PlatformConfig result=null;
        try{
            result=platformConfigMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<PlatformConfig> getPlatformConfig()throws Exception {
        PlatformConfigExample example = new PlatformConfigExample();
        example.createCriteria();
        List<PlatformConfig> list=null;
        try{
            list=platformConfigMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<PlatformConfig> select(String key, int offset, int rows)throws Exception {
        List<PlatformConfig> list=null;
        try{
            list=platformConfigMapperEx.selectByConditionPlatformConfig(key, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countPlatformConfig(String key)throws Exception {
        Long result=null;
        try{
            result=platformConfigMapperEx.countsByPlatformConfig(key);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertSystemConfig(JSONObject obj, HttpServletRequest request) throws Exception{
        PlatformConfig platformConfig = JSONObject.parseObject(obj.toJSONString(), PlatformConfig.class);
        int result=0;
        try{
            result=platformConfigMapper.insertSelective(platformConfig);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateSystemConfig(JSONObject obj, HttpServletRequest request) throws Exception{
        PlatformConfig platformConfig = JSONObject.parseObject(obj.toJSONString(), PlatformConfig.class);
        int result=0;
        try{
            result = platformConfigMapper.updateByPrimaryKeySelective(platformConfig);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteSystemConfig(Long id, HttpServletRequest request)throws Exception {
        int result=0;
        try{
            result=platformConfigMapper.deleteByPrimaryKey(id);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteSystemConfig(String ids, HttpServletRequest request)throws Exception {
        List<Long> idList = StringUtil.strToLongList(ids);
        PlatformConfigExample example = new PlatformConfigExample();
        example.createCriteria().andIdIn(idList);
        int result=0;
        try{
            result=platformConfigMapper.deleteByExample(example);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int updatePlatformConfigByKey(String platformKey, String platformValue)throws Exception {
        int result=0;
        try{
            PlatformConfig platformConfig = new PlatformConfig();
            platformConfig.setPlatformValue(platformValue);
            PlatformConfigExample example = new PlatformConfigExample();
            example.createCriteria().andPlatformKeyEqualTo(platformKey);
            result = platformConfigMapper.updateByExampleSelective(platformConfig, example);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public PlatformConfig getPlatformConfigByKey(String platformKey)throws Exception {
        PlatformConfig platformConfig = new PlatformConfig();
        try{
            PlatformConfigExample example = new PlatformConfigExample();
            example.createCriteria().andPlatformKeyEqualTo(platformKey);
            List<PlatformConfig> list=platformConfigMapper.selectByExample(example);
            if(list!=null && list.size()>0){
                platformConfig = list.get(0);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return platformConfig;
    }
}
