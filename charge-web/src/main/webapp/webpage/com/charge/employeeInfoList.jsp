<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<head><meta name="format-detection" content="telephone=no"></head>
<t:datagrid name="employeeInfoList" title="员工信息表" actionUrl="employeeInfoController.do?datagrid" idField="id" fit="true" queryMode="group" >
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="员工编号" field="code" extendParams="resizable:false" query="true"></t:dgCol>
   <t:dgCol title="员工姓名" field="name" extendParams="resizable:false" query="true"></t:dgCol>
   <t:dgCol title="所属部门" field="department.departname" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="员工属性" field="employeeFlag" extendParams="resizable:false" replace="派遣_0,本社_1"></t:dgCol>
   <t:dgCol title="A（标准）" field="aStandardSalary" formatterjs="formatTwoDecimal" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="招商银行账户" field="cmbAccount" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="工商银行账户" field="icbcAccount" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="收入确认用A1（工资）" field="a1ConfirmSalary" formatterjs="formatTwoDecimal" extendParams="resizable:false"></t:dgCol>
   <%-- <t:dgCol title="签约法人主体" field="signCorporate.name" extendParams="resizable:false"></t:dgCol> --%>
   <t:dgCol title="六金基数" field="sixGoldBase" formatterjs="formatTwoDecimal" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="发薪地1" field="a1Place" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="发薪地1（金额）" field="a1Payment" formatterjs="formatTwoDecimal" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="双发地点2" field="a2Place" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="双发地2（金额）" field="a2Payment" formatterjs="formatTwoDecimal" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="手机号" field="contactWay" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="户口性质" field="householdRegistration" replace="本市农业_0,本市城镇_1,外埠农业_2,外埠城镇_3" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="性别" field="gender" extendParams="resizable:false" replace="男_0,女_1"></t:dgCol>
   <t:dgCol title="入职日" field="entryDate" formatter="yyyy-MM-dd" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="离职日" field="quitDate" formatter="yyyy-MM-dd" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="离职理由" field="quitReason" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="登录时间" field="loginDate" formatter="yyyy-MM-dd hh:mm:ss" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="创建人" field="createdBy" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="创建日期" field="createdDate" formatter="yyyy-MM-dd hh:mm:ss" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="最后修改人" field="lastModifiedBy" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="最后修改时间" field="lastModifiedDate" formatter="yyyy-MM-dd hh:mm:ss" extendParams="resizable:false"></t:dgCol>
   <t:dgToolBar title="新增" icon="icon-add" url="employeeInfoController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="删除" icon="icon-remove" url="#" funname="delSubmit" operationCode="delBtn"></t:dgToolBar>
  </t:datagrid>
  <div id="tempSearchColums" style="display: none;">
    <div name="searchColums">
       <t:departSelect hasLabel="true" selectedNamesInputId="orgNames" radioEnabled="true" ></t:departSelect>
    </div>
  </div>
  <script>
   $(document).ready(function(){
		//添加部门条件
		var datagrid = $("#employeeInfoListtb");
		datagrid.find("div[name='searchColums']").find("form#employeeInfoListForm span:eq(0)").append($("#tempSearchColums div[name='searchColums']").html());
		$("#tempSearchColums").html('');
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
			$.messager.alert("提示","请选择一条数据","info");
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
  </script>