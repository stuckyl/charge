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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: 员工薪酬表
 * @author zhangdaihao
 * @date 2018-03-29 13:45:40
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_salary", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class SalaryOneEntity implements java.io.Serializable {
	/**id*/
	@Excel(name = "ID")
	private java.lang.Integer id;
	/**薪酬年月 - 201802、201803、201804*/
	@Excel(name = "薪酬年月")
	private java.util.Date salaryDate;
	/**员工信息*/
	private EmployeeInfoEntity employeeInfo;
	/**B折扣率 注意：这里是【折扣率】，不是【试用期折扣率】*/
	private java.lang.Integer btDiscountRate;
	/**C1电脑补贴 单位 分*/
	private java.lang.Double c1ComputerSubsidy;
	/**C2加班费 单位 分*/
	private java.lang.Double c2OvertimePay;
	/**C3其他补贴 单位 分*/
	private java.lang.Double c3OtherSubsidy;
	/**特别扣减 单位 分*/
	private java.lang.Double specialDeduction;
	/**C补贴 -  计算公式 C=C1+C2+C3-特别扣减 单位 分*/
	private java.lang.Double cnumSubsidy;
	/**D年终奖 单位 分*/
	private java.lang.Double dnumYearEndBonus;
	/**双发地点1 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝*/
	private java.lang.String placeOne;
	/**工资1*/
	private java.lang.Double salaryOne;
	/**六金（个人负担）1*/
	private java.lang.Double sixPersonalBurdenOne;
	/**年终奖1*/
	private java.lang.Double yearEndBonusOne;
	/**个调税1*/
	private java.lang.Double perToneTaxOne;
	/**年个税1*/
	private java.lang.Double yearTaxPersonalOne;
	/**打卡金额1*/
	private java.lang.Double transferSalaryOne;
	/**六金（公司负担）1*/
	private java.lang.Double sixCompanyBurdenOne;
	/**公司成本1*/
	private java.lang.Double companyCostOne;
	/**双发地点2- 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝*/
	private java.lang.String placeTwo;
	/**工资2*/
	@Excel(name = "工资")
	private java.lang.Double salaryTwo;
	/**六金（个人负担）2*/
	@Excel(name = "六金（个人负担）")
	private java.lang.Double sixPersonalBurdenTwo;
	/**年终奖2 - 如果【员工表】里的双发地点2不是空，年终奖1=0，且年终奖2=D年终奖；否则，年终奖2=0，且年终奖1=D年终奖*/
	@Excel(name = "年终奖")
	private java.lang.Double yearEndBonusTwo;
	/**个调税2*/
	@Excel(name = "个调税")
	private java.lang.Double perToneTaxTwo;
	/**年个税2*/
	@Excel(name = "年个税")
	private java.lang.Double yearTaxPersonalTwo;
	/**打卡金额2*/
	@Excel(name = "打卡金额")
	private java.lang.Double transferSalaryTwo;
	/**六金（公司负担）2*/
	@Excel(name = "六金（公司负担）")
	private java.lang.Double sixCompanyBurdenTwo;
	/**公司成本2*/
	@Excel(name = "公司成本")
	private java.lang.Double companyCostTwo;
	/**状态*/
	private java.lang.Integer state;
	/**登录/更新日时(时间戳*/
	private java.util.Date updateDate;
	/**登录/更新者*/
	private java.lang.String updateBy;
	/**删除Flg 1是删除*/
	private java.lang.Integer deleteFlg;
	
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
	@Column(name ="SALARY_DATE",nullable=false)
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
	 * 员工信息
	 * @return
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="EMPLOYEE_ID")
	public EmployeeInfoEntity getEmployeeInfo() {
		return employeeInfo;
	}
	public void setEmployeeInfo(EmployeeInfoEntity employeeInfo) {
		this.employeeInfo = employeeInfo;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  B折扣率 注意：这里是【折扣率】，不是【试用期折扣率】
	 */
	@Column(name ="BT_DISCOUNT_RATE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getBtDiscountRate(){
		return this.btDiscountRate;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  B折扣率 注意：这里是【折扣率】，不是【试用期折扣率】
	 */
	public void setBtDiscountRate(java.lang.Integer btDiscountRate){
		this.btDiscountRate = btDiscountRate;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  C1电脑补贴 单位 分
	 */
	@Column(name ="C1_COMPUTER_SUBSIDY",nullable=false,precision=10,scale=2)
	public java.lang.Double getC1ComputerSubsidy(){
		return this.c1ComputerSubsidy;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  C1电脑补贴 单位 分
	 */
	public void setC1ComputerSubsidy(java.lang.Double c1ComputerSubsidy){
		this.c1ComputerSubsidy = c1ComputerSubsidy;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  C2加班费 单位 分
	 */
	@Column(name ="C2_OVERTIME_PAY",nullable=false,precision=10,scale=2)
	public java.lang.Double getC2OvertimePay(){
		return this.c2OvertimePay;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  C2加班费 单位 分
	 */
	public void setC2OvertimePay(java.lang.Double c2OvertimePay){
		this.c2OvertimePay = c2OvertimePay;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  C3其他补贴 单位 分
	 */
	@Column(name ="C3_OTHER_SUBSIDY",nullable=false,precision=10,scale=2)
	public java.lang.Double getC3OtherSubsidy(){
		return this.c3OtherSubsidy;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  C3其他补贴 单位 分
	 */
	public void setC3OtherSubsidy(java.lang.Double c3OtherSubsidy){
		this.c3OtherSubsidy = c3OtherSubsidy;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  特别扣减 单位 分
	 */
	@Column(name ="SPECIAL_DEDUCTION",nullable=false,precision=10,scale=2)
	public java.lang.Double getSpecialDeduction(){
		return this.specialDeduction;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  特别扣减 单位 分
	 */
	public void setSpecialDeduction(java.lang.Double specialDeduction){
		this.specialDeduction = specialDeduction;
	}

	@Column(name ="CNUM_SUBSIDY",nullable=false,precision=10,scale=2)
	public java.lang.Double getCnumSubsidy() {
		return cnumSubsidy;
	}

	public void setCnumSubsidy(java.lang.Double cnumSubsidy) {
		this.cnumSubsidy = cnumSubsidy;
	}

	@Column(name ="DNUM_YEAR_END_BONUS",nullable=false,precision=10,scale=2)
	public java.lang.Double getDnumYearEndBonus() {
		return dnumYearEndBonus;
	}

	public void setDnumYearEndBonus(java.lang.Double dnumYearEndBonus) {
		this.dnumYearEndBonus = dnumYearEndBonus;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  双发地点1 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
	 */
	@Column(name ="PLACE_ONE",nullable=false,length=8)
	public java.lang.String getPlaceOne(){
		return this.placeOne;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  双发地点1 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
	 */
	public void setPlaceOne(java.lang.String placeOne){
		this.placeOne = placeOne;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  工资1
	 */
	@Column(name ="SALARY_ONE",nullable=false,precision=10,scale=2)
	public java.lang.Double getSalaryOne(){
		return this.salaryOne;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  工资1
	 */
	public void setSalaryOne(java.lang.Double salaryOne){
		this.salaryOne = salaryOne;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  六金（个人负担）1
	 */
	@Column(name ="SIX_PERSONAL_BURDEN_ONE",nullable=false,precision=10,scale=2)
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
	 *@return: java.lang.Double  年终奖1
	 */
	@Column(name ="YEAR_END_BONUS_ONE",nullable=false,precision=10,scale=2)
	public java.lang.Double getYearEndBonusOne(){
		return this.yearEndBonusOne;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  年终奖1
	 */
	public void setYearEndBonusOne(java.lang.Double yearEndBonusOne){
		this.yearEndBonusOne = yearEndBonusOne;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  个调税1
	 */
	@Column(name ="PER_TONE_TAX_ONE",nullable=false,precision=10,scale=2)
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
	 *@return: java.lang.Double  年个税1
	 */
	@Column(name ="YEAR_TAX_PERSONAL_ONE",nullable=false,precision=10,scale=2)
	public java.lang.Double getYearTaxPersonalOne(){
		return this.yearTaxPersonalOne;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  年个税1
	 */
	public void setYearTaxPersonalOne(java.lang.Double yearTaxPersonalOne){
		this.yearTaxPersonalOne = yearTaxPersonalOne;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  打卡金额1
	 */
	@Column(name ="TRANSFER_SALARY_ONE",nullable=false,precision=10,scale=2)
	public java.lang.Double getTransferSalaryOne(){
		return this.transferSalaryOne;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  打卡金额1
	 */
	public void setTransferSalaryOne(java.lang.Double transferSalaryOne){
		this.transferSalaryOne = transferSalaryOne;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  六金（公司负担）1
	 */
	@Column(name ="SIX_COMPANY_BURDEN_ONE",nullable=false,precision=10,scale=2)
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
	 *@return: java.lang.Double  公司成本1
	 */
	@Column(name ="COMPANY_COST_ONE",nullable=false,precision=10,scale=2)
	public java.lang.Double getCompanyCostOne(){
		return this.companyCostOne;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  公司成本1
	 */
	public void setCompanyCostOne(java.lang.Double companyCostOne){
		this.companyCostOne = companyCostOne;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  双发地点2 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
	 */
	@Column(name ="PLACE_TWO",nullable=false,length=8)
	public java.lang.String getPlaceTwo(){
		return this.placeTwo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  双发地点2 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
	 */
	public void setPlaceTwo(java.lang.String placeTwo){
		this.placeTwo = placeTwo;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  工资2
	 */
	@Column(name ="SALARY_TWO",nullable=false,precision=10,scale=2)
	public java.lang.Double getSalaryTwo(){
		return this.salaryTwo;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  工资2
	 */
	public void setSalaryTwo(java.lang.Double salaryTwo){
		this.salaryTwo = salaryTwo;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  六金（个人负担）2
	 */
	@Column(name ="SIX_PERSONAL_BURDEN_TWO",nullable=false,precision=10,scale=2)
	public java.lang.Double getSixPersonalBurdenTwo(){
		return this.sixPersonalBurdenTwo;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  六金（个人负担）2
	 */
	public void setSixPersonalBurdenTwo(java.lang.Double sixPersonalBurdenTwo){
		this.sixPersonalBurdenTwo = sixPersonalBurdenTwo;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  年终奖2 - 如果【员工表】里的双发地点2不是空，年终奖1=0，且年终奖2=D年终奖；否则，年终奖2=0，且年终奖1=D年终奖
	 */
	@Column(name ="YEAR_END_BONUS_TWO",nullable=false,precision=10,scale=2)
	public java.lang.Double getYearEndBonusTwo(){
		return this.yearEndBonusTwo;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  年终奖2 - 如果【员工表】里的双发地点2不是空，年终奖1=0，且年终奖2=D年终奖；否则，年终奖2=0，且年终奖1=D年终奖
	 */
	public void setYearEndBonusTwo(java.lang.Double yearEndBonusTwo){
		this.yearEndBonusTwo = yearEndBonusTwo;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  个调税2
	 */
	@Column(name ="PER_TONE_TAX_TWO",nullable=false,precision=10,scale=2)
	public java.lang.Double getPerToneTaxTwo(){
		return this.perToneTaxTwo;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  个调税2
	 */
	public void setPerToneTaxTwo(java.lang.Double perToneTaxTwo){
		this.perToneTaxTwo = perToneTaxTwo;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  年个税2
	 */
	@Column(name ="YEAR_TAX_PERSONAL_TWO",nullable=false,precision=10,scale=2)
	public java.lang.Double getYearTaxPersonalTwo(){
		return this.yearTaxPersonalTwo;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  年个税2
	 */
	public void setYearTaxPersonalTwo(java.lang.Double yearTaxPersonalTwo){
		this.yearTaxPersonalTwo = yearTaxPersonalTwo;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  打卡金额2
	 */
	@Column(name ="TRANSFER_SALARY_TWO",nullable=false,precision=10,scale=2)
	public java.lang.Double getTransferSalaryTwo(){
		return this.transferSalaryTwo;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  打卡金额2
	 */
	public void setTransferSalaryTwo(java.lang.Double transferSalaryTwo){
		this.transferSalaryTwo = transferSalaryTwo;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  六金（公司负担）2
	 */
	@Column(name ="SIX_COMPANY_BURDEN_TWO",nullable=false,precision=10,scale=2)
	public java.lang.Double getSixCompanyBurdenTwo(){
		return this.sixCompanyBurdenTwo;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  六金（公司负担）2
	 */
	public void setSixCompanyBurdenTwo(java.lang.Double sixCompanyBurdenTwo){
		this.sixCompanyBurdenTwo = sixCompanyBurdenTwo;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  公司成本2
	 */
	@Column(name ="COMPANY_COST_TWO",nullable=false,precision=10,scale=2)
	public java.lang.Double getCompanyCostTwo(){
		return this.companyCostTwo;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  公司成本2
	 */
	public void setCompanyCostTwo(java.lang.Double companyCostTwo){
		this.companyCostTwo = companyCostTwo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getState() {
		return state;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setState(java.lang.Integer state) {
		this.state = state;
	}

	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  登录/更新日时(时间戳
	 */
	@Column(name ="UPDATE_DATE",nullable=false)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  登录/更新日时(时间戳
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  登录/更新者
	 */
	@Column(name ="UPDATE_BY",nullable=false,length=20)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  登录/更新者
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  删除Flg 1是删除
	 */
	@Column(name ="DELETE_FLG",nullable=false,precision=10,scale=0)
	public java.lang.Integer getDeleteFlg(){
		return this.deleteFlg;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  删除Flg 1是删除
	 */
	public void setDeleteFlg(java.lang.Integer deleteFlg){
		this.deleteFlg = deleteFlg;
	}
}
