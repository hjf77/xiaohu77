<!--
  模块名称：发起流程
  开发人员：彭梦倩
  创建时间:2022/12/2 14:00
-->
<template>
    <div class="app-container">
      <pagex-crud
        :columns="columns"
        :api="api"
        :methodType="methodType"
      >
      </pagex-crud>
    </div>
  </template>
  <script>
  export default {
    name: "processCreate",
    components: {},
    data() {
      return {
        api: '/basic/ms/process-definition/list?suspensionState=' + 1,
        methodType: 'GET',
        isEdit: false,
        open: false,
        ruleId: '',
        columns: [
          {label: '流程名称', name: 'name'},
          {label: '流程分类', name: 'transMap.categoryName'},
          {label: '流程版本', name: 'processDefinition.version',},
          {label: '流程描述', name: 'description'},
          {
            label: '操作',
            name: 'operation',
            type: 'textBtn',
            width: '100px',
            textBtn: [
              {
                title: "选择",
                icon: 'el-icon-plus',
                type: "text",
                size: 'mini',
                click: (_row) => {
                  // this.$set(this, "init", _row);
                  this.$router.push({
                    path: '/bpm/processDetail',
                    query:{id: _row.id}
                  });
                }
              },
            ],
          }
        ],
      };
    },
    created() {
     
    },
    methods: {}
  };
  </script>