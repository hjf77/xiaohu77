<template>
  <div>
      <el-switch
        :active-value="activeValue"
        :inactive-value="inactiveValue"
        v-model="switchValue"
        :active-color="activeColor"
        :inactive-color="inactiveColor"
        @change="switchChange"
        v-bind="$attrs"
        v-on="$listeners"
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
    activeColor: {
      type: String,
      default:  "#13ce66",
    },
    inactiveColor: {
      type: String,
      default:  "gray",
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
