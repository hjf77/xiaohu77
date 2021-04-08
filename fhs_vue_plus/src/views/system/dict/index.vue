<template>
  <div class="app-container">

    <el-col :span="1.5">
      <el-button
        type="danger"
        icon="el-icon-edit"
        size="mini"
        @click="refreshCache"
      >刷新所有字典缓存
      </el-button>
    </el-col>

    <pagex-crud
      :filter="filter"
      :columns="columns"
      :api="api"
    >
       <div slot="import">
         我是表单和按钮
       </div>
    </pagex-crud>

    <!-- 新增 修改 弹框-->
    <el-dialog :title="title" v-if="open" :visible.sync="open" width="500px">
      <addDict :init="init" :isEdit="isEdit"></addDict>
    </el-dialog>
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
      api: '/ms/wordbook/findWordbookGroupForPage?isVue=true',
      //delUrl
      //editUrl
      //form:{}
      methods:{
        importExcelSubmit:function(_formData){

        },
      },
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [
        {
          title: '批量删除', click: function (_row, _checkRows) {

          }, icon: 'del', color: '#0000'
        },{
          title:'excel导入',name:'import',
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
          label: '操作', type: 'operation', buttons: [
            {
              label: "修改",
              type: "button",
              actionType: "dialog",
              dialog: {
                title: "修改表单",
                body: {
                  type: "form",
                  initApi: "/ms/wordbook/getWordbookGroupBean?groupId=",
                  updateApi: "/ms/wordbook/updateWordbookGroup",
                  isEdit: true,
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
                }
              }
            },
            {
              label: "删除",
              type: "button",
              actionType: "ajax",
              level: "danger",
              confirmText: "确认要删除？",
              api: "delete:https://houtai.baidu.com/api/sample/${id}"
            },]
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
