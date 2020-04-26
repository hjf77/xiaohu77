<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>

      <el-col :span="20" :xs="24">
        <el-row :gutter="10" class="mb8">
        </el-row>

        <!--角色数据-->
        <el-table v-loading="loading" :data="roleList">
          <el-table-column width="55" align="center"/>
          <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true" width="150"/>
          <el-table-column label="状态" align="center" width="100">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.isEnable"
                active-value=0
                inactive-value=1
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="所属机构" align="center" prop="transMap.orgName" :show-overflow-tooltip="true"/>
          <el-table-column label="备注" prop="remark" :show-overflow-tooltip="true" width="150"/>
          <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-plus"
                @click="handleAdd(scope.row)"
                hasPermi="['system:dept:add']"
              >新增
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                hasPermi="['system:role:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                hasPermi="['system:role:remove']"
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
      </el-col>
    </el-row>

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色名称" prop="roleName">
              <el-input v-model="form.roleName" placeholder="请输入角色名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归属部门" prop="organizationId"  >
              <treeselect v-model="form.organizationId" :options="deptOptions" placeholder="请选择归属部门"  :normalizer="normalizer" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.isEnable">
                <el-radio
                  v-for="dict in statusOptions"
                  :value="dict.wordbookCode"
                  :label="dict.wordbookCode"
                >{{dict.wordbookDesc}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单权限" prop="methods">
              <el-tree
                :data="menuOptions"
                show-checkbox
                ref="name"
                node-key="id"
                empty-text="加载中，请稍后"
                :props="menuProps"
              ></el-tree>
            </el-form-item>
          </el-col>

          <el-col :span="24">
           <treeTable></treeTable>
          </el-col>

          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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
  import { listRole, getRole, delRole, addRole, updateRole, dataScope } from '@/api/system/role'
  import { treeselect } from '@/api/system/dept'
  import Treeselect from '@riophae/vue-treeselect'
  import TreeTableAuthor from '@/components/TreeTableAuthor'
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: 'Role',
    components: { Treeselect,TreeTableAuthor },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 角色表格数据
        roleList: [],
        // 总条数
        total: 0,
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 部门树选项
        deptOptions: undefined,
        // 是否显示弹出层（数据权限）
        openDataScope: false,
        // 状态数据字典
        statusOptions: [],
        // 菜单列表
        menuOptions: [],
        // 查询参数
        queryParams: {
          organizationId: undefined
        },
        // 表单参数
        form: {},
        defaultProps: {
          children: 'children',
          label: 'text'
        },
        menuProps: {
          children: 'children',
          label: 'name'
        },
        //给表单部门tree改别名
        normalizer(node) {
          return {
            id: node.id,
            label: node.text,
            children: node.children
          }
        },
        // 表单校验
        rules: {
          roleName: [
            { required: true, message: '角色名称不能为空', trigger: 'blur' }
          ],
          organizationId: [
            { required: true, message: '部门归属不能为空', trigger: 'blur' }
          ],
          methods: [
            { required: true, message: '权限不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getTreeselect()
      this.queryParams.organizationId = '001'
      this.getList()
      this.getDicts('is_enable').then(response => {
        this.statusOptions = response
      })
    },
    methods: {
      /** 查询角色列表 */
      getList() {
        this.loading = true
        listRole(this.queryParams.organizationId).then(
          response => {
            this.roleList = response.rows
            this.total = response.total
            this.loading = false
          }
        )
      },
      /** 查询菜单树结构 */
      getMenuTreeselect() {
        dataScope().then(response => {
          this.menuOptions = response
        })
      },
      /** 查询部门下拉树结构 */
      getTreeselect() {
        treeselect().then(response => {
          this.deptOptions = response
        })
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true
        return data.label.indexOf(value) !== -1
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.organizationId = data.id
        this.getList()
      },

      // 角色状态修改
      handleStatusChange(row) {
        let text = row.isEnable === '0' ? '停用' : '启用'
        this.$confirm('确认要"' + text + '""' + row.roleName + '"角色吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return updateRole(row)
        }).then(() => {
          this.msgSuccess(text + '成功')
        }).catch(function() {
          row.isEnable = row.isEnable === '0' ? '1' : '0'
        })
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      // 表单重置
      reset() {
        if (this.$refs.menu != undefined) {
          this.$refs.menu.setCheckedKeys([])
        }
        this.form = {
          roleId: undefined,
          roleName: undefined,
          isEnable: '0',
          methods: [],
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset()
        this.getMenuTreeselect()
        if (row != undefined) {
          this.form.organizationId = row.organizationId
        }
        this.open = true
        this.title = '添加角色'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        this.getTreeselect()
        const roleId = row.roleId || this.ids
        this.$nextTick(() => {
          this.getRoleMenuTreeselect(roleId)
        })
        this.getMenuTreeselect()
        getRole(roleId).then(response => {
          this.form = response
          this.form.isEnable = response.isEnable.toString()
          this.open = true
          this.title = '修改角色'
        })
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.form.roleId != undefined) {
              updateRole(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('修改成功')
                  this.open = false
                  this.getList()
                } else {
                  this.msgError(response.msg)
                }
              })
            } else {
              addRole(this.form).then(response => {
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
        this.$confirm('是否确认删除角色名称为"' + row.roleName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return delRole(row)
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        }).catch(function() {
        })
      }
    }
  }
</script>
