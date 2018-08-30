<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>收支预演</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <style type="text/css">
  	label{
  	    width: 100px;
    	display: inline-block;
    	text-align: right;
  	}
  </style>
 </head>
 <body style="margin-top:0px;">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="employeeDeclareController.do?save" callback="initTableHeaderColor" tiptype="1" beforeSubmit="otherCheck()">
		<div style="width:100%;text-align:center;background:white" class="formtable">
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:15px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 80px;border:1px solid #ababab;">员工基本信息</div>
		        <div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>A(标准):</label>
						<input class="inputxt" id="aStandardSalary" name="aStandardSalary" autocomplete="off" maxlength="10"  data-options="precision:2,groupSeparator:','"
						onchange="salary()" />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						      <label class="Validform_label">六金城市:</label>
						      <select id="sixGoldCity" name="sixGoldCity"  autocomplete="off" style="width: 157px;" onchange="sixGoldScaleChange()"></select>
			      	          <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						      <label class="Validform_label">六金基数:</label>
			      		      <input class="inputxt" id="sixGoldBase" name="sixGoldBase"  maxlength="10"  data-options="precision:2,groupSeparator:','" autocomplete="off"
			      		      onchange="sixGoldChange()"/>
			      		      <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">发薪地1:</label>
					      <t:dictSelect field="a1Place" typeGroupCode="payArea"  extendJson="{onchange='a1pChange()',style='width: 157px;'}"></t:dictSelect>
		      		      <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>发薪金额1:</label>
		            	  <input class="inputxt" id="a1Payment" name="a1Payment" onchange="a1pChange()" autocomplete="off" data-options="precision:2,groupSeparator:','"
		            	  datatype="/^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/"/>
		            	  <span class="Validform_checktip"></span>
		        	</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">B试用折扣率(%):</label>
						<input class="inputxt" id="bDiscount" name="bDiscount" maxlength="3"
						onchange="incomeChange()" autocomplete="off" ignore="ignore" errorMsg="请填写1-100之间的整数"
						datatype="/(^[1-9][0-9]$)|(^[1-9]$)|^100$/"  />
		            	 <span class="Validform_checktip"></span>
					</div>
		        </div>
			    <div style = "clear:both"></div>
		        <div>
		            <div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">发薪地2:</label>
		      		      <input class="inputxt" id=a2Place name="a2Place" disabled="disabled"/>
		      		      <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">发薪金额2:</label>
				          <input class="inputxt" id="a2Payment" name="a2Payment" disabled="disabled"/>
		            	  <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		   </div>

		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 60px;border:1px solid #ababab;">当月收入</div>
		        <div style="margin-top: -25px;">
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>单价方式:</label>
				          <t:dictSelect field="unitPriceType" typeGroupCode="upType"  id="unitPriceType"  extendJson="{style:'width:157px',onchange='incomeChange()'}"></t:dictSelect>
		      	   		  <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>单价:</label>
				          <input class="inputxt" id="unitPrice" name="unitPrice" autocomplete="off"  maxlength="10"  data-options="precision:2,groupSeparator:','"
				          onchange="incomeChange()"/>
		      		      <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="margin-top: -25px;">
		       		<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>约定出勤日数:</label>
				          <input class="inputxt" id="appointedAttendanceDay" name="appointedAttendanceDay" ignore="ignore"
				          errorMsg="请填写1-31之间的数字" onblur="incomeChange()" autocomplete="off" maxlength="5" onchange="incomeChange()"
				          datatype="/^[0-2]?[1-9]([.]{1}[0-9]{1,3})?$/|/^10([.]{1}[0-9]{1,3})?$/|/^20([.]{1}[0-9]{1,3})?$/|/^[3][0](\.([0]{1})?([0]{1})?)?$/" />
                          <span class="Validform_checktip"></span>
					</div>

					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>验收出勤日数:</label>
				          <input class="inputxt" id="acceptedAttendanceDay" name="acceptedAttendanceDay" ignore="ignore"
				          errorMsg="请填写0-31之间的数字" onblur="incomeChange()" autocomplete="off" maxlength="5"
				           onblur="incomeChange()" datatype="/^[0-2]?[0-9]([.]{1}[0-9]{1,3})?$/|/^[3][01]$/" />
				          <span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
		        <div>
		           <div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">当月加算:</label>
				          <input class="inputxt" id="monthOther" name="monthOther"  onchange="incomeChange()" maxlength="10"
				          data-options="precision:2,groupSeparator:','" autocomplete="off"
				          datatype="/(^\s*$)|(^(-)?[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^[0]{1}$)|(^(-)?([0-9])+(\.[0-9]{1,2})?$)/|/^(-)?(\d{1,3})?(,\d{3})*(\.\d+)?$/"  />
			              <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">验收加算:</label>
				          <input class="inputxt" id="acceptanceAdd" name="acceptanceAdd"  onchange="incomeChange()" maxlength="10"
				           data-options="precision:2,groupSeparator:','" autocomplete="off"
				          datatype="/(^\s*$)|(^(-)?[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(-)?(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(-)?(\d{1,3})?(,\d{3})*(\.\d+)?$/" />
		      		      <span class="Validform_checktip"></span>
					</div>
		            <div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">月间调整:</label>
				          <input class="inputxt" id="monthAdjustment" name="monthAdjustment"  onchange="incomeChange()" maxlength="10"
				           data-options="precision:2,groupSeparator:','" autocomplete="off"
				          datatype="/(^\s*$)|(^(-)?[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^(-)?[0-9]\.[0-9]([0-9])?$)/|/^(-)?(\d{1,3})?(,\d{3})*(\.\d+)?$/" />
		      	          <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>流转税:</label>
				          <select name="isTurnoverTax" id="isTurnoverTax"  style="width: 157px;" onchange="incomeChange()">
				          		<option value="1" selected>不免税</option>
				          		<option value="0" >免税</option>
		                  </select>
                          <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		   </div>
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
				<div style="position:relative;background:white;top:-15px;width: 60px;border:1px solid #ababab;">六金个税</div>
				<div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">六金公司负担:</label>
						<input class="inputxt" id="sixCompanyBurdenOne" name="sixCompanyBurdenOne" disabled="disabled"
						 data-options="precision:2,groupSeparator:','"    />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">六金个人负担:</label>
						<input class="inputxt" id="sixPersonalBurdenOne" name="sixPersonalBurdenOne" disabled="disabled"
						 data-options="precision:2,groupSeparator:','"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">个人所得税:</label>
						<input class="inputxt" id="perToneTaxOne" name="perToneTaxOne" disabled="disabled"
						  data-options="precision:2,groupSeparator:','" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
			</div>
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
				<div style="position:relative;background:white;top:-15px;width: 60px;border:1px solid #ababab;">当月损益</div>
				<div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">应付工资:</label>
						<input class="inputxt" id="payableSalary" name="payableSalary" disabled="disabled"   maxlength="9"
						 data-options="precision:2,groupSeparator:','"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-top: -25px;">
						<div style="margin-right:25px;float:left;height:35px">
							<label class="Validform_label">员工到手:</label>
							<input class="inputxt" id="employeeRealSalary" name="employeeRealSalary" disabled="disabled"   maxlength="9"
							 data-options="precision:2,groupSeparator:','"   />
							<span class="Validform_checktip"></span>
						</div>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">总成本:</label>
						<input class="inputxt" id="companyCost" name="companyCost" disabled="disabled"  maxlength="9"
						data-options="precision:2,groupSeparator:','" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
				<div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">收入:</label>
						<input class="inputxt" id="income" name="income" disabled="disabled"   maxlength="9"
						data-options="precision:2,groupSeparator:','"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">净收入:</label>
						<input class="inputxt" id="netIncome" name="netIncome" disabled="disabled"   maxlength="9" data-options="precision:2,groupSeparator:','"  />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">毛利:</label>
						<input class="inputxt" id="companyProfit" name="companyProfit" disabled="disabled"  maxlength="9"
						data-options="precision:2,groupSeparator:','"/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">毛利率(%):</label>
						<input class="inputxt" id="companyProfitRate" name="companyProfitRate" disabled="disabled" />
						<span class="Validform_checktip"></span>
					</div>
				</div>
				<div style="clear:both"></div>
			</div>
		</div>
		<input id="turnoverTax" name="turnoverTax" type="hidden" value="${turnoverTax }">
		<input id="perTaxBase" name="turnoverTax" type="hidden" value="${perTaxBase }">
  </t:formvalid>
  <!-- 金钱正则： /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/ -->
  <script type="text/javascript">
  //新增A标准修改方法
  function salary(){
	  var a = handleNum($('#aStandardSalary').val());
	  if(a!=0){
		  salaryChange2();
		  a1pChange();
		  incomeChange();
	  }
  }
  $(document).ready(function(){
	  /* 对金钱格式进行设置 */
	    //A(标准)
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

	    $('#bDiscount').numberbox({
		    min:0,
		    precision:0
		});
	    //单价:
	    $('#unitPrice').numberbox({
		    min:0,
		    precision:2
		});
	    //当月加算:
	    $('#monthOther').numberbox({
		    //min:0,
		    precision:2
		});
	    //验收加算:
	    $('#acceptanceAdd').numberbox({
		   // min:0,
		    precision:2
		});
	    //月间调整:
	    $('#monthAdjustment').numberbox({
		   // min:0,
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
		    /* min:0, */
		    precision:2
		});
	    //毛利:
	    $('#companyProfit').numberbox({
		    /* min:0, */
		    precision:2
		});
	    //毛利率:
	    $('#companyProfitRate').numberbox({
		    /* min:0, */
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
	    $('#bDiscount').val(100);
	    cityGroup();
  });

  //字符串 格式化 小数点两位 显示
  function formatTwoDecimal(value){
  	if(""!=$.trim(value)){
  		if(value.indexOf(".")!=-1&&value.substring(value.indexOf(".")+1).length==1){
  			return value+"0";
  		}
  	}
  	return value;
  }
 //判断该参数 是数字 还是其他（字符串）
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
  //将 控件的值转化成可参与计算的 浮点数
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
  //将 数字格式化两位小数 并转化成 千分位字符串显示
  function myFormat(num) {
	  return (num.toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
  }
// 薪资变动
/* function salaryChange() {
	  //计算绩效工资
	var aStandardSalary = handleNum($('#aStandardSalary').val());
	var basePay = handleNum($('#basePay').val());
	if(aStandardSalary==0) {
		$('#a1Payment').numberbox('setValue','');
	}else {
		if(basePay>aStandardSalary) {
			$('#meritPay').numberbox('setValue',0);
		}else{
			$('#meritPay').numberbox('setValue',aStandardSalary-basePay);
		}
		 		$("#a1Payment").val(myFormat(Math.round(parseFloat(aStandardSalary))));
		$('#a1Payment').numberbox('setValue',aStandardSalary);
 	}

	$("#a2Place").val("");
	$("#a2Payment").val("");
	incomeChange();
} */
function salaryChange2() {
	  //计算绩效工资
	var aStandardSalary = handleNum($('#aStandardSalary').val());
	var basePay = handleNum($('#basePay').val());
	if(basePay>aStandardSalary) {
		$('#meritPay').numberbox('setValue',0);
	}else{
		$('#meritPay').numberbox('setValue',aStandardSalary-basePay);
	}
}
  //六金城市改变
  function sixGoldScaleChange() {
	  var sixGoldPlace = $('#sixGoldCity').val();
	  if(""==sixGoldPlace||sixGoldPlace=="undefined") {
		  $("#sixGoldBase").numberbox('setValue',0);
		  $("#sixGoldBase").val('');
		  return;
	  }
	  var sixGoldBase = handleNum($('#sixGoldBase').val());
	  if(sixGoldBase==0) {
		  return;
	  }else {
		  sixGoldChange();
	  }
  }
  //六金动态计算
  function sixGoldChange() {
	  var sixGoldBase = handleNum($('#sixGoldBase').val());
	  var sixGoldPlace = $('#sixGoldCity').val();
	  if(""==sixGoldPlace||sixGoldPlace=="undefined") {
		  layer.alert("请先选择六金城市");
		  //$("#sixGoldBase").val('');
		  return;
	  }
	  if(sixGoldBase>0){
	  	calSixGold(sixGoldBase,sixGoldPlace);
	  }else{
		  $('#sixGoldBase').val(0.0);
		  $('#sixCompanyBurdenOne').val(0.0);
		  $('#sixPersonalBurdenOne').val(0.0);
		  incomeChange();
	  }
  }
  function calSixGold(sixGoldBase,sixGoldPlace) {
	//六金 计算
	$.ajax({
		url:"employeeDeclareController.do?calculateSixGold&sixGoldPlace="+encodeURI(encodeURI(sixGoldPlace),'UTF-8')+"&sixGoldBase="+sixGoldBase,
		type:"get",
		async:false,
		success:function(data) {
			data=JSON.parse(data);
			$("#sixCompanyBurdenOne").val(myFormat(Math.round(parseFloat(data.companySum)*100)/100));
			$("#sixPersonalBurdenOne").val(myFormat(Math.round(parseFloat(data.personalSum)*100)/100));
			incomeChange();
		},
		error:function(){layer.alert("网络异常");}
	});
  }
  //当月损益动态计算
  function incomeChange(){
		//公司 收入 计算
		//单价方式
		var unitPriceType = $("#unitPriceType option:selected").val();
		//单价
		var unitPrice = handleNum($("#unitPrice").val());
		//约定出勤日
		var appointedAttendanceDay = handleNum($("#appointedAttendanceDay").val());
		//验收出勤日
		var acceptedAttendanceDay = handleNum($("#acceptedAttendanceDay").val());
		//当月加算
		var monthOther = handleNum($("#monthOther").val());
		//验收加算
		var acceptanceAdd = handleNum($("#acceptanceAdd").val());
		//月间调整
		var monthAdjustment = handleNum($("#monthAdjustment").val());

		if(""!=$.trim(unitPriceType)){
			if(appointedAttendanceDay==0) {
			}else {
				switch(parseInt(unitPriceType)){
			  	case 0:
			  		 income = unitPrice*acceptedAttendanceDay/appointedAttendanceDay+monthOther+acceptanceAdd+monthAdjustment;
			  		 break;
			  	case 2:
			  		income = unitPrice*acceptedAttendanceDay+monthOther+acceptanceAdd+monthAdjustment;
				     break;
			  	case 3:
			  		income = unitPrice*8*acceptedAttendanceDay+monthOther+acceptanceAdd+monthAdjustment;
			  	}

			  	$("#income").val(myFormat(Math.round(parseFloat(income)*100)/100));
			}
		}
		/*公司收入*/
		var income = handleNum($("#income").val());
		var isTurnoverTax = handleNum($("#isTurnoverTax").val());
		var tornoverTax=handleNum($("#turnoverTax").val());
		if(appointedAttendanceDay!=0) {
			if(isTurnoverTax==0) { //不计算流转税
				$('#netIncome').val(myFormat(Math.round(parseFloat(income)*100)/100));
			}else {
				$('#netIncome').val(myFormat(Math.round(parseFloat(income*(1-tornoverTax/100))*100)/100));
			}
		}
		var netIncome = handleNum($("#netIncome").val());
		//应付工资   应付基本工资+应付绩效工资+补贴合计
		//试用折扣率
		/* var bDiscount = $('#bDiscount').numberbox('getValue'); */
		var bDiscount = handleNum($("#bDiscount").val());
		if(bDiscount==""){
			bDiscount=100;
		}

		var aStandardSalary = handleNum($('#aStandardSalary').val());
		//基本工资
		var basePay = handleNum($('#basePay').val());
		//绩效工资
		var meritSalary = aStandardSalary-basePay;
		//补贴合计
		var totalSubsidy = 0;

		//应付基本工资
		var payableBaseSalary = basePay*bDiscount/100;
		//应付绩效：  有绩效出勤日/法定出勤日*A2绩效*B折扣率
		var payablePerformance = meritSalary*bDiscount/100;
		var payableSalary = payableBaseSalary+payablePerformance+totalSubsidy;
		$("#payableSalary").val(myFormat(Math.round(parseFloat(payableSalary)*100)/100));
		//应付工资(应付合计)
		var payableSalary = handleNum($("#payableSalary").val());
		//员工到手 计算    （应付工资-个税-个人六金负担）
		//员工六金负担
		var perTaxBase = handleNum($("#perTaxBase").val());
		var sixPersonalBurdenOne = handleNum($("#sixPersonalBurdenOne").val());
			//个税计算
			var a1Payment = handleNum($("#a1Payment").val());
			var a2Payment = handleNum($("#a2Payment").val());
			//个税计算
			if(a2Payment==0||payableSalary-a1Payment<=0) { //一个发薪地或者当月扣钱太多发薪地2的扣光了  应付工资-员工六金负担
				var tax = calPersonalTax(payableSalary-sixPersonalBurdenOne,perTaxBase);
				$("#perToneTaxOne").val(myFormat(Math.round(parseFloat(tax)*100)/100));
			}else {  //两个发薪地  发薪地2为江苏
				var tax1 = calPersonalTax(a1Payment-sixPersonalBurdenOne,3500); //发薪地1 - 个人六金
				var tax2 = calPersonalTax(a2Payment+payableSalary-basePay-meritSalary,perTaxBase); //发薪地2 + 当月浮动
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
		if(appointedAttendanceDay!=0) {
			//毛利  收入-总成本
			var companyProfit = netIncome - companyCost;
			$("#companyProfit").val(myFormat(Math.round(parseFloat(companyProfit)*100)/100));
		}
		if(netIncome==0||appointedAttendanceDay==0) {
			$("#companyProfitRate").val('');
		}else {
			//毛利率  毛利/收入
			if(companyProfit<0&&netIncome<0){
				var companyProfitRate = (-companyProfit)/(-netIncome);
			}else if(companyProfit<0){
				var companyProfitRate = -((-companyProfit)/netIncome);
			}else {
				var companyProfitRate = companyProfit/netIncome;
			}
			$("#companyProfitRate").val(myFormat(Math.round(parseFloat(companyProfitRate*100)*100)/100));
		}
  }
  //获取系统中所有六金地区
  function cityGroup(){
	   $.ajax({
		   url: "sixGoldScaleController.do?sixGoldCitys",
		   type: "get",
		   success: function(data){
			   data = JSON.parse(data);
			   var code = 0;
			   var cityOpt = "<option value='' selected='selected'>--- 请选择 ---</option>";
			   for(var i = 0;i<data.length;i++){
					   cityOpt+= "<option value='"+data[i]+"' >"+data[i]+"</option>";
			   }
			   $("#sixGoldCity").html(cityOpt);
		   },
		   error:function(){}
	   });
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

function a1pChange(){
	//a标准
	var a1 = handleNum($("#a1Payment").val());
	var a = handleNum($("#aStandardSalary").val());
	if(a>0){
    	if(a-a1<0){
  		 	$("#a2Place").val("");
  		 	$("#a2Payment").val("");
  		 	layer.msg('发薪金额1 不能大于A（标准）工资');
 		   return false;
  	   }else{
  		  	var a2 = formatNumber(a-a1,2,1);
  		  	if (a-a1>0&&a1>0){
  		  		$("#a2Place").val("江苏");
  		  		$("#a2Payment").val(a2);
  		  	}else{
  		  		a2 = 0;
  		  	$("#a2Place").val("");
  		  $("#a2Payment").val("");
  		  	}
	    	if($("select[name='a1Place']").val()=="江苏"&&a-a1>0&&a1>0){
	    		$("select[name='a1Place']").val("");
	    		layer.msg('发薪金额1小于A(标准)，此时发薪地1不可选择为江苏');
	    		return false;
	    	}
  	   }
    	incomeChange();
	}
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
  </script>
 </body>