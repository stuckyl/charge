<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <t:datagrid name="expensesControllerList" title="经费审批"
			actionUrl="expensesController.do?expensesDatagrFindbyMonth1"
			checkbox="true" pageSize="20"
			fitColumns="true" idField="id" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="申报主题" field="theme" extendParams="resizable:false"  align="center" width="80"></t:dgCol>
   <t:dgCol title="活动起始日" field="startDate" formatter=" yyyy-MM-dd" align="center"></t:dgCol>
   <t:dgCol title="活动日数" field="numberDate" extendParams="resizable:false"  align="center" width="60"></t:dgCol>
   <t:dgCol title="申报金额" field="money" extendParams="resizable:false,styler: function(value,row,index){return 'text-align:right;'}" formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="活动人数" field="numberPeople" extendParams="resizable:false"  align="center" width="60"></t:dgCol>
   <t:dgCol title="人均消费" field="average" extendParams="styler: function(value,row,index){return 'text-align:right;'}" formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="部门" field="departmentId" replace="${depts }" query="true" align="center"></t:dgCol>
   <t:dgCol title="申报状态" field="declareStatus" replace="待处理_1,审核拒绝_2,待审核_3,审批拒绝_4,待审批_5,审批通过_6,待处理_8"
   extendParams="resizable:false,styler: function(value,row,index){if(value=='5'){return 'color:red;';}}"
   query="true" align="center" width="60"></t:dgCol>
   <t:dgCol title="失败理由" field="declareReturnReason" align="center" showLen="8" width="120"></t:dgCol>
	<t:dgCol title="录入者" field="inputerName" align="center" width="100"></t:dgCol>
	<t:dgCol title="申报者" field="reporterName" align="center" width="100"></t:dgCol>
	<t:dgCol title="创建者" field="createdBy" align="center" width="100" hidden="true"></t:dgCol>
	<t:dgCol title="审核者" field="checkerName" align="center" width="100"></t:dgCol>
   <%-- <t:dgToolBar title="新增" icon="icon-add" width="100%" height="100%"  url="expensesController.do?addorupdate1" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" width="100%" height="100%" url="expensesController.do?addorupdate" funname="update"></t:dgToolBar> --%>
   <t:dgToolBar title="查看" icon="icon-edit" width="100%" height="100%" url="expensesController.do?addorupdate"  funname="detail"></t:dgToolBar>
   <t:dgToolBar title="通过" icon="icon-le-ok" url="#" funname="entryComplete"  width="120"></t:dgToolBar>
   <t:dgToolBar title="拒绝" icon="icon-le-back" url="#" funname="returnSubmit" ></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-le-back" url="#" funname="" ></t:dgToolBar>
   <%-- <t:dgToolBar title="删除" icon="icon-remove" url="#" funname="delSubmit" ></t:dgToolBar> --%>
   <input type="hidden" id="sessionUser"  value="${sessionScope.LOCAL_CLINET_USER.userName }">
</t:datagrid>
  <style>
      .datagrid-toolbar .datagrid-toolbar {
           height: 35px;
      }
  </style>
<script>

var newYear="";
var newMonth="";
//添加%
function companyProfitRateStyle(value){
	value = (value!="")?value+"%":value;
	return value;
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
			$(".datagrid-toolbar").eq(i).css("height","35px");
			$(".datagrid-toolbar").eq(i).find(".l-btn-left").css({"width":"70px","height":"30px"});
			$(".datagrid-toolbar").eq(i).find(".l-btn-left .l-btn-text").css({"height":"20px","line-height":"20px","font-size":"18px"});
		}
	}
	/* $(".datagrid-toolbar").eq(1).find(".l-btn-left").eq(1).css({"width":"106px","height":"30px"}); */
}

