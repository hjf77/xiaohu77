<template>
  <el-select
    ref="pagexSelectRef"
    v-bind="$attrs"
    v-on="$listeners"
    clearable
    :disabled="disabled"
    filterable
    :allow-create="isAllowCreate"
    :default-first-option="isDefaultFirstOption"
    @change="_change"
  >
    <template v-if="isHaveFatherOption">
      <el-option
        v-for="item in options"
        :key="item[valueField]"
        :label="item[labelField]"
        :value="item[valueField]"
      >
      </el-option>
    </template>
    <template v-else>
      <el-option
        v-for="item in ownerOption"
        :key="item.valueField"
        :label="item.labelField"
        :value="item.valueField"
      >
      </el-option>
    </template>
  </el-select>
</template>

<script>
import { handleStrParam } from "@/lib/utils/param";
export default {
  name: "pagexSelect",
  props: {
    options: {
      type: Array,
      default: () => undefined,
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
      type: String | Number,
      default: () => "id",
    },
    param: {
      type: Object,
      default: () => {},
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
    isValueNum:{
      type: Boolean,
      default: false,
    },
    isCustomLabel: {
      type: Boolean,
      default: false,
    },
    customLabelQuerys: {
      type: Object,
      default: () => {
        return {
          field: 'name',
          start: '(',
          end: ')'
        }
      },
    },
    isDefaultFirstOption: {
      type: Boolean,
      default: false,
    },
    isAllowCreate: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      ownerOption: [],
      isHaveFatherOption: false,
      newUrl: "",
      newLabelField: "",
      newValueField: "",
    };
  },
  async mounted() {
    this.isHaveFatherOption = typeof this.options != "undefined";
    this.newURL = this.url;
    this.newLabelField = this.labelField;
    this.newValueField = this.valueField;
    if (this.dictCode) {
      this.newURL =
        "/ms/dictItem/findList?dictGroupCode=" + this.dictCode;
      this.newValueField = "dictCode";
      this.newLabelField = "dictDesc";
    }
    if (this.newURL) {
      this.loadData();
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
