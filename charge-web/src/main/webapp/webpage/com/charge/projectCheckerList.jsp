<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<head><meta name="format-detection" content="telephone=no"></head>
<script type="text/javascript">
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
}
//初始化表头颜色
/* function initTableHeaderColor(){
	var ths = $(".datagrid-header-row");
	for(var i = 1;i < ths.length;i++){
	    switch(i){
	        case 1:
	        	for(var j = 0;j<$(ths).eq(1).find("td").length;j++){
                    $(ths).eq(1).find("td").eq(j).css("background","#C6E0B4");
                    $(ths).eq(1).find("div").eq(j).css("text-align","center");
                }
	    }
	}
} */
/**
 * 动态显示 选中行的 统计信息
 * reporter用户能看到的合计项（收入、毛利、人数、人均毛利、毛利率）
 */
 function showStatistics() {
		//收入
		var incomes = getprojectListSelections("projectIncome");
		//数目
		var numbers = incomes.length;
		//毛利
		var profits = getprojectListSelections("projectProfit");
		//毛利率
		var profitsRate= getprojectListSelections("projectProfitRate");
		var total=0.0;
		var profit=0.0;
		for(var i = 0 ; i < incomes.length ;i++) {
			total +=handleNum(incomes[i]);
			profit +=handleNum(profits[i]);

		}

		var avgProfit = isNaN(profit/numbers)?0.0:(profit/numbers);
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
		$("#renshu").text(numbers);
		$("#avgProfit").text(avgProfit);
	}
function detail(title,url, id,width,height) {
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length == 0) {
		layer.alert("未选中任何数据");
		return;
	}
	if (rowsData.length > 1) {
		layer.alert("请选中一条数据查看");
		return;
	}
    url += '&load=detail&id='+rowsData[0].id;
	createdetailwindow(title,url,width,height);
}
function update(title,url, id,width,height,isRestful) {
	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		layer.alert("未选中任何数据");
		return;
	}
	if (rowsData.length>1) {
		layer.alert("请选中一条数据编辑");
		return;
	}
	var index =  $('#projectList').datagrid('getRows');
	var sessionUser = "${sessionScope.LOCAL_CLINET_USER.userName }";
	var projectStatus = getprojectListSelections("projectStatus");
	var id1 = getprojectListSelections("id");
	//var names = getemployeeDeclareListSelections("employeeInfo.name");
	var inputNames = getprojectListSelections("inputerId");
	var isErr = false;
	var isErr1 = false;
	var tip = "";
	if(projectStatus!="") {
		var arr = projectStatus.toString().split(',');
		//var arr2 = names.toString().split(",");
		var arr2 = id1.toString().split(",");
		var arr3 = inputNames.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			/* if(arr[0]!=1){

			}else {
				isErr=true;
			} */
			/* if(arr3[0]==sessionUser){

			}else {
				isErr1=true;
			} */
			if(arr[i]>"${actionCode}") {
				isErr = true;
				for(var j=0;j<index.length;j++){
					if(index[j].id==arr2[i]){
						var a = j+1;
						tip+= a +",";
					}
				}
			}
		}
	}
	if(isErr) {
		layer.alert('编号：'+tip+'的项目当前状态不可编辑');
		return false;
	}
	/* if(isErr1) {
		layer.alert("录入者非本人的项目不可编辑");
		return false;
	} */
	if(isRestful!='undefined'&&isRestful){
		url += '/'+rowsData[0].id;
	}else{
		url += '&id='+rowsData[0].id;
	}
	createwindow(title,url,width,height);
}

//通过 new
function declareSubmit(){
	   if($(".datagrid-btable tr").length<1){
		   layer.alert('列表没有任何数据');
			return false;
		}
	   var id = getprojectListSelections("id");
	   if(id == "") {
		   layer.alert('未选中任何数据');
			return false;
		}
	   var id = getprojectListSelections("id");
	   var index =  $('#projectList').datagrid('getRows');
	    var declares = getprojectListSelections("projectStatus");
		//var names = getprojectListSelections("name");
		var isErr = false;
		var isErr2 = false;
		var tip1 = "";
		var tip2 = "";
		if(declares!="") {
			var arr = declares.toString().split(',');
			var arr2 = id.toString().split(",");
			for(var i=0;i<arr.length;i++) {
				if(arr[i]!="${actionCode-1}"&&arr[i]!="${actionCode}") {
					isErr=true;
					for(var j=0;j<index.length;j++){
						if(index[j].id==arr2[i]){
							var a = j+1;
							tip2+= a +",";
						}
					}
				}
			}
		}
		if(isErr) {
			layer.alert('编号：'+tip2+'的项目当前状态不可通过');
			return false;
		}
		$.ajax({
			url:"projectController.do?batchPass&id="+id,
			type:"get",
			success:function(data){
				data = JSON.parse(data);
				if(data.errCode==0){
					$("#projectList").datagrid();
				}else{
					layer.alert(data.errMsg,{closeBtn: 0},function(){
						$("#projectList").datagrid();
						layer.closeAll('dialog');
					});
				}
			},
			error:function(){
				layer.alert('通过失败');
			}
		});
}

