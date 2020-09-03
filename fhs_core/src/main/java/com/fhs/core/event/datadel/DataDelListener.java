package com.fhs.core.event.datadel;

import java.util.Set;

/**
 * 监听数据删除
 */
public interface DataDelListener {

     void onDel(String namespace,Object pkey);

     Set<String> namespaces();
}
