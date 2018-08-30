<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>系统参数表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>

<script type="text/javascript">
function check(){
	var constantKey = $("#constantKey").val();
	var constantValue = $("#constantValue").val();
	/* if(constantName == "基本工资"){
		var reg = /^\d+(\.\d+)?$/;
 		if(!reg.test(constantValue)){
			layer.alert("请输入数字");
			return false;
 		}
	} */
	if(constantKey == "c_turnoverTax"){
		var reg = /^(\d|[1-9]\d|99)(\.\d{0,2})|100.00$/;
 		if(!reg.test(constantValue)){
			layer.msg("请输入0-100的数");
			return false;
 		}
	}
	if(constantKey == "c_email_pass" || constantName == "c_email_host"){
 		var reg = /^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}:;,.<>/?？~！@#￥%……&*（）——+|{}【】‘；：”“]){6,}$/;
 		if(!reg.test(constantValue)){
			layer.msg("请输入正确的格式");
			return false;
 		}
	}
	if(constantKey == "c_email_account" || constantName == "c_email_group"){
 		var reg = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/;
 		if(!reg.test(constantValue)){
			layer.msg("邮箱格式不正确");
			return false;
 		}
	}
}

$(document).ready(function(){
	//var constantKey = "{$constantDictionaryPage.constantKey}";
	var constantKey = $("#constantKey").val();
	var constantValue = $("#constantValue").val();
	if(constantKey == "c_basePay"){
		$('#constantValue').numberbox({
		    min:0,
		    precision:2,
		    groupSeparator:','
		});
        $('#constantValue').numberbox().attr('maxlength', 11);
		$("#constantValue").datatype="/(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})+(,\d{3})*(\.\d+)?$/";
	}
	if(constantKey == "c_perTax"){
		$('#constantValue').numberbox({
		    min:0,
		    precision:2,
		    groupSeparator:','
		});
        $('#constantValue').numberbox().attr('maxlength', 11);
		$("#constantValue").datatype="/(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})+(,\d{3})*(\.\d+)?$/";
	}
	if(constantKey == "c_turnoverTax"){
		$('#constantValue').numberbox({
		    min:0,
		    precision:2
		});
		$('#constantValue').numberbox().attr('maxlength', 7);
		$("#constantValue").datatype="/(^[1-9][0-9]$)|(^[1-9]$)|^100$/";
	}
});
</script>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="2" action="constantDictionaryController.do?save" beforeSubmit="check()">
			<input id="id" name="id" type="hidden" value="${constantDictionaryPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr style="display:none">
					<td align="right">
						<label class="Validform_label">
							code:
						</label>
					</td>
					<td class="value">
						${constantDictionaryPage.constantKey}
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							参数名:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="constantName" name="constantName"   value="${constantDictionaryPage.constantName}" datatype="*" disabled="disabled"/>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							参数值:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="constantValue" name="constantValue"   value="${constantDictionaryPage.constantValue}" datatype="*" autocomplete="off"/>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
		<input id="constantKey" name="constantKey"  value="${constantDictionaryPage.constantKey}" hidden="true"></input>
 </body>