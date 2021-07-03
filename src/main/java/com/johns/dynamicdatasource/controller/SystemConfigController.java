package com.johns.dynamicdatasource.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.datasource.entities.Depot;
import com.johns.dynamicdatasource.datasource.entities.SystemConfig;
import com.johns.dynamicdatasource.service.depot.DepotService;
import com.johns.dynamicdatasource.service.systemConfig.SystemConfigService;
import com.johns.dynamicdatasource.service.user.UserService;
import com.johns.dynamicdatasource.service.userBusiness.UserBusinessService;
import com.johns.dynamicdatasource.utils.BaseResponseInfo;
import com.johns.dynamicdatasource.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description
 * @Author: jishenghua
 * @Date: 2021-3-13 0:01
 */
@RestController
@RequestMapping(value = "/systemConfig")
public class SystemConfigController {
    private Logger logger = LoggerFactory.getLogger(SystemConfigController.class);

    @Resource
    private UserService userService;

    @Resource
    private DepotService depotService;

    @Resource
    private UserBusinessService userBusinessService;

    @Resource
    private SystemConfigService systemConfigService;

    @GetMapping(value = "/getDictItems/{dictCode}")
    public BaseResponseInfo getDictItems(@PathVariable String dictCode,
                                       HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            Long userId = userService.getUserId(request);
            JSONArray arr = new JSONArray();
            if(StringUtil.isNotEmpty(dictCode)) {
                if (dictCode.equals("depotDict")) {
                    List<Depot> dataList = depotService.findUserDepot();
                    //开始拼接json数据
                    if (null != dataList) {
                        boolean depotFlag = systemConfigService.getDepotFlag();
                        for (Depot depot : dataList) {
                            JSONObject item = new JSONObject();
                            //勾选判断1
                            Boolean flag = false;
                            String type = "UserDepot";
                            try {
                                flag = userBusinessService.checkIsUserBusinessExist(type, userId.toString(), "[" + depot.getId().toString() + "]");
                            } catch (DataAccessException e) {
                                logger.error(">>>>>>>>>>>>>>>>>查询用户对应的仓库：类型" + type + " KeyId为： " + userId + " 存在异常！");
                            }
                            if (!depotFlag || flag) {
                                item.put("value", depot.getId().toString());
                                item.put("text", depot.getName());
                                item.put("title", depot.getName());
                                arr.add(item);
                            }
                        }
                    }
                }
            }
            res.code = 200;
            res.data = arr;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 获取当前租户的配置信息
     * @param request
     * @return
     */
    @GetMapping(value = "/getCurrentInfo")
    public BaseResponseInfo getCurrentInfo(HttpServletRequest request) throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        try{
            List<SystemConfig> list = systemConfigService.getSystemConfig();
            res.code = 200;
            if(list.size()>0) {
                res.data = list.get(0);
            }
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }
}
