package com.fhs.pagex.tag.grid;

import com.fhs.core.exception.ParamException;

import java.util.HashMap;
import java.util.Map;

/**
 * 列表tag工厂
 *
 * @ProjectName: framework_v2_idea2
 * @Package: com.fhs.pagex.tag.grid
 * @ClassName: GridTagFactory
 * @Author: JackWang
 * @CreateDate: 2018/12/3 0003 19:28
 * @UpdateUser: JackWang
 * @UpdateDate: 2018/12/3 0003 19:28
 * @Version: 1.0
 */
public class GridTagFactory {
    /**
     * key标签名字 value标签对象
     */
    private static Map<String, Class> formTagMap = new HashMap<>();

    /**
     * 根据tag别名获取tag
     *
     * @param tagName tag别名
     * @return tag
     */
    public static Class getTag(String tagName) {
        if (!formTagMap.containsKey(tagName)) {
            throw new ParamException(tagName + "不存在");
        }
        return formTagMap.get(tagName);
    }

    /**
     * 注册tag
     *
     * @param tagName tag别名
     * @param tag     tag
     */
    public static void regTag(String tagName, Class tag) {
        if (formTagMap.containsKey(tagName)) {
            throw new ParamException(tagName + "已存在");
        }
        formTagMap.put(tagName, tag);
    }
}
