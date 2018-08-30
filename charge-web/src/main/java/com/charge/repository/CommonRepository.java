package com.charge.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.minidao.annotation.Sql;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.Else;
import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.utils.Utils;

@Repository
public class CommonRepository extends GenericBaseCommonDao<TSDepart, Integer>{

	@Autowired
	private EmployeeDeclareRepository employeeDeclareRepo;
	@Autowired
	private CommonService commonService;

	/**
	 * 查询我能查看的部门
	 *
	 * @return
	 */
	public List findMyDeptInfo() {
		  TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		  String deptId = user.getDepartid();
		  TSDepart myDep = commonService.getEntity(TSDepart.class,deptId);
		  String sql = "select org_type from t_s_depart where id=?";
		  List result = new ArrayList();
		  String orgType = (String)getSession().createSQLQuery(sql).setParameter(0, deptId).uniqueResult();
		  //判断  orgType  =2 则是审计/审核部门/访客部门，可以查看所有部门  orgType=4 则只能查看本部门
		  if("2".equals(orgType)||"1".equals(orgType)) {
			  result = getSession().createQuery("from TSDepart t where t.orgType in (:orgType)").setParameterList("orgType",new String[]{"4", "5"}).list();
		  }else {
			  result = subdivision(myDep);
		  }
		  return result;
	}
	/**
	 * 查询我能查看的部门********经费申报专用
	 *
	 * @return
	 */
	public List findMyDeptInfo1() {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		String deptId = user.getDepartid();
		String sql = "select org_type from t_s_depart where id=?";
		List result = new ArrayList();
		String orgType = (String)getSession().createSQLQuery(sql).setParameter(0, deptId).uniqueResult();
		//判断  orgType  =2 则是审计/审核部门/访客部门，可以查看所有部门  orgType=4 则只能查看本部门
		  if("2".equals(orgType)||"1".equals(orgType)) {
			  sql = "select * from t_s_depart where  org_type in (2,4) and org_code != 'A07A05' and org_code != 'A07A01' order by depart_order asc,departname asc";
			  result = getSession().createSQLQuery(sql).list();
		  }else {
			  sql = "select * from t_s_depart where id=?";
			  result = getSession().createSQLQuery(sql).setParameter(0, deptId).list();
		  }
		return result;
	}
	/**
	 * 通过部门ID 获取该部门总监
	 */
	public TSUser getDepartChief(String department){
		List<TSUser> list = commonService.findByProperty(TSUser.class, "departid", department);
		TSUser result = null;
		//通过 账户名 获取 账户角色
		for(TSUser user : list) {
			if(user.getDeleteFlag()==0) {
				String role = findMyRoleByUserName(user.getUserName());
				if("t_input_m".equals(role)||"t_report".equals(role)) {
					result=user;
					break;
				}
			}
		}
		return result;
	}
	/**
	 * 通过部门ID获取该部门（不包括子部门）所有账号
	 */
	public List<TSUser> getDepartAllUser(String department){
//		String sql ="select * from t_s_base_user where departid=? and delete_flag=0";
//		List<TSUser> list = (List<TSUser>)getSession().createSQLQuery(sql).setParameter(0, department).list();
		List<TSUser> list = commonService.findByProperty(TSUser.class, "departid", department);
		return list;
	}
	/**
	 * 通过部门ID 获取该部门所有客户经理账号
	 */
	public List<TSUser> getDepartAllInputer(String department){
		List<TSUser> list = commonService.findByProperty(TSUser.class, "departid", department);
		Iterator<TSUser> iterator = list.iterator();
		while(iterator.hasNext()) {
			TSUser next = iterator.next();
			String role = findMyRoleByUserName(next.getUserName());
			if("t_input_m".equals(role)||"t_report".equals(role)) {
				iterator.remove();
			}
		}
		return list;
	}

	/**
	 * 通过处理状态层级和员工所在部门ID查询出当前员工应当为哪一部门总监处理
	 * 当为客户经理时，员工层级为员工所在部门层级+1
	 *
	 */
	public TSUser getManagerNow(Integer empLv,String departId,String inputId){
		TSUser manager =new TSUser();
		TSDepart depart = commonService.findUniqueByProperty(TSDepart.class, "id", departId);
		Integer rankDifference = getDepartGread(depart)-empLv;
		if(rankDifference == -1){
			manager = commonService.findUniqueByProperty(TSUser.class, "userName", inputId);
		}else if(rankDifference == 0){
			manager = getDepartChief(departId);
		}else{
			for(int i = 0;i < rankDifference;i++){
				depart = depart.getTSPDepart();
			}
			manager = getDepartChief(depart.getId());
		}
		return manager;
	}

