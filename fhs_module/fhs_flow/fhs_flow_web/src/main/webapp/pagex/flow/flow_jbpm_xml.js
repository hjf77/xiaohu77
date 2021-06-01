var modelConfig= {title:'流程列表',pkey:'id',type:'uuid',
    orderBy:'update_time Desc',namespace:"flow_jbpm_xml",table:'t_flow_jbpm_xml',trans:true,
    saveUrl:'${path.basePath}/ms/flow_jbpm_xml/addFlow'};
var listPage={
    listFieldSett:function(){
        return [
            {name:'name',title:'流程名称',width:'15%',align:'center'},
            {name:'process_key',title:'流程key',width:'15%',align:'center'},
            {name:'server',title:'表单在哪个服务器上',width:'15%',align:'center',trans:'auto',key:'sett_ms_system',showField:'transMap.serverName'},
            {name:'is_pagex',title:'表单是否是pagex实现',width:'14%',align:'center',trans:'book',key:'yesOrNo',showField:'transMap.is_pagexName'},
            {name:'status',title:'流程状态',width:'10%',trans:'book',key:'flow_status',showField:'transMap.statusName',align:'center'},
            {name:'create_user',title:'创建人',width:'15%',align:'center',trans:'user',showField:'transMap.create_userUserName'},
            {name:'create_time',title:'创建时间',width:'15%',align:'center'},
        ]},
    isColumnButton:function(){
        return  true;
    },
    filters:function(){
        return [
            {name:'status',title:'流程状态',type:'book',code:'flow_status'},
            {name:'name',title:'流程名称',type:'input',filterType:'like'},
        ];
    },
    buttons:function(){
        return [
            {title:'编辑流程实例',permissionsCode:'flow_jbpm_xml:add',isRow:true,id:'editXml'},
            {title:'发布流程',fun:'publishFlow',permissionsCode:'flow_jbpm_xml:add',isRow:true},
            {title:'自定义按钮',fun:'buttons_manager',permissionsCode:'flow_jbpm_xml:add',isRow:true},
            {title:'自定义表单',fun:'form_manager',permissionsCode:'flow_jbpm_xml:add',isRow:true},
        ];
    },
    disableButtons:function(){
        return [];
    },
    otherFunctions:function(){
        return {
            onListPageReady:function(){
                $('#listGrid').datagrid({
                    onClickRow:function( rowIndex, rowData){
                        console.log(rowData);
                        $('#editXml').attr('href',"${path.basePath}/b/page-work_flow/index?xmlId=" + rowData.id  + "&processKey="+rowData.processKey+"&processName="+rowData.name);
                        $('#editXml').attr('target','_blank');
                    }
                });
            },
            buttons_manager:function(row){
                var _openMarketMsg = {
                    url: '/ms/pagex/flow_jbmp_xml_button_list.jsp?xml_id=' + row.id,
                    title: row.name + '的自定义按钮'
                }
                top.postMessage(_openMarketMsg, '*');
            },
            form_manager:function(row){
                var _openMarketMsg = {
                    url: '/ms/pagex/flow_jbmp_auth_form_list.jsp?xml_id=' + row.id,
                    title: row.name + '的自定义按钮'
                }
                top.postMessage(_openMarketMsg, '*');
            },

            publishFlow:function (row) {
                if(row.status==1){
                    EalertE("流程已发布过了");
                    return;
                }
                swal({
                    title: '发布提示 ',
                    text: "确定发布吗?",
                    type: 'info',   //感叹号图标
                    showCancelButton: true,   //显示取消按钮
                    confirmButtonText: "是的，我要发布",
                    confirmButtonColor: "#ec6c62",
                    cancelButtonText: '取消',
                }, function () {
                    $.ajax({
                        url:'${path.basePath}/ms/flow_jbpm_xml/releaseWorkFlow',
                        dataType:'json',
                        data:{xmlId:row.id},
                        success:function(res){
                            if(res.code==200){
                                Ealert("发布成功");
                                reload();
                            }else{
                                EalertE("发布失败,请查看后台日志")
                            }
                        }
                    })
                });
            }
        }
    }
};
var add={
    formFields:function(){
        return [
            {name:'name',title:'流程名称',type:'input',required:true},
            {name:'process_key',title:'流程key',type:'input',required:true},
            {name:'server',title:'表单在哪个服务器上',type:'select',url: '${path.fhs_basics_url}ms/sett_ms_menu_server/getMenuServersJsonp?jsonpCallback=?',valuefield:'id',textfield:'serverName',required:true},
            {name:'is_pagex',title:'表单是否是pagex实现',type:'book',code:'yesOrNo',onSelect:'showUrlOrNamespace',dftIndex:1},
            {type:'divStart',id:'uriInput',style:'display:none;'},
            {name:'uri',title:'uri',type:'input'},
            {type:'divEnd'},
            {type:'divStart',id:'namespaceInput'},
            {name:'namespace',title:'namespace',type:'input'},
            {type:'divEnd'},
            {name:'xml',title:'xml',type:'hide'},
            {name:'version',title:'version',type:'hide'}
        ];
    },
    otherFunctions:function(){
        return {
            ready:function(){
            },
            loadSuccess:function(info){
            },
            onSave:function(){
            },
            saveSucess:function(){
            },
            saveError:function(){
            },
            showUrlOrNamespace:function(record){
                if(record.wordbookCode == 1){
                    $("#uriInput").hide();
                    $("#namespaceInput").show();
                }else{
                    $("#namespaceInput").hide();
                    $("#uriInput").show();
                }

            }
        }
    }
}