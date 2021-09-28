<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/14 14:35
-->


<template>
  <div>
    <template v-if="fileAllList.length > 0">
      <pagex-fileItem v-for="(file,i) in fileAllList" :key="i" :file="file" :is-download="isDownload" :is-delete="isDelete" @delete="deleteFile" :index="i"></pagex-fileItem>
    </template>
    <el-upload
      v-else
      action="''"
      ref="upload"
      v-model="upload"
      v-bind="$attrs"
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
      <div class="el-upload-box" slot="trigger">
        <img src="@/assets/icon/uploadFile.png">
      </div>
    </el-upload>
  </div>
</template>

<script>
import {getFile} from "@/utils/download";

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
    // fileList: {
    //   type: Array,
    //   default: [],
    // },
    url: {
      type: String,
    },
    acceptFormat: {
      //限制文件上传时的文件类型
      type: String,
      default: "",
    },
    fileIds: {
      type: String,
      default: null,
    },
    isDownload: {
      type: Boolean,
      default: true
    },
    isDelete: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      upload: {
        open: true,
        title: "",
        // 是否禁用上传
        isUploading: false,
      },
      fileList: [],
      fileAllList: [],
    };
  },
  mounted() {
  },
  created() {
    //文件回显
    if (this.fileIds) {
      this.initFileList();
    }
  },
  methods: {
    initFileList() {
      //调用后台接口，并且初始化组件的文件列表
      this.$pagexRequest({
        method: "get",
        url: "/downLoad/listData?fileIds=" + this.fileIds,
      })
        .then((res) => {
          let _that = this;
          res.forEach(function (_item) {
            _that.fileList.push({
              fileId: _item.fileId,
              fileName: _item.fileName,
              fileSizeDesc: _item.fileSizeDesc,
              name: _item.fileName,
              status: "success",
              response: {
                uid: _item.id,
              },
              uid: _item.id,
            });
            // _that.fileIdList.push(_item.id);
          });
          _that.fileAllList = _that.fileList
          // this.$emit("change", this.fileRaw);
          // this.dispatch("ElFormItem", "el.form.change", [this.fileRaw]);
          //这里些回显的代码
        })
        .catch((res) => {
          console.log(res);
        });
    },



    //下载
    handlePreview(file) {

    },
    handleFileUploadProgress(file, fileList) {
      if (fileList.length > 1) {
        fileList.splice(0, 1);
      }
      this.fileList = fileList
      this.beforeUpload(file);
      this.dispatch("ElFormItem", "el.form.change", [this.fileRaw]);
    },

    beforeUpload(file) {
      if(file.name.length > 54){
        this.msgError("文件名称太长，请精简文件名！")
        return
      }
      this.fileAllList = []
      const fileObj = {
        fileName: file.name,
        fileSizeDesc: (file.size/1024/1024).toFixed(2) + 'M'
      }
      this.fileAllList.push(fileObj)
      this.$emit("change", file.raw);
    },

    //删除文件
    deleteFile() {
      this.fileAllList = []
      this.fileList = [];
      this.fileRaw = null
      this.$emit("change", this.fileRaw);
      this.dispatch("ElFormItem", "el.form.change", [this.fileRaw]);
    },

    //删除文件列表
    onRemove(file, fileList) {
      this.fileList = fileList;
      this.fileRaw = null
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
  height: 100%;
  width: 100%;
}
/deep/ .el-upload {
  width: 740px;
}
::v-deep .file-container {
  width: 740px;
}
.el-upload-box {
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  border: 1px dashed #0F9B9C;
  border-radius: 6px;
  box-sizing: border-box;
  width: 100%;
  text-align: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  flex-direction: column;
}
.el-upload-box .el-icon-upload {
  font-size: 56px;
  color: #c0c4cc;
  line-height: 50px;
}
::v-deep .el-upload-list{
  display: none;
}
</style>
