<!--
  模块名称：我的流程
  开发人员：彭梦倩
  创建时间:2022/11/24 8:00
-->
<template>
  <div class="app-container">
    <pagex-crud
      ref="crud"
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
              title: "取消",
              icon: 'el-icon-view',
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
                  const id = _row.id;
                  const reason = value;
                  this.$pagexRequest({
                    url: '/basic/ms/process-instance/cancel',
                    method: 'DELETE',
                    data: { id, reason}
                    }).then((res) => {
                      this.$refs.crud.search();
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
                // 点击审批按钮后，先调一次详情接口，获取到processDefinition.formCustomViewPath后，带type跳转页面
                this.$pagexRequest({
                  url: '/basic/ms/process-instance/get?id=' + _row.id,
                  method: 'get',
                  }).then((res) => {
                    if (res.data) {
                      const type = res.data.processDefinition.formCustomViewPath;
                      if (type!==null && res.data.businessKey) {
                        this.$router.push({
                          path: '/bpm/processDetail/'+ type,
                          query:{id: _row.id,
                            businessKey: res.data.businessKey
                          }
                        });
                      } else {
                        this.$router.push({
                          path: '/bpm/processDetail/',
                          query:{id: _row.id}
                        });
                      }
                    }
                })
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