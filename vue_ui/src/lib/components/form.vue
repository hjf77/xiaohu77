<template>
  <el-scrollbar class="scrollbar" :style="styleName">
    <div class="pagex-from-theme">
      <el-form
        ref="form"
        size="small"
        label-position="right"
        :label-width="labelWidth"
        :model="model"
        :driver="isVee ? 'vee' : ''"
        :rules="rules"
        v-bind="$attrs"
        :inline="true"
      >
        <el-form-item
          :disabled="proxyIf(item.visibleOn, true)"
          v-for="item in realControls"
          v-if="item.type !='ext_slot'&&!item.isHide"
          :label="item.label ? item.label + ':' : ''"
          :prop="item.name"
          :key="item.name"
          :class="{'labelLeft': item.isAndClass}"
        >
          <el-input
            :disabled="proxyIf(item.disabledOn, false)"
            v-model="model[item.name]"
            v-if="item.type === 'password'"
            v-bind="item"
            :clearable = "item.clearable?item.clearable:true"
            :style="{ width: item.width ? item.width + 'px' : '305px' }"
          ></el-input>
          <el-input
            :disabled="proxyIf(item.disabledOn, false)"
            v-model="model[item.name]"
            v-if="item.type === 'text'"
            :clearable = "item.clearable?item.clearable:true"
            show-word-limit
            v-bind="item"
            :style="{ width: item.width ? item.width + 'px' : '305px' }"
          ></el-input>
          <el-input
            :disabled="proxyIf(item.disabledOn, false)"
            v-model="model[item.name]"
            v-if="item.type === 'prefixText'"
            :clearable = "item.clearable?item.clearable:true"
            show-word-limit
            v-bind="item"
            style="width: 305px"
          >
            <template slot="prepend">{{ item.prefixTextContent }}</template>
          </el-input>
          <el-input
            :disabled="proxyIf(item.disabledOn, false)"
            v-model="model[item.name]"
            v-if="item.type === 'textarea'"
            :clearable = "item.clearable?item.clearable:true"
            maxlength="200"
            v-bind="item"
            show-word-limit
            rows="3"
            :style="{ 'width': item.width ? item.width + 'px' : '740px' }"
          ></el-input>
          <Editor
            v-model="model[item.name]"
            v-if="item.type === 'textareaEditor'"
          />
          <el-date-picker
            v-model="model[item.name]"
            :type="item.formatType || 'date'"
            :disabled="proxyIf(item.disabledOn, false)"
            :placeholder="item.placeholder"
            :value-format="item.formatVal"
            v-if="item.type === 'dates'"
            :picker-options="item.pickerOptions"
            @change = "(value) => changeFn(value,item.changeFn)"
            :class="{'surplus' : item.name === 'effectiveDate'}"
            :style="{ width: item.width ? item.width + 'px' : '305px' }"
          ></el-date-picker>
          <pagex-select
            :disabled="proxyIf(item.disabledOn, false)"
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'select'"
            :url="item.url"
            :methodType="item.methodType"
            :querys="item.querys"
            :labelField="item.labelField"
            :valueField="item.valueField"
            :isValueNum="item.isValueNum"
            :style="{ width: item.width ? item.width + 'px' : (item.multiple ? '740px' : '305px') }"
            @change="item.change?item.change.call(this,item, model[item.name]):''"
          ></pagex-select>

          <pagex-radio
            v-bind="item"
            v-model="model[item.name]"
            :valueData="model[item.name]"
            v-if="item.type === 'radio'"
            @change="item.click.call(this,item, model[item.name])"
          ></pagex-radio>

          <pagex-checkbox
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'checkbox'"
            style="width: 740px; line-height: 40px"
          ></pagex-checkbox>
          <pagex-switchs
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'switch'"
          ></pagex-switchs>

          <pagex-inputNumber
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'inputNumber'"
          ></pagex-inputNumber>

          <pagex-slider
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'slider'"
          ></pagex-slider>

          <pagex-pagination
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'pagination'"
          ></pagex-pagination>

          <pagex-cascader
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'cascader'"
            :url="item.api"
            :methodType="item.methodType"
            :querys="item.querys"
            :labelField="item.labelField"
            :valueField="item.valueField"
            :isValueNum="item.isValueNum"
            :propsField = "item.propsField"
            :style="{ 'width': item.width ? item.width + 'px' : '305px' }"
          ></pagex-cascader>

          <pagex-transfer
            v-if="item.type === 'transfer'"
            :url="item.api"
            :querys="item.querys"
            v-model="model[item.name]"
            :propsField = "item.propsField"
            @change="item.change ? item.change.call(this,item, model[item.name]):''"
          ></pagex-transfer>

          <slot
            v-bind:data="model[item.name]"
            v-if="item.type === 'slot'"
            :name="item.name"
          ></slot>

          <pagex-uploadFileAsync
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'uploadFileAsync'"
          ></pagex-uploadFileAsync>
          <!-- @success="upLoadSuccess" -->

          <pagex-uploadFile
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'uploadFile'"
          ></pagex-uploadFile>
          <pagex-uploadPicture
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'uploadPicture'"
          >
          </pagex-uploadPicture>

          <pagex-formTreeSelect
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'treeSelect'"
            :api="item.api"
            :selectOn = "item.selectOn"
            :isDisabled = "item.isDisabled"
            :style="{ width: item.width ? item.width + 'px' : (item.multiple ? '740px' : '305px') }"
          ></pagex-formTreeSelect>

          <pagex-treeSelect
            v-bind="item"
            v-model="model[item.name]"
            v-if="item.type === 'treeSelect2'"
            :api="item.api"
          ></pagex-treeSelect>

          <el-date-picker
            v-model="model[item.name]"
            type="datetime"
            placeholder="选择日期时间"
            v-if="item.type === 'dateTimePicker'"
          >
          </el-date-picker>
        </el-form-item>
        <template v-for="item in realControls">
          <slot
            v-bind:data="model[item.name]"
            v-if="item.type === 'ext_slot'"
            :name="item.name"
          ></slot>
        </template>
      </el-form>
      <slot></slot>
      <div class="form-btn">
        <el-button
          class="save-btn form-btn-item"
          v-if="isHaveAddBtn"
          :disabled="saveButtonDisable"
          @click="submit()"
          >保存</el-button
        >
        <el-button
          class="save-btn form-btn-item"
          v-for="(itemBtn, index) in buttons"
          :key="index"
          @click="btnClick(itemBtn)"
          >{{ itemBtn.name }}</el-button
        >
        <el-button @click="cancel" class="form-btn-item">取消</el-button>
      </div>
    </div>
  </el-scrollbar>

  <!--
      支持下载模板   downloadUrl:
      支持上传文件--异步
      asyncUploadUrl --
      支持同步传 --
      图片上传 -- 单图，多图(删除)  -- 点击每个图片要查看  配置数量，单图最大size
      多文件上传，要能删除和下载
  -->
