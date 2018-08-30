<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>客户信息表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <style type="text/css">#float { position:absolute; display:none;background:#FFC; }</style>
  <script>
   	function showDetail(arg) {
		var input = arg;
		if(arg.value.length>14) {
			 $("#float").css({top:event.clientY+5,left:event.clientX+5});
			 $("#float").css("padding-left",5);
			 $("#float").text(arg.value);
			 $("#float").show();
		}
   	}
   	function closeDetail(arg) {
   		$("#float").hide();
   	}
	</script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="customerInfoController.do?save" beforeSubmit="otherCheck()" tiptype="1">
		<input id="id" name="id" type="hidden" value="${customerInfoPage.id }">
		<fieldset class="step">
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>客户简称:</label>
		      <input class="inputxt" id="code" name="code" autocomplete="off" value="${customerInfoPage.code}" style="width:193px;" validType="c_customer_info,code,id"  datatype="*" maxlength="50" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>客户全称:</label>
		      <input class="inputxt" id="name" name="name" autocomplete="off" value="${customerInfoPage.name}" style="width:193px;" validType="c_customer_info,name,id"  datatype="*" maxlength="30"  onmouseover="showDetail(this)" onmouseout="closeDetail(this)"/>
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>地址:</label>
		      <input class="inputxt" id="address" name="address" autocomplete="off" value="${customerInfoPage.address}" style="width:193px;" datatype="*" maxlength="300" onmouseover="showDetail(this)" onmouseout="closeDetail(this)"/>
		       <div id="float"></div>
		      <span class="Validform_checktip"></span>
		    </div>
			<%-- <div class="form">
		      <label class="Validform_label">联系人:</label>
		      <input class="inputxt" id="contact" name="contact"  value="${customerInfoPage.contact}" style="width:193px;"  datatype="*" />
		      <span class="Validform_checktip"></span>
		    </div> --%>
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>联系电话:</label>
		      <input class="inputxt" id="tel" name="tel"  autocomplete="off"  value="${customerInfoPage.tel}"  style="width:193px;" datatype="/^(((0\d{2,3}-)?\d{7,8})|(1[358479]\d{9}))$/" maxlength="25"/>
		      <span class="Validform_checktip"></span>
		      </div>
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>约定月工作日数:</label>
		      <input class="inputxt" id="workDays" name="workDays"  autocomplete="off"  value="${customerInfoPage.workDays}" placeholder="请填写1-30的数！"
		      style="width:193px;" datatype="/^[0-2]?[1-9]([.]{1}[0-9]{1,3})?$/|/^10([.]{1}[0-9]{1,3})?$/|/^20([.]{1}[0-9]{1,3})?$/|/^[3][0](\.([0]{1})?([0]{1})?)?$/" maxlength="9"/>
		      <span class="Validform_checktip"><%-- <t:mutiLang langKey="请填写1-31的数！"/> --%></span>
		    </div>
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>约定账龄:</label>
		      <input class="inputxt" id="accountDelay" name="accountDelay" autocomplete="off" value="${customerInfoPage.accountDelay}" placeholder="请填写1-12的数！"
		      style="width:193px;" datatype="/^([1-9])([.]{1}[0-9]{1,3})?$/|/^(10|11)([.]{1}[0-9]{1,3})?$/|/^[1][2]([.]{1}[0]{1,3})?$/" maxlength="11"/>
		      <span class="Validform_checktip"><%-- <t:mutiLang langKey="请填写1-12的整数！"/> --%></span>
		    </div>
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>企业性质:</label>
		      <%-- <input class="inputxt" id="tel4" name="tel4" ignore="ignore"   value="${customerInfoPage.tel4}" /> --%>
		      <select id="corporateType" name="corporateType" style="width:200px;">
  					<option value ="0">国企</option>
  					<option value ="1">外企</option>
  					<option value ="3">上市公司</option>
  					<option value ="2">民企</option>
  					<option value ="6">新顾客</option>
				</select>
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>签约法人:</label>
		      <select name="signCorporateId" id="signCorporateId" style="width:200px;">
		      </select>
		    <%--  <t:comboTree url="corporateInfoController.do?getComboTreeData" value="${customerInfoPage.signCorporate}" name="signCorporateId" id="signCorporateId" width="200"></t:comboTree> --%>
<%-- 			<div class="form">		      <label class="Validform_label">签约类型:</label>
		      <t:dictSelect field="signType" typeGroupCode="signType" defaultVal="${customerInfoPage.signType}" datatype="*" ></t:dictSelect>
		      <span class="Validform_checktip"></span>
		    </div> --%>
		   <%--  <div class="form">
		      <label class="Validform_label">备注:</label>
		      <textarea rows="5" cols="30" id="remark" name="remark" >${customerInfoPage.remark}</textarea>
		      <span class="Validform_checktip"></span>
		    </div> --%>
	    </fieldset>
  </t:formvalid>
 </body>

 <script type="text/javascript">
 $(document).ready(function(){
	  corporateName();
	  var corporateType = "${customerInfoPage.corporateType}";
	  if(corporateType!=""){
	  $("#corporateType").val(corporateType);
	  }
	  $('#workDays').numberbox({
		    min:0,
		    precision:2
		});
	  $('#accountDelay').numberbox({
		    min:0,
		    precision:2
		});
 });
  function otherCheck(){
	   var rs = true;
	   if($("#signCorporateId").val()==""){
		   layer.alert("请选择签约法人！");
		   //$.Showmsg("");
		   return false;
	   }
	   return rs;
  }

 function corporateName() {
	  $.ajax({
		   url: "corporateInfoController.do?getComboTreeData2",
		   type: "get",
		   success: function(data){
			   data = JSON.parse(data);
			   var nameOpt = "<option value='' selected='selected'>--- 请选择 ---</option>";
			   for(var i = 0;i<data.length-1;i+=2){
				   if("${customerInfoPage.signCorporate}"==data[i]){
					   nameOpt+= "<option value='"+data[i]+"' selected='selected'>"+data[i+1]+"</option>";
				   }else{
					   nameOpt+= "<option value='"+data[i]+"' >"+data[i+1]+"</option>";
				   }
			   }
			   $("#signCorporateId").html(nameOpt);
		   },
		   error:function(){ alert("haha");}
	   });
  }
/*   function corporateName() {
	  $.ajax({
		   url: "corporateInfoController.do?getComboTreeData",
		   type: "get",
		   success: function(data){
			   data = JSON.parse(data);
			   var nameOpt = "<option value='' selected='selected'>--- 请选择 ---</option>";
			   for(var i = 0;i<data.length;i++){
				   if("${customerInfoPage.signCorporate}"==data[i].id){
					   nameOpt+= "<option value='"+data[i].id+"' selected='selected'>"+data[i].text+"</option>";
				   }else{
					   nameOpt+= "<option value='"+data[i].id+"' >"+data[i].text+"</option>";
				   }
			   }
			   $("#signCorporateId").html(nameOpt);
		   },
		   error:function(){}
	   });
  } */

  </script>