//退回 new
function returnSubmit(){
	   if($(".datagrid-btable tr").length<1){
		   layer.alert('列表没有任何数据');
			return false;
		}
	   var id = getprojectListSelections("id");
	   if(id == "") {
		   layer.alert('未选中任何数据');
			return false;
		}
	   var id = getprojectListSelections("id");
	   var index =  $('#projectList').datagrid('getRows');
	    var declares = getprojectListSelections("projectStatus");
		//var names = getprojectListSelections("name");
		var inpName = getprojectListSelections("inputName");
		var isErr = false;
		var isErr3 = false;
		var tip1 = "";
		var tip2 = "";
		if(declares!="") {
			var arr = declares.toString().split(',');
			var arr2 = id.toString().split(",");
			var arr3 = inpName.toString().split(",");
			for(var i=0;i<arr.length;i++) {
				if(arr[i] != "${actionCode-1}" &&arr[i]!="${actionCode}") {
					isErr = true;
					for(var j=0;j<index.length;j++){
						if(index[j].id==arr2[i]){
							var a = j+1;
							tip1+= a +",";
						}
					}
				}
				if(arr3[i] == "${sessionScope.LOCAL_CLINET_USER.realName}") {
					isErr3 = true;
					for(var j=0;j<index.length;j++){
						if(index[j].id==arr2[i]){
							var a = j+1;
							tip2+= a +",";
						}
					}
				}
			}
		}
		if(isErr) {
			layer.alert('编号：'+tip1+'的项目当前状态不可拒绝');
			return false;
		}
		if(isErr3) {
			layer.alert('编号：'+tip1+'的项目当前状态不可拒绝');
			return false;
		}
		var departId = $("#orgIds").val();
		var loseReason = $(".datagrid-row-selected:eq(1)").find(".datagrid-cell-c1-loseReason").text();
		dcAlert("<div><p>拒绝理由：</p><textarea rows='10' id='returnReason' style='width: 300px;'>"+loseReason+"</textarea></div>",true,function(){
			var returnReason = window.parent.document.getElementById("returnReason").value;
			if(null==returnReason||""==$.trim(returnReason)){
				layer.alert('拒绝理由不能为空');
			}else{
				if($.trim(returnReason).length>255){
					layer.alert('拒绝理由字数超出长度限制');
				}else{
					returnReason = encodeURI(encodeURI(returnReason));
					$.ajax({
						url:"projectController.do?batchReturn&Id="+id+
								"&returnReason="+returnReason,
						type:"get",
						success:function(data){
							data = JSON.parse(data);
							if(data.errCode==0){
								$("#projectList").datagrid();
							}else{
								$("#projectList").datagrid();
								layer.alert(data.errMsg);
							}
						},
						error:function(){
							layer.alert('拒绝失败');
						}
					});
				}
			}
		});
}
function dcAlert(ct,cl,fun){
	$.dialog({
		title:"提示",
		content: ct,
		lock: true,
		opacity: 0.3,
		width:300,
		height:150,
		cache:false,
		cancelVal: "关闭",
		cancel:cl,
		ok:fun
	});
}

