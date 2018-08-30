<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<head><meta name="format-detection" content="telephone=no"></head>
 <link rel="stylesheet"  href="plug-in/jquery-file-upload/css/jquery.fileupload.css"/>
  <script type="text/javascript" src="plug-in/jquery-file-upload/js/vendor/jquery.ui.widget.js"></script>
  <script type="text/javascript" src="plug-in/jquery-file-upload/js/jquery.iframe-transport.js"></script>
  <script type="text/javascript" src="plug-in/jquery-file-upload/js/jquery.fileupload.js"></script>
<script>
//格式化数字
function formatTwoDecimal(value,row,index){
	if(""!=$.trim(value)){
		if(value.indexOf(".")!=-1&&value.substring(value.indexOf(".")+1).length==1){
			return value+"0";
		}
	}
	return value;
}
/**处理数字千分符*/
function comma(num) {
	 var source = String(num).split(".");//按小数点分成2部分
	 source[0] = source[0].replace(new RegExp('(\\d)(?=(\\d{3})+$)','ig'),"$1,");//只将整数部分进行都好分割
	 return source.join(".");//再将小数部分合并进来
	}
/**处理数字千分符*/
function salaryFormat(value,row,index){
	 if(!value) return "";
	var str_n = comma(value);
	var strs = str_n.split('.');
	//var SUM = Number(strs[1]);
    if(strs[1].length>=2){

    }if(strs[1].length==1){
    	strs[1]=strs[1]+"0";
    }
    str_n=strs[0];
  return str_n;
}
/**处理数字百分数*/
function percentFormat(value,row,index) {
	  if(!value) return "";
	  var num = parseFloat(value);
	  /* num = num.toFixed(2); */
	  return num+"%";
}

	$(document).ready(function(){
		//初始化表头颜色
		/*  initTableHeaderColor(); */
		initCusToolbarStyle();
		$("#sixGoldScaleImportBtn").remove();
		$("#excelImportBtn").prepend("<input type='file' name='sgFile' id='sixGoldScaleImportBtn' accept='.xlsx' style='position:absolute;width:74px !important;height:32px;opacity:0'/>");
		$("#sixGoldScaleImportBtn").fileupload({
			autoUpload: false,//是否自动上传
			maxNumberOfFiles : 1,
			dataType: 'text',
			forceIframeTransport: true,
			add:function (e, data) { data.submit(); },
			url : 'sixGoldScaleController.do?sixGoldScaleImport',
			start : function(e, data) {
			},
			done : function(e, result) {
				var data = JSON.parse(result.result);
				if(data.errCode == 0){
					layer.alert(data.errMsg,function(){
						layer.closeAll('dialog');
						location.reload(true);
					});
				} else {
					layer.alert(data.errMsg);
				}
			},
			fail : function(e, result) {
				layer.alert("导入控件出现异常");
			}
		});
	});

	 //初始化表头颜色
/* function initTableHeaderColor(){
	var ths = $(".datagrid-header-row");
	for(var i = 1;i < ths.length;i++){
	    switch(i){
	        case 1:
	        	for(var j = 0;j<$(ths).eq(2).find("td").length;j++){
                  $(ths).eq(1).find("td").eq(j).css("background","#C6E0B4");
                  $(ths).eq(2).find("td").eq(j).css("background","#C6E0B4");
                  $(ths).eq(1).find("div").eq(j).css("text-align","center");
                  $(ths).eq(2).find("div").eq(j).css("text-align","center");
              }
	        	for(var j = 0;j<$(ths).eq(2).find("td").length;j++){
	        		$(ths).eq(0).find("td").eq(j).css("border-color","#FFFFFF");
	        		$(ths).eq(1).find("td").eq(j).css("border-color","#FFFFFF");
                  $(ths).eq(2).find("td").eq(j).css("border-color","#FFFFFF");
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
</script>
<t:datagrid name="sixGoldScaleList" title="六金比例" actionUrl="sixGoldScaleController.do?datagrid" pageSize="20" idField="id" fit="true" fitColumns="false">
   <t:dgCol title="编号" field="id" hidden="true" rowspan ="2"></t:dgCol>
   <t:dgCol title="城市" field="sixGoldPlace" rowspan ="2"  align="center" extendParams="resizable:false" ></t:dgCol>
   <t:dgCol title="养老保险" colspan="4"></t:dgCol>
   <t:dgCol title="医疗保险" colspan="4" ></t:dgCol>
   <t:dgCol title="失业保险" colspan="4"></t:dgCol>
   <t:dgCol title="工伤" colspan="3"></t:dgCol>
   <t:dgCol title="生育" colspan="3"></t:dgCol>
   <t:dgCol title="住房公积金" colspan="4" newColumn="true"></t:dgCol>
   <t:dgCol title="企业" field="companyEndowment"  formatterjs="percentFormat" align="center" extendParams="resizable:false" ></t:dgCol>
   <t:dgCol title="个人" field="personalEndowment"    formatterjs="percentFormat" align="center"  extendParams="resizable:false"  ></t:dgCol>
   <t:dgCol title="最高" field="endowmentMax"   formatterjs="salaryFormat"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
   <t:dgCol title="最低" field="endowmentMin"   formatterjs="salaryFormat" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
   <t:dgCol title="企业" field="companyMedical"   formatterjs="percentFormat"  align="center"  extendParams="resizable:false"  ></t:dgCol>
   <t:dgCol title="个人" field="personalMedical"   formatterjs="percentFormat" align="center"  extendParams="resizable:false"  ></t:dgCol>
   <t:dgCol title="最高" field="medicalMax"   formatterjs="salaryFormat"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
   <t:dgCol title="最低" field="medicalMin"  formatterjs="salaryFormat"   extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  width="59"></t:dgCol>
   <t:dgCol title="企业" field="companyUnemployment"  formatterjs="percentFormat" align="center"  extendParams="resizable:false"  ></t:dgCol>
   <t:dgCol title="个人" field="personalUnemployment"   formatterjs="percentFormat" align="center"  extendParams="resizable:false"  ></t:dgCol>
   <t:dgCol title="最高" field="unemploymentMax"   formatterjs="salaryFormat"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
   <t:dgCol title="最低" field="unemploymentMin"   formatterjs="salaryFormat"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
   <t:dgCol title="企业" field="companyInjury"   formatterjs="percentFormat" align="center" extendParams="resizable:false"  ></t:dgCol>
   <t:dgCol title="最高" field="injuryMax"   formatterjs="salaryFormat" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
   <t:dgCol title="最低" field="injuryMin"   formatterjs="salaryFormat" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
   <t:dgCol title="企业" field="companyMaternity"  formatterjs="percentFormat"  align="center" extendParams="resizable:false"  ></t:dgCol>
   <t:dgCol title="最高" field="maternityMax"   formatterjs="salaryFormat" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
   <t:dgCol title="最低" field="maternityMin"   formatterjs="salaryFormat" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
   <t:dgCol title="企业" field="companyHousingFund"   formatterjs="percentFormat" align="center" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="个人" field="personalHousingFund"   formatterjs="percentFormat" align="center" extendParams="resizable:false"></t:dgCol>
   <t:dgCol title="最高" field="housingFundMax"   formatterjs="salaryFormat" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
	<t:dgCol title="最低" field="housingFundMin"   formatterjs="salaryFormat" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false" width="59"></t:dgCol>
  <%--  <t:dgCol title="操作" field="opt"></t:dgCol> --%>
   <t:dgToolBar title="导入" icon="icon-putout" url="#" funname="" id="excelImportBtn"></t:dgToolBar>
  </t:datagrid>

