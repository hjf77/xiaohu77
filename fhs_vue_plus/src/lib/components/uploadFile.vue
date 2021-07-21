<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/14 14:35
-->


<template>
  <div>
    <el-upload
      ref="upload"
      v-model="upload"
      :limit="null"
      :disabled="upload.isUploading"
      :file-list="fileList"
      :auto-upload="false"
      :before-upload="beforeUpload"
      :on-change="handleFileUploadProgress"
      :on-preview="handlePreview"
      :accept="acceptFormat"
      :on-remove="onRemove"
      drag
    >
      <div class="el-upload__text">
        <span style="color: #a0a4af"> 将文件拖到此处，或 </span>
        <em>点击上传</em>
      </div>
      <!--      <div class="el-upload__tip" style="color:red;margin-top:4px;" slot="tip">提示：仅允许导入 xxx 格式文件！</div>-->
    </el-upload>
  </div>
</template>

<script>
import { getFile } from "@/utils/download";
export default {
  name: "uploadFile",
  model: {
    prop: "fileRaw", //指向props的参数名
    event: "change", //事件名称
  },
  inject: {
    elForm: {
      default: "",
    },
    elFormItem: {
      default: "",
    },
  },
  props: {
    fileRaw: {
      type: File,
      default: null,
    },
    fileList: {
      type: Array,
      default: [],
    },
    url: {
      type: String,
    },
    acceptFormat: {
      //限制文件上传时的文件类型
      type: String,
      default: "",
    },
  },
  data() {
    return {
      upload: {
        open: true,
        title: "",
        // 是否禁用上传
        isUploading: false,
      },
    };
  },
  mounted() {},
  created() {
  },
  methods: {
    //下载
    handlePreview(file) {},
    handleFileUploadProgress(file, fileList) {
      if (fileList.length > 1) {
        fileList.splice(0, 1);
      }
      this.fileList =fileList
      this.beforeUpload(file);
      this.dispatch("ElFormItem", "el.form.change", [this.fileRaw]);
    },

    beforeUpload(file) {
      console.log(file.raw);
      this.$emit("change", file.raw);
    },
    //删除文件列表
    onRemove(file, fileList) {
      this.fileList = fileList;
      this.fileRaw =null
      this.$emit("change", this.fileRaw);
      this.dispatch("ElFormItem", "el.form.change", [this.fileRaw]);
    },
    /**
     * 表单验证
     * @param componentName
     * @param eventName
     * @param params
     */
    dispatch(componentName, eventName, params) {
      var parent = this.$parent || this.$root;
      var name = parent.$options.componentName;

      while (parent && (!name || name !== componentName)) {
        parent = parent.$parent;

        if (parent) {
          name = parent.$options.componentName;
        }
      }
      if (parent) {
        parent.$emit.apply(parent, [eventName].concat(params));
      }
    },
  },
};
</script>

<style scoped lang="scss">
::v-deep .el-upload-dragger {
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