function newDelSubmit(){
	   if($(".datagrid-btable tr").length<1){
		   layer.alert('列表没有任何数据');
			return false;
		}
	   var id = getprojectListSelections("id");
	   if(id == "") {
		   layer.alert('未选中任何数据');
			return false;
		}
	   var id = getprojectListSelections("id");
	   var index =  $('#projectList').datagrid('getRows');
	    //var quits = getprojectListSelections("quitstatus");
	    var declares = getprojectListSelections("projectStatus");
		//var names = getprojectListSelections("name");
		var inpName = getprojectListSelections("inputName");
		var isErr = false;
		var isErr3 = false;
		var tip1 = "";
		var tip2 = "";
		if(declares!="") {
			var arr = declares.toString().split(',');
			var arr2 = id.toString().split(",");
			var arr3 = inpName.toString().split(",");
			for(var i=0;i<arr.length;i++) {
				if(!(arr[i] == "${actionCode}" ||arr[i]=="${actionCode-1}")) {
					isErr = true;
					for(var j=0;j<index.length;j++){
						if(index[j].id==arr2[i]){
							var a = j+1;
							tip1+= a +",";
						}
					}
				}
				if(arr3[i] != "${sessionScope.LOCAL_CLINET_USER.realName}") {
					isErr3 = true;
					for(var j=0;j<index.length;j++){
						if(index[j].id==arr2[i]){
							var a = j+1;
							tip2+= a +",";
						}
					}
				}
			}
		}
		if(isErr) {
			layer.alert('编号：'+tip1+'的项目当前状态不可删除');
			return false;
		}
		if(isErr3) {
			layer.alert('编号：'+tip2+'的项目当前状态不可删除');
			return false;
		}
			//var name = getprojectListSelections("name");
			layer.confirm('确认要删除已选中项目？', {
	            btn: ['确定','取消'], //按钮
	            shade: false //不显示遮罩
	        }, function(){
	        	$.ajax({
					url: "projectController.do?delForId&Id="+id,
					type: "get",
					success: function(data){
							data = JSON.parse(data);
							$("#projectList").datagrid();
							layer.alert(data.msg);
						},
					error:function(){
						$("#projectList").datagrid();
						layer.alert(data.msg);
				}
				});
	        }, function(){
	            return;
	        });
}


