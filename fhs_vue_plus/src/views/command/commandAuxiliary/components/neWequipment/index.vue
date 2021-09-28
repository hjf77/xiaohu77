<!--
  模块名称：辅助指挥--资源调度--新增装备
  开发人员：金丹雅
  创建时间: 2021-09-23
-->
<template>
        <div style="background:#F7F7F7;height:680px;">
            <div class="contentBox">
                    <div class="leftBox"><freshList v-on:childList="childList" :equipmentType="equipmentType"></freshList></div>
                    <div class="rightBox">
                        <span class="box_span" v-if="showSpan">已选装备(<span>{{outNumber1}}</span>)</span>
                        <span class="box_span" v-else>已选装备(<span>{{outNumber2}}</span>)</span>
                        <div class="numbers">
                            <ul>
                                <li v-for="i in getOutside" :key="i.id"><span class="nameSpan">{{i.typeName}}</span><span style="color:red;cursor:pointer;" @click="deleteID(i.id)">删除</span></li>
                            </ul>
                        </div>
                    </div>
            </div>
            <div class="button_buttom">
                <el-button style="margin-left:70%;margin-top:15px;" type="primary" @click="dialogVisible">保 存</el-button>
                <el-button @click="dialogVisibleCancel">取 消</el-button>
            </div>
        </div>
</template>
<script>
import freshList from './newEquipment.vue'
export default{
    components:{
        freshList,
    },
    props:{
        title:{
            type:String,
            default:()=>''
        },
        equipmentType:{
            type:Array,
            default:()=>[]
        }
    },
    data(){
        return{
            showSpan:true,
            getOutside:'',//装备id集合
            outNumber1:'0',
            outNumber2:'0',
            insideBox:true,
            temporaryBox:false,
            list:'',
            acceptFormat: "",
        }
    },
    methods: {
        childList(childValue){//获取每条装备
            this.showSpan = true
            this.getOutside = childValue
            for (var i = 0, len = this.getOutside.length; i < len; i++) {//装备去重
                for (var j = i + 1, len = this.getOutside.length; j < len; j++) {
                    if (this.getOutside[i].id === this.getOutside[j].id) {
                        this.getOutside.splice(j, 1);
                        j--;        // 每删除一个数j的值就减1
                        len--;      // j值减小时len也要相应减1（减少循环次数，节省性能）   
                        }
                }
            }
            this.outNumber1 = this.getOutside.length
        },
        deleteID(ids){//删除所选装备
            for(var i =0;i<this.getOutside.length;i++){
                if(this.getOutside[i].id==ids){
                    this.getOutside.splice(i,1);
                }
            }
            this.showSpan = false
            this.outNumber2 = this.getOutside.length
        },
        dialogVisible(){//点击保存装备 保存
            let ids = []
            if(this.getOutside){
                this.getOutside.forEach((item)=>{
                    ids.push(item.id)
                })
            }
            // console.log('ids-------',ids)
            this.$emit("changeEquipment",ids,false)
        },
        dialogVisibleCancel(){//取消
            let ids = []
            this.$emit("changeEquipment",ids,false)
        },
    /**
     * 获取org
     * @param org
     * @returns {boolean}
     */
    refreshRight(_companyIdQuery){
        
    },
    //  关闭上传按钮
    uploadCancel(data) {
      this.open = data ? data : false;
      this.$refs.crud.reload();
    },
  },
}
</script>
<style lang="scss" scoped>
::v-deep.list{
    width:850px !important;
    overflow: auto;
    height: 600px;
}
::v-deep.search{
    width: 100%;
    white-space:nowrap;
    background: #FFF;
}
.elementButton{
    border-radius: 8px;
}
::v-deep.leftBox{
    overflow: auto;
}
::v-deep.base-container{
    width: 900px;
}
.contentBox{
    margin-top:20px;
    display:flex;
    justify-content: space-around;
    height:600px;
    overflow:hidden;
}
.rightBox{
    margin-top: 20px;
}
::v-deep.rightBox{
    width: 350px;
    height: 600px;
    float: right;
    margin-left: 20px;
    overflow: auto;
    background: #FFF;
}
::v-deep.el-pagination{
    margin: 26px 130px !important;
}
.box_span{
    height: 50px;
    line-height: 50px;
    font-size: 18px;
    color: black;
    padding-left: 20px;
}
.numbers{
    height: 550px;
}
.numbers ul li{
    list-style: none;
    margin-top: 13px;
    width: 250px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-left: -15px;
}
.numbers ul li span{
    font-size:14px ;
}
.el_button{
    padding-top:20px;
    padding-left:10px;
}
.nameSpan{
    width:150px;
    font-size: 20px;
    overflow:hidden;
    word-break:keep-all;
    white-space:nowrap;
    text-overflow:ellipsis;
}
.button_buttom{
    margin-top: 15px;
}
</style>