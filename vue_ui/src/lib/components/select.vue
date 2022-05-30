<template>
  <el-select
    ref="pagexSelectRef"
    v-bind="$attrs"
    v-on="$listeners"
    clearable
    :disabled="disabled"
    filterable
    :remote="remote"
    :allow-create="isAllowCreate"
    :default-first-option="isDefaultFirstOption"
    @change="_change"
    :remote-method="remoteMethod"
    :placeholder="placeholder"
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
    //选项
    options: {
      type: Array,
      default: () => undefined,
    },
    //api地址 如果去后台请求数据的话
    url: {
      type: String,
      default: () => "",
    },
    //显示的字段
    labelField: {
      type: String,
      default: () => "title",
    },
    //赋值的 字段
    valueField: {
      type: String | Number,
      default: () => "id",
    },
    //请求后台接口附带参数
    param: {
      type: Object,
      default: () => {},
    },
    //如果是字典的话字典分组编码是多少
    dictCode: {
      type: String,
      default: () => "",
    },
    //请求后台的参数 ?后面拼接
    querys: {
      type: Object,
      default: () => null,
    },
    //是否禁用
    disabled: {
      type: Boolean,
      default: false,
    },
    //后台接口类型
    methodType: {
      type: String,
      default: 'GET',
    },
    //value是否强制转换为number
    isValueNum:{
      type: Boolean,
      default: false,
    },
    //通过自定义方法格式化显示的数据 配合 customLabelQuerys使用
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
    //是否默认选中第一个
    isDefaultFirstOption: {
      type: Boolean,
      default: false,
    },
    //是否允许创建新的选项
    isAllowCreate: {
      type: Boolean,
      default: false,
    },
    // 是否为远程搜索
    remote: {
      type: Boolean,
      default: false,
    },
    placeholder: {
      type: String,
      default: '请选择',
    },
    //选中事件
    selectOn:{
      type: Function
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
        "/basic/ms/dictItem/findList?dictGroupCode=" + this.dictCode;
      this.newValueField = "dictCode";
      this.newLabelField = "dictDesc";
    }
    if (this.newURL && !this.remote) {
      this.loadData();
    }
    this._change();
  },
  methods: {
    async loadData(query) {
      let data = null;
      if (this.querys) {
        data = await this.$pagexRequest({
          url: this.newURL,
          data: this.querys,
          method: this.methodType,
        });
      } else {
        data = await this.$pagexRequest.get(
          handleStrParam(query ? this.newURL + query : this.newURL, this.param)
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
      this.selectOn && this.selectOn(this.radioValue);
      this.$emit("change", this.radioValue);
    },

    // 远程搜索
    remoteMethod(query) {
      if (query.trim() !== '') {
        this.loading = true;
        this.loadData(query)
        this.loading = false;
      } else {
        this.options = [];
      }
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
