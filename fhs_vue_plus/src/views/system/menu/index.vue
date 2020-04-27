<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="14" :xs="24">

        <el-table
          v-loading="loading"
          :data="menuList"
          row-key="id"
          border highlight-current-row
          default-expand-all @row-click="getPermissionClick"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="menuName" label="菜单名称" :show-overflow-tooltip="true" width="200"></el-table-column>
          <el-table-column prop="orderIndex" label="菜单序号" :show-overflow-tooltip="true" width="100"></el-table-column>
          <el-table-column prop="menuUrl" label="菜单链接" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini"
                         type="text"
                         icon="el-icon-edit"
                         @click="handleUpdate(scope.row)"
                         hasPermi="['system:menu:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-plus"
                @click="handleAdd(scope.row)"
                hasPermi="['system:menu:add']"
              >新增
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                hasPermi="['system:menu:remove']"
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
              hasPermi="['system:menu:add']"
            >批量添加增删改查权限</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="permissionHandleAdd"
              hasPermi="['system:user:add']"
            >新增</el-button>
          </el-col>
        </el-row>
        <el-table v-loading="loading" :data="permissionList">
          <el-table-column prop="permissionName" label="权限名称" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="method" label="方法名称" :show-overflow-tooltip="true" ></el-table-column>
          <el-table-column prop="transMap.isEnableName" label="状态" :show-overflow-tooltip="true" ></el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini"
                         type="text"
                         icon="el-icon-edit"
                         @click="permissionHandleUpdate(scope.row)"
                         hasPermi="['system:menu:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="permissionHandleDelete(scope.row)"
                hasPermi="['system:menu:remove']"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单" prop="fatherMenuId">
              <treeselect
                v-model="form.fatherMenuId"
                :options="menuOptions"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="选择上级菜单"
              />
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
          <el-col :span="12">
            <el-form-item label="space" prop="namespace">
              <el-input v-model="form.namespace" placeholder="请输入mamespace"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在服务" prop="serverNameId">
              <el-select v-model="form.serverNameId" placeholder="请选择所在服务">
                <el-option
                  v-for="item in serverOptions"
                  :key="item.id"
                  :label="item.serverName"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="链接地址" prop="menuUrl">
              <el-input v-model="form.menuUrl" placeholder="请输入链接地址"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.isEnable">
                <el-radio
                  v-for="dict in visibleOptions"
                  :value="dict.wordbookCode"
                  :label="dict.wordbookCode"
                >{{dict.wordbookDesc}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否隐藏">
              <el-radio-group v-model="form.menuState">
                <el-radio
                  v-for="dict in yesOrNoOptions"
                  :value="dict.wordbookCode"
                  :label="dict.wordbookCode"
                >{{dict.wordbookDesc}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="子系统" prop="systemId">
              <el-select v-model="form.systemId" placeholder="请选择子系统">
                <el-option
                  v-for="item in systemOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单类型" prop="menuType">
              <el-select v-model="form.menuType" placeholder="请选择菜单类型">
                <el-option
                  v-for="item in menuTypeOptions"
                  :key="item.wordbookCode"
                  :label="item.wordbookDesc"
                  :value="item.wordbookCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!--
                  <el-col :span="24">
                      <el-form-item v-if="form.image != 'F'" label="菜单图标">
                        <el-popover
                          placement="bottom-start"
                          width="460"
                          trigger="click"
                          @show="$refs['iconSelect'].reset()"
                        >
                          <IconSelect ref="iconSelect" @selected="selected" />
                          <el-input slot="reference" v-model="form.icon" placeholder="点击选择图标" readonly>
                            <svg-icon
                              v-if="form.icon"
                              slot="prefix"
                              :icon-class="form.icon"
                              class="el-input__icon"
                              style="height: 32px;width: 16px;"
                            />
                            <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-col>
          -->
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改权限对话框 -->
    <el-dialog :title="title" :visible.sync="permissionOpen" width="600px">
      <el-form ref="permissionForm" :model="permissionForm" :rules="permissionRules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="permissionName">
              <el-input v-model="permissionForm.permissionName" placeholder="请输入菜单名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="方法名称" prop="method">
              <el-input v-model="permissionForm.method" placeholder="请输入方法名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单类型" prop="permissionType" >
              <el-select v-model="permissionForm.permissionType" placeholder="请选择菜单类型" @change="menuTypeChange" >
                <el-option
                  v-for="item in permissionTypeOptions"
                  :key="item.wordbookCode"
                  :label="item.wordbookDesc"
                  :value="item.wordbookCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="permissionForm.isEnable " >
                <el-radio
                  v-for="dict in visibleOptions"
                  :value="dict.wordbookCode"
                  :label="dict.wordbookCode"
                >{{dict.wordbookDesc}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="permissionSubmitForm">确 定</el-button>
        <el-button @click="permissionCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style>
  .el-table__body tr.current-row>td {
    background: #f57878 !important;
  }
</style>

<script>
  import {
    listMenu,
    getMenu,
    delMenu,
    addMenu,
    updateMenu,
    getMenuTree,
    getServerOptions,
    getSystemOptions,
    getPermissionList,
    getPermission,
    addAllPermission,
    addPermission,
    updatePermission,
    delPermission
  } from '@/api/system/menu'
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'

  export default {
    name: 'Menu',
    components: { Treeselect },
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
          serverNameId: [
            { required: true, message: '所在服务不能为空', trigger: 'blur' }
          ],
          menuUrl: [
            { required: true, message: '链接地址不能为空', trigger: 'blur' }
          ],
          systemId: [
            { required: true, message: '子系统不能为空', trigger: 'blur' }
          ],
          menuType: [
            { required: true, message: '菜单类型不能为空', trigger: 'blur' }
          ]
        },
        // 权限表单参数
        permissionForm: {},
        // 权限表单校验
        permissionRules: {
          permissionName: [
            { required: true, message: '权限名称不能为空', trigger: 'blur' }
          ],
          method: [
            { required: true, message: '方法名称不能为空', trigger: 'blur' }
          ],
          permissionType: [
            { required: true, message: '菜单类型不能为空', trigger: 'blur' }
          ],
        }
      }
    },
    created() {
      this.getList()
      this.getDicts('is_enable').then(response => {
        this.visibleOptions = response
      })
      this.getDicts('yesOrNo').then(response => {
        this.yesOrNoOptions = response
      })
      this.getDicts('menu_type').then(response => {
        this.menuTypeOptions = response
      })
      this.getDicts('permission_type').then(response => {
        this.permissionTypeOptions = response
      })
      getServerOptions().then(response => {
        this.serverOptions = response
      })
      getSystemOptions().then(response => {
        this.systemOptions = response
      })
    },
    methods: {
      // 选择图标
      selected(name) {
        this.form.icon = name
      },
      /** 查询菜单列表 */
      getList() {
        this.loading = true
        listMenu(this.queryParams).then(response => {
          this.menuList = response
          this.loading = false
        })
      },
      /** 转换菜单数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children
        }
        return {
          id: node.menuId,
          label: node.menuName,
          children: node.children
        }
      },
      /** 查询菜单下拉树结构 */
      getTreeselect() {
        listMenu().then(response => {
          this.menuOptions = response
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
          serverNameId: undefined,
          menuUrl: undefined,
          menuType: undefined,
          orderIndex: undefined,
          isFrame: undefined,
          menuState: '0',
          isEnable: '1',
          systemId: undefined
        }
        this.resetForm('form')
      },
      // 权限表单重置
      permissionReset() {
        this.permissionForm = {
          menuId: undefined,
          permissionName: undefined,
          method: undefined,
          permissionType: undefined,
          isEnable: '1'
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
        this.getTreeselect()
        if (row != null) {
          this.form.fatherMenuId = row.menuId
        }
        this.open = true
        this.title = '添加菜单'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        this.getTreeselect()
        getMenu(row.menuId).then(response => {
          this.form = response
          this.form.isEnable = response.isEnable.toString()
          this.form.menuState = response.menuState.toString()
          this.form.serverNameId = response.serverNameId.toString()
          this.open = true
          this.title = '修改菜单'
        })
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
        this.$confirm('是否确认删除名称为"' + row.menuName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return delMenu(row.menuId)
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        }).catch(function() {
        })
      },
      /***************************************** start 以下权限相关 *********************************/
      // 取消按钮
      permissionCancel() {
        this.permissionOpen = false
        this.permissionReset()
      },
      /** 查询权限菜单列表 */
      getPermissionClick(row) {
        this.permissionMenuId = row.menuId || row
        getPermissionList(this.permissionMenuId).then(response => {
          this.permissionList = response
        })
      },
      /** 批量增加增删改查权限 **/
      getPermissionAdd(){
        if (this.permissionMenuId == null) {
          this.msgError("请选择菜单");
          return;
        }
        addAllPermission(this.permissionMenuId).then(response => {
          if (response.result) {
            this.msgSuccess('新增成功')
            this.open = false
            this.getPermissionClick(this.permissionMenuId);
          } else {
            this.msgError(response.msg)
          }
        })
      },
      /** 新增按钮操作 */
      permissionHandleAdd(row) {
        if (this.permissionMenuId == null) {
          this.msgError("请选择菜单");
          return;
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
          this.permissionForm.isEnable = response.isEnable.toString()
          this.permissionForm.permissionType = response.permissionType.toString()
          this.permissionOpen = true
          this.title = '修改权限'
        })
      },
      /** 权限提交按钮 */
      permissionSubmitForm: function() {
        this.$refs['permissionForm'].validate(valid => {
          if (valid) {
            this.permissionForm.menuId = parseInt(this.permissionMenuId)
            if (this.permissionForm.permissionId != undefined) {
              updatePermission(this.permissionForm).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('修改成功')
                  this.permissionOpen = false
                  this.getPermissionClick(this.permissionMenuId)
                } else {
                  this.msgError(response.msg)
                }
              })
            } else {
              addPermission(this.permissionForm).then(response => {
                if (response.code === 200) {
                  this.msgSuccess('新增成功')
                  this.permissionOpen = false
                  this.getPermissionClick(this.permissionMenuId)
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
        }).then(function() {
          return delPermission(row.permissionId)
        }).then(() => {
          this.getPermissionClick(this.permissionMenuId)
          this.msgSuccess('删除成功')
        }).catch(function() {
        })
      },
      /** 下拉选中事件 **/
      menuTypeChange(index){
       var data =  this.permissionTypeOptions[index - 1]
      },
    }
  }
</script>
