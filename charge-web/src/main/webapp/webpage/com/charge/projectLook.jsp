<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>项目信息表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <style type="text/css">
  	label{
  	    width: 100px;
    	display: inline-block;
    	text-align: right;
  	}
  </style>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="projectController.do?save"  tiptype="1" beforeSubmit="otherCheck()">
			<input id="id" name="id" type="hidden" value="${projectPage.id }">
		<div style="width:100%;text-align:center;background:white" class="formtable">
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 60px;">项目信息</div>
		        <div style="margin-top: -15px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>客户:</label>
						<select name="projectCustomer1" id="projectCustomer1"  datatype="*" style="width: 155px;"> </select>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					  <label class="Validform_label"><span style="color:red">*</span>收入:</label>
					  <input class="inputxt" id="projectIncome" name="projectIncome" value="${projectPage.projectIncome}" style="width: 148px;"
					  onchange="forMoney()" maxlength="9" autocomplete="off" data-options="precision:2,groupSeparator:','"
					   datatype="*"/>
					  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>流转税:</label>
				          <select name="isturnovertax" id="isturnovertax"  datatype="*" style="width: 157px;"onchange="transferChange()" >
				          		<option value="1" <c:if test="${projectPage.isturnovertax==1}">selected</c:if> >不免税</option>
				          		<option value="0" <c:if test="${projectPage.isturnovertax==0}">selected</c:if> >免税</option>
		                  </select>
                          <span class="Validform_checktip"></span>
					</div>

		        </div>
		        <div style="clear:both"></div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">总支出:</label>
				          <input class="inputxt" id="projectPay" name="projectPay" autocomplete="off" style="width: 148px;"
				           value="${projectPage.projectPay}" maxlength="9" disabled="true"  data-options="precision:2,groupSeparator:','"/>
		            	  <span class="Validform_checktip"></span>
					</div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">项目毛利:</label>
				          <input class="inputxt" id="projectProfit" name="projectProfit" autocomplete="off" style="width: 148px;"
				           value="${projectPage.projectProfit}" maxlength="9" disabled="true"  data-options="precision:2,groupSeparator:','"/>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">净利润:</label>
				          <input class="inputxt" id="projectPay1" name="projectPay1" autocomplete="off" style="width: 150px;"
				           value="${projectPay1}" maxlength="9" disabled="true"  data-options="precision:2,groupSeparator:','"/>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">净利率(%):</label>
						<input class="inputxt" id="projectProfitRate" name="projectProfitRate" autocomplete="off" value="${projectPage.projectProfitRate}" ignore="ignore" maxlength="99" disabled="true" />
						<span class="Validform_checktip"></span>
					</div>
			   </div>
		        <div style="clear:both"></div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">c1类型:</label>
					      <select id="c1Other" name="c1Other"  onchange="gradeChange()" >
								<option value="-1" >---请选择---</option>
								<option value="0" >内部</option>
								<option value="1" >外部</option>
					      </select>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">c1供应商:</label>
						 <select id="c1OtherSupplier" name="c1OtherSupplier" >
								<option value="-1" >---请选择---</option>
					      </select>
					     <span class="Validform_checktip"></span>
					</div>
				   <div style="margin-right:25px;float:left;height:35px;">
						<label class="Validform_label">c1金额:</label>
						<input class="inputxt" id="c1OtherMoney" name="c1OtherMoney" autocomplete="off" value="${projectPage.c1OtherMoney}"
						onchange="forMoney()" ignore="ignore" maxlength="9" data-options="precision:2,groupSeparator:','" autocomplete="off"
						datatype="/(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/" />
						 <span class="Validform_checktip"></span>
					</div>
				   <div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">c1备注:</label>
						<input class="inputxt" id="c1OtherRemarks" name="c1OtherRemarks" autocomplete="off" value="${projectPage.c1OtherRemarks}" ignore="ignore" maxlength="255" />
						 <span class="Validform_checktip"></span>
					</div>
			   </div>
			   <div style="clear:both"></div>
		        <div>
		       		<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">c2类型:</label>
					      <select id="c2Other" name="c2Other"  onchange="gradeChange()" >
								<option value="-1" >---请选择---</option>
								<option value="0" >内部</option>
								<option value="1" >外部</option>
					      </select>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">c2供应商:</label>
						 <select id="c2OtherSupplier" name="c2OtherSupplier">
								<option value="-1" >---请选择---</option>
					      </select>
					     <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px;">
						<label class="Validform_label">c2金额:</label>
						<input class="inputxt" id="c2OtherMoney" name="c2OtherMoney" autocomplete="off" value="${projectPage.c2OtherMoney}"
						onchange="forMoney()" ignore="ignore" maxlength="9" data-options="precision:2,groupSeparator:','" autocomplete="off"
						datatype="/(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/" />
						 <span class="Validform_checktip"></span>
					</div>

					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">c2备注:</label>
						<input class="inputxt" id="c2OtherRemarks" name="c2OtherRemarks" autocomplete="off" value="${projectPage.c2OtherRemarks}" ignore="ignore" maxlength="255" />
						 <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">c3类型:</label>
					      <select id="c3Other" name="c3Other"  onchange="gradeChange()" >
								<option value="-1" >---请选择---</option>
								<option value="0" >内部</option>
								<option value="1" >外部</option>
					      </select>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">c3供应商:</label>
						 <select id="c3OtherSupplier" name="c3OtherSupplier">
								<option value="-1" >---请选择---</option>
					      </select>
					     <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px;">
						<label class="Validform_label">c3金额:</label>
						<input class="inputxt" id="c3OtherMoney" name="c3OtherMoney" autocomplete="off" value="${projectPage.c3OtherMoney}"
						 onchange="forMoney()" ignore="ignore" maxlength="9" data-options="precision:2,groupSeparator:','" autocomplete="off"
						datatype="/(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/" />
						 <span class="Validform_checktip"></span>
					</div>

					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">c3备注:</label>
						<input class="inputxt" id="c3OtherRemarks" name="c3OtherRemarks" autocomplete="off" value="${projectPage.c3OtherRemarks}" ignore="ignore" maxlength="255" />
						 <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">c4类型:</label>
					      <select id="c4Other" name="c4Other"  onchange="gradeChange()" >
								<option value="-1" >---请选择---</option>
								<option value="0" >内部</option>
								<option value="1" >外部</option>
					      </select>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">c4供应商:</label>
						 <select id="c4OtherSupplier" name="c4OtherSupplier">
								<option value="-1" >---请选择---</option>
					      </select>
					     <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px;">
						<label class="Validform_label">c4金额:</label>
						<input class="inputxt" id="c4OtherMoney" name="c4OtherMoney" autocomplete="off" value="${projectPage.c4OtherMoney}"
						 onchange="forMoney()" ignore="ignore" maxlength="9" data-options="precision:2,groupSeparator:','" autocomplete="off"
						datatype="/(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/" />
						 <span class="Validform_checktip"></span>
					</div>

					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">c4备注:</label>
						<input class="inputxt" id="c4OtherRemarks" name="c4OtherRemarks" autocomplete="off" value="${projectPage.c4OtherRemarks}" ignore="ignore" maxlength="255" />
						 <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label">c5类型:</label>
					      <select id="c5Other" name="c5Other"  onchange="gradeChange()" >
								<option value="-1" >---请选择---</option>
								<option value="0" >内部</option>
								<option value="1" >外部</option>
					      </select>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">c5供应商:</label>
						 <select id="c5OtherSupplier" name="c5OtherSupplier">
					      </select>
					     <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px;">
						<label class="Validform_label">c5金额:</label>
						<input class="inputxt" id="c5OtherMoney" name="c5OtherMoney" autocomplete="off" value="${projectPage.c5OtherMoney}"
						 onchange="forMoney()" ignore="ignore" maxlength="9"  data-options="precision:2,groupSeparator:','" autocomplete="off"
						datatype="/(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/" />
						 <span class="Validform_checktip"></span>
					</div>

					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">c5备注:</label>
						<input class="inputxt" id="c5OtherRemarks" name="c5OtherRemarks" autocomplete="off" value="${projectPage.c5OtherRemarks}" ignore="ignore" maxlength="255" />
						 <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <input id="turnoverTax" name="turnoverTax" value="${turnoverTax }"  type="hidden"></input>
		   </div>
		</div>
	</t:formvalid>
 </body>
 <script >
 //流转税
 function transferChange(){
	 //流转税  0-无   1-有
	 var isturnovertax = $("#isturnovertax").val();
	 //总支出
	 var projectPay = handleNum($("#projectPay").val());
	 //总收入
	 var projectIncome = handleNum($("#projectIncome").val());
	 //毛利
	 var projectProfit = handleNum($("#projectProfit").val());
	 //流转税
	 var turnoverTax = $("#turnoverTax").val();
	//altre("turnoverTax");
	if(projectProfit>0){
		if(isturnovertax==1){
			//净利润
			var projectPay2=projectProfit*(1-turnoverTax/100);
			//毛利率
			var projectProfitRate = (projectPay2/projectIncome)*100;

			$("#projectPay1").val(myFormat(Math.round(parseFloat(projectPay2)*100)/100));
			$("#projectProfitRate").val(myFormat(Math.round(parseFloat(projectProfitRate)*100)/100));
		 }else{
			//净利润
			 var projectPay2=projectProfit;
			//毛利率
			 var projectProfitRate = (projectPay2/projectIncome)*100;

			 $("#projectPay1").val(myFormat(Math.round(parseFloat(projectPay2)*100)/100));
			$("#projectProfitRate").val(myFormat(Math.round(parseFloat(projectProfitRate)*100)/100));
		 }
	}else{
		$("#projectPay1").val("");
		$("#projectProfitRate").val("");
	}
 }

 var customers={};
 var customers1={};
 $(document).ready(function(){
	 mixAndNumber();
	 gradeChange1();
	 var c1 = "${projectPage.c1Other}";
	 if(c1!=""){
		 if(c1==0){
			 $("#c1Other").val("0");
		 }
		 if(c1==1){
			 $("#c1Other").val("1");
		 }
	 }
	 var c2 = "${projectPage.c2Other}";
	 if(c2!=""){
		 if(c2==0){
			 $("#c2Other").val("0");
		 }
		 if(c2==1){
			 $("#c2Other").val("1");
		 }
	 }
	 var c3 = "${projectPage.c3Other}";
	 if(c3!=""){
		 if(c3==0){
			 $("#c3Other").val("0");
		 }
		 if(c3==1){
			 $("#c3Other").val("1");
		 }
	 }
	 var c4 = "${projectPage.c4Other}";
	 if(c4!=""){
		 if(c4==0){
			 $("#c4Other").val("0");
		 }
		 if(c4==1){
			 $("#c4Other").val("1");
		 }
	 }
	 var c5 = "${projectPage.c5Other}";
	 if(c5!=""){
		 if(c5==0){
			 $("#c5Other").val("0");
		 }
		 if(c5==1){
			 $("#c5Other").val("1");
		 }
	 }
	 $.ajax({//上游客户
		  url:"customerInfoController.do?getComboTreeData",
		  type:"get",
		  success:function(data){
			  data = JSON.parse(data);
			  var selectHtml = "<option value='' >--- 请选择  ---</option>";
			  for(var i =0;i<data.length;i++){
				  if("${projectPage.projectCustomer1}"==data[i].id){
				  		selectHtml += "<option value='"+data[i].id+"' selected='selected'>"+data[i].text+"</option>";
				  }else{
					  selectHtml += "<option value='"+data[i].id+"'>"+data[i].text+"</option>";
				  }
				  customers[data[i].id+""]=data[i].attr;
			  }
			  $("#projectCustomer1").html(selectHtml);
			  /* cusChange(); */
		  },
		  error:function(){}
	  });
	 $.ajax({//下游供应商
		  url:"suppliersController.do?getComboTreeData",
		  type:"get",
		  success:function(data){
			  data = JSON.parse(data);
			  var selectHtml1 = "<option value='' >--- 请选择 ---</option>";
			  var selectHtml2 = "<option value='' >--- 请选择 ---</option>";
			  var selectHtml3 = "<option value='' >--- 请选择 ---</option>";
			  var selectHtml4 = "<option value='' >--- 请选择 ---</option>";
			  var selectHtml5 = "<option value='' >--- 请选择 ---</option>";

			  for(var i =0;i<data.length;i++){
				  //c1
				  if("${projectPage.c1OtherSupplier}"==data[i].id){
				  		selectHtml1 += "<option value='"+data[i].id+"' selected='selected'>"+data[i].text+"</option>";
				  }else{
					  selectHtml1 += "<option value='"+data[i].id+"'>"+data[i].text+"</option>";
				  }
				  //c2
				  if("${projectPage.c2OtherSupplier}"==data[i].id){
				  		selectHtml2 += "<option value='"+data[i].id+"' selected='selected'>"+data[i].text+"</option>";
				  }else{
					  selectHtml2 += "<option value='"+data[i].id+"'>"+data[i].text+"</option>";
				  }
				  //c3
				  if("${projectPage.c3OtherSupplier}"==data[i].id){
				  		selectHtml3 += "<option value='"+data[i].id+"' selected='selected'>"+data[i].text+"</option>";
				  }else{
					  selectHtml3 += "<option value='"+data[i].id+"'>"+data[i].text+"</option>";
				  }
				  //c4
				  if("${projectPage.c4OtherSupplier}"==data[i].id){
				  		selectHtml4 += "<option value='"+data[i].id+"' selected='selected'>"+data[i].text+"</option>";
				  }else{
					  selectHtml4 += "<option value='"+data[i].id+"'>"+data[i].text+"</option>";
				  }
				  //c5
				  if("${projectPage.c5OtherSupplier}"==data[i].id){
				  		selectHtml5 += "<option value='"+data[i].id+"' selected='selected'>"+data[i].text+"</option>";
				  }else{
					  selectHtml5 += "<option value='"+data[i].id+"'>"+data[i].text+"</option>";
				  }
				  customers1[data[i].id+""]=data[i].attr;
			  }
			  $("#c1OtherSupplier").html(selectHtml1);
			  $("#c2OtherSupplier").html(selectHtml2);
			  $("#c3OtherSupplier").html(selectHtml3);
			  $("#c4OtherSupplier").html(selectHtml4);
			  $("#c5OtherSupplier").html(selectHtml5);
			  /* cusChange(); */
		  },
		  error:function(){}
	  });

 });
 //设置最小值与小数点位数
 function mixAndNumber(){
	//收入
    $('#projectIncome').numberbox({
	    min:0,
	    precision:2
	});
	//收入
    $('#c1OtherMoney').numberbox({
    	min:0,
	    precision:2
	});
	//收入
    $('#c2OtherMoney').numberbox({
    	min:0,
	    precision:2
	});
	//收入
    $('#c3OtherMoney').numberbox({
    	min:0,
	    precision:2
	});
	//收入
    $('#c4OtherMoney').numberbox({
    	min:0,
	    precision:2
	});
	//收入
    $('#c5OtherMoney').numberbox({
    	min:0,
	    precision:2
	});
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
 function forMoney(){
	 var a= 0;
	 var b= 0;
	 //收入
	 var projectIncome=
		 handleNum($("#projectIncome").val()==null?0.0:$("#projectIncome").val());
	 if(isNaN(projectIncome)){
		 a=1;
	 }
	 //c1
	 var c1OtherMoney=
		 handleNum($("#c1OtherMoney").val()==null?0.0:$("#c1OtherMoney").val());
	 if(isNaN(c1OtherMoney)){
		 b=1;
	 }
	 //c2
	 var c2OtherMoney=
		 handleNum($("#c2OtherMoney").val()==null?0.0:$("#c2OtherMoney").val());
	 if(isNaN(c2OtherMoney)){
		 b=1;
	 }
	 //c3
	 var c3OtherMoney=
		 handleNum($("#c3OtherMoney").val()==null?0.0:$("#c3OtherMoney").val());
	 if(isNaN(c3OtherMoney)){
		 b=1;
	 }
	 //c4
	 var c4OtherMoney=
		 handleNum($("#c4OtherMoney").val()==null?0.0:$("#c4OtherMoney").val());
	 if(isNaN(c4OtherMoney)){
		 b=1;
	 }
	 //c5
	 var c5OtherMoney=
		 handleNum($("#c5OtherMoney").val()==null?0.0:$("#c5OtherMoney").val());
	 if(isNaN(c5OtherMoney)){
		 b=1;
	 }

	 //总支出
	 var projectPay=c1OtherMoney+c2OtherMoney+c3OtherMoney+c4OtherMoney+c5OtherMoney;

	//毛利
	 if(a!=1){
		 var projectProfit=projectIncome-projectPay;
	}else{
	 var projectProfit=0-projectPay;
	}

	 if(projectIncome!=0&&projectPay!=0){
		//总支出
		 $("#projectPay").val(myFormat(Math.round(parseFloat(projectPay)*100)/100));

		 $("#projectProfit").val(myFormat(Math.round(parseFloat(projectProfit)*100)/100));
		 //毛利率
		 if(projectProfit>=0){
			var projectProfitRate = projectProfit/projectIncome;
			$("#projectProfitRate").val(myFormat(Math.round(parseFloat(projectProfitRate*100)*100)/100));
		 }else{
			$("#projectProfitRate").val("");
		 }
	 }else if(projectIncome==0&&projectPay!=0){
		 $("#projectPay").val(myFormat(Math.round(parseFloat(projectPay)*100)/100));
		 $("#projectProfit").val(myFormat(Math.round(parseFloat(projectProfit)*100)/100));
		 $("#projectProfitRate").val("");
	 }else {
		$("#projectPay").val("");
		$("#projectProfit").val("");
		$("#projectProfitRate").val("");
	 }
	if(a==1){
		$("#projectPay").val("");
	}
	if(b==1){
		$("#projectProfit").val("");
		$("#projectProfitRate").val("");
	}
	transferChange();
 }

 function gradeChange(){

	 if($("#c1Other").val()==-1&&$("#c2Other").val()==-1
			 &&$("#c3Other").val()==-1&&$("#c4Other").val()==-1
			 &&$("#c5Other").val()==-1)
	 {
		 $("#projectPay").val("");
		 $("#projectProfit").val("");
		 $("#projectProfitRate").val("");
	 }

	 //var c1Other=$("#c1Other option:selected").val();
	 var c1Other=$("#c1Other").val();
	 var c2Other=$("#c2Other").val();
	 var c3Other=$("#c3Other").val();
	 var c4Other=$("#c4Other").val();
	 var c5Other=$("#c5Other").val();
	 if(c1Other==0){
		 $("#c1OtherSupplier").val("-1");
	 }else if(c1Other==1){
		 $("#c1OtherRemarks").val("");
	 }else{
		 $("#c1OtherSupplier").val("-1")
		 $("#c1OtherMoney").val("");
		 $("#c1OtherRemarks").val("");
	 }

	 if(c2Other==0){
		 $("#c2OtherSupplier").val("-1");
	 }else if(c2Other==1){
		 $("#c2OtherRemarks").val("");
	 }else{
		 $("#c2OtherSupplier").val("-1")
		 $("#c2OtherMoney").val("");
		 $("#c2OtherRemarks").val("");
	 }

	 if(c3Other==0){
		 $("#c3OtherSupplier").val("-1");
	 }else if(c3Other==1){
		 $("#c3OtherRemarks").val("");
	 }else{
		 $("#c3OtherSupplier").val("-1")
		 $("#c3OtherMoney").val("");
		 $("#c3OtherRemarks").val("");
	 }

	 if(c4Other==0){
		 $("#c4OtherSupplier").val("-1");
	 }else if(c4Other==1){
		 $("#c4OtherRemarks").val("");
	 }else{
		 $("#c4OtherSupplier").val("-1")
		 $("#c4OtherMoney").val("");
		 $("#c4OtherRemarks").val("");
	 }

	 if(c5Other==0){
		 $("#c5OtherSupplier").val("-1");
	 }else if(c5Other==1){
		 $("#c5OtherRemarks").val("");
	 }else{
		 $("#c5OtherSupplier").val("-1")
		 $("#c5OtherMoney").val("");
		 $("#c5OtherRemarks").val("");
	 }
	 forMoney();

 }
 function gradeChange1(){
	 var c1 = "${projectPage.c1Other}"==""?-1:"${projectPage.c1Other}";
	 var c2 = "${projectPage.c2Other}"==""?-1:"${projectPage.c2Other}";
	 var c3 = "${projectPage.c3Other}"==""?-1:"${projectPage.c3Other}";
	 var c4 = "${projectPage.c4Other}"==""?-1:"${projectPage.c4Other}";
	 var c5 = "${projectPage.c5Other}"==""?-1:"${projectPage.c5Other}";

	 if($("#c1Other").val()==-1&&$("#c2Other").val()==-1
			 &&$("#c3Other").val()==-1&&$("#c4Other").val()==-1
			 &&$("#c5Other").val()==-1)
	 {
		 $("#projectPay").val("");
		 $("#projectProfit").val("");
		 $("#projectProfitRate").val("");
	 }

	 //var c1Other=$("#c1Other option:selected").val();
	 var c1Other=$("#c1Other").val()==-1?c1:$("#c1Other").val();
	 var c2Other=$("#c2Other").val()==-1?c2:$("#c2Other").val();
	 var c3Other=$("#c3Other").val()==-1?c3:$("#c3Other").val();
	 var c4Other=$("#c4Other").val()==-1?c4:$("#c4Other").val();
	 var c5Other=$("#c5Other").val()==-1?c5:$("#c5Other").val();
	 if(c1Other==0){
		 $("#c1OtherSupplier").val("-1");
	 }else if(c1Other==1){
		 $("#c1OtherRemarks").val("");
	 }else{
		 $("#c1OtherSupplier").val("-1")
		 $("#c1OtherMoney").val("");
		 $("#c1OtherRemarks").val("");
	 }

	 if(c2Other==0){
		 $("#c2OtherSupplier").val("-1");
	 }else if(c2Other==1){
		 $("#c2OtherRemarks").val("");
	 }else{
		 $("#c2OtherSupplier").val("-1")
		 $("#c2OtherMoney").val("");
		 $("#c2OtherRemarks").val("");
	 }

	 if(c3Other==0){
		 $("#c3OtherSupplier").val("-1");
	 }else if(c3Other==1){
		 $("#c3OtherRemarks").val("");
	 }else{
		 $("#c3OtherSupplier").val("-1")
		 $("#c3OtherMoney").val("");
		 $("#c3OtherRemarks").val("");
	 }

	 if(c4Other==0){
		 $("#c4OtherSupplier").val("-1");
	 }else if(c4Other==1){
		 $("#c4OtherRemarks").val("");
	 }else{
		 $("#c4OtherSupplier").val("-1")
		 $("#c4OtherMoney").val("");
		 $("#c4OtherRemarks").val("");
	 }

	 if(c5Other==0){
		 $("#c5OtherSupplier").val("-1");
	 }else if(c5Other==1){
		 $("#c5OtherRemarks").val("");
	 }else{
		 $("#c5OtherSupplier").val("-1")
		 $("#c5OtherMoney").val("");
		 $("#c5OtherRemarks").val("");
	 }
	 forMoney();
 }
 function otherCheck(){
	 var allMoney=0.0;

	 var c1Other=$("#c1Other").val();
	 var c1OtherSupplier=$("#c1OtherSupplier").val()==""?"a":$("#c1OtherSupplier").val();
	 var c1OtherMoney=$("#c1OtherMoney").val()==""?"a":$("#c1OtherMoney").val();
	 var c1OtherRemarks=$("#c1OtherRemarks").val()==""?"a":$("#c1OtherRemarks").val();
	 allMoney +=handleNum($("#c1OtherMoney").val()==null?0.0:$("#c1OtherMoney").val());

	 var c2Other=$("#c2Other").val();
	 var c2OtherSupplier=$("#c2OtherSupplier").val()==""?"a":$("#c2OtherSupplier").val();
	 var c2OtherMoney=$("#c2OtherMoney").val()==""?"a":$("#c2OtherMoney").val();
	 var c2OtherRemarks=$("#c2OtherRemarks").val()==""?"a":$("#c2OtherRemarks").val();
	 allMoney +=handleNum($("#c2OtherMoney").val()==null?0.0:$("#c2OtherMoney").val());

	 var c3Other=$("#c3Other").val();
	 var c3OtherSupplier=$("#c3OtherSupplier").val()==""?"a":$("#c3OtherSupplier").val();
	 var c3OtherMoney=$("#c3OtherMoney").val()==""?"a":$("#c3OtherMoney").val();
	 var c3OtherRemarks=$("#c3OtherRemarks").val()==""?"a":$("#c3OtherRemarks").val();
	 allMoney +=handleNum($("#c3OtherMoney").val()==null?0.0:$("#c3OtherMoney").val());

	 var c4Other=$("#c4Other").val();
	 var c4OtherSupplier=$("#c4OtherSupplier").val()==""?"a":$("#c4OtherSupplier").val();
	 var c4OtherMoney=$("#c4OtherMoney").val()==""?"a":$("#c4OtherMoney").val();
	 var c4OtherRemarks=$("#c4OtherRemarks").val()==""?"a":$("#c4OtherRemarks").val();
	 allMoney +=handleNum($("#c4OtherMoney").val()==null?0.0:$("#c4OtherMoney").val());

	 var c5Other=$("#c5Other").val();
	 var c5OtherSupplier=$("#c5OtherSupplier").val()==""?"a":$("#c5OtherSupplier").val();
	 var c5OtherMoney=$("#c5OtherMoney").val()==""?"a":$("#c5OtherMoney").val();
	 var c5OtherRemarks=$("#c5OtherRemarks").val()==""?"a":$("#c5OtherRemarks").val();
	 allMoney +=handleNum($("#c5OtherMoney").val()==null?0.0:$("#c5OtherMoney").val());

	 if((c1Other==0&&(c1OtherMoney=="a"||c1OtherRemarks=="a"))||(c1Other==1&&(c1OtherMoney=="a"||c1OtherSupplier=="a"))){
		 layer.msg('请填写完整的c1或不要填写c1');
		   return false;
	 }
	 if((c2Other==0&&(c2OtherMoney=="a"||c2OtherRemarks=="a"))||(c2Other==1&&(c2OtherMoney=="a"||c2OtherSupplier=="a"))){
		 layer.msg('请填写完整的c2或不要填写c2');
		   return false;
	 }
	 if((c3Other==0&&(c3OtherMoney=="a"||c3OtherRemarks=="a"))||(c3Other==1&&(c3OtherMoney=="a"||c3OtherSupplier=="a"))){
		 layer.msg('请填写完整的c3或不要填写c3');
		   return false;
	 }
	 if((c4Other==0&&(c4OtherMoney=="a"||c4OtherRemarks=="a"))||(c4Other==1&&(c4OtherMoney=="a"||c4OtherSupplier=="a"))){
		 layer.msg('请填写完整的c4或不要填写c4');
		   return false;
	 }
	 if((c5Other==0&&(c5OtherMoney=="a"||c5OtherRemarks=="a"))||(c5Other==1&&(c5OtherMoney=="a"||c5OtherSupplier=="a"))){
		 layer.msg('请填写完整的c5或不要填写c5');
		   return false;
	 }
	 if(allMoney<=0){
		 layer.msg('请至少填写一行c信息');
		   return false;
	 }
	 return true;
 }
 </script>