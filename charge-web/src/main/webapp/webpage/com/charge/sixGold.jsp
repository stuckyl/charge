<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>社保六金</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="sixGoldController.do?save">
		<input id="id" name="id" type="hidden" value="${sixGoldPage.id }">
		<fieldset class="step">
			<div class="form">
		      <label class="Validform_label">员工编号:</label>
		      <input class="inputxt" id="employeeCode" name="employeeCode" ignore="ignore"   value="${sixGoldPage.employeeCode}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">六金地点:</label>
		      <input class="inputxt" id="sixGoldPlace" name="sixGoldPlace" ignore="ignore"   value="${sixGoldPage.sixGoldPlace}" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">养老保险（企业）:</label>
		      <input class="inputxt" id="companyEndowment" name="companyEndowment" ignore="ignore"   value="${sixGoldPage.companyEndowment}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">养老保险（个人）:</label>
		      <input class="inputxt" id="personalEndowment" name="personalEndowment" ignore="ignore"   value="${sixGoldPage.personalEndowment}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">医疗保险（企业）:</label>
		      <input class="inputxt" id="companyMedical" name="companyMedical" ignore="ignore"   value="${sixGoldPage.companyMedical}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">医疗保险（个人）:</label>
		      <input class="inputxt" id="personalMedical" name="personalMedical" ignore="ignore"   value="${sixGoldPage.personalMedical}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">失业保险（企业）:</label>
		      <input class="inputxt" id="companyUnemployment" name="companyUnemployment" ignore="ignore"   value="${sixGoldPage.companyUnemployment}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">失业保险（个人）:</label>
		      <input class="inputxt" id="personalUnemployment" name="personalUnemployment" ignore="ignore"   value="${sixGoldPage.personalUnemployment}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">工伤（企业）:</label>
		      <input class="inputxt" id="companyInjury" name="companyInjury" ignore="ignore"   value="${sixGoldPage.companyInjury}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">生育（企业）:</label>
		      <input class="inputxt" id="companyMaternity" name="companyMaternity" ignore="ignore"   value="${sixGoldPage.companyMaternity}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">住房公积金（企业）:</label>
		      <input class="inputxt" id="companyHousingFund" name="companyHousingFund" ignore="ignore"   value="${sixGoldPage.companyHousingFund}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">住房公积金（个人）:</label>
		      <input class="inputxt" id="personalHousingFund" name="personalHousingFund" ignore="ignore"   value="${sixGoldPage.personalHousingFund}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">企业合计:</label>
		      <input class="inputxt" id="companySum" name="companySum" ignore="ignore"   value="${sixGoldPage.companySum}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">个人合计:</label>
		      <input class="inputxt" id="personalSum" name="personalSum" ignore="ignore"   value="${sixGoldPage.personalSum}" datatype="d" />
		      <span class="Validform_checktip"></span>
		    </div>
	    </fieldset>
  </t:formvalid>
 </body>