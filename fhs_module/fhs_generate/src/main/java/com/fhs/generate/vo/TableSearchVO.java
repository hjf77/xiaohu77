package com.fhs.generate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author LiYuLin
 * @date 2021年08月17日
 * @return
 */
@Data
@ApiModel("表信息")
public class TableSearchVO {


    @ApiModelProperty("数据库名")
    private String dbName;
    @ApiModelProperty("表名")
    private String tableName;
    @ApiModelProperty("表字段集合")
    List<FieldsVO> list;
}