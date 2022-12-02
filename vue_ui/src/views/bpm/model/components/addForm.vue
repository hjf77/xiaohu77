<!--
模块名称：新增流程
开发人员：彭梦倩
创建时间:2022/11/24 16:21
-->

<template>
    <div>
      <!-- 添加或修改参数配置对话框 -->
      <pagex-form
        addApi="/basic/ms/model/create"
        :controls="controls"
      >
      </pagex-form>
    </div>
  </template>
  
  <script>
  export default {
    name: "addForm",
    props: {
      addOpen: Boolean,
      isAdd: Boolean,
    },
    data() {
      return {
        controls: [
          {
            type: 'text',
            name: 'key',
            label: '流程标识',
            rule:'required',
            placeholder: '请输入流程标识'
          },
          {
            type: 'text',
            name: 'name',
            label: '流程名称',
            rule:'required',
            placeholder: '请输入流程名称'
          },
          {
            type: 'textarea',
            name: 'description',
            label: '流程描述',
            row: 2,
            placeholder:'请输入流程描述'
          },
        ],
      }
    },
    mounted() {
    },
    created() {},
    methods: {}
  }
  </script>
  