package com.johns.dynamicdatasource.service.materialProperty;

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

@Service(value = "materialProperty_component")
@MaterialPropertyResource
public class MaterialPropertyComponent implements ICommonQuery {

    @Resource
    private MaterialPropertyService materialPropertyService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return materialPropertyService.getMaterialProperty(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getMaterialPropertyList(map);
    }

    private List<?> getMaterialPropertyList(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        String order = QueryUtils.order(map);
        return materialPropertyService.select(name, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String name = StringUtil.getInfo(search, "name");
        return materialPropertyService.countMaterialProperty(name);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request)throws Exception {
        return materialPropertyService.insertMaterialProperty(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return materialPropertyService.updateMaterialProperty(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return materialPropertyService.deleteMaterialProperty(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return materialPropertyService.batchDeleteMaterialProperty(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return materialPropertyService.checkIsNameExist(id, name);
    }

}
