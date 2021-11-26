<template>
  <div class="pictureCss">
    <el-upload
      action="#"
      :file-list="fileList"
      list-type="picture-card"
      :http-request="uploadFile"
      :on-success="onSuccess"
      :limit="limit"
      :on-exceed="onExceed"
      :auto-upload="true"
      :accept='accept'
    >
      <i slot="default" class="el-icon-plus"></i>
      <div slot="file" slot-scope="{file}">
        <img
          class="el-upload-list__item-thumbnail"
          :src="file.url" alt=""
        >
        <span class="el-upload-list__item-actions">
        <span
          class="el-upload-list__item-preview"
          @click="handlePictureCardPreview(file)"
        >
          <i class="el-icon-zoom-in"></i>
        </span>
        <span
          v-if="!disabled"
          class="el-upload-list__item-delete"
          @click="handleDownload(file)"
        >
          <i class="el-icon-download"></i>
        </span>
        <span
          v-if="!disabled"
          class="el-upload-list__item-delete"
          @click="handleRemove(file)"
        >
          <i class="el-icon-delete"></i>
        </span>
      </span>
      </div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
import {getFile} from "../../utils/download";

export default {
  name: "uploadPicture",
  props: {
    value: {},
    mode: {
      type: String,
      validator: val => ["text", "picture-card", "picture"].indexOf(val) !== -1,
      default: "text"
    },

    //是否禁用删除，和下载
    disabled: {
      type: Boolean,
      default: false
    },

    //最大上传个数
    limit: {
      type: Number,
      default: 2
    },

    // 文件类型
    accept: {
      type: String,
      default: '.jpg,.jpeg,.png,.bmp,.gif'
    }
  },
  data() {
    return {
      text: undefined,
      dialogImageUrl: '',
      dialogVisible: false,
      fileList: [],
    };
  },
  watch: {
    value: {
      handler: function () {
        this.initVal()
      },
      immediate: true
    }
  },

  mounted() {
    this.initFileList()
  },

  methods: {

    //删除
    handleRemove(file) {
      console.log(file);
      this.$confirm("确定是否删除", "请注意", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        for (let i = 0; i < this.fileList.length; i++) {
          let _item = this.fileList[i];
          if (file.response.uid == _item.response.uid) {
            this.fileList.splice(i, 1);
            break;
          }
        }
        this.updateValue()
      });
    },

    //查看图片
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },

    //下载图片
    handleDownload({response}) {
      console.log(response);
      getFile("/downLoad/file?fileId=" + response.uid)
    },

    //上传成功
    onSuccess(response, file, fileList) {
      console.log(response)
      console.log(file)
      console.log(fileList)
      this.fileList = fileList
      this.updateValue()
    },

    //上传超出限制
    onExceed(files, fileList) {
      this.$confirm(`当前限制上传${this.limit}个文件，是否替换最后一个文件`, "请注意", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.fileList.splice(this.fileList.length - 1, 1);
        this.uploadFile({ file: files[0] },async res=>{
          const _item = {
            fileId: res.fileId,
            fileName: res.fileName,
            fileSizeDesc: res.fileSizeDesc,
            name: res.fileName,
            status: "success",
            url: await this.getFileAddress(res.fileId),
            response: {
              uid: res.fileId,
            },
          }
          this.fileList.push(_item);
          this.updateValue()
        })
      })
    },

    initVal() {
      this.text = this.value
    },

    //更新数据
    updateValue() {
      let uuIds = []
      this.fileList.forEach(item => {
        uuIds.push(item.response.uid)
      })
      this.text = uuIds.join(",")
      this.$emit("input", this.text)
      this.$emit("getFile", this.fileList)
      this.dispatch("ElFormItem", "el.form.change", [this.text]);
    },


    //通过文件ids获取全部文件信息
    initFileList(ids) {
      //调用后台接口，并且初始化组件的文件列表
      this.$pagexRequest({
        method: "get",
        url: "/downLoad/listData?fileIds=" + this.text,
      })
        .then(async (res) => {
          let _that = this;
          for (const _item of res) {
            _that.fileList.push({
              fileId: _item.fileId,
              fileName: _item.fileName,
              fileSizeDesc: _item.fileSizeDesc,
              name: _item.fileName,
              status: "success",
              url: await _that.getFileAddress(_item.fileId),
              response: {
                uid: _item.fileId,
              },
            });
          }
          this.dispatch("ElFormItem", "el.form.change", [this.text]);
          //这里些回显的代码
        })
        .catch((res) => {
          console.log(res);
        });
    },


    //上传文件
    uploadFile(file,fn) {
      let fd = new FormData();
      fd.append("Filedata", file.file || file.raw); //传文件
      this.$pagexRequest({
        method: "post",
        url: "/upload/file?ext=" + this.$store.state.user.user.companyId,
        data: fd,
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
        .then((res) => {
          const _item = res;
          file.onSuccess ? file.onSuccess({uid: _item.fileId}) : fn(_item);
        })
        .catch((res) => {
          console.log(res);
        });
    },


    //获取图标base64地址
    async getFileAddress(fileId) {
      const res = await this.$pagexRequest({
        methods: "get",
        url: "/downLoad/file?fileId=" + fileId,
        responseType: "arraybuffer"
      })
      return 'data:image/png;base64,' + btoa(
        new Uint8Array(res)
          .reduce((data, byte) => data + String.fromCharCode(byte), "")
      )
    },


    /**
     * 表单验证
     * @param componentName
     * @param eventName
     * @param params
     */
    dispatch(componentName, eventName, params) {
      let parent = this.$parent || this.$root;
      let name = parent.$options.componentName;

      while (parent && (!name || name !== componentName)) {
        parent = parent.$parent;

        if (parent) {
          name = parent.$options.componentName;
        }
      }
      if (parent) {
        parent.$emit.apply(parent, [eventName].concat(params));
      }
    }
  }
}
</script>

<style scoped>

</style>
