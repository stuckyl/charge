package com.charge.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.charge.entity.EmployeeDeclareCopyEntity;
import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.ProjectCopyEntity;
import com.charge.entity.ProjectEntity;
import com.charge.repository.EmployeeDeclareRepository;
import com.charge.repository.ProjectRepository;

/**
 * @author wenst
 * @version v1.0
 * @Title: EmployeeDeclareService
 * @Description: 收支数据历史备份
 * @date 2018年8月10日
 */
@Service
@Transactional
public class EmployeeDeclareCopyService {

	@Autowired
    private CommonService commonService;
	@Autowired
	private EmployeeDeclareRepository employeeDeclareRepo;
	@Autowired
	private ProjectRepository projectRepo;

	/**
     * j08011: 选中数据批量还原
     * 员工收支信息
     */
	public Map<String, Object> employeeRestore(List<Integer> lids) {

		Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        List<EmployeeDeclareEntity> employeeDeclareList = employeeDeclareRepo.findAllByEmployeeDeclareId(lids);
        if (!employeeDeclareList.isEmpty()) {
        	for (int i = 0; i < employeeDeclareList.size(); i++) {
        		employeeRestore(employeeDeclareList.get(i));
        	}
        }else {
            result.put("errMsg", "无选中数据还原");
            result.put("errCode", -1);
        }
        return result;
	}

	/**
     * j08011: 选中数据批量还原
     * 项目收支信息
     */
	public Map<String, Object> projectsRestore(List<Integer> lids) {

		Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        List<ProjectEntity> projectList = projectRepo.findByDeclareStatusId(lids);
        if (!projectList.isEmpty()) {
        	for (int i = 0; i < projectList.size(); i++) {
        		projectRestore(projectList.get(i));
        	}
        }else {
            result.put("errMsg", "无选中数据还原");
            result.put("errCode", -1);
        }
        return result;
	}

