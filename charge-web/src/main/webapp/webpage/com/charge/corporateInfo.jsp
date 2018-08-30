<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>法人主体信息表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <style type="text/css">#float { position:absolute; display:none;background:#FFC;z-index:10; }</style>
  <script>
   	function showDetail(arg) {
		var input = arg;
		if(arg.value.length>14) {
			 $("#float").css({top:event.clientY+5, left:event.clientX+5});
			 $("#float").text(arg.value);
			 $("#float").show();
		}
   	}
   	function closeDetail(arg) {
   		$("#float").hide();
   	}
	</script>
 </head>
 <body style="overflow-y: hidden" scroll="no">
 <div id="float"></div>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="corporateInfoController.do?save" tiptype="1" >
		<input id="id" name="id" type="hidden" value="${corporateInfoPage.id }">
		<fieldset class="step">
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>法人简称:</label>
		      <input class="inputxt" id="code" name="code"  autocomplete="off"  value="${corporateInfoPage.code}" validType="c_corporate_info,code,id"  datatype="*"  maxlength="50" style="width:200px;"/>
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>法人全称:</label>
		      <input class="inputxt" id="name" name="name"  autocomplete="off"  value="${corporateInfoPage.name}"  validType="c_corporate_info,name,id" datatype="*" maxlength="30" style="width:200px;" onmouseover="showDetail(this)" onmouseout="closeDetail(this)"/>
		       <div id="float"></div>
		      <span class="Validform_checktip"></span>
		    </div>
	    </fieldset>
  </t:formvalid>
 </body>