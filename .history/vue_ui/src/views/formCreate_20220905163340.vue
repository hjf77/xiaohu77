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
    <el-dialog :title="title[type]" :visible.sync="state" class="_fc-t-dialog">
            <div ref="editor" v-if="state"></div>
            <span style="color: red;" v-if="err">输入内容格式有误!</span>
            <span slot="footer" class="dialog-footer" v-if="type > 2">
            <el-button @click="state = false" size="small">取 消</el-button>
            <el-button type="primary" @click="onOk" size="small">确 定</el-button>
          </span>
        </el-dialog>
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
    showJson() {
      this.state = true;
      this.type = 0;
      this.value = this.$refs.designer.getRule();
    },
    showOption() {
      this.state = true;
      this.type = 1;
      this.value = this.$refs.designer.getOption();
    },
    showTemplate() {
      this.state = true;
      this.type = 2;
      this.value = this.makeTemplate();
    },
    makeTemplate() {
      const rule = this.$refs.designer.getRule();
      const opt = this.$refs.designer.getOption();
      return `<template>
  <form-create
    v-model="fapi"
    :rule="rule"
    :option="option"
    @submit="onSubmit"
  ></form-create>
</template>

<script>
import formCreate from "@form-create/element-ui";

export default {
  data () {
    return {
        fapi: null,
        rule: formCreate.parseJson('${formCreate
          .toJson(rule)
          .replaceAll("\\", "\\\\")}'),
        option: formCreate.parseJson('${JSON.stringify(opt)}')
    }
  },
  methods: {
    onSubmit (formData) {
      //todo 提交表单
    }
  }
}
<\/script>`;
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
  