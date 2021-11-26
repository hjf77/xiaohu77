<!--
  模块名称：预警启动
  开发人员：zfw
  创建时间: 2021-09-24
-->
<template>
    <div>
        <!-- 详情参数配置对话框 -->
        <pagex-formDetail
                v-if="isDetail"
                :controls="controls"
                :data="init"
                :labelWidth="155"
        >
        </pagex-formDetail>

        <!-- 编辑参数配置对话框 -->
        <pagex-form
                v-else
                :isEdit="isEdit"
                :addApi="addApi"
                ref="pagexForm"
                :updateApi="updateApi"
                :data="formData"
                :init="init"
                :controls="controls"
                :buttons="buttons"
                :labelWidth="'155px'"

        >

        </pagex-form>
    </div>
</template>
<script>
    import {mapGetters} from "vuex";
    export default {
        name: "commandStartForm",
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
            const companyId = this.$store.getters.user.companyId
            return {
                formData: {
                     id: this.init.id ? this.init.id : null,
                     sourceType: this.init.sourceType ? this.init.sourceType : null,
                     status: this.init.status ? this.init.status : null,
                     applyUserId: this.init.applyUserId ? this.init.applyUserId : null,
                     responsibleUnit: this.$store.getters.user.companyName,
                     sourceType:1,//数据来源，此处前端写死；

                },
                userInfoArr: [],
                addApi: "/ms/commandStart/",
                updateApi: "/ms/commandStart/",
                controls: [
                    {
                         "name":"name",
                         "rule":"required",
                         "label":"信息标题",
                         "type":"text",
                         "width": "275"
                    },
                    {
                        "formatVal":"yyyy-MM-dd",
                        "name":"incidentTime",
                        "rule":"required",
                        "label":"事发时间",
                        "type":"dates",
                         "width": "275"
                    },
                    {
                        "name":"locationOfTheIncident",
                        "rule":"required",
                        "label":"时间发生地点",
                        "type":"text",
                         "width": "275"
                    },
                    {
                        "formatVal":"yyyy-MM-dd",
                        "name":"reportTime",
                        "rule":"required",
                        "label":"上报时间",
                        "type":"dates",
                         "width": "275"
                    },
                    {
                        "name":"handler",
                        "rule":"required",
                        "label":"经办人",
                        "type":"text",
                         "width": "275"
                    },
                    {
                        "name":"handlerPhone",
                        "rule":"required",
                        "label":"经办人联系电话",
                        "type":"text",
                         "width": "275"
                    },
                    {
                        "name":"liaison",
                        "rule":"required",
                        "label":"现场联络员",
                        "type":"select",
                        "width": "275",

                        'labelField': "userName",
                        'valueField': "userId",
                        'placeholder': "请选择",
                        'url': `/ms/sysUser/getUserByCompanyId?companyId=${companyId}`,
                        'isAllowCreate': true,
                        'isDefaultFirstOption': true,
                        "rule":"required",
                        change: (val, id) => {
                            const pagexForm = this.$refs.pagexForm
                            if (this.userInfoArr.length > 0) {
                            this.$refs.pagexForm.model.liaisonPhone= ''
                            for (let i = 0; i < this.userInfoArr.length; i++) {
                                if (id === this.userInfoArr[i].userId) {
                                pagexForm.$set(pagexForm.model,'liaisonPhone',this.userInfoArr[i].mobile)
                                pagexForm.checkForm('liaison')
                                break
                                }
                            }
                            }
                        }
                    },
                    {
                        "name":"liaisonPhone",
                        "rule":"required",
                        "label":"现场联络人联系电话",
                        "type":"text",
                         "width": "275"
                    },
                    {
                        "name":"incident",
                        "rule":"required",
                        "label":"事件简要经过",
                        "type":"textarea",
                        "width":715,
                    },
                    {
                        "name":"eventType",
                        "label":"事件类型",
                        "type":"select",
                        "dictCode":"event_type",
                        "width": "275"
                    },
                    {
                        "name":"eventLevel",
                        "label":"响应级别",
                        "type":"select",
                        "dictCode":"event_level",
                        "width": "275"
                    },
                    {
                        "name":"eventNature",
                        "label":"事件性质",
                        "type":"text",
                         "width": "275"
                    },
                    {
                        "name":"responsibleUnit",
                        "label":"责任单位",
                        "type":"text",
                        "width": "275",
                        'disabledOn':"disabled"
                    },
                    {
                        "name":"eventDetailAddress",
                        "label":"事件详细地址",
                        "type":"text",
                        "width":715,
                    },
                    {
                        "name":"economicLoss",
                        "label":"直接经济损失(万元)",
                        "type":"text",
                        "width": "275",
                        "rule":{decimal:2},
                    },
                    {
                        "name":"deathNum",
                        "label":"死亡(含失踪)(人)",
                        "type":"text",
                        "width": "275",
                        "rule":"numeric",
                    },
                    {
                        "name":"seriouslyInjuredNum",
                        "label":"重伤(中毒)(人)",
                        "type":"text",
                        "width": "275",
                        "rule":"numeric",
                    },
                    {
                        "name":"minorInjuriesNum",
                        "label":"轻伤人数(人)",
                        "type":"text",
                        "width": "275",
                        "rule":"numeric",
                    },
                    {
                        "name":"dangerousSituationNum",
                        "label":"危及生命安全(人)",
                        "type":"text",
                        "width": "275",
                        "rule":"numeric",
                    },
                    {
                        "name":"needToTransferNum",
                        "label":"需紧急转移安置(人)",
                        "type":"text",
                        "width": "275",
                        "rule":"numeric",
                    },
                    {
                        "name":"responsiblePersonId",
                        "label":"责任人",
                        "type":"text",
                         "width": "275"
                    },
                    {
                        "name":"issuerId",
                        "label":"签发人",
                        "type":"text",
                         "width": "275"
                    },
                    {
                        "name":"associatedFax",
                        "label":"关联传真",
                        "type":"text",
                        "width": "275"
                    },
                    {
                        "name":"parentCompanyId",
                        "label":"报送单位",
                        "type":"treeSelect",
                        api:'/ms/sysOrganization/getCompanyTree?isHandleId=0',
                        httpMehod:"GET",
                    },
                    {"name":"photoId","label":"事故照片","type":"uploadPicture","limit": 9999},
                    {"name":"accessoryId","label":"附件","type":"uploadFileAsync",},
                    {"name":"remark","label":"备注","type":"textarea","width":715,},

                ]
            }
        },
        created() {
            this.getUserInfo();
        },
        methods: {
            // 初始化用户用户信息
            getUserInfo() {
                this.$pagexRequest({
                    url: `/ms/sysUser/getUserByCompanyId?companyId=${this.$store.getters.user.companyId}`,
                    method: 'GET',
                    data: this.detailForm,
                }).then((res) => {
                    this.userInfoArr = res
                })
            }
        },
    };
</script>

<style lang="scss" scoped>
::v-deep .el-upload.el-upload--text {
    width: 715px;
    }
::v-deep .el-form-item--small .el-form-item__content .el-date-editor .el-input__prefix {
        left: 245px;
    }
    ::v-deep .vue-treeselect {
        width: 275px;
    }
</style>
