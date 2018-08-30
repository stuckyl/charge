<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <link rel="stylesheet"  href="plug-in/jquery-file-upload/css/jquery.fileupload.css"/>
  <script type="text/javascript" src="plug-in/jquery-file-upload/js/vendor/jquery.ui.widget.js"></script>
  <script type="text/javascript" src="plug-in/jquery-file-upload/js/jquery.iframe-transport.js"></script>
  <script type="text/javascript" src="plug-in/jquery-file-upload/js/jquery.fileupload.js"></script>
<script>

function update(title,url, id,width,height,isRestful) {

	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		layer.alert('未选中数据');
		return;
	}
	if (rowsData.length>1) {
		layer.alert('请选中一条数据再编辑');
		return;
	}
	var declares = getemployeeInfoListSelections("declareStatus");
	var names = getemployeeInfoListSelections("name");
	var arr = declares.toString().split(',');
	var arr2 = names.toString().split(",");
	var tip1 = "";
	var isErr = false;
	for(var i=0;i<arr.length;i++) {
		if(arr[i] != "4"&&arr[i] != "2"&&arr[i] != "1") {
			tip1+=arr2[i];
			isErr = true;
		}
	}
	if(isErr) {
		layer.alert('员工：'+tip1+'尚未申报或已经拒绝，请等待下级处理');
		return false;
	}
	if(isRestful!='undefined'&&isRestful){
		url += '/'+rowsData[0].id;
	}else{
		url += '&id='+rowsData[0].id;
	}
	createwindow(title,url,width,height);
}
function detail(title,url, id,width,height) {
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length == 0) {
		layer.alert('请选择查看项目');
		return;
	}
	if (rowsData.length > 1) {
		layer.alert('请选择一条记录再查看');
		return;
	}
    url += '&load=detail&id='+rowsData[0].id;
	createdetailwindow(title,url,width,height);
}
$(document).ready(function(){
	$(":input[name='name']").attr("autocomplete","off");
	initCusToolbarStyle();
	$('#employeeInfoListForm').attr('onkeydown','if(event.keyCode==13){employeeInfoListsearch();return false;}');
    $("#empImportBtn").remove();
	$("#excelImportBtn").prepend("<input type='file' name='empFile' id='empImportBtn' accept='.xlsx' style='position:absolute;width:74px !important;height:32px;opacity:0'/>");
	$("#empImportBtn").fileupload({
		autoUpload: false,//是否自动上传
	    maxNumberOfFiles : 1,
	    dataType: 'text',
	    forceIframeTransport: true,
	    add:function (e, data) { data.submit(); },
	    url : 'employeeInfoController.do?empImport',
	    start : function(e, data) {
	   	 //$.messager.alert("提示","导入中......","info");
	    },
	    done : function(e, result) {
			var data = JSON.parse(result.result);
	    	if(data.errCode == 0){
	    		//$.messager.defaults("提示",data.errMsg,"info");
	        	setTimeout(function(){
	        		location.reload(true);
	        	},2000);
	    	}else if(data.errCode == 1){
	    		layer.alert(data.errMsg,
	                    function () {
	    					location.reload(true);
	                    });
	    	}else{
	    		layer.alert(data.errMsg,
	                    function () {
	    					location.reload(true);
	                    });
	    	}
			initCusToolbarStyle();
	    },
	    fail : function(e, result) {
	    	layer.alert('导入有错');
	   	 //$.messager.defaults("导入有错");
			initCusToolbarStyle();
	    }
	});
});

//初始化表头颜色
   function initTableHeaderColor(){
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
   }

function editDepart(){
	var id = getemployeeInfoListSelected("id");
	if (id ==""){
		tip('请选择一条数据');
	}else{
		var url = "employeeInfoController.do?editDepart&id=" + id
		createwindow("修改部门", url, 300, 400);
	}
}

