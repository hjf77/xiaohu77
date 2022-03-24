var modelConfig = {
    title: '选修课成绩', pkey: 'id', type: 'uuid', orderBy: 'update_time Desc',extendsParam:'student_id=${param.userId}',
    namespace: "school_elective_courses", table: 't_school_elective_courses',trans: true};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'student_id', title: '学生名称', width: '10%', align: 'center',trans:'pagex',key:'student',showField:'transMap.studentName'},
            {name: 'name', title: '课程名称', width: '20%', align: 'center'},
            {name: 'score', title: '分数', width: '10%', align: 'center'},
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
            {name: 'name', title: '课程', required: true, type: 'select',url:'${path.basePath}/ms/student/electiveCourses?studentId=${param.student_id}',valueField:'name',textField:'name'},
            {name: 'score', title: '分数', required: true,dataType:'n',type: 'input'},
            {name:'student_id',type:'hide'},
        ];
    },
    otherFunctions: function () {
        return {
            ready: function () {
                if('${param.isAdd}'=='true')
                {
                    if('${param.student_id}'!='')
                    {
                        $('#studentId').val('${param.student_id}');
                    }

                }
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
