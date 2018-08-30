<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
   <link rel="stylesheet"  href="plug-in/jquery-file-upload/css/jquery.fileupload.css"/>
  <script type="text/javascript" src="plug-in/jquery-file-upload/js/vendor/jquery.ui.widget.js"></script>
  <script type="text/javascript" src="plug-in/jquery-file-upload/js/jquery.iframe-transport.js"></script>
 <script type="text/javascript" src="plug-in/jquery-file-upload/js/jquery.fileupload.js"></script>
 <script>
 $(document).ready(function(){
	//初始化表头颜色
	 /* initTableHeaderColor(); */
	 initCusToolbarStyle();
	    $("#attImportBtn").remove();
		$("#excelImportBtn").prepend("<input type='file' name='attFile' id='attImportBtn' accept='.xlsx' style='position:absolute;width:74px !important;height:32px;opacity:0'/>");
		$("#attImportBtn").fileupload({
			autoUpload: false,//是否自动上传
		    maxNumberOfFiles : 1,
		    dataType: 'text',
		    forceIframeTransport: true,
		    add:function (e, data) { data.submit(); },
		    url : 'attendanceCalendarController.do?AttendanceCalendarImport',
		    start : function(e, data) {
		    },
		    done : function(e, result) {
				var data = JSON.parse(result.result);
		    	if(data.errCode == 0){
		    		layer.alert(data.errMsg);
		        	setTimeout(function(){
		        		location.reload(true);
		        	},2000);
		    	}else if(data.errCode == 1){
		    		searchReset('attendanceCalendarList');
		    		layer.alert(data.errMsg);
		    	}else{
		    		searchReset('attendanceCalendarList');
		    		layer.alert(data.errMsg);
		    	}
		    	/* initTableHeaderColor(); */
				initCusToolbarStyle();
		    },
		    fail : function(e, result) {
		    	layer.alert('导入有错');
		    	/* initTableHeaderColor(); */
				initCusToolbarStyle();
		    }
		});
 });

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
					reloadTable();
					layer.closeAll('dialog');
					/* data = JSON.parse(data);
					layer.alert(data.msg,function(){
						reloadTable();
						layer.closeAll('dialog');
					}); */
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
  <t:datagrid name="attendanceCalendarList" title="工作日"  pageSize="20"
  actionUrl="attendanceCalendarController.do?datagrid" sortOrder="desc" sortName="year"
   idField="id" fit="true">
 <!--   onDblClick="update('编辑','attendanceCalendarController.do?addorupdate','attendanceCalendarList','100%','100%')" -->
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="年度" field="year" width="60" align="center"></t:dgCol>
   <t:dgCol title="1月" field="month1" width="60"  align="center"></t:dgCol>
   <t:dgCol title="2月" field="month2" width="60"  align="center"></t:dgCol>
   <t:dgCol title="3月" field="month3" width="60"  align="center"></t:dgCol>
   <t:dgCol title="4月" field="month4" width="60"  align="center"></t:dgCol>
   <t:dgCol title="5月" field="month5" width="60"  align="center"></t:dgCol>
   <t:dgCol title="6月" field="month6" width="60"  align="center"></t:dgCol>
   <t:dgCol title="7月" field="month7" width="60"  align="center"></t:dgCol>
   <t:dgCol title="8月" field="month8" width="60"  align="center"></t:dgCol>
   <t:dgCol title="9月" field="month9" width="60"  align="center"></t:dgCol>
   <t:dgCol title="10月" field="month10" width="60" align="center"></t:dgCol>
   <t:dgCol title="11月" field="month11" width="60" align="center"></t:dgCol>
   <t:dgCol title="12月" field="month12" width="60" align="center"></t:dgCol>
   <%-- <t:dgCol title="创建人" field="createdBy"   width="120"></t:dgCol>
   <t:dgCol title="创建日期" field="createdDate" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="最后修改人" field="lastModifiedBy"   width="120"></t:dgCol>
   <t:dgCol title="最后修改时间" field="lastModifiedDate" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol> --%>
   <t:dgCol title="操作" field="opt" align="center" width="60"></t:dgCol>
   <t:dgDelOpt title="删除" url="attendanceCalendarController.do?del&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   <t:dgToolBar title="导入" icon="icon-putout" url="#" funname="" id="excelImportBtn"></t:dgToolBar>
   <t:dgToolBar title="新增" icon="icon-add" width="100%" height="100%" url="attendanceCalendarController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="attendanceCalendarController.do?addorupdate" funname="update" height="100%"></t:dgToolBar>

  </t:datagrid>
  </div>
 </div>
