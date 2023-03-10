<%@ page language="java" contentType="text/html; charset=UTF-8"
		 isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="/page/ms/common/js_css_include.jsp"></jsp:include>
	<style>
	</style>
</head>
<body>

<table id="wordbookListGrid" title="字典列表" class="easyui-datagrid"
	   fit="true" style="width: 100%;"
	   pagination="true"
	   rownumbers="true" fitColumns="true" singleSelect="true" pageSize="10"
	   striped="true" toolbar="#toolbar">
	<thead>
	<tr>
		<th align='center' field="wordbookGroupCode" width="25%">分组编码</th>
		<th align='center' field="wordbookCode" width="25%">字典code</th>
		<th align='center' field="wordbookDesc" width="25%">字典翻译</th>
		<th align='center' field="orderNum" width="23%">排序</th>
	</tr>
	</thead>
</table>


<div id="toolbar">


	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addWordBook()">添加</a>

	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick=" addSelectRowFun('wordbookListGrid',updateWordBook)">修改</a>

	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="pubDel('wordbookListGrid','${basePath}ms/wordbook/delWordbook?wordbookId=','wordbookId')">删除</a>


</div>


<div id="addOrUpdateDialog" class="easyui-dialog" style="width: 500px; height: 450px; padding: 10px 20px"
	 data-options="modal:true" closed="true" buttons="#dlg-buttons">
	<form id="addOrUpdateForm" method="post">
		<input type="hidden" id="wordbookId" name="wordbookId" />

		<div class="fitem">
			<label>字典code:</label>
			<input type="text" id="wordbookCode" name="wordbookCode" datatype="*" nullmsg="请填写字典code"  />
			<span class="form-field-required">*</span>
		</div>
		<div class="fitem">
			<label>字典翻译:</label>
			<input type="text" id="wordbookDesc" name="wordbookDesc" datatype="*" nullmsg="请填写字典翻译"  />
			<span class="form-field-required">*</span>
		</div>
		<div class="fitem">
			<label>翻译英文:</label>
			<input type="text" id="wordbookDescEN" name="wordbookDescEN"  />

		</div>
		<div class="fitem">
			<label>翻译繁体:</label>
			<input type="text" id="wordbookDescTW" name="wordbookDescTW"/>

		</div>
		<div class="fitem">
			<label>排序序号(ASC):</label>
			<input type="text" id="orderNum" name="orderNum" datatype="n" nullmsg="排序序号"  />
			<span class="form-field-required">*</span>
		</div>

	</form>
</div>
<div id="dlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeDialog('addOrUpdateDialog')">关闭</a>
</div>




</body>

<script type="text/javascript">

    var final_wordbookGroupCode = undefined;
    $(function(){
        taskForm  = $('#addOrUpdateForm').Validform({
            tiptype:5
        });
    })

    var addOrUpdateDialogName = null;

    function openDialog() {
        removeValidate('addOrUpdateForm');
        $('#addOrUpdateDialog').dialog('open').dialog('setTitle', addOrUpdateDialogName);

    }

    // 重新加载列表数据
    function reload() {
        $('#wordbookListGrid').datagrid('reload', {
            wordbookGroupCode : final_wordbookGroupCode
        });
    }

    // 修改
    function updateWordBook(row) {
        $('#addOrUpdateForm').form('clear');
        addOrUpdateDialogName = '修改字典';
        addOrUpdateUrl = '${basePath}ms/wordbook/updateWordbook?wordbookGroupCode='  + final_wordbookGroupCode;
        //初始化表单
        $('#addOrUpdateForm').form('load','${basePath}ms/wordbook/getWordbookBean?wordbookId='+row.wordbookId);
        openDialog();
    }


    //添加
    function addWordBook() {
        if(final_wordbookGroupCode == null || final_wordbookGroupCode == undefined){
            Ealert('请选择字典类型');
            return false;
        }
        addOrUpdateDialogName = '添加字典';
        addOrUpdateUrl = '${basePath}ms/wordbook/addWordbook?wordbookGroupCode=' + final_wordbookGroupCode;
        $('#addOrUpdateForm').form('clear');
        openDialog();
    }


    function save(){
        $('#addOrUpdateForm').form('submit',{
            url : addOrUpdateUrl,
            onSubmit: function(){
                if(!taskForm.check()){
                    return false;
                };
            },
            success:function(data){
                data = eval('('+data+')');
                if(data.code == 200)
                {
                    Ealert('操作成功');
                    reload();
                }
                else
                {
                    Ealert('操作失败');
                }
                closeDialog('addOrUpdateDialog');
            }
        });
    }
    // 点击字典类型时的操作
    function getWordbookList(wordbookGroupCode){
        final_wordbookGroupCode = wordbookGroupCode;
        $('#wordbookListGrid').datagrid({
            url : '${systemServiceUrl}ms/wordbook/findWordbookForPage',
            queryParams: {
                wordbookGroupCode: wordbookGroupCode
            }
        });
    }
    // 字典类型页面查询后，字典清空
    function clearWordbookGrid(){
        $('#wordbookListGrid').datagrid('clear');
        final_wordbookGroupCode = undefined;
    }

</script>
</html>
