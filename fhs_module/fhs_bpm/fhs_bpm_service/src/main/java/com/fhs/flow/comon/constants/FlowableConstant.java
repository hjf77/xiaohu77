package com.fhs.flow.comon.constants;

/**
 * @author tanyukun
 */
public interface FlowableConstant {

    /**
     * 流程任务分配规则类型
     */
    public static class TaskAssignRuleType {

        /**
         * 角色
         */
        public static final Integer ROLE = 10;
        /**
         * 用户
         */
        public static final Integer USER = 30;

    }
    /**
     * 后加签
     */
    public static final String AFTER_ADDSIGN = "after";
    //
    /**
     * 前加签
     */
    public static final String BEFORE_ADDSIGN = "before";

    /**
     * 提交人的变量名称
     */
    public static final String FLOW_SUBMITTER_VAR = "initiator";
    /**
     * 提交人节点名称
     */
    public static final String FLOW_SUBMITTER = "提交";
    /**
     * 提交人节点标识
     */
    public static final String FLOW_SUBMITTER_KEY = "auto";

}
