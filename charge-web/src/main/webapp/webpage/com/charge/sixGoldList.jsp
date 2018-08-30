<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<link rel="stylesheet"  href="plug-in/jquery-file-upload/css/jquery.fileupload.css"/>
<script type="text/javascript" src="plug-in/jquery-file-upload/js/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="plug-in/jquery-file-upload/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="plug-in/jquery-file-upload/js/jquery.fileupload.js"></script>
  <t:datagrid name="sixGoldList" title="社保六金" actionUrl="sixGoldController.do?datagrid" idField="id" pageSize="20" fit="true" queryMode="group" >
   <t:dgCol title="编号" field="id" hidden="true" rowspan ="2"></t:dgCol>
   <t:dgCol title="缴纳月数" field="numMonth"  width="60"  extendParams="resizable:false"  rowspan ="2" align="center"></t:dgCol>
   <t:dgCol title="身份证号" field="employeeCode"   extendParams="resizable:false" rowspan ="2"></t:dgCol>
   <t:dgCol title="员工姓名" field="employeeName" query="true" extendParams="resizable:false"  rowspan ="2"  align="center"></t:dgCol>
   <t:dgCol title="养老保险" colspan="2"></t:dgCol>
   <t:dgCol title="医疗保险" colspan="2" ></t:dgCol>
   <t:dgCol title="失业保险" colspan="2"></t:dgCol>
   <t:dgCol title="工伤(企业)" field="companyInjury" width="60" rowspan ="2"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="生育(企业)" field="companyMaternity" width="60" rowspan ="2"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="住房公积金" colspan="2"></t:dgCol>
   <t:dgCol title="企业合计" field="companySum" width="60" rowspan ="2"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="个人合计" field="personalSum" width="60"  rowspan ="2" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat" newColumn="true"></t:dgCol>
   <t:dgCol title="企业" field="companyEndowment" width="60"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="个人" field="personalEndowment" width="60"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="企业" field="companyMedical"  width="60" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="个人" field="personalMedical" width="60"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="企业" field="companyUnemployment" width="60"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="个人" field="personalUnemployment" width="60"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="企业" field="companyHousingFund" width="60"  extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
   <t:dgCol title="个人" field="personalHousingFund"  width="60" extendParams="styler: function(value,row,index){return 'text-align:right;'},resizable:false"  formatterjs="formatTwoDecimal,salaryFormat"></t:dgCol>
