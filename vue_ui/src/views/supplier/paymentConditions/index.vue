<!--
  模块名称：供应商付款条件
  开发人员：蔡昱
  创建时间: 2022-5-30
-->

<template>

    <pagex-crudForm :namespace="'supplierPaymentConditions'" class="changrow"  :label-width="labelWidth" :isVee="false" :controls.sync="formSett.controls"  :title="title" :crudSett="crudSett" :formSett="formSett" :idFieldName="idFieldName" >
    </pagex-crudForm>

</template>

<script>
import crudMixins from '@/mixins/crudMixins';
//这个路径可能需要改下

export default {
  name: 'paymentConditions',
  mixins: [crudMixins],
  data() {
    return {
        title:'付账条件配置',
        idFieldName:'id',
        crudSett:{
        api: '/purchase/ms/supplierPaymentConditions/pagerAdvance',
        buttons: [
          {
            title: '新增',
            type: 'primary',
            size: 'mini',
            name:'add',
            icon: 'el-icon-plus',
            permission: ['supplierPaymentConditions:add'],

          },

        ],
        columns: [
        {label: '序号',type: 'index',fixed: 'left'},
        {label: '付款条件代码', name: 'conditionsCode'},
        {label: '付款条件名称', name: 'name'},
        {label: '说明', name: 'remark'},
        {label: '付款日期基数', name: 'transMap.dateBaseName'},
        {label: '固定日天数', name: 'dateBaseDay'},
        {label: '付款日期基数缺省值', name: 'transMap.dateBaseOmitName'},
        {label: '付款天数', name: 'days'},
        {label: '创建人', name: 'transMap.createUserUserName'},
        {label: '创建时间', name: 'createTime'},
        {
            label: '操作',
            name: 'operation',
            type: 'textBtn',
            textBtn: [
              {
                title: "编辑",
                type: "bottom",
                size: 'mini',

              },
              {
                title: "删除",
                type: "danger",
                size: 'mini',
                api: '/purchase/ms/supplierPaymentConditions/'
              }
            ],
          }
        ],
        filters: [
          {label: '付款条件代码:', name: 'conditionsCode', placeholder: "请输入付款条件代码", type: 'text'},
          {label: '付款条件名称:', name: 'name', placeholder: "请输入付款条件名称", type: 'text', operation: 'like'}
        ],
      },
      formSett:{
        addApi: '/purchase/ms/supplierPaymentConditions',
        updateApi: '/purchase/ms/supplierPaymentConditions',
        data:{},
        labelWidth:'125px',
        controls:[
          {
            type: 'text',
            name: 'conditionsCode',
            label: '付款条件代码',
            disabledOn:'disabled',
            placeholder: '付款条件代码自动生成'
          }, {
            type: 'text',
            name: 'name',
            label: '付款条件名称',
            rule: 'required',
            placeholder: '请输入付款条件名称'
          }, {
            type: 'text',
            name: 'remark',
            label: '说明',
            placeholder: '请输入说明'
          }, {
            type: 'text',
            name: 'days',
            label: '付款天数',
            rule: 'required',
            placeholder: '请输入付款天数'
          }, {
            type: 'radio',
            changeTag: true,
            name: 'dateBase',
            width:'700',
            label: '付款日期基数',
            rule: 'required',
            dictCode:'supplierPaymentDateBase',
            isValueNum:true
          }, {
            type: 'select',
            name: 'dateBaseDay',
            label: "固定日",
            url: '/purchase/ms/supplierPaymentConditions/dateBaseDayList',
            rule: 'required',
            labelField: "dictDesc",
            valueField: "dictCode",
            ifFun: (_form) => {
              if (_form.dateBase === 0 || _form.dateBase === 1 || _form.dateBase === 2 || _form.dateBase === 4) {
                return false
              }
                return true
            }
          },{
            type: 'radio',
            width:'700',
            dictCode:'supplierPaymentDateBaseOmit',
            name: 'dateBaseOmit',
            label: '付款日期缺省值',
            isValueNum:true,
            rule: 'required'
          }
        ]
      },

    }
  },
  created() {},
  computed: {},
  methods: {},
};
</script>

<style lang="less" scoped>

::v-deep .el-form-item__content{
    vertical-align: middle ;
}

</style>

