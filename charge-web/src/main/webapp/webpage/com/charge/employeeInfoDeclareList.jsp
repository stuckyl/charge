<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base><!-- pagination="flase" -->
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
	if(declares.toString()=="1") {
		layer.alert('员工：'+names+'已离职，不可编辑');
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
		layer.alert('未选中数据');
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
    $(":input[name='code']").attr("autocomplete","off");
	initCusToolbarStyle();
	$('#employeeInfoListForm').attr('onkeydown','if(event.keyCode==13){employeeInfoListsearch();return false;}');
    $("#empImportBtn").remove();
    $("#excelImportBtn").prepend("<input type='file' name='empFile' id='empImportBtn' style='position:absolute;width:74px !important;height:32px;opacity:0'/>");
    $("#empImportBtn").fileupload({
        autoUpload: false,//是否自动上传
         maxNumberOfFiles : 1,
         dataType: 'text',
         forceIframeTransport: true,
         add:function (e, data) { data.submit(); },
         url : 'employeeInfoController.do?empImport',
         start : function(e, data) {
	        	 $.Showmsg("导入中......");
        	 //$.messager.alert("提示","导入中......","info");
         },
         done : function(e, result) {
        	 var data = JSON.parse(result.result);
         	if(data.errCode == 0){
         		//$.messager.defaults("提示",data.errMsg,"info");
         		$.Showmsg(data.errMsg);
             	setTimeout(function(){
             		location.reload(true);
             	},2000);
         	}else if(data.errCode == 1){
         		$("#employeeInfoList").datagrid();
         		$.Showmsg(data.errMsg);
         	}else{
         		$.Showmsg("导入失败，导入数据有误");
         		//$.messager.defaults("导入失败，导入数据有误");
         	}
         },
         fail : function(e, result) {
         	$.Showmsg("导入有错");
        	 //$.messager.defaults("导入有错");
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

function salaryFormat(value,row,index){
   if(!value) return "";
   var strs = value.toString().split('.');
   var SUM = Number(strs[1]);
    if(SUM>=10){

    }else{
  	  strs[1]=strs[1]+"0";
    }
   var str_n = strs[0];
   var result="";
   while(str_n.length>3){
     result=","+str_n.slice(-3)+result;
     str_n=str_n.slice(0,str_n.length-3)
   }
   //var re=/(?=(?!(\b))(\d{3})+$)/g;
   return str_n+result+"."+strs[1];
 }
function declareSubmit(){
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
			if(arr[i]!= "${actionCode}"&&arr[i] != "${actionCode-1}") {
				isErr = true;
				tip1+=arr2[i]+"，";
			}
		}
	}
	if(isErr) {
		layer.alert('员工：'+tip1+'当前状态无法进行申报操作');
		return false;
	}
	/* $.messager.confirm("提示","是否提交申报？",function(r){
		if(r){ */
			/* var id = $(".datagrid-row-selected:eq(1)").find(".datagrid-cell-c1-id").text(); */
			//alert(id)
				$.ajax({
					url:"employeeInfoController.do?batchDeclare&id="+id,
					type:"get",
					success:function(data){
						data = JSON.parse(data);
						if(data.errCode==0){
							$("#employeeInfoList").datagrid();
						}else{
							$("#employeeInfoList").datagrid();
							layer.alert(data.errMsg);
						}
					},
					error:function(){
						layer.alert('申报失败');
					}
				});
		/* }
	}); */

}
function newDelSubmit(){
   if($(".datagrid-btable tr").length<1){
	   layer.alert('列表没有数据');
		return false;
	}
   var id = getemployeeInfoListSelections("id");
   if(id == "") {
	   layer.alert('未选中数据');
		return false;
	}
    var quits = getemployeeInfoListSelections("quitstatus");
    var declares = getemployeeInfoListSelections("declareStatus");
	var names = getemployeeInfoListSelections("name");
	var isErr = false;
	var isErr2 = false;
	var tip1 = "";
	var tip2 = "";
	if(declares!="") {
		var arr = declares.toString().split(',');
		var arr2 = names.toString().split(",");
		var arr3 = quits.toString().split(",");
		for(var i=0;i<arr.length;i++) {
			if(!(arr[i] == "${actionCode}" ||arr[i]=="${actionCode-1}")||arr3=="1") {
				isErr = true;
				tip1+=arr2[i]+"，";
			}
		}
	}
	if(isErr) {
		layer.alert('员工：'+tip1+'已上报或已入职，不可删除');
		return false;
	}
		var name = getemployeeInfoListSelections("name");
		layer.confirm('确认要删除员工：【'+name+'】？', {
	        btn: ['确定','取消'], //按钮
	        shade: false //不显示遮罩
	    }, function(){
			$.ajax({
				url: "employeeInfoController.do?delForId&Id="+id,
				type: "get",
				success: function(data){
						data = JSON.parse(data);
						$("#employeeInfoList").datagrid();
						layer.alert(data.msg);
					},
				error: function () {//请求失败处理函数
						layer.alert('请求失败');
				    }
				});

	    }, function(){
	        return;
	    });
	}

/* function excelExport(){
	var id = getemployeeInfoListSelections("id");
	var name = $("input[name='name']").val();
	var depart = $("select[name='department']").val();
	var employeeFlag = $("select[name='employeeFlag']").val();
	var declareStatus = $("select[name='declareStatus']").val();
	var insurance = $("select[name='insurance']").val();
	location.href="employeeInfoController.do?excelExport&id="+id+"&departId="+depart+"&name="+name+"&employeeFlag="+employeeFlag+"&declareStatus="+declareStatus+"&insurance="+insurance;
} */


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
	var isErr2 =false;
	var isErr = false;
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

</script>
<t:datagrid name="employeeInfoList" title="员工录入" actionUrl="employeeInfoController.do?declareDatagrid" checkbox="true" pageSize="20" sortName="declareStatus" sortOrder="desc" fitColumns="true" idField="id" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="离职状态" field="quitstatus" hidden="true"></t:dgCol>
   <t:dgCol title="姓名" field="name" extendParams="resizable:false" query="true" align="center" width="120"></t:dgCol>
   <t:dgCol title="身份证号" field="code" extendParams="resizable:false" query="true" align="center" width="160"></t:dgCol>
   <c:if test="${useCode==1}">
   <t:dgCol title="员工类别" field="employeeFlag" extendParams="resizable:false" replace="TECH_0,OP_1" query="true" align="center" width="120"></t:dgCol>
   </c:if>
   <t:dgCol title="A（标准）" field="AStandardSalary" extendParams="resizable:false,styler: function(value,row,index){return 'text-align:right;'}" formatterjs="formatTwoDecimal,salaryFormat" width="120"></t:dgCol>
   <t:dgCol title="手机号" field="contactWay" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgCol title="邮箱" field="email" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgCol title="入职日" field="entryDate" formatter="yyyy-MM-dd" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgCol title="申报状态" field="declareStatus" replace="待处理_${actionCode},失败_${actionCode-1},已上报_${actionCode-2},入职成功_2,离职_1" extendParams="resizable:false,styler: function(value,row,index){if(value=='${actionCode-1}'||value=='${actionCode}'){return 'color:red;';}}" query="true" align="center" width="120"></t:dgCol>
   <t:dgCol title="社保状态" field="insurance" replace="待入保_0,已入保_1,待退保_2,已退保_3"  extendParams="resizable:false" align="center" width="120" query="true"></t:dgCol>
   <t:dgCol title="失败理由" field="loseReason" align="center" showLen="8" width="120"></t:dgCol>
   <%-- <t:dgToolBar title="导入" icon="icon-putout" url="#" funname=""  operationCode="excelImportBtn" id="excelImportBtn"></t:dgToolBar> --%>
   <t:dgToolBar title="新增" icon="icon-add" width="100%" height="100%"  url="employeeInfoController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="删除" icon="icon-remove" url="#" funname="newDelSubmit" ></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" width="100%" height="100%" url="employeeInfoController.do?detailAllInfo" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" width="100%" height="100%" url="employeeInfoController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="申报" icon="icon-le-ok" url="#" funname="declareSubmit"  width="120"></t:dgToolBar>
   <t:dgToolBar title="离职" icon="icon-le-reback" url="#" funname="leaveSubmit"></t:dgToolBar>
</t:datagrid>