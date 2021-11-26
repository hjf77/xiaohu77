<template>
  <div class="pagex-from-theme">
    <el-form
        ref="form"
        size="small"
        label-position="right"
        :label-width="'120px'"
        :model="value"
        :inline="true"
        :driver="!formOption.isVee ? 'vee' : ''"
        :rules="rules"
    >
      <el-form-item
          v-for="item in formColumn"
          :label="item.label"
          :prop="item.name"
          :key="item.name"
      >
        <el-input
            v-if="item.type === 'text'"
            :disabled="proxyIf(item.disabledOn, false)"
            v-model="form[item.name]"
            show-word-limit
            v-bind="item"
            style="width:305px"
        ></el-input>

        <el-input
            :disabled="proxyIf(item.disabledOn, false)"
            v-model="form[item.name]"
            v-if="item.type === 'prefixText'"
            show-word-limit
            v-bind="item"
            style="width: 305px"
        >
          <template slot="prepend">{{ item.prefixTextContent }}</template>
        </el-input>

        <pagex-checkbox
            v-bind="item"
            v-model="form[item.name]"
            v-if="item.type === 'checkbox'"
            style="width: 740px; line-height: 40px"
        ></pagex-checkbox>

        <el-date-picker
            v-model="form[item.name]"
            type="date"
            placeholder="请选择"
            :value-format="item.formatVal"
            v-if="item.type === 'dates'"
        ></el-date-picker>

        <el-date-picker
            v-model="form[item.name]"
            type="datetime"
            placeholder="选择日期时间"
            v-if="item.type === 'dateTimePicker'"
        >
        </el-date-picker>

        <pagex-select
            v-bind="item"
            v-model="form[item.name]"
            v-if="item.type === 'select'"
            :url="item.url"
            :methodType="item.methodType"
            :querys="item.querys"
            :labelField="item.labelField"
            :valueField="item.valueField"
            style="width: 740px"
        ></pagex-select>

        <pagex-formTreeSelect
          v-bind="item"
          v-model="form[item.name]"
          v-if="item.type === 'treeSelect'"
          :api="item.api"
          :httpMehod="item.httpMehod"
        ></pagex-formTreeSelect>

        <pagex-uploadFile
          v-bind="item"
          v-model="form[item.name]"
          v-if="item.type === 'uploadFile'"
        ></pagex-uploadFile>

        <pagex-uploadFileAsync
            v-bind="item"
            v-model="form[item.name]"
            v-if="item.type === 'uploadFileAsync'"
        ></pagex-uploadFileAsync>

        <!--富文本编辑器-->
        <Editor
            v-model="form[item.name]"
            v-if="item.type === 'textareaEditor'"
        ></Editor>
        <!--富文本编辑器-->
        <!--textArea组件-->
        <el-input
            :disabled="proxyIf(item.disabledOn, false)"
            v-model="form[item.name]"
            v-if="item.type === 'textarea'"
            maxlength="200"
            v-bind="item"
            show-word-limit
            rows="3"
        ></el-input>
        <!--textArea组件-->
      </el-form-item>
    </el-form>
    <slot name="form-slot"></slot>
    <div class="form-btn">
      <el-button class="save-btn form-btn-item" @click="submit()">保存</el-button>
      <el-button class="save-btn form-btn-item" v-for="(itemBtn,index) in buttons" :key="index"
                 @click="btnClick(itemBtn)">{{ itemBtn.name }}
      </el-button>
      <el-button @click="cancel" class="form-btn-item">取消</el-button>
    </div>
  </div>
</template>

