<template>
  <div>
    <p class="file-container" v-for="(file,index) in fileList" :key="index">
      <img src="@/assets/icon/file.png">
      <span style="line-height: 34px;padding-left:15px;">{{ file.fileName }}</span>
      <span class="file-right">
          <span class="file-size">{{ file.fileSizeDesc }}</span>
          <el-button type="text" v-if="isDown" @click="download(file)">下载</el-button>
          <el-button type="text" v-if="isDel" class="del-btn" @click="deleteFile(file)">删除</el-button>
      </span>
    </p>
  </div>
</template>

<script>
import {getFile} from "@/utils/download"
export default {
  name: "FileList",
  props: {
    id:{
      type: String | Number,
    },
    isDown:{
      type: Boolean,
      default:true
    },
    isDel:{
      type: Boolean,
      default:false
    }
  },
  computed: {

  },
  data() {
    return {
      fileList:[],
    }
  },
  created() {
    this.getFile()
  },
  methods: {

    // 请求回显的附件
    async getFile() {
      const res = await this.$pagexRequest({
        method: "get",
        url: "/downLoad/listData?fileIds=" + this.id,
      })
      const _that = this
      res.forEach(function (_item) {
        _that.fileList.push({
          fileId: _item.fileId,
          fileName: _item.fileName,
          fileSizeDesc: _item.fileSizeDesc
        })
      })
    },

    download(file){
      let url = `/downLoad/file?fileId=${file.fileId}`
      getFile(url)

    },

    //删除
    deleteFile(file){
      this.$emit("delete",file)
    }

  }
}
</script>

<style lang="scss" scoped>
.file-container {
  margin: 0;
  background: #F7F9FB;
  padding: 8px 20px;
  border-radius: 4px;
  margin-bottom: 10px;
  cursor: pointer;
  color: #333333;

  > img {
    vertical-align: top;
  }

  > span {
    line-height: 30px;
  }

  > .file-size {
    color: #999999;
    margin-right: 10px;
  }

  > .file-right {
    float: right;
  }
}

.del-btn {
  color:#E50808;
}
.del-btn:hover {
  color:#e82525;
}
</style>
