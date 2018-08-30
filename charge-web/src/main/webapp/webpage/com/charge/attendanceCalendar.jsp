<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>一年日程</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <style type="text/css">
  	label{
  	    width: 40px;
    	display: inline-block;
    	text-align: right;
  	}
/* 	input{
  		width: 80px;
  	} */
  </style>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="attendanceCalendarController.do?save" callback="initTableHeaderColor" tiptype="1" beforeSubmit="otherCheck()">
		<input id="id" name="id" type="hidden" value="${attendanceCalendarPage.id }">
		<div style="width:100%;text-align:center;background:white" class="formtable">
		   <div style="border:1px solid #ababab;padding:5px;border-radius: 5px;margin-top:30px;padding-bottom: 20px;">
		        <div style="position:relative;background:white;top:-15px;width: 155px;border:0px solid #ababab;">
		        	<select id="year" name="year"  onchange="gradeChange()" >
					<c:if test="${attendanceCalendarPage.year!=null }">
						<option value="${attendanceCalendarPage.year}" selected="selected" >${attendanceCalendarPage.year }年</option>
					</c:if>
					</select>
		        </div>
		        <div style="margin-top: -25px;">
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">1月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month1" name="month1" maxlength="2" value="${attendanceCalendarPage.month1 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays1" name="totaldays1" disabled="disabled" value="${attendanceCalendarPage.totaldays1 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends1" name="weekends1" maxlength="2" value="${attendanceCalendarPage.weekends1 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays1" name="holidays1" maxlength="2" value="${attendanceCalendarPage.holidays1 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">2月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month2" name="month2" maxlength="2" value="${attendanceCalendarPage.month2 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays2" name="totaldays2" disabled="disabled" value="${attendanceCalendarPage.totaldays2 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends2" name="weekends2" maxlength="2" value="${attendanceCalendarPage.weekends2 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays2" name="holidays2" maxlength="2" value="${attendanceCalendarPage.holidays2 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
					<label class="Validform_label">3月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month3" name="month3" maxlength="2" value="${attendanceCalendarPage.month3 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays3" name="totaldays3" disabled="disabled" value="${attendanceCalendarPage.totaldays3 }" style="width:80px;"
						autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends3" name="weekends3" maxlength="2" value="${attendanceCalendarPage.weekends3 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays3" name="holidays3" maxlength="2" value="${attendanceCalendarPage.holidays3 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">4月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month4" name="month4" maxlength="2" value="${attendanceCalendarPage.month4 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays4" name="totaldays4" disabled="disabled" value="${attendanceCalendarPage.totaldays4 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends4" name="weekends4" maxlength="2" value="${attendanceCalendarPage.weekends4 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays4" name="holidays4" maxlength="2" value="${attendanceCalendarPage.holidays4 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">5月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month5" name="month5" maxlength="2" value="${attendanceCalendarPage.month5 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays5" name="totaldays5" disabled="disabled" value="${attendanceCalendarPage.totaldays5 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends5" name="weekends5" maxlength="2" value="${attendanceCalendarPage.weekends5 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays5" name="holidays5" maxlength="2" value="${attendanceCalendarPage.holidays5 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
					<label class="Validform_label">6月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month6" name="month6" maxlength="2" value="${attendanceCalendarPage.month6 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays6" name="totaldays6" disabled="disabled" value="${attendanceCalendarPage.totaldays6 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends6" name="weekends6" maxlength="2" value="${attendanceCalendarPage.weekends6 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays6" name="holidays6" maxlength="2" value="${attendanceCalendarPage.holidays6 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">7月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month7" name="month7" maxlength="2" value="${attendanceCalendarPage.month7 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays7" name="totaldays7" disabled="disabled" value="${attendanceCalendarPage.totaldays7 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends7" name="weekends7" maxlength="2" value="${attendanceCalendarPage.weekends7 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays7" name="holidays7" maxlength="2" value="${attendanceCalendarPage.holidays7 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">8月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month8" name="month8" maxlength="2" value="${attendanceCalendarPage.month8 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays8" name="totaldays8" disabled="disabled" value="${attendanceCalendarPage.totaldays8 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends8" name="weekends8" maxlength="2" value="${attendanceCalendarPage.weekends8 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays8" name="holidays8" maxlength="2" value="${attendanceCalendarPage.holidays8 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						 <span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">9月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month9" name="month9" maxlength="2" value="${attendanceCalendarPage.month9 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays9" name="totaldays9" disabled="disabled" value="${attendanceCalendarPage.totaldays9 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends9" name="weekends9" maxlength="2" value="${attendanceCalendarPage.weekends9 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays9" name="holidays9" maxlength="2" value="${attendanceCalendarPage.holidays9 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">10月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month10" name="month10" maxlength="2" value="${attendanceCalendarPage.month10 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays10" name="totaldays10" disabled="disabled" value="${attendanceCalendarPage.totaldays10 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends10" name="weekends10" maxlength="2" value="${attendanceCalendarPage.weekends10 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays10" name="holidays10" maxlength="2" value="${attendanceCalendarPage.holidays10 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">11月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month11" name="month11" maxlength="2" value="${attendanceCalendarPage.month11 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays11" name="totaldays11" disabled="disabled" value="${attendanceCalendarPage.totaldays11 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends11" name="weekends11" maxlength="2" value="${attendanceCalendarPage.weekends11 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays11" name="holidays11" maxlength="2" value="${attendanceCalendarPage.holidays11 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		        <div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label">12月:</label>
						<label class="Validform_label"><span style="color:red">*</span>工作日数:</label>
						<input class="inputxt" id="month12" name="month12" maxlength="2" value="${attendanceCalendarPage.month12 }" style="width:80px;"
						datatype="/^[1][3-9]$/|/^2[0-5]$/" errorMsg="请填写13-25的整数！" nullMsg="输入不能为空！" placeholder="请输入13-25" autocomplete="off"
						/>
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>自然日数:</label>
						<input class="inputxt" id="totaldays12" name="totaldays12" disabled="disabled" value="${attendanceCalendarPage.totaldays12 }" style="width:80px;"
						 />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div>
		        	<div style="margin-right:25px;float:left;height:35px">
		        		<label class="Validform_label"><span style="color:red">*</span>双休日数:</label>
						<input class="inputxt" id="weekends12" name="weekends12" maxlength="2" value="${attendanceCalendarPage.weekends12 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[4-9]$/" errorMsg="请填写4-10的整数！" nullMsg="输入不能为空！" placeholder="请输入4-10" autocomplete="off"
						 />
						<span class="Validform_checktip"></span>
					</div>
					<div style="margin-right:25px;float:left;height:35px">
						<label class="Validform_label"><span style="color:red">*</span>公休日数:</label>
						<input class="inputxt" id="holidays12" name="holidays12" maxlength="2" value="${attendanceCalendarPage.holidays12 }" style="width:80px;"
						datatype="/^[1][0]$/|/^[0-9]$/" errorMsg="请填写0-10的整数！" nullMsg="输入不能为空！" placeholder="请输入0-10" autocomplete="off"
						   />
						<span class="Validform_checktip"></span>
					</div>
		        </div>
		        <div style="clear:both"></div>
		   </div>
		</div>
  </t:formvalid>
  <script type="text/javascript">
  var year1 = document.getElementById("year").value;
  function YearFunction(){
 	 	var dd = new Date();
 	    var currentYear = dd.getFullYear();
 	    var yearOld=0;
 	    var year2 = document.getElementById("year");
 		 year2.options.length=0;
 	        $("#year").append($("<option value=-1 onclick='cleanYear()'>--- 请选择 ---</option>"));
 	    for( var i=0; i<currentYear+4-2015; i++ ){
 	        var yearOld =2015+i;
 	        $("#year").append($("<option value="+yearOld+">"+yearOld+"年</option>"));
 		}
  }
  $(function(){
 	if(year1==""){
 	 YearFunction();
 	}else{
 	 YearFunction();
 	 $("select").find('option[value="'+year1+'"]').attr("selected",true);
 	}
 	limitInput();
  });
  function limitInput() {
	  for(var i=1;i<13;i++) {
		  $('#month'+i).numberbox({
			    min:0,
		  });
		  $('#weekends'+i).numberbox({
			    min:0,
		  });
		  $('#holidays'+i).numberbox({
			    min:0,
		  });
	  }

  }
  function gradeChange(){
		 //存入输入的年份，查看数据库中是否已有此年份
		 var year = document.getElementById("year").value;

		 var flag=true;
		 if(year1!=null&&year1!=year){
		 $.ajax({
			   url: "attendanceCalendarController.do?watch&year="+year,
			   type: "get",
			   async:false,
			   success: function(data){
				   data = JSON.parse(data);
					if(1==data.watchY){
						flag = false;
						$("select[name='year'] option[value='-1']").attr("selected", true).prop("selected", true);//设置下拉框的值变为初始默认值
						layer.alert("已存在此年份工作日程！");
					}else {
						generateMonthDays(year);
					}
			   },
			   error:function(){}
		 });
		 return flag;
	 }
  }
  function generateMonthDays(year) {
	  for(var i=1;i<13;i++) {
		  var month = new Date(year,i,0);
		  var daycount = month.getDate();
		  var tmp1 = "totaldays"+i;
		  $("#"+tmp1).val(daycount);
	  }
  }

  function handleNum(num){
	  if(null==num||""==$.trim(num)||typeof(num)=="undefined"){
		  num = 0.0;
	  }
	  if(isNaN(num)){  //如果 是一个 非数字
		  if(num.indexOf(",")!=-1){
			  num=num.replace(/,/gi,'');
		  }
	  }
	  return parseFloat(num);
  }
  function otherCheck(){
		 if(document.getElementById("year").value==-1){
			 layer.alert("请选择年份！");
			 return false;
		 }
		 for(var i=1;i<13;i++) {
			  var t1=handleNum($('#month'+i).val());
			  var t2=handleNum($('#weekends'+i).val());
			  var t3=handleNum($('#holidays'+i).val());
			  var t4=handleNum($('#totaldays'+i).val());
			  if(t1+t2+t3!=t4) {
				  layer.alert(i+"月工作日数、双休日数和公休日数之和与该月自然日数不匹配！");
				  return false;
			  }
		  }
		 for(var i=1;i<13;i++) {
			 $('#totaldays'+i).prop("disabled", false);
		  }
  }
  </script>
 </body>