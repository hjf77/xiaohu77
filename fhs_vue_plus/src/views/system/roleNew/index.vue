<!--
  模块名称：用户列表
  开发人员：王磊
  创建时间:2021/7/23 09:30
-->

<template>
  <base-container>
    <organization slot="org" :isCheckedUserCompany="false" :is-specified-node="false" :type="'2'" @change="orgClick"></organization>
    <template v-if="rightContent">
      <pagex-crud
        ref="pagex-crud"
        v-loading="loading"
        :filters="filters"
        :columns="columns"
        :api="api"
        :buttons="buttons"
        :querys="querys"
        :sortSett="sortSett"
      >
        <template v-slot:form="prop">
          <!-- 新增 修改 弹框-->
          <el-dialog
            v-if="open"
            class="pagex-dialog-theme"
            slot="form"
            :title="title"
            :visible.sync="open"
          >
            <!-- 新增/修改/修订 -->
            <role-form
              :is-edit="isEdit"
              :init="init"
              :org="org"
            >
            </role-form>
          </el-dialog>
        </template>
      </pagex-crud>
    </template>
  </base-container>
</template>
<script>
import crudMixins from "../../../mixins/crudMixins";
import Organization from "../../../components/Organization";
import roleForm from "./components/roleForm";
import {mapGetters} from "vuex"

export default {
  name: "user",
  components: {
      roleForm,
    Organization
  },
  mixins: [crudMixins],
  computed: {
    ...mapGetters(["user"]),
  },
  data() {
    return {
      org: {},
      options: [],
      dialogVisible: false,
      title: '新增',
      loading: false,
      rightContent: false,//右侧内容区域
      api: "/ms/sysRole/pagerAdvance",
      sortSett: [
        {
          direction: "DESC",
          property: "createTime",
        },
      ],
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [
        {
          title: "新增",
          type: "primary",
          size: "mini",
          icon: "el-icon-plus",
          permission: ['sysRole:add'],
          click: (_row) => {
            this.title ='新增';
            this.$set(this, "init", {organizationId:this.org.id})
            this.isEdit = false;
            this.open = true
          }
        },
      ],
      columns: [
        {label: "角色名称", name: "roleName", minWidth: 140, type: 'popover'},
        {label: "状态", name: "isEnableName", width: 140, type: 'popover'},
        {label: "所属机构", name: "transMap.orgName", width: 150},
        {label: "备注", name: "remark", width: 140, type: 'popover'},
        {label: "创建时间", name: "createTime", minWidth: 140, type: 'popover'},
        {
          label: "操作",
          name: "operation",
          type: "textBtn",
          width: '260px',
          textBtn: [
            {
              name: "编辑",
              type: "primary",
              size: "mini",
              permission: ['sysRole:update'],
              click: (_row) => {
                this.title ='编辑';
                this.$pagexRequest({
                    url: "/ms/sysRole/getRolePermissionButtons?roleId=" + _row.roleId,
                    method: "get",
                }).then((res) => {
                    _row.methods = res
                    this.$set(this, "init", _row)
                    this.isEdit = true
                    this.open = true
                })
              },
            },
            {
              name: "删除",
              type: "danger",
              size: "mini",
              permission: ['sysRole:del'],
              idFieldName:'roleId',
              api:'/ms/sysRole/'
            }
          ]
        }
      ],
      filters: [
        {
          formname: "角色名称",
          name: "roleName",
          type: "text",
          operation: "like",
        }
      ],
      querys: [
        {
          operation: "=",
          property: "organizationId",
          relation: "AND",
          value: null,
        }
      ]
    }
  },
  created() {
  },
  methods: {
    /**
     * 获取org
     * @param org
     * @returns {boolean}
     */
    orgClick(val) {
      if (this.org && (val.id === this.org.id)) {
        return false
      }
      this.$set(this, "org", val)
      this.loading = true
      this.rightContent = false
      this.querys[0].value = val.id
      this.$nextTick(() => {
        this.rightContent = true
      })
      setTimeout(() => {
        this.loading = false
      }, 500)
    },

  }
}
</script>

<style lang="scss" scoped>

</style>
