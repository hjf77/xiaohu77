var modelConfig = {
    title: '日志管理', pkey: 'log_id', type: 'uuid', orderBy: 'update_time Desc',
    namespace: "log_operator_main", table: 't_log_operator_main',trans: true};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'model', title: '模块', width: '13%', align: 'center'},
            {name: 'namespace', title: 'namespace', width: '12%', align: 'center'},
            {name: 'type', title: '操作类型', width: '12%', align: 'center',key:'type',trans:'book',showField:'transMap.typeName'},
            {name: 'url', title: '请求url', width: '13%', align: 'center'},
            {name: 'req_param', title: '入参', width: '13%', align: 'center'},
            {name: 'state', title: '状态', width: '11%', align: 'center',key:'state',trans:'book',showField:'transMap.stateName'},
            {name:'create_user',title:'操作人',width:'11%',align:'center',trans:'auto',showField:'transMap.create_userUserName'},
            {name: 'create_time', title: '操作时间', width: '13%', align: 'center'},
        ]
    },
    isColumnButton: function () {
        return false;
    },
    filters: function () {
        return [
            {name: 'model', type: 'select',url:'${path.basePath}/ms/logOperatorMain/moduleSelect',
                    valuefield:'logId',textfield:'model',showAll:'true',filterType:'like',title: '用户管理'},
            {name: 'log_id', type: 'input', title: 'id',filterType: 'like'},
            {name: 'type', type: 'book', title: '操作类型',code:'type',showAll:'true'},
            {name: 'create_user', type: 'select', title: '操作人',url:'${path.basePath}/ms/logOperatorMain/getUserList',
                valuefield:'userId',textfield:'userName',showAll:'true',filterType:'like'},
            {name: 'create_time', type: 'dateBT', title: '起止日期',isBT: true},
        ];
    },
    buttons: function () {
        return [
             {title: '详情', fun: 'details', permissionsCode: 'log_operator_main:see', isRow: true},
        ];
    },
    disableButtons: function () {
        return ["add", "update", "delete", "view", "export"];
    },
    otherFunctions: function () {
        return {details: function (row) {
                openDialog('${path.basePath}/b/page-ms-logger/logDetails?isView=true&logId='+row.id,
                    '详情');
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