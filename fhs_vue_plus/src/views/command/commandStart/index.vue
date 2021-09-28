<!--
  模块名称：预警启动
  开发人员：zfw
  创建时间: 2021-09-24
-->
<template>
    <base-container>
            <pagex-crud
                    ref="crud"
                    :filters="filters"
                    :columns="columns"
                    :api="api"
                    :sortSett="sortSett"
                    :buttons="buttons"
                    :querys="querys"
            >
                <template v-if="open" v-slot:form="prop">
                    <!-- 新增 修改 详情 弹框-->
                    <el-dialog
                            class="pagex-dialog-theme"
                            slot="form"
                            :title="title"
                            :visible.sync="open"
                            v-if="open"
                    >
                        <dataUpload v-if="title == '辅助材料'" @closeDialog="closeDialog"></dataUpload>
                        <commandStartForm
                                v-else
                                :init="init"
                                :isDetail="isDetail"
                                :isEdit="isEdit"
                        ></commandStartForm>
                    </el-dialog>
                </template>
            </pagex-crud>
    </base-container>
</template>

<script>
    import crudMixins from "@/mixins/crudMixins";
    import commandStartForm from "./components/commandStartForm";
    import dataUpload from "./components/dataUpload.vue";
    import {mapGetters} from "vuex"
    export default {
        name: "commandStart",
        components: {
            commandStartForm,
            dataUpload,
        },
        mixins: [crudMixins],
        data() {
            return {
                rightContent: false,//右侧内容区域
                api: "/ms/commandStart/pagerAdvance",
                isEdit: false,
                open: false,
                isDetail:false,
                // 列表排列顺序（更新时间）
                sortSett: [
                    {
                        direction: "DESC",
                        property: "updateTime",
                    },
                ],
                //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
                buttons: [
                    {
                        title: "新增",
                        name: "add",
                        type: "primary",
                        size: "mini",
                        icon: "el-icon-plus",
                        permission: ["commandStart:add"],
                        click: () => {
                            this.title = "新增";
                            this.isEdit = false;
                            this.$set(this, "init", {});
                            this.open = true;
                            this.isDetail = false;
                        },
                    },
                ],
                columns: [
                    { label: "序号", name: "", type: "index", width: "100" },
                    {label: '信息标题', name: 'name', type: "popover"},
                    {label: '事发时间', name: 'incidentTime', width: 150},
                    {label: '事件发生地点', name: 'locationOfTheIncident',type: "popover"},
                    {label: '上报时间', name: 'reportTime', width: 150},
                    {label: '经办人', name: 'handler', width: 150},
                    {label: '现场联络员', name: 'liaison', width: 150},
                    {label: '责任单位', name: 'responsiblePersonId',type: "popover"},
                    {label: '状态', name: 'statusName', width: 150},
                    {
                        label: "操作",
                        name: "operation",
                        type: "textBtn",
                        width:420,
                        textBtn: [
                            {
                                name: "研判待改",
                                type: "success",
                                size: "mini",
                                click: (_row) => {
                                    this.$set(this, "init", _row);
                                    this.title = "详情";
                                    this.open = true;
                                    this.isDetail = true;
                                    this.isEdit = false;
                                },
                            },
                            {
                                name: "编辑",
                                type: "primary",
                                size: "mini",
                                permission: ["commandStart:update"],
                                click: (_row) => {
                                    this.$set(this, "init", _row);
                                    this.title = "编辑";
                                    this.isEdit = true;
                                    this.open = true;
                                    this.isDetail = false;
                                },
                            },
                            {
                                name: "详情",
                                type: "success",
                                size: "mini",
                                click: (_row) => {
                                    this.$set(this, "init", _row);
                                    this.title = "详情";
                                    this.open = true;
                                    this.isDetail = true;
                                    this.isEdit = false;
                                },
                            },
                            {
                                name: "删除",
                                type: "danger",
                                api: "/ms/commandStart/",
                                size: "mini",
                                permission: ["commandStart:del"],
                            },
                            {
                                name: "辅助材料",
                                type: "success",
                                size: "mini",
                                click: (_row) => {
                                    //this.$set(this, "init", _row);
                                    this.title = "辅助材料";
                                    this.open = true;
                                    //this.isDetail = true;
                                    //this.isEdit = false;
                                },
                            },
                        ],
                    },
                ],
                filters:[ 
                    {
                        formname: "事发时间:",
                        name: "incidentTime",
                        type: "date-picker",
                        operation: "between",
                    },
                    {
                        formname: "状态:",
                        name: "statusName",
                        type: "select",
                        dictCode: "command_start_status",
                        operation: "=",
                    },
                    {
                        formname: '信息标题', 
                        name: 'name', 
                        placeholder: "信息标题", 
                        type: 'text',
                        operation: "like"},
                ],
                querys: [

                ],
            };
        },
        created() {},
        computed: {
            ...mapGetters(["user"]),

        },
        methods: {
            closeDialog(){
            this.$set(this,"open",false)
            }
        },
    };
</script>
<style lang="scss" scoped>

</style>
