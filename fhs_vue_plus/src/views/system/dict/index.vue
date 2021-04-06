<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item  prop="groupName">
        <el-input
          v-model="queryParams.groupName"
          placeholder="分组名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item  prop="wordbookGroupCode">
        <el-input
          v-model="queryParams.wordbookGroupCode"
          placeholder="分组code"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

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
          v-hasPermi="['wordbook:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['wordbook:update']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['wordbook:del']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-edit"
          size="mini"
          @click="refreshCache"
          v-hasPermi="['wordbook:refreshRedisCache']"
        >刷新所有字典缓存</el-button>
      </el-col>

    </el-row>
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
        {label:'分组编码',name:'wordbookGroupCode',type: 'formart',
          formart:"<a >你好世界88888</a>",
          click:function(_row){
            console.log(_row);
            console.log(this);
          }
        },
        {label:'操作',type:'operation',buttons: [
            {
              label: "修改",
              type: "button",
              actionType: "dialog",
              dialog: {
                title: "修改表单",
                body: {
                  type: "form",
                  initApi: "/api/sample/${id}",
                  api: "put:/api/sample/${id}",
                  controls: [
                    {
                      type: "text",
                      name: "engine",
                      label: "Engine"
                    },
                    {
                      type: "text",
                      name: "browser",
                      label: "Browser"
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
            },]}
      ],
      filter:{
        controls:[
          {name:'groupName',placeholder: "分组名称",type:'text'},
          {name:'wordbookGroupCode',placeholder: "分组code",type:'text'}
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
  methods: {
    /** 查询字典类型列表 */
    getList() {
      this.loading = true;
      listType(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.typeList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        groupId: undefined,
        groupName: undefined,
        wordbookGroupCode: undefined,

      };
      this.resetForm("form");
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
      this.title = "添加字典类型";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dictId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.open = true;
      const groupId = row.groupId
      getType(groupId).then(response => {
        this.form = response;
        this.title = "修改字典类型";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.groupId != undefined) {
            updateType(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addType(this.form).then(response => {
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const groupId = row.groupId;
      this.$confirm('是否确认删除分组名称为"' + row.groupName + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delType(row.wordbookGroupCode,groupId);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function() {});
    },
    /** 刷新字典缓存按钮操作 */
    refreshCache(row) {
      let wordbookGroupCode ='';
      if (row.wordbookGroupCode != null || row.wordbookGroupCode !== undefined){
        wordbookGroupCode = row.wordbookGroupCode;
      }else {
        wordbookGroupCode = '';
      }
      refresh(wordbookGroupCode).then(response => {
        if (response.code === 200) {
          this.msgSuccess("操作成功");
          this.open = false;
          this.getList();
        } else {
          this.msgError(response.msg);
        }
      });

    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有类型数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return exportType(queryParams);
      }).then(response => {
        this.download(response.msg);
      }).catch(function() {});
    }
  }
};
</script>
