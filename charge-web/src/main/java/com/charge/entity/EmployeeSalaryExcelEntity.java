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
 * @author wenst
 * @date 2018-03-20 11:09:38
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_employee_salary", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class EmployeeSalaryExcelEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	/**id*/
	@Excel(name = "ID")
	private java.lang.Integer id;
	/**薪酬年月*/
	@Excel(name = "薪酬年月")
	private java.util.Date paymentDate;
	/**员工编号*/
	@Excel(name = "员工编号")
	private java.lang.String employeeCode;
	/**薪资发放地点*/
	@Excel(name = "地点")
	private java.lang.String paymentPlace;
	/**工资*/
	@Excel(name = "工资")
	private java.lang.Double salary;
	/**六金（个人负担）*/
	@Excel(name = "六金（个人负担）")
	private java.lang.Double personalSixGold;
	/**个调税*/
	@Excel(name = "个调税")
	private java.lang.Double presonalTax;
	/**年终奖*/
	@Excel(name = "年终奖")
	private java.lang.Double annualBonus;
	/**年个税*/
	@Excel(name = "年个税")
	private java.lang.Double annualBonusTax;
	/**打卡金额*/
	@Excel(name = "打卡金额")
	private java.lang.Double enterAmountMoney;
	/**六金（公司负担）*/
	@Excel(name = "六金（公司负担）")
	private java.lang.Double companySixGold;
	/**公司成本*/
	@Excel(name = "公司成本")
	private java.lang.Double companyCost;
	
	
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
	 *@return: java.util.Date  薪酬年月
	 */
	@Column(name ="PAYMENT_DATE",nullable=true)
	public java.util.Date getPaymentDate(){
		return this.paymentDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  薪酬年月
	 */
	public void setPaymentDate(java.util.Date paymentDate){
		this.paymentDate = paymentDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  员工编号
	 */
	@Column(name ="EMPLOYEE_CODE",nullable=true,length=50)
	public java.lang.String getEmployeeCode(){
		return this.employeeCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  员工编号
	 */
	public void setEmployeeCode(java.lang.String employeeCode){
		this.employeeCode = employeeCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  薪资发放地点
	 */
	@Column(name ="PAYMENT_PLACE",nullable=true,length=30)
	public java.lang.String getPaymentPlace(){
		return this.paymentPlace;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  薪资发放地点
	 */
	public void setPaymentPlace(java.lang.String paymentPlace){
		this.paymentPlace = paymentPlace;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  工资
	 */
	@Column(name ="SALARY",nullable=true,precision=9,scale=2)
	public java.lang.Double getSalary(){
		return this.salary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  工资
	 */
	public void setSalary(java.lang.Double salary){
		this.salary = salary;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  六金（个人负担）
	 */
	@Column(name ="PERSONAL_SIX_GOLD",nullable=true,precision=9,scale=2)
	public java.lang.Double getPersonalSixGold(){
		return this.personalSixGold;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  六金（个人负担）
	 */
	public void setPersonalSixGold(java.lang.Double personalSixGold){
		this.personalSixGold = personalSixGold;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  个调税
	 */
	@Column(name ="PRESONAL_TAX",nullable=true,precision=9,scale=2)
	public java.lang.Double getPresonalTax(){
		return this.presonalTax;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  个调税
	 */
	public void setPresonalTax(java.lang.Double presonalTax){
		this.presonalTax = presonalTax;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  年终奖
	 */
	@Column(name ="ANNUAL_BONUS",nullable=true,precision=9,scale=2)
	public java.lang.Double getAnnualBonus(){
		return this.annualBonus;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  年终奖
	 */
	public void setAnnualBonus(java.lang.Double annualBonus){
		this.annualBonus = annualBonus;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  年个税
	 */
	@Column(name ="ANNUAL_BONUS_TAX",nullable=true,precision=9,scale=2)
	public java.lang.Double getAnnualBonusTax(){
		return this.annualBonusTax;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  年个税
	 */
	public void setAnnualBonusTax(java.lang.Double annualBonusTax){
		this.annualBonusTax = annualBonusTax;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  打卡金额
	 */
	@Column(name ="ENTER_AMOUNT_MONEY",nullable=true,precision=9,scale=2)
	public java.lang.Double getEnterAmountMoney(){
		return this.enterAmountMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  打卡金额
	 */
	public void setEnterAmountMoney(java.lang.Double enterAmountMoney){
		this.enterAmountMoney = enterAmountMoney;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  六金（公司负担）
	 */
	@Column(name ="COMPANY_SIX_GOLD",nullable=true,precision=9,scale=2)
	public java.lang.Double getCompanySixGold(){
		return this.companySixGold;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  六金（公司负担）
	 */
	public void setCompanySixGold(java.lang.Double companySixGold){
		this.companySixGold = companySixGold;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  公司成本
	 */
	@Column(name ="COMPANY_COST",nullable=true,precision=9,scale=2)
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
	
}
