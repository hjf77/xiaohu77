package com.fhs.demo.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ExcelField {
    /**
     * 文本类型：支持【text】【label】【select】
     */
    private String type;

    /**
     * 字段code
     */
    private String code;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 显示的label
     */
    private String label;

    /**
     * 校验规则：【required】
     */
    private String rule;

    /**
     * 是否导入
     */
    @JSONField(name = "import")
    private Boolean importFlag;

    /**
     * 是否导出
     */
    private Boolean export;

    /**
     * 导入重复校验
     */
    private Boolean notRepeat;

    /**
     * 导出模板宽度: 默认是2000(4个字的距离)
     */
    private Integer cellWidth = 2000;

    /**
     * 下拉字典code：
     */
    private String dictCode;

    public void setCellWidth(Integer charCount) {
        this.cellWidth = charCount * 500;
    }
}
