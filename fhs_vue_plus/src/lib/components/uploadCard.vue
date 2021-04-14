<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/14 14:34
-->


<template>
  <div class="uploadCard">
    <el-upload
      action="https://jsonplaceholder.typicode.com/posts/"
      list-type="picture-card"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove">
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog :modal="false" :visible.sync="dialogVisible">
      <viewer :images="dialogImageUrl"></viewer>
    </el-dialog>
  </div>
</template>

<script>
import viewer from "./viewer"
import images from "./../../assets/404_images/404.png"
export default {
  name: "uploadCard",
  components:{
    viewer
  },
  data() {
    return {
      dialogImageUrl: [],
      dialogVisible: false
    }
  },
  mounted() {
  },
  created() {
  },
  methods: {
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = [];
      this.dialogImageUrl.push(file.url);
      this.dialogVisible = true;
    }
  }
}
</script>

<style scoped lang="scss">
  .uploadCard{

  }
</style>
