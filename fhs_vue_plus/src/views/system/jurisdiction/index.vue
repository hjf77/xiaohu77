<template>
  <div class="app-container">
    <base-container>
      <organization is-filter slot="org" api="/ms/sysOrganization/tree" method-type="POST" @check="handleNodeClick"></organization>

      <div style="padding:20px;background: #FFFFFF;">
        <div style="margin-bottom:20px;">
          <!-- <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['sysRole:add']"
          >新增
          </el-button> -->
        </div>
        <!--角色数据-->
        <el-table v-loading="loading" :data="roleList">
          <el-table-column width="55" align="center"/>
          <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true" width="150"/>
          <el-table-column label="状态" align="center" prop="isEnableName" width="150">
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
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['sysRole:update']"
              >授权
              </el-button>
              <!-- <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sysRole:del']"
              >删除
              </el-button> -->
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.page"
          :limit.sync="queryParams.rows"
          @pagination="getList"
        />
      </div>


    </base-container>


    <!--    <el-row :gutter="20">-->
    <!--      &lt;!&ndash;部门数据&ndash;&gt;-->

    <!--      <el-col :span="20" :xs="24">-->
    <!--        <el-row :gutter="10" class="mb8">-->
    <!--          <el-col :span="1.5">-->
    <!--            <el-button-->
    <!--              type="primary"-->
    <!--              icon="el-icon-plus"-->
    <!--              size="mini"-->
    <!--              @click="handleAdd"-->
    <!--              v-hasPermi="['sysRole:add']"-->
    <!--            >新增</el-button>-->
    <!--          </el-col>-->
    <!--        </el-row>-->

    <!--        &lt;!&ndash;角色数据&ndash;&gt;-->
    <!--        <el-table v-loading="loading" :data="roleList">-->
    <!--          <el-table-column width="55" align="center"/>-->
    <!--          <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true" width="150"/>-->
    <!--          <el-table-column label="状态" align="center" prop="isEnableName" width="150">-->
    <!--          </el-table-column>-->
    <!--          <el-table-column label="所属机构" align="center" prop="transMap.orgName" :show-overflow-tooltip="true"/>-->
    <!--          <el-table-column label="备注" prop="remark" :show-overflow-tooltip="true" width="150"/>-->
    <!--          <el-table-column label="创建时间" align="center" prop="createTime" width="180">-->
    <!--            <template slot-scope="scope">-->
    <!--              <span>{{ parseTime(scope.row.createTime) }}</span>-->
    <!--            </template>-->
    <!--          </el-table-column>-->
    <!--          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
    <!--            <template slot-scope="scope">-->
    <!--              <el-button-->
    <!--                size="mini"-->
    <!--                type="text"-->
    <!--                icon="el-icon-edit"-->
    <!--                @click="handleUpdate(scope.row)"-->
    <!--                v-hasPermi="['sysRole:update']"-->
    <!--              >修改-->
    <!--              </el-button>-->
    <!--              <el-button-->
    <!--                size="mini"-->
    <!--                type="text"-->
    <!--                icon="el-icon-delete"-->
    <!--                @click="handleDelete(scope.row)"-->
    <!--                v-hasPermi="['sysRole:del']"-->
    <!--              >删除-->
    <!--              </el-button>-->
    <!--            </template>-->
    <!--          </el-table-column>-->
    <!--        </el-table>-->

    <!--        <pagination-->
    <!--          v-show="total>0"-->
    <!--          :total="total"-->
    <!--          :page.sync="queryParams.pageNum"-->
    <!--          :limit.sync="queryParams.pageSize"-->
    <!--          @pagination="getList"-->
    <!--        />-->

    <!--      </el-col>-->
    <!--    </el-row>-->

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <!-- <el-col :span="12">
            <el-form-item label="角色名称" prop="roleName">
              <el-input v-model="form.roleName" placeholder="请输入角色名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归属部门" prop="organizationId">
             <treeselect v-model="form.organizationId" :options="deptOptions" placeholder="请选择归属部门"
                         :normalizer="normalizer"/>
              <pagex-formTreeSelect
                v-model="form.organizationId"
                api="/ms/sysOrganization/selectTree"
              ></pagex-formTreeSelect>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="12">
            <el-form-item>
              <label slot="label">状&#8195;&#8195;态</label>
              <el-radio-group v-model="form.isEnable">
                <el-radio
                  v-for="dict in statusOptions"
                  :value="dict.wordbookCode"
                  :label="dict.wordbookCode"
                  :key="dict.wordbookCode"
                >{{ dict.wordbookDesc }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item label="菜单列表" prop="methods">
              <el-tree
                :data="menuOptions"
                show-checkbox
                ref="name"
                node-key="id"
                :default-expanded-keys="expandedKeys"
                empty-text="加载中，请稍后"
                :props="menuProps"
              ></el-tree>
            </el-form-item>
          </el-col>

          <!-- <el-col :span="24">
            <el-form-item>
              <label slot="label">备&#8195;&#8195;注</label>
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col> -->
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
import {listRole, getRole, delRole, addRole, updateRole, dataScope, getPermissionByRoleId} from '@/api/system/role'
import {treeselect} from '@/api/system/sysOrganization'
import Treeselect from '@riophae/vue-treeselect'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import Organization from "../../../components/Organization";

export default {
  name: 'Role',
  components: {Treeselect, Organization},
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
      //顶层元素
      fatherId: undefined,
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层（数据权限）
      openDataScope: false,
      // 状态数据字典
      statusOptions: [],
      // 菜单列表
      menuOptions: [],
      //tree 默认展开节点
      expandedKeys: [],
      // 查询参数
      queryParams: {
        page:1,
        rows:10,
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
          children: (node.children instanceof Array && node.children.length > 0) ? node.children : null
        }
      },
      // 表单校验
      rules: {
        // roleName: [
        //   {required: true, message: '角色名称不能为空', trigger: 'blur'}
        // ],
        // organizationId: [
        //   {required: true, message: '部门归属不能为空', trigger: 'blur'}
        // ]
      }
    }
  },
  created() {
    this.getTreeselect();
    this.getDicts('is_enable').then(response => {
      this.statusOptions = response
    })
  },
  mounted() {
    // setTimeout(() => {
    //   this.queryParams.organizationId = this.fatherId
    //   this.getList()
    // }, 500);
  },
  methods: {
    /** 查询角色列表 */
    getList() {
      this.loading = true
      listRole(this.queryParams).then(
        response => {
          this.roleList = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    /** 查询菜单权限树结构 */
    getMenuTreeselect() {
      dataScope().then(response => {
        this.menuOptions = response
        this.expandedKeys.push(response[0].id)
      })
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response
        this.fatherId = response[0].id;
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
      }).then(function () {
        return updateRole(row)
      }).then(() => {
        this.msgSuccess(text + '成功')
      }).catch(function () {
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
      if (this.$refs.name != undefined) {
        this.$refs.name.setCheckedKeys([])
      }
      this.form = {
        // roleId: undefined,
        // roleName: undefined,
        isEnable: '1',
        methods: [],
        // remark: undefined
      }
      this.resetForm('form')
    },
    /** 新增按钮操作 */
    // handleAdd() {
    //   this.reset()
    //   this.getMenuTreeselect()
    //   this.form.organizationId = this.queryParams.organizationId
    //   this.open = true
    //   this.title = '添加角色'
    // },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getTreeselect()
      this.getMenuTreeselect()
      const roleId = row.roleId
      getRole(roleId).then(response => {
        this.form = response
        this.form.isEnable = response.isEnable.toString()
        this.title = '角色授权'
      })
      this.open = true
      /** 根据角色ID查询菜单树结构 **/
      this.$nextTick(() => {
        getPermissionByRoleId(roleId).then(response => {
          this.$nextTick(() => {
            this.$refs.name.setCheckedKeys(response);
          });
        });
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      var treeKeys = this.$refs.name.getCheckedKeys()
      var ids = []
      var methods = []
      this.getTreeNodes(treeKeys, ids)
      ids.forEach(function (id) {
        if (id.startsWith("p_")) {
          methods.push(id)
        }
      })
      this.form.methods = methods
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
    // handleDelete(row) {
    //   this.$confirm('是否确认删除角色名称为"' + row.roleName + '"的数据项?', '警告', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(function () {
    //     return delRole(row)
    //   }).then(() => {
    //     this.getList()
    //     this.msgSuccess('删除成功')
    //   }).catch(function () {
    //   })
    // },
    // 获取选中的树节点
    getTreeNodes(childNodes, ids) {
      // 将所有选中的子节点保存
      for (var i = 0; i < childNodes.length; i++) {
        ids.push(childNodes[i])
        // 获取父级节点
        this.getTreeParentNode(childNodes[i], ids)
      }
    },
    // 递归查询所有上级数据
    getTreeParentNode(id, ids) {
      // 获取当前节点的上级节点id
      var parentId = this.$refs.name.getNode(id).parent.data.id
      if (parentId && parentId !== null) {
        ids.push(parentId)
        this.getTreeParentNode(parentId, ids)
      }
    }
  }
}
</script>
