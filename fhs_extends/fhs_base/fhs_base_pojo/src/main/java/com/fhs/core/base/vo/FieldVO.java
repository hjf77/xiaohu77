package com.fhs.core.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description Excel导出字段VO
 * @Author ZhangDong
 * @Date 2022年10月27日 17:27 星期四
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldVO {

    @ApiModelProperty("字段名称")
    private String name;

    @ApiModelProperty("字段中文展示名称")
    private String label;
}
