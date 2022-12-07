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

        Map<String, Object> paramParams = new HashMap<>();
        if (null != super.getQuerys()) {
            paramParams = super.getQuerys().stream().collect(Collectors.toMap(QueryField::getProperty, QueryField::getValue));
        }
        if (null != super.getPagerInfo()) {
            paramParams.put("pageNumber", super.getPagerInfo().getPageNumber());
            paramParams.put("pageSize", super.getPagerInfo().getPageSize());
        }
        return paramParams;
    }
}
