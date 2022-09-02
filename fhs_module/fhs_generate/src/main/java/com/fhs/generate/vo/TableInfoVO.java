package com.fhs.generate.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fhs.common.utils.ConverterUtils;
import com.fhs.generate.po.TableInfoPO;
import com.fhs.generate.util.ColumnNameUtil;
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



    /**
     * namespace = t_ucenter_user  去掉t_ 转驼峰  为 ucenterUser
     *
     * @return
     */
    public String getNamespace() {
        String result = this.getTableName().replace("t_", "");
        return ColumnNameUtil.underlineToCamel(ConverterUtils.toString(result));
    }

    @ApiModelProperty("表字段集合")
    private FieldsVO[] fields;
}
