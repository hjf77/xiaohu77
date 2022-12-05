<!--
模块名称：流程详情
开发人员：彭梦倩
创建时间:2022/11/30 17:00
-->

<template>
  <div>
    <el-card v-if="$route.params.type">
      <div slot="header" class="clearfix">
        <span class="el-icon-document">申请信息【{{ processInstance.name }}】</span>
      </div>
        <router-view></router-view>
    </el-card>
    <!-- 审批任务【老师审批】 -->
   <el-card v-for="(item, index) in runningTasks" :key="index">
    <div slot="header" class="clearfix">
      <span class="el-icon-picture-outline">审批任务【{{ item.name }}】</span>
    </div>
    <el-col :span="16" :offset="6" >
        <el-form :ref="'form' + index"  label-width="100px" :rules="auditRule" :model="auditForms[index]">
          <el-form-item label="流程名" v-if="processInstance && processInstance.name">{{ processInstance.name }}</el-form-item>
          <el-form-item label="流程发起人" > {{ processInstance.startUser.nickname }}
            <el-tag type="info" size="mini">{{ processInstance.startUser.deptName }}</el-tag>
          </el-form-item>
          <el-form-item label="审批建议" prop="reason" >
            <el-input type="textarea"  placeholder="请输入审批建议" v-model="auditForms[index].reason"/>
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

    <!-- 对话框(转派审批人) -->
    <el-dialog title="转派审批人" :visible.sync="updateAssignee.open" width="500px" append-to-body>
      <el-form ref="updateAssigneeForm" :model="updateAssignee.form" :rules="updateAssignee.rules" label-width="110px">
        <el-form-item label="新审批人" prop="assigneeUserId">
          <el-select v-model="updateAssignee.form.assigneeUserId" clearable style="width: 100%">
            <el-option v-for="item in userOptions" :key="parseInt(item.userId)" :label="item.userName" :value="parseInt(item.userId)" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpdateAssigneeForm">确 定</el-button>
        <el-button @click="cancelUpdateAssigneeForm">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import MyProcessViewer from "@/components/bpmnProcessDesigner/package/designer/ProcessViewer.vue";
