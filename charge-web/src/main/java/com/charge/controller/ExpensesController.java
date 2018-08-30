package com.charge.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.p3.core.utils.common.StringUtils;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.charge.entity.CustomerInfoEntity;
import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.ExpensesEntity;
import com.charge.repository.CommonRepository;
import com.charge.repository.EmployeeDeclareRepository;
import com.charge.repository.EmployeeInfoRepository;
import com.charge.repository.ExpensesRepository;
import com.charge.service.EmployeeInfoService;
import com.charge.service.SixGoldService;
import com.charge.service.ExpensesDeclareService;

/**
 * @Title: Controller
 * @Description: 经费申请
 * @author sunyj
 * @date 2018-03-19 16:44:26
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/expensesController")
public class ExpensesController extends BaseController {
	private final static Logger log = Logger.getLogger(ExpensesController.class);

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExpensesController.class);

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
	private CommonRepository jeecgRepo;
	@Autowired
	private ExpensesDeclareService expensesDeclareService;
	@Autowired
	private EmployeeDeclareRepository employeeDeclareRepo;
	@Autowired
	private ExpensesRepository expensesRepo;


	/**
	 * 经费申请信息更改判断
	 * j 6-7
	 * @return
	 */
	public Integer saveReason(ExpensesEntity expenses, ExpensesEntity t){
		Integer i = 0;
		StringBuffer reason =new StringBuffer("申请理由：");
		if (!DateUtils.isSameDay(t.getStartDate(),expenses.getStartDate())) {
			//起始日期
			i=1;
			reason.append("活动起始日期,");
		}
		if (expenses.getNumberDate()!=(t.getNumberDate())) {
			//活动日数
			i=1;
			reason.append("活动天数,");
		}
		if (!expenses.getTheme().equals(t.getTheme())) {
			//活动主题
			i=1;
			reason.append("活动主题,");
		}
		if (!expenses.getContent().equals(t.getContent())) {
			//内容
			i=1;
			reason.append("活动内容,");
		}
		if (expenses.getMoney()!=(t.getMoney())) {
			//申请金额
			i=1;
			reason.append("申请金额,");
		}
		if (expenses.getNumberPeople()!=(t.getNumberPeople())) {
			//活动人数
			i=1;
			reason.append("活动人数,");
		}
		if (!expenses.getAverage().equals(t.getAverage())) {
			//人均消费
			i=1;
			reason.append("人均消费,");
		}
		if (!expenses.getNamePeople().equals(t.getNamePeople())) {
			//参与人员
			i=1;
			reason.append("参与人员,");
		}

		if(i==1){
			reason.append("发生改变");
		}
		return i;
	}

	/**
	 * 添加经费申请表
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ExpensesEntity expenses, HttpServletRequest request) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		//超级管理员、顶层管理员可以自己选择更改部门
		String orgIds = request.getParameter("orgIds");
		String orgCode = null;//部门编号
		List<String> topManager = employeeInfoService.getTopManager();
		if(!"admin".equals(user.getUserName())&&!topManager.contains(user.getUserName())){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
			orgIds = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			orgCode = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getOrgCode():null;
		}
		List<String> orgIdList = extractIdListByComma(orgIds);
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(expenses.getId())) {
			message = "经费申请表更新成功";
			ExpensesEntity t = commonService.get(ExpensesEntity.class, expenses.getId());
			//if(t.getEditors().equals(expenses.getEditors())){
			try {
				Integer lookUp = saveReason(expenses,t);
				Integer oldDeclareStatus = t.getDeclareStatus();
				MyBeanUtils.copyBeanNotNull2Bean(expenses, t);
				t.setAverage(t.getMoney()/t.getNumberPeople());
				//导入经费申请表职成功，导入申请表不可修改
				if(t.getDeclareStatus()==100){//目前无用
					t.setDeclareStatus(8);
				}else{
					if (lookUp == 0) {
						t.setDeclareStatus(oldDeclareStatus);
					}else{
						if (t.getDeclareStatus()!=1) {
							t.setDeclareStatus(1);
						}
					}
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				String month = request.getParameter("startDate");
					Date date = sdf.parse(month);
					t.setStartMonth(date);
				/*if(expenses.getDeclareStatus()==1&&jeecgRepo.findMyRoleByUserName(user.getUserName()).equals("t_control")){
					t.setDeclareStatus(8);
				}*/
				if(t.getDeclareStatus()==1&&(jeecgRepo.findMyRoleByUserName(user.getUserName())).equals("t_check")){
					t.setDeclareStatus(3);
				}
				t.setExpensesDelFlg(0);
				if(null!=user){
					t.setLastModifiedBy(user.getUserName());
				}
				t.setLastModifiedDate(new Date());
				t.setDeclareReturnreason("");
				/**
				 * 需要解释这一步   更改编辑限制标记吗？
				 *
				 * */
				t.setEditors(UUID.randomUUID().toString().replace("-", "").toLowerCase());
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "经费申请表更新失败";
			}
			//}else{
			//	message = "您所编辑的经费申请表已发生改变，经费申请更新失败";
			//}
		} else {
			message = "经费申请表添加成功";
			//对是否是同一部门进行判断
			if(!orgIdList.isEmpty()){
				TSDepart depart = commonService.get(TSDepart.class, orgIdList.get(0));
				if(org.springframework.util.StringUtils.isEmpty(expenses.getDepartmentId())){
					expenses.setDepartmentId(depart.getId());
				}
			}

			/*if(!jeecgRepo.findMyRoleByUserName(user.getUserName()).equals("t_control")){
				expenses.setReporterId(user.getUserName());
			}*/
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String month = request.getParameter("startDate");
				Date date;
				try {
					date = sdf.parse(month);
					expenses.setStartMonth(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//如果没有传入Inputername，则设置为自己，如果为导入，则设置为空
				expenses.setInputerId(user.getUserName());
				expenses.setReporterId("无");
				expenses.setCheckerId("无");
			/*if(!StringUtils.isNotEmpty(expenses.getReporterId())){
				expenses.setReporterId(user.getRealName());
			}*/
			/**
			 * 如果操作为审计部门，设置录入者，审核者为操作用户，申报者置空
			 * */
			if(expensesDeclareService.getUserRoleCold().equals(3)){
				expenses.setInputerId(user.getUserName());
				expenses.setReporterId("无");
				expenses.setCheckerId(user.getUserName());
				//expenses.setDeclareStatus(8);
			}
			//设置发薪地2工资，发薪地2，基本工资和绩效工资
			/*if(null!=expenses.getaStandardSalary()&&null!=employeeInfo.getA1Payment()){
				expenses.setA2Payment(Math.round((employeeInfo.getaStandardSalary()-employeeInfo.getA1Payment())*100)/100.0);
			}*/
			//设置人均消费
			if(null!=expenses.getMoney()&&null!=expenses.getNumberPeople()){
				expenses.setAverage((expenses.getMoney()/expenses.getNumberPeople()));
			}
			expenses.setExpensesDelFlg(0);
			if(null!=user){
				expenses.setCreatedBy(user.getUserName());
			}
			//新增权限判断设置setDeclareStatus
			//若录入者为总监，则经费申请表状态为录入完成    若录入者为审计，则申请状态为审核待处理
			/*if(!StringUtil.isNotEmpty(expenses.getDeclareStatus())){
				if (employeeInfoService.getUserRoleCold().equals(1) ) {
					expenses.setDeclareStatus(1);
					expenses.setEmployeeFlag(0);
				}else {
					expenses.setDeclareStatus(1);
					expenses.setEmployeeFlag(1);
				}
			}else if(expenses.getDeclareStatus()==1){
				if (expensesService.getUserRoleCold().equals(1) ) {
					expenses.setDeclareStatus(1);
					expenses.setEmployeeFlag(0);
				}else {
					expenses.setDeclareStatus(1);
					expenses.setEmployeeFlag(1);
				}*/
			if(expensesDeclareService.getUserRoleCold().equals(2)){
				expenses.setDeclareStatus(1);
			}else if(expensesDeclareService.getUserRoleCold().equals(3)){
				expenses.setDeclareStatus(3);
			}

				//审计录入，设为审核待处理,部门为审计
				if(jeecgRepo.findMyRoleByUserName(user.getUserName()).equals("t_check")){
					expenses.setDeclareStatus(3);
					//expenses.setEmployeeFlag(1);
					//expenses.setQuitstatus(1);
					expenses.setDepartmentId(user.getDepartid());
				}

			/*if(null == expenses.getQuitstatus()){//入职离职状态，不需要
				expenses.setQuitstatus(0);
			}*/
			expenses.setEditors(UUID.randomUUID().toString().replace("-", "").toLowerCase());
			expenses.setCreatedDate(new Date());
			expenses.setLastModifiedDate(new Date());
			expenses.setLastModifiedBy(user.getUserName());
			commonService.save(expenses);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);

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
	 * 删除经费申报表
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
			j.setMsg("未选中任何数据");
			return j;
		}
		//切割 传来的 员工申报记录  id
		String[] sids = ids.split(",");
		List<Integer> lids = new ArrayList<Integer>();
		for(String sid : sids) {
			lids.add(Integer.parseInt(sid));
		}
		ExpensesEntity expensesDeclare = new ExpensesEntity();
		StringBuffer sb = new StringBuffer();
		List<String> succ = new ArrayList<>();
		List<String> fail = new ArrayList<>();
		for(int i=0;i<lids.size();i++) {
			commonService.deleteEntityById(ExpensesEntity.class,lids.get(i));
		}
		message = "经费申请删除成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 经费编辑页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(ExpensesEntity expensesDeclare, HttpServletRequest request,int id) {
		if (StringUtil.isNotEmpty(id)) {
			expensesDeclare = commonService.getEntity(ExpensesEntity.class, id);
			request.setAttribute("expensesDeclarePage", expensesDeclare);
			/*List<TSDepart> departList = commonService.findHql("from TSDepart t where t.id=?", expensesDeclare.getEmployeeInfo().getDepartment());*/
			//List<TSDepart> departList = commonService.findHql("from TSDepart t where t.id=?", expensesDeclare.getDepartmentid());
			//request.setAttribute("departName",departList.get(0).getDepartname());
			/*if(expensesDeclare.getEmployeeType()==1) { //0外派，1本部
				return new ModelAndView("com/charge/employeeDeclareInner");
			}*/
		}

		return new ModelAndView("com/charge/expensesDeclare");
	}
	/**
	 * 经费录入页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate1")
	public ModelAndView addorupdate1(ExpensesEntity expensesDeclare, HttpServletRequest request) {

		return new ModelAndView("com/charge/expensesDeclare");
	}

	/**
	 * 经费申请录入列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "expensesDeclareList")
	public ModelAndView employeeInfoDeclareList(HttpServletRequest request) {

		try {

			List<Object[]> myDeparts = jeecgRepo.findMyDeptInfo();
			String depts = "";
			for(int i=0;i<myDeparts.size();i++) {
				Object[] obj = myDeparts.get(i);
				depts+=obj[1].toString()+"_"+obj[0].toString();
				if(i<myDeparts.size()-1) {
					depts+=",";
				}
			}
			request.setAttribute("depts",depts);
        }catch(Exception e) {
			e.printStackTrace();
		}finally{
			return new ModelAndView("com/charge/expensesDeclareList");
		}

	}
	/**
	 * 经费申请审批列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "expensesControllerList")
	public ModelAndView expensesControllerList(HttpServletRequest request) {

		try {

			List<Object[]> myDeparts = jeecgRepo.findMyDeptInfo1();
			String depts = "";
			for(int i=0;i<myDeparts.size();i++) {
				Object[] obj = myDeparts.get(i);
				depts+=obj[1].toString()+"_"+obj[0].toString();
				if(i<myDeparts.size()-1) {
					depts+=",";
				}
			}
			request.setAttribute("depts",depts);
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			return new ModelAndView("com/charge/expensesControllerList");
		}

	}

	/**
	 * 经费申请审核列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "expensesCheckeList")
	public ModelAndView expensesCheckeList(HttpServletRequest request) {

		try {

			List<Object[]> myDeparts = jeecgRepo.findMyDeptInfo1();
			String depts = "";
			for(int i=0;i<myDeparts.size();i++) {
				Object[] obj = myDeparts.get(i);
				depts+=obj[1].toString()+"_"+obj[0].toString();
				if(i<myDeparts.size()-1) {
					depts+=",";
				}
			}
			request.setAttribute("depts",depts);
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			return new ModelAndView("com/charge/expensesCheckeList");
		}

	}
	private Date timeCondition1;
	@RequestMapping(params = "clearMonth")
	public void clearMonth() {
		this.timeCondition1=null;
		//this.timeCondition2=null;
		//this.timeCondition3=null;
		//this.timeCondition4=null;
		//this.timeCondition5=null;
		//this.timeConditionController=null;
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

	@RequestMapping(params = "expensesDatagrFindbyMonth1")
	public void expensesDatagrFindbyMonth1(ExpensesEntity expensesDeclare,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String month;
		if(request.getParameter("month")!=null&&!"".equals(request.getParameter("month"))) {
			month = request.getParameter("month");
		}else {
			Date date = timeCondition1==null?new Date():timeCondition1;
			month = sdf.format(date);
		}
		try {
			Date date = sdf.parse(month);
			expensesDeclare.setStartMonth(date);
			timeCondition1=date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date date1 = new Date();
		request.setAttribute("date1", date1);
		dataGrid.setField("id,theme,startDate,numberDate,money,numberPeople,average,departmentId,declareStatus,declareReturnReason,inputerId,reporterId,checkerId,createdBy");
		//排序
		if("inputerName".equals(dataGrid.getSort())) {
			dataGrid.setSort("inputerId");
		}else if("reporterName".equals(dataGrid.getSort())) {
			dataGrid.setSort("reporterId");
		}else if("checkerName".equals(dataGrid.getSort())){
			dataGrid.setSort("checkerId");
		}
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
	    String myRoleName = employeeDeclareRepo.findMyRole(user.getId());

		 if("t_input_m".equals(myRoleName)||"t_report".equals(myRoleName)){ //管理部门/业务部门：  录入完成1   审核拒绝 2  申报中（审核待处理3  审批拒绝4 审批待处理 5） 审批通过 6
		    	expensesDeclareService.setDataGrid2(expensesDeclare, request.getParameterMap(), dataGrid, new Integer[]{1,3,2,4,5,6}, 0,true);
		    }else{
		    	if("t_check".equals(myRoleName)){ //审计部门：  审核待处理3   审批拒绝4   申报中（   审批待处理 5） 审批通过 6   审核录入待处理 8
			    	expensesDeclareService.setDataGrid2(expensesDeclare, request.getParameterMap(), dataGrid, new Integer[]{1,3,2,4,5,6,8}, 0,true);
			    }else{ //控制部门：  审批待处理 5  审批通过 6
			    	expensesDeclareService.setDataGrid2(expensesDeclare, request.getParameterMap(), dataGrid, new Integer[]{1,3,2,4,5,6,8}, 0,true);
			    }
		    }
		//获取申报审核审批的对应姓名
			Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
	        List<ExpensesEntity> ExpensesEntitys = dataGrid.getResults();
	        for(ExpensesEntity temp: ExpensesEntitys){
		//此为针对原来的行数据，拓展的新字段
	        Map m = new HashMap();
	        List<TSUser> inputer =systemService.findHql("from TSUser t where t.userName=?", new Object[]{temp.getInputerId()});
	        if(null == inputer || inputer.size() ==0){
	        	m.put("inputerName", "无");
	        }else{
	        	m.put("inputerName", inputer.get(0).getRealName());
	        }
	        List<TSUser> reporter =systemService.findHql("from TSUser t where t.userName=?", new Object[]{temp.getReporterId()});
	        if(null == reporter || reporter.size() ==0){
	        	m.put("reporterName", "无");
	        }else{
	        	m.put("reporterName", reporter.get(0).getRealName());
	        }
	        List<TSUser> checker =systemService.findHql("from TSUser t where t.userName=?", new Object[]{temp.getCheckerId()});
	        if(null == checker || checker.size() ==0){
	        	m.put("checkerName", "无");
	        }else{
	        	m.put("checkerName", checker.get(0).getRealName());
	        }
	        extMap.put(temp.getId().toString(), m);
     }
		//输出到客户端   输出内容：List<EmployeeDeclareEntiry>  dataGrid.results
		TagUtil.datagrid(response, dataGrid,extMap);
	}

	/**
	 *
	 * 录入完成，申报功能
	 * @return
	 */
	@RequestMapping(params="entryComplete")
	@ResponseBody
	public Map<String,Object> entryComplete(@RequestParam("ids") String ids){
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", "0");
		if(user == null) {
			result.put("errCode", "-1");
			log.info("当前用户未登录，无法更新经费申请状态");
			return result;
		}
		if(ids==null || "".equals(ids)) {
			// 未选中 数据
			result.put("errCode", "-1");
			log.info("您未选中任何数据");
		}else {
			StringBuffer expensesSb = new StringBuffer();
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
			String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			List<ExpensesEntity> espensesDeclareList = null;


			//更新 选中 id 经费的状态
			boolean flag = true;
			//切分 expensesDeclareId
			String[] strs = ids.split(",");
			List<Integer> lids = new ArrayList<Integer>();
			for(String str : strs) {
				lids.add(Integer.parseInt(str));
			}
			/*for(Integer id : lids) {
				ExpensesEntity expensesDeclare = employeeDeclareRepo.findByExpensesId(id);
				//如果 选中 员工 状态为 录入中（不是 录入完成）
				if(employeeDeclare.getDeclareStatus()==0) {
					flag = false; break;
				}
			}*/
			if(flag) {
				int declareStatus = 1;
				String myRoleName = employeeDeclareRepo.findMyRole(user.getId());
				if("t_report".equals(myRoleName)||"t_input_m".equals(myRoleName)) {  //如果 角色为 t_report/t_input_m 则说明是总监申报，申报直接到checker
					declareStatus = 3;
				}
				if("t_check".equals(myRoleName)) {  //如果 角色为 t_check 则说明是审核通过，申报直接到controller
					declareStatus = 5;
				}
				if("t_control".equals(myRoleName)) {//如果 角色为 t_check 则说明是审批通过，申报直接到审批通过状态
					declareStatus = 6;
				}
				//依次 修改  选中经费的 申报状态
				for(Integer id : lids) {
					int newStatus = declareStatus;
					ExpensesEntity expensesDeclare = expensesRepo.findByExpensesId(id);
					if(null!=expensesDeclare){
						/*if(newStatus == 4&&employeeDeclare.getEmployeeType()==1) {
							employeeDeclare.setInputerId(user.getUserName());
							newStatus=6;
						}*/
						if(newStatus == 3) {
							expensesDeclare.setReporterId(user.getUserName());
							//newStatus=3;
						}
						if(newStatus == 5) {
							expensesDeclare.setCheckerId(user.getUserName());
							//newStatus=5;
						}
						expensesDeclareService.updateDeclareStatus(user, expensesDeclare, newStatus);
						expensesSb.append(expensesDeclare.getId()+",");
						log.info("经费申报更改状态："+expensesDeclare.getDeclareStatus()+"成功");
					}else{
						result.put("errCode", "-1");
						log.info("经费申报信息未找到，ID="+id);
					}
				}
			}else {
				result.put("errCode", "1");
				log.info("选中 经费录入未完成");
			}
		}
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
		Map<String, Object> result = expensesDeclareService.batchReturn2(lids, returnReason,type);
		return result;
	}

}