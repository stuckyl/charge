<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script>
$(window).load(function(){
	$(":input[name='name']").attr("autocomplete","off");
	/* initTableHeaderColor(); */
	initCusToolbarStyle();
	/* $('#employeeInfoListForm').attr('onkeydown','if(event.keyCode==13){employeeInfoListsearch();initTableHeaderColor();return false;}'); */
});
	//点击查询后设置表头颜色
$(".l-btn").click(function(){
	/* initTableHeaderColor(); */
	initCusToolbarStyle();
	/* 	setCellsDiffStyle(); */
	});

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
		if(rowsData[0].employeeFlag == 1){
			layer.alert('没有权限查看本部员工详细信息');
			return;
		}
	    url += '&load=detail&id='+rowsData[0].id;
		createdetailwindow(title,url,width,height);
	}
/*     $(document).ready(function(){
	   initCusToolbarStyle();
		//添加部门条件
		var datagrid = $("#employeeInfoListtb");
		datagrid.find("div[name='searchColums']").find("form#employeeInfoListForm span:eq(0)").append($("#tempSearchColums div[name='searchColums']").html());
		$("#tempSearchColums").html('');
	}); */

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
	//点击查询后给表头付色
	   $(".l-btn").click(function(){
			/* initTableHeaderColor(); */
			initCusToolbarStyle();
			/* setCellsDiffStyle(); */
		});

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

   function excelExport(){
		if($(".datagrid-btable tr").length<1){
			$.messager.alert("提示","列表没有数据","info");
			return false;
		}
		var departId = "";

		if(""!=$.trim($("#orgIds").val())){
			departId = $("#orgIds").val();
		}

		location.href="employeeInfoController.do?excelExport&departId="+departId;
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
  </script>
<t:datagrid name="employeeInfoList" title="员工信息" actionUrl="employeeInfoController.do?accessDatagrid" fitColumns="true" pageSize="20" idField="id" sortName="code" sortOrder="asc" checkbox="true" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="身份证号" field="code" extendParams="resizable:false" align="center" width="220"></t:dgCol>
   <t:dgCol title="姓名" field="name" extendParams="resizable:false" query="true" align="center" width="120"></t:dgCol>
   <t:dgCol title="部门" field="department" replace="${depts}" query="true" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgCol title="员工类别" field="employeeFlag" extendParams="resizable:false" replace="TECH_0,OP_1" query="true" align="center" width="120"></t:dgCol>
   <%-- <t:dgCol title="A（标准）" field="AStandardSalary" formatterjs="formatTwoDecimal,salaryFormat" extendParams="resizable:false,styler: function(value,row,index){return 'text-align:right;'}" width="120"></t:dgCol> --%>
   <t:dgCol title="入职日" field="entryDate" formatter="yyyy-MM-dd" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgCol title="六金城市" field="sixGoldCity" extendParams="resizable:false" width="120" align="center"></t:dgCol>
   <t:dgCol title="六金基数" field="sixGoldBase" formatterjs="formatTwoDecimal,salaryFormat" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  width="120"></t:dgCol>
   <%-- <t:dgCol title="申报状态" field="declareStatus" replace="待审批_5,入职成功_8"  extendParams="resizable:false" align="center" width="120"></t:dgCol> --%>
<%--    <t:dgCol title="申报状态" field="declareStatus" query="true" replace="待审批_5,入职成功_8"  extendParams="resizable:false" align="center" width="120"></t:dgCol> --%>
   <t:dgCol title="所属上级" field="inputName" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgToolBar title="查看" icon="icon-search" width="100%" height="100%" url="employeeInfoController.do?addorupdate" funname="detail"></t:dgToolBar>
   <%-- <t:dgToolBar title="导出" icon="icon-put" url="#" funname="excelExport" operationCode="excelExportBtn" id="excelExportBtn"></t:dgToolBar> --%>
<%--    <t:dgToolBar title="拒绝" icon="icon-le-back" url="#" funname="returnSubmit" operationCode="returnBtn"></t:dgToolBar>
   <t:dgToolBar title="通过" icon="icon-le-ok" url="#" funname="passSubmit" operationCode="passBtn"></t:dgToolBar> --%>
   <%-- <t:dgToolBar title="删除" icon="icon-remove" url="#" funname="delSubmit" operationCode="delBtn"></t:dgToolBar> --%>
</t:datagrid>
