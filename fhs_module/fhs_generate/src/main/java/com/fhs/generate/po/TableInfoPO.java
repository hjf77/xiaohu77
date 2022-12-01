package com.fhs.generate.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.common.utils.DateUtils;
import com.fhs.core.base.po.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("mysql表信息")
@TableName(value = "information_schema.tables",autoResultMap = true, excludeProperty = {"createUser",  "updateUser", "updateTime", "isDelete"})
public class TableInfoPO extends BasePO<TableInfoPO> {

    @ApiModelProperty("表名字")
    @TableId("TABLE_NAME")
    private String tableName;

    @ApiModelProperty("表备注")
    @TableField("TABLE_COMMENT")
    private String tableComment;

    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    private Date createTime;

    @ApiModelProperty("数据库")
    @TableField("TABLE_SCHEMA")
    private String tableSchema;

    @ApiModelProperty("编码")
    @TableField("TABLE_COLLATION")
    private String tableCollation;


}
