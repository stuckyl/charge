 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<head><meta name="format-detection" content="telephone=no"></head>
<script>


var newYear="";
var newMonth="";
//添加%
function companyProfitRateStyle(value){
	if(value=="") return value;
	return parseFloat(value).toFixed(2)+"%";
}
//格式化数字
function formatTwoDecimal(value,row,index){
	if(""!=$.trim(value)){
		if(value.indexOf(".")!=-1&&value.substring(value.indexOf(".")+1).length==1){
			return value+"0";
		}
	}
	return value;
}
/**处理数字千分符*/
function comma(num) {
	 var source = String(num).split(".");//按小数点分成2部分
	 source[0] = source[0].replace(new RegExp('(\\d)(?=(\\d{3})+$)','ig'),"$1,");//只将整数部分进行都好分割
	 return source.join(".");//再将小数部分合并进来
	}
/**处理数字千分符*/
function salaryFormat(value,row,index){
	 if(!value) return "";
	var str_n = comma(value);
	var strs = str_n.split('.');
	//var SUM = Number(strs[1]);
    if(strs[1].length>=2){

    }if(strs[1].length==1){
    	strs[1]=strs[1]+"0";
    }
    str_n=strs[0]+"."+strs[1];
  return str_n;
}
//初始化工具栏按钮
function initCusToolbarStyle(){
	for(var i = 0;i<$(".datagrid-toolbar").length;i++){
		if($($(".datagrid-toolbar").eq(i)).attr("style").indexOf("border-bottom-width")!=-1){
			$(".datagrid-toolbar").eq(i).css("height","37px");
			$(".datagrid-toolbar").eq(i).find(".l-btn-left").css({"width":"70px","height":"30px"});
			$(".datagrid-toolbar").eq(i).find(".l-btn-left .l-btn-text").css({"height":"20px","line-height":"20px","font-size":"18px"});
		}
	}
	/* $(".datagrid-toolbar").eq(1).find(".l-btn-left").eq(1).css({"width":"106px","height":"30px"}); */
}


function delSubmit(){
	var ids = getemployeeDeclareListSelections("id");
	if(ids == "") {
		layer.alert("未选中数据");
		return false;
	}
	var declares = getemployeeDeclareListSelections("declareStatus");
	var names = getemployeeDeclareListSelections("employeeInfo.name");
	var baceUpFlags = getemployeeDeclareListSelections("baceUpFlag");
	var isErr = false;
	var tip = "";
	if(declares!="") {
		var arr = declares.toString().split(',');
		var arr2 = names.toString().split(",");
		var arr3 = baceUpFlags.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			if((arr[i]=="${actionCode}"||arr[i]=="${actionCode-1}"||arr[i]=="${actionCode-2}")&&arr3[i]!=1) {
			}else{
				isErr = true;
				tip+=arr2[i]+"，";
			}
		}
	}
	if(isErr) {
		layer.alert("员工："+tip+"当前状态不能删除");
		return false;
	}
	if(""!=$.trim(ids)){
		layer.confirm('确认删除？', {
            btn: ['确定','取消'], //按钮
            shade: false //不显示遮罩
        }, function(){
        	$.ajax({
				url: "employeeDeclareController.do?del&ids="+ids,
				type: "get",
				success: function(data){
					data = JSON.parse(data);
					layer.alert(data.msg,function(){
						$("#employeeDeclareList").datagrid();
						layer.closeAll('dialog');
					});
				}
			});
        }, function(){
            return;
        });
	}
}


