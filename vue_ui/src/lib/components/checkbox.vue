<template>
  <div>
    <template v-if="isHaveFatherOption">
      <el-checkbox
        v-model="newValue"
        v-for="item in options"
        :key="item.value"
        :label="item.value"
      >{{ item.label }}
      </el-checkbox>
    </template>

    <template v-else>
      <el-checkbox
        v-model="newValue"
        v-for="item in ownerOption"
        :key="item.valueField"
        :label="item.valueField"
      >{{ item.labelField }}
      </el-checkbox>
    </template>
  </div>
</template>

<script>
import {handleStrParam} from "@/lib/utils/param";

export default {
  name: "pagexCheckBox",
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
      type: String,
      default: () => "id",
    },
    param: {
      type: Object,
      default: () => {
      },
    },
    value: {
      type: Array,
      default: () => []
    },
    dictCode: {
      type: String,
      default: () => "",
    },
  },
  data() {
    return {
      ownerOption: [],
      isHaveFatherOption: false,
      newUrl: "",
      newLabelField: "",
      newValueField: ""
    };
  },
  computed: {
    newValue: {
      get: function () {
        return this.value;
      },
      set: function (value) {
        this.$emit("input", value);
      },
    },
  },
  async mounted() {
    this.isHaveFatherOption = typeof this.options != "undefined";
    this.newURL = this.url;
    this.newLabelField = this.labelField;
    this.newValueField = this.valueField;
    if (this.dictCode) {
      this.newURL =
        "/webApi/wordbook/getData?wordbookGroupCode=" + this.dictCode;
      this.newValueField = "wordbookCode";
      this.newLabelField = "wordbookDesc";
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
          method: "GET",
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
      } else {
        this.ownerOption = _options;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.el-checkbox {
  color: #999999;
}
</style>
