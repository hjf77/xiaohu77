<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/14 14:35
-->
<template>
  <div>

    <pagex-fileItem v-for="(file,i) in fileAllList" :key="i" :file="file" :isDownload="isDownload" :isDelete="isDelete" @delete="deleteFile"></pagex-fileItem>

    <!-- on-remove -->
    <el-upload
      ref="upload"
      v-model="upload"
      :accept="acceptFormat"
      action=""
      :multiple="isMultiple"
      :disabled="upload.isUploading"
      :auto-upload="true"
      :before-upload="beforUpload"
      :http-request="uploadFile"
      :on-remove="onRemove"
      :on-change="onChange"
      :on-success="onSuccess"
      :on-preview="handlePreview"
      :file-list="fileList"
      :on-error="onError"
      :limit="limit"
      :on-exceed="onExceed"
    >
      <!-- <div slot="trigger"
           class="el-upload__text" @click="clickSubmit">
        <span style="color: #a0a4af"> 将文件拖到此处，或 </span>
        <em>点击上传</em>
      </div> -->

      <div class="el-upload-box" @click="clickSubmit" slot="trigger">
        <!-- <i class="el-icon-upload"></i> -->
        <img src="@/assets/icon/uploadFile.png">
        <!-- <el-button type="text">点击上传</el-button> -->
      </div>
      <!-- <div v-if="restricted">
        <i style="color: #999999; font-size: 10px"
          >文件最大限制:{{ size }}Mb,文件最大数量:{{ limit }}个
        </i>
      </div> -->
    </el-upload>
    <el-tooltip class="tooltipStyle" effect="light" v-if="isTip" :content="tipContent" placement="left-end">
        <i class="tipStyle">
          <i class="exclamationHeardStyle"></i>
          <i class="exclamationStyle"></i>
        </i>
    </el-tooltip>
  </div>
</template>

<script>
import { getFile } from "@/utils/download";

