<template>
    <div>
        <pagex-form
            :isEdit="isEdit"
            :addApi="addApi"
            :updateApi="updateApi"
            :init="init"
            :data="formData"
            :controls="controls"
            :buttons="buttons"
        >
        </pagex-form>
    </div>
</template>

<script>
export default {
    name:'addArchives',
    props: {
        init: Object
    },
    data(){
        return{
            isEdit:'',
            addApi:'',
            updateApi:'',
            formData: {
                groupId: this.init.groupId
            },
            buttons:[
                 {
                name:'保存'
                },
                {
                name:'发布'
                }
            ],
            controls:[
                {
                    type: 'text',
                    name: 'name',
                    label: '应急档案名称',
                    rule:'required',
                    placeholder:'请输入'
                },
                {
                    type: 'prefixText',
                    name: 'number',
                    label: '应急档案编号',
                    rule:'required',
                    placeholder:'请输入',
                    prefixTextContent:'Q/ND-'
                },
                {
                    type: 'select',
                    name: 'types',
                    label: '应急档案类型',
                    multiple: "multiple",
                    rule:'required',
            //         url: "/dsp/user_data_auth/getSecretLevels",
            //         labelField: "label",
            //         valueField: "value",
                    placeholder:'请选择',
                    dictCode: "record_file_type",
                },
                {
                    type: 'treeSelect',
                    name: 'orgIds',
                    label: '适用单位/部门',
                    rule:'required',
                    placeholder:'请选择',
                    api: "/dsp/service_deduction_scheme_category/v1/treeSelect",
                },
                {
                    type: 'text',
                    name: 'recordCompanyId',
                    label: '建档单位',
                    placeholder:'请输入'
                },
                 {
                    type: 'dates',
                    name: 'createdTime',
                    label: '建档时间',
                    placeholder:'请选择'
                },
                {
                    type: 'text',
                    name: 'remark',
                    label: '备注',
                    placeholder:'请输入'
                },
            ]
        }
    }
}
</script>