//生成员工收入数据
function generateData(){
    var salaryDateChoose = null;
 /* 获取日期 */
    if(""!=$("input[name='salaryDateChoose']").val()){
        salaryDateChoose = $("input[name='salaryDateChoose']").val();
    }
    if(salaryDateChoose==null||salaryDateChoose==""){
    	layer.alert("请选择生成月度");
        return false;
    }
    layer.confirm('是否生成收支运营数据？', {
        btn: ['确定','取消'], //按钮
        shade: false //不显示遮罩
    }, function(){
    	layer.closeAll('dialog');
    	$.ajax({
            url:"employeeDeclareController.do?declareInit&month="+salaryDateChoose,
            type:"get",
            success:function(data){
                data = JSON.parse(data);
                if(data.errCode==0){
                	layer.alert(data.successInfo,function(){
                		$('#employeeDeclareList').datagrid({
                   			url:'employeeDeclareController.do?employeeDeclareFindbyMonth&month='+salaryDateChoose,
                   		});
                       	layer.closeAll('dialog');
					});
                } else {
                	layer.alert(data.errInfo,function(){
                		reloadTable();
                		layer.closeAll('dialog');
					});
                }
            },
            error:function(){
            	layer.alert("网络错误，生成失败");
            	reloadTable();
            	layer.closeAll('dialog');
            }
        });
    }, function(){
        return;
    });
}
function update(title,url, id,width,height,isRestful) {
	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		layer.alert("未选中数据");
		return;
	}
	if (rowsData.length>1) {
		layer.alert("请选中一条数据编辑");
		return;
	}
	var sessionUser = "${sessionScope.LOCAL_CLINET_USER.userName }";
	var declares = getemployeeDeclareListSelections("declareStatus");
	var names = getemployeeDeclareListSelections("employeeInfo.name");
	var inputNames = getemployeeDeclareListSelections("inputerId");
	var isErr = false;
	var tip = "";
	if(declares!="") {
		var arr = declares.toString().split(',');
		var arr2 = names.toString().split(",");
		var arr3 = inputNames.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			//可以编辑
			if(parseInt(arr[i])<'${actionCode+1 }'&&arr3[i]==sessionUser){
			}else {
				isErr=true;
				tip+=arr2[i]+",";
			}
		}
	}
	/* if(isErr) {
		layer.alert("员工"+tip+"当前状态不能编辑");
		return false;
	} */
	if(isRestful!='undefined'&&isRestful){
		url += '/'+rowsData[0].id;
	}else{
		url += '&id='+rowsData[0].id;
	}
	createwindow(title,url,width,height);
}
//录入完成  将所有录入完成的发去申报
function entryComplete(){
	if($(".datagrid-btable tr").length<1){
		layer.alert("列表没有数据");
		return false;
	}

	var ids = getemployeeDeclareListSelections("id");
	if(ids == "") {
		layer.alert("未选中数据");
		return false;
	}
	var declares = getemployeeDeclareListSelections("declareStatus");
	var names = getemployeeDeclareListSelections("employeeInfo.name");
	var isErr = false;
	var tip = "";
	if(declares!="") {
		var arr = declares.toString().split(',');
		var arr2 = names.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			if(arr[i]=="${actionCode-1}"||arr[i]=="${actionCode-2}") {
			}else {
				isErr = true;
				tip+=arr2[i]+"，";
			}
		}
	}
	if(isErr) {
		layer.alert("员工："+tip+"当前状态无法申报");
		return false;
	}
	$.ajax({
		url:"employeeDeclareController.do?batchPass&ids="+ids,
		type:"get",
		success:function(data){
			data = JSON.parse(data);
			if(data.errCode==0){
				$("#employeeDeclareList").datagrid();
			}else if(data.errCode==1){
				layer.alert("选中数据 录入未完成");
			}else {
				layer.alert("数据申报失败");
			}
		},
		error:function(){layer.alert("数据申报失败");}
	});
}
function showStatistics() {
	var incomes = getemployeeDeclareListSelections("income");
	var peoples = incomes.length;
	var profits = getemployeeDeclareListSelections("companyProfit");

	var profitsRate= getemployeeDeclareListSelections("companyProfitRate");
	var total=0.0;
	var profit=0.0;
	for(var i = 0 ; i < incomes.length ;i++) {
		total +=handleNum(incomes[i]);
		profit +=handleNum(profits[i]);

	}

	var avgProfit = isNaN(profit/peoples)?0.0:(profit/peoples);
	total = total.toFixed(2);
	total = comma(total);
	profit = profit.toFixed(2);
	profit = comma(profit);
	avgProfit = avgProfit.toFixed(2);
	avgProfit = comma(avgProfit);
	//$("#shouru").text(isNaN(total)?0.0:total);
	$("#shouru").text(total);
	//$("#maoli").text(isNaN(profit)?0.0:profit);
	$("#maoli").text(profit);
	$("#renshu").text(peoples);
	$("#avgProfit").text(avgProfit);
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

function OneKeyTotal() {
	var salaryDateChoose = $("input[name='salaryDateChoose']").val();
	//ajax 获取当前用户所能看到的总收入、总毛利、人数
	$.ajax({
		url:"employeeDeclareController.do?oneKeyTotal&month="+salaryDateChoose+"&employeeInfo.name="
			+$("input[name='employeeInfo.name']").val()+"&employeeDepartment="+$("select[name='employeeDepartment']").val()
			+"&employeeType="+$("select[name='employeeType']").val()+"&customerName="+$("select[name='customerName']").val()
			+"&declareStatus="+$("select[name='declareStatus']").val()+"&isAll="+$("select[name='isAll']").val(),
		type:"get",
		async:false,
		success: function(data){
			data = JSON.parse(data);
			var incomeTotal = handleNum(data.incomeTotal);
			var peopleTotal = handleNum(data.peopleTotal);
			var profitTotal = handleNum(data.profitTotal);
			var avgProfit = isNaN(profitTotal/peopleTotal)?0.0:(profitTotal/peopleTotal);
			incomeTotal = incomeTotal.toFixed(2);
			incomeTotal = comma(incomeTotal);
			profitTotal = profitTotal.toFixed(2);
			profitTotal = comma(profitTotal);
			avgProfit = avgProfit.toFixed(2);
			avgProfit = comma(avgProfit);
			$("#shouru").text(incomeTotal);
			$("#maoli").text(profitTotal);
			$("#renshu").text(peopleTotal);
			$("#avgProfit").text(avgProfit);
		}
	});
}
function excelExport(){
	var ids = getemployeeDeclareListSelections("id");
	var salaryDateChoose = $("input[name='salaryDateChoose']").val();
	if(ids == "") {
		//未选中数据
		location.href="employeeDeclareController.do?excelExport&month="+salaryDateChoose+"&employeeInfo.name="
				+$("input[name='employeeInfo.name']").val()+"&employeeDepartment="+$("select[name='employeeDepartment']").val()
				+"&customerName="+$("select[name='customerName']").val()+"&employeeType="+$("select[name='employeeType']").val()
				+"&declareStatus="+$("select[name='declareStatus']").val()+"&isAll="+$("select[name='isAll']").val();
	}else {
		location.href="employeeDeclareController.do?excelExportSelectedIds&month="+salaryDateChoose+"&ids="+ids;
	}
}
/* functiore() {
	if($(".datagrid-btable tr").length<1){
		layer.alert("列表没有数据");
		return false;
	}
	var ids = getemployeeDeclareListSelections("id");
	if(ids == "") {
		layer.alert("未选中数据");
		return false;
	}
	var sessionUser = "${sessionScope.LOCAL_CLINET_USER.userName }";
    var inputNames = getemployeeDeclareListSelections("inputerId");
	var declares = getemployeeDeclareListSelections("declareStatus");
	var baceUpFlags = getemployeeDeclareListSelections("baceUpFlag");
	var names = getemployeeDeclareListSelections("employeeInfo.name");
	var isErr = false;
	var isErr1 = false;
	var tip = "";
	if(baceUpFlags!="") {
		var arr = baceUpFlags.toString().split(',');
		var arr1 = declares.toString().split(',');
		var arr2 = names.toString().split(",");
		var arr3 = inputNames.toString().split(",");
		var arr4 = sessionUser.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			if(arr[i]=="1"&&arr1[i]!=2) { //审批通过已备份
			}else {
				isErr = true;
				tip+=arr2[i]+"，";
			}
			if(sessionUser!=arr3[i]){
				isErr1=true;
			}
		}
	}
	if(isErr||baceUpFlags=="") {
		if(baceUpFlags=="") layer.alert("员工"+names+"当前状态无法还原");
		else layer.alert("员工"+tip+"当前状态无法还原");
		return false;
	}
	if(isErr1) {
		layer.alert('项目录入者非本人，不可进行还原操作');
		return false;
	}
	$.ajax({
		url:"employeeDeclareController.do?employeeRestore&ids="+ids,
		type:"get",
		success:function(data){
			data = JSON.parse(data);
			if(data.errCode==0){
				$("#employeeDeclareList").datagrid();
			}else {
				layer.alert("还原失败");
			}
		},
		error:function(){layer.alert("数据还原失败");}
	});
} */
</script>
 <style>
      .span{
      	    margin-top: 6px;
      }
  </style>
  <t:datagrid name="employeeDeclareList" title="收支录入"  pageSize="20"
   actionUrl="employeeDeclareController.do?employeeDeclareFindbyMonth"
   extendParams="onSelect:function(index,data) {showStatistics();},onUnselect:function(index,data) {showStatistics();},onCheckAll:function(rows){showStatistics();},onUncheckAll:function(rows){showStatistics();}"
   checkbox="true" queryMode="group"   fit="true" sortName="declareStatus" sortOrder="desc">
    <%-- 客户经理 录入界面 --%>

      <t:dgCol title="编号" field="id" align="center" hidden="true"  extendParams="resizable:false"></t:dgCol>
      <t:dgCol title="年月" field="salaryDate" formatter=" yyyy-MM"  align="center"  extendParams="resizable:false"></t:dgCol>
      <t:dgCol title="姓名" field="employeeInfo.name" align="center" width="100" extendParams="resizable:false" query="true"></t:dgCol>
      <t:dgCol title="身份证号" field="employeeInfo.code" align="center"  extendParams="resizable:false"></t:dgCol>
      <t:dgCol title="部门" field="employeeDepartment" align="center"  width="100" replace="${depts }"  extendParams="resizable:false"></t:dgCol>
      <t:dgCol title="员工类型" field="employeeType" align="center" replace="TECH_0,OP_1" query="true" ></t:dgCol>
      <t:dgCol title="顾客" field="customerName" align="center" width="100" query="true" replace="${customers }" extendParams="resizable:false"></t:dgCol>
      <t:dgCol title="收入" field="income" extendParams="styler: function(value,row,index){return 'text-align:right;'}" formatterjs="formatTwoDecimal,salaryFormat" width="100" ></t:dgCol>
      <t:dgCol title="毛利" field="companyProfit" extendParams="styler: function(value,row,index){return 'text-align:right;'}" formatterjs="formatTwoDecimal,salaryFormat" width="100" ></t:dgCol>
      <t:dgCol title="毛利率" field="companyProfitRate"  formatterjs="formatTwoDecimal,salaryFormat,companyProfitRateStyle" extendParams="styler: function(value,row,index){return 'text-align:center;'}" width="100" ></t:dgCol>
 	  <t:dgCol title="状态" field="declareStatus" align="center"  width="100" query="true"  replace="待录入_${actionCode },待申报_${actionCode-1 },申报拒绝_${actionCode-2 },已上报_${actionCode-3 },审批通过_2"
 	  extendParams="styler: function(value,row,index){if(value=='${actionCode-1 }'||value=='${actionCode-2 }'||value=='${actionCode }'){return 'color:red;';}}"></t:dgCol>
<%--  	  <t:dgCol title="状态" field="declareStatus" align="center" replace="录入中_0,录入完成_1,申报中_2,申报拒绝_3,审核中_4,审核拒绝_5,审批中_6,审批拒绝_7,审批通过_8" width="120"></t:dgCol> --%>
     <t:dgCol title="失败理由" field="declareReturnreason" align="center" width="200" extendParams="resizable:false" showLen="12"></t:dgCol>
    <%--  <t:dgCol title="录入者" field="inputName" align="center" width="100"></t:dgCol> --%>
     <t:dgCol title="是否备份" field="baceUpFlag" align="center" hidden="true"></t:dgCol>
     <t:dgCol title="合计项" field="isAll" align="center" replace="部门_0,人力_1" hidden="true" query="true"></t:dgCol>
      <t:dgToolBar title="生成" url="#" icon="icon-add" funname="generateData" ></t:dgToolBar>
      <t:dgToolBar title="编辑" icon="icon-edit" url="employeeDeclareController.do?addorupdate"  funname="update" height="100%"></t:dgToolBar>
 	  <t:dgToolBar title="申报" url="#"  icon="icon-le-ok" funname="entryComplete" ></t:dgToolBar>
  	  <t:dgToolBar title="删除" url="#" icon="icon-remove" funname="delSubmit" ></t:dgToolBar>
  	 <%--  <t:dgToolBar title="还原" url="#" icon="icon-le-reback" funname="restore" ></t:dgToolBar> --%>
  	  <t:dgToolBar title="合计" url="#" icon="icon-add" funname="OneKeyTotal" ></t:dgToolBar>
  	 <%--  <t:dgToolBar title="导出" icon="icon-put" url="#" funname="excelExport" ></t:dgToolBar> --%>
  </t:datagrid>
  <div>
		<span class="span" style="font-size:20px;color:#2F4050;margin-left:25px;">总收入：</span><span class="span" id="shouru"  style="font-size:20px;display:inline-block;width:25%;text-align:left;color:red"></span><span style="font-size:20px">&nbsp;&nbsp;</span>
		<span class="span" style="font-size:20px;color:#2F4050">总毛利：</span><span class="span" id="maoli" style="font-size:20px;display:inline-block;width:20%;text-align:left;color:red"></span><span style="font-size:20px">&nbsp;&nbsp;</span>
		<span class="span" style="font-size:20px;color:#2F4050">人数：</span><span class="span" id="renshu" style="font-size:20px;display:inline-block;width:10%;text-align:left;color:red"></span><span style="font-size:20px">&nbsp;&nbsp;</span>
		<span class="span" style="font-size:20px;color:#2F4050">人均毛利：</span><span class="span" id="avgProfit" style="font-size:20px;display:inline-block;width:10%;text-align:left;color:red"></span>&nbsp;&nbsp;
	</div>
 <script>
 $(document).ready(function(){
	 	initCusToolbarStyle();
		if(document.getElementById('declareDate_end')){
		}else{
			$(".datagrid-toolbar").eq(1).prepend("<div style='width: 178px;float: left;margin-top: 1px;border: 1px solid #cccccc;height: 30px;background-color: #f4f4f4;'>"+
			        "<span style='padding: 0px 4px;height: 27px;line-height: 27px;font-size: 16px;'>生成月度:</span>"+
			         "<input id='declareDate_end' style='width: 100px;'  onchange='generateData()'  onclick='WdatePicker()' name='salaryDateChoose'  class='Wdate'  type='text'></div>");
		}
		/* color: #8f0911; */
	    var db= $('#declareDate_end');
	    db.datebox({
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
	                    db.datebox('hidePanel').datebox('setValue', year + '-' + (month < 10 ? ('0' + month) : (month + '')));

	                    // 自动查询
	                    var salaryDateChoose = $("input[name='salaryDateChoose']").val();
	                    $('#employeeDeclareList').datagrid({
	            			url:'employeeDeclareController.do?employeeDeclareFindbyMonth&month='+salaryDateChoose,
	            			pageNumber:1
	            		});
	                	newYear = year;
	                	newMonth = month;
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
	             return d.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + ''));
	        }
	    });
	    $('.combo.datebox').attr('style','margin-bottom:4px;');
	    $('.combo-text.validatebox-text').attr('readonly','readonly');
	    // 日期选择对象
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
	            db.datebox('hidePanel').datebox('setValue', now.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + '')));
	        });
	    }
	    var now = new Date();
	    var month = now.getMonth() + 1;
	    db.datebox('hidePanel').datebox('setValue', now.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + '')));
	    $.ajax({url:"employeeDeclareController.do?clearMonth",
	    		async:true,
	    		success: function (){
	    		},
	    		error: function(){
	    		}
	    });
	});
 $(".l-btn").click(function(){
		var db= $('#declareDate_end');
		if(newYear=="") {
			var now = new Date();
		    var month = now.getMonth() + 1;
		    db.datebox('hidePanel').datebox('setValue', now.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + '')));
		}else {
			db.datebox('hidePanel').datebox('setValue', newYear + '-' + (newMonth < 10 ? ('0' + newMonth) : (newMonth + '')));
		}
	});
 </script>
