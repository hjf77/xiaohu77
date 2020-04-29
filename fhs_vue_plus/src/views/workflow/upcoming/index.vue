<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="流程标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入流程标题"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务名称" prop="taskName">
        <el-input
          v-model="queryParams.taskName"
          placeholder="请输入任务名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--<el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          hasPermi="['system:service:add']"
        >办理</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:service:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          hasPermi="['system:service:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:service:export']"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="taskList" >
      <el-table-column label="流程标题" prop="title" align="center" :show-overflow-tooltip="true" width="260" />
      <el-table-column label="任务名称" prop="taskName" align="center" :show-overflow-tooltip="true" width="260" />
      <el-table-column label="申请人" prop="transMap.createUserUserName" align="center" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="申请时间" prop="instanceCreateTime" align="center"  width="260">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.instanceCreateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务创建时间" align="center" prop="taskCreateTime" width="260">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskCreateTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            hasPermi="['system:service:edit']"
          >办理</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:service:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" >
      <template>
        <el-tabs style="height: 400px">
          <el-tab-pane label="详情" name="first">
                <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                  <el-row>
                     <el-col :span="12">
                      <el-form-item label="姓名" prop="name">
                        <el-input v-model="form.name" placeholder="请输入姓名" />
                      </el-form-item>
                     </el-col>
                       <el-col :span="12">
                      <el-form-item label="涨幅" prop="increase">
                        <el-input v-model="form.increase" placeholder="请输入涨幅" />
                      </el-form-item>
                       </el-col>
                  </el-row>
                </el-form>
             <el-tag type="info" size=medium >审批操作</el-tag>
            <el-form ref="instanceDetails" :model="instanceDetails" :rules="rules" label-width="100px">
              <el-row>
                <el-col :span="24">
                  <el-form-item label="备注">
                    <el-input  type="textarea"  v-model="instanceDetails.remark"  placeholder="请输入内容"></el-input>
                    <el-input   v-model="instanceDetails.taskId"  style="display: none"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="抄送人" prop="ccToUserUserName">
                    <el-input  v-model="instanceDetails.ccToUserUserName" placeholder="请输入抄送人" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                <el-button type="primary" :style="{ display: visibleYesOk }" size="mini" @click="yesOk">同意OK</el-button>
                <el-button type="primary" :style="{ display: visibleAddCCTo }"  size="mini" @click="addCCTo">抄送</el-button>
                <el-button type="primary"  :style="{ display: visibleDelegate }" size="mini" @click="delegate">委派</el-button>
                <el-button type="primary"  :style="{ display: visibleTurnDown }" size="mini" @click="turnDown">驳回OK</el-button>
                <el-button type="primary" :style="{ display: visibleNodeOperat }" size="mini" @click="nodeOperat">驳回到指定操作OK</el-button>
                <el-button type="primary"  size="mini"  @click="submitForm">前加签</el-button>
                <el-button type="primary"  size="mini"  @click="submitForm">后加签</el-button>
                <el-button type="primary"  size="mini"  @click="submitForm">撤回OK</el-button>
                <el-button type="primary"  size="mini"  @click="submitForm">撤回申请OK</el-button>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="审批历史" name="second">
            <template>
              <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="taskName" align="center" label="任务名称" width="140">
                </el-table-column>
                <el-table-column prop="createTime" align="center" label="任务创建时间" width="150">
                </el-table-column>
                <el-table-column prop="taskFinishTime" align="center" label="处理时间" width="150">
                </el-table-column>
                <el-table-column prop="transMap.userName" align="center" label="处理人" width="120">
                </el-table-column>
                <el-table-column prop="transMap.useStatusName" align="center" label="处理状态" width="120">
                </el-table-column>
                <el-table-column prop="remark" label="备注意见" width="160">
                </el-table-column>
              </el-table>
            </template>
          </el-tab-pane>
          <el-tab-pane label="流程图" name="third">
            <div class="demo-image__placeholder">
              <div class="block">
                <el-image :src="src"></el-image>
              </div>
            </div>
          </el-tab-pane>

        </el-tabs>
      </template>

     <!-- <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>-->
    </el-dialog>
    <el-dialog :title="title" :visible.sync="openUser" width="500px" >
      <template>
        <el-form ref="userForm" :model="userForm" :rules="rules" label-width="100px">
          <el-row>
            <el-col :span="24">
              <el-form-item label="抄送人" prop="ccToUserUserName" >
                <treeselect
                  v-model="userForm.ccToUserUserName"
                  :multiple="true"
                  :options="userOptions"  placeholder="请选择抄送人"  :normalizer="normalizer" @select="selectUserOptions">
                </treeselect>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </template>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="userSubmitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="title" :visible.sync="openDelegate" width="500px" >
      <template>
        <el-form ref="userForm" :model="delegateForm" :rules="rules" label-width="100px">
          <el-row>
            <el-col :span="24">
              <el-form-item label="委派"  >
                <treeselect
                  v-model="delegateForm.ccToUserUserName"
                  :options="userOptions"  placeholder="请选择委派人"  :normalizer="normalizer" @select="selectUserOptions">
                </treeselect>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </template>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="delegateSubmitForm">确 定</el-button>
        <el-button @click="cancels">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="title" :visible.sync="openNode" width="500px" >
      <template>
        <el-form ref="nodeForms" :model="nodeForm" :rules="rules" label-width="100px">
          <el-row>
            <el-col :span="24">
                <el-form-item label="节点" prop="roleIds" >
                  <el-select v-model="nodeForm.id"  placeholder="请选择节点">
                    <el-option
                      v-for="item in nodeOptions"
                      :key="item.id"
                      :label="item.title"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注">
                <el-input  type="textarea"  v-model="nodeForm.remark"  placeholder="请输入内容"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </template>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="turnDownForm">确 定</el-button>
        <el-button @click="cancels">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTask, findInstanceById, getTask,findBackAvtivity, delService,getNode, backTask,backTaskDesignated,addService,delegate, addccToUserUserName,updateService, exportService ,complateTask, getApprovalRecord, getUserTree} from "@/api/workflow/upcoming";

