<!--
  模块名称：辅助指挥的资源调度
  开发人员：金丹雅
  创建时间:2021/7/23 09:30
-->

<template>
    <div style="margin:20px;">
        <el-tabs v-model="activeName" @tab-click="handleClick" style="background: #fff;">
            <el-tab-pane v-for="(item,index) in resourcesData" :key="index" :label="item.label" :name="item.name">
                <pagex-crud 
                    ref="pagexCrud"
                    :columns="item.columns"
                    :api="item.api"
                    :querys="querys"
                    :isNeedPager="false"
                    :sortSett="sortSett"
                ></pagex-crud>
                <div style="background:#fff;width:100%;padding:0 20px;height:60px;">
                   <div class="el-upload-box" @click="clickAdd(index)">
                        <img src="@/assets/icon/uploadFile.png">
                    </div> 
                </div>
            </el-tab-pane>
        </el-tabs>
        <div class="btnStyle">
            <el-button type="primary"  @click="startUsing" class="btnStyle-item">确定</el-button>
            <el-button  @click="cancel" type="cancel" class="btnStyle-item">取消</el-button>
        </div>
        <el-dialog width="75%" :visible.sync="openRulesListEquipment" :title="'添加'+title" v-if="openRulesListEquipment" append-to-body>
            <addequipment  v-on:changeEquipment="changeEquipment" :selectEquipment="selectEquipment" :title="title" :equipmentType="equipmentType" v-if="title == '装备'"></addequipment>
        </el-dialog>
    </div>
