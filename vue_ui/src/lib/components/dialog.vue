<template>
  <el-dialog v-bind="$attrs" v-on="$listeners" :visible.sync="visible">
    <slot></slot>
  </el-dialog>
</template>

<script>
export default {
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    namespace: {
      type: String,
      default: () => null,
    }
  },
  computed: {},
  mounted() {
    this.$nextTick(() => {
      if (this.namespace) {
        this.$EventBus.$on(this.namespace + '_closeDialog', () => {
          this.$emit('update:visible', false);
        })
      }
    })
  },
  beforeDestroy() {
    this.$EventBus.$off(this.namespace + '_closeDialog');
  },
  methods: {}
}
</script>

<style lang="scss" scoped>

</style>
