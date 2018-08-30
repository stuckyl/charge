package com.charge.service;

import static org.hamcrest.Matchers.stringContainsInOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.Else;
import com.charge.entity.EmailConfigEntity;
import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.ExpensesEntity;
import com.charge.repository.AttendanceCalendarRepository;
import com.charge.repository.CommonRepository;
import com.charge.repository.CorporateInfoRepository;
import com.charge.repository.EmailConfigRepository;
import com.charge.repository.EmployeeDeclareRepository;
import com.charge.repository.EmployeeInfoRepository;
import com.charge.repository.ExpensesRepository;
import com.charge.repository.SixGoldScaleRepository;

@Service
@Transactional
public class ExpensesDeclareService {
	private static final Logger log = Logger.getLogger(EmployeeDeclareService.class);


    @Autowired
    private CommonService commonService;

    @Autowired
	private SystemService systemService;

	@Autowired
	private CommonRepository jeecgRepo;

	@Autowired
	private ExpensesRepository expensesRepo;

	@Autowired
	private EmployeeDeclareRepository employeeDeclareRepo;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	/**
     * 设置datagrid数据：新版 setDataGrid  优化查询速度，解决修改部门后原先收支显示不出来bug
     * @param employeeDeclare
     * @param parameterMap
     * @param dataGrid
     * @param declareStatus
     * @param delFlg
     * @param isEntry
     */

