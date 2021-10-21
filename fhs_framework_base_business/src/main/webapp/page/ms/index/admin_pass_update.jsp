<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="hfs" uri="http://www.ylmo2o.com/tag/hfs"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${param.isFrame eq true}">
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/page/ms/common/js_css_include.jsp"></jsp:include>
	</head>
	<body class="skin-1">
</c:if>

<jsp:include page="/page/ms/common/form_include.jsp"></jsp:include>
	<!--修改密码-->
	<div id="ResetPasswordlog">
		<form id="ResetPasswordForm" method="post" style="text-align: center">
			<div class="fitem">
				<label>原&#8195;密&#8195;码:</label> <input type="password" oncopy="return false" onpaste="return false" oncut="return false" oncontextmenu="return false" name="oldPassword"
					datatype="*" nullmsg="请填写原密码" />
			</div>
			<div class="fitem">
				<label>新&#8195;密&#8195;码:</label> <input type="password" id="newPassword" oncopy="return false" onpaste="return false" oncut="return false" oncontextmenu="return false"
					name="newPassword" class="inputxt Validform_error" datatype="*6-100"
					nullmsg="请填写新密码" errormsg="密码范围在6~100位之间！" />
			</div>
			<div class="fitem">
				<label>确认新密码:</label> <input type="password" id="againNewPassword" oncopy="return false" onpaste="return false" oncut="return false" oncontextmenu="return false"
					name="againNewPassword" recheck="newPassword"
					class="inputxt Validform_error" datatype="*" nullmsg="请再次填写新密码"
					errormsg="您两次输入的账号密码不一致！" />
				<input id="password" name="password" hidden="hidden">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<center>
			<a href="javascript:void(0)" class="easyui-linkbutton btn btn-suc l-btn l-btn-small"
				onclick="save()">提交</a> <a href="javascript:void(0)"
				class="easyui-linkbutton btn btn-can l-btn l-btn-small" onclick="top.closeDialog();;">关闭</a>
		</center>
	</div>

<script type="text/javascript" src="${staticPath}js/jQuery.md5.js"></script>
<script type="text/javascript">
	var url_temp = "${basePath}ms/sysUser/updatePass";
	var ResetPasswordForm = null;
	$(document).ready(function() {
		//form验证。
		ResetPasswordForm = $('#ResetPasswordForm').Validform({
			tiptype : 5
		});
		// 如果是编辑页面，就初始化一些数据
	});

	function save() {
        $("#password").val($.md5($("input[name='oldPassword']").val()));
        $("#newPassword").val($.md5($("#newPassword").val()));
        $("#againNewPassword").val($.md5($("#againNewPassword").val()));
		$('#ResetPasswordForm').form('submit', {
			url : url_temp,
			onSubmit : function() {
				return (ResetPasswordForm.check());
			},
			success : function(d) {
				d = $.parseJSON($.parseJSON(d));
				if (d.result) {
					Ealert('修改成功');
                    setTimeout(function(){top.closeDialog()},2000);
				} else {
                    $("#password").val('');
                    $("input[name='oldPassword']").val('');
                    $("#newPassword").val('');
                    $("#againNewPassword").val('');
					EalertE('修改失败，可能是原密码不正确');
				}
			}
		});

	}
</script>
<c:if test="${not empty  param.isFrame}">
	</body>
	</html>
</c:if>