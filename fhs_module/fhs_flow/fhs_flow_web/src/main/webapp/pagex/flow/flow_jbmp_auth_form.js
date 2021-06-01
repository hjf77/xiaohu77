var modelConfig = {
    title: '自定义按钮',
    pkey: 'auth_form_id',
    type: 'uuid',
    orderBy: 'update_time Desc',
    namespace: "flow_jbmp_auth_form",
    table: 't_flow_jbmp_auth_form',
    trans: true,
    extendsParam: 'xml_id=${param.xml_id}'
};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'form_name', title: '表单名称', width: '10%', align: 'center'},
            {name: 'task_key', title: '流程任务key', width: '10%', align: 'center'},
            {name: 'form_url', title: '表单url', width: '10%', align: 'center'},
            {name: 'create_user', title: '创建人', width: '10%', align: 'center', trans: 'auto', showField: 'transMap.create_userUserName'},
            {name: 'create_time', title: '创建时间', width: '10%', align: 'center'},
            {name: 'update_user', title: '更新人', width: '10%', align: 'center', trans: 'auto', showField: 'transMap.update_userUserName'},
            {name: 'update_time', title: '更新时间', width: '10%', align: 'center'}
        ]
    },
    isColumnButton: function () {
        return true;
    },
    filters: function () {
        return [];
    },
    buttons: function () {
        return [];
    },
    disableButtons: function () {
        return [];
    },
    otherFunctions: function () {
        return {
            onListPageReady: function () {

            }
        }
    }
};

var add = {
    formFields: function () {
        return [
            {name: 'form_name', type: 'input', title: '表单名称', required: true},
            {name: 'task_key', type: 'input', title: '任务key', required: true},
            {name: 'form_url', type: 'input', title: '表单url', required: true},
            {name: 'xml_id', type: 'hide'}
        ];
    },
    otherFunctions: function () {
        return {
            ready: function () {
                $('#xmlId').val('${param.xml_id}');
            },
            loadSuccess: function (info) {

            },
            onSave: function () {
            },
            saveSuccess: function () {
            },
            saveError: function () {

            },
        }
    }
}