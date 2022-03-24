var modelConfig = {
    title: '我的学生', pkey: 'user_id', type: 'uuid', orderBy: 'update_time Desc',extendsParam:'type=student',
    namespace: "my_student", table: 't_ucenter_ms_user',trans: true,dataGridUrl:'${path.basePath}/ms/student/pager'};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'user_name', title: '姓名', width: '20%', align: 'center'},
            {name: 'num', title: '学号', width: '10%', align: 'center'},
            {name: 'sex', title: '性别', width: '10%', align: 'center',key:'sex',trans:'book',showField:'transMap.sexName'},
            {name: 'birthday', title: '生日', width: '10%', align: 'center'},
            {name: 'classes_id', title: '班级', width: '10%', align: 'center',trans:'pagex',key:'school_classes',showField:'transMap.classesName'},
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
            {name: 'user_name', type: 'input', title: '姓名', filterType: 'like'},
            {name: 'num', type: 'input', title: '学号', filterType: '='}
        ];
    },
    buttons: function () {
        return [ ];
    },
    disableButtons: function () {
        return ['export','view','delete','add','update'];
    },
    otherFunctions: function () {
        return {

        }
    }
};

var add = {
    formFields: function () {
        return [
            {name: 'user_name', title: '姓名', required: true, type: 'input'}
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

            },
            getConflictMsg:function(){

            }
        }
    }
};