</template>
<script>
import addequipment from './neWequipment/index';
export default {
    name:"schedulingOfResources",
    components:{ addequipment,},
    data(){
        return{
            openRulesListEquipment:false,    // 添加装备的开关
            selectEquipment:[],   //已选择的装备
            equipmentType:null,   // 装备类别
            activeName: 'emergencySupplies',
            querys:[],
             // 列表排列顺序（更新时间）
            sortSett:[{
              direction: "DESC",
              property: "updateTime"
            }],
            resourcesData:[
                {
                    label:"应急物资",
                    name:"emergencySupplies",
                    api:"/ms/workAccessory/accessoryList/Record",
                    columns:[]
                },
                {
                    label:"应急装备",
                    name:"commonEquipment",
                    api:"/ms/commonEquipment/findListAdvance",
                    columns:[
                        {label: "序号", name: "", type: "index"},
                        {
                            type: "cascader",
                            name: "type",
                            label: "装备类别",
                            propsField:{ label:"name", value:"id"},
                            querys:{},
                            api: "/ms/commonEquipmentType/tree"
                        },
                        { label: "所属部门", name: "orgIdName", type: "popover" },
                        { label: "关联装备", name: "teamIdName", type: "popover" },
                        { label: "装备数量", name: "quantity" },
                        { label: "剩余有效天数", name: "effectiveDate" },
                        {
                            label: "操作",
                            name: "operation",
                            type: "textBtn",
                            width: "90",
                            textBtn: [
                                {
                                    name: "详情",
                                    type: "success",
                                    size: "mini",
                                    ifFun: (_row) => {
                                        return !_row.add;
                                    },
                                    click: (_row) => {
                                        this.$pagexRequest({
                                            url: "/ms/commonEquipment/info/" + _row.id,
                                            method: "get",
                                        }).then((res) => {
                                            // const copyRow = deepClone(res);
                                            // copyRow.type = copyRow.typeName;
                                            // copyRow.purchaseCompany = copyRow.purchaseCompanyName;
                                            // copyRow.companyId = copyRow.companyIdName;
                                            // copyRow.orgId = copyRow.orgName;
                                            // this.$set(this, "init", copyRow);
                                            // this.title = "详情";
                                            // this.open = true;
                                            // this.isDetail = true;
                                            // this.isEdit = false;
                                        });
                                    },
                                },
                                {
                                    name: "添加",
                                    type: "success",
                                    size: "mini",
                                    ifFun: (_row) => {
                                        return _row.add;
                                    },
                                    click: (_row) => {
                                        if(!_row.type || _row.type.length == 0){
                                            this.msgError('请选择装备类别');
                                            return
                                        }
                                        this.title = "装备";
                                        this.openRulesListEquipment = true;
                                        this.equipmentType = _row.type
                                    },
                                },
                                {
                                    name: "编辑",
                                    type: "success",
                                    size: "mini",
                                    ifFun: (_row) => {
                                        return _row.add;
                                    },
                                    click: (_row) => {
                                        if(!_row.type || _row.type.length == 0){
                                            this.msgError('请选择装备类别');
                                            return
                                        }
                                        this.title = "装备";
                                        this.openRulesListEquipment = true;
                                        this.equipmentType = _row.type
                                    },
                                },
                                {
                                    name: "删除",
                                    type: "success",
                                    size: "mini",
                                    ifFun: (_row) => {
                                        return _row.add;
                                    },
                                    click: (_row) => {
                                        this.title = "装备";
                                        this.openRulesListEquipment = true;
                                        this.equipmentType = _row.type
                                    },
                                },
                            ]
                        }
                    ]
                },
                {
                    label:"应急队伍",
                    name:"resourceTeam",
                    api:"/ms/workAccessory/accessoryList/Record",
                    columns: [
                        {label: "序号", name: "", type: "index"},
                        {label: "类型", name: "names", minWidth: 140, type: 'popover', align: 'left'},
                        {label: "名称", name: "name", minWidth: 140, type: 'popover', align: 'left'},
                        // {label: '小组类型', name: 'groupTypeName', type: 'popover', width: 100, ifFun:()=>{ return this.namespace !== 'backbone' }},
                        {label: "负责人/联系人", name: "principalPerson", width: 100},
                        // {label: '联系人', name: 'contactPerson', type: 'popover', width: 100 ,ifFun:()=>{ return this.namespace !== 'backbone' }},
                        {label: "队伍人数", name: "number", width: 150},
                        {label: "所属单位", name: "companyIdName", width: 140, type: 'popover'},
                        {
                            label: '操作',
                            name: 'operation',
                            type: 'textBtn',
                            width: "90",
                            textBtn: [
                                {
                                    name: "详情",
                                    type: "success",
                                    size: "mini",
                                    click: (_row) => {
                                        this.dialogStatus = "detail"
                                        this.$set(this, "init", _row)
                                        this.isDetail = true
                                        this.isEdit = false
                                        this.$set(this, "dialogVisible", true)
                                    }
                                },,
                                {
                                    name: "添加",
                                    type: "success",
                                    size: "mini",
                                    ifFun: (_row) => {
                                        return _row.add;
                                    },
                                    click: (_row) => {
                                        this.title = "队伍";
                                        this.openRulesListEquipment = true;
                                        // this.$pagexRequest({
                                        //     url: "/ms/commonEquipment/info/" + _row.id,
                                        //     method: "get",
                                        // }).then((res) => {
                                        //     // const copyRow = deepClone(res);
                                        //     // copyRow.type = copyRow.typeName;
                                        //     // copyRow.purchaseCompany = copyRow.purchaseCompanyName;
                                        //     // copyRow.companyId = copyRow.companyIdName;
                                        //     // copyRow.orgId = copyRow.orgName;
                                        //     // this.$set(this, "init", copyRow);
                                        //     // this.isDetail = true;
                                        //     // this.isEdit = false;
                                        // });
                                    },
                                }
                            ]
                        }
                    ],
                },
            ]
        }
    },
    methods: {
        // 保存装备
        changeEquipment(id,val){
            // this.formData.equipmentIds=[...id,...this.formData.equipmentIds]
            // this.formData.equipmentIds = [...new Set(this.formData.equipmentIds)]
            this.openRulesListEquipment=val
        },
      handleClick(tab, event) {
        console.log(tab, event);
      },
      clickAdd(index){
        this.$refs.pagexCrud[index].getList({typeName:'',add:true})
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
    ::v-deep .el-tabs__header {
        padding: 0;
        position: relative;
        margin: 0 0 0px 0;
        padding-top: 14px;
        padding-left: 20px;
    }
    ::v-deep .list{
        padding: 10px 20px 0px 20px;
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