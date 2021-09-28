<template>
  <el-dialog
    v-if="boxVisible"
    class="pagex-dialog-theme"
    :title="textMap[dialogType]"
    :before-close="hide"
    :visible.sync="boxVisible">
      <el-scrollbar class="scrollbar" :style="styleName">
        <div>
      <crud-form-detail
        v-if="dialogType == 'detail'"
        :dialogType="dialogType"
        :form-option="formOption"
        :value="tableForm"
        @cancel="cancelForm"
      >
      </crud-form-detail>
      <crud-form
        v-else
        v-model="tableForm"
        :add-api="crud.option.addApi"
        :update-api="crud.option.updateApi"
        :dialogType="dialogType"
        :form-option="formOption"
        @cancel="cancelForm"
        @sucess="saveForm"
        :is-form-data-sub="isFormDataSub"
      >
      </crud-form>
        </div>
      </el-scrollbar>

  </el-dialog>
</template>

<script>

import crudForm from "./form"
import CrudFormDetail from "./formDetail";

export default {
  name: "dialog-form",
  inject: ["crud"],
  components: {CrudFormDetail, crudForm},
  data() {
    return {
      tableForm: {
      },
      dialogType: "",
      boxVisible: false,
      isFormDataSub:false,
      textMap: {
        add: "新增",
        edit: "编辑",
        detail: "详情"
      },
    }
  },
  computed: {
    //form表单配置项
    formOption() {
      const option = this.deepClone(this.crud.option.formOption || {})
      return option
    },
    formVal() {
      const option = this.deepClone(this.crud.option.formOption.data || {})
      return option
    },
    dialogHeight() {
      return 'auto'
    },
    styleName(){
      return {
        height: this.dialogHeight || "auto",
      }
    }
  },
  created() {
    this.$nextTick(()=>{
      this.tableForm = this.deepClone(this.formVal)
    })
  },
  methods: {
    // 显示Dialog表单

    show(type, index = -1) {
      this.index = index;
      this.dialogType = type;
      const callback = () => {
        this.$nextTick(() => {
          this.boxVisible = true;
        });
      };
      if (typeof this.crud.beforeOpen === "function") {
        this.crud.beforeOpen(callback, this.dialogType);
      } else {
        callback();
      }
    },


    // 隐藏表单
    hide(done) {
      const callback = () => {
        done && done();
        this.crud.tableIndex = -1;
        this.tableForm = {};
        this.$nextTick(() => {
          this.boxVisible = false;
        });
      };
      if (typeof this.crud.beforeClose === "function") {
        this.crud.beforeClose(callback, this.dialogType);
      } else {
        callback();
      }
    },


    proxyIf(_ifFun, _default) {
      if (_ifFun == 'disabled') {
        return true
      }
      return _ifFun ? _ifFun.call(this.model) : _default;
    },


    //form取消按钮回调
    cancelForm() {
      this.hide()
    },


    //保存成功后的回调
    saveForm() {
      this.hide()
      this.crud.getList()
    },
    deepClone(source) {
      if (!source && typeof source !== 'object') {
        throw new Error('error arguments', 'deepClone')
      }
      const targetObj = source.constructor === Array ? [] : {}
      Object.keys(source).forEach(keys => {
        if (source[keys] && typeof source[keys] === 'object') {
          targetObj[keys] = this.deepClone(source[keys])
        } else {
          targetObj[keys] = source[keys]
        }
      })
      return targetObj
    }
  }
}
</script>

<style lang="scss" scoped>
  /deep/ .scrollbar {
      overflow: hidden;
    .el-scrollbar__wrap {
      overflow-x: hidden;
      overflow-y: scroll;
    }
  }
</style>
