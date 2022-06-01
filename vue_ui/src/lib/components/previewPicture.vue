<template>
  <div>
    <ul class="el-upload-list el-upload-list--picture-card" >
      <li class="el-upload-list__item is-success" v-for="(file,i) in files">
        <img :src="file.url" class="el-upload-list__item-thumbnail">
        <span class="el-upload-list__item-actions">
          <span
            class="el-upload-list__item-preview"
            @click="handlePictureCardPreview(i)"
          >
            <i class="el-icon-zoom-in"></i>
          </span>

          <span
            class="el-upload-list__item-delete"
            @click="handleDownload(file.fileId)"
          >
            <i class="el-icon-download"></i>
          </span>
        </span>
      </li>
    </ul>
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
    fileIds: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      files: [],
      dialogVisible: false,
      dialogImageUrl: ''
    };
  },

  mounted() {
    this.initFileList()
  },

  methods: {
    //查看图片
    handlePictureCardPreview(index) {
      this.dialogImageUrl = this.files[index].url
      this.dialogVisible = true;
    },

    //下载图片
    handleDownload(fileId) {
      getFile("/downLoad/file?fileId=" + fileId)
    },

    //通过文件ids获取全部文件信息
    initFileList() {
      //调用后台接口，并且初始化组件的文件列表
      this.$pagexRequest({
        method: "get",
        url: "/basic/downLoad/listData?fileIds=" + this.fileIds,
      })
        .then(async (res) => {
          let _that = this;
          for (const _item of res) {
            _that.files.push({
             url: await _that.getFileAddress(_item.fileId),
             fileId: _item.fileId
            });
          }
          _that.url = _that.files[0].url
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
    }
  }
}
</script>

<style lang="scss" scoped>

.el-upload-list--picture-card {
  margin: 0;
  display: inline;
  vertical-align: top;
}

.el-upload-list--picture-card .el-upload-list__item {
  overflow: hidden;
  background-color: #fff;
  border: 1px solid #c0ccda;
  border-radius: 5px;
  box-sizing: border-box;
  width: 120px;
  height: 120px;
  margin: 0 8px 8px 0;
  display: inline-block;
}
.el-upload-list--picture-card .el-upload-list__item-thumbnail {
  width: 100%;
  height: 100%;
}

.el-upload-list--picture-card .el-upload-list__item-actions {
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  cursor: default;
  text-align: center;
  color: #fff;
  opacity: 0;
  font-size: 20px;
  background-color: rgba(0,0,0,0.5);
  transition: opacity .3s;
}

.el-upload-list--picture-card .el-upload-list__item-actions span {
  cursor: pointer;
}

.el-upload-list--picture-card .el-upload-list__item-actions .el-upload-list__item-delete {
  position: static;
  font-size: inherit;
  color: inherit;
}

.el-upload-list--picture-card .el-upload-list__item-actions:hover {
  opacity: 1;
}

</style>
