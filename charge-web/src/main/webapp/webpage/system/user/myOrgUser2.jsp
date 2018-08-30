<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>
<t:base type="jquery,easyui,tools"></t:base>
    <script>
<%-- //        update-start--Author:zhangguoming  Date:20140826 for：将combobox修改为combotree
        function setOrgIds() {
//            var orgIds = $("#orgSelect").combobox("getValues");
            var orgIds = $("#orgSelect").combotree("getValues");
            $("#orgIds").val(orgIds);
        }
        $(function() {
            $("#orgSelect").combotree({
                onChange: function(n, o) {
                    if($("#orgSelect").combotree("getValues") != "") {
                        $("#orgSelect option").eq(1).attr("selected", true);
                    } else {
                        $("#orgSelect option").eq(1).attr("selected", false);
                    }
                }
            });
            $("#orgSelect").combobox("setValues", ${orgIdList});
            $("#orgSelect").combotree("setValues", ${orgIdList});
        }); --%>

        function setOrgIds(){
        	alert($.trim($("#roleName").val()));
        	if(""==$.trim($("#roleName").val())){
                $.Showmsg("请选择角色权限");
     		   return false;
     	   }
        	return true;
        }

		function openDepartmentSelect() {
			$.dialog.setting.zIndex = getzIndex();
			var orgIds = $("#orgIds").val();

			$.dialog({content: 'url:organzationController.do?myDepartSelect&orgIds='+orgIds, zIndex: getzIndex(), title: '组织机构列表', lock: true, width: '400px', height: '350px', opacity: 0.4, button: [
			   {name: '<t:mutiLang langKey="common.confirm"/>', callback: callbackDepartmentSelect, focus: true},
			   {name: '<t:mutiLang langKey="common.cancel"/>', callback: function (){}}
		   ]}).zindex();

		}

		   function roleName(){
			   $.ajax({
				   url: "userController.do?roleNameList&departid=${orgIds}",
				   type: "get",
				   success: function(data){
					   data = JSON.parse(data);
					   if(data.length==2){
						   var cityOpt = "<option value='"+data[1]+"' selected='selected'>"+data[0]+"</option>";
					   }else{
						   for(var i = 0;i<data.length-1;i+=2){
							   if("${fn:replace(roleName,",","")}"==data[i]){
								   cityOpt+= "<option value='"+data[i+1]+"' selected='selected'>"+data[i]+"</option>";
							   }else{
								   cityOpt+= "<option value='"+data[i+1]+"' >"+data[i]+"</option>";
							   }
						   }
					   }
					   $("#roleName").html(cityOpt);
				   },
				   error:function(){}
			   });
		   }
		function callbackDepartmentSelect() {
			  var iframe = this.iframe.contentWindow;
			  var treeObj = iframe.$.fn.zTree.getZTreeObj("departSelect");
			  var nodes = treeObj.getCheckedNodes(true);
			  if(nodes.length>0){
			  var ids='',names='';
			  for(i=0;i<nodes.length;i++){
			     var node = nodes[i];
			     ids += node.id+',';
			    names += node.name+',';
			 }
			 $('#departname').val(names);
			 $('#departname').blur();
			 $('#orgIds').val(ids);
			}
		}

		function callbackClean(){
			$('#departname').val('');
			 $('#orgIds').val('');
		}

		function setOrgIds() {}
		$(function(){
			$("#departname").prev().hide();
		});
        $(document).ready(function(){
        	roleName();
        });
    </script>
</head>
<body >
<t:formvalid formid="formobj" dialog="true" usePlugin="password" tiptype="1" layout="table" action="userController.do?saveUser" beforeSubmit="setOrgIds()">
	<input id="id" name="id" type="hidden" value="${user.id }"/>
	<input id="devFlag" name="devFlag" type="hidden" value="0"/>
	<input id="activitiSync" name="activitiSync" type="hidden" value="1"/>
	<input id="userType" name="userType" type="hidden" value="1"/>
	<input id="orgIds" name="orgIds" type="hidden" value="${orgIds}"/>
	<table style="width: 100%;" cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right" width="25%" nowrap>
                <label class="Validform_label"><span style="color:red">*</span> <t:mutiLang langKey="common.username"/>:  </label>
            </td>
			<td class="value" width="85%">
                <c:if test="${user.id!=null }"> ${user.userName } </c:if>
                <c:if test="${user.id==null }">
                    <input id="userName" class="inputxt" name="userName" autocomplete="off" validType="t_s_base_user,userName,id" value="${user.userName }" datatype="/(^[0-9a-zA-Z-]{2,18}$)/" />
                    <span class="Validform_checktip"> <t:mutiLang langKey="请输入2到18个大、小写字母或数字"/></span>
                </c:if>
            </td>
		</tr>
		<tr>
			<td align="right" width="25%" nowrap>
                <label class="Validform_label"> <span style="color:red">*</span>权限角色:</label>
            </td>
			<td class="value" width="85%">
			 <c:if test="${roleName!=null }"> ${fn:replace(roleName,",","")} </c:if>
            <c:if test="${roleName==null }">
                <select id="roleName" name="roleName"  nullmsg="请选择权限角色" datatype="*"></select>
                <span class="Validform_checktip"></span>
           </c:if>
            </td>
		</tr>
		<tr>
			<td align="right" width="10%" nowrap><label class="Validform_label"> <span style="color:red">*</span><t:mutiLang langKey="common.real.name"/>: </label></td>
			<td class="value" width="10%">
                <input id="realName" class="inputxt" name="realName" value="${user.realName }" autocomplete="off" datatype="*2-18"/>
                <span class="Validform_checktip"><t:mutiLang langKey="fill.realname"/></span>
            </td>
		</tr>
		<c:if test="${user.id==null }">
		<tr>
			<td align="right" width="25%" nowrap>
                <label class="Validform_label"><span style="color:red">*</span> <t:mutiLang langKey="common.password"/>:  </label>
            </td>
			<td class="value" width="85%">
                    <input id="password" class="inputxt" name="password" value="" autocomplete="off" datatype="/(^[0-9a-zA-Z-]{6,18}$)/" />
                    <!-- <input id="departEntryPwd" name="departEntryPwd" type="text" style="width: 150px" class="inputxt"  datatype="*6-18" /> -->
                    <span class="Validform_checktip"> <t:mutiLang langKey="请输入6到18位大、小写字母或数字"/></span>
            </td>
		</tr>
		</c:if>
		<tr>
			<td align="right" nowrap><label class="Validform_label">  <t:mutiLang langKey="common.phone"/>: </label></td>
			<td class="value">
                <input class="inputxt" name="mobilePhone" value="${user.mobilePhone}" autocomplete="off" datatype="m" errormsg="手机号码不正确" ignore="ignore"/>
                <span class="Validform_checktip"></span>
            </td>
		</tr>
		<tr>
			<td align="right"><label class="Validform_label"> <span style="color:red">*</span><t:mutiLang langKey="common.common.mail"/>: </label></td>
			<td class="value">
				<!-- validType="t_s_user,email,id" -->
                <input class="inputxt" name="email" value="${user.email}" autocomplete="off"  datatype="e" errormsg="邮箱格式不正确" />
                <span class="Validform_checktip"></span>
            </td>
		</tr>

	</table>
</t:formvalid>
</body>