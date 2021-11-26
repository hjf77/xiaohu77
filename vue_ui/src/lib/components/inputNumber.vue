<template>
  <div>
    <el-input-number
      :min="min"
      :max="max"
      :precision="0" :step="1"
      :size="this.size"
      v-bind="$attrs"
      @change="handleChange"
      v-model="showValue"
    ></el-input-number>
  </div>
</template>

<script>

export default {
  name: "pagexIpnutNumber",
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    options: {
      type: Object,
      default: () => {
        return {};
      }
    },
    size: {
      type: String,
      default: () => 'large'
    },
    value:{
      type:Number,
      default: () => 0
    },
    min:{
      type:Number,
      default: () => 0
    },
    max:{
      type:Number,
    },

  },
  data() {
    return {
      showValue:0,
    };
  },
  created() {
    this.showValue = this.value;
  },
  async mounted() {

  },
  methods: {
    handleChange(val){
      this.$emit("change", val);
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
