<!--
  模块名称：辅助指挥的辅助材料
  开发人员：金丹雅
  创建时间:2021/7/23 09:30
-->

<template>
    <div class="dataUploadStyle">
        <pagex-crud 
          ref="accessoryList"
          :columns="columns"
          api="/ms/workAccessory/accessoryList/Record"
          :querys="querys"
          :isNeedPager="false"
          :sortSett="sortSett"
        ></pagex-crud>
        <uploadFile
            v-model="updateAdd"
        ></uploadFile>
        <div class="btnStyle">
            <el-button type="primary"  @click="startUsing" class="btnStyle-item">确定</el-button>
            <el-button  @click="cancel" type="cancel" class="btnStyle-item">取消</el-button>
        </div>
    </div>
</template>
<script>
import { getFile } from "@/utils/download";
import uploadFile from "./uploadFile"
export default {
    name:"dataUpload",
    components:{ uploadFile },
    props: {
      namespace: {
        type: String,
        default: () => ''
      },
      companyId: {
        type: String,
        default: () => ''
      },
      orgId: {
        type: String,
        default: () => ''
      },
      fKey: {
        type: String,
        default: () => ''
      },
      //isEnable 1为未归档   0为已归档
      isEnable:{
        type:Number,
        default:()=>1
      },
      isUploader:{
        type:Boolean,
        default:()=>false
      }
    },
    created() {
      this.querys[0].value = this.namespace;
      this.querys[1].value = this.fKey;
      this.querys[2].value = this.companyId;
      this.querys[3].value = this.orgId;
      this.userId = this.$store.state.user.user.userId;
    },
    data(){
        return{
            updateAdd:'',
            api:'',
            open:false,
            title:'',
            userId:null,
             // 列表排列顺序（更新时间）
            sortSett:[{
              direction: "DESC",
              property: "updateTime"
            }],
            querys: [
              {
                operation: "=",
                property: "namespace",
                relation: "AND",
                value: null
              },
              {
                operation: "=",
                property: "fKey",
                relation: "AND",
                value: null
              },
              {
                operation: "=",
                property: "companyId",
                relation: "AND",
                value: null
              },
              {
                operation: "=",
                property: "orgId",
                relation: "AND",
                value: null
              }
            ],
            columns:[
                {label: "附件名称", name: "accessoryIdName", type: "popover" },
                {label: '附件类型', name: 'transMap.createUserUserName', type: 'popover'},
                {label: '附件来源', name: 'orgIdName', type: 'popover'},
                {label: '上传时间', name: 'createTime', type: "popover"},
                {
                    label: '操作',
                    width:"290px",
                    name: 'operation',
                    type: 'textBtn',
                    textBtn: [
                        {
                            name: "下载",
                            type: "primary",
                            size: 'mini',
                            click: (_row) => {
                                getFile('/downLoad/file?fileId=' + _row.accessoryId);
                            }
                        },
                        {
                            name: "预览",
                            type: "success",
                            size: 'mini',
                            click: (_row) => {
                                this.$set(this,'init',_row)
                                this.title = '详情';
                                // this.isEdit = true;
                                this.open = true;
                            }
                        },
                        {
                          name: "删除",
                          type: "danger",
                          size: 'mini',
                        //   ifFun: (_row) => {
                        //     //  刷新详情的列表
                        //     this.$emit("refreshListHan")
                        //     return _row.createUser == this.userId && this.isEnable == 1;
                        //   },
                          api:'/ms/workAccessory/',
                        },
                    ]
                }
            ]
        }
    },
    methods:{
      refreshAcc(){
        this.$refs.accessoryList.getList();
      },
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
