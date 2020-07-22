var modelConfig = {
    title: '报表', pkey: 'log_id', type: 'uuid', orderBy: 'update_time Desc',
    exjs:'${path.basePath}/js/My97DatePicker/WdatePicker.js',
    namespace: "log_report", table: 't_log_operator_main',trans: true,
    dataGridUrl: '${path.basePath}/ms/logOperatorMain/getAccessManyList'};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'model',id:'model', title: '模块名', width: '25%', align: 'center'},
            {name: 'reqMethod',id:'reqMethod', title: '请求类型', width: '25%', align: 'center'},
            {name: 'url', id:'url',title: '请求url', width: '25%', align: 'center'},
            {name: 'visits',id:'visits', title: '访问次数', width: '25%', align: 'center'},
        ]
    },
    isColumnButton: function () {
        return false;
    },
    filters: function () {
        return [
            {name: 'create_time', type: 'dateBT', title: '调用时间',isBT: true}
        ];
    },
    buttons: function () {
        return [
        ];
    },
    disableButtons: function () {
        return ["add", "update", "delete", "view", "export"];
    },
    otherFunctions: function () {
        return {
            reload: function (row) {
                var startTime = $('#createTime_start_F').val();
                var endTime = $('#createTime_end_F').val();
                $('#listGrid').datagrid('load', '${path.basePath}/ms/logOperatorMain/getAccessManyList?startTime='+ startTime+'&endTime='+endTime);
            }
        }
    }
};
var add = {
    formFields: function () {
        return [

        ];
    },
    otherFunctions: function () {
        return {
            ready: function () {
            },
            loadSuccess: function (info) {
            },
            onSave: function () {

            },
            saveSucess: function () {
            },
            saveError: function () {
            }
        }
    }
};