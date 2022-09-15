<!--树形组件-->
<template>
  <div>
    <treeselect
      v-model="text"
      :options="dic"
      :normalizer="normalizer"
      :multiple="multiple"
      :placeholder="placeholder || '请选择'"
      :load-options="initOption"
      @input="handleChange"
      @select="handleSelect"
      :value-consists-of="valueConsistsOf"
      :flat="flat"
      :clearable="clearable"
    />
  </div>
</template>
<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  props: {
    url: String,
    query: Object,
    placeholder: String,
    option: Object,
    value: {
      default: () => {
        return undefined;
      },
    },
    validateEvent: {
      type: Boolean,
      default: true
    },
    methodType: {
      default: () => {
        return 'POST';
      },
    },
    multiple: {
      type: Boolean,
      default: () => {
        return false;
      },
    },
    valueConsistsOf: {
      type: String,
      default: 'LEAF_PRIORITY'
    },
    //是开开启平面模式
    flat: {
      type: Boolean,
      default: true
    },
    clearable:{
      type: Boolean,
      default: () => {
        return true;
      },
    },
  },
  inject: {
    elForm: {
      default: ''
    },
    elFormItem: {
      default: ''
    }
  },
  components: {Treeselect},
  data() {
    return {
      text: undefined,
      dic: [],
    };
  },
  created() {
    this.initOption();
    this.initVal();
  },
  watch: {
    text: {
      handler(n, o) {
        this.handleChange(n);
      },
    },
    value: {
      handler(n, o) {
        this.initVal();
      },
    },
  },
  methods: {
    initVal() {
      this.text = this.value;
    },

    async initOption() {
      let _data = this.option;
      if (this.url) {
        _data = await this.$pagexRequest({
          url: this.url,
          data: this.query,
          method: this.methodType || "POST",
        });
      }
      this.$set(this, "dic", _data);
    },

    handleChange(value) {
      this.$nextTick(() => {
        let result = value;
        this.$emit("input", result);
        this.$emit("change", result);
        if (this.validateEvent) {
          this.dispatch('ElFormItem', 'el.form.change', result);
        }
      });
    },

    handleSelect(data) {
      this.$nextTick(() => {
        this.$emit('select', data)
      })
    },

    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children,
      };
    },

    //自定义组件触发校验
    dispatch(componentName, eventName, params) {
      let parent = this.$parent || this.$root;
      let name = parent.$options.componentName;

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
  },
};
</script>

<style scoped>
</style>
