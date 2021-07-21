<template>
  <el-select
    ref="pagexSelectRef"
    v-bind="$attrs"
    v-on="$listeners"
    clearable
    :disabled="disabled"
  >
    <template v-if="isHaveFatherOption">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
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
        "/portal/sys/dataDict/v1/getByTypeKey?typeKey=" + this.dictCode;
      this.newValueField = "key";
      this.newLabelField = "text";
    }
    if (this.newURL) {
      this.loadData();
    }
  },
  methods: {
    async loadData() {
      let data = null;
      if (this.querys) {
        data = await this.$pagexRequest({
          url: this.newURL,
          data: this.querys,
          method: "POST",
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
          _item.labelField = _item[_that.newLabelField];
          _item.valueField = _item[_that.newValueField];
        });
      } else { 
        const ArrayOptions = _options.value;
        ArrayOptions.forEach(function (_item) {
          _item.labelField = _item[_that.newLabelField];
          _item.valueField = _item[_that.newValueField];
        });
        this.ownerOption = ArrayOptions;
        return
      }

      if (this.isHaveFatherOption) {
        this.$emit("update:options", _options);
      }else{
        this.ownerOption = _options;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
