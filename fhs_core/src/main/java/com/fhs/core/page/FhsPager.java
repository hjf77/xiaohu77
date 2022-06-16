package com.fhs.core.page;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用来分页使用
 *
 * @param <T>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FhsPager<T> implements IPage<T> {

    private List<T> records;

    private List<T> rows;

    private long total;

    private long size;

    private long current;


    private long pageSize;

    private long pageNumber;

    private Map<String, Object> extMap;


    @Override
    public List<T> getRecords() {
        return records;
    }

    public List<T> getRows() {
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