<script>
import mixins from "./mixins";
import utils from "./utils";
export default {
  name: "BaseForm",
  mixins: [mixins, utils],
  model: {
    prop: "value",
    event: "input"
  },
  data() {
    return {
      formCreate:false,
      rules: {},
      form:{},
      formDefault: {},
      formVal: {}
    }
  },
  props: {
    // 新增 接口
    addApi: {
      type: String,
      default: "",
    },
    // 更新 接口
    updateApi: {
      type: String,
      default: "",
    },
    dialogType: {
      type: String,
      default: null
    },
    formOption: {
      type: Object,
      default: null
    },
    //初始化数据
    value: {
      type: Object,
      default: () => {
        return {};
      }
    },

    //提交格式是否为formData
    isFormDataSub: {
      type: Boolean,
      default: false
    }
  },
  computed: {

    //表单项
    formColumn() {
      return this.formOption.group || []
    },

    //表单底部的按钮
    buttons() {
      return this.formOption.buttons || []
    },

  },
  value: {
    handler (val) {
      debugger
      if (this.formCreate) {
        this.setForm(val);
      } else {
        this.formVal = Object.assign(this.formVal, val || {});
      }
    },
    deep: true,
    immediate: true
  },
  created() {
    this.$nextTick(()=>{
      this.init()
      this.dataFormat()
      this.setForm(this.value)
      this.setVal();
      this.clearValidate();
      this.formCreate = true
    })
  },
  methods: {
    proxyIf(_ifFun, _default) {
      if (_ifFun == 'disabled') {
        return true
      }
      return _ifFun ? _ifFun.call(this.form) : _default;
    },

    //提交
    submit() {
      if (this.isFormDataSub) {
        this.debounce(this.subFormData)
      } else {
        this.debounce(this.subForm)
      }
    },

    //取消
    cancel() {
      this.$emit("cancel")
    },

    // 普通提交表单
    subForm() {
      this.$refs.form.validate((valid, errors) => {
        if (valid) {
          this.$pagexRequest({
            url: this.dialogType == 'add' ? this.addApi : this.updateApi,
            data: this.value,
            method: this.dialogType == 'add' ? 'POST' : 'PUT',
          })
              .then((res) => {
                if (res.code === 200) {
                  this.msgSuccess(
                      res.message || (this.dialogType == 'add' ? "新增成功" : "修改成功")
                  );
                  this.$emit('sucess')
                }
              })
              .catch((res) => {
              });
        } else {
          return false;
        }
      });
    },

    //formData提交表单
    subFormData() {
      this.$refs.form.validate((valid, errors) => {
        if (valid) {
          let fd = new FormData();
          for (let i in this.value) {
            if (this.value[i] != undefined) {
              fd.append(i, this.value[i]); //传文件
            }
          }
          this.$pagexRequest({
            url: this.dialogType == 'add' ? this.addApi : this.updateApi,
            method: this.dialogType == 'add' ? 'POST' : 'PUT',
            data: fd,
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }).then((res) => {
            if (res.code === 200) {
              this.msgSuccess(
                res.message || (this.dialogType == 'add' ? "新增成功" : "修改成功")
              );
              this.$emit('sucess')
            } else {
            }
          });
        } else {
          return false;
        }
      });
    },


    //初始化
    init() {
      this.initRules()
    },

    //初始化校验
    initRules() {
      this.formColumn.forEach(item => {
        if (item.rule) {
          this.rules[item.name] = item.rule;
        }
      })
    },

    setForm(value){
      Object.keys(value).forEach(ele => {
        let result = value[ele];
        // let column = this.formColumn.find(column => column.prop == ele)
        this.$set(this.form, ele, result);
      });
    },

    //初始化表单的值
    formInitValue(group = []) {
        let tableForm = {}
        group.forEach(item => {
          if (item.type === 'text') {
            tableForm[item.name] = undefined
          }else{
            tableForm[item.name] = undefined
          }
          // if (item.dataType === 'array') {
          //   tableForm[item.name] = []
          // }
          // if (item.dataType === 'number') {
          //   tableForm[item.name] = undefined
          // }
        })
        return {tableForm}
    },

    //初始化表单
    dataFormat () {
      this.formDefault = this.formInitValue(this.formColumn);
      let value =  this.deepClone(this.formDefault.tableForm);
      this.setForm(this.deepClone(Object.assign(value, this.formVal)))
    },

    //设置值
    setVal () {
      this.$emit("input", this.form);
      this.$emit("change", this.form);
    },
    //清除校验
    clearValidate (list) {
      this.$nextTick(() => {
        this.$refs.form.clearValidate(list);
      })
    },

    // 自定义按钮
    btnClick(itemBtn) {
      if(itemBtn.click && typeof itemBtn.click === 'function'){
        itemBtn.click.call(this, this.value, itemBtn);
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.form-btn {
  text-align: right;
  margin-right: 20px;
  margin-bottom:17px;
}

::v-deep .save-btn {
  color: #fff;
  border: #0F9B9C;
  background: #0F9B9C;
}

.form-btn .form-btn-item {
  margin-left: 20px;
  height: 36px;
  width: 80px;
}

::v-deep .el-form-item--small .el-form-item__content .el-date-editor {
  width: 305px;

  .el-input__prefix {
    left: 281px;
  }

  .el-input__suffix {
    display: none;
  }
}

::v-deep .el-form-item--small .el-form-item__content .el-date-editor .el-input__prefix .el-input__inner {
  padding-left: 18px;
}

::v-deep .el-form-item .el-form-item__content .el-input--small .el-input__icon {
  font-size: 20px;
}

::v-deep .el-input--small .el-input__inner {
  height: 40px;
  width: 100%;
}

::v-deep .el-textarea {
  width: 740px;
}

::v-deep .vue-treeselect {
  width: 305px;
}

::v-deep .el-form-item__label {
  line-height: 40px;
}

</style>