<%--    <t:dgCol title="创建人" field="createdBy"   width="120"></t:dgCol>
   <t:dgCol title="创建日期" field="createdDate" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="最后修改人" field="lastModifiedBy"   width="120"></t:dgCol> --%>
   <%-- <t:dgCol title="最后修改时间" field="lastModifiedDate" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="sixGoldController.do?del&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/> --%>
	<t:dgToolBar title="导入" icon="icon-putout" url="#" funname=""   id="excelImportBtn"></t:dgToolBar>
   <%-- <t:dgToolBar title="新增" icon="icon-add" width="100%" height="100%" url="sixGoldController.do?addorupdate" funname="add"></t:dgToolBar>
   onDblClick="update('编辑','sixGoldController.do?addorupdate','sixGoldList',null,null)"--%>
   </t:datagrid>
  <script>
  var newYear="";
  var newMonth="";
  //改变月份以后表头付色
	function color(){
	    initCusToolbarStyle();
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
	    str_n=strs[0]+"."+strs[1];
	  return str_n;
	}
 $(function(){
	 initCusToolbarStyle();
 })


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
 $(document).ready(function(){
		initCusToolbarStyle();
		if(document.getElementById('declareDate_end')){
		} else {
				$(".datagrid-toolbar").eq(1).prepend("<div style='width: 148px;float: left;margin-top: 1px;border: 1px solid #cccccc;height: 30px;background-color: #f4f4f4;'>"+
				        "<span style='padding: 0px 4px;height: 27px;line-height: 27px;font-size: 16px;color: #333;'>月度:</span>"+
				         "<input id='declareDate_end' style='width: 100px;'  name='salaryDateChoose'  class='Wdate'  type='text'></div>");
		}

		var db= $('#declareDate_end');
	    db.datebox({
	    	// 显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
	        onShowPanel: function () {
	        	// 触发click事件弹出月份层
	            span.trigger('click');
	            // fix 1.3.x不选择日期点击其他地方隐藏在弹出日期框显示日期面板
	            if (p.find('div.calendar-menu').is(':hidden')){
	            	p.find('div.calendar-menu').show();
	            }
	            // 延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
	            if (!tds) setTimeout(function () {
	                tds = p.find('div.calendar-menu-month-inner td');
	                tds.click(function (e) {
	                	 // 禁止冒泡执行easyui给月份绑定的事件
	                    e.stopPropagation();
	                    // 得到年份
	                    var year = /\d{4}/.exec(span.html())[0];
	                    // 月份，这里不需要+1
	                    var month = parseInt($(this).attr('abbr'), 10);
	                    // 设置隐藏日期对象的值
	                    db.datebox('hidePanel').datebox('setValue', year + '-' + (month < 10 ? ('0' + month) : (month + '')));
	                    /* sixGoldListsearch(); */
	                    //新增 自动查询
	                    /* var enterDate = new Date(year,month,1);
	                    var queryParams=$('#sixGoldList').datagrid('options').queryParams;
						queryParams['enterDate']=enterDate; */

						var salaryDateChoose = $("input[name='salaryDateChoose']").val();
	                    $('#sixGoldList').datagrid({
	            			url:'sixGoldController.do?sixGoldFindbyMonth&month='+salaryDateChoose,
	            			pageNumber:1
	            		});
	                    newYear = year;
	                	newMonth = month;
	                });
	            }, 0);
	            // 解绑年份输入框中事件
	            yearIpt.unbind();
	        },
	        parser: function (s) {
	            if (!s) return new Date();
	            var arr = s.split('-');
	            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
	        },
	        formatter: function (d) {
	        	 var month = d.getMonth() + 1;
	             return d.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + ''));
	        }
	    });
	    $('.combo.datebox').attr('style','margin-bottom:4px;');
	    $('.combo-text.validatebox-text').attr('readonly','readonly');
		// 日期选择对象
	    var p = db.datebox('panel');
	    // 日期选择对象中月份
	    var tds = false;
	    // 判断是否是 1.3.x版本
	    var aToday = p.find('a.datebox-current');
	    // 年份输入框
	    var yearIpt = p.find('input.calendar-menu-year');
	    // 显示月份层的触发控件                            1.3.x版本                           1.4.x版本
	    var span = aToday.length ? p.find('div.calendar-title span') : p.find('span.calendar-text');
	    // 1.3.x版本，取消Today按钮的click事件，重新绑定新事件设置日期框为今天，防止弹出日期选择面板
	    if (aToday.length) {
	        aToday.unbind('click').click(function () {
	            var now = new Date();
	            var month = now.getMonth() + 1;
	            db.datebox('hidePanel').datebox('setValue', now.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + '')));
	        });
	    }


		$("#excelImportBtn").unbind("click");
		// 设置 默认当前月份
		var now = new Date();
        var month = now.getMonth() + 1;
        db.datebox('hidePanel').datebox('setValue', now.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + '')));
		var salaryDateChoose = null;
	   	$("#excelImportBtn").click(function() {
	   	 	/* 获取日期 */
	   	 	if(""!=$("input[name='salaryDateChoose']").val()){
	   	 		salaryDateChoose = $("input[name='salaryDateChoose']").val();
	   	 	}
	   	 	if(salaryDateChoose==null || salaryDateChoose==""){
	   	 		layer.alert('请选择月度');
	   			return false;
	   		}
   	 	});

		$("#sixGoldImportBtn").remove();
		$("#excelImportBtn").prepend("<input type='file' name='sgFile' id='sixGoldImportBtn' accept='.xlsx' style='position:absolute;width:74px !important;height:32px;opacity:0'/>");
		$("#sixGoldImportBtn").fileupload({
			autoUpload: false,//是否自动上传
	        maxNumberOfFiles : 1,
	        dataType: 'text',
	        forceIframeTransport: true,
		    start : function(e, data) {
		    	// $.Showmsg("导入中......");
		    },
			add: function (e, data) {
				$(this).fileupload('option', 'url', 'sixGoldController.do?sixGoldImport&month=' + salaryDateChoose);
				data.submit();
			},
			done : function(e, result) {
				var data = JSON.parse(result.result);
	         	if(data.errCode == 0){
	         		layer.alert(data.errMsg,function(){
	                    $('#sixGoldList').datagrid({
	            			url:'sixGoldController.do?sixGoldFindbyMonth&month='+salaryDateChoose,
	            			pageNumber:1
	            		});
	                    /* initTableHeaderColor(); */
	               	 	initCusToolbarStyle();
	               	    layer.closeAll('dialog');
					});

	         	} else {
	         		layer.alert(data.errMsg);
	         	}
	         },
	         fail : function(e, result) {
	        	 layer.alert(data.errMsg);
	         }
	     });
		var now = new Date();
	    var month = now.getMonth() + 1;
	    db.datebox('hidePanel').datebox('setValue', now.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + '')));

	    $.ajax({url:"sixGoldController.do?clearMonth",
			async:true,
			success: function (){
			},
			error: function(){
			}
		});
	    initCusToolbarStyle();
	});
 $(".l-btn").click(function(){
		var db= $('#declareDate_end');
		if(newYear=="") {
			var now = new Date();
		    var month = now.getMonth() + 1;
		    db.datebox('hidePanel').datebox('setValue', now.getFullYear() + '-' + (month < 10 ? ('0' + month) : (month + '')));
		}else {
			db.datebox('hidePanel').datebox('setValue', newYear + '-' + (newMonth < 10 ? ('0' + newMonth) : (newMonth + '')));
		}
	});
 </script>