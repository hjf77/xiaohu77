package com.fhs.module.base.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.bislogger.api.anno.LogMethod;
import com.fhs.bislogger.constant.LoggerConstant;
import com.fhs.common.tree.TreeNode;
import com.fhs.common.tree.Treeable;
import com.fhs.common.utils.*;
import com.fhs.core.base.controller.BaseController;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.pojo.vo.UpdateFieldVO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.config.EConfig;
import com.fhs.core.excel.exception.ValidationException;
import com.fhs.core.excel.service.ExcelService;
import com.fhs.core.exception.NotPremissionException;
import com.fhs.core.exception.ParamException;
import com.fhs.core.result.HttpResult;
import com.fhs.core.safe.repeat.anno.NotRepeat;
import com.fhs.core.trans.vo.VO;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.core.valid.group.Add;
import com.fhs.core.valid.group.Update;
import com.fhs.excel.dto.ExcelImportSett;
import com.fhs.logger.Logger;
import com.fhs.module.base.common.ExcelExportTools;
import com.fhs.basics.context.UserContext;
import com.fhs.trans.service.impl.TransService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mybatis.jpa.context.DataPermissonContext;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 基础的model super controller
 *
 * @Filename: ModelSuperController.java
 * @Version: 1.0
 * @Author: jianbo.qin
 * @History:<br> 陕西小伙伴网络科技有限公司 Copyright (c) 2017 All Rights Reserved.
 */
public abstract class ModelSuperController<V extends VO, D extends BasePO> extends BaseController {
    protected Logger log = Logger.getLogger(getClass());

    /**
     * 用于导出用的缓存
     */
    protected Cache<String, QueryWrapper> exportParamCache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build();

    @Autowired
    protected BaseService<V, D> baseService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private TransService transService;

    public BaseService<V, D> getBaseService() {
        return baseService;
    }

    /**
     * 查询bean列表数据
     *
     * @param request
     * @throws Exception
     */
    @PostMapping("pagerAdvance")
    @ResponseBody
    @ApiOperation("后台-高级分页查询-vue推荐")
    public IPage<V> findPagerAdvance(@RequestBody QueryFilter<D> filter, HttpServletRequest request) {
        if (isPermitted(request, "see")) {
            QueryWrapper wrapper = filter.asWrapper(getDOClass());
            this.setExportCache(wrapper);
            //这里的是1是DO的index
            return baseService.selectPageMP(filter.getPagerInfo(), wrapper);
        } else {
            throw new NotPremissionException();
        }
    }


    /**
     * 查询bean列表数据-不分页
     *
     * @param request
     * @throws Exception
     */
    @PostMapping("findListAdvance")
    @ResponseBody
    @LogMethod(voParamIndex = 0)
    @ApiOperation("后台-高级查询不分页一般用于下拉-vue推荐")
    public List<V> findListAdvance(@RequestBody QueryFilter<D> filter, HttpServletRequest request) {
        if (isPermitted(request, "see")) {
            //这里的是1是DO的index
            return baseService.selectListMP(filter.asWrapper(getDOClass()));
        } else {
            throw new NotPremissionException();
        }
    }

    /**
     * 无分页查询bean列表数据
     *
     * @param request response
     * @throws Exception
     */
    @GetMapping("findList")
    @ResponseBody
    @LogMethod(voParamIndex = 0)
    @ApiOperation("后台-不分页查询集合-一般用于下拉")
    public List<V> findList(V e, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (isPermitted(request, "see")) {
            List<V> dataList = baseService.findForList((D) e);
            return dataList;
        } else {
            throw new NotPremissionException();
        }
    }

