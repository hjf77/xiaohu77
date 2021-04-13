<template>
  <div class="app-container">
    <pagex-crud
      :filter="filter"
      :columns="columns"
      :api="api"
      :buttons="buttons"
    >
      <template v-slot:import="prop">
        <!-- 新增 修改 弹框-->
        <el-dialog slot="import" :title="title" v-if="open" :visible.sync="open" width="500px">
          <addDict :init="init" :isEdit="isEdit"></addDict>
        </el-dialog>
      </template>
    </pagex-crud>

  </div>
</template>

<script>
import {listType, getType, delType, addType, updateType, refresh} from "@/api/system/dict/type";
import addDict from "@/views/system/dict/components/addDict";
// import crud from "@/lib/components/crud";
export default {
  name: "Dict",
  components: {
    addDict,
    // crud
  },
  provide() {
    return {
      wlTest: cb => {
        cb(this)
      }
    }
  },
  data() {
    return {
      slots: {
        controls: [
          {
            type: 'text',
            name: 'groupName',
            label: '分组名称',
            rule: 'required'
          }, {
            type: 'text',
            name: 'wordbookGroupCode',
            label: '分组编码',
            rule: 'required'
          }
        ]
      },
      title: '编辑',
      open: false,
      isEdit: false,
      api: '/ms/wordbook/findWordbookGroupForPage?isVue=true',
      //delUrl
      //addUrl
      //updateUrl
      //initUrl
      //batchDelUrl
      //form:{}
      methods: {
        importExcelSubmit: (_formData) => {

        },
      },
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [
        {
          title: '批量删除',
          icon: 'el-icon-delete',
          type: 'primary',
          click: (_row, _checkRows) => {
            console.log(_row, _checkRows);
          },
        }, {
          title: 'excel导入',
          name: 'import',
          type: 'warning',
          click: (_row, _checkRows) => {

          }
        }, {
          title: '导出',
          name: 'import',
          type: 'danger',
          click: (_row, _checkRows) => {
            console.log(_row, _checkRows);
          }
        }, {
          title: '新增',
          name: 'add',
          type: 'primary',
          click: () => {
            this.title = '新增';
            this.open = true;
          }
        }
      ],
      columns: [
        {label: '', name: '', type: 'selection'},
        {label: '分组名称', name: 'groupName'},
        {
          label: '分组编码', name: 'wordbookGroupCode', type: 'formart',
          formart: "<label style='cursor:pointer'>${wordbookGroupCode}</label>",
          click: function (_row) {
            this.$router.push({path: '/dict/type/data/' + _row.groupId});
          }
        },
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          textBtn: [
            {
              name: "详情",
              click: (_row, name) => {
                console.log(_row, name);
                this.title = '详情';
                this.open = true;
              }
            },
            {
              name: "编辑",
              click: (_row, name) => {
                console.log(_row, name);
                this.title = '编辑';
                this.open = true;
              }
            }
          ],
        }
      ],
      filter: {
        controls: [
          {name: 'groupName', placeholder: "分组名称", type: 'text'},
          {name: 'wordbookGroupCode', placeholder: "分组code", type: 'text'}
        ]
      },
    };
  },
  created() {
  },
  methods: {}
};
</script>
