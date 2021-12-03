<!--
  模块名称：
  开发人员：王磊
  创建时间: 2021-09-30
-->
<template>
    <div>
        <!-- 详情参数配置对话框 -->
        <pagex-formDetail
                v-if="isDetail"
                :controls="controls"
                :data="init"
        >
        </pagex-formDetail>

        <!-- 编辑参数配置对话框 -->
        <pagex-form
                v-else
                :isEdit="isEdit"
                :addApi="addApi"
                :updateApi="updateApi"
                :data="formData"
                :init="init"
                :controls="controls"
                :buttons="buttons"
        >

        </pagex-form>
    </div>
</template>
<script>
    import {mapGetters} from "vuex";
    export default {
        name: "setmsMenuServerForm",
        inject: ["reloadable"],
        props: {
            isEdit: Boolean,
            init: Object,
            isDetail:Boolean,
        },
        computed: {
            ...mapGetters(["user"])
        },
        data() {
            return {
                formData: {
                     id: this.init.id ? this.init.id : null,

                },
                addApi: "/basic/ms/sett_ms_menu_server/",
                updateApi: "/basic/ms/sett_ms_menu_server/",
                controls: [
                     {"name":"serverName","rule":"required","label":"服务名称","type":"text"},
                     {"name":"serverUrl","rule":"required","label":"服务url","type":"text"},
                ]
            }
        },
        created() {

        },
        methods: {


        },
    };
</script>

<style lang="scss" scoped>
    ::v-deep .pageX-form-detail .type-file-span {
        height: 0px;
    }
</style>
