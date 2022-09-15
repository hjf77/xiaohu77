<template>
  <el-select
    ref="pagexSelectRef"
    v-bind="$attrs"
    v-on="$listeners"
    clearable
    :disabled="disabled"
    filterable
    :remote="remote"
    :multiple="multiple"
    :allow-create="isAllowCreate"
    :default-first-option="isDefaultFirstOption"
    @change="_change"
    @visible-change="visibleChange"
    :remote-method="remote?remoteMethod:null"
    :placeholder="placeholder"
  >
    <template v-if="isHaveFatherOption">
      <el-option
        v-if="showAll || item[valueField] == $attrs.value"
        v-for="item in options"
        :key="item[valueField]"
        :label="item[labelField]"
        :value="item[valueField]"
      >
      </el-option>
    </template>
    <template v-else>
      <el-option
        v-if="showAll || item.valueField == $attrs.value"
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
import {handleStrParam} from "@/lib/utils/param";
import {debounce} from "@/utils";
import {deepClone} from "../utils";

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
    //本组件绑定字段的名称
    name: {
      type: String,
      default: () => "select",
    },
    //显示的字段
    labelField: {
      type: String,
      default: () => "title",
    },
    //labelField(remarkLabelField)
    remarkLabelField: {
      type: String,
      default: null,
    },
    //赋值的 字段
    valueField: {
      type: String | Number,
      default: () => "id",
    },
    //请求后台接口附带参数
    param: {
      type: Object,
      default: () => {
      },
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
    isValueNum: {
      type: Boolean,
      default: false,
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
    multiple:{
      type: Boolean,
      default: false,
    },
    //选中事件
    selectOn: {
      type: Function
    },
    title: {
      type: String,
      default: '',
    }
  },
  data() {
    return {
      ownerOption: [],
      isHaveFatherOption: false,
      newUrl: "",
      newLabelField: "",
      newValueField: "",
      visible:false,
    };
  },
  computed:{
    //当visible 为true或者 多选的时候显示全部
    showAll: {
      get() {
        return this.visible || this.multiple
      },
      set(val) {

      }
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
    if (this.newURL && !this.remote) {
      this.loadData();
    }
    this._change();
    // 回显的时候构造出一条数据
    if (this.title) {
      const obj = {
        labelField: this.title,
        valueField: this.$attrs.value
      }
      this.ownerOption.push(obj)
    }
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
          if (_that.remarkLabelField) {
            _item.labelField = _item[_that.newLabelField] + '(' + _item[_that.remarkLabelField] + ')';
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
      } else {
        this.ownerOption = _options;
      }
      this.$emit("refreshOptions", {name: this.name, options: _options});
    },
    _change() {
      this.selectOn && this.selectOn(this.$attrs.value);
      this.$emit("change", this.$attrs.value);
    },

    // 远程搜索
    remoteMethod(query) {
      if (query.trim() !== '') {
        this.loading = true;
        debounce(this.loadData(query))
        this.loading = false;
      } else {
        this.options = [];
      }
    },
    visibleChange(_visible){
      this.visible = _visible;
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
