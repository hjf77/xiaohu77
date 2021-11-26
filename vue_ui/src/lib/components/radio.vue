<template>
  <div>
    <template v-if="isHaveFatherOption">
      <el-radio
        @change="_change"
        v-model="radioValue"
        v-for="item in options"
        :key="item.value"
        :label="item.value"
        >{{ item.label }}</el-radio
      >
    </template>
    <template v-else>
      <el-radio
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
    valueData: String | Number | Boolean,
  },
  data() {
    return {
      ownerOption: [],
      isHaveFatherOption: false,
      radioValue: this.valueData,
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
    if (this.url) {
      this.loadData();
    }
    this._change();
  },
  methods: {
    async loadData() {
      const { data } = awaitget(
        handleStrParam(this.url, this.param)
      );

      let _options = data || [];
      let _that = this;
      _options.forEach(function (_item) {
        _item.labelField = _item[_that.labelField];
        _item.valueField = _item[_that.valueField];
      });
      if (this.isHaveFatherOption) {
        this.$emit("update:options", _options);
      } else {
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
