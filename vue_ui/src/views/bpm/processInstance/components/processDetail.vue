<!--
模块名称：流程详情
开发人员：彭梦倩
创建时间:2022/11/30 17:00
-->

<template>
  <div>
    <!-- 添加或修改参数配置对话框 -->
    <pagex-formDetail
      :init="init"
      :controls="controls"
    >
    </pagex-formDetail>
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
export default {
  name: "editForm",
  components: { MyProcessViewer },
  props: {
    open: Boolean,
    init: Object,
    isEdit: Boolean,
  },
  data() {
    return {
      activityList: [],
      processInstance: {},
      id:'',
      controls: [
        {
          type: 'text',
          name: 'key',
          label: '请假天数',
          rule:'required',
          placeholder: '请输入请假天数',
          disabledOn: "disabled",
          isValueNum: true,
        },
        {
          type: 'text',
          name: 'name',
          label: '请假说明',
          rule:'required',
          placeholder: '请输入请假说明',
          disabledOn: "disabled",
          isValueNum: true,
        },
      ],
    }
  },
  mounted() {
  },
  created() {
    this.id = this.$route.query.id;
    if (this.id) {
      this.$pagexRequest({
        url: '/basic/ms/process-instance/get?id=' + this.id,
        method: 'get'
      }).then((res) => {
        this.processInstance = res.data;
      })
      console.log('this.processInstance',this.processInstance);
    }
    this.$pagexRequest({
    //   url: '/basic/ms/activity/list?id=' + this.processInstance.id,
      method: 'get'
    }).then((res) => {
      this.activityList = res.data;
    })
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