import formManagerVue from "../../formManager/formManager.vue";
export default {
  name: "processDetail",
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

      // 转派审批人
      userOptions: [],
      updateAssignee: {
        open: false,
        form: {
          assigneeUserId: undefined,
        },
        rules: {
          assigneeUserId: [{ required: true, message: "新审批人不能为空", trigger: "change" }],
        }
      },
    }
  },
  mounted() {
  },
  created() {
    this.id = this.$route.query.id;
    if (!this.id) {
      this.$message.error('未传递 id 参数，无法查看流程信息');
      return;
    }
    this.getDetail();

    // 获得用户列表
    this.userOptions = [];
    this.$pagexRequest({
      url: '/basic/ms/sysUser/findList',
      method: 'get'
    }).then((res) => {
      this.userOptions.push(...res);
    })
    console.log('this.userOptions',this.userOptions);
  },
  methods: {
    /** 获得流程实例 */
    getDetail(){
      // 获得流程实例相关
      this.$pagexRequest({
        url: '/basic/ms/process-instance/get?id=' + this.id,
        method: 'get'
      }).then((res) => {
        if (!res.data) {
          this.$message.error('查询不到流程信息！');
          return;
        }
        this.processInstance = res.data;

        // 加载流程图
        this.$pagexRequest({
          url: '/basic/ms/process-definition/get-bpmn-xml?id=' + this.processInstance.processDefinition.id,
          method: 'get'
          }).then((res) => {
          this.bpmnXML = res.data;
        })

        // 加载活动列表
        this.$pagexRequest({
          url: '/basic/ms/activity/list?processInstanceId=' + this.processInstance.id,
          method: 'get'
          }).then((res) => {
          this.activityList = res;
          })
      })

      // 获得流程任务列表（审批记录）
      this.runningTasks = [];
      this.auditForms = [];
      this.$pagexRequest({
        url: '/basic/ms/task/list-by-process-instance-id?processInstanceId=' + this.id,
        method: 'get'
      }).then((res) => {
        this.tasks = [];
        // 移除已取消的审批
        res.forEach(item => {
          if (item.result !== 4) {
            this.tasks.push(item);
          }
        });
      
        // 排序，将未完成的排在前面，已完成的排在后面；
        this.tasks.sort((a, b) => {
          // 有已完成的情况，按照完成时间倒序
          if (a.endTime && b.endTime) {
            return b.endTime - a.endTime;
          } else if (a.endTime) {
            return 1;
          } else if (b.endTime) {
            return -1;
            // 都是未完成，按照创建时间倒序
          } else {
            return b.createTime - a.createTime;
          }
        });

        // 需要审核的记录
        // const userId = store.getters.userId;
        const userId = this.$store.state.user.user.userId;
        console.log(this.tasks);
        this.tasks.forEach(item => {
          if (item.result !== 1) { // 只有待处理才需要
            return;
          }
          if (!item.assigneeUser || item.assigneeUser.id !== userId) { // 自己不是处理人
            return;
          }
          this.runningTasks.push({...item});
          this.auditForms.push({
            reason: ''
          })
        });
      });
    },

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
          this.$pagexRequest({
            url: `/basic/ms/task/approve`,
            method: "PUT",
            data: data,
          }).then((res) => {
            this.msgSuccess("审批通过成功！");
            this.getDetail(); // 获得最新详情
          });
        } else {
          this.$pagexRequest({
            url: `/basic/ms/task/reject`,
            method: "PUT",
            data: data,
          }).then((res) => {
            this.msgSuccess("审批不通过成功！");
            this.getDetail(); // 获得最新详情
          });
        }
      });
    },
    getDateStar(ms) {
      // return getDate(ms);
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
    /** 处理转派审批人 */
    handleUpdateAssignee(task) {
      // 设置表单
      this.resetUpdateAssigneeForm();
      this.updateAssignee.form.id = task.id;
      // 设置为打开
      this.updateAssignee.open = true;
    },
    /** 提交转派审批人 */
    submitUpdateAssigneeForm() {
      this.$refs['updateAssigneeForm'].validate(valid => {
        if (!valid) {
          return;
        }
        this.$pagexRequest({
            url: `/basic/ms/task/update-assignee`,
            method: "PUT",
            data: this.updateAssignee.form,
          }).then((res) => {
            this.msgSuccess("转派任务成功！");
            this.updateAssignee.open = false;
            this.getDetail(); // 获得最新详情
          });
      });
    },
    /** 取消转派审批人 */
    cancelUpdateAssigneeForm() {
      this.updateAssignee.open = false;
      this.resetUpdateAssigneeForm();
    },
    /** 重置转派审批人 */
    resetUpdateAssigneeForm() {
      this.updateAssignee.form = {
        id: undefined,
        assigneeUserId: undefined,
      };
      this.resetForm("updateAssigneeForm");
    },
    /** 处理审批退回的操作 */
    handleDelegate(task) {
      this.msgError("暂不支持【委派】功能，可以使用【转派】替代！");
    },
    /** 处理审批退回的操作 */
    handleBack(task) {
      const reason = this.auditForms[this.auditForms.length-1].reason;
      let data = {
        taskId: task.id,
        processInstanceId: task.processInstance.id,
        reason: reason,
      }
      this.$pagexRequest({
        url: `/basic/ms/task/getBackNodesByProcessInstanceId?taskId=${ task.id }&processInstanceId=${ task.processInstance.id }`,
        method: 'GET',
      }).then((res) => {
        console.log(res,'res');
        if (res.length) {
          res.forEach((item,i) => {
            data.definitionKey = res[0].definitionKey
          });
          this.$pagexRequest({
            url: `/basic/ms/task/doBackStep`,
            method: 'POST',
            data: data,
          }).then((res) => {
            this.getDetail(); // 获得最新详情
            this.msgSuccess("任务驳回成功！");
          })
        }
      })

    }
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