//初始化表头颜色
/* function initTableHeaderColor(){
	var departId = "";
	if($.trim($("#orgIds").val())!=""){
		departId = $("#orgIds").val().split(",")[0];
	}
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
function detail(title,url, id,width,height) {
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length == 0) {
		layer.alert('未选中数据');
		return;
	}
	if (rowsData.length > 1) {
		layer.alert('请选中一条数据查看');
		return;
	}
    url += '&load=detail&id='+rowsData[0].id;
	createdetailwindow(title,url,width,height);
}

function update(title,url, id,width,height,isRestful) {
	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		layer.alert("未选中数据");
		return;
	}
	if (rowsData.length>1) {
		layer.alert("请选中一条数据查看");
		return;
	}
	var declares = getexpensesControllerListSelections("declareStatus");
	var themes = getexpensesControllerListSelections("theme");
	var sessionUser = "${sessionScope.LOCAL_CLINET_USER.userName }";
	var createdBy = getexpensesControllerListSelections("createdBy");
	var isErr = false;
	var isErr1 = false;
	var tip = "";
	var tip1 = "";
	if(declares!="") {
		var arr = declares.toString().split(',');
		var arr2 = themes.toString().split(",");
		var arr3 = createdBy.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			//可以编辑
			if(arr[i]!=8||(arr[i]==4&&arr3[i]!=sessionUser)){
				isErr=true;
				tip +=arr2[i]+"，";

			}/* else if(arr[i]==6){
				isErr1=true;
				tip1+=arr2[i]+",";
			} */
		}
	}
	if(isErr) {
		layer.alert("主题为 "+tip+"的经费当前状态无法编辑");
		return false;
	}
	/* if(isErr1) {
		layer.alert("主题为 "+tip1+"的经费已通过审批，不可进行编辑");
		return false;
	} */
	if(isRestful!='undefined'&&isRestful){
		url += '/'+rowsData[0].id;
	}else{
		url += '&id='+rowsData[0].id;
	}
	createwindow(title,url,width,height);
}
function delSubmit(){
	var ids = getexpensesControllerListSelections("id");
	if(ids == "") {
		layer.alert("未选中数据");
		return false;
	}
	var declares = getexpensesControllerListSelections("declareStatus");
	var themes = getexpensesControllerListSelections("theme");
	var sessionUser = "${sessionScope.LOCAL_CLINET_USER.userName }";
	var createdBy = getexpensesControllerListSelections("createdBy");
	var isErr = false;
	var isErr1 = false;
	var tip = "";
	var tip1 = "";
	if(declares!="") {
		var arr = declares.toString().split(',');
		var arr2 = themes.toString().split(",");
		var arr3 = createdBy.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			//可以编辑
			if(arr[i]!=8||(arr[i]==4&&arr3[i]!=sessionUser)){
				isErr=true;
				tip +=arr2[i]+"，";

			}/* else if(arr[i]==6){
				isErr1=true;
				tip1+=arr2[i]+",";
			} */
		}
	}
	if(isErr) {
		layer.alert("主题为 "+tip+"的经费当前状态无法删除");
		return false;
	}
	/* if(isErr3) {
		layer.alert("员工"+tip3+"目前无法删除，请等待相关人员退回后再操作");
		return false;
	} */
	if(""!=$.trim(ids)){
		layer.confirm('确认删除？', {
            btn: ['确定','取消'], //按钮
            shade: false //不显示遮罩
        }, function(){
        	$.ajax({
				url: "expensesController.do?del&ids="+ids,
				type: "get",
				success: function(data){
					data = JSON.parse(data);
					layer.alert(data.msg,function(){
						$("#expensesControllerList").datagrid();
						/* initTableHeaderColor(); */
						initCusToolbarStyle();
						var db= $('#declareDate_end');
						if(newYear=="") {
							var now = new Date();
						    var month = now.getMonth() + 1;
						    db.datebox('hidePanel').datebox('setValue', now.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + '')));
						}else {
							db.datebox('hidePanel').datebox('setValue', newYear + '-' + (newMonth < 10 ? ('0' + newMonth) : (newMonth + '')));
						}
						layer.closeAll('dialog');
					});
				}
			});
        }, function(){
            return;
        });
	}
}

