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
        <template v-slot:form="prop">
          <!-- 修改 弹框-->
          <pagex-dialog slot="form"
           :title="title"  
           :visible.sync="open" 
           :before-close="closeFn" 
            class="pagex-dialog-theme">
            <editForm v-if="isEdit" :init="init" 
            :isEdit="isEdit">编辑</editForm>
            <addForm v-else :isAdd="isAdd">新增</addForm>
          </pagex-dialog>
        </template>
        
        <!-- <template v-slot:topSlot="prop">
          <pagex-dialog slot="topSlot" width="1200px" :title="ruleTitle" :namespace="namespace"
            v-if="openRules" :visible.sync="openRules" class="pagex-dialog-theme">
            <div class="pagex-dialog-theme">
              <allocationRules :ruleId="ruleId" :init="init"></allocationRules>
            </div>
          </pagex-dialog>
        </template> -->
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
        openRules: false,
        methodType: 'post',
        ruleTitle: '任务分配规则',
        isEdit: false,
        isAdd: false,
        addOpen: false,
        init: {},
        ruleId: '',
        buttons: [
        ],
        columns: [
          {label: '任务编号', name: 'id',width:'300'},
          {label: '任务名称', name: 'name',width:'230'},
          {label: '所属流程', name: 'processInstance.name',width:'230'},
          {label: '流程发起人', name: 'processInstance.startUserNickname',width:'200' },
          {label: '创建时间', name: 'createTime',width:'200'},
          {label: '状态', name: 'transMap.suspensionStateName',width:'230', type: "formart",
          formart: function(row) {
            let color = "#13ce66";
            if (row.suspensionState == 1) {
              color = "#09DD84";
            }
            return ( "<div style='color:" + color + ";'>" + row.transMap.suspensionStateName + "</div>" );
            // return row.suspensionState === 1 ?  row.transMap.suspensionStateName +
            //       '<span style="color:#13ce66;">' : "";
          },},
          {
            label: '操作',
            name: 'operation',
            type: 'textBtn',
            width: '200px',
            textBtn: [
              {
                title: "审批",
                // icon: 'el-icon-delete',
                type: "text",
                api: '/basic/ms/dictItem/',
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
        filters: [
        {label: '流程名', name: 'name', type: 'text'},
        {label: '创建时间', name: 'createTime', type: 'date-picker',},
      ]
      };
    },
    created() {
     
    },
    methods: {
      closeFn(){
        this.open = false;
      },
    }
  };
  </script>
  