<!--
  模块名称：待办任务
  开发人员：chuyemiao
  创建时间:2022/12/1 
-->
<template>
    <div class="app-container">
      <pagex-crud
        :filters="filters"
        :columns="columns"
        :api="api"
        :buttons="buttons"
        :methodType="methodType"
        :namespace="namespace"
      >
      </pagex-crud>
    </div>
  </template>
  <script>
  
  export default {
    name: "todo",
    components: {
    },
    data() {
      return {
        namespace: 'todo',
        api: '/basic/ms/task/todo-page',
        methodType: 'post',
        init: {},
        buttons: [
        ],
        columns: [
          {label: '任务编号', name: 'id'},
          {label: '任务名称', name: 'name',},
          {label: '所属流程', name: 'processInstance.name'},
          {label: '流程发起人', name: 'processInstance.startUserNickname' },
          {label: '创建时间', name: 'createTime'},
          {label: '状态', name: 'transMap.suspensionStateName', type: "formart",
          formart: function(row) {
             return row.suspensionState === 1 ?  `<span style="color: #13ce66">${row.transMap.suspensionStateName} </span>`  : ""
          },},
          {
            label: '操作',
            name: 'operation',
            type: 'textBtn',
            textBtn: [
              {
                title: "审批",
                icon: 'el-icon-edit',
                type: "text",
                size: 'mini',
                click: (row) => {
                  // 点击审批按钮后，先调一次详情接口，获取到processDefinition.formCustomViewPath后，带type跳转页面
                  this.$pagexRequest({
                    url: '/basic/ms/process-instance/get?id=' + row.processInstance.id,
                    method: 'get',
                    }).then((res) => {
                      if (res.data) {
                        const type = res.data.processDefinition.formCustomViewPath;
                        if (type!==null && res.data.businessKey) {
                          this.$router.push({
                            path: '/bpm/processDetail/'+ type,
                            query:{id: row.processInstance.id,
                              businessKey: res.data.businessKey
                            }
                          });
                        } else {
                          this.$router.push({
                            path: '/bpm/processDetail/',
                            query:{id: row.processInstance.id}
                          });
                        }
                      }
                  })
                }
              },
            ],
          }
        ],
        filters: [
        {label: '流程名', name: 'name', type: 'text'},
        {label: '创建时间', name: 'createTime', type: 'date-picker',},
      ]
      };
    },
    created() {},
    methods: {}
  };
  </script>
 <style lang="scss" scoped>
    ::v-deep .button-wrap{
        display: block !important;
    }
 </style>
  