<%@ page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--老代码后期优化-->
<form id="addUpateRoleForm" method="post">
	<div id="formulaCaclDiv">
		<input type="hidden" name="roleId" id="roleId" value="${param.roleId}" />
			
		<div class="fitem">
			<div class="fitemDiv">
			<label>角色名称:</label> <input type="text" id="roleName"
				value="${roleName}" name="roleName" datatype="*" nullmsg="请填写角色名称"
				ajaxPost="true" ajaxurl="${basePath}ms/sysRole/validataName?isEdit=${param.isEdit}"   >
				<span class="form-field-required">*</span>
			</div>
			<div class="fitemDiv">
				<label>所属机构:</label>
				<input class="easyui-combotree" id="organizationId" name="organizationId" datatype="*" nullmsg="请选择机构" data-options="
					url:'${basePath}ms/sysOrganization/getOrgIdComBoxData',
					method:'post',

					"
				></input>
				<span class="form-field-required">*</span>
			</div>
		</div>

		<div class="fitem">
			<div class="bigLabelDiv">
				<label>菜单权限:</label>
			</div>
			<div class="bigContent">
				<table id="treediv" class="easyui-treegrid"
					style="width: 100%; min-height: 270px;" rownumbers="true"
					data-options="onLoadSuccess:resetChecketBox"
					url="${basePath}ms/sysMenu/findMenuRootTrees" idField="id"
					treeField="name" animate="true" >
					<thead>
						<tr>

							<th  data-options="field:'name',width:200">菜单名称</th>
							<th align='center'
								data-options="field:'readitem',state:'open' ,width:80,formatter:formatRead">读取</th>
							<th align='center'
								data-options="field:'writeitem',state:'open' ,width:80,formatter:formatWrite">写入</th>
							<th align='center'
								data-options="field:'editeitem',state:'open' ,width:80,formatter:formatEdite">修改</th>
							<th align='center'
								data-options="field:'delitem',state:'open' ,width:80,formatter:formatDel">删除</th>
							<th align='center'
								data-options="field:'elseitem',state:'open' ,width:150,formatter:formatElse">其他</th>
							<th align='center'
								data-options="field:'isdir',state:'open' ,width:80,hidden:true">父</th>
							<th align='center'
								data-options="field:'isall',state:'open' ,width:80,hidden:true">有所有</th>
							<th align='center'
								data-options="field:'allitem',state:'open' ,width:80,formatter:formatAll">所有</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		
		<div class="fitem">
			<div class="fitemDiv">
				<label >是否启用:</label> 
				<select id="isDisable" name="isDisable" class="easyui-combobox"
						prompt="状态" nullmsg="请选择"
						data-options="
									valueField: 'wordbookCode',
									textField: 'wordbookDesc',
									editable : false,
									panelHeight: 'auto',
									url: '${systemServiceUrl}webApi/wordbook/getData?wordbookGroupCode=is_disable&jsonpCallback=?',
									onLoadSuccess : function (rec){
										var peo = $(this).combobox('getValue');
										if(peo==''){
											peo = 0;
										}
										$(this).combobox('select', rec[peo].wordbookCode);
									},
								">
				</select>
				<span class="form-field-required">*</span>
			</div>
		</div>

		<c:if test="${permissonDataUrl !=  null}">
			<div class="fitem">
				<div class="bigLabelDiv">
					<label >${permissonDataTitle}:</label>
				</div>
				<div class="bigContent">
					<input type="hidden" name="dataPermissions" id="dataPermissions"/>
					<select id="dataPermissions_select"  dataType="*" nullmsg="请选择${permissonDataTitle}" name="dataPermissions_select" class="easyui-combobox"
							nullmsg="请选择"
							valuefield="id" textfield="parkName"
							data-options="
									url: '${permissonDataUrl}',
									multiple: true,
								">
					</select>
					<span class="form-field-required">*</span>
				</div>
			</div>
		</c:if>
		
		<div class="fitem">
			<div class="bigLabelDiv">
			<label>备注:</label>
			</div>
			<div class="bigLabelDiv">
			<textarea cols="20" rows="5" id="remark" name="remark" value="${remark}"></textarea>
			</div>
		</div>



	</div>