//录入完成  将所有录入完成的发去申报
function entryComplete(){
	if($(".datagrid-btable tr").length<1){
		   layer.alert('列表没有数据');
			return false;
		}
	   var ids = getexpensesControllerListSelections("id");
	   if(ids == "") {
		   layer.alert('未选中数据');
			return false;
		}
	    var declares = getexpensesControllerListSelections("declareStatus");
	    var themes = getexpensesControllerListSelections("theme");
		//var names = getemployeeInfoListSelections("name");
		var isErr = false;
		var isErr2 = false;
		var tip1 = "";
		var tip2 = "";
		if(declares!="") {
			var arr = declares.toString().split(',');
			var arr2 = themes.toString().split(",");
			for(var i=0;i<arr.length;i++) {
				if(arr[i]!="5") {
					isErr = true;
					tip1+=arr2[i]+"，";
				}
				/* if(arr[i]=="6") {
					isErr2=true;
					tip2+=arr2[i]+",";
				} */
			}
		}
		if(isErr) {
			layer.alert("主题为 "+tip1+"的经费当前状态无法审批通过");
			return false;
		}
		//if(!(id=="")){
		/* $.messager.confirm("提示","是否通过并提交？",function(r){
			if(r){ */
				/* var id = $(".datagrid-row-selected:eq(1)").find(".datagrid-cell-c1-id").text(); */
				//alert(id)
					$.ajax({
						url:"expensesController.do?entryComplete&ids="+ids,
						type:"get",
						success:function(data){
							data = JSON.parse(data);
							if(data.errCode==0){
								$("#expensesControllerList").datagrid();
								//employeeInfoListsearch();
								/* initTableHeaderColor(); */
							}else{
								layer.alert(data.errMsg,{closeBtn: 0},function(){
									//employeeInfoListsearch();
									/* initTableHeaderColor(); */
									layer.closeAll('dialog');
								});
							}
						},
						error:function(){
							layer.alert('通过失败');
						}
					});
			/* }
		}); */
	/* }else{
		$.messager.alert("提示","请选择一条数据","info");
	} */
}
//退回申请
function returnSubmit(){
	if($(".datagrid-btable tr").length<1){
		layer.alert("列表没有数据");
		return false;
	}
	var ids = getexpensesControllerListSelections("id");
	if(ids == "") {
		layer.alert("未选中数据");
		return false;
	}
	var declares = getexpensesControllerListSelections("declareStatus");
	var themes = getexpensesControllerListSelections("theme");
	var sessionUser = "${sessionScope.LOCAL_CLINET_USER.userName }";
	var createdBy = getexpensesControllerListSelections("createdBy");
	var oldReason = getexpensesControllerListSelections("declareReturnReason");
	var isErr = false;
	var isErr1 = false;
	var tip = "";
	var tip1 = "";
	if(declares!="") {
		var arr = declares.toString().split(',');
		var arr2 = themes.toString().split(",");
		var arr3 = createdBy.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			//可以退回
			if(arr[i]!=5){
				isErr=true;
				tip +=arr2[i]+",";
			}/* else if(arr[i]==6){
				isErr1=true;
				tip1+=arr2[i]+",";
			} */
		}
	}
	if(isErr) {
		layer.alert("主题为 "+tip+"的经费当前状态无法审批拒绝");
		return false;
	}
	dcAlert("<div><p>拒绝理由：</p><textarea rows='10' id='returnReason' style='width: 300px;'>"+oldReason+"</textarea></div>",true,function(){
		var returnReason = window.parent.document.getElementById("returnReason").value;
		if(null==returnReason||""==$.trim(returnReason)){
			layer.alert("拒绝理由不能为空");
		}else if($.trim(returnReason).length>255){
			layer.alert("拒绝理由字数超出长度限制");
		}else{
			$.ajax({
				url:"expensesController.do?batchReturn2&returnReason="+encodeURI(encodeURI(returnReason),'UTF-8')+"&type=2&ids="+ids,
				type:"get",
				success:function(data){
					data = JSON.parse(data);
					if(data.errCode==0){
						$("#expensesControllerList").datagrid();
						/* initTableHeaderColor(); */
					}else{
						layer.alert(data.errMsg);
					}
				},
				error:function(){
					layer.alert("拒绝失败");
				}
			});
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

$(".l-btn").click(function(){
	/* initTableHeaderColor(); */
	initCusToolbarStyle();
	var db= $('#declareDate_end');
	if(newYear=="") {
		var now = new Date();
	    var month = now.getMonth() + 1;
	    db.datebox('hidePanel').datebox('setValue', now.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + '')));
	}else {
		db.datebox('hidePanel').datebox('setValue', newYear + '-' + (newMonth < 10 ? ('0' + newMonth) : (newMonth + '')));
	}

});
$(document).ready(function(){

	initCusToolbarStyle();
	/* initTableHeaderColor(); */

	if(document.getElementById('declareDate_end')){
	}else{
		$(".datagrid-toolbar").eq(1).prepend("<div style='width: 178px;float: left;margin-top: 1px;border: 1px solid #cccccc;height: 30px;background-color: #f4f4f4;'>"+
		        "<span style='padding: 0px 4px;height: 27px;line-height: 27px;font-size: 16px;'>选择月度:</span>"+
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
                    $('#expensesControllerList').datagrid({
            			url:'expensesController.do?expensesDatagrFindbyMonth1&month='+salaryDateChoose,
            			pageNumber:1
            		});
                    /* initTableHeaderColor(); */
                	initCusToolbarStyle();
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
    $.ajax({url:"expensesController.do?clearMonth",
    		async:true,
    		success: function (){
    		},
    		error: function(){
    		}
    });
});

</script>