	public void employeeBackUp(EmployeeDeclareEntity employee){
		EmployeeDeclareCopyEntity employeeBack;
		employeeBack = commonService.findUniqueByProperty(EmployeeDeclareCopyEntity.class,"batId",employee.getId());
		if(!StringUtil.isNotEmpty(employeeBack))
			employeeBack =new EmployeeDeclareCopyEntity();
		employeeBack.setBatId(employee.getId());
		employeeBack.setSalaryDate(employee.getSalaryDate());
		employeeBack.setEmployeeId(employee.getEmployeeInfo().getId());
		employeeBack.setEmployeeBasepay(employee.getEmployeeBasePay());
		employeeBack.setEmployeeAsalary(employee.getEmployeeASalary());
		employeeBack.setEmployeeDepartment(employee.getEmployeeDepartment());
		employeeBack.setEmployeeType(employee.getEmployeeType());
		employeeBack.setCustomerId(employee.getCustomerInfo());
		employeeBack.setCustomerName(employee.getCustomerName());
		employeeBack.setCorporateId(employee.getCorporateId());
		employeeBack.setUnitPriceType(employee.getUnitPriceType());
		employeeBack.setUnitPrice(employee.getUnitPrice());
		employeeBack.setIsturnovertax(employee.getIsTurnoverTax());
		employeeBack.setAcceptedAttendanceDay(employee.getAcceptedAttendanceDay());
		employeeBack.setAppointedAttendanceDay(employee.getAppointedAttendanceDay());
		employeeBack.setMonthOther(employee.getMonthOther());
		employeeBack.setAcceptanceAdd(employee.getAcceptanceAdd());
		employeeBack.setMonthAdjustment(employee.getMonthAdjustment());
		employeeBack.setBDiscount(employee.getBDiscount());
		employeeBack.setPerformanceAttendanceDay(employee.getPerformanceAttendanceDay());
		employeeBack.setNoPerformanceAttendanceDay(employee.getNoPerformanceAttendanceDay());
		employeeBack.setCComputerSubsidy(employee.getCComputerSubsidy());
		employeeBack.setCOvertimeSalary(employee.getCOvertimeSalary());
		employeeBack.setDAnnualBonus(employee.getDAnnualBonus());
		employeeBack.setC1OtherSubsidy(employee.getC1OtherSubsidy());
		employeeBack.setC1OtherSubsidyRemark(employee.getC1OtherSubsidyRemark());
		employeeBack.setC2OtherSubsidy(employee.getC2OtherSubsidy());
		employeeBack.setC2OtherSubsidyRemark(employee.getC2OtherSubsidyRemark());
		employeeBack.setC3OtherSubsidy(employee.getC3OtherSubsidy());
		employeeBack.setC3OtherSubsidyRemark(employee.getC3OtherSubsidyRemark());
		employeeBack.setSixCompanyBurdenOne(employee.getSixCompanyBurdenOne());
		employeeBack.setSixPersonalBurdenOne(employee.getSixPersonalBurdenOne());
		employeeBack.setPerToneTaxOne(employee.getPerToneTaxOne());
		employeeBack.setPerToneTaxTwo(employee.getPerToneTaxTwo());
		employeeBack.setIncome(employee.getIncome());
		employeeBack.setNetIncome(employee.getNetIncome());
		employeeBack.setPayableSalary(employee.getPayableSalary());
		employeeBack.setPayablePerformance(employee.getPayablePerformance());
		employeeBack.setEmployeeRealSalary(employee.getEmployeeRealSalary());
		employeeBack.setCompanyCost(employee.getCompanyCost());
		employeeBack.setCompanyProfit(employee.getCompanyProfit());
		employeeBack.setCompanyProfitRate(employee.getCompanyProfitRate());
		employeeBack.setDeclareStatus(employee.getDeclareStatus());
		employeeBack.setPoCode(employee.getPoCode());
		employeeBack.setLegalAttendanceDay(employee.getLegalAttendanceDay());
		employeeBack.setInputerId(employee.getInputerId());
		if(StringUtil.isNotEmpty(employeeBack.getId())){
			commonService.saveOrUpdate(employeeBack);
		}else{
			commonService.save(employeeBack);
		}
		/*employee.setBaceUpFlag(1);*/
		commonService.saveOrUpdate(employee);
	}
	public void employeeRestore(EmployeeDeclareEntity employee){
		EmployeeDeclareCopyEntity employeeBack=new EmployeeDeclareCopyEntity();
		employeeBack = commonService.findUniqueByProperty(EmployeeDeclareCopyEntity.class,"batId",employee.getId());
		employee.setSalaryDate(employeeBack.getSalaryDate());
		EmployeeInfoEntity employeeInfo = commonService.getEntity(EmployeeInfoEntity.class, employeeBack.getEmployeeId());
		employee.setEmployeeInfo(employeeInfo);
		employee.setEmployeeBasePay(employeeBack.getEmployeeBasepay());
		employee.setEmployeeASalary(employeeBack.getEmployeeAsalary());
		employee.setEmployeeDepartment(employeeBack.getEmployeeDepartment());
		employee.setEmployeeType(employeeBack.getEmployeeType());
		employee.setCustomerInfo(employeeBack.getCustomerId());
		employee.setCustomerName(employeeBack.getCustomerName());
		employee.setCorporateId(employeeBack.getCorporateId());
		employee.setUnitPriceType(employeeBack.getUnitPriceType());
		employee.setUnitPrice(employeeBack.getUnitPrice());
		employee.setIsTurnoverTax(employeeBack.getIsturnovertax());
		employee.setAcceptedAttendanceDay(employeeBack.getAcceptedAttendanceDay());
		employee.setAppointedAttendanceDay(employeeBack.getAppointedAttendanceDay());
		employee.setMonthOther(employeeBack.getMonthOther());
		employee.setAcceptanceAdd(employeeBack.getAcceptanceAdd());
		employee.setMonthAdjustment(employeeBack.getMonthAdjustment());
		employee.setBDiscount(employeeBack.getBDiscount());
		employee.setPerformanceAttendanceDay(employeeBack.getPerformanceAttendanceDay());
		employee.setNoPerformanceAttendanceDay(employeeBack.getNoPerformanceAttendanceDay());
		employee.setCComputerSubsidy(employeeBack.getCComputerSubsidy());
		employee.setCOvertimeSalary(employeeBack.getCOvertimeSalary());
		employee.setDAnnualBonus(employeeBack.getDAnnualBonus());
		employee.setC1OtherSubsidy(employeeBack.getC1OtherSubsidy());
		employee.setC1OtherSubsidyRemark(employeeBack.getC1OtherSubsidyRemark());
		employee.setC2OtherSubsidy(employeeBack.getC2OtherSubsidy());
		employee.setC2OtherSubsidyRemark(employeeBack.getC2OtherSubsidyRemark());
		employee.setC3OtherSubsidy(employeeBack.getC3OtherSubsidy());
		employee.setC3OtherSubsidyRemark(employeeBack.getC3OtherSubsidyRemark());
		employee.setSixCompanyBurdenOne(employeeBack.getSixCompanyBurdenOne());
		employee.setSixPersonalBurdenOne(employeeBack.getSixPersonalBurdenOne());
		employee.setPerToneTaxOne(employeeBack.getPerToneTaxOne());
		employee.setPerToneTaxTwo(employeeBack.getPerToneTaxTwo());
		employee.setIncome(employeeBack.getIncome());
		employee.setNetIncome(employeeBack.getNetIncome());
		employee.setPayableSalary(employeeBack.getPayableSalary());
		employee.setPayablePerformance(employeeBack.getPayablePerformance());
		employee.setEmployeeRealSalary(employeeBack.getEmployeeRealSalary());
		employee.setCompanyCost(employeeBack.getCompanyCost());
		employee.setCompanyProfit(employeeBack.getCompanyProfit());
		employee.setCompanyProfitRate(employeeBack.getCompanyProfitRate());
		employee.setDeclareStatus(employeeBack.getDeclareStatus());
		employee.setPoCode(employeeBack.getPoCode());
		employee.setLegalAttendanceDay(employeeBack.getLegalAttendanceDay());
		commonService.saveOrUpdate(employee);
	}
	public void projectBackUp(ProjectEntity project){
		ProjectCopyEntity projectBackUp;
		projectBackUp =commonService.findUniqueByProperty(ProjectCopyEntity.class, "batId", project.getId());
		if(!StringUtil.isNotEmpty(projectBackUp))
			projectBackUp = new ProjectCopyEntity();
		projectBackUp.setBatId(project.getId());
		projectBackUp.setProjectMonth(project.getProjectMonth());
		projectBackUp.setProjectDate(project.getProjectDate());
		projectBackUp.setProjectDepartment(project.getProjectDepartment());
		projectBackUp.setProjectCustomer1(project.getProjectCustomer1());
		projectBackUp.setProjectCustomer2(project.getProjectCustomer2());
		projectBackUp.setProjectConstant(project.getProjectConstant());
		projectBackUp.setProjectIncome(project.getProjectIncome());
		projectBackUp.setProjectPay(project.getProjectPay());
		projectBackUp.setProjectProfit(project.getProjectProfit());
		projectBackUp.setProjectProfitRate(project.getProjectProfitRate());
		projectBackUp.setProjectStatus(project.getProjectStatus());
		projectBackUp.setEditors(project.getEditors());
		projectBackUp.setC1Other(project.getC1Other());
		projectBackUp.setC1OtherRemarks(project.getC1OtherRemarks());
		projectBackUp.setC1OtherSupplier(project.getC1OtherSupplier());
		projectBackUp.setC1OtherMoney(project.getC1OtherMoney());
		projectBackUp.setC2Other(project.getC2Other());
		projectBackUp.setC2OtherRemarks(project.getC2OtherRemarks());
		projectBackUp.setC2OtherSupplier(project.getC2OtherSupplier());
		projectBackUp.setC2OtherMoney(project.getC2OtherMoney());
		projectBackUp.setC3Other(project.getC3Other());
		projectBackUp.setC3OtherRemarks(project.getC3OtherRemarks());
		projectBackUp.setC3OtherSupplier(project.getC3OtherSupplier());
		projectBackUp.setC3OtherMoney(project.getC3OtherMoney());
		projectBackUp.setC4Other(project.getC4Other());
		projectBackUp.setC4OtherRemarks(project.getC4OtherRemarks());
		projectBackUp.setC4OtherSupplier(project.getC4OtherSupplier());
		projectBackUp.setC4OtherMoney(project.getC4OtherMoney());
		projectBackUp.setC5Other(project.getC5Other());
		projectBackUp.setC5OtherRemarks(project.getC5OtherRemarks());
		projectBackUp.setC5OtherSupplier(project.getC5OtherSupplier());
		projectBackUp.setC5OtherMoney(project.getC5OtherMoney());
		projectBackUp.setIsturnovertax(project.getIsturnovertax());
		projectBackUp.setInputerId(project.getInputerId());

		if(StringUtil.isNotEmpty(projectBackUp.getId())){
			commonService.saveOrUpdate(projectBackUp);
		}else{
			commonService.save(projectBackUp);
		}
	}
	public void projectRestore(ProjectEntity project) {
		ProjectCopyEntity projectBackUp = new ProjectCopyEntity();
		projectBackUp =commonService.findUniqueByProperty(ProjectCopyEntity.class, "batId",project.getId());
		project.setProjectMonth(projectBackUp.getProjectMonth());
		project.setProjectDate(projectBackUp.getProjectDate());
		project.setProjectDepartment(projectBackUp.getProjectDepartment());
		project.setProjectCustomer1(projectBackUp.getProjectCustomer1());
		project.setProjectCustomer2(projectBackUp.getProjectCustomer2());
		project.setProjectConstant(projectBackUp.getProjectConstant());
		project.setProjectIncome(projectBackUp.getProjectIncome());
		project.setProjectPay(projectBackUp.getProjectPay());
		project.setProjectProfit(projectBackUp.getProjectProfit());
		project.setProjectProfitRate(projectBackUp.getProjectProfitRate());
		project.setProjectStatus(projectBackUp.getProjectStatus());
		project.setEditors(projectBackUp.getEditors());
		project.setC1Other(projectBackUp.getC1Other());
		project.setC1OtherRemarks(projectBackUp.getC1OtherRemarks());
		project.setC1OtherSupplier(projectBackUp.getC1OtherSupplier());
		project.setC1OtherMoney(projectBackUp.getC1OtherMoney());
		project.setC2Other(projectBackUp.getC2Other());
		project.setC2OtherRemarks(projectBackUp.getC2OtherRemarks());
		project.setC2OtherSupplier(projectBackUp.getC2OtherSupplier());
		project.setC2OtherMoney(projectBackUp.getC2OtherMoney());
		project.setC3Other(projectBackUp.getC3Other());
		project.setC3OtherRemarks(projectBackUp.getC3OtherRemarks());
		project.setC3OtherSupplier(projectBackUp.getC3OtherSupplier());
		project.setC3OtherMoney(projectBackUp.getC3OtherMoney());
		project.setC4Other(projectBackUp.getC4Other());
		project.setC4OtherRemarks(projectBackUp.getC4OtherRemarks());
		project.setC4OtherSupplier(projectBackUp.getC4OtherSupplier());
		project.setC4OtherMoney(projectBackUp.getC4OtherMoney());
		project.setC5Other(projectBackUp.getC5Other());
		project.setC5OtherRemarks(projectBackUp.getC5OtherRemarks());
		project.setC5OtherSupplier(projectBackUp.getC5OtherSupplier());
		project.setC5OtherMoney(projectBackUp.getC5OtherMoney());
		project.setProjectReturnreason("");
		commonService.saveOrUpdate(project);
	}

}
