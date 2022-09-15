<!--此VUE 使用了ruoyi 原来的代码-->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="14" :xs="24">

        <!--default-expand-all  默认展开属性-->
        <el-table
          v-loading="loading"
          :data="menuList"
          row-key="id"
          :expand-row-keys="expandedKeys"
          border highlight-current-row
          @row-click="refreshPermissions"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="name" label="菜单名称" :show-overflow-tooltip="true" width="200"></el-table-column>
          <el-table-column prop="data.orderIndex" label="菜单序号" :show-overflow-tooltip="true" width="100"></el-table-column>
          <el-table-column prop="data.menuUrl" label="菜单链接" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini"
                         type="text"
                         icon="el-icon-edit"
                         @click="handleUpdate(scope.row)"
                         v-hasPermi="['sysMenu:update']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-plus"
                @click="handleAdd(scope.row)"
                v-hasPermi="['sysMenu:add']"
              >新增
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sysMenu:del']"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>

      <!--权限数据-->
      <el-col :span="10" :xs="24">
        <!-- 按钮 -->
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="getPermissionAdd"
              v-hasPermi="['sysMenuPermission:add']"
            >批量添加增删改查权限
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="permissionHandleAdd"
              v-hasPermi="['sysMenuPermission:add']"
            >新增
            </el-button>
          </el-col>
        </el-row>
        <el-table v-loading="loading" :data="permissionList">
          <el-table-column prop="permissionName" label="权限名称" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="permissionCode" label="权限编码" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="isEnableName" label="状态" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini"
                         type="text"
                         icon="el-icon-edit"
                         @click="permissionHandleUpdate(scope.row)"
                         v-hasPermi="['sysMenuPermission:update']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="permissionHandleDelete(scope.row)"
                v-hasPermi="['sysMenuPermission:del']"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog v-if="open" :title="title" :visible.sync="open"  class="pagex-dialog-theme">
      <div class="pagex-from-theme">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-row>
            <el-col :span="24">
              <el-form-item label="上级菜单" prop="fatherMenuId">
                <pagex-formTreeSelect
                  v-model="form.fatherMenuId"
                  url="/basic/ms/sysMenu/tree"
                ></pagex-formTreeSelect>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="菜单名称" prop="menuName">
                <el-input v-model="form.menuName" placeholder="请输入菜单名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="显示排序" prop="orderIndex">
                <el-input-number v-model="form.orderIndex" controls-position="right" :min="0"/>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="命名空间" prop="namespace">
                <el-input v-model="form.namespace" placeholder="请输入命名空间"/>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="路由地址" prop="menuUrl">
                <el-input v-model="form.menuUrl" placeholder="请输入路由地址"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态">
                <pagex-radio  v-model="form.isEnable" :isValueNum="true" dict-code="isEnable"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否显示">
                <pagex-radio  v-model="form.isShow" :isValueNum="true" dict-code="yesOrNo"/>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="图标">
                <e-icon-picker v-model="form.icon"/>
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

    <!-- 添加或修改权限对话框 -->
    <el-dialog :title="title" :visible.sync="permissionOpen" v-if="permissionOpen" class="pagex-dialog-theme" >
      <div class="pagex-from-theme">
        <el-form ref="permissionForm" :model="permissionForm" :rules="permissionRules" label-width="120px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="权限名称" prop="permissionName">
                <el-input v-model="permissionForm.permissionName" placeholder="请输入权限名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="权限编码" prop="permissionCode">
                <el-input v-model="permissionForm.permissionCode" placeholder="请输入权限编码"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="权限类型" prop="permissionType">
                <pagex-select v-model="permissionForm.permissionType" :isValueNum="true"  dict-code="permissionType" placeholder="请选择权限类型"
                              :style="{width: '318px'}">
                </pagex-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态">
                <pagex-radio  v-model="permissionForm.isEnable" :isValueNum="true"  dict-code="isEnable"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="permissionSubmitForm">确 定</el-button>
        <el-button @click="permissionCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listMenu,
    getMenu,
    delMenu,
    addMenu,
    updateMenu,
    getSystemOptions,
    getPermissionList,
    getPermission,
    addAllPermission,
    addPermission,
    updatePermission,
    delPermission
  } from '@/api/system/menu'

  export default {
    name: 'sysMenu',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 菜单表格树数据
        menuList: [],
        // 菜单树选项
        menuOptions: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 菜单状态数据字典
        visibleOptions: [],
        //菜单隐藏数据字典
        yesOrNoOptions: [],
        //菜单菜单类型数据字典
        menuTypeOptions: [],
        //菜单子系统数据字典
        systemOptions: [],
        //菜单所在服务统数据字典
        serverOptions: [],
        //默认展开元素
        expandedKeys: [],

        /****************************** start 以下权限相关 **************** **/
        //权限外键：菜单id
        permissionMenuId: null,
        // 权限菜单页表
        permissionList: [],
        // 权限弹出层标题
        permissionTitle: '',
        // 是否显示弹出层
        permissionOpen: false,
        //菜单所在服务统数据字典
        permissionTypeOptions: [],
        /****************************** end 以下权限相关 **************** **/

        // 查询参数
        queryParams: {
          menuName: undefined,
          visible: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          menuName: [
            { required: true, message: '菜单名称不能为空', trigger: 'blur' }
          ],
          orderIndex: [
            { required: true, message: '菜单顺序不能为空', trigger: 'blur' }
          ],
          namespace: [
            { required: true, message: 'namespace不能为空', trigger: 'blur' }
          ],
          menuUrl: [
            { required: true, message: '链接地址不能为空', trigger: 'blur' }
          ]
        },
        // 权限表单参数
        permissionForm: {},
        // 权限表单校验
        permissionRules: {
          permissionName: [
            { required: true, message: '权限名称不能为空', trigger: 'blur' }
          ],
          permissionCode: [
            { required: true, message: '权限编码不能为空', trigger: 'blur' }
          ],
          permissionType: [
            { required: true, message: '权限类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getList();
      /*getSystemOptions().then(response => {
        this.systemOptions = response
      })*/
    },
    methods: {
      // 选择图标
      selected(name) {
        this.form.icon = name
      },
      /** 查询菜单列表 */
      getList() {
        this.loading = true
        listMenu({}).then(response => {
          this.menuList = response
          this.expandedKeys.push(response[0].id)
          this.loading = false
        })
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      //表单重置
      reset() {
        this.form = {
          menuId: undefined,
          fatherMenuId: 0,
          menuName: undefined,
          icon: undefined,
          namespace: undefined,
          menuUrl: undefined,
          menuType: undefined,
          orderIndex: undefined,
          isFrame: undefined,
          isShow: 1,
          isEnable: 1,
          systemId: undefined
        }
        this.resetForm('form')
      },
      // 权限表单重置
      permissionReset() {
        this.permissionForm = {
          menuId: undefined,
          permissionName: undefined,
          permissionCode: undefined,
          permissionType: undefined,
          isEnable: 1
        }
        this.resetForm('permissionForm')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList()
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset()
        if (row != null) {
          this.form.fatherMenuId = row.id
        }
        this.open = true
        this.title = '添加菜单'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        this.form = row.data
        this.open = true
        this.title = '修改菜单'
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.form.menuId != undefined) {
              updateMenu(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('修改成功')
                  this.open = false
                  this.getList()
                } else {
                  this.msgError(response.msg)
                }
              })
            } else {
              addMenu(this.form).then(response => {
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
        }).then(() => {
           delMenu(row.id).then(() => {
             this.getList()
             this.msgSuccess('删除成功')
           })
        })
      },
      /***************************************** start 以下权限相关 *********************************/
      // 取消按钮
      permissionCancel() {
        this.permissionOpen = false
        this.permissionReset()
      },
      /** 查询权限菜单列表 */
      refreshPermissions(row) {
        this.permissionMenuId = row.id || row;
        getPermissionList(this.permissionMenuId).then(response => {
          this.permissionList = response
        })
      },
      /** 批量增加增删改查权限 **/
      getPermissionAdd() {
        if (this.permissionMenuId == null) {
          this.msgError('请选择菜单')
          return
        }
        addAllPermission(this.permissionMenuId).then(response => {
          if (response.result) {
            this.msgSuccess('新增成功')
            this.open = false
            this.refreshPermissions(this.permissionMenuId)
          } else {
            this.msgError(response.msg)
          }
        })
      },
      /** 新增按钮操作 */
      permissionHandleAdd(row) {
        if (this.permissionMenuId == null) {
          this.msgError('请选择菜单')
          return
        }
        this.permissionReset()
        this.permissionOpen = true
        this.permissionTitle = '添加权限'
      },
      /** 修改按钮操作 */
      permissionHandleUpdate(row) {
        this.permissionReset()
        getPermission(row.permissionId).then(response => {
          this.permissionForm = response
          this.permissionOpen = true
          this.title = '修改权限'
        })
      },
      /** 权限提交按钮 */
      permissionSubmitForm: function() {
        this.$refs['permissionForm'].validate(valid => {
          if (valid) {
            this.permissionForm.menuId = this.permissionMenuId
            if (this.permissionForm.permissionId != undefined) {
              updatePermission(this.permissionForm).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('修改成功')
                  this.permissionOpen = false
                  this.refreshPermissions(this.permissionMenuId)
                } else {
                  this.msgError(response.msg)
                }
              })
            } else {
              addPermission(this.permissionForm).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('新增成功')
                  this.permissionOpen = false
                  this.refreshPermissions(this.permissionMenuId)
                } else {
                  this.msgError(response.msg)
                }
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      permissionHandleDelete(row) {
        this.$confirm('是否确认删除名称为"' + row.permissionName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
           delPermission(row.permissionId).then(()=>{
            this.refreshPermissions(this.permissionMenuId)
            this.msgSuccess('删除成功')
          })
        })
      },
    }
  }
</script>
