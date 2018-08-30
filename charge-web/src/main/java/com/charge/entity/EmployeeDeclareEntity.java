 package com.charge.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;

/**
 * @Title: Entity
 * @Description: 员工申报实体类
 * @author zhangdaihao
 * @date 2018-05-22 13:23:28
 * @version V1.0
 *
 */
@Entity
@Table(name = "c_employee_declare", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class EmployeeDeclareEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**薪酬年月 - 201802、201803、201804*/
	private java.util.Date salaryDate;
	/**员工主键ID*/
	private EmployeeInfoEntity employeeInfo;
	/**员工基本工资*/
	private Double employeeBasePay;
	/**员工A标准工资*/
	private Double employeeASalary;
	/**员工所属部门*/
	private String employeeDepartment;
	/**员工类别*/
	private Integer employeeType;
	/**客户表主键ID*/
	private Integer customerInfo;
	/**客户名称*/
	private java.lang.String customerName;
	/**法人Id*/
	private java.lang.Integer corporateId;
	/**单价方式*/
	private java.lang.Integer unitPriceType;
	/**单价*/
	private java.lang.Double unitPrice;
	/**是否含有流转税 0 不含  1 含有*/
	private java.lang.Integer isTurnoverTax;
	/**验收出勤日*/
	private Double acceptedAttendanceDay;
	/**预定出勤日*/
	private Double appointedAttendanceDay;
	/**当月加算*/
	private java.lang.Double monthOther;
	/**验收加算*/
	private java.lang.Double acceptanceAdd;
	/**月间调整*/
	private java.lang.Double monthAdjustment;
	/**B折扣*/
	private java.lang.Integer bDiscount;
	/**有绩效出勤日*/
	private java.lang.Double performanceAttendanceDay;
	/**无绩效出勤日*/
	private java.lang.Double noPerformanceAttendanceDay;
	/**C电脑补贴*/
	private java.lang.Double cComputerSubsidy;
	/**C加班费*/
	private java.lang.Double cOvertimeSalary;
	/**D年终奖*/
	private java.lang.Double dAnnualBonus;
	/**C1其他补贴*/
	private java.lang.Double c1OtherSubsidy;
	/**C1补贴备注*/
	private java.lang.String c1OtherSubsidyRemark;
	/**C2其他补贴*/
	private java.lang.Double c2OtherSubsidy;
	/**C2补贴备注*/
	private java.lang.String c2OtherSubsidyRemark;
	/**C3其他补贴*/
	private java.lang.Double c3OtherSubsidy;
	/**C3补贴备注*/
	private java.lang.String c3OtherSubsidyRemark;
	/**六金（公司负担）1*/
	private java.lang.Double sixCompanyBurdenOne;
	/**六金（个人负担）1*/
	private java.lang.Double sixPersonalBurdenOne;
	/**个调税1*/
	private java.lang.Double perToneTaxOne;
	/**个调税2*/
	private java.lang.Double perToneTaxTwo;
	/**收入 公式：月单价*验收出勤日/约定出勤日+日单价*验收出勤日+小时单价*8*验收出勤日*/
	private java.lang.Double income;
	/**净收入 收入-流转税 */
	private java.lang.Double netIncome;
	/**应付工资*/
	private java.lang.Double payableSalary;
	/**应付绩效*/
	private java.lang.Double payablePerformance;
	/**员工到手*/
	private java.lang.Double employeeRealSalary;
	/**公司成本*/
	private java.lang.Double companyCost;
	/**公司毛利*/
	private java.lang.Double companyProfit;
	/**公司毛利率*/
	private java.lang.Double companyProfitRate;
	/**申报状态*/
	private java.lang.Integer declareStatus;
	/**申报拒绝理由*/
	private java.lang.String declareReturnreason;
	/**删除标记*/
	private java.lang.Integer delFlg;
	/**创建人*/
	private java.lang.String createdBy;
	/**创建日期*/
	private java.util.Date createdDate;
	/**最后修改人*/
	private java.lang.String lastModifiedBy;
	/**最后修改时间*/
	private java.util.Date lastModifiedDate;
	/**录入者id*/
	private java.lang.String inputerId;
	/**申报者id*/
	private java.lang.String reporterId;
	/**审查者id*/
	private java.lang.String checkerId;
	/**审批者id*/
	private java.lang.String controllerId;

	/**订单号  PO编号*/
	private java.lang.String poCode;
	/**法定出勤日*/
	private java.lang.Double legalAttendanceDay;
	/**备份标记*/
	private java.lang.Integer baceUpFlag;
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  薪酬年月 - 201802、201803、201804
	 */
	@Column(name ="SALARY_DATE",nullable=true)
	public java.util.Date getSalaryDate(){
		return this.salaryDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  薪酬年月 - 201802、201803、201804
	 */
	public void setSalaryDate(java.util.Date salaryDate){
		this.salaryDate = salaryDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  员工主键ID
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="employee_id")
	public EmployeeInfoEntity getEmployeeInfo(){
		return this.employeeInfo;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  员工主键ID
	 */
	public void setEmployeeInfo(EmployeeInfoEntity employeeInfo){
		this.employeeInfo = employeeInfo;
	}



//	@ManyToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name="customer_id")
	@Column(name="customer_id")
	public Integer getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(Integer customerInfo) {
		this.customerInfo = customerInfo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客户名称
	 */
	@Column(name ="CUSTOMER_NAME",nullable=true,length=30)
	public java.lang.String getCustomerName(){
		return this.customerName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客户名称
	 */
	public void setCustomerName(java.lang.String customerName){
		this.customerName = customerName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  单价方式
	 */
	@Column(name ="UNIT_PRICE_TYPE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getUnitPriceType(){
		return this.unitPriceType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  单价方式
	 */
	public void setUnitPriceType(java.lang.Integer unitPriceType){
		this.unitPriceType = unitPriceType;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  单价
	 */
	@Column(name ="UNIT_PRICE",nullable=true,precision=13,scale=2)
	public java.lang.Double getUnitPrice(){
		return this.unitPrice;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  单价
	 */
	public void setUnitPrice(java.lang.Double unitPrice){
		this.unitPrice = unitPrice;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  当月加算
	 */
	@Column(name ="MONTH_OTHER",nullable=true,precision=13,scale=2)
	public java.lang.Double getMonthOther(){
		return this.monthOther;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  当月加算
	 */
	public void setMonthOther(java.lang.Double monthOther){
		this.monthOther = monthOther;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  验收加算
	 */
	@Column(name ="ACCEPTANCE_ADD",nullable=true,precision=13,scale=2)
	public java.lang.Double getAcceptanceAdd(){
		return this.acceptanceAdd;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  验收加算
	 */
	public void setAcceptanceAdd(java.lang.Double acceptanceAdd){
		this.acceptanceAdd = acceptanceAdd;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  月间调整
	 */
	@Column(name ="MONTH_ADJUSTMENT",nullable=true,precision=13,scale=2)
	public java.lang.Double getMonthAdjustment(){
		return this.monthAdjustment;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  月间调整
	 */
	public void setMonthAdjustment(java.lang.Double monthAdjustment){
		this.monthAdjustment = monthAdjustment;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  B折扣
	 */
	@Column(name ="B_DISCOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getBDiscount(){
		return this.bDiscount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  B折扣
	 */
	public void setBDiscount(java.lang.Integer bDiscount){
		this.bDiscount = bDiscount;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  有绩效出勤日
	 */
	@Column(name ="PERFORMANCE_ATTENDANCE_DAY",nullable=true)
	public java.lang.Double getPerformanceAttendanceDay(){
		return this.performanceAttendanceDay;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  有绩效出勤日
	 */
	public void setPerformanceAttendanceDay(java.lang.Double performanceAttendanceDay){
		this.performanceAttendanceDay = performanceAttendanceDay;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  无绩效出勤日
	 */
	@Column(name ="NO_PERFORMANCE_ATTENDANCE_DAY",nullable=true)
	public java.lang.Double getNoPerformanceAttendanceDay(){
		return this.noPerformanceAttendanceDay;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  无绩效出勤日
	 */
	public void setNoPerformanceAttendanceDay(java.lang.Double noPerformanceAttendanceDay){
		this.noPerformanceAttendanceDay = noPerformanceAttendanceDay;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  C电脑补贴
	 */
	@Column(name ="C_COMPUTER_SUBSIDY",nullable=true,precision=13,scale=2)
	public java.lang.Double getCComputerSubsidy(){
		return this.cComputerSubsidy;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  C电脑补贴
	 */
	public void setCComputerSubsidy(java.lang.Double cComputerSubsidy){
		this.cComputerSubsidy = cComputerSubsidy;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  C加班费
	 */
	@Column(name ="C_OVERTIME_SALARY",nullable=true,precision=13,scale=2)
	public java.lang.Double getCOvertimeSalary(){
		return this.cOvertimeSalary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  C加班费
	 */
	public void setCOvertimeSalary(java.lang.Double cOvertimeSalary){
		this.cOvertimeSalary = cOvertimeSalary;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  D年终奖
	 */
	@Column(name ="D_ANNUAL_BONUS",nullable=true,precision=13,scale=2)
	public java.lang.Double getDAnnualBonus(){
		return this.dAnnualBonus;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  D年终奖
	 */
	public void setDAnnualBonus(java.lang.Double dAnnualBonus){
		this.dAnnualBonus = dAnnualBonus;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  C1其他补贴
	 */
	@Column(name ="C1_OTHER_SUBSIDY",nullable=true,precision=13,scale=2)
	public java.lang.Double getC1OtherSubsidy(){
		return this.c1OtherSubsidy;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  C1其他补贴
	 */
	public void setC1OtherSubsidy(java.lang.Double c1OtherSubsidy){
		this.c1OtherSubsidy = c1OtherSubsidy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  C1补贴备注
	 */
	@Column(name ="C1_OTHER_SUBSIDY_REMARK",nullable=true,length=255)
	public java.lang.String getC1OtherSubsidyRemark(){
		return this.c1OtherSubsidyRemark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  C1补贴备注
	 */
	public void setC1OtherSubsidyRemark(java.lang.String c1OtherSubsidyRemark){
		this.c1OtherSubsidyRemark = c1OtherSubsidyRemark;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  C2其他补贴
	 */
	@Column(name ="C2_OTHER_SUBSIDY",nullable=true,precision=13,scale=2)
	public java.lang.Double getC2OtherSubsidy(){
		return this.c2OtherSubsidy;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  C2其他补贴
	 */
	public void setC2OtherSubsidy(java.lang.Double c2OtherSubsidy){
		this.c2OtherSubsidy = c2OtherSubsidy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  C2补贴备注
	 */
	@Column(name ="C2_OTHER_SUBSIDY_REMARK",nullable=true,length=255)
	public java.lang.String getC2OtherSubsidyRemark(){
		return this.c2OtherSubsidyRemark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  C2补贴备注
	 */
	public void setC2OtherSubsidyRemark(java.lang.String c2OtherSubsidyRemark){
		this.c2OtherSubsidyRemark = c2OtherSubsidyRemark;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  C3其他补贴
	 */
	@Column(name ="C3_OTHER_SUBSIDY",nullable=true,precision=13,scale=2)
	public java.lang.Double getC3OtherSubsidy(){
		return this.c3OtherSubsidy;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  C3其他补贴
	 */
	public void setC3OtherSubsidy(java.lang.Double c3OtherSubsidy){
		this.c3OtherSubsidy = c3OtherSubsidy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  C3补贴备注
	 */
	@Column(name ="C3_OTHER_SUBSIDY_REMARK",nullable=true,length=255)
	public java.lang.String getC3OtherSubsidyRemark(){
		return this.c3OtherSubsidyRemark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  C3补贴备注
	 */
	public void setC3OtherSubsidyRemark(java.lang.String c3OtherSubsidyRemark){
		this.c3OtherSubsidyRemark = c3OtherSubsidyRemark;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  六金（公司负担）1
	 */
	@Column(name ="SIX_COMPANY_BURDEN_ONE",nullable=true,precision=13,scale=2)
	public java.lang.Double getSixCompanyBurdenOne(){
		return this.sixCompanyBurdenOne;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  六金（公司负担）1
	 */
	public void setSixCompanyBurdenOne(java.lang.Double sixCompanyBurdenOne){
		this.sixCompanyBurdenOne = sixCompanyBurdenOne;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  六金（个人负担）1
	 */
	@Column(name ="SIX_PERSONAL_BURDEN_ONE",nullable=true,precision=13,scale=2)
	public java.lang.Double getSixPersonalBurdenOne(){
		return this.sixPersonalBurdenOne;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  六金（个人负担）1
	 */
	public void setSixPersonalBurdenOne(java.lang.Double sixPersonalBurdenOne){
		this.sixPersonalBurdenOne = sixPersonalBurdenOne;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  个调税1
	 */
	@Column(name ="PER_TONE_TAX_ONE",nullable=true,precision=13,scale=2)
	public java.lang.Double getPerToneTaxOne(){
		return this.perToneTaxOne;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  个调税1
	 */
	public void setPerToneTaxOne(java.lang.Double perToneTaxOne){
		this.perToneTaxOne = perToneTaxOne;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  收入 公式：月单价*验收出勤日/约定出勤日+日单价*验收出勤日+小时单价*8*验收出勤日
	 */
	@Column(name ="INCOME",nullable=true,precision=13,scale=2)
	public java.lang.Double getIncome(){
		return this.income;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  收入 公式：月单价*验收出勤日/约定出勤日+日单价*验收出勤日+小时单价*8*验收出勤日
	 */
	public void setIncome(java.lang.Double income){
		this.income = income;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  应付工资
	 */
	@Column(name ="PAYABLE_SALARY",nullable=true,precision=13,scale=2)
	public java.lang.Double getPayableSalary(){
		return this.payableSalary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  应付工资
	 */
	public void setPayableSalary(java.lang.Double payableSalary){
		this.payableSalary = payableSalary;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  员工到手
	 */
	@Column(name ="EMPLOYEE_REAL_SALARY",nullable=true,precision=13,scale=2)
	public java.lang.Double getEmployeeRealSalary(){
		return this.employeeRealSalary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  员工到手
	 */
	public void setEmployeeRealSalary(java.lang.Double employeeRealSalary){
		this.employeeRealSalary = employeeRealSalary;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  公司成本
	 */
	@Column(name ="COMPANY_COST",nullable=true,precision=13,scale=2)
	public java.lang.Double getCompanyCost(){
		return this.companyCost;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  公司成本
	 */
	public void setCompanyCost(java.lang.Double companyCost){
		this.companyCost = companyCost;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  公司毛利
	 */
	@Column(name ="COMPANY_PROFIT",nullable=true,precision=13,scale=2)
	public java.lang.Double getCompanyProfit(){
		return this.companyProfit;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  公司毛利
	 */
	public void setCompanyProfit(java.lang.Double companyProfit){
		this.companyProfit = companyProfit;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  公司毛利率
	 */
	@Column(name ="COMPANY_PROFIT_RATE",nullable=true,precision=13,scale=2)
	public java.lang.Double getCompanyProfitRate(){
		return this.companyProfitRate;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  公司毛利率
	 */
	public void setCompanyProfitRate(java.lang.Double companyProfitRate){
		this.companyProfitRate = companyProfitRate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  申报状态
	 */
	@Column(name ="DECLARE_STATUS",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDeclareStatus(){
		return this.declareStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  申报状态
	 */
	public void setDeclareStatus(java.lang.Integer declareStatus){
		this.declareStatus = declareStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  申报拒绝理由
	 */
	@Column(name ="DECLARE_RETURNREASON",nullable=true,length=255)
	public java.lang.String getDeclareReturnreason(){
		return this.declareReturnreason;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  申报拒绝理由
	 */
	public void setDeclareReturnreason(java.lang.String declareReturnreason){
		this.declareReturnreason = declareReturnreason;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  删除标记
	 */
	@Column(name ="DEL_FLG",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDelFlg(){
		return this.delFlg;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  删除标记
	 */
	public void setDelFlg(java.lang.Integer delFlg){
		this.delFlg = delFlg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人
	 */
	@Column(name ="CREATED_BY",nullable=true,length=50)
	public java.lang.String getCreatedBy(){
		return this.createdBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人
	 */
	public void setCreatedBy(java.lang.String createdBy){
		this.createdBy = createdBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATED_DATE",nullable=true)
	public java.util.Date getCreatedDate(){
		return this.createdDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreatedDate(java.util.Date createdDate){
		this.createdDate = createdDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后修改人
	 */
	@Column(name ="LAST_MODIFIED_BY",nullable=true,length=50)
	public java.lang.String getLastModifiedBy(){
		return this.lastModifiedBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后修改人
	 */
	public void setLastModifiedBy(java.lang.String lastModifiedBy){
		this.lastModifiedBy = lastModifiedBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后修改时间
	 */
	@Column(name ="LAST_MODIFIED_DATE",nullable=true)
	public java.util.Date getLastModifiedDate(){
		return this.lastModifiedDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后修改时间
	 */
	public void setLastModifiedDate(java.util.Date lastModifiedDate){
		this.lastModifiedDate = lastModifiedDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  录入者id
	 */
	@Column(name ="INPUTER_ID",nullable=true,length=32)
	public java.lang.String getInputerId(){
		return this.inputerId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  录入者id
	 */
	public void setInputerId(java.lang.String inputerId){
		this.inputerId = inputerId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  申报者id
	 */
	@Column(name ="REPORTER_ID",nullable=true,length=32)
	public java.lang.String getReporterId(){
		return this.reporterId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  申报者id
	 */
	public void setReporterId(java.lang.String reporterId){
		this.reporterId = reporterId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  审查者id
	 */
	@Column(name ="CHECKER_ID",nullable=true,length=32)
	public java.lang.String getCheckerId(){
		return this.checkerId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审查者id
	 */
	public void setCheckerId(java.lang.String checkerId){
		this.checkerId = checkerId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  审批者id
	 */
	@Column(name ="CONTROLLER_ID",nullable=true,length=32)
	public java.lang.String getControllerId(){
		return this.controllerId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审批者id
	 */
	public void setControllerId(java.lang.String controllerId){
		this.controllerId = controllerId;
	}

	@Column(name ="PO_CODE",nullable=true,length=50)
	public java.lang.String getPoCode(){
		return this.poCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  订单号  PO编号
	 */
	public void setPoCode(java.lang.String poCode){
		this.poCode = poCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  法定出勤日
	 */
	@Column(name ="LEGAL_ATTENDANCE_DAY",nullable=true)
	public java.lang.Double getLegalAttendanceDay(){
		return this.legalAttendanceDay;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  法定出勤日
	 */
	public void setLegalAttendanceDay(java.lang.Double legalAttendanceDay){
		this.legalAttendanceDay = legalAttendanceDay;
	}

	@Column(name ="appointed_attendance_day",nullable=true)
	public Double getAppointedAttendanceDay() {
		return appointedAttendanceDay;
	}

	public void setAppointedAttendanceDay(Double appointedAttendanceDay) {
		this.appointedAttendanceDay = appointedAttendanceDay;
	}
	@Column(name ="accepted_attendance_day",nullable=true)
	public Double getAcceptedAttendanceDay() {
		return acceptedAttendanceDay;
	}

	public void setAcceptedAttendanceDay(Double acceptedAttendanceDay) {
		this.acceptedAttendanceDay = acceptedAttendanceDay;
	}


	@Column(name ="per_tone_tax_two",nullable=true)
	public java.lang.Double getPerToneTaxTwo() {
		return perToneTaxTwo;
	}

	public void setPerToneTaxTwo(java.lang.Double perToneTaxTwo) {
		this.perToneTaxTwo = perToneTaxTwo;
	}

	@Column(name ="payable_performance",nullable=true)
	public java.lang.Double getPayablePerformance() {
		return payablePerformance;
	}

	public void setPayablePerformance(java.lang.Double payablePerformance) {
		this.payablePerformance = payablePerformance;
	}

	@Column(name="employee_aSalary")
	public Double getEmployeeASalary() {
		return employeeASalary;
	}

	public void setEmployeeASalary(Double employeeASalary) {
		this.employeeASalary = employeeASalary;
	}
	@Column(name="employee_basePay")
	public Double getEmployeeBasePay() {
		return employeeBasePay;
	}

	public void setEmployeeBasePay(Double employeeBasePay) {
		this.employeeBasePay = employeeBasePay;
	}
	@Column(name="employee_type")
	public Integer getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(Integer employeeType) {
		this.employeeType = employeeType;
	}
	@Column(name="employee_department")
	public String getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}
	@Column(name="net_income")
	public java.lang.Double getNetIncome() {
		return netIncome;
	}

	public void setNetIncome(java.lang.Double netIncome) {
		this.netIncome = netIncome;
	}
	@Column(name="isturnovertax")
	public java.lang.Integer getIsTurnoverTax() {
		return isTurnoverTax;
	}

	public void setIsTurnoverTax(java.lang.Integer isTurnoverTax) {
		this.isTurnoverTax = isTurnoverTax;
	}
	@Column(name="corporate_id")
	public java.lang.Integer getCorporateId() {
		return corporateId;
	}

	public void setCorporateId(java.lang.Integer corporateId) {
		this.corporateId = corporateId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  备份标记
	 */
	@Column(name ="backup_flag")
	public java.lang.Integer getBaceUpFlag(){
		return this.baceUpFlag;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  备份标记
	 */
	public void setBaceUpFlag(java.lang.Integer baceUpFlag){
		this.baceUpFlag = baceUpFlag;
	}

}
