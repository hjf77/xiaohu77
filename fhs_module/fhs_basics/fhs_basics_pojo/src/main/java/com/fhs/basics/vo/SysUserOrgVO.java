package com.fhs.basics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户下拉tree DTO
 * @author user
 * @date 2020-05-18 15:38:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserOrgVO {

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
     * 是否是用户
     */
    private Integer isUser;

    /**
     * 子集合
     */
    private List<SysUserOrgVO> children = new ArrayList<>();

}
