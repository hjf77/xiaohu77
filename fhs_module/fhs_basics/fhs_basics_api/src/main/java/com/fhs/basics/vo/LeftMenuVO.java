package com.fhs.basics.vo;

import com.fhs.core.base.pojo.BaseObject;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 左侧菜单.
 *
 * @Filename: LeftMenu.java
 * @Description:
 * @Version: 1.0
 * @Author: jackwang
 * @Email: wanglei@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司 Copyright (c) 2017 All Rights Reserved.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeftMenuVO extends BaseObject<LeftMenuVO> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * id.
     */
    private String id;

    /**
     * 菜单名称.
     */
    private String name;

    /**
     * url.
     */
    private String url;

    /**
     * 排序.
     */
    private int orderIndex;

    /**
     * 子菜单.
     */
    private List<LeftMenuVO> sonMenu = new ArrayList<>();

    /**
     * 菜单服务器id
     */
    private int menuServer;

    /**
     * 图标-自己上传
     */
    private String image;

    /**
     * iframe模式 服务器url
     */
    private String serverUrl;

    /**
     * 图标
     */
    private String icon;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 菜单是否显示
     */
    private Integer isShow;
}
