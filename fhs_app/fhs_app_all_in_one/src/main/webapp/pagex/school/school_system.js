var modelConfig = {
    title: '系管理', pkey: 'id', type: 'uuid', orderBy: 'update_time Desc',
    namespace: "school_system", table: 't_school_system',trans: true,joinColumns:'{"name":"systemName"}'};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'name', title: '系名称', width: '20%', align: 'center'},
            {name: 'code', title: '编号', width: '10%', align: 'center'},
            {name: 'college_id', title: '所属学院', width: '10%', align: 'center',trans:'pagex',key:'school_college',showField:'transMap.collegeName'},
            {name: 'create_time', title: '创建时间', width: '15%', align: 'center'},
            {name:'create_user',title:'创建人',width:'10%',align:'center',trans:'auto',showField:'transMap.create_userUserName'},
            {name: 'update_time', title: '更新时间', width: '14%', align: 'center'},
            {name:'update_user',title:'修改人',width:'10%',align:'center',trans:'auto',showField:'transMap.update_userUserName'},
        ]
    },
    isColumnButton: function () {
        return false;
    },
    filters: function () {
        return [
            {name: 'name', type: 'input', title: '系名称', filterType: 'like'}
        ];
    },
    buttons: function () {
        return [];
    },
    disableButtons: function () {
        return [];
    },
    otherFunctions: function () {
        return {}
    }
};

var add = {
    formFields: function () {
        return [
            {name: 'name', title: '系名称', required: true, type: 'input'},
            {name: 'code', title: '编号', required: true, type: 'input'},
            {name: 'college_id', title: '所属学院', required: true, type: 'select',url:'${path.basePath}/ms/x/school_college/findListData',valueField:'id',textField:'name'}
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
