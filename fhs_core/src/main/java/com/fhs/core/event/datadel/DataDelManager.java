package com.fhs.core.event.datadel;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataDelManager {

    Map<String, List<DataDelListener>> namespaceListenerMap = new HashMap<>();


    public void registerListener(DataDelListener listener) {
        //listener  namespaces

    }

    public void onDel(String namespace, Object pkey) {
        // 如果namespace是空的话，不理他
        //从map中get出list调用list的onDel
    }
}
