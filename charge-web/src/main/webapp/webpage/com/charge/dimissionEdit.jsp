<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>员工离职</title>
  <t:base type="jquery,easyui,tools,DatePicker,autocomplete"></t:base>
  <style type="text/css">
  	label{
  	    width: 100px;
    	display: inline-block;
    	text-align: right;
  	}
  </style>
 </head>
 <body>
   <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="employeeInfoController.do?leaveSave" tiptype="1" beforeSubmit="otherCheck()">
		 <div style="width:100%;text-align:center;background:white" class="formtable">
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 60px;">员工信息</div>
		        <div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>姓名:</label>
						<input class="inputxt" id="name" name="name" datatype="*"  value="${employeeInfoPage.name}" maxlength="30" disabled="disabled" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					     <label class="Validform_label"><span style="color:red">*</span>身份证号:</label>
				         <input class="inputxt" id="code" name="code"  value="${employeeInfoPage.code}" maxlength="49" disabled="disabled"/>
						 <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>入职日:</label>
				          <input class="Wdate" onClick="WdatePicker()"  style="width: 155px;padding: 4px 0px;border: 1px solid #D7D7D7;border-radius: 3px" id="entryDate" name="entryDate" disabled="disabled" value="<fmt:formatDate value='${employeeInfoPage.entryDate}' type="date" pattern="yyyy-MM-dd"/>" datatype="*"/>
		            	  <span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
				<div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>离职日:</label>
				          <input class="Wdate" onClick="WdatePicker()" autocomplete="off" style="width: 155px;padding: 4px 0px;border: 1px solid #D7D7D7;border-radius: 3px" id="quitDate" name="quitDate"  value="<fmt:formatDate value='${employeeInfoPage.quitDate}' type="date" pattern="yyyy-MM-dd"/>"/>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>离职理由:</label>
						<select id="quitReason" name="quitReason"  nullmsg="请选择离职理由！" onchange="sixGordCheck()" datatype="*" style="width: 157px;">
						<option value ="">--请选择--</option>
	  					<option value="1">转正岗</option>
	  					<option value="0">其他原因</option>
	  					</select>
						<span class="Validform_checktip"></span>
					</div>
				</div>
		        <div style="clear:both"></div>
		   </div>
		</div>
		<input id="id" name="id" type="hidden" value="${employeeInfoPage.id }">
		<div style="width: auto;height: 200px;">
				<%-- 增加一个div，用于调节页面大小，否则默认太小 --%>
				<div style="width:690px;height:1px;"></div>
	    </div>
  </t:formvalid>
  <script>
  $(document).ready(function(){
	  var quitReason = "${employeeInfoPage.quitReason}";
	  if(quitReason!=""){
	  $("#quitReason").val(quitReason);
	  }
  });
  function otherCheck(){
	   var rs = true;
	   //校验入职日、离职日不能大于当前日期
	   /* if(""!=$.trim($("#quitDate").val())){
		   if((new Date()).getTime()<(new Date($("#quitDate").val())).getTime()){
			   alert("离职日不能大于当前日期");
			   return false;
		   }
	   } */
	   //校验离职日不能大于入职日
	   if(""!=$.trim($("#entryDate").val())
			   &&""!=$.trim($("#quitDate").val())){
		   if((new Date($("#quitDate").val())).getTime()<(new Date($("#entryDate").val())).getTime()){
			   layer.msg('离职日不能小于入职日！');
			   return false;
		   }
	   }
	   //校验离职日不为空的时候，离职理由不能为空
	   if(""!=$.trim($("#quitDate").val())&&""==$.trim($("#quitReason").val())){
		   layer.msg('离职理由不能为空！');
		   return false;
	   }else if(""==$.trim($("#quitDate").val())){
		   layer.msg('请填写离职日！');
		   return false;
	   }
	   return rs;
  }
  </script>
 </body>