<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>邮箱配置</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="emailConfigController.do?save">
		<input id="id" name="id" type="hidden" value="${emailConfigPage.id }">
		<fieldset class="step">
			<div class="form">
		      <label class="Validform_label">主机:</label>
		      <input class="inputxt" id="host" name="host" ignore="ignore"   value="${emailConfigPage.host}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">账号:</label>
		      <input class="inputxt" id="account" name="account" ignore="ignore"   value="${emailConfigPage.account}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">密码:</label>
		      <input class="inputxt" id="password" name="password" ignore="ignore"   value="${emailConfigPage.password}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">最大可发送次数:</label>
		      <input class="inputxt" id="maxCount" name="maxCount" ignore="ignore"   value="${emailConfigPage.maxCount}" datatype="n" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">发送人:</label>
		      <input class="inputxt" id="mailFrom" name="mailFrom" ignore="ignore"   value="${emailConfigPage.mailFrom}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">端口:</label>
		      <input class="inputxt" id="port" name="port" ignore="ignore"   value="${emailConfigPage.port}" datatype="n" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">是否开启SSL:</label>
		      <t:dictSelect field="openSsl" typeGroupCode="openSsl" defaultVal="${emailConfigPage.openSsl}" datatype="*"></t:dictSelect>
		      <span class="Validform_checktip"></span>
		    </div>
	    </fieldset>
  </t:formvalid>
 </body>