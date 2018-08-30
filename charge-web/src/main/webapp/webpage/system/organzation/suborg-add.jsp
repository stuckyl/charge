<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>添加一级部门</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" callback="@Override callbackOrg" action="systemController.do?saveDepart2" beforeSubmit="otherCheck()">
					<input id="id" name="id" type="hidden" />
					<input id="cc" type="hidden" name="TSPDepart.id" value="${pid }">
		<table style="width: 100%;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							部门名称:
						</label>
					</td>
					<td class="value">
					     	 <input id="departname" name="departname" type="text" style="width: 150px" autocomplete="off" class="inputxt" validType="t_s_depart,departname,id" datatype="*2-18"/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">部门名称</label>
					</td>
				</tr>
<!-- 				<tr>
					<td align="right">
						<label class="Validform_label">
							部门描述:
						</label>
					</td>
					<td class="value">
					     	 <textarea id="description" name="description" rows="5" cols="80"></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">部门描述</label>
						</td>
				</tr> -->
<%-- 				<tr>
					<td align="right">
						<label class="Validform_label">
							机构类型:
						</label>
					</td>
					<td class="value">
					     	 <select name="orgType" id="orgType">
					                 <option value="1" <c:if test="${orgType=='1'}">selected="selected"</c:if>>公司</option>
					                 <option value="2" <c:if test="${orgType=='2'}">selected="selected"</c:if>>部门</option>
					                 <option value="3" <c:if test="${orgType=='3'}">selected="selected"</c:if>>岗位</option>
					         </select>
					         <input name="orgType" id="orgType" type="radio" value="2" checked="checked"/> 部门
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">职务名称</label>
						</td>
				</tr> --%>
<!-- 				<tr>
					<td align="right">
						<label class="Validform_label">
							电话:
						</label>
					</td>
					<td class="value">
					     	 <input id="mobile" name="mobile" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">电话</label>
						</td>
				</tr> -->
<!-- 				<tr>
					<td align="right">
						<label class="Validform_label">
							传真:
						</label>
					</td>
					<td class="value">
					     	 <input id="fax" name="fax" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">传真</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							地址:
						</label>
					</td>
					<td class="value">
					     	 <input id="address" name="address" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">地址</label>
						</td>
				</tr> -->
				<tr>
					<td align="right" width="300">
						<label class="Validform_label">
								录入者账号:
						</label>
					</td>
					<td class="value">
						<input id="departEntryUser" name="departEntryUser" type="text" style="width: 150px" autocomplete="off" class="inputxt" validType="t_s_base_user,userName,id"  datatype="*2-18"/>
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">录入者账号</label>
					</td>
				</tr>
				<tr>
						<td align="right">
							<label class="Validform_label">
								录入者密码:
							</label>
						</td>
						<td class="value">
						     	<input id="departEntryPwd" name="departEntryPwd" type="text" style="width: 150px" autocomplete="off" class="inputxt"  datatype="*6-18" />
								<span class="Validform_checktip"></span>
								<label class="Validform_label" style="display: none;">录入者密码</label>
						</td>
				</tr>
				<tr>
						<td align="right" nowrap>
							<label class="Validform_label">
								 录入者手机:
							</label>
						</td>
						<td class="value">
                				<input class="inputxt" name="departEntryMobilePhone" id="departEntryMobilePhone" autocomplete="off" datatype="m" errormsg="手机号码不正确" ignore="ignore"/>
                				<span class="Validform_checktip"></span>
            			</td>
				</tr>
				<tr>
						<td align="right">
							<label class="Validform_label">
								录入者邮箱:
							</label>
						</td>
						<td class="value">
							<!-- validType="t_s_user,email,id" -->
                			<input class="inputxt" name="departEntryEmail" id="departEntryEmail" autocomplete="off"  datatype="e" errormsg="邮箱格式不正确!" />
               				<span class="Validform_checktip"></span>
          				</td>
				</tr>
				<tr>
					<td align="right" width="300">
						<label class="Validform_label">
								申报者账号:
						</label>
					</td>
					<td class="value">
						<input id="departDeclareUser" name="departDeclareUser" type="text" style="width: 150px" validType="t_s_base_user,userName,id" class="inputxt"  datatype="*2-18"/>
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">申报者账号</label>
					</td>
				</tr>
				<tr>
						<td align="right">
							<label class="Validform_label">
								申报者密码:
							</label>
						</td>
						<td class="value">
						     	<input id="departDeclarePwd" name="departDeclarePwd" type="text" style="width: 150px" class="inputxt"  datatype="*6-18" />
								<span class="Validform_checktip"></span>
								<label class="Validform_label" style="display: none;">申报者密码</label>
						</td>
				</tr>
				<tr>
						<td align="right" nowrap>
								<label class="Validform_label">
								  申报者电话:
								</label>
						</td>
						<td class="value">
                				<input class="inputxt" name="departDeclareMobilePhone" id="departDeclareMobilePhone" datatype="m" errormsg="手机号码不正确" ignore="ignore"/>
                				<span class="Validform_checktip"></span>
            			</td>
				</tr>
				<tr>
						<td align="right">
							<label class="Validform_label">
								申报者邮箱:
							</label>
						</td>
						<td class="value">
							<!-- validType="t_s_user,email,id" -->
                			<input class="inputxt" name="departDeclareEmail" id="departDeclareEmail"   datatype="e" errormsg="邮箱格式不正确!" />
               				<span class="Validform_checktip"></span>
          				</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
						</label>
					</td>
					<td class="value">
					     	<button type="button" class="blueButton" style="width:80px;height:30px" onclick="formSubmit();">保存 </button>
						</td>
				</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/system/position/tSCompanyPositionList.js"></script>
<script type="text/javascript">
function formSubmit(){
	$('#btn_sub').click();
}
function otherCheck(){
	//校验部门名是否重复
	var departname = $.trim($("#departname").val())
	var rs = true;
	   $.ajax({
		   url:"systemController.do?checkDepartName&departName="+departname,
		   type:"get",
		   async:false,
		   success:function(data){
			   data = JSON.parse(data);
			   if(data.errCode == -1){
			    	rs = false;
			    	$.messager.alert("提示","该部门在系统中已存在！","info");
			   }
		   },
		   error:function(){}
	   });

	   if($.trim($("#departEntryUser").val())==$.trim($("#departDeclareUser").val())){
		   $.messager.alert("提示","录入者和申报者不可一样！","info");
		   return false;
	   }
	return rs;
}

function callbackOrg(data){
	if(data.success==true){
		parent.layer.alert(data.msg, {
	        shadeClose: false,
	        title: '提示'
	    },function(index){
	    	parent.loadTree();
			location.reload();
	    	parent.layer.close(index);
	    });
	}else{
		parent.layer.alert(data.msg, {
	        shadeClose: false,
	        title: '提示'
	    },function(index){
	    	parent.layer.close(index);
	    });
	}
	//tip(data.msg);
}
</script>