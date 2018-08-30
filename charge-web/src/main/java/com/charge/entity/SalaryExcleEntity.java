package com.charge.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

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
public class SalaryExcleEntity implements java.io.Serializable {
	/**id*/
	@Excel(name = "ID")
	private java.lang.Integer id;
	/**薪酬年月 - 201802、201803、201804*/
	@Excel(name = "薪酬年月")
	private java.util.Date salaryDate;
	/**员工编号 - 身份证号码、【员工表.员工编号】*/
	@Excel(name = "员工编号")
	private java.lang.String staffNo;
	/**B折扣率 注意：这里是【折扣率】，不是【试用期折扣率】*/
	@Excel(name = "B折扣率")
	private java.lang.Integer btDiscountRate;
	/**C1电脑补贴 单位 分*/
	@Excel(name = "C1电脑补贴")
	private java.lang.Integer c1ComputerSubsidy;
	/**C2加班费 单位 分*/
	@Excel(name = "C2加班费")
	private java.lang.Integer c2OvertimePay;
	/**C3其他补贴 单位 分*/
	@Excel(name = "C3其他补贴")
	private java.lang.Integer c3OtherSubsidy;
	/**特别扣减 单位 分*/
	@Excel(name = "特别扣减")
	private java.lang.Integer specialDeduction;
	/**C补贴 -  计算公式 C=C1+C2+C3-特别扣减 单位 分*/
	@Excel(name = "C补贴")
	private java.lang.Integer cnumSubsidy;
	/**D年终奖 单位 分*/
	@Excel(name = "D年终奖")
	private java.lang.Integer dnumYearEndBonus;
	/**双发地点1 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝*/
	@Excel(name = "双发地点1")
	private java.lang.String placeOne;
	/**工资1*/
	@Excel(name = "工资1")
	private java.lang.Integer salaryOne;
	/**六金（个人负担）1*/
	@Excel(name = "六金（个人负担）1")
	private java.lang.Integer sixPersonalBurdenOne;
	/**年终奖1*/
	@Excel(name = "年终奖1")
	private java.lang.Integer yearEndBonusOne;
	/**个调税1*/
	@Excel(name = "个调税1")
	private java.lang.Integer perToneTaxOne;
	/**年个税1*/
	@Excel(name = "年个税1")
	private java.lang.Integer yearTaxPersonalOne;
	/**打卡金额1*/
	@Excel(name = "打卡金额1")
	private java.lang.Integer transferSalaryOne;
	/**六金（公司负担）1*/
	@Excel(name = "六金（公司负担）1")
	private java.lang.Integer sixCompanyBurdenOne;
	/**公司成本1*/
	@Excel(name = "公司成本1")
	private java.lang.Integer companyCostOne;
	/**双发地点2- 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝*/
	@Excel(name = "双发地点2")
	private java.lang.String placeTwo;
	/**工资2*/
	@Excel(name = "工资2")
	private java.lang.Integer salaryTwo;
	/**六金（个人负担）2*/
	@Excel(name = "六金（个人负担）2")
	private java.lang.Integer sixPersonalBurdenTwo;
	/**年终奖2 - 如果【员工表】里的双发地点2不是空，年终奖1=0，且年终奖2=D年终奖；否则，年终奖2=0，且年终奖1=D年终奖*/
	@Excel(name = "年终奖2")
	private java.lang.Integer yearEndBonusTwo;
	/**个调税2*/
	@Excel(name = "个调税2")
	private java.lang.Integer perToneTaxTwo;
	/**年个税2*/
	@Excel(name = "年个税2")
	private java.lang.Integer yearTaxPersonalTwo;
	/**打卡金额2*/
	@Excel(name = "打卡金额2")
	private java.lang.Integer transferSalaryTwo;
	/**六金（公司负担）2*/
	@Excel(name = "六金（公司负担）2")
	private java.lang.Integer sixCompanyBurdenTwo;
	/**公司成本2*/
	@Excel(name = "公司成本2")
	private java.lang.Integer companyCostTwo;
	
	
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  员工编号 - 身份证号码、【员工表.员工编号】
	 */
	@Column(name ="STAFF_NO",nullable=false,length=20)
	public java.lang.String getStaffNo(){
		return this.staffNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  员工编号 - 身份证号码、【员工表.员工编号】
	 */
	public void setStaffNo(java.lang.String staffNo){
		this.staffNo = staffNo;
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  C1电脑补贴 单位 分
	 */
	@Column(name ="C1_COMPUTER_SUBSIDY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getC1ComputerSubsidy(){
		return this.c1ComputerSubsidy;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  C1电脑补贴 单位 分
	 */
	public void setC1ComputerSubsidy(java.lang.Integer c1ComputerSubsidy){
		this.c1ComputerSubsidy = c1ComputerSubsidy;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  C2加班费 单位 分
	 */
	@Column(name ="C2_OVERTIME_PAY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getC2OvertimePay(){
		return this.c2OvertimePay;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  C2加班费 单位 分
	 */
	public void setC2OvertimePay(java.lang.Integer c2OvertimePay){
		this.c2OvertimePay = c2OvertimePay;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  C3其他补贴 单位 分
	 */
	@Column(name ="C3_OTHER_SUBSIDY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getC3OtherSubsidy(){
		return this.c3OtherSubsidy;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  C3其他补贴 单位 分
	 */
	public void setC3OtherSubsidy(java.lang.Integer c3OtherSubsidy){
		this.c3OtherSubsidy = c3OtherSubsidy;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  特别扣减 单位 分
	 */
	@Column(name ="SPECIAL_DEDUCTION",nullable=false,precision=10,scale=0)
	public java.lang.Integer getSpecialDeduction(){
		return this.specialDeduction;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  特别扣减 单位 分
	 */
	public void setSpecialDeduction(java.lang.Integer specialDeduction){
		this.specialDeduction = specialDeduction;
	}

	@Column(name ="CNUM_SUBSIDY",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCnumSubsidy() {
		return cnumSubsidy;
	}

	public void setCnumSubsidy(java.lang.Integer cnumSubsidy) {
		this.cnumSubsidy = cnumSubsidy;
	}

	@Column(name ="DNUM_YEAR_END_BONUS",nullable=false,precision=10,scale=0)
	public java.lang.Integer getDnumYearEndBonus() {
		return dnumYearEndBonus;
	}

	public void setDnumYearEndBonus(java.lang.Integer dnumYearEndBonus) {
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  工资1
	 */
	@Column(name ="SALARY_ONE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getSalaryOne(){
		return this.salaryOne;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  工资1
	 */
	public void setSalaryOne(java.lang.Integer salaryOne){
		this.salaryOne = salaryOne;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  六金（个人负担）1
	 */
	@Column(name ="SIX_PERSONAL_BURDEN_ONE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getSixPersonalBurdenOne(){
		return this.sixPersonalBurdenOne;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  六金（个人负担）1
	 */
	public void setSixPersonalBurdenOne(java.lang.Integer sixPersonalBurdenOne){
		this.sixPersonalBurdenOne = sixPersonalBurdenOne;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  年终奖1
	 */
	@Column(name ="YEAR_END_BONUS_ONE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getYearEndBonusOne(){
		return this.yearEndBonusOne;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  年终奖1
	 */
	public void setYearEndBonusOne(java.lang.Integer yearEndBonusOne){
		this.yearEndBonusOne = yearEndBonusOne;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  个调税1
	 */
	@Column(name ="PER_TONE_TAX_ONE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getPerToneTaxOne(){
		return this.perToneTaxOne;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  个调税1
	 */
	public void setPerToneTaxOne(java.lang.Integer perToneTaxOne){
		this.perToneTaxOne = perToneTaxOne;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  年个税1
	 */
	@Column(name ="YEAR_TAX_PERSONAL_ONE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getYearTaxPersonalOne(){
		return this.yearTaxPersonalOne;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  年个税1
	 */
	public void setYearTaxPersonalOne(java.lang.Integer yearTaxPersonalOne){
		this.yearTaxPersonalOne = yearTaxPersonalOne;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  打卡金额1
	 */
	@Column(name ="TRANSFER_SALARY_ONE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getTransferSalaryOne(){
		return this.transferSalaryOne;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  打卡金额1
	 */
	public void setTransferSalaryOne(java.lang.Integer transferSalaryOne){
		this.transferSalaryOne = transferSalaryOne;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  六金（公司负担）1
	 */
	@Column(name ="SIX_COMPANY_BURDEN_ONE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getSixCompanyBurdenOne(){
		return this.sixCompanyBurdenOne;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  六金（公司负担）1
	 */
	public void setSixCompanyBurdenOne(java.lang.Integer sixCompanyBurdenOne){
		this.sixCompanyBurdenOne = sixCompanyBurdenOne;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  公司成本1
	 */
	@Column(name ="COMPANY_COST_ONE",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCompanyCostOne(){
		return this.companyCostOne;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  公司成本1
	 */
	public void setCompanyCostOne(java.lang.Integer companyCostOne){
		this.companyCostOne = companyCostOne;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  双发地点1 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
	 */
	@Column(name ="PLACE_TWO",nullable=false,length=8)
	public java.lang.String getPlaceTwo(){
		return this.placeTwo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  双发地点1 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
	 */
	public void setPlaceTwo(java.lang.String placeTwo){
		this.placeTwo = placeTwo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  工资1
	 */
	@Column(name ="SALARY_TWO",nullable=false,precision=10,scale=0)
	public java.lang.Integer getSalaryTwo(){
		return this.salaryTwo;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  工资1
	 */
	public void setSalaryTwo(java.lang.Integer salaryTwo){
		this.salaryTwo = salaryTwo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  六金（个人负担）
	 */
	@Column(name ="SIX_PERSONAL_BURDEN_TWO",nullable=false,precision=10,scale=0)
	public java.lang.Integer getSixPersonalBurdenTwo(){
		return this.sixPersonalBurdenTwo;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  六金（个人负担）
	 */
	public void setSixPersonalBurdenTwo(java.lang.Integer sixPersonalBurdenTwo){
		this.sixPersonalBurdenTwo = sixPersonalBurdenTwo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  年终奖1 - 如果【员工表】里的双发地点2不是空，年终奖1=0，且年终奖2=D年终奖；否则，年终奖2=0，且年终奖1=D年终奖
	 */
	@Column(name ="YEAR_END_BONUS_TWO",nullable=false,precision=10,scale=0)
	public java.lang.Integer getYearEndBonusTwo(){
		return this.yearEndBonusTwo;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  年终奖1 - 如果【员工表】里的双发地点2不是空，年终奖1=0，且年终奖2=D年终奖；否则，年终奖2=0，且年终奖1=D年终奖
	 */
	public void setYearEndBonusTwo(java.lang.Integer yearEndBonusTwo){
		this.yearEndBonusTwo = yearEndBonusTwo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  个调税2
	 */
	@Column(name ="PER_TONE_TAX_TWO",nullable=false,precision=10,scale=0)
	public java.lang.Integer getPerToneTaxTwo(){
		return this.perToneTaxTwo;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  个调税2
	 */
	public void setPerToneTaxTwo(java.lang.Integer perToneTaxTwo){
		this.perToneTaxTwo = perToneTaxTwo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  年个税1
	 */
	@Column(name ="YEAR_TAX_PERSONAL_TWO",nullable=false,precision=10,scale=0)
	public java.lang.Integer getYearTaxPersonalTwo(){
		return this.yearTaxPersonalTwo;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  年个税1
	 */
	public void setYearTaxPersonalTwo(java.lang.Integer yearTaxPersonalTwo){
		this.yearTaxPersonalTwo = yearTaxPersonalTwo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  打卡金额1
	 */
	@Column(name ="TRANSFER_SALARY_TWO",nullable=false,precision=10,scale=0)
	public java.lang.Integer getTransferSalaryTwo(){
		return this.transferSalaryTwo;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  打卡金额1
	 */
	public void setTransferSalaryTwo(java.lang.Integer transferSalaryTwo){
		this.transferSalaryTwo = transferSalaryTwo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  六金（公司负担）2
	 */
	@Column(name ="SIX_COMPANY_BURDEN_TWO",nullable=false,precision=10,scale=0)
	public java.lang.Integer getSixCompanyBurdenTwo(){
		return this.sixCompanyBurdenTwo;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  六金（公司负担）2
	 */
	public void setSixCompanyBurdenTwo(java.lang.Integer sixCompanyBurdenTwo){
		this.sixCompanyBurdenTwo = sixCompanyBurdenTwo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  公司成本1
	 */
	@Column(name ="COMPANY_COST_TWO",nullable=false,precision=10,scale=0)
	public java.lang.Integer getCompanyCostTwo(){
		return this.companyCostTwo;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  公司成本1
	 */
	public void setCompanyCostTwo(java.lang.Integer companyCostTwo){
		this.companyCostTwo = companyCostTwo;
	}
}
