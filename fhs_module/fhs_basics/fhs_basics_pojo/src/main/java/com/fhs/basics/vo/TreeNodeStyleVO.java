package com.fhs.basics.vo;


import java.io.Serializable;

/**
 * ZTree树节点样式Model
 *
 * @author jackwong
 * @date 2020-05-18 15:38:33
 */
public class TreeNodeStyleVO implements Serializable {

    private static final long serialVersionUID = 6708467867386985283L;

    private String color = null;

    public TreeNodeStyleVO() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