function delSubmit(){
	layer.confirm('注意确认清空员工信息，收支信息，员工六金，六金配置信息？', {
        btn: ['确定','取消'], //按钮
        shade: false //不显示遮罩
    }, function(){
			$.ajax({
				async : false,
				type: "get",
		        url: "myDepartController.do?emptyAll",//请求的action路径
		        error: function () {//请求失败处理函数
					layer.alert('请求失败');
		        },
		        success:function(data){ //请求成功后处理函数。
		        	data = JSON.parse(data);
					if(data.errCode==1){
						layer.alert(data.errMsg);
						$("#employeeInfoList").datagrid();
					}else{
						layer.alert(data.errMsg,{closeBtn: 0},function(){
							layer.closeAll('dialog');
							$("#employeeInfoList").datagrid();
						});
					}
		        }
			});
    }, function(){
        return;
    });
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
}
//通过
function passSubmit(){
   if($(".datagrid-btable tr").length<1){
	   layer.alert('列表没有数据');
		return false;
	}
   var id = getemployeeInfoListSelections("id");
   if(id == "") {
	   layer.alert('未选中数据');
		return false;
	}
    var declares = getemployeeInfoListSelections("declareStatus");
	var names = getemployeeInfoListSelections("name");
	var isErr = false;
	var isErr2 = false;
	var tip1 = "";
	var tip2 = "";
	if(declares!="") {
		var arr = declares.toString().split(',');
		var arr2 = names.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			if(arr[i] != "4") {
				isErr = true;
				tip1+=arr2[i]+"，";
			}
		}
	}
	if(isErr) {
		layer.alert('员工：'+tip1+'当前状态不可进行通过操作');
		return false;
	}
		$.ajax({
			url:"employeeInfoController.do?batchDeclare&id="+id,
			type:"get",
			success:function(data){
				data = JSON.parse(data);
				if(data.errCode==0){
					/* $.messager.alert("提示","通过成功","info",function(){
					}); */
					$("#employeeInfoList").datagrid();
				}else{
					layer.alert(data.errMsg,{closeBtn: 0},function(){
						$("#employeeInfoList").datagrid();
						layer.closeAll('dialog');
					});
				}
			},
			error:function(){
				layer.alert('通过失败');
			}
		});
}

