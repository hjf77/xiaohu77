package com.fhs.flow.dal.dataobject.definition;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fhs.flow.base.po.BpmBasePO;
import com.fhs.flow.controller.admin.definition.vo.model.BpmModelPageItemRespVO;
import com.fhs.flow.enums.definition.BpmModelFormTypeEnum;
import lombok.*;

import java.util.List;

/**
 * Bpm 流程定义的拓展表
 * 主要解决 Activiti {@link BpmModelPageItemRespVO.ProcessDefinition} 不支持拓展字段，所以新建拓展表
 *
 * @author 芋道源码
 */
@TableName(value = "bpm_process_definition_ext", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BpmProcessDefinitionExtPO extends BpmBasePO  {
    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 表单名
     */
    private String name;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 表单的配置
     */
    private String conf;
    /**
     * 表单项的数组
     *
     * 目前直接将 https://github.com/JakHuang/form-generator 生成的 JSON 串，直接保存
     * 定义：https://github.com/JakHuang/form-generator/issues/46
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> fields;
    /**
     * 备注
     */
    private String remark;

    /**
     * 流程定义的编号
     *
     * 关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;
    /**
     * 流程模型的编号
     *
     * 关联 Model 的 id 属性
     */
    private String modelId;
    /**
     * 描述
     */
    private String description;

    /**
     * 表单类型
     *
     * 关联 {@link BpmModelFormTypeEnum}
     */
    private Integer formType;
    /**
     * 动态表单编号
     * 在表单类型为 {@link BpmModelFormTypeEnum#NORMAL} 时
     *
     * 关联 {@link BpmFormPO#getId()}
     */
    private Long formId;
    /**
     * 表单的配置
     * 在表单类型为 {@link BpmModelFormTypeEnum#NORMAL} 时
     *
     * 冗余 {@link BpmFormPO#getConf()}
     */
    private String formConf;
    /**
     * 表单项的数组
     * 在表单类型为 {@link BpmModelFormTypeEnum#NORMAL} 时
     *
     * 冗余 {@link BpmFormPO#getFields()} ()}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> formFields;
    /**
     * 自定义表单的提交路径，使用 Vue 的路由地址
     * 在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时
     */
    private String formCustomCreatePath;
    /**
     * 自定义表单的查看路径，使用 Vue 的路由地址
     * 在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时
     */
    private String formCustomViewPath;


}
