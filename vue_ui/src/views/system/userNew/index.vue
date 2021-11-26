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
            <user-form
              :is-edit="isEdit"
              :init="init"
              :org="org"
            >
            </user-form>
          </el-dialog>
        </template>
      </pagex-crud>
    </template>
  </base-container>
</template>
<script>
import crudMixins from "../../../mixins/crudMixins";
import Organization from "../../../components/Organization";
import userForm from "./components/userForm";
import {mapGetters} from "vuex"

export default {
  name: "user",
  components: {
      userForm,
    Organization
  },
  mixins: [crudMixins],
  computed: {
    ...mapGetters(["user"]),
  },
  data() {
    return {
      org: {},
      title: '新增',
      loading: false,
      rightContent: false,//右侧内容区域
      api: "/ms/sysUser/pagerAdvance",
      sortSett: [ //默认排序
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
          permission: ['sysUser:add'],
          click: (_row) => {
            this.title ='新增';
            this.$set(this, "init", {organizationId:this.org.id})
            this.isEdit = false;
            this.open = true
          }
        },
      ],
      columns: [
        {label: "姓名", name: "userName", minWidth: 140, type: 'popover'},
        {label: "账号", name: "userLoginName", width: 140, type: 'popover'},
        {label: "性别", name: "sexName", width: 150},
        {label: "手机号", name: "mobile", width: 140, type: 'popover'},
        {label: "邮箱", name: "email", minWidth: 140, type: 'popover'},
        {label: "状态", name: "isEnableName", width: 100},
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
              permission: ['sysUser:update'],
              click: (_row) => {
                this.title ='编辑';
                this.$pagexRequest({
                    url: "/ms/sysUser/info/" + _row.userId,
                    method: "get",
                }).then((res) => {
                    let roleIdArray = [];
                    for (let _index in res.roleList) {
                        roleIdArray.push(Number(res.roleList[_index]));
                    }
                    res.roleIds = roleIdArray;
                    res.password = 'defaultPass';
                    this.$set(this, "init", res)
                    this.isEdit = true
                    this.open = true
                })

              },
            },
            {
              name: "删除",
              type: "danger",
              size: "mini",
              permission: ['sysUser:del'],
              idFieldName:'userId',
              api:'/ms/sysUser/'
            }
          ]
        }
      ],
      filters: [
        {
          formname: "姓名",
          name: "userName",
          type: "text",
          operation: "like",
        },
        {
          formname: "账号:",
          name: "userLoginName",
          type: "text",
          operation: "like",
        },
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
