<template>
    <div >
      <el-checkbox-group
        v-if="isHaveFatherOption"
        v-model="newValue">
        <el-checkbox
          v-for="item in options"
          :key="item.value"
          :label="item.value"
        >{{ item.label }}
        </el-checkbox>
      </el-checkbox-group>
      <el-checkbox-group
        v-model="newValue" v-else>
        <el-checkbox
          v-for="item in ownerOption"
          :key="item.valueField"
          :label="item.valueField"
        >{{ item.labelField }}
        </el-checkbox>
      </el-checkbox-group>
    </div>
</template>

<script>
import {handleStrParam} from "@/lib/utils/param";
import {deepClone} from "../utils";

export default {
  name: "pagexCheckBox",

  model: {
    prop: "value",
    event: "input"
  },
  props: {
    options: {
      type: Array,
      default: () => undefined,
    },
    url: {
      type: String,
      default: () => "",
    },
    //本组件绑定字段的名称
    name: {
      type: String,
      default: () => "select",
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
      this.$emit("refreshOptions", {name: this.name, options: _options});
    },
  },
};
</script>

<style lang="scss" scoped>
.el-checkbox {
  color: #999999;
}
</style>
