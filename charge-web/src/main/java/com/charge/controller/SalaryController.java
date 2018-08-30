package com.charge.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
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
import org.jeecgframework.p3.core.utils.common.StringUtils;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;

import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.SalaryEntity;
import com.charge.entity.SalaryExcleEntity;
import com.charge.entity.SalaryOneEntity;
import com.charge.repository.EmployeeDeclareRepository;
import com.charge.repository.EmployeeInfoRepository;
import com.charge.repository.SalaryOneRepository;
import com.charge.repository.SalaryRepository;
import com.charge.service.SalaryService;
import com.charge.utils.Utils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.sql.Date;

import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Title: Controller
 * @Description: 员工薪酬表
 * @author zhangdaihao
 * @date 2018-03-29 13:45:40
 * @version V1.0
 *
 */
@Controller
@RequestMapping("salaryController")
public class SalaryController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SalaryController.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CommonService commonService;

	@Autowired
	private SalaryRepository salaryRepository;
	@Autowired
	private SalaryService salaryService;

	@Autowired
	private SalaryOneRepository salaryOneRepository;

	@Autowired
	private EmployeeDeclareRepository employeeDeclareRepository;

	@Autowired
	private EmployeeInfoRepository employeeInfoRepo;


	/******************************************     修改区域开始    修改人：文世庭           ******************************************/

	/**
	 * 薪酬审批列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "salaryApprovalList")
	public ModelAndView salaryApprovalList(HttpServletRequest request) {
		return new ModelAndView("com/charge/salaryApprovalList");
	}


	/**
	 * 薪酬审批数据列表
	 * @param salaryEntity
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "salaryApprovalDatagrid")
	public void salaryApprovalDatagrid(SalaryEntity salaryEntity,HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		String orgIds = request.getParameter("orgIds");
		List<String> orgIdList = extractIdListByComma(orgIds);
		//获取datagrid对象
		salaryService.setDataGrid(salaryEntity, request.getParameterMap(), dataGrid, orgIdList,new Object[]{4,6,7},0);
		//输出到客户端
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 薪酬审核列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "salaryAuditList")
	public ModelAndView salaryAuditlList(HttpServletRequest request) {
		return new ModelAndView("com/charge/salaryAuditList");
	}
	/**
	 * 薪酬审核数据列表
	 * @param salaryEntity
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "salaryAuditDatagrid")
	public void salaryAuditDatagrid(SalaryEntity salaryEntity,HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		String orgIds = request.getParameter("orgIds");
		List<String> orgIdList = extractIdListByComma(orgIds);
		//获取datagrid对象
		salaryService.setAuditDataGrid(salaryEntity, request.getParameterMap(), dataGrid, orgIdList,new Object[]{2,4,5,7},0);
		//输出到客户端
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 录入完成
	 * @return
	 */
	@RequestMapping(params="complete")
	@ResponseBody
	public Map<String,Object> complete(){
		Map<String, Object> result = salaryService.complete();
		return result;
	}
	/**
	 * 申报*批量通过
	 * @return
	 */
	@RequestMapping(params="salaryDeclare")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String,Object> salaryDeclare(){
		Map<String, Object> result = salaryService.salarBybatchDeclare();
		System.out.println("result .size :"+ result.size() );
		System.out.println("result  :"+ result);
		return result;
	}
	/**
	 * 申报*批量退回
	 * @return
	 */
	@RequestMapping(params="declareReturn")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String,Object> declareReturn(@RequestParam("returnReason") String returnReason){
		Map<String, Object> result = salaryService.declareReturn(returnReason);
		return result;
	}
	/**
	 * 审核* 批量通过
	 * @return
	 */
	@RequestMapping(params="auditPass")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String,Object>auditPass(){
		Map<String, Object> result = salaryService.auditPass();
		return result;
	}

	/**
	 * 审核*批量退回
	 * @return
	 */
	@RequestMapping(params="auditReturn")
	@ResponseBody
	public Map<String,Object> auditReturn(
			@RequestParam("returnReason") String returnReason){
		Map<String, Object> result = salaryService.auditReturn(returnReason);
		return result;
	}

	/**
	 * 审批* 批量通过
	 * @return
	 */
	@RequestMapping(params="batchPass")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String,Object> batchPass(){
		Map<String, Object> result = salaryService.batchPass();
		return result;
	}

	/**
	 * 审批*批量退回
	 * @return
	 */
	@RequestMapping(params="batchReturn")
	@ResponseBody
	public Map<String,Object> batchReturn(
			@RequestParam("returnReason") String returnReason){
		Map<String, Object> result = salaryService.batchReturn(returnReason);
		return result;
	}

	/**
	 * 导出excel
	 * @param departId
	 * @param declareStartDate
	 * @param declareEndDate
	 * @param status
	 * @return
	 */
	@RequestMapping(params="excelExport")
	public void exportExcel(String departId,
			HttpServletResponse response){
		Map<String,Object> result=salaryService.exportDatas(departId,0);
		ServletOutputStream out = null;
		FileInputStream fis = null;
		try {
			String txtUrl = (String) result.get("txtUrl");
			String fileName = (String) result.get("fileName");
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment;filename="
					+new String(fileName.getBytes("utf-8"),"ISO_8859_1"));
			out=response.getOutputStream();
			fis = new FileInputStream(txtUrl);
			byte[] buff = new byte[1024];
			int len = 0;
			while((len=fis.read(buff))>0){
				out.write(buff,0,len);
			}
			out.close();
			fis.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if(null!=fis){
					fis.close();
				}
				if(null!=out){
					out.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	/**
	 * 检测是否有数据导出EXCEL
	 * @return
	 */
	@RequestMapping(params="checkIsExportExcel")
	@ResponseBody
	public Map<String,Object> checkIsExportExcel(String departId){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		List<String> placeOneList = salaryRepository.getPlaceOneGroup(departId,6);
		if(placeOneList.isEmpty()){
			result.put("errCode", -1);
		}
		return result;
	}
	/**
	 * 导出excel
	 * @param departId
	 * @param declareStartDate
	 * @param declareEndDate
	 * @param status
	 * @return
	 */
	@RequestMapping(params="excelExportNew")
	public void exportExcelNew(String departId,
			HttpServletResponse response){
		Map<String,Object> result=salaryService.exportDatasNew(departId,0);
		ServletOutputStream out = null;
		FileInputStream fis = null;
		try {
			String txtUrl = (String) result.get("txtUrl");
			String fileName = (String) result.get("fileName");
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment;filename="
					+new String(fileName.getBytes("utf-8"),"ISO_8859_1"));
			out=response.getOutputStream();
			fis = new FileInputStream(txtUrl);
			byte[] buff = new byte[1024];
			int len = 0;
			while((len=fis.read(buff))>0){
				out.write(buff,0,len);
			}
			out.close();
			fis.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if(null!=fis){
					fis.close();
				}
				if(null!=out){
					out.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	/**
	 * 检测是否有数据导出EXCEL
	 * @return
	 */
	@RequestMapping(params="checkIsExportExcelNew")
	@ResponseBody
	public Map<String,Object> checkIsExportExcelNew(String departId){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		List<String> placeOneList = salaryRepository.getPlaceOneGroupNew(departId);
		if(placeOneList.isEmpty()){
			result.put("errCode", -1);
		}
		return result;
	}

	/**
	 * 处理字符串Int数组
	 * @param status
	 * @return
	 */
	public Integer[] handleArrayByStr(String status){
		List<Integer> result = new ArrayList<Integer>();
		for(int i = 0;i<status.split(",").length;i++){
			if(!Pattern.compile("[^0-9]+").matcher(status.split(",")[i]).find()){
				result.add(Integer.valueOf(status.split(",")[i]));
			}
		}
		return result.toArray(new Integer[result.size()]);
	}


	/******************************************     修改区域结束          ******************************************/


	/**
	 * 员工薪酬表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {

		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		//申报初始化
		declareInit();
		//非超级管理员添加部门条件
		String departId = null;
		//超级管理员、顶层管理员可以看到所有数据
		List<String> topManager = getTopManager();
        if(!"admin".equals(user.getUserName())&&!topManager.contains(user.getUserName())){
        	List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        	departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
        }
        return new ModelAndView("com/charge/salaryList");
	}
	/**
	 * 申报薪酬初始化
	 */
	private void declareInit() {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(null!=user){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        	String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			if(null!=departId){
				//查询当前月是否有数据
				salaryRepository.getSameMonthDeclareAdd(departId);
			}
		}
	}
	/**
	 * easyui AJAX请求数据
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 * (申报或审批人员可以查看到的数据)
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(SalaryEntity salary,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		String orgIds = request.getParameter("orgIds");
        List<String> orgIdList = extractIdListByComma(orgIds);
		CriteriaQuery cq = new CriteriaQuery(SalaryEntity.class, dataGrid);
		cq.eq("deleteFlg", 0);
		cq.in("state", new Object[]{1,2,3});
		//超级管理员、顶层管理员可以看到所有数据
		List<String> topManager = getTopManager();
		if(!"admin".equals(user.getUserName())&&!topManager.contains(user.getUserName())){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        	String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			if(null!=departId){
				orgIdList.add(departId);
			}else{
				//log.info("当前登录用户："+user.getUserName()+"所在部门为空。");
			}
		}
		if(!orgIdList.isEmpty()){
			List<EmployeeInfoEntity> employeeList = employeeInfoRepo.findByDepartIdsAll(orgIdList.toArray(new String[orgIdList.size()]));
			if(!employeeList.isEmpty()){
				Integer[] eids = new Integer[employeeList.size()];
				for(int i = 0;i<employeeList.size();i++){
					eids[i]=employeeList.get(i).getId();
				}
				cq.in("employeeInfo.id",eids);
			}else{
				cq.eq("employeeInfo.id",-99999);
			}
		}
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, salary, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 员工薪酬表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "generateList")
	public ModelAndView generateList(HttpServletRequest request) {

		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		//申报初始化
		declareInit();
		//非超级管理员添加部门条件
		String departId = null;
		//超级管理员、顶层管理员可以看到所有数据
		List<String> topManager = getTopManager();
        if(!"admin".equals(user.getUserName())&&!topManager.contains(user.getUserName())){
        	List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        	departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
        }
        return new ModelAndView("com/charge/salaryGenerateList");
	}
	/**
	 * easyui AJAX请求数据
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 * (录入人员可以查看到的数据)
	 */
	@RequestMapping(params = "generateDatagrid")
	public void generateDatagrid(SalaryEntity salary,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		String orgIds = request.getParameter("orgIds");
        List<String> orgIdList = extractIdListByComma(orgIds);
		CriteriaQuery cq = new CriteriaQuery(SalaryEntity.class, dataGrid);
		cq.eq("deleteFlg", 0);
		cq.in("state", new Object[]{0,1,2,3});
		//超级管理员、顶层管理员可以看到所有数据
		List<String> topManager = getTopManager();
		if(!"admin".equals(user.getUserName())&&!topManager.contains(user.getUserName())){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        	String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			if(null!=departId){
				orgIdList.add(departId);
			}else{
				//log.info("当前登录用户："+user.getUserName()+"所在部门为空。");
			}
		}
		if(!orgIdList.isEmpty()){
			List<EmployeeInfoEntity> employeeList = employeeInfoRepo.findByDepartIds(orgIdList.toArray(new String[orgIdList.size()]));
			if(!employeeList.isEmpty()){
				Integer[] eids = new Integer[employeeList.size()];
				for(int i = 0;i<employeeList.size();i++){
					eids[i]=employeeList.get(i).getId();
				}
				cq.in("employeeInfo.id",eids);
			}else{
				cq.eq("employeeInfo.id",-99999);
			}
		}
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, salary, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除员工薪酬表
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(SalaryEntity salary, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		salary = systemService.getEntity(SalaryEntity.class, salary.getId());
		message = "员工薪酬表删除成功";
		commonService.delete(salary);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}


	/**
	 * 添加员工薪酬表
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(SalaryEntity salary, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(salary.getId())) {
			message = "员工薪酬表更新成功";
			SalaryEntity t = commonService.get(SalaryEntity.class, salary.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(salary, t);
				moneyCalc(t);
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "员工薪酬表更新失败";
			}
		} else {
			message = "员工薪酬表添加成功";
			commonService.save(salary);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * 计算：
	 *
	 * c补贴 C1+C2+C3-特别扣减
	 * 双发地1：公司成本1=工资1+六金（公司负担）1+年终奖1
	 * 双发地2：公司成本2=工资2+六金（公司负担）2+年终奖2
	 * 【个调税1\2】
	 * 【年个税1\2】
	 * 【打卡金额1】=【工资1】-【六金（个人负担）1】-【个调税1】+【年终奖1】-【年个税1】
	 * 【年终奖1】：如果【员工表】里的双发地点2不是空，年终奖1=0，且年终奖2=D年终奖；否则，年终奖2=0，且年终奖1=D年终奖
	 *
	 */
	private void moneyCalc(SalaryEntity t) {
		Double cnumSubsidy = 0.0;
		Double perToneTaxOne =0.0;
		Double perOneAdd = 0.0;
		Double perToneTaxTwo =0.0;
		Double perTwoAdd = 0.0;
		Double clockInOne =0.0;
		Double clockInTwo =0.0;
		Double wageOne =0.0;
		Double wageTwo =0.0;
		Double companyCostOne =0.0;
		Double companyCostTwo =0.0;
		Double c1Subsidy = (Double) (t.getC1ComputerSubsidy()==null?0.0:t.getC1ComputerSubsidy());
		Double c2Subsidy = (Double) (t.getC2OvertimePay()==null?0.0:t.getC2OvertimePay());

		Double c1SubsidyOth = (Double) (t.getC1OtherSubsidy()==null?0.0:t.getC1OtherSubsidy());
		Double c2SubsidyOth = (Double) (t.getC2OtherSubsidy()==null?0.0:t.getC2OtherSubsidy());
		Double c3Subsidy = (Double) (t.getC3OtherSubsidy()==null?0.0:t.getC3OtherSubsidy());
		Double speciaDeduction = (Double) (t.getSpecialDeduction()==null?0.0:t.getSpecialDeduction());

		Double salaryOne = (Double) (t.getSalaryOne()==null?0.0:t.getSalaryOne());
		Double sixOne = (Double) (t.getSixCompanyBurdenOne()==null?0.0:t.getSixCompanyBurdenOne());
		Double yearOne =  (Double) (t.getYearEndBonusOne()==null?0.0:t.getYearEndBonusOne());

		Double salaryTwo = (Double) (t.getSalaryTwo()==null?0.0:t.getSalaryTwo());
		Double sixTwo = (Double) (t.getSixCompanyBurdenTwo()==null?0.0:t.getSixCompanyBurdenTwo());
		Double yearTwo =  (Double) (t.getYearEndBonusTwo()==null?0.0:t.getYearEndBonusTwo());

		Double sixPersonaOne = t.getSixPersonalBurdenOne()==null?0.0:t.getSixPersonalBurdenOne();
		Double perOne = t.getPerToneTaxOne()==null?0.0:t.getPerToneTaxOne();
		Double yearTaxOne = t.getYearTaxPersonalOne()==null?0.0:t.getYearTaxPersonalOne();

		Double sixPersonaTwo = t.getSixPersonalBurdenTwo()==null?0.0:t.getSixPersonalBurdenTwo();
		Double perTwo = t.getPerToneTaxTwo()==null?0.0:t.getPerToneTaxTwo();
		Double yearTaxTwo = t.getYearTaxPersonalTwo()==null?0.0:t.getYearTaxPersonalTwo();

		Double dnumYearEndBonus = t.getDnumYearEndBonus()==null?0.0:t.getDnumYearEndBonus();
		Double yearEndBonusOne = t.getYearEndBonusOne()==null?0.0:t.getYearEndBonusOne();
		Double yearEndBonusTwo = t.getYearEndBonusTwo()==null?0.0:t.getYearEndBonusTwo();
		String placeTwo = t.getPlaceTwo()==null?"":t.getPlaceTwo();

		Utils test = new Utils();
		//年个税yearEndBonusNew
		Double yearEndBonusNew = test.calculateYearEndBonusesIncomeTax(dnumYearEndBonus);
		/*System.out.println("年终奖 :"+ dnumYearEndBonus);*/
		//c补贴 C1+C2+C3-特别扣减
		Double cnumSubsidyNew = (Double) (t.getCnumSubsidy()==null?0.0:t.getCnumSubsidy());
		cnumSubsidy = c1Subsidy+c2Subsidy+c1SubsidyOth+c2SubsidyOth+c3Subsidy-speciaDeduction;
		t.setCnumSubsidy(cnumSubsidy);
		/*System.out.println("c补贴 :"+ cnumSubsidy);*/
		Double cnum = 0.0;
		if(cnumSubsidy>cnumSubsidyNew){
		}
		cnum = cnumSubsidy-cnumSubsidyNew;
		/*System.out.println("c补贴cnum :"+ cnum);*/

		//计算个调税1
		perToneTaxOne =salaryOne+ cnum-sixPersonaOne;
		perOneAdd = test.calculateIndividualIncomeTax(perToneTaxOne);
		//计算个调税2
		perToneTaxTwo =salaryTwo+ cnum-sixPersonaTwo;
		perTwoAdd = test.calculateIndividualIncomeTax(perToneTaxTwo);

		//计算打卡金额1
		clockInOne =salaryOne+cnum+dnumYearEndBonus-sixPersonaOne-perOneAdd-yearEndBonusNew;
		//计算打卡金额2
		clockInTwo =salaryTwo+cnum+dnumYearEndBonus-sixPersonaTwo-perTwoAdd-yearEndBonusNew;

		//计算工资1
		wageOne =salaryOne+cnum;
		//计算工资2
		wageTwo =salaryTwo+cnum;

		//计算公司成本1
		companyCostOne =salaryOne+cnum+sixOne+dnumYearEndBonus;
		//计算公司成本2
		companyCostTwo =salaryTwo+cnum+sixTwo+dnumYearEndBonus;

		if(placeTwo == null || placeTwo =="" && salaryTwo == null || salaryTwo ==0.0){
			//工资1
			t.setSalaryOne(wageOne);
			//年终奖1
			t.setYearEndBonusOne(dnumYearEndBonus);
			//年个税1
			t.setYearTaxPersonalOne(yearEndBonusNew);
			//个调税1
			t.setPerToneTaxOne(perOneAdd);
			//打卡金额1
			t.setTransferSalaryOne(clockInOne);
			//公司成本1
			t.setCompanyCostOne(companyCostOne);
		}else{
			//年终奖2
			t.setYearEndBonusTwo(dnumYearEndBonus);
			//年个税2
			t.setYearTaxPersonalTwo(yearEndBonusNew);
			//个调税2
			t.setPerToneTaxTwo(perTwoAdd);
			//打卡金额2
			t.setTransferSalaryTwo(clockInTwo);
			//工资2
			t.setSalaryTwo(wageTwo);
			/*System.out.println("工资 :"+ wageTwo);*/
			//公司成本2
			t.setCompanyCostTwo(companyCostTwo);
		}
	}

	/**
	 * 员工薪酬表列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(SalaryEntity salary, HttpServletRequest req,String departId,Integer status) {
		if (StringUtil.isNotEmpty(salary.getId())) {
			Long idp = salary.getId();
			Integer str = salaryRepository.getStatusCount(idp);
			if(str == 2 || str == 3){
				salary = commonService.getEntity(SalaryEntity.class, salary.getId());
				req.setAttribute("salaryPage", salary);
				return new ModelAndView("com/charge/salaryAdd");
			}else{
				salary = commonService.getEntity(SalaryEntity.class, salary.getId());
				req.setAttribute("salaryPage", salary);
				return new ModelAndView("com/charge/salary");
			}
		}
		return null;
	}
	@RequestMapping(params = "salaryAddUpdate")
	public ModelAndView salaryAddUpdate(SalaryEntity salary, HttpServletRequest req,String departId,Integer status) {
		if (StringUtil.isNotEmpty(salary.getId())) {
			Long idp = salary.getId();
			Integer str = salaryRepository.getStatusCount(idp);
			salary = commonService.getEntity(SalaryEntity.class, salary.getId());
			req.setAttribute("salaryPage", salary);
			return new ModelAndView("com/charge/salaryAdd");
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SalaryEntity> list() {
		List<SalaryEntity> listSalarys=commonService.getList(SalaryEntity.class);
		return listSalarys;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SalaryEntity task = commonService.get(SalaryEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SalaryEntity salary, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SalaryEntity>> failures = validator.validate(salary);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.save(salary);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		Long id = salary.getId();
		URI uri = uriBuilder.path("/rest/salaryController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SalaryEntity salary) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SalaryEntity>> failures = validator.validate(salary);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.saveOrUpdate(salary);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		commonService.deleteEntityById(SalaryEntity.class, id);
	}

/*add	*/
	/**
	 * 员工申报表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "declarelist")
	public ModelAndView declarelist(HttpServletRequest request) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		//申报初始化
		declareInit();
		//非超级管理员添加部门条件
		String departId = null;
		//超级管理员、顶层管理员可以看到所有数据
		List<String> topManager = getTopManager();
        if(!"admin".equals(user.getUserName())&&!topManager.contains(user.getUserName())){
        	List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        	departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
        }
		Map<String,Object> result = salaryService.getTableHeaderCalc(departId,new Integer[]{1});
		return new ModelAndView("com/charge/salaryDeclareList").addObject("hds", result);
	}
	/**
	 * easyui AJAX请求数据
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "declareDatagrid")
	public void declareDatagrid(SalaryEntity salaryDeclare,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		CriteriaQuery cq = new CriteriaQuery(SalaryEntity.class, dataGrid);
		cq.eq("deleteFlg", 0);
		cq.in("state", new Object[]{0});
		String orgIds = request.getParameter("orgIds");
        List<String> orgIdList = extractIdListByComma(orgIds);
		//超级管理员、顶层管理员可以看到所有数据
		List<String> topManager = getTopManager();
		if(!"admin".equals(user.getUserName())&&!topManager.contains(user.getUserName())){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        	String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			if(null!=departId){
				orgIdList.add(departId);
			}else{
				//log.info("当前登录用户："+user.getUserName()+"所在部门为空。");
			}
		}
		if(!orgIdList.isEmpty()){
			List<EmployeeInfoEntity> employeeList = employeeInfoRepo.findByDepartIds(orgIdList.toArray(new String[orgIdList.size()]));
			if(!employeeList.isEmpty()){
				Integer[] eids = new Integer[employeeList.size()];
				for(int i = 0;i<employeeList.size();i++){
					eids[i]=employeeList.get(i).getId();
				}
				cq.in("employeeInfo.id",eids);
			}else{
				cq.eq("employeeInfo.id",-99999);
			}
		}
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, salaryDeclare, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 获取顶层管理员
	 * @return
	 */
	public List<String> getTopManager(){
		List<String> topManager = new ArrayList<String>();
		List<TSTypegroup> tsTypeGroupList = commonService.findByProperty(TSTypegroup.class, "typegroupcode", "topManager");
		if(!tsTypeGroupList.isEmpty()&&!tsTypeGroupList.get(0).getTSTypes().isEmpty()){
			for(int i =0;i<tsTypeGroupList.get(0).getTSTypes().size();i++){
				TSType tsType = tsTypeGroupList.get(0).getTSTypes().get(i);
				//获取人员信息
				List<TSUser> tsuserList = commonService.findByProperty(TSUser.class,"userName",tsType.getTypecode());
				if(!tsuserList.isEmpty()){
					topManager.add(tsuserList.get(0).getUserName());
				}
			}
		}
		return topManager;
	}

	/**
	 * 外派生成数据
	 */
	@RequestMapping(params = "add")
	@ResponseBody
	public Object add(String dateString) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {
			salaryService.add(dateString);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("errCode", 1);
			return result;
		}
		result.put("errCode", 0);
		return result;
	}
	/**
	 * 生成数据
	 */
	@RequestMapping(params = "addAll")
	@ResponseBody
	public Object addAll(String dateString) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {
			salaryService.addAll(dateString);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("errCode", 1);
			return result;
		}
		result.put("errCode", 0);
		return result;
	}
}
