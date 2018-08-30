<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>员工信息表</title>
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
   <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="employeeInfoController.do?saveDepart" tiptype="1"  beforeSubmit="otherCheck()">
		 <div style="width:100%;text-align:center;background:white" class="formtable">
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 60px;">员工信息</div>
		        <div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>姓名:</label>
						<input class="inputxt" id="name" name="name" datatype="*" autocomplete="off" value="${employeeInfoPage.name}" maxlength="30" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					     <label class="Validform_label"><span style="color:red">*</span>身份证号:</label>
				         <input class="inputxt" id="code" name="code" autocomplete="off" value="${employeeInfoPage.code}" onchange="codeChake()" disabled="disabled" datatype="/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/" maxlength="49" />
						 <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="margin-top: -25px;">
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">手机:</label>
				          <input class="inputxt" id="contactWay" name="contactWay" autocomplete="off" datatype="m" value="${employeeInfoPage.contactWay}" ignore="ignore" maxlength="99" />
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">邮箱:</label>
						<input class="inputxt" id="email" name="email" datatype="e" autocomplete="off" value="${employeeInfoPage.email}" ignore="ignore" maxlength="99"/>
						<span class="Validform_checktip"></span>
					</div>
		        </div>
			   <div style="margin-top: -25px;">
				   <div style="margin-right:25px;float:left;height:35px">
					   <label class="Validform_label">户口性质:</label>
					   <t:dictSelect field="householdRegistration" typeGroupCode="houseReg" defaultVal="${employeeInfoPage.householdRegistration}" extendJson="{style='width: 157px;'}"></t:dictSelect>
					   <span class="Validform_checktip"></span>
				   </div>
				   <div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">性别:</label>
						<t:dictSelect field="gender" typeGroupCode="sex" defaultVal="${employeeInfoPage.gender}" extendJson="{style='width: 157px;'}"></t:dictSelect>
						<span class="Validform_checktip"></span>
					</div>
			   </div>
		        <div>
		        <div style="margin-right:25px;float:left;height:35px">
					  <label class="Validform_label">招商银行账户:</label>
					  <%-- <input class="inputxt" id="cmbAccount" name="cmbAccount" value="${employeeInfoPage.cmbAccount}"  datatype="/^[0-9]*$/"/> --%>
					  <input class="inputxt" id="cmbAccount" name="cmbAccount" value="${employeeInfoPage.cmbAccount}" maxlength="49" autocomplete="off"
						  datatype="/^\s*$/|/^(620520)\d{13}$/g|/^(370285|370286|370287|370289)\d{9}$/g|/^(356885|356886|356887|356888|356890|439188|439227|479228|479229|521302|356889|545620|545621|545947|545948|552534|552587|622575|622576|622577|622578|622579|545619|622581|622582|545623|628290|439225|518710|518718|628362|439226|628262|625802|625803)\d{10}$/g|/^(690755)\d{12}$/g|/^(690755)\d{9}$/g|/^(402658|410062|468203|512425|524011|622580|622588|622598|622609|95555|621286|621483|621485|621486|621299)\d{10}$/g"/>
					  <span class="Validform_checktip"></span>
				</div>
				<div style="margin-right:25px;float:left;height:35px">
	  				 <label class="Validform_label">工商银行账户:</label>
	 				 <input class="inputxt" id="icbcAccount" name="icbcAccount" value="${employeeInfoPage.icbcAccount}" maxlength="49" autocomplete="off"
	 					 datatype="/^\s*$/|/^(620114|620187|620046)\d{13}$/g|/^(620054|620142|620184|620030|620050|620143|620149|620124|620183|620094|620186|620148|620185)\d{10}$/g|/^(622210|622211|622212|622213|622214|622220|622223|622225|622229|622215|622224)\d{10}$/g|/^(45806|53098|45806|53098)\d{11}$/g|/^(427010|427018|427019|427020|427029|427030|427039|438125|438126|451804|451810|451811|458071|489734|489735|489736|510529|427062|524091|427064|530970|530990|558360|524047|525498|622230|622231|622232|622233|622234|622235|622237|622239|622240|622245|622238|451804|451810|451811|458071|628288|628286|622206|526836|513685|543098|458441|622246|544210|548943|356879|356880|356881|356882|528856|625330|625331|625332|622236|524374|550213|625929|625927|625939|625987|625930|625114|622159|625021|625022|625932|622889|625900|625915|625916|622171|625931|625113|625928|625914|625986|625925|625921|625926|625942|622158|625917|625922|625934|625933|625920|625924|625017|625018|625019)\d{10}$/g|/^(370246|370248|370249|370247|370267|374738|374739)\d{9}$/g|/^(9558)\d{15}$/g|/^(402791|427028|427038|548259|621376|621423|621428|621434|621761|621749|621300|621378|622944|622949|621371|621730|621734|621433|621370|621764|621464|621765|621750|621377|621367|621374|621731|621781)\d{10}$/g|/^(622200|622202|622203|622208|621225|620058|621281|900000|621558|621559|621722|621723|620086|621226|621618|620516|621227|621288|621721|900010|623062|621670|621720|621379|621240|621724|621762|621414|621375|622926|622927|622928|622929|622930|622931|621733|621732|621372|621369|621763)\d{13}$/g|/^(620200|620302|620402|620403|620404|620406|620407|620409|620410|620411|620412|620502|620503|620405|620408|620512|620602|620604|620607|620611|620612|620704|620706|620707|620708|620709|620710|620609|620712|620713|620714|620802|620711|620904|620905|621001|620902|621103|621105|621106|621107|621102|621203|621204|621205|621206|621207|621208|621209|621210|621302|621303|621202|621305|621306|621307|621309|621311|621313|621211|621315|621304|621402|621404|621405|621406|621407|621408|621409|621410|621502|621317|621511|621602|621603|621604|621605|621608|621609|621610|621611|621612|621613|621614|621615|621616|621617|621607|621606|621804|621807|621813|621814|621817|621901|621904|621905|621906|621907|621908|621909|621910|621911|621912|621913|621915|622002|621903|622004|622005|622006|622007|622008|622010|622011|622012|621914|622015|622016|622003|622018|622019|622020|622102|622103|622104|622105|622013|622111|622114|622017|622110|622303|622304|622305|622306|622307|622308|622309|622314|622315|622317|622302|622402|622403|622404|622313|622504|622505|622509|622513|622517|622502|622604|622605|622606|622510|622703|622715|622806|622902|622903|622706|623002|623006|623008|623011|623012|622904|623015|623100|623202|623301|623400|623500|623602|623803|623901|623014|624100|624200|624301|624402|623700|624000)\d{12}$/g" />
	  				<span class="Validform_checktip"></span>
				</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
		        <div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>部门:</label>
						<select id="department" name="department"  nullmsg="请选择部门" datatype="*" style="width: 157px;" onchange="inputerName()"></select>
			      	    <span class="Validform_checktip"></span>
				   </div>
				   <div style="margin-right:25px;float:left;height:35px">
					   <label class="Validform_label"><span style="color:red">*</span>员工类别:</label>
					   <!--  <input class="inputxt" id="employeeFlag" name="employeeFlag"  value="外派" disabled="disabled"/>-->
					   <t:dictSelect field="employeeFlag" typeGroupCode="empFlag" defaultVal="${employeeInfoPage.employeeFlag}" datatype="*"
					   id="empFlagId" extendJson="{style='width: 157px;',onchange='inputerIdSUM()'}" ></t:dictSelect>
					   <span class="Validform_checktip"></span>
				   </div>
			   		<div style="margin-right:25px;float:left;height:35px">
					   <label class="Validform_label"><span style="color:red">*</span>所属上级:</label>
					   <select id="inputerId" name="inputerId" class="inputerId" datatype="*"  nullmsg="请选择所属上级" style="width: 157px;"></select>
					   <span class="Validform_checktip"></span>
				   </div>
				   </div>
				   <div style="clear:both"></div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>入职日:</label>
				          <input class="Wdate" onClick="WdatePicker()"  style="width: 155px;padding: 4px 0px;border: 1px solid #D7D7D7;border-radius: 3px" id="entryDate" name="entryDate"  value="<fmt:formatDate value='${employeeInfoPage.entryDate}' type="date" pattern="yyyy-MM-dd"/>" datatype="*"/>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					     <label class="Validform_label">签约客户:</label>
		                 <select name="customerId" id="customerId" style="width: 157px;">
		                 </select>
		                 <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">单价方式:</label>
				          <t:dictSelect field="unitPriceType" typeGroupCode="upType" defaultVal="${employeeInfoPage.unitPriceType}" id="unitPriceType"
				            extendJson="{style='width: 157px;'}"></t:dictSelect>
		      	   		  <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">单价:</label>
				          <input class="inputxt" id="unitPrice" name="unitPrice"   maxlength="9" autocomplete="off" onchange="incomeChange()" ignore="ignore"
				          value="${employeeInfoPage.unitPrice}" data-options="precision:2,groupSeparator:','" autocomplete="off"/>
				          <!-- datatype="/(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})+(,\d{3})*(\.\d+)?$/"/> -->
		      		      <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		   </div>
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 60px;">薪酬安排</div>
		        <div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
					     <label class="Validform_label"><span style="color:red">*</span>A（标准）:</label>
		                 <input class="inputxt" id="aStandardSalary" name="aStandardSalary" onchange="performanceChange()" disabled="disabled" maxlength="9" value="${employeeInfoPage.AStandardSalary}"
		                data-options="precision:2,groupSeparator:','" datatype="*"  autocomplete="off"/>
		                 <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>基本工资:</label>
				          <!-- <input class="inputxt" id="basePay" name="basePay" onchange="performanceChange()"  datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" /> -->
		            	  <input class="inputxt" id="basePay" name="basePay" disabled="disabled" maxlength="9"  value="${employeeInfoPage.basePay}" data-options="precision:2,groupSeparator:','" />
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>绩效工资:</label>
				          <!-- <input class="inputxt" id="discount" name="discount" onchange="performanceChange()"  datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" /> -->
		            	  <input class="inputxt" id="meritPay" name="meritPay"  maxlength="9" disabled="disabled" data-options="precision:2,groupSeparator:','"
		            	  value="${employeeInfoPage.meritPay}" />
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>试用折扣率(%):</label>
					      <!-- <input class="inputxt" id=null name="null"/> -->
				          <!-- <input class="inputxt" id="meritPay" name="meritPay" onchange="performanceChange()"  datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" /> -->
		            	  <input class="inputxt" id="discount" name="discount" maxlength="9" autocomplete="off"
		            	  value="${employeeInfoPage.discount}"
		            	  datatype="/(^[1-9][0-9]$)|(^[1-9]$)|^100$/"  />
		            	  <%-- <span class="Validform_checktip"><t:mutiLang langKey="例：90%输入90即可"/></span> --%>
		            	  <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style = "clear:both"></div>
		        <div>
		        	<br>
			        <div style="margin-top: -20px;">
						<div style="margin-right:25px;float:left;height:35px">
						      <label class="Validform_label"><span style="color:red">*</span>六金城市:</label>
						      <input class="inputxt" id="sixGoldCity" name="sixGoldCity" datatype="*" value="${employeeInfoPage.sixGoldCity}" disabled="disabled"></input>
			      	          <span class="Validform_checktip"></span>
						</div>
						<div style="margin-right:25px;float:left;height:35px">
						      <label class="Validform_label"><span style="color:red">*</span>六金基数:</label>
					          <%-- <input class="inputxt" id="sixGoldBase" name="sixGoldBase" value="${employeeInfoPage.sixGoldBase}" datatype="/(^\s*$)|(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" /> --%>
			      		      <input class="inputxt" id="sixGoldBase" name="sixGoldBase"  maxlength="9" data-options="precision:2,groupSeparator:','"
			      		      value="${employeeInfoPage.sixGoldBase}" autocomplete="off" disabled="disabled"
			      		      datatype="*" />
			      		      <!-- datatype="/^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$/|/^0$/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/" /> -->
			      		      <span class="Validform_checktip"></span>
						</div>
			        </div>
		        </div>
		        <div style = "clear:both"></div>
		        <div>
		            <div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>发薪地1:</label>
				          <input class="inputxt" id="a1Place" name="a1Place" disabled="disabled" value="${employeeInfoPage.a1Place}"></input>
		      	          <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>发薪金额1:</label>
				          <%-- <input class="inputxt" id="a1Payment" name="a1Payment" onchange="performanceChange()" value="${employeeInfoPage.a1Payment}" datatype="/(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" /> --%>
		            	  <input class="inputxt" id="a1Payment" name="a1Payment" onchange="a1pChange()" maxlength="9" data-options="precision:2,groupSeparator:','" autocomplete="off"
		            	  value="${employeeInfoPage.a1Payment}" disabled="disabled" datatype="*"/>
		            	  <span class="Validform_checktip"></span>
					</div>
		        </div>
			    <div style = "clear:both"></div>
		        <div>
		            <div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">发薪地2:</label>
				          <input class="inputxt" id="a2Place" name="a2Place" disabled="disabled" value="${employeeInfoPage.a2Place}" />
		      	          <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">发薪金额2:</label>
				          <input class="inputxt" id="a2Payment" name="a2Payment" maxlength="9" data-options="precision:2,groupSeparator:','" autocomplete="off"
				          value="${employeeInfoPage.a2Payment}" disabled="disabled"/>
		            	  <%-- <input class="inputxt" id="a2Payment" name="a2Payment" value="${employeeInfoPage.a2Payment}" disabled="disabled"/><!-- /^[0-9]{1}?+(\.[0-9]{1,2})|[0-9]{0,}(\.[0.9]{1,2})?$/ --> --%>
		            	  <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		   </div>
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 60px;">薪酬变动</div>
		        <div style="margin-top: -25px;">
		        <div style="margin-right:25px;float:left;height:35px">
				      <label class="Validform_label">生效日期:</label>
			          <input class="Wdate" onClick="WdatePicker({minDate:'%y-%M-{%d+1}'})"  style="width: 157px;padding: 4px 0px;border: 1px solid #D7D7D7;border-radius: 3px" id="changeDate" name="changeDate"  value="<fmt:formatDate value='${employeeInfoPage.changeDate}' type="date" pattern="yyyy-MM-dd"/>"/>
	            	  <span class="Validform_checktip"></span>
				</div>
				 </div>
				 <div style = "clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
					     <label class="Validform_label">A（标准）:</label>
		                 <input class="inputxt" id="aStandardSalaryCh" name="aStandardSalaryCh" onchange="performanceChange()" maxlength="9" value="${employeeInfoPage.AStandardSalaryCh}"
		                data-options="precision:2,groupSeparator:','" autocomplete="off"/>
		                 <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">基本工资:</label>
				          <input class="inputxt" id="basePayCh" name="basePayCh" disabled="disabled" maxlength="9" autocomplete="off" value="${basePay}" data-options="precision:2,groupSeparator:','" />
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">绩效工资:</label>
		            	  <input class="inputxt" id="meritPayCh" name="meritPayCh"  maxlength="9" disabled="disabled" data-options="precision:2,groupSeparator:','"
		            	  value="${employeeInfoPage.AStandardSalaryCh - basePay>0?employeeInfoPage.AStandardSalaryCh - basePay:0}" />
		            	  <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style = "clear:both"></div>
		        <div>
					<br>
			        <div style="margin-top: -20px;">
						<div style="margin-right:25px;float:left;height:35px">
						      <label class="Validform_label">六金城市:</label>
						      <select id="sixGoldCityCh" name="sixGoldCityCh" style="width: 157px;"></select>
			      	          <span class="Validform_checktip"></span>
						</div>
						<div style="margin-right:25px;float:left;height:35px">
						      <label class="Validform_label">六金基数:</label>
					          <%-- <input class="inputxt" id="sixGoldBase" name="sixGoldBase" value="${employeeInfoPage.sixGoldBase}" datatype="/(^\s*$)|(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" /> --%>
			      		      <input class="inputxt" id="sixGoldBaseCh" name="sixGoldBaseCh" maxlength="9" data-options="precision:2,groupSeparator:','"
			      		      value="${employeeInfoPage.sixGoldBaseCh == null?employeeInfoPage.sixGoldBase:employeeInfoPage.sixGoldBaseCh}" autocomplete="off"/>
			      		      <span class="Validform_checktip"></span>
						</div>
			        </div>
		        </div>
		        <div style = "clear:both"></div>
		        <div>
		            <div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">发薪地1:</label>
				          <t:dictSelect field="a1PlaceCh" typeGroupCode="payArea"  defaultVal="${(employeeInfoPage.a1PlaceCh == null||employeeInfoPage.a1PlaceCh == '')?employeeInfoPage.a1Place:employeeInfoPage.a1PlaceCh}" extendJson="{onchange='a1pChange()',style='width: 157px;'}"></t:dictSelect>
		      	          <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">发薪金额1:</label>
		            	  <input class="inputxt" id="a1PaymentCh" name="a1PaymentCh" autocomplete="off" onchange="a1pChange()" maxlength="9" data-options="precision:2,groupSeparator:','"
		            	  value="${employeeInfoPage.a1PaymentCh}"/>
		            	  <span class="Validform_checktip"></span>
					</div>
		        </div>
			    <div style = "clear:both"></div>
		        <div>
		            <div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">发薪地2:</label>
				          <%-- <t:dictSelect field="a2Place" typeGroupCode="payArea" defaultVal="${employeeInfoPage.a2Place}" extendJson="{style='width: 157px;'}"></t:dictSelect> --%>
				          <input class="inputxt" id="a2PlaceCh" name="a2PlaceCh" disabled="disabled" value="${employeeInfoPage.a2PlaceCh}" />
		      	          <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">发薪金额2:</label>
				          <input class="inputxt" id="a2PaymentCh" name="a2PaymentCh" maxlength="9" data-options="precision:2,groupSeparator:','"
				          value="${employeeInfoPage.a2PaymentCh}" disabled="disabled"/>
		            	  <%-- <input class="inputxt" id="a2Payment" name="a2Payment" value="${employeeInfoPage.a2Payment}" disabled="disabled"/><!-- /^[0-9]{1}?+(\.[0-9]{1,2})|[0-9]{0,}(\.[0.9]{1,2})?$/ --> --%>
		            	  <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		   </div>
		</div>
		<input id="id" name="id" type="hidden" value="${employeeInfoPage.id }">
		<input id="editors" name="editors" type="hidden" value="${employeeInfoPage.editors }">
	    <input id="inputName1" name="inputName1" type="hidden" value="${inputName1}">
	    <input id="departflag" name="departflag" type="hidden" value="${departflag}">
  </t:formvalid>
  <script>
  //设置当员工为本部时，部门经理下拉框默认为部门总监且变灰
   function departCharge(){
	   $("#inputerId").empty();
	   inputerName();
   }
   function otherCheck(){
	   var rs = true;
	   var emId = $("#id").val();
	   //校验身份证
		/* rs = codeChake();
		if(!rs){
		 return false;
		} */
		var changeDate = $("input[name='changeDate']").val();
	   //a工资
	   var a = handleNum($("#aStandardSalaryCh").val());
   		//a1工资
   	   var a1 = handleNum($("#a1PaymentCh").val());
   		var a1Place = $("select[name='a1PlaceCh']").val();
	   //a2工资
	   var base = handleNum($("#basePayCh").val());
	   var sexgod = handleNum($("#sixGoldBaseCh").val());
	   var sixplace = $.trim($("#sixGoldCityCh").val());
	   var i = 0;
	   if(changeDate!=""){
		   if(a>0)
			   i++;
		   if(a1>0)
			   i++;
		   if(sexgod>0)
			   i++;
		   if(a1Place!="")
			   i++;
		   if(sixplace!="")
			   i++;
		   if(i==0){

		   }
		   if(i!=5){
			   layer.msg('请填写完整的薪资变动或不要填写薪资变动');
			   return false;
		   }
	   }else{
		   $("#a2PlaceCh").val("");
		   $("#a2PaymentCh").val("");
		   $("#a1PlaceCh").val("");
		   $("select[name='a1PlaceCh']").val("");
		   $("#sixGoldCityCh").val("");
		   $("#sixGoldBaseCh").val("");
		   $("#aStandardSalaryCh").val("");
	   }
	    if(changeDate!=""&&changeDate!=null){
	    	var dates = changeDate.split('-');
	 	   	var now = new Date();
	 	    var month = now.getMonth() + 1;
	    	if(dates[0] < now.getFullYear()||dates[1]<(now.getMonth() + 1)){
		    	layer.msg('不可变更历史信息，请填写正确的变动日期');
				return false;
			}
	    }
	   //$("#a2Payment").val(a2);
	   if(a-a1>0 &&""==($("select[name='a2PlaceCh']").val())){
		   layer.msg('请选择发薪地2');
		   return false;
	   }else if(a-a1<0){
		   layer.msg('发薪金额1 不能大于A（标准）工资');
		   return false;
	   }

	   if($("select[name='a2PlaceCh']").val()==$("select[name='a1PlaceCh']").val()){
		   layer.msg('发薪地1 与 发薪地2 不可一致');
		   return false;
	   }

	   if(""==$.trim($("#sixGoldCity").val())&&sexgod>0){
		   layer.msg('六金地不可为空');
		   return false;
	   }
	   if(rs){
		   $("#a2Place").prop("disabled", false);
		   $("#a2PlaceCh").prop("disabled", false);
		   $("#a2PaymentCh").prop("disabled", false);
		   $("#inputerId").prop("disabled", false);
		   $("#empFlagId").prop("disabled", false);
		   $("#code").prop("disabled", false);
		   $("#a1PaymentCh").prop("disabled", false);
	   }
	   return rs;
   }
   //身份证号校验函数
	function codeChake(){
		 var idCheck = checkIdCard($("#code").val());
		   if(!idCheck){
			   layer.msg('请填写正确的身份证号');
	           return false;
		   }
		   var condition = "code-'"+$("#code").val()+"',delFlg-0";
		   var emId = $("#id").val();
		   if($.trim(emId)!=""){
			   condition+=",id-"+emId;
		   }
		   //校验身份证是否重复
		   $.ajax({
			   url:"employeeInfoController.do?checkFieldRepeat&condition="+condition,
			   type:"get",
			   async:false,
			   success:function(data){
				   data = JSON.parse(data);
				   if(data.errCode == -1){
	                   layer.msg('身份证号在系统中已存在');
	                   return false;
				   }
			   },
			   error:function(){}
		   });
		   return true;
	}
  //六金基数校验函数
  function sixGordCheck(){
	   var sixGord = handleNum($("#sixGoldBase").val());
	   var sixGordPlace = $('#sixGoldCity').val();
	   var sixGordMin = 0;
	   if(""!=sixGordPlace&&""!=sixGord){
		   $.ajax({
			   url:"sixGoldScaleController.do?sixGoldBase&sixGoldCity="+sixGordPlace,
			   type:"get",
			   async:false,
			   success:function(data){
					data = JSON.parse(data);
					console.log("data:"+data);
					 for(var i = 0;i<data.length;i++){
						 if(sixGordMin<data[i])	sixGordMin = data[i]
					 }
			   },
			   error:function(){}
		   });
		   if(sixGordMin-sixGord>0){
				 layer.msg('六金基数不可小于最低基数：'+sixGordMin);
			     return false;
			 }
	   }
	   return true;
  }
   function formatNumber(num,cent,isThousand) {
	    num = num.toString().replace(/\$|\,/g,'');

	    // 检查传入数值为数值类型
	     if(isNaN(num))
	      num = "0";

	    // 获取符号(正/负数)
	    sign = (num == (num = Math.abs(num)));

	    num = Math.floor(num*Math.pow(10,cent)+0.50000000001); // 把指定的小数位先转换成整数.多余的小数位四舍五入
	    cents = num%Math.pow(10,cent);       // 求出小数位数值
	    num = Math.floor(num/Math.pow(10,cent)).toString();  // 求出整数位数值
	    cents = cents.toString();        // 把小数位转换成字符串,以便求小数位长度

	    // 补足小数位到指定的位数
	    while(cents.length<cent)
	     cents = "0" + cents;

	    if(isThousand) {
	     // 对整数部分进行千分位格式化.
	     for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
	      num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));
	    }

	    if (cent > 0)
	     return (((sign)?'':'-') + num + '.' + cents);
	    else
	     return (((sign)?'':'-') + num);
	   }
   var customers={};
   $('#changeDate').focus(function(){
	   if($('#changeDate').val()!=""){
	    $("#aStandardSalaryCh").prop("disabled", false);
   		$("#sixGoldCityCh").prop("disabled", false);
   		$("#sixGoldBaseCh").prop("disabled", false);
   		$("#a1PaymentCh").prop("disabled", false);
   		$("select[name='a1PlaceCh']").prop("disabled", false);
	   }
   });
   $('#changeDate').blur(function(){
	   if($('#changeDate').val()==""){
		   $("#a2PlaceCh").val("");
		   $("#a2PaymentCh").val("");
		   $("#a1PaymentCh").val("");
		   $("#aStandardSalaryCh").val("");
		   $("#aStandardSalaryCh").prop("disabled", true);
	   		$("#sixGoldCityCh").prop("disabled", true);
	   		$("#sixGoldBaseCh").prop("disabled", true);
	   		$("#a1PaymentCh").prop("disabled", true);
	   		$("select[name='a1PlaceCh']").prop("disabled", true);
	   }
   });
   $(document).ready(function(){
	   //本部客户经理变灰，外派则可以选择
	   cityGroup();
	   allDepatr();
	   inputerName();
	   $('#aStandardSalary').numberbox({
		   min:0,
		   precision:2
		});
	   $('#basePay').numberbox({
		    min:0,
		    precision:2
		});
	   $('#meritPay').numberbox({
		    min:0,
		    precision:2
		});
	   $('#sixGoldBase').numberbox({
		   min:0,
		   precision:2
		});
	   $('#a1Payment').numberbox({
		    min:0,
		    precision:2
		});
	   $('#a2Payment').numberbox({
		    min:0,
		    precision:2
		});
	   $('#aStandardSalaryCh').numberbox({
		   min:0,
		   precision:2
		});
	   $('#basePayCh').numberbox({
		    min:0,
		    precision:2
		});
	   $('#meritPayCh').numberbox({
		    min:0,
		    precision:2
		});
	   $('#sixGoldBaseCh').numberbox({
		   min:0,
		   precision:2
		});
	   $('#a1PaymentCh').numberbox({
		    min:0,
		    precision:2
		});
	   $('#a2PaymentCh').numberbox({
		    min:0,
		    precision:2
		});
	   $('#discount').numberbox({
		    precision:0
		});
	   $('#unitPrice').numberbox({
		    min:0,
		    precision:2
		});
	  // a2paycity();
	   //入职日如果为空默认给个默认值
	   if(""==$.trim($("#entryDate").val())){
		   var date = new Date();
		   $("#entryDate").val(new Date().Format("yyyy-MM-dd"));
	   }
	   /* var db= $('#changeDate'); */
	   if ($.fn.datebox){
			$.fn.datebox.defaults.currentText = '清空';
			$.fn.datebox.defaults.closeText = '关闭';
			$.fn.datebox.defaults.okText = '确定';
			$.fn.datebox.defaults.missingMessage = '该输入项为必输项';
			$.fn.datebox.defaults.formatter = function(date){
				var y = date.getFullYear();
				var m = date.getMonth()+1;
				var d = date.getDate();
				return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
			};
			$.fn.datebox.defaults.parser = function(s){
				if (!s) return new Date();
				var ss = s.split('-');
				var y = parseInt(ss[0],10);
				var m = parseInt(ss[1],10);
				var d = parseInt(ss[2],10);
				if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
					return new Date(y,m-1,d);
				} else {
					return new Date();
				}
			};
		}
	   /*db.datebox({
		  	// 显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
	        onShowPanel: function () {
	       	// 触发click事件弹出月份层
	           span.trigger('click');
	           // fix 1.3.x不选择日期点击其他地方隐藏在弹出日期框显示日期面板
	           if (p.find('div.calendar-menu').is(':hidden')){
	           	p.find('div.calendar-menu').show();
	           }
	           // 延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
	           if (!tds) setTimeout(function () {
	               tds = p.find('div.calendar-menu-month-inner td');
	               tds.click(function (e) {
	               	 // 禁止冒泡执行easyui给月份绑定的事件
	                   e.stopPropagation();
	                   // 得到年份
	                   var year = /\d{4}/.exec(span.html())[0];
	                   // 月份，这里不需要+1
	                   var month = parseInt($(this).attr('abbr'), 10);
	                   // 设置隐藏日期对象的值
	                   db.datebox('hidePanel').datebox('setValue', year + '-' + (month < 10 ? ('0' + month) : (month + ''))+'-01');
	            		$("#aStandardSalaryCh").prop("disabled", false);
	            		$("#sixGoldCityCh").prop("disabled", false);
	            		$("#sixGoldBaseCh").prop("disabled", false);
	            		$("#a1PaymentCh").prop("disabled", false);
	            		$("select[name='a1PlaceCh']").prop("disabled", false);
	               });
	           }, 0);
	           // 解绑年份输入框中事件
	           yearIpt.unbind();
	       },
	       parser: function (s) {
	           	if (!s) return new Date();
	           	var arr = s.split('-');
	           	return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
	       },
	       formatter: function (d) {
				var month = d.getMonth() + 1;
	         	return d.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + ''))+'-01';
	       }
	   });*/
	   $('.combo-text.validatebox-text').attr('readonly','readonly');
	/* // 日期选择对象
	    var p = db.datebox('panel');
	   // 日期选择对象中月份
	   var tds = false;
	   // 判断是否是 1.3.x版本
	   var aToday = p.find('a.datebox-current');
	   // 年份输入框
	   var yearIpt = p.find('input.calendar-menu-year');
	   // 显示月份层的触发控件                            1.3.x版本                           1.4.x版本
	   var span = aToday.length ? p.find('div.calendar-title span') : p.find('span.calendar-text');
	   // 1.3.x版本，取消Today按钮的click事件，重新绑定新事件设置日期框为今天，防止弹出日期选择面板
	   if (aToday.length) {
	       aToday.unbind('click').click(function () {
	           var now = new Date();
	           var month = now.getMonth() + 1;
	           db.datebox('hidePanel').datebox('setValue', '');
	           $('#aStandardSalaryCh').numberbox('setValue','');
	           $('#a1PaymentCh').numberbox('setValue','');
	           $('#a2PaymentCh').numberbox('setValue','');
	           $("#aStandardSalaryCh").prop("disabled",true);
			   $("#sixGoldCityCh").prop("disabled",true);
			   $("#sixGoldBaseCh").prop("disabled",true);
			   $("#a1PaymentCh").prop("disabled",true);
			   $("select[name='a1PlaceCh']").prop("disabled",true);
	       });
	   } */
	   var changeDates = "${employeeInfoPage.changeDate}";
	   if(changeDates==""||changeDates==null){
		   $("#aStandardSalaryCh").prop("disabled",true);
		   $("#sixGoldCityCh").prop("disabled",true);
		   $("#sixGoldBaseCh").prop("disabled",true);
		   $("#a1PaymentCh").prop("disabled",true);
		   $("select[name='a1PlaceCh']").prop("disabled",true);
	   }
	   $.ajax({
			  url:"customerInfoController.do?getComboTreeData",
			  type:"get",
			  success:function(data){
				  data = JSON.parse(data);
				  var selectHtml = "<option value='' >--- 请选择 ---</option>";
				  for(var i =0;i<data.length;i++){
					  if("${employeeInfoPage.customerId}"==data[i].id){
					  		selectHtml += "<option value='"+data[i].id+"' selected='selected'>"+data[i].text+"</option>";
					  }else{
						  selectHtml += "<option value='"+data[i].id+"'>"+data[i].text+"</option>";
					  }
					  customers[data[i].id+""]=data[i].attr;
				  }
				  $("#customerId").html(selectHtml);
				  /* cusChange(); */
			  },
			  error:function(){}
		  });
   });

   function allDepatr(){
	   $.ajax({
		   url: "employeeInfoController.do?getAllDepatr",
		   type: "get",
		   success: function(data){
			   data = JSON.parse(data);
			   var departOpt = "<option value='' selected='selected'>--- 请选择 ---</option>";
			   for(var i = 0;i<data.length-1;i+=2){
				   if("${employeeInfoPage.department}"==data[i]){
					   departOpt+= "<option value='"+data[i]+"' selected='selected'>"+data[i+1]+"</option>";
				   }else{
					   departOpt+= "<option value='"+data[i]+"' >"+data[i+1]+"</option>";
				   }
			   }
			   if(data.length == 0){
				   departOpt+= "<option value='' selected='selected'>没有部门</option>";
			   }
			   $("#department").html(departOpt);
		   },
		   error:function(){}
	   });
   }
   function cityGroup(){
	   $.ajax({
		   url: "sixGoldScaleController.do?sixGoldCitys",
		   type: "get",
		   success: function(data){
			   data = JSON.parse(data);
			   var code = 0;
			   var citys = "${employeeInfoPage.sixGoldCityCh}";
			   if(citys == ""){
				   citys = "${employeeInfoPage.sixGoldCity}";
			   }
			   var cityOpt = "<option value='' selected='selected'>--- 请选择 ---</option>";
			   for(var i = 0;i<data.length;i++){
				   if(citys==data[i]){
					   cityOpt+= "<option value='"+data[i]+"' selected='selected'>"+data[i]+"</option>";
					   code = 1;
				   }else{
					   cityOpt+= "<option value='"+data[i]+"' >"+data[i]+"</option>";
				   }
			   }
			   if(code == 0&&""!= "${employeeInfoPage.sixGoldCity}"){
				   cityOpt+= "<option value='"+"${employeeInfoPage.sixGoldCity}"+"' selected='selected'>"+"${employeeInfoPage.sixGoldCity}"+"</option>";
			   }
			   $("#sixGoldCityCh").html(cityOpt);
		   },
		   error:function(){}
	   });
   }
   function a2paycity(){
	   $.ajax({
		   url: "sixGoldScaleController.do?getPayCityGroup",
		   type: "get",
		   success: function(data){
			   data = JSON.parse(data);
			   var cityOpt = "<option value='' selected='selected'>--- 请选择 ---</option>";
			   for(var i = 0;i<data.length;i++){
				   if("${employeeInfoPage.sixGoldCity}"==data[i]){
					   cityOpt+= "<option value='"+data[i]+"' selected='selected'>"+data[i]+"</option>";
				   }else{
					   cityOpt+= "<option value='"+data[i]+"' >"+data[i]+"</option>";
				   }
			   }
			   $("#a2Place").html(cityOpt);
		   },
		   error:function(){}
	   });
   }
   function inputerName(){
	   	var employeeFlag = $("select[name='employeeFlag']").val();
		  var depart = $("select[name='department']").val();
		   if(null == depart){
			   depart = "${employeeInfoPage.department}";
		   }
		   if(null == employeeFlag){
			   employeeFlag = "${employeeInfoPage.employeeFlag}";
		   }
		   if(1==employeeFlag){
				  $.ajax({
					   url: "employeeInfoController.do?getInputerNameDepart&depart="+depart,
					   type: "get",
					   success: function(data){
						   data = JSON.parse(data);
						   var nameOpt="";
						   if(1 == data[0]){
							   var flagOpt = "<option value=1 selected='selected'>OP</option>";
							   $("#empFlagId").empty();
							   $("#empFlagId").html(flagOpt);
							   $("#empFlagId").prop("disabled", true);
							   $("#inputerId").prop("disabled", true);
						   }else{
							   var flagOpt ="<option value=1 selected='selected'>OP</option>";
								flagOpt +="<option value=0 >TECH</option>";
								$("#empFlagId").empty();
							   $("#empFlagId").html(flagOpt);
							   $("#empFlagId").prop("disabled", false);
							   $("#inputerId").prop("disabled", false);
							   nameOpt="<option value='' selected='selected'>--- 请选择 ---</option>";
						   }
						   for(var i = 1;i<data.length-1;i+=2){
							   if("${employeeInfoPage.inputerId}"==data[i]){
								   nameOpt+= "<option value='"+data[i]+"' selected='selected'>"+data[i+1]+"</option>";
							   }else{
								   nameOpt+= "<option value='"+data[i]+"' >"+data[i+1]+"</option>";
							   }
							}
							$("#inputerId").empty();
							$("#inputerId").removeClass('Validform_error');
							$("#inputerId").html(nameOpt);
						$('#unitPrice').val("");
						$("#customerId").prop("disabled",true);
						$("#unitPriceType").prop("disabled",true);
						$("#unitPrice").prop("disabled",true);
						$("#customerId").removeClass('Validform_error');
						$("#unitPriceType").removeClass('Validform_error');
						$("#unitPrice").removeClass('Validform_error');
					   },
						error:function(){}
				  });
			  }else{
				  $.ajax({
					   url: "employeeInfoController.do?getInputerNameDepart&depart="+depart,
					   type: "get",
					   success: function(data){
						   data = JSON.parse(data);
						   if(1 == data[0]){
							   var flagOpt = "<option value=1 selected='selected'>OP</option>";
							   $("#empFlagId").empty();
							   $("#empFlagId").html(flagOpt);
							   $("#empFlagId").prop("disabled", true);
							   var nameOpt="";
							   for(var i = 1;i<data.length-1;i+=2){
									   nameOpt+= "<option value='"+data[i]+"' selected='selected'>"+data[i+1]+"</option>";
							   }
							$("#inputerId").empty();
							$("#inputerId").removeClass('Validform_error');
							$("#inputerId").html(nameOpt);
							$("#inputerId").prop("disabled", true);
							$('#unitPrice').val("");
							$("#customerId").prop("disabled",true);
							$("#unitPriceType").prop("disabled",true);
							$("#unitPrice").prop("disabled",true);
							$("#customerId").removeClass('Validform_error');
							$("#unitPriceType").removeClass('Validform_error');
							$("#unitPrice").removeClass('Validform_error');
						   }else{
							   var flagOpt ="<option value=1 >OP</option>";
								flagOpt +="<option value=0 selected='selected'>TECH</option>";
								$("#empFlagId").empty();
							   $("#empFlagId").html(flagOpt);
							   $("#empFlagId").prop("disabled", false);
							   var nameOpt = "<option value='' selected='selected'>--- 请选择 ---</option>";
							   for(var i = 1;i<data.length-1;i+=2){
								   if("${employeeInfoPage.inputerId}"==data[i]){
									   nameOpt+= "<option value='"+data[i]+"' selected='selected'>"+data[i+1]+"</option>";
								   }else{
									   nameOpt+= "<option value='"+data[i]+"' >"+data[i+1]+"</option>";
								   }
							   }
							   $("#inputerId").prop("disabled", false);
							   $("#inputerId").empty();
							   $("#inputerId").html(nameOpt);
								$("#customerId").prop("disabled",false);
								$("#unitPriceType").prop("disabled",false);
								$("#unitPrice").prop("disabled",false);
						   }
					   },
					   error:function(){}
				   });
			  }
   }
   function inputerIdSUM(){
		  var employeeFlag = $("select[name='employeeFlag']").val();
		  var depart = $("select[name='department']").val();
		  if(null == depart){
			   depart = "${employeeInfoPage.department}";
		   }
		   if(null == employeeFlag){
			   employeeFlag = "${employeeInfoPage.employeeFlag}";
		   }
			  if(1==employeeFlag){
						$('#unitPrice').val("");
						$("#customerId").prop("disabled",true);
						$("#unitPriceType").prop("disabled",true);
						$("#unitPrice").prop("disabled",true);
						$("#customerId").removeClass('Validform_error');
						$("#unitPriceType").removeClass('Validform_error');
						$("#unitPrice").removeClass('Validform_error');
			  }else{
							$("#customerId").prop("disabled",false);
							$("#unitPriceType").prop("disabled",false);
							$("#unitPrice").prop("disabled",false);
			  }
   }
   Date.prototype.Format = function (fmt) { // author: meizz
	    var o = {
	        "M+": this.getMonth() + 1, // 月份
	        "d+": this.getDate(), // 日
	        "h+": this.getHours(), // 小时
	        "m+": this.getMinutes(), // 分
	        "s+": this.getSeconds(), // 秒
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
	        "S": this.getMilliseconds() // 毫秒
	    };
	    if (/(y+)/.test(fmt))
	        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	            return fmt;
	}

    function openDepartmentSelect() {
		$.dialog.setting.zIndex = getzIndex();
		var orgIds = $("#orgIds").val();

		$.dialog({content: 'url:departController.do?departSelect&orgIds='+orgIds+'&isRadio=true', zIndex: getzIndex(), title: '组织机构列表', lock: true, width: '400px', height: '350px', opacity: 0.4, button: [
		   {name: '<t:mutiLang langKey="common.confirm"/>', callback: callbackDepartmentSelect, focus: true},
		   {name: '<t:mutiLang langKey="common.cancel"/>', callback: function (){}}
	   ]}).zindex();

	}
    function callbackDepartmentSelect() {
		  var iframe = this.iframe.contentWindow;
		  var treeObj = iframe.$.fn.zTree.getZTreeObj("departSelect");
		  var nodes = treeObj.getCheckedNodes(true);
		  if(nodes.length>0){
		  var ids='',names='';
		  for(i=0;i<nodes.length;i++){
		     var node = nodes[i];
		     if(i==0){
		    	 ids += node.id;
			     names += node.name;
		     }else{
		    	 ids += ','+node.id;
			     names += ','+node.name;
		     }
		 }
		 $('#departname').val(names);
		 $('#departname').blur();
		 $('#orgIds').val(ids);
		}
	}
    function callbackClean(){
		$('#departname').val('');
		 $('#orgIds').val('');
	}
    function performanceChange(){
    	//a标准
    	var a = handleNum($("#aStandardSalaryCh").val());

    	//a1工资
    	var a1 = handleNum($("#a1PaymentCh").val());
    	var b = handleNum($("#basePayCh").val());
    	if((a1==0||a1==null)&&a-${employeeInfoPage.a1Payment}>=0){
    		a1 = ${employeeInfoPage.a1Payment};
    		$('#a1PaymentCh').numberbox('setValue',parseFloat(a1));
    	}
 	   //赋值
 	   if(a-b<0){
 		  var c = 0;
 	   }else{
 		  var c = formatNumber(a-b,2,1);
 	   }
 	  $("#meritPayCh").numberbox('setValue',c);
    	 if(($("select[name='a1PlaceCh']").val()=="江苏"||$("select[name='a1PlaceCh']").val()=="昆山")){
   			$("#a1PaymentCh").numberbox('setValue',parseFloat(a));
   			$("#a1PaymentCh").prop("disabled",true);
   			$("#a2PlaceCh").val("");
   		  	$("#a2PaymentCh").val("");
   		 $("#a1Payment").removeClass('Validform_error');
   	    	}else{
   	    		$("#a1PaymentCh").prop("disabled",false);
   			 	 if(a1 > 0){
   			 	  if(a-a1<0 ){
   			 		 $("#a2PlaceCh").val("");
   			 		$("#a2PaymentCh").val("");
   					  layer.msg('发薪金额1 不能大于A（标准）工资');
   					   return false;
   				   }else{
   				    		var a2 = a-a1;
   				  		  	if (a-a1>0&&a1>0){
   					  		  	$("#a2PlaceCh").val("江苏");
   					  		  	$("#a2PaymentCh").numberbox('setValue',a2);
   				  		  	}else{
   					  		  	a2 = 0;
   					  		  	$("#a2PlaceCh").val("");
   					  		  	$("#a2PaymentCh").numberbox('setValue','');
   				  		  	}
   				    	}
   			  	   }
    	 }
    }
    function a1pChange(){
    	//a标准
    	var a1 = handleNum($("#a1PaymentCh").val());
    	var a = handleNum($("#aStandardSalaryCh").val());
    	if((a==0||a==null)&&${employeeInfoPage.AStandardSalary}-a1>=0){
    		a = ${employeeInfoPage.AStandardSalary};
    		$('#aStandardSalaryCh').numberbox('setValue',a);
    	}
    	if((a1==0||a1==null)&&a-${employeeInfoPage.a1Payment}>=0){
    		a1 = ${employeeInfoPage.a1Payment};
    		$('#a1PaymentCh').numberbox('setValue',a1);
    	}
    	if(($("select[name='a1PlaceCh']").val()=="江苏"||$("select[name='a1PlaceCh']").val()=="昆山")){
  			$("#a1PaymentCh").numberbox('setValue',a);
  			$("#a1PaymentCh").prop("disabled",true);
  			$("#a2PlaceCh").val("");
  		  	$("#a2PaymentCh").val("");
  		  $("#a1Payment").removeClass('Validform_error');
	    	}else{
	    		$("#a1PaymentCh").prop("disabled",false);
	    		if(a>0){
	    	    	if(a-a1<0){
	    	  		 	$("#a2PlaceCh").val("");
	    	  		 	$("#a2PaymentCh").val("");
	    	  		 	layer.msg('发薪金额1 不能大于A（标准）工资');
	    	 		   return false;
	    	  	   }else{
	    	    		var a2 = a-a1;
	    	  		  	if (a-a1>0&&a1>0){
	    		  		  	$("#a2PlaceCh").val("江苏");
	    		  		  	$("#a2PaymentCh").numberbox('setValue',a2);
	    	  		  	}else{
	    		  		  	a2 = 0;
	    		  		  	$("#a2PlaceCh").val("");
	    		  		  	$("#a2PaymentCh").val("");
	    	  		  	}
	    	  	   }
	    		}
	    	}
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
  //身份证校验
    function checkIdCard(idcard){
 	   var cArray = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2];
 	   //格式是否正确
 	   if(/(^\d{15}$|^\d{17}[0-9xX]{1}$)/.test(idcard)){
 		   var sum = 0;
 		   if(idcard.length==15){
 			   idcard = insert_flg(idcard,"19",6);
 		   }
 		   for(var i = 0;i<cArray.length;i++){
 			   sum += cArray[i] * parseInt(idcard.charAt(i));
 		   }
 		   var checkRemains = ['1','0','X','9','8','7','6','5','4','3','2'];
 		   var s = sum%11;
 		   if(/^([0-9]{1}|10)$/.test(s.toString())){
 			   if(idcard.length==18){
 				  var icLastPosition = idcard.substring(idcard.length-1);
 				  if(checkRemains[s]==icLastPosition.toUpperCase()){
 					 return true;
 				  }
 			   }else{
				  return true;
 			   }
 		   }
 	   }
	   return false;
    }

    function insert_flg(str,flg,sn){
 	   var nStr = "";
 	   for(var i = 0;i<str.length;i++){
 		   if(i==sn){
 			   nStr+=flg;
 		   }
 		   nStr+=str.charAt(i);
 	   }
 	   return nStr;
 	}

  </script>
 </body>