<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="emailConfigList" title="邮箱配置" actionUrl="emailConfigController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="主机" field="host"   width="120"></t:dgCol>
   <t:dgCol title="账号" field="account"   width="120"></t:dgCol>
   <t:dgCol title="密码" field="password"   width="120"></t:dgCol>
   <t:dgCol title="最后发送时间" field="lastSendTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="发送次数" field="sendCount"   width="120"></t:dgCol>
   <t:dgCol title="最大可发送次数" field="maxCount"   width="120"></t:dgCol>
   <t:dgCol title="发送人" field="mailFrom"   width="120"></t:dgCol>
   <t:dgCol title="端口" field="port"   width="120"></t:dgCol>
   <t:dgCol title="是否开启SSL" field="openSsl"   width="120"></t:dgCol>
   <t:dgCol title="创建人" field="createdBy"   width="120"></t:dgCol>
   <t:dgCol title="创建日期" field="createdDate" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="最后修改人" field="lastModifiedBy"   width="120"></t:dgCol>
   <t:dgCol title="最后修改时间" field="lastModifiedDate" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="emailConfigController.do?del&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   <t:dgToolBar title="录入" icon="icon-add" url="emailConfigController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="emailConfigController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="emailConfigController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>