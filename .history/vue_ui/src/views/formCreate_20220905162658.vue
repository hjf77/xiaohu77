<template>
  <base-container>
    <div class="_fc-t-header">
      <div class="_fc-t-menu">
        <el-button size="mini" type="primary" @click="showJson"
          >生成JSON</el-button
        >
        <el-button size="mini" type="success" @click="showOption"
          >生成Options</el-button
        >
        <el-button size="mini" type="danger" @click="showTemplate"
          >生成组件</el-button
        >
      </div>
    </div>
    <fc-designer ref="designer" />
  </base-container>
</template>
  
<script>
import FcDesigner from "@/formCreate/index.js";

export default {
  name: "Index",
  components: {
    FcDesigner,
  },
  methods: {
    handleClick(row) {
      console.log(row);
    },
  },
};
</script>
<style>
._fc-t-header {
  height: 60px;
  margin: 0 20px;
  position: relative;
  display: flex;
  align-items: center;
  cursor: default;
}
._fc-t-menu {
  position: absolute;
  right: 0;
}
</style>
  