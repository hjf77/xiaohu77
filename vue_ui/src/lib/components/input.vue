<template>
  <div>
    <el-input
      v-bind="$attrs"
      @change="handleChange"
      @blur="handleBlur"
      v-model="newValue"
    ></el-input>
  </div>
</template>

<script>

import {deepClone} from "../utils";

export default {
  name: "pagexInput",
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    value:{
      default: ''
    },
    index:{
      type: Number,
      default: () => (-1)
    },
    datas:{
      type:Array,
      default: () => ([])
    },
    one2xBlur:{
      type: Function,
      default: null
    }
  },
  data() {
    return {
      newValue:0,
    };
  },
  created() {
    this.newValue = this.value;
  },
  async mounted() {

  },
  methods: {
    handleChange(val){
      this.$emit("change", val);
    },
    //one2x适配
    handleBlur(){
      this.$emit("blur", this.newValue);
      if(this.one2xBlur){
        let _datas = deepClone(this.datas);
        this.one2xBlur(this.newValue,_datas,this.index);
        this.$emit("update:datas", _datas);
      }
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
