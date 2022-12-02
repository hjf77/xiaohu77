<!--
  模块名称：流程模型
  开发人员：彭梦倩
  创建时间:2022/11/24 8:00
-->
<template>
  <div class="app-container">
    <pagex-crud
      :filters="filters"
      :columns="columns"
      :api="api"
      :buttons="buttons"
      :namespace="namespace"
    >
      <template v-slot:form="prop">
        <!-- 修改 弹框-->
        <pagex-dialog slot="form" :title="title"  :visible.sync="open" :before-close="closeFn"  class="pagex-dialog-theme">
          <editForm v-if="isEdit" :init="init" :isEdit="isEdit">编辑</editForm>
          <addForm v-else :isAdd="isAdd">新增</addForm>
        </pagex-dialog>
      </template>
      <template v-slot:topSlot="prop">
        <pagex-dialog slot="topSlot" width="1200px" :title="ruleTitle" :namespace="namespace"
          v-if="openRules" :visible.sync="openRules" class="pagex-dialog-theme">
          <div class="pagex-dialog-theme">
            <allocationRules :ruleId="ruleId" :init="init"></allocationRules>
          </div>
        </pagex-dialog>
      </template>
    </pagex-crud>
  </div>
</template>
<script>
import crudMixins from "@/mixins/crudMixins";
import editForm from "@/views/bpm/model/components/editForm.vue";
import addForm from "@/views/bpm/model/components/addForm.vue";
import allocationRules from "@/views/bpm/model/components/allocationRules.vue";

export default {
  name: "model",
  components: {
    editForm,
    addForm,
    allocationRules
  },
  mixins: [crudMixins],
  data() {
    return {
      namespace: 'model',
      api: '/basic/ms/model/page',
      openRules: false,
      ruleTitle: '任务分配规则',
      isEdit: false,
      isAdd: false,
      addOpen: false,
      init: {},
      ruleId: '',
      buttons: [
        {
          title: '新增流程',
          type: 'primary',
          size: 'mini',
          icon: 'el-icon-plus',
          click: () => {
            this.title = '新增流程';
            this.open = true;
            this.isEdit = false;
          }
        },
        {
          title: '导入流程',
          type: 'primary',
          size: 'mini',
          icon: 'el-icon-upload2',
          click: () => {
            this.title = '导入流程';
          }
        }
      ],
      columns: [
        {label: '流程标识', name: 'key'},
        {label: '流程名称', name: 'name'},
        {label: '流程分类', name: 'transMap.categoryName'},
        {label: '表单信息', name: 'formType', type: 'formart',
          formart: (row) => {
            if (row.formId) {
              return row.formName;
            } else if(row.formCustomCreatePath) {
              return row.formCustomCreatePath;
            } else {
              return '暂无表单';
            }
          }
        },
        {label: '创建时间', name: 'createTime'},
        {label: '流程版本', name: 'processDefinition.version', type: 'formart',
          formart: (row) => {
            if (row.processDefinition) {
              return "v" + row.processDefinition.version;
            } else {
              return '未部署';
            }
          }
        },
        {label: '激活状态', name: 'processDefinition.transMap.suspensionStateName' },
        {label: '部署时间', name: 'deploymentTime'},
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          width: '400px',
          textBtn: [
            {
              title: "修改流程",
              icon: 'el-icon-edit',
              type: "text",
              size: 'mini',
              click: (_row) => {
                  this.$set(this, 'init', _row)
                  this.title = '编辑';
                  this.open = true;
                  this.isEdit = true;
              }
            },
            {
              title: "设计流程",
              icon: 'el-icon-setting',
              type: "text",
              size: 'mini',
              click: (_row) => {
                this.$router.push({
                  path: '/bpm/modelEditor',
                  query:{modelId: _row.id}
                });
              }
            },
            {
              title: "分配规则",
              icon: 'el-icon-s-custom',
              type: "text",
              api: '/basic/ms/dictItem/',
              size: 'mini',
              click: (_row) => {
                this.$set(this, 'init', _row)
                this.openRules = true;
                this.ruleId = _row.id
              }
            },
            {
              title: "发布流程",
              icon: 'el-icon-thumb',
              type: "text",
              api: '/basic/ms/dictItem/',
              size: 'mini',
              click: (_row) => {
                this.$confirm('是否部署该流程！！')
                  .then(_ => {
                    this.$pagexRequest({
                      url: '/basic/ms/model/deploy?id=' + _row.id,
                      data: data,
                      method: 'POST',
                    }).then((res) => {
                      done();
                    })
                  })
                  .catch(_ => { });
              },
            },
            {
              title: "流程定义",
              icon: 'el-icon-ice-cream-round',
              type: "text",
              api: '/basic/ms/dictItem/',
              size: 'mini',
              click: (_row) => {
                this.$router.push({
                  // path: '/bpm/modelEditor',
                  query:{modelId: _row.id}
                });
              }
            },
            {
              title: "删除",
              icon: 'el-icon-delete',
              type: "text",
              api: '/basic/ms/dictItem/',
              size: 'mini',
              idFieldName: 'dictId'
            },
          ],
        }
      ],
      filters: [
        {label: '流程标识', name: 'key', type: 'text'},
        {label: '流程名称', name: 'name', type: 'text'},
        {label: '流程分类', name: 'category', type: 'select', dictCode: 'categoryDictDatas'},
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
