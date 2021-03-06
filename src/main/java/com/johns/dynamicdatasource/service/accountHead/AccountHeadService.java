package com.johns.dynamicdatasource.service.accountHead;

import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.constants.BusinessConstants;
import com.johns.dynamicdatasource.datasource.entities.AccountHead;
import com.johns.dynamicdatasource.datasource.entities.AccountHeadExample;
import com.johns.dynamicdatasource.datasource.entities.AccountHeadVo4ListEx;
import com.johns.dynamicdatasource.datasource.entities.User;
import com.johns.dynamicdatasource.datasource.mappers.AccountHeadMapper;
import com.johns.dynamicdatasource.datasource.mappers.AccountHeadMapperEx;
import com.johns.dynamicdatasource.datasource.mappers.AccountItemMapperEx;
import com.johns.dynamicdatasource.exception.JshException;
import com.johns.dynamicdatasource.service.accountItem.AccountItemService;
import com.johns.dynamicdatasource.service.log.LogService;
import com.johns.dynamicdatasource.service.orgaUserRel.OrgaUserRelService;
import com.johns.dynamicdatasource.service.user.UserService;
import com.johns.dynamicdatasource.utils.StringUtil;
import com.johns.dynamicdatasource.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountHeadService {
    private Logger logger = LoggerFactory.getLogger(AccountHeadService.class);
    @Resource
    private AccountHeadMapper accountHeadMapper;
    @Resource
    private AccountHeadMapperEx accountHeadMapperEx;
    @Resource
    private OrgaUserRelService orgaUserRelService;
    @Resource
    private AccountItemService accountItemService;
    @Resource
    private UserService userService;
    @Resource
    private LogService logService;
    @Resource
    private AccountItemMapperEx accountItemMapperEx;

    public AccountHead getAccountHead(long id) throws Exception {
        AccountHead result=null;
        try{
            result=accountHeadMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<AccountHead> getAccountHeadListByIds(String ids)throws Exception {
        List<Long> idList = StringUtil.strToLongList(ids);
        List<AccountHead> list = new ArrayList<>();
        try{
            AccountHeadExample example = new AccountHeadExample();
            example.createCriteria().andIdIn(idList);
            list = accountHeadMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<AccountHead> getAccountHead() throws Exception{
        AccountHeadExample example = new AccountHeadExample();
        List<AccountHead> list=null;
        try{
            list=accountHeadMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<AccountHeadVo4ListEx> select(String type, String roleType, String billNo, String beginTime, String endTime, int offset, int rows) throws Exception{
        List<AccountHeadVo4ListEx> resList = new ArrayList<AccountHeadVo4ListEx>();
        List<AccountHeadVo4ListEx> list=null;
        try{
            String [] creatorArray = getCreatorArray(roleType);
            beginTime = Tools.parseDayToTime(beginTime, BusinessConstants.DAY_FIRST_TIME);
            endTime = Tools.parseDayToTime(endTime,BusinessConstants.DAY_LAST_TIME);
            list = accountHeadMapperEx.selectByConditionAccountHead(type, creatorArray, billNo, beginTime, endTime, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if (null != list) {
            for (AccountHeadVo4ListEx ah : list) {
                if(ah.getChangeAmount() != null) {
                    ah.setChangeAmount(ah.getChangeAmount().abs());
                }
                if(ah.getTotalPrice() != null) {
                    ah.setTotalPrice(ah.getTotalPrice().abs());
                }
                ah.setBillTimeStr(Tools.getCenternTime(ah.getBillTime()));
                resList.add(ah);
            }
        }
        return resList;
    }

    public Long countAccountHead(String type, String roleType, String billNo, String beginTime, String endTime) throws Exception{
        Long result=null;
        try{
            String [] creatorArray = getCreatorArray(roleType);
            beginTime = Tools.parseDayToTime(beginTime,BusinessConstants.DAY_FIRST_TIME);
            endTime = Tools.parseDayToTime(endTime,BusinessConstants.DAY_LAST_TIME);
            result = accountHeadMapperEx.countsByAccountHead(type, creatorArray, billNo, beginTime, endTime);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    /**
     * ???????????????????????????????????????
     * @param roleType
     * @return
     * @throws Exception
     */
    private String[] getCreatorArray(String roleType) throws Exception {
        String creator = "";
        User user = userService.getCurrentUser();
        if(BusinessConstants.ROLE_TYPE_PRIVATE.equals(roleType)) {
            creator = user.getId().toString();
        } else if(BusinessConstants.ROLE_TYPE_THIS_ORG.equals(roleType)) {
            creator = orgaUserRelService.getUserIdListByUserId(user.getId());
        }
        String [] creatorArray=null;
        if(StringUtil.isNotEmpty(creator)){
            creatorArray = creator.split(",");
        }
        return creatorArray;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertAccountHead(JSONObject obj, HttpServletRequest request) throws Exception{
        AccountHead accountHead = JSONObject.parseObject(obj.toJSONString(), AccountHead.class);
        int result=0;
        try{
            User userInfo=userService.getCurrentUser();
            accountHead.setCreator(userInfo==null?null:userInfo.getId());
            result = accountHeadMapper.insertSelective(accountHead);
            logService.insertLog("??????",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(accountHead.getBillNo()).toString(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateAccountHead(JSONObject obj, HttpServletRequest request)throws Exception {
        AccountHead accountHead = JSONObject.parseObject(obj.toJSONString(), AccountHead.class);
        int result=0;
        try{
            result = accountHeadMapper.updateByPrimaryKeySelective(accountHead);
            logService.insertLog("??????",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(accountHead.getBillNo()).toString(), request);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteAccountHead(Long id, HttpServletRequest request)throws Exception {
        return batchDeleteAccountHeadByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteAccountHead(String ids, HttpServletRequest request)throws Exception {
        return batchDeleteAccountHeadByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteAccountHeadByIds(String ids)throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        List<AccountHead> list = getAccountHeadListByIds(ids);
        for(AccountHead accountHead: list){
            sb.append("[").append(accountHead.getBillNo()).append("]");
        }
        logService.insertLog("??????", sb.toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User userInfo=userService.getCurrentUser();
        String [] idArray=ids.split(",");
        int result = 0;
        try{
            //????????????
            result = accountItemMapperEx.batchDeleteAccountItemByHeadIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
            //????????????
            result = accountHeadMapperEx.batchDeleteAccountHeadByIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
        }catch(Exception e){
            JshException.writeFail(logger, e);
        }
        return result;
    }

    public int checkIsNameExist(Long id, String name)throws Exception {
        AccountHeadExample example = new AccountHeadExample();
        example.createCriteria().andIdNotEqualTo(id).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<AccountHead> list = null;
        try{
            list = accountHeadMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    public void addAccountHeadAndDetail(String beanJson, String rows, HttpServletRequest request) throws Exception {
        AccountHead accountHead = JSONObject.parseObject(beanJson, AccountHead.class);
        User userInfo=userService.getCurrentUser();
        accountHead.setCreator(userInfo==null?null:userInfo.getId());
        accountHeadMapper.insertSelective(accountHead);
        //??????????????????????????????id
        AccountHeadExample dhExample = new AccountHeadExample();
        dhExample.createCriteria().andBillNoEqualTo(accountHead.getBillNo()).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<AccountHead> list = accountHeadMapper.selectByExample(dhExample);
        if(list!=null) {
            Long headId = list.get(0).getId();
            String type = list.get(0).getType();
            /**????????????????????????*/
            accountItemService.saveDetials(rows, headId, type, request);
        }
        logService.insertLog("????????????",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(accountHead.getBillNo()).toString(), request);
    }

    public void updateAccountHeadAndDetail(String beanJson, String rows, HttpServletRequest request) throws Exception {
        AccountHead accountHead = JSONObject.parseObject(beanJson, AccountHead.class);
        accountHeadMapper.updateByPrimaryKeySelective(accountHead);
        //??????????????????????????????id
        AccountHeadExample dhExample = new AccountHeadExample();
        dhExample.createCriteria().andBillNoEqualTo(accountHead.getBillNo()).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<AccountHead> list = accountHeadMapper.selectByExample(dhExample);
        if(list!=null) {
            Long headId = list.get(0).getId();
            String type = list.get(0).getType();
            /**????????????????????????*/
            accountItemService.saveDetials(rows, headId, type, request);
        }
        logService.insertLog("????????????",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(accountHead.getBillNo()).toString(), request);
    }

    public BigDecimal findAllMoney(Integer supplierId, String type, String mode, String endTime) {
        String modeName = "";
        if (mode.equals("??????")) {
            modeName = "change_amount";
        } else if (mode.equals("??????")) {
            modeName = "total_price";
        }
        BigDecimal result = null;
        try{
            result = accountHeadMapperEx.findAllMoney(supplierId, type, modeName, endTime);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    /**
     * ???????????????
     * @param getS
     * @param type
     * @param mode ??????????????????
     * @param endTime
     * @return
     */
    public BigDecimal allMoney(String getS, String type, String mode, String endTime) {
        BigDecimal allMoney = BigDecimal.ZERO;
        try {
            Integer supplierId = Integer.valueOf(getS);
            BigDecimal sum = findAllMoney(supplierId, type, mode, endTime);
            if(sum != null) {
                allMoney = sum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //??????????????????????????????????????????
        if ((allMoney.compareTo(BigDecimal.ZERO))==-1) {
            allMoney = allMoney.abs();
        }
        return allMoney;
    }

    /**
     * ????????????????????????????????????????????????????????????????????????
     * @param supplierId
     * @param endTime
     * @param supType
     * @return
     */
    public BigDecimal findTotalPay(Integer supplierId, String endTime, String supType) {
        BigDecimal sum = BigDecimal.ZERO;
        String getS = supplierId.toString();
        int i = 1;
        if (("customer").equals(supType)) { //??????
            i = 1;
        } else if (("vendor").equals(supType)) { //?????????
            i = -1;
        }
        //???????????????
        sum = sum.add((allMoney(getS, "??????", "??????",endTime).add(allMoney(getS, "??????", "??????",endTime))).multiply(new BigDecimal(i)));
        sum = sum.subtract((allMoney(getS, "??????", "??????",endTime).add(allMoney(getS, "??????", "??????",endTime))).multiply(new BigDecimal(i)));
        sum = sum.add((allMoney(getS, "??????", "??????",endTime).subtract(allMoney(getS, "??????", "??????",endTime))).multiply(new BigDecimal(i)));
        sum = sum.subtract((allMoney(getS, "??????", "??????",endTime).subtract(allMoney(getS, "??????", "??????",endTime))).multiply(new BigDecimal(i)));
        return sum;
    }

    public List<AccountHeadVo4ListEx> getDetailByNumber(String billNo)throws Exception {
        List<AccountHeadVo4ListEx> resList = new ArrayList<AccountHeadVo4ListEx>();
        List<AccountHeadVo4ListEx> list = null;
        try{
            list = accountHeadMapperEx.getDetailByNumber(billNo);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if (null != list) {
            for (AccountHeadVo4ListEx ah : list) {
                if(ah.getChangeAmount() != null) {
                    ah.setChangeAmount(ah.getChangeAmount().abs());
                }
                if(ah.getTotalPrice() != null) {
                    ah.setTotalPrice(ah.getTotalPrice().abs());
                }
                ah.setBillTimeStr(Tools.getCenternTime(ah.getBillTime()));
                resList.add(ah);
            }
        }
        return resList;
    }
}
