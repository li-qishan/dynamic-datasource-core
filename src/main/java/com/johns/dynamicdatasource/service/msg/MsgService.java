package com.johns.dynamicdatasource.service.msg;

import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.constants.BusinessConstants;
import com.johns.dynamicdatasource.constants.ExceptionConstants;
import com.johns.dynamicdatasource.datasource.entities.Msg;
import com.johns.dynamicdatasource.datasource.entities.MsgExample;
import com.johns.dynamicdatasource.datasource.entities.User;
import com.johns.dynamicdatasource.datasource.mappers.MsgMapper;
import com.johns.dynamicdatasource.datasource.mappers.MsgMapperEx;
import com.johns.dynamicdatasource.exception.BusinessRunTimeException;
import com.johns.dynamicdatasource.service.depotHead.DepotHeadService;
import com.johns.dynamicdatasource.service.log.LogService;
import com.johns.dynamicdatasource.service.user.UserService;
import com.johns.dynamicdatasource.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class MsgService {
    private Logger logger = LoggerFactory.getLogger(MsgService.class);
    @Resource
    private MsgMapper msgMapper;

    @Resource
    private MsgMapperEx msgMapperEx;

    @Resource
    private DepotHeadService depotHeadService;

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    public Msg getMsg(long id)throws Exception {
        Msg result=null;
        try{
            result=msgMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE, ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return result;
    }

    public List<Msg> getMsg()throws Exception {
        MsgExample example = new MsgExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Msg> list=null;
        try{
            list=msgMapper.selectByExample(example);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE, ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return list;
    }

    public List<Msg> select(String name, int offset, int rows)throws Exception {
        List<Msg> list=null;
        try{
            list=msgMapperEx.selectByConditionMsg(name, offset, rows);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE, ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return list;
    }

    public Long countMsg(String name)throws Exception {
        Long result=null;
        try{
            result=msgMapperEx.countsByMsg(name);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE, ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertMsg(JSONObject obj, HttpServletRequest request)throws Exception {
        Msg msg = JSONObject.parseObject(obj.toJSONString(), Msg.class);
        int result=0;
        try{
            result=msgMapper.insertSelective(msg);
            logService.insertLog("消息",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(msg.getMsgTitle()).toString(), request);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE, ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateMsg(JSONObject obj, HttpServletRequest request) throws Exception{
        Msg msg = JSONObject.parseObject(obj.toJSONString(), Msg.class);
        int result=0;
        try{
            result=msgMapper.updateByPrimaryKeySelective(msg);
            logService.insertLog("消息",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(msg.getMsgTitle()).toString(), request);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE, ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteMsg(Long id, HttpServletRequest request)throws Exception {
        int result=0;
        try{
            result=msgMapper.deleteByPrimaryKey(id);
            logService.insertLog("消息",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(id).toString(), request);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE, ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMsg(String ids, HttpServletRequest request) throws Exception{
        List<Long> idList = StringUtil.strToLongList(ids);
        MsgExample example = new MsgExample();
        example.createCriteria().andIdIn(idList);
        int result=0;
        try{
            result=msgMapper.deleteByExample(example);
            logService.insertLog("消息", "批量删除,id集:" + ids, request);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE, ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name)throws Exception {
        MsgExample example = new MsgExample();
        example.createCriteria().andIdNotEqualTo(id).andMsgTitleEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Msg> list=null;
        try{
            list= msgMapper.selectByExample(example);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE, ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return list==null?0:list.size();
    }

    /**
     * create by: qiankunpingtai
     *  逻辑删除角色信息
     * create time: 2019/3/28 15:44
     * @Param: ids
     * @return int
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMsgByIds(String ids) throws Exception{
        logService.insertLog("序列号",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_DELETE).append(ids).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        String [] idArray=ids.split(",");
        int result=0;
        try{
            result=msgMapperEx.batchDeleteMsgByIds(idArray);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE, ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
        return result;
    }

    public List<Msg> getMsgByStatus(String status)throws Exception {
        MsgExample example = new MsgExample();
        example.createCriteria().andStatusEqualTo(status).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Msg> list=null;
        try{
            list=msgMapper.selectByExample(example);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE, ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return list;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void batchUpdateStatus(String ids, String status) throws Exception{
        List<Long> idList = StringUtil.strToLongList(ids);
        Msg msg = new Msg();
        msg.setStatus(status);
        MsgExample example = new MsgExample();
        example.createCriteria().andIdIn(idList);
        try{
            msgMapper.updateByExampleSelective(msg, example);
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_WRITE_FAIL_CODE, ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                    ExceptionConstants.DATA_WRITE_FAIL_MSG);
        }
    }

    public Long getMsgCountByStatus(String status)throws Exception {
        Long result=null;
        try{
            User userInfo=userService.getCurrentUser();
            result=msgMapperEx.getMsgCountByStatus(status, userInfo.getId());
        }catch(Exception e){
            logger.error("异常码[{}],异常提示[{}],异常[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE, ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return result;
    }
}
