package com.fhs.core.base.vo;

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

/**
 * 用来分页使用
 * @param <T>
 */
@Data
@Builder
@ApiModel("分页对象")
@AllArgsConstructor
@NoArgsConstructor
public class FhsPager<T> implements IPage<T>  {

    @ApiModelProperty("数据")
    private List<T> rows;

    @ApiModelProperty("总条数")
    private long total;

    @ApiModelProperty("每页多少条")
    @JsonIgnore
    private long size;

    @ApiModelProperty("当前第几页")
    @JsonIgnore
    private long current;

    @ApiModelProperty("当前第几页")
    private long page;

    @ApiModelProperty("每页多少条")
    private long pageSize;


    @Override
    public List<OrderItem> orders() {
        return new ArrayList<>();
    }

    @Override
    @JSONField(serialize = false)
    public List<T> getRecords() {
        return new ArrayList<>(this.getRows());
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        this.setRows(records);
        return this;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public void setPage(long page) {
        this.page = page;
        this.current = page;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
        this.size = pageSize;
    }

    @Override
    @JSONField(serialize = false)
    public IPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    @JSONField(serialize = false)
    public long getCurrent() {
        return current;
    }

    @Override
    @JSONField(serialize = false)
    public IPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

}
