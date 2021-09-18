package com.fhs.generate.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.utils.ConverterUtils;
import com.mybatis.jpa.common.ColumnNameUtil;
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
public class TableInfoVO {


    @ApiModelProperty("数据库名")
    private String dbName;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("开发者名字")
    private String author;

    @ApiModelProperty("表注释")
    private String comment;

    /**
     * 主键
     */
    @JsonIgnore
    private String pkey;

    /**
     * 命名空间
     */
    @JsonIgnore
    private String namespace;


    /**
     * namespace = t_ucenter_user  去掉t_ 转驼峰  为 ucenterUser
     *
     * @return
     */
    public String getNamespace() {
        String result = this.tableName.replace("t_", "");
        return ColumnNameUtil.underlineToCamel(ConverterUtils.toString(result));
    }

    @ApiModelProperty("表字段集合")
    private FieldsVO[] fieldsVOS;
}