</template>

<script>
import Editor from "@/components/Editor/index.vue";
import PagexSelect from "./select.vue";
export default {
  inject: ["reloadable"],
  components: {
    PagexSelect,
    Editor,
  },
  props: {
   labelWidth:{
     type:String,
     default:"120px",
    },
    isVee: {
      type: Boolean,
      default: () => {
        return true;
      },
    },
    isHaveAddBtn: {
      //是否有保存按钮
      type: Boolean,
      default: true,
    },
    //是否使用formdata提交
    isFormDataSub: {
      type: Boolean,
      default: false,
    },
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
    // 复制 接口
    copyApi: {
      type: String,
      default: "",
    },
    other: {
      type: Object,
    },
    // 详情 接口
    initApi: {
      type: String,
      default: "",
    },
    //区别 详情 编辑 和 新增
    isEdit: {
      type: Boolean,
      default: false,
    },
    //如果是编辑的话，通过此属性传入后台请求回来的数据
    init: {
      type: Object,
    },
    // 接口附带参数
    param: {
      type: Object,
      default: () => {},
    },
    // 表单字段和插件
    controls: {
      type: Array,
      default: () => [],
    },
    //自定义按钮，比如审批等等
    buttons: {
      type: Array,
      default: () => [],
    },
    //新增的时候设置默认值用的
    data: {
      type: Object,
      default: () => {},
    },
    //新增接口的methodType
    addMethodType: {
      type: String,
      default: "POST",
    },
    //修改接口的methodType
    updateMethodType: {
      type: String,
      default: "PUT",
    },
    //表单高度，超过高度有滚动条
    dialogHeight: {
      type: String,
      default: "auto",
    },
    //触发一些事件，用来关闭dialog，刷新列表等
    eventBusNames: {
      type: Array,
      default: () => [],
    },
    //是否关闭父级弹窗
    isColseParent: {
      type: Boolean,
      default: true,
    },
    //提交之前去执行 一些自定义业务逻辑，返回true代表可以给后台提交返回false 不可以给后台提交
    onSubmit: {
        type: Function,
        default: null,
    },
    //用来关闭dialog，刷新列表
    namespace: {
      type: String,
      default: "",
    },
  },
  computed: {
    styleName() {
      return {
        height: this.dialogHeight,
      };
    }
  },
  data() {
    return {
      formCreate: false, //是否已经执行完created方法
      model: {},
      rules: {},
      //编辑 表单的详情数据
      initData: {},
      realControls: [],
      saveButtonDisable:false,
      // newDate: new Date(),
    };
  },

  watch: {
    controls: {
      handler: function (val) {
        //当执行完created方法完毕后，开始监听
        if (this.formCreate) {
          this.$set(this, "realControls", val);
          const _that = this
          this.$nextTick(() => {
            this.realControls.forEach(function (_item) {
              if (_item.rule) {
                _that.rules[_item.name] = _item.rule;
              }
              if (_item.type == "checkbox") {
                _that.model[_item.name] = _that.model[_item.name]
                  ? _that.model[_item.name]
                  : [];
              }
              if (_item.type == "switch") {
                _that.model[_item.name] = _that.model[_item.name]
                  ? _that.model[_item.name]
                  : [];
              }
              if (_item.type == "treeSelect" && _item.multiple == true) {
                _that.model[_item.name] = _that.model[_item.name]
                  ? _that.model[_item.name]
                  : [];
              }
              if (_item.type == "dates") {
                _that.$set(_that.model, _item.name, _that.model[_item.name]
                  ? _that.model[_item.name]
                  :'');
              }
            });
          });
        }
      },
      deep: true,
    },
    data: function(){
      if(this.model.companyId){
        this.model.companyId = this.data.companyId;
      }
      if(this.model.companyIdName){
        this.model.companyIdName = this.data.companyIdName;
      }
    }
  },

  created() {
    if (this.data) {
      this.$set(this, "model", JSON.parse(JSON.stringify(this.data)));
      console.log(this.model);
    }
    this.controls.forEach((_item) => {
      if (this.proxyIf(_item.ifFun, true)) {
        if(!_item.placeholder){
          _item.placeholder =  (_item.type=='text' || _item.type=='textarea') ? '请输入' : '请选择';
          _item.placeholder =  _item.placeholder + _item.label;
        }
        this.realControls.push(_item);
      }
    });
    let _that = this;
    //处理checkbox和开关
    this.realControls.forEach(function (_item) {
      if (_item.rule) {
        _that.rules[_item.name] = _item.rule;
      }
      if (_item.type == "checkbox") {
        _that.model[_item.name] = _that.model[_item.name]
          ? _that.model[_item.name]
          : [];
      }
      if (_item.type == "switch") {
        _that.model[_item.name] = _that.model[_item.name]
          ? _that.model[_item.name]
          : [];
      }
      if (_item.type == "treeSelect" && _item.multiple == true) {
        _that.model[_item.name] = _that.model[_item.name]
          ? _that.model[_item.name]
          : [];
      }
      if (_item.type == "dates") {
        if (_item.type == "dates") {
         _that.$set(_that.model, _item.name, _that.model[_item.name]
          ? _that.model[_item.name]
          :'');
      }
      }
    });
    this.edit(); //获取到 详情信息
    this.formCreate = true;
  },
  methods: {
    proxyIf(_ifFun, _default) {
      if (_ifFun == "disabled") {
        return true;
      }
      return _ifFun ? _ifFun.call(this.model) : _default;
    },
    cancel() {
      if(this.isColseParent){
        if (this.namespace) {
          this.$EventBus.$emit(this.namespace + '_closeDialog');
        }else {
          this.$emit("open", false);
          this.reloadable.$parent.$parent.open = false;
          this.reloadable.open = false;
          // 自定义弹窗
          this.reloadable.dialogVisible = false;
          this.reloadable.$parent.$parent.dialogVisible = false;
          this.reloadable.$parent.$parent.$parent.dialogVisible = false;
          this.reloadable.$parent.dialogVisible = false;
          this.reloadable.$parent.open = false;
          this.reloadable.$parent.$parent.open = false;
          this.reloadable.$parent.$parent.$parent.open = false;
        }
      }else{
        this.$emit("open", false);
      }

    },
    btnClick(itemBtn) {
      this.$refs.form.validate((valid, errors) => {
        if (valid) {
          itemBtn.click.call(this, this.model);
        } else {
          return false;
        }
      });
    },
    submit(_extParam) {
      if (this.isFormDataSub) {
          this.subFormData(_extParam);
      } else {
          this.subForm()
      }
    },
    changeFn(val,itemBtn){
      itemBtn && itemBtn(val);
    },
    // 防抖
    debounce(fn, wait) {
      var timeout = null;
      return function () {
        if (timeout !== null) clearTimeout(timeout);
        timeout = setTimeout(fn, wait);
      };
    },
    // 普通提交表单
    subForm(_extParam) {
      this.$refs.form.validate((valid, errors) => {
        if (valid) {
          let obj = {};
          if (this.other) {
            obj = Object.assign(this.other, this.model);
          }else if(_extParam){
            obj = Object.assign(_extParam, this.model);
          } else {
            obj = this.model;
          }
          obj = JSON.parse(JSON.stringify(obj))
          if(this.onSubmit && !this.onSubmit(obj)){
              return;
          }this.saveButtonDisable = true;
          this.$pagexRequest({
            url: this.isEdit ? this.updateApi : this.addApi,
            data: obj,
            method: this.isEdit ? this.updateMethodType : this.addMethodType,
          })
            .then((res) => {
              if (res.code === 200) {
                this.msgSuccess(
                  res.message || (this.isEdit ? "修改成功" : "新增成功")
                );
                // 通过 $EventBus 刷新指定页面的列表 和关闭指定弹框
                if (this.namespace) {
                  this.$EventBus.$emit(this.namespace + '_reload',this.model)
                  this.$EventBus.$emit(this.namespace + '_closeDialog');
                }else{
                  this.reload();
                  this.$emit("open", false);
                  this.reloadable.open = false;
                  this.reloadable.$parent.open = false;
                  this.reloadable.$parent.$parent.open = false;
                  // 自定义弹窗
                  this.reloadable.dialogVisible = false;
                  this.$parent.reloadable.dialogVisible = false;
                  this.reloadable.$parent.dialogVisible = false;
                  this.reloadable.$parent.$parent.dialogVisible = false;
                }
              }
            })
            .catch((res) => {
              this.saveButtonDisable = false;
              this.reloadable.$parent.$parent.open = true;
            });
        } else {
          return false;
        }
      });
    },


    //formdata提交表单
    subFormData(_extParam) {
      this.$refs.form.validate((valid, errors) => {
        if (valid) {
          let fd = new FormData();
          for (let i in this.model) {
            if (this.model[i] != undefined) {
              fd.append(i, this.model[i]); //传文件
            }
          }
          if(_extParam) {
            for (let i in _extParam) {
              if (_extParam[i] != undefined) {
                fd.append(i, _extParam[i]); //传文件
              }
            }
          }
          this.saveButtonDisable = true;
          this.$pagexRequest({
            url: this.isEdit ? this.updateApi : this.addApi,
            data: fd,
            method: this.isEdit ? "PUT" : "POST",
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }).then((res) => {
            this.saveButtonDisable = false;
            if (res.result) {
              this.msgSuccess(
                res.data || (this.isEdit ? "修改成功" : "新增成功")
              );
              this.cancel()
              this.reload();
              // 通过 $EventBus 刷新指定页面的列表
              if (this.eventBusNames.length > 0) {
                this.eventBusNames.forEach((item) => {
                  this.$EventBus.$emit(item,this.model)
                })
              }
            } else {
              this.saveButtonDisable = false;
              this.msgError(
                res.message && this.isEdit
                  ? "修改失败," + res.message
                  : "新增失败," + res.message
              );
            }
          }).catch(()=>{
            this.saveButtonDisable = false;
          });
        } else {
          return false;
        }
      });
    },
    //获取到 详情信息
    edit() {
      if (this.isEdit) {
        this.realControls.forEach((i) => {
          this.$set(this.model, i.name, this.init[i.name]);
        });
        if (this.initApi) {
          this.$pagexRequest.get(this.initApi).then((resp) => {
            this.model = resp;
          });
        }
      }
    },
    reload() {
      this.reloadable.reload();
    },
    // 手动触发校验
    checkForm(propertyName) {
      this.$refs['form'].validateField(propertyName)
    }
  },
};
</script>

