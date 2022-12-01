<!--
模块名称：修改流程
开发人员：彭梦倩
创建时间:2022/11/24 16:21
-->

<template>
    <div>
      <!-- 添加或修改参数配置对话框 -->
      <pagex-form
        updateApi="/basic/ms/model/update"
        :init="init"
        :isEdit="isEdit"
        :controls="controls"
      >
      </pagex-form>
    </div>
  </template>
  
  <script>
  export default {
    name: "editForm",
    props: {
      open: Boolean,
      init: Object,
      isEdit: Boolean,
    },
    data() {
      return {
        controls: [
          {
            type: 'text',
            name: 'key',
            label: '流程标识',
            rule:'required',
            placeholder: '请输入流程标识',
            disabledOn: "disabled",
            isValueNum: true,
          },
          {
            type: 'text',
            name: 'name',
            label: '流程名称',
            rule:'required',
            placeholder: '请输入流程名称',
            disabledOn: "disabled",
            isValueNum: true,
          },
          {
            type: 'select',
            name: 'category',
            label: '流程分类',
            rule:'required',
            placeholder: '请选择流程名称',
            dictCode: 'categoryDictDatas',
            isValueNum: true,
          },
          {
            type: 'textarea',
            name: 'description',
            label: '流程描述',
            row: 2,
            placeholder:'请输入流程描述',
            isValueNum: true,
          },
          {
            type: "select",
            name: "formType",
            label: "表单类型",
            rule:'required',
            dictCode: "bpm_model_form_type",
            isValueNum: true,
            change: (_row, val) => {
              if (val) {
                if (val == 1) {
                  console.log('1111');
                  this.controls[5].isHide = false;
                  this.controls[6].isHide = true;
                  this.controls[7].isHide = true;
                }else{
                  this.controls[5].isHide = true;
                  this.controls[6].isHide = false;
                  this.controls[7].isHide = false;
                }
              }
            }
          },
          {
            type: "select",
            name: "formId",
            label: "流程表单",
            rule:'required',
            isHide: false,
            dictCode: "bpm_model_form_type",
          },
          {
            type: "text",
            name: "formCustomCreatePath",
            label: "表单提交路由",
            rule:'required',
            isHide: true,
          },
          {
            type: "text",
            name: "formCustomViewPath",
            label: "表单查看路由",
            rule:'required',
            isHide: true,
          },
        ],
      }
    },
    mounted() {
    },
    created() {
      console.log(this.isEdit)
    },
    methods: {}
  }
  </script>
  