<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%@ page import="com.charge.entity.*" %>
<%
ExpensesEntity a= (ExpensesEntity)request.getAttribute("expensesDeclarePage");

%>
<!DOCTYPE html>
<html>
 <head>
  <title>经费申请</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <style type="text/css">
  	label{
  	    width: 100px;
    	display: inline-block;
    	text-align: right;
  	}
  	#div1 {
            margin-top:15px;
        }
  	#div2 {
            margin-top:15px;
        }
  </style>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="expensesController.do?save" callback="initTableHeaderColor" tiptype="1" beforeSubmit="otherCheck()">
		<div style="width:100%;text-align:center;background:white" class="formtable">
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 60px;">经费信息</div>
		        <div style="margin-top: -25px;">
					<%-- <div style="margin-right:25px;float:left;height:35px">
				      <label class="Validform_label"><span style="color:red">*</span>活动日期:</label>
			          <input class="inputxt" onClick="WdatePicker()"
			          style="width: 155px;padding: 4px 0px;border: 1px solid #D7D7D7;border-radius: 3px"
			          id="startDate" name="startDate"
			          value="<fmt:formatDate value='${expensesDeclarePage.startDate}' type="date" pattern="yyyy-MM-dd"/>"
			          datatype="*"/>
	            	  <span class="Validform_checktip"></span>
					</div> --%>
					<div style="margin-right:25px;float:left;height:35px">
					      <label class="Validform_label"><span style="color:red">*</span>活动日期:</label>
				          <input class="Wdate" onClick="WdatePicker()"  style="width: 155px;padding: 4px 0px;border: 1px solid #D7D7D7;border-radius: 3px" id="startDate" name="startDate"  value="<fmt:formatDate value='${expensesDeclarePage.startDate}' type="date" pattern="yyyy-MM-dd"/>"/>
		            	  <span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>申报主题:</label>
						<input class="inputxt" id="theme" name="theme" value="${expensesDeclarePage.theme}" maxlength="60"  autocomplete="off" datatype="*"/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
				      <label class="Validform_label">活动日数:</label>
			          <input class="inputxt" id="numberDate" name="numberDate"  value="${expensesDeclarePage.numberDate}"  autocomplete="off"
			          datatype="/^\s*$/|/^[0-2]?[1-9]([.]{1}[0-9]{1,3})?$/|/^10([.]{1}[0-9]{1,3})?$/|/^20([.]{1}[0-9]{1,3})?$/|/^[3][0](\.([0]{1})?([0]{1})?)?$/"/>
	            	  <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <%-- <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">申报主题:</label>
						<input class="inputxt" id="theme" name="theme" value="${expensesDeclarePage.theme}" maxlength="60"  />
						<span class="Validform_checktip"></span>
					</div>
		        </div> --%>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>申报金额:</label>
						<input class="inputxt" id="money" name="money" maxlength="9" onchange="incomeChange()"
						value="${expensesDeclarePage.money}" data-options="precision:2,groupSeparator:','"
						datatype="/(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})?(,\d{3})*(\.\d+)?$/"  autocomplete="off"/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>活动人数:</label>
						<input class="inputxt" id="numberPeople" name="numberPeople" maxlength="9" onchange="incomeChange1()"
						value="${expensesDeclarePage.numberPeople}"   autocomplete="off"
						datatype="/(^[1-9]([0-9]+)?$)|(^[1-9]$)/|/^(\d{1,3})+(,\d{3})*(\.\d+)?$/"/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>人均消费:</label>
						<input class="inputxt" id="average" name="average" maxlength="9" onchange="incomeChange2()"
						value="${expensesDeclarePage.average}" data-options="precision:2,groupSeparator:','"  autocomplete="off"
						datatype="/(^[0-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^[0-9]\.[0-9]([0-9])?$)/|/^(\d{1,3})+(,\d{3})*(\.\d+)?$/"/>
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <%-- <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">参与人员:</label>
						<input class="inputxt" id="namePeople" name="namePeople" maxlength="9"  autocomplete="off"
						value="${expensesDeclarePage.namePeople}"  />
						<span class="Validform_checktip"></span>
					</div>

		        </div> --%>
		        <div style="clear:both"></div>
		        <div>
					<div id="div1" style="margin-right:50px;float:left;height:200px;">
						<label class="Validform_label">参与人员:</label>
						<%-- <input class="inputxt" type="text" id="contrne" style="width:400px;height:150px;" name="contrne" value="${expensesDeclarePage.contrne}"  /> --%>
						<textarea  class="inputxt" rows="10" cols="30"  maxlength="255"
						style="width:400px;height:120px;vertical-align: top;resize:none;"  autocomplete="off"
						id="namePeople" name="namePeople" >${expensesDeclarePage.namePeople}</textarea>
						<span class="Validform_checktip"></span>
					</div>
		        <div>
					<div id="div2" style="margin-right:50px;float:left;height:200px;">
						<label class="Validform_label">申报内容:</label>
						<%-- <input class="inputxt" type="text" id="contrne" style="width:400px;height:150px;" name="contrne" value="${expensesDeclarePage.contrne}"  /> --%>
						<textarea  class="inputxt" rows="10" cols="30"  maxlength="255"
						style="width:400px;height:120px;vertical-align: top;resize:none;"  autocomplete="off"
						id="content" name="content" >${expensesDeclarePage.content}</textarea>
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		   </div>
		   <input id="id" name="id" type="hidden" value="${expensesDeclarePage.id }">
  </t:formvalid>
  <!-- 金钱正则： /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/ -->
  <script type="text/javascript">
