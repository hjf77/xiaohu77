var modelConfig = {
    title: '必修课学生关联', pkey: 'id', type: 'uuid', orderBy: 'update_time Desc',
    namespace: "school_curriculum_student_rela", table: 't_school_curriculum_student_rela',trans: true};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'student_id', title: '学生', width: '10%', align: 'center',trans:'pagex',key:'student',showField:'transMap.studentName'},
            {name: 'curriculum_id', title: '课程', width: '10%', align: 'center',trans:'pagex',key:'school_curriculum',showField:'transMap.curriculumName'},
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
            {name: 'student_id', type: 'select',showAll:true, title: '学生', type: 'select',url:'${path.basePath}/ms/x/teacher/findListData?type=student',valueField:'userId',textField:'userName'}
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
            {name: 'student_id', title: '学生', required: true, type: 'select',url:'${path.basePath}/ms/x/teacher/findListData?type=student',valueField:'userId',textField:'userName'},
            {name: 'curriculum_id', title: '课程', required: true, type: 'select',url:'${path.basePath}/ms/x/school_curriculum/findListData',valueField:'id',textField:'name'}
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
                return '此学生已经选择过此课程';
            }
        }
    }
};
