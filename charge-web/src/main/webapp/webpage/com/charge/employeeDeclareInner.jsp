<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%@ page import="com.charge.entity.*" %>
<%
EmployeeDeclareEntity a= (EmployeeDeclareEntity)request.getAttribute("employeeDeclarePage");

%>
<!DOCTYPE html>
<html>
 <head>
  <title>员工申报表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <style type="text/css">
  	label{
  	    width: 100px;
    	display: inline-block;
    	text-align: right;
  	}
  </style>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="employeeDeclareController.do?saveInnerEmployee" callback="initTableHeaderColor" tiptype="1" beforeSubmit="otherCheck()">
		<div style="width:100%;text-align:center;background:white" class="formtable">
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 60px;">员工信息</div>
		        <div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">姓名:</label>
						<input class="inputxt" id="employeeName" name="employeeName" disabled="disabled"  value="${employeeDeclarePage.employeeInfo.name}" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					     <label class="Validform_label">ID:</label>
				         <input class="inputxt" id="employeeCode" name="employeeCode" disabled="disabled" value="${employeeDeclarePage.employeeInfo.code}" />
						 <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">部门:</label>
				          <input class="inputxt" id="employeeDepartment" name="employeeDepartment" disabled="disabled"  value="${departName}" />
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">A(标准):</label>
						<input class="inputxt" id="aStandardSalary" name="aStandardSalary" disabled="disabled"
						value="${employeeDeclarePage.employeeASalary}" data-options="precision:2,groupSeparator:','" />
						<span class="Validform_checktip"></span>
					</div>
		        </div>

		        <div style="clear:both"></div>
		   </div>

			<div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
				<div style="position:relative;background:white;top:-15px;width: 80px;">当月薪酬浮动</div>
				<div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>B试用折扣率(%):</label>
						<input class="inputxt" id="bDiscount" name="bDiscount"  value="<%=a.getBDiscount()==null?"":a.getBDiscount() %>"
						onchange="incomeChange()" autocomplete="off" maxlength="3" errorMsg="请填写1-100之间的整数"
						datatype="/(^[1-9][0-9]$)|(^[1-9]$)|^100$/"  />
		            	<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">法定出勤日数:</label>
				          <input class="inputxt" id="legalAttendanceDay" name="legalAttendanceDay"  disabled="disabled"  value="${employeeDeclarePage.legalAttendanceDay}"
				          autocomplete="off"
				          datatype="/^[0-2]?[0-9]([.]{1}[0-9]{1,2})?$/|/^[3][0](\.([0]{1})?([0]{1})?)?$/" />
				          <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>有绩效出勤日数:</label>
				          <input class="inputxt" id="performanceAttendanceDay" name="performanceAttendanceDay"  onchange="incomeChange()"  value="${employeeDeclarePage.performanceAttendanceDay}"
				          autocomplete="off" maxlength="5"
				          datatype="/^[0-2]?[0-9]([.]{1}[0-9]{1,2})?$/|/^[3][0](\.([0]{1})?([0]{1})?)?$/" />
				          <span class="Validform_checktip"></span>
					</div>

					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>无绩效出勤日数:</label>
				          <input class="inputxt" id="noPerformanceAttendanceDay" name="noPerformanceAttendanceDay"  onchange="incomeChange()"  value="${employeeDeclarePage.noPerformanceAttendanceDay}"
				          autocomplete="off" maxlength="5"
				          datatype="/^[0-2]?[0-9]([.]{1}[0-9]{1,2})?$/|/^[3][0](\.([0]{1})?([0]{1})?)?$/" />
				          <span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
				<div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C电脑:</label>
						<input class="inputxt" id="cComputerSubsidy" name="cComputerSubsidy"  maxlength="9"
						value="<%=a.getCComputerSubsidy()==null?"":a.getCComputerSubsidy() %>"  data-options="precision:2,groupSeparator:','"
						onchange="incomeChange()" autocomplete="off"
						datatype="/(^\s*$)|(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C加班:</label>
						<input class="inputxt" id="cOvertimeSalary" name="cOvertimeSalary" maxlength="9"
						value="<%=a.getCOvertimeSalary()==null?"":a.getCOvertimeSalary() %>"  data-options="precision:2,groupSeparator:','"
						onchange="incomeChange()" autocomplete="off"
						datatype="/(^\s*$)|(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">D年终奖:</label>
						<input class="inputxt" id="dnumYearEndBonus" name="dnumYearEndBonus" ignore="ignore"  maxlength="9" disabled="disabled"
						value="<%=a.getDAnnualBonus()==null?"":a.getDAnnualBonus() %>"  data-options="precision:2,groupSeparator:','" autocomplete="off"
						datatype="/(^\s*$)|(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
				<div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C1其它:</label>
						<input class="inputxt" id="c1OtherSubsidy" name="c1OtherSubsidy"  maxlength="9"
						value="${employeeDeclarePage.c1OtherSubsidy}"  data-options="precision:2,groupSeparator:','"
						onchange="incomeChange()" autocomplete="off"
						datatype="/(^\s*$)|(^(\-)?[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(\-)?(0){1}$)|(^(\-)?[0-9]\.[0-9]([0-9])?$)/|/^(\-)?(\d{1,3})?(,\d{3})*(\.\d+)?$/"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C1备注:</label>
						<input class="inputxt" id="c1Note" name="c1Note" autocomplete="off"  value="${employeeDeclarePage.c1OtherSubsidyRemark}" maxlength="255" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
				<div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C2其它:</label>
						<input class="inputxt" id="c2OtherSubsidy" name="c2OtherSubsidy"  maxlength="9"
						value="${employeeDeclarePage.c2OtherSubsidy}"  data-options="precision:2,groupSeparator:','"
						onchange="incomeChange()" autocomplete="off"
						datatype="/(^\s*$)|(^(\-)?[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(\-)?(0){1}$)|(^(\-)?[0-9]\.[0-9]([0-9])?$)/|/^(\-)?(\d{1,3})?(,\d{3})*(\.\d+)?$/"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C2备注:</label>
						<input class="inputxt" id="c2Note" name="c2Note" autocomplete="off"  value="${employeeDeclarePage.c2OtherSubsidyRemark}" maxlength="255" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
				<div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C3其它:</label>
						<input class="inputxt" id="c3OtherSubsidy" name="c3OtherSubsidy"  maxlength="9" autocomplete="off"
						value="${employeeDeclarePage.c3OtherSubsidy}" data-options="precision:2,groupSeparator:','"
						onchange="incomeChange()"
						datatype="/(^\s*$)|(^(\-)?[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(\-)?(0){1}$)|(^(\-)?[0-9]\.[0-9]([0-9])?$)/|/^(\-)?(\d{1,3})?(,\d{3})*(\.\d+)?$/"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C3备注:</label>
						<input class="inputxt" id="c3Note"  autocomplete="off" name="c3Note" value="${employeeDeclarePage.c3OtherSubsidyRemark}" maxlength="255" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
			</div>
			<div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
				<div style="position:relative;background:white;top:-15px;width: 60px;">六金个税</div>
				<div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">六金公司负担:</label>
						<input class="inputxt" id="sixCompanyBurdenOne" name="sixCompanyBurdenOne" disabled="disabled"  maxlength="9"
						value="${employeeDeclarePage.sixCompanyBurdenOne}" data-options="precision:2,groupSeparator:','"    />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">六金个人负担:</label>
						<input class="inputxt" id="sixPersonalBurdenOne" name="sixPersonalBurdenOne" disabled="disabled"  maxlength="9"
						value="${employeeDeclarePage.sixPersonalBurdenOne}" data-options="precision:2,groupSeparator:','"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">个人所得税:</label>
						<input class="inputxt" id="perToneTaxOne" name="perToneTaxOne" disabled="disabled" maxlength="9"
						value="${employeeDeclarePage.perToneTaxOne+employeeDeclarePage.perToneTaxTwo}"  data-options="precision:2,groupSeparator:','" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
			</div>
			<div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
				<div style="position:relative;background:white;top:-15px;width: 60px;">当月损益</div>
				<div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">应付工资:</label>
						<input class="inputxt" id="payableSalary" name="payableSalary" disabled="disabled"   maxlength="9"
						value="${employeeDeclarePage.payableSalary}" data-options="precision:2,groupSeparator:','"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-top: -25px;">
						<div style="margin-right:25px;float:left;height:35px">
							<label class="Validform_label">员工到手:</label>
							<input class="inputxt" id="employeeRealSalary" name="employeeRealSalary" disabled="disabled"   maxlength="9"
							value="${employeeDeclarePage.employeeRealSalary}" data-options="precision:2,groupSeparator:','"   />
							<span class="Validform_checktip"></span>
						</div>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">总成本:</label>
						<input class="inputxt" id="companyCost" name="companyCost" disabled="disabled"  maxlength="9"
						value="${employeeDeclarePage.companyCost }" data-options="precision:2,groupSeparator:','" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
			</div>
		</div>
		<input id="id" name="id" type="hidden" value="${employeeDeclarePage.id }">
		<input id="basePay" name="basePay" type="hidden" value="${employeeDeclarePage.employeeInfo.basePay}">
		<input id="meritPay" name="meritPay" type="hidden" value="${employeeDeclarePage.employeeASalary-employeeDeclarePage.employeeBasePay}">
		<input id="a1Payment" name="a1Payment" type="hidden" value="${employeeDeclarePage.employeeInfo.a1Payment}">
		<input id="a2Payment" name="a2Payment" type="hidden" value="${employeeDeclarePage.employeeInfo.a2Payment}">
		<%-- <div style="width: auto;height: 200px;">
				增加一个div，用于调节页面大小，否则默认太小
				<div style="width:690px;height:1px;"></div>
	    </div> --%>
  </t:formvalid>
  <!-- 金钱正则： /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/ -->
  <script type="text/javascript">
  var customers={};
  $(document).ready(function(){
	  /* 对金钱格式进行设置 */
	    //A(标准)
	    $('#aStandardSalary').numberbox({
		    min:0,
		    precision:2
		});
	    //C电脑:1
	    $('#cComputerSubsidy').numberbox({
		    min:0,
		    precision:2
		});
	    //C加班:
	    $('#cOvertimeSalary').numberbox({
		    min:0,
		    precision:2
		});
	    //D年终奖:
	    $('#dnumYearEndBonus').numberbox({
		    min:0,
		    precision:2
		});
	    //C其它:1
	    $('#c1OtherSubsidy').numberbox({
		   // min:0,
		    precision:2
		});
	    //C其它:2
	    $('#c2OtherSubsidy').numberbox({
		   // min:0,
		    precision:2
		});
	    //C其它:3
	    $('#c3OtherSubsidy').numberbox({
		  //  min:0,
		    precision:2
		});
	    //六金公司负担:
	    $('#sixCompanyBurdenOne').numberbox({
		    min:0,
		    precision:2
		});
	    //六金个人负担:
	    $('#sixPersonalBurdenOne').numberbox({
		    min:0,
		    precision:2
		});
	    //个人所得税:
	    $('#perToneTaxOne').numberbox({
		    min:0,
		    precision:2
		});
	    //应付工资:
	    $('#payableSalary').numberbox({
		    min:0,
		    precision:2
		});
	    //员工到手:
	    $('#employeeRealSalary').numberbox({
		    min:0,
		    precision:2
		});
	    //总成本:
	    $('#companyCost').numberbox({
		    /* min:0, */
		    precision:2
		});
	    //法定出勤日数:
	    $('#legalAttendanceDay').numberbox({
		    min:0,
		    precision:2
		});
	    //有绩效出勤日数:
	    $('#performanceAttendanceDay').numberbox({
		    min:0,
		    precision:2
		});
	    //无绩效出勤日数:
	    $('#noPerformanceAttendanceDay').numberbox({
		    min:0,
		    precision:2
		});
	  	//B试用折扣率
	    $('#bDiscount').numberbox({
		    precision:0
		});
	  if(""!=$.trim($("#income").val())){
		  $("#income").val(formatTwoDecimal($("#income").val()));
	  }

  });

  //格式化数字
  function formatTwoDecimal(value){
  	if(""!=$.trim(value)){
  		if(value.indexOf(".")!=-1&&value.substring(value.indexOf(".")+1).length==1){
  			return value+"0";
  		}
  	}
  	return value;
  }


  function otherCheck(){
	//添加验证，若c其他有数值，则c备注必须不为空
		if($("#c1OtherSubsidy").val()!=0&&$("#c1Note").val()==""){
			layer.alert("请填写对应的C备注");
			   return false;
		}
		if($("#c2OtherSubsidy").val()!=0&&$("#c2Note").val()==""){
			layer.alert("请填写对应的C备注");
			   return false;
		}
		if($("#c3OtherSubsidy").val()!=0&&$("#c3Note").val()==""){
			layer.alert("请填写对应的C备注");
			   return false;
		}

		  //法定出勤日
		  var l_Day = handleNum($("#legalAttendanceDay").val());
		  //有绩效出勤日
		  var p_Day = handleNum($("#performanceAttendanceDay").val());
		  //无绩效出勤日
		  var np_Day = handleNum($("#noPerformanceAttendanceDay").val());
		  if(l_Day<p_Day){
			  layer.alert("有绩效出勤日不得大于法定出勤日");
			  return false;
		  }
		  if(l_Day<np_Day){
			  layer.alert("无绩效出勤日不得大于法定出勤日");
			  return false;
		  }

		  if(l_Day<(p_Day+np_Day)&&np_Day!=0){
			  layer.alert("有绩效出勤日与无绩效出勤日之和不得大于法定出勤日");
			  return false;
		  }

	   return true;
  }
  function isRealNum(val){
	    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
	    if(val === "" || val ==null){
	        return false;
	    }
	    if(!isNaN(val)){
	        return true;
	    }else{
	        return false;
	    }
	}
  function incomeChange(){
		//应付工资   应付基本工资+应付绩效工资+补贴合计
		//试用折扣率
		/* var bDiscount = $('#bDiscount').numberbox('getValue'); */
		var bDiscount = handleNum($("#bDiscount").val());
		//法定出勤日数
		/* var legalAttendanceDay = $('#legalAttendanceDay').numberbox('getValue'); */
		var legalAttendanceDay = handleNum($("#legalAttendanceDay").val());
		//有绩效出勤日
/* 		var performanceAttendanceDay = $("#performanceAttendanceDay").numberbox('getValue'); */
		var performanceAttendanceDay = handleNum($("#performanceAttendanceDay").val());
		//无绩效出勤日
		/* var noPerformanceAttendanceDay = $("#noPerformanceAttendanceDay").numberbox('getValue'); */
		var noPerformanceAttendanceDay = handleNum($("#noPerformanceAttendanceDay").val());
		//C电脑补贴
		/* var cComputerSubsidy = $("#cComputerSubsidy").numberbox('getValue'); */
		var cComputerSubsidy = handleNum($("#cComputerSubsidy").val());
		//C加班
		/* var cOvertimeSalary = $("#cOvertimeSalary").numberbox('getValue'); */
		var cOvertimeSalary = handleNum($("#cOvertimeSalary").val());
		//C其他1
		/* var c1OtherSubsidy = $("#c1OtherSubsidy").numberbox('getValue'); */
		var c1OtherSubsidy = handleNum($("#c1OtherSubsidy").val());
		//C其他2
		/* var c2OtherSubsidy = $("#c2OtherSubsidy").numberbox('getValue'); */
		var c2OtherSubsidy = handleNum($("#c2OtherSubsidy").val());
		//C其他3
		/* var c3OtherSubsidy = $("#c3OtherSubsidy").numberbox('getValue'); */
		var c3OtherSubsidy = handleNum($("#c3OtherSubsidy").val());
		//基本工资
		var basePay = handleNum($('#basePay').val());
		//绩效工资
		var meritSalary = handleNum($("#meritPay").val());
		//补贴合计
		var totalSubsidy = cComputerSubsidy+cOvertimeSalary+c1OtherSubsidy+c2OtherSubsidy+c3OtherSubsidy;

		if(legalAttendanceDay==0) {
		}else {
			//应付基本工资
			var payableBaseSalary = (performanceAttendanceDay+noPerformanceAttendanceDay)/legalAttendanceDay*basePay*bDiscount/100;
			//应付绩效：  有绩效出勤日/法定出勤日*A2绩效*B折扣率
			var payablePerformance = performanceAttendanceDay/legalAttendanceDay*meritSalary*bDiscount/100;
			var payableSalary = payableBaseSalary+payablePerformance+totalSubsidy;
			$("#payableSalary").val(myFormat(Math.round(parseFloat(payableSalary)*100)/100));
		}
		//应付工资(应付合计)
		var payableSalary = handleNum($("#payableSalary").val());

		//员工到手 计算    （应付工资-个税-个人六金负担）
		//员工六金负担
		var sixPersonalBurdenOne = handleNum($("#sixPersonalBurdenOne").val());

				var a1Payment = handleNum($("#a1Payment").val());
				var a2Payment = handleNum($("#a2Payment").val());
				//个税计算
				if(a2Payment==0||payableSalary-a1Payment<=0) { //一个发薪地或者当月扣钱太多发薪地2的扣光了  应付工资-员工六金负担
					var tax = calPersonalTax(payableSalary-sixPersonalBurdenOne,3500);
					$("#perToneTaxOne").val(myFormat(Math.round(parseFloat(tax)*100)/100));
				}else {  //两个发薪地  发薪地2为江苏
					var tax1 = calPersonalTax(a1Payment-sixPersonalBurdenOne,3500); //发薪地1 - 个人六金
					var tax2 = calPersonalTax(a2Payment+payableSalary-basePay-meritSalary,3500); //发薪地2 + 当月浮动
					$("#perToneTaxOne").val(myFormat(Math.round(parseFloat(tax1+tax2)*100)/100));
				}
		// 个税
		var perToneTax =  handleNum($("#perToneTaxOne").val());
		var employeeRealSalary = payableSalary-sixPersonalBurdenOne-perToneTax;

		$("#employeeRealSalary").val(myFormat(Math.round(parseFloat(employeeRealSalary)*100)/100));

		//公司总成本    应付合计 + 公司六金负担
		//公司六金负担
		/* 	var sixCompanyBurdenOne = $('#sixCompanyBurdenOne').numberbox('getValue'); */
		var sixCompanyBurdenOne = handleNum($("#sixCompanyBurdenOne").val());
		var companyCost = payableSalary+sixCompanyBurdenOne;
		$("#companyCost").val(myFormat(Math.round(parseFloat(companyCost)*100)/100));
}
  function handleNum(num){
	  if(null==num||""==$.trim(num)||typeof(num)=="undefined"){
		  num = 0.0;
	  }
	  if(isNaN(num)){  //如果 是一个 非数字
		  if(num.indexOf(",")!=-1){
			  num=num.replace(/,/gi,'');
		  }
	  }
	  return parseFloat(num);
  }

  function myFormat(num) {
	  return (num.toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
  }
  function calPersonalTax(money,baseMoney) {
	  if(money<= baseMoney) {
		  return 0;
	  }
	  money = money - baseMoney;
	  if (money<=1500){
	  	money=money*0.03;
	  }else if (money<=4500){
	  	money=money*0.10-105;
	  }else if (money<=9000){
	  	money=money*0.20-555;
	  }else if (money<=35000){
	  	money=money*0.25-1005;
	  }else if (money<=55000){
	  	money=money*0.30-2755;
	  }else if (money<=80000){
	  	money=money*0.35-5505;
	  }else {
		money=money*0.45-13505;
	  }
	  return money;
}
  </script>
 </body>