function OneKeyTotal() {
	var salaryDateChoose = $("input[name='salaryDateChoose']").val();
	var isAll = $("input[name='isAll']").val();
	//ajax 获取当前用户所能看到的总收入、总毛利、人数
	$.ajax({
		url:"projectController.do?oneKeyTotal&month="+salaryDateChoose
			+"&employeeDepartment="+$("select[name='projectDepartment']").val()
			+"&customerName="+$("select[name='projectCustomer1']").val()
			+"&declareStatus="+$("select[name='projectStatus']").val()
			+"&isAll="+$("select[name='isAll']").val(),
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
	var ids = getprojectListSelections("id");
	var salaryDateChoose = $("input[name='salaryDateChoose']").val();
	if(ids == "") {
		//未选中任何数据
		location.href="projectController.do?excelExport&month="+salaryDateChoose
			+"&employeeDepartment="+$("select[name='projectDepartment']").val()
			+"&customerName="+$("select[name='projectCustomer1']").val()
			+"&declareStatus="+$("select[name='projectStatus']").val()
			+"&isAll="+$("select[name='isAll']").val();
	}else {
		location.href="projectController.do?excelExportSelectedIds&month="+salaryDateChoose+"&ids="+ids;
	}

}
</script>
  <t:datagrid name="projectList" title="项目审核" pageSize="20"
  actionUrl="projectController.do?projectFindbyMonth2"
  queryMode="group" fit="true" checkbox="true" extendParams="onSelect:function(index,data) {showStatistics();},onUnselect:function(index,data) {showStatistics();},onCheckAll:function(rows){showStatistics();},onUncheckAll:function(rows){showStatistics();}"
  sortName="projectStatus" sortOrder="desc" >
  <%-- actionUrl="employeeDeclareController.do?declareDatagrid" --%>
 <!--  onDblClick="update('查看','employeeDeclareController.do?lookorcheck','employeeDeclareList','100%','100%')" -->

     <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="项目工期" field="projectDate" formatter="yyyy-MM"  width="120" align="center"></t:dgCol>
   <t:dgCol title="部门" field="projectDepartment"   width="120" replace="${depts }" align="center" query="true"></t:dgCol>
   <t:dgCol title="客户" field="projectCustomer1"   width="120" replace="${customers }" align="center" query="true"></t:dgCol>
   <%-- <t:dgCol title="供应商" field="projectCustomer2"   width="120"></t:dgCol> --%>
   <t:dgCol title="收入" field="projectIncome" formatterjs="formatTwoDecimal,salaryFormat" extendParams="resizable:false,styler: function(value,row,index){return 'text-align:right;'}"  width="120"></t:dgCol>
   <t:dgCol title="支出" field="projectPay" formatterjs="formatTwoDecimal,salaryFormat" extendParams="resizable:false,styler: function(value,row,index){return 'text-align:right;'}"  width="120"></t:dgCol>
   <t:dgCol title="毛利" field="projectProfit" formatterjs="formatTwoDecimal,salaryFormat" extendParams="resizable:false,styler: function(value,row,index){return 'text-align:right;'}"  width="120"></t:dgCol>
   <t:dgCol title="净利率" field="projectProfitRate" formatterjs="formatTwoDecimal,salaryFormat,companyProfitRateStyle"   width="120" align="center"></t:dgCol>
   <t:dgCol title="申报状态" field="projectStatus"   width="120" query="true" replace="审批通过_1,待处理_${actionCode},失败_${actionCode-1},未上报_${actionCode+1},已上报_${actionCode-2}," extendParams="resizable:false,styler: function(value,row,index){if(value=='${actionCode-1}'||value=='${actionCode}'){return 'color:red;';}}" align="center"></t:dgCol>
   <t:dgCol title="拒绝理由" field="projectReturnreason"   width="120" align="center"></t:dgCol>
   <t:dgCol title="录入者" field="inputerId" width="120" align="center" hidden="true"></t:dgCol>
    <t:dgCol title="录入者" field="inputName" align="center" width="100"></t:dgCol>
    <t:dgCol title="还原" field="backUpFlag" width="120" align="center" hidden="true"></t:dgCol>
    <t:dgCol title="合计项" field="isAll" align="center" replace="部门_0,项目_2" hidden="true" query="true"></t:dgCol>
   <t:dgToolBar title="查看" icon="icon-search" url="projectController.do?addReporterLook" funname="detail" height="100%" ></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" width="100%" height="100%" url="projectController.do?addReporter" funname="update"></t:dgToolBar>
   <t:dgToolBar title="通过" icon="icon-le-ok" url="#" funname="declareSubmit" ></t:dgToolBar>
   <t:dgToolBar title="拒绝" icon="icon-le-back" url="#" funname="returnSubmit" ></t:dgToolBar>
   <%-- <t:dgToolBar title="还原" icon="icon-le-reback" url="#" funname="backSubmit" ></t:dgToolBar> --%>
   <t:dgToolBar title="合计" url="#" icon="icon-add" funname="OneKeyTotal" ></t:dgToolBar>
   <t:dgToolBar title="导出" url="#" icon="icon-put" funname="excelExport" ></t:dgToolBar>
  </t:datagrid>
  <div>
<!--  -->
		<span class="span" style="font-size:20px;color:#2F4050;margin-left:25px;">总收入：</span><span class="span" id="shouru"  style="font-size:20px;display:inline-block;width:25%;text-align:left;color:red"></span><span style="font-size:20px">&nbsp;&nbsp;</span>
		<span class="span" style="font-size:20px;color:#2F4050">总毛利：</span><span class="span" id="maoli" style="font-size:20px;display:inline-block;width:20%;text-align:left;color:red"></span><span style="font-size:20px">&nbsp;&nbsp;</span>
		<span class="span" style="font-size:20px;color:#2F4050">项目数：</span><span class="span" id="renshu" style="font-size:20px;display:inline-block;width:10%;text-align:left;color:red"></span><span style="font-size:20px">&nbsp;&nbsp;</span>
		<span class="span" style="font-size:20px;color:#2F4050">平均毛利：</span><span class="span" id="avgProfit" style="font-size:20px;display:inline-block;width:10%;text-align:left;color:red"></span>&nbsp;&nbsp;
	</div>
  <style>
      .datagrid-toolbar .datagrid-toolbar {
           height: 35px;
      }
      .span{
      	    margin-top: 6px;
      }
  </style>
<script>
$(document).ready(function(){
	if(document.getElementById('declareDate_end')){
	}else{
		$(".datagrid-toolbar").eq(1).prepend("<div style='width: 178px;float: left;margin-top: 1px;border: 1px solid #cccccc;height: 30px;background-color: #f4f4f4;'>"+
		        "<span style='padding: 0px 4px;height: 27px;line-height: 27px;font-size: 16px;'>选择月度:</span>"+
		         "<input id='declareDate_end' style='width: 100px;'  onchange='generateData()'  onclick='WdatePicker()' name='salaryDateChoose'  class='Wdate'  type='text'></div>");
	}
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
                    $('#projectList').datagrid({
            			url:'projectController.do?projectFindbyMonth2&month='+salaryDateChoose,
            			pageNumber:1
            		});
                    /* initTableHeaderColor(); */
                	newYear = year;
                	newMonth = month;
                });
            }, 0);
            // 解绑年份输入框中任何事件
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
    /* $('#employeeDeclareListForm').attr('onkeydown','if(event.keyCode==13){employeeDeclareListsearch();initTableHeaderColor();return false;}'); */
    /* $('.easyui-linkbutton.l-btn').eq(1).attr('onclick','temp();'); */
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

    $.ajax({url:"projectController.do?clearMonth",
		async:true,
		success: function (){
		},
		error: function(){
		}
	});
    initCusToolbarStyle();
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