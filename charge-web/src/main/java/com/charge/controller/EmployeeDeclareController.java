package com.charge.controller;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.p3.core.utils.common.StringUtils;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ResourceUtil;

import com.charge.entity.CustomerInfoEntity;
import com.charge.entity.EmployeeDeclareCopyEntity;
import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.ProjectCopyEntity;
import com.charge.entity.ProjectEntity;
import com.charge.entity.SixGoldEntity;
import com.charge.entity.SixGoldScaleEntity;
import com.charge.repository.CustomerInfoRepository;
import com.charge.repository.EmployeeDeclareRepository;
import com.charge.repository.SixGoldScaleRepository;
import com.charge.repository.CommonRepository;
import com.charge.service.DictCategoryService;
import com.charge.service.EmailConfigService;
import com.charge.service.EmployeeDeclareCopyService;
import com.charge.service.EmployeeDeclareService;
import com.charge.service.ExportDiagramService;
import com.charge.service.ProjectService;
import com.charge.utils.Utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Title: Controller
 * @Description: 收支申报表
 * @author wenst
 * @date 2018-03-19 16:45:06
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/employeeDeclareController")
public class EmployeeDeclareController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(EmployeeDeclareController.class);
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EmployeeDeclareService employeeDeclareService;
	@Autowired
	private EmployeeDeclareRepository employeeDeclareRepo;
	@Autowired
	private CommonRepository jeecgRepo;
	@Autowired
	private CustomerInfoRepository customerInfoRepo;
	@Autowired
	private SixGoldScaleRepository sixGoldScaleRepository;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private EmployeeDeclareCopyService employeeDeclareCopyService;
	@Autowired
	private ExportDiagramService exportDiagramService;
	/**
	 * 申报员工初始化
	 */
	@RequestMapping(params="declareInit")
	@ResponseBody
	public Map<String,Object> declareInit(String month) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", "0");
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		List<String> warnMsg = new ArrayList<String>();
		ContextHolderUtils.getRequest().setAttribute("warnMsg", warnMsg);
		List<Integer> nums = new ArrayList<Integer>();
		ContextHolderUtils.getRequest().setAttribute("nums", nums);
		if(null!=user){
			//通过user_id获取 用户所在部门
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        	String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			if(null==departId){
				result.put("errCode", "-1");
				result.put("errInfo", "当前部门为空，无法生成基础报表");
				log.info("账号："+user.getUserName()+"部门为空，无法初始化申报员工");
			}else{
				try {
					employeeDeclareService.initDefalutData(departId, month, user);
					String nullMsg =(String) ContextHolderUtils.getRequest().getAttribute("nullMsg");
					if(nullMsg!=null || "".equals(nullMsg)) {
						result.put("errCode", "-1");
						result.put("errInfo", nullMsg);
					}else {
						StringBuffer sb = new StringBuffer();
						nums = (List<Integer>)ContextHolderUtils.getRequest().getAttribute("nums");
						if(!nums.isEmpty()) {
							sb.append("所选月份新生成"+nums.get(0)+"条信息\n\r");

						}
						warnMsg = (List<String>) ContextHolderUtils.getRequest().getAttribute("warnMsg");
						if(warnMsg.size()!=0||!warnMsg.isEmpty()) {
							for(String str : warnMsg) {
								sb.append(str+",");
							}
							sb.append("上述员工六金地点为空，或者公司暂未提供该员工所填六金地点服务，请线下联系管理员");
						}
						result.put("successInfo", sb.toString());
						log.info("初始化本月员工数据成功");
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
					result.put("errCode", "-1");
					result.put("errInfo", "该年的出勤日历还未填写，无法生成收支申报，请联系审计人员");
				}catch (Exception e) {
					e.printStackTrace();
					result.put("errCode", "-1");
					result.put("errInfo", "数据库异常，生成失败，请联系管理员");
				}
			}
		} else{
			result.put("errCode", "-1");
			result.put("errInfo", "登录超时，生成失败。请重新登陆");
			log.info("当前用户未登录，无法查看申报列表");
		}
		return result;
	}

	/**
	 * 收支申报信息录入列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "entrylist")
	public ModelAndView entrylist(HttpServletRequest request) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		String myRoleName = employeeDeclareRepo.findMyRole(user.getId());
		int page=1;
		if("t_input_m".equals(myRoleName)){
			page=3;
		}
		try{
			Integer lv = jeecgRepo.getDeclareInitStatusNumber(user);
		    request.setAttribute("actionCode",lv);

			List<TSDepart> myDeparts = jeecgRepo.findMyDeptInfo();
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
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			return new ModelAndView("com/charge/employeeDeclareEntryList"+page);
		}
	}

	/**
	 * 收支申报列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "declarelist")
	public ModelAndView declarelist(HttpServletRequest request) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Integer lv = jeecgRepo.getDeclareInitStatusNumber(user);
	    request.setAttribute("actionCode",lv);
        try {
        	List<TSDepart> myDeparts = jeecgRepo.findMyDeptInfo();
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
        }catch(Exception e) {
			e.printStackTrace();
		}finally{
			return new ModelAndView("com/charge/employeeDeclareDeclareList");
		}



	}


	/**
	 * 员工申报列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "verifylist")
	public ModelAndView verifylist(HttpServletRequest request) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Integer lv = jeecgRepo.getDeclareInitStatusNumber(user);
	    request.setAttribute("actionCode",lv);
		try {
			List<TSDepart> myDeparts = (List<TSDepart>)jeecgRepo.findMyDeptInfo();
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

		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			return new ModelAndView("com/charge/employeeDeclareVerifyList");
		}

	}

	/**
	 * 员工申报审批列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "approvallist")
	public ModelAndView approvallist(HttpServletRequest request) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Integer lv = jeecgRepo.getDeclareInitStatusNumber(user);
	    request.setAttribute("actionCode",lv);
		try {
			List<TSDepart> myDeparts = (List<TSDepart>)jeecgRepo.findMyDeptInfo();
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

//			//点击即看过  批量修改 收支系统消息 待处理为已处理
//			TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
//			String sql = "update c_aa_test set message_declare=1 where message_type=1 and message_to=?";
//			systemService.executeSql(sql, user.getUserName());

		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			return new ModelAndView("com/charge/employeeDeclareApprovalList");
		}
	}
	/**
	 * 访客收支查看  页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "accesslist")
	public ModelAndView accesslist(HttpServletRequest request) {
		//获取表头数据
//		Map<String, Object> result = employeeDeclareService.getTableHeaderDatas(new Integer[]{1});
//		return new ModelAndView("com/charge/employeeApprovalList").addObject("hds", result);

		try {
			List<TSDepart> myDeparts = (List<TSDepart>)jeecgRepo.findMyDeptInfo();
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

			List<EmployeeInfoEntity> employeeinfolist = commonService.loadAll(EmployeeInfoEntity.class);
			String employeeinfoName ="";
			String employeeinfoCode ="";
			for(int i=0;i<employeeinfolist.size();i++) {
				EmployeeInfoEntity obj = employeeinfolist.get(i);
				employeeinfoName+=obj.getName()+"_"+obj.getId();

				employeeinfoCode+=obj.getCode()+"_"+obj.getId();

				if(i<employeeinfolist.size()-1) {
					employeeinfoName+=",";
					employeeinfoCode+=",";
				}
			}
			request.setAttribute("employeeinfoName", employeeinfoName);
			request.setAttribute("employeeinfoCode", employeeinfoCode);
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			return new ModelAndView("com/charge/employeeDeclareAccesslList");
		}
	}
	/**
	 * 收支预演 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "previewDeclare")
	public ModelAndView previewDeclare(HttpServletRequest request) {
		//获取表头数据
		//加入 流转税率 和  税收起征点
		String systemTurnoverTax = jeecgRepo.getSystemTurnoverTax();
		String systemPerTax = "3500";
		request.setAttribute("turnoverTax",systemTurnoverTax);
		request.setAttribute("perTaxBase",systemPerTax);
		return new ModelAndView("com/charge/employeeDeclarePreview");
	}

	/**
	 * Ajax获取 六金及个税
	 * @param aStandardSalary
	 * @param basePay
	 * @return
	 */
	@RequestMapping(params = "calculateSixGold",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> calculateSixGold(String sixGoldPlace,double sixGoldBase) {

		try {
			sixGoldPlace = URLDecoder.decode(sixGoldPlace, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,Object> calResult = new HashMap<>();
		//六金
		SixGoldEntity sixGold = calSixGold(sixGoldPlace,sixGoldBase);
		calResult.put("companySum", sixGold.getCompanySum());
		calResult.put("personalSum", sixGold.getPersonalSum());
		return calResult;
	}

	private SixGoldEntity calSixGold(String sixGoldPlace,double basePay) {
		SixGoldScaleEntity sixGoldScale = sixGoldScaleRepository.findByCity(sixGoldPlace);
		SixGoldEntity sixGoldEntity = new SixGoldEntity();

    	//公司 个人 养老金
    	Double companyEndowment = sixGoldScale.getCompanyEndowment();
    	Double personalEndowment = sixGoldScale.getPersonalEndowment();
    	Double endowmentMax = sixGoldScale.getEndowmentMax();
    	Double endowmentMin = sixGoldScale.getEndowmentMin();
    	if(basePay < endowmentMin) {
    		companyEndowment = endowmentMin * companyEndowment/100;
    		personalEndowment = endowmentMin * personalEndowment/100;
    	}else if(basePay > endowmentMax) {
    		companyEndowment = endowmentMax * companyEndowment/100;
    		personalEndowment = endowmentMax * personalEndowment/100;
    	}else {
    		companyEndowment = basePay * companyEndowment/100;
    		personalEndowment = basePay * personalEndowment/100;
    	}

    	//公司 个人 住房公积金
    	Double companyHousingFund = sixGoldScale.getCompanyHousingFund();
    	Double personalHousingFund = sixGoldScale.getPersonalHousingFund();
    	Double housingFundMax = sixGoldScale.getHousingFundMax();
    	Double housingFundMin = sixGoldScale.getHousingFundMin();
    	if(basePay < housingFundMin) {
    		companyHousingFund = housingFundMin * companyHousingFund/100;
    		personalHousingFund = housingFundMin * personalHousingFund/100;
    	}else if(basePay > housingFundMax) {
    		companyHousingFund = housingFundMax * companyHousingFund/100;
    		personalHousingFund = housingFundMax * personalHousingFund/100;
    	}else {
    		companyHousingFund = basePay * companyHousingFund/100;
    		personalHousingFund = basePay * personalHousingFund/100;
    	}

    	//公司个人 医疗保险
    	Double companyMedical = sixGoldScale.getCompanyMedical();
    	Double personalMedical = sixGoldScale.getPersonalMedical();
    	Double medicalMax = sixGoldScale.getMedicalMax();
    	Double medicalMin = sixGoldScale.getMedicalMin();
    	if(basePay < medicalMin) {
    		companyMedical = medicalMin * companyMedical/100;
    		personalMedical = medicalMin * personalMedical/100;
    	}else if(basePay > medicalMax) {
    		companyMedical = medicalMax * companyMedical/100;
    		personalMedical = medicalMax * personalMedical/100;
    	}else {
    		companyMedical = basePay * companyMedical/100;
    		personalMedical = basePay * personalMedical/100;
    	}

    	//公司 个人 失业保险
    	Double companyUnemployment = sixGoldScale.getCompanyUnemployment();
    	Double personalUnemployment = sixGoldScale.getPersonalUnemployment();
    	Double unemploymentMax = sixGoldScale.getUnemploymentMax();
    	Double unemploymentMin = sixGoldScale.getUnemploymentMin();
    	if(basePay < unemploymentMin) {
    		companyUnemployment = unemploymentMin * companyUnemployment/100;
    		personalUnemployment = unemploymentMin * personalUnemployment/100;
    	}else if(basePay > unemploymentMax) {
    		companyUnemployment = unemploymentMax * companyUnemployment/100;
    		personalUnemployment = unemploymentMax * personalUnemployment/100;
    	}else {
    		companyUnemployment = basePay * companyUnemployment/100;
    		personalUnemployment = basePay * personalUnemployment/100;
    	}

    	//工伤 保险
    	Double companyInjury = sixGoldScale.getCompanyInjury();
    	Double injuryMax = sixGoldScale.getInjuryMax();
    	Double injuryMin = sixGoldScale.getInjuryMin();
    	if(basePay < injuryMin) {
    		companyInjury = unemploymentMin * companyInjury/100;
    	}else if(basePay > injuryMax) {
    		companyInjury = unemploymentMax * companyInjury/100;
    	}else {
    		companyInjury = basePay * companyInjury/100;
    	}

    	//生育保险
    	Double companyMaternity = sixGoldScale.getCompanyMaternity();
    	Double maternityMax = sixGoldScale.getMaternityMax();
    	Double maternityMin = sixGoldScale.getMaternityMin();
    	if(basePay < maternityMin) {
    		companyMaternity = maternityMin * companyMaternity/100;
    	}else if(basePay > maternityMax) {
    		companyMaternity = maternityMax * companyMaternity/100;
    	}else {
    		companyMaternity = basePay * companyMaternity/100;
    	}
    	//重点六金 合计
    	sixGoldEntity.setCompanySum(companyEndowment+companyHousingFund+companyInjury+companyMaternity+companyMedical+companyUnemployment);
    	sixGoldEntity.setPersonalSum(personalEndowment+personalHousingFund+personalMedical+personalUnemployment);

    	return sixGoldEntity;
	}

	@RequestMapping(params = "clearMonth")
	public void clearMonth() {
		this.timeCondition=null;
		this.timeCondition6=null;
	}

	private Date timeCondition;
	@RequestMapping(params = "employeeDeclareFindbyMonth")
	public void employeeDeclareFindbyMonth(EmployeeDeclareEntity employeeDeclare,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String month;
		if(request.getParameter("month")!=null&&!"".equals(request.getParameter("month"))) {
			month = request.getParameter("month");
		}else {
			Date date = timeCondition==null?new Date():timeCondition;
			month = sdf.format(date);
		}
		try {
			Date date = sdf.parse(month);
			employeeDeclare.setSalaryDate(date);
			timeCondition=date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataGrid.setField("id,customerName,salaryDate,employeeDepartment,employeeInfo.name,employeeInfo.code,"
				+ "employeeType,income,companyProfit,companyProfitRate,declareStatus,declareReturnreason,inputerId,baceUpFlag");
		//排序
		if("inputName".equals(dataGrid.getSort())) {
			dataGrid.setSort("inputerId");
		}
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Integer lv = jeecgRepo.getDeclareInitStatusNumber(user);
		//加入姓名模糊查询
		if(employeeDeclare.getEmployeeInfo()!=null) {
			employeeDeclare.getEmployeeInfo().setName("*"+employeeDeclare.getEmployeeInfo().getName()+"*");
		}
		//部门查询在setDataGrid中处理
//		if(employeeDeclare.getEmployeeDepartment()!=null && !"".equals(employeeDeclare.getEmployeeDepartment())) {
//			employeeDeclare.setEmployeeDepartment(employeeDeclare.getEmployeeDepartment()+"*");
//		}
		//客户简称查询替换为客户id查询
		if(employeeDeclare.getCustomerName()!=null && !"".equals(employeeDeclare.getCustomerName())) {
			employeeDeclare.setCustomerInfo(Integer.parseInt(employeeDeclare.getCustomerName()));
			employeeDeclare.setCustomerName(null);
		}
		if(employeeDeclare.getDeclareStatus()!=null) {
			if(employeeDeclare.getDeclareStatus()==lv+1) { //查询 未上报
				employeeDeclare.setDeclareStatus(null); //清空eq查询条件
				employeeDeclareService.setDataGrid(employeeDeclare, request.getParameterMap(), dataGrid, new Integer[]{lv+1}, 0,2);
	        }else if(employeeDeclare.getDeclareStatus()==lv-3){//查询已上报
	        	employeeDeclare.setDeclareStatus(null); //清空eq查询条件
				employeeDeclareService.setDataGrid(employeeDeclare, request.getParameterMap(), dataGrid, new Integer[]{lv-3}, 0,1);
	        }else {
	        	employeeDeclareService.setDataGrid(employeeDeclare, request.getParameterMap(), dataGrid, null, 0,0);
	        }
		}else {
			employeeDeclareService.setDataGrid(employeeDeclare, request.getParameterMap(), dataGrid, null, 0,0);
		}
		//获取申报审核审批的对应姓名
		Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
        List<EmployeeDeclareEntity> EmployeeDeclareEntitys = dataGrid.getResults();
        for(EmployeeDeclareEntity temp: EmployeeDeclareEntitys){
	        //此为针对原来的行数据，拓展的新字段
	        Map m = new HashMap();
	        List<TSUser> inputer =systemService.findHql("from TSUser t where t.userName=?", new Object[]{temp.getInputerId()});
	        if(null == inputer || inputer.size() ==0){
	        	m.put("inputName", "无");
	        }else{
	        	m.put("inputName", inputer.get(0).getRealName());
	        }
	        extMap.put(temp.getId().toString(), m);
        }
        for(EmployeeDeclareEntity temp: EmployeeDeclareEntitys){
	        if(temp.getDeclareStatus()>lv.intValue()) { //未上报
	        	temp.setDeclareStatus(lv+1);
	        }else if(temp.getDeclareStatus()<=lv.intValue()-3 && temp.getDeclareStatus()!=2) {  //已上报
	        	temp.setDeclareStatus(lv-3);
	        }
        }
		//输出到客户端   输出内容：List<EmployeeDeclareEntiry>  dataGrid.results
		TagUtil.datagrid(response, dataGrid,extMap);
	}
	/*
	 * 访客人力收支页面列表专用
	 *
	 * */
	private Date timeCondition6;
	@RequestMapping(params = "employeeDeclareFindbyMonth6")
	public void employeeDeclareFindbyMonth6(EmployeeDeclareCopyEntity employeeDeclareCopy,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String month;
		if(request.getParameter("month")!=null&&!"".equals(request.getParameter("month"))) {
			month = request.getParameter("month");
		}else {
			Date date = timeCondition6==null?new Date():timeCondition6;
			month = sdf.format(date);
		}
		try {
			Date date = sdf.parse(month);
			employeeDeclareCopy.setSalaryDate(date);
			timeCondition6=date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dataGrid.setField("id,customerName,salaryDate,employeeDepartment,employeeId,employeeInfo.code,"
				+ "employeeType,income,companyProfit,companyProfitRate,declareStatus,declareReturnreason,batId,");

		if("inputName".equals(dataGrid.getSort())) {
			dataGrid.setSort("inputerId");
		}
		//客户简称查询替换为客户id查询
		if(employeeDeclareCopy.getCustomerName()!=null && !"".equals(employeeDeclareCopy.getCustomerName())) {
			employeeDeclareCopy.setCustomerId(Integer.parseInt(employeeDeclareCopy.getCustomerName()));
			employeeDeclareCopy.setCustomerName(null);
		}
		employeeDeclareService.setDataGridforAccess(employeeDeclareCopy, request.getParameterMap(), dataGrid);
		//获取申报审核审批的对应姓名
		Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
		List<EmployeeDeclareCopyEntity> employeeDeclareCopys = dataGrid.getResults();
		for(EmployeeDeclareCopyEntity temp: employeeDeclareCopys){
			//此为针对原来的行数据，拓展的新字段
			Map m = new HashMap();
			List<TSUser> inputer =systemService.findHql("from TSUser t where t.userName=?", new Object[]{temp.getInputerId()});
			if(null == inputer || inputer.size() ==0){
				m.put("inputName", "无");
			}else{
				m.put("inputName", inputer.get(0).getRealName());
			}
			extMap.put(temp.getId().toString(), m);
		}
		//输出到客户端   输出内容：List<EmployeeDeclareEntiry>  dataGrid.results
		TagUtil.datagrid(response, dataGrid,extMap);
	}

	/**
	 * 批量还原
	 * 批量还原 选中的“申报中”数据
	 * @param  String ids
	 * @return
	 */
	@RequestMapping(params="employeeRestore")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String,Object> employeeRestore(@RequestParam("ids") String ids){
		//切割 传来的 员工申报记录  id
		String[] sids = ids.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		Map<String, Object> result = employeeDeclareCopyService.employeeRestore(lids);
		return result;
	}

	/**
	 * 批量通过
	 * 批量通过 选中的“申报中”数据
	 * @param  String ids
	 * @return
	 */
	@RequestMapping(params="batchPass")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String,Object> batchPass(@RequestParam("ids") String ids){
		//切割 传来的 员工申报记录  id
		String[] sids = ids.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		Map<String, Object> result = employeeDeclareService.batchPass(lids);
		return result;
	}
	/**
	 * 0507： 批量退回选中
	 *
	 */
	@RequestMapping(params="batchReturn2",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> batchReturn2(@RequestParam("ids") String ids,
			@RequestParam("returnReason") String returnReason,
			@RequestParam("type") String type){

		String[] sids = ids.split(",");
		try {
			returnReason = URLDecoder.decode(returnReason, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		Map<String, Object> result = employeeDeclareService.batchReturn(lids, returnReason,type);
		return result;
	}

	/**
	 * 导出excel
	 * @param response
	 * @param list
	 */
	@RequestMapping(params="excelExport")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response){
		String month = request.getParameter("month");
		String employeeName = request.getParameter("employeeInfo.name");
		String department = request.getParameter("employeeDepartment");
		String customerName = request.getParameter("customerName");
		String employeeType = request.getParameter("employeeType");
		String declareStatus = request.getParameter("declareStatus");
		String place = request.getParameter("place");
		Date date=null;
		EmployeeDeclareEntity employeeDeclare = new EmployeeDeclareEntity();
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date = sdf.parse(month);
				employeeDeclare.setSalaryDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(employeeName==null||"".equals(employeeName)||"undefined".equals(employeeName)) {
		} else {
			EmployeeInfoEntity employee = new EmployeeInfoEntity();
			employee.setName("*"+employeeName+"*");
			employeeDeclare.setEmployeeInfo(employee);;
		}
		if(department==null||"".equals(department)||"undefined".equals(department)) {
		} else {
			employeeDeclare.setEmployeeDepartment(department);
		}
		if(customerName==null||"".equals(customerName)||"undefined".equals(customerName)) {
		} else {
			employeeDeclare.setCustomerInfo(Integer.parseInt(customerName));;
		}
		if(employeeType==null||"".equals(employeeType)||"undefined".equals(employeeType)) {
		} else {
			employeeDeclare.setEmployeeType(Integer.parseInt(employeeType));
		}

		//exportDiagramService.exportFinancialVoucher(place, employeeDeclareCopy.getSalaryDate());
		Map<String,Object> result = null;
		DataGrid dataGrid = new DataGrid();
		String isAll = request.getParameter("isAll");
        if("0".equals(isAll)) {  // 部门总收支
        	result = employeeDeclareService.exportDepartData(request, declareStatus, employeeDeclare, dataGrid, date);
        } else if("1".equals(isAll)){   // 人力 收支总计
        	employeeDeclareService.setDataGridByStatus(request, declareStatus, employeeDeclare, dataGrid);
        	result= employeeDeclareService.exportDeclareData((List<EmployeeDeclareEntity>)dataGrid.getResults(), date);
        }else if("2".equals(isAll)) {  // 项目 收支总计
        	//先不写
        }else { //缺省   人力收支导出
        	employeeDeclareService.setDataGridByStatus(request, declareStatus, employeeDeclare, dataGrid);
        	result= employeeDeclareService.exportDeclareData((List<EmployeeDeclareEntity>)dataGrid.getResults(), date);
        }
//        result= exportDiagramService.exportCostStatistics(place, employeeDeclareCopy.getSalaryDate());

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
	 * 导出excel -访客专用
	 * @param response
	 * @param list
	 */
	@RequestMapping(params="excelExportforAccess")
	public void excelExportforAccess(HttpServletRequest request,HttpServletResponse response){
		String month = request.getParameter("month");
		String employeeName = request.getParameter("employeeInfo.name");
		String department = request.getParameter("employeeDepartment");
		String customerName = request.getParameter("customerName");
		Date date=null;
		EmployeeDeclareCopyEntity employeeDeclare = new EmployeeDeclareCopyEntity();
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date = sdf.parse(month);
				employeeDeclare.setSalaryDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(employeeName==null||"".equals(employeeName)||"undefined".equals(employeeName)) {
		} else {
			EmployeeInfoEntity employee = new EmployeeInfoEntity();
			employee.setName("*"+employeeName+"*");
			employeeDeclare.setEmployeeId(employee.getId());
		}
		if(department==null||"".equals(department)||"undefined".equals(department)) {
		} else {
			employeeDeclare.setEmployeeDepartment(department);
		}
		if(customerName==null||"".equals(customerName)||"undefined".equals(customerName)) {
		} else {
			employeeDeclare.setCustomerId(Integer.parseInt(customerName));
		}
		Map<String,Object> result = null;
		DataGrid dataGrid = new DataGrid();
		String isAll = request.getParameter("isAll");
        if("0".equals(isAll)) {  // 部门总收支
        	result = employeeDeclareService.exportDepartDataforAccess(request,employeeDeclare,dataGrid,date);
        } else if("1".equals(isAll)){   // 人力 收支总计
        	employeeDeclareService.setDataGridforAccess(employeeDeclare,request.getParameterMap(),dataGrid);
        	result= employeeDeclareService.exportDeclareDataforAccess((List<EmployeeDeclareCopyEntity>)dataGrid.getResults(),date);
        }else if("2".equals(isAll)) {  // 项目 收支总计
        	//先不写
        }else { //缺省   人力收支导出
        	employeeDeclareService.setDataGridforAccess(employeeDeclare,request.getParameterMap(),dataGrid);
        	result= employeeDeclareService.exportDeclareDataforAccess((List<EmployeeDeclareCopyEntity>)dataGrid.getResults(),date);
        }
//        result= exportDiagramService.exportCostStatistics(place, employeeDeclareCopy.getSalaryDate());
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
	 * 根据id导出excel
	 * @param response
	 * @param list
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
		List<EmployeeDeclareEntity> list =employeeDeclareRepo.findAllByEmployeeDeclareId(lids);
		Map<String,Object> result = new HashMap<String,Object>();
		result=employeeDeclareService.exportDeclareData(list,date);
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
	 * 根据id导出excel-访客专用
	 * @param response
	 * @param list
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
		List<EmployeeDeclareCopyEntity> list =employeeDeclareRepo.findAllByEmployeeDeclareCopyId(lids);
		Map<String,Object> result = new HashMap<String,Object>();
		result=employeeDeclareService.exportDeclareDataforAccess(list,date);
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
	 * 导出excelSalaryExport
	 *
	 * @param response
	 * @param list
	 */
	@RequestMapping(params = "excelSalaryExport")
	public void exportSalaryExport(HttpServletRequest request, HttpServletResponse response) {
		String month = request.getParameter("month");
		String employeeName = request.getParameter("employeeInfo.name");
		String department = request.getParameter("employeeDepartment");
		String declareStatus = request.getParameter("declareStatus");
		String customerName = request.getParameter("customerName");
		String employeeType = request.getParameter("employeeType");
		Date date=null;
		EmployeeDeclareEntity employeeDeclare = new EmployeeDeclareEntity();
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date = sdf.parse(month);
				employeeDeclare.setSalaryDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (employeeName == null || "".equals(employeeName) || "undefined".equals(employeeName)) {
		} else {
			EmployeeInfoEntity employee=employeeDeclare.getEmployeeInfo()==null?new EmployeeInfoEntity():employeeDeclare.getEmployeeInfo();
			employee.setName("*" + employeeName + "*");
			employeeDeclare.setEmployeeInfo(employee);
		}
		if (department == null || "".equals(department) || "undefined".equals(department)) {
		} else {
			employeeDeclare.setEmployeeDepartment(department);
		}
		if (customerName == null || "".equals(customerName) || "undefined".equals(customerName)) {
		} else {
			employeeDeclare.setCustomerInfo(Integer.parseInt(customerName));
		}
		if (employeeType == null || "".equals(employeeType) || "undefined".equals(employeeType)) {
		} else {
			employeeDeclare.setEmployeeType(Integer.parseInt(employeeType));
		}
		Map<String, Object> result = null;
		DataGrid dataGrid = new DataGrid();
		employeeDeclareService.setDataGridByStatus(request, declareStatus, employeeDeclare, dataGrid);
		result = employeeDeclareService.exportDeclareSalaryData((List<EmployeeDeclareEntity>) dataGrid.getResults(),date);
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
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}
	@RequestMapping(params = "excelSalaryExportSelectedIds")
	public void excelSalaryExportSelectedIds(HttpServletRequest request, HttpServletResponse response) {
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
		for (String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		List<EmployeeDeclareEntity> list = employeeDeclareRepo.findAllByEmployeeDeclareId(lids);
		Map<String, Object> result = new HashMap<String, Object>();
		result = employeeDeclareService.exportDeclareSalaryIdsData(list,date);
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
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * 导出 工资表/成本统计/网银导入
	 * @param request
	 * @param response
	 */
	@RequestMapping(params="exportFinancialVoucherExcel")
	public void exportFinancialVoucherExcel(HttpServletRequest request,HttpServletResponse response){
		String month = request.getParameter("month");
		String aPlace = request.getParameter("salaryCorporateId");
		if("".equals(aPlace)) return ;
		String code = request.getParameter("code");
		if (month == null || "".equals(month) || "undefined".equals(month)) {
		} else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				Date date;
				date = sdf.parse(month);
				Map<String,Object> result = new HashMap<String,Object>();
				String place = replacePlace(Integer.parseInt(aPlace));
				if("1".equals(code)) {  // 导出工资表
					result = exportDiagramService.exportFinancialVoucher(place, date);
				}else if("2".equals(code)){ //导出成本统计
					result = exportDiagramService.exportCostStatistics(place, date);
				}else if("3".equals(code)) {
					result = exportDiagramService.exportEmpDeclareForAccount(place, date);
				}
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
//						codedFilename = new String(filePath.getBytes("UTF-8"), "iso-8859-1");
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
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private String replacePlace(int code) {
		switch(code) {
		case 0:	return "江苏";
		case 1:  return "上海";
		case 2: return "昆山";
		case 3: return "深圳";
		case 4:	return "广州";
		case 5: return "北京";
		default : return "江苏";
		}
	}



	/**
	 * 删除员工申报表
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(@RequestParam("ids") String ids) {
		//申报数据未审批通过、关联员工是已经离职的
		String message = null;
		AjaxJson j = new AjaxJson();
		if(ids==null) { //如果 未选中 则 默认 全选
			j.setMsg("未选中数据");
			return j;
		}
		//切割 传来的 员工申报记录  id
		String[] sids = ids.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		EmployeeDeclareEntity employeeDeclare = new EmployeeDeclareEntity();
		StringBuffer sb = new StringBuffer();
		List<String> succ = new ArrayList<>();
		List<String> fail = new ArrayList<>();
		for(int i=0;i<lids.size();i++) {
			commonService.deleteEntityById(EmployeeDeclareEntity.class,lids.get(i));
		}
		message = "收支录入删除成功";
		j.setMsg(message);
		return j;
	}
	/**
	 * 人力收支信息更改判断 - 内部
	 * j 6-7
	 * @return 0-未更新  1-更新
	 */
	public Integer saveReason(EmployeeDeclareEntity employeeDeclare, EmployeeDeclareEntity t){
		Integer i = 0;
		StringBuffer reason =new StringBuffer("申请理由：");

		/*if (!(t.getUnitPriceType()==null?"a":t.getUnitPriceType()).equals(employeeDeclare.getUnitPriceType()==null?"a":employeeDeclare.getUnitPriceType())) {
			//单价方式
			i=1;
			reason.append("单价方式,");
		}
		if (!(t.getUnitPriceType()==null?"a":t.getUnitPriceType()).equals(employeeDeclare.getUnitPriceType()==null?"a":employeeDeclare.getUnitPriceType())) {
			//单价方式
			i=1;
			reason.append("单价方式,");
		}
		if (!(t.getUnitPrice()==null?"a":t.getUnitPrice()).equals(employeeDeclare.getUnitPrice()==null?"a":employeeDeclare.getUnitPrice())) {
			//单价
			i=1;
			reason.append("单价,");
		}
		if (!(t.getIsTurnoverTax()==null?"a":t.getIsTurnoverTax()).equals(employeeDeclare.getIsTurnoverTax()==null?"a":employeeDeclare.getIsTurnoverTax())) {
			//流转税
			i=1;
			reason.append("流转税,");
		}
		if (!(t.getAcceptedAttendanceDay()==null?"a":t.getAcceptedAttendanceDay()).equals(employeeDeclare.getAcceptedAttendanceDay()==null?"a":employeeDeclare.getAcceptedAttendanceDay())) {
			//验收出勤日
			i=1;
			reason.append("验收出勤日,");
		}
		if (!(t.getAppointedAttendanceDay()==null?"a":t.getAppointedAttendanceDay()).equals(employeeDeclare.getAppointedAttendanceDay()==null?"a":employeeDeclare.getAppointedAttendanceDay())) {
			//预定出勤日
			i=1;
			reason.append("预定出勤日,");
		}
		if (!(t.getMonthOther()==null?"a":t.getMonthOther()).equals(employeeDeclare.getMonthOther()==null?"a":employeeDeclare.getMonthOther())) {
			//当月加算
			i=1;
			reason.append("当月加算,");
		}
		if (!(t.getAcceptanceAdd()==null?"a":t.getAcceptanceAdd()).equals(employeeDeclare.getAcceptanceAdd()==null?"a":employeeDeclare.getAcceptanceAdd())) {
			//验收加算
			i=1;
			reason.append("验收加算,");
		}
		if (!(t.getMonthAdjustment()==null?"a":t.getMonthAdjustment()).equals(employeeDeclare.getMonthAdjustment()==null?"a":employeeDeclare.getMonthAdjustment())) {
			//月间调整
			i=1;
			reason.append("月间调整,");
		}*/
		if (!(t.getBDiscount()==null?"a":t.getBDiscount()).equals(employeeDeclare.getBDiscount()==null?"a":employeeDeclare.getBDiscount())) {
			//B折扣
			i=1;
			reason.append("B折扣,");
		}
		if (!(t.getPerformanceAttendanceDay()==null?"a":t.getPerformanceAttendanceDay()).equals(employeeDeclare.getPerformanceAttendanceDay()==null?"a":employeeDeclare.getPerformanceAttendanceDay())) {
			//有绩效出勤日
			i=1;
			reason.append("有绩效出勤日,");
		}
		if (!(t.getNoPerformanceAttendanceDay()==null?"a":t.getNoPerformanceAttendanceDay()).equals(employeeDeclare.getNoPerformanceAttendanceDay()==null?"a":employeeDeclare.getNoPerformanceAttendanceDay())) {
			//无绩效出勤日
			i=1;
			reason.append("无绩效出勤日,");
		}
		if (!(t.getCComputerSubsidy()==null?"a":t.getCComputerSubsidy()).equals(employeeDeclare.getCComputerSubsidy()==null?"a":employeeDeclare.getCComputerSubsidy())) {
			//C电脑补贴
			i=1;
			reason.append("C电脑补贴,");
		}
		if (!(t.getCOvertimeSalary()==null?"a":t.getCOvertimeSalary()).equals(employeeDeclare.getCOvertimeSalary()==null?"a":employeeDeclare.getCOvertimeSalary())) {
			//C加班费
			i=1;
			reason.append("C加班费,");
		}
		if (!(t.getDAnnualBonus()==null?"0.0":t.getDAnnualBonus()).equals(employeeDeclare.getDAnnualBonus()==null?0.0:employeeDeclare.getDAnnualBonus())) {
			//D年终奖
			i=1;
			reason.append("D年终奖,");
		}
		if (!(t.getC1OtherSubsidy()==null?"a":t.getC1OtherSubsidy()).equals(employeeDeclare.getC1OtherSubsidy()==null?"a":employeeDeclare.getC1OtherSubsidy())) {
			//C1其他补贴
			i=1;
			reason.append("C1其他补贴,");
		}
		/*if (!((t.getC1OtherSubsidyRemark()=="")?"a":t.getC1OtherSubsidyRemark()).equals((employeeDeclare.getC1OtherSubsidyRemark()==null)?"a":employeeDeclare.getC1OtherSubsidyRemark())) {
			//C1补贴备注
			i=1;
			reason.append("C1补贴备注,");
		}*/
		if (!(t.getC2OtherSubsidy()==null?"a":t.getC2OtherSubsidy()).equals(employeeDeclare.getC2OtherSubsidy()==null?"a":employeeDeclare.getC2OtherSubsidy())) {
			//C2其他补贴
			i=1;
			reason.append("C2其他补贴,");
		}
		/*if (!(t.getC2OtherSubsidyRemark()==null?"a":t.getC2OtherSubsidyRemark()).equals(employeeDeclare.getC2OtherSubsidyRemark()==null?"a":employeeDeclare.getC2OtherSubsidyRemark())) {
			//C2补贴备注
			i=1;
			reason.append("C2补贴备注,");
		}*/
		if (!(t.getC3OtherSubsidy()==null?"a":t.getC3OtherSubsidy()).equals(employeeDeclare.getC3OtherSubsidy()==null?"a":employeeDeclare.getC3OtherSubsidy())) {
			//C3其他补贴
			i=1;
			reason.append("C3其他补贴,");
		}
		/*if (!(t.getC3OtherSubsidyRemark()==null?"a":t.getC3OtherSubsidyRemark()).equals(employeeDeclare.getC3OtherSubsidyRemark()==null?"a":employeeDeclare.getC3OtherSubsidyRemark())) {
			//C3补贴备注
			i=1;
			reason.append("C3补贴备注,");
		}*/
		if(i==1){
			reason.append("发生改变");
		}
		return i;
	}
	/**
	 * 人力收支信息更改判断 - 外派
	 * j 6-7
	 * @return 0-未更新  1-更新
	 */
	public Integer saveReason1(EmployeeDeclareEntity employeeDeclare, EmployeeDeclareEntity t){
		Integer i = 0;
		StringBuffer reason =new StringBuffer("申请理由：");

		if (!(t.getCorporateId()==null?"a":t.getCorporateId()).equals(employeeDeclare.getCorporateId()==null?"a":employeeDeclare.getCorporateId())) {
			//法人Id
			i=1;
			reason.append("法人Id,");
		}
		if (!(t.getUnitPriceType()==null?"a":t.getUnitPriceType()).equals(employeeDeclare.getUnitPriceType()==null?"a":employeeDeclare.getUnitPriceType())) {
			//单价方式
			i=1;
			reason.append("单价方式,");
		}
		if (!(t.getUnitPriceType()==null?"a":t.getUnitPriceType()).equals(employeeDeclare.getUnitPriceType()==null?"a":employeeDeclare.getUnitPriceType())) {
			//单价方式
			i=1;
			reason.append("单价方式,");
		}
		if (!(t.getUnitPrice()==null?"a":t.getUnitPrice()).equals(employeeDeclare.getUnitPrice()==null?"a":employeeDeclare.getUnitPrice())) {
			//单价
			i=1;
			reason.append("单价,");
		}
		if (!(t.getIsTurnoverTax()==null?"a":t.getIsTurnoverTax()).equals(employeeDeclare.getIsTurnoverTax()==null?"a":employeeDeclare.getIsTurnoverTax())) {
			//流转税
			i=1;
			reason.append("流转税,");
		}
		if (!(t.getAcceptedAttendanceDay()==null?"a":t.getAcceptedAttendanceDay()).equals(employeeDeclare.getAcceptedAttendanceDay()==null?"a":employeeDeclare.getAcceptedAttendanceDay())) {
			//验收出勤日
			i=1;
			reason.append("验收出勤日,");
		}
		if (!(t.getAppointedAttendanceDay()==null?"a":t.getAppointedAttendanceDay()).equals(employeeDeclare.getAppointedAttendanceDay()==null?"a":employeeDeclare.getAppointedAttendanceDay())) {
			//预定出勤日
			i=1;
			reason.append("预定出勤日,");
		}
		if (!(t.getMonthOther()==null?"a":t.getMonthOther()).equals(employeeDeclare.getMonthOther()==null?"a":employeeDeclare.getMonthOther())) {
			//当月加算
			i=1;
			reason.append("当月加算,");
		}
		if (!(t.getAcceptanceAdd()==null?"a":t.getAcceptanceAdd()).equals(employeeDeclare.getAcceptanceAdd()==null?"a":employeeDeclare.getAcceptanceAdd())) {
			//验收加算
			i=1;
			reason.append("验收加算,");
		}
		if (!(t.getMonthAdjustment()==null?"a":t.getMonthAdjustment()).equals(employeeDeclare.getMonthAdjustment()==null?"a":employeeDeclare.getMonthAdjustment())) {
			//月间调整
			i=1;
			reason.append("月间调整,");
		}
		if (!(t.getBDiscount()==null?"a":t.getBDiscount()).equals(employeeDeclare.getBDiscount()==null?"a":employeeDeclare.getBDiscount())) {
			//B折扣
			i=1;
			reason.append("B折扣,");
		}
		if (!(t.getPerformanceAttendanceDay()==null?"a":t.getPerformanceAttendanceDay()).equals(employeeDeclare.getPerformanceAttendanceDay()==null?"a":employeeDeclare.getPerformanceAttendanceDay())) {
			//有绩效出勤日
			i=1;
			reason.append("有绩效出勤日,");
		}
		if (!(t.getNoPerformanceAttendanceDay()==null?"a":t.getNoPerformanceAttendanceDay()).equals(employeeDeclare.getNoPerformanceAttendanceDay()==null?"a":employeeDeclare.getNoPerformanceAttendanceDay())) {
			//无绩效出勤日
			i=1;
			reason.append("无绩效出勤日,");
		}
		if (!(t.getCComputerSubsidy()==null?"a":t.getCComputerSubsidy()).equals(employeeDeclare.getCComputerSubsidy()==null?"a":employeeDeclare.getCComputerSubsidy())) {
			//C电脑补贴
			i=1;
			reason.append("C电脑补贴,");
		}
		if (!(t.getCOvertimeSalary()==null?"a":t.getCOvertimeSalary()).equals(employeeDeclare.getCOvertimeSalary()==null?"a":employeeDeclare.getCOvertimeSalary())) {
			//C加班费
			i=1;
			reason.append("C加班费,");
		}
		if (!(t.getDAnnualBonus()==null?"0.0":t.getDAnnualBonus()).equals(employeeDeclare.getDAnnualBonus()==null?0.0:employeeDeclare.getDAnnualBonus())) {
			//D年终奖
			i=1;
			reason.append("D年终奖,");
		}
		if (!(t.getC1OtherSubsidy()==null?"a":t.getC1OtherSubsidy()).equals(employeeDeclare.getC1OtherSubsidy()==null?"a":employeeDeclare.getC1OtherSubsidy())) {
			//C1其他补贴
			i=1;
			reason.append("C1其他补贴,");
		}
		/*if (!((t.getC1OtherSubsidyRemark()=="")?"a":t.getC1OtherSubsidyRemark()).equals((employeeDeclare.getC1OtherSubsidyRemark()==null)?"a":employeeDeclare.getC1OtherSubsidyRemark())) {
			//C1补贴备注
			i=1;
			reason.append("C1补贴备注,");
		}*/
		if (!(t.getC2OtherSubsidy()==null?"a":t.getC2OtherSubsidy()).equals(employeeDeclare.getC2OtherSubsidy()==null?"a":employeeDeclare.getC2OtherSubsidy())) {
			//C2其他补贴
			i=1;
			reason.append("C2其他补贴,");
		}
		/*if (!(t.getC2OtherSubsidyRemark()==null?"a":t.getC2OtherSubsidyRemark()).equals(employeeDeclare.getC2OtherSubsidyRemark()==null?"a":employeeDeclare.getC2OtherSubsidyRemark())) {
			//C2补贴备注
			i=1;
			reason.append("C2补贴备注,");
		}*/
		if (!(t.getC3OtherSubsidy()==null?"a":t.getC3OtherSubsidy()).equals(employeeDeclare.getC3OtherSubsidy()==null?"a":employeeDeclare.getC3OtherSubsidy())) {
			//C3其他补贴
			i=1;
			reason.append("C3其他补贴,");
		}
		/*if (!(t.getC3OtherSubsidyRemark()==null?"a":t.getC3OtherSubsidyRemark()).equals(employeeDeclare.getC3OtherSubsidyRemark()==null?"a":employeeDeclare.getC3OtherSubsidyRemark())) {
			//C3补贴备注
			i=1;
			reason.append("C3补贴备注,");
		}*/
		if(i==1){
			reason.append("发生改变");
		}
		return i;
	}

	/**
	 * 编辑内部员工收支
	 * @param employeeDeclare
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "saveInnerEmployee")
	@ResponseBody
	public AjaxJson saveInnerEmployee(EmployeeDeclareEntity employeeDeclare, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(employeeDeclare.getId())) {
			message = "员工申报表更新成功";
			EmployeeDeclareEntity t = commonService.get(EmployeeDeclareEntity.class, employeeDeclare.getId());
			try {
				Integer lookUp = saveReason(employeeDeclare,t);
				t.setCustomerInfo(null);
				t.setCustomerName(null);

				//无论任何状态修改 都将变为 待申报
				TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
				if(lookUp==1){
					t.setDeclareStatus(jeecgRepo.getDeclareInitStatusNumber(user)-1);
				}


				//更新单价方式
				t.setUnitPriceType(null);
				t.setUnitPrice(null);
				//约定出勤日数
				t.setAppointedAttendanceDay(null);
				//验收出勤日数
				t.setAcceptedAttendanceDay(null);

				//当月加算  验收加算  月间调整
				t.setMonthOther(null);
				t.setAcceptanceAdd(null);
				t.setMonthAdjustment(null);

				//当月薪酬浮动
				//更新 试用期折扣率
//					t.getSalary().setBtDiscountRate("".equals(request.getParameter("btDiscountRate"))?null:Integer.parseInt(request.getParameter("btDiscountRate")));
				String parameter = request.getParameter("bDiscount");
				t.setBDiscount("".equals(request.getParameter("bDiscount"))?null:Integer.parseInt(request.getParameter("bDiscount")));
				//有效出勤日数
				Double double3 = getDouble("performanceAttendanceDay",request);
				t.setPerformanceAttendanceDay(double3);
				//无效出勤
				Double double4 = getDouble("noPerformanceAttendanceDay",request);
				t.setNoPerformanceAttendanceDay(double4);
				//电脑补贴 加班补贴 其他补贴 备注  年终奖
				t.setCComputerSubsidy(getDouble("cComputerSubsidy",request));
				t.setCOvertimeSalary(getDouble("cOvertimeSalary",request));
				t.setC1OtherSubsidy(getDouble("c1OtherSubsidy",request));
				t.setC1OtherSubsidyRemark(request.getParameter("c1Note")==null?null:request.getParameter("c1Note"));
				t.setC2OtherSubsidy(getDouble("c2OtherSubsidy",request));
				t.setC2OtherSubsidyRemark(request.getParameter("c2Note")==null?null:request.getParameter("c2Note"));
				t.setC3OtherSubsidy(getDouble("c3OtherSubsidy",request));
				t.setC3OtherSubsidyRemark(request.getParameter("c3Note")==null?null:request.getParameter("c3Note"));
				t.setDAnnualBonus(getDouble("dnumYearEndBonus",request));
				//计算 收支利润
				jeecgRepo.calEmployeeDeclare(t);
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "员工申报表更新失败";
			}
		} else {
			log.info("员工申请报表无法更新，ID为空");
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 编辑外派员工收支
	 * @param employeeDeclare
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(EmployeeDeclareEntity employeeDeclare, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(employeeDeclare.getId())) {
			message = "员工申报表更新成功";
			EmployeeDeclareEntity t = commonService.get(EmployeeDeclareEntity.class, employeeDeclare.getId());
			try {
				Integer lookUp = saveReason1(employeeDeclare,t);
				/*MyBeanUtils.copyBeanNotNull2Bean(employeeDeclare, t);*/
				if(StringUtils.isNotBlank(request.getParameter("customerInfoId"))){
					CustomerInfoEntity customerInfo = commonService.get(CustomerInfoEntity.class,
							Integer.valueOf(request.getParameter("customerInfoId")));

					if(customerInfo.getId()!=t.getCustomerInfo()){
						lookUp=1;
					}
					t.setCustomerInfo(customerInfo.getId());
					t.setCustomerName(customerInfo.getCode());
					//保存 法人
					t.setCorporateId(Integer.parseInt(request.getParameter("corporateId")));

					//无论任何状态修改 都将变为 待申报
					TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
					if(lookUp==1){
						t.setDeclareStatus(jeecgRepo.getDeclareInitStatusNumber(user)-1);
					}


					//更新单价方式
					String unitPriceType = request.getParameter("unitPriceType");
					t.setUnitPriceType(Integer.parseInt(request.getParameter("unitPriceType")));

					Double double1 = getDouble("unitPrice",request);
					t.setUnitPrice(double1);
					int isTurnoverTax = Integer.parseInt(request.getParameter("isTurnoverTax"));
					t.setIsTurnoverTax(isTurnoverTax);

					//约定出勤日数
					Double appointedAttendanceDay = getDouble("appointedAttendanceDay",request);
					t.setAppointedAttendanceDay(appointedAttendanceDay);
					//验收出勤日数
					Double acceptedAttendanceDay = getDouble("acceptedAttendanceDay",request);
					t.setAcceptedAttendanceDay(acceptedAttendanceDay);

					//当月加算  验收加算  月间调整
					Double double2 = getDouble("monthOther",request);
					t.setMonthOther(double2);
					t.setAcceptanceAdd(getDouble("acceptanceAdd",request));
					t.setMonthAdjustment(getDouble("monthAdjustment",request));

					//当月薪酬浮动
					//更新 试用期折扣率
//					t.getSalary().setBtDiscountRate("".equals(request.getParameter("btDiscountRate"))?null:Integer.parseInt(request.getParameter("btDiscountRate")));
					String parameter = request.getParameter("bDiscount");
					t.setBDiscount("".equals(request.getParameter("bDiscount"))?null:Integer.parseInt(request.getParameter("bDiscount")));

					//有效出勤日数
					Double double3 = getDouble("performanceAttendanceDay",request);
					t.setPerformanceAttendanceDay(double3);
					//无效出勤
					Double double4 = getDouble("noPerformanceAttendanceDay",request);
					t.setNoPerformanceAttendanceDay(double4);

					//电脑补贴 加班补贴 其他补贴 备注  年终奖
					t.setCComputerSubsidy(getDouble("cComputerSubsidy",request));
					t.setCOvertimeSalary(getDouble("cOvertimeSalary",request));


					t.setC1OtherSubsidy(getDouble("c1OtherSubsidy",request));
					t.setC1OtherSubsidyRemark(request.getParameter("c1Note")==null?null:request.getParameter("c1Note"));
					t.setC2OtherSubsidy(getDouble("c2OtherSubsidy",request));
					t.setC2OtherSubsidyRemark(request.getParameter("c2Note")==null?null:request.getParameter("c2Note"));
					t.setC3OtherSubsidy(getDouble("c3OtherSubsidy",request));
					t.setC3OtherSubsidyRemark(request.getParameter("c3Note")==null?null:request.getParameter("c3Note"));
					t.setDAnnualBonus(getDouble("dnumYearEndBonus",request));
				}
				//计算 收支利润
				jeecgRepo.calEmployeeDeclare(t);
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "员工申报表更新失败";
			}
		} else {
			log.info("员工申请报表无法更新，ID为空");
		}
		j.setMsg(message);
		return j;
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
	 * 员工收支申报表编辑页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(EmployeeDeclareEntity employeeDeclare, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(employeeDeclare.getId())) {
			employeeDeclare = commonService.getEntity(EmployeeDeclareEntity.class, employeeDeclare.getId());
			req.setAttribute("employeeDeclarePage", employeeDeclare);
			List<TSDepart> departList = commonService.findHql("from TSDepart t where t.id=?", employeeDeclare.getEmployeeInfo().getDepartment());
			req.setAttribute("departName",departList.get(0).getDepartname());
			if(employeeDeclare.getEmployeeType()==1) { //0外派，1本部
				return new ModelAndView("com/charge/employeeDeclareInner");
			}
		}
		//获取系统 流转税率 和  税收起征点 加入
		String systemTurnoverTax = jeecgRepo.getSystemTurnoverTax();
		req.setAttribute("turnoverTax",systemTurnoverTax);
		return new ModelAndView("com/charge/employeeDeclare");
	}

	/**
	 * 查看员工收支信息
	 * @param employeeDeclare
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "lookorcheck")
	public ModelAndView lookorcheck(EmployeeDeclareEntity employeeDeclare, HttpServletRequest req) {

			if (StringUtil.isNotEmpty(employeeDeclare.getId())) {
			employeeDeclare = commonService.getEntity(EmployeeDeclareEntity.class, employeeDeclare.getId());
			req.setAttribute("employeeDeclarePage", employeeDeclare);
			List<TSDepart> departList = commonService.findHql("from TSDepart t where t.id=?", employeeDeclare.getEmployeeInfo().getDepartment());
			req.setAttribute("departName",departList.get(0).getDepartname());
		}
		return new ModelAndView("com/charge/employeeDeclareLook");
	}
	/**
	 * 查看员工收支信息 - 访客专用
	 * @param employeeDeclareCopy
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "lookorcheckforAccess")
	public ModelAndView lookorcheckforAccess(EmployeeDeclareCopyEntity employeeDeclareCopy, HttpServletRequest req) {

		if (StringUtil.isNotEmpty(employeeDeclareCopy.getId())) {
			employeeDeclareCopy = commonService.getEntity(EmployeeDeclareCopyEntity.class, employeeDeclareCopy.getId());
			req.setAttribute("employeeDeclareCopyPage", employeeDeclareCopy);
			EmployeeInfoEntity employeeInfo = commonService.getEntity(EmployeeInfoEntity.class, employeeDeclareCopy.getEmployeeId());
			req.setAttribute("employeeInfoPage", employeeInfo);
			List<TSDepart> departList = commonService.findHql("from TSDepart t where t.id=?", employeeInfo.getDepartment());
			req.setAttribute("departName",departList.get(0).getDepartname());
		}
		return new ModelAndView("com/charge/employeeDeclareAccessLook");
	}


	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<EmployeeDeclareEntity> list() {
		List<EmployeeDeclareEntity> listEmployeeDeclares=commonService.getList(EmployeeDeclareEntity.class);
		return listEmployeeDeclares;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		EmployeeDeclareEntity task = commonService.get(EmployeeDeclareEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody EmployeeDeclareEntity employeeDeclare, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<EmployeeDeclareEntity>> failures = validator.validate(employeeDeclare);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}
		//保存
		commonService.save(employeeDeclare);
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		Integer id = employeeDeclare.getId();
		URI uri = uriBuilder.path("/rest/employeeDeclareController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody EmployeeDeclareEntity employeeDeclare) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<EmployeeDeclareEntity>> failures = validator.validate(employeeDeclare);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.saveOrUpdate(employeeDeclare);
		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		commonService.deleteEntityById(EmployeeDeclareEntity.class, id);
	}
	/**
	 * 获取系统消息数量前置
	 * @return
	 */
	@RequestMapping(params = "getMyRoleNum")
	@ResponseBody
	public int getMyRoleNum() {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		String myRoleName = employeeDeclareRepo.findMyRole(user.getId());
		int page=0;
		if("t_control".equals(myRoleName)) {
			page=4;
		}else if("t_check".equals(myRoleName)){
			page=3;
		}else if("t_report".equals(myRoleName)){
			page=2;
		}else if("t_input".equals(myRoleName)||"t_input_m".equals(myRoleName)){
			page=1;
		}
		return page;
	}
	/**
	 * 获取我的系统消息数量
	 * @return
	 */
	@RequestMapping(params = "getMyMessageCout")
	@ResponseBody
	public int getMyMessageCout() {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		int count = employeeDeclareService.getMyMessageCount(user);
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
		String employeeName = request.getParameter("employeeInfo.name");
		String department = request.getParameter("employeeDepartment");
		String customerName = request.getParameter("customerName");
		String employeeType = request.getParameter("employeeType");
		String declareStatus = request.getParameter("declareStatus");

		Date date = new Date();
		EmployeeDeclareEntity employeeDeclare = new EmployeeDeclareEntity();
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date =sdf.parse(month);
				employeeDeclare.setSalaryDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(employeeName==null||"".equals(employeeName)||"undefined".equals(employeeName)) {
		} else {
			EmployeeInfoEntity employee = new EmployeeInfoEntity();
			employee.setName("*"+employeeName+"*");
			employeeDeclare.setEmployeeInfo(employee);
		}
		if(department==null||"".equals(department)||"undefined".equals(department)) {
		} else {
			employeeDeclare.setEmployeeDepartment(department);
		}
		int customerId = -1; //默认 没有 客户查询
		if(customerName==null||"".equals(customerName)||"undefined".equals(customerName)) {
		} else {
			customerId = Integer.parseInt(customerName);
			employeeDeclare.setCustomerInfo(Integer.parseInt(customerName));;
		}
		if(employeeType==null||"".equals(employeeType)||"undefined".equals(employeeType)) {
		} else {
			employeeDeclare.setEmployeeType(Integer.parseInt(employeeType));
		}
		DataGrid dataGrid = new DataGrid();
		Map<String,Double> map = new HashMap<String,Double>();
		// 2:审批通过  5：待审批  7：未上报
		String isAll = request.getParameter("isAll");
        if("0".equals(isAll)) {  // 项目+人力 收支申报总计
        	ProjectEntity project = new ProjectEntity();
        	project.setProjectMonth(employeeDeclare.getSalaryDate());
            if(employeeDeclare.getEmployeeDepartment()!=null)  project.setProjectDepartment(employeeDeclare.getEmployeeDepartment());
            if(employeeDeclare.getCustomerInfo()!=null)  project.setProjectCustomer1(employeeDeclare.getCustomerInfo()+"");
            //人力收支申报状态 转换  项目收支申报状态
            String projectStatus = "";
            if(declareStatus==null||"".equals(declareStatus)||"undefined".equals(declareStatus)){
            }else {
            	 Integer ps = jeecgRepo.changeStatusEmployeeDeclareToProject(Integer.parseInt(declareStatus));
            	 projectStatus = ""+ps;
            }
        	Map<String, Double> map1 = projectService.oneKeyTotalXM(request, projectStatus, project, dataGrid);
        	Map<String,Double> map2 = employeeDeclareService.oneKeyTotalRL(request, declareStatus, employeeDeclare, dataGrid);
        	//项目+人力
        	map.put("incomeTotal", map1.get("incomeTotal")+map2.get("incomeTotal"));
            map.put("profitTotal", map1.get("profitTotal")+map2.get("profitTotal"));
            map.put("peopleTotal", map1.get("peopleTotal")+map2.get("peopleTotal"));
        } else if("1".equals(isAll)){   //人力收支申报总计
        	map=employeeDeclareService.oneKeyTotalRL(request, declareStatus, employeeDeclare, dataGrid);
        }else if("2".equals(isAll)) {  //项目收支申报总计
        }else { //缺省
        	map=employeeDeclareService.oneKeyTotalRL(request, declareStatus, employeeDeclare, dataGrid);
        }
		return map;
	}
	/**
	 * 一键统计-访客专属
	 * @return
	 */
	@RequestMapping(params = "oneKeyTotalByVisitor")
	@ResponseBody
	public Map<String,Double> oneKeyTotalByVisitor(HttpServletRequest request) {
		String month = request.getParameter("month");
		String employeeName = request.getParameter("employeeInfo.name");
		String department = request.getParameter("employeeDepartment");
		String customerName = request.getParameter("customerName");

		Date date = new Date();
		EmployeeDeclareCopyEntity employeeDeclareCopy = new EmployeeDeclareCopyEntity();
		if(month==null||"".equals(month)||"undefined".equals(month)) {
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				date =sdf.parse(month);
				employeeDeclareCopy.setSalaryDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(employeeName==null||"".equals(employeeName)||"undefined".equals(employeeName)) {
		} else {
			EmployeeInfoEntity employee = new EmployeeInfoEntity();
			employee.setName("*"+employeeName+"*");
			employeeDeclareCopy.setEmployeeId(employee.getId());
		}
		if(department==null||"".equals(department)||"undefined".equals(department)) {
		} else {
			employeeDeclareCopy.setEmployeeDepartment(department);
		}
		int customerId = -1;
		if(customerName==null||"".equals(customerName)||"undefined".equals(customerName)) {
		} else {
			customerId = Integer.parseInt(customerName);
			employeeDeclareCopy.setCustomerId(Integer.parseInt(customerName));;
		}
		DataGrid dataGrid = new DataGrid();
		Map<String,Double> map = new HashMap<String,Double>();

		Map<String, Double> maprl = employeeDeclareService.oneKeyTotalRLByVisitor(request, employeeDeclareCopy, dataGrid);

		String isAll = request.getParameter("isAll");
        if("0".equals(isAll)) {  // 项目+人力 收支申报总计
        	Map<String,Double> map1 = projectService.oneKeyTotalXMByVisitor(date,department,customerId);
        	Map<String,Double> map2 = maprl;
        	//项目+人力
        	map.put("incomeTotal", map1.get("incomeTotal")+map2.get("incomeTotal"));
            map.put("profitTotal", map1.get("profitTotal")+map2.get("profitTotal"));
            map.put("peopleTotal", map1.get("peopleTotal")+map2.get("peopleTotal"));
        } else if("1".equals(isAll)){   //人力收支申报总计
        	map=maprl;
        }else if("2".equals(isAll)) {  //项目收支申报总计
        	map=projectService.oneKeyTotalXMByVisitor(date,department,customerId);
        }else { //缺省
        	map=maprl;
        }
		return map;
	}

}
