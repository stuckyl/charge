<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>分配职务</title>
<t:base type="jquery,easyui,tools,DatePicker,ztree"></t:base>
<script type="text/javascript" src="plug-in/ztree/js/ztreeCreator.js"></script>
<script type="text/javascript">
function setUsersCompanyPosition(id) {
	var zNodes;
	var ztreeCreator = new ZtreeCreator('orgTree',"","")
			.setCheckboxType({ "Y": "ps", "N": "ps" })
			.setAsync({
	                enable: true,
	                url:"tSCompanyPositionController.do?getTreeData&departid=${departid}&userId="+id,
	                autoParam:["id", "name", "level"],
	                dataFilter:filter
	            })
 			.initZtree({},function(treeObj){orgTree = treeObj});
	//菜单权限保存
	$("#functionListPanel").panel({
		title : '分配职务',
		tools : [ {
			iconCls : 'icon-save',
			handler : function() {
				mysubmit(id);
			}
		} ]
	});
};

function openAuthorizeSet(id) {
	var zNodes;
	jQuery.ajax({
        async : false,
        cache:false,
        type: 'POST',
        dataType : "json",
        url: 'departAuthGroupController.do?getDepartGroupRoleTree&userId='+id+'&departId=${departid}',//请求的action路径
        error: function () {//请求失败处理函数
            tip('请求失败');
        },
        success:function(data){ //请求成功后处理函数。
            zNodes = data.obj;   //把后台封装好的简单Json格式赋给zNodes
        }
    });

	var ztreeCreator = new ZtreeCreator('orgTree',"departAuthGroupController.do?getDepartRoleTree",zNodes)
			.setCheckboxType({ "Y": "ps", "N": "ps" })
 			.initZtree({},function(treeObj){orgTree = treeObj});
	//菜单权限保存
	$("#functionListPanel").panel({
		title : '授权部门角色',
		tools : [ {
			iconCls : 'icon-save',
			handler : function() {
				mysubmitAuthorize(id);
			}
		} ]
	});
};

function filter(treeId, parentNode, childNodes){
	if (!childNodes) return null;
    for (var i=0, l=childNodes.length; i<l; i++) {
        childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
    }
    return childNodes;
}


//获取实际被选中的节点
function GetNode() {
	var zTree = $.fn.zTree.getZTreeObj("orgTree");
	var node = zTree.getCheckedNodes(true);
	var cnodes = '';
	if(node.length<=0){
		return cnodes;
	}
	for ( var i = 0; i < node.length; i++) {
		cnodes += node[i].id + ',';
	}
	cnodes = cnodes.substring(0, cnodes.length - 1);
	return cnodes;
}
function mysubmit(userId) {
	var s = GetNode();
	$.ajax({
		url : "tSCompanyPositionController.do?saveUserCompanyPosition",
		type : "POST",
		data : {
			"departid":"${departid}",
			"userId":userId,
			"positionIds":s
		},
		success:function(data){
			tip('保存数据成功');
		},
		error:function(data) {
			var d = $.parseJSON(data);
			tip(d.msg);
		}
	});
}
function mysubmitAuthorize(userId) {
	var s = GetAuthNode();
	$.ajax({
		url : "departAuthGroupController.do?saveRoleUser",
		type : "POST",
		data : {
			"userId":userId,
			"roleId":s
		},
		success:function(data){
			tip('保存数据成功');
		},
		error:function(data) {
			var d = $.parseJSON(data);
			tip(d.msg);
		}
	});
}
function update(title,url, id,width,height,isRestful) {

	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		layer.alert('请选择一个用户');
		return;
	}
	if(isRestful!='undefined'&&isRestful){
		url += '/'+rowsData[0].id;
	}else{
		url += '&id='+rowsData[0].id;
	}
	createwindow(title,url,width,height);
}
function add(title,addurl,gname,width,height) {
	$.ajax({
		   url:"myDepartController.do?checkSize&departid=${departid}",
		   type:"get",
		   async:false,
		   success:function(data){
				data = JSON.parse(data);
				 if(data.errCode >0){
					 layer.alert('该部门只能存在一个部门总监');
						return;
				 }else{
					 gridname=gname;
					 createwindow(title, addurl,width,height);
				 }
		   },
		   error:function(){}
	});

}
//获取实际被选中的角色组节点，去除第一节点
function GetAuthNode() {
	var zTree = $.fn.zTree.getZTreeObj("orgTree");
	var node = zTree.getCheckedNodes(true);
	//加入实际被选中的节点
	var cnodes = '';
	for ( var i = 0; i < node.length; i++) {
		//取消一级节点
		if(node[i].isParent!=true){
			cnodes += node[i].id + ',';
		}
	}
	cnodes = cnodes.substring(0, cnodes.length - 1);
	return cnodes;
}
</script>
</head>
<body>
<!-- <div id="divUserList" class="easyui-layout" style="width:100%;">
    <div data-options="region:'center'"> -->
	<t:datagrid name="departUserList" title="common.operation"
	            actionUrl="organzationController.do?userDatagrid&departid=${departid}" fit="true" fitColumns="true" sortName="userName" idField="id" queryMode="group" pageSize="20">
		<t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
		<t:dgCol title="common.username" field="userName" query="true" width="50" align="center"></t:dgCol>
		<t:dgCol title="common.real.name" field="realName" query="true" width="50" align="center"></t:dgCol>
		<t:dgCol title="common.status" sortable="true" width="40" field="status" replace="common.active_1,common.inactive_0,super.admin_-1" align="center"></t:dgCol>
		<t:dgCol title="权限角色" field="roleName" sortable="false"  width="40" align="center"></t:dgCol>
		<%-- <t:dgCol title="common.operation" field="opt" width="130"></t:dgCol>
		<t:dgDelOpt title="解除机构关系" url="organzationController.do?delUserOrg&userid={id}&departid=${departid }" urlclass="ace_button"  urlfont="fa-trash-o"/>
		<t:dgFunOpt funname="setUsersCompanyPosition(id)" title="分配职务" urlclass="ace_button"  urlfont="fa-user"></t:dgFunOpt>
	    <t:dgFunOpt funname="openAuthorizeSet(id)" title="分配部门角色" urlclass="ace_button"  urlfont="fa-cog" urlStyle="background-color:#18a689;"></t:dgFunOpt> --%>
		<t:dgCol title="common.operation" field="opt" width="130" align="center"></t:dgCol>
		<t:dgFunOpt funname="resetPwd(this)" title="重置密码" urlclass="ace_button"  urlfont="fa-cog" urlStyle="background-color:#18a689;"></t:dgFunOpt>
		<t:dgFunOpt funname="deleteDialog(id)" title="common.delete" urlclass="ace_button"  urlfont="fa-trash-o"></t:dgFunOpt>
		<t:dgToolBar title="common.add.param" width="100%" height="100%" langArg="common.user" icon="icon-add" url="userController.do?addorupdateMyOrgUser2&departid=${departid}" funname="add"></t:dgToolBar>
		<t:dgToolBar title="common.edit.param" width="100%" height="100%"  langArg="common.user" icon="icon-edit" url="userController.do?addorupdateMyOrgUser2&departid=${departid}" funname="update"></t:dgToolBar>
		<t:dgToolBar title="common.lock.user" icon="icon-remove" url="userController.do?lock&lockvalue=0" funname="lockObj"></t:dgToolBar>
		<t:dgToolBar title="common.unlock.user" icon="icon-le-ok" url="userController.do?lock&lockvalue=1" funname="unlockObj"></t:dgToolBar>
		<%-- <t:dgToolBar title="添加已有用户" icon="icon-add" url="organzationController.do?goAddMyOrgUserToOrg&orgId=${departid}" funname="add" width="650"></t:dgToolBar> --%>
	</t:datagrid>
