package com.fhs.core.event.datadel;

import java.util.Map;
import java.util.Set;

/**
 * 监听数据删除
 */
public interface DataDelListener {

     void onDel(String namespace, Map<String,Object> paramMap);

     Set<String> namespaces();
}
