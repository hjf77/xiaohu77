package com.fhs.flow.comon.utils;

import com.fhs.flow.comon.pojo.PageParam;

/**
 *
 * @author 芋道源码
 */
public class PageUtils {

    public static int getStart(PageParam pageParam) {
        return (pageParam.getPageNo() - 1) * pageParam.getPageSize();
    }

}
