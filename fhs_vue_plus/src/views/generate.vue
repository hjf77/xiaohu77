<template>
  <div id="generateApp">
    <div class="xhb-crud-top">
      <span class="soft-name">FHS代码生成器</span>
    </div>
    <div class="xhb-crud-header">
      <div class="xhb-crud--header-menu">
        <el-button type="success " size="mini">暂存</el-button>
        <el-button type="primary" size="mini" @click="createCode">生成代码</el-button>
      </div>
    </div>
    <div class="xhb-crud-container">
      <main class="xhb-crud-main">
        <div class="xhb-curd-wrap">
          <div class="left">
            <div class="xhb-crud-group">
              <el-form class="group-form" :mode="baseForm" size="mini">
                <el-form-item label="数据库名称(找后台要)">
                  <el-input v-model="baseForm.dbName"></el-input>
                </el-form-item>
                <el-form-item label="表格名(找后台要)">
                  <el-input v-model="baseForm.tableName"></el-input>
                </el-form-item>
                <el-form-item label="作者(就是你的名字)">
                  <el-input v-model="baseForm.author"></el-input>
                </el-form-item>
                <el-form-item label="">
                  <el-button type="primary" @click="getTableInfo">解析</el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>
          <div class="middle">
            <div class="xhb-crud-tab middle-tools">
              <div class="table-header">
                <el-checkbox-group>
                  <!--                  <el-checkbox label="add" name="add">添加</el-checkbox>-->
                  <!--                  <el-checkbox label="edit" name="edit">修改</el-checkbox>-->
                  <!--                  <el-checkbox label="select" name="select">查询</el-checkbox>-->
                  <!--                  <el-checkbox label="del" name="del">删除</el-checkbox>-->
                </el-checkbox-group>
              </div>
              <div class="table-header">
                <!--                <el-checkbox label="del" name="del">删除</el-checkbox>-->
              </div>
            </div>
            <div class="xhb-curd-content">
              <div class="crud-table">
                <el-table
                  ref="table"
                  :data="baseForm.fieldsVOS"
                  tooltip-effect="dark"
                  style="width: 100%"
                  height="100%"
                  @select="selectRow"
                  @select-all="selectAll"
                  @selection-change="selectionChange">
                  <el-table-column
                    type="selection"
                    width="55">
                  </el-table-column>
                  <el-table-column
                    type="index"
                    width="55">
                  </el-table-column>
                  <el-table-column
                    label="字段名"
                    prop="filedName"
                  >
                  </el-table-column>
                  <el-table-column
                    label="prop名称"
                  >
                    <template slot-scope="scope">
                      <!--                      <el-input ref="gain" size="mini" v-if="scope.row.isOk" @keyup.native.enter="blurClick(scope)"-->
                      <!--                                @blur="blurClick(scope)" v-model="scope.row.comment"-->
                      <!--                                style="width:150px"></el-input>-->
                      <el-input ref="gain" size="mini" v-if="scope.row.isOk" v-model="scope.row.comment"
                                style="width:150px"></el-input>
                      <span size="mini" v-else>{{ scope.row.comment }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="字段类型"
                    prop="pageElementType"
                  >
                  </el-table-column>
                  <el-table-column
                    label="是否忽略"
                    width="100"
                  >
                    <template slot-scope="scope">
                      <el-switch
                        v-model="scope.row.isIgnore"
                        active-color="#13ce66"
                        :inactive-value="switchStatus[0]"
                        :active-value="switchStatus[1]"
                        inactive-color="#ff4949">
                      </el-switch>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="列表展示"
                    width="100"
                  >
                    <template slot-scope="scope">
                      <el-switch
                        v-model="scope.row.isList"
                        active-color="#13ce66"
                        :inactive-value="switchStatus[0]"
                        :active-value="switchStatus[1]"
                        inactive-color="#ff4949">
                      </el-switch>
                    </template>
                  </el-table-column>

                  <el-table-column
                    label="表单展示"
                    width="100"
                  >
                    <template slot-scope="scope">
                      <el-switch
                        v-model="scope.row.isForm"
                        active-color="#13ce66"
                        :inactive-value="switchStatus[0]"
                        :active-value="switchStatus[1]"
                        inactive-color="#ff4949">
                      </el-switch>
                    </template>
                  </el-table-column>

                  <el-table-column
                    label="列表过滤条件"
                    width="100"
                  >
                    <template slot-scope="scope">
                      <el-switch
                        v-model="scope.row.isFilter"
                        active-color="#13ce66"
                        :inactive-value="switchStatus[0]"
                        :active-value="switchStatus[1]"
                        inactive-color="#ff4949">
                      </el-switch>

                    </template>
                  </el-table-column>
                  isIgnore
                  <el-table-column
                    label="是否必填"
                    width="100"
                  >
                    <template slot-scope="scope">
                      <el-switch
                        v-model="scope.row.isRequired"
                        active-color="#13ce66"
                        :inactive-value="switchStatus[0]"
                        :active-value="switchStatus[1]"
                        inactive-color="#ff4949">
                      </el-switch>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
          <div class="right" v-show="isChecked">
            <div class="xhb-crud-tabs">
              <div class="xhb-crud-tab">
                CRUD配置
              </div>

              <div class="xhb-crud-group">
                <el-form class="group-form" :mode="configForm" size="mini">
                  <el-form-item label="组件类型">
                    <el-select size="mini" v-model="configForm.pageElementType" placeholder="请选择">
                      <el-option
                        v-for="item in componentList"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>

                  <!--此处为select相关-->
                  <div class="type_select" v-if="configForm.pageElementType == 'select'">
                    <el-form-item label="字典编码(数据源是字典的时候必填)">
                      <el-input type="text" v-model="configForm.extParam.dictCode"></el-input>
                    </el-form-item>
                    <el-form-item label="API接口地址(非字典当作数据源必填)">
                      <el-input type="text" v-model="configForm.extParam.api"></el-input>
                    </el-form-item>
                    <el-form-item label="请求类型(POST需要配合querys参数)">
                      <pagex-select v-model="configForm.extParam.methodType" :options="[{
                                                                                  value: 'GET',
                                                                                  label: 'GET'
                                                                                }, {
                                                                                  value: 'POST',
                                                                                  label: 'POST'
                                                                                }]">

                      </pagex-select>
                    </el-form-item>
                    <el-form-item label="label字段名(组件默认值为title)">
                      <el-input type="text" v-model="configForm.extParam.labelField"></el-input>
                    </el-form-item>
                    <el-form-item label="value字段名(组件默认值为id)">
                      <el-input type="text" v-model="configForm.extParam.valueField"></el-input>
                    </el-form-item>
                  </div>
                  <!--此处为select相关-->

                  <!--此处为textarea相关-->
                  <div class="type_select" v-if="configForm.pageElementType == 'textarea'">
                    <el-form-item label="最大长度">
                      <el-input type="text" v-model="configForm.extParam.minlength"></el-input>
                    </el-form-item>
                    <el-form-item label="最小长度">
                      <el-input type="text" v-model="configForm.extParam.minlength"></el-input>
                    </el-form-item>
                    <el-form-item label="输入框行数">
                      <el-input type="text" v-model="configForm.extParam.rows"></el-input>
                    </el-form-item>
                  </div>
                  <!--此处为textarea相关-->

                  <!--日期相关-->
                  <div class="type_select" v-if="configForm.pageElementType == 'dates'">
                    <el-form-item label="日期格式化格式">
                      <el-input type="text" v-model="configForm.extParam.formatVal"></el-input>
                    </el-form-item>
                  </div>
                  <!--日期相关结束-->

                  <!--此处为checkbox相关-->
                  <div class="type_select" v-if="configForm.pageElementType == 'checkbox'">
                    <el-form-item label="字典编码(数据源是字典的时候必填)">
                      <el-input type="text" v-model="configForm.extParam.dictCode"></el-input>
                    </el-form-item>
                  </div>
                  <!--此处为checkbox相关-->

                  <!--此处为treeSelect相关-->
                  <div class="type_select" v-if="configForm.pageElementType == 'treeSelect'">
                    <el-form-item label="API接口地址">
                      <el-input type="text" v-model="configForm.extParam.api"></el-input>
                    </el-form-item>
                    <el-form-item label="请求类型(POST需要配合querys参数)">
                      <pagex-select v-model="configForm.extParam.methodType" :options="[{
                                                                                  value: 'GET',
                                                                                  label: 'GET'
                                                                                }, {
                                                                                  value: 'POST',
                                                                                  label: 'POST'
                                                                                }]">

                      </pagex-select>
                    </el-form-item>
                    <el-form-item label="label字段名(组件默认值为title)">
                      <el-input type="text" v-model="configForm.extParam.labelField"></el-input>
                    </el-form-item>
                    <el-form-item label="value字段名(组件默认值为id)">
                      <el-input type="text" v-model="configForm.extParam.valueField"></el-input>
                    </el-form-item>
                  </div>
                  <!--此处为treeSelect相关-->

                  <!--此处为uploadFileAsync相关-->
                  <div class="type_select" v-if="configForm.pageElementType == 'uploadFileAsync'">
                    <el-form-item label="最多可以上传几个">
                      <el-input type="text" v-model="configForm.extParam.limit"></el-input>
                    </el-form-item>
                    <el-form-item label="支持上传的格式(留空为不控制，比如word可以这么写:.doc,.docx)">
                      <el-input type="text" v-model="configForm.extParam.acceptFormat"></el-input>
                    </el-form-item>
                  </div>
                  <!--此处为uploadFileAsync相关-->

                  <el-form-item label="">
                    <el-button type="primary" @click="editSuccess">完成</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>

  </div>

