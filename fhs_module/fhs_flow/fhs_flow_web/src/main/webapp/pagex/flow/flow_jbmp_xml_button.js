var modelConfig = {
    title: '自定义按钮',
    pkey: 'button_id',
    type: 'uuid',
    orderBy: 'update_time Desc',
    namespace: "flow_jbmp_xml_button",
    table: 't_flow_jbmp_xml_button',
    trans: true,
    extendsParam: 'xml_id=${param.xml_id}'
};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'task_name', title: '任务名称', width: '10%', align: 'center'},
            {name: 'button_name', title: '按钮名称', width: '10%', align: 'center'},
            {name: 'variables_key', title: '流程变量key', width: '10%', align: 'center'},
            {name: 'variables_val', title: '流程变量值', width: '10%', align: 'center'},
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
        return [{name: 'task_name', type: 'input', title: '任务名称',filterType:'like'}];
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
            {name: 'task_name', type: 'input', title: '任务名称', required: true},
            {name: 'button_name', type: 'input', title: '按钮名称', required: true},
            {name: 'variables_key', type: 'input', title: '流程变量key', required: true},
            {name: 'variables_val', type: 'input', title: '流程变量值', required: true},
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