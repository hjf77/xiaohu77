<template>
  <div>
    <p class="file-container">
      <img src="@/assets/icon/file.png">
      <span class="textStyle" :title="file.fileName">{{ file.fileName }}</span>
      <span class="file-right">
          <span class="file-size">{{ file.fileSizeDesc }}</span>
          <el-button v-if="downloadble"  type="text" @click="download">下载</el-button>
          <el-button v-if="deleteble" type="text" class="del-btn" @click="deleteFile">删除</el-button>
      </span>
    </p>
  </div>
</template>

<script>
import {deepClone} from "../utils"
import {getFile} from "../../utils/download"
export default {
  name: "FileItem",
  props: {
    file: {
      type: Object,
      default: 0
    },
    downloadble: {
      type: Boolean,
      default: true
    },
    deleteble: {
      type: Boolean,
      default: true
    }
  },
  computed: {},
  data() {
    return {
      isSameCompany: this.file.ext ? this.file.ext == this.$store.state.user.user.companyId :true
    }
  },
  created() {
  },
  methods: {


    download(){
      let url = `/downLoad/file?fileId=${this.file.fileId}`
      getFile(url)

    },

    //删除
    deleteFile(){
      this.$emit("delete",this.file)
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
  background: #fff;
}
.textStyle{
  line-height: 34px;
  padding-left: 15px;
  width: 540px;
  overflow: hidden;
  display: inline-block;
  white-space: nowrap;
  text-overflow: ellipsis;
  vertical-align: middle;
}
</style>
