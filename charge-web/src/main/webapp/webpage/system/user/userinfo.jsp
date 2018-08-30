<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<t:base type="jquery,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<%-- <t:formvalid formid="formobj" layout="div" dialog="true">
	<fieldset class="step">
		<div class="form"><label class="form"> <t:mutiLang langKey="common.username"/>: </label><input name="userName" class="inputxt" value="${user.userName }"> </div>
		<div class="form"><label class="form"> <t:mutiLang langKey="common.surname"/>: </label><input name="realName" class="inputxt" value="${user.realName }"> </div>
		<div class="form"><label class="form"> <t:mutiLang langKey="common.phone"/>: </label> <input name="mobilePhone" class="inputxt" value="${user.mobilePhone}"> </div>
		<div class="form"><label class="form"> <t:mutiLang langKey="common.mail"/>: </label><input name="email" class="inputxt" value="${user.email}"> </div>
	</fieldset>
	</form>
</t:formvalid> --%>
<%-- <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="corporateInfoController.do?save" >
	<fieldset class="step">
		<div class="form">
			<label class="Validform_label"> 用户账号: </label>
			<input name="userName" class="inputxt" value="${user.userName }" disabled="disabled">
		</div>
		<div class="form">
			<label class="Validform_label"> 姓名: </label>
			<input name="realName" class="inputxt" value="${user.realName }">
		</div>
		<div class="form">
			<label class="Validform_label"> 手机号码: </label>
			<input name="mobilePhone" class="inputxt" value="${user.mobilePhone }">
		</div>
		<div class="form">
			<label class="Validform_label"> 邮箱: </label>
			<input name="email" class="inputxt" value="${user.email }">
		</div>
	</fieldset>
</t:formvalid> --%>
<t:formvalid formid="formobj" refresh="false" dialog="true" action="userController.do?changeUserInfo" usePlugin="password" layout="table">
	<input id="id" name="id" type="hidden" value="${user.id }">
	<table style="width: 550px" cellpadding="0" cellspacing="1" class="formtable">
		<tbody>
			<tr>
				<td align="right" width="10%"><span class="filedzt">用户账号:</span></td>
				<td class="value"><input type="text"  style="color:#CCC;" name="userName" class="inputxt" disabled="disabled" value="${user.userName }"/>
				</td>
			</tr>
			<tr>
				<td align="right"><span class="filedzt">姓名:</span></td>
				<td class="value"><input type="text"  name="realName" autocomplete="off" class="inputxt" value="${sessionScope.LOCAL_CLINET_USER.realName }"/>
				</td>
			</tr>
			<tr>
				<td align="right"><span class="filedzt">手机号码:</span></td>
				<td class="value"><input type="text"  name="mobilePhone" autocomplete="off" class="inputxt" value="${sessionScope.LOCAL_CLINET_USER.mobilePhone }"
				datatype="m" errormsg="手机号码不正确" ignore="ignore"/>
			</td>
			</tr>
			<tr>
				<td align="right"><span class="filedzt">邮箱:</span></td>
				<td class="value"><input type="text"  name="email" autocomplete="off"  class="inputxt" value="${sessionScope.LOCAL_CLINET_USER.email }"
				datatype="e" errormsg="邮箱格式不正确!"/>
			</td>
			</tr>
		</tbody>
	</table>
</t:formvalid>
</body>
</html>

