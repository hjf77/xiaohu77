
<%@ page contentType="text/html; charset=gbk" language="java"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title>�޸�����</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>

<body class="skin-1">
	<!--�޸�����-->
	<div id="resetPasswordlog">
		<form id="resetPasswordForm" method="post">
			<input id="userId" name="userId" type="hidden"
				value="${param.userId}" />
			<div class="fitem">
				<label>ԭ&#8195;��&#8195;��:</label> <input type="password" name="oldPassword"
					datatype="*" nullmsg="����дԭ����" ajaxPost="true"
					ajaxurl="${basePath}adminUser/validataPass" />
			</div>
			<div class="fitem">
				<label>��&#8195;��&#8195;��:</label> <input type="password" id="newPassword"
					name="newPassword" class="inputxt Validform_error" datatype="*6-16"
					nullmsg="����д������" errormsg="���뷶Χ��6~16λ֮�䣡" style="width:200px;" />
			</div>
			<div class="fitem">
				<label>ȷ��������:</label> <input type="password" id="againNewPassword"
					name="againNewPassword" recheck="newPassword"
					class="inputxt Validform_error" datatype="*" nullmsg="���ٴ���д������"
					errormsg="������������˺����벻һ�£�" />
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<center>
			<a href="javascript:void(0)" class="easyui-linkbutton btn btn-suc l-btn l-btn-small"
				onclick="save()">�ύ</a> <a href="javascript:void(0)"
				class="easyui-linkbutton btn btn-can l-btn l-btn-small" onclick="closeDialog();">�ر�</a>
		</center>
	</div>

	<script type="text/javascript">
		var url_temp = "${basePath}adminUser/updatePass";
		var resetPasswordForm = null;
		$(document).ready(function() {
			//form��֤��
            resetPasswordForm = $('#resetPasswordForm').Validform({
				tiptype : 5
			});
			// ����Ǳ༭ҳ�棬�ͳ�ʼ��һЩ����
		})

		function save() {

			$('#resetPasswordForm').form('submit', {
				url : url_temp,
				onSubmit : function() {
					return (resetPasswordForm.check());
				},
				success : function(d) {
					d = $.parseJSON(d);
					if (d && d.result) {
						Ealert("�����ɹ���");
						closeDialog();
						reload();
					} else {
						Ealert('����ʧ��');
					}
				}
			});

		}
	</script>
</body>

</html>

