package com.fhs.core.base.vo;

import com.baomidou.mybatisplus.advance.query.QueryField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Slf4j
@AllArgsConstructor
@ApiModel("用于自定义条件过滤")
public class QueryFilter<T> extends com.baomidou.mybatisplus.advance.query.QueryFilter<T> {
    public Map<String, Object> queryFieldsMap() {
        if (super.getQuerys() == null) {
            return new HashMap<>(0);
        }
        return super.getQuerys().stream().collect(Collectors.toMap(QueryField::getProperty, QueryField::getValue));
    }
}
