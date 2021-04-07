<template>
  <el-form ref="form" size="small" label-position="left" :label-width="'80px'" :model="model" driver="vee"
           :rules="rules">
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
  inject: ['runPageEvent', 'wlTest'],
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
    //查询 详情 id
    init: {
      type: Object,
    },
    // 接口附带参数
    param: {
      type: Object,
      default: () => {
      },
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
      //编辑 表单的详情数据
      initData: {}
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
    this.edit()//获取到 详情信息
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
            this.$pagexRequest({url: this.addApi, data: this.model, method: 'post'}).then((res) => {
              console.log(res);
              this.wlTest(root => {
                root.open = false
                root.getList()
              })
            }).catch(() => {})
          } else {}
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
    //获取到 详情信息
    edit() {
      this.initData = {
        'createTime': null,
        'createUser': null,
        'groupId': 1,
        'groupIdE': null,
        'groupName': "超级错误类型",
        'pkey': 1,
        'transMap': {},
        'updateTime': null,
        'updateUser': null,
        'wordbookGroupCode': "super_error_type"
      }
      this.controls.forEach((i)=>{
        this.model[i.name] = this.initData[i.name]
      })
      this.$forceUpdate();
      console.log(this.initData);
      console.log(this.model);
      /*if (this.isEdit) {
        this.$pagexRequest({method: 'get', url: this.initApi + this.init.groupId,}).then((res) => {
          this.initData = res.data;
        }).catch(() => {
          return false;
        });
      }*/
    },
  },
};
</script>

<style>
</style>
