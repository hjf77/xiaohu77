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
        <pagex-select v-model="queryParams.isEnable" placeholder="部门状态" clearable size="small" dict-code="isEnable"></pagex-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button>
        <el-button   type="primary" size="mini" plain @click="resetQuery">重置</el-button>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['sysOrganization:add']"
        >新增
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="deptList"
      row-key="id"
      style="width: 100%"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="name" label="部门名称"  min-width="60%"></el-table-column>
      <el-table-column prop="data.isEnableName" label="状态"  min-width="10%"   align="center">
      </el-table-column>
      <el-table-column label="创建时间" align="center" min-width="10%"  prop="createTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.data.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="20%" class-name="small-padding fixed-width">
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
    <el-dialog :title="title" :visible.sync="open" class="pagex-dialog-theme"  >
      <div class="pagex-from-theme">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-row>
            <el-col :span="24" v-if="form.parentId !== ''">
              <el-form-item label="上级部门" prop="parentId">
                <pagex-formTreeSelect
                  :disabled="this.form.id != undefined" v-model="form.parentId"
                  api="/basic/ms/sysOrganization/tree"
                ></pagex-formTreeSelect>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="部门名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入部门名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="部门状态">
                <pagex-radio v-model="form.isEnable"  :isValueNum="true" dict-code="isEnable"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否子公司">
                <pagex-radio v-model="form.isCompany" :disabled="this.form.id != undefined" :isValueNum="true" dict-code="yesOrNo"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
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
    components: {Treeselect },
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
        isEdit:false,
        // 查询参数
        queryParams: {
          nameOP:'like',
          parse: true,
          name: undefined,
          isEnable: undefined,
        },
        // 表单参数
        form: {
          extJson:null,
        },
        // 表单校验
        rules: {
          parentId: [
            { required: true, message: '上级部门不能为空', trigger: 'blur' }
          ],
          name: [
            { required: true, message: '部门名称不能为空', trigger: 'blur' }
          ],
          isCompany: [
            { required: true, message: '请选择是否为单位', trigger: 'blur' }
          ],
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {
      /** 查询部门列表 */
      getList() {
        this.loading = true
        listDept(this.queryParams).then(response => {
          this.deptList = this.handleTree(response, 'deptId')
          this.loading = false
        })
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
        this.queryParams.name = undefined;
        this.queryParams.isEnable = undefined;
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset()
        if (row != undefined) {
          this.form.parentId = row.id
        }
        this.$set(this.form,'isCompany',0)
        this.$set(this.form,'isEnable',1)
        this.open = true
        this.isEdit = false;
        this.title = '添加部门'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        this.isEdit = true;
        getDept(row.id).then(response => {
          this.form = response
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
