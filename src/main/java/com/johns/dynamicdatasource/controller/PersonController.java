package com.johns.dynamicdatasource.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.datasource.entities.Person;
import com.johns.dynamicdatasource.service.person.PersonService;
import com.johns.dynamicdatasource.utils.BaseResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ji|sheng|hua 华夏erp
 */
@RestController
@RequestMapping(value = "/person")
public class PersonController {
    private Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Resource
    private PersonService personService;

    @GetMapping(value = "/getAllList")
    public BaseResponseInfo getAllList(HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Person> personList = personService.getPerson();
            map.put("personList", personList);
            res.code = 200;
            res.data = personList;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据Id获取经手人信息
     * @param personIds
     * @param request
     * @return
     */
    @GetMapping(value = "/getPersonByIds")
    public BaseResponseInfo getPersonByIds(@RequestParam("personIds") String personIds,
                                           HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Map<Long,String> personMap = personService.getPersonMap();
            String names = personService.getPersonByMapAndIds(personMap, personIds);
            map.put("names", names);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据类型获取经手人信息
     * @param type
     * @param request
     * @return
     */
    @GetMapping(value = "/getPersonByType")
    public BaseResponseInfo getPersonByType(@RequestParam("type") String type,
                                            HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Person> personList = personService.getPersonByType(type);
            map.put("personList", personList);
            res.code = 200;
            res.data = map;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.data = "获取数据失败";
        }
        return res;
    }

    /**
     * 根据类型获取经手人信息 1-业务员，2-仓管员，3-财务员
     * @param typeNum
     * @param request
     * @return
     */
    @GetMapping(value = "/getPersonByNumType")
    public JSONArray getPersonByNumType(@RequestParam("type") String typeNum,
                                        HttpServletRequest request)throws Exception {
        JSONArray dataArray = new JSONArray();
        try {
            String type = "";
            if (typeNum.equals("1")) {
                type = "业务员";
            } else if (typeNum.equals("2")) {
                type = "仓管员";
            } else if (typeNum.equals("3")) {
                type = "财务员";
            }
            List<Person> personList = personService.getPersonByType(type);
            if (null != personList) {
                for (Person person : personList) {
                    JSONObject item = new JSONObject();
                    item.put("value", person.getId().toString());
                    item.put("text", person.getName());
                    dataArray.add(item);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return dataArray;
    }
}
