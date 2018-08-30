<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="expensesEntityList" title="经费审核" actionUrl="expensesEntityController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="活动年月 201802、201803、201804" field="startMonth" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="活动起始日期" field="startDate" formatter="yyyy-MM-dd"  width="120"></t:dgCol>
   <t:dgCol title="活动日数" field="numberDate"   width="120"></t:dgCol>
   <t:dgCol title="活动主题" field="theme"   width="120"></t:dgCol>
   <t:dgCol title="活动内容" field="content"   width="120"></t:dgCol>
   <t:dgCol title="申请金额" field="money"   width="120"></t:dgCol>
   <t:dgCol title="活动参与人数" field="numberPeople"   width="120"></t:dgCol>
   <t:dgCol title="人均消费" field="average"   width="120"></t:dgCol>
   <t:dgCol title="活动参与人" field="namePeople"   width="120"></t:dgCol>
   <t:dgCol title="所属部门" field="departmentId"   width="120"></t:dgCol>
   <t:dgCol title="申报状态   1总监录入待处理，2审核拒绝，3审核待处理，4审批拒绝，5审批待处理，6审批通过， 7申报中8，审计录入待处理 " field="declareStatus"   width="120"></t:dgCol>
   <t:dgCol title="失败理由" field="declareReturnreason"   width="120"></t:dgCol>
   <t:dgCol title="录入者" field="inputerId"   width="120"></t:dgCol>
   <t:dgCol title="申报者" field="reporterId"   width="120"></t:dgCol>
   <t:dgCol title="审核者" field="checkerId"   width="120"></t:dgCol>
   <t:dgCol title="创建人" field="createdBy"   width="120"></t:dgCol>
   <t:dgCol title="创建日期" field="createdDate" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="删除标记  0未删除 1已删除" field="expensesDelFlg"   width="120"></t:dgCol>
   <t:dgCol title="最后修改人" field="lastModifiedBy"   width="120"></t:dgCol>
   <t:dgCol title="最后修改时间" field="lastModifiedDate" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="修改标记  0_编辑中  1_无人编辑" field="editors"   width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="expensesEntityController.do?del&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   <t:dgToolBar title="录入" icon="icon-add" url="expensesEntityController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="expensesEntityController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="expensesEntityController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>