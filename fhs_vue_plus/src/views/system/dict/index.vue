<template>
  <div class="app-container">
    <crud
      :filter="filter"
      :columns="columns"
      :api="api"
    ></crud>

    <!-- 新增 修改 弹框-->
    <el-dialog title="title" v-if="open" :visible.sync="open" width="500px">
      <addDict></addDict>
    </el-dialog>
  </div>
</template>

<script>
import { listType, getType, delType, addType, updateType, refresh } from "@/api/system/dict/type";
import addDict from "@/views/system/dict/components/addDict";
import crud from "@/lib/components/crud";
export default {
  name: "Dict",
  components:{
    addDict,
    crud
  },
  provide(){
    return {
      wlTest:cb=>{
        cb(this)
      }
    }
  },
  data() {
    return {
      api:'/ms/wordbook/findWordbookGroupForPage',
      // 查询参数
      queryParams: {
        groupName: undefined,
        wordbookGroupCode: undefined
      },

      columns:[
        {label:'分组名称',name:'groupName'},
        {label:'分组编码',name:'wordbookGroupCode'}
      ],
      filter:{
        controls:[
          {label:'分组名称',name:'groupName',placeholder: "通过关键字搜索",type:'text'},
          {label:'分组编码',name:'wordbookGroupCode',placeholder: "通过关键字搜索",type:'text'}
        ]
      },


      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 字典表格数据
      typeList: [
        'groupName',
      ],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 日期范围
      dateRange: [],

      // 表单参数
      form: {},
      // 表单校验
      rules: {
          groupName: [
          { required: true, message: "分组名称不能为空", trigger: "blur" }
        ],
          wordbookGroupCode: [
          { required: true, message: "分组编码不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
  },
  methods: {}
};
</script>
