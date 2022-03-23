var modelConfig = {
    title: '教师管理', pkey: 'user_id', type: 'uuid', orderBy: 'update_time Desc',extendsParam:'type=teacher',
    namespace: "teacher", table: 't_ucenter_ms_user',trans: true,exjs:'${path.fhs_static_url}/js/jQuery.md5.js',joinColumns:'{"userName":"teacherName"}'};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'user_name', title: '姓名', width: '20%', align: 'center'},
            {name: 'num', title: '教师号', width: '10%', align: 'center'},
            {name: 'curriculum_id', title: '课程', width: '10%', align: 'center',trans:'pagex',key:'school_curriculum',showField:'transMap.curriculumName'},
            {name: 'sex', title: '性别', width: '10%', align: 'center',key:'sex',trans:'book',showField:'transMap.sexName'},
            {name: 'title', title: '职称', width: '10%', align: 'center'},
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
            {name: 'user_name', type: 'input', title: '姓名', filterType: 'like'}
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
            {name: 'user_name', title: '姓名', required: true, type: 'input'},
            {name: 'num', title: '教师号',  type: 'input', required: true},
            {name:'title',title:'职称',type:'input', required: true},
            {name: 'sex', title: '性别', type:'book',code:'sex', required: true},
            {name: 'user_login_name', title: '登录名',  type: 'input', required: true},
            {name: 'password', title: '密码',  type: 'password', required: true},
            {name: 'type', title: '密码',  type: 'hide', value:'teacher'},
            {name: 'classes_id', title: '班级', required: true, multiple:true,type: 'select',url:'${path.basePath}/ms/x/school_classes/findListData',valueField:'id',textField:'name'},
            {name: 'curriculum_id', title: '课程', required: true, type: 'select',url:'${path.basePath}/ms/x/school_curriculum/findListData',valueField:'id',textField:'name'}
        ];
    },
    otherFunctions: function () {
        return {
            ready: function () {
            },
            loadSuccess: function (info) {
                $("input[name='password']").val('default');
                $("input[name='password']").attr('passval',info.password);
                $('#userLoginName').attr("readonly","readonly");
            },
            onSave: function () {
                if($("input[name='password']").val() !='default'){
                    $("input[name='password']").val($.md5($("input[name='password']").val()))
                }else{
                    $("input[name='password']").val($("input[name='password']").attr('passval'));
                }
            },
            saveSucess: function () {

            },
            saveError: function () {

            },
            getConflictMsg:function(){
                return '登录名不可重复';
            }
        }
    }
};
