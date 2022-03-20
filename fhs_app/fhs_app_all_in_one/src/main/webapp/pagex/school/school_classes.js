var modelConfig = {
    title: '班级管理', pkey: 'id', type: 'uuid', orderBy: 'update_time Desc',
    namespace: "school_classes", table: 't_school_classes',trans: true};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'name', title: '班级名称', width: '20%', align: 'center'},
            {name: 'code', title: '编号', width: '10%', align: 'center'},
            {name: 'major_id', title: '所属专业', width: '10%', align: 'center',trans:'pagex',key:'school_major',showField:'transMap.majorName'},
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
            {name: 'name', type: 'input', title: '班级名称', filterType: 'like'}
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
            {name: 'name', title: '班级名称', required: true, type: 'input'},
            {name: 'code', title: '编号', required: true, type: 'input'},
            {name: 'major_id', title: '所属专业', required: true, type: 'select',url:'${path.basePath}/ms/x/school_major/findListData',valueField:'id',textField:'name'}
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
