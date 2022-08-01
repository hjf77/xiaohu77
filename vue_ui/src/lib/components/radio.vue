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
import {deepClone} from "../utils";
export default {
  name: "pagexRadio",
  model: {
    event: "change",
    prop: "valueData",
  },
  props: {
    options: {
      type: Array,
      default: () => undefined,
    },
    name: {
      type: String,
      default: "",
    },
    dictCode: {
      type: String,
      default: () => "",
    },
    querys: {
      type: Object,
      default: () => null,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    methodType: {
      type: String,
      default: 'GET',
    },
    url: {
      type: String,
      default: () => "",
    },
    labelField: {
      type: String,
      default: () => "title",
    },
    valueField: {
      type: String,
      default: () => "id",
    },
    param: {
      type: Object,
      default: () => {},
    },
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
    if (this.isHaveFatherOption) {
      let _options = deepClone(this.options);
      _options.forEach((item) => {
        item.valueField = item[this.valueField]
      })
      this.$emit("refreshOptions", {name: this.name, options: _options});
    }
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
      this.$emit("refreshOptions", {name: this.name, options: _options});
    },
    _change() {
      this.$emit("change", this.radioValue);
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
