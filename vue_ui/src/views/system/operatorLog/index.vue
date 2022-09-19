<!--
  模块名称：操作日志
  开发人员：wanglei
  创建时间: 2022-05-13
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
        name: "logOperatorMain",
        mixins: [crudMixins],
        data() {
            return {
                api: "/basic/ms/logOperatorMain/pagerAdvance",
                // 列表排列顺序（更新时间）
                sortSett: [
                    {
                        direction: "DESC",
                        property: "createTime",
                    },
                ],
                //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
                buttons: [

                ],
                columns: [
                     {label: '时间', name: 'createTime'},
                     {label: '模块', name: 'menuName', width: 150},
                     {label: 'url', name: 'url', width: 150},
                     {label: '操作类型', name: 'typeName', width: 150},
                     {label: '请求参数', name: 'reqParam', type: 'popover', width: 150},
                     {label: '返回参数', name: 'respBody',type: 'popover', width: 150},
                     {label: '状态', name: 'stateName', width: 150},
                     {label: '操作人', name: 'createUserUserName', width: 150},
                     {label: 'IP', name: 'ip', width: 150},
                     {label: '数据主键', name: 'pkey'}
                ],
                filters:[
                     {"name":"namespace","label":"模块","type":"treeSelect","url":'/basic/ms/sysMenu/tree',idField:"namespace"},
                     {"name":"createTime","label":"操作时间","type":"datetimerange","operation":"between"},
                     {"name":"createUser","label":"操作人","type":"select",url:"/basic/ms/sysUser/findList",labelField:"userName",valueField:"userId"},
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
