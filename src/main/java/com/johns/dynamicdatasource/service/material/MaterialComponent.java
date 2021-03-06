package com.johns.dynamicdatasource.service.material;

import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.service.ICommonQuery;
import com.johns.dynamicdatasource.utils.Constants;
import com.johns.dynamicdatasource.utils.QueryUtils;
import com.johns.dynamicdatasource.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "material_component")
@MaterialResource
public class MaterialComponent implements ICommonQuery {

    @Resource
    private MaterialService materialService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return materialService.getMaterial(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getMaterialList(map);
    }

    private List<?> getMaterialList(Map<String, String> map) throws Exception{
        String search = map.get(Constants.SEARCH);
        String categoryId = StringUtil.getInfo(search, "categoryId");
        String barCode = StringUtil.getInfo(search, "barCode");
        String name = StringUtil.getInfo(search, "name");
        String standard = StringUtil.getInfo(search, "standard");
        String model = StringUtil.getInfo(search, "model");
        String mpList = StringUtil.getInfo(search, "mpList");
        return materialService.select(barCode, name, standard, model,categoryId,mpList, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String categoryId = StringUtil.getInfo(search, "categoryId");
        String barCode = StringUtil.getInfo(search, "barCode");
        String name = StringUtil.getInfo(search, "name");
        String standard = StringUtil.getInfo(search, "standard");
        String model = StringUtil.getInfo(search, "model");
        String mpList = StringUtil.getInfo(search, "mpList");
        return materialService.countMaterial(barCode, name, standard, model,categoryId,mpList);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception{
        return materialService.insertMaterial(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return materialService.updateMaterial(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return materialService.deleteMaterial(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return materialService.batchDeleteMaterial(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return materialService.checkIsNameExist(id, name);
    }

}
