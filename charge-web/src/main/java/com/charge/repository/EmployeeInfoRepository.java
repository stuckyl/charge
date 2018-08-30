package com.charge.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.minidao.annotation.Sql;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDataRule;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.stereotype.Repository;

import com.charge.entity.EmployeeInfoEntity;
import com.charge.utils.CalendarUtil;
import com.fasterxml.jackson.core.io.SegmentedStringWriter;
import com.thoughtworks.xstream.io.json.JsonWriter.Format;

/**
* @Title: EmployeeInfoRepository
* @Description: 员工信息持久层
* @author wenst
* @date 2018年3月22日
* @version v1.0
*/
@Repository
public class EmployeeInfoRepository extends GenericBaseCommonDao<EmployeeInfoEntity, Integer>{
	private static final Logger log = Logger.getLogger(EmployeeInfoRepository.class);


	/**
	 * 更新员工信息
	 * @param employeeInfo
	 */
	public void update(EmployeeInfoEntity employeeInfo){
		getSession().update(employeeInfo);
		getSession().flush();
	}
	/**
	 *得到部门总监 userName
	 *根据部门id
	 *+"and t.TSRole.roleCode = :roleCode"  and
	 *.setString("roleCode", "t_report")
	 * @return
	 * */
	public String getInputerUserName(String id) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		List<String> role = new ArrayList<>();
		role.add("t_report");
		role.add("t_input_m");
		List<TSRoleUser> input =  getSession().createQuery("from TSRoleUser t where t.TSUser.departid = :departid and t.TSRole.roleCode in (:roleCode) "
				).setString("departid", id).setParameterList("roleCode", role).list();
		if(!(null == input || input.size() ==0)){
			for(TSRoleUser m : input){
				if(m.getTSUser().getStatus()==1){
					return m.getTSUser().getUserName();
				}
			}
		}
		return null;
	}

	/**
	 * 获取基本信息
	 * @param findBasePay
	 * @return
	 */
	public String findBasePay(){
		List<TSType> tsTypes = getSession().createQuery("from "
				+ "TSType t where t.typename = :type").setString("type", "basepay").list();
		return tsTypes.get(0).getTypecode();
	}
	/**
	 * 根据部门id和申报状态查找员工数据
	 * @param departId
	 * @param declareStatus
	 * @return
	 */
	public List<EmployeeInfoEntity> findByDeclareStatus(String departId,Object[] declareStatus){
		List<EmployeeInfoEntity> employeeInfoList = null;
		if(StringUtils.isNotBlank(departId)){
			employeeInfoList = getSession().createQuery("from EmployeeInfoEntity t where "
					+ "t.delFlg = 0 and t.department = :departId and "
					+ " t.declareStatus in (:status) ").setString("departId", departId).setParameterList("status", declareStatus).list();

			//from EmployeeInfoEntity t where t.delFlg = 0 and t.department.id = departId and  t.declareStatus in (1，7)
		}else{
			employeeInfoList = getSession().createQuery("from EmployeeInfoEntity t where "
					+ "t.delFlg = 0 and t.declareStatus in (:status) ").setParameterList("status", declareStatus).list();
		}
		return employeeInfoList;
	}

	/**
	 * 根据部门id和申报状态查找员工数据
	 * @param departId
	 * @param declareStatus
	 * @param empFlg 0外派  1本部
	 * @return
	 */
	public List<EmployeeInfoEntity> findByDeclareStatus(String departId,Object[] declareStatus,int empFlg){
		List<EmployeeInfoEntity> employeeInfoList = null;
		if(StringUtils.isNotBlank(departId)){
			employeeInfoList = getSession().createQuery("from EmployeeInfoEntity t where "
					+ "t.delFlg = 0 and t.employeeFlag =:empFlg and t.department = :departId and "
					+ " t.declareStatus in (:status) ").setInteger("empFlg", empFlg).setString("departId", departId).setParameterList("status", declareStatus).list();
		}else{
			employeeInfoList = getSession().createQuery("from EmployeeInfoEntity t where "
					+ "t.delFlg = 0 and t.employeeFlag =:empFlg and t.declareStatus in (:status) ").setInteger("empFlg", empFlg).setParameterList("status", declareStatus).list();
		}
		return employeeInfoList;
	}

	/**
	 * 查找固定员工信息
	 * j 5-9
	 *
	 * @param id
	 *
	 */

	public List<EmployeeInfoEntity> findByDeclareStatusId(List<Integer> id){
		List<EmployeeInfoEntity> employeeInfoList = null;
		employeeInfoList = getSession().createQuery("from EmployeeInfoEntity t where "
				+ "t.delFlg = 0 and t.id in (:id) ").setParameterList("id", id).list();
		return employeeInfoList;
	}


	public List<EmployeeInfoEntity> findByDeclareStatusTsUserId(String departId,Integer[] declareStatus,String tsUserId){
		List<EmployeeInfoEntity> employeeInfoList = null;
		if(StringUtils.isNotBlank(departId)){
			employeeInfoList = getSession().createQuery("from EmployeeInfoEntity t where "
					+ "t.delFlg = 0 and t.department = :departId and "
					+ "and t.tsUserId = :tsUserId"
					+ " t.declareStatus in (:status) "
					).setString("departId", departId).setString("tsUserId",tsUserId).setParameterList("status", declareStatus).list();

			//from EmployeeInfoEntity t where t.delFlg = 0 and t.department.id = departId and  t.declareStatus in (1，7)
		}else{
			employeeInfoList = getSession().createQuery("from EmployeeInfoEntity t where "
					+ "t.delFlg = 0 and t.declareStatus in (:status) ").setParameterList("status", declareStatus).list();
		}
		return employeeInfoList;
	}


	/**
	 * 0505：不需要 申请状态判断
	 * 外派将指定部门下人员查询出来
	 * @return
	 */
	public List<EmployeeInfoEntity> findByDepartIds(String... departId){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		Calendar calendar=Calendar.getInstance();
		Date theDate=calendar.getTime();
		GregorianCalendar gcLast=(GregorianCalendar)Calendar.getInstance();
		gcLast.setTime(theDate);
		//设置为第一天
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first=da.format(gcLast.getTime());
		//打印本月第一天
		System.out.println("day_first  :"+ day_first);
		// 获取上个月
        Date date = null;
        Date dateNow = null;
        try {
            date = sdf.parse(CalendarUtil.getLastMonthDate("yyyy-MM-01"));
            dateNow = sdf.parse(CalendarUtil.getLastMonthDate("yyyy-MM-01"));
            System.out.println("date  :"+ date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date sum = (Date) getSession().createQuery("select entryDate from EmployeeInfoEntity t where t.id = 3").uniqueResult();
        System.out.println("sum  :"+ sum);

		/*List<EmployeeInfoEntity> rsList = getSession().createQuery("from EmployeeInfoEntity t where t.delFlg = 0 t.employeeFlag = 0 "
				+ " and (t.entryDate  < :dateNow) and (t.quitDate is null or t.quitDate  >= :date) and t.department.id in (:departId) ")
				.setDate("dateNow", dateNow).setDate("date", date).setParameterList("departId", departId).list();*/
//        List<EmployeeInfoEntity> rsList = getSession().createQuery("from EmployeeInfoEntity t where t.delFlg = 0 and t.employeeFlag = 0  and t.declareStatus = 6"
//				+ " and (t.quitDate is null or t.quitDate  >= :date) and t.department.id in (:departId) ")
//				.setDate("date", date).setParameterList("departId", departId).list();
        List<EmployeeInfoEntity> rsList = getSession().createQuery("from EmployeeInfoEntity t where t.delFlg = 0 and t.employeeFlag = 0"
				+ " and (t.quitDate is null or t.quitDate  >= :date) and t.department.id in (:departId) ")
				.setDate("date", date).setParameterList("departId", departId).list();
		return rsList;
	}
	/**
	 * 0505：不需要 申请状态判断
	 * 将指定部门下人员查询出来
	 * @return
	 */
	public List<EmployeeInfoEntity> findByDepartIdsAll(String... departId){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取上个月
        Date date = null;
        Date dateNow = null;
        try {
            date = sdf.parse(CalendarUtil.getLastMonthDate("yyyy-MM-01"));
            dateNow = sdf.parse(CalendarUtil.getLastMonthDate("yyyy-MM-01"));
            System.out.println("date  :"+ date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        List<EmployeeInfoEntity> rsList = getSession().createQuery("from EmployeeInfoEntity t where t.delFlg = 0 and t.declareStatus = 6"
//				+ " and (t.quitDate is null or t.quitDate  >= :date) and t.department.id in (:departId) ")
//				.setDate("date", date).setParameterList("departId", departId).list();
        List<EmployeeInfoEntity> rsList = getSession().createQuery("from EmployeeInfoEntity t where t.delFlg = 0"
				+ " and (t.quitDate is null or t.quitDate  >= :date) and t.department.id in (:departId) ")
				.setDate("date", date).setParameterList("departId", departId).list();
		return rsList;
	}

	/**
	 * 获取上个月可以结算工资的员工数据
	 * 一、未离职的，判断入职不是当月的、员工标志派遣、未删除的、本部门的。
	 * 二、离职的分为4种情况
	 *        1.入职日不包含上个月，离职日小于当月。
	 *        2.入职日不包含上个月，离职日等于当月。
	 *        3.入职日包含上个月，离职日小于当月。
	 *        4.入职日包含上个月，离职日等于当月。
	 * @return
	 */
	public List<EmployeeInfoEntity> getLastMonth(String departId){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH-1));
		date = calendar.getTime();
		String lastMonthStr = new SimpleDateFormat("yyyy-MM").format(date);
		List<EmployeeInfoEntity> rs = new ArrayList<EmployeeInfoEntity>();
		List<EmployeeInfoEntity> noResignationList = getSession().createQuery("from EmployeeInfoEntity t where  t.department.id=:departId and "
				+ "t.delFlg = 0 and t.employeeFlag = 0 and t.quitDate is null and date_format(t.entryDate,'%Y-%m')<date_format(now(),'%Y-%m')").setString("departId", departId).list();
		List<EmployeeInfoEntity> aResignationList =  getSession().createQuery("from EmployeeInfoEntity t where t.department.id=:departId and "
				+ "t.delFlg = 0 and t.employeeFlag = 0 and date_format(t.entryDate,'%Y-%m')< '"+lastMonthStr+"' and date_format(t.quitDate,'%Y-%m')<date_format(now(),'%Y-%m')").setString("departId", departId).list();
		List<EmployeeInfoEntity> bResignationList = getSession().createQuery("from EmployeeInfoEntity t where t.department.id=:departId and "
				+ "t.delFlg = 0 and t.employeeFlag = 0 and date_format(t.entryDate,'%Y-%m') < '"+lastMonthStr+"' and date_format(t.quitDate,'%Y-%m')=date_format(now(),'%Y-%m')").setString("departId", departId).list();
		List<EmployeeInfoEntity> cResignationList = getSession().createQuery("from EmployeeInfoEntity t where t.department.id=:departId and "
				+ "t.delFlg = 0 and t.employeeFlag = 0 and date_format(t.entryDate,'%Y-%m')= '"+lastMonthStr+"' and date_format(t.quitDate,'%Y-%m')<date_format(now(),'%Y-%m')").setString("departId", departId).list();
		List<EmployeeInfoEntity> dResignationList = getSession().createQuery("from EmployeeInfoEntity t where t.department.id=:departId and "
				+ "t.delFlg = 0 and t.employeeFlag = 0 and date_format(t.entryDate,'%Y-%m')='"+lastMonthStr+"' and date_format(t.quitDate,'%Y-%m')=date_format(now(),'%Y-%m')").setString("departId", departId).list();
		rs.addAll(noResignationList);
		rs.addAll(aResignationList);
		rs.addAll(bResignationList);
		rs.addAll(cResignationList);
		rs.addAll(dResignationList);
		return rs;
	}

	/**
	 * 0505：不需要 申请状态判断
	 * 获取当月可以结算工资的员工数据
	 * 一、未离职的，员工标志派遣、未删除的、本部门的、审批通过的。
	 * 二、当月离职的，员工标志派遣、未删除的、本部门的、审批通过的。
	 * @return
	 */
	public List<EmployeeInfoEntity> getSameMonth(String departId){
		List<EmployeeInfoEntity> rs = new ArrayList<EmployeeInfoEntity>();
//		List<EmployeeInfoEntity> aList = getSession().createQuery("from EmployeeInfoEntity t where t.department.id=:departId and "
//				+ "t.delFlg = 0 and t.employeeFlag = 0 and t.declareStatus=6 and t.quitDate is null").setString("departId", departId).list();
//		List<EmployeeInfoEntity> bList = getSession().createQuery("from EmployeeInfoEntity t where t.department.id=:departId and "
//				+ "t.delFlg = 0 and t.employeeFlag = 0 and t.declareStatus=6 and date_format(t.quitDate,'%Y-%m') = date_format(now(),'%Y-%m')").setString("departId", departId).list();
		List<EmployeeInfoEntity> aList = getSession().createQuery("from EmployeeInfoEntity t where t.department.id=:departId and "
				+ "t.delFlg = 0 and t.employeeFlag = 0 and t.quitDate is null").setString("departId", departId).list();
		List<EmployeeInfoEntity> bList = getSession().createQuery("from EmployeeInfoEntity t where t.department.id=:departId and "
				+ "t.delFlg = 0 and t.employeeFlag = 0 and date_format(t.quitDate,'%Y-%m') = date_format(now(),'%Y-%m')").setString("departId", departId).list();
		rs.addAll(aList);
		rs.addAll(bList);
		return rs;
	}


	/**
	 * 统计自定义条件满足数量
	 * @param condition
	 * @return
	 */
	public Long countByCusField(String condition){
		Long count = (Long) getSession().createQuery("select count(*) from EmployeeInfoEntity t where "+condition).uniqueResult();
		return count;
	}


	/**
	 * 查询部门下指定人员数据
	 * @param departId
	 * @param declareStatus
	 * @param empFlg
	 * @return
	 */
	public List<EmployeeInfoEntity> findByDepartId(String[] departId,Integer empFlg,Integer[] declareStatus){
		List<EmployeeInfoEntity> empList = getSession().createQuery("from EmployeeInfoEntity t where t.delFlg = 0 and t.employeeFlag = :empFlg and "
				+ " t.department.id in (:departId) and t.declareStatus in (:declareStatus) ")
		.setInteger("empFlg", empFlg).setParameterList("departId", departId).setParameterList("declareStatus", declareStatus).list();
		return empList;
	}
	/**
	 * 0507 修改-查询部门下指定人员数据
	 * @param departId
	 * @param empFlags 外派：0 本部：1
	 * @return
	 */
	public List<EmployeeInfoEntity> findByDepartId(String[] departId,Integer[] empFlags){
		List<EmployeeInfoEntity> empList = getSession().createQuery("from EmployeeInfoEntity t where t.delFlg = 0 and t.employeeFlag in (:empFlg) and "
				+ " t.department in (:departId)")
		.setParameterList("empFlg", empFlags).setParameterList("departId", departId).list();
		return empList;
	}


	/**
	 * 查询所有部门所有员工
	 *  2018-05-05 增
	 */
	public List<EmployeeInfoEntity> findByAllDepartId(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		Date dateNow = null;
		try {
			date = sdf.parse(CalendarUtil.getLastMonthDate("yyyy-MM-01"));
			dateNow = sdf.parse(CalendarUtil.getFirstMonthDay("yyyy-MM-01"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(date+"  "+dateNow);
		// 查询所有员工 和 辞职日期 大于 上个月1号的（上个月有上班)
		List<EmployeeInfoEntity> rsList = getSession().createQuery("from EmployeeInfoEntity t where t.delFlg = 0"
				+ " and (t.quitDate is null or t.quitDate  >= :date) ").setDate("date", date).list();
		return rsList;
	}

	/**
	 * 查询所有待处理员工,controller
	 *  2018-08-09 j 增
	 */
	public Integer controllerPand (){
		Long count =(Long)getSession().createQuery("select count(*) from EmployeeInfoEntity t where t.declareStatus=4"
										+" and (date_format(effectiveDate,'%Y-%m')<=date_format(:month1,'%Y-%m')"
										+"and date_format(expiryDate,'%Y-%m')>date_format(:month2,'%Y-%m'))").setString("month1",(new java.text.SimpleDateFormat("yyyy-MM-dd")).format(new Date())).setString("month2",(new java.text.SimpleDateFormat("yyyy-MM-dd")).format(new Date())).uniqueResult();

		return (Integer)count.intValue();
	}
	/**
	 * 查询所有待处理员工,checker
	 *  2018-08-09 j 增
	 */
	public Integer checkerPand(){
		Long count =(Long)getSession().createQuery("select count(*) from EmployeeInfoEntity t where (date_format(effectiveDate,'%Y-%m')<=date_format(:month1,'%Y-%m')"
				+"and date_format(expiryDate,'%Y-%m')>date_format(:month2,'%Y-%m'))"
				+"and (t.insurance=0 or t.insurance=2)").setString("month1",(new java.text.SimpleDateFormat("yyyy-MM-dd")).format(new Date())).setString("month2",(new java.text.SimpleDateFormat("yyyy-MM-dd")).format(new Date())).uniqueResult();
	return (Integer)count.intValue();
	}
	/**
	 * 查询所有待处理员工,rep和inp
	 *  2018-08-09 j 增
	 */
	public Integer otherPand(Integer lv,List<String> depart){
		Long count =(Long)getSession().createQuery("select count(*) from EmployeeInfoEntity t where (date_format(effectiveDate,'%Y-%m')<=date_format(:month1,'%Y-%m')"
				+"and date_format(expiryDate,'%Y-%m')>date_format(:month2,'%Y-%m'))"
				+"and (t.declareStatus=:lv1 or t.declareStatus=:lv2)"
				+"and t.department in (:depart)").setString("month1",(new java.text.SimpleDateFormat("yyyy-MM-dd")).format(new Date())).setString("month2",(new java.text.SimpleDateFormat("yyyy-MM-dd")).format(new Date())).setInteger("lv1", lv).setInteger("lv2", lv-1).setParameterList("depart", depart).uniqueResult();
		return (Integer)count.intValue();
	}


	public Integer inputerPand(Integer lv,TSUser user){
		Long count =(Long)getSession().createQuery("select count(*) from EmployeeInfoEntity t where (date_format(effectiveDate,'%Y-%m')<=date_format(:month1,'%Y-%m')"
				+"and date_format(expiryDate,'%Y-%m')>date_format(:month2,'%Y-%m'))"
				+"and (t.declareStatus=:lv1 or t.declareStatus=:lv2)"
				+"and t.inputerId = :inputer").setString("month1",(new java.text.SimpleDateFormat("yyyy-MM-dd")).format(new Date())).setString("month2",(new java.text.SimpleDateFormat("yyyy-MM-dd")).format(new Date())).setInteger("lv1", lv).setInteger("lv2", lv-1).setString("inputer", user.getUserName()).uniqueResult();
		return (Integer)count.intValue();
	}
	/**
	 * 清空表EmployeeInfoEntity
	 *  2018-07-11
	 */
	public void emptyAllEmployeeInfo(){
		getSession().createQuery("delete EmployeeInfoEntity").executeUpdate();
	}

}