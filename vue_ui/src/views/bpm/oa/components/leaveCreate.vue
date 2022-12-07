<!--
模块名称：新增流程
开发人员：彭梦倩
创建时间:2022/11/24 16:21
-->

<template>
    <div>
      <!-- 添加或修改参数配置对话框 -->
      <pagex-form
        addApi="/basic/ms/oa-leave/create"
        :controls="controls"
      >
      </pagex-form>
    </div>
  </template>
  
  <script>
  export default {
    name: "leaveCreate",
    data() {
      return {
        controls: [
          {
            type: "dates",
            name: "startTime",
            label: "开始时间",
            formatVal: "timestamp",
            rule: "required",
            isValueNum: true,
          },
          {
            type: "dates",
            name: "endTime",
            label: "结束时间",
            formatVal: "timestamp",
            rule: "required",
            isValueNum: true,
          },
          {
            type: "select",
            name: "type",
            label: "请假类型",
            rule:'required',
            dictCode: 'bpm_oa_leave_type',
            placeholder: '请选择请假类型'
          },
          {
            type: "text",
            name: "reason",
            label: "原因",
            rule:'required',
            placeholder: '请输入原因'
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
  