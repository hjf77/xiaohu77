<template>
  <div>
    <template v-if="isHaveFatherOption">
      <el-radio
        @change="_change"
        :disabled="disabled"
        v-model="radioValue"
        v-for="item in options"
        :key="item.value"
        :label="item.value"
        >{{ item.label }}</el-radio
      >
    </template>
    <template v-else>
      <el-radio
        :disabled="disabled"
        @change="_change"
        v-model="radioValue"
        v-for="item in ownerOption"
        :key="item.value"
        :label="item.valueField"
        >{{ item.labelField }}</el-radio
      >
    </template>
  </div>
</template>

<script>
import { handleStrParam } from "@/lib/utils/param";
export default {
  name: "pagexRadio",
  model: {
    event: "change",
    prop: "valueData",
  },
  props: {
    // 数组对象
    options: {
      type: Array,
      default: () => undefined,
    },
    // 字段名
    name: {
      type: String,
      default: "",
    },
    // 字典值
    dictCode: {
      type: String,
      default: () => "",
    },
    querys: {
      type: Object,
      default: () => null,
    },
    // 禁用
    disabled: {
      type: Boolean,
      default: false,
    },
    // 方法类型
    methodType: {
      type: String,
      default: 'GET',
    },
    // 路径
    url: {
      type: String,
      default: () => "",
    },
    // 数据名称
    labelField: {
      type: String,
      default: () => "title",
    },
    // 数据值
    valueField: {
      type: String,
      default: () => "id",
    },
    param: {
      type: Object,
      default: () => {},
    },
    // 字典汉子转化
    isValueNum:{
      type: Boolean,
      default: false,
    },
    valueData: String | Number | Boolean,
  },
  data() {
    return {
      ownerOption: [],
      isHaveFatherOption: false,
      radioValue: this.valueData,
      newUrl: "",
      newLabelField: "",
      newValueField: "",
    };
  },
  // computed: {
  //     newValue: {
  //         get: function () {
  //             return this.value;
  //         },
  //         set: function (value) {
  //             this.$emit("input", value);
  //         },
  //     },
  // },

  async mounted() {
    this.isHaveFatherOption = typeof this.options != "undefined";
    this.isHaveFatherOption = typeof this.options != "undefined";
    this.newURL = this.url;
    this.newLabelField = this.labelField;
    this.newValueField = this.valueField;
    if (this.dictCode) {
      this.newURL =
        "/basic/ms/dictItem/findList?dictGroupCode=" + this.dictCode;
      this.newValueField = "dictCode";
      this.newLabelField = "dictDesc";
    }
    if (this.newURL) {
      await this.loadData();
    }
    this._change();
  },
  methods: {
    async loadData() {
      let data = null;
      if (this.querys) {
        data = await this.$pagexRequest({
          url: this.newURL,
          data: this.querys,
          method: this.methodType,
        });
      } else {
        data = await this.$pagexRequest.get(
          handleStrParam(this.newURL, this.param)
        );
      }
      let _options = data || [];
      let _that = this;
      if (Array.isArray(_options)) {
        _options.forEach(function (_item) {
          if (_that.isCustomLabel) {
            _item.labelField = _item[_that.newLabelField] + _that.customLabelQuerys.start + _item[_that.customLabelQuerys.field] + _that.customLabelQuerys.end;
          } else {
            _item.labelField = _item[_that.newLabelField];
          }
          _item.valueField = _that.isValueNum ? parseInt(_item[_that.newValueField]) : _item[_that.newValueField];
        });
      } else {
        const arrayOptions = _options.value;
        arrayOptions.forEach(function (_item) {
          _item.labelField = _item[_that.newLabelField];
          _item.valueField = _that.isValueNum ? parseInt(_item[_that.newValueField]) : _item[_that.newValueField];
        });
        this.ownerOption = arrayOptions;
        return
      }

      if (this.isHaveFatherOption) {
        this.$emit("update:options", _options);
      }else{
        this.ownerOption = _options;
      }
    },
    _change() {
      this.$emit("change", this.radioValue);
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
