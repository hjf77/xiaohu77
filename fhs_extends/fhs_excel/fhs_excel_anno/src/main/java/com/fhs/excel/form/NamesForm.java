package com.fhs.excel.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 反向翻译表单
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NamesForm {
    /**
     * 需要反向翻译的名称集合
     */
    private Set<String> names;
}
