<!--
  模块名称：用户表单
  开发人员：王磊
  创建时间:2021/7/26 14:23
-->

<template>
  <div>

    <!-- 编辑参数配置对话框 -->
    <pagex-form
      :isEdit="isEdit"
      :addApi="addApi"
      :updateApi="updateApi"
      :data="formData"
      :init="init"
      :controls="controls"
      :isHaveAddBtn="true"
      :onSubmit="onSubmit"
    >
    </pagex-form>

  </div>
</template>
<script>
    import {mapGetters} from "vuex";
    import md5 from "js-md5";

    export default {
        name: "userForm",
        inject: ["reloadable"],
        props: {
            data: Object,
            isEdit: Boolean,
            init: Object,
            org: Object
        },
        computed: {
            ...mapGetters(["user"])
        },
        data() {
            return {
                formData: {
                    roleIds: this.isEdit ? this.init.roleIds : [],
                    organizationId: this.init.organizationId,
                    isEnable:1,
                    userId: this.init.userId,
                },
                onSubmit: function (model) {
                    model.roleIds = model.roleIds.join(',');
                    model.password = model.password != 'defaultPass' ? md5(model.password) : model.password;
                    return true;
                },
                addApi: "/basic/ms/sysUser/",
                updateApi: "/basic/ms/sysUser/",
                controls: [
                    {
                        type: "text",
                        name: "userName",
                        label: "姓名",
                        rule: "required",
                        mock:'@name'
                    },
                    {
                        type: "treeSelect",
                        name: "organizationId",
                        label: "部门",
                        rule: "required",
                        query: {},
                        url: '/basic/ms/sysOrganization/tree',
                        selectOn: (node) => {
                            this.changeRoleSelect(node.id);
                        }
                    },
                    {
                        type: "text",
                        name: "userLoginName",
                        label: "账号",
                        rule: "required",
                        mock:'@userName'
                    },
                    {
                        type: "password",
                        name: "password",
                        label: "密码",
                        rule: "required",
                    },
                    {
                        type: "select",
                        name: "roleIds",
                        label: "角色",
                        rule: "required",
                        options: [],
                        labelField: 'roleName',
                        valueField: 'roleId',
                        multiple: true
                    },
                    {
                        type: "select",
                        name: "isEnable",
                        label: "状态",
                        rule: "required",
                        dictCode: "isEnable",
                        isValueNum: true
                    },
                    {
                        type: "text",
                        name: "email",
                        label: "邮箱",
                        rule: "email|required",
                        isValueNum: true,
                        mock:'@email'
                    },
                    {
                        type: "text",
                        name: "mobile",
                        label: "手机号",
                        rule: "mobile|required",
                        mock:'@mobile'
                    },
                    {
                        type: "select",
                        name: "sex",
                        label: "性别",
                        dictCode: "sex",
                        isValueNum: true
                    },
                    {
                        type: "textarea",
                        name: "remark",
                        label: "备注"
                    },
                ],
                isEditFirst: true //是否是第一次加载
            }
        },
        created() {
            this.changeRoleSelect(this.init.organizationId);
        },
        methods: {
            changeRoleSelect(orgId) {
                //部门切换后角色也切换
                this.$pagexRequest({
                    url: "/basic/ms/sysRole/findList?organizationId=" + orgId,
                    method: "get",
                }).then((res) => {
                    if (!this.isEditFirst) {
                        this.formData.roleIds = [];
                    }
                    this.isEditFirst = false;
                    //如果改字段顺序这里可能会出坑
                    this.controls[4].options = res;
                })
            }
        }
    };
</script>
