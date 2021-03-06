package com.johns.dynamicdatasource.service.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.johns.dynamicdatasource.constants.BusinessConstants;
import com.johns.dynamicdatasource.constants.ExceptionConstants;
import com.johns.dynamicdatasource.datasource.entities.*;
import com.johns.dynamicdatasource.datasource.mappers.*;
import com.johns.dynamicdatasource.exception.BusinessRunTimeException;
import com.johns.dynamicdatasource.exception.JshException;
import com.johns.dynamicdatasource.service.depot.DepotService;
import com.johns.dynamicdatasource.service.depotItem.DepotItemService;
import com.johns.dynamicdatasource.service.log.LogService;
import com.johns.dynamicdatasource.service.materialCategory.MaterialCategoryService;
import com.johns.dynamicdatasource.service.materialExtend.MaterialExtendService;
import com.johns.dynamicdatasource.service.redis.RedisService;
import com.johns.dynamicdatasource.service.unit.UnitService;
import com.johns.dynamicdatasource.service.user.UserService;
import com.johns.dynamicdatasource.utils.BaseResponseInfo;
import com.johns.dynamicdatasource.utils.ExcelUtils;
import com.johns.dynamicdatasource.utils.StringUtil;
import jxl.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
public class MaterialService {
    private Logger logger = LoggerFactory.getLogger(MaterialService.class);

    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private MaterialExtendMapper materialExtendMapper;
    @Resource
    private MaterialMapperEx materialMapperEx;
    @Resource
    private MaterialCategoryMapperEx materialCategoryMapperEx;
    @Resource
    private MaterialExtendMapperEx materialExtendMapperEx;
    @Resource
    private LogService logService;
    @Resource
    private UserService userService;
    @Resource
    private DepotItemMapperEx depotItemMapperEx;
    @Resource
    private DepotItemService depotItemService;
    @Resource
    private MaterialCategoryService materialCategoryService;
    @Resource
    private UnitService unitService;
    @Resource
    private MaterialInitialStockMapper materialInitialStockMapper;
    @Resource
    private MaterialCurrentStockMapper materialCurrentStockMapper;
    @Resource
    private DepotService depotService;
    @Resource
    private MaterialExtendService materialExtendService;
    @Resource
    private RedisService redisService;

