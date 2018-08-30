<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript">
function myupdate(id) {
	createwindow('编辑','constantDictionaryController.do?addorupdate&id='+id,600,200);
}
</script>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
	  <t:datagrid name="constantDictionaryList" title="系统参数" actionUrl="constantDictionaryController.do?datagrid" idField="id" fit="true">
	   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
	   <t:dgCol title="名称" field="constantName"   width="120" align="center"></t:dgCol>
	   <t:dgCol title="值" field="constantValue"   width="120" align="center"></t:dgCol>
	  <t:dgCol title="操作" field="opt" width="100" align="center"></t:dgCol>
	  <%--  <t:dgOpenOpt url="constantDictionaryController.do?addorupdate&id={id}" title="编辑" height="200" width="600"></t:dgOpenOpt> --%>
	   <t:dgFunOpt funname="myupdate(id)" title="编辑" urlclass="ace_button" ></t:dgFunOpt>
	   <%-- <t:dgDelOpt title="删除" url="constantDictionaryController.do?del&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/> --%>
	  <%--  <t:dgToolBar title="录入" icon="icon-add" url="constantDictionaryController.do?addorupdate" funname="add"></t:dgToolBar>
	   <t:dgToolBar title="编辑" icon="icon-edit" url="constantDictionaryController.do?addorupdate" funname="update"></t:dgToolBar>
	   <t:dgToolBar title="查看" icon="icon-search" url="constantDictionaryController.do?addorupdate" funname="detail"></t:dgToolBar> --%>
	  </t:datagrid>
  </div>
</div>