import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: "Upcoming",
    components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
       // 非多个禁用
      multiple: true,

        visibleYesOk:'',
        visibleAddCCTo:'',
        visibleDelegate:'',
        visibleTurnDown:'',
        visibleNodeOperat:'',
      // 非单个禁用
      single: true,
        //节点
        nodeOptions:[],

        //人员tree
        userOptions: undefined,
        //选中的usertree
        nodes: [],

      taskList: [],

      tableData: [],

      // 是否显示弹出层
      open: false,
        // 是否显示弹出层子层
        openUser: false,
        //驳回到指定层
        openNode:false,
        //委派
        openDelegate: false,

      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 菜单列表
      menuOptions: [],

      //流程图
      src : "",

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
          taskName: undefined
      },
      // 表单参数
      form: {},
      // 实例详情
      instanceDetails: {
          ccToUserUserName:""
      },
        //人员form
        userForm: {},
        //委派
        delegateForm: {},

        nodeForm:{},

      defaultProps: {
        children: "children",
        label: "label"
      },
        //给表单部门tree改别名
        normalizer(node) {
            return {
                id : node.id,
                label: node.text,
                children: node.children,
            }

        },
      // 表单校验
      rules: {
        serverName: [
          { required: true, message: "服务名称不能为空", trigger: "blur" }
        ],
        serverUrl: [
          { required: true, message: "服务连接不能为空", trigger: "blur" }
        ]
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询服务列表 */
      getList() {
          this.loading = true;
          listTask(this.queryParams).then(response => {
              this.taskList = response.rows;
              this.total = response.total;
              this.loading = false;
          });
      },
      /** 查询实例历史 */
      getTableData(taskId) {
          this.loading = true;
          getApprovalRecord(taskId).then(response => {
              this.tableData = response.rows;
              this.total = response.total;
              this.loading = false;
          });
      },
    // 取消按钮
    cancel() {
      this.openUser = false;
      this.resetUserForm();
    },
      // 取消按钮
      cancels() {
          this.openDelegate = false;
          this.resetUserForm();
      },
    // 表单重置
    reset() {
      this.form = {
        name: undefined,
        increase: undefined
      };
      this.resetForm("form");
        this.instanceDetails = {
            remark: undefined,
            ccToUserUserName: undefined,
            taskId: undefined,
            activitiInstanceId: undefined
        };
        this.resetForm("instanceDetails");
    },
      //重置人员tree表单
      resetUserForm(){
          this.userForm = {
              ccToUserUserName: undefined
          }
          this.resetForm("userOptions");
          this.delegateForm = {
              ccToUserUserName: undefined
          }
          this.nodeForms = {
              id : undefined,
              title : undefined,
              remark: undefined
          }
          this.resetForm("nodeForms");
      },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加服务";
    },
    /** 办理按钮操作 */
    handleUpdate(row) {
      this.reset();
        this.open = true
        findInstanceById(row.activitiInstanceId).then(response => {
        this.instanceDetails = response.data;
            getTask(response.data.formPkey,response.data.processKey).then(response => {
                this.form = response
            });
            /**加载流程图*/
            this.src = 'http://localhost:8081/ms/myWorks/getWorkFlowImg?instanceId='+row.activitiInstanceId;
            this.instanceDetails.taskId = row.taskId;
            this.instanceDetails.activitiInstanceId = row.activitiInstanceId;
            /**加载抄送人*/
            const ccToUserUserName = response.data.transMap.ccToUserUserName;
            if (ccToUserUserName != undefined){
                this.instanceDetails.ccToUserUserName =ccToUserUserName
            }
      });

        this.title = '处理任务{'+row.title+'}';
        /**加载实例历史*/
       this.getTableData(row.activitiInstanceId);
        this.backAvtivity(row.taskId);
    },
    /** 提交按钮 */
    submitForm: function() {

      this.$refs["form"].validate(valid => {
        if (valid) {
            debugger
          if (this.form.id != undefined) {
            updateService(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addService(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
    },
      /** 同意OK */
      yesOk: function() {
          const taskId = this.instanceDetails.taskId;
          const remark = this.instanceDetails.remark;
          complateTask(taskId,remark).then(response => {
              if (response.code === 200) {
                  this.msgSuccess("操作成功");
                  this.open = false;
                  this.getList();
              } else {
                  this.msgError(response.msg);
              }
          });

      },
      /** 抄送 */
      addCCTo: function() {
          this.resetUserForm();
          this.nodes = [];
          this.openUser = true
          getUserTree().then(response => {
             this.userOptions = response
          })
      },
      /** 委派 */
      delegate: function() {
          this.resetUserForm();
          this.nodes = [];
          this.openDelegate = true
          getUserTree().then(response => {
              this.userOptions = response
          })
      },
      /**驳回到指定操作*/
      nodeOperat: function() {
          this.resetUserForm();
          this.openNode = true
          const taskId = this.instanceDetails.taskId
          getNode(taskId).then(response => {
              this.nodeOptions = response.data;
          })
      },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除服务名称为"' + row.serverName + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delService(row);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportService(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    },
      /**获取下拉的userTree*/
      selectUserOptions(val) {
        this.nodes.push(val);
      },
      /**添加人员*/
      userSubmitForm(){
          const ccUserTos =  this.getSelectUsers ();
          if(ccUserTos===''){
              this.msgError('请至少选择一个用户(选中组织机构请确定组织机构下有用户)');
              return false;
          }
          const instanceId = this.instanceDetails.activitiInstanceId;
          const ccUserToss = ccUserTos;
          addccToUserUserName(ccUserToss,instanceId).then(response => {
              if (response.code === 200) {
                  this.msgSuccess("操作成功");
                  findInstanceById(instanceId).then(response=> {
                      this.instanceDetails.ccToUserUserName =response.data.transMap.ccToUserUserName;
                      this.openUser = false;
                  })
              } else {
                  this.msgError(response.msg);
              }
          })
      },
      /**委任*/
      delegateSubmitForm() {
          const ccUserTos = this.getSelectUsers ();
          if(ccUserTos===''){
              this.msgError('请选择委派人(注意不是组织)');
              return false;
          }
         const userName = this.nodes[0].text;
          const userId = this.nodes[0].id;
          const taskId = this.instanceDetails.taskId
          delegate(userName,userId,taskId).then(response=>{
              if (response.code === 200) {
                  this.msgSuccess("操作成功");
                  this.open = false;
                  this.openDelegate = false;
                  this.getList();
              } else {
                  this.msgError(response.msg);
              }
          })
      },
      /**驳回*/
      turnDown(){
          const taskId = this.instanceDetails.taskId
          backTask(taskId).then(response =>{
              if (response.code === 200) {
                  this.msgSuccess("操作成功");
                  this.open = false;
                  this.getList();
              } else {
                  this.msgError(response.msg);
              }
          })

      },
      /**驳回到指定节点*/
      turnDownForm(){
          debugger
          const taskId = this.instanceDetails.taskId
          const id = this.nodeForm.id
          const remark = this.nodeForm.remark
          backTaskDesignated(taskId, id,remark).then(response =>{
              if (response.code === 200) {
                  this.msgSuccess("操作成功");
                  this.open = false;
                  this.openNode = false;
                  this.getList();
              } else {
                  this.msgError(response.msg);
              }
          })
      },
      /**校验选择有人员*/
      getSelectUsers (){
          const nodes = this.nodes;
          let ccUserTos = [];
          for(let i=0;i<nodes.length;i++){
              let node = nodes[i];
              if(node.isUser===1){
                  ccUserTos.push(node.id);
              }
          }
            ccUserTos = ccUserTos + '';
          return ccUserTos;
      },
      /**回退节点为0*/
      backAvtivity(taskId){
          findBackAvtivity(taskId).then(response =>{
              if (response.code === 200){
                  if (response.data.length === 0){
                      this.visibleYesOk = 'none'
                      this.visibleAddCCTo = 'none'
                      this.visibleDelegate = 'none'
                      this.visibleTurnDown = 'none'
                      this.visibleNodeOperat ='none'
                  }else {
                      this.visibleYesOk = ''
                      this.visibleAddCCTo = ''
                      this.visibleDelegate = ''
                      this.visibleTurnDown = ''
                      this.visibleNodeOperat =''
                  }
              }
          })
      }
  }
};
</script>
