<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="子系统名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item prop="isEnable">
        <el-select v-model="queryParams.isEnable" placeholder="子系统状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.wordbookCode"
            :label="dict.wordbookDesc"
            :value="dict.wordbookCode"
          />
        </el-select>
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
          hasPermi="['sett_ms_system::add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          hasPermi="['sett_ms_system:export']"
        >导出
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="msSystemList">
      <el-table-column label="子系统名称" align="center" prop="name"/>
      <el-table-column label="岗位排序" align="center" prop="sort"/>
      <el-table-column prop="transMap.isEnableName" label="状态" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="transMap.typeName" label="类型" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="transMap.createUserUserName" :show-overflow-tooltip="true"/>
      <el-table-column label="修改时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改人" align="center" prop="transMap.updateUserUserName" :show-overflow-tooltip="true"/>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sett_ms_system:update']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sett_ms_system:del']"
          >删除
          </el-button>
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

    <!-- 添加或修改岗位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入子系统名称"/>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="状态" prop="isEnable">
          <el-radio-group v-model="form.isEnable">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.wordbookCode"
              :label="dict.wordbookCode"
            >{{dict.wordbookDesc}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="类型" >
            <el-option
              v-for="item in systemTypeOptions"
              :key="item.wordbookCode"
              :label="item.wordbookDesc"
              :value="item.wordbookCode"
            ></el-option>
          </el-select>
        </el-form-item>
          <el-form-item label="跳转url" prop="url">
            <el-input v-model="form.url" placeholder="请输入跳转url"/>
          </el-form-item>
          <el-form-item label="首页url" prop="indexUrl">
            <el-input v-model="form.indexUrl" placeholder="请输入首页url"/>
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
  import {
    listMsSystem,
    getMsSystem,
    delMsSystem,
    addMsSystem,
    updateMsSystem,
    exportMsSystem
  } from '@/api/system/msSystem'

  export default {
    name: 'msSystem',
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
        // 岗位表格数据
        msSystemList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 状态数据字典
        statusOptions: [],
        //类型数据字典
        systemTypeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          isEnable: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            { required: true, message: '子系统名称不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: '排序不能为空', trigger: 'blur' }
          ],
          isEnable: [
            { required: true, message: '状态不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getList()
      this.getDicts('is_enable').then(response => {
        this.statusOptions = response
      })
      this.getDicts('system_type').then(response => {
        this.systemTypeOptions = response
      })
    },
    methods: {
      /** 查询岗位列表 */
      getList() {
        this.loading = true
        listMsSystem(this.queryParams).then(response => {
          this.msSystemList = response.rows
          this.total = response.total
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
          name: undefined,
          sort: 0,
          type: "",
          isEnable: '0',
          url: "",
          indexUrl: "",
          logo : ""
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.postId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.open = true
        this.title = '添加子系统'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        const id = row.id
        getMsSystem(id).then(response => {
          this.form = response
          this.form.type = response.type.toString();
          this.form.isEnable = response.isEnable.toString();
          this.open = true
          this.title = '修改子系统'
        })
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateMsSystem(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('修改成功')
                  this.open = false
                  this.getList()
                } else {
                  this.msgError(response.msg)
                }
              })
            } else {
              addMsSystem(this.form).then(response => {
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
        const id = row.id || this.ids
        this.$confirm('是否确认删除子系统编号为"' + id + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return delMsSystem(id)
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        }).catch(function() {
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams
        this.$confirm('是否确认导出子系统数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return exportMsSystem(queryParams)
        }).then(response => {
          this.download(response.msg)
        }).catch(function() {
        })
      }
    }
  }
</script>
