<!--
  模块名称：发起流程
  开发人员：彭梦倩
  创建时间:2022/12/2 14:00
-->
<template>
    <div class="app-container">
      <div v-if="!selectProcessInstance">
        <pagex-crud
          :columns="columns"
          :api="api"
        >
        </pagex-crud>
      </div>
      <!-- 第二步，填写表单，进行流程的提交 -->
      <div v-else>
        <el-card class="box-card" >
          <div slot="header" class="clearfix">
            <span class="el-icon-document">申请信息【{{ selectProcessInstance.name }}】</span>
            <el-button style="float: right;" type="primary" @click="selectProcessInstance = undefined">选择其它流程</el-button>
          </div>
          <el-col :span="16" :offset="6">
            <div>
              <!-- <parser :key="new Date().getTime()" :form-conf="detailForm" @submit="submitForm" /> -->
            </div>
          </el-col>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span class="el-icon-picture-outline">流程图</span>
          </div>
          <my-process-viewer key="designer" v-model="bpmnXML" v-bind="bpmnControlForm" />
        </el-card>
      </div>
    </div>
  </template>
  <script>
  import MyProcessViewer from "@/components/bpmnProcessDesigner/package/designer/ProcessViewer.vue";
  export default {
    name: "processCreate",
    components: { MyProcessViewer },
    data() {
      return {
        api: '/basic/ms/process-definition/list?suspensionState=' + 1,
        // BPMN 数据
        bpmnXML: null,
        bpmnControlForm: {
          prefix: "flowable"
        },
        // 流程表单
        selectProcessInstance: undefined, // 选择的流程实例

        columns: [
          {label: '流程名称', name: 'name'},
          {label: '流程分类', name: 'transMap.categoryName'},
          {label: '流程版本', name: 'version', type: 'formart',
            formart: (row) => {
              if (row.version) {
                return "v" + row.version;
              } else {
                return '未部署';
              }
            }
          },
          {label: '流程描述', name: 'description'},
          {
            label: '操作',
            name: 'operation',
            type: 'textBtn',
            width: '100px',
            textBtn: [
              {
                title: "选择",
                icon: 'el-icon-plus',
                type: "text",
                size: 'mini',
                click: (_row) => {
                  // 设置选择的流程
                  this.selectProcessInstance = _row;
                  if (_row.formId) {
                    // 加载流程图
                    this.$pagexRequest({
                      url: '/basic/ms/process-definition/get-bpmn-xml?id=' + _row.id,
                      method: 'get'
                      }).then((res) => {
                      this.bpmnXML = res.data;
                    })
                  } else if (_row.formCustomCreatePath) {
                    this.$router.push({ path: _row.formCustomCreatePath});
                    // 这里暂时无需加载流程图，因为跳出到另外个 Tab；
                  }
                }
              },
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