<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>选择登录身份部门</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<script >
 $(document).ready(function(){

	var userList = ${userList};
		for(var x in userList){
			var userList1 = userList[x];
			var realName = userList1.realName;
			var userName = userList1.userName;
			$("#userName").append($("<option value="+userName+">"+userName+"</option>"));
		}
});
</script>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" layout="table" action="loginController.do?login" tiptype="2">
	<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable" >
		<tr>
			<td align="right"><label class="Validform_label">请选择账号: </label></td>
			<td class="value">
                <select id="userName" name="userName" datatype="*">

                </select>
            	<span class="Validform_checktip"></span>
            </td>
		</tr>

	</table>
</t:formvalid>

</body>