	/**
	 * 通用方法： 返回当前登陆用户可查看的部门ID
	 *
	 */
	public List<String> getMyCanSeeDeptInfo() {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		String myParentDeptOrgType = fingMyParentDeptOrgType(user.getDepartid());
		List<String> result = new ArrayList<String>();
		if("4".equals(myParentDeptOrgType)) { //判断 是否是 BU中的reporter
			String myRole = employeeDeclareRepo.findMyRole(user.getId());
			if("t_report".equals(myRole)) {  //返回两个部门 BU-TECH BU-OP
				String sql = "select id from t_s_depart where parentdepartid=(select parentdepartid from t_s_depart where id=?)";
				result = getSession().createSQLQuery(sql).setParameter(0, user.getDepartid()).list();
			}else {//返回一个部门
				result.add(user.getDepartid());
			}
		}else {
			result.add(user.getDepartid());
		}
		return result;
	}


	/**
	 * 查询 父类部门 orgType
	 *
	 */
	private String fingMyParentDeptOrgType(String deptId) {
		String orgType = null;
		String sql = "select org_type from t_s_depart where id=(select parentdepartid from t_s_depart where id=?)";
		orgType = (String)getSession().createSQLQuery(sql).setParameter(0, deptId).uniqueResult();
		return orgType;
	}

	/**
	 * 查询 部门层级
	 * control 1，check 2,错误 0
	 */
	public Integer getDepartGread(TSDepart depart){
		Integer gread =0;
		int lv = 0;
		if(StringUtil.isNotEmpty(depart.getOrgCode())){
			lv = depart.getOrgCode().length();
		}
		if(lv<7){
			if("A07A01".equals(depart.getOrgCode()))
				gread =1;
			if("A07A02".equals(depart.getOrgCode()))
				gread =2;
		}else{
			gread = lv/3;
		}
		return gread;
	}
	/**
	 * 查询 部门当前部门下的所有子部门
	 * 包括子部门的子部门和当前部门
	 * control 1，check 2,错误 0
	 */
	public List<TSDepart> subdivision(TSDepart depart){
		List<TSDepart> departs = getSession().createQuery("from TSDepart t where t.orgCode like :orgCode").setString("orgCode", depart.getOrgCode()+"%").list();
		return departs;
	}
	/**
	 * 查询当前部门下的所有子部门(1.包括当前部门 2.不用考虑第一二层特殊情况)
	 * @param depart
	 * @return
	 */
	public List<TSDepart> findSubDepart(TSDepart depart){
		String orgCode = depart.getOrgCode();
		if("A07A01".equals(depart.getOrgCode())||"A07A02".equals(depart.getOrgCode())) {
			orgCode = "A07";
		}
		List<TSDepart> departs = getSession().createQuery("from TSDepart t where t.orgCode like :orgCode").setString("orgCode", orgCode+"%").list();
		return departs;
	}

	public TSDepart getCurrentDepart(TSUser user) {
		TSDepart depart = (TSDepart) getSession().createQuery("from TSDepart t where t.id like :id").setString("id", user.getDepartid()).uniqueResult();
		return depart;
	}
	/**
	 * 获取员工收支 初始状态值
	 * @param user
	 * @return
	 */
	public Integer getDeclareInitStatusNumber(TSUser user) {
		Integer departGread = getDepartGread(getCurrentDepart(user));
		String roleName = findMyRoleByUserName(user.getUserName());
		if(roleName.equals("t_input")) {
			return (departGread+1)*3+3;  //
		}else if(roleName.contains("report")) {
			return (departGread+1)*3;    //
		}else {
			return (departGread+1)*3;	//
		}
	}



	/**
	 * 通过 账户ID返回对应的账户名
	 * @param userId
	 * @return
	 */
	public String findUserNameByUserId(String userId) {
		String sql = "select username from t_s_base_user where id=?";
		String username = (String) getSession().createSQLQuery(sql).setParameter(0, userId).uniqueResult();
		return username;
	}

	/**
	 * 通过用户名userName 找到对应角色名 (可以录入本部员工)
	 * @author gc
	 */
	public String findMyRoleByUserName(String userName) {
		// TODO Auto-generated method stub
		String sql = "select id from t_s_base_user where username=?";
		String id = (String) getSession().createSQLQuery(sql).setParameter(0, userName).uniqueResult();
		sql = "select tsr.rolecode from t_s_role tsr LEFT JOIN t_s_role_user tsru on tsr.id=tsru.roleid where userid=?";
		String roleName = (String) getSession().createSQLQuery(sql).setString(0, id).uniqueResult();
		return roleName;
	}

