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
      :methodType="methodType"
      :api="api"
      :buttons="buttons"
      :namespace="namespace"
    >
      <!-- <template v-slot:form="prop">
        <pagex-dialog slot="form" :title="title"  v-if="open" :visible.sync="open"  class="pagex-dialog-theme">
          <processDetail :init="init" :isEdit="isEdit"></processDetail>
        </pagex-dialog>
      </template> -->
    </pagex-crud>
  </div>
</template>
<script>
export default {
  name: "processInstance",
  components: {},
  data() {
    return {
      namespace: 'process-instance',
      api: '/basic/ms/process-instance/my-page',
      methodType: 'GET',
      isEdit: false,
      open: false,
      ruleId: '',
      buttons: [
        {
          title: '发起流程',
          type: 'primary',
          size: 'mini',
          icon: 'el-icon-plus',
          click: () => {
            this.title = '发起流程';
            this.addOpen = true;
            this.open = false;
            this.isAdd = true;
          }
        },
      ],
      columns: [
        {label: '编号', name: 'id'},
        {label: '流程名', name: 'name'},
        {label: '流程分类', name: 'transMap.categoryName'},
        {label: '当前审批任务', name: 'tasks',},
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
              icon: 'el-icon-edit',
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
        {label: '所属流程', name: 'processDefinitionId', type: 'text'},
        {label: '流程分类', name: 'category', type: 'select', dictCode: 'categoryDictDatas'},
        {label: '提交时间', name: 'createTime', type: 'daterange',},
        
        {label: '状态', name: 'status', type: 'select', dictCode: 'bpm_process_instance_status'},
        {label: '结果', name: 'result', type: 'select', dictCode: 'bpm_process_instance_result'},
      ]
    };
  },
  created() {
   
  },
  methods: {}
};
</script>