<!--  </div>
</div> -->
<!-- <div region="east" title="分配职务" style="width: 300px;" split="true" id="functionListPanel">
	<div id="orgTree" class="ztree"></div>
</div> -->

</body>
<script type="text/javascript">
$(document).ready(function(){
	initCusToolbarStyle();
	$('.datagrid-toolbar').children("span").attr("style","float:left;margin-top:-3px");
});
//初始化工具栏按钮
function initCusToolbarStyle(){
	   	for(var i = 0;i<$(".datagrid-toolbar").length;i++){
	   		if($($(".datagrid-toolbar").eq(i)).attr("style").indexOf("border-bottom-width")!=-1){
	   			$(".datagrid-toolbar").eq(i).css("height","34.5px");
	   			$(".datagrid-toolbar").eq(i).find(".l-btn-left").css({"width":"75px","height":"22.5px"});
	   			$(".datagrid-toolbar").eq(i).find(".l-btn-left .l-btn-text").css({"height":"20px","line-height":"22px","font-size":"13px"});
	   		}
	   	}
}

	var divHeight = $(window).height();
	$("#divUserList").css("height",divHeight+"px");

	function resetPwd(obj){
		setTimeout(function(){
			update('密码重置','userController.do?changepasswordforuser','departUserList',null,null);
		},30);
	}
</script>
<script type="text/javascript">
function deleteDialog(id){
	var url = "myDepartController.do?departUserDel&id=" + id;
	layer.confirm('确认删除该账号？', {
        btn: ['确定','取消'], //按钮
        shade: false //不显示遮罩
    }, function(){
			$.ajax({
				async : false,
				type: "get",
				dataType : "json",
		        url: url,//请求的action路径
		        success:function(data){ //请求成功后处理函数。
				    if(data.success){
				    	reloadTable();
				    	layer.closeAll('dialog');
				    }else{
				    	layer.alert(data.msg);
				    }
		        },
				error: function () {//请求失败处理函数
					layer.alert('请求失败');
		        }
			});
    }, function(){
        return;
    });
}
function lockObj(title,url, id) {

	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		layer.alert('请选择一个用户');
		return;
	}
	 if(rowsData[0].status == 0){
	    	layer.alert('您选择的用户无需锁定');
	        return;
	    }
		url += '&id='+rowsData[0].id;
		layer.confirm('确定锁定该用户？', {
            btn: ['确定','取消'], //按钮
            shade: false //不显示遮罩
        }, function(){
        	lockuploadify(url, '&id');
        }, function(){
            return;
        });
}
function unlockObj(title,url, id) {
	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		layer.alert('请选择一个用户');
		return;
	}
    if(rowsData[0].status == 1){
    	layer.alert('您选择的用户无需激活');
        return;
    }
		url += '&id='+rowsData[0].id;
		layer.confirm('确定激活该用户？', {
            btn: ['确定','取消'], //按钮
            shade: false //不显示遮罩
        }, function(){
        	lockuploadify(url, '&id');
        }, function(){
            return;
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
			var d = $.parseJSON(data);
			if (d.success) {
			var msg = d.msg;
			layer.alert(msg);
				reloadTable();
			}
		}
	});
}
</script>
</html>