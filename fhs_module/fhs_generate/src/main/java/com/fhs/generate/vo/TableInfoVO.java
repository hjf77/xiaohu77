package com.fhs.generate.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.core.jsonfilter.anno.AutoArray;
import com.fhs.core.jsonfilter.serializer.Str2ArrayValueSerializer;
import com.fhs.generate.po.TableInfoPO;
import com.fhs.generate.util.ColumnNameUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author LiYuLin
 * @date 2021年08月17日
 * @return
 */
@Data
@ApiModel("表信息")
public class TableInfoVO extends TableInfoPO {

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

    @ApiModelProperty("开发者名字")
    private String author;

    @AutoArray
    @JSONField(serializeUsing = Str2ArrayValueSerializer.class)
    @ApiModelProperty("此表有哪些操作")
    private String tableOperation = "";

    @ApiModelProperty(value = "表单配置")
    private String formConfig;

    /**
     * namespace = t_ucenter_user  去掉t_ 转驼峰  为 ucenterUser
     *
     * @return
     */
    public String getNamespace() {
        String tableName = this.getTableName();
        if(tableName.startsWith("t_") || tableName.startsWith("v_")){
            tableName = tableName.substring(2);
        }
        return ColumnNameUtil.underlineToCamel(ConverterUtils.toString(tableName));
    }

    @ApiModelProperty("表字段集合")
    private ListFieldVO[] fields;
}
