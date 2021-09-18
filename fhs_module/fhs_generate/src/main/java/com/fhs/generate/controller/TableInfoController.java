package com.fhs.generate.controller;


import com.fhs.generate.service.TableInfoService;
import com.fhs.generate.vo.TableInfoVO;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanglei
 */
@RestController
@Api(tags = {"获取库表信息"})
@RequestMapping("ms/table")
@ApiGroup(group = "group_default")
public class TableInfoController {

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
    public TableInfoVO getTableInfo(String dbName, String tableName) {
        return tableInfoService.getTableInfo(dbName, tableName);
    }
}
