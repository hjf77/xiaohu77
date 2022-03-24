var modelConfig = {
    title: '成绩管理', pkey: 'id', type: 'uuid', orderBy: 'update_time Desc',
    namespace: "school_curriculum_student_score", table: 't_school_curriculum_student_score',trans: true};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'student_id', title: '学生', width: '10%', align: 'center',trans:'pagex',key:'student',showField:'transMap.studentName'},
            {name: 'curriculum_id', title: '课程', width: '10%', align: 'center',trans:'pagex',key:'school_curriculum',showField:'transMap.curriculumName'},
            {name: 'midterm_score', title: '期中分数', width: '10%', align: 'center'},
            {name: 'terminal_score', title: '期末分数', width: '10%', align: 'center'},
            {name: 'attendance_score', title: '考勤分数', width: '10%', align: 'center'},
            {name: 'work_score', title: '作业分数', width: '10%', align: 'center'},
            {name: 'result_score', title: '总分数', width: '10%', align: 'center'},
            {name: 'status', title: '状态', width: '10%', align: 'center'},
            {name: 'create_time', title: '创建时间', width: '10%', align: 'center'},
            {name:'create_user',title:'创建人',width:'10%',align:'center',trans:'auto',showField:'transMap.create_userUserName'},
            {name: 'update_time', title: '更新时间', width: '10%', align: 'center'},
            {name:'update_user',title:'修改人',width:'10%',align:'center',trans:'auto',showField:'transMap.update_userUserName'},
        ]
    },
    isColumnButton: function () {
        return false;
    },
    filters: function () {
        return [
            {name: 'curriculum_id', title: '课程', showAll: true, type: 'select',url:'${path.basePath}/ms/x/school_curriculum/findListData',valueField:'id',textField:'name'}
        ];
    },
    buttons: function () {
        return [{title:'审批',fun:'apply',isRow:true,permissionsCode:'school_curriculum_student_score:apply'},];
    },
    disableButtons: function () {
        return ['export','view'];
    },
    otherFunctions: function () {
        return {
            apply:function(_row){
                if(_row.status!='待审批'){
                    EalertE('审批过不能重复审批');
                    return;
                }
                $.ajax({
                    url:  "/ms/student/apply?id=" + _row.id,
                    type: "GET",
                    success: function (data) {
                        Ealert('审批成功');
                        reload();
                    }
                });

            }
        }
    }
};

var add = {
    formFields: function () {
        return [
            {name: 'student_id', title: '学生', required: true, type: 'select',url:'${path.basePath}/ms/student/list',valueField:'userId',textField:'userName'},
            {name: 'curriculum_id', title: '课程', required: true, type: 'select',url:'${path.basePath}/ms/x/school_curriculum/findListData',valueField:'id',textField:'name'},
            {name: 'midterm_score', title: '期中分数', required: true, type: 'input',dataType:'n'},
            {name: 'terminal_score', title: '期末分数', required: true, type: 'input',dataType:'n'},
            {name: 'attendance_score', title: '考勤分数', required: true, type: 'input',dataType:'n'},
            {name: 'work_score', title: '作业分数', required: true, type: 'input',dataType:'n'},
            {name: 'result_score', title: '总分数', required: true, type: 'input',dataType:'n'},
            {name: 'status', title: '状态',value:'待审批', type: 'hide'}
        ];
    },
    otherFunctions: function () {
        return {
            ready: function () {
                $('#curriculumId').combobox('setValue', '${session.user.curriculumId}');
                $('#curriculumId').combobox({readonly:true});
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
                return '此学生已经录入分数';
            }
        }
    }
};
