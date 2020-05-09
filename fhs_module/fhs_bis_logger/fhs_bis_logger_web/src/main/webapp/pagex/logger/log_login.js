var modelConfig = {
    title: '登录日志', pkey: 'log_id', type: 'uuid', orderBy: 'create_time Desc',
    exjs:'${path.basePath}/js/My97DatePicker/WdatePicker.js',
    namespace: "log_login", table: 't_log_login',trans: true};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'visit_number', title: '访问编码', width: '10%', align: 'center'},
            {name: 'login_name', title: '用户名称', width: '10%', align: 'center'},
            {name: 'ip_address', title: '登陆地址', width: '10%', align: 'center'},
            {name: 'ip_info', title: '登陆地点', width: '10%', align: 'center'},
            {name: 'browser', title: '浏览器', width: '10%', align: 'center'},
            {name: 'os', title: '操作系统', width: '10%', align: 'center'},
            {name: 'type', title: '登录状态', width: '10%', align: 'center',key:'log_login_type',trans:'book',showField:'transMap.typeName'},
            {name: 'error_info', title: '错误信息', width: '14%', align: 'center',key:'log_login_error',trans:'book',showField:'transMap.errorInfoName'},
            {name: 'create_time', title: '登录日期', width: '14%', align: 'center'},
        ]
    },
    isColumnButton: function () {
        return false;
    },
    filters: function () {
        return [
            {name: 'ip_address', type: 'input', title: '登录地址',filterType: 'like'},
            {name: 'os', type: 'input', title: '操作系统',filterType: 'like'},
            {name: 'login_name', type: 'input', title: '用户名称',filterType: 'like'},
            {name: 'create_time', type: 'dateBT', title: '起止日期',isBT: true},
        ];
    },
    buttons: function () {
        return [
        ];
    },
    disableButtons: function () {
        return ["add", "update", "delete", "view" ];
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