<style lang="scss" scoped>
.form-btn {
  text-align: right;
  margin-right: 20px;
  margin-bottom: 17px;
}
::v-deep .save-btn {
  color: #fff;
  border: #0f9b9c;
  background: #0f9b9c;
}
.form-btn .form-btn-item {
  margin-left: 20px;
  height: 36px;
  width: 80px;
}

::v-deep .el-form-item--small .el-form-item__content .el-date-editor {
  width: 305px;
  .el-input__prefix {
    left: 275px;
    .el-input__icon{
      line-height: 40px;
    }
  }
  .el-input__suffix {
    display: none;
  }
}
::v-deep .el-form-item--small .el-form-item__content .surplus {

  .el-input__prefix {
    left: 170px;
  }
}
::v-deep
  .el-form-item--small
  .el-form-item__content
  .el-date-editor
  .el-input__prefix
  .el-input__inner {
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
::v-deep .el-textarea__inner {
  resize: none;
}
::v-deep .el-form-item__label {
  line-height: 40px;
}
::v-deep .scrollbar {
  overflow: hidden;
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
  overflow-y: scroll;
}
::v-deep .labelLeft .el-form-item__label{
  padding-left: 10px;
  text-align: left;
  line-height: 20px;
  padding-top: 10px;

}
</style>
