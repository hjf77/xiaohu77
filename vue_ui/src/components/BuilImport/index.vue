<!--
  模块名称：批量上传
  开发人员：金丹雅
  创建时间:2021/8/12 10:00
-->

<template>
    <div>
        <pagex-form
            :addApi="excelImportApi"
            :controls="controls"
            :isFormDataSub = "true"
            :data="formData"
            :eventBusName="eventBusName"
        >
        <el-button slot="planExplain" type="primary" icon="el-icon-download" size= "mini" @click="downloadHandle" style="margin-right:600px">下载模板</el-button>
        </pagex-form>
    </div>
</template>
<script>
import {getFile} from "@/utils/download";
export default {
    name:'BuilImport',
    props:{
        excelTemplateName:{
            type:String,
            default:()=>'work_rules'
        },
        excelImportApi:{
            type:String,
            default:()=>''
        },
        formData:{
            type:Object,
            default:()=>{
                return {};
            }
        },
        controls:{
            type:Array,
            default:()=>{
                return [
                {
                    type:'slot',
                    name: 'planExplain',
                    label: '下载模板',
                    width:700
                },
                {
                    type:'uploadFile',
                    name: "file",
                    label: "上传文件",
                    rule: "required",
                    width: "425",
                    isDownload: false,
                }
            ]
            }
        },
        eventBusName: {
          type:Array,
          default:()=> {
            return []
          }
        }
    },
    data(){
        return{
        }
    },
    methods:{
        // 下载模板
        downloadHandle(){
            getFile(`/basic/ms/excel_template/download?excelTemplateName=${this.excelTemplateName}.xlsx`)
        },
    }
}
</script>
<style lang="scss" scoped>
::v-deep .el-form-item--small .el-form-item__content {
    line-height: 40px;
}
</style>
