<!--
  模块名称：订单方状态管理
  开发人员：梁晓涛
  创建时间: 2022-06-02
-->
<template>
  <base-container>
    <pagex-crud
      class="crud-class"
      ref="crud"
      :columns="columns"
      :api="api"
      :filters="filters"
      :sortSett="sortSett"
      :buttons="buttons"
      :querys="querys"
      namespace="supplierOrderStatus"
    >
      <template v-slot:form="">
        <!-- 新增 弹框-->
        <pagex-dialog
          v-if="open"
          class="pagex-dialog-theme"
          slot="form"
          :title="title"
          :visible.sync="open"
          namespace="supplierOrderStatus"
        >
          <orderPartyStatusForm  :init="init" :isEdit="isEdit"></orderPartyStatusForm>
        </pagex-dialog>
      </template>
    </pagex-crud>
  </base-container>
</template>

<script>
import crudMixins from '@/mixins/crudMixins';

import orderPartyStatusForm from "@/views/supplier/orderStatus/components/orderPartyStatusForm.vue";

export default {
  name: 'orderPartyStatus',
  mixins: [crudMixins],
  components: {
    orderPartyStatusForm
  },
  data() {
    return {
      api: '/purchase/ms/supplierOrderStatus/pagerAdvance',
      // 列表排列顺序（更新时间）
      sortSett: [
        {
          direction: 'DESC',
          property: 'createTime',
        },
      ],
      title: '新增',
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [
        {
          title: '新增',
          type: 'primary',
          size: 'mini',
          permission: ['supplierOrderStatus:add'],
          click: (_row) => {
            this.title = '新增';
            this.$set(this, "init", {})
            this.isEdit = false;
            this.open = true;
          },
        }
      ],
      columns: [
        { label: '代码', name: 'orderStatusCode', width: 150, fixed: 'left' },
        { label: '名称', name: 'name', width: 150, fixed: 'left' },
        { label: '是否启用', name: 'isEnableName', width: 80 },
        { label: '业务控制', name: 'businessControlName'},
        { label: '创建人', name: 'transMap.createUserUserName', width: 80 },
        { label: '创建时间', name: 'createTime', width: 150 },
        { label: '修改人', name: 'transMap.updateUserUserName', width: 80 },
        { label: '最后修改时间', name: 'updateTime', width: 150 },
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          fixed: 'right',
          textBtn: [
            {
              title: '编辑',
              type: 'bottom',
              size: 'mini',
              permission: ['supplierSupplier:update'],
              click: (_row) => {
                title: '编辑';
                this.$set(this, "init", _row)
                this.open = true;
                this.isEdit = true;
              },
            },
            {
              title: "删除",
              type: "danger",
              size: "mini",
              permission: ['supplierSupplier:del'],
              idFieldName:'id',
              api:'/purchase/ms/supplierOrderStatus/'
            }
          ],
        },
      ],
      querys: [],
      filters:[
        {
          name: 'isEnable',
          label: '是否启用：',
          type: 'select',
          dictCode: 'yesOrNo',
        },
      ]
    };
  },
  created() {},
  computed: {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.crud-class {
}
.mar-btm-20 {
  margin-bottom: 20px;
}
</style>
