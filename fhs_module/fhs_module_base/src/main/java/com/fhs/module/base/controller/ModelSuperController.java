package com.fhs.module.base.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 基础的model super controller
 *
 * @Filename: ModelSuperController.java
 * @Version: 1.0
 * @Author: jianbo.qin
 * @History:<br> 陕西小伙伴网络科技有限公司 Copyright (c) 2017 All Rights Reserved.
 */
public abstract class ModelSuperController<V extends VO, P extends BasePO, PT extends Serializable> extends BaseController {
    protected Logger log = Logger.getLogger(getClass());

    /**
     * 用于导出用的缓存
     */
    protected Cache<Long, QueryWrapper> exportParamCache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build();

    @Autowired
    protected BaseService<V, P> baseService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private TransService transService;

    public BaseService<V, P> getBaseService() {
        return baseService;
    }

    /**
     * 高级查询带分页
     *
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("pagerAdvance")
    @ApiOperation("后台-高级分页查询")
    public IPage<V> findPagerAdvance(@RequestBody QueryFilter<P> filter) {
        if (isPermitted("see")) {
            LambdaJoinQueryWrapper<P> wrapper = filter.asWrapper(getPOClass());
            //this.setExportCache(wrapper);
            //这里的是1是DO的index
            return baseService.selectPageMP(filter.getPagerInfo(), wrapper);
        } else {
            throw new NotPremissionException();
        }
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
     * 高级查询不分页
     *
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("findListAdvance")
    @ApiOperation("后台-高级查询不分页一般用于下拉")
    public List<V> findListAdvance(@RequestBody QueryFilter<P> filter) {
        if (isPermitted("see")) {
            //这里的是1是DO的index
            return baseService.selectListMP(filter.asWrapper(getPOClass()));
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
            List<V> dataList = baseService.selectListMP(QueryFilter.reqParam2Wrapper(baseService.getPoClass(),super.getParameterMap()));
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
    public void exportExcelField(@RequestBody ExcelExportFieldVO excelExportFieldVO) throws Exception {
        QueryWrapper<P> wrapper = this.exportParamCache.getIfPresent(UserContext.getSessionuser().getUserId());
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
    public HttpResult<Object> add(@RequestBody @Validated(Add.class) V e) {
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
            baseService.insert((P) e);
            return HttpResult.success(e.getPkey());

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
            baseService.updateById((P) e);
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
    public List<TreeNode<Treeable>> treeData(@ApiParam(name = "queryFilter", value = "过滤条件") @RequestBody QueryFilter<P> queryFilter) throws IllegalAccessException {
        List<V> datas = this.baseService.selectListMP(queryFilter.asWrapper(getPOClass()));
        return TreeUtils.formartTree(datas);
    }

    protected Class<P> getPOClass() {
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
            baseService.updateSelectiveById((P) e);
            return HttpResult.success(true);
        }
        throw new NotPremissionException();
    }


    @Override
    public EMap<String, String> getParameterMap() {
        EMap<String, String> result = super.getParameterMap();
        result.put("loginUserId", ConverterUtils.toString(getSessionuser().getUserId()));
        return result;
    }

    /**
     * 设置导出缓存
     *
     * @param wrapper 过滤条件
     */
    protected void setExportCache(QueryWrapper<P> wrapper) {
        exportParamCache.put(UserContext.getSessionuser().getUserId(), wrapper);
    }

    @PostMapping("pubImportExcel")
    @ApiOperation("公共excel导入")
    @NotRepeat
    public HttpResult<String> pubImportExcel(MultipartFile file, V otherParam) throws Exception {
        if (otherParam == null) {
            otherParam = baseService.getVOClass().newInstance();
        }
        ExcelImportSett importSett = getExcelImportSett(otherParam);
        ParamChecker.isNotNull(importSett, "此接口后台没有配置导入参数，请联系后台");
        try {
            importSett.setVoModel(otherParam);
            this.excelService.importExcel(file, this.getBaseService(), baseService.getVOClass(), importSett);
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
    protected ExcelImportSett getExcelImportSett(V otherParam) {
        return null;
    }

    /**
     * 设置do模板，本次excel导入的部分字段excel是不包含的，在这里初始默认值
     *
     * @param p
     */
    public void excelImportInitDo(P p) {
        p.preInsert(UserContext.getSessionuser().getUserId());
    }


}