	 /**
     * 0507 测试： 批量退回2
     * batchReturn2
     *
     */
    public Map<String, Object> batchReturn2(List<Integer> ids, String returnReason, String type) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        if (StringUtils.isNotBlank(type)) {
            Integer statusChange = -1;
            //type类型 0 申报退回   1 审核退回   2  审批退回
            if ("0".equals(type)) {
                statusChange = 3;//申报拒绝
            } else if ("1".equals(type)) {
                statusChange = 2;//审核拒绝
            } else if ("2".equals(type)) {
                statusChange = 4;//审批拒绝
            }
            //当前登录人员信息
            TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
            //加载数据

            List<ExpensesEntity> expensesEntityList = expensesRepo.findAllByExpensesId(ids);
            if (!expensesEntityList.isEmpty()) {
            	 int newStatus = statusChange;
            	 for (int i = 0; i < expensesEntityList.size(); i++) {
                     ExpensesEntity expenses = expensesEntityList.get(i);
//                     employee.setDeclareDate(new Date());
                     //如果 为 本部门员工 且 被审核拒绝  且 是业务部门BU 申报上去的 状态变为3
                     /*String inputerId = expenses.getInputerId();
                     String myRole = jeecgRepo.findMyRoleByUserName(inputerId);
                     if("t_control".equals(myRole)){
                    	 newStatus=1;
                     }else {
                    	 newStatus = statusChange;
                     }*/
                     newStatus = statusChange;
                     expenses.setLastModifiedDate(new Date());
                     expenses.setLastModifiedBy(user.getRealName());
                     expenses.setDeclareStatus(newStatus);
                     expenses.setDeclareReturnreason(returnReason);
                     expensesRepo.update(expenses);
                 }
               /* Set<String> departSet = new HashSet<String>();
                for (int i = 0; i < expensesEntityList.size(); i++) {
                    departSet.add(expensesEntityList.get(i).getEmployeeInfo().getDepartment());
                }*/

            } else {
                result.put("errMsg", "没有需要退回的申报数据");
                result.put("errCode", -1);
            }
        } else {
            log.info("退回类型为空。type=" + type);
        }
        return result;


    }

    public void setDataGrid2(ExpensesEntity expensesDeclare,Map<String, String[]> parameterMap, DataGrid dataGrid,Integer[] declareStatus, Integer expensesDelFlg,boolean isEntry) {
    	//通过 session 获取 当前 登陆用户
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
    	// 最后导出表的 查询条件
        if(expensesDeclare == null) {
        	ContextHolderUtils.getSession().setAttribute("expensesDeclare", null);
        }else {
        	 ContextHolderUtils.getSession().setAttribute("expensesDeclare", expensesDeclare);
        	 /*if(expensesDeclare.getEmployeeInfo()!=null) {
        		 ContextHolderUtils.getSession().setAttribute("employeeInfo",expensesDeclare.getEmployeeInfo());
        	 }*/
        }

        CriteriaQuery cq = new CriteriaQuery(ExpensesEntity.class, dataGrid);
        cq.eq("expensesDelFlg", expensesDelFlg);
        cq.in("declareStatus", declareStatus);
        //如果是 controller和checker 就可以查看全部 c_employee_declare
        String myRoleName = employeeDeclareRepo.findMyRole(user.getId());
        if("t_control".equals(myRoleName)) {

        }else if("t_check".equals(myRoleName)) {  //审计部门

        }else {//
        	if("t_report".equals(myRoleName)||"t_inpte_m".equals(myRoleName)) {  //总监情况特殊，录入界面只能看到自己录入的，申报界面 只能看到 部门下客户经理申报的
        		if(isEntry) { //录入界面
        			//expensesDeclare.setInputerId(user.getUserName());
        		}
        	}else {
        		expensesDeclare.setCreatedBy(user.getUserName());
        	}
        	if("t_report".equals(myRoleName)) {  //总监情况特殊，录入界面只能看到自己录入的，申报界面 只能看到 部门下客户经理申报的
        		List<TSUser> list = jeecgRepo.getDepartAllUser(user.getDepartid());
    			/*if(list.size() == 0) { //部门下 没有 客户经理 账号，则 显示空
    				cq.eq("inputerId", user.getUserName());
        		} else {
        			int size = list.size();
        			String[] list1 = new String[size];
        			for(int i =0;i<size;i++) {
        				list1[i]=list.get(i).getUserName();
        			}
        			cq.in("inputerId", list1);
        		}*/
        		//cq.eq("inputerId", user.getRealName());
        		cq.eq("createdBy", user.getUserName());
        		/**
        		 * 部门总监分 录入界面 与申报界面
        		 * if(isEntry) { //录入界面
        			employeeDeclare.setInputerId(user.getUserName());
        		} else { //申报界面
        			List<TSUser> list = jeecgRepo.getDepartAllInputer(user.getDepartid());
        			if(list.size() == 0) { //部门下 没有 客户经理 账号，则 显示空
        				cq.eq("employeeInfo.id", -99999);
            		} else {
            			int size = list.size();
            			String[] list1 = new String[size];
            			for(int i =0;i<size;i++) {
            				list1[i]=list.get(i).getUserName();
            			}
            			cq.in("inputerId", list1);
            		}
        		}
        		*/
        	}else {
        		expensesDeclare.setCreatedBy(user.getUserName());
        	}
        }
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, expensesDeclare, parameterMap);
        commonService.getDataGridReturn(cq, true);
    }

	/**
	 * 去掉空格、回车、换行符、制表符
	 * @return
	 */
	public static String replaceAllBlank(String str) {
			String dest = "";
		        if (str!=null) {
		        	java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\s*|\t|\r|\n");
		            java.util.regex.Matcher m = p.matcher(str);
		            dest = m.replaceAll("");
		        }
		        dest = dest.replace("\n", "").replace("\t","").replace("\r","").replace(" ", "");
		        return dest;
		    }
	/**
	 * 去掉收尾空格、回车、换行符
	 * @return
	 */
	public static String replaceBlank(String str) {
		str = str.trim();
		str = str.replace("\n", "").replace("\t","").replace("\r","");
		while (str.startsWith("　")) {//这里判断是不是全角空格
			str = str.substring(1, str.length()).trim();
			}
			while (str.endsWith("　")) {
				str = str.substring(0, str.length() - 1).trim();
			}
		return str;
	}

	/**
	 * 获取部门下的数据
	 * j 5-11
	 * @param employeeDeclare
	 * @param dataGrid
	 * @param parameterMap
	 */
	public void setDataGriddepart(ExpensesEntity expenses, Map<String,String[]> parameterMap,DataGrid dataGrid,Object[] declareStatus,Integer flag){
		CriteriaQuery cq = new CriteriaQuery(ExpensesEntity.class,dataGrid);
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		cq.in("declareStatus", declareStatus);
		cq.eq("expensesDelFlg", 0);
		cq.eq("departmentId",user.getDepartid());
		cq.addOrder("declareStatus", SortDirection.asc);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, expenses, parameterMap);
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
	 * 获取inputerId的数据
	 * j 5-11
	 * @param employeeDeclare
	 * @param dataGrid
	 * @param parameterMap
	 */
	public void setDataGridFlag(ExpensesEntity expenses, Map<String,String[]> parameterMap,DataGrid dataGrid,Object[] declareStatus,Integer flag){
		CriteriaQuery cq = new CriteriaQuery(EmployeeInfoEntity.class,dataGrid);
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		cq.in("declareStatus", declareStatus);
		cq.eq("delFlg", 0);
		cq.eq("employeeFlag", flag);
		cq.eq("inputerId", user.getUserName());
		cq.eq("department", user.getDepartid());
		cq.addOrder("declareStatus", SortDirection.asc);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, expenses, parameterMap);
		commonService.getDataGridReturn(cq, true);
	}

	/**
	 * 通过ID查找经费申报数据
	 * @param id
	 * @return
	 */
	/*public ExpensesEntity findById(Integer id){
		ExpensesEntity expensesDeclareEntity = (ExpensesEntity) getSession().createQuery("from ExpensesEntity t "
				+ "where t.id = :id").setInteger("id", id).uniqueResult();
		return expensesDeclareEntity;
	}*/


	private void setModifyPerson(TSUser user,ExpensesEntity expensesDeclare,int status) {
		if(status == 2) {
			expensesDeclare.setInputerId(user.getUserName());
		}else if(status == 3){
			expensesDeclare.setReporterId(user.getUserName());
		}else if(status == 6) {
			/*if(expensesDeclare.getEmployeeType()==1) { //内部员工 没有审核者
				expensesDeclare.setReporterId(user.getUserName());
			}else{
				expensesDeclare.setCheckerId(user.getUserName());
			}*/
			//expensesDeclare.setReporterId(user.getUserName());
		}/*else if(status == 8){
			expensesDeclare.setControllerId(user.getUserName());
		}*/
	}
	/*
	 * 经费更新
	 * **/
	public void updateDeclareStatus(TSUser user, ExpensesEntity expenses, int changeStatus) {
		// TODO Auto-generated method stub
		expenses.setLastModifiedDate(new Date());
		expenses.setLastModifiedBy(user.getUserName());
		expenses.setDeclareStatus(changeStatus);
		expenses.setDeclareReturnreason(null);
        setModifyPerson(user,expenses,changeStatus);
        expensesRepo.update(expenses);
	}

}