<template>
  <el-form ref="form" size="small" label-position="left" :label-width="'80px'" :model="model" driver="vee" :rules="rules">
    <el-form-item v-if="proxyIf(item.visibleOn,true)"
                  v-for="item in controls"
                  :label="item.label"
                  :prop="item.name"
                  :key="item.name"
    >
      <el-input
        :disabled="proxyIf(item.disabledOn,false)"
        v-model="model[item.name]"
        v-if="item.type === 'text'"
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

    </el-form-item>
    <el-form-item>
      <el-button size="small" @click="submit">确定</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  inject: ['runPageEvent','wlTest'],
  props: {
    // 新增 接口
    addApi:{
      type: String,
      default: "",
    },
    // 更新 接口
    updateApi: {
      type: String,
      default: "",
    },
    // 详情 接口
    initApi: {
      type: String,
      default: "",
    },
    isEdit: {
      type: Boolean,
      default: false,
    },

    // 接口附带参数
    param: {
      type: Object,
      default:() => {},
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
      default: () => {
      },
    },
    data: {
      type: Object,
      default: () => {
      },
    },
  },

  data() {
    return {
      model: {},
      rules: {},
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
        _that.model[_item.name] ? _that.model[_item.name] : [];
      }
      if (_item.type == "switch") {
        _that.model[_item.name] ? _that.model[_item.name] : [];
      }
    });
    console.log(this.rules);
  },
  methods: {
    proxyIf(_ifFun, _default) {
      return (_ifFun ? _ifFun.call(this.model) : _default);
    },
    submit() {
      console.log(this.model);
      this.$refs.form.validate((valid, errors) => {
        if (valid) {
          if (!this.isEdit) {
            this.$pagexRequest({url:this.addApi,data:this.model,method:'post'}).then((res)=>{
              console.log(res);
              this.wlTest(root=>{
                root.open = false
                root.getList()
              })
            }).catch(()=>{

            })
          }

        } else {

          return false;
        }
      });

      // if (this.api) {

      //   const { code, msg } = await this.$axios.get(this.api,this.query);
      //   if (code === 0) {
      //     this.$message.success("保存成功");
      //   } else {
      //     this.$message.error(msg || "失败");
      //   }
      //   console.log(this)
      //   this.$emit('success')
      // }
    },
  },
};
</script>

<style>
</style>
