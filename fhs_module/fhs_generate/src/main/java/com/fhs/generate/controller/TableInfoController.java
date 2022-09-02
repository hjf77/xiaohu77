package com.fhs.generate.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fhs.core.base.vo.QueryFilter;
import com.fhs.core.exception.NotPremissionException;
import com.fhs.generate.po.TableInfoPO;
import com.fhs.generate.service.TableInfoService;
import com.fhs.generate.vo.TableInfoVO;
import com.fhs.module.base.controller.ModelSuperController;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * @param tableName
     * @return com.fhs.generate.vo.TableSearchVO
     * @author LiYuLin
     * @date 2021年08月17日
     */
    @GetMapping("/getTableInfo")
    @ResponseBody
    @ApiOperation("根据库名和表名获取表信息")
    public TableInfoVO getTableInfo(String tableSchema, String tableName, String configType) {
        return tableInfoService.getTableInfo(tableSchema, tableName, configType);
    }


    @PostMapping("pagerAdvance")
    @ApiOperation("后台-高级分页查询")
    public IPage<TableInfoVO> findPagerAdvance(@RequestBody QueryFilter<TableInfoPO> filter) {
        QueryWrapper wrapper = filter.asWrapper(getDOClass());
        this.setExportCache(wrapper);
        //这里的是1是DO的index
        return baseService.selectPageMP(filter.getPagerInfo(), wrapper);
    }

    @Override
    @GetMapping("findList")
    @ApiOperation("后台-不分页查询")
    public List<TableInfoVO> findList(TableInfoVO e) throws Exception {
        List<TableInfoVO> dataList = baseService.findForList((TableInfoPO) e);
        return dataList;
    }
}
