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
    one2xChange:{
      type: Function,
      default: null
    },
    one2xOptionsSetts:{
      type:Array,
      default: () => ([])
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
      if(this.one2xChange){
        let _datas = deepClone(this.datas);
        let _one2xOptionsSetts = deepClone(this.one2xOptionsSetts);
        this.one2xChange(val,_datas,this.index,_one2xOptionsSetts,this);
        this.$emit("update:datas", _datas);
        this.$emit("update:one2xOptionsSetts", _one2xOptionsSetts);
      }
    },
    //one2x适配
    handleBlur(){
      this.$emit("blur", this.newValue);
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
