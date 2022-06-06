<!--
  模块名称：供应商对账条件
  开发人员：樊思鹏
  创建时间: 2022-6-6
-->
<template>

    <pagex-crudForm namespace="supplierReconciliationConditions" class="changrow"  :label-width="labelWidth" :isVee="false" :controls.sync="formSett.controls"  :title="title" :crudSett="crudSett" :formSett="formSett" :idFieldName="idFieldName" >
    </pagex-crudForm>

</template>

<script>
import crudMixins from '@/mixins/crudMixins';
//这个路径可能需要改下

export default {
  name: 'reconciliationConditions',
  mixins: [crudMixins],
  data() {
    return {
        title:'对账条件配置',
        idFieldName:'groupId',
        crudSett:{
            api: '/purchase/ms/supplierReconciliationConditions/pagerAdvance',
           
        buttons: [
          {
            title: '新增',
            type: 'primary',
            size: 'mini',
            name:'add',
            icon: 'el-icon-plus',
            permission: ['supplierReconciliationConditions:add'],
            click:()=>{
              console.log(123)
            }
          },
        
        ],
        columns: [
        {label: '序号',type: 'index',fixed: 'left'},
        {label: '对账条件代码', name: 'conditionsCode'},
        {label: '对账条件名称', name: 'name'},
        {label: '说明', name: 'remark'},
        {label: '对账日期基数', name: 'transMap.dateBaseName'},
        {label: '对账日期基数缺省值', name: 'transMap.dateBaseOmitName'},
        {label: '对账天数', name: 'days'},
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
                click(){
                    console.log(123)
                }
              },
              {
                title: "删除",
                type: "danger",
                size: 'mini',
                api: ''
              }
            ],
          }
        ],
        filters: [
          {label: '对账条件代码:', name: 'conditionsCode', placeholder: "请输入对账条件代码", type: 'text'},
          {label: '对账条件名称:', name: 'name', placeholder: "请输入对账条件名称", type: 'text', operation: 'like'}
        ],
      },
      formSett:{
        addApi: '/purchase/ms/supplierReconciliationConditions',
        updateApi: '',
        data:{},
        labelWidth:'125px',
        controls:[
          {
            type: 'text',
            name: 'conditionsCode',
            label: '对账条件代码',
            rule: 'required',
            disabledOn:'disabled',
            placeholder: '对账条件代码自动生成'
          }, {
            type: 'text',
            name: 'name',
            label: '对账条件名称',
            rule: 'required',
            placeholder: '请输入对账条件名称'
          }, {
            type: 'text',
            name: 'remark',
            label: '说明',
            placeholder: '请输入说明'
          }, {
            type: 'text',
            name: 'days',
            label: '对账天数',
            rule: 'required',
            placeholder: '请输入对账天数'
          }, {
            type: 'radio',
            changeTag: true,
            name: 'dateBase',
            width:'700',
            label: '对账日期基数',
            rule: 'required',
            dictCode:'supplierReconciliationDateBase',
            isValueNum:true,
          
          }, {
            type: 'radio',
            width:'700',
            dictCode:'supplierReconciliationDateBaseOmit',
            name: 'dateBaseOmit',
            label: '对账日期缺省值',
            isValueNum:true,
            rule: 'required',
            placeholder: '请输入对账日期缺省值'
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
