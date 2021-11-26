<!--文件上传  包含多文件上传-->
<template>
  <div>
    <el-upload
      ref="upload"
      :limit="multiple ? '' : 1"
      :multiple="multiple"
      :action="uploadApi"
      :auto-upload="false"
      :http-request="httpRequest"
      :on-change="handleFileUploadProgress"
      :file-list="fileList"
      drag
    >
      <div class="el-upload__text">
        <span style="color: #a0a4af"> 将文件拖到此处，或 </span>
        <em>点击上传</em>
      </div>
    </el-upload>
  </div>
</template>
<script>
export default {
  props: {
    uploadApi: String,
    multiple: Boolean,
    query: Array,
  },
  data() {
    return {
      fileList: [],
    };
  },
  mounted() {},
  created() {},
  methods: {
    handleFileUploadProgress(file, fileList) {
      this.fileList = fileList;
      this.$emit('fileList',this.fileList)
    },
    httpRequest(param) {
      let file = param.file;
      let formData = new FormData();
      formData.append("protoFile", file);
      if (this.query.length != 0) {
        this.query.forEach((element) => {
          formData.append(
            Object.keys(element).toString(),
            Object.values(element).toString()
          );
        });
      }
      this.$pagexRequest({
        url: this.uploadApi,
        data: formData,
        method: "put",
      })
        .then((res) => {
          if(res.state){

          }else{

          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    submitUpload(data) {
      this.$refs.upload.submit(data);
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