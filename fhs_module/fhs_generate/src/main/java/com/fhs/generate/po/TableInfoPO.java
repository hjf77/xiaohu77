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
@ApiModel("PG表信息")
@TableName(value = "v_tables", excludeProperty = {"createUser",  "updateUser", "updateTime", "isDelete"},autoResultMap = true)
public class TableInfoPO extends BasePO<TableInfoPO> {

    @ApiModelProperty("表名字")
    @TableId("table_name")
    private String tableName;

    @ApiModelProperty("表备注")
    @TableField("table_comment")
    private String tableComment;

    @TableField(exist = false)
    @ApiModelProperty("创建时间")
    @JSONField(format = DateUtils.DATETIME_PATTERN)
    private Date createTime;

    @ApiModelProperty("数据库")
    @TableField(exist = false)
    private String tableSchema = "public";

    @ApiModelProperty("编码")
    @TableField(exist = false)
    private String tableCollation;


}