</template>

<script>
import {deepClone} from "../utils";
import {postFile} from "../utils/download";
import PagexSelect from "@/lib/components/select";

const statusList = [
  {
    label: "是",
    value: 1
  },
  {
    label: "否",
    value: 0
  }
]
export default {
  name: "generate",
  components: {PagexSelect},
  data() {
    return {
      formCreate: false,

      baseForm: {
        dbName: "emergency",
        tableName: "t_resource_expert",
        author: undefined,
        fieldsVOS: []
      },
      switchStatus: [0, 1],

      currentRow: {},

      configForm: {},
      defaultForm: {
        pageElementType: undefined,
        extParam: {
          api: undefined,
          dictCode: undefined,
          rows:undefined,
          maxlength:undefined,
          minlength:undefined,
          labelField: undefined,
          valueField: undefined,
          methodType: undefined,
          formatVal: "yyyy-MM-dd",
          acceptFormat:undefined,
          limit:1
        }
      },
      pageElementParams:{
        textarea:{
          rows:true,
          maxlength:true,
          minlength:true,
        },
        select:{
          labelField: true,
          valueField: true,
          methodType: true,
          dictCode:true,
          api: true,
        },
        treeSelect:{
          labelField: true,
          valueField: true,
          methodType: true,
          api: true,
        },
        uploadFileAsync:{
          acceptFormat:true,
          limit:true
        },
        dates:{
          formatVal: true
        },
        checkbox:{
          dictCode: true,
        }
      },
      componentList: [
        {
          label: "输入框(el-input)",
          value: "text"
        },
        {
          label: "下拉框(自己封装的select.vue)",
          value: "select"
        },
        {
          label: "多行文本框(el-input)",
          value: "textarea"
        },
        {
          label: "日期el-date-picker",
          value: "dates"
        },
        {
          label: "富文本",
          value: "textareaEditor"
        }
        ,
        {
          label: "密码(el-input)",
          value: "password"
        }
        ,
        {
          label: "单选(radio.vue)",
          value: "radio"
        }
        ,
        {
          label: "多选(checkbox.vue)",
          value: "checkbox"
        }
        ,
        {
          label: "滑块(switch.vue)",
          value: "switch"
        }
        ,
        {
          label: "计数器滑块(inputNumber.vue)",
          value: "inputNumber"
        },
        {
          label: "插槽(自己写业务插件)",
          value: "slot"
        },
        {
          label: "下拉树(treeSelect.vue)",
          value: "treeSelect"
        },
        {
          label: "异步上传(uploadFileAsync.vue)",
          value: "uploadFileAsync"
        },
        {
          label: "同步上传，一般用于导入(uploadFile.vue)",
          value: "uploadFile"
        },
        {
          label: "超级扩展插槽(一般用于一对多)",
          value: "ext_slot"
        }
      ],
      isChecked: false,//是否有选中的
    }
  },
  computed: {},
  created() {
    this.configForm = deepClone(this.defaultForm)
    this.formCreate = true
  },
  filters: {
    getLabelByValue(val) {
      let _temp = ""
      for (let i = 0; i < statusList.length; i++) {
        if (statusList[i].value == val) {
          _temp = statusList[i].label
        }
      }
      return _temp
    }
  },
  methods: {
    //获取表信息
    getTableInfo() {
      const {dbName, tableName} = this.baseForm
      let params = {
        dbName: dbName,
        tableName: tableName
      }
      this.$pagexRequest({
        url: "/ms/table/getTableInfo",
        params
      }).then(res => {
        this.baseForm = res
      })
    },

    //生成代码
    createCode() {
      postFile("/ms/generateCode/generate", this.baseForm)
    },

    //禁止点击全选按钮
    selectAll(selection) {
      this.$refs.table.clearSelection()
    },

    //选择
    selectRow(selection, row) {
      let index = 0;
      this.baseForm.fieldsVOS.forEach((o, i) => {
        if (row.filedName === o.filedName) {
          index = i
        }
        this.$set(o, 'isOk', undefined)
      })
      this.$set(row, 'isOk', true)
      this.$set(this, 'currentRow', index)
      const newData = deepClone(row)
      if (this.isEmptyObj(newData.extParam)) {
        const configFormExtparam = deepClone(this.defaultForm.extParam)
        newData.extParam = configFormExtparam
      }
      this.configForm = newData
    },
    selectionChange(val) {
      if (val.length > 1) {
        this.$refs.table.clearSelection()
        this.$refs.table.toggleRowSelection(val.pop())
      }
      if (!val.length) {
        this.isChecked = false
      } else {
        this.isChecked = true
      }

    },
    //编辑完成
    editSuccess() {
      const _tempConfigForm = deepClone(this.configForm)
      if(this.pageElementParams[_tempConfigForm.pageElementType]){
        let params = this.pageElementParams[_tempConfigForm.pageElementType];
        let newExtParam = {};
        Object.keys(params).forEach((key)=>{
          if(_tempConfigForm.extParam[key]){
            newExtParam[key]=_tempConfigForm.extParam[key];
          }
        })
        _tempConfigForm.extParam = newExtParam;
      }else{
        _tempConfigForm.extParam = {};
      }
      this.$set(this.baseForm.fieldsVOS[this.currentRow], 'extParam', _tempConfigForm.extParam)
      this.$set(this.baseForm.fieldsVOS[this.currentRow], 'pageElementType', _tempConfigForm.pageElementType)
      this.$set(this.baseForm.fieldsVOS[this.currentRow], 'isOk', undefined)
      this.configForm = deepClone(this.defaultForm)
      /*this.$refs.table.clearSelection()*/
    },
    //编辑完成
    blurClick({row}) {
      this.$set(row, 'isOk', undefined)
    },
    isEmptyObj(obj) {
      return Object.keys(obj).length == 0
    }
  }
}
</script>

