<!--
    模块名称：分配规则弹窗
    开发人员：彭梦倩
    创建时间:2022/11/29 10:21
-->
<template>
  <div class="app-container">
    <pagex-crud
      :api="api"
      :columns="columns"
    >
    <template v-slot:form="prop">
        <!-- 新增 修改 弹框-->
        <pagex-dialog slot="form" :title="title"  v-if="open" :visible.sync="open"  class="pagex-dialog-theme" append-to-body>
          <processForm :editInit="editInit" :isEdit="isEdit"></processForm>
        </pagex-dialog>
      </template>
    </pagex-crud>
  </div>
</template>
<script>
import processForm from './processForm.vue'
export default {
  name: "",
  props: {
    init: Object,
  },
  components: { processForm },
  data() {
    return {
      api: '/basic/ms/task-assign-rule/list?modelId='+ this.init.id,
      open: false,
      editInit: {},
      title: '修改任务规则',
      columns: [
        {label: '任务名', name: 'taskDefinitionName'},
        {label: '任务标识', name: 'taskDefinitionKey'},
        {label: '规则类型', name: 'transMap.typeName', dictCode: 'bpm_task_assign_rule_type',isValueNum: true,},
        {label: '规则范围', name: 'optionNames',isValueNum: true,},
        {
          label: '操作',
          type: 'textBtn',
          width: '80px',
          textBtn: [
            {
              title: "修改流程",
              icon: 'el-icon-edit',
              type: "text",
              size: 'mini',
              click: (_row) => {
                  _row.modelId = this.init.id;
                  this.$set(this, 'editInit', _row)
                  this.title = '编辑';
                  this.open = true;
                  if (_row.type&&_row.options) {
                    this.isEdit = true;
                  }else {
                    this.isEdit = false;
                  }
              }
            }
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