    public Material getMaterial(long id)throws Exception {
        Material result=null;
        try{
            result=materialMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<Material> getMaterialListByIds(String ids)throws Exception {
        List<Long> idList = StringUtil.strToLongList(ids);
        List<Material> list = new ArrayList<>();
        try{
            MaterialExample example = new MaterialExample();
            example.createCriteria().andIdIn(idList);
            list = materialMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Material> getMaterial() throws Exception{
        MaterialExample example = new MaterialExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Material> list=null;
        try{
            list=materialMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<MaterialVo4Unit> select(String barCode, String name, String standard, String model, String categoryId, String mpList, int offset, int rows)
            throws Exception{
        String[] mpArr = new String[]{};
        if(StringUtil.isNotEmpty(mpList)){
            mpArr= mpList.split(",");
        }
        List<MaterialVo4Unit> resList = new ArrayList<>();
        List<MaterialVo4Unit> list =null;
        try{
            List<Long> idList = new ArrayList<>();
            if(StringUtil.isNotEmpty(categoryId)){
                idList = getListByParentId(Long.parseLong(categoryId));
            }
            list= materialMapperEx.selectByConditionMaterial(barCode, name, standard, model, idList, mpList, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if (null != list) {
            for (MaterialVo4Unit m : list) {
                //????????????
                String materialOther = "";
                for (int i = 0; i < mpArr.length; i++) {
                    if (mpArr[i].equals("?????????")) {
                        materialOther = materialOther + ((m.getMfrs() == null || m.getMfrs().equals("")) ? "" : "(" + m.getMfrs() + ")");
                    }
                    if (mpArr[i].equals("?????????1")) {
                        materialOther = materialOther + ((m.getOtherField1() == null || m.getOtherField1().equals("")) ? "" : "(" + m.getOtherField1() + ")");
                    }
                    if (mpArr[i].equals("?????????2")) {
                        materialOther = materialOther + ((m.getOtherField2() == null || m.getOtherField2().equals("")) ? "" : "(" + m.getOtherField2() + ")");
                    }
                    if (mpArr[i].equals("?????????3")) {
                        materialOther = materialOther + ((m.getOtherField3() == null || m.getOtherField3().equals("")) ? "" : "(" + m.getOtherField3() + ")");
                    }
                }
                m.setMaterialOther(materialOther);
                Long tenantId = m.getTenantId();
                m.setStock(depotItemService.getStockByParam(null,m.getId(),null,null,tenantId));
                resList.add(m);
            }
        }
        return resList;
    }

    public Long countMaterial(String barCode, String name, String standard, String model, String categoryId,String mpList)throws Exception {
        Long result =null;
        try{
            List<Long> idList = new ArrayList<>();
            if(StringUtil.isNotEmpty(categoryId)){
                idList = getListByParentId(Long.parseLong(categoryId));
            }
            result= materialMapperEx.countsByMaterial(barCode, name, standard, model, idList, mpList);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertMaterial(JSONObject obj, HttpServletRequest request)throws Exception {
        Material m = JSONObject.parseObject(obj.toJSONString(), Material.class);
        m.setEnabled(true);
        try{
            Long mId = null;
            materialMapper.insertSelective(m);
            List<Material> materials = getMaterialListByParam(m.getName(),m.getModel(),m.getColor(),
                    m.getStandard(), m.getMfrs(),m.getUnit(),m.getUnitId());
            if(materials!=null && materials.size()>0) {
                mId = materials.get(0).getId();
            }
            materialExtendService.saveDetials(obj, obj.getString("sortList"), mId, "insert");
            if(obj.get("stock")!=null) {
                JSONArray stockArr = obj.getJSONArray("stock");
                for (int i = 0; i < stockArr.size(); i++) {
                    JSONObject jsonObj = stockArr.getJSONObject(i);
                    if(jsonObj.get("id")!=null && jsonObj.get("initStock")!=null) {
                        String number = jsonObj.getString("initStock");
                        Long depotId = jsonObj.getLong("id");
                        if(StringUtil.isNotEmpty(number) && Double.valueOf(number)>0) {
                            insertInitialStockByMaterialAndDepot(depotId, mId, parseBigDecimalEx(number));
                            insertCurrentStockByMaterialAndDepot(depotId, mId, parseBigDecimalEx(number));
                        }
                    }
                }
            }
            logService.insertLog("??????",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(m.getName()).toString(), request);
            return 1;
        }
        catch (BusinessRunTimeException ex) {
            throw new BusinessRunTimeException(ex.getCode(), ex.getMessage());
        }
        catch(Exception e){
            JshException.writeFail(logger, e);
            return 0;
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateMaterial(JSONObject obj, HttpServletRequest request) throws Exception{
        Material material = JSONObject.parseObject(obj.toJSONString(), Material.class);
        try{
            materialMapper.updateByPrimaryKeySelective(material);
            if(material.getUnitId() == null) {
                materialMapperEx.setUnitIdToNull(material.getId());
            }
            materialExtendService.saveDetials(obj, obj.getString("sortList"),material.getId(), "update");
            if(obj.get("stock")!=null) {
                JSONArray stockArr = obj.getJSONArray("stock");
                for (int i = 0; i < stockArr.size(); i++) {
                    JSONObject jsonObj = stockArr.getJSONObject(i);
                    if (jsonObj.get("id") != null && jsonObj.get("initStock") != null) {
                        String number = jsonObj.getString("initStock");
                        Long depotId = jsonObj.getLong("id");
                        //????????????-??????????????????
                        MaterialInitialStockExample example = new MaterialInitialStockExample();
                        example.createCriteria().andMaterialIdEqualTo(material.getId()).andDepotIdEqualTo(depotId);
                        materialInitialStockMapper.deleteByExample(example);
                        if (StringUtil.isNotEmpty(number) && Double.valueOf(number) > 0) {
                            insertInitialStockByMaterialAndDepot(depotId, material.getId(), parseBigDecimalEx(number));
                        }
                        //??????????????????
                        Long tenantId = redisService.getTenantId(request);
                        depotItemService.updateCurrentStockFun(material.getId(), depotId, tenantId);
                    }
                }
            }
            logService.insertLog("??????",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(material.getName()).toString(), request);
            return 1;
        }catch(Exception e){
            JshException.writeFail(logger, e);
            return 0;
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteMaterial(Long id, HttpServletRequest request)throws Exception {
        return batchDeleteMaterialByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMaterial(String ids, HttpServletRequest request)throws Exception {
        return batchDeleteMaterialByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMaterialByIds(String ids) throws Exception{
        String [] idArray=ids.split(",");
        //??????????????????	jsh_depot_item
        List<DepotItem> depotItemList =null;
        try{
            depotItemList=  depotItemMapperEx.getDepotItemListListByMaterialIds(idArray);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if(depotItemList!=null&&depotItemList.size()>0){
            logger.error("?????????[{}],????????????[{}],??????,MaterialIds[{}]",
                    ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,ExceptionConstants.DELETE_FORCE_CONFIRM_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.DELETE_FORCE_CONFIRM_CODE,
                    ExceptionConstants.DELETE_FORCE_CONFIRM_MSG);
        }
        //????????????
        StringBuffer sb = new StringBuffer();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        List<Material> list = getMaterialListByIds(ids);
        for(Material material: list){
            sb.append("[").append(material.getName()).append("]");
        }
        logService.insertLog("??????", sb.toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        User userInfo=userService.getCurrentUser();
        //??????????????????????????????
        try{
            //??????????????????
            materialMapperEx.batchDeleteMaterialByIds(new Date(),userInfo==null?null:userInfo.getId(),idArray);
            //??????????????????????????????
            materialExtendMapperEx.batchDeleteMaterialExtendByMIds(idArray);
            return 1;
        }catch(Exception e){
            JshException.writeFail(logger, e);
            return 0;
        }
    }

    public int checkIsNameExist(Long id, String name)throws Exception {
        MaterialExample example = new MaterialExample();
        example.createCriteria().andIdNotEqualTo(id).andNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Material> list =null;
        try{
            list=  materialMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    public int checkIsExist(Long id, String name, String model, String color, String standard, String mfrs,
                            String otherField1, String otherField2, String otherField3, String unit, Long unitId)throws Exception {
        return materialMapperEx.checkIsExist(id, name, model, color, standard, mfrs, otherField1,
                otherField2, otherField3, unit, unitId);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchSetStatus(Boolean status, String ids)throws Exception {
        logService.insertLog("??????",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(ids).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        List<Long> materialIds = StringUtil.strToLongList(ids);
        Material material = new Material();
        material.setEnabled(status);
        MaterialExample example = new MaterialExample();
        example.createCriteria().andIdIn(materialIds);
        int result =0;
        try{
            result=  materialMapper.updateByExampleSelective(material, example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public Unit findUnit(Long mId)throws Exception{
        Unit unit = new Unit();
        try{
            List<Unit> list = materialMapperEx.findUnitList(mId);
            if(list!=null && list.size()>0) {
                unit = list.get(0);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return unit;
    }

    public List<MaterialVo4Unit> findById(Long id)throws Exception{
        List<MaterialVo4Unit> list =null;
        try{
            list=  materialMapperEx.findById(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<MaterialVo4Unit> findByIdWithBarCode(Long meId)throws Exception{
        List<MaterialVo4Unit> list =null;
        try{
            list=  materialMapperEx.findByIdWithBarCode(meId);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<Long> getListByParentId(Long parentId) {
        List<Long> idList = new ArrayList<Long>();
        List<MaterialCategory> list = materialCategoryMapperEx.getListByParentId(parentId);
        idList.add(parentId);
        if(list!=null && list.size()>0) {
            getIdListByParentId(idList, parentId);
        }
        return idList;
    }

    public List<Long> getIdListByParentId(List<Long> idList, Long parentId){
        List<MaterialCategory> list = materialCategoryMapperEx.getListByParentId(parentId);
        if(list!=null && list.size()>0) {
            for(MaterialCategory mc : list){
                idList.add(mc.getId());
                getIdListByParentId(idList, mc.getId());
            }
        }
        return idList;
    }

    public List<MaterialVo4Unit> findBySelectWithBarCode(Long categoryId, String q, Integer offset, Integer rows)throws Exception{
        List<MaterialVo4Unit> list =null;
        try{
            List<Long> idList = new ArrayList<>();
            if(categoryId!=null){
                Long parentId = categoryId;
                idList = getListByParentId(parentId);
            }
            if(StringUtil.isNotEmpty(q)) {
                q = q.replace("'", "");
            }
            list=  materialMapperEx.findBySelectWithBarCode(idList, q, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public int findBySelectWithBarCodeCount(Long categoryId, String q)throws Exception{
        int result=0;
        try{
            List<Long> idList = new ArrayList<>();
            if(categoryId!=null){
                Long parentId = categoryId;
                idList = getListByParentId(parentId);
            }
            if(StringUtil.isNotEmpty(q)) {
                q = q.replace("'", "");
            }
            result = materialMapperEx.findBySelectWithBarCodeCount(idList, q);
        }catch(Exception e){
            logger.error("?????????[{}],????????????[{}],??????[{}]",
                    ExceptionConstants.DATA_READ_FAIL_CODE,ExceptionConstants.DATA_READ_FAIL_MSG,e);
            throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                    ExceptionConstants.DATA_READ_FAIL_MSG);
        }
        return result;
    }

    public List<MaterialVo4Unit> findByAll(String barCode, String name, String standard, String model, String categoryId)throws Exception {
        List<MaterialVo4Unit> resList = new ArrayList<>();
        List<MaterialVo4Unit> list =null;
        try{
            List<Long> idList = new ArrayList<>();
            if(StringUtil.isNotEmpty(categoryId)){
                idList = getListByParentId(Long.parseLong(categoryId));
            }
            list=  materialMapperEx.findByAll(barCode, name, standard, model, idList);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        if (null != list) {
            for (MaterialVo4Unit m : list) {
                resList.add(m);
            }
        }
        return resList;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public BaseResponseInfo importExcel(Sheet src, HttpServletRequest request) throws Exception {
        List<Depot> depotList= depotService.getDepot();
        int depotCount = depotList.size();
        List<MaterialWithInitStock> mList = new ArrayList<>();
        for (int i = 2; i < src.getRows(); i++) {
            String name = ExcelUtils.getContent(src, i, 0); //??????
            String standard = ExcelUtils.getContent(src, i, 1); //??????
            String model = ExcelUtils.getContent(src, i, 2); //??????
            String color = ExcelUtils.getContent(src, i, 3); //??????
            String categoryName = ExcelUtils.getContent(src, i, 4); //??????
            String safetyStock = ExcelUtils.getContent(src, i, 5); //????????????
            String unit = ExcelUtils.getContent(src, i, 6); //????????????
            //??????????????????????????????????????????
            if(StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(model) && StringUtil.isNotEmpty(unit)) {
                MaterialWithInitStock m = new MaterialWithInitStock();
                m.setName(name);
                m.setStandard(standard);
                m.setModel(model);
                m.setColor(color);
                Long categoryId = materialCategoryService.getCategoryIdByName(categoryName);
                m.setCategoryId(categoryId);
                m.setSafetyStock(parseBigDecimalEx(safetyStock));
                String manyUnit = ExcelUtils.getContent(src, i, 7); //?????????
                String barCode = ExcelUtils.getContent(src, i, 8); //????????????
                String manyBarCode = ExcelUtils.getContent(src, i, 9); //?????????
                String ratio = ExcelUtils.getContent(src, i, 10); //??????
                String purchaseDecimal = ExcelUtils.getContent(src, i, 11); //?????????
                String commodityDecimal = ExcelUtils.getContent(src, i, 12); //?????????
                String wholesaleDecimal = ExcelUtils.getContent(src, i, 13); //?????????
                String lowDecimal = ExcelUtils.getContent(src, i, 14); //????????????
                JSONObject materialExObj = new JSONObject();
                JSONObject basicObj = new JSONObject();
                basicObj.put("barCode", barCode);
                basicObj.put("commodityUnit", unit);
                basicObj.put("purchaseDecimal", purchaseDecimal);
                basicObj.put("commodityDecimal", commodityDecimal);
                basicObj.put("wholesaleDecimal", wholesaleDecimal);
                basicObj.put("lowDecimal", lowDecimal);
                materialExObj.put("basic", basicObj);
                if(StringUtil.isNotEmpty(manyUnit.trim())){ //?????????
                    String manyUnitAll = unit + "," + manyUnit + "(1:" + ratio + ")";
                    Long unitId = unitService.getUnitIdByName(manyUnitAll);
                    m.setUnitId(unitId);
                    JSONObject otherObj = new JSONObject();
                    otherObj.put("barCode", manyBarCode);
                    otherObj.put("commodityUnit", manyUnit);
                    otherObj.put("purchaseDecimal", parsePrice(purchaseDecimal,ratio));
                    otherObj.put("commodityDecimal", parsePrice(commodityDecimal,ratio));
                    otherObj.put("wholesaleDecimal", parsePrice(wholesaleDecimal,ratio));
                    otherObj.put("lowDecimal", parsePrice(lowDecimal,ratio));
                    materialExObj.put("other", otherObj);
                } else {
                    m.setUnit(unit);
                }
                m.setMaterialExObj(materialExObj);
                String enabled = ExcelUtils.getContent(src, i, 15); //??????
                m.setEnabled(enabled.equals("1")? true: false);
                //?????????????????????????????????
                Map<Long, BigDecimal> stockMap = new HashMap<Long, BigDecimal>();
                for(int j=1; j<=depotCount;j++) {
                    int col = 15+j;
                    if(col < src.getColumns()){
                        String depotName = ExcelUtils.getContent(src, 1, col); //??????????????????
                        Long depotId = depotService.getIdByName(depotName);
                        if(depotId!=0L){
                            String stockStr = ExcelUtils.getContent(src, i, col);
                            if(StringUtil.isNotEmpty(stockStr)) {
                                stockMap.put(depotId, parseBigDecimalEx(stockStr));
                            }
                        }
                    }
                }
                m.setStockMap(stockMap);
                mList.add(m);
            }
        }
        logService.insertLog("??????",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_IMPORT).append(mList.size()).append(BusinessConstants.LOG_DATA_UNIT).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        BaseResponseInfo info = new BaseResponseInfo();
        try {
            Long mId = 0L;
            for(MaterialWithInitStock m: mList) {
                //??????????????????????????????????????????????????????????????????????????????
                List<Material> materials = getMaterialListByParam(m.getName(),m.getModel(),m.getColor(),m.getStandard(),
                        m.getMfrs(),m.getUnit(),m.getUnitId());
                if(materials.size()<=0) {
                    materialMapper.insertSelective(m);
                    List<Material> newList = getMaterialListByParam(m.getName(),m.getModel(),m.getColor(),m.getStandard(),
                            m.getMfrs(),m.getUnit(),m.getUnitId());
                    if(newList!=null && newList.size()>0) {
                        mId = newList.get(0).getId();
                    }
                } else {
                    mId = materials.get(0).getId();
                    String materialJson = JSON.toJSONString(m);
                    Material material = JSONObject.parseObject(materialJson, Material.class);
                    material.setId(mId);
                    materialMapper.updateByPrimaryKeySelective(material);
                }
                //??????????????????????????????????????????
                User user = userService.getCurrentUser();
                JSONObject materialExObj = m.getMaterialExObj();
                if(StringUtil.isExist(materialExObj.get("basic"))){
                    String basicStr = materialExObj.getString("basic");
                    MaterialExtend basicMaterialExtend = JSONObject.parseObject(basicStr, MaterialExtend.class);
                    basicMaterialExtend.setMaterialId(mId);
                    basicMaterialExtend.setDefaultFlag("1");
                    basicMaterialExtend.setCreateTime(new Date());
                    basicMaterialExtend.setUpdateTime(System.currentTimeMillis());
                    basicMaterialExtend.setCreateSerial(user.getLoginName());
                    basicMaterialExtend.setUpdateSerial(user.getLoginName());
                    Long meId = materialExtendService.selectIdByMaterialIdAndDefaultFlag(mId, "1");
                    if(meId==0L){
                        materialExtendMapper.insertSelective(basicMaterialExtend);
                    } else {
                        basicMaterialExtend.setId(meId);
                        materialExtendMapper.updateByPrimaryKeySelective(basicMaterialExtend);
                    }
                }
                if(StringUtil.isExist(materialExObj.get("other"))) {
                    String otherStr = materialExObj.getString("other");
                    MaterialExtend otherMaterialExtend = JSONObject.parseObject(otherStr, MaterialExtend.class);
                    otherMaterialExtend.setMaterialId(mId);
                    otherMaterialExtend.setDefaultFlag("0");
                    otherMaterialExtend.setCreateTime(new Date());
                    otherMaterialExtend.setUpdateTime(System.currentTimeMillis());
                    otherMaterialExtend.setCreateSerial(user.getLoginName());
                    otherMaterialExtend.setUpdateSerial(user.getLoginName());
                    Long meId = materialExtendService.selectIdByMaterialIdAndDefaultFlag(mId, "0");
                    if(meId==0L){
                        materialExtendMapper.insertSelective(otherMaterialExtend);
                    } else {
                        otherMaterialExtend.setId(meId);
                        materialExtendMapper.updateByPrimaryKeySelective(otherMaterialExtend);
                    }
                }
                //????????????????????????getAllListWithStock
                Map<Long, BigDecimal> stockMap = m.getStockMap();
                Long depotId = null;
                for(Depot depot: depotList){
                    BigDecimal stock = stockMap.get(depot.getId());
                    //????????????-??????????????????
                    MaterialInitialStockExample example = new MaterialInitialStockExample();
                    example.createCriteria().andMaterialIdEqualTo(mId).andDepotIdEqualTo(depot.getId());
                    materialInitialStockMapper.deleteByExample(example);
                    if(stock!=null && stock.compareTo(BigDecimal.ZERO)!=0) {
                        depotId = depot.getId();
                        insertInitialStockByMaterialAndDepot(depotId, mId, stock);
                        //??????????????????
                        Long tenantId = redisService.getTenantId(request);
                        depotItemService.updateCurrentStockFun(mId, depotId, tenantId);
                    }
                }
            }
            info.code = 200;
            info.data = "????????????";
        } catch (Exception e) {
            e.printStackTrace();
            info.code = 500;
            info.data = "????????????";
        }
        return info;
    }

    /**
     * ??????????????????????????????
     * @param name
     * @param model
     * @param color
     * @param standard
     * @param mfrs
     * @param unit
     * @param unitId
     * @return
     */
    private List<Material> getMaterialListByParam(String name, String model, String color,
                                                  String standard, String mfrs, String unit, Long unitId) {
        MaterialExample example = new MaterialExample();
        MaterialExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        if (StringUtil.isNotEmpty(model)) {
            criteria.andModelEqualTo(model);
        }
        if (StringUtil.isNotEmpty(color)) {
            criteria.andColorEqualTo(color);
        }
        if (StringUtil.isNotEmpty(standard)) {
            criteria.andStandardEqualTo(standard);
        }
        if (StringUtil.isNotEmpty(mfrs)) {
            criteria.andMfrsEqualTo(mfrs);
        }
        if (StringUtil.isNotEmpty(unit)) {
            criteria.andUnitEqualTo(unit);
        }
        if (unitId !=null) {
            criteria.andUnitIdEqualTo(unitId);
        }
        criteria.andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<Material> list = materialMapper.selectByExample(example);
        return list;
    }

    /**
     * ??????????????????
     * @param depotId
     * @param mId
     * @param stock
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insertInitialStockByMaterialAndDepot(Long depotId, Long mId, BigDecimal stock){
        MaterialInitialStock materialInitialStock = new MaterialInitialStock();
        materialInitialStock.setDepotId(depotId);
        materialInitialStock.setMaterialId(mId);
        materialInitialStock.setNumber(stock);
        materialInitialStockMapper.insertSelective(materialInitialStock); //??????????????????
    }

    /**
     * ??????????????????
     * @param depotId
     * @param mId
     * @param stock
     */
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insertCurrentStockByMaterialAndDepot(Long depotId, Long mId, BigDecimal stock){
        MaterialCurrentStock materialCurrentStock = new MaterialCurrentStock();
        materialCurrentStock.setDepotId(depotId);
        materialCurrentStock.setMaterialId(mId);
        materialCurrentStock.setCurrentNumber(stock);
        materialCurrentStockMapper.insertSelective(materialCurrentStock); //??????????????????
    }

    public List<MaterialVo4Unit> getMaterialEnableSerialNumberList(String q, Integer offset, Integer rows)throws Exception {
        List<MaterialVo4Unit> list =null;
        try{
            list=  materialMapperEx.getMaterialEnableSerialNumberList(q, offset, rows);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long getMaterialEnableSerialNumberCount(String q)throws Exception {
        Long count =null;
        try{
            count=  materialMapperEx.getMaterialEnableSerialNumberCount(q);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return count;
    }

    public BigDecimal parseBigDecimalEx(String str) throws Exception{
        if(!StringUtil.isEmpty(str)) {
            return  new BigDecimal(str);
        } else {
            return null;
        }
    }

    public BigDecimal parsePrice(String price, String ratio) throws Exception{
        if(StringUtil.isEmpty(price) || StringUtil.isEmpty(ratio)) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal pr=new BigDecimal(price);
            BigDecimal r=new BigDecimal(ratio);
            return pr.multiply(r);
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     * @param materialId
     * @return
     */
    public BigDecimal getInitStockByMid(Long depotId, Long materialId) {
        BigDecimal stock = BigDecimal.ZERO;
        MaterialInitialStockExample example = new MaterialInitialStockExample();
        if(depotId!=null) {
            example.createCriteria().andMaterialIdEqualTo(materialId).andDepotIdEqualTo(depotId)
                    .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        } else {
            example.createCriteria().andMaterialIdEqualTo(materialId)
                    .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        }
        List<MaterialInitialStock> list = materialInitialStockMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            for(MaterialInitialStock ms: list) {
                if(ms!=null) {
                    stock = stock.add(ms.getNumber());
                }
            }
        }
        return stock;
    }

    /**
     * ???????????????????????????????????????
     * @param materialId
     * @param depotId
     * @return
     */
    public BigDecimal getInitStock(Long materialId, Long depotId) {
        BigDecimal stock = BigDecimal.ZERO;
        MaterialInitialStockExample example = new MaterialInitialStockExample();
        example.createCriteria().andMaterialIdEqualTo(materialId).andDepotIdEqualTo(depotId)
                .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<MaterialInitialStock> list = materialInitialStockMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            stock = list.get(0).getNumber();
        }
        return stock;
    }

    /**
     * ???????????????????????????????????????
     * @param materialId
     * @param depotId
     * @return
     */
    public BigDecimal getCurrentStock(Long materialId, Long depotId) {
        BigDecimal stock = BigDecimal.ZERO;
        MaterialCurrentStockExample example = new MaterialCurrentStockExample();
        example.createCriteria().andMaterialIdEqualTo(materialId).andDepotIdEqualTo(depotId)
                .andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<MaterialCurrentStock> list = materialCurrentStockMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            stock = list.get(0).getCurrentNumber();
        } else {
            stock = getInitStock(materialId,depotId);
        }
        return stock;
    }

    public List<MaterialVo4Unit> getMaterialByMeId(Long meId) {
        List<MaterialVo4Unit> result = new ArrayList<MaterialVo4Unit>();
        try{
            if(meId!=null) {
                result= materialMapperEx.getMaterialByMeId(meId);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public String getMaxBarCode() {
        String maxBarCodeOld = materialMapperEx.getMaxBarCode();
        return Long.parseLong(maxBarCodeOld)+"";
    }

    public List<String> getMaterialNameList() {
        return materialMapperEx.getMaterialNameList();
    }

    public List<MaterialVo4Unit> getMaterialByBarCode(String barCode) {
        return materialMapperEx.getMaterialByBarCode(barCode);
    }

    public List<MaterialVo4Unit> getListWithStock(Long depotId, List<Long> idList, String materialParam, Integer offset, Integer rows) {
        return materialMapperEx.getListWithStock(depotId, idList, materialParam, offset, rows);
    }

    public int getListWithStockCount(Long depotId, List<Long> idList, String materialParam) {
        return materialMapperEx.getListWithStockCount(depotId, idList, materialParam);
    }

    public MaterialVo4Unit getTotalStockAndPrice(Long depotId, List<Long> idList, String materialParam) {
        return materialMapperEx.getTotalStockAndPrice(depotId, idList, materialParam);
    }
}
