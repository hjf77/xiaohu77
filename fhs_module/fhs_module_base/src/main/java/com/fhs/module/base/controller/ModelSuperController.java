package com.fhs.module.base.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaJoinQueryWrapper;
import com.fhs.basics.api.anno.LogMethod;
import com.fhs.basics.constant.LoggerConstant;
import com.fhs.basics.context.UserContext;
import com.fhs.basics.vo.UcenterMsUserVO;
import com.fhs.common.tree.TreeNode;
import com.fhs.common.tree.Treeable;
import com.fhs.common.utils.*;
import com.fhs.core.base.controller.BaseController;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.service.BaseService;
import com.fhs.core.base.valid.group.Add;
import com.fhs.core.base.valid.group.Update;
import com.fhs.core.base.vo.ExcelExportFieldVO;
import com.fhs.core.base.vo.FieldVO;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.config.EConfig;
import com.fhs.core.excel.exception.ValidationException;
import com.fhs.core.excel.service.ExcelService;
import com.fhs.core.exception.NotPremissionException;
import com.fhs.core.exception.ParamException;
import com.fhs.core.logger.Logger;
import com.fhs.core.result.HttpResult;
import com.fhs.core.safe.repeat.anno.NotRepeat;
import com.fhs.core.trans.vo.VO;
import com.fhs.core.valid.checker.ParamChecker;
import com.fhs.excel.dto.ExcelImportSett;
import com.fhs.trans.service.impl.TransService;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * 基础的model super controller
 *
 * @Filename: ModelSuperController.java
 * @Version: 1.0
 * @Author: jianbo.qin
 * @History:<br> 陕西小伙伴网络科技有限公司 Copyright (c) 2017 All Rights Reserved.
 */
public abstract class ModelSuperController<V extends VO, D extends BasePO, PT extends Serializable> extends BaseController {
    protected Logger log = Logger.getLogger(getClass());

    /**
     * 用于导出用的缓存
     */
    protected Cache<Long, QueryFilter<D>> exportParamCache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build();

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
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("pagerAdvance")
    @ApiOperation("后台-高级分页查询")
    public IPage<V> findPagerAdvance(@RequestBody QueryFilter<D> filter) {
        if (isPermitted("see")) {
            LambdaJoinQueryWrapper<D> wrapper = filter.asWrapper(getDOClass());
            initQueryWrapper(wrapper, filter, true);
            this.setExportCache(filter);
            //这里的是1是DO的index
            IPage<V> result = baseService.selectPageMP(filter.getPagerInfo(), wrapper);
            parseRecords(result.getRecords(), true, false);
            return result;
        } else {
            throw new NotPremissionException();
        }
    }

    /**
     * 通过此方法自动拼接自定义的过滤字段
     *
     * @param wrapper wrapper
     * @param filter  前端传的 可能为null
     * @param isPager 是否分页场景
     */
    public void initQueryWrapper(LambdaJoinQueryWrapper<D> wrapper, QueryFilter<D> filter, boolean isPager) {

    }

    /**
     * 格式化返回结果
     * 比如密码字段不给前端返回
     *
     * @param records 数据集合
     * @param isPager 是否分页
     */
    public void parseRecords(List<V> records, boolean isPager, boolean isExport) {

    }


