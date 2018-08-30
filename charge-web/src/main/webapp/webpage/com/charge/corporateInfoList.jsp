<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script>
 $(function(){
	//初始化表头颜色
	/*  initTableHeaderColor(); */
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
 function delObj(url,name) {
		gridname=name;
		layer.confirm('确认删除？', {
            btn: ['确定','取消'], //按钮
            shade: false //不显示遮罩
        }, function(){
        	$.ajax({
				url: url,
				type: "get",
				success: function(data){
					data = JSON.parse(data);
					layer.alert(data.msg,function(){
						reloadTable();
						layer.closeAll('dialog');
					});
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
		if(isRestful!='undefined'&&isRestful){
			url += '/'+rowsData[0].id;
		}else{
			url += '&id='+rowsData[0].id;
		}
		createwindow(title,url,width,height);
	}
 </script>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="corporateInfoList" title="法人信息" actionUrl="corporateInfoController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="法人简称" field="code"   width="60" align="center"></t:dgCol>
   <t:dgCol title="法人全称" field="name"   width="120" align="center" showLen="30"></t:dgCol>
  <%--  <t:dgCol title="创建人" field="createdBy"   width="120"></t:dgCol>
   <t:dgCol title="创建日期" field="createdDate" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="最后修改人" field="lastModifiedBy"   width="120"></t:dgCol>
   <t:dgCol title="最后修改时间" field="lastModifiedDate" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol> --%>
   <t:dgCol title="操作" field="opt" width="60" align="center"></t:dgCol>
   <t:dgDelOpt title="删除" url="corporateInfoController.do?del&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   <t:dgToolBar title="录入" icon="icon-add"  width="660"  url="corporateInfoController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit"  width="660"  url="corporateInfoController.do?addorupdate" funname="update"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