</form>
<script type="text/javascript">
	// 最大的层级是几级
	var maxLevel = 0;

	// key 枝几点level val 是同level的枝节点的id集合
	var fatherLevel = {};

	var allMenuId = {};


	//设置爸爸的menulevel
	function setFatherLevel(rec){
        allMenuId[rec.id]=true;
        var  menuLevel = rec.menuLevel -1;
        if(menuLevel < 0)
		{
		    return;
		}
		if(menuLevel > 0 && menuLevel > maxLevel)
		{
            maxLevel = menuLevel;
		}
        if(!fatherLevel[menuLevel])
		{
            fatherLevel[menuLevel] = {};
		}
		fatherLevel[menuLevel][rec.parentid]=true;
	}

    function formatRead(value, rec) {
        setFatherLevel(rec);
		value = value + "";
		if ((value + "") != "" && value != "null" && value != "undefined") {
			var s = '<span>' + '<input parentId="'+ rec.parentid + '" menuId="' + rec.id + '"   vt="1" id = "r_' + value
					+ '"  type = "checkbox" onclick="validate(\'' + value
					+ '\',\'r_\')" />查询'
			'</span>';
			if (rec.isdir == 0) {
				s = '<span>'
						+ '<input  parentId="'+ rec.parentid + '"  menuId="' + rec.id + '"  id = "r_'
						+ value
						+ '" vt="1" value = "v_'
						+ value
						+ ':1" name = "methods" type = "checkbox" onclick="validate(\''
						+ value + '\',\'r_\')" />查询'
				'</span>';
			}

			return s;
		} else {
			return "";
		}

	}

	function formatWrite(value, rec) {
        setFatherLevel(rec);
		value = value + "";
		if ((value + "") != "" && value != "null" && value != "undefined") {
			var s = '<span>' + '<input parentId="'+ rec.parentid + '"  id = "w_' + value
					+ '" vt="2" type = "checkbox"  menuId="' + rec.id + '" onclick="validate(\'' + value
					+ '\',\'w_\')" />添加'
			'</span>';
			if (rec.isdir == 0) {
				s = '<span>'
						+ '<input parentId="'+ rec.parentid + '"  menuId="' + rec.id + '" id = "w_'
						+ value
						+ '" vt="2" value = "v_'
						+ value
						+ ':2" name = "methods" type = "checkbox" onclick="validate(\''
						+ value + '\',\'w_\')" />添加'
				'</span>';
			}
			return s;
		} else {
			return "";
		}
	}
	function formatEdite(value, rec) {
        setFatherLevel(rec);
		value = value + "";
		if ((value + "") != "" && value != "null" && value != "undefined") {
			var s = '<span>' + '<input  parentId="'+ rec.parentid + '"  menuId="' + rec.id + '" id = "e_' + value
					+ '"  vt="3" type = "checkbox" onclick="validate(\'' + value
					+ '\',\'e_\')" />修改'
			'</span>';
			if (rec.isdir == 0) {
				s = '<span>'
						+ '<input  parentId="'+ rec.parentid + '"  menuId="' + rec.id + '" id = "e_'
						+ value
						+ '" vt="3" value = "v_'
						+ value
						+ ':3" name = "methods" type = "checkbox" onclick="validate(\''
						+ value + '\',\'e_\')" />修改'
				'</span>';
			}
			return s;
		} else {
			return "";
		}

	}
	function formatDel(value, rec) {
        setFatherLevel(rec);
		value = value + "";
		if ((value + "") != "" && value != "null" && value != "undefined") {
			var s = '<span>' + '<input vt="4"  menuId="' + rec.id + '"  parentId="'+ rec.parentid + '"  id = "d_' + value
					+ '"  type = "checkbox" onclick="validate(\'' + value
					+ '\',\'d_\')" />删除'
			'</span>';
			if (rec.isdir == 0) {
				s = '<span>'
						+ '<input  menuId="' + rec.id + '"  parentId="'+ rec.parentid + '" id = "d_'
						+ value
						+ '" vt="4" value = "v_'
						+ value
						+ ':4" name = "methods" type = "checkbox" onclick="validate(\''
						+ value + '\',\'d_\')" />删除'
				'</span>';
			}

			return s;
		} else {
			return "";
		}

	}

	function formatElse(value, rec) {
        setFatherLevel(rec);
		value = value + "";
		if ((value + "") != "" && value != "null" && value != "undefined") {
			if (rec.isdir == 0) {
				var s = '<div tyle="width: 200px;" >';
				$
						.ajax({
							url : "${basePath}ms/sysMenuPermission/findMapListByType",
							async : false,
							type : "POST",
							data : {
								"permissionType" : "5",
								"menuId" : value
							},
							dataType : "json",
							success : function(ds) {
								if (ds && ds.length > 0) {
									$
											.each(
													ds,
													function(i, d) {
														s += '<span style="width: 80px;display: block;"    >'
																+ '<input id = "el_'
																+ d.permissionId
																+ '" nam = "el_'
																+ value
																+ '" permissionId="'
																+ d.permissionId
																+ '"   name = "methods" value = "t_'
																+ value
																+ ':'
																+ d.permissionId
																+ '"  vt="5"  type = "checkbox" onclick="validate_else(\''
																+ value
																+ '\',\'el_\')" />'
																+ d.permissionName
																+ '</span>';
													});
								}
							}
						});
				return s + "</div>";
			} else {
				return "";
			}
		} else {
			return "";
		}

	}

	function formatAll(value, rec) {
		value = value + "";
		if (rec.isdir == 0 && rec.isall == 0) {
			return "";
		}
		if ((value + "") != "" && value != "null" && value != "undefined") {
			var s = '<span>' + '<input id = "a_' + value
					+ '"  type = "checkbox"  onclick="selectAll(\'' + value
					+ '\',this)" />所有'
			'</span>';
			return s;
		} else {
			return "";
		}

	}
	//全选时激发的事件
	function selectAll(val, obj) {
		var items = ($('#treediv').treegrid('getChildren', val));
		if ($(obj).prop("checked")) {
			if ($("#r_" + val).length > 0) {
				$("#r_" + val).prop("checked", $(obj).prop("checked"));
			}
			selectChilds(items, 'r_', true);
			if ($("#w_" + val).length > 0) {
				$("#w_" + val).prop("checked", $(obj).prop("checked"));
			}
			selectChilds(items, 'w_', true);
			if ($("#e_" + val).length > 0) {
				$("#e_" + val).prop("checked", $(obj).prop("checked"));
			}
			selectChilds(items, 'e_', true);
			if ($("#d_" + val).length > 0) {
				$("#d_" + val).prop("checked", $(obj).prop("checked"));
			}
			selectChilds(items, 'd_', true);
			if ($("input[nam=el_" + val + "]").length > 0) {
				$("input[nam=el_" + val + "]").prop("checked",
						$(obj).prop("checked"));
			}
			selectChilds(items, 'el_', true);
			validate_v(val, 'r_', true);
			validate_v(val, 'w_', true);
			validate_v(val, 'e_', true);
			validate_v(val, 'd_', true);
			validate_v(val, 'el_', true);
			validate_v(val, 'a_', true);
		} else {
			if ($("#r_" + val).length > 0) {
				$("#r_" + val).prop("checked", false);
			}
			selectChilds(items, 'r_', false);
			if ($("#w_" + val).length > 0) {
				$("#w_" + val).prop("checked", false);
			}
			selectChilds(items, 'w_', false);
			if ($("#e_" + val)) {
				$("#e_" + val).prop("checked", false);
			}
			selectChilds(items, 'e_', false);
			if ($("#d_" + val).length > 0) {
				$("#d_" + val).prop("checked", false);
			}
			selectChilds(items, 'd_', false);
			if ($("input[nam=el_" + val + "]").length > 0) {
				$("input[nam=el_" + val + "]").prop("checked", false);
			}
			selectChilds(items, 'el_', false);
			validate_v(val, 'r_', false);
			validate_v(val, 'w_', false);
			validate_v(val, 'e_', false);
			validate_v(val, 'd_', false);
			validate_v(val, 'el_', false);
			validate_v(val, 'a_', false);
		}

	}

	//监测当前权限是否全选
	function validate(val, pix) {
		var items = ($('#treediv').treegrid('getChildren', val));
		if ($("#" + pix + val).prop("checked")) {
			selectChilds(items, pix, true);
			validate_v(val, pix, true);
		} else {
			selectChilds(items, pix, false);
			validate_v(val, pix, false);
		}
	}

	function validate_else(val, pix) {
		var items = ($('#treediv').treegrid('getChildren', val));
		if ($("input[nam=" + pix + val + "]").prop("checked")) {
			selectChilds(items, pix, true);
			validate_v(val, pix, true);
		} else {
			selectChilds(items, pix, false);
			validate_v(val, pix, false);
		}
	}

	//纵向监测
	function validate_v(val, pix, ble) {
		if (val + "" == "0") {
			return;
		}
		//横向监测
		validate_h(val);
		var prarent = ($('#treediv').treegrid('getParent', val));
		if (!prarent) {
			return;
		}
		var items = ($('#treediv').treegrid('getChildren', prarent.id));
		valchange(items, pix, prarent.id, ble);
		validate_v(prarent.id, pix, ble);
	}
	//根据id改编复选框状态
	function valchange(items, pix, parentId, bel) {
		if (pix == 'el_') {
			return;
		}
		for (var i = 0; i < items.length; i++) {
			var d = items[i];
			var val = d.id;
			if ($("#" + pix + val).length > 0)
				if (!$("#" + pix + val).prop("checked")) {
					$("#" + pix + parentId).prop("checked", false);
					return;
				}
			;
		}
		;
		if (bel) {
			$("#" + pix + parentId).prop("checked", true);
		} else {
			$("#" + pix + parentId).prop("checked", false);
		}

	}

	//横向监测
	function validate_h(val) {
		var isnull = false;
		if ($("#r_" + val).length > 0) {
			isnull = true;
			if (!$("#r_" + val).prop("checked")) {
				if ($("#a_" + val).length > 0)
					$("#a_" + val).prop("checked", false);
				return;
			}
		}

		;
		if ($("#w_" + val).length > 0) {
			isnull = true;
			if (!$("#w_" + val).prop("checked")) {
				if ($("#a_" + val).length > 0)
					$("#a_" + val).prop("checked", false);
				return;
			}
		}

		;
		if ($("#e_" + val).length > 0) {
			isnull = true;
			if (!$("#e_" + val).prop("checked")) {
				if ($("#a_" + val).length > 0)
					$("#a_" + val).prop("checked", false);
				return;
			}
		}

		;
		if ($("#d_" + val).length > 0) {
			isnull = true;
			if (!$("#d_" + val).prop("checked")) {
				if ($("#a_" + val).length > 0)
					$("#a_" + val).prop("checked", false);
				return;
			}
		}

		if ($("input[nam=el_" + val + "]").length > 0) {
			isnull = true;
			for (var i = 0; i < $("input[nam=el_" + val + "]").length; i++) {
				if (!$($("input[nam=el_" + val + "]")[i]).prop("checked")) {
					if ($("#a_" + val).length > 0)
						$("#a_" + val).prop("checked", false);
					return;
				}

			}

		}

		;
		if ($("#a_" + val).length > 0) {
			$("#a_" + val).prop("checked", "checked");
		} else {
			$("#a_" + val).prop("checked", false);
		}

	}

	//修改子节点状态
	function selectChilds(items, pix, sel) {

		$
				.each(
						items,
						function(i, d) {
							var val = d.id;
							if (pix == 'el_') {
								if ($("input[nam=el_" + val + "]").length > 0) {
									for (var i = 0; i < $("input[nam=el_" + val
											+ "]").length; i++) {
										if (sel) {
											$($("input[nam=el_" + val + "]")[i])
													.prop("checked", "checked");
										} else {
											$($("input[nam=el_" + val + "]")[i])
													.prop("checked", false);
										}
										//横向监测
									}
									return validate_h(val);
								}
							} else {
								if ($("#" + pix + val).length > 0) {
									if (sel) {
										$("#" + pix + val).prop("checked",
												"checked");
									} else {
										$("#" + pix + val)
												.prop("checked", false);
									}
									//横向监测
									return validate_h(val);
								}
							}

						});
	}

	var url_temp = "${basePath}ms/sysRole/addSysRole";
	isEdit = ("${param.isEdit}" == "true");
	$(function() {
		if (isEdit) {
			var roleId = "${param.roleId}";
			$.post("${basePath}ms/sysRole/toUpdate", {
				"roleId" : roleId
			}, function(d) {
				if (d) {
					$("#roleId").val(d.roleId);
					$("#roleName").val(d.roleName);
					$("#remark").val(d.remark);
					$('#isDisable').combobox('setValue', d.isDisable);
                    $('#organizationId').combotree('setValue', d.organizationId);
					//如果包含数据权限
                    if($("#dataPermissions").length>0)
					{
                        if(d.dataPermissions){
                            d.dataPermissions = str2json(d.dataPermissions);
                            if(d.dataPermissions['${permissonDataKey}'])
							{
                                $('#dataPermissions_select').combobox('setValues',d.dataPermissions['${permissonDataKey}'].split(","));
							}
						}
					}
                    url_temp = "${basePath}ms/sysRole/updateSysRole";
				}
			}, "json");
		};


	});

	function resetChecketBox(row, data) {
		if (isEdit) {
			var roleId = "${param.roleId}";
			$.post("${basePath}ms/sysRole/searchButtons", {
				"roleId" : roleId
			}, function(d) {
				if (d.length > 0) {
					$.each(d, function(i, ds) {
						$('#' + ds.boxId).prop("checked", "checked");
						$('#' + ds.boxId).click();
						$('#' + ds.boxId).prop("checked", "checked");
					});
					for(i = 0;i<=maxLevel;i++)
					{
                        var levelParentIds = fatherLevel[maxLevel-i];
                        // 处理 枝节点的checkbox 是否选中
                        for(parentId in levelParentIds)
						{
						    //默认都选中
                            $('#r_' + parentId).prop("checked", "checked");
                            $('#w_' + parentId).prop("checked", "checked");
                            $('#e_' + parentId).prop("checked", "checked");
                            $('#d_' + parentId).prop("checked", "checked");
                            // 遍历自己的儿子们有一个不是checked就把自己取消选中
						    $('input[parentid=' + parentId + ']').each(function(){
						        if(!$(this).prop("checked"))
								{
                                    if($(this).attr('vt') == '1')
                                    {
                                        $('#r_' + parentId).prop("checked", false);
                                    }else if($(this).attr('vt') == '2')
                                    {
                                        $('#w_' + parentId).prop("checked", false);
                                    }else if($(this).attr('vt') == '3')
                                    {
                                        $('#e_' + parentId).prop("checked", false);
                                    }else if($(this).attr('vt') == '4')
                                    {
                                        $('#d_' + parentId).prop("checked", false);
                                    }
								}

						    })

						}
					}
					//处理是否自动选中all
					for(menuId in allMenuId)
					{
					    //默认选中
                        $('#a_' + menuId).prop("checked", "checked");
                        $('input[menuId=' + menuId + ']').each(function(){
                            //包含vt代表不是all自己   并且没有被选中则取消all的选中状态
							if($(this).attr('vt') && !$(this).prop("checked"))
							{
                                $('#a_' + menuId).prop("checked", false);
							}
                        });
					}

                }
			}, "json");
		}

	}

	var addUpateRoleForm = null;
	$(document).ready(function() {
        $('#organizationId').combotree('setValue', '${param.organizationId}');
		//form验证。
		addUpateRoleForm = $('#addUpateRoleForm').Validform({
			tiptype : 5
		});
		// 如果是编辑页面，就初始化一些数据
	});

	function save() {
		$('#addUpateRoleForm').form('submit', {
			url : url_temp,
			onSubmit : function() {
                //如果包含数据权限
                if($("#dataPermissions").length>0)
                {
                    var _dataPermissions = {};
                    _dataPermissions['${permissonDataKey}']=$('#dataPermissions_select').combobox('getValues') + "";
                    $("#dataPermissions").val(json2str(_dataPermissions));
                }
				if(addUpateRoleForm.check()){
					// 验证是否有勾选项
					var checkF = false;
					$('#addUpateRoleForm').find(':checkbox').each(function(){
						if(this.checked){
							checkF = true;
							return false;
						}else{
							return true;
						}
					});
					if(! checkF){
						EalertE('请设置该岗位权限界面');
						return false;
					};
				}else{
					initError('dlg');
					return false;
				}
			},
			success : function(d) {
				d = $.parseJSON(d);
				if (d.code == 200) {
					Ealert("操作成功！");
					closeDialog();
					reload();
				} else if(d.message != null && d.message != '')
				{
                    Ealert(d.message);
				} else {
					Ealert('操作失败');
				}
			}
		});

	}
</script>
