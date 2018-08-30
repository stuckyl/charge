<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script>
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
    $(document).ready(function(){
        $(":input[name='name']").attr("autocomplete","off");
    	initCusToolbarStyle();
    	$('#employeeInfoListForm').attr('onkeydown','if(event.keyCode==13){employeeInfoListsearch();return false;}');
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
	   //点击查询后给表头付色
	   $(".l-btn").click(function(){
			initCusToolbarStyle();
			/* setCellsDiffStyle(); */
		});

   function delSubmit(){
		var id = $(".datagrid-row-selected:eq(1)").find(".datagrid-cell-c1-id").text();
		var name = $(".datagrid-row-selected:eq(1)").find(".datagrid-cell-c1-name").text();
		if(""!=$.trim(id)){
			$.messager.confirm("提示","确认要删除员工：【"+name+"】？",function(r){
				if(r){
					$.ajax({
						url: "employeeInfoController.do?del&id="+id,
						type: "get",
						success: function(data){
							data = JSON.parse(data);
							$.messager.alert("提示",data.msg,"info",function(){
								location.reload(true);
							});
						}
					});
				}
			});
		}else{
			tip('请选择一条数据');
		}
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
			$.messager.alert("提示","列表没有数据","info");
			return false;
		}
		//var departId = $("#orgIds").val();
		var alertMsg = "是否将所有部门下审核人员信息全部通过？";
/* 		if(null!=departId&&""!=$.trim(departId)){
			var departName = $("#orgNames").val();
			alertMsg = "是否将部门：【"+departName+"】下审核人员信息全部通过？";
			if(id!=null){
			}
		}else{
			departId = "";
		} */
		var id = getemployeeInfoListSelections("id");
		var name = $(".datagrid-row-selected:eq(1)").find(".datagrid-cell-c1-name").text();
		/* if(!a.length || a == "" || a == null){
			$.messager.alert("提示","未选中数据","info");
		}else{ */
			alertMsg ="是否将【"+name+"】审核人员信息全部通过？"
			$.messager.confirm("提示",alertMsg,function(r){
				$.ajax({
					url:"employeeInfoController.do?batchDeclare&id="+id,
					type:"get",
					success:function(data){
						data = JSON.parse(data);
						if(data.errCode==0){
							/* $.messager.alert("提示","通过成功，请耐心等待审批。","info",function(){
							}); */
							location.reload(true);
						}else{
							$.messager.alert("提示",data.errMsg,"info");
						}
					},
					error:function(){
						$.messager.alert("提示","通过失败","info");
					}
				});
				/* $.ajax({
					url:"employeeInfoController.do?batchDeclare&Id="+id,
					type:"get",
					success:function(data){
						data = JSON.parse(data);
						if(data.errCode==0){
							$.messager.alert("提示","已经全部审批通过。","info",function(){
								location.reload(true);
							});
						}else{
							$.messager.alert("提示",data.errMsg,"info");
						}
					},
					error:function(){
						$.messager.alert("提示","审批通过失败。","info");
					}
				}); */
		});
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

   //退回
   function returnSubmit(){
		if($(".datagrid-btable tr").length<1){
			$.messager.alert("提示","列表没有数据","info");
			return false;
		}
		var id = getemployeeInfoListSelections("id");
		//var departId = $("#orgIds").val();
		var oldReason = getemployeeInfoListSelections("loseReason");
		dcAlert("<div><p>拒绝理由：</p><textarea rows='10' id='returnReason' style='width: 300px;'>"+oldReason+"</textarea></div>",true,function(){
			var returnReason = window.parent.document.getElementById("returnReason").value;
			if(null==returnReason||""==$.trim(returnReason)){
				$.messager.alert("提示","拒绝理由不能为空","info");
			}else if($.trim(returnReason).length>255){
				layer.alert("拒绝理由字数超出长度限制");
			}else{
				$.ajax({
					url:"employeeInfoController.do?batchReturn&Id="+id+
							"&returnReason="+returnReason,
					type:"get",
					success:function(data){
						data = JSON.parse(data);
						if(data.errCode==0){
							/* $.messager.alert("提示","拒绝成功","info",function(){
							}); */
							location.reload(true);
						}else{
							$.messager.alert("提示",data.errMsg,"info",function(){
								location.reload(true);
							});
						}
					},
					error:function(){
						$.messager.alert("提示","拒绝失败","info");
					}
				});
			}
		});
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

   function insuranceSubmit(isId){
	   if($(".datagrid-btable tr").length<1){
		   layer.alert('列表没有数据');
			return false;
		}
	   var id = getemployeeInfoListSelections("id");
	   if(id == "") {
		   layer.alert('未选中数据');
			return false;
		}
	    var insurance = getemployeeInfoListSelections("insurance");
		var names = getemployeeInfoListSelections("name");
		var isErr = false;
		var isErr2 = false;
		var tip1 = "";
		var tip2 = "";
		if(insurance!="") {
			var arr = insurance.toString().split(',');
			var arr2 = names.toString().split(",");
			for(var i=0;i<arr.length;i++) {
				if(isId== 1&&arr[i] != "0") {
					isErr = true;
					tip1+=arr2[i]+"，";
				}
				if(isId== 2&&arr[i] != "2") {
					isErr2 = true;
					tip1+=arr2[i]+"，";
				}
			}
		}else{
			layer.alert('员工：'+names+'目前状态不可进行此操作');
			return false;
		}
		 if(isErr) {
			layer.alert('员工：'+tip1+'目前状态不可进行入保');
			return false;
		}
		if(isErr2) {
			layer.alert('员工：'+tip1+'目前状态不可进行退保');
			return false;
		}
		$.ajax({
			url:"employeeInfoController.do?insuranceChange&id="+id+"&isId="+isId,
			type:"get",
			success:function(data){
				data = JSON.parse(data);
				if(data.errCode==0){
					$("#employeeInfoList").datagrid();
				}else{
					layer.alert(data.errMsg,{closeBtn: 0},function(){
						$("#employeeInfoList").datagrid();
						layer.closeAll('dialog');
					});
				}
			},
			error:function(){
				layer.alert('操作失败');
			}
		});
   }
  </script>
<t:datagrid name="employeeInfoList" title="员工审核" actionUrl="employeeInfoController.do?approvalDatagrid" fitColumns="true" sortName="declareStatus" sortOrder="desc" pageSize="20" idField="id" checkbox="true" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="姓名" field="name" extendParams="resizable:false" query="true" align="center" width="120"></t:dgCol>
   <t:dgCol title="身份证号" field="code" extendParams="resizable:false" align="center" width="220"></t:dgCol>
   <t:dgCol title="部门" field="department" replace="${depts}" query="true" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgCol title="员工类别" field="employeeFlag" extendParams="resizable:false" replace="TECH_0,OP_1" query="true" align="center" width="120"></t:dgCol>
   <t:dgCol title="六金城市" field="sixGoldCity" extendParams="resizable:false" width="120" align="center"></t:dgCol>
   <t:dgCol title="六金基数" field="sixGoldBase" formatterjs="formatTwoDecimal,salaryFormat" extendParams="resizable:false,styler: function(value,row,index){return 'text-align:right;'}" width="120"></t:dgCol>
   <t:dgCol title="入职日" field="entryDate" formatter="yyyy-MM-dd" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgCol title="离职日" field="quitDate" formatter="yyyy-MM-dd" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgCol title="申报状态" field="declareStatus" replace="待审批_4,入职成功_2,未上报_5,离职_1" query="true" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgCol title="社保状态" field="insurance" query="true" replace="待入保_0,已入保_1,待退保_2,已退保_3"  extendParams="resizable:false,styler: function(value,row,index){if(value=='2'||value=='0'){return 'color:red;';}}" align="center" width="120"></t:dgCol>
   <t:dgCol title="所属上级" field="inputName" extendParams="resizable:false" align="center" width="120"></t:dgCol>
   <t:dgToolBar title="查看" icon="icon-search" width="100%" height="100%" url="employeeInfoController.do?addorupdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="入保" icon="icon-le-ok"  url="#" funname="insuranceSubmit(1)"></t:dgToolBar>
   <t:dgToolBar title="退保" icon="icon-le-back" url="#" funname="insuranceSubmit(2)"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-put" url="#" funname="excelExport"></t:dgToolBar>
</t:datagrid>