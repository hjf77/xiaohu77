<template>

  <el-cascader
    ref="pagexCascaderRef"
    v-bind="$attrs"
    v-on="$listeners"
    :options="modifyOptions"
    :props="propsField"
    :disabled="disabled"
    >
  </el-cascader>
</template>

<script>
export default {
  name: "pagexCascader",
  props: {
    options: {
      type: Array,
      default: () => undefined,
    },
    url: {
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
      default: 'POST',
    },
    width:{
      type: Number,
      default: 425,
    },
    //显示的字段
    labelField: {
      type: String,
      default: () => "label",
    },
    valueField: {
      type: String | Number,
      default: () => "value",
    },
  },
  data() {
    return {
      modifyOptions:[],
      newUrl: "",
      propsField:{
        label:null,
        value:null,
      }
    };
  },
  created() {
    this.propsField.label = this.labelField;
    this.propsField.value = this.valueField;
  },
  async mounted() {
    this.newURL = this.url;
    if(!this.options){
      this.loadData();
    }else{
      this.modifyOptions = this.options;
    }
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
        console.log("没传入数据，也没有传请求参数和请求地址；")
      }

      this.removeEmptyArr(data);
      this.modifyOptions = data;
    },
    removeEmptyArr(arr){
      arr.forEach(item => {
        if(item.children.length > 0){
          this.removeEmptyArr(item.children);
        }else{
          delete item.children;
        }
      });
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
