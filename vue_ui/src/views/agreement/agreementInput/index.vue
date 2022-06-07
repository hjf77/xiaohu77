<!--
  模块名称：采购协议录入总汇
  开发人员：马军伟
  创建时间: 2022-5-30
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
    >
    </pagex-crud>
  </base-container>
</template>

<script>
import crudMixins from '@/mixins/crudMixins';
//这个路径可能需要改下

export default {
  name: 'agreementAgreement',
  mixins: [crudMixins],
  data() {
    return {
      api: '/purchase/ms/agreementAgreement/pagerAdvance',
      sortSett: [
        {
          direction: 'DESC',
          property: 'createTime',
        },
      ],
      buttons: [
        {
          title: '新增',
          type: 'primary',
          size: 'mini',
          permission: ['agreementAgreement:add'],
          click: (_row) => {
            this.$router.push({
              path: '/agreementInput/agreementDetail'
            })
          }
        }
      ],
      columns: [
        { label: '序号', type: 'index',fixed: 'left' },
        { label: '协议号', name: 'no', width: 150, type: 'formart',
          formart: "<a style='cursor:pointer;color: #1c84c6'>${no}</a>",
          click: function (_row) {
            this.$router.push({path: '/agreementInput/agreementDetail',query:{id: _row.id}});
          }
        },
        { label: '状态', name: 'statusName', width: 150 },
        { label: '订单方', name: 'orderPartyIdName' },
        { label: '供应商', name: 'supplierIdName', width: 80 },
        { label: '合同编号', name: 'contractNo', width: 150 },
        { label: '明细数', name: 'detailNum', width: 150 },
        { label: '所属组织', name: 'orgName', width: 150 },
        { label: '创建人', name: 'createUserUserName', width: 150 },
        { label: '创建时间', name: 'createTime', width: 150 },
        { label: '最后修改人', name: 'updateUserUserName', width: 150 },
        { label: '最后修改时间', name: 'updateTime', width: 150 },
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          fixed: 'right',
          textBtn: [
            {
              title: '审核',
              type: 'primary',
              permission: ['agreementAgreement:audit'],
              size: 'mini',
              ifFun: (_row) => {
                return _row.status === 0;
              },
              click: (_row) => {
                // 审核
              }
            },
            {
              title: '编辑',
              type: 'bottom',
              permission: ['agreementAgreement:update'],
              size: 'mini',
              click: (_row) => {
                this.$router.push({path: '/agreementInput/agreementDetail',query:{id: _row.id}});
              }
            },
            {
              title: "删除",
              type: "danger",
              size: "mini",
              permission: ['agreementAgreement:del'],
              idFieldName:'id',
              api:'/purchase/ms/agreementAgreement/'
            }
          ]
        }
      ],
      filters: [
        {
          name:"no",
          label:"协议号",
          type:"select",
          operation:"=",
          url: '/purchase/ms/agreementAgreement/findList?no=',
          methodType: 'GET',
          valueField: 'no',
          labelField: 'no',
          remote: true,
          placeholder: "请输入协议号"
        },
        {
          name: 'status',
          label: '状态：',
          type: 'select',
          operation:"=",
          dictCode: 'agreementState',
          placeholder: "请选择状态"
        },
        {
          name:"orderPartyId",
          label:"订单方",
          type:"select",
          operation:"=",
          url: '/purchase/ms/supplierOrderParty/findList?orderPartyId=',
          methodType: 'GET',
          valueField: 'id',
          labelField: 'name',
          remote: true,
          placeholder: "请输入订单方"
        },
        {
          name: 'goodsCode',
          label: '商品代码：',
          // type: 'select',
          type: 'text',
          operation:"=",
          dictCode: 'supplierState',
          placeholder: "请输入商品代码"
        }
      ],
      querys: [],
    };
  },
  created() {},
  computed: {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.crud-class {
  .mar-btm-20 {
    margin-bottom: 20px;
  }
}
</style>
