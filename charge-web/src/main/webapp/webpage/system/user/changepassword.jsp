<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" refresh="false" dialog="true" action="userController.do?savenewpwd" usePlugin="password" layout="table" tiptype="2" beforeSubmit="otherCheck()">
	<input id="id" type="hidden" value="${user.id }">
	<table style="width: 550px" cellpadding="0" cellspacing="1" class="formtable">
		<tbody>
			<tr>
				<td align="right" width="10%"><span class="filedzt">原密码:</span></td>
				<td class="value"><input type="password" id="oldpassword" name="password" class="inputxt" datatype="*" errormsg="请输入原密码" /> <span class="Validform_checktip"> 请输入原密码 </span></td>
			</tr>
			<tr>
				<td align="right"><span class="filedzt">新密码:</span></td>
				<td class="value"><input type="password" id="newpassword1" name="newpassword" class="inputxt" plugin="passwordStrength" datatype="*6-18" errormsg="密码至少6个字符,最多18个字符！" onchange="checkpassword()" /> <span
					class="Validform_checktip"> 密码至少6个字符,最多18个字符！ </span> <span class="passwordStrength" style="display: none;">
				</td>
			</tr>
			<tr>
				<td align="right"><span class="filedzt">重复密码:</span></td>
				<td class="value"><input id="newpassword" type="password" recheck="newpassword" class="inputxt" datatype="*6-18" errormsg="两次输入的密码不一致！" onchange="checkpassword()"> <span class="Validform_checktip"></span></td>
			</tr>
		</tbody>
	</table>
</t:formvalid>
<script>
	function checkpassword() {
		var oldpassword=$("#oldpassword").val();
		var newpassword=$("#newpassword1").val();
		var newpassword1=$("#newpassword").val();
		//if((""!=newpassword&&newpassword==oldpassword)||(""!=newpassword1&&newpassword1==oldpassword)){
		if(""!=newpassword&&newpassword==oldpassword){
			layer.msg("新密码不能与原密码相同！");
			   return false;
		}
		if(""!=newpassword&&""!=newpassword1&&newpassword1!=newpassword){
			layer.msg("重复密码与新密码不同！");
			   return false;
		}
	}
	function otherCheck(){
		return checkpassword();
	}

</script>
</body>