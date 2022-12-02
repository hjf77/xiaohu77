<!--
模块名称：流程详情
开发人员：彭梦倩
创建时间:2022/11/30 17:00
-->

<template>
  <div>
    <router-view v-if="$route.params.type"></router-view>
    <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span class="el-icon-picture-outline">流程图</span>
    </div>
    <my-process-viewer key="designer" v-model="bpmnXML" v-bind="bpmnControlForm" :activityData="activityList"
      :processInstanceData="processInstance" :taskData="tasks" />
  </el-card>
  </div>
</template>

<script>
import MyProcessViewer from "@/components/bpmnProcessDesigner/package/designer/ProcessViewer.vue";
import formManagerVue from "../../formManager/formManager.vue";
export default {
  name: "editForm",
  components: { MyProcessViewer,formManagerVue },
  props: {
    open: Boolean,
    init: Object,
    isEdit: Boolean,
  },
  data() {
    return {
      id:'',
      activityList: [],
      processInstance: {},
      tasks: [],
      // BPMN 数据
      bpmnXML: null,
      bpmnControlForm: {
        prefix: "flowable"
      },
    }
  },
  mounted() {
  },
  created() {
    this.id = this.$route.query.id;
    if (this.id) {
      // processInstance
      this.$pagexRequest({
        url: '/basic/ms/process-instance/get?id=' + this.id,
        method: 'get'
      }).then((res) => {
        if (!res.data) {
          this.$message.error('查询不到流程信息！');
          return;
        }
        this.processInstance = res.data;

        // bpmnXML
        this.$pagexRequest({
          url: '/basic/ms/process-definition/get-bpmn-xml?id=' + this.processInstance.processDefinition.id,
          method: 'get'
          }).then((res) => {
          this.bpmnXML = res.data;
        })

        // activityList
        this.$pagexRequest({
          url: '/basic/ms/activity/list?processInstanceId=' + this.processInstance.id,
          method: 'get'
          }).then((res) => {
          this.activityList = res;
          })
        })

        // tasks
        this.$pagexRequest({
          url: '/basic/ms/task/list-by-process-instance-id?processInstanceId=' + this.id,
          method: 'get'
        }).then((res) => {
            console.log('res',res);
          this.tasks = [];
          // 移除已取消的审批
          res.forEach(task => {
          if (task.result !== 4) {
            this.tasks.push(task);
          }
        });
        })
    }
  },
  methods: {}
}
</script>
<style lang="scss">
.my-process-designer {
  height: calc(100vh - 200px);
}

.box-card {
  width: 100%;
  margin-bottom: 20px;
}
</style>