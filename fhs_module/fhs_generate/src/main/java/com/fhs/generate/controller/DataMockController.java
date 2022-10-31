package com.fhs.generate.controller;

import com.fhs.common.constant.Constant;
import com.fhs.core.base.pojo.pager.Pager;
import com.fhs.core.base.vo.FhsPager;
import com.fhs.generate.constant.GenerateConstant;
import com.fhs.generate.service.TableInfoService;
import com.fhs.generate.vo.ListFieldVO;
import com.fhs.generate.vo.TableInfoVO;
import com.fhs.module.base.swagger.anno.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = {"mock数据"})
@RequestMapping("ms/mock")
@ApiGroup(group = "group_default")
public class DataMockController {

    @Autowired
    private TableInfoService tableInfoService;

    @PostMapping("pager")
    @ApiOperation("mock分页数据")
    public FhsPager<Map<String, Object>> mockPager(String tableSchema, String tableName) {
        TableInfoVO tableInfo = tableInfoService.getTableInfo(tableSchema, tableName, GenerateConstant.CONFIG_TYPE_LIST_COLUMN);
        List<ListFieldVO> fields = Arrays.asList(tableInfo.getFields()).stream().filter(f -> {
            return f.getIsListShow() != null && f.getIsListShow() == Constant.INT_TRUE;
        }).collect(Collectors.toList());
        Map<String, Object> record = new HashMap<>();
        Map<String, String> transMap = new HashMap<>();
        record.put("transMap", transMap);
        for (ListFieldVO field : fields) {
            if (field.getIsDict() != null && field.getIsDict() == Constant.INT_TRUE) {
                transMap.put(field.getCamelFieldName(false) + "Name","mock");
            }
            record.put(field.getCamelFieldName(false),"moack");
        }
        FhsPager reulst = new FhsPager<Map<String, Object>>();
        reulst.setTotal(1);
        reulst.setRecords(Arrays.asList(record));
        return reulst;
    }
}