    /**
     * 查询bean列表数据-不分页
     *
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("findListAdvance")
    @ApiOperation("后台-高级查询不分页一般用于下拉")
    public List<V> findListAdvance(@RequestBody QueryFilter<D> filter) {
        if (isPermitted("see")) {
            LambdaJoinQueryWrapper<D> wrapper = filter.asWrapper(getDOClass());
            initQueryWrapper(wrapper, filter, false);
            //这里的是1是DO的index
            List<V> result = baseService.selectListMP(wrapper);
            parseRecords(result, false, false);
            return result;
        } else {
            throw new NotPremissionException();
        }
    }

    /**
     * 无分页查询bean列表数据
     *
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("findList")
    @ApiOperation("后台-不分页查询集合-一般用于下拉")
    public List<V> findList()
            throws Exception {
        if (isPermitted("see")) {
            LambdaJoinQueryWrapper<D> wrapper = QueryFilter.reqParam2Wrapper(baseService.getPoClass());
            initQueryWrapper(wrapper, null, false);
            List<V> dataList = baseService.selectListMP(wrapper);
            parseRecords(dataList, false, false);
            return dataList;
        } else {
            throw new NotPremissionException();
        }
    }

    /**
     * 获取所有列
     *
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("getAllColumn")
    @ApiOperation("后台-获取所有列")
    public List<FieldVO> getAllColumn(@RequestBody List<FieldVO> fieldVOS) throws Exception {
        LinkedHashMap<String, String> columnMap = baseService.getAllColumn(baseService.getPoClass(), null);
        for (FieldVO field : fieldVOS) {
            //剔除前端传过来的字段
            columnMap.remove(field.getName());
        }
        List<FieldVO> allField = new ArrayList<>();
        for (String key : columnMap.keySet()) {
            FieldVO field = FieldVO.builder().name(key).label(columnMap.get(key)).build();
            allField.add(field);
        }
        return allField;
    }


    /**
     * 公共导出指定字段excel
     * by zhangdong
     */
    @PostMapping("advanceExportExcelField")
    @ApiOperation("配合高级搜索一起使用的excel指定字段导出")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_EXPORT)
    public void exportExcelField(@RequestBody ExcelExportFieldVO excelExportFieldVO) throws Exception {
        QueryFilter<D> filter = this.exportParamCache.getIfPresent(UserContext.getSessionuser().getUserId());
        LambdaJoinQueryWrapper<D> wrapper = filter == null ? new LambdaJoinQueryWrapper<>(getDOClass()) : filter.asWrapper(getDOClass());
        //查询出需要导出的数据
        List<V> data = this.baseService.selectListMP(wrapper);
        transService.transMore(data);
        parseRecords(data, false, true);
        Workbook book = this.excelService.exportExcelField(data, excelExportFieldVO.getFieldVOList());
        String excelTempPath = EConfig.getPathPropertiesValue("fileSavePath") + "/" + StringUtils.getUUID() + ".xlsx";
        FileExportUtil.export(book, new File(excelTempPath));
        FileUtils.download(excelTempPath, getResponse(), excelExportFieldVO.getFileName() + ".xlsx");
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
    @GetMapping("{id}")
    @ApiOperation("根据id获取单挑数据信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")}
    )
    public V info(@PathVariable("id") PT id, HttpServletRequest request)
            throws Exception {
        if (isPermitted("see")) {
            V bean = baseService.selectById(id);
            transService.transOne(bean);
            return bean;
        } else {
            throw new NotPremissionException();
        }
    }

    @NotRepeat
    @ResponseBody
    @PostMapping
    @ApiOperation(value = "新增")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_ADD, voParamIndex = 0)
    public HttpResult<Boolean> add(@RequestBody @Validated(Add.class) V e) {
        if (isPermitted("add")) {
            if (e instanceof BasePO) {
                BasePO<?> baseDo = (BasePO<?>) e;
                //如果是saas模式，并且bean中包含groupcode字段，则给其设置值
                if (ConverterUtils.toBoolean(EConfig.getOtherConfigPropertiesValue("isSaasModel"))
                        && ReflectUtils.getDeclaredField(e.getClass(), "groupCode") != null && ReflectUtils.getValue(e, "groupCode") == null) {
                    ReflectUtils.setValue(e, "groupCode", getSessionuser().getGroupCode());
                }
            }
            beforSave(e, true);
            baseService.insert((D) e);
            return HttpResult.success(true);

        }
        throw new NotPremissionException();
    }


    /**
     * 根据id删除对象
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_DEL, pkeyParamIndex = 0)
    public HttpResult<Boolean> del(@ApiParam(name = "id", value = "实体id") @PathVariable PT id) {
        if (isPermitted("del")) {
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
    @PutMapping
    @ApiOperation(value = "修改")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> update(@RequestBody @Validated(Update.class) V e) {
        if (isPermitted("update")) {
            beforSave(e, false);
            baseService.updateById((D) e);
            return HttpResult.success(true);
        }
        throw new NotPremissionException();
    }


    /**
     * 判断登录人是否有权限
     *
     * @param permitName 权限名称
     * @return true 有权限 false 没有权限
     */
    public boolean isPermitted(String permitName) {
        String path = super.getRequest().getServletPath();
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

    @PatchMapping("/")
    @ApiOperation("只修改某些字段")
    @LogMethod(type = LoggerConstant.METHOD_TYPE_UPATE, voParamIndex = 0)
    public HttpResult<Boolean> updateField(@RequestBody @Validated V e) {
        if (e.getPkey() == null) {
            throw new ParamException("主键为必填");
        }
        if (isPermitted("update")) {
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


    @Override
    public EMap<String, Object> getParameterMap() {
        EMap<String, Object> result = super.getParameterMap();
        result.put("loginUserId", getSessionuser().getUserId());
        return result;
    }

    /**
     * 设置导出缓存
     *
     * @param filter 过滤条件
     */
    protected void setExportCache(QueryFilter<D> filter) {
        exportParamCache.put(UserContext.getSessionuser().getUserId(), filter);
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
