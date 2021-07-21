<template>
  <el-form
    ref="form"
    size="small"
    label-position="right"
    :label-width="'110px'"
    :model="model"
    :driver="!isVee ? 'vee' : ''"
    :rules="rules"
    v-bind="$attrs"
  >
    <el-form-item
      v-if="proxyIf(item.visibleOn, true)"
      v-for="item in controls"
      :label="item.label"
      :prop="item.name"
      :key="item.name"
    >
      <el-input
        :disabled="proxyIf(item.disabledOn, false)"
        v-model="model[item.name]"
        v-if="item.type === 'password'"
        v-bind="item"
      ></el-input>
      <el-input
        :disabled="proxyIf(item.disabledOn, false)"
        v-model="model[item.name]"
        v-if="item.type === 'text'"
        v-bind="item"
      ></el-input>
      <el-input
        :disabled="proxyIf(item.disabledOn, false)"
        v-model="model[item.name]"
        v-if="item.type === 'textarea'"
        maxlength="200"
        v-bind="item"
      ></el-input>

      <pagex-select
        v-bind="item"
        v-model="model[item.name]"
        v-if="item.type === 'select'"
      ></pagex-select>

      <pagex-radio
        v-bind="item"
        v-model="model[item.name]"
        v-if="item.type === 'radio'"
      ></pagex-radio>

      <pagex-checkbox
        v-bind="item"
        v-model="model[item.name]"
        v-if="item.type === 'checkbox'"
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

      <slot
        v-bind:data="model"
        v-if="item.type === 'slot'"
        :name="item.name"
      ></slot>

      <pagex-uploadFileAsync
        v-bind="item"
        v-model="model[item.name]"
        v-if="item.type === 'uploadFileAsync'"
        @success="upLoadSuccess"
      ></pagex-uploadFileAsync>

      <pagex-uploadFile
        v-bind="item"
        v-model="model[item.name]"
        v-if="item.type === 'uploadFile'"
      ></pagex-uploadFile>
      <pagex-formTreeSelect
        v-bind="item"
        v-model="model[item.name]"
        v-if="item.type === 'treeSelect'"
      ></pagex-formTreeSelect>
    </el-form-item>
    <el-form-item>
      <el-button size="small" type="primary" @click="submit">确定</el-button>
      <el-button size="small" @click="cancel">取消</el-button>
    </el-form-item>
  </el-form>

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
export default {
  inject: ["reloadable"],
  props: {
    isVee: {
      type: Boolean,
      default: () => {
        return false;
      },
    },
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
    //区别 复制
    isCopy: {
      type: Boolean,
      default: false,
    },
    //查询 详情 id
    init: {
      type: Object,
    },
    // 接口附带参数
    param: {
      type: Object,
      default: () => {},
    },
    uid: {
      type: [String, Number],
      default: "default",
    },
    controls: {
      type: Array,
      default: () => [],
    },
    success: {
      type: Function,
      default: () => {},
    },
    data: {
      type: Object,
      default: () => {},
    },
    addMethodType: {
      type: String,
      default: "POST",
    },
    updateMethodType: {
      type: String,
      default: "PUT",
    },
  },
  data() {
    return {
      model: {},
      rules: {},
      //编辑 表单的详情数据
      initData: {},
    };
  },
  created() {
    if (this.data) {
      this.model = JSON.parse(JSON.stringify(this.data));
    }
    let _that = this;
    //处理checkbox和开关
    this.controls.forEach(function (_item) {
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
    });

    this.edit(); //获取到 详情信息
  },
  methods: {
    proxyIf(_ifFun, _default) {
      return _ifFun ? _ifFun.call(this.model) : _default;
    },
    cancel() {
      this.$emit("open", false);
      this.reloadable.$parent.open = false;
      this.reloadable.open = false;
    },
    submit() {
      if (this.isFormDataSub) {
        if (this.isCopy) {
          this.copyFormData();
        } else {
          this.subFormData();
        }
      } else {
        this.debounce(this.subForm(), 3000);
      }
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
    subForm() {
      this.$refs.form.validate((valid, errors) => {
        if (valid) {
          this.$emit("open", false);
          // if (!this.isEdit) {
          let obj = {};
          if (this.other) {
            obj = Object.assign(this.other, this.model);
            console.log(obj);
          } else {
            obj = this.model;
          }
          this.$pagexRequest({
            url: this.isEdit ? this.updateApi : this.addApi,
            data: obj,
            method: this.isEdit ? this.updateMethodType : this.addMethodType,
          })
            .then((res) => {
              if (res.state) {
                this.msgSuccess(
                  res.value || (this.isEdit ? "修改成功" : "新增成功")
                );
                this.reload();
                this.$emit("open", false);
                this.reloadable.$parent.open = false;
              }
            })
            .catch((res) => {
              this.reloadable.$parent.open = true;
            });
        } else {
          return false;
        }
      });
    },

    //复制
    copyFormData() {
      console.log("copyData");
      this.$refs.form.validate(async (valid, errors) => {
        if (valid) {
          let fd = new FormData();
          for (let i in this.model) {
            if (this.model[i] != undefined) {
              fd.append(i, this.model[i]); //传文件
            }
          }

          try {
            const res = await this.$pagexRequest({
              url: this.isCopy ? this.copyApi : "",
              data: fd,
              method: this.isCopy ? "POST" : "PUT",
              headers: {
                "Content-Type": "multipart/form-data",
              },
            });
            if (res.state) {
              this.msgSuccess(res.value || "复制成功");
              this.reload();
              this.$emit("open", false);
              this.reloadable.$parent.open = false;
            }
          } catch (error) {
            this.msgError(res.message || "复制失败");
            this.reloadable.$parent.open = true;
          }
          return false;
        }
      });
    },

    //formdata提交表单
    subFormData() {
      this.$refs.form.validate((valid, errors) => {
        if (valid) {
          let fd = new FormData();
          for (let i in this.model) {
            if (this.model[i] != undefined) {
              fd.append(i, this.model[i]); //传文件
            }
          }
          this.$pagexRequest({
            url: this.isEdit ? this.updateApi : this.addApi,
            data: fd,
            method: this.isEdit ? "PUT" : "POST",
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }).then((res) => {
            if (res.state) {
              this.msgSuccess(
                res.value || (this.isEdit ? "修改成功" : "新增成功")
              );
              this.$emit("open", false);
              this.reloadable.$parent.open = false;
              this.reloadable.open = false;
              this.reload();
            } else {
              this.msgError(
                res.message && this.isEdit
                  ? "修改失败," + res.message
                  : "新增失败," + res.message
              );
            }
          });
        } else {
          return false;
        }
      });
    },
    //获取到 详情信息
    edit() {
      if (this.isEdit || this.isCopy) {
        this.controls.forEach((i) => {
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
  },
};
</script>

<style>
</style>
