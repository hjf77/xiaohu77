<!--树形组件-->
<template>
  <div>
    <treeselect
      :flat="flat"
      v-bind="$attrs"
      v-on="$listeners"
      :options="treeDatas"
      :normalizer="normalizer"
      :placeholder="placeholder"
      v-model="newValue"
      :multiple="multiple"
      :default-expand-level="defaultExpandLevel"
      :searchable="searchable"
      :value-consists-of="valueConsistsOf"
      @select="selectFn"
      :clearable="clearable"
      :disabled="disabled"
    />
  </div>
</template>
<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
// 文档地址：https://www.vue-treeselect.cn/
export default {
  model: {
    prop: "value",
    event: "change",
  },
  inject: {
    elForm: {
      default: "",
    },
    elFormItem: {
      default: "",
    },
  },
  props: {
    url: String,
    value: {
      default: () => {
        return undefined;
      },
    },
    //是开开启平面模式
    flat: {
      type: Boolean,
      default: true
    },
    //是否支持搜索过滤
    searchable: {
      type: Boolean,
      default: true
    },
    disabled:{
      type: Boolean,
      default: false
    },
    queryFilter: {
      type: Object,
      default: () => {
        return {};
      },
    },
    defaultExpandLevel: {
      type: Number,
      default: 1,
    },
    placeholder: {
      type: String,
      default: () => {
        return '请选择';
      },
    },
    valueField: {
      type: String,
      default: () => {
        return 'id';
      },
    },
    labelField: {
      type: String,
      default: () => {
        return 'name';
      },
    },
    clearable:{
      type: Boolean,
      default: () => {
        return true;
      },
    },
    multiple: {
      type: Boolean,
      default: () => {
        return false;
      },
    },
    httpMehod: {
      type: String,
      default: () => {
        return 'POST';
      }
    },
    disableOn:{
      type: Function
    },
    selectOn:{
      type: Function
    },
    valueConsistsOf: {
      type: String,
      default: 'LEAF_PRIORITY'
    },
  },
  computed: {
    newValue: {
      get: function () {
        return this.value;
      },
      set: function (value) {
        this.$emit("change", value);
        this.$emit("input", value);
        this.dispatch("ElFormItem", "el.form.change", value);
      },
    },
  },
  components: {Treeselect},
  data() {
    return {
      treeDatas: [],
    };
  },
  mounted() {
    this.requestData();
  },
  watch: {},
  methods: {
    // 请求数据
    requestData() {
      if (this.httpMehod == 'POST') {
        this.$pagexRequest({
          url: this.url,
          data: this.queryFilter,
          method: "POST",
        })
          .then((res) => {
            this.treeDatas = res;
          });
      } else if (this.httpMehod == 'GET') {
        this.$pagexRequest.get(this.url)
          .then((res) => {
            this.treeDatas = res;
          });
      }

    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: this.valueField == 'id' ? node.id : node.data[this.valueField],
        label: node.name,
        isDisabled: this.disableOn && (typeof node =='object')  ? this.disableOn(node) : false,
        children: node.children,
        data:node.data
      };
    },

    /**
     * 表单验证
     * @param componentName
     * @param eventName
     * @param params
     */
    dispatch(componentName, eventName, params) {
      var parent = this.$parent || this.$root;
      var name = parent.$options.componentName;

      while (parent && (!name || name !== componentName)) {
        parent = parent.$parent;

        if (parent) {
          name = parent.$options.componentName;
        }
      }
      if (parent) {
        parent.$emit.apply(parent, [eventName].concat(params));
      }
    },
    selectFn(node){
      this.selectOn && this.selectOn(node);
    }
  },
};
</script>

<style scoped>
</style>
