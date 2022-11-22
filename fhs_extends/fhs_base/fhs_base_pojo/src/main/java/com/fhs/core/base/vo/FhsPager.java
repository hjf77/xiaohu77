package com.fhs.core.base.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用来分页使用
 *
 * @param <T>
 */
@Data
@Builder
@ApiModel("分页对象")
@AllArgsConstructor
@NoArgsConstructor
public class FhsPager<T> implements IPage<T> {

    @ApiModelProperty("数据")
    private List<T> records;

    @ApiModelProperty("总条数")
    private long total;

    @JsonIgnore
    @ApiModelProperty("每页多少条")
    @JSONField(serialize = false)
    private long size;

    @JsonIgnore
    @ApiModelProperty("当前第几页-仅仅后端用")
    @JSONField(serialize = false)
    private long current;


    @ApiModelProperty("每页多少条")
    private long pageSize;

    @ApiModelProperty("当前第几页-给前端用")
    private long pageNumber;

    @ApiModelProperty("分页扩展字段")
    private Map<String, Object> extMap;

    public FhsPager(List<T> records,long total){
        this.records = records;
        this.total = total;
    }


    @Override
    public List<OrderItem> orders() {
        return new ArrayList<>();
    }

    @Override
    public List<T> getRecords() {
        return records;
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
        this.size = pageSize;
    }

    public void setPageNumber(long pageNumber){
        this.current = pageNumber;
        this.pageNumber = pageNumber;
    }

    @Override
    @JSONField(serialize = false)
    public IPage<T> setSize(long size) {
        this.size = size;
        this.pageSize = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return current;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

}
