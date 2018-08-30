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
			 $("#float").css({top:event.clientY+5, left:event.clientX+5});
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
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="suppliersController.do?save" beforeSubmit="otherCheck()" tiptype="1">
		<input id="id" name="id" type="hidden" value="${suppliersEntityPage.id }">
		<fieldset class="step">
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>供应商简称:</label>
		      <input class="inputxt" id="code" name="code" autocomplete="off" value="${suppliersEntityPage.code}" style="width:193px;" validType="c_suppliers,code,id"  datatype="*" maxlength="50" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>供应商全称:</label>
		      <input class="inputxt" id="name" name="name" autocomplete="off" value="${suppliersEntityPage.name}" style="width:193px;"  datatype="*" maxlength="30"  onmouseover="showDetail(this)" onmouseout="closeDetail(this)"/>
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">开户行:</label>
		      <input class="inputxt" id="openBank" name="openBank" autocomplete="off" value="${suppliersEntityPage.openBank}" style="width:193px;" maxlength="60" onmouseover="showDetail(this)" onmouseout="closeDetail(this)"/>
		       <div id="float"></div>
		      <span class="Validform_checktip"></span>
		    </div>
			<%-- <div class="form">
		      <label class="Validform_label">联系人:</label>
		      <input class="inputxt" id="contact" name="contact"  value="${customerInfoPage.contact}" style="width:193px;"  datatype="*" />
		      <span class="Validform_checktip"></span>
		    </div> --%>
			<div class="form">
		      <label class="Validform_label">银行账号:</label>
		      <input class="inputxt" id="bankAccount" name="bankAccount"  autocomplete="off"  value="${suppliersEntityPage.bankAccount}"  style="width:193px;"  maxlength="25" />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">所在省:</label>
		      <input class="inputxt" id="province" name="province"  autocomplete="off"  value="${suppliersEntityPage.province}"  style="width:193px;"  maxlength="50"/>
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">所在市:</label>
		      <input class="inputxt" id="city" name="city"  autocomplete="off"  value="${suppliersEntityPage.city}"  style="width:193px;"  maxlength="50"/>
		      <span class="Validform_checktip"></span>
		    </div>

			<div class="form">
		      <label class="Validform_label"><span style="color:red">*</span>企业性质:</label>
		      <%-- <input class="inputxt" id="tel4" name="tel4" ignore="ignore"   value="${customerInfoPage.tel4}" /> --%>
		      <select id="corporateType" name="corporateType" style="width:200px;">
  					<option value ="4">小微</option>
  					<option value ="6">其他</option>
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
	  var corporateType = "${suppliersEntityPage.corporateType}";
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
		   layer.alert("请选择签约法人");
		   //$.Showmsg("请选择签约法人");
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
				   if("${suppliersEntityPage.signCorporate}"==data[i]){
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
			   var nameOpt = "<option value='' selected='selected'>-- 请选择 -- </option>";
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