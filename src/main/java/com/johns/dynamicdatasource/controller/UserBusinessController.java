package com.johns.dynamicdatasource.controller;

import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.datasource.entities.UserBusiness;
import com.johns.dynamicdatasource.service.user.UserService;
import com.johns.dynamicdatasource.service.userBusiness.UserBusinessService;
import com.johns.dynamicdatasource.utils.BaseResponseInfo;
import com.johns.dynamicdatasource.utils.ErpInfo;
import com.johns.dynamicdatasource.utils.ResponseJsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ji_sheng_hua jshERP
 */
@RestController
@RequestMapping(value = "/userBusiness")
public class UserBusinessController {
    private Logger logger = LoggerFactory.getLogger(UserBusinessController.class);

    @Resource
    private UserBusinessService userBusinessService;
    @Resource
    private UserService userService;

    @GetMapping(value = "/getBasicData")
    public BaseResponseInfo getBasicData(@RequestParam(value = "KeyId") String keyId,
                                         @RequestParam(value = "Type") String type,
                                         HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            List<UserBusiness> list = userBusinessService.getBasicData(keyId, type);
            Map<String, List> mapData = new HashMap<String, List>();
            mapData.put("userBusinessList", list);
            res.code = 200;
            res.data = mapData;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "查询权限失败";
        }
        return res;
    }

    @GetMapping(value = "/checkIsValueExist")
    public String checkIsValueExist(@RequestParam(value ="type", required = false) String type,
                                   @RequestParam(value ="keyId", required = false) String keyId,
                                   HttpServletRequest request)throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        Long id = userBusinessService.checkIsValueExist(type, keyId);
        if(id != null) {
            objectMap.put("id", id);
        } else {
            objectMap.put("id", null);
        }
        return ResponseJsonUtil.returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }

    /**
     * 更新角色的按钮权限
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping(value = "/updateBtnStr")
    public BaseResponseInfo updateBtnStr(@RequestBody JSONObject jsonObject,
                                         HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            String roleId = jsonObject.getString("roleId");
            String btnStr = jsonObject.getString("btnStr");
            String keyId = roleId;
            String type = "RoleFunctions";
            int back = userBusinessService.updateBtnStr(keyId, type, btnStr);
            if(back > 0) {
                res.code = 200;
                res.data = "成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "更新权限失败";
        }
        return res;
    }
}
