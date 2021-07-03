package com.johns.dynamicdatasource.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.datasource.entities.Depot;
import com.johns.dynamicdatasource.datasource.entities.DepotEx;
import com.johns.dynamicdatasource.service.depot.DepotService;
import com.johns.dynamicdatasource.service.material.MaterialService;
import com.johns.dynamicdatasource.service.userBusiness.UserBusinessService;
import com.johns.dynamicdatasource.utils.BaseResponseInfo;
import com.johns.dynamicdatasource.utils.ErpInfo;
import com.johns.dynamicdatasource.utils.ResponseJsonUtil;
import com.johns.dynamicdatasource.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ji sheng hua 752*718*920
 */
@RestController
@RequestMapping(value = "/depot")
public class DepotController {
    private Logger logger = LoggerFactory.getLogger(DepotController.class);

    @Resource
    private DepotService depotService;

    @Resource
    private UserBusinessService userBusinessService;

    @Resource
    private MaterialService materialService;

    @GetMapping(value = "/getAllList")
    public BaseResponseInfo getAllList(HttpServletRequest request) throws Exception{
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            List<Depot> depotList = depotService.getAllList();
            res.code = 200;
            res.data = depotList;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 用户对应仓库显示
     * @param type
     * @param keyId
     * @param request
     * @return
     */
    @GetMapping(value = "/findUserDepot")
    public JSONArray findUserDepot(@RequestParam("UBType") String type, @RequestParam("UBKeyId") String keyId,
                                 HttpServletRequest request) throws Exception{
        JSONArray arr = new JSONArray();
        try {
            List<Depot> dataList = depotService.findUserDepot();
            //开始拼接json数据
            JSONObject outer = new JSONObject();
            outer.put("id", 0);
            outer.put("key", 0);
            outer.put("value", 0);
            outer.put("title", "仓库列表");
            outer.put("attributes", "仓库列表");
            //存放数据json数组
            JSONArray dataArray = new JSONArray();
            if (null != dataList) {
                for (Depot depot : dataList) {
                    JSONObject item = new JSONObject();
                    item.put("id", depot.getId());
                    item.put("key", depot.getId());
                    item.put("value", depot.getId());
                    item.put("title", depot.getName());
                    item.put("attributes", depot.getName());
                    //勾选判断1
                    Boolean flag = false;
                    try {
                        flag = userBusinessService.checkIsUserBusinessExist(type, keyId, "[" + depot.getId().toString() + "]");
                    } catch (Exception e) {
                        logger.error(">>>>>>>>>>>>>>>>>设置用户对应的仓库：类型" + type + " KeyId为： " + keyId + " 存在异常！");
                    }
                    if (flag == true) {
                        item.put("checked", true);
                    }
                    //结束
                    dataArray.add(item);
                }
            }
            outer.put("children", dataArray);
            arr.add(outer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    /**
     * 获取当前用户拥有权限的仓库列表
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findDepotByCurrentUser")
    public BaseResponseInfo findDepotByCurrentUser(HttpServletRequest request) throws Exception{
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            JSONArray arr = depotService.findDepotByCurrentUser();
            res.code = 200;
            res.data = arr;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    @PostMapping(value = "/updateIsDefault")
    public String updateIsDefault(@RequestBody JSONObject object,
                                       HttpServletRequest request) throws Exception{
        Long depotId = object.getLong("id");
        Map<String, Object> objectMap = new HashMap<>();
        int res = depotService.updateIsDefault(depotId);
        if(res > 0) {
            return ResponseJsonUtil.returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else {
            return ResponseJsonUtil.returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }

    @GetMapping(value = "/getAllListWithStock")
    public BaseResponseInfo getAllList(@RequestParam("mId") Long mId,
                                       HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            List<Depot> list = depotService.getAllList();
            List<DepotEx> depotList = new ArrayList<DepotEx>();
            for(Depot depot: list) {
                DepotEx de = new DepotEx();
                if(mId!=0) {
                    BigDecimal initStock = materialService.getInitStock(mId, depot.getId());
                    BigDecimal currentStock = materialService.getCurrentStock(mId, depot.getId());
                    de.setInitStock(initStock);
                    de.setCurrentStock(currentStock);
                } else {
                    de.setInitStock(BigDecimal.ZERO);
                    de.setCurrentStock(BigDecimal.ZERO);
                }
                de.setId(depot.getId());
                de.setName(depot.getName());
                depotList.add(de);
            }
            res.code = 200;
            res.data = depotList;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }
}
