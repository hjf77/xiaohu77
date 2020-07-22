var modelConfig = {
    title: '登录日志统计', pkey: 'log_id', type: 'uuid',
    exjs:'${path.basePath}/js/My97DatePicker/WdatePicker.js',
    dataGridUrl: '${path.basePath}/ms/logLogin/getloginIogSummary',
    namespace: "log_login_summary", table: 't_log_login',trans: true};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'visit_number', title: '用户姓名', width: '5%', align: 'center'},
            {name: 'login_name', title: '登录名称', width: '10%', align: 'center'},
            {name: 'ip_info', title: '数量', width: '10%', align: 'center'},
        ]
    },
    isColumnButton: function () {
        return false;
    },
    filters: function () {
        return [
            {name: 'create_time', type: 'dateBT', title: '起止日期',isBT: true},
        ];
    },
    buttons: function () {
        return [
        ];
    },
    disableButtons: function () {
        return ["add", "update", "delete", "view", "export" ];
    },
    otherFunctions: function () {
        return {
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