var modelConfig = {
    title: '专业必修课关联', pkey: 'id', type: 'uuid', orderBy: 'update_time Desc',
    namespace: "school_curriculum_major_rela", table: 't_school_curriculum_major_rela',trans: true};
var listPage = {
    listFieldSett: function () {
        return [
            {name: 'major_id', title: '专业', width: '10%', align: 'center',trans:'pagex',key:'school_major',showField:'transMap.majorName'},
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
            {name: 'major_id', type: 'select',showAll:true, title: '专业', type: 'select',url:'${path.basePath}/ms/x/school_major/findListData',valueField:'id',textField:'name'}
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
            {name: 'major_id', title: '专业', required: true, type: 'select',url:'${path.basePath}/ms/x/school_major/findListData',valueField:'id',textField:'name'},
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
                return '此专业已经选择过此课程';
            }
        }
    }
};
