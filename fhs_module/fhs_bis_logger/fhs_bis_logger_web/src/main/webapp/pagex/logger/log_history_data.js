var modelConfig = {
    title: '历史数据', pkey: 'log_id', type: 'uuid', orderBy: 'update_time Desc',
    namespace: "log_history_data", table: 't_log_history_data', trans: true,
    dataGridUrl: '${path.basePath}/ms/logOperatorMain/getLogHistoryDataList'
};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'create_time', title: '创建时间', width: '25%', align: 'center'},
            {name: 'version', title: '版本', width: '25%', align: 'center'},
            {name: 'data', title: '数据json', width: '25%', align: 'center'},
            {name: 'create_user', title: '操作人', width: '25%', align: 'center',trans:'auto',showField:'transMap.create_userUserName'},
        ]
    },
    isColumnButton: function () {
        return false;
    },
    filters: function () {
        return [
            {name: 'namespace', type: 'input', title: 'namespace', filterType: 'like'},
            {name: 'pkey', type: 'input', title: '主键', filterType: 'like'},
        ];
    },
    buttons: function () {
        return [
            {title: '详情', fun: 'details', permissionsCode: 'log_operator_main:see', isRow: true}
        ];
    },
    disableButtons: function () {
        return ["add", "update", "delete", "view", "export"];
    },
    otherFunctions: function () {
        return {
            details: function (row) {
                if (row.version != 1) {
                    openDialog('${path.basePath}/b/page-ms-logger/logHistoryModifyData?isView=true&pkey=' + row.pkey + '&version=' + row.version,
                        '详情');
                } else {
                    openDialog('${path.basePath}/b/page-ms-logger/logView?isView=true&pkey=' + row.pkey + '&version=' + row.version,
                        '详情');
                }
            }
        }
    }
};
var add = {
    formFields: function () {
        return [];
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