<!--
  模块名称：我的流程
  开发人员：彭梦倩
  创建时间:2022/11/24 8:00
-->
<template>
  <div class="app-container">
    <pagex-crud
      :filters="filters"
      :columns="columns"
      :api="api"
      :buttons="buttons"
      :namespace="namespace"
    >
      <template v-slot:form="prop">
        <pagex-dialog slot="form" :title="title"  :visible.sync="open" :before-close="closeFn"  class="pagex-dialog-theme">
          <processCreate v-if="isAdd"></processCreate>
        </pagex-dialog>
      </template>
    </pagex-crud>
  </div>
</template>
<script>
import processCreate from './components/processCreate.vue'
export default {
  name: "processInstance",
  components: { processCreate },
  data() {
    return {
      namespace: 'process-instance',
      api: '/basic/ms/process-instance/my-page',
      isAdd: false,
      open: false,
      title: '',
      buttons: [
        {
          title: '发起流程',
          type: 'primary',
          size: 'mini',
          icon: 'el-icon-plus',
          click: () => {
            this.title = '发起流程';
            this.open = true;
            this.isAdd = true;
            // this.$router.push({
            //   path: '/bpm/processDetail',
            // });
          }
        },
      ],
      columns: [
        {label: '编号', name: 'id'},
        {label: '流程名', name: 'name'},
        {label: '流程分类', name: 'transMap.categoryName'},
        {label: '当前审批任务', name: 'tasks',type: "formart",
          formart: ( row ) => {
            if (row.tasks !== null) {
              for (let i = 0; i < row.tasks.length; i++) {
                const item = row.tasks[i];
                return '<el-button><span>'+ item.name +'</span></el-button>'
              }
            } else {
              return;
            }
          },},
        {label: '状态', name: 'transMap.statusName'},
        {label: '结果', name: 'transMap.resultName',},
        {label: '提交时间', name: 'createTime' },
        {label: '结束时间', name: 'endTime'},
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          width: '100px',
          textBtn: [
            {
              title: "详情",
              icon: 'el-icon-view',
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
      filters: [
        {label: '流程名', name: 'name', type: 'text'},
        {label: '所属流程', name: 'processDefinitionId', type: 'text', placeholder: '请输入流程定义的编号'},
        {label: '流程分类', name: 'category', type: 'select', dictCode: 'categoryDictDatas'},
        {label: "提交时间", name: "createTime", type: "date-picker", operation: "between", },
        {label: '状态', name: 'status', type: 'select', dictCode: 'bpm_process_instance_status'},
        {label: '结果', name: 'result', type: 'select', dictCode: 'bpm_process_instance_result'},
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