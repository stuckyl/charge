<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%@ page import="com.charge.entity.*" %>
<%
EmployeeDeclareCopyEntity a= (EmployeeDeclareCopyEntity)request.getAttribute("employeeDeclareCopyPage");

%>
<!DOCTYPE html>
<html>
 <head>
  <title>员工信息表</title>
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
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="employeeDeclareController.do?look" callback="initTableHeaderColor" tiptype="1" beforeSubmit="otherCheck()">
		<div style="width:100%;text-align:center;background:white" class="formtable">
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 60px;">员工信息</div>
		        <div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">姓名:</label>
						<input class="inputxt" id="employeeName" name="employeeName" disabled="disabled"  value="${employeeInfoPage.name}" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					     <label class="Validform_label">ID:</label>
				         <input class="inputxt" id="employeeCode" name="employeeCode" disabled="disabled" value="${employeeInfoPage.code}" />
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
						value="${employeeDeclareCopyPage.employeeAsalary}" data-options="precision:2,groupSeparator:','" />
						<span class="Validform_checktip"></span>
					</div>
		        </div>

		        <div style="clear:both"></div>
		   </div>


		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 60px;">当月收入</div>
		        <div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
					     <label class="Validform_label"><span style="color:red">*</span>签约客户:</label>
		                 <input class="inputxt" name="customerInfoId" id="customerInfoId" value="${employeeDeclareCopyPage.customerName}" disabled="disabled" >
		                 <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>单价方式:</label>
				          <t:dictSelect field="unitPriceType" typeGroupCode="upType" readonly="readonly" defaultVal="${employeeDeclareCopyPage.unitPriceType}" extendJson="{style='width: 157px;'}" id="unitPriceType" datatype="*"></t:dictSelect>
		      	   		  <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>单价:</label>
				          <%-- <input class="inputxt" id="unitPrice" name="unitPrice"  onchange="incomeChange()" value="${employeeDeclareCopyPage.unitPrice}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" /> --%>
				          <input class="inputxt" id="unitPrice" name="unitPrice"  disabled="disabled" onchange="incomeChange()"
				          value="${employeeDeclareCopyPage.unitPrice}"  data-options="precision:2,groupSeparator:','"
				          datatype="/^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/"/>
		      		      <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
		       	 	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>流转税:</label>
				          <select name="isTurnoverTax" id="isTurnoverTax"  datatype="*" style="width: 157px;" onchange="incomeChange()">
				          		<option value="0" <c:if test="${employeeDeclareCopyPage.isturnovertax==0}">selected</c:if> >不扣税</option>
				          		<option value="1" <c:if test="${employeeDeclareCopyPage.isturnovertax==1}">selected</c:if> >扣税</option>
		                  </select>
                          <span class="Validform_checktip"></span>
					</div>
		       		<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>约定出勤日数:</label>
				          <input class="inputxt" id="appointedAttendanceDay" name="appointedAttendanceDay" disabled="disabled"   value="${employeeDeclareCopyPage.appointedAttendanceDay}"
				          datatype="/^[0-2]?[0-9]([.]{1}[0-9]{1,2})?$/|/^[3][0]$/" />
                          <span class="Validform_checktip"></span>
					</div>

					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>验收出勤日数:</label>
				          <input class="inputxt" id="acceptedAttendanceDay" name="acceptedAttendanceDay"  disabled="disabled"  value="${employeeDeclareCopyPage.acceptedAttendanceDay}"
				          datatype="/^[0-2]?[0-9]([.]{1}[0-9]{1,2})?$/|/^[3][0]$/" />
				          <span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
		        <div>
		           <div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">当月加算:</label>
				          <input class="inputxt" id="monthOther" name="monthOther" disabled="disabled" onchange="incomeChange()"
				           value="${employeeDeclareCopyPage.monthOther}" data-options="precision:2,groupSeparator:','"
				           datatype="/(^\s*$)|(^(-)?[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^(-)?[0-9]\.[0-9]([0-9])?$)/"  />
			              <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">验收加算:</label>
				          <%-- <input class="inputxt" id="acceptanceAdd" name="acceptanceAdd"  onchange="incomeChange()"  value="${employeeDeclareCopyPage.acceptanceAdd}" datatype="/(^\s*$)|(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" /> --%>
				          <input class="inputxt" id="acceptanceAdd" name="acceptanceAdd" disabled="disabled" onchange="incomeChange()"
				          value="${employeeDeclareCopyPage.acceptanceAdd}" data-options="precision:2,groupSeparator:','"
				          datatype="/(^\s*$)|(^(-)?[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^(-)?[0-9]\.[0-9]([0-9])?$)/" />
		      		      <span class="Validform_checktip"></span>
					</div>
		            <div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">月间调整:</label>
				          <input class="inputxt" id="monthAdjustment" name="monthAdjustment" disabled="disabled" onchange="incomeChange()"
				          value="${employeeDeclareCopyPage.monthAdjustment}"  data-options="precision:2,groupSeparator:','"
				          datatype="/(^\s*$)|(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^(-)?[0-9]\.[0-9]([0-9])?$)/" />
		      	          <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		   </div>


			<div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
				<div style="position:relative;background:white;top:-15px;width: 80px;">当月薪酬浮动</div>
				<div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">B试用折扣率(%):</label>
						<input class="inputxt" id="bDiscount" name="bDiscount" disabled="disabled" value="<%=a.getBDiscount()==null?"":a.getBDiscount() %>"
						datatype="/(^\s*$)|(^[1-9][0-9](\.[0-9]{1,2})?$)|(^[1-9](\.[0-9]{1,2})?$)|^100$/" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">法定出勤日数:</label>
				          <input class="inputxt" id="legalAttendanceDay" name="legalAttendanceDay"  disabled="disabled"  value="${employeeDeclareCopyPage.legalAttendanceDay}"
				          datatype="/^[0-2]?[0-9]([.]{1}[0-9]{1,2})?$/|/^[3][0]$/" />
                          <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>有绩效出勤日数:</label>
				          <input class="inputxt" id="performanceAttendanceDay" name="performanceAttendanceDay" disabled="disabled"   value="${employeeDeclareCopyPage.performanceAttendanceDay}"
				          datatype="/^[0-2]?[0-9]([.]{1}[0-9]{1,2})?$/|/^[3][0]$/" />
                          <span class="Validform_checktip"></span>
					</div>

					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>无绩效出勤日数:</label>
				          <input class="inputxt" id="noPerformanceAttendanceDay" name="noPerformanceAttendanceDay"  disabled="disabled"  value="${employeeDeclareCopyPage.noPerformanceAttendanceDay}"
				          datatype="/^[0-2]?[0-9]([.]{1}[0-9]{1,2})?$/|/^[3][0]$/" />
				          <span class="Validform_checktip"></span>
					</div>
			   </div>
			   <div style="clear:both"></div>
			   <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C电脑:</label>
						<input class="inputxt" id="cComputerSubsidy" name="cComputerSubsidy" disabled="disabled"
						value="<%=a.getCComputerSubsidy()==null?"":a.getCComputerSubsidy() %>"  data-options="precision:2,groupSeparator:','"
						datatype="/(^\s*$)|(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C加班:</label>
						<input class="inputxt" id="cOvertimeSalary" name="cOvertimeSalary" disabled="disabled"
						value="<%=a.getCOvertimeSalary()==null?"":a.getCOvertimeSalary() %>" data-options="precision:2,groupSeparator:','"
						datatype="/(^\s*$)|(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">D年终奖:</label>
						<input class="inputxt" id="dnumYearEndBonus" name="dnumYearEndBonus" disabled="disabled" ignore="ignore"
						value="<%=a.getDAnnualBonus()==null?"":a.getDAnnualBonus() %>" data-options="precision:2,groupSeparator:','"
						datatype="/(^\s*$)|(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
				<div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C1其它:</label>
						<input class="inputxt" id="c1OtherSubsidy" name="c1OtherSubsidy" disabled="disabled"
						value="${employeeDeclareCopyPage.c1OtherSubsidy}" data-options="precision:2,groupSeparator:','"
						datatype="/(^\s*$)|(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C1备注:</label>
						<input class="inputxt" id="c1Note" name="c1Note" disabled="disabled"  value="${employeeDeclareCopyPage.c1OtherSubsidyRemark}" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
				<div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C2其它:</label>
						<input class="inputxt" id="c2OtherSubsidy" name="c2OtherSubsidy" disabled="disabled"
						value="${employeeDeclareCopyPage.c2OtherSubsidy}" data-options="precision:2,groupSeparator:','"
						datatype="/(^\s*$)|(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C2备注:</label>
						<input class="inputxt" id="c2Note" name="c2Note" disabled="disabled"  value="${employeeDeclareCopyPage.c2OtherSubsidyRemark}" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
				<div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C3其它:</label>
						<input class="inputxt" id="c3OtherSubsidy" name="c3OtherSubsidy" disabled="disabled"
						value="${employeeDeclareCopyPage.c3OtherSubsidy}" data-options="precision:2,groupSeparator:','"
						datatype="/(^\s*$)|(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">C3备注:</label>
						<input class="inputxt" id="c3Note"  name="c3Note"  disabled="disabled" value="${employeeDeclareCopyPage.c3OtherSubsidyRemark}" />
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
						<input class="inputxt" id="sixCompanyBurdenOne" name="sixCompanyBurdenOne" disabled="disabled"
						value="${employeeDeclareCopyPage.sixCompanyBurdenOne}" data-options="precision:2,groupSeparator:','" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">六金个人负担:</label>
						<input class="inputxt" id="sixPersonalBurdenOne" name="sixPersonalBurdenOne" disabled="disabled"
						value="${employeeDeclareCopyPage.sixPersonalBurdenOne}" data-options="precision:2,groupSeparator:','" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">个人所得税:</label>
						<input class="inputxt" id="perToneTaxOne" name="perToneTaxOne" disabled="disabled"
						value="${employeeDeclareCopyPage.perToneTaxOne+employeeDeclareCopyPage.perToneTaxTwo}" data-options="precision:2,groupSeparator:','" />
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
						<input class="inputxt" id="payableSalary" name="payableSalary" disabled="disabled"
						value="${employeeDeclareCopyPage.payableSalary}" data-options="precision:2,groupSeparator:','" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-top: -25px;">
						<div style="margin-right:25px;float:left;height:35px">
							<label class="Validform_label">员工到手:</label>
							<input class="inputxt" id="employeeRealSalary" name="employeeRealSalary" disabled="disabled"
							value="${employeeDeclareCopyPage.employeeRealSalary}" data-options="precision:2,groupSeparator:','" />
							<span class="Validform_checktip"></span>
						</div>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">总成本:</label>
						<input class="inputxt" id="companyCost" name="companyCost" disabled="disabled"
						value="${employeeDeclareCopyPage.companyCost }" data-options="precision:2,groupSeparator:','" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
				<div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">收入:</label>
						<input class="inputxt" id="income" name="income" disabled="disabled"   maxlength="9"
						value="${employeeDeclareCopyPage.income}" data-options="precision:2,groupSeparator:','"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">净收入:</label>
						<input class="inputxt" id="netIncome" name="netIncome" disabled="disabled"   maxlength="9"
						value="${employeeDeclareCopyPage.netIncome}" data-options="precision:2,groupSeparator:','"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">毛利:</label>
						<input class="inputxt" id="companyProfit" name="companyProfit" disabled="disabled"  maxlength="9"
						value="${employeeDeclareCopyPage.companyProfit}" data-options="precision:2,groupSeparator:','"/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">毛利率(%):</label>
						<input class="inputxt" id="companyProfitRate" name="companyProfitRate" disabled="disabled" value="${employeeDeclareCopyPage.companyProfitRate }" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
			</div>
		</div>
		<input id="id" name="id" type="hidden" value="${employeeDeclareCopyPage.id }">
		<input id="appointmentAttendanceDay" name="appointmentAttendanceDay" type="hidden" value="${employeeDeclareCopyPage.legalAttendanceDay==null?21:employeeDeclareCopyPage.legalAttendanceDay }">
  </t:formvalid>
  <!-- 金钱正则： /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/ -->

 </body>

  <script type="text/javascript">
  var customers={};
  $(document).ready(function(){

	   /* 对金钱格式进行设置 */
	    //A(标准)
	    $('#aStandardSalary').numberbox({
		    min:0,
		    precision:2
		});
	    //单价:
	    $('#unitPrice').numberbox({
		    min:0,
		    precision:2
		});
	    //当月加算:
	    $('#monthOther').numberbox({
		   // min:0,
		    precision:2
		});
	    //验收加算:
	    $('#acceptanceAdd').numberbox({
		   // min:0,
		    precision:2
		});
	    //月间调整:
	    $('#monthAdjustment').numberbox({
		 //   min:0,
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
	    //收入:
	    $('#income').numberbox({
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
		  /*   min:0, */
		    precision:2
		});
	    //毛利:
	    $('#companyProfit').numberbox({
		   /*  min:0, */
		    precision:2
		});
	    //毛利率:
	    $('#companyProfitRate').numberbox({
		   /*  min:0, */
		    precision:2
		});
	  //约定出勤日数:
	    $('#appointedAttendanceDay').numberbox({
		    min:0,
		    precision:2
		});
	    //验收出勤日数:
	    $('#acceptedAttendanceDay').numberbox({
		    min:0,
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
		    precision:2
		});

  });
  </script>