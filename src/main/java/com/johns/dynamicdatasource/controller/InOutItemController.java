package com.johns.dynamicdatasource.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.datasource.entities.InOutItem;
import com.johns.dynamicdatasource.service.inOutItem.InOutItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jishenghua  jshERP 2018年12月25日14:38:08
 */
@RestController
@RequestMapping(value = "/inOutItem")
public class InOutItemController {
    private Logger logger = LoggerFactory.getLogger(InOutItemController.class);

    @Resource
    private InOutItemService inOutItemService;

    /**
     * 查找收支项目信息-下拉框
     * @param request
     * @return
     */
    @GetMapping(value = "/findBySelect")
    public String findBySelect(@RequestParam("type") String type, HttpServletRequest request) throws Exception{
        String res = null;
        try {
            List<InOutItem> dataList = inOutItemService.findBySelect(type);
            //存放数据json数组
            JSONArray dataArray = new JSONArray();
            if (null != dataList) {
                for (InOutItem inOutItem : dataList) {
                    JSONObject item = new JSONObject();
                    item.put("id", inOutItem.getId());
                    //收支项目名称
                    item.put("name", inOutItem.getName());
                    dataArray.add(item);
                }
            }
            res = dataArray.toJSONString();
        } catch(Exception e){
            e.printStackTrace();
            res = "获取数据失败";
        }
        return res;
    }
}
