<template>
  <!-- <base-container> -->
  <div style="padding: 0 20px 20px 20px">
    <div class="_fc-t-header">
      <div class="_fc-t-menu">
        <el-button size="small" type="primary" @click="showJson"
          >生成JSON</el-button
        >
        <el-button size="small" type="success" @click="showOption"
          >生成Options</el-button
        >
        <el-button size="small" type="danger" @click="showTemplate"
          >生成组件</el-button
        >
      </div>
    </div>
    <fc-designer ref="designer" />
    <el-dialog :title="title[type]" :visible.sync="state" class="_fc-t-dialog">
      <div ref="editor" v-if="state"></div>
      <span style="color: red" v-if="err">输入内容格式有误!</span>
      <span slot="footer" class="dialog-footer" v-if="type > 2">
        <el-button @click="state = false" size="small">取 消</el-button>
        <el-button type="primary" @click="onOk" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
  <!-- </base-container> -->
</template>
  
<script>
import FcDesigner from "./generate/formCreate/index";
import jsonlint from "jsonlint-mod";
import "codemirror/lib/codemirror.css";
import "codemirror/addon/lint/lint.css";
import CodeMirror from "codemirror/lib/codemirror";
import "codemirror/addon/lint/lint";
import "codemirror/addon/lint/json-lint";
import "codemirror/mode/javascript/javascript";
import "codemirror/mode/vue/vue";
import "codemirror/mode/xml/xml";
import "codemirror/mode/css/css";
import "codemirror/addon/mode/overlay";
import "codemirror/addon/mode/simple";
import "codemirror/addon/selection/selection-pointer";
import "codemirror/mode/handlebars/handlebars";
import "codemirror/mode/htmlmixed/htmlmixed";
import "codemirror/mode/pug/pug";

import is from "@form-create/utils/lib/type";
import formCreate from "@form-create/element-ui";
const TITLE = ["生成规则", "表单规则", "生成组件"];

import { getOptions } from "./generate/formCreate/config/base/field.js";

export default {
  name: "Index",
  components: {
    FcDesigner,
  },
  props: {
    tableSchema: String,
    tableName: String,
  },
  data() {
    return {
      state: false,
      value: null,
      title: TITLE,
      editor: null,
      err: false,
      type: -1,
      init: {
        tableSchema: "fhs-demo",
        tableName: "t_user",
        tableComment: "用户",
        fields: [
          {
            label: "username",
            name: "用户名",
            width: "150",
            isListShow: 1,
            isTrans: 0,
            transType: "dict",
            dictCode: "sex",
            transTable: "",
            transDB: "",
            transField: "",
            uniqueField: "",
          },
        ],
      },
    };
  },
  watch: {
    state(n) {
      if (!n) {
        this.value = null;
        this.err = false;
      }
    },
    value() {
      this.load();
    },
  },
  created() {
    // if (this.tableSchema !== "undefined" || this.tableName !== "undefined") {
    //   this.tableSchema = this.$route.query && this.$route.query.tableSchema;
    //   this.tableName = this.$route.query && this.$route.query.tableName;
    // }
  },
  mounted() {
    this.initData();
  },
  methods: {
    async initData(this.tableSchema,'this.tableSchema',this.tableName) {
      console.log()
      getOptions(this.tableSchema, this.tableName).then((res) => {
        let options = [];
        for (let i = 0; i < res.fields.length; i++) {
          const item = res.fields[i];
          options.push({
            lable: item.label,
            value: item.name,
          });
        }
        return options;
      });
    },

    // let dictGroups = await this.$pagexRequest({
    //   url: "/basic/ms/dictGroup/findList",
    //   method: "get",
    // });
    // console.log("dictGroups", dictGroups);

    // dictGroups.forEach((item) => {
    //   item.valueField = item.groupCode;
    //   item.labelField = item.groupName + "(" + item.groupCode + ")";
    // });

    // this.controls[3].controls[4].options = dictGroups;

    // let tables = await this.$pagexRequest({
    //   url: `/basic/ms/table/findList?tableSchema=${this.tableSchema}`,
    //   method: "get",
    // });

    // tables.forEach((item) => {
    //   item.valueField = item.tableName;
    //   item.labelField = item.tableComment + "(" + item.tableName + ")";
    // });

    // this.controls[3].controls[5].options = tables;

    // this.$nextTick(() => {
    //   this.$set(this, "initFinsh", true);
    // });
    load() {
      let val;
      if (this.type === 2) {
        val = this.value;
      } else if (this.type === 0) {
        val = formCreate.toJson(this.value, 2);
      } else {
        val = JSON.stringify(this.value, null, 2);
      }
      this.$nextTick(() => {
        this.editor = CodeMirror(this.$refs.editor, {
          lineNumbers: true,
          mode: this.type === 2 ? { name: "vue" } : "application/json",
          gutters: ["CodeMirror-lint-markers"],
          lint: true,
          line: true,
          tabSize: 2,
          lineWrapping: true,
          value: val || "",
        });
        this.editor.on("blur", () => {
          this.err = this.editor.state.lint.marked.length > 0;
        });
      });
    },
    onValidationError(e) {
      this.err = e.length !== 0;
    },
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
    onOk() {
      if (this.err) return;
      const json = this.editor.getValue();
      let val = JSON.parse(json);
      if (this.type === 3) {
        if (!Array.isArray(val)) {
          this.err = true;
          return;
        }
        this.$refs.designer.setRule(formCreate.parseJson(json));
      } else {
        if (!is.Object(val) || !val.form) {
          this.err = true;
          return;
        }
        this.$refs.designer.setOption(val);
      }
      this.state = false;
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
    beforeCreate() {
      window.jsonlint = jsonlint;
    },
  },
};
</script>
<style>
._fc-t-header {
  height: 50px;
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
._fc-t-dialog .CodeMirror {
  height: 500px;
}

._fc-t-dialog .CodeMirror-line {
  line-height: 16px !important;
  font-size: 14px !important;
}

.CodeMirror-lint-tooltip {
  z-index: 2021 !important;
}

._fc-t-dialog .el-dialog__body {
  padding: 0px 20px;
}
._fc-b-item {
  display: flex;
}
</style>
  