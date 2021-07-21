package com.fhs.common.utils;

import com.fhs.common.tree.TreeNode;
import com.fhs.common.tree.Treeable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * tree数据格式化工具类
 */
public class TreeUtils {

    /**
     * 格式化tree数据
     * @param datas
     * @return
     */
    public static List<TreeNode<Treeable>> formartTree(List datas){
        if(datas.isEmpty()){
            return new ArrayList<>();
        }
        if(!(datas.get(0) instanceof Treeable)){
            throw new RuntimeException("PO请实现Treeable接口");
        }

        List<TreeNode<Treeable>> result = new ArrayList<>();
        Map<String,TreeNode<Treeable>> nodeMap = new LinkedHashMap<>();
        TreeNode<Treeable> tempNode = null;
        List  tempDatas = datas;
        List<Treeable> treeDatas = tempDatas;
        for (Treeable tree : treeDatas) {
            tempNode = new TreeNode();
            tempNode.setId(tree.getId());
            tempNode.setName(tree.getName());
            tempNode.setParentId(tree.getParentId());
            tempNode.setData(tree);
            nodeMap.put(tempNode.getId(),tempNode);
        }
        for (String id : nodeMap.keySet()) {
            tempNode = nodeMap.get(id);
            if(nodeMap.containsKey(tempNode.getParentId())){
                nodeMap.get(tempNode.getParentId()).getChildren().add(tempNode);
            }else{
                result.add(tempNode);
            }
        }
        return result;
    }

}
