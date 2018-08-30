package com.charge.service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.charge.entity.CustomerInfoEntity;
import com.charge.entity.EmployeeDeclareCopyEntity;
import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.ProjectCopyEntity;
import com.charge.entity.ProjectEntity;
import com.charge.repository.CommonRepository;
import com.charge.repository.ProjectRepository;

@Service
@Transactional
public class ProjectService {
	private static final Logger log = Logger.getLogger(ProjectService.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private ProjectRepository projectRepo;

	@Autowired
	private CommonRepository commonRepository;

	@Autowired
	private SystemService systemService;

	@Autowired
	private EmailConfigService emailConfigService;
	@Autowired
	private EmployeeDeclareService employeeDeclareService;
	@Autowired
	private EmployeeDeclareCopyService employeeDeclareCopyService;

	/**
	 * 得到录入者 IntputerName
	 * @return
	 */
	public String getIntputerName(ProjectEntity projectEntity ) {
		TSBaseUser input = systemService.findUniqueByProperty(TSBaseUser.class,"userName", projectEntity.getInputerId());
		if(input == null||input.getRealName()==null){
			return null;
		}
		return input.getRealName();
	}

	/**
	 * 获取部门下的数据
	 * j 5-11
	 * @param employeeDeclare
	 * @param dataGrid
	 * @param parameterMap
	 */
	public void setDataGriddepart(ProjectEntity project, Map<String,String[]> parameterMap,DataGrid dataGrid,Integer flag,Integer declareStatus,Integer lv){
		CriteriaQuery cq = new CriteriaQuery(ProjectEntity.class,dataGrid);
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(StringUtil.isNotEmpty(declareStatus)){
			if(declareStatus>lv){
				cq.ge("projectStatus", declareStatus);
			}else if(declareStatus<lv){
				cq.between("projectStatus",5,declareStatus);
			}
		}
		TSDepart depart = commonService.getEntity(TSDepart.class, user.getDepartid());
		List<TSDepart > departs = commonRepository.subdivision(depart);
		String[] departIds = new String[departs.size()];
		int i = 0;
		for(TSDepart dp : departs){
			departIds[i] = dp.getId();
			i++;
		}
		cq.eq("delFlage", 0);
		cq.in("projectDepartment",departIds);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, project, parameterMap);
		commonService.getDataGridReturn(cq, true);
	}
	/**
	 * 获取全部数据
	 * j 5-8
	 * @param employeeDeclare
	 * @param dataGrid
	 * @param parameterMap
	 */
	public void setDataGridAll(ProjectEntity project, Map<String,String[]> parameterMap,DataGrid dataGrid,Integer declareStatus){
		CriteriaQuery cq = new CriteriaQuery(ProjectEntity.class,dataGrid);
		if(StringUtil.isNotEmpty(declareStatus)){
			cq.ge("declareStatus", declareStatus);
		}
		cq.eq("delFlage", 0);
		//cq.addOrder("declareStatus", SortDirection.asc);
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);

		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, project, parameterMap);
		commonService.getDataGridReturn(cq, true);
	}

	/**
	 * 得到权限
	 * 加逻辑
	 * 得到权限名 getdeclareStatus or getUserRoleCode
	 * @return
	 */
	public Integer getUserRoleCold() {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Integer cord = null;
		if(null!=user){
			List<TSRoleUser> tsRoleList =commonService.findHql("from TSRoleUser t where t.TSUser.id=?", new Object[]{user.getId()});//id,得到TSrole对象
			for(TSRoleUser ts:tsRoleList ){
				if(ts.getTSRole().getRoleCode().equals("t_input")){
					cord = 1;
				}else if (ts.getTSRole().getRoleCode().equals("t_report")) {
					cord = 2;
				}else if (ts.getTSRole().getRoleCode().equals("t_check")) {
					cord = 3;
				}else if (ts.getTSRole().getRoleCode().equals("t_control")){
					cord = 4;
				}else if(ts.getTSRole().getRoleCode().equals("t_input_m")){
					cord = 2;
				}else {
					cord = 1;
				}
			}
		}
		return cord;
	}

	/**
	 * 更新申报状态
	 * 5-9 j
	 * 1+2 / 2+1
	 * @param user
	 * @param declareStatus
	 *
	 */
	@Transactional(readOnly=false)
	public StringBuffer updateDeclareStatus(TSUser user,List<ProjectEntity> projectList) {
		//批量更新状态
		StringBuffer insuranceMassage = new StringBuffer();
		StringBuffer errorName = new StringBuffer();
		Integer newchangeStatus = null;
		Date projectMonth = projectList.size()==0?new Date():projectList.get(0).getProjectMonth();
		for(int i = 0;i<projectList.size();i++){
			ProjectEntity project = projectList.get(i);
//			if(getUpdate(employeeInfo)){
				if(((project.getProjectStatus())%2)!=0 ){
					newchangeStatus= project.getProjectStatus()-1;
				}else if(project.getProjectStatus()==2){
					newchangeStatus= project.getProjectStatus()-1;
					project.setBackUpFlag(1);
				}else{
					newchangeStatus= project.getProjectStatus()-2;
				}
				project.setProjectStatus(newchangeStatus);
				if(getUserRoleCold().equals(3)){
					project.setReporterId(user.getUserName());
				}else if(getUserRoleCold().equals(4)){
					project.setControllerId(user.getUserName());
				}else if(getUserRoleCold().equals(2)){
					project.setReporterId(user.getUserName());
				}
				project.setLastModifiedBy(user.getUserName());
				project.setLastModifiedDate(new Date());
				project.setProjectReturnreason(null);
				projectRepo.update(project);
				if(!getUserRoleCold().equals(4)){
					if (insuranceMassage.length()>0)
						insuranceMassage.append("，");
					CustomerInfoEntity cus = systemService.getEntity(CustomerInfoEntity.class,Integer.parseInt(project.getProjectCustomer1()));
					insuranceMassage.append(cus.getCode());
				}else{
					employeeDeclareCopyService.projectBackUp(project);
				}
			}
		if(insuranceMassage.length()>0){
			Map<String, String> msg = new HashMap<>();
			Integer lv = (newchangeStatus+1)/2;
			TSUser inputNow = new TSUser();
			if(lv ==1){
				inputNow = commonRepository.findUserByRoleName("t_control").get(0);
			}else if(lv==2){
				inputNow =commonRepository.findUserByRoleName("t_check").get(0);
			}else{
				inputNow = commonRepository.getManagerNow(lv, projectList.get(0).getProjectDepartment(), projectList.get(0).getInputerId());
			}
			msg.put("subject", "收支申报通知");
			msg.put("content","收支信息已申报，请及时处理。<br><br>月份："+(projectMonth.getMonth()+1)+"月<br>申报："+ user.getRealName()+"<br><br>< 收支运营 SaaS >");
			emailConfigService.mailSend(inputNow.getEmail(),msg);
		}
		return errorName;
	}


	/**
	 * 申报和通过ID id,部门 departId，离职状态 delFlg
	 * j-5-8
	 * @return
	 */

	public Map<String, Object> declareAndPass(List<Integer> id) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		StringBuffer name = new StringBuffer();
		//将当前用户部门下的所有本月待申报的数据加载出来
		List<ProjectEntity> projectList = null;
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(null!=user){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});//用户信息
        	String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			if(null==departId){
				result.put("errMsg", "当前登录用户："+user.getUserName()+"所在部门为空");
				result.put("errCode", -1);
			}else{
				projectList = projectRepo.findByDeclareStatusId(id);
				if(!projectList.isEmpty()){
					name = updateDeclareStatus(user,projectList);
				}else{
					result.put("errMsg", "没有需要的数据");
					result.put("errCode", -1);
				}
			}
		}else{
			String errMsg = "当前用户未登录，无法进行此操作";
			log.info(errMsg);
			result.put("errMsg", errMsg);
			result.put("errCode", -1);
		}
