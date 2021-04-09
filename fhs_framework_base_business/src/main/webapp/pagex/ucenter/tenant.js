var modelConfig = {
    title: '租户管理', pkey: 'id', type: 'uuid',
    orderBy: 'update_time Desc',
    namespace: "tenant", table: 't_ucenter_ms_tenant', trans: false,
    saveUrl:'${path.basePath}/ms/tenant/add',
    updateUrl:'${path.basePath}/ms/tenant/updateTenant?id='
};

var listPage = {
    listFieldSett: function () {
        return [
            {name: 'tenant_name', title: '租户名称', width: '10%', align: 'center'},
            {name: 'contacts', title: '联系人', width: '10%', align: 'center'},
            {name: 'mobile', title: '电话', width: '10%', align: 'center'},
            {name: 'email', title: '邮箱', width: '10%', align: 'center'},
            {name: 'remark', title: '备注', width: '58%', align: 'center'},
            {name: 'group_code',title:'集团编码',hidden:true},
        ]
    },
    isColumnButton: function () {
        return true;
    },
    filters: function () {
        return [
            {name: 'tenant_name', title: '租户名称', type: 'input', filterType: 'like'},
        ];
    },
    buttons: function () {
        return [{title: '重置管理员密码', fun: 'resetAdminPass', permissionsCode: 'parking:see', isRow: true},];
    },
    disableButtons: function () {
        return ['delete'];
    },
    otherFunctions: function () {
        return {
            resetAdminPass:function(_row){
                debugger;
                $.ajax({
                    url:'${path.basePath}/ms/tenant/resetAdminPass?groupCode=' + _row.groupCode,
                    dataType:'json',
                    success:function(_rep){
                        if(_rep.code==200)
                        {
                            //swal("密码已经修改为:" + _rep.data);
                            $.messager.show({
                                title:'密码修改成功',
                                msg:'新密码:' + _rep.data,
                                showType:'fade',
                                width:500,
                                height:500,
                                style:{
                                    right:'',
                                    bottom:''
                                }
                            });
                        }
                        else{
                            EalertE(_rep.message);
                        }
                    }
                })
            }
        }
    }
};
var add = {
    formFields: function () {
        return [
            {name: 'tenant_name', title: '租户名称', type: 'input', required: true},
            {name: 'group_code', title: '租户编码', type: 'input', required: true},
            {name: 'contacts', title: '联系人', type: 'input', required: true},
            {name: 'mobile', title: '电话', type: 'input',dataType:'tel|tel_p'},
            {name: 'system_ids', title: '子系统', type: 'select',required:true,valueField:'id',
                textField:'text',url:' ${path.basePath}/ms/sysSystem/getSystemComBoxData',multiple:true},
            {name:'email',title:'邮箱',type:'bigInput',dataType:'e'},
            {name:'logo',title:'图片',type:'up',placeholder:'请上传图片'},
            {name:'remark',title:'备注',type:'text'},
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
            getConflictMsg:function(){
                return '租户编码不可重复';
            },
            saveSucess: function () {
            },
            saveError: function () {

            },
        }
    }
}
