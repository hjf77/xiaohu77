<!--
  模块名称：应急启动的辅助材料
  开发人员：zfw
  创建时间:2021/9/24
-->

<template>
    <div>
        <pagex-form
                :addApi="addApi"
                ref="pagexForm"
                :updateApi="updateApi"
                :data="formData"
                :controls="controls"
                :buttons="buttons"
        >

        </pagex-form>
    </div>
</template>
<script>
import { getFile } from "@/utils/download";
export default {
    name:"dataUpload",
    props: {
    },
    created() {
    },
    data(){
        return{
            updateAdd:'',
            api:'',
            open:false,
            title:'',
            userId:null,
            formData:{},
            controls:[
              {"name":"accessoryId","label":"文件上传","type":"uploadFileAsync",},
            ]
        }
    },
    methods:{

    //   确定
      startUsing(){
          this.cancel()
      },
      //   取消
      cancel(){
          this.$emit("closeDialog")
      },
    }
}
</script>
<style lang="scss" scoped>
    .dataUploadStyle{
        margin: 20px;
    }
    ::v-deep .dataUploadStyle .list{
        padding: 20px 20px 0px 20px;
    }
    .btnStyle{
        text-align: right;
        height: 60px;
        background: #fff;
        border-radius: 4px;
        line-height: 60px;
        margin-left: -20px;
        width:960px
    }
    .btnStyle-item{
        margin-right: 20px;
        height: 36px;
        width: 80px;
    }
</style>
