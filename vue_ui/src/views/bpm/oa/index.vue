<!--
  模块名称：请假查询
  开发人员：彭梦倩
  创建时间:2022/12/1 18:00
-->
<template>
  <div class="app-container">
    <pagex-crud
      :filters="filters"
      :columns="columns"
      :api="api"
      :buttons="buttons"
      :namespace="namespace"
      :querys="querys"
    >
      <template v-slot:form="prop">
        <pagex-dialog slot="form" :title="title"  :visible.sync="open" :before-close="closeFn"  class="pagex-dialog-theme">
          <leaveCreate v-if="isAdd"></leaveCreate>
          <leaveDetail v-else :id="id"></leaveDetail>
        </pagex-dialog>
      </template>
    </pagex-crud>
  </div>
</template>
<script>
import leaveCreate from "./components/leaveCreate.vue";
import leaveDetail from "./components/leaveDetail.vue";
export default {
  name: "processInstance",
  components: { leaveCreate, leaveDetail },
  data() {
    return {
      namespace: 'oa-leave',
      api: '/basic/ms/oa-leave/page',
      isAdd: false,
      open: false,
      title: '',
      id: '',
      buttons: [
        {
          title: '发起请假',
          type: 'primary',
          size: 'mini',
          icon: 'el-icon-plus',
          click: () => {
            this.title = '发起OA请假';
            this.open = true;
            this.isAdd = true;
          }
        },
      ],
      columns: [
        {label: '申请编号', name: 'id'},
        {label: '状态', name: 'transMap.resultName'},
        {label: '开始时间', name: 'startTime' },
        {label: '结束时间', name: 'endTime'},
        {label: '请假类型', name: 'transMap.typeName'},
        {label: '原因', name: 'reason'},
        {label: '申请时间', name: 'createTime',},
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          fixed: 'right',
          textBtn: [
            {
              title: "取消请假",
              icon: 'el-icon-delete',
              type: "text",
              size: 'mini',
              ifFun: (_row) => {
                return _row.result === 1; // 运行中有取消权限
              },
              click: (_row) => {
                this.$prompt('请输入取消原因？', "取消流程", {
                  type: 'warning',
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  inputPattern: /^[\s\S]*.*\S[\s\S]*$/, // 判断非空，且非空格
                  inputErrorMessage: "取消原因不能为空",
                }).then(({ value }) => {
                  const id = _row.processInstanceId;
                  const reason = value;
                  this.$pagexRequest({
                    url: '/basic/ms/process-instance/cancel',
                    method: 'DELETE',
                    data: { id, reason}
                    }).then((res) => {
                    this.$set(this, "init", res.data);
                  })
                }).then(() => {
                  this.$refs.crud.search();
                  this.$modal.msgSuccess("取消成功");
                })
              }
            },
            {
              title: "详情",
              icon: 'el-icon-view',
              type: "text",
              size: 'mini',
              click: (_row) => {
                this.$set(this, "id", _row.id);
                this.title = '查看OA请假';
                this.open = true;
                this.isAdd = false;
              }
            },
            {
              title: "审批进度",
              icon: 'el-icon-edit',
              type: "text",
              size: 'mini',
              click: (_row) => {
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
          {label: '请假类型', name: 'type', type: 'select', dictCode: 'bpm_oa_leave_type'},
          {label: '申请时间', name: 'createTime', type: 'text', type: 'daterange',},
          {label: '结果', name: 'result', type: 'select', dictCode: 'bpm_process_instance_result'},
          {label: '原因', name: 'reason', type: 'text'},
      ],
      querys: [],
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