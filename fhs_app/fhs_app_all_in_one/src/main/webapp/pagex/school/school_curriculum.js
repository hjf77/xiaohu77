var modelConfig = {
    title: '课程管理', pkey: 'id', type: 'uuid', orderBy: 'update_time Desc',
    namespace: "school_curriculum", table: 't_school_curriculum',trans: true,joinColumns:'{"name":"curriculumName"}'};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'name', title: '课程名称', width: '20%', align: 'center'},
            {name: 'code', title: '编号', width: '10%', align: 'center'},
            {name: 'score', title: '学分', width: '10%', align: 'center'},
            {name: 'teacher_id', title: '代课教师', width: '10%', align: 'center',trans:'pagex',key:'teacher',showField:'transMap.teacherName'},
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
            {name: 'name', type: 'input', title: '课程名称', filterType: 'like'}
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
            {name: 'name', title: '课程名称', required: true, type: 'input'},
            {name: 'code', title: '编号', required: true, type: 'input'},
            {name: 'score', title: '学分', required: true,dataType:'n',type: 'input'},
            {name: 'teacher_id', title: '代课教师', required: true, type: 'select',url:'${path.basePath}/ms/x/teacher/findListData?type=teacher',valueField:'userId',textField:'userName'}
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
