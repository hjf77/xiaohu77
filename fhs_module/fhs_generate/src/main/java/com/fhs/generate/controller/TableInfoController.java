package com.fhs.generate.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaJoinQueryWrapper;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.result.HttpResult;
import com.fhs.generate.constant.GenerateConstant;
import com.fhs.generate.po.SystemTableGenerateConfigPO;
import com.fhs.generate.po.TableInfoPO;
import com.fhs.generate.service.GenerateCodeService;
import com.fhs.generate.service.SystemTableGenerateConfigService;
import com.fhs.generate.service.TableInfoService;
import com.fhs.generate.vo.FormSettVO;
import com.fhs.generate.vo.ListSettVO;
import com.fhs.generate.vo.SystemTableGenerateConfigVO;
import com.fhs.generate.vo.TableInfoVO;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author wanglei
 */
@RestController
@Api(tags = {"获取库表信息"})
@RequestMapping("ms/table")
@ApiGroup(group = "group_default")
public class TableInfoController extends ModelSuperController<TableInfoVO, TableInfoPO, String> {

    @Autowired
    private TableInfoService tableInfoService;

    @Autowired
    private SystemTableGenerateConfigService tableGenerateConfigService;

    @Autowired
    private GenerateCodeService generateCodeService;

    /**
     * @param tableName
     * @return com.fhs.generate.vo.TableSearchVO
     * @author LiYuLin
     * @date 2021年08月17日
     */
    @GetMapping("/getTableInfo")
    @ApiOperation("根据库名和表名获取表信息")
    public TableInfoVO getTableInfo(String tableSchema, String tableName, String configType) {
        TableInfoVO result = tableInfoService.getTableInfo(tableSchema, tableName, configType);
        return result;
    }


    @PostMapping("pagerAdvance")
    @ApiOperation("后台-高级分页查询")
    public IPage<TableInfoVO> findPagerAdvance(@RequestBody QueryFilter<TableInfoPO> filter) {
        LambdaJoinQueryWrapper<TableInfoPO> wrapper = filter.asWrapper(getPOClass());
        //这里的是1是DO的index
        return baseService.selectPageMP(filter.getPagerInfo(), wrapper);
    }

    @Override
    @GetMapping("findList")
    @ApiOperation("后台-不分页查询")
    public List<TableInfoVO> findList() throws Exception {
        List<TableInfoVO> dataList = baseService.selectListMP(QueryFilter.reqParam2Wrapper(baseService.getPoClass(),super.getParameterMap()));
        return dataList;
    }

    @ApiOperation("更新配置")
    @PutMapping("updateTableGenerateConfig")
    public HttpResult<Boolean> updateTableGenerateConfig(@RequestBody @Validated SystemTableGenerateConfigVO tableGenerateConfigPO) throws Exception {
        SystemTableGenerateConfigPO old = tableGenerateConfigService.selectBean(SystemTableGenerateConfigPO.builder().dbName(tableGenerateConfigPO.getDbName())
                .tableName(tableGenerateConfigPO.getTableName()).build());
        if (old != null) {
            tableGenerateConfigPO.setId(old.getId());
            tableGenerateConfigService.updateSelectiveById(tableGenerateConfigPO);
        } else {
            tableGenerateConfigService.insert(tableGenerateConfigPO);
        }
        return HttpResult.success(true);
    }


    @ApiOperation("获取列表预览json")
    @GetMapping("listViewJson")
    public ListSettVO findListViewJson(String tableSchema, String tableName) {
        TableInfoVO tableInfo = tableInfoService.getTableInfo(tableSchema, tableName, GenerateConstant.CONFIG_TYPE_LIST_COLUMN);
        return generateCodeService.getListJson(tableInfo);
    }

    @ApiOperation("获取表单预览json")
    @GetMapping("formViewJson")
    public FormSettVO findFormViewJson(String tableSchema, String tableName) {
        TableInfoVO tableInfo = tableInfoService.getTableInfo(tableSchema, tableName, GenerateConstant.CONFIG_TYPE_LIST_COLUMN);
        return generateCodeService.getFormJson(tableInfo);
    }




}