<style scoped>

/deep/ body {
  min-height: 100vh;
  padding: 0;
  margin: 0;
  display: flex !important;
  flex-direction: column !important;
}

.xhb-crud-top {
  background-color: #282828;
  height: 35px;
  padding: 0 20px;
  position: relative;
  cursor: default;
  line-height: 35px;
}

.xhb-crud-top span.soft-name {
  color: #FFFFFF;
  font-size: 16px;
  font-weight: 600;
  margin-left: 5px;
}


.xhb-crud-header {
  height: 60px;
  margin: 0 20px;
  position: relative;
  display: flex;
  align-items: center;
  cursor: default;
}

.xhb-crud--header-menu {
  position: absolute;
  right: 0;
}

.xhb-crud-container {
  min-height: 500px;
  overflow: hidden;
  cursor: default;
  position: relative;
  height: 100%;
  box-sizing: border-box;
  min-width: 0;
  flex-basis: auto;
  flex: 1;
  display: flex;
}

.xhb-crud-main {
  display: block;
  flex: 1;
  flex-basis: auto;
  overflow: auto;
  box-sizing: border-box;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 0;
}

.xhb-curd-wrap {
  display: flex;
  flex-direction: row;
  flex: 1;
  flex-basis: auto;
  box-sizing: border-box;
  min-width: 0;
  height: 100%;
}

