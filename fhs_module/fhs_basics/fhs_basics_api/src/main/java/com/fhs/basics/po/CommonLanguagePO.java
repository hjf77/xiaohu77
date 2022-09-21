package com.fhs.basics.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.po.BasePO;
import com.fhs.core.base.valid.group.Delete;
import com.fhs.core.base.valid.group.Update;
import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 语言设置表(CommonLanguage)实体类
 *
 * @author LiYuLin
 * @since 2022-09-13 15:05:44
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_common_language")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CommonLanguagePO", description = "语言设置表")
public class CommonLanguagePO extends BasePO<CommonLanguagePO> {
    private static final long serialVersionUID = 326100504483373917L;
    @TableId(value = "id", type = IdType.NONE)
    @NotNull(message = "id字段不可为空", groups = {Update.class, Delete.class})
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 名称
     */
    //@Length(message = "名称字段的长度最大为128", groups = {Add.class, Update.class}, max = 128)
    @TableField("name")
    @ApiModelProperty(value = "名称")
    @ExcelColumn(index = 1)
    private String name;

    /**
     * 中文
     */
    //@Length(message = "中文字段的长度最大为128", groups = {Add.class, Update.class}, max = 128)
    @TableField("values_zh")
    @ApiModelProperty(value = "中文")
    @ExcelColumn(index = 2)
    private String valuesZh;

    /**
     * 英文
     */
    //@Length(message = "英文字段的长度最大为16", groups = {Add.class, Update.class}, max = 16)
    @TableField("values_en")
    @ApiModelProperty(value = "英文")
    @ExcelColumn(index = 3)
    private String valuesEn;

    /**
     * 阿拉伯文
     */
    //@Length(message = "阿拉伯文字段的长度最大为128", groups = {Add.class, Update.class}, max = 128)
    @TableField("values_ar")
    @ApiModelProperty(value = "阿拉伯文")
    @ExcelColumn(index = 4)
    private String valuesAr;
}