/*   $("#unitPriceType").blur(function(){
	  incomeChange();
	}); */
  $(document).ready(function(){
	  /* 对金钱格式进行设置 */
	    //申请金额
	    $('#money').numberbox({
		    min:0,
		    precision:2
		});
	    //人均消费:
	    $('#average').numberbox({
		    min:0,
		    precision:2
		});
	    //活动人数:
	    $('#numberPeople').numberbox({
		    min:1,
		    precision:0
		});
	    //活动日数:
	    $('#numberDate').numberbox({
		    min:0,
		    precision:1
		});


	  //格式化数字
	  if(""!=$.trim($("#unitPrice").val())){
		  $("#unitPrice").val(formatTwoDecimal($("#unitPrice").val()));
	  }
	  if(""!=$.trim($("#appointmentAttendanceDay").val())){
		  $("#appointmentAttendanceDay").val(formatTwoDecimal($("#appointmentAttendanceDay").val()));
	  }
	  if(""!=$.trim($("#acceptanceAttendanceDay").val())){
		  $("#acceptanceAttendanceDay").val(formatTwoDecimal($("#acceptanceAttendanceDay").val()));
	  }
	  if(""!=$.trim($("#monthOther").val())){
		  $("#monthOther").val(formatTwoDecimal($("#monthOther").val()));
	  }
	  if(""!=$.trim($("#acceptanceAdd").val())){
		  $("#acceptanceAdd").val(formatTwoDecimal($("#acceptanceAdd").val()));
	  }
	  if(""!=$.trim($("#monthAdjustment").val())){
		  $("#monthAdjustment").val(formatTwoDecimal($("#monthAdjustment").val()));
	  }
	  if(""!=$.trim($("#income").val())){
		  $("#income").val(formatTwoDecimal($("#income").val()));
	  }

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
//将 数字格式化两位小数 并转化成 千分位字符串显示
  function myFormat(num) {
	  return (num.toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
  }

  function otherCheck(){
	  var startDate = $("#startDate").val();
	  if(""==startDate){
		  layer.msg('请填写活动日期');
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
		//申请金额
		var money = handleNum($("#money").val());
		//活动人数
		var numberPeople = handleNum($("#numberPeople").val());
		//人均消费
		var average = handleNum($("#average").val());
		if(""!=money&&""!=numberPeople){
			//$("#average").val(formatNumber(money/numberPeople,2,1));
			$("#average").numberbox('setValue',money/numberPeople);
		}
		/* if(""!=money&&""!=average){
			$("#numberPeople").val(formatNumber(Math.round(money/average),0,1));
		} */
		/* if(""!=average&&""!=numberPeople){
			$("#money").val(average*numberPeople);
		} */
}

  function incomeChange1(){
		//申请金额
		var money = handleNum($("#money").val());
		//活动人数
		var numberPeople = handleNum($("#numberPeople").val());
		//人均消费
		var average = handleNum($("#average").val());
		if(""!=money&&""!=numberPeople){
			//$("#average").val(formatNumber(money/numberPeople,2,1));
			$("#average").numberbox('setValue',money/numberPeople);
		}
		/* if(""!=numberPeople&&""!=average){
			$("#money").val(formatNumber(numberPeople*average,2,1));
		} */
}
  function incomeChange2(){
		//申请金额
		var money = handleNum($("#money").val());
		//活动人数
		var numberPeople = handleNum($("#numberPeople").val());
		//人均消费
		var average = handleNum($("#average").val());
		/* if(""!=money&&""!=average){
			$("#average").val(money/numberPeople);
		} */
		if(""!=numberPeople&&""!=average){
			//$("#money").val(formatNumber(numberPeople*average,2,1));
			$("#money").numberbox('setValue',numberPeople*average);
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