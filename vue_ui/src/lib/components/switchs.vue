<template>
  <div>
      <el-switch
        :active-value="activeValue"
        :inactive-value="inactiveValue"
        v-model="switchValue"
        active-color="#13ce66"
        inactive-color="gray"
        @change="switchChange"
      >
      </el-switch>
  </div>
</template>

<script>
export default {
  name: "pagexSwitchs",
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    value: {
      type: String | Boolean | Array,
    },
  },
  data() {
    return {
      switchValue: this.value,
      activeValue: 1,
      inactiveValue: 0,
    };
  },
  async mounted() {
    this.switchChange(this.switchValue);

  },

  methods: {
    switchChange(val) {
      this.$emit("change", val);
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
