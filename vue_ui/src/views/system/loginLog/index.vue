<!--
  模块名称：登录日志
  开发人员：wanglei
  创建时间: 2021-12-07
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

            </pagex-crud>
    </base-container>
</template>

<script>
    import crudMixins from "@/mixins/crudMixins";
    //这个路径可能需要改下

    import {mapGetters} from "vuex"
    export default {
        name: "logLogin",
        mixins: [crudMixins],
        data() {
            return {
                rightContent: false,//右侧内容区域
                api: "/basic/ms/logLogin/pagerAdvance",
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

                ],
                columns: [
                     {label: '用户', name: 'loginUserName', width: 150},
                     {label: '用户名', name: 'loginName', width: 150},
                     {label: '时间', name: 'createTime'},
                     {label: 'ip', name: 'ipAddress', width: 150},
                     {label: 'ip信息', name: 'ipInfo', width: 150},
                     {label: '浏览器', name: 'browser', width: 150},
                     {label: '操作系统', name: 'os', width: 150},
                     {label: '状态', name: 'stateName', width: 150},
                     {label: '类型', name: 'typeName', width: 150},
                     {label: '错误类型', name: 'errorInfoName'}

                ],
                filters:[
                     {"name":"ipAddress","label":"ip","type":"text","operation":"like"},
                     {"name":"loginName","label":"用户名","type":"text","operation":"like"},
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

        },
    };
</script>
<style lang="scss" scoped>

</style>