//		if(0<name.length()){
//			result.put("errMsg", "员工"+name+"在当前状态无法进行此操作");
//			result.put("errCode", -1);
//		}
		return result;
	}

	public Integer getNameRoleCold(TSUser user) {
		Integer cord = null;
		if(null!=user){
			List<TSRoleUser> tsRoleList =commonService.findHql("from TSRoleUser t where t.TSUser.id=?", new Object[]{user.getId()});//id,得到TSrole对象
			for(TSRoleUser ts:tsRoleList ){
				if(ts.getTSRole().getRoleCode().equals("t_input")){
					cord = 1;
				}else if (ts.getTSRole().getRoleCode().equals("t_report")) {
					cord = 2;
				}else if (ts.getTSRole().getRoleCode().equals("t_check")) {
					cord = 3;
				}else if (ts.getTSRole().getRoleCode().equals("t_control")){
					cord = 4;
				}else if (ts.getTSRole().getRoleCode().equals("t_input_m")){
					cord = 5;
				}else if (ts.getTSRole().getRoleCode().equals("t_access")){
					cord = 6;
				}else{
					cord = 1;
				}
			}
		}
		return cord;
	}

	/**
	 * 得到当前用户处理中状态码
	 * @return
	 */
	public Integer getStatus(TSUser user) {
		Integer declareStatus = 0;
		Integer role = getNameRoleCold(user);
		Integer addRole = 0;
		TSDepart depart = commonService.getEntity(TSDepart.class, user.getDepartid());
		if(role == 1)
			addRole = 2;
		Integer lv = commonRepository.getDepartGread(depart);
		if (lv == 1) {
			declareStatus = 2;
		}else{
			declareStatus = lv*2+addRole;
		}
		return declareStatus;
	}

	/**
	 * 更新退回状态
	 * 5-9 j
	 * 3-1 / 4-2
	 * @param user
	 * @param declareStatus
	 *
	 */
	@Transactional(readOnly=false)
	public StringBuffer returnDeclareStatus(TSUser user,List<ProjectEntity> projectList,String returnReason) {
		//批量更新状态
		StringBuffer errorname = new StringBuffer();
		StringBuffer succeedName = new StringBuffer();
		Integer newchangeStatus = null;
		Map<String,Map<String, String>> tplContent = new HashMap<>();// 收件人邮箱   发送内容Map<title:value;content:value>
		for(int i = 0;i<projectList.size();i++){
			ProjectEntity project = projectList.get(i);
			TSUser input = systemService.findUniqueByProperty(TSUser.class, "userName", project.getInputerId());
			if(user.getUserName().equals(project.getInputerId())){
				errorname.append("，");
			}else{
				if(!project.getProjectStatus().equals(2)){
					if(((project.getProjectStatus())%2)==0 ){
						newchangeStatus= project.getProjectStatus()+1;
					}else{
						newchangeStatus= project.getProjectStatus()+2;
					}
					project.setProjectStatus(newchangeStatus);
					project.setProjectReturnreason(returnReason);
					project.setLastModifiedDate(new Date());
					if(user.getUserName().equals(project.getInputerId()))
						project.setProjectStatus(getStatus(user)-1);
					projectRepo.update(project);
				}else{
					newchangeStatus=3;
					project.setProjectStatus(3);
					project.setProjectReturnreason(returnReason);
					project.setLastModifiedDate(new Date());
					projectRepo.update(project);
				}
			}
			Integer lv = (newchangeStatus+1)/2;
			List<TSUser> list = new ArrayList<TSUser>();
			if(lv ==1){
				list = commonRepository.findUserByRoleName("t_control");
			}else if(lv==2){
				list =commonRepository.findUserByRoleName("t_check");
			}else{
				TSUser toSend = commonRepository.getManagerNow(lv, project.getProjectDepartment(), project.getInputerId());
				list.add(toSend);
			}
			CustomerInfoEntity cus = systemService.getEntity(CustomerInfoEntity.class, Integer.parseInt(project.getProjectCustomer1()));
			if(list.size()!=0) {
				for(TSUser managerNow : list) {
					String mail = managerNow.getEmail();
					if(tplContent.containsKey(mail)) { //包含此 email  //去掉人名
						StringBuffer sb =new StringBuffer(tplContent.get(mail).get("content"));
	            		sb.append("<br><br>月份："+(project.getProjectMonth().getMonth()+1)+"月<br>顾客："
						+cus.getCode()+"<br>拒绝理由："+returnReason);
	            		tplContent.get(mail).put("content", sb.toString());
					}else { //第一次添加
						Map<String, String> map = new HashMap<String,String>();
	            		map.put("subject", "收支拒绝通知");
	            		map.put("content","收支信息被拒绝，请及时处理。<br><br>月份："+(project.getProjectMonth().getMonth()+1)+"月<br>顾客："
	            		+cus.getCode()+"<br>拒绝理由："+returnReason);
	            		tplContent.put(mail, map);
					}
				}
			}
		}
		if(tplContent.size()!=0) {
    		//补全 content
    		for(String toSend : tplContent.keySet()) {
				String string = tplContent.get(toSend).get("content");
				String content = string+"<br><br>< 收支运营 SaaS >";
				tplContent.get(toSend).put("content", content);
			}
			//发送邮件
			emailConfigService.mailSendAll2(tplContent);
    	}
		return errorname;
	}

	/**
     * dataGird查询函数
     * @param project
     * @param parameterMap
     * @param dataGrid
     * @param declareStatus
     * @param delFlg
     * @param statusQueryCondition 状态码查询条件  0: in  1: <= less   2:>= greater
     */
    public void setDataGrid(ProjectEntity project,Map<String, String[]> parameterMap, DataGrid dataGrid,Integer[] declareStatus, Integer delFlg,int statusQueryCondition) {
    	//通过 session 获取 当前 登陆用户
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        //EmployeeDeclareEntity
        CriteriaQuery cq = new CriteriaQuery(ProjectEntity.class, dataGrid);
        cq.eq("delFlage", delFlg);
        switch(statusQueryCondition) {
	        case 1: cq.le("projectStatus", declareStatus[0]);cq.ge("projectStatus", 2);break;
	        case 2: cq.ge("projectStatus", declareStatus[0]);break;
	        default: cq.in("projectStatus", declareStatus);
        }
        //部门查询在这里处理
        if(StringUtil.isNotEmpty(project.getProjectDepartment())){
        	String depatrId = project.getProjectDepartment();
        	project.setProjectDepartment(null);
			TSDepart depart = commonService.getEntity(TSDepart.class,depatrId);
			List<TSDepart> departs = commonRepository.subdivision(depart);
			String[] departIds = new String[departs.size()];
			int i = 0;
			for(TSDepart dp : departs){
				departIds[i] = dp.getId();
				i++;
			}
			cq.in("projectDepartment",departIds);
        }

        String myRoleName = projectRepo.findMyRole(user.getId());
        if("t_control".equals(myRoleName)) {
        }else if("t_access".equals(myRoleName)) { //访客
        	project.setProjectStatus(1);
        }else if("t_check".equals(myRoleName)) {
        }else {//
	        if("t_report".equals(myRoleName)) {  //总监
	    		TSDepart currentDepart = commonRepository.getCurrentDepart(user);
	    		List<TSUser> list = commonRepository.getDepartAllUser(currentDepart);
				if(list.size() == 0) { //部门下 没有 客户经理 账号，则 显示空
					cq.eq("inputerId", user.getUserName());
	    		} else {
	    			int size = list.size();
	    			String[] list1 = new String[size];
	    			for(int i =0;i<size;i++) {
	    				list1[i]=list.get(i).getUserName();
	    			}
	    			cq.in("inputerId", list1);
	    		}
	    	}else if("t_input".equals(myRoleName)){
	    		project.setInputerId(user.getUserName());
	    	}
        }

        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, project, parameterMap);
        commonService.getDataGridReturn(cq, true);
    }
    /**
     * 访客查询专用，查询备份数据
     * dataGird查询函数
     * @param project
     * @param parameterMap
     * @param dataGrid
     * @param declareStatus
     * @param delFlg
     * @param statusQueryCondition 状态码查询条件  0: in  1: <= less   2:>= greater
     */
    public void setDataGridforAccess(ProjectCopyEntity projectCopy,Map<String, String[]> parameterMap,DataGrid dataGrid) {
    	CriteriaQuery cq = new CriteriaQuery(ProjectCopyEntity.class, dataGrid);
    	//部门查询在这里处理
    	if(StringUtil.isNotEmpty(projectCopy.getProjectDepartment())){
    		String depatrId = projectCopy.getProjectDepartment();
    		projectCopy.setProjectDepartment(null);
    		TSDepart depart = commonService.getEntity(TSDepart.class,depatrId);
    		List<TSDepart> departs = commonRepository.subdivision(depart);
    		String[] departIds = new String[departs.size()];
    		int i = 0;
    		for(TSDepart dp : departs){
    			departIds[i] = dp.getId();
    			i++;
    		}
    		cq.in("projectDepartment",departIds);
    	}
    	if(StringUtil.isNotEmpty(projectCopy.getProjectCustomer1())){
    		cq.eq("projectCustomer1", projectCopy.getProjectCustomer1());
    	}


    	//查询条件组装器
    	org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, projectCopy, parameterMap);
    	commonService.getDataGridReturn(cq, true);
    }

	/**
	 * 新-退回-选中
	 * @param departId
	 * @param departName
	 * @param returnReason
	 * @return
	 */

	public Map<String, Object> newBatchReturn(List<Integer> id,String returnReason) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		//当前登录人员信息
		StringBuffer name= new StringBuffer();
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(null!=user){
			//Map<String,String> tplContent = new HashMap<String,String>();
			//加载申报未通过的数据
			List<ProjectEntity> projectList = projectRepo.findByDeclareStatusId(id);
			if(!projectList.isEmpty()){
				name =returnDeclareStatus(user,projectList,returnReason);
				result.put("errCode", 0);
			}else{
				result.put("errMsg", "当前部门："+id.toString()+"没有需要退回的申报数据");
				result.put("errCode", -1);
			}
		}else{
			String errMsg = "当前用户未登录，无法进行退回操作";
			log.info(errMsg);
			result.put("errMsg", errMsg);
			result.put("errCode", -1);
		}
		if (0 < name.length()) {
			result.put("errMsg", "项目在当前状态无法进行此操作");
			result.put("errCode", -1);
		}
		return result;
	}


    /**
     * 格式化日期
     *
     * @return
     */
    public String formatDate(Date date, String fmt) {
        String dateStr = "";
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }

    /**
     * 导出项目Excel
     * @param list
     * @return
     */
	public Map<String, Object> exportProjectData(List<ProjectEntity> list,Date date) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = mo.toString()+"月项目收支总计";
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/project-declare.xlsx");
        if (!excelFile.exists()) {
            return null;
        }
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(excelFile);
            wb = new XSSFWorkbook(is);
            Sheet sheet = wb.getSheetAt(0);
            projectDeclareExportDemo(list, sheet);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        result.put("wb", wb);
        result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");

		return result;
	}
	/**
	 * 导出项目Excel-访客专用
	 * @param list
	 * @return
	 */
	public Map<String, Object> exportProjectCopyData(List<ProjectCopyEntity> list,Date date) {
		Map<String, Object> result = new HashMap<String, Object>();
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = mo.toString()+"月项目收支总计";
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
				.getPath() + "excel-template/project-declare.xlsx");
		if (!excelFile.exists()) {
			return null;
		}
		InputStream is = null;
		Workbook wb = null;
		try {
			is = new FileInputStream(excelFile);
			wb = new XSSFWorkbook(is);
			Sheet sheet = wb.getSheetAt(0);
			projectDeclareExportDemoforAccess(list, sheet);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		result.put("wb", wb);
		result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");

		return result;
	}

	/**
	 * 导出 部门 总收支
	 * @param request
	 * @param declareStatus
	 * @param project
	 * @param dataGrid
	 * @return
	 */
	public Map<String, Object> exportDepartData(HttpServletRequest request, String declareStatus, ProjectEntity projectEntity,
			DataGrid dataGrid,Date date) {
		Map<String, Object> result = new HashMap<String, Object>();
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = mo.toString()+"月部门总收支";
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/depart-declare.xlsx");
        if (!excelFile.exists()) {
            return null;
        }
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(excelFile);
            wb = new XSSFWorkbook(is);

            //sheet1 - 人力收支 （带 查询条件）
            Sheet sheet1 = wb.getSheetAt(1);
            List<EmployeeDeclareEntity> list = new ArrayList<EmployeeDeclareEntity>();
            EmployeeDeclareEntity employeeDeclare = new EmployeeDeclareEntity();
            //给人力加入查询条件
            employeeDeclare.setSalaryDate(projectEntity.getProjectMonth());
            if(projectEntity.getProjectDepartment()!=null) employeeDeclare.setEmployeeDepartment(projectEntity.getProjectDepartment());;
            if(projectEntity.getProjectCustomer1()!=null) employeeDeclare.setCustomerInfo(Integer.parseInt(projectEntity.getProjectCustomer1()));
            String newStatus = "";
            if(declareStatus==null||"".equals(declareStatus)||"undefined".equals(declareStatus)){
            }else {
            	 Integer ns = commonRepository.changeStatusProjectToEmployeeDeclare(Integer.parseInt(declareStatus));
            	 newStatus = ""+ns;
            }
            employeeDeclareService.setDataGridByStatus(request, newStatus, employeeDeclare, dataGrid);
            list = dataGrid.getResults();
            //收支申报导出模板
            employeeDeclareService.empDeclareExportDemo(list,sheet1);
            //让sheet的求和函数生效
            sheet1.setForceFormulaRecalculation(true);


            //sheet2 - 项目收支 （all）
            Sheet sheet2 = wb.getSheetAt(2);
            //加入查询条件
            setDataGridByStatus(request, declareStatus, projectEntity, dataGrid);
            List<ProjectEntity> projectList= dataGrid.getResults();
            //项目申报导出模板
            projectDeclareExportDemo(projectList,sheet2);


            //sheet0 - 部门合计
            Sheet sheet0 = wb.getSheetAt(0);
            Row row = sheet0.getRow(1);
            //总收入  总毛利  总人数（人数+项目数）
            double incomeTotal = 0;
            double profitTotal = 0;
            double num = list.size()+projectList.size();
            for(EmployeeDeclareEntity declare : list) {
            	incomeTotal += declare.getIncome()==null?0:declare.getIncome();
            	profitTotal += declare.getCompanyProfit()==null?0:declare.getCompanyProfit();
            }
            for(ProjectEntity project : projectList) {
            	incomeTotal += project.getProjectIncome()==null?0:project.getProjectIncome();
            	profitTotal += project.getProjectProfit()==null?0:project.getProjectProfit();
            }
            row.getCell(1).setCellValue(incomeTotal);
            //人数
            row.getCell(2).setCellValue(list.size());
            //项目数
            row.getCell(3).setCellValue(projectList.size());
            //总毛利
            row.getCell(4).setCellValue(profitTotal);
            //平均毛利
            row.getCell(5).setCellValue(num==0?0:profitTotal/num);

        } catch (Exception e) {
            log.info(e.getMessage(), e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        result.put("wb", wb);
        result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
		return result;
	}
	/**
	 * 访客 - 导出 部门 总收支
	 * ProjectCopyEntity
	 * @param request
	 * @param declareStatus
	 * @param project
	 * @param dataGrid
	 * @return
	 */
	public Map<String, Object> exportDepartDataforAccess(HttpServletRequest request,ProjectCopyEntity projectEntity,DataGrid dataGrid,Date date) {
		Map<String, Object> result = new HashMap<String, Object>();
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = mo.toString()+"月部门总收支";
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/depart-declare.xlsx");
        if (!excelFile.exists()) {
            return null;
        }
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(excelFile);
            wb = new XSSFWorkbook(is);

            //sheet1 - 人力收支 （带 查询条件）
            Sheet sheet1 = wb.getSheetAt(1);
            List<EmployeeDeclareCopyEntity> list = new ArrayList<EmployeeDeclareCopyEntity>();
            EmployeeDeclareCopyEntity employeeDeclare = new EmployeeDeclareCopyEntity();
            //给人力加入查询条件
            employeeDeclare.setSalaryDate(projectEntity.getProjectMonth());
            if(projectEntity.getProjectDepartment()!=null) employeeDeclare.setEmployeeDepartment(projectEntity.getProjectDepartment());;
            if(projectEntity.getProjectCustomer1()!=null) employeeDeclare.setCustomerId(Integer.parseInt(projectEntity.getProjectCustomer1()));
            employeeDeclareService.setDataGridforAccess(employeeDeclare, request.getParameterMap(), dataGrid);
            list = dataGrid.getResults();
            //收支申报导出模板
            employeeDeclareService.empDeclareExportDemoforAccess(list, sheet1);
            //让sheet的求和函数生效
            sheet1.setForceFormulaRecalculation(true);


            //sheet2 - 项目收支 （all）
            Sheet sheet2 = wb.getSheetAt(2);
            //加入查询条件
            setDataGridforAccess(projectEntity, request.getParameterMap(), dataGrid);
            List<ProjectCopyEntity> projectList= dataGrid.getResults();
            //项目申报导出模板
            projectDeclareExportDemoforAccess(projectList,sheet2);


            //sheet0 - 部门合计
            Sheet sheet0 = wb.getSheetAt(0);
            Row row = sheet0.getRow(1);
            //总收入  总毛利  总人数（人数+项目数）
            double incomeTotal = 0;
            double profitTotal = 0;
            double num = list.size()+projectList.size();
            for(EmployeeDeclareCopyEntity declare : list) {
            	incomeTotal += declare.getIncome()==null?0:declare.getIncome();
            	profitTotal += declare.getCompanyProfit()==null?0:declare.getCompanyProfit();
            }
            for(ProjectCopyEntity project : projectList) {
            	incomeTotal += project.getProjectIncome()==null?0:project.getProjectIncome();
            	profitTotal += project.getProjectProfit()==null?0:project.getProjectProfit();
            }
            row.getCell(1).setCellValue(incomeTotal);
            //人数
            row.getCell(2).setCellValue(list.size());
            //项目数
            row.getCell(3).setCellValue(projectList.size());
            //总毛利
            row.getCell(4).setCellValue(profitTotal);
            //平均毛利
            row.getCell(5).setCellValue(num==0?0:profitTotal/num);

        } catch (Exception e) {
            log.info(e.getMessage(), e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        result.put("wb", wb);
        result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
		return result;
	}
	/**
	 * 访客-项目申报 导出模板
	 * ProjectCopyEntity
	 * @param list
	 * @param sheet
	 */
	public void projectDeclareExportDemoforAccess(List<ProjectCopyEntity> list, Sheet sheet) {
		if (!list.isEmpty()) {
		    for (int i = 0; i < list.size(); i++) {
		        Row oldRow = sheet.getRow(i + 1);
		        Row row = sheet.createRow(i + 2);
		        //设置新增行的单元格风格与上一行一样
		        row.setRowStyle(oldRow.getRowStyle());
		        short lastCellNum = oldRow.getLastCellNum();
		        for(int j = 1 ;j < lastCellNum;j++) {
		        	Cell createCell = row.createCell(j);
		        	Cell cell = oldRow.getCell(j);
		        	CellStyle cellStyle = cell.getCellStyle();
//                    	DataFormat format = wb.createDataFormat();
////                    	//poi 设置excel单元格样式封装 样式"($#,##0.00_)"
//                    	cellStyle.setDataFormat(format.getFormat("#,##0.00"));
		        	createCell.setCellStyle(cellStyle);
		        }

		        ProjectCopyEntity project = list.get(i);
		        //申报月份
		        if(project.getProjectMonth()!=null)
		        row.getCell(1).setCellValue(project.getProjectMonth());
		        //申报部门
		        if(project.getProjectDepartment()!=null) {
		        	TSDepart department = systemService.getEntity(TSDepart.class, project.getProjectDepartment());
		            row.getCell(2).setCellValue(department.getDepartname());
		        }
		        //客户简称
		        if(project.getProjectCustomer1()!=null) {
		        	Integer cusId =Integer.parseInt(project.getProjectCustomer1());
		        	CustomerInfoEntity customer = systemService.getEntity(CustomerInfoEntity.class, cusId);
		        	row.getCell(3).setCellValue(customer.getCode());
		        }
		        //收入
		        if(project.getProjectIncome()!=null)
		        row.getCell(4).setCellValue(project.getProjectIncome());
		        //支出
		        if(project.getProjectPay()!=null)
		        row.getCell(5).setCellValue(project.getProjectPay());
		        //毛利
		        if(project.getProjectProfit()!=null)
		        row.getCell(6).setCellValue(project.getProjectProfit());
		        //毛利率
		        if(project.getProjectProfitRate()!=null)
		        row.getCell(7).setCellValue((project.getProjectProfitRate())/100);
		    }
		}

	}

	/**
	 * 项目申报 导出模板
	 * @param list
	 * @param sheet
	 */
	public void projectDeclareExportDemo(List<ProjectEntity> list, Sheet sheet) {
		if (!list.isEmpty()) {
		    for (int i = 0; i < list.size(); i++) {
		        Row oldRow = sheet.getRow(i + 1);
		        Row row = sheet.createRow(i + 2);
		        //设置新增行的单元格风格与上一行一样
		        row.setRowStyle(oldRow.getRowStyle());
		        short lastCellNum = oldRow.getLastCellNum();
		        for(int j = 1 ;j < lastCellNum;j++) {
		        	Cell createCell = row.createCell(j);
		        	Cell oldcell = oldRow.getCell(j);
		        	CellStyle cellStyle = oldcell.getCellStyle();
		        	CellStyle copy = sheet.getWorkbook().createCellStyle();
		        	copy.cloneStyleFrom(cellStyle);
		        	if(j>=4&&j<=6) {
		        		copy.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		        	}
		        	createCell.setCellStyle(copy);
		        }

		        ProjectEntity project = list.get(i);
		        //申报月份
		        if(project.getProjectMonth()!=null)
		        row.getCell(1).setCellValue(project.getProjectMonth());
		        //申报部门
		        if(project.getProjectDepartment()!=null) {
		        	TSDepart department = systemService.getEntity(TSDepart.class, project.getProjectDepartment());
		            row.getCell(2).setCellValue(department.getDepartname());
		        }
		        //客户简称
		        if(project.getProjectCustomer1()!=null) {
		        	CustomerInfoEntity customer = systemService.getEntity(CustomerInfoEntity.class, Integer.parseInt(project.getProjectCustomer1()));
		        	row.getCell(3).setCellValue(customer.getCode());
		        }
		        //收入
		        if(project.getProjectIncome()!=null)
		        row.getCell(4).setCellValue(project.getProjectIncome());
		        //支出
		        if(project.getProjectPay()!=null)
		        row.getCell(5).setCellValue(project.getProjectPay());
		        //毛利
		        if(project.getProjectProfit()!=null)
		        row.getCell(6).setCellValue(project.getProjectProfit());
		        //毛利率
		        DecimalFormat df = new DecimalFormat("#.00");
		        if(project.getProjectProfitRate()!=null)
		        //row.getCell(7).setCellValue((df.format(project.getProjectProfitRate())));
		        row.getCell(7).setCellValue((project.getProjectProfitRate()/100));
		    }
		}
	}

	/**
	 * 获取待处理信息数量
	 * @param user
	 * @return
	 */
	public int getMyMessageCount(TSUser user) {
		// TODO Auto-generated method stub
		TSDepart currentDepart = commonRepository.getCurrentDepart(user);
		int lv = commonRepository.getDepartGread(currentDepart);
		String roleName = commonRepository.findMyRoleByUserName(user.getUserName());
		int count = 0;
		if("t_input".equals(roleName)) {
			count = projectRepo.getMessageCount(lv,currentDepart.getOrgCode(),true);
		}else {
			count = projectRepo.getMessageCount(lv,currentDepart.getOrgCode(),false);
		}

		return count;
	}
	/**
	 * 访客 - 一键统计项目全部
	 * 供访客人力页面一键统计部门调用
	 * @param date  月份
	 * @param departId  部门
	 * @param customerId  顾客
	 * @return
	 */
	public Map<String, Double> oneKeyTotalXMByVisitor(Date date, String departId,int customerId) {
		Map<String,Double> map = new HashMap<String,Double>();

		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSDepart curDepart = commonRepository.getCurrentDepart(user);
		String orgCode = curDepart.getOrgCode();
		if(departId == null || "".equals(departId)||"undefined".equals(departId)) {  // 部门查询框未选中
			int lv = commonRepository.getDepartGread(curDepart); //判断 是否是 控制/审计
			if(lv<3) {  //一层 二层 部门orgCode需要特殊处理
				orgCode = orgCode.substring(0,3);
			}
		}else {	//部门查询框选中
			TSDepart findDepart = commonService.findUniqueByProperty(TSDepart.class, "id", departId);
			orgCode = findDepart.getOrgCode();
		}
		Object oneKeyTotal = projectRepo.oneKeyTotalforAccess(date, orgCode,customerId);
		Object[] obj = (Object[]) oneKeyTotal;
		if(obj[0]==null&&obj[2]==null) {
			map.put("incomeTotal", 0.0);
            map.put("profitTotal", 0.0);
            map.put("peopleTotal", 0.0);
		}else{
			map.put("incomeTotal", Double.parseDouble(obj[0].toString()));
            map.put("profitTotal", Double.parseDouble(obj[1].toString()));
            map.put("peopleTotal", Double.parseDouble(obj[2].toString()));
		}
		return map;
	}
	/**
	 * 一键统计-项目收支
	 * @param request
	 * @param declareStatus
	 * @param project
	 * @param dataGrid
	 * @return
	 */
	public Map<String, Double> oneKeyTotalXM(HttpServletRequest request, String declareStatus, ProjectEntity project,
			DataGrid dataGrid) {
		setDataGridByStatus(request, declareStatus, project, dataGrid);
		Map<String,Double> map = new HashMap<String,Double>();
		List<ProjectEntity> list = dataGrid.getResults();
		if(list ==null || list.size() == 0) {
        	map.put("incomeTotal", 0.0);
            map.put("profitTotal", 0.0);
            map.put("peopleTotal", 0.0);
        }else {
        	double incomeTotal = 0.0;
        	double profitTotal = 0.0;
        	for(ProjectEntity p : list) {
        		double income = p.getProjectIncome()==null?0.0:p.getProjectIncome();
        		double profit = p.getProjectProfit()==null?0.0:p.getProjectProfit();
        		incomeTotal+=income;
        		profitTotal+=profit;
        	}
        	double peopleTotal = list.size();
            map.put("incomeTotal", incomeTotal);
            map.put("profitTotal", profitTotal);
            map.put("peopleTotal", peopleTotal);
        }
		return map;
	}
	/**
	 * 一键统计-访客专属
	 * @param request
	 * @param project
	 * @param dataGrid
	 * @return
	 */
	public Map<String, Double> oneKeyTotalXMByVisitor(HttpServletRequest request, ProjectCopyEntity projectCopy,
			DataGrid dataGrid) {
		setDataGridforAccess(projectCopy,request.getParameterMap(),dataGrid);
		Map<String,Double> map = new HashMap<String,Double>();
		List<ProjectCopyEntity> list = dataGrid.getResults();
		if(list ==null || list.size() == 0) {
        	map.put("incomeTotal", 0.0);
            map.put("profitTotal", 0.0);
            map.put("peopleTotal", 0.0);
        }else {
        	double incomeTotal = 0.0;
        	double profitTotal = 0.0;
        	for(ProjectCopyEntity p : list) {
        		double income = p.getProjectIncome()==null?0.0:p.getProjectIncome();
        		double profit = p.getProjectProfit()==null?0.0:p.getProjectProfit();
        		incomeTotal+=income;
        		profitTotal+=profit;
        	}
        	double peopleTotal = list.size();
            map.put("incomeTotal", incomeTotal);
            map.put("profitTotal", profitTotal);
            map.put("peopleTotal", peopleTotal);
        }
		return map;
	}
	/**
	 * datagrid查询 - 加入申报状态包装查询
	 * @param request
	 * @param declareStatus
	 * @param employeeDeclare
	 * @param dataGrid
	 */
	public void setDataGridByStatus(HttpServletRequest request, String declareStatus,
			ProjectEntity project, DataGrid dataGrid) {
		if(declareStatus==null||"".equals(declareStatus)||"undefined".equals(declareStatus)) {
			setDataGrid(project, request.getParameterMap(), dataGrid, null, 0,0);
		} else { //有选择申报状态
			TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
			TSDepart currentDepart = commonRepository.getCurrentDepart(user);
			Integer lv = commonRepository.getDepartGread(currentDepart);
			int initStatus = lv*2;
			int selectStatus = Integer.parseInt(declareStatus);
			if(selectStatus == 1){ //查询审批通过
				project.setProjectStatus(1);
	        	setDataGrid(project, request.getParameterMap(), dataGrid, null, 0,0);
			}else if(selectStatus==initStatus+1) { //查询 未上报
				setDataGrid(project, request.getParameterMap(), dataGrid, new Integer[]{selectStatus}, 0,2);
	        }else if(selectStatus==initStatus-2){//查询已上报
				setDataGrid(project, request.getParameterMap(), dataGrid, new Integer[]{selectStatus}, 0,1);
	        }else { //正常
	        	project.setProjectStatus(selectStatus);
	        	setDataGrid(project, request.getParameterMap(), dataGrid, null, 0,0);
	        }
		}
	}
	/**
	 * 根据主键集合查询
	 * @param lids
	 * @return
	 */
	public List<ProjectEntity> findAllByProjectId(List<Integer> lids) {
		// TODO Auto-generated method stub
		return projectRepo.findAllByProjectId(lids);
	}
	/**
	 * 根据主键集合查询-访客专用
	 * @param lids
	 * @return
	 */
	public List<ProjectCopyEntity> findAllByProjectCopyId(List<Integer> lids) {
		// TODO Auto-generated method stub
		return projectRepo.findAllByProjectCopyId(lids);
	}
	/**
	 * 查询 当前用户 所在部门下所有项目（包括子部门）
	 * @param user
	 * @return
	 */
	public List<ProjectEntity> findAllByCurrentDept(TSUser user) {
		List<ProjectEntity> result = new ArrayList<ProjectEntity>();
		TSDepart currentDepart = commonRepository.getCurrentDepart(user);
		List<TSDepart> subdivision = commonRepository.findSubDepart(currentDepart);
		for(TSDepart depart : subdivision) {
			List<ProjectEntity> list = commonService.findByProperty(ProjectEntity.class, "projectDepartment", depart.getId());
			result.addAll(list);
		}
		return result;
	}
	/**
	 * 查询 当前用户 所在部门下所有项目（包括子部门）-访客专用
	 * @param user
	 * @return
	 */
	public List<ProjectCopyEntity> findAllByCurrentDeptforAccess(TSUser user,String CustomerId) {
//		List<ProjectCopyEntity> result = new ArrayList<ProjectCopyEntity>();
//		TSDepart currentDepart = commonRepository.getCurrentDepart(user);
//		List<TSDepart> subdivision = commonRepository.findSubDepart(currentDepart);
//		for(TSDepart depart : subdivision) {
//			List<ProjectCopyEntity> list = commonService.findByProperty(ProjectCopyEntity.class, "projectDepartment", depart.getId());
//			result.addAll(list);
//		}

		List<ProjectCopyEntity> result = new ArrayList<ProjectCopyEntity>();
		List<ProjectCopyEntity> list = commonService.loadAll(ProjectCopyEntity.class);
		result.addAll(list);
		return result;
	}
	/**
	 * 通过部门ID 查询该部门下的所有项目（包括子部门）
	 * @param employeeDepartment
	 * @return
	 */
	public List<ProjectEntity> findAllByCurrentDept(String departId) {
		List<ProjectEntity> result = new ArrayList<ProjectEntity>();
		TSDepart currentDepart = commonService.getEntity(TSDepart.class, departId);
		List<TSDepart> subdivision = commonRepository.findSubDepart(currentDepart);
		for(TSDepart depart : subdivision) {
			List<ProjectEntity> list = commonService.findByProperty(ProjectEntity.class, "projectDepartment", depart.getId());
			result.addAll(list);
		}
		return result;
	}
	/**
	 * 通过部门ID 查询该部门下的所有项目（包括子部门）-访客专用
	 * @param employeeDepartment
	 * @return
	 */
	public List<ProjectCopyEntity> findAllByCurrentDeptforAccess(String departId) {
		List<ProjectCopyEntity> result = new ArrayList<ProjectCopyEntity>();
		TSDepart currentDepart = commonService.getEntity(TSDepart.class, departId);
		List<TSDepart> subdivision = commonRepository.findSubDepart(currentDepart);
		for(TSDepart depart : subdivision) {
			List<ProjectCopyEntity> list = commonService.findByProperty(ProjectCopyEntity.class, "projectDepartment", depart.getId());
			result.addAll(list);
		}
		return result;
	}


}