	/**
	 * 通过 顾客id找到对应签约法人id
	 * @param customerId
	 * @return
	 */
	public Integer findCorporateIdByCustomerId(Integer customerId) {
		String sql = "select sign_corporate from c_customer_info where id=?";
		Integer corporateId = (Integer) getSession().createSQLQuery(sql).setParameter(0, customerId).uniqueResult();
		return corporateId;
	}

	/**
	 * 成本统计用查询，部门的OP或者TECH员工薪资
	 * @param customerId
	 * @return
	 */
	public Map<String,Double> getDepatrCost(String place,Date date){
		Map<String,Double> result = new HashMap<>();
		if("江苏".equals(place)){
			//查询发薪地1为江苏的员工id
			List<EmployeeInfoEntity> employee1 = getSession().createQuery("from EmployeeInfoEntity t where (date_format(t.effectiveDate,'%Y-%m')<=date_format(:month1,'%Y-%m')"
					+"and date_format(t.expiryDate,'%Y-%m')>date_format(:month2,'%Y-%m'))"
					+"and t.a1Place = :place ").setString("place", place).setString("month1",(new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setString("month2",(new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).list();
			//查询发薪地2为江苏的员工id
			List<EmployeeInfoEntity> employee2 = getSession().createQuery("from EmployeeInfoEntity t where (date_format(effectiveDate,'%Y-%m')<=date_format(:month1,'%Y-%m')"
					+"and date_format(t.expiryDate,'%Y-%m')>date_format(:month2,'%Y-%m'))"
					+"and t.a2Payment is not null and t.a2Payment>0").setString("month1",(new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setString("month2",(new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).list();
			List<TSDepart> depart = findMyDeptInfo();
			for(TSDepart dep:depart){

				String aString = (new java.text.SimpleDateFormat("yyyy-MM-01")).format(date);
				List<EmployeeDeclareEntity> costallOp1 = new ArrayList<>();
				List<EmployeeDeclareEntity> costallTech1 = new ArrayList<>();
				if(StringUtil.isNotEmpty(employee1)&&employee1.size()>0){
					//发薪地1的本部员工
					costallOp1 = getSession().createQuery("from EmployeeDeclareEntity t where date_format(t.salaryDate,'%Y-%m') = date_format(:Month,'%Y-%m')"
							+"and employeeInfo in (:empId) and t.employeeDepartment = :depart and t.declareStatus= 2").setString("Month", (new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setParameterList("empId", employee1).setString("depart", dep.getId()).list();
					//发薪地1的外派员工
					//costallTech1 = getSession().createQuery("from EmployeeDeclareEntity t where date_format(t.salaryDate,'%Y-%m') = date_format(:Month,'%Y-%m')"
					//		+"and employeeInfo in (:empId) and t.employeeDepartment = :depart and t.employeeType = 0 and t.declareStatus= 2").setString("Month", (new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setParameterList("empId", employee1).setString("depart", dep.getId()).list();
				}
				List<EmployeeDeclareEntity> costallTech2 = new ArrayList<>();
				List<EmployeeDeclareEntity> costallOp2  = new ArrayList<>();
				if(StringUtil.isNotEmpty(employee2)&&employee2.size()>0){
					//发薪地2的本部员工
					costallOp2 = getSession().createQuery("from EmployeeDeclareEntity t where date_format(t.salaryDate,'%Y-%m') = date_format(:Month,'%Y-%m')"
							+"and t.employeeInfo in (:empId) and t.employeeDepartment = :depart and t.declareStatus= 2").setString("Month", (new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setParameterList("empId", employee2).setString("depart", dep.getId()).list();
					//发薪地2的外派员工
					//costallTech2 = getSession().createQuery("from EmployeeDeclareEntity t where date_format(t.salaryDate,'%Y-%m') = date_format(:Month,'%Y-%m')"
					//		+"and t.employeeInfo in (:empId) and t.employeeDepartment = :depart and t.employeeType = 0 and t.declareStatus= 2").setString("Month", (new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setParameterList("empId", employee2).setString("depart", dep.getId()).list();
				}
				//计算OP成本
				Double finCostOp = 0.0;
				for(EmployeeDeclareEntity cost :costallOp1){
					if(cost.getPayableSalary()>cost.getEmployeeInfo().getA1Payment()){
						finCostOp = finCostOp+cost.getEmployeeInfo().getA1Payment()+cost.getSixCompanyBurdenOne();
					}else{
						finCostOp = finCostOp+cost.getPayableSalary()+cost.getSixCompanyBurdenOne();
					}
				}
				for(EmployeeDeclareEntity cost :costallOp2){
					if(cost.getPayableSalary()>cost.getEmployeeInfo().getA1Payment()){
						finCostOp =finCostOp +(cost.getPayableSalary()-cost.getEmployeeInfo().getA1Payment());
					}
				}
				//判断是否有TECH成本
				/*if((StringUtil.isNotEmpty(costallTech1)&&costallTech1.size()>0)||(StringUtil.isNotEmpty(costallTech2)&&costallTech2.size()>0)){
					result.put(dep.getDepartname()+"-OP", finCostOp);
					Double finCostTech = 0.0;
					for(EmployeeDeclareEntity cost :costallTech1){
						if(cost.getPayableSalary()>cost.getEmployeeInfo().getA1Payment()){
							finCostTech = finCostTech+cost.getEmployeeInfo().getA1Payment()+cost.getSixCompanyBurdenOne();
						}else{
							finCostTech = finCostTech+cost.getPayableSalary()+cost.getSixCompanyBurdenOne();
						}
					}
					for(EmployeeDeclareEntity cost :costallTech2){
						if(cost.getPayableSalary()>cost.getEmployeeInfo().getA1Payment()){
							finCostTech =finCostTech +(cost.getPayableSalary()-cost.getEmployeeInfo().getA1Payment());
						}
					}
					result.put(dep.getDepartname()+"-TECH", finCostOp);
				}else{*/
					result.put(dep.getDepartname(), finCostOp);
				//}
			}
		}else{
			//查询发薪1为选定发薪地的员工id
			List<EmployeeInfoEntity> employee = getSession().createQuery("from EmployeeInfoEntity t where (date_format(t.effectiveDate,'%Y-%m')<=date_format(:month1,'%Y-%m')"
				+"and date_format(t.expiryDate,'%Y-%m')>date_format(:month2,'%Y-%m'))"
				+"and t.a1Place = :place").setString("place", place).setString("month1",(new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setString("month2",(new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).list();
			List<TSDepart> depart = findMyDeptInfo();
			//循环每个部门
			for(TSDepart dep:depart){
				Double finCostOp = 0.0;
				List<EmployeeDeclareEntity> costallOp = new ArrayList<>();
				List<EmployeeDeclareEntity> costallTech = new ArrayList<>();
				if(StringUtil.isNotEmpty(employee)&&employee.size()>0){
					//查询该部门本部员工成本
					costallOp = getSession().createQuery("from EmployeeDeclareEntity t where date_format(t.salaryDate,'%Y-%m') = date_format(:Month,'%Y-%m')"
							+"and t.employeeInfo in (:empId) and t.employeeDepartment = :depart and t.declareStatus= 2").setString("Month", (new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setParameterList("empId", employee).setString("depart", dep.getId()).list();
					//查询该部门外派员工成本
					//costallTech = getSession().createQuery("from EmployeeDeclareEntity t where date_format(t.salaryDate,'%Y-%m') = date_format(:Month,'%Y-%m')"
					//		+"and employeeInfo in (:empId) and t.employeeDepartment = :depart and t.employeeType = 0 and t.declareStatus= 2").setString("Month", (new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setParameterList("empId", employee).setString("depart", dep.getId()).list();
				}
				//计算本部员工的成本
				for(EmployeeDeclareEntity cost :costallOp){
					if(cost.getPayableSalary()>cost.getEmployeeInfo().getA1Payment()){
						finCostOp = finCostOp+cost.getEmployeeInfo().getA1Payment()+cost.getSixCompanyBurdenOne();
					}else{
						finCostOp = finCostOp+cost.getPayableSalary()+cost.getSixCompanyBurdenOne();
					}
				}
				//判断该部门有没有外派员工成本
				//if(!StringUtil.isNotEmpty(costallTech)){
					result.put(dep.getDepartname(), finCostOp);
				/*}else{
					Double finCostTech = 0.0;
					result.put(dep.getDepartname()+"-OP", finCostOp);
					for(EmployeeDeclareEntity cost :costallTech){
						if(cost.getPayableSalary()>cost.getEmployeeInfo().getA1Payment()){
							finCostTech = finCostTech+cost.getEmployeeInfo().getA1Payment()+cost.getSixCompanyBurdenOne();
						}else{
							finCostTech = finCostTech+cost.getPayableSalary()+cost.getSixCompanyBurdenOne();
						}
					}
					result.put(dep.getDepartname()+"-TECH",finCostTech);
				}*/
			}
		}
		return result;
	}
	/**
	 * 财务凭证用查询,银行导出用查询
	 * 根据发薪地查询员工薪资信息
	 * @param customerId
	 * @return
	 */
	public List<EmployeeDeclareEntity> getVoucherList (String place ,Date date){
		List<EmployeeInfoEntity> employee = getSession().createQuery("from EmployeeInfoEntity t where (date_format(t.effectiveDate,'%Y-%m')<=date_format(:month1,'%Y-%m')"
				+"and date_format(t.expiryDate,'%Y-%m')>date_format(:month2,'%Y-%m'))"
				+"and (t.a1Place = :place or (t.a2Place = :place1 and t.a2Payment>0))").setString("place", place).setString("place1", place).setString("month1",(new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setString("month2",(new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).list();
		if(StringUtil.isNotEmpty(employee)&&!employee.isEmpty()){
			List<EmployeeDeclareEntity> costallTech = getSession().createQuery("from EmployeeDeclareEntity t where date_format(t.salaryDate,'%Y-%m') = date_format(:Month,'%Y-%m')"
					+"and t.employeeInfo in (:empId) and t.declareStatus= 2").setString("Month", (new java.text.SimpleDateFormat("yyyy-MM-01")).format(date)).setParameterList("empId", employee).list();
			return costallTech;
		}else{
			return new ArrayList<>();
		}
	}


	/**
	 * 邮件推送 小写月份 转 汉字
	 * @param n
	 * @return
	 */
	public  String toChinese(int n) {
        String[] s1 = { "一", "二", "三", "四", "五", "六", "七", "八", "九","十","十一","十二"};
        return s1[n];
    }
	//身份证前17位每位加权因子
	private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

	//身份证第18位校检码
	private static String[] refNumber ={"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

	/**
	 * 二代身份证号码有效性校验
	 *校验对外接口
	 * @param idNo
	 * @return
	 */
	public static boolean isValidIdNo(String idNo) {
	  return isIdNoPattern(idNo) && isValidProvinceId(idNo.substring(0, 2))
	      && isValidDate(idNo.substring(6, 14)) && checkIdNoLastNum(idNo);
	}

	/**
	 * 二代身份证正则表达式
	 *
	 * @param idNo
	 * @return
	 */
	private static boolean isIdNoPattern(String idNo) {
	  return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$", idNo);
	}

	//省(直辖市)码表
	private static String provinceCode[] = { "11", "12", "13", "14", "15", "21", "22",
	        "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
	        "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
	        "64", "65", "71", "81", "82", "91" };

	/**
	 * 检查身份证的省份信息是否正确
	 * @param provinceId
	 * @return
	 */
	public static boolean isValidProvinceId(String provinceId){
	    for (String id : provinceCode) {
	        if (id.equals(provinceId)) {
	            return true;
	        }
	    }
	    return false;
	}

	/**
	 * 判断日期是否有效
	 * @param inDate
	 * @return
	 */
	 public static boolean isValidDate(String inDate) {
	    if (inDate == null){
	      return false;
	    }
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	    if (inDate.trim().length() != dateFormat.toPattern().length()){
	          return false;
	    }
	    dateFormat.setLenient(false);//执行严格的日期匹配
	    try {
	        dateFormat.parse(inDate.trim());
	    } catch (Exception e) {
	        return false;
	    }
	    return true;
	}

	 /**
	  * 计算身份证的第十八位校验码
	  * @param cardIdArray
	  * @return
	  */
	 public static String sumPower(int[] cardIdArray){
	     int result = 0;
	     for(int i=0;i<power.length;i++){
	         result += power[i] * cardIdArray[i];
	     }
	     return refNumber[(result%11)];
	 }

	 /**
	  * 校验身份证第18位是否正确(只适合18位身份证)
	  * @param idNo
	  * @return
	  */
	 public static boolean checkIdNoLastNum(String idNo){
	     if(idNo.length() != 18){
	         return false;
	     }
	     char[] tmp = idNo.toCharArray();
	     int[] cardidArray = new int[tmp.length-1];
	     int i=0;
	     for(i=0;i<tmp.length-1;i++){
	         cardidArray[i] = Integer.parseInt(tmp[i]+"");
	     }
	     String checkCode = sumPower(cardidArray);
	     String lastNum = tmp[tmp.length-1] + "";
	     if(lastNum.equals("x")){
	         lastNum = lastNum.toUpperCase();
	     }
	     if(!checkCode.equals(lastNum)){
	         return false;
	     }
	     return true;
	 }

	 //获取系统 基本工资
	 public String getSystemBasePay() {
		String sql = "select constant_value from c_constant_dic where constant_key='c_basePay'";
		String basePay = (String) getSession().createSQLQuery(sql).uniqueResult();
		return basePay;
	 }

	 //获取流转税比例
	 public String getSystemTurnoverTax() {
			String sql = "select constant_value from c_constant_dic where constant_key='c_turnoverTax'";
			String turnoverTax = (String) getSession().createSQLQuery(sql).uniqueResult();
			return turnoverTax;
	 }
	 //获取系统邮箱密码
	 public String getSystemEmailPass() {
			String sql = "select constant_value from c_constant_dic where constant_key='c_email_pass'";
			String emailPass = (String) getSession().createSQLQuery(sql).uniqueResult();
			return emailPass;
	 }
	//获取系统邮箱地址
		public String getSystemEmailAccount() {
				String sql = "select constant_value from c_constant_dic where constant_key='c_email_account'";
				String emailPass = (String) getSession().createSQLQuery(sql).uniqueResult();
				return emailPass;
		}
	//获取系统邮箱主机
	public String getSystemEmailHost() {
			String sql = "select constant_value from c_constant_dic where constant_key='c_email_host'";
			String emailPass = (String) getSession().createSQLQuery(sql).uniqueResult();
			return emailPass;
	}
	 //根据code获取系统常量
	 public String getSystemConstant(String constantCode) {
			String sql = "select constant_value from c_constant_dic where constant_key=?";
			String value = (String) getSession().createSQLQuery(sql).setParameter(0, constantCode).uniqueResult();
			return value;
	 }
	 /**
	  * 获取当前部门（包括子部门）下所有账号
	  * @param depart
	  * @return
	  */
	 public List<TSUser> getDepartAllUser(TSDepart depart) {
		 List<TSDepart> listDepart = subdivision(depart);
		 List<TSUser> all = new ArrayList<TSUser>();
		 for(TSDepart dep : listDepart) {
			 List<TSUser> departAllUser = getDepartAllUser(dep.getId());
			 all.addAll(departAllUser);
		 }
		 return all;
	 }
	 /**
	  * 通过角色名找到对应账户
	  * @param roleName
	  * @return
	  */
	 public List<TSUser> findUserByRoleName(String roleName) {
		 String sql = "select id from t_s_role where roleCode=?";
		 String id = (String) getSession().createSQLQuery(sql).setParameter(0, roleName).uniqueResult();
		 sql = "select userid from t_s_role_user where roleid=?";
		 List<String> list = getSession().createSQLQuery(sql).setParameter(0, id).list();
		 List<TSUser> result = new ArrayList<TSUser>();
		 for(String userid : list) {
			 TSUser user = (TSUser) getSession().createQuery("from TSUser where id like :id").setParameter("id", userid).uniqueResult();
			 result.add(user);
		 }
		 return result;
	 }
	 /**
	  * 发薪地与发薪公司对应函数
	  * @param roleName
	  * @return
	  */
	 public String placeToCompany(String place){
		 if("江苏".equals(place)){
			 return "江苏智蓝信息科技有限公司";
		 }else if("上海".equals(place)){
			 return "彦捷（上海）信息科技有限公司";
		 }else if("昆山".equals(place)){
			 return "昆山智蓝企业咨询服务有限公司";
		 }else if("深圳".equals(place)){
			 return "深圳智蓝信息科技有限公司";
		 }else if("广州".equals(place)){
			 return "广州智蓝信息科技有限公司";
		 }else if("北京".equals(place)){
			 return "北京智蓝云信科技有限公司";
		 }else{
			 return null;
		 }
	 }


	 /**
	  * 人力收支申报状态 转换  项目收支申报状态
	  * @param declareStatus
	  * @return
	  */
	 public Integer changeStatusEmployeeDeclareToProject(Integer declareStatus) {
		 TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		 Integer initStatus = getDeclareInitStatusNumber(user);
		 //层级
		 int lv = initStatus/3-1;
		 if(declareStatus==initStatus+1) { //查询 未上报
			 return lv*2+1;  //项目的 未上报
	     }else if(declareStatus==initStatus-3){//查询已上报
	    	 return lv*2-2;  //项目的 已上报
	     }else if(declareStatus == 2){ //审批通过
	    	 return 1;  //项目的 审批通过
	     }else if(declareStatus == initStatus||declareStatus == initStatus-1) { //待录入 待申报  等价于  （项目）待申报
	    	 return lv*2;  //项目的待申报
	     }else if(declareStatus == initStatus -2) { //申报拒绝
	    	 return lv*2-1;  //项目的申报拒绝
	     }
		 return null;
	 }

	 public Integer changeStatusProjectToEmployeeDeclare(Integer projectStatus) {
		 TSUser myUser = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		 TSDepart depart = commonService.getEntity(TSDepart.class, myUser.getDepartid());
		 int lv = getDepartGread(depart);
		 Integer initStatus = lv*2;
		 if(projectStatus == 1){ //查询审批通过
			return 2;//人力收支审批通过
 		}else if(projectStatus==initStatus+1) { //查询 未上报
 			return (lv+1)*3+1; //人力收支未上报
 		}else if(projectStatus==initStatus-2){//查询已上报
 			return (lv+1)*3-3; //人力收支已上报
 		}else if(projectStatus == initStatus ){ //待申报
 			return (lv+1)*3-1;
 		}else if(projectStatus == initStatus ) { //申报拒绝
 			return (lv+1)*3 -2;
 		}
		 return null;
	 }
 	/**
	  * 计算员工收支信息
	  * 1、计算应付基本工资：（有绩效出勤日数+无绩效出勤日数）/ 法定出勤日数 * A1（基本工资）* B（折扣率）
	  * 2、计算应付细绩效工资： 有绩效出勤日数 / 法定出勤日数 * A2（绩效工资） * B（折扣率）
	  * 3、
	  * @param employeeDeclare
	  */
	 public void calEmployeeDeclare(EmployeeDeclareEntity employeeDeclare) {
		//计算基数

		 //收入
		 //单价
		 Double unitPrice = employeeDeclare.getUnitPrice()==null?0.0:employeeDeclare.getUnitPrice();
		 //当月加算
		 Double currMonthOther = employeeDeclare.getMonthOther()==null?0.0:employeeDeclare.getMonthOther();
		 //验收加算
		 Double acceptanceAdd = employeeDeclare.getAcceptanceAdd()==null?0.0:employeeDeclare.getAcceptanceAdd();
		 //月间调整
		 Double monthAdjustment = employeeDeclare.getMonthAdjustment()==null?0.0:employeeDeclare.getMonthAdjustment();
		 //约定出勤日数
		 Double appointedAttendanceDay = employeeDeclare.getAppointedAttendanceDay();
		 //验收出勤日数
		 Double acceptedAttendanceDay = employeeDeclare.getAcceptedAttendanceDay()==null?0.0:employeeDeclare.getAcceptedAttendanceDay();

		 //支出
		 // 公司 给 员工的 补贴
		 Double cComputerSubsidy = employeeDeclare.getCComputerSubsidy()==null?0.0:employeeDeclare.getCComputerSubsidy();
		 Double cOvertimeSubsidy = employeeDeclare.getCOvertimeSalary()==null?0.0:employeeDeclare.getCOvertimeSalary();
		 Double c1Subsidy = employeeDeclare.getC1OtherSubsidy()==null?0.0:employeeDeclare.getC1OtherSubsidy();
		 Double c2Subsidy = employeeDeclare.getC2OtherSubsidy()==null?0.0:employeeDeclare.getC2OtherSubsidy();
		 Double c3Subsidy = employeeDeclare.getC3OtherSubsidy()==null?0.0:employeeDeclare.getC3OtherSubsidy();
		 //折扣率
		 Double bDiscount = employeeDeclare.getBDiscount()>100?100.0:employeeDeclare.getBDiscount();
		 //法定出勤日数
		 Double legalDays = employeeDeclare.getLegalAttendanceDay()==null?22.0:employeeDeclare.getLegalAttendanceDay();
		 //有绩效出勤日数
		 Double performanceDays = employeeDeclare.getPerformanceAttendanceDay()==null?0:employeeDeclare.getPerformanceAttendanceDay();
		 //无绩效出勤日数
		 Double noPerformanceDays = employeeDeclare.getNoPerformanceAttendanceDay()==null?0:employeeDeclare.getNoPerformanceAttendanceDay();
		 //员工 基本工资
		 Double baseSalary = employeeDeclare.getEmployeeBasePay()==null?0:employeeDeclare.getEmployeeBasePay();
		 //员工A标准工资
		 Double employeeSalary = employeeDeclare.getEmployeeASalary()==null?0:employeeDeclare.getEmployeeASalary();
		 //员工绩效工资
		 Double meritSalary = employeeSalary - baseSalary;


		 //应付工资: 应付基本工资+应付绩效+补贴合计
		 // 应付基本工资  （有绩效出勤日+无绩效出勤日）/法定出勤日*A1工资*折扣率
		 Double payableBaseSalary = (performanceDays+noPerformanceDays)/legalDays*baseSalary*bDiscount/100;
		//应付绩效：  有绩效出勤日/法定出勤日*A2绩效*B折扣率
		 Double payablePerformance = performanceDays/legalDays*meritSalary*bDiscount/100;
		 //补贴合计
		 Double totalSubsidy = cComputerSubsidy+cOvertimeSubsidy+c1Subsidy+c2Subsidy+c3Subsidy;
		 //应付合计：  应付工资+应付绩效 + 补贴合计
		 Double payableSalary = payableBaseSalary+payablePerformance+totalSubsidy;
		 employeeDeclare.setPayableSalary(payableSalary);
		 //员工到手 应付合计-个人六金-个税
		 Double perToneTaxOne = 0.0;
		 Double perToneTaxTwo = 0.0;
		 Double sixPersonalBurden = employeeDeclare.getSixPersonalBurdenOne()==null?0.0:employeeDeclare.getSixPersonalBurdenOne();
		 Double a1Payment = employeeDeclare.getEmployeeInfo().getA1Payment();
		 //个税计算
		 if(employeeDeclare.getEmployeeInfo().getA2Place()==null||employeeDeclare.getEmployeeInfo().getA2Payment()==0||payableSalary-a1Payment<=0) { //只有一个发薪地
			 //个税基数： 应付合计-个人六金
			 perToneTaxOne = Utils.calculateIndividualIncomeTax(payableSalary-sixPersonalBurden, 3500);
		 }else {  //两个发薪地
			 //发薪地1个税基数：   发薪地1金额-个人六金
			 perToneTaxOne = Utils.calculateIndividualIncomeTax(a1Payment-sixPersonalBurden, 3500);
			 //发薪地2个税基数：		发薪地2金额+（应付合计-员工标准工资）（当月薪酬浮动）
			 perToneTaxTwo = Utils.calculateIndividualIncomeTax(employeeDeclare.getEmployeeInfo().getA2Payment()+payableSalary-employeeSalary, 3500);
		 }
		 employeeDeclare.setPerToneTaxOne(perToneTaxOne);
		 employeeDeclare.setPerToneTaxTwo(perToneTaxTwo);
		 Double employeeRealSalary = payableSalary-(perToneTaxOne+perToneTaxTwo)-sixPersonalBurden;
		 employeeDeclare.setEmployeeRealSalary(employeeRealSalary);
		 //总成本  应付合计 + 公司六金负担
		 Double companyCost = payableSalary+(employeeDeclare.getSixCompanyBurdenOne()==null?0.0:employeeDeclare.getSixCompanyBurdenOne());
		 employeeDeclare.setCompanyCost(companyCost);
		 //收入
		 Double income = 0.0;
		 if(null!=employeeDeclare.getUnitPriceType()&&appointedAttendanceDay!=null){
			switch (employeeDeclare.getUnitPriceType()) {
			case 0:  //月单价     月单价*验收出勤日/约定出勤日+当月其他+月间调整+验收加算
				income = unitPrice*acceptedAttendanceDay/appointedAttendanceDay+currMonthOther+acceptanceAdd+monthAdjustment;
				if(income==null||income.isNaN()){
					employeeDeclare.setIncome(0.0);
				}else{
					employeeDeclare.setIncome(income);
				}
				break;
			case 2:  //日单价
				income = unitPrice*acceptedAttendanceDay+currMonthOther+acceptanceAdd+monthAdjustment;
				if(income==null||income.isNaN()){
					employeeDeclare.setIncome(0.0);
				}else{
					employeeDeclare.setIncome(income);
				}
				break;
			case 3: //时单价
				income = unitPrice*8*acceptedAttendanceDay+currMonthOther+acceptanceAdd+monthAdjustment;
				if(income==null||income.isNaN()){
					employeeDeclare.setIncome(0.0);
				}else{
					employeeDeclare.setIncome(income);
				}
			}
		}
		employeeDeclare.setIncome(income);
	 	//净收入
	 	Double netIncome = income;
		if(employeeDeclare.getIsTurnoverTax()!=null&&employeeDeclare.getIsTurnoverTax()==1){ //含有流转税
			String sysTurTax = getSystemTurnoverTax();
			double turTax = sysTurTax==null||"".equals(sysTurTax)?0.06:Double.parseDouble(sysTurTax)/100;
			netIncome = income * (1-turTax);
		}
		employeeDeclare.setNetIncome(netIncome);
		//毛利  净收入-总成本
		Double companyProfit = netIncome - companyCost;
		employeeDeclare.setCompanyProfit(companyProfit);
		//毛利率  毛利/收入
		double companyProfitRate = 0;
		if(netIncome>0) {
			companyProfitRate = (netIncome==0?0:companyProfit*100/netIncome);
		}
		employeeDeclare.setCompanyProfitRate(companyProfitRate);
	 }
}
