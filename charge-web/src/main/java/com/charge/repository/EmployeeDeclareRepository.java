package com.charge.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.p3.core.utils.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.charge.entity.EmployeeDeclareCopyEntity;
import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
/**
* @Title: EmployeeDeclareRepository
* @Description: 员工申报表持久层
* @author wenst
* @date 2018年3月22日
* @version v1.0
*/
@Repository
public class EmployeeDeclareRepository extends GenericBaseCommonDao<EmployeeDeclareEntity, Integer>{

	@Autowired
	private CommonRepository commonRepository;

	/**
	 * 将部门作为条件查询所有人员收入信息
	 * @return
	 */
	public List<EmployeeDeclareEntity> findAllByCurrMonthDepart(String departId){
//		List<EmployeeDeclareEntity> employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity t where t.declareStatus in (0,3) and "
//				+ " date_format(t.createdDate,'%Y-%m') = date_format(now(),'%Y-%m') and t.employeeInfo.department.id=:departId ")
//				.setString("departId", departId).list();
		List<EmployeeDeclareEntity> employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity t where t.declareStatus in (0,3) and "
				+ " t.employeeInfo.department.id=:departId ")
				.setString("departId", departId).list();
		return employeeDeclareList;
	}

	/**
	 * 通过ID查找申报员工数据
	 * @param id
	 * @return
	 */
	public EmployeeDeclareEntity findById(Integer id){
		EmployeeDeclareEntity employeeDeclareEntity = (EmployeeDeclareEntity) getSession().createQuery("from EmployeeDeclareEntity t "
				+ "where t.id = :id").setInteger("id", id).uniqueResult();
		return employeeDeclareEntity;
	}



	/**
	 * 0505 新增
	 * 通过员工ID 和 生成月份 获取收入数据
	 * @return
	 */
	public EmployeeDeclareEntity findByMonthAndEmployeeId(Integer id,String month){
		EmployeeDeclareEntity employeeDeclare =
				(EmployeeDeclareEntity) getSession().createQuery("from EmployeeDeclareEntity t where t.delFlg=0 and date_format(t.salaryDate,'%Y-%m') = date_format(:requiredMonth,'%Y-%m') "
						+ " and t.employeeInfo.id = :employeeId ").setString("requiredMonth",month+"-01").setInteger("employeeId", id).uniqueResult();
		return employeeDeclare;
	}
	/**
	 * 0506 新增
	 * 批量 通过 员工ID集合 和 生成月份 获取收入数据
	 * @return
	 */
	public List<EmployeeDeclareEntity> findAllByMonthAndEmployeeId(Integer[] ids,String month){
		List<EmployeeDeclareEntity> employeeDeclareList = new ArrayList<EmployeeDeclareEntity>();
		employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity t where t.delFlg=0 and date_format(t.createdDate,'%Y-%m') = date_format(:requiredMonth,'%Y-%m')  "
				+ "and t.employeeInfo.id in (:employeeId) ").setString("requiredMonth",month).setParameterList("employeeId", ids).list();
		return employeeDeclareList;
	}

	/**
	 * 根据employeeDeclare中 月份和法人
	 * 查找所有 employeeDeclare
	 * @param employeeDeclare
	 * @return
	 */
	public List<EmployeeDeclareEntity> findAllEmpDeclareByMonthAndCorporateId(EmployeeDeclareEntity employeeDeclare){
		//employeeDeclare.setCorporateId(68);
		List<EmployeeDeclareEntity> employeeDeclareList = new ArrayList<EmployeeDeclareEntity>();
		if(employeeDeclare.getSalaryDate() == null){
		}else{
			if(employeeDeclare.getCorporateId() != null){
				//System.out.println("111" + employeeDeclare.getCorporateId());
				employeeDeclareList =(List<EmployeeDeclareEntity>) getSession().createQuery("from EmployeeDeclareEntity t where t.delFlg=0 and "
						+ "date_format(t.salaryDate,'%Y-%m') = date_format(:requiredMonth,'%Y-%m') and "
						+ "t.corporateId = :requiredCorporateId").setDate(
								"requiredMonth", employeeDeclare.getSalaryDate()).setInteger(
								"requiredCorporateId", employeeDeclare.getCorporateId()).list();
			}else{
				employeeDeclareList =(List<EmployeeDeclareEntity>) getSession().createQuery("from EmployeeDeclareEntity t where t.delFlg=0 and "
						+ "date_format(t.salaryDate,'%Y-%m') = date_format(:requiredMonth,'%Y-%m') ").setDate("requiredMonth", employeeDeclare.getSalaryDate()).list();
			}
		}
		return employeeDeclareList;
	}

	/**
	 * 更新
	 * @param employeeDeclareEntity
	 */
	public void update(EmployeeDeclareEntity employeeDeclareEntity){
		getSession().update(employeeDeclareEntity);
		getSession().flush();
	}


	/**
	 * 统计当月记录数
	 * @return
	 */
	public Long countBySameMonth(String departId){
		Long sum = (Long) getSession().createQuery("select count(*) from EmployeeDeclareEntity t where t.employeeInfo.department.id=:departId and"
				+ " date_format(t.createdDate,'%Y-%m')=date_format(now(),'%Y-%m')").setString("departId", departId).uniqueResult();
		return sum;
	}
	/**
	 * 获取当月申报数据
	 * @param departID
	 * @return
	 */
	public List<EmployeeDeclareEntity> getSameMonthDeclare(String departId,Integer[] declareStatus){
		List<EmployeeDeclareEntity> employeeDeclareList = new ArrayList<EmployeeDeclareEntity>();
		//部门为空查询所有
		if(StringUtils.isNotBlank(departId)){
			employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity t where t.employeeInfo.declareStatus = 6 and t.employeeInfo.delFlg = 0 and "
					+ " t.declareStatus in (:declareStatus) and t.employeeInfo.department.id = :departId")
					.setParameterList("declareStatus", declareStatus).setString("departId", departId).list();
		}else{
			employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity t where t.employeeInfo.declareStatus = 6 and t.employeeInfo.delFlg = 0 and "
					+ " t.declareStatus in (:declareStatus) ").setParameterList("declareStatus", declareStatus).list();
		}
		return employeeDeclareList;
	}


	/**
	 * 获取当月申报数据
	 * @param departID
	 * @return
	 */
	public List<EmployeeDeclareEntity> getSameMonthDeclare(String departId,Integer declareStatus,String limitStartDate,String limitEndDate){
		List<EmployeeDeclareEntity> employeeDeclareList = new ArrayList<EmployeeDeclareEntity>();
		//部门为空查询所有
		if(StringUtils.isNotBlank(departId)){
			employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity t where date_format(t.declareDate,'%Y-%m-%d') >= "
					+ " :limitStartDate and date_format(t.declareDate,'%Y-%m-%d') <= :limitEndDate and "
					+ " t.declareStatus = :declareStatus and t.employeeInfo.department.id = :departId")
					.setInteger("declareStatus", declareStatus).setString("departId", departId)
					.setString("limitStartDate", limitStartDate).setString("limitEndDate", limitEndDate).list();
		}else{
			employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity t where date_format(t.declareDate,'%Y-%m-%d') >= "
					+ " :limitStartDate and date_format(t.declareDate,'%Y-%m-%d') <= :limitEndDate and "
					+ " t.declareStatus = :declareStatus").setInteger("declareStatus", declareStatus)
					.setString("limitStartDate", limitStartDate).setString("limitEndDate", limitEndDate).list();
		}
		return employeeDeclareList;
	}



	/**
	 * 统计指定状态员工名称不为空的记录数
	 * @param declareStatus
	 * @return
	 */
	public Long getNotEmptyEmployeeNameCount(String departId,Integer... declareStatus){
		Long sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Long) getSession().createQuery("select count(*) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.employeeInfo.name is not null and trim(t.employeeInfo.name) != '' "
					+ "and t.declareStatus in (:ds)").setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
	    }else{
	    	sum = (Long) getSession().createQuery("select count(*) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.name is not null and trim(t.employeeInfo.name) != '' "
					+ "and t.declareStatus in (:ds)").setParameterList("ds", declareStatus).uniqueResult();
	    }
		return sum;
	}

	/**
	 * 获取指定单价类型的单价平均值
	 * @return
	 */
	public Double getUnitPriceAvg(String departId,Integer unitPriceType,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select avg(t.unitPrice) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.unitPriceType = :unitPriceType "
					+ "and t.declareStatus in (:ds)").setParameterList("ds", declareStatus)
					.setInteger("unitPriceType", unitPriceType).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select avg(t.unitPrice) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.unitPriceType = :unitPriceType "
					+ "and t.declareStatus in (:ds)").setParameterList("ds", declareStatus)
					.setInteger("unitPriceType", unitPriceType).uniqueResult();
		}
		return sum;
	}

	/**
	 * 获取约定出勤日数平均值
	 * @return
	 */
	public Double getAppointmentAttendanceDayAvg(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select avg(t.appointmentAttendanceDay) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0  and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select avg(t.appointmentAttendanceDay) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 获取月间调整不为空记录数
	 * @return
	 */
	public Long getNotEmptyMonthAdjustmentCount(String departId,Integer... declareStatus){
		Long sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Long) getSession().createQuery("select count(*) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.monthAdjustment is not null "
					+ "and t.declareStatus in (:ds)").setParameterList("ds", declareStatus)
					.setString("departId", departId).uniqueResult();
		}else{
			sum = (Long) getSession().createQuery("select count(*) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.monthAdjustment is not null "
					+ "and t.declareStatus in (:ds)").setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 获取收入总和
	 * @param declareStatus
	 * @return
	 */
	public Double getIncomeSum(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select sum(t.income) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0  and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select sum(t.income) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 月间调整占收入的比例
	 * @return
	 */
	public Double getMonthIncomeProportion(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select  (sum((case when t.monthAdjustment is not null and t.monthAdjustment>0 then t.monthAdjustment else 0 end))-sum((case when t.monthAdjustment is not null and t.monthAdjustment<0 then t.monthAdjustment else 0 end)))/sum(t.income) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select  (sum((case when t.monthAdjustment is not null and t.monthAdjustment>0 then t.monthAdjustment else 0 end))-sum((case when t.monthAdjustment is not null and t.monthAdjustment<0 then t.monthAdjustment else 0 end)))/sum(t.income) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 无绩效出勤日数占有绩效、无绩效出勤日数和有薪年假比例
	 * @return
	 */
	public Double getNoPerformanceAttendanceDayProportion(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select sum(t.noPerformanceAttendanceDay)/sum(ifnull(t.noPerformanceAttendanceDay,0)+ifnull(t.performanceAttendanceDay,0)+"
					+ "ifnull(t.salaryAnnualLeave,0)) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and  t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select sum(t.noPerformanceAttendanceDay)/sum(ifnull(t.noPerformanceAttendanceDay,0)+ifnull(t.performanceAttendanceDay,0)+"
					+ "ifnull(t.salaryAnnualLeave,0)) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 获取A（标准）总和
	 * @return
	 */
	public Double getAstandardSalarySum(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select sum(t.aStandardSalary) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select sum(t.aStandardSalary) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 获取试用期折扣平均值
	 * @return
	 */
	public Double getBdiscountAvg(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select avg(t.bDiscount) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0  and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select avg(t.bDiscount) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0  and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 获取应付绩效占应付工资+应付绩效比例
	 * @return
	 */
	public Double getPayablePerformanceProportion(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select sum(t.payablePerformance)/sum(ifnull(t.payableSalary,0)+ifnull(t.payablePerformance,0)) "
					+ "from EmployeeDeclareEntity t where  t.delFlg=0  and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select sum(t.payablePerformance)/sum(ifnull(t.payableSalary,0)+ifnull(t.payablePerformance,0)) "
					+ "from EmployeeDeclareEntity t where  t.delFlg=0  and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 获取电脑补贴总和
	 * @return
	 */
	public Double getC1ComputerSubsidySum(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select sum(t.c1ComputerSubsidy) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select sum(t.c1ComputerSubsidy) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 获取加班费总和
	 * @return
	 */
	public Double getC2OvertimeSalarySum(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select sum(t.c2OvertimeSalary) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select sum(t.c2OvertimeSalary) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 获取其它补贴总和
	 * @return
	 */
	public Double getC3OtherSubsidySum(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select sum(t.c3OtherSubsidy) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select sum(t.c3OtherSubsidy) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 获取c补贴总和
	 * @return
	 */
	public Double getCsubsidySum(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select sum(t.cSubsidy) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select sum(t.cSubsidy) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 应发合计总和
	 * @return
	 */
	public Double getPayableTotalSum(String departId,Integer... declareStatus){
		Double sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Double) getSession().createQuery("select sum(t.payableTotal) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Double) getSession().createQuery("select sum(t.payableTotal) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	/**
	 * 应发合计大于0的记录数
	 * @return
	 */
	public Long getPayableTotalGtZeroSum(String departId,Integer... declareStatus){
		Long sum = null;
		if(StringUtils.isNotBlank(departId)){
			sum = (Long) getSession().createQuery("select sum(case when t.payableTotal>0 then 1 else 0 end) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.employeeInfo.department.id=:departId and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).setString("departId", departId).uniqueResult();
		}else{
			sum = (Long) getSession().createQuery("select sum(case when t.payableTotal>0 then 1 else 0 end) from EmployeeDeclareEntity t where "
					+ "t.delFlg=0 and t.declareStatus in (:ds)")
					.setParameterList("ds", declareStatus).uniqueResult();
		}
		return sum;
	}

	public List<EmployeeDeclareEntity> getAllInsertE(){
		List<EmployeeDeclareEntity> employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity").list();
		return employeeDeclareList;
	}

	/**
	 * 获取薪酬支付需要的数据
	 * @param departID
	 * @return
	 */
	public List<EmployeeDeclareEntity> findAllSalaryByDepartId(String departId){

		List<EmployeeDeclareEntity> employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity t where t.employeeInfo.department.id=:departId and"
				+ " declareStatus = 0")
				.setString("departId", departId).list();

		/*List<EmployeeDeclareEntity> employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity t where t.employeeInfo.department.id=:departId and"
				+ " declareStatus = :declareStatus and declareDate >= :startDate")
				.setString("departId", departId).setInteger("declareStatus", declareStatus).setDate("declareDate", startDate).list();	*/
		return employeeDeclareList;
	}
	/**
	 * 0507 新增 批量 by 员工申报表主键（id） 查找
	 * @param lids
	 * @author gc
	 */
	public List<EmployeeDeclareEntity> findAllByEmployeeDeclareId(List<Integer> lids) {
		List<EmployeeDeclareEntity> employeeDeclareList = new ArrayList<EmployeeDeclareEntity>();
		employeeDeclareList = getSession().createQuery("from EmployeeDeclareEntity t where t.id in (:ids) ").setParameterList("ids", lids).list();
		return employeeDeclareList;
	}

	/**
	 * 0822  新增 批量 by 员工申报表主键（id） 查找-访客专用
	 * @param lids
	 * @author gc
	 */
	public List<EmployeeDeclareCopyEntity> findAllByEmployeeDeclareCopyId(List<Integer> lids) {
		List<EmployeeDeclareCopyEntity> employeeDeclareCopyList = new ArrayList<EmployeeDeclareCopyEntity>();
		employeeDeclareCopyList = getSession().createQuery("from EmployeeDeclareCopyEntity t where t.id in (:ids) ").setParameterList("ids", lids).list();
		return employeeDeclareCopyList;
	}

	/**
	 * 通过登陆用户ID 找到对应角色名 (t_reporter可以录入本部员工)
	 * @author gc
	 * 0517
	 */
	public String findMyRole(String id) {
		// TODO Auto-generated method stub
		String sql = "select tsr.rolecode from t_s_role tsr LEFT JOIN t_s_role_user tsru on tsr.id=tsru.roleid where userid=?";
		String roleName = (String) getSession().createSQLQuery(sql).setString(0, id).uniqueResult();
		return roleName;
	}

	/**
	 * 通过 员工身份证号 和 申报日期 返回 员工收支申报实体
	 * @param employeeCode
	 * @param enterDate
	 * @return
	 */
	public List<EmployeeDeclareEntity> findByEmployeeCodeAndMoth(String employeeCode, Date enterDate) {
		// TODO Auto-generated method stub
		List<EmployeeDeclareEntity> employeeDeclareList = (List<EmployeeDeclareEntity>) getSession().createQuery("from EmployeeDeclareEntity t "
				+ "where t.employeeInfo.code = :code and t.salaryDate = :salaryDate").setString("code", employeeCode).setDate("salaryDate", enterDate).list();
		return employeeDeclareList;

	}

	/**
	 * 查找指定部门下 指定申报状态及员工属性，指定此员工的客户经理id 按照月份返回在职员工列表
	 * @param departId  部门ID
	 * @param empFlags   0外派  1总部
	 * @param month    生成月份
	 * @param id  录入者ID(客户经理)
	 * @return
	 */

	public List<EmployeeInfoEntity> findByDeclareStatusAndByInputer(String departId, Integer[] empFlags,
			String month, String id) {
		List<EmployeeInfoEntity> employeeInfoList = null;
		String username = commonRepository.findUserNameByUserId(id);
		if(StringUtils.isNotBlank(departId)){
			//删除标记 delFlag  员工属性 empFlag  部门   入职日 entryDate   在职状态 quitStatus 0未入职1在职2离职
			//离职日 quitDate   生效日 effectiveDate   无效日 expiryDate
			//and t.quitStatus =1
			//and (t.quitDate is null or date_format(t.quitDate,'%Y-%m')<=date_format(:month1,'%Y-%m'))
			employeeInfoList = getSession().createQuery("from EmployeeInfoEntity t where"
					+ " t.delFlg = 0 and t.employeeFlag in (:empFlg) and t.department = :departId"
					+ " and date_format(t.entryDate,'%Y-%m') <= date_format(:month,'%Y-%m')"
					+ " and t.inputerId=:inputId "
					+ " and (date_format(effectiveDate,'%Y-%m')<=date_format(:month1,'%Y-%m') and date_format(expiryDate,'%Y-%m')>date_format(:month2,'%Y-%m'))")
					.setParameterList("empFlg", empFlags).setString("departId", departId)
					.setString("month", month+"-01").setString("inputId", username).setString("month1", month+"-01")
					.setString("month2", month+"-01").list();
		}
		return employeeInfoList;
	}


	/**
	 * 生成员工收支申报数据
	 * @param departId 部门
	 * @param empFlags 0外派  1本部
	 * @param month 生成月份  yyyy-MM  生成月份+1>必须大于员工入职日
	 * @return
	 */
	public List<EmployeeInfoEntity> findByDeclareStatus(String departId,Integer[] empFlags,String month){
		List<EmployeeInfoEntity> employeeInfoList = null;
		if(StringUtils.isNotBlank(departId)){
			employeeInfoList = getSession().createQuery("from EmployeeInfoEntity t where"
					+ " t.delFlg = 0 and t.employeeFlag in (:empFlg) and t.department = :departId"
					+ " and t.quitStatus =1 and date_format(t.entryDate,'%Y-%m') <= date_format(:month,'%Y-%m')"
					+ " and (t.quitDate is null or date_format(t.quitDate,'%Y-%m')<=date_format(:month1,'%Y-%m'))"
					+ " and (date_format(effectiveDate,'%Y-%m')<=date_format(:month2,'%Y-%m') and date_format(expiryDate,'%Y-%m')>date_format(:month3,'%Y-%m'))")
					.setParameterList("empFlg", empFlags).setString("departId", departId).setString("month", month+"-01")
					.setString("month1", month+"-01").setString("month2", month+"-01").setString("month3", month+"-01").list();
		}else{
			employeeInfoList = getSession().createQuery("from EmployeeInfoEntity t where "
					+ "t.delFlg = 0 and t.employeeFlag in (:empFlg)"
					+ " and t.quitStatus =1 and date_format(t.entryDate,'%Y-%m') <= date_format(:month,'%Y-%m')"
					+ " and (t.quitDate is null or date_format(t.quitDate,'%Y-%m')<=date_format(:month1,'%Y-%m'))"
					+ " and (date_format(effectiveDate,'%Y-%m')<=date_format(:month2,'%Y-%m') and date_format(expiryDate,'%Y-%m')>date_format(:month3,'%Y-%m'))")
					.setParameterList("empFlg", empFlags).setString("month", month+"-01")
					.setString("month1", month+"-01").setString("month2", month+"-01").setString("month3", month+"-01").list();
		}
		return employeeInfoList;
	}
	/**
	 * 清空表EmployeeDeclareEntity
	 *  2018-07-11
	 */
	public void emptyAllEmployeeDeclare(){
		getSession().createQuery("delete EmployeeDeclareEntity").executeUpdate();
	}

	/**
	 * 获取总监 待处理消息
	 * @param orgCode
	 * @param initNum
	 * @return
	 */
	public int getMyMessageCount(String orgCode, Integer initNum) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from c_employee_declare where declare_status>? and declare_status<=? and employee_department in (select id from t_s_depart where org_code like ?) and salary_date<=?";
		Date now = new Date();
		BigInteger obj =  (BigInteger) getSession().createSQLQuery(sql).setInteger(0, initNum-3).setInteger(1, initNum).setString(2, orgCode+"%").setDate(3, now).uniqueResult();
		return obj.intValue();
	}
	/**
	 * 获取经理待处理消息
	 * @param orgCode
	 * @param initNum
	 * @param userName
	 * @return
	 */
	public int getMyMessageCount(String orgCode, Integer initNum, String userName) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from c_employee_declare where declare_status>? and declare_status<=? and inputer_id=? and employee_department in (select id from t_s_depart where org_code like ?) and salary_date<=?";
		Date now = new Date();
		BigInteger obj =  (BigInteger) getSession().createSQLQuery(sql).setInteger(0, initNum-3).setInteger(1, initNum).setString(2, userName).setString(3, orgCode+"%").setDate(4, now).uniqueResult();
		return obj.intValue();
	}
	/**
	 * 获取一层、二层待处理消息
	 * @param isChecker
	 * @return
	 */
	public int getSpecialRoleMessageCount(boolean isChecker) {
		String sql = "select count(*) from c_employee_declare where declare_status>? and declare_status<=? and salary_date<=?";
		BigInteger obj = new BigInteger("0");
		Date now = new Date();
		if(isChecker) {
			obj =  (BigInteger) getSession().createSQLQuery(sql).setInteger(0, 6).setInteger(1, 9).setDate(2, now).uniqueResult();
		}else {
			obj =  (BigInteger) getSession().createSQLQuery(sql).setInteger(0, 3).setInteger(1, 6).setDate(2, now).uniqueResult();
		}
		return obj.intValue();
	}

	/**
	 * 一键统计-人力收支全部
	 * @param date 月份
	 * @param orgCode 部门orgCode
	 * @param empFlags  内部/外派
	 * @return
	 */
	public Object oneKeyTotalGC(Date date,String orgCode,Integer[] empFlags,Integer customerId) {
		// TODO Auto-generated method stub
		Object uniqueResult = null;
		if(customerId == null||customerId==-1) {
			String sql ="select SUM(income) as '总收入',SUM(company_profit) as '总毛利',COUNT(*) as '总人数' from c_employee_declare "
					+ "where DATE_FORMAT(salary_date,'%Y-%m')=DATE_FORMAT(?,'%Y-%m') "
					+ "and employee_type in (?,?) "
					+ "and employee_department in (select id from t_s_depart where org_code like ?)";
			uniqueResult = getSession().createSQLQuery(sql).setDate(0, date).setInteger(1, empFlags[0]).setInteger(2, empFlags[1]==null?0:empFlags[1]).setString(3, orgCode+"%").uniqueResult();
		}else {
			String sql ="select SUM(income) as '总收入',SUM(company_profit) as '总毛利',COUNT(*) as '总人数' from c_employee_declare "
					+ "where DATE_FORMAT(salary_date,'%Y-%m')=DATE_FORMAT(?,'%Y-%m') "
					+ "and employee_type in (?,?) "
					+ "and employee_department in (select id from t_s_depart where org_code like ?) "
					+ "and customer_id =?";
			uniqueResult = getSession().createSQLQuery(sql).setDate(0, date).setInteger(1, empFlags[0]).setInteger(2, empFlags[1]==null?0:empFlags[1]).setString(3, orgCode+"%").setInteger(4, customerId).uniqueResult();
		}

		return uniqueResult;
	}

	/**
	 * 一键统计-人力收支全部-访客专用
	 * @param date 月份
	 * @param orgCode 部门orgCode
	 * @param empFlags  内部/外派
	 * @return
	 */
	public Object oneKeyTotalforAccess(Date date,String orgCode,Integer[] empFlags,Integer customerId) {
		// TODO Auto-generated method stub
		Object uniqueResult = null;
		if(customerId == null||customerId==-1) {
			String sql ="select SUM(income) as '总收入',SUM(company_profit) as '总毛利',COUNT(*) as '总人数' from c_employee_declare_copy "
					+ "where DATE_FORMAT(salary_date,'%Y-%m')=DATE_FORMAT(?,'%Y-%m') "
					+ "and employee_type in (?,?) "
					+ "and employee_department in (select id from t_s_depart where org_code like ?)";
			uniqueResult = getSession().createSQLQuery(sql).setDate(0, date).setInteger(1, empFlags[0]).setInteger(2, empFlags[1]==null?0:empFlags[1]).setString(3, orgCode+"%").uniqueResult();
		}else {
			String sql ="select SUM(income) as '总收入',SUM(company_profit) as '总毛利',COUNT(*) as '总人数' from c_employee_declare_copy "
					+ "where DATE_FORMAT(salary_date,'%Y-%m')=DATE_FORMAT(?,'%Y-%m') "
					+ "and employee_type in (?,?) "
					+ "and employee_department in (select id from t_s_depart where org_code like ?) "
					+ "and customer_id =?";
			uniqueResult = getSession().createSQLQuery(sql).setDate(0, date).setInteger(1, empFlags[0]).setInteger(2, empFlags[1]==null?0:empFlags[1]).setString(3, orgCode+"%").setInteger(4, customerId).uniqueResult();
		}

		return uniqueResult;
	}


}
