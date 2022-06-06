<!--
  模块名称：供应商资料管理
  开发人员：何静静
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
  name: 'AgreementInput',
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
        },
        // {
        //   title: '审核',
        //   type: 'primary',
        //   size: 'mini',
        //   permission: ['agreementAgreement:audit'],
        //   click: (_selects, _ids) => {
        //     // 审核业务
        //   }
        // },
        // {
        //   title: '删除',
        //   type: 'primary',
        //   size: 'mini',
        //   permission: ['agreementAgreement:del'],
        //   click: (_selects, _ids) => {
        //     // 删除业务
        //   }
        // }
      ],
      columns: [
        { type: 'selection' },
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
          operation:"find_in_set",
          url: '/purchase/ms/agreementAgreement/findList?no=',
          methodType: 'GET',
          valueField: 'id',
          labelField: 'no',
          remote: true,
        },
        {
          name: 'status',
          label: '状态：',
          type: 'select',
          operation: 'like',
          dictCode: 'agreementState',
        },
        {
          name:"teachersIds",
          label:"订单方",
          type:"select",
          operation:"find_in_set",
          url: '/purchase/ms/supplierOrderParty/findList?orderPartyCode=',
          methodType: 'GET',
          valueField: 'id',
          labelField: 'name',
          remote: true,
        },
        {
          name: 'status',
          label: '商品代码：',
          type: 'select',
          operation: 'like',
          dictCode: 'supplierState',
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
