package com.charge.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.charge.entity.CustomerInfoEntity;
import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.ProjectCopyEntity;
import com.charge.entity.ProjectEntity;
import com.charge.repository.CommonRepository;
import com.charge.repository.CustomerInfoRepository;
import com.charge.repository.EmployeeDeclareRepository;
import com.charge.repository.SixGoldScaleRepository;
import com.charge.service.DictCategoryService;
import com.charge.service.EmployeeDeclareCopyService;
import com.charge.service.EmployeeDeclareService;
import com.charge.service.EmployeeInfoService;
import com.charge.service.ProjectService;

/**
 * @Title: Controller
 * @Description: 项目收支
 * @author wenst
 * @date 2018-03-19 16:45:06
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/projectController")
public class projectController extends BaseController {

	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(ProjectEntity.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EmployeeInfoService employeeInfoService;

	@Autowired
	private EmployeeDeclareService employeeDeclareService;

	@Autowired
	private CustomerInfoRepository customerInfoRepo;

	@Autowired
	private ProjectService projectService;
	@Autowired
	private CommonRepository commonRepository;
	@Autowired
	private EmployeeDeclareCopyService employeeDeclareCopyService;

	@RequestMapping(params = "clearMonth")
	public void clearMonth() {
		this.timeCondition2=null;
		this.timeCondition3=null;
	}

	private Date timeCondition2;
	/*
	 * 项目流程页面信息专用
	 * **/
	@RequestMapping(params = "projectFindbyMonth2")
	public void projectFindbyMonth2(ProjectEntity project,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String month;
		if(request.getParameter("month")!=null&&!"".equals(request.getParameter("month"))) {
			month = request.getParameter("month");
		}else {
			Date date = timeCondition2==null?new Date():timeCondition2;
			month = sdf.format(date);
		}
		try {
			Date date = sdf.parse(month);
			project.setProjectMonth(date);
			timeCondition2=date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataGrid.setField("id,projectDate,projectDepartment,projectCustomer1,projectCustomer2,projectConstant,"
				+ "projectIncome,projectPay,projectProfit,projectProfitRate,projectStatus,projectReturnreason,inputerId,"
				+ "reporterId,checkerId,controllerId,backUpFlag,");
		//排序
		if("inputName".equals(dataGrid.getSort())) {
			dataGrid.setSort("inputerId");
		}
		TSUser myUser = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSDepart depart = commonService.getEntity(TSDepart.class, myUser.getDepartid());
	    Integer initStatus = commonRepository.getDepartGread(depart)*2;
		//查询条件为 申报状态
	    	if(project.getProjectStatus()!=null) {
	    		int selectStatus = project.getProjectStatus();
	    		if(selectStatus == 1){ //查询审批通过
	    			projectService.setDataGrid(project, request.getParameterMap(), dataGrid, null, 0,0);
	    		}else if(selectStatus==initStatus+1) { //查询 未上报
	    			project.setProjectStatus(null);
	    			projectService.setDataGrid(project, request.getParameterMap(), dataGrid, new Integer[]{selectStatus}, 0,2);
	    		}else if(selectStatus==initStatus-2){//查询已上报
	    			project.setProjectStatus(null);
	    			projectService.setDataGrid(project, request.getParameterMap(), dataGrid, new Integer[]{selectStatus}, 0,1);
	    		}else { //正常
	    			projectService.setDataGrid(project, request.getParameterMap(), dataGrid, null, 0,0);
	    		}
	    	}else {
	    		projectService.setDataGrid(project, request.getParameterMap(), dataGrid, null, 0,0);
	    	}

        Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
        List<ProjectEntity> projectEntitys = dataGrid.getResults();
        for(ProjectEntity temp: projectEntitys){
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
        int SUM = projectService.getUserRoleCold();
        if(SUM!=1){
        	for(ProjectEntity temp: projectEntitys){
            	if(temp.getProjectStatus()>initStatus) { // 未上报
            		temp.setProjectStatus(initStatus+1);
            	}else if(temp.getProjectStatus()<initStatus-1&&temp.getProjectStatus()!=1) { //已上报
            		temp.setProjectStatus(initStatus-2);
            	}
           }
        }else{
        	for(ProjectEntity temp: projectEntitys){
            	if(temp.getProjectStatus()>initStatus+2) { // 未上报
            		temp.setProjectStatus(initStatus+3);
            	}else if(temp.getProjectStatus()<initStatus-1+2&&temp.getProjectStatus()!=1) { //已上报
            		temp.setProjectStatus(initStatus);
            	}
           }
        }

       dataGrid.setResults(projectEntitys);
       TagUtil.datagrid(response, dataGrid,extMap);
	}
	private Date timeCondition3;
	/*
	 * 项目访客页面信息专用
	 * **/
	@RequestMapping(params = "projectFindbyMonth3")
	public void projectFindbyMonth3(ProjectCopyEntity projectCopy,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String month;
		if(request.getParameter("month")!=null&&!"".equals(request.getParameter("month"))) {
			month = request.getParameter("month");
		}else {
			Date date = timeCondition3==null?new Date():timeCondition3;
			month = sdf.format(date);
		}
		try {
			Date date = sdf.parse(month);
			projectCopy.setProjectMonth(date);
			timeCondition3=date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataGrid.setField("id,projectDate,projectDepartment,projectCustomer1,projectCustomer2,projectConstant,"
				+ "projectIncome,projectPay,projectProfit,projectProfitRate,projectStatus,projectReturnreason,inputerId,"
				+ "reporterId,checkerId,controllerId,batId,");
		//排序
		if("inputName".equals(dataGrid.getSort())) {
			dataGrid.setSort("inputerId");
		}

		projectService.setDataGridforAccess(projectCopy, request.getParameterMap(), dataGrid);
		Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
		List<ProjectCopyEntity> projectCopyEntitys = dataGrid.getResults();
		for(ProjectCopyEntity temp: projectCopyEntitys){
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
		dataGrid.setResults(projectCopyEntitys);
		TagUtil.datagrid(response, dataGrid,extMap);
	}



	/**
	 *
	 * 项目录入列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "projectInputerList")
	public ModelAndView projectInputerList(HttpServletRequest request) {

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
		List<CustomerInfoEntity> custlist = customerInfoRepo.findAllCustomer();
		String customers ="";
		for(int i=0;i<custlist.size();i++) {
			CustomerInfoEntity obj = custlist.get(i);
			customers+=obj.getCode()+"_"+obj.getId();
			if(i<custlist.size()-1) {
				customers+=",";
			}
		}
		request.setAttribute("customers", customers);
		//点击即看过  批量修改 收支系统消息 待处理为已处理
//			TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
//			String sql = "update c_aa_test set message_declare=1 where message_type=1 and message_to=?";
//			systemService.executeSql(sql, user.getUserName());

		return new ModelAndView("com/charge/projectInputerList");

	}
	/**
	 *
	 * 项目申报列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {

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
			List<CustomerInfoEntity> custlist = customerInfoRepo.findAllCustomer();
			String customers ="";
			for(int i=0;i<custlist.size();i++) {
				CustomerInfoEntity obj = custlist.get(i);
				customers+=obj.getCode()+"_"+obj.getId();
				if(i<custlist.size()-1) {
					customers+=",";
				}
			}
			request.setAttribute("customers", customers);
			//点击即看过  批量修改 收支系统消息 待处理为已处理
//			TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
//			String sql = "update c_aa_test set message_declare=1 where message_type=1 and message_to=?";
//			systemService.executeSql(sql, user.getUserName());

			return new ModelAndView("com/charge/projectList");

	}
	/**
	 *
	 * 项目审核列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "projectCheckerList")
	public ModelAndView projectCheckerList(HttpServletRequest request) {

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
		List<CustomerInfoEntity> custlist = customerInfoRepo.findAllCustomer();
		String customers ="";
		for(int i=0;i<custlist.size();i++) {
			CustomerInfoEntity obj = custlist.get(i);
			customers+=obj.getCode()+"_"+obj.getId();
			if(i<custlist.size()-1) {
				customers+=",";
			}
		}
		request.setAttribute("customers", customers);
		//点击即看过  批量修改 收支系统消息 待处理为已处理
//			TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
//			String sql = "update c_aa_test set message_declare=1 where message_type=1 and message_to=?";
//			systemService.executeSql(sql, user.getUserName());

		return new ModelAndView("com/charge/projectCheckerList");

	}
	/**
	 *
	 * 项目审批列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "projectControllerList")
	public ModelAndView projectControllerList(HttpServletRequest request) {

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
		List<CustomerInfoEntity> custlist = customerInfoRepo.findAllCustomer();
		String customers ="";
		for(int i=0;i<custlist.size();i++) {
			CustomerInfoEntity obj = custlist.get(i);
			customers+=obj.getCode()+"_"+obj.getId();
			if(i<custlist.size()-1) {
				customers+=",";
			}
		}
		request.setAttribute("customers", customers);
		//点击即看过  批量修改 收支系统消息 待处理为已处理
//			TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
//			String sql = "update c_aa_test set message_declare=1 where message_type=1 and message_to=?";
//			systemService.executeSql(sql, user.getUserName());

		return new ModelAndView("com/charge/projectControllerList");

	}
	/**
	 *
	 * 项目访客列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "projectAccessList")
	public ModelAndView projectAccessList(HttpServletRequest request) {

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
		List<CustomerInfoEntity> custlist = customerInfoRepo.findAllCustomer();
		String customers ="";
		for(int i=0;i<custlist.size();i++) {
			CustomerInfoEntity obj = custlist.get(i);
			customers+=obj.getCode()+"_"+obj.getId();
			if(i<custlist.size()-1) {
				customers+=",";
			}
		}
		request.setAttribute("customers", customers);
		//点击即看过  批量修改 收支系统消息 待处理为已处理
//			TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
//			String sql = "update c_aa_test set message_declare=1 where message_type=1 and message_to=?";
//			systemService.executeSql(sql, user.getUserName());

		return new ModelAndView("com/charge/projectAccessList");

	}

	/**
	 * 项目信息表列表页面跳转
	 * 查看，repoter修改，repoter录入
	 * @return
	 */
	@RequestMapping(params = "addReporter")
	public ModelAndView addReporter(ProjectEntity projectEntity, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(projectEntity.getId())) {
			projectEntity = commonService.getEntity(ProjectEntity.class, projectEntity.getId());
			req.setAttribute("projectPage", projectEntity);
//			req.setAttribute("inputName", ProjectService.getIntputerName(projectEntity));
//			if(projectEntity.getChangeFlag()!=null){
//				return new ModelAndView("com/charge/employeeInfoRepoterEdit");
//			}
		}
		String systemTurnoverTax = commonRepository.getSystemTurnoverTax();
		req.setAttribute("turnoverTax",systemTurnoverTax);
		return new ModelAndView("com/charge/project");
	}

	/**
	 * 项目信息表列表页面跳转 -查看专用
	 *
	 * @return
	 */
	@RequestMapping(params = "addReporterLook")
	public ModelAndView addReporterLook(ProjectEntity projectEntity, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(projectEntity.getId())) {
			projectEntity = commonService.getEntity(ProjectEntity.class, projectEntity.getId());
			req.setAttribute("projectPage", projectEntity);
//			req.setAttribute("inputName", ProjectService.getIntputerName(projectEntity));
//			if(projectEntity.getChangeFlag()!=null){
//				return new ModelAndView("com/charge/employeeInfoRepoterEdit");
//			}
		}
		String systemTurnoverTax = commonRepository.getSystemTurnoverTax();
		req.setAttribute("turnoverTax",systemTurnoverTax);
		return new ModelAndView("com/charge/projectLook");
	}

	/**
	 * 项目信息表列表页面跳转 -访客专用
	 * 查看，repoter修改，repoter录入
	 * @return
	 */
	@RequestMapping(params = "addReporterforAccess")
	public ModelAndView addReporterforAccess(ProjectCopyEntity projectCopyEntity, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(projectCopyEntity.getId())) {
			projectCopyEntity = commonService.getEntity(ProjectCopyEntity.class, projectCopyEntity.getId());
			req.setAttribute("projectPage", projectCopyEntity);
//			req.setAttribute("inputName", ProjectService.getIntputerName(projectEntity));
//			if(projectEntity.getChangeFlag()!=null){
//				return new ModelAndView("com/charge/employeeInfoRepoterEdit");
//			}
		}
		String systemTurnoverTax = commonRepository.getSystemTurnoverTax();
		req.setAttribute("turnoverTax",systemTurnoverTax);
		return new ModelAndView("com/charge/projectLook");
	}

	/**
	 * 因为表单传来的值有的是""有的是null，而都要进行Double转换 所以特别写个方法
	 * @author gc
	 */
	private Double getDouble(String parameter,HttpServletRequest request) {
		if("".equals(request.getParameter(parameter))||request.getParameter(parameter)==null) {
			return 0.0;
		}
		return Double.parseDouble(request.getParameter(parameter));
	}

	/**
	 * 项目信息更改判断
	 * j 6-7
	 * @return 0-未更新  1-更新
	 */
	public Integer saveReason(ProjectEntity projectEntity, ProjectEntity t){
		Integer i = 0;
		StringBuffer reason =new StringBuffer("申请理由：");
		/*if (!DateUtils.isSameDay(t.getEntryDate(),employeeInfo.getEntryDate())) {
			//入职日
			i=1;
			reason.append("入职日,");
		}*/

		if (!t.getProjectDate().equals(projectEntity.getProjectDate())&&projectEntity.getProjectDate()!=null) {
			//立项日期
			i=1;
			reason.append("立项日期,");
		}
		if (!t.getProjectCustomer1().equals(projectEntity.getProjectCustomer1())&&projectEntity.getProjectCustomer1()!=null) {
			//上游客户
			i=1;
			reason.append("上游客户,");
		}
//		if (!t.getProjectCustomer2().equals(projectEntity.getProjectCustomer2())&&projectEntity.getProjectCustomer2()!=null) {
//			//下游顾客
//			i=1;
//			reason.append("下游顾客,");
//		}
		if (!t.getProjectIncome().equals(projectEntity.getProjectIncome())&&projectEntity.getProjectIncome()!=null) {
			//项目收入
			i=1;
			reason.append("项目收入,");
		}
//		if (!t.getProjectPay().equals(projectEntity.getProjectPay())&&projectEntity.getProjectPay()!=null) {
//			//项目支出
//			i=1;
//			reason.append("项目支出,");
//		}
		if (!t.getC1Other().equals(projectEntity.getC1Other())&&projectEntity.getC1Other()!=null) {
			//c1类型
			i=1;
			reason.append("c1类型,");
		}
		if (!(t.getC1OtherSupplier()==null?"a":t.getC1OtherSupplier()).equals(projectEntity.getC1OtherSupplier()==null?"a":projectEntity.getC1OtherSupplier())) {
			//c1供应商
			i=1;
			reason.append("c1供应商,");
		}
		if (!(t.getC1OtherRemarks()==null?"a":t.getC1OtherRemarks()).equals(projectEntity.getC1OtherRemarks()==null?"a":projectEntity.getC1OtherRemarks())) {
			//c1备注
			i=1;
			reason.append("c1备注,");
		}
		if (!(t.getC1OtherMoney()==null?"a":t.getC1OtherMoney()).equals(projectEntity.getC1OtherMoney()==null?"a":projectEntity.getC1OtherMoney())) {
			//c1金额
			i=1;
			reason.append("c1金额,");
		}
		if (!t.getC2Other().equals(projectEntity.getC2Other())&&projectEntity.getC2Other()!=null) {
			//c2类型
			i=1;
			reason.append("c2类型,");
		}
		if (!(t.getC2OtherSupplier()==null?"a":t.getC2OtherSupplier()).equals(projectEntity.getC2OtherSupplier()==null?"a":projectEntity.getC2OtherSupplier())) {
			//c2供应商
			i=1;
			reason.append("c2供应商,");
		}
		if (!(t.getC2OtherRemarks()==null?"a":t.getC2OtherRemarks()).equals(projectEntity.getC2OtherRemarks()==null?"a":projectEntity.getC2OtherRemarks())) {
			//c2备注
			i=1;
			reason.append("c2备注,");
		}
		if (!(t.getC2OtherMoney()==null?"a":t.getC2OtherMoney()).equals(projectEntity.getC2OtherMoney()==null?"a":projectEntity.getC2OtherMoney())) {
			//c2金额
			i=1;
			reason.append("c2金额,");
		}
		if (!t.getC3Other().equals(projectEntity.getC3Other())&&projectEntity.getC3Other()!=null) {
			//c3类型
			i=1;
			reason.append("c3类型,");
		}
		if (!(t.getC3OtherSupplier()==null?"a":t.getC3OtherSupplier()).equals(projectEntity.getC3OtherSupplier()==null?"a":projectEntity.getC3OtherSupplier())) {
			//c3供应商
			i=1;
			reason.append("c3供应商,");
		}
		if (!(t.getC3OtherRemarks()==null?"a":t.getC3OtherRemarks()).equals(projectEntity.getC3OtherRemarks()==null?"a":projectEntity.getC3OtherRemarks())) {
			//c3备注
			i=1;
			reason.append("c3备注,");
		}
		if (!(t.getC3OtherMoney()==null?"a":t.getC3OtherMoney()).equals(projectEntity.getC3OtherMoney()==null?"a":projectEntity.getC3OtherMoney())) {
			//c3金额
			i=1;
			reason.append("c3金额,");
		}
		if (!t.getC4Other().equals(projectEntity.getC4Other())&&projectEntity.getC4Other()!=null) {
			//c4类型
			i=1;
			reason.append("c4类型,");
		}
		if (!(t.getC4OtherSupplier()==null?"a":t.getC4OtherSupplier()).equals(projectEntity.getC4OtherSupplier()==null?"a":projectEntity.getC4OtherSupplier())) {
			//c4供应商
			i=1;
			reason.append("c4供应商,");
		}
		if (!(t.getC4OtherRemarks()==null?"a":t.getC4OtherRemarks()).equals(projectEntity.getC4OtherRemarks()==null?"a":projectEntity.getC4OtherRemarks())) {
			//c4备注
			i=1;
			reason.append("c4备注,");
		}
		if (!(t.getC4OtherMoney()==null?"a":t.getC4OtherMoney()).equals(projectEntity.getC4OtherMoney()==null?"a":projectEntity.getC4OtherMoney())) {
			//c4金额
			i=1;
			reason.append("c4金额,");
		}
		if (!t.getC5Other().equals(projectEntity.getC5Other())&&projectEntity.getC5Other()!=null) {
			//c5类型
			i=1;
			reason.append("c5类型,");
		}
		if (!(t.getC5OtherSupplier()==null?"a":t.getC5OtherSupplier()).equals(projectEntity.getC5OtherSupplier()==null?"a":projectEntity.getC5OtherSupplier())) {
			//c5供应商
			i=1;
			reason.append("c5供应商,");
		}
		if (!(t.getC5OtherRemarks()==null?"a":t.getC5OtherRemarks()).equals(projectEntity.getC5OtherRemarks()==null?"a":projectEntity.getC5OtherRemarks())) {
			//c5备注
			i=1;
			reason.append("c5备注,");
		}
		if (!(t.getC5OtherMoney()==null?"a":t.getC5OtherMoney()).equals(projectEntity.getC5OtherMoney()==null?"a":projectEntity.getC5OtherMoney())) {
			//c5金额
			i=1;
			reason.append("c5金额,");
		}
		if (!(t.getIsturnovertax()==null?"a":t.getIsturnovertax()).equals(projectEntity.getIsturnovertax()==null?"a":projectEntity.getIsturnovertax())) {
			//流转税
			i=1;
			reason.append("流转税,");
		}
		if(i==1){
			reason.append("发生改变");
		}
		return i;
	}

	/**
	 * 添加项目信息表
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ProjectEntity projectEntity, HttpServletRequest request) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		//流转税率
		Double turnoverTax = Double.parseDouble(commonRepository.getSystemTurnoverTax());
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
//		if("A07A03".equals(orgCodeStr) && employeeInfo.getEmployeeFlag() == null){
//			employeeInfo.setEmployeeFlag(1);
//			//如果是用户业务部门的,并且前端并未传员工类型过来(代表不是业务部门总监登陆系统)
//		}else if("A07A04".equals(orgCodeStr) && employeeInfo.getEmployeeFlag() == null){
//			employeeInfo.setEmployeeFlag(0);
//		}
		List<String> orgIdList = extractIdListByComma(orgIds);
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(projectEntity.getId())) {
			message = "项目信息表更新成功";
			ProjectEntity t = commonService.get(ProjectEntity.class, projectEntity.getId());
//			if(t.getEditors().equals(projectEntity.getEditors())){
			try {
				Integer lookUp = saveReason(projectEntity,t);
				Integer oldDeclareStatus = t.getProjectStatus();
//				Integer newDeclareStatus = employeeInfoService.getStatus(user);
				Integer newDeclareStatus = projectService.getStatus(user);
				MyBeanUtils.copyBeanNotNull2Bean(projectEntity, t);
//				if(!StringUtil.isNotEmpty(projectEntity.getChangeDate())){
//					t.setChangeDate(null);t.setAStandardSalaryCh(null);t.setSixGoldBaseCh(null);t.setA1PaymentCh(null);t.setA2PaymentCh(null);
//					t.setSixGoldCityCh(null);t.setA1PlaceCh(null);t.setA2PlaceCh(null);
//				}
				//设置上游客户、c、收入、支出、毛利、毛利率
				String projectCustomer1 = t.getProjectCustomer1();
//				String projectCustomer2 = t.getProjectCustomer2();

				Double projectIncome = t.getProjectIncome();//收
				Double projectPay = 0.0;//总支出

				if(projectEntity.getC1Other()==0){
					t.setC1Other(projectEntity.getC1Other());
					projectPay+=projectEntity.getC1OtherMoney();
					t.setC1OtherRemarks(projectEntity.getC1OtherRemarks());
					t.setC1OtherSupplier(null);
				}
				if(projectEntity.getC1Other()==1){
					t.setC1Other(projectEntity.getC1Other());
					projectPay+=projectEntity.getC1OtherMoney();
					t.setC1OtherSupplier(projectEntity.getC1OtherSupplier());
					t.setC1OtherRemarks(null);
				}
				if(projectEntity.getC2Other()==0){
					t.setC2Other(projectEntity.getC2Other());
					projectPay+=projectEntity.getC2OtherMoney();
					t.setC2OtherRemarks(projectEntity.getC2OtherRemarks());
					t.setC2OtherSupplier(null);
				}
				if(projectEntity.getC2Other()==1){
					t.setC2Other(projectEntity.getC2Other());
					projectPay+=projectEntity.getC2OtherMoney();
					t.setC2OtherSupplier(projectEntity.getC2OtherSupplier());
					t.setC2OtherRemarks(null);
				}
				if(projectEntity.getC3Other()==0){
					t.setC3Other(projectEntity.getC3Other());
					projectPay+=projectEntity.getC3OtherMoney();
					t.setC3OtherRemarks(projectEntity.getC3OtherRemarks());
					t.setC3OtherSupplier(null);
				}
				if(projectEntity.getC3Other()==1){
					t.setC3Other(projectEntity.getC3Other());
					projectPay+=projectEntity.getC3OtherMoney();
					t.setC3OtherSupplier(projectEntity.getC3OtherSupplier());
					t.setC3OtherRemarks(null);
				}
				if(projectEntity.getC4Other()==0){
					projectEntity.setC4Other(projectEntity.getC4Other());
					projectPay+=projectEntity.getC4OtherMoney();
					projectEntity.setC4OtherRemarks(projectEntity.getC4OtherRemarks());
					t.setC4OtherSupplier(null);
				}
				if(projectEntity.getC4Other()==1){
					t.setC4Other(projectEntity.getC4Other());
					projectPay+=projectEntity.getC4OtherMoney();
					t.setC4OtherSupplier(projectEntity.getC4OtherSupplier());
					t.setC4OtherRemarks(null);
				}
				if(projectEntity.getC5Other()==0){
					t.setC5Other(projectEntity.getC5Other());
					projectPay+=projectEntity.getC5OtherMoney();
					t.setC5OtherRemarks(projectEntity.getC5OtherRemarks());
					t.setC5OtherSupplier(null);
				}
				if(projectEntity.getC5Other()==1){
					t.setC5Other(projectEntity.getC5Other());
					projectPay+=projectEntity.getC5OtherMoney();
					t.setC5OtherSupplier(projectEntity.getC5OtherSupplier());
					t.setC5OtherRemarks(null);
				}
				Double projectProfit;//毛利
				Double projectProfitRate;//毛利率
				Double projectPay2;//净利润
				if(projectEntity.getIsturnovertax()==0){//无流转税
					projectProfit = projectIncome-projectPay;//毛利
					projectProfitRate = (projectProfit==0?0:projectProfit*100/projectIncome);//毛利率
				}else{//有流转税
					projectProfit = projectIncome-projectPay;//毛利
					projectPay2 = projectProfit*(1-turnoverTax/100.0);//净利润
					projectProfitRate = (projectPay2==0?0:projectPay2*100/projectIncome);//毛利率

				}

				t.setIsturnovertax(projectEntity.getIsturnovertax());
				t.setProjectCustomer1(projectCustomer1);
//				t.setProjectCustomer2(projectCustomer2);
				t.setProjectIncome(projectIncome);
				t.setProjectPay(projectPay);
				t.setProjectProfit(projectProfit);
				t.setProjectProfitRate(projectProfitRate);
				//导入员工设置为入职成功，部门经理修改下级无法看到，其他修改都为初始未申报状态
				/*if(projectEntity.getDeclareStatus()==100){
					t.setDeclareStatus(2);
				}else*/
				if (lookUp == 0) {
					t.setProjectStatus(oldDeclareStatus);
				}else {
					t.setProjectStatus(newDeclareStatus);
				}
				t.setDelFlage(0);
				if(null!=user){
					t.setLastModifiedBy(user.getUserName());
				}
				t.setLastModifiedDate(new Date());
				t.setEditors(UUID.randomUUID().toString().replace("-", "").toLowerCase());
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "项目信息表更新失败";
			}
