package com.charge.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.charge.service.SixGoldService;
import com.sun.star.drawing.framework.TabBarButton;

import springfox.documentation.builders.RequestHandlerSelectors;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.parser.EnterpriseUnixFTPEntryParser;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.TotalDigitsDocument;
import org.aspectj.apache.bcel.generic.InstructionTargeter;
import org.codehaus.groovy.runtime.DateGroovyMethods;
import org.hibernate.validator.util.LazyValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.omg.CORBA.ObjectHolder;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.Else;
import com.charge.entity.CorporateInfoEntity;
import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.repository.EmployeeInfoRepository;
import com.charge.repository.CommonRepository;
import com.charge.repository.SixGoldScaleRepository;
import com.charge.service.EmailConfigService;
import com.charge.service.EmployeeInfoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Title: Controller
 * @Description: 员工信息表
 * @author wenst
 * @date 2018-03-19 16:44:26
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/employeeInfoController")
public class EmployeeInfoController extends BaseController {
	private final static Logger log = Logger.getLogger(EmployeeInfoController.class);

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EmployeeInfoController.class);
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EmployeeInfoService employeeInfoService;
	@Autowired
	private EmployeeInfoRepository employeeInfoRepo;
	@Autowired
	private SixGoldService sixGoldService;
	@Autowired
	private CommonRepository commonRepository;
	@Autowired
	private EmailConfigService emailConfigService;

	/**
	 * 访客员工列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "employeeInfoAccessList")
	public ModelAndView employeeInfoAccessList(HttpServletRequest request) {
		List<TSDepart> myDeparts = commonRepository.findMyDeptInfo();
		String depts = "";
		for(int i=0;i<myDeparts.size();i++) {
			TSDepart obj = myDeparts.get(i);
			depts+=obj.getDepartname()+"_"+obj.getId();
			if(i<myDeparts.size()-1) {
				depts+=",";
			}
		}
		request.setAttribute("depts",depts);
		return new ModelAndView("com/charge/employeeInfoAccesslList");
	}

	/**
	 * easyui AJAX请求数据
	 * 访客员工信息action
	 * j-5-11
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "accessDatagrid")
	public void accessDatagrid(EmployeeInfoEntity employeeInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String orgIds = request.getParameter("orgIds");
		List<String> orgIdList = extractIdListByComma(orgIds);
		if("inputName".equals(dataGrid.getSort())){
			dataGrid.setSort("inputerId");
		}
		if(employeeInfo!=null){
        	if(employeeInfo.getName()!=null)
        		employeeInfo.setName("*"+employeeInfo.getName()+"*");
        	if(employeeInfo.getCode()!=null)
        		employeeInfo.setCode("*"+employeeInfo.getCode()+"*");
        }
//      employeeInfoService.setDataGridAll(employeeInfo, request.getParameterMap(), dataGrid,new Object[]{2,3,4});
		employeeInfoService.setDataGridAll(employeeInfo, request.getParameterMap(), dataGrid,null);
		Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
		List<EmployeeInfoEntity> employeeInfos = dataGrid.getResults();
		for(EmployeeInfoEntity temp: employeeInfos){
			//此为针对原来的行数据，拓展的新字段
			Map m = new HashMap();
			List<TSUser> inputer =systemService.findHql("from TSUser t where t.userName=?", new Object[]{temp.getInputerId()});
			if(null == inputer || inputer.size() ==0){
				m.put("inputName", "无");
			}else{
				m.put("inputName", inputer.get(0).getRealName());
			}
			List<TSUser> reporter =systemService.findHql("from TSUser t where t.userName=?", new Object[]{temp.getReporterId()});
			if(null == reporter || reporter.size() ==0){
				m.put("reportName", "无");
			}else{
				m.put("reportName", reporter.get(0).getRealName());
			}
			extMap.put(temp.getId().toString(), m);
		}
		TagUtil.datagrid(response, dataGrid,extMap);
		//   TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 员工信息录入列表 页面跳转
	 *
	 * @return
	 */
	/*@RequestMapping(params = "employeeInfoEntryList")
	public ModelAndView employeeInfoEntryList(HttpServletRequest request) {
		return new ModelAndView("com/charge/employeeInfoEntryList");
	}
*/

	/**
	 * 员工信息录入列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "employeeInfoDeclareList")
	public ModelAndView employeeInfoDeclareList(HttpServletRequest request) {
		Integer useCode = employeeInfoService.getUserRoleCold();
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSDepart depart = commonService.getEntity(TSDepart.class, user.getDepartid());
        Integer lv = commonRepository.getDepartGread(depart);
        if(employeeInfoService.getUserRoleCold()==1){
        	lv = lv*2 +2;}else {
        		lv = lv*2;}
        request.setAttribute("actionCode",lv);
		request.setAttribute("useCode",useCode);
		return new ModelAndView("com/charge/employeeInfoDeclareList");
	}



	/**
	 * 员工信息审核列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "employeeInfoApprovalList")
	public ModelAndView employeeInfoApprovalList(HttpServletRequest request) {
		List<TSDepart> myDeparts = commonRepository.findMyDeptInfo();
		String depts = "";
		for(int i=0;i<myDeparts.size();i++) {
			TSDepart obj = myDeparts.get(i);
			depts+=obj.getDepartname()+"_"+obj.getId();
			if(i<myDeparts.size()-1) {
				depts+=",";
			}
		}
		request.setAttribute("depts",depts);
		return new ModelAndView("com/charge/employeeInfoApprovalList");
	}

	/**
	 * 员工信息申报批列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "employeeInfoCheckList")
	public ModelAndView employeeInfoChecklList(HttpServletRequest request) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSDepart depart = commonService.getEntity(TSDepart.class, user.getDepartid());
        Integer lv = commonRepository.getDepartGread(depart);lv = lv*2;
        request.setAttribute("actionCode",lv);
		List<TSDepart> myDeparts = commonRepository.findMyDeptInfo();
		String depts = "";
		for(int i=0;i<myDeparts.size();i++) {
			TSDepart obj = myDeparts.get(i);
			depts+=obj.getDepartname()+"_"+obj.getId();
			if(i<myDeparts.size()-1) {
				depts+=",";
			}
		}
		request.setAttribute("depts",depts);
		return new ModelAndView("com/charge/employeeInfoCheckList");
	}

	/**
	 * 员工信息审批列表 页面跳转
	 * j 5-14
	 * @return
	 */
	@RequestMapping(params = "employeeInfoControl")
	public ModelAndView employeeInfoControl(HttpServletRequest request) {
		List<TSDepart> myDeparts = commonRepository.findMyDeptInfo();
		String depts = "";
		for(int i=0;i<myDeparts.size();i++) {
			TSDepart obj = myDeparts.get(i);
			depts+=obj.getDepartname()+"_"+obj.getId();
			if(i<myDeparts.size()-1) {
				depts+=",";
			}
		}
		request.setAttribute("depts",depts);
		return new ModelAndView("com/charge/employeeInfoControl");
	}

	/**
	 * 员工信息部门修改 页面跳转
	 * j 6-17
	 * @return
	 */
	@RequestMapping(params = "editDepart")
	public String editDepart(EmployeeInfoEntity employeeInfo,HttpServletRequest request) {
		employeeInfo = systemService.getEntity(EmployeeInfoEntity.class, employeeInfo.getId());
		request.setAttribute("employeeInfo", employeeInfo);
		return "com/charge/depart-edit";
	}

	/**
	 * Ajax获取部门总监
	 * @param depart
	 * @return
	 */
	@RequestMapping(params = "getDepartChief")
 	@ResponseBody
	public List<String> getDepartChief(String depart) {
		List<String> inputerName = new ArrayList<>();
 		TSUser user = commonRepository.getDepartChief(depart);
		if (user!=null){
			inputerName.add(user.getUserName());
			inputerName.add(user.getRealName());
		}
 		return inputerName;
	}

	/**
	 * easyui AJAX请求数据
	 * 录入action
	 * j-5-11
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "declareDatagrid")
	public void declareDatagrid(EmployeeInfoEntity employeeInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		TSUser myUser = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSDepart depart = commonService.getEntity(TSDepart.class, myUser.getDepartid());
        Integer code  =employeeInfoService.getUserRoleCold();
        Integer lv = commonRepository.getDepartGread(depart);
        if(employeeInfo!=null){
        	if(employeeInfo.getName()!=null)
        		employeeInfo.setName("*"+employeeInfo.getName()+"*");
        	if(employeeInfo.getCode()!=null)
        		employeeInfo.setCode("*"+employeeInfo.getCode()+"*");
        }
        if(employeeInfoService.getUserRoleCold()==1){
            lv = lv*2+2;
        }else {
			lv = lv*2;
		}
        if(null==employeeInfo.getDeclareStatus()||employeeInfo.getDeclareStatus() <= 2||employeeInfo.getDeclareStatus() == 4||employeeInfo.getDeclareStatus() == lv||employeeInfo.getDeclareStatus()==(lv-1)){
	        if (code.equals(1)) {
	            employeeInfoService.setDataGridFlag(employeeInfo, request.getParameterMap(), dataGrid,0,null,lv);
			}else if(code.equals(2)){
				employeeInfoService.setDataGriddepart(employeeInfo, request.getParameterMap(), dataGrid,1,null,lv);
			}
        }else{
        	Integer DeclareStatus = employeeInfo.getDeclareStatus();
        	employeeInfo.setDeclareStatus(null);
        	if (code.equals(1)) {
	            employeeInfoService.setDataGridFlag(employeeInfo, request.getParameterMap(), dataGrid,0,DeclareStatus,lv);
			}else if(code.equals(2)){
				employeeInfoService.setDataGriddepart(employeeInfo, request.getParameterMap(), dataGrid,1,null,lv);
			}
        }
        List<EmployeeInfoEntity> employeeInfos = dataGrid.getResults();
        for(EmployeeInfoEntity temp: employeeInfos){
        	 if (temp.getDeclareStatus()>2) {
        		 if(temp.getDeclareStatus()<lv-1){
            		 temp.setDeclareStatus(lv-2);
        		 }else if(temp.getDeclareStatus()>lv){
        			 temp.setDeclareStatus(lv+1);
        		 }
			}
        }
        /*        if(!StringUtil.isNotEmpty(dataGrid.getSort())||"declareStatus".equals(dataGrid.getSort())){
	        Collections.sort(employeeInfos, new Comparator<EmployeeInfoEntity>() {
				@Override
				public int compare(EmployeeInfoEntity o1, EmployeeInfoEntity o2) {
					// TODO Auto-generated method stub
					if (o1.getDeclareStatus().intValue() > o2.getDeclareStatus().intValue()) {
						return 1;
				    }
				    if (o1.getDeclareStatus().intValue() == o2.getDeclareStatus().intValue()) {
				        return 0;
				    }
				return -1;
				}
	        });
        }*/
     dataGrid.setResults(employeeInfos);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * easyui AJAX请求数据
	 * 统计
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	public void statistics(EmployeeInfoEntity employeeInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {

		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * easyui AJAX请求数据
	 * 审批action
	 * j-5-11
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "approvalDatagrid")
	public void approvalDatagrid(EmployeeInfoEntity employeeInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String orgIds = request.getParameter("orgIds");
        List<String> orgIdList = extractIdListByComma(orgIds);
        if("inputName".equals(dataGrid.getSort())){
			dataGrid.setSort("inputerId");
		}
        if(employeeInfo!=null){
        	if(employeeInfo.getName()!=null)
        		employeeInfo.setName("*"+employeeInfo.getName()+"*");
        	if(employeeInfo.getCode()!=null)
        		employeeInfo.setCode("*"+employeeInfo.getCode()+"*");
        }
        if(null != employeeInfo.getDeclareStatus()&&5==employeeInfo.getDeclareStatus()){
        	employeeInfo.setDeclareStatus(null);
        	employeeInfoService.setDataGridAll(employeeInfo, request.getParameterMap(), dataGrid,5);
        }else{
            employeeInfoService.setDataGridAll(employeeInfo, request.getParameterMap(), dataGrid,null);
        }
        Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
        List<EmployeeInfoEntity> employeeInfos = dataGrid.getResults();
        for(EmployeeInfoEntity temp: employeeInfos){
	        //此为针对原来的行数据，拓展的新字段
	        Map m = new HashMap();
	        List<TSUser> inputer =systemService.findHql("from TSUser t where t.userName=?", new Object[]{temp.getInputerId()});
	        if(null == inputer || inputer.size() ==0){
	        	m.put("inputName", "无");
	        }else{
	        	m.put("inputName", inputer.get(0).getRealName());
	        }
	        List<TSUser> reporter =systemService.findHql("from TSUser t where t.userName=?", new Object[]{temp.getReporterId()});
	        if(null == reporter || reporter.size() ==0){
	        	m.put("reportName", "无");
	        }else{
	        	m.put("reportName", reporter.get(0).getRealName());
	        }
	        extMap.put(temp.getId().toString(), m);
        }
        for(EmployeeInfoEntity temp: employeeInfos){
        	 if (temp.getDeclareStatus()!=2&&temp.getDeclareStatus()!=4) {
        		 if(temp.getDeclareStatus()>4){
            		 temp.setDeclareStatus(5);
			}
        }
        }
        dataGrid.setResults(employeeInfos);
	 TagUtil.datagrid(response, dataGrid,extMap);
     //   TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * easyui AJAX请求数据
	 *申报中
	 *j 5-11
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "checkDatagrid")
	public void checkDatagrid(EmployeeInfoEntity employeeInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		if("inputName".equals(dataGrid.getSort())){
			dataGrid.setSort("inputerId");
		}
		if(employeeInfo!=null){
        	if(employeeInfo.getName()!=null){
        		employeeInfo.setName("*"+employeeInfo.getName()+"*");
        	}
        	if(employeeInfo.getCode()!=null){
        		employeeInfo.setCode("*"+employeeInfo.getCode()+"*");
        	}
        }
		TSUser myUser = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSDepart depart = commonService.getEntity(TSDepart.class, myUser.getDepartid());
	    Integer lv = commonRepository.getDepartGread(depart)*2;
		TSUser users = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(null==employeeInfo.getDeclareStatus()||employeeInfo.getDeclareStatus() == 2||employeeInfo.getDeclareStatus() == 4||employeeInfo.getDeclareStatus() == 1||employeeInfo.getDeclareStatus() == lv||employeeInfo.getDeclareStatus()==(lv-1)){
			employeeInfoService.setDataGriddepart(employeeInfo, request.getParameterMap(), dataGrid,0,null,lv);
		}else{
			Integer DeclareStatus = employeeInfo.getDeclareStatus();
        	employeeInfo.setDeclareStatus(null);
        	employeeInfoService.setDataGriddepart(employeeInfo, request.getParameterMap(), dataGrid,0,DeclareStatus,lv);
		}
        Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
        List<EmployeeInfoEntity> employeeInfos = dataGrid.getResults();
        for(EmployeeInfoEntity temp: employeeInfos){
	        //此为针对原来的行数据，拓展的新字段
	        Map m = new HashMap();
	        List<TSUser> user =systemService.findHql("from TSUser t where t.userName=?", new Object[]{temp.getInputerId()});
	        if(null == user || user.size() ==0){
	        	m.put("inputName", "无");
	        }else{
	        	m.put("inputName", user.get(0).getRealName());
	        }
	        extMap.put(temp.getId().toString(), m);
        }
        for(EmployeeInfoEntity temp: employeeInfos){
       	 if (temp.getDeclareStatus()>2) {
	       		if(temp.getDeclareStatus()<lv-1){
	           		temp.setDeclareStatus(lv-2);
	       		}else if(temp.getDeclareStatus()>lv){
	       			 temp.setDeclareStatus(lv+1);
	       		}
			}
       }
       dataGrid.setResults(employeeInfos);
	 TagUtil.datagrid(response, dataGrid,extMap);
	//	TagUtil.datagrid(response, dataGrid);
	}


	/**
	 * easyui AJAX请求数据
	 *获取同部门下客户经理组（inputer)
	 *j 6-4
	 * @return
	 * @param depart
	 */
 	@RequestMapping(params = "getInputerName")
 	@ResponseBody
 	public List<String> getInputerName(){
 		List<String> inputerName = new ArrayList<>();
 		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
 		List<TSUser> inputer = systemService.findByProperty(TSUser.class, "departid", user.getDepartid());
 		for(TSUser name : inputer){
 			Integer code = employeeInfoService.getNameRoleCold(name);
 			if (name.getStatus()==1&&(code ==1 || code ==2||code ==5)){
 				inputerName.add(name.getUserName());
 				inputerName.add(name.getRealName());
 			}
 		}
 		return inputerName;
 	}

 	/**
	 * easyui AJAX请求数据
	 *获取同部门下客户经理组（inputer)
	 *j 6-22
	 * @return
	 * @param depart
	 */
 	@RequestMapping(params = "getInputerNameDepart")
 	@ResponseBody
 	public List<String> getInputerNameDepart(String depart){
 		List<String> inputerName = new ArrayList<>();
 		if(depart.length()==0||null == depart){
 			TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
 			depart = user.getDepartid();
 		}
 		List<TSUser> inputer = systemService.findByProperty(TSUser.class, "departid", depart);
 		TSDepart tsDepart = systemService.getEntity(TSDepart.class, depart);
 		if("2c902db662f164650162f1ce7e340045".equals(tsDepart.getTSPDepart().getId())){
 			inputerName.add("1");
 		}else{
 			inputerName.add("0");
 		}
 		for(TSUser name : inputer){
 			Integer code = employeeInfoService.getNameRoleCold(name);
 			if (name.getStatus()==1&&(code ==1 || code ==2||code ==5)){
 				inputerName.add(name.getUserName());
 				inputerName.add(name.getRealName());
 			}
 		}
 		return inputerName;
 	}

 	/**
	 * easyui AJAX请求数据
	 *获取同部门下部门总监组（repoter)
	 *j 6-22
	 * @return
	 * @param depart
	 */
 	@RequestMapping(params = "getRepoterNameDepart")
 	@ResponseBody
 	public List<String> getRepoterNameDepart(String depart){
 		List<String> inputerName = new ArrayList<>();
 		if(depart.length()==0||null == depart){
 			TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
 			depart = user.getDepartid();
 		}
 		List<TSUser> repoter = systemService.findByProperty(TSUser.class, "departid", depart);
 		TSDepart tsDepart = systemService.getEntity(TSDepart.class, depart);
 		if("2c902db662f164650162f1ce7e340045".equals(tsDepart.getTSPDepart().getId())){
 			inputerName.add("1");
 		}else{
 			inputerName.add("0");
 		}
 		for(TSUser name : repoter){
 			Integer code = employeeInfoService.getNameRoleCold(name);
 			if (name.getStatus()==1&&(code ==2||code ==5)){
 				inputerName.add(name.getUserName());
 				inputerName.add(name.getRealName());
 			}
 		}
 		return inputerName;
 	}

	/**
	 * 员工信息部门修改,获取所有部门
	 * j 6-17
	 * @return
	 * @param employeeInfo
	 * @param request
	 */
	@RequestMapping(params = "getAllDepatr")
	@ResponseBody
	public List getAllDepatr(EmployeeInfoEntity employeeInfo,HttpServletRequest request) {
		List<TSDepart> myDeparts = commonRepository.findMyDeptInfo();
		List<String> depatrList = new ArrayList<>();
		for(int i=0;i<myDeparts.size();i++){
			TSDepart obj = myDeparts.get(i);
			depatrList.add(obj.getId());
			depatrList.add(obj.getDepartname());
		}
		return depatrList;
	}

	/**
	 * 入保退保工作
	 * @return
	 */
	@RequestMapping(params="insuranceChange")
	@ResponseBody
	public Map<String,Object> insuranceChange(HttpServletRequest request,@RequestParam("id") String id,@RequestParam("isId") String isId ){
		Map<String, Object> result =null;
		String[] sids = id.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
	}
		result = employeeInfoService.setInsuranceChange(lids,isId);
		return result;
	}
	/**
	 * 批量通过-选中-全部
	 * j 5-8
	 * @return
	 * @param id
	 */
	@RequestMapping(params="batchDeclare")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String,Object> batchDeclare(@RequestParam("id") String id){
		Map<String, Object> result = null;
		if(id==null) { //如果 未选中 则 默认 全选
			List<Integer> lids = null;
			result = employeeInfoService.declareAndPass(lids);
		}else{
		//切割 传来的 员工申报记录  id
		String[] sids = id.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		result = employeeInfoService.declareAndPass(lids);
		}
		return result;
	}

	/**
	 * 选中，成功到职
	 * j 5-8
	 * @return
	 * @param id
	 */

	@SuppressWarnings("null")
	@RequestMapping(params="succed")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String,Object> succed(@RequestParam("Id") String id){
		Map<String, Object> result = null;
		//切割 传来的 员工申报记录  id
		String[] sids = id.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
				lids.add(Integer.parseInt(sid));
		}
		List<EmployeeInfoEntity> employeeInfoList = employeeInfoRepo.findByDeclareStatusId(lids);
		result = employeeInfoService.updateSucced(employeeInfoList);
        //Map<String, Object> result = employeeDeclareService.batchPassVerify(lids);
		return result;
	}

	/**
	 * 新-退回-选中
	 * j 5-8
	 * @return
	 * @param id
	 * @param returnReason
	 */

	@RequestMapping(params="batchReturn",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> BatchReturn(@RequestParam("Id") String id,
			@RequestParam("returnReason") String returnReason){
		Map<String, Object> result = null;
		try {
			returnReason = URLDecoder.decode(returnReason, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			result.put("errMsg", "拒绝失败，请刷新后重试");
			result.put("errCode", -1);
			e.printStackTrace();
		}
		if(id.equals(null)) {
			//如果 未选中 则 默认 全选
			//List<Integer> lids = null;
			result.put("errMsg","未选中数据");
		}else{
		//切割 传来的 员工申报记录  id
		String[] sids = id.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
			}
		result = employeeInfoService.newBatchReturn(lids, returnReason);
		}
		return result;
	}
	/**
	 * 离职
	 * j 8-6
	 * @return
	 * @param returnReason
	 */
	@RequestMapping(params = "leaveSave")
	@ResponseBody
	public AjaxJson leaveSave(EmployeeInfoEntity employeeInfo, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "员工离职成功";
		EmployeeInfoEntity t = commonService.get(EmployeeInfoEntity.class, employeeInfo.getId());
		t.setQuitDate(employeeInfo.getQuitDate());
		t.setQuitReason(employeeInfo.getQuitReason());
		if(null != t.getInsurance()){
			t.setInsurance(2);
		}else{
			t.setInsurance(3);
		}
		if(!new Date().before(employeeInfo.getQuitDate())){
			t.setDeclareStatus(1);
			t.setQuitStatus(1);
		}
		if(StringUtil.isNotEmpty(t.getChangeFlag())&&(t.getChangeFlag()==2||t.getChangeFlag()==3)){
			employeeInfoService.changeInfoEmpty(t);
		}else if(StringUtil.isNotEmpty(t.getChangeDate())){
			if(new Date().before(t.getChangeDate())||employeeInfo.getQuitDate().before(t.getChangeDate())){
				employeeInfoService.changeInfoEmpty(t);
			}
		}
		commonService.saveOrUpdate(t);
		systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		Map<String,String> dictMap = new HashMap<>();
		TSUser inputNow =commonRepository.findUserByRoleName("t_check").get(0);
		dictMap.put("subject", "员工退保通知");
		//"<br>离职日期："+(new java.text.SimpleDateFormat("yyyy-MM-dd")).format(t.getQuitDate())+
		dictMap.put("content","<br>请办理退保手续。<br><br>公司："+commonRepository.placeToCompany(t.getA1Place())+"<br><br>< 收支运营 SaaS >");
		if(t.getInsurance()==2){
			emailConfigService.mailSend(dictMap);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 员工信息表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/charge/employeeInfoList");
	}

	/**
	 * easyui AJAX请求数据
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(EmployeeInfoEntity employeeInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String orgIds = request.getParameter("orgIds");
        List<String> orgIdList = extractIdListByComma(orgIds);
		CriteriaQuery cq = new CriteriaQuery(EmployeeInfoEntity.class, dataGrid);
		if(!orgIdList.isEmpty()){
			cq.eq("department.id", orgIdList.get(0));
		}
		cq.eq("delFlg", 0);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, employeeInfo, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}


	/**
	 * 删除员工信息表
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(EmployeeInfoEntity employeeInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工信息删除失败";
		j.setSuccess(false);
		Integer id = employeeInfo.getId();
		if(null!=id){
			employeeInfo = systemService.getEntity(EmployeeInfoEntity.class, id);
			if(null!=employeeInfo){
				employeeInfo.setDelFlg(1);
				commonService.saveOrUpdate(employeeInfo);
			//	commonService.delete(employeeInfo);
				message = "员工信息删除成功";
			}else{
				logger.info("员ID："+id+"未查找到数据");
			}
			logger.info(message+",id="+id);
		}else{
			logger.info("员工ID为空，删除失败");
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 删除员工信息id多条
	 * j 5-10
	 * @return
	 */
	@RequestMapping(params = "delForId")
	@ResponseBody
	public AjaxJson delForId(@RequestParam("Id") String id) {
		StringBuffer name = new StringBuffer();
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工信息删除失败";
		if(id.equals(null)) {
			//如果 未选中 则 默认 全选
			//List<Integer> lids = null;
			message = "未选中数据";
		}else{
		//切割 传来的 员工申报记录  id
		String[] sids = id.split(",");
		for(String sid : sids) {
			EmployeeInfoEntity employeeInfo = commonService.getEntity(EmployeeInfoEntity.class,Integer.parseInt(sid));
			commonService.deleteEntityById(EmployeeInfoEntity.class,Integer.parseInt(sid));
			message = "员工信息删除成功";
			}
		logger.info(message+",id="+id);
		}
		if(0<name.length()){
			message = "员工"+name.substring(0,name.length()-1)+"信息不可删除";
			j.setSuccess(false);
		}
		/*for(EmployeeInfoEntity employee : employeeInfo) {
				employee.setDelFlg(1);
				commonService.saveOrUpdate(sid);
				message = "员工信息删除成功";
			}*/
		j.setMsg(message);
		return j;
	}

	/*@RequestMapping(params = "passControl")
	public AjaxJson passControl(EmployeeInfoEntity employeeInfo,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();

		message = "员工信息修改失败";
		j.setSuccess(false);
		Integer id = employeeInfo.getId();
		if(null!=id){
			employeeInfo = systemService.getEntity(EmployeeInfoEntity.class, id);
			if(null!=employeeInfo){
				employeeInfo.setDeclareStatus(1);;
				commonService.deleteAllEntitie(employeeInfo);
				message = "员工信息修改成功";
			}else{
				logger.info("员ID："+id+"未查找到数据。");
			}
			logger.info(message+",id="+id);
		}else{
			logger.info("员工ID为空，修改失败。");
		}
		j.setMsg(message);
		return j;
	}*/

	/**
	 * 员工信息更改部门
	 * j 6-18
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveDepart")
	@ResponseBody
	public AjaxJson saveDepart(EmployeeInfoEntity employeeInfo, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = null;
		Integer i = 0;
		EmployeeInfoEntity t = commonService.get(EmployeeInfoEntity.class, employeeInfo.getId());
		if(t.getEditors().equals(employeeInfo.getEditors())){
		if(employeeInfo.getDepartment()!=null){
			try{
				message = "员工信息更新成功";
				if(!employeeInfo.getDepartment().equals(t.getDepartment())){
					TSDepart depart = commonService.getEntity(TSDepart.class,employeeInfo.getDepartment());
					if("2c902db662f164650162f1ce7e340045".equals(depart.getTSPDepart().getId())){
						employeeInfo.setEmployeeFlag(1);
					}
					if(null==employeeInfo.getInputerId()){
					employeeInfo.setInputerId(employeeInfoRepo.getInputerUserName(employeeInfo.getDepartment()));
					}
					employeeInfo.setReporterId(t.getInputerId());
					if(employeeInfoRepo.getInputerUserName(employeeInfo.getDepartment())==null){
						t.setInputerId(null);
						t.setReporterId(null);
					}
				}
				if((t.getChangeDate()==null&&employeeInfo.getChangeDate()!=null)||(t.getChangeDate()!=null&&employeeInfo.getChangeDate()==null)){
					i=1;
				}else if (t.getChangeDate()!=null&&employeeInfo.getChangeDate()!=null){
					if (!DateUtils.isSameDay(t.getChangeDate(),employeeInfo.getChangeDate())) {
						//生效日期
						i=1;
					}else if (!employeeInfo.getAStandardSalaryCh().equals(t.getAStandardSalaryCh())) {
						//A(标准)
						i=1;
					}else if (!employeeInfo.getSixGoldCityCh().equals(t.getSixGoldCityCh())) {
						//六金城市
						i=1;
					}else if(!employeeInfo.getSixGoldBaseCh().equals(t.getSixGoldBaseCh())) {
						//六金基数
						i=1;
					}else if(!employeeInfo.getA1PlaceCh().equals(t.getA1PlaceCh())) {
						//a1发薪地
						i=1;
					}else if(!employeeInfo.getA1PaymentCh().equals(t.getA1PaymentCh())) {
						//发薪金额1
						i=1;
					}
				}
				MyBeanUtils.copyBeanNotNull2Bean(employeeInfo, t);
				t.setA2Payment(t.getAStandardSalary()-t.getA1Payment());
				if(!StringUtil.isNotEmpty(employeeInfo.getChangeDate())){
					t.setChangeDate(null);t.setAStandardSalaryCh(null);t.setSixGoldBaseCh(null);t.setA1PaymentCh(null);t.setA2PaymentCh(null);
					t.setSixGoldCityCh(null);t.setA1PlaceCh(null);t.setA2PlaceCh(null);
				}else if(i == 1)t.setA2PaymentCh(t.getAStandardSalaryCh()-t.getA1PaymentCh());
				t.setA2PlaceCh(employeeInfo.getA2PlaceCh());
				//设置为OP员工，没有客户,单价,单价方式
				if(t.getEmployeeFlag()==1){
					t.setCustomerId(null);
					t.setUnitPriceType(null);
					t.setUnitPrice(null);
				}else{
					t.setCustomerId(employeeInfo.getCustomerId());
					t.setUnitPriceType(employeeInfo.getUnitPriceType());
					t.setUnitPrice(employeeInfo.getUnitPrice());
				}
				t.setLastModifiedDate(new Date());
				t.setEditors(UUID.randomUUID().toString().replace("-", "").toLowerCase());
				commonService.saveOrUpdate(t);
				t = commonService.getEntity(EmployeeInfoEntity.class, t.getId());
				if(i==1){
					if(t.getChangeFlag()==null||t.getChangeFlag()==0||t.getChangeFlag()==1){
						employeeInfoService.addEmployeeInfo(t);
					}else if (t.getChangeFlag()==2||t.getChangeFlag()==3) {
						employeeInfoService.changeEmployeeInfo(t);
					}
				}
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "员工信息更新失败";
			}
			j.setMsg(message);
		}
		}else{
			message = "您所编辑的员工信息已发生改变，员工信息更新失败";
			j.setMsg(message);
		}
		return j;
	}

	/**
	 * 编辑员工信息表
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "change")
	@ResponseBody
	public AjaxJson change(EmployeeInfoEntity employeeInfo, HttpServletRequest request) {
		EmployeeInfoEntity t = commonService.get(EmployeeInfoEntity.class, employeeInfo.getId());
		AjaxJson j = new AjaxJson();
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Integer i = 0;
		String message = null;
		if(t.getEditors().equals(employeeInfo.getEditors())){
			if((t.getChangeDate()==null&&employeeInfo.getChangeDate()!=null)||(t.getChangeDate()!=null&&employeeInfo.getChangeDate()==null)){
				i=1;
			}else if (t.getChangeDate()!=null&&employeeInfo.getChangeDate()!=null){
				if (!DateUtils.isSameDay(t.getChangeDate(),employeeInfo.getChangeDate())) {
					//生效日期
					i=1;
				}else if (!employeeInfo.getAStandardSalaryCh().equals(t.getAStandardSalaryCh())) {
					//A(标准)
					i=1;
				}else if (!employeeInfo.getSixGoldCityCh().equals(t.getSixGoldCityCh())) {
					//六金城市
					i=1;
				}else if(!employeeInfo.getSixGoldBaseCh().equals(t.getSixGoldBaseCh())) {
					//六金基数
					i=1;
				}else if(!employeeInfo.getA1PlaceCh().equals(t.getA1PlaceCh())) {
					//a1发薪地
					i=1;
				}else if(!employeeInfo.getA1PaymentCh().equals(t.getA1PaymentCh())) {
					//发薪金额1
					i=1;
				}
			}
			if(i==0){
				message = "员工信息表更新成功";
				j.setMsg(message);
				try {
					Integer ds = t.getDeclareStatus();
					MyBeanUtils.copyBeanNotNull2Bean(employeeInfo, t);
					//设置为OP员工，没有客户,单价,单价方式
					if(t.getEmployeeFlag()==1){
						t.setCustomerId(null);
						t.setUnitPriceType(null);
						t.setUnitPrice(null);
					}else{
						t.setCustomerId(employeeInfo.getCustomerId());
						t.setUnitPriceType(employeeInfo.getUnitPriceType());
						t.setUnitPrice(employeeInfo.getUnitPrice());
					}
					t.setDeclareStatus(ds);
					t.setLastModifiedDate(new Date());
					t.setEditors(UUID.randomUUID().toString().replace("-", "").toLowerCase());
					commonService.saveOrUpdate(t);
				} catch (Exception e) {
					e.printStackTrace();
					message = "员工信息表更新失败";
					j.setMsg(message);
				}
			}else{
				if (t.getChangeFlag()==0) {
					employeeInfo.setChangeFlag(1);
					j = save(employeeInfo, request);
				}else if(t.getChangeFlag()==1){
					j = save(employeeInfo, request);
				}else if(t.getChangeFlag()==2){
					employeeInfo.setChangeFlag(3);
					j = save(employeeInfo, request);
				}else if (t.getChangeFlag()==3) {
					j = save(employeeInfo, request);
				}
			}
		}else{
			message = "您所编辑的员工信息已发生改变，员工信息更新失败";
			j.setMsg(message);
		}
		return j;
	}

	/**
	 * 员工信息更改判断
	 * j 6-7
	 * @return
	 */
	public Integer saveReason(EmployeeInfoEntity employeeInfo, EmployeeInfoEntity t){
		Integer i = 0;
		StringBuffer reason =new StringBuffer("申请理由:");
		/*if (!DateUtils.isSameDay(t.getEntryDate(),employeeInfo.getEntryDate())) {
			//入职日
			i=1;
			reason.append("入职日,");
		}*/
		if (!t.getAStandardSalary().equals(employeeInfo.getAStandardSalary())&&employeeInfo.getAStandardSalary()!=null) {
			//A(标准)
			i=1;
			reason.append("A(标准),");
		}
		/*if (employeeInfo.getDiscount()!=t.getDiscount()) {
			//试用折扣率
			i=1;
			reason.append("试用折扣率,");
		}*/
		if (!t.getSixGoldCity().equals(employeeInfo.getSixGoldCity())&&employeeInfo.getSixGoldCity()!=null) {
			//六金城市
			i=1;
			reason.append("六金城市,");
		}
		if (!t.getSixGoldBase().equals(employeeInfo.getSixGoldBase())&&employeeInfo.getSixGoldBase()!=null) {
			//六金基数
			i=1;
			reason.append("六金基数,");
		}
		if (!t.getA1Place().equals(employeeInfo.getA1Place())&&employeeInfo.getA1Place()!=null) {
			//a1发薪地
			i=1;
			reason.append("a1发薪地,");
		}
		if (!t.getA1Payment().equals(employeeInfo.getA1Payment())&&employeeInfo.getA1Payment()!=null) {
			//发薪金额1
			i=1;
			reason.append("发薪金额1,");
		}
		if((t.getChangeDate()==null&&employeeInfo.getChangeDate()!=null)||(t.getChangeDate()!=null&&employeeInfo.getChangeDate()==null)){
			i=1;
		}else if (employeeInfo.getChangeDate()!=null){
			if (!DateUtils.isSameDay(t.getChangeDate(),employeeInfo.getChangeDate())) {
				//生效日期
				i=1;
			}else if (!employeeInfo.getAStandardSalaryCh().equals(t.getAStandardSalaryCh())) {
				//A(标准)
				i=1;
			}else if (!employeeInfo.getSixGoldCityCh().equals(t.getSixGoldCityCh())) {
				//六金城市
				i=1;
			}else if(!employeeInfo.getSixGoldBaseCh().equals(t.getSixGoldBaseCh())) {
				//六金基数
				i=1;
			}else if(!employeeInfo.getA1PlaceCh().equals(t.getA1PlaceCh())) {
				//a1发薪地
				i=1;
			}else if(!employeeInfo.getA1PaymentCh().equals(t.getA1PaymentCh())) {
				//发薪金额1
				i=1;
			}
		}
		if(i==1){
			reason.append("发生改变");
		}
		return i;
	}



	/**
	 * 添加员工信息表
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(EmployeeInfoEntity employeeInfo, HttpServletRequest request) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		//超级管理员、顶层管理员可以自己选择更改部门
		String orgIds = request.getParameter("orgIds");
		String orgCode = null;//部门编号
		TSDepart depart = new TSDepart();
		List<String> topManager = employeeInfoService.getTopManager();
		if(!"admin".equals(user.getUserName())&&!topManager.contains(user.getUserName())){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
			orgIds = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			orgCode = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getOrgCode():null;
			depart = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart():null;
		}
		//根据截取部门编号得知用户属于哪个部门，来判断应该录入的员工类型
		String orgCodeStr = null;
		if(StringUtil.isNotEmpty(orgCode)){
			orgCodeStr = orgCode.substring(0, 6);
		}
		//如果是用户管理部门的,并且前端并未传员工类型过来(代表不是业务部门总监登陆系统)
		if("A07A03".equals(orgCodeStr) && employeeInfo.getEmployeeFlag() == null){
			employeeInfo.setEmployeeFlag(1);
			//如果是用户业务部门的,并且前端并未传员工类型过来(代表不是业务部门总监登陆系统)
		}else if("A07A04".equals(orgCodeStr) && employeeInfo.getEmployeeFlag() == null){
			employeeInfo.setEmployeeFlag(0);
		}
		List<String> orgIdList = extractIdListByComma(orgIds);
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(employeeInfo.getId())) {
			message = "员工信息表更新成功";
			EmployeeInfoEntity t = commonService.get(EmployeeInfoEntity.class, employeeInfo.getId());
			if(t.getEditors().equals(employeeInfo.getEditors())){
			try {
				Integer lookUp = saveReason(employeeInfo,t);
				Integer oldDeclareStatus = t.getDeclareStatus();
				MyBeanUtils.copyBeanNotNull2Bean(employeeInfo, t);
				if(!StringUtil.isNotEmpty(employeeInfo.getChangeDate())){
					t.setChangeDate(null);t.setAStandardSalaryCh(null);t.setSixGoldBaseCh(null);t.setA1PaymentCh(null);t.setA2PaymentCh(null);
					t.setSixGoldCityCh(null);t.setA1PlaceCh(null);t.setA2PlaceCh(null);
				}
				//设置基本工资和绩效工资
				if(null!=employeeInfo.getAStandardSalary()&&null!=employeeInfo.getA1Payment()){
					t.setA2Payment(Math.round((employeeInfo.getAStandardSalary()-employeeInfo.getA1Payment())*100)/100.0);
					Double b = Double.parseDouble(commonRepository.getSystemBasePay());
					t.setMeritPay((employeeInfo.getAStandardSalary()-b)*100/100.0);
					if(employeeInfo.getMeritPay()<0) {employeeInfo.setMeritPay((double)0);}
				}
				if(null==t.getA2Payment()||0==t.getA2Payment()){
					t.setA2Place(null);
				}
				t.setA2PlaceCh(employeeInfo.getA2PlaceCh());
				if(null!=employeeInfo.getAStandardSalaryCh()&&null!=employeeInfo.getA1PaymentCh()){
					t.setA2PaymentCh(Math.round((employeeInfo.getAStandardSalaryCh()-employeeInfo.getA1PaymentCh())*100)/100.0);
					if(employeeInfo.getAStandardSalaryCh()==employeeInfo.getA1PaymentCh())
						t.setA2PaymentCh(null);
				}
				//设置为OP员工，没有客户,单价,单价方式
				if(t.getEmployeeFlag()==1){
					t.setCustomerId(null);
					t.setUnitPriceType(null);
					t.setUnitPrice(null);
				}else{
					t.setCustomerId(employeeInfo.getCustomerId());
					t.setUnitPriceType(employeeInfo.getUnitPriceType());
					t.setUnitPrice(employeeInfo.getUnitPrice());
				}
				//导入员工设置为入职成功，部门经理修改下级无法看到，其他修改都为初始未申报状态
				if(employeeInfo.getDeclareStatus()==100||employeeInfo.getDeclareStatus()==101){
					t.setDeclareStatus(2);
				}else if (lookUp == 0) {
					t.setDeclareStatus(oldDeclareStatus);
				}else {
					t.setDeclareStatus(employeeInfoService.getStatus(user));
				}
				t.setDelFlg(0);
				if(null!=user){
					t.setLastModifiedBy(user.getUserName());
				}
				t.setLastModifiedDate(new Date());
				t.setEditors(UUID.randomUUID().toString().replace("-", "").toLowerCase());
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "员工信息表更新失败";
			}
			}else{
				message = "您所编辑的员工信息已发生改变，员工信息更新失败";
			}
		} else {
			message = "员工信息表添加成功";
			//对是否是同一部门进行判断
			if(!orgIdList.isEmpty()){
				if(org.springframework.util.StringUtils.isEmpty(employeeInfo.getDepartment())){
					employeeInfo.setDepartment(depart.getId());
				}
			}
			//如果没有传入inputername，则设置为自己，如果为导入，则设置为空
			if(!StringUtils.isNotEmpty(employeeInfo.getInputerId())){
				employeeInfo.setInputerId(user.getUserName());
			}
			//设置发薪地2工资，发薪地2，基本工资和绩效工资
			if(null!=employeeInfo.getAStandardSalary()&&null!=employeeInfo.getA1Payment()){
				employeeInfo.setA2Payment(Math.round((employeeInfo.getAStandardSalary()-employeeInfo.getA1Payment())*100)/100.0);
			}
			Double b = Double.parseDouble(commonRepository.getSystemBasePay());
			employeeInfo.setBasePay(b);
			employeeInfo.setMeritPay((employeeInfo.getAStandardSalary()-b)*100/100.0);
			if(null!=employeeInfo.getAStandardSalaryCh()&&null!=employeeInfo.getA1PaymentCh()){
				employeeInfo.setA2PaymentCh(Math.round((employeeInfo.getAStandardSalaryCh()-employeeInfo.getA1PaymentCh())*100)/100.0);
				if(employeeInfo.getAStandardSalaryCh()==employeeInfo.getA1PaymentCh())
					employeeInfo.setA2PaymentCh(null);
			}
			//设置为OP员工，没有客户,单价,单价方式
			if(employeeInfo.getEmployeeFlag()==1){
				employeeInfo.setCustomerId(null);
				employeeInfo.setUnitPriceType(null);
				employeeInfo.setUnitPrice(null);
			}
			GregorianCalendar gc=new GregorianCalendar();
			if(employeeInfo.getEntryDate().after(new Date())||employeeInfo.getDeclareStatus()==101){
				gc.setTime(new Date());
			}else{
				gc.setTime(employeeInfo.getEntryDate());
			}
			gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
			employeeInfo.setEffectiveDate(gc.getTime());
			gc.add(1, 15);
			gc.set(GregorianCalendar.DAY_OF_MONTH, 0);
			employeeInfo.setExpiryDate(gc.getTime());
			if(employeeInfo.getMeritPay()<0) {employeeInfo.setMeritPay((double)0);}
			employeeInfo.setDelFlg(0);
			if(null!=user){
				employeeInfo.setCreatedBy(user.getUserName());
			}
			//新增权限判断设置setDeclareStatus
			if(employeeInfo.getDeclareStatus()==100||employeeInfo.getDeclareStatus()==101){
				employeeInfo.setInsurance(1);
				employeeInfo.setDeclareStatus(2);
			}else{
				employeeInfo.setDeclareStatus(employeeInfoService.getStatus(user));
			}
			if(null == employeeInfo.getQuitStatus()){
				employeeInfo.setQuitStatus(0);
			}
			employeeInfo.setEditors(UUID.randomUUID().toString().replace("-", "").toLowerCase());
			employeeInfo.setCreatedDate(new Date());
			commonService.save(employeeInfo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * 导出excel
	 * @param departId
	 * @param declareStartDate
	 * @param declareEndDate
	 * @param status
	 * @return
	 *
	 */
	@RequestMapping(params="excelExport")
	public void exportXls(@RequestParam("id") String id,@RequestParam("departId") String departId,
			@RequestParam("name") String name,@RequestParam("employeeFlag") String employeeFlag,
			@RequestParam("declareStatus") String declareStatus,@RequestParam("insurance") String insurance,
			HttpServletRequest request,
			HttpServletResponse response,
			DataGrid dataGrid) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSDepart depart = commonService.getEntity(TSDepart.class, user.getDepartid());
        CriteriaQuery cq = new CriteriaQuery(EmployeeInfoEntity.class, dataGrid);
        cq.eq("delFlg", 0);
        if(!StringUtil.isNotEmpty(id)||"undefined".equals(id)){
        	Integer lv = commonRepository.getDepartGread(depart);
	        if(lv ==1||lv==2){
	        	lv = 4;
	        }else{
	        	if(employeeInfoService.getNameRoleCold(user)==1){
	        		lv = lv*2+2;
	        	}else{
	        		lv *=2;
	        	}
	        }
	        if(StringUtil.isNotEmpty(declareStatus)&&!"undefined".equals(declareStatus)){
	        	if(Integer.parseInt(declareStatus)==lv||Integer.parseInt(declareStatus)==lv-1||Integer.parseInt(declareStatus)==2||Integer.parseInt(declareStatus)==1){
		        	cq.eq("declareStatus",Integer.parseInt(declareStatus));
		        }else if(Integer.parseInt(declareStatus)>lv){
		        	cq.ge("declareStatus", Integer.parseInt(declareStatus));
		        }else{
		        	cq.between("declareStatus", 4, Integer.parseInt(declareStatus));
		        }
	        }
	        if (StringUtil.isEmpty(departId)||"undefined".equals(departId)){
	        	if(employeeInfoService.getNameRoleCold(user)==3||employeeInfoService.getNameRoleCold(user)==4){
	        		departId="2c902db662f164650162f1ce2337003f";
	        	}else{
	        		departId = user.getDepartid();
	        	}
	        }
			TSDepart exDepart = commonService.getEntity(TSDepart.class,departId);
			List<TSDepart > departs = commonRepository.subdivision(exDepart);
			String[] departIds = new String[departs.size()];
			int i = 0;
			for(TSDepart dp : departs){
				departIds[i] = dp.getId();
				i++;
			}
			cq.in("department",departIds);
			if(employeeInfoService.getNameRoleCold(user)==1){
				cq.eq("inputerId", user.getUserName());
			}
			if(StringUtil.isNotEmpty(name)&&!"undefined".equals(name)){
				cq.like("name", "%"+name+"%");
			}
			if(StringUtil.isNotEmpty(employeeFlag)&&!"undefined".equals(employeeFlag)){
				cq.eq("employeeFlag", Integer.parseInt(employeeFlag));
			}
			if(StringUtil.isNotEmpty(insurance)&&!"undefined".equals(insurance)){
				cq.eq("insurance", Integer.parseInt(insurance));
			}
		}else{
			String[] sids = id.split(",");
			Integer[] lids = new Integer[sids.length];
			int i = 0;
			for(String sid : sids) {
				lids[i]=Integer.parseInt(sid);
				i++;
			}
			cq.in("id", lids);
		}
        cq.gt("expiryDate", new Date());
		cq.le("effectiveDate", new Date());
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,new EmployeeInfoEntity(), request.getParameterMap());
        List<EmployeeInfoEntity> employeeInfoList = this.commonService.getListByCriteriaQuery(cq,false);
        //实体得到完成
        Map<String,Object> result=employeeInfoService.exportExcelDatas(employeeInfoList);
		ServletOutputStream out = null;
		try {
			String userAgent = request.getHeader("user-agent").toLowerCase();
			String filePath =(String) result.get("fileName");
			String codedFilename = new String();
			if (userAgent.contains("msie") || userAgent.contains("like gecko") ) {
		        // win10 ie edge 浏览器 和其他系统的ie
				codedFilename = "="+URLEncoder.encode(filePath, "UTF-8");
			} else {
		        // fe
				codedFilename = "*=utf-8''"+URLEncoder.encode(filePath, "UTF-8");
//				codedFilename = new String(filePath.getBytes("UTF-8"), "iso-8859-1");
			}
			response.addHeader("Content-Disposition", "attachment;filename"
					+codedFilename);
			out=response.getOutputStream();
			XSSFWorkbook wb = (XSSFWorkbook) result.get("wb");
			wb.write(out);
			out.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if(null!=out){
				try {
					out.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}



	/**
	 * 校验字段是否重复
	 * @param field  格式： name-王二
	 * @param condition  格式：delFlg-0
	 * @return
	 */
	@RequestMapping(params="checkFieldRepeat")
	@ResponseBody
	public Map<String,Object> checkFieldRepeat(String condition){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		if(StringUtils.isNotBlank(condition)){
			String[] sFields = condition.split(",");
			String c = "";
			for(int i = 0;i<sFields.length;i++){
				if(i==0){
					if("id".equals(sFields[i].split("-")[0])){
						c = sFields[i].split("-")[0]+"!="+sFields[i].split("-")[1];
					}else{
						c = sFields[i].split("-")[0]+"="+sFields[i].split("-")[1];
					}
				}else{
					if("id".equals(sFields[i].split("-")[0])){
						c += " and "+sFields[i].split("-")[0]+"!="+sFields[i].split("-")[1];
					}else{
						c += " and "+sFields[i].split("-")[0]+"="+sFields[i].split("-")[1];
					}
				}
			}
			Long cusCount = employeeInfoRepo.countByCusField(c);
			if(cusCount!=null&&cusCount>0){
				result.put("errCode", -1);
			}
		}
		return result;
	}
	/**
	 * 判断行为空
	 * @param empFile
	 * @param response
	 * @return
	 */

	public boolean isRowEmpty(Row row) {
	    for (int c = 0; c < row.getLastCellNum(); c++) {
	        Cell cell = row.getCell(c);
	        if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
	            return false;
	    }
	    return true;
	}
	/**
	 * 员工信息导入
	 * @param empFile
	 * @param response
	 * @return
	 */
	@RequestMapping(params="empImport")

	public ResponseEntity<Map<String,Object>> employeeInfoImport(@RequestParam("empFile") MultipartFile empFile,
			HttpServletRequest request,
			HttpServletResponse response){

		Map<String,Object> result = new HashMap<String,Object>();
		Workbook wb = null;
		boolean flag = true;
		boolean flag1 = true;
		int success = 0;
		int mould = 0;										//模板判断
		int errorList = 1;									//行号
		int line = 0;           							//起始列
		Map<Integer,String> map = new HashMap<>();

		AjaxJson aj = null;
		String lineName = new String(); 							//错误列名
		StringBuffer dpartError = new StringBuffer();		//员工部门不存在
		StringBuffer covername = new StringBuffer();		//更新员工信息
		StringBuffer tableCell = new StringBuffer();		//单元格数据错误
		StringBuffer coderror = new StringBuffer();			//身份证号错误
		StringBuffer coderror1 = new StringBuffer();			//身份证号错误
		StringBuffer errorName = new StringBuffer();		//未知失败
		StringBuffer massage = new StringBuffer();			//错误信息
		Double basePay =Double.parseDouble(commonRepository.getSystemBasePay()); //
		try {
			if(!empFile.isEmpty()){
				wb = WorkbookFactory.create(empFile.getInputStream());
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rows = sheet.iterator();
				Row row=null;
				while(rows.hasNext()){
					row = rows.next();
					errorList++;
					if (!isRowEmpty(row)) {
						line = row.getFirstCellNum();
						break;
					}
				}
				while(rows.hasNext()){
					row = rows.next();
					errorList=row.getRowNum()+1;
					//判断模板单元格入职日、姓名、ID同时为空直接退出循环
					if(isRowEmpty(row)){
						continue;
					}
					int trueLine1 = line;
					if(null==row.getCell(trueLine1)&&null==row.getCell(trueLine1+1)&&null==row.getCell(trueLine1+2) ){
						continue;
					}
					trueLine1++;trueLine1++;
					try {
						String id1 = EmployeeInfoService.replaceBlank(row.getCell(trueLine1).getStringCellValue());
						if(map.size()!=0){
							for(Object obj : map.keySet()){
								if(id1.equals(map.get(obj))){
									coderror1.append("第"+errorList+"行和第"+obj+"行身份证号相同，请检查。");
									flag1 = false;
								}
							}
						}
						map.put(errorList, id1);
					} catch (Exception e) {
						log.error(e.getMessage(),e);
						tableCell.append("第"+errorList+"行，身份证单元格格式错误。");
					}
				}
				map.clear();
				if(flag1){
					errorList = 1;
					wb = WorkbookFactory.create(empFile.getInputStream());
					 sheet = wb.getSheetAt(0);
					 rows = sheet.iterator();
					 row=null;
					while(rows.hasNext()){
						row = rows.next();
						errorList++;
						if (!isRowEmpty(row)) {
							line = row.getFirstCellNum();
							break;
						}
					}
					if(rows.hasNext()&&(null==row.getCell(line+20)||null!=row.getCell(line+21))){
						massage.append("请导入正确模板");
					}else{
					while(rows.hasNext()){
						int trueLine = line;

						mould = 1;
						row = rows.next();
						errorList=row.getRowNum()+1;
						//判断模板单元格入职日、姓名、ID同时为空直接退出循环
						if(isRowEmpty(row)){
							continue;
						}
						if(null==row.getCell(trueLine)&&null==row.getCell(trueLine+1)&&null==row.getCell(trueLine+2) ){
							continue;
						}
						try {
//						String tsUserId = user.getId();
							lineName= "入职日";
							Date entryDate = row.getCell(trueLine).getDateCellValue();trueLine++;lineName= "姓名";
							String name = EmployeeInfoService.replaceBlank(row.getCell(trueLine).getStringCellValue());trueLine++;lineName= "身份证号";
							String id = EmployeeInfoService.replaceBlank(row.getCell(trueLine).getStringCellValue());trueLine++;
							if(!CommonRepository.isValidIdNo(id)){
								coderror.append("，第"+errorList+"行身份证号错误。");
								continue;
							}
							lineName= "招商银行账户";
							String cmbAccount = EmployeeInfoService.replaceBlank(row.getCell(trueLine).getStringCellValue());trueLine++;lineName= "工商银行账户";
							String icbcAccount = EmployeeInfoService.replaceBlank(row.getCell(trueLine).getStringCellValue());trueLine++;lineName= "部门";
							String depart = EmployeeInfoService.replaceAllBlank(row.getCell(trueLine).getStringCellValue());trueLine++;lineName= "属性";
							String empflag = EmployeeInfoService.replaceAllBlank(row.getCell(trueLine).getStringCellValue());trueLine++; lineName= "六金地点"; //属性
							String sixGoldPlace = EmployeeInfoService.replaceAllBlank(row.getCell(trueLine).getStringCellValue());trueLine++;lineName= "六金基数";
							Double sixGoldBase = row.getCell(trueLine).getNumericCellValue();trueLine++;lineName= "A";
							Double aStandard = row.getCell(trueLine).getNumericCellValue();
							String salaryPlace1 = null;
							Double salary1 = null;
							boolean isMatchPlace1 = false;
							//设置发薪地1
							if(StringUtils.isNotBlank(sixGoldPlace)){
								//北京
								if(sixGoldPlace.equals("北京")){
									salaryPlace1 = "北京";
									trueLine++;lineName= "北京";
									salary1 = row.getCell(trueLine).getNumericCellValue();
									isMatchPlace1 = true;
								}/*else if(sixGoldPlace.equals("智蓝")){
								salaryPlace1 = "智蓝";
								num=12;
								salary1 = row.getCell(12).getNumericCellValue();
								isMatchPlace1 = true;
							}*/else if(sixGoldPlace.equals("昆山")){
								salaryPlace1 = "昆山";
								trueLine+=2;lineName= "昆山";
								salary1 = row.getCell(trueLine).getNumericCellValue();
								isMatchPlace1 = true;
							}else if(sixGoldPlace.equals("上海")){
								salaryPlace1 = "上海";
								trueLine+=3;lineName= "上海";
								salary1 = row.getCell(trueLine).getNumericCellValue();
								isMatchPlace1 = true;
							}else if(sixGoldPlace.equals("广州")){
								salaryPlace1 = "广州";
								trueLine+=4;lineName= "广州";
								salary1 = row.getCell(trueLine).getNumericCellValue();
								isMatchPlace1 = true;
							}else if(sixGoldPlace.equals("深圳")){
								salaryPlace1 = "深圳";
								trueLine+=5;lineName= "深圳";
								salary1 = row.getCell(trueLine).getNumericCellValue();
								isMatchPlace1 = true;
							}else if(sixGoldPlace.equals("江苏")){
								salaryPlace1 = "江苏";
								trueLine+=6;lineName= "江苏";
								salary1 = row.getCell(trueLine).getNumericCellValue();
								isMatchPlace1 = true;
							}
								//为空第一遍寻找发薪地1
								trueLine = line +9;
								if(StringUtils.isEmpty(salaryPlace1)||salary1==0){
									if(3!=row.getCell(trueLine+1).getCellType()&&0!=row.getCell(trueLine+1).getNumericCellValue()){
										salaryPlace1 = "北京";
										trueLine+=1;lineName= "北京";
										salary1 = row.getCell(trueLine).getNumericCellValue();
									}else if(3!=row.getCell(trueLine+2).getCellType()&&0!=row.getCell(trueLine+2).getNumericCellValue()){
									salaryPlace1 = "昆山";
									trueLine+=2;lineName= "昆山";
									salary1 = row.getCell(trueLine).getNumericCellValue();
									}else if(3!=row.getCell(trueLine+3).getCellType()&&0!=row.getCell(trueLine+3).getNumericCellValue()){
										salaryPlace1 = "上海";
										trueLine+=3;lineName= "上海";
										salary1 = row.getCell(trueLine).getNumericCellValue();
									}else if(3!=row.getCell(trueLine+4).getCellType()&&0!=row.getCell(trueLine+4).getNumericCellValue()){
										salaryPlace1 = "广州";
										trueLine+=4;lineName= "广州";
										salary1 = row.getCell(trueLine).getNumericCellValue();
									}else if(3!=row.getCell(trueLine+5).getCellType()&&0!=row.getCell(trueLine+5).getNumericCellValue()){
										salaryPlace1 = "深圳";
										trueLine+=5;lineName= "深圳";
										salary1 = row.getCell(trueLine).getNumericCellValue();
									}else if(3!=row.getCell(trueLine+6).getCellType()&&0!=row.getCell(trueLine+6).getNumericCellValue()){
										salaryPlace1 = "江苏";
										trueLine+=6;lineName= "江苏";
										salary1 = row.getCell(trueLine).getNumericCellValue();
									}
								}
							}
							trueLine =line +9;
							String salaryPlace2 = null;
							Double salary2 = null;
							//设置发薪地2 如果匹配到就设置
							if(isMatchPlace1){
								//不包含发薪地1并且是有内容的就设置为发薪地2
								if(!salaryPlace1.equals("北京")
										&&3!=row.getCell(trueLine+1).getCellType()){
									salaryPlace2 = "北京";
									trueLine+=1;lineName= "北京";
									salary2 = row.getCell(trueLine).getNumericCellValue();
								}/*else if(!sixGoldPlace.equals("智蓝")
									&&3!=row.getCell(12).getCellType()){
								salaryPlace2 = "智蓝";
								num=12;
								salary2 = row.getCell(12).getNumericCellValue();
							}*/else if(!salaryPlace1.equals("昆山")
									&&3!=row.getCell(trueLine+2).getCellType()){
								salaryPlace2 = "昆山";
								trueLine+=2;lineName= "昆山";
								salary2 = row.getCell(trueLine).getNumericCellValue();
								}else if(!salaryPlace1.equals("上海")
										&&3!=row.getCell(trueLine+3).getCellType()){
									salaryPlace2 = "上海";
									trueLine+=3;lineName= "上海";
									salary2 = row.getCell(trueLine).getNumericCellValue();
								}else if(!salaryPlace1.equals("广州")
										&&3!=row.getCell(trueLine+4).getCellType()){
									salaryPlace2 = "广州";
									trueLine+=4;lineName= "广州";
									salary2 = row.getCell(trueLine).getNumericCellValue();
								}else if(!salaryPlace1.equals("深圳")
										&&3!=row.getCell(trueLine+5).getCellType()){
									salaryPlace2 = "深圳";
									trueLine+=5;lineName= "深圳";
									salary2 = row.getCell(trueLine).getNumericCellValue();
								}else if(!salaryPlace1.equals("江苏")
										&&3!=row.getCell(trueLine+6).getCellType()){
									salaryPlace2 = "江苏";
									trueLine+=6;lineName= "江苏";
									salary2 = row.getCell(trueLine).getNumericCellValue();
								}
							}
							if("昆山".equals(salaryPlace1)){
								salary1 = aStandard;
								salaryPlace2 = null;
							}
							if(aStandard-salary1>0){
								salaryPlace2 = "江苏";
							}
							trueLine =line +16;lineName= "试用折扣率";
							//手机 属性，性别，邮箱，户口性质
							Double discount = row.getCell(trueLine).getNumericCellValue();trueLine++;lineName= "手机号"; //试用折扣率
							String mobile = row.getCell(trueLine).getStringCellValue();trueLine++;lineName= "性别";
							String gender = row.getCell(trueLine).getStringCellValue();trueLine++;lineName= "邮箱";
							String email = row.getCell(trueLine).getStringCellValue();trueLine++;lineName= "户口性质";
							String houseHold = row.getCell(trueLine).getStringCellValue();
							//再写身份证重复判断语句
							//对部门进行校验
							//depart.equals(user.getDepartid());
							trueLine =line;
							if(entryDate==null){
								tableCell.append("第"+errorList+"行，入职日不可为空，");
								continue;
							}else if (StringUtils.isEmpty(name)) {
								tableCell.append("第"+errorList+"行，姓名不可为空，");
								continue;
							}else if (StringUtils.isEmpty(id)) {
								tableCell.append("第"+errorList+"行，身份证号不可为空，");
								continue;
							}else if (StringUtils.isEmpty(depart)) {
								tableCell.append("第"+errorList+"行，部门不可为空，");
								continue;
							}else if (StringUtils.isEmpty(empflag)||!(empflag.equals("TECH")||empflag.equals("OP"))) {
								tableCell.append("第"+errorList+"行，属性必须填写“TECH”或者“OP”，");
								continue;
							}else if (StringUtils.isEmpty(sixGoldPlace)) {
								tableCell.append("第"+errorList+"行，六金城市不可为空，");
								continue;
							}else if (sixGoldBase.equals((double)0)) {
								tableCell.append("第"+errorList+"行，六金基数不可为空，");
								continue;
							}else if (aStandard.equals((double)0)) {
								tableCell.append("第"+errorList+"行，A不可为空，");
								continue;
							}else if (discount.equals((double)0)) {
								tableCell.append("第"+errorList+"行，试用折扣率不可为空，");
								continue;
							}/*else if (StringUtils.isEmpty(mobile)) {
								tableCell.append("第"+errorList+"行，手机号不可为空，");
								continue;
							}*/else if (StringUtil.isNotEmpty(gender)&&!("男".equals(gender)||"女".equals(gender))) {
								tableCell.append("第"+errorList+"行，性别必须填写为“男”或者“女”，");
								continue;
							}/*else if (StringUtils.isEmpty(email)) {
								tableCell.append("第"+errorList+"行，邮箱不可为空，");
								continue;
							}*/else if (StringUtil.isNotEmpty(houseHold)&&!("本埠农业".equals(houseHold)||"本埠城镇".equals(houseHold)||"外埠农业".equals(houseHold)||"外埠城镇".equals(houseHold))) {
								tableCell.append("第"+errorList+"行，户口性质必须填写为“本埠农业”、“本埠城镇”、“外埠农业”、“外埠城镇”之一，");
								continue;
							}else if(StringUtils.isEmpty(cmbAccount)&&StringUtils.isEmpty(icbcAccount)){
								tableCell.append("第"+errorList+"行，招商银行账户或者工商银行账户至少填写一个，");
								continue;
							}/*else if(aStandard != salary1+salary2){
								tableCell.append("第"+errorList+"行，a工资不等于a1工资加a2工资，");
								continue;
							}else if(aStandard < basePay){
								tableCell.append("第"+errorList+"行，a工资小于基本工资，");
								continue;
							}*/else if(discount>100 || discount<1){
								tableCell.append("第"+errorList+"行，试用折扣率填写错误，");
								continue;
							}
							List<EmployeeInfoEntity> employeeInfoTest = commonService.findByProperty(EmployeeInfoEntity.class, "code", id);
/*							System.out.println((employeeInfoTest.size() ==0 || null == employeeInfoTest));
							System.out.println(!(employeeInfoTest.size() ==0 || null == employeeInfoTest));
							System.out.println((null == employeeInfoTest ));
							System.out.println(( employeeInfoTest.size() ==0));*/
							if(!(employeeInfoTest.size() ==0 || null == employeeInfoTest)){
								List<TSDepart> tsList = commonService.findByProperty(TSDepart.class, "departname", depart);
								if(!tsList.isEmpty()&&("4".equals(tsList.get(0).getOrgType())||"5".equals(tsList.get(0).getOrgType()))){
									if ("2c902db662f164650162f1ce7e340045".equals(tsList.get(0).getTSPDepart().getId())) {
										empflag = "OP";
									}
									EmployeeInfoEntity employeeInfo = new EmployeeInfoEntity();
									for(EmployeeInfoEntity emp :employeeInfoTest){
										GregorianCalendar gc=new GregorianCalendar();
										gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
										if(emp.getExpiryDate().after(gc.getTime())){
											if(DateUtils.isSameDay(emp.getEffectiveDate(),gc.getTime())){
												employeeInfo =emp;
												if(employeeInfo.getChangeDate()!=null){
													employeeInfo.setChangeDate(null);
													change(employeeInfo, request);
												}
											}else if(emp.getEffectiveDate().after(gc.getTime())){
												commonService.delete(emp);
											}else{
												gc.add(GregorianCalendar.DATE,1);
												emp.setExpiryDate(gc.getTime());
												commonService.saveOrUpdate(emp);
											}
										}
									}
									GregorianCalendar gc=new GregorianCalendar();
									gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
									employeeInfo.setEffectiveDate(gc.getTime());
									gc.add(1, 15);
									gc.set(GregorianCalendar.DAY_OF_MONTH, 0);
									employeeInfo.setExpiryDate(gc.getTime());
									//设置部门总监
									employeeInfo.setInputerId(employeeInfoRepo.getInputerUserName(tsList.get(0).getId()));
									employeeInfo.setReporterId(employeeInfoRepo.getInputerUserName(tsList.get(0).getId()));
									employeeInfo.setEntryDate(entryDate);
									employeeInfo.setName(name);
									employeeInfo.setCode(id);
									employeeInfo.setCmbAccount(cmbAccount);
									employeeInfo.setIcbcAccount(icbcAccount);
									employeeInfo.setDepartment(tsList.get(0).getId());
									employeeInfo.setEmployeeFlag(empflag.equals("TECH")?0:1);
									employeeInfo.setSixGoldCity(sixGoldPlace);
									employeeInfo.setA1Place(salaryPlace1);
									employeeInfo.setSixGoldBase(sixGoldBase);
									employeeInfo.setAStandardSalary(aStandard);
									employeeInfo.setA1Payment(salary1);
									employeeInfo.setA2Place(salaryPlace2);
									employeeInfo.setA2Payment(salary2);
									Double b = Double.parseDouble(commonRepository.getSystemBasePay());
									employeeInfo.setBasePay((double)b);
									employeeInfo.setMeritPay(aStandard-b);
									if(employeeInfo.getMeritPay()<0) {employeeInfo.setMeritPay((double)0);}
									employeeInfo.setDiscount(discount.intValue());
									employeeInfo.setContactWay(mobile);
									if(StringUtils.isNotBlank(gender)){
										if("男".equals(gender)){
											employeeInfo.setGender(0);
										}else if("女".equals(gender)){
											employeeInfo.setGender(1);
										}
									}
									employeeInfo.setEmail(email);
									//本埠农业_0,本埠城镇_1,外埠农业_2,外埠城镇_3
									if(StringUtils.isNotBlank(houseHold)){
										if("本埠农业".equals(houseHold)){
											employeeInfo.setHouseholdRegistration(0);
										}else if("本埠城镇".equals(houseHold)){
											employeeInfo.setHouseholdRegistration(1);
										}else if("外埠农业".equals(houseHold)){
											employeeInfo.setHouseholdRegistration(2);
										}else if("外埠城镇".equals(houseHold)){
											employeeInfo.setHouseholdRegistration(3);
										}
									}
									employeeInfo.setDeclareStatus(101);
									employeeInfo.setChangeFlag(0);
									employeeInfo.setQuitStatus(1);
									if(empflag.equals("TECH")){
										employeeInfo.setEmployeeFlag(0);
										aj =  save(employeeInfo,request);
										if(aj.isSuccess()){
											success ++;
											covername.append(name+",");
										}else{
											errorName.append(name+",");
										}
									}else if(empflag.equals("OP")){
										employeeInfo.setEmployeeFlag(1);
										aj =  save(employeeInfo,request);
										if(aj.isSuccess()){
											success ++;
											covername.append(name+",");
										}else{
											errorName.append(name+",");
										}
									}
								}else {
									dpartError.append(name+"部门不存在。");
								}
							}else {
								EmployeeInfoEntity employeeInfo = new EmployeeInfoEntity();
								//设置入职日
								employeeInfo.setEntryDate(entryDate);
								//设置姓名
								employeeInfo.setName(name);
								employeeInfo.setCode(id);
								employeeInfo.setCmbAccount(cmbAccount);
								employeeInfo.setIcbcAccount(icbcAccount);
								//部门判断，没有的部门导入失败
								List<TSDepart> tsList = commonService.findByProperty(TSDepart.class, "departname", depart);
								if(!tsList.isEmpty()&&("4".equals(tsList.get(0).getOrgType())||"5".equals(tsList.get(0).getOrgType()))){
									//
									if ("2c902db662f164650162f1ce7e340045".equals(tsList.get(0).getTSPDepart().getId())) {
										empflag = "OP";
									}
									employeeInfo.setInputerId(employeeInfoRepo.getInputerUserName(tsList.get(0).getId()));
									employeeInfo.setReporterId(employeeInfoRepo.getInputerUserName(tsList.get(0).getId()));
									employeeInfo.setDepartment(tsList.get(0).getId());
									employeeInfo.setSixGoldCity(sixGoldPlace);
									employeeInfo.setA1Place(salaryPlace1);
									employeeInfo.setSixGoldBase(sixGoldBase);
									employeeInfo.setAStandardSalary(aStandard);
									employeeInfo.setA1Payment(salary1);
									employeeInfo.setA2Place(salaryPlace2);
									employeeInfo.setA2Payment(salary2);
									Double b = Double.parseDouble(commonRepository.getSystemBasePay());
									employeeInfo.setBasePay(b);
									employeeInfo.setMeritPay(aStandard-b);
									if(employeeInfo.getMeritPay()<0) {employeeInfo.setMeritPay((double)0);}
									employeeInfo.setDiscount(discount.intValue());
									employeeInfo.setContactWay(mobile);
									if(StringUtils.isNotBlank(gender)){
										if("男".equals(gender)){
											employeeInfo.setGender(0);
										}else if("女".equals(gender)){
											employeeInfo.setGender(1);
										}
									}
									employeeInfo.setEmail(email);
									//本埠农业_0,本埠城镇_1,外埠农业_2,外埠城镇_3
									if(StringUtils.isNotBlank(houseHold)){
										if("本埠农业".equals(houseHold)){
											employeeInfo.setHouseholdRegistration(0);
										}else if("本埠城镇".equals(houseHold)){
											employeeInfo.setHouseholdRegistration(1);
										}else if("外埠农业".equals(houseHold)){
											employeeInfo.setHouseholdRegistration(2);
										}else if("外埠城镇".equals(houseHold)){
											employeeInfo.setHouseholdRegistration(3);
										}
									}
									employeeInfo.setDeclareStatus(100);
									employeeInfo.setChangeFlag(0);
									employeeInfo.setQuitStatus(1);
									if(empflag.equals("TECH")){
										employeeInfo.setEmployeeFlag(0);
										aj =  save(employeeInfo,request);
										if(aj.isSuccess()){
											success ++;
											covername.append(name+",");
										}else{
											errorName.append(name+",");
										}
									}else if(empflag.equals("OP")){
										employeeInfo.setEmployeeFlag(1);
										aj =  save(employeeInfo,request);
										if(aj.isSuccess()){
											success ++;
											covername.append(name+",");
										}else{
											errorName.append(name+",");
										}
									}
									/*if(user.getDepartid().equals(tsList.get(0).getId())){}else{
									//employeeInfo.setDepartment(null);
									error++;
									errorname.append(name);
								}*/
								}else{
									//employeeInfo.setDepartment(null);
									dpartError.append(name+"部门不存在。");
								}
							}
						} catch (Exception e) {
							log.error(e.getMessage(),e);
							tableCell.append("第"+errorList+"行，"+lineName+"列，表格格式错误。");
						}
					}
				}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			flag = false;
		}
		if(flag){
			if(covername.length()>0){
				/*massage.append(covername+"信息更新");*/
				massage.insert(0,covername+"信息更新。");
				massage.insert(0,"成功导入："+success+"条。");
			}
			if(dpartError.length()>0){
				/*massage.append(dpartError+"部门不存在");*/
				massage.insert(0,dpartError);
			}
			if(coderror.length()>0){
				/*massage.append(coderror+"身份证号错误");*/
				massage.insert(0,coderror);
			}
			if(tableCell.length()>0){
				/*massage.append(tableCell+"错误!");*/
				massage.insert(0,tableCell);
			}
			if(errorName.length()>0){
				/*massage.append(errorName+"失败");*/
				massage.insert(0,errorName+"失败。");
			}
			String str = new String();
			if(coderror1.length()>0){
				/*massage.append(coderror1);*/
				massage.insert(0,coderror1);
			}
			if(massage.length()>0){
				str = massage.toString();
				result.put("errMsg",str);
				result.put("errCode", 1);
			}else{
				result.put("errMsg","成功导入："+success+"条。");
				result.put("errCode", 0);
			}
		}else{
			result.put("errCode", -1);
			result.put("errMsg", "请导入正确的员工信息模板");
		}
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

	/**
	 * 当前用户由多少条待处理数据
	 * @return
	 */
	@RequestMapping(params = "pendingData")
	@ResponseBody
	public int pendingData() {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSDepart depart = commonService.getEntity(TSDepart.class, user.getDepartid());
		Integer code = 0;
		if(employeeInfoService.getNameRoleCold(user)==3){
			//获得所有待入保，需退保员工
			code = employeeInfoRepo.checkerPand();
		}else if(employeeInfoService.getNameRoleCold(user)==4){
			//获得所有状态为4的员工
			code = employeeInfoRepo.controllerPand();
		}else {
			Integer lv=0;
			List<String> departs = new ArrayList<>();
			if(employeeInfoService.getNameRoleCold(user)==1){
				lv = commonRepository.getDepartGread(depart)*2+2;
				departs.add(depart.getId());
				code = employeeInfoRepo.inputerPand(lv, user);
			}else{
				lv = commonRepository.getDepartGread(depart)*2;
				List<TSDepart> myDeparts = commonRepository.findMyDeptInfo();
				for(TSDepart dep:myDeparts){
					departs.add(dep.getId());
				}
				code = employeeInfoRepo.otherPand(lv, departs);
			}
		}
		return code;
	}



	/**
	 * 员工信息表列表页面跳转
	 * input，input_m录入,查看
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(EmployeeInfoEntity employeeInfo, HttpServletRequest req) {
		Double b = Double.parseDouble(commonRepository.getSystemBasePay());
		req.setAttribute("basePay",b);
		Integer rolecode = employeeInfoService.getUserRoleCold();
		if(rolecode ==3 ) rolecode = 1;
		req.setAttribute("rolecode", rolecode);
		if (StringUtil.isNotEmpty(employeeInfo.getId())) {
			employeeInfo = commonService.getEntity(EmployeeInfoEntity.class, employeeInfo.getId());
			req.setAttribute("employeeInfoPage", employeeInfo);
			if(employeeInfo.getChangeFlag()!=null){
				return new ModelAndView("com/charge/employeeInfo2");
			}
		}
		return new ModelAndView("com/charge/employeeInfo");
	}

	/**
	 * 员工信息表列表页面跳转
	 * 查看
	 * @return
	 */
	@RequestMapping(params = "addorupdate2")
	public ModelAndView addorupdate2(EmployeeInfoEntity employeeInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(employeeInfo.getId())) {
			employeeInfo = commonService.getEntity(EmployeeInfoEntity.class, employeeInfo.getId());
			req.setAttribute("employeeInfoPage", employeeInfo);
		}
			return new ModelAndView("com/charge/employeeInfo2");
	}

	/**
	 * 员工信息表列表页面跳转
	 * controller编辑
	 * @return
	 */
	@RequestMapping(params = "editController")
	public ModelAndView editController(EmployeeInfoEntity employeeInfo, HttpServletRequest req) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		String inputName1 = ((TSBaseUser) commonService.getEntity(TSBaseUser.class, user.getId())).getRealName();
		String departid = ((TSBaseUser) commonService.getEntity(TSBaseUser.class, user.getId())).getDepartid();
		int departflag = 0;
		if(("2c902db662f164650162f1ce49760041").equals(departid)||("2c902db662f164650162f1ce6b5a0043").equals(departid)){//判断是否为控制部门和审计部门
			departflag = 1;
		}
		req.setAttribute("inputName1", inputName1);
		if (StringUtil.isNotEmpty(employeeInfo.getId())) {
			employeeInfo = commonService.getEntity(EmployeeInfoEntity.class, employeeInfo.getId());
			req.setAttribute("employeeInfoPage", employeeInfo);
			req.setAttribute("inputName", employeeInfoService.getIntputerName(employeeInfo));
		}
		Double b = Double.parseDouble(commonRepository.getSystemBasePay());
		req.setAttribute("basePay",b);
		return new ModelAndView("com/charge/employeeInfoControllerEdit");
		}

	/**
	 * 员工信息表列表页面跳转
	 * 查看，repoter修改，repoter录入
	 * @return
	 */
	@RequestMapping(params = "addReporter")
	public ModelAndView addReporter(EmployeeInfoEntity employeeInfo, HttpServletRequest req) {
		Double b =  Double.parseDouble(commonRepository.getSystemBasePay());
		req.setAttribute("basePay",b);
		if (StringUtil.isNotEmpty(employeeInfo.getId())) {
			employeeInfo = commonService.getEntity(EmployeeInfoEntity.class, employeeInfo.getId());
			req.setAttribute("employeeInfoPage", employeeInfo);
			req.setAttribute("inputName", employeeInfoService.getIntputerName(employeeInfo));
			if(employeeInfo.getChangeFlag()!=null){
				return new ModelAndView("com/charge/employeeInfoRepoterEdit");
			}
		}
		return new ModelAndView("com/charge/employeeInfoRepoterAdd");
	}

	/**
	 * 员工离职页面跳转
	 * 编辑，repoter，inputer
	 * @return
	 */
	@RequestMapping(params = "leave")
	public ModelAndView leave(EmployeeInfoEntity employeeInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(employeeInfo.getId())) {
			employeeInfo = commonService.getEntity(EmployeeInfoEntity.class, employeeInfo.getId());
			req.setAttribute("employeeInfoPage", employeeInfo);
		}
		return new ModelAndView("com/charge/dimissionEdit");
	}

	/**
	 * 员工所有信息查看跳转
	 * 编辑，repoter，inputer
	 * @return
	 */
	@RequestMapping(params = "detailAllInfo")
	public ModelAndView detailAllInfo(EmployeeInfoEntity employeeInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(employeeInfo.getId())) {
			employeeInfo = commonService.getEntity(EmployeeInfoEntity.class, employeeInfo.getId());
			req.setAttribute("employeeInfoPage", employeeInfo);
		}
		return new ModelAndView("com/charge/employeeInfoDetailAll");
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<EmployeeInfoEntity> list() {
		List<EmployeeInfoEntity> listEmployeeInfos=commonService.getList(EmployeeInfoEntity.class);
		return listEmployeeInfos;
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		EmployeeInfoEntity task = commonService.get(EmployeeInfoEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody EmployeeInfoEntity employeeInfo, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<EmployeeInfoEntity>> failures = validator.validate(employeeInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.save(employeeInfo);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		Integer id = employeeInfo.getId();
		URI uri = uriBuilder.path("/rest/employeeInfoController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody EmployeeInfoEntity employeeInfo) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<EmployeeInfoEntity>> failures = validator.validate(employeeInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.saveOrUpdate(employeeInfo);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		commonService.deleteEntityById(EmployeeInfoEntity.class, id);
	}
}