    /**
     * 公共导出excel
     * by wanglei
     */
    @GetMapping("advanceExportExcel")
    @ApiOperation("配合高级搜索一起使用的excel导出")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_EXPORT)
    public void exportExcel(HttpServletResponse response, String fileName, String ids) throws IOException {
        QueryWrapper wrapper = this.exportParamCache.getIfPresent(UserContext.getSessionuser().getUserId());
        wrapper = wrapper == null ? new QueryWrapper() : wrapper;
        wrapper = (QueryWrapper) wrapper.clone();
        if (CheckUtils.isNotEmpty(ids)) {
            wrapper.in("id", ids.split(","));
        }
        Workbook book = this.excelService.exportExcel(wrapper, this.baseService, this.getDOClass());
        String excelTempPath = EConfig.getPathPropertiesValue("fileSavePath") + "/" + StringUtils.getUUID() + ".xlsx";
        FileOutputStream os = new FileOutputStream(excelTempPath);
        book.write(os);
        try {
            os.close();
        } catch (Exception e) {
            log.error("关闭流错误", e);
        }
        FileUtils.download(excelTempPath, response, fileName);
        FileUtils.deleteFile(excelTempPath);
    }



    /**
     * 根据ID集合查询对象数据
     *
     * @param id      id
     * @param request request
     * @return
     * @throws Exception
     */
    @LogMethod
    @ResponseBody
    @GetMapping("info/{id}")
    @ApiOperation("根据id获取单挑数据信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")}
    )
    public V info(@PathVariable("id") String id, HttpServletRequest request)
            throws Exception {
        if (isPermitted(request, "see")) {
            V bean = baseService.selectById(id);
            transService.transOne(bean);
            return bean;
        } else {
            throw new NotPremissionException();
        }
    }


    /**
     * groupcode字段
     */
    private Field groupCodeField;

    @NotRepeat
    @ResponseBody
    @PostMapping("/")
    @ApiOperation(value = "新增-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> add(@RequestBody @Validated(Add.class) V e, HttpServletRequest request,
                                    HttpServletResponse response) {
        if (isPermitted(request, "add")) {

            if (e instanceof BasePO) {
                BasePO<?> baseDo = (BasePO<?>) e;
                //如果是saas模式，并且bean中包含groupcode字段，则给其设置值
                if (ConverterUtils.toBoolean(EConfig.getOtherConfigPropertiesValue("isSaasModel"))
                        && ReflectUtils.getDeclaredField(e.getClass(), "groupCode") != null && ReflectUtils.getValue(e, "groupCode") == null) {
                    ReflectUtils.setValue(e, "groupCode", getSessionuser().getGroupCode());
                }
                baseDo.preInsert(getSessionuser().getUserId());
            }
            beforSave(e, true);
            baseService.insertSelective((D) e);
            return HttpResult.success(true);

        }
        throw new NotPremissionException();
    }


    /**
     * 根据id删除对象
     *
     * @param id
     * @param request
     * @return
     */
    @ResponseBody
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_DEL, pkeyParamIndex = 0)
    public HttpResult<Boolean> del(@ApiParam(name = "id", value = "实体id") @PathVariable String id, HttpServletRequest request) {
        if (isPermitted(request, "del")) {
            baseService.deleteById(id);
            return HttpResult.success(true);
        }
        throw new NotPremissionException();
    }


    /**
     * 更新bean数据
     *
     * @param e bean
     */
    @ResponseBody
    @PutMapping("/")
    @ApiOperation(value = "修改-vue专用")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> update(@RequestBody @Validated(Update.class) V e, HttpServletRequest request,
                                            HttpServletResponse response) {
        if (isPermitted(request, "update")) {
            if (e instanceof BasePO) {
                BasePO<?> baseDo = (BasePO<?>) e;
                baseDo.preUpdate(getSessionuser().getUserId());
            }
            beforSave(e, false);
            baseService.updateSelectiveById((D) e);
            return HttpResult.success(true);
        }
        throw new NotPremissionException();
    }


    /**
     * 判断登录人是否有权限
     *
     * @param request    request
     * @param permitName 权限名称
     * @return true 有权限 false 没有权限
     */
    public boolean isPermitted(HttpServletRequest request, String permitName) {
        String path = request.getServletPath();
        String namespace = path.split("/")[2];
        boolean bool = StpUtil.hasPermission(namespace + ":" + permitName);
        return bool;
    }

    /**
     * 获取session里面的user
     *
     * @return session里面的user
     */
    protected UcenterMsUserVO getSessionuser() {
        return UserContext.getSessionuser();
    }


    /**
     * 在保存之前执行部分自定义业务逻辑
     *
     * @param e     对象
     * @param isAdd 是否是添加
     */
    protected void beforSave(V e, boolean isAdd) {

    }


    private static final Map<String, String> BASE_FIELD_MAP = new HashMap<>();

    static {
        BASE_FIELD_MAP.put("transMap.createUserUserName", "create_user");
        BASE_FIELD_MAP.put("transMap.updateUserUserName", "update_user");
        BASE_FIELD_MAP.put("transMap.businessIdEntName", "business_id");
        BASE_FIELD_MAP.put("transMap.verifyUserIdUserName", "verify_user_id");
    }

    /**
     * 格式化order by xx asc desc
     *
     * @param request request
     * @return order by 内容
     */
    protected String formartOrderBy(HttpServletRequest request) {
        String fieldName = request.getParameter("sortTzwName");
        String order = request.getParameter("order");
        if (CheckUtils.isNullOrEmpty(fieldName) || CheckUtils.isNullOrEmpty(order)) {
            return "";
        }
        Map<String, String> fieldMap = getFormartField();
        // 如果子类设置了某个字段的db 字段名则取 子类设置的
        if (fieldMap != null && fieldMap.containsKey(fieldName)) {
            fieldName = fieldMap.get(fieldName);
        } else {
            // 如果子类
            if (BASE_FIELD_MAP.containsKey(fieldName)) {
                fieldName = BASE_FIELD_MAP.get(fieldName);
            } else {
                // 如果带翻译的话，把后面的 name去掉
                if (fieldName.contains("transMap")) {
                    fieldName = fieldName.replace("transMap.", "");
                    fieldName = fieldName.substring(0, fieldName.lastIndexOf("Name"));
                }
                for (char i = 'A'; i < 'Z'; i++) {
                    fieldName = fieldName.replaceAll(i + "", "_" + i);
                }
            }

        }
        return fieldName.toLowerCase() + " " + order;
    }

    /**
     * @param queryFilter
     * @return
     */
    @PostMapping(
            value = {"/tree"},
            produces = {"application/json; charset=utf-8"}
    )
    @ApiOperation("获取tree格式的json数据")
    public List<TreeNode<Treeable>> treeData(@ApiParam(name = "queryFilter", value = "过滤条件") @RequestBody QueryFilter<D> queryFilter) throws IllegalAccessException {
        List<V> datas = this.baseService.selectListMP(queryFilter.asWrapper(getDOClass()));
        return TreeUtils.formartTree(datas);
    }

    protected Class<D> getDOClass() {
        return (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @PutMapping("/updateField")
    @ApiOperation("批量修改某个字段")
    public HttpResult<Boolean> updateField(@RequestBody @Validated UpdateFieldVO<D> vo) throws InstantiationException, IllegalAccessException {
        String[] ids = vo.getIds().split(",");
        List<D> dos = new ArrayList<>();
        D temp = null;
        for (String id : ids) {
            temp = this.getDOClass().newInstance();
            BeanUtils.copyProperties(vo.getDoContent(), temp);
            temp.setPkey(id);
            dos.add(temp);
        }
        baseService.batchUpdate(dos);
        return HttpResult.success(true);
    }


    /**
     * 获取格式化字段的参数
     *
     * @return key 前端字段，val db字段
     */
    public Map<String, String> getFormartField() {
        return null;
    }

    @Override
    public EMap<String, Object> getParameterMap() {
        EMap<String, Object> result = super.getParameterMap();
        result.put("permission", DataPermissonContext.getDataPermissonMap());
        result.put("loginUserId", getSessionuser().getUserId());
        return result;
    }

    /**
     * 设置导出缓存
     *
     * @param wrapper 过滤条件
     */
    protected void setExportCache(QueryWrapper<D> wrapper) {
        exportParamCache.put(UserContext.getSessionuser().getUserId(), wrapper);
    }

    @PostMapping("pubImportExcel")
    @ApiOperation("公共excel导入")
    @NotRepeat
    public HttpResult<String> pubImportExcel(MultipartFile file, D otherParam) throws Exception {
        if (otherParam == null) {
            otherParam = this.getDOClass().newInstance();
        }
        ExcelImportSett importSett = getExcelImportSett(otherParam);
        ParamChecker.isNotNull(importSett, "此接口后台没有配置导入参数，请联系后台");
        try {
            importSett.setDoModel(otherParam);
            this.excelService.importExcel(file, this.getBaseService(), this.getDOClass(), importSett);
        } catch (ValidationException e) {
            throw new ParamException(e.getMessage());
        }
        return HttpResult.success("导入成功");
    }

    /**
     * excel的一些默认配置
     *
     * @return
     */
    protected ExcelImportSett getExcelImportSett(D otherParam) {
        return null;
    }

}