//退回
function returnSubmit(){
   if($(".datagrid-btable tr").length<1){
	   layer.alert('列表没有数据');
		return false;
	}
   var id = getemployeeInfoListSelections("id");
   if(id == "") {
	   layer.alert('未选中数据');
		return false;
	}
    var declares = getemployeeInfoListSelections("declareStatus");
	var names = getemployeeInfoListSelections("name");
	var isErr = false;
	var isErr2 = false;
	var tip1 = "";
	var tip2 = "";
	if(declares!="") {
		var arr = declares.toString().split(',');
		var arr2 = names.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			if(arr[i] != "4") {
				isErr = true;
				tip1+=arr2[i]+"，";
			}
		}
	}
	if(isErr) {
		layer.alert('员工：'+tip1+'当前状态不可拒绝');
		return false;
	}
	//if(!(id=="")){
	//var oldReasons = getemployeeInfoListSelections("loseReason");
	var loseReason = $(".datagrid-row-selected:eq(1)").find(".datagrid-cell-c1-loseReason").text();
	/* if(!(loseReason=="")){
		alert(oldReasons);
		var oldReason = oldReasons.split(",");
		alert(oldReason);
		oldReasons = oldReason[0];
	} */
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
					url:"employeeInfoController.do?batchReturn&Id="+id+
							"&returnReason="+returnReason,
					type:"get",
					success:function(data){
						data = JSON.parse(data);
						if(data.errCode==0){
							/* $.messager.alert("提示","拒绝成功","info",function(){
							}); */
							$("#employeeInfoList").datagrid();
						}else{
							$("#employeeInfoList").datagrid();
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
/* }else{
	$.messager.alert("提示","请选择一条数据","info");
} */
}
function leaveSubmit(title,url, id,width,height,isRestful){
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		layer.alert('未选中数据');
		return;
	}
	if (rowsData.length>1) {
		layer.alert('请选中一条数据再进行离职');
		return;
	}
	var declares = getemployeeInfoListSelections("quitstatus");
	var names = getemployeeInfoListSelections("name");
	var arr = declares.toString().split(',');
	var arr2 = names.toString().split(",");
	var tip1 = "";
	var isErr = false;
	var isErr2 = false;
	for(var i=0;i<arr.length;i++) {
		if(arr[i] == 2) {
			tip1+=arr2[i];
			isErr = true;
		}
		if(arr[i] == 0){
			tip1+=arr2[i];
			isErr2 = true;
		}
	}
	if(isErr) {
		layer.alert('员工：'+tip1+'已离职，无法进行离职操作');
		return false;
	}
	if(isErr2){
		layer.alert('员工：'+tip1+'尚未入职，不可进行离职操作');
		return false;
	}
	var url = "employeeInfoController.do?leave"
	if(isRestful!='undefined'&&isRestful){
		url += '/'+rowsData[0].id;
	}else{
		url += '&id='+rowsData[0].id;
	}
	createwindow("员工离职", url);
}
//弹框
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
function excelExport(){
	var id = getemployeeInfoListSelections("id");
	var name = $("input[name='name']").val();
	var depart = $("select[name='department']").val();
	var employeeFlag = $("select[name='employeeFlag']").val();
	var declareStatus = $("select[name='declareStatus']").val();
	var insurance = $("select[name='insurance']").val();
	location.href="employeeInfoController.do?excelExport&id="+id+"&departId="+depart+"&name="+name+"&employeeFlag="+employeeFlag+"&declareStatus="+declareStatus+"&insurance="+insurance;
}
</script>
<t:datagrid name="employeeInfoList" title="员工审批"  actionUrl="employeeInfoController.do?approvalDatagrid" checkbox="true" sortName="declareStatus" sortOrder="desc" pageSize="20" idField="id" fitColumns="true" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="离职状态" field="quitstatus" hidden="true"></t:dgCol>
   <t:dgCol title="姓名" field="name" extendParams="resizable:false"  query="true" align="center" width="120"></t:dgCol>
   <t:dgCol title="身份证号" field="code" extendParams="resizable:false" align="center" width="220"></t:dgCol>
   <t:dgCol title="部门" field="department" extendParams="resizable:false" replace="${depts }" query="true" align="center" width="120"></t:dgCol>
   <t:dgCol title="员工类别" field="employeeFlag" extendParams="resizable:false" replace="TECH_0,OP_1" query="true"  align="center" width="120"></t:dgCol>
   <t:dgCol title="A（标准）" field="AStandardSalary" formatterjs="formatTwoDecimal,salaryFormat" extendParams="resizable:false,styler: function(value,row,index){return 'text-align:right;'}" width="120"></t:dgCol>
   <t:dgCol title="入职日" field="entryDate" formatter="yyyy-MM-dd" extendParams="resizable:false"  align="center" width="120"></t:dgCol>
   <t:dgCol title="申报状态" field="declareStatus" replace="待处理_4,入职成功_2,未上报_5,离职_1" query="true" extendParams="resizable:false,styler: function(value,row,index){if(value=='4'){return 'color:red;';}}" align="center" width="120"></t:dgCol>
   <t:dgCol title="社保状态" field="insurance" replace="未入保_0,已入保_1,需退保_2,已退保_3"  extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgCol title="所属上级" field="inputName" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <%-- <t:dgToolBar title="导出" icon="icon-put" url="#" funname="excelExport" operationCode="excelExportBtn" id="excelExportBtn"></t:dgToolBar> --%>
   <t:dgToolBar title="导入" icon="icon-putout" url="#" funname="" id="excelImportBtn"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-put" url="#" funname="excelExport"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" width="100%" height="100%" url="employeeInfoController.do?editController" funname="update"></t:dgToolBar>
   <t:dgToolBar title="通过" icon="icon-le-ok" url="#" funname="passSubmit"></t:dgToolBar>
   <t:dgToolBar title="拒绝" icon="icon-le-back" url="#" funname="returnSubmit"></t:dgToolBar>
   <t:dgToolBar title="离职" icon="icon-le-reback" url="#" funname="leaveSubmit"></t:dgToolBar>
   <t:dgToolBar title="清空" icon="icon-remove" url="#"  funname="delSubmit"></t:dgToolBar>
   <%--<t:dgToolBar title="编辑" icon="icon-edit" funname="editDepart" ></t:dgToolBar>
   <t:dgToolBar title="删除" icon="icon-remove" url="#" funname="delSubmit" operationCode="delBtn"></t:dgToolBar> --%>
  </t:datagrid>