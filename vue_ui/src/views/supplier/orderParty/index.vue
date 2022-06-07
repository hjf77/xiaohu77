<!--
  模块名称：订单方资料管理
  开发人员：何静静
  创建时间: 2022-6-6
-->
<template>
  <base-container>
    <pagex-crud
      class="crud-class"
      ref="crud"
      :filters="filters"
      :columns="columns"
      :api="api"
      :sortSett="sortSett"
      :buttons="buttons"
      :querys="querys"
      namespace="supplierOrderParty"
    >
      <template v-slot:form>
        <!-- 新增 弹框-->
        <pagex-dialog
          v-if="open"
          class="pagex-dialog-theme"
          slot="form"
          :title="title"
          :visible.sync="open"
          namespace="supplierOrderParty"
        >
          <!-- 新增 -->
          <pagex-form
            :addApi="addApi"
            :controls="controls"
            :onSubmit="onSubmit"
            :isHaveAddBtn="true"
            :isVee="false"
            :isEdit="isEdit"
            namespace="supplierOrderParty"
          ></pagex-form>
        </pagex-dialog>
      </template>
    </pagex-crud>
  </base-container>
</template>

<script>
import crudMixins from '@/mixins/crudMixins';
//这个路径可能需要改下

export default {
  mixins: [crudMixins],
  data() {
    return {
      api: '/purchase/ms/supplierOrderParty/pagerAdvance',
      // 列表排列顺序（更新时间）
      sortSett: [
        {
          direction: 'DESC',
          property: 'createTime',
        },
      ],
      title: '新增',
      addApi: '/purchase/ms/supplierOrderParty',
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [
        {
          title: '新增',
          type: 'primary',
          size: 'mini',
          permission: ['supplierOrderParty:add'],
          click: (_row) => {
            this.title = '新增';
            this.isEdit = false;
            this.open = true;
          },
        },
        {
          title: '导出',
          type: '',
          size: 'mini',
          // permission: ['sysUser:add'],
          click: (_row) => {
            this.title = '导出';
            this.$set(this, 'init', { organizationId: this.org.id });
            this.isEdit = false;
            this.open = true;
          },
        },
      ],
      controls: [
        {
          type: 'text',
          name: 'name',
          label: '订单方名称',
          rule: [
            {
              required: true,
              message: '订单方名称不能为空',
              trigger: 'blur',
            },
            {
              min: 1,
              max: 128,
              message: '订单方名称字符长度1-128位之间',
            },
          ],
        },
        {
          type: 'select',
          name: 'status',
          label: '状态',
          url: '/purchase/ms/supplierOrderStatus/findList',
          methodType: 'GET',
          valueField: 'id',
          labelField: 'name',
          rule: [
            {
              required: true,
              message: '状态不能为空',
              trigger: 'change',
            },
          ],
        },
      ],
      onSubmit: function (model) {
        return true;
      },
      columns: [
        { label: '序号', type: 'index', fixed: 'left' },
        { label: '订单方代码', name: 'orderPartyCode', width: 150, fixed: 'left',
          type: 'formart',
          formart: "<label style='cursor:pointer'>${orderPartyCode}</label>",
          click: function (_row) {
            this.$router.push({
              path: '/supplier/type/editForm',
              query: { id: _row.id },
            });
            this.isEdit = true;
          },
        },
        // {
        //   label: '供应商代码',
        //   name: 'supplierCodeSupplierCode',
        //   width: 150,
        //   fixed: 'left',
        // },
        { label: '订单方名称', name: 'name',  width: 150 },
        { label: '订单方状态', name: 'transMap.statusName', width: 150 },
        { label: '状态', name: 'transMap.status' },
        { label: '结算模式', name: 'transMap.settlementPatternName', width: 150 },
        { label: '对账条件', name: 'transMap.reconciliationConditionsName', width: 150 },
        { label: '付款条件', name: 'transMap.paymentConditionsName', width: 150 },
        { label: '所属大类', name: 'transMap.categoryIdName', width: 150 },
        { label: '负责人', name: 'transMap.principalIdUserName', width: 150 },
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
              permission: ['supplierOrderParty:update'],
              click: (_row) => {
                this.$router.push({
                  path: '/orderParty/type/editOrderForm',
                  query: { id: _row.id },
                });
                this.isEdit = true;
              },
            },
            {
              title: '删除',
              type: 'danger',
              size: 'mini',
              permission: ['supplierOrderParty:del'],
              idFieldName: 'id',
              api: '/purchase/ms/supplierOrderParty/',
            },
          ],
        },
      ],
      filters: [
        {
          name: 'orderPartyCode',
          label: '订单方代码：',
          placeholder: '订单方代码',
          operation: 'like',
          type: 'text',
        },
        {
          name: 'name',
          label: '订单方名称：',
          placeholder: '订单方名称',
          operation: 'like',
          type: 'text',
        },
        {
          name: 'supplierCode',
          target:"com.fhs.supplier.po.SupplierSupplierPO",
          field:"supplierId",
          label: '供应商代码：',
          placeholder: '供应商代码',
          operation: 'like',
          type: 'text',
        },
        {
          name:"status",
          label:"状态：",
          type:"select",
          url: '/purchase/ms/supplierOrderStatus/findList',
          methodType: 'GET',
          valueField: 'id',
          labelField: 'name',
          placeholder: "请选择状态"
        },
      ],
      querys: [],
    };
  },
  created() {
    // this.statusList()
  },
  computed: {},
  methods: {
    // statusList() {
    //   this.$pagexRequest({
    //     url: '/purchase/ms/supplierOrderStatus/findList',
    //     method: 'GET',
    //   })
    //     .then((res) => {
    //       console.log(res);
    //       this.stateTable = res

    //     })
    //     .catch((err) => {
    //       console.log(err);
    //     });
    // }
  },
};
</script>
<style lang="scss" scoped>
.crud-class {
  .mar-btm-20 {
    margin-bottom: 20px;
  }
}
</style>
