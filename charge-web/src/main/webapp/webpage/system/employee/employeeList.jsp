<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>

<t:eDatagrid name="userList" title="common.operation" actionUrl="userController.do?datagrid" fit="true">
	<t:eDgCol title="ID" field="realName"></t:eDgCol>
	<t:eDgCol title="姓名" field="realName"></t:eDgCol>
	<t:eDgCol title="部门" field="realName"></t:eDgCol>
	<t:eDgCol title="直签客户" field="realName"></t:eDgCol>
	<t:eDgCol title="转签客户" field="realName"></t:eDgCol>
	<t:eDgCol title="月单价" field="realName"></t:eDgCol>
	<t:eDgCol title="日单价" field="realName"></t:eDgCol>
	<t:eDgCol title="小时单价" field="realName"></t:eDgCol>
	<t:eDgCol title="约定出勤日数" field="realName"></t:eDgCol>
	<t:eDgCol title="验收出勤日数" field="realName"></t:eDgCol>
	<t:eDgCol title="当月其它" field="realName"></t:eDgCol>
	<t:eDgCol title="验收加算" field="realName"></t:eDgCol>
	<t:eDgCol title="月间调整" field="realName"></t:eDgCol>
	<t:eDgCol title="收入" field="realName"></t:eDgCol>
	<t:eDgCol title="PO编号" field="realName"></t:eDgCol>
	<t:eDgCol title="法定出勤日数" field="realName"></t:eDgCol>
	<t:eDgCol title="有绩效出勤日数" field="realName"></t:eDgCol>
	<t:eDgCol title="无绩效出勤日数" field="realName"></t:eDgCol>
	<t:eDgCol title="Ａ1（工资）" field="realName"></t:eDgCol>
	<t:eDgCol title="Ａ2（绩效）" field="realName"></t:eDgCol>
	<t:eDgCol title="Ａ（标准）" field="realName"></t:eDgCol>
	<t:eDgCol title="Ｂ（折扣率）" field="realName"></t:eDgCol>
	<t:eDgCol title="应付工资" field="realName"></t:eDgCol>
	<t:eDgCol title="应付绩效" field="realName"></t:eDgCol>
	<t:eDgCol title="Ｃ1（电脑补贴）" field="realName"></t:eDgCol>
	<t:eDgCol title="Ｃ2（加班费)" field="realName"></t:eDgCol>
	<t:eDgCol title="Ｃ3（其它补贴)" field="realName"></t:eDgCol>
	<t:eDgCol title="Ｃ（补贴）" field="realName"></t:eDgCol>
	<t:eDgCol title="应发合计" field="realName"></t:eDgCol>
	<t:eDgCol title="备注" field="realName"></t:eDgCol>
	<t:eDgFunOpt funname="deleteDialog(id)" title="common.delete" urlclass="ace_button"  urlfont="fa-trash-o"></t:eDgFunOpt>
	<t:eDgToolBar title="common.add.param" langArg="common.user" icon="icon-add" url="userController.do?addorupdate" funname="add"></t:eDgToolBar>
	<t:eDgToolBar title="common.edit.param" langArg="common.user" icon="icon-edit" url="userController.do?addorupdate" funname="add"></t:eDgToolBar>
	<t:eDgToolBar title="申报" langArg="common.user" icon="icon-add" url="userController.do?addorupdate" funname="xxx"></t:eDgToolBar>
</t:eDatagrid>
<script>
    $(function() {
        var datagrid = $("#userListtb");
		datagrid.find("div[name='searchColums']").find("form#userListForm").append($("#realNameSearchColums div[name='searchColumsRealName']").html());
		$("#realNameSearchColums").html('');
        datagrid.find("div[name='searchColums']").find("form#userListForm").append($("#tempSearchColums div[name='searchColums']").html());
        $("#tempSearchColums").html('');
	});
</script>
<div id="realNameSearchColums" style="display: none;">
	<div name="searchColumsRealName">
		<t:userSelect hasLabel="true" selectedNamesInputId="realName" windowWidth="1000px" windowHeight="600px" title="用户名称"></t:userSelect>
	</div>
</div>
<div id="tempSearchColums" style="display: none;">
    <div name="searchColums">
       <t:departSelect hasLabel="true" selectedNamesInputId="orgNames"></t:departSelect>
    </div>
</div>
<script type="text/javascript">
function deleteDialog(id){
	var url = "userController.do?deleteDialog&id=" + id
	createwindow("删除模式", url, 200, 100);
}
function lockObj(title,url, id) {

	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		tip('<t:mutiLang langKey="common.please.select.edit.item"/>');
		return;
	}
		url += '&id='+rowsData[0].id;

	$.dialog.confirm('<t:mutiLang langKey="common.lock.user.tips"/>', function(){
		lockuploadify(url, '&id');
	}, function(){
	});
}
function unlockObj(title,url, id) {
	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		tip('<t:mutiLang langKey="common.please.select.edit.item"/>');
		return;
	}
		url += '&id='+rowsData[0].id;

	$.dialog.confirm('<t:mutiLang langKey="common.unlock.user.tips"/>', function(){
		lockuploadify(url, '&id');
	}, function(){
	});
}


function lockuploadify(url, id) {
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		url : url,// 请求的action路径
		error : function() {// 请求失败处理函数
		
		},
		success : function(data) {
			var d = typeof(data)=='string'?$.parseJSON(data):data;
			if (d.success) {
			var msg = d.msg;
				tip(msg);
				reloadTable();
			}
		}
	});
}
</script>

<script type="text/javascript">
	//导入
	function ImportXls() {
		openuploadwin('Excel导入', 'userController.do?upload', "userList");
	}

	//导出
	function ExportXls() {
		JeecgExcelExport("userController.do?exportXls", "userList");
	}

	//模板下载
	function ExportXlsByT() {
		JeecgExcelExport("userController.do?exportXlsByT", "userList");
	}
</script>