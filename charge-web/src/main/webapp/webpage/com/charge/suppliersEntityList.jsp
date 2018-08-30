<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<head><meta name="format-detection" content="telephone=no"></head>
<script>
/**
 * 签约法人 未录入 禁止录入客户
 */
 function add(title,addurl,gname,width,height) {
	$.ajax({
		  url:"corporateInfoController.do?isNull",
		  type:"get",
		  success:function(data){
			  if(data=="1") {
				  gridname=gname;
				  createwindow(title, addurl,width,height);
				  return;
			  }else {
				  layer.alert("请先录入签约法人");
				  return ;
			  }
		  },
		  error:function(){
			  layer.alert("网络异常");
		  }
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
		if(isRestful!='undefined'&&isRestful){
			url += '/'+rowsData[0].id;
		}else{
			url += '&id='+rowsData[0].id;
		}
		createwindow(title,url,width,height);
	}
 function delObj(url,name) {
	 var U = url.split("&")[0];
	 var flg = url.split("=")[2];
	 var del = "suppliersController.do?del";
	 var Frozen = "suppliersController.do?Frozen";
	 var activation = "suppliersController.do?activation";
	 var T;
	 if(del==U){
		 T="确认删除？";
	 }else if(Frozen==U){
		 T="确认冻结？";
		 if(flg==1){
			 layer.alert("不可重复冻结");
			 return false;
		 }
	 }else if(activation==U){
		 T="确认激活？";
		 if(flg==0){
			 layer.alert("不可重复激活");
			 return false;
		 }
	 }
	 var name = name;
		gridname=name;
		layer.confirm(T, {
         btn: ['确定','取消'], //按钮
         shade: false //不显示遮罩
     	}, function(){
     	$.ajax({
				url: url,
				type: "get",
				success: function(data){
					data = JSON.parse(data);
					/* layer.alert(data.msg,function(){
						reloadTable();
						layer.closeAll('dialog');
					}); */
					reloadTable();
					layer.closeAll('dialog');
				}
			});
     }, function(){
         return;
     });
}
 /* 处理约定工作日数、帐龄 */
  function salaryFormat(value,row,index){
	  var value = ""+value;
 	var strs = value.split('.');
 	if(strs[1].length==1){
 		strs[1]=strs[1]+"0";
 	}
     str_n=strs[0]+"."+strs[1];
   return str_n;
 }
 $(function(){
	//初始化表头颜色
	 /* initTableHeaderColor(); */
	 initCusToolbarStyle();
 })
 //初始化表头颜色
/* function initTableHeaderColor(){
	var ths = $(".datagrid-header-row");
	for(var i = 1;i < ths.length;i++){
	    switch(i){
	        case 1:
	        	for(var j = 0;j<$(ths).eq(1).find("td").length;j++){
                    $(ths).eq(1).find("td").eq(j).css("background","#C6E0B4");
                }
	    }
	}
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
  </script>
<t:datagrid name="suppliersEntityList" title="供应商" actionUrl="suppliersController.do?datagrid" fitColumns="false"
 pageSize="20" idField="id" checkbox="true" fit="true" queryMode="group"  sortName="activeFlg" sortOrder="asc">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="简称" field="code"  align="center" query="true" width="120"></t:dgCol>
   <t:dgCol title="全称" field="name" align="center" showLen="16"></t:dgCol>
   <t:dgCol title="企业性质" field="corporateType" replace="小微_4,其他_6"  align="center" width="100" query="true"></t:dgCol>
   <t:dgCol title="所在省" field="province" align="center" showLen="22" width="120"></t:dgCol>
   <t:dgCol title="所在市" field="city" align="center" width="120"></t:dgCol>
   <t:dgCol title="开户银行" field="openBank" align="center" width="120"></t:dgCol>
   <t:dgCol title="银行账号" field="bankAccount" align="center" width="120"></t:dgCol>
   <t:dgCol title="签约法人" field="signCorporate.Code"  align="center" width="120"></t:dgCol>
   <t:dgCol title="激活状态" field="activeFlg" replace="冻结_1,激活_0,"  align="center" query="true" width="80"></t:dgCol>
   <t:dgCol title="操作" field="opt" align="center" width="140"></t:dgCol>
   <t:dgDelOpt title="冻结" url="suppliersController.do?Frozen&id={id}&activeFlg={activeFlg}" urlclass="ace_button"  urlfont="fa-lock"/>
   <t:dgDelOpt title="激活" url="suppliersController.do?activation&id={id}&activeFlg={activeFlg}" urlclass="ace_button"  urlfont="fa-unlock"/>
   <t:dgToolBar title="录入" icon="icon-add"  width="660"  height="100%" url="suppliersController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit"  width="660"  height="100%"  url="suppliersController.do?addorupdate" funname="update"></t:dgToolBar>
 </t:datagrid>