//			}
//			else{
//				message = "您所编辑的项目信息已发生改变，项目信息更新失败";
//			}
		} else {
			message = "项目信息表添加成功";
			//对是否是同一部门进行判断
			if(!orgIdList.isEmpty()){
				if(org.springframework.util.StringUtils.isEmpty(projectEntity.getProjectDepartment())){
					projectEntity.setProjectDepartment(depart.getId());
				}
			}
			//如果没有传入inputername，则设置为自己，如果为导入，则设置为空
			if(!StringUtils.isNotEmpty(projectEntity.getInputerId())){
				projectEntity.setInputerId(user.getUserName());
			}
			//设置上游客户、c、收入、支出、毛利、毛利率
			String projectCustomer1 = projectEntity.getProjectCustomer1();
			String projectCustomer2 = projectEntity.getProjectCustomer2();

			Double projectIncome = projectEntity.getProjectIncome();//收
			Double projectPay = 0.0;//总支出
			if(projectEntity.getC1Other()==0){
				projectEntity.setC1Other(projectEntity.getC1Other());
				projectPay+=projectEntity.getC1OtherMoney();
				projectEntity.setC1OtherRemarks(projectEntity.getC1OtherRemarks());
			}
			if(projectEntity.getC1Other()==1){
				projectEntity.setC1Other(projectEntity.getC1Other());
				projectPay+=projectEntity.getC1OtherMoney();
				projectEntity.setC1OtherSupplier(projectEntity.getC1OtherSupplier());
			}
			if(projectEntity.getC2Other()==0){
				projectEntity.setC2Other(projectEntity.getC2Other());
				projectPay+=projectEntity.getC2OtherMoney();
				projectEntity.setC2OtherRemarks(projectEntity.getC2OtherRemarks());
			}
			if(projectEntity.getC2Other()==1){
				projectEntity.setC2Other(projectEntity.getC2Other());
				projectPay+=projectEntity.getC2OtherMoney();
				projectEntity.setC2OtherSupplier(projectEntity.getC2OtherSupplier());
			}
			if(projectEntity.getC3Other()==0){
				projectEntity.setC3Other(projectEntity.getC3Other());
				projectPay+=projectEntity.getC3OtherMoney();
				projectEntity.setC3OtherRemarks(projectEntity.getC3OtherRemarks());
			}
			if(projectEntity.getC3Other()==1){
				projectEntity.setC3Other(projectEntity.getC3Other());
				projectPay+=projectEntity.getC3OtherMoney();
				projectEntity.setC3OtherSupplier(projectEntity.getC3OtherSupplier());
			}
			if(projectEntity.getC4Other()==0){
				projectEntity.setC4Other(projectEntity.getC4Other());
				projectPay+=projectEntity.getC4OtherMoney();
				projectEntity.setC4OtherRemarks(projectEntity.getC4OtherRemarks());
			}
			if(projectEntity.getC4Other()==1){
				projectEntity.setC4Other(projectEntity.getC4Other());
				projectPay+=projectEntity.getC4OtherMoney();
				projectEntity.setC4OtherSupplier(projectEntity.getC4OtherSupplier());
			}
			if(projectEntity.getC5Other()==0){
				projectEntity.setC5Other(projectEntity.getC5Other());
				projectPay+=projectEntity.getC5OtherMoney();
				projectEntity.setC5OtherRemarks(projectEntity.getC5OtherRemarks());
			}
			if(projectEntity.getC5Other()==1){
				projectEntity.setC5Other(projectEntity.getC5Other());
				projectPay+=projectEntity.getC5OtherMoney();
				projectEntity.setC5OtherSupplier(projectEntity.getC5OtherSupplier());
			}


			Double projectProfit;//毛利
			Double projectProfitRate;//毛利率
			Double projectPay2;//净利润
			if(projectEntity.getIsturnovertax()==0){//无流转税
				projectProfit = projectIncome-projectPay;//毛利
				projectProfitRate = (projectProfit==0?0:projectProfit*100/projectIncome);//毛利率
			}else{//有流转税
				projectProfit = projectIncome-projectPay;//毛利
				projectPay2 = projectProfit*(1-turnoverTax/100.0);//净利润
				projectProfitRate = (projectPay2==0?0:projectPay2*100/projectIncome);//毛利率

			}


			projectEntity.setProjectCustomer1(projectCustomer1);
//			projectEntity.setProjectCustomer2(projectCustomer2);
			projectEntity.setProjectIncome(projectIncome);
			projectEntity.setProjectPay(projectPay);
			projectEntity.setProjectProfit(projectProfit);
			projectEntity.setProjectProfitRate(projectProfitRate);

//			projectEntity.setProjectDate(new Date());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String month;
			if(request.getParameter("month")!=null&&!"".equals(request.getParameter("month"))) {
				month = request.getParameter("month");
			}else {
				Date date = timeCondition2==null?new Date():timeCondition2;
				month = sdf.format(date);
			}

//			 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//			 String dateString = formatter.format(new Date());
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//			String month = dateString;
				Date date;
				try {
					date = sdf.parse(month);
					projectEntity.setProjectMonth(date);
					projectEntity.setProjectDate(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			projectEntity.setDelFlage(0);
			if(null!=user){
				projectEntity.setCreatedBy(user.getUserName());
				projectEntity.setLastModifiedBy(user.getUserName());

			}
			/*//新增权限判断设置setDeclareStatus
			projectEntity.setProjectStatus(employeeInfoService.getStatus(user));*/
			//新增权限判断设置setDeclareStatus
			if(projectEntity.getProjectStatus()!=null&&projectEntity.getProjectStatus()==100){
				projectEntity.setProjectStatus(2);
			}else{
				projectEntity.setProjectStatus(projectService.getStatus(user));
			}
			projectEntity.setEditors(UUID.randomUUID().toString().replace("-", "").toLowerCase());
			projectEntity.setCreatedDate(new Date());
			projectEntity.setLastModifiedDate(new Date());
			projectEntity.setBackUpFlag(0);
			commonService.save(projectEntity);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量还原
	 * 批量还原 选中的“申报中”数据
	 * @param  String ids
	 * @return
	 */
	@RequestMapping(params="projectRestore")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String,Object> projectRestore(@RequestParam("ids") String ids){
		//切割 传来的 员工申报记录  id
		String[] sids = ids.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		Map<String, Object> result = employeeDeclareCopyService.projectsRestore(lids);
		return result;
	}


	/**
	 * 批量通过-选中-全部
	 * j 5-8
	 * @return
	 * @param id
	 */
	@RequestMapping(params="batchPass")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String,Object> batchPass(@RequestParam("id") String id){
		Map<String, Object> result = new HashMap<String,Object>();
		if(id==null) { //如果 未选中 则 默认 全选
			List<Integer> lids = null;
			result = projectService.declareAndPass(lids);
		}else{
		//切割 传来的 员工申报记录  id
		String[] sids = id.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		result = projectService.declareAndPass(lids);
		}
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
		result = projectService.newBatchReturn(lids, returnReason);
		}
		return result;
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
		message = "项目信息删除失败";
		if(id.equals(null)) {
			//如果 未选中 则 默认 全选
			//List<Integer> lids = null;
			message = "未选中数据";
		}else{
		//切割 传来的 员工申报记录  id
		String[] sids = id.split(",");
		for(String sid : sids) {
			ProjectEntity project = commonService.getEntity(ProjectEntity.class,Integer.parseInt(sid));
			if ((!project.getProjectStatus().equals(1)||!project.getProjectStatus().equals(2)||!((project.getProjectStatus().equals(4)||!project.getProjectStatus().equals(3))&&2==employeeInfoService.getUserRoleCold()))) {
				commonService.deleteEntityById(ProjectEntity.class,Integer.parseInt(sid));
				message = "项目信息删除成功";
			}else{
				name.append(project.getId()+"，");
			}
			}
		log.info(message+",id="+id);
		}
		if(0<name.length()){
			//message = "ID为"+name.substring(0,name.length()-1)+"的项目信息不可删除";
			message = "该项目信息不可删除";
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
	/**
	 * 导出 Excel -根据查询条件
	 * @param request
	 * @param response
	 */
	@RequestMapping(params="excelExport")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response){
		String month = request.getParameter("month");
		String department = request.getParameter("employeeDepartment");
		String customerName = request.getParameter("customerName");
		String declareStatus = request.getParameter("declareStatus");

		Date date = new Date();
		ProjectEntity project = new ProjectEntity();
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date =sdf.parse(month);
				project.setProjectMonth(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(department==null||"".equals(department)||"undefined".equals(department)) {
		} else {
			project.setProjectDepartment(department);
		}
		if(customerName==null||"".equals(customerName)||"undefined".equals(customerName)) {
		} else {
			project.setProjectCustomer1(customerName);
		}

		Map<String,Object> result = null;
		DataGrid dataGrid = new DataGrid();
		String isAll = request.getParameter("isAll");
        if("0".equals(isAll)) {  // 部门总收支
        	result = projectService.exportDepartData(request, declareStatus, project, dataGrid,date);
        } else if("1".equals(isAll)){   // 人力收支
        	//前台禁止
        }else if("2".equals(isAll)) {  // 项目 收支总计导出
        	projectService.setDataGridByStatus(request, declareStatus, project, dataGrid);
        	List<ProjectEntity> list= dataGrid.getResults();
    		result=projectService.exportProjectData(list,date);
        }else { //缺省 项目收支导出
        	projectService.setDataGridByStatus(request, declareStatus, project, dataGrid);
        	List<ProjectEntity> list= dataGrid.getResults();
    		result=projectService.exportProjectData(list,date);
        }



		ServletOutputStream out = null;
		try {
			String filePath =(String) result.get("fileName");
			response.setContentType("application/msexcel");
			response.addHeader("Content-Disposition", "attachment;filename="
					+new String(filePath.getBytes("utf-8"),"ISO_8859_1"));
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
	 * 导出 Excel -根据查询条件
	 * @param request
	 * @param response
	 */
	@RequestMapping(params="excelExportforAccess")
	public void excelExportforAccess(HttpServletRequest request,HttpServletResponse response){
		String month = request.getParameter("month");
		String department = request.getParameter("employeeDepartment");
		String customerName = request.getParameter("customerName");

		Date date = new Date();
		ProjectCopyEntity project = new ProjectCopyEntity();
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date =sdf.parse(month);
				project.setProjectMonth(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(department==null||"".equals(department)||"undefined".equals(department)) {
		} else {
			project.setProjectDepartment(department);
		}
		if(customerName==null||"".equals(customerName)||"undefined".equals(customerName)) {
		} else {
			project.setProjectCustomer1(customerName);
		}

		Map<String,Object> result = null;
		DataGrid dataGrid = new DataGrid();
		String isAll = request.getParameter("isAll");
        if("0".equals(isAll)) {  // 部门总收支
        	result = projectService.exportDepartDataforAccess(request,project, dataGrid,date);
        } else if("1".equals(isAll)){   // 人力收支
        	//前台禁止
        }else if("2".equals(isAll)) {  // 项目 收支总计导出
        	projectService.setDataGridforAccess(project, request.getParameterMap(),dataGrid);
        	List<ProjectCopyEntity> list= dataGrid.getResults();
    		result=projectService.exportProjectCopyData(list,date);
        }else { //缺省 项目收支导出
        	projectService.setDataGridforAccess(project, request.getParameterMap(),dataGrid);
        	List<ProjectCopyEntity> list= dataGrid.getResults();
    		result=projectService.exportProjectCopyData(list,date);
        }



		ServletOutputStream out = null;
		try {
			String filePath =(String) result.get("fileName");
			response.setContentType("application/msexcel");
			response.addHeader("Content-Disposition", "attachment;filename="
					+new String(filePath.getBytes("utf-8"),"ISO_8859_1"));
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
	 * Excel导出选中项目数据
	 * @param request
	 * @param response
	 */
	@RequestMapping(params="excelExportSelectedIds")
	public void excelExportSelectedIds(HttpServletRequest request,HttpServletResponse response){
		String ids = request.getParameter("ids");
		String month = request.getParameter("month");
		Date date=null;
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date = sdf.parse(month);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] sids = ids.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		List<ProjectEntity> list = projectService.findAllByProjectId(lids);
		Map<String,Object> result = new HashMap<String,Object>();
		result=projectService.exportProjectData(list,date);
		ServletOutputStream out = null;
		try {
			String filePath =(String) result.get("fileName");
			response.setContentType("application/msexcel");
			response.addHeader("Content-Disposition", "attachment;filename="
					+new String(filePath.getBytes("utf-8"),"ISO_8859_1"));
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
	 * Excel导出选中项目数据 -访客专用
	 * @param request
	 * @param response
	 */
	@RequestMapping(params="excelExportSelectedIdsforAccess")
	public void excelExportSelectedIdsforAccess(HttpServletRequest request,HttpServletResponse response){
		String ids = request.getParameter("ids");
		String month = request.getParameter("month");
		Date date=null;
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date = sdf.parse(month);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] sids = ids.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		List<ProjectCopyEntity> list = projectService.findAllByProjectCopyId(lids);
		Map<String,Object> result = new HashMap<String,Object>();
		result=projectService.exportProjectCopyData(list,date);
		ServletOutputStream out = null;
		try {
			String filePath =(String) result.get("fileName");
			response.setContentType("application/msexcel");
			response.addHeader("Content-Disposition", "attachment;filename="
					+new String(filePath.getBytes("utf-8"),"ISO_8859_1"));
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
	 * 获取我的（项目）待处理数量
	 * @return
	 */
	@RequestMapping(params = "getMyMessageCout")
	@ResponseBody
	public int getMyMessageCout() {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		int count = projectService.getMyMessageCount(user);
		return count;
	}
	/**
	 * 一键统计
	 * @param month  查询条件-月份
	 * @param isAll 0人力+项目 1人力 2项目
	 * @param departId  查询条件-部门
	 * @return
	 */
	@RequestMapping(params = "oneKeyTotal")
	@ResponseBody
	public Map<String,Double> oneKeyTotal(HttpServletRequest request) {

		String month = request.getParameter("month");
		String department = request.getParameter("employeeDepartment");
		String customerName = request.getParameter("customerName");
		String declareStatus = request.getParameter("declareStatus");

		Date date = new Date();
		ProjectEntity project = new ProjectEntity();
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date =sdf.parse(month);
				project.setProjectMonth(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(department==null||"".equals(department)||"undefined".equals(department)) {
		} else {
			project.setProjectDepartment(department);
		}
		int customerId = -1;
		if(customerName==null||"".equals(customerName)||"undefined".equals(customerName)) {
		} else {
			customerId = Integer.parseInt(customerName);
			project.setProjectCustomer1(customerName);
		}
		DataGrid dataGrid = new DataGrid();
		Map<String,Double> map = new HashMap<String,Double>();
		// 1:审批通过  2：待审批  3：未上报

		String isAll = request.getParameter("isAll");
        if("0".equals(isAll)) {  // 项目+人力 收支申报总计

        	EmployeeDeclareEntity employeeDeclare = new EmployeeDeclareEntity();
        	employeeDeclare.setSalaryDate(project.getProjectMonth());
            if(project.getProjectDepartment()!=null) employeeDeclare.setEmployeeDepartment(project.getProjectDepartment());;
            if(project.getProjectCustomer1()!=null) employeeDeclare.setCustomerInfo(Integer.parseInt(project.getProjectCustomer1()));
            //项目收支申报状态 转换  人力收支申报状态
            String newStatus = "";
            if(declareStatus==null||"".equals(declareStatus)||"undefined".equals(declareStatus)){
            }else {
            	 Integer ns = commonRepository.changeStatusProjectToEmployeeDeclare(Integer.parseInt(declareStatus));
            	 newStatus = ""+ns;
            }
            Map<String,Double> map1 = projectService.oneKeyTotalXM(request,declareStatus,project,dataGrid);
        	Map<String,Double> map2 = employeeDeclareService.oneKeyTotalRL(request, newStatus, employeeDeclare, dataGrid);
        	//项目+人力
        	map.put("incomeTotal", map1.get("incomeTotal")+map2.get("incomeTotal"));
            map.put("profitTotal", map1.get("profitTotal")+map2.get("profitTotal"));
            map.put("peopleTotal", map1.get("peopleTotal")+map2.get("peopleTotal"));
        } else if("1".equals(isAll)){   // 人力收支申报总计
        }else if("2".equals(isAll)) {  //项目收支申报总计
        	map = projectService.oneKeyTotalXM(request,declareStatus,project,dataGrid);
        }else { //缺省
        	map = projectService.oneKeyTotalXM(request,declareStatus,project,dataGrid);
        }
		return map;
	}
	/**
	 * 一键统计 -访客专属
	 * @return
	 */
	@RequestMapping(params = "oneKeyTotalByVisitor")
	@ResponseBody
	public Map<String,Double> oneKeyTotalByVisitor(HttpServletRequest request) {
		String month = request.getParameter("month");
		String department = request.getParameter("employeeDepartment");
		String customerName = request.getParameter("customerName");

		Date date = new Date();
		ProjectCopyEntity projectCopy = new ProjectCopyEntity();
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date =sdf.parse(month);
				projectCopy.setProjectMonth(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(department==null||"".equals(department)||"undefined".equals(department)) {
		} else {
			projectCopy.setProjectDepartment(department);
		}
		int customerId = -1;
		if(customerName==null||"".equals(customerName)||"undefined".equals(customerName)) {
		} else {
			customerId = Integer.parseInt(customerName);
			projectCopy.setProjectCustomer1(customerName);
		}
		DataGrid dataGrid = new DataGrid();
		Map<String,Double> map = new HashMap<String,Double>();
		Map<String,Double> mapxm = projectService.oneKeyTotalXMByVisitor(request,projectCopy,dataGrid);

		String isAll = request.getParameter("isAll");
        if("0".equals(isAll)) {  // 项目+人力 收支申报总计
        	Map<String,Double> map1 = mapxm;
        	Map<String,Double> map2 = employeeDeclareService.oneKeyTotalRLByVisitor(date, department,customerId);
        	//项目+人力
        	map.put("incomeTotal", map1.get("incomeTotal")+map2.get("incomeTotal"));
            map.put("profitTotal", map1.get("profitTotal")+map2.get("profitTotal"));
            map.put("peopleTotal", map1.get("peopleTotal")+map2.get("peopleTotal"));
        } else if("1".equals(isAll)){   // 人力收支申报总计
        	map = employeeDeclareService.oneKeyTotalRLByVisitor(date, department,customerId);
        }else if("2".equals(isAll)) {  //项目收支申报总计
        	map = mapxm;
        }else { //缺省
        	map = mapxm;
        }
		return map;
	}
}
