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
      <template slot="methods" slot-scope="slot">
        <el-tree
          :data="permissionTreeOptions"
          show-checkbox
          ref="permissions"
          node-key="id"
          :default-expanded-keys="expandedKeys"
          empty-text="加载中，请稍后"
          :props="menuProps"
        ></el-tree>
      </template>
    </pagex-form>

  </div>
</template>
<script>
    import {mapGetters} from "vuex";
    import md5 from "js-md5";

    export default {
        name: "roleForm",
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
                    methods: this.isEdit ? this.init.methods : [],
                    organizationId: this.init.organizationId,
                    roleId: this.init.roleId,
                },
                onSubmit:(model) =>  {
                    model.methods = this.$refs.permissions.getCheckedKeys();
                    if(model.methods && model.methods.length>0){
                        return true;
                    }
                    this.msgError('您至少为此角色分配一个权限');
                    return false;
                },
                addApi: "/ms/sysRole/",
                updateApi: "/ms/sysRole/",
                permissionTreeOptions:[],
                expandedKeys: [],
                menuProps: {
                    children: 'children',
                    label: 'name'
                },
                controls: [
                    {
                        type: "text",
                        name: "roleName",
                        label: "角色名称",
                        rule: "required",
                    },

                    {
                        type: "treeSelect",
                        name: "organizationId",
                        label: "部门",
                        rule: "required",
                        query: {},
                        api: 'ms/sysOrganization/tree'
                    },
                    {
                        type: "select",
                        name: "isEnable",
                        label: "状态",
                        rule: "required",
                        width:'740',
                        dictCode: "isEnable",
                        isValueNum: true,
                    },
                    {
                        type: "slot",
                        name: "methods",
                        label: "权限",
                    },
                    {
                        type: "textarea",
                        name: "remark",
                        label: "备注",
                    },
                ],
            }
        },
        created() {
            this.initPermissionOptions();
        },
        mounted(){
            this.$refs.permissions.setCheckedKeys(this.init.methods);
        },
        methods: {
            initPermissionOptions() {
                //部门切换后角色也切换
                this.$pagexRequest({
                    url: "/ms/sysMenu/getMenuPermissionTree",
                    method: "get",
                }).then((res) => {
                    this.permissionTreeOptions = res;
                })
            }
        }
    };
</script>