export default {
  name: "uploadFile",
  model: {
    prop: "fileIds", //指向props的参数名
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
    fileIds: {
      type: String || Array,
      default: null,
    },
    // fileList: {
    //     type: Array,
    //     default: []
    // },
    limit: {
      type: Number, //文件数量
      default: 10,
    },
    restricted: {
      type: Boolean,
      default: true,
    },
    async: {
      type: String,
      default: "",
    },
    url: {
      type: String,
    },
    isMultiple: {
      type: Boolean,
      default: true,
    },
    size: {
      type: Number, //单个文件最大限制 单位为"M"
      default: 10,
    },
    acceptFormat: {
      //限制文件上传时的文件类型
      type: String,
      default: "",
    },
    isDownload: {
      type: Boolean,
      default: true
    },
    isDelete: {
      type: Boolean,
      default: true
    },
    tipContent:{
      type:String,
      default:()=>''
    },
    isTip:{
      type:Boolean,
      default:()=>false
    }
  },
  data() {
    return {
      fileList: [],
      fileAllList: [],
      upload: {
        open: true,
        title: "",
        // 是否禁用上传
        isUploading: false,
      },
      fileIdList: [],
    };
  },
  computed: {
    newFileIds() {
      return this.fileIdList.join(",");
    },
  },
  mounted() {},
  created() {
    //文件回显
    if (this.fileIds && this.fileIds.length !== 0) {
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
                uid: _item.fileId,
              },
              uid: _item.fileId,
            });
            _that.fileIdList.push(_item.fileId);
          });
          _that.fileAllList = _that.fileList
          this.dispatch("ElFormItem", "el.form.change", [this.newFileIds]);
          //这里些回显的代码
        })
        .catch((res) => {
          console.log(res);
        });
    },


    //下载文件
    handlePreview(file) {
      //拿到
      const _url = `/dsp/fileService/v1/down?fileId=${file.response.uid}`;
      getFile(_url);
    },
    onChange(file, fileList) {
      // this.$set(file,'status','success')
    },

    //当上传文件超出文件个数时,上传的文件始终替换最后一个文件
    onExceed(files, fileList) {
      this.uploadFile({ file: files[0] }, (res) => {
        const _item = {
          uid: res.fileId,
        };
        this.$set(fileList[fileList.length - 1], "file", files[0]);
        this.$set(fileList[fileList.length - 1], "name", files[0].name);
        this.$set(fileList[fileList.length - 1], "response", _item);
        // this.$refs['upload'].clearFiles();//清除文件
        // this.$refs['upload'].handleStart(files[0]);//选
        this.fileIdList.splice(this.fileIdList.length - 1, 1, _item.uid);
        this.$emit("change", this.newFileIds);
        this.dispatch("ElFormItem", "el.form.change", [this.newFileIds]);
      });
    },

    //上传文件
    uploadFile(file, fn) {
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
          if(this.fileAllList.length >= this.limit){
            this.fileAllList.pop()
          }
          this.fileAllList.push(res)
          const _item = res;
          if (fn) {
            fn(_item);
          } else {
            file.onSuccess({ uid: _item.fileId });
          }
        })
        .catch((res) => {
          console.log(res);
        });
    },

    //上传前的回调
    beforUpload(file) {
      if(file.name.length > 54){
        this.msgError("文件名称太长，请精简文件名！")
        return false;
      }
      if(this.acceptFormat){
        let name = file.name;
        let acceptTypes = this.acceptFormat.split(",");
        let isCheck = false;
        acceptTypes.forEach((item)=>{
          if(name.indexOf(item)!=-1){
            isCheck = true;
          }
        })
        if(!isCheck){
          this.$message.error(`选中格式不支持，支持的文件格式有：` + this.acceptFormat);
          return false;
        }
      }

      if (typeof this.size == "number") {
        const maxSize = 1024 * 1024 * this.size;
        if (file.size > maxSize) {
          setTimeout(() => {
            this.$message.error(`[${file.name}] 文件最大限制${this.size}MB!`);
          }, 150);
          return false;
        }
      } else {
        return true;
      }
    },

    //上传成功时的回调
    onSuccess(response, file, fileList) {
      const _item = response;
      this.fileIdList.push(_item.uid);
      // if(this.fileIdList.length>0){
      //   this.$emit("change", this.newFileIds);
      //   this.dispatch("ElFormItem", "el.form.change", [this.newFileIds])
      // }
      this.$emit("change", this.newFileIds);
      this.dispatch("ElFormItem", "el.form.change", [this.newFileIds]);
    },

    //删除文件列表
    onRemove(file, fileList) {
      for (let i = 0; i < this.fileIdList.length; i++) {
        let _item = this.fileIdList[i];
        if (file.response.uid == _item) {
          this.fileIdList.splice(i, 1);
          break;
        }
      }
      this.fileList = fileList;
      this.$emit("change", this.newFileIds);
      this.dispatch("ElFormItem", "el.form.change", [this.newFileIds]);
    },

    //上传前弹出提示信息
    clickSubmit(e) {
      e.stopPropagation();
      let self = this;
      if (self.fileIdList.length >= this.limit) {
        this.$confirm(
          "已经超出文件最大数量，再次上传将覆盖最后一份文件，是否继续上传",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        ).then(() => {
          self.$refs["upload"].$refs["upload-inner"].handleClick();
        });
      } else {
        self.$refs["upload"].$refs["upload-inner"].handleClick();
      }
      // self.$refs["upload"].$refs["upload-inner"].handleClick();
    },


    //删除文件
    deleteFile(file){
      for (let i = 0; i < this.fileIdList.length; i++) {
        let _item = this.fileIdList[i];
        if (file.fileId == _item) {
          this.fileIdList.splice(i, 1);
          break;
        }
      }

      for (let i = 0; i < this.fileAllList.length; i++) {
        let _item = this.fileAllList[i].fileId;
        if (file.fileId == _item) {
          this.fileAllList.splice(i, 1);
          break;
        }
      }

      // this.fileList = fileList;
      this.$emit("change", this.newFileIds);
      this.dispatch("ElFormItem", "el.form.change", [this.newFileIds]);
    },



    onError(err, file, fileList) {
      console.log(err);
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
::v-deep .el-upload {
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
.tipStyle{
  position: absolute;
  right: -32px;
  bottom: 10px;
  width: 24px;
  height: 24px;
  background: rgba($color: #E50808, $alpha: 0.1);
  border-radius: 50%;
  display: flex;
  flex-direction:column;
  justify-content: center;
  align-items: center;
}
.exclamationHeardStyle{
  display: inline-block;
  width: 2px;
  height: 2px;
  background:#E50808;
  margin-bottom: 2px;
}
.exclamationStyle{
  display: inline-block;
  width: 2px;
  height: 10px;
  background:#E50808;
}
</style>
