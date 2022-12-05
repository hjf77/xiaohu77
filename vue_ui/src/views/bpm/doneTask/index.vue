<!--
  模块名称：已办任务
  开发人员：chuyemiao
  创建时间:2022/12/2 
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
    name: "done",
    components: {
    },
    data() {
      return {
        namespace: 'done',
        api: '/basic/ms/task/done-page',
        methodType: 'post',
        init: {},
        buttons: [
        ],
        columns: [
          {label: '任务编号', name: 'id',width:'300'},
          {label: '任务名称', name: 'name',width:'130'},
          {label: '所属流程', name: 'processInstance.name',width:'130'},
          {label: '流程发起人', name: 'processInstance.startUserNickname',width:'130' },
          {label: '结果', name: 'transMap.resultName',type:'formart', 
          formart: function(row) {
            let color = "#13ce66";
            if (row.result == 2) {
              color = "#09DD84";
            }
            if (row.result == 4) {
              color = "#ff4949";
            }
            return ( "<div style='color:" + color + ";'>" + row.transMap.resultName + "</div>" );
          },},
          {label: '审批意见', name: 'reason', type: '',width:'200'}, 
          {label: '创建时间', name: 'createTime',width:'150'},
          {label: '审批时间', name: 'endTime',width:'150'},
          {label: '耗时', name: 'durationInMillis',
          },
          {
            label: '操作',
            name: 'operation',
            type: 'textBtn',
            width: '100px',
            textBtn: [
            {
              
              title: "详情",
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
    created() {
     
    },
    methods: {
    }
  };
  </script>
  <style lang="scss" scoped>
  ::v-deep .button-wrap{
      display: block !important;
  }
</style>
  