.xhb-curd-wrap .left {
  border-top: 1px solid #ececec;
  box-sizing: border-box;
  width: 266px;
}

.xhb-curd-wrap .middle {
  border-top: 1px solid #ececec;
  box-sizing: border-box;
  flex: 1;
  display: flex;
  flex-direction: column;
}

/deep/ .middle .el-table {
  height: 100% !important;
  padding: 5px 15px !important;
  overflow-y: auto;
}

.xhb-curd-wrap .crud-table {
  padding: 20px !important;
  box-sizing: border-box;
  background: #f5f5f5;
  flex: 1;
  width: 600px;
}


.xhb-curd-wrap .right {
  border-top: 1px solid #ececec;
  box-sizing: border-box;
  width: 320px;
}

.xhb-crud-group {
  padding: 0 12px;
}

.group-form {
  margin: 18px 0 5px;
}

.group-form .el-form-item__label {
  float: none !important;
  font-weight: 600;
}

.group-form .el-select {
  width: 100%;
}

.xhb-crud-tabs {
  height: 40px;
}

.xhb-crud-tab {
  height: 40px;
  box-sizing: border-box;
  line-height: 40px;
  display: inline-block;
  list-style: none;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  position: relative;
  flex: 1;
  text-align: center;
  width: 100%;
  border-bottom: 1px solid #ececec;
}

.middle-tools {
  height: 40px;
  align-items: center;
  display: flex;
  justify-content: space-between;
  border: 1px solid #ececec;
  border-top: 0;
  flex: none;
}

.xhb-curd-content {
  height: calc(100% - 45px);
  display: flex;
}

.xhb-right-main {
  display: block;
  flex: 1;
  flex-basis: auto;
  overflow: auto;
  box-sizing: border-box;
  padding: 20px;
}

/deep/ .table-header {
  border-right: 1px solid #ececec;
}

/deep/ .form-header, .table-header {
  width: 50%;
  box-sizing: border-box;
  text-align: right;
  padding: 0 20px;
}

#generateApp {
  display: flex;
  flex-direction: column;
  flex: 1;
  height: 100%;
}



.container-base {
  width: 500px;
}

.container-list {
  width: 500px;
}
</style>

