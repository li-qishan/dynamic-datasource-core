package com.johns.dynamicdatasource.controller;

import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.constants.ExceptionConstants;
import com.johns.dynamicdatasource.exception.BusinessParamCheckingException;
import com.johns.dynamicdatasource.service.serialNumber.SerialNumberService;
import com.johns.dynamicdatasource.utils.ErpInfo;
import com.johns.dynamicdatasource.utils.ResponseJsonUtil;
import com.johns.dynamicdatasource.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Description
 *
 * @Author: cjl
 * @Date: 2019/1/22 10:29
 */
@RestController
public class SerialNumberController {
    private Logger logger = LoggerFactory.getLogger(SerialNumberController.class);

    @Resource
    private SerialNumberService serialNumberService;
    /**
     * create by: cjl
     * description:
     *  检查序列号是否存在
     * create time: 2019/1/22 11:02
     * @Param: id
     * @Param: materialName
     * @Param: serialNumber
     * @Param: request
     * @return java.lang.Object
     */
    @PostMapping("/serialNumber/checkIsExist")
    @ResponseBody
    public Object checkIsExist(@RequestParam("id") Long id, @RequestParam("materialName") String materialName,
                               @RequestParam("serialNumber") String serialNumber, HttpServletRequest request) throws Exception{
        JSONObject result = ExceptionConstants.standardSuccess();
        if(StringUtil.isEmpty(serialNumber)){
            throw new BusinessParamCheckingException(ExceptionConstants.SERIAL_NUMBERE_NOT_BE_EMPTY_CODE,
                    ExceptionConstants.SERIAL_NUMBERE_NOT_BE_EMPTY_MSG);
        }
        serialNumberService.checkIsExist(id, materialName, serialNumber);
        return result;
    }

    /**
     * create by: cjl
     * description:
     *批量添加序列号
     * create time: 2019/1/29 15:11
     * @Param: materialName
     * @Param: serialNumberPrefix
     * @Param: batAddTotal
     * @Param: remark
     * @return java.lang.Object
     */
    @PostMapping("/serialNumber/batAddSerialNumber")
    @ResponseBody
    public String batAddSerialNumber(@RequestBody JSONObject jsonObject, HttpServletRequest request)throws Exception{
        Map<String, Object> objectMap = new HashMap<>();
        String materialCode = jsonObject.getString("materialCode");
        String serialNumberPrefix = jsonObject.getString("serialNumberPrefix");
        Integer batAddTotal = jsonObject.getInteger("batAddTotal");
        String remark = jsonObject.getString("remark");
        int insert = serialNumberService.batAddSerialNumber(materialCode,serialNumberPrefix,batAddTotal,remark);
        if(insert > 0) {
            return ResponseJsonUtil.returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else if(insert == -1) {
            return ResponseJsonUtil.returnJson(objectMap, ErpInfo.TEST_USER.name, ErpInfo.TEST_USER.code);
        } else {
            return ResponseJsonUtil.returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }
}
