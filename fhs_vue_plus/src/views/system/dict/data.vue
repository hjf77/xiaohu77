<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          hasPermi="['system:dict:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:dict:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:dict:remove']"
        >删除</el-button>
      </el-col>

    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column label="分组编码" align="center" prop="wordbookGroupCode" />
      <el-table-column label="字典code" align="center" prop="wordbookCode" />
      <el-table-column label="字典翻译" align="center" prop="wordbookDesc" />
      <el-table-column label="字典排序" align="center" prop="orderNum" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            hasPermi="['system:dict:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            hasPermi="['system:dict:remove']"
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

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="118px">
        <el-form-item label="字典编码">
          <el-input v-model="form.wordbookGroupCode" :disabled="true" />
        </el-form-item>
        <el-form-item label="字典code" prop="wordbookCode">
          <el-input v-model="form.wordbookCode" placeholder="请输入字典code" />
        </el-form-item>
        <el-form-item label="字典翻译" prop="wordbookDesc">
          <el-input v-model="form.wordbookDesc" placeholder="请输入字典翻译" />
        </el-form-item>
        <el-form-item label="翻译英文" prop="wordbookDescEN">
          <el-input v-model="form.wordbookDescEN" placeholder="请输入翻译英文" />
        </el-form-item>
        <el-form-item label="翻译繁体" prop="wordbookDescTW">
          <el-input v-model="form.wordbookDescTW"  placeholder="请输入翻译繁体"  />
        </el-form-item>
        <el-form-item label="排序序号(ASC)" prop="orderNum">
          <el-input-number v-model="form.orderNum"  placeholder="请输入排序序号(ASC)"  />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listData, getData, delData, addData, updateData, exportData } from "@/api/system/dict/data";
import { listType, getType } from "@/api/system/dict/type";

export default {
  name: "Data",
  data() {
    return {
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
      dataList: [],
      // 默认字典类型
      defaultDictType: "",
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 类型数据字典
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
          wordbookGroupCode: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
          wordbookCode: [
          { required: true, message: "字典code不能为空", trigger: "blur" }
        ],
          wordbookDesc: [
          { required: true, message: "字典翻译不能为空", trigger: "blur" }
        ],
          orderNum: [
          { required: true, message: "排序序号不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    const groupId = this.$route.params && this.$route.params.dictId;
    this.getType(groupId);
    this.getTypeList();
  },
  methods: {
    /** 查询字典类型详细 */
    getType(groupId) {
      getType(groupId).then(response => {
        this.queryParams.wordbookGroupCode = response.wordbookGroupCode;
        this.defaultDictType = response.wordbookGroupCode;
        this.getList();
      });
    },
    /** 查询字典类型列表 */
    getTypeList() {
      listType().then(response => {
        this.typeOptions = response.rows;
      });
    },
    /** 查询字典数据列表 */
    getList() {
      this.loading = true;
      listData(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
          wordbookCode: undefined,
          wordbookDesc: undefined,
          wordbookDescEN: undefined,
          wordbookDescTW: undefined,
          orderNum: undefined
      };
      this.resetForm("form");
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加字典数据";
      this.form.wordbookGroupCode = this.queryParams.wordbookGroupCode;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dictCode)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
       this.open = true;
      const wordbookId = row.wordbookId
      getData(wordbookId).then(response => {
        this.form = response;
        this.title = "修改字典数据";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.wordbookId != undefined) {
            updateData(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addData(this.form).then(response => {
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
      const wordbookId = row.wordbookId
      this.$confirm('是否确认删除字典code为"' + row.wordbookCode + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delData(wordbookId);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    }

  }
};
</script>
