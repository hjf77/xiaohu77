<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item >
        <el-input
          v-model="queryParams.name"
          placeholder="部门名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item >
        <el-select v-model="queryParams.isEnable" placeholder="部门状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.wordbookCode"
            :label="dict.wordbookDesc"
            :value="dict.wordbookCode"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button>
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['sysOrganization:add']"
        >新增
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="deptList"
      row-key="id"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="name" label="部门名称" width="260"></el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isEnable"
            active-text="禁用"
            inactive-text="启用"
            :active-value="0"
            :inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sysOrganization:update']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['sysOrganization:add']"
          >新增
          </el-button>
          <el-button v-if="scope.row.parentId != 0"
                     size="mini"
                     type="text"
                     icon="el-icon-delete"
                     @click="handleDelete(scope.row)"
                     v-hasPermi="['sysOrganization:del']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== ''">
            <el-form-item label="上级部门" prop="parentId">
              <treeselect v-model="form.parentId" :options="deptOptions" :normalizer="normalizer" placeholder="选择上级部门"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入部门名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门状态">
              <el-radio-group v-model="form.isEnable">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.wordbookCode"
                  :label="dict.wordbookCode"
                >{{dict.wordbookDesc}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listDept, getDept, delDept, addDept, updateDept } from '@/api/system/sysOrganization'
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'

  export default {
    name: 'sysOrganization',
    components: { Treeselect },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 表格树数据
        deptList: [],
        // 部门树选项
        deptOptions: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 状态数据字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          name: undefined,
          isEnable: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          parentId: [
            { required: true, message: '上级部门不能为空', trigger: 'blur' }
          ],
          name: [
            { required: true, message: '部门名称不能为空', trigger: 'blur' }
          ],
        }
      }
    },
    created() {
      this.getList()
      this.getDicts('is_enable').then(response => {
        this.statusOptions = response
      })
    },
    methods: {
      /** 查询部门列表 */
      getList() {
        this.loading = true
        listDept(this.queryParams).then(response => {
          this.deptList = this.handleTree(response.rows, 'deptId')
          this.loading = false
        })
      },
      /** 转换部门数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children
        }
        return {
          id: node.id,
          label: node.name,
          children: node.children
        }
      },
      /** 查询部门下拉树结构 */
      getTreeselect() {
        listDept().then(response => {
          this.deptOptions = this.handleTree(response.rows, 'deptId')
        })
      },
      // 字典状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.isEnable)
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          parentId: undefined,
          name: undefined,
          isEnable: '0'
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {};
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset()
        this.getTreeselect()
        if (row != undefined) {
          this.form.parentId = row.id
        }
        this.open = true
        this.title = '添加部门'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        this.getTreeselect()
        getDept(row.id).then(response => {
          this.form = response
          this.form.isEnable = response.isEnable.toString();
          this.open = true
          this.title = '修改部门'
        })
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateDept(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('修改成功')
                  this.open = false
                  this.getList()
                } else {
                  this.msgError(response.msg)
                }
              })
            } else {
              addDept(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('新增成功')
                  this.open = false
                  this.getList()
                } else {
                  this.msgError(response.msg)
                }
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        this.$confirm('是否确认删除名称为"' + row.name + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return delDept(row.id)
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        }).catch(function() {
        })
      },

      // 部门状态修改
      handleStatusChange(row) {
        let text = row.isEnable === 0 ? '禁用' : '启用'
        this.$confirm('确认要"' + text + '""' + row.name + '"部门吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return updateDept(row)
        }).then(() => {
          this.msgSuccess(text + '成功')
        }).catch(function() {
          row.isEnable = row.isEnable === 0 ? 1 : 0
        })
      }
    }
  }
</script>
