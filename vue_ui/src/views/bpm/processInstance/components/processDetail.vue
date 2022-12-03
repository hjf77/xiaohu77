<!--
模块名称：流程详情
开发人员：彭梦倩
创建时间:2022/11/30 17:00
-->

<template>
  <div>
    <router-view v-if="$route.params.type"></router-view>
    <!-- 审批任务【老师审批】 -->
   <el-card>
    <div slot="header" class="clearfix">
      <span class="el-icon-picture-outline">审批任务【老师审批】</span>
    </div>
    <el-col :span="16" :offset="6" >
        <el-form :ref="'form' + index"  label-width="100px" :rules="auditRule" :model="auditForms[index]">
          <el-form-item label="流程名" v-if="processInstance && processInstance.name">{{ processInstance.name }}</el-form-item>
          <el-form-item label="流程发起人" > {{ processInstance.startUser.nickname }}
            <el-tag type="info" size="mini">{{ processInstance.startUser.deptName }}</el-tag>
          </el-form-item>
          <el-form-item label="审批建议" prop="reason" >
            <el-input type="textarea"  placeholder="请输入审批建议"  v-model="auditForms.reason"/>
          </el-form-item>
        </el-form>
        <div style="margin-left: 10%; margin-bottom: 20px; font-size: 14px;">
          <el-button  icon="el-icon-edit-outline" type="success" size="mini" @click="handleAudit(item, true)">通过</el-button>
          <el-button  icon="el-icon-circle-close" type="danger" size="mini" @click="handleAudit(item, false)">不通过</el-button>
          <el-button  icon="el-icon-edit-outline" type="primary" size="mini" @click="handleUpdateAssignee(item)">转办</el-button>
          <el-button icon="el-icon-edit-outline" type="primary" size="mini" @click="handleDelegate(item)">委派</el-button>
          <el-button icon="el-icon-refresh-left" type="warning" size="mini" @click="handleBack(item)">退回</el-button>
        </div>
      </el-col>
   </el-card>
 <!-- 审批记录 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="el-icon-picture-outline">审批记录</span>
      </div>
      <el-col :span="16" :offset="4" >
        <div class="block">
          <el-timeline>
            <el-timeline-item v-for="(item, index) in tasks" :key="index" :icon="getTimelineItemIcon(item)" :type="getTimelineItemType(item)">
              <p style="font-weight: 700">任务：{{ item.name }}</p>
              <el-card :body-style="{ padding: '10px' }">
                <label v-if="item.assigneeUser" style="font-weight: normal; margin-right: 30px;">
                  审批人：{{ item.assigneeUser.nickname }}
                  <el-tag type="info" size="mini">{{ item.assigneeUser.deptName }}</el-tag>
                </label>
                <label style="font-weight: normal" v-if="item.createTime">创建时间：</label>
                <label style="color:#8a909c; font-weight: normal">{{ parseTime(item.createTime) }}</label>
                <label v-if="item.endTime" style="margin-left: 30px;font-weight: normal">审批时间：</label>
                <label v-if="item.endTime" style="color:#8a909c;font-weight: normal"> {{ parseTime(item.endTime) }}</label>
                <label v-if="item.durationInMillis" style="margin-left: 30px;font-weight: normal">耗时：</label>
                <label v-if="item.durationInMillis" style="color:#8a909c;font-weight: normal"> {{ getDateStar(item.durationInMillis) }} </label>
                <p v-if="item.reason">
                  <el-tag :type="getTimelineItemType(item)">{{ item.reason }}</el-tag>
                </p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-col>
    </el-card>
    <!-- 流程图 -->
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
      // 审批记录
      // tasksLoad: true,
      tasks: [],
      // 审批表单
      runningTasks: [],
      auditForms: [],
      auditRule: {
        reason: [{ required: true, message: "审批建议不能为空", trigger: "blur" }],
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
  methods: {
        /** 处理审批通过和不通过的操作 */
        handleAudit(task, pass) {
      const index = this.runningTasks.indexOf(task);
      this.$refs['form' + index][0].validate(valid => {
        if (!valid) {
          return;
        }
        const data = {
          id: task.id,
          reason: this.auditForms[index].reason
        }
        if (pass) {
          approveTask(data).then(response => {
            this.$modal.msgSuccess("审批通过成功！");
            this.getDetail(); // 获得最新详情
          });
        } else {
          rejectTask(data).then(response => {
            this.$modal.msgSuccess("审批不通过成功！");
            this.getDetail(); // 获得最新详情
          });
        }
      });
    },
       getDateStar(ms) {
      return getDate(ms);
    },
    getTimelineItemIcon(item) {
      if (item.result === 1) {
        return 'el-icon-time';
      }
      if (item.result === 2) {
        return 'el-icon-check';
      }
      if (item.result === 3) {
        return 'el-icon-close';
      }
      if (item.result === 4) {
        return 'el-icon-remove-outline';
      }
      return '';
    },
    getTimelineItemType(item) {
      if (item.result === 1) {
        return 'primary';
      }
      if (item.result === 2) {
        return 'success';
      }
      if (item.result === 3) {
        return 'danger';
      }
      if (item.result === 4) {
        return 'info';
      }
      return '';
    },
  }
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