package com.fhs.core.base.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    private long size;

    @ApiModelProperty("一共多少页")
    private long current;


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
        setTotal( total);
        return this;
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
