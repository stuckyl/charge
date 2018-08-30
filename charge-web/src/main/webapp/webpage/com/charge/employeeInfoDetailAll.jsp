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
   <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="#" tiptype="1">
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
					      <label class="Validform_label"><span style="color:red">*</span>离职日:</label>
				          <input class="Wdate" onClick="WdatePicker()"  style="width: 155px;padding: 4px 0px;border: 1px solid #D7D7D7;border-radius: 3px" id="quitDate" name="quitDate"  value="<fmt:formatDate value='${employeeInfoPage.quitDate}' type="date" pattern="yyyy-MM-dd"/>" datatype="*"/>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>离职理由    :</label>
				          <input class="inputxt" id="quitReason" name="quitReason" datatype="*" autocomplete="off" value="${employeeInfoPage.quitReason==1?'转正岗':employeeInfoPage.quitReason==0?'其他原因':''}" maxlength="30" />
				          <span class="Validform_checktip"></span>
					</div>
					</div>
		        <div style="clear:both"></div>
		   		<div>
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
			          <input class="Wdate" onClick="WdatePicker()"  style="width: 157px;padding: 4px 0px;border: 1px solid #D7D7D7;border-radius: 3px" id="changeDate" name="changeDate"  value="<fmt:formatDate value='${employeeInfoPage.changeDate}' type="date" pattern="yyyy-MM-dd"/>"/>
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
				          <input class="inputxt" id="basePayCh" name="basePayCh" disabled="disabled" maxlength="9" autocomplete="off" value="${employeeInfoPage.AStandardSalaryCh==0?0:employeeInfoPage.basePay}" data-options="precision:2,groupSeparator:','" />
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">绩效工资:</label>
		            	  <input class="inputxt" id="meritPayCh" name="meritPayCh"  maxlength="9" disabled="disabled" data-options="precision:2,groupSeparator:','"
		            	  value="${employeeInfoPage.AStandardSalaryCh ==0?employeeInfoPage.AStandardSalaryCh - employeeInfoPage.basePay:0}" />
		            	  <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style = "clear:both"></div>
		        <div>
					<br>
			        <div style="margin-top: -20px;">
						<div style="margin-right:25px;float:left;height:35px">
						      <label class="Validform_label">六金城市:</label>
						      <input class="inputxt" id="sixGoldCity" name="sixGoldCity" datatype="*" value="${employeeInfoPage.sixGoldCityCh}" disabled="disabled"></input>
			      	          <span class="Validform_checktip"></span>
						</div>
						<div style="margin-right:25px;float:left;height:35px">
						      <label class="Validform_label">六金基数:</label>
					          <%-- <input class="inputxt" id="sixGoldBase" name="sixGoldBase" value="${employeeInfoPage.sixGoldBase}" datatype="/(^\s*$)|(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/" /> --%>
			      		      <input class="inputxt" id="sixGoldBaseCh" name="sixGoldBaseCh" maxlength="9" data-options="precision:2,groupSeparator:','"
			      		      value="${employeeInfoPage.sixGoldBaseCh}" autocomplete="off"/>
			      		      <span class="Validform_checktip"></span>
						</div>
			        </div>
		        </div>
		        <div style = "clear:both"></div>
		        <div>
		            <div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">发薪地1:</label>
				          <t:dictSelect field="a1PlaceCh" typeGroupCode="payArea"  defaultVal="${employeeInfoPage.a1PlaceCh}" extendJson="{onchange='a1pChange()',style='width: 157px;'}"></t:dictSelect>
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
  $(document).ready(function(){
	   //本部客户经理变灰，外派则可以选择
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
	   $.ajax({
			  url:"customerInfoController.do?getComboTreeData",
			  type:"get",
			  success:function(data){
				  data = JSON.parse(data);
				  var selectHtml = "<option value='' >--- 请选择  ---</option>";
				  for(var i =0;i<data.length;i++){
					  if("${employeeInfoPage.customerId}"==data[i].id){
					  		selectHtml += "<option value='"+data[i].id+"' selected='selected'>"+data[i].text+"</option>";
					  }else{
						  selectHtml += "<option value='"+data[i].id+"'>"+data[i].text+"</option>";
					  }
					  //customers[data[i].id+""]=data[i].attr;
				  }
				  $("#customerId").empty();
				  $("#customerId").html(selectHtml);
				  /* cusChange(); */
			  },
			  error:function(){}
		  });
  });
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
							   nameOpt="<option value='' selected='selected'>-- 请选择 -- </option>";
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
							   var nameOpt = "<option value='' selected='selected'>--- 请选择 ---</option>";
							   for(var i = 1;i<data.length-1;i+=2){
								   if("${employeeInfoPage.inputerId}"==data[i]){
									   nameOpt+= "<option value='"+data[i]+"' selected='selected'>"+data[i+1]+"</option>";
								   }else{
									   nameOpt+= "<option value='"+data[i]+"' >"+data[i+1]+"</option>";
								   }
							   }
							   $("#inputerId").empty();
							   $("#inputerId").html(nameOpt);
						   }
					   },
					   error:function(){}
				   });
			  }
 }
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
  </script>
 </body>