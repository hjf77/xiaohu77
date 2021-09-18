package com.fhs.pagex.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * tree VO
 *
 * @author user
 * @date 2020-05-19 14:20:00
 */
@Data
public class TreeVO {
    /**
     * 名字
     */
    private String text;
    /**
     * id
     */
    private String id;
    /**
     * 父id
     */
    private String parentId;

    /**
     * 子集合
     */
    private List<TreeVO> children = new ArrayList<>();
}
