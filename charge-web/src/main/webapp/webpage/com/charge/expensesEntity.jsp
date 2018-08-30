<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>gg</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="expensesEntityController.do?save">
		<input id="id" name="id" type="hidden" value="${expensesEntityPage.id }">
		<fieldset class="step">
			<div class="form">
		      <label class="Validform_label">活动年月 201802、201803、201804:</label>
		      <input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width: 150px" id="startMonth" name="startMonth" ignore="ignore"     value="<fmt:formatDate value='${expensesEntityPage.startMonth}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">活动起始日期:</label>
		      <input class="Wdate" onClick="WdatePicker()"  style="width: 150px" id="startDate" name="startDate" ignore="ignore"   value="<fmt:formatDate value='${expensesEntityPage.startDate}' type="date" pattern="yyyy-MM-dd"/>" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">活动日数:</label>
		      <input class="inputxt" id="numberDate" name="numberDate" ignore="ignore"   value="${expensesEntityPage.numberDate}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">活动主题:</label>
		      <input class="inputxt" id="theme" name="theme" ignore="ignore"   value="${expensesEntityPage.theme}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">活动内容:</label>
		      <input class="inputxt" id="content" name="content" ignore="ignore"   value="${expensesEntityPage.content}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">申请金额:</label>
		      <input class="inputxt" id="money" name="money" ignore="ignore"   value="${expensesEntityPage.money}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">活动参与人数:</label>
		      <input class="inputxt" id="numberPeople" name="numberPeople" ignore="ignore"   value="${expensesEntityPage.numberPeople}" datatype="n" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">人均消费:</label>
		      <input class="inputxt" id="average" name="average" ignore="ignore"   value="${expensesEntityPage.average}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">活动参与人:</label>
		      <input class="inputxt" id="namePeople" name="namePeople" ignore="ignore"   value="${expensesEntityPage.namePeople}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">所属部门:</label>
		      <input class="inputxt" id="departmentId" name="departmentId" ignore="ignore"   value="${expensesEntityPage.departmentId}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">申报状态   1总监录入待处理，2审核拒绝，3审核待处理，4审批拒绝，5审批待处理，6审批通过， 7申报中
8，审计录入待处理 :</label>
		      <input class="inputxt" id="declareStatus" name="declareStatus" ignore="ignore"   value="${expensesEntityPage.declareStatus}" datatype="n" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">失败理由:</label>
		      <input class="inputxt" id="declareReturnreason" name="declareReturnreason" ignore="ignore"   value="${expensesEntityPage.declareReturnreason}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">录入者:</label>
		      <input class="inputxt" id="inputerId" name="inputerId" ignore="ignore"   value="${expensesEntityPage.inputerId}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">申报者:</label>
		      <input class="inputxt" id="reporterId" name="reporterId" ignore="ignore"   value="${expensesEntityPage.reporterId}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">审核者:</label>
		      <input class="inputxt" id="checkerId" name="checkerId" ignore="ignore"   value="${expensesEntityPage.checkerId}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">创建人:</label>
		      <input class="inputxt" id="createdBy" name="createdBy" ignore="ignore"   value="${expensesEntityPage.createdBy}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">创建日期:</label>
		      <input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width: 150px" id="createdDate" name="createdDate" ignore="ignore"     value="<fmt:formatDate value='${expensesEntityPage.createdDate}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">删除标记  0未删除 1已删除:</label>
		      <input class="inputxt" id="expensesDelFlg" name="expensesDelFlg" ignore="ignore"   value="${expensesEntityPage.expensesDelFlg}" datatype="n" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">最后修改人:</label>
		      <input class="inputxt" id="lastModifiedBy" name="lastModifiedBy" ignore="ignore"   value="${expensesEntityPage.lastModifiedBy}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">最后修改时间:</label>
		      <input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width: 150px" id="lastModifiedDate" name="lastModifiedDate" ignore="ignore"     value="<fmt:formatDate value='${expensesEntityPage.lastModifiedDate}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">修改标记  0_编辑中  1_无人编辑:</label>
		      <input class="inputxt" id="editors" name="editors" ignore="ignore"   value="${expensesEntityPage.editors}" />
		      <span class="Validform_checktip"></span>
		    </div>
	    </fieldset>
  </t:formvalid>
 </body>