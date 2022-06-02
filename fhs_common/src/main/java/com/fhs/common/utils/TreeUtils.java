package com.fhs.common.utils;

import com.fhs.common.tree.TreeNode;
import com.fhs.common.tree.Treeable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * tree数据格式化工具类
 */
public class TreeUtils {

    public static List<TreeNode<Treeable>> formartTree(List datas, String rootId) {
        if (datas.isEmpty()) {
            return new ArrayList<>();
        }
        if (!(datas.get(0) instanceof Treeable)) {
            throw new RuntimeException("PO请实现Treeable接口");
        }

        List<TreeNode<Treeable>> result = new ArrayList<>();
        Map<Serializable, TreeNode<Treeable>> nodeMap = new LinkedHashMap<>();
        TreeNode<Treeable> tempNode = null;
        List tempDatas = datas;
        List<Treeable> treeDatas = tempDatas;
        for (Treeable tree : treeDatas) {
            tempNode = new TreeNode();
            tempNode.setId(tree.getTreeNodeId());
            //兼容老代码
            tempNode.setName(tree.getTreeNodeName() != null ? tree.getTreeNodeName() : tree.getName());
            tempNode.setParentId(tree.getTreeNodeParentId());
            tempNode.setData(tree);
            nodeMap.put(tempNode.getId(), tempNode);
        }
        for (Serializable id : nodeMap.keySet()) {
            tempNode = nodeMap.get(id);
            if (nodeMap.containsKey(tempNode.getParentId())) {
                nodeMap.get(tempNode.getParentId()).getChildren().add(tempNode);
            } else {
                result.add(tempNode);
            }
        }
        if (rootId != null) {
            return result.stream().filter(node -> {
                return node.getId().equals(rootId);
            }).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 格式化tree数据
     *
     * @param datas
     * @return
     */
    public static List<TreeNode<Treeable>> formartTree(List datas) {
        return formartTree(datas, null);
    }

}
