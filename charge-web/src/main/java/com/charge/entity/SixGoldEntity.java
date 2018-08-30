package com.charge.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @Title: Entity
 * @Description: 社保六金
 * @author wenst
 * @date 2018-03-20 09:31:32
 * @version V1.0
 *
 */
@Entity
@Table(name = "c_six_gold", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class SixGoldEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**员工编号*/
	private java.lang.String employeeCode;
	/**员工姓名*/
	private java.lang.String employeeName;
	/**六金地点*/
	private java.lang.String sixGoldPlace;
	/**养老保险（企业）*/
	private java.lang.Double companyEndowment;
	/**养老保险（个人）*/
	private java.lang.Double personalEndowment;
	/**医疗保险（企业）*/
	private java.lang.Double companyMedical;
	/**医疗保险（个人）*/
	private java.lang.Double personalMedical;
	/**失业保险（企业）*/
	private java.lang.Double companyUnemployment;
	/**缴纳月数 缴纳月数 默认是1*/
	private java.lang.Integer numMonth;
	/**失业保险（个人）*/
	private java.lang.Double personalUnemployment;
	/**工伤（企业）*/
	private java.lang.Double companyInjury;
	/**生育（企业）*/
	private java.lang.Double companyMaternity;
	/**住房公积金（企业）*/
	private java.lang.Double companyHousingFund;
	/**住房公积金（个人）*/
	private java.lang.Double personalHousingFund;
	/**企业合计*/
	private java.lang.Double companySum;
	/**个人合计*/
	private java.lang.Double personalSum;
	/**录入日期*/
	private java.util.Date enterDate;
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
	 *@return: java.lang.String  员工姓名
	 */
	@Column(name ="EMPLOYEE_NAME",nullable=true,length=50)
	public java.lang.String getEmployeeName() {
		return employeeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  员工姓名
	 */
	public void setEmployeeName(java.lang.String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  六金地点
	 */
	@Column(name ="SIX_GOLD_PLACE",nullable=true,length=50)
	public java.lang.String getSixGoldPlace(){
		return this.sixGoldPlace;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  六金地点
	 */
	public void setSixGoldPlace(java.lang.String sixGoldPlace){
		this.sixGoldPlace = sixGoldPlace;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  养老保险（企业）
	 */
	@Column(name ="COMPANY_ENDOWMENT",nullable=true,precision=9,scale=2)
	public java.lang.Double getCompanyEndowment(){
		return this.companyEndowment;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  养老保险（企业）
	 */
	public void setCompanyEndowment(java.lang.Double companyEndowment){
		this.companyEndowment = companyEndowment;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  养老保险（个人）
	 */
	@Column(name ="PERSONAL_ENDOWMENT",nullable=true,precision=9,scale=2)
	public java.lang.Double getPersonalEndowment(){
		return this.personalEndowment;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  养老保险（个人）
	 */
	public void setPersonalEndowment(java.lang.Double personalEndowment){
		this.personalEndowment = personalEndowment;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  医疗保险（企业）
	 */
	@Column(name ="COMPANY_MEDICAL",nullable=true,precision=9,scale=2)
	public java.lang.Double getCompanyMedical(){
		return this.companyMedical;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  医疗保险（企业）
	 */
	public void setCompanyMedical(java.lang.Double companyMedical){
		this.companyMedical = companyMedical;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  医疗保险（个人）
	 */
	@Column(name ="PERSONAL_MEDICAL",nullable=true,precision=9,scale=2)
	public java.lang.Double getPersonalMedical(){
		return this.personalMedical;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  医疗保险（个人）
	 */
	public void setPersonalMedical(java.lang.Double personalMedical){
		this.personalMedical = personalMedical;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  失业保险（企业）
	 */
	@Column(name ="COMPANY_UNEMPLOYMENT",nullable=true,precision=9,scale=2)
	public java.lang.Double getCompanyUnemployment(){
		return this.companyUnemployment;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  失业保险（企业）
	 */
	public void setCompanyUnemployment(java.lang.Double companyUnemployment){
		this.companyUnemployment = companyUnemployment;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  失业保险（个人）
	 */
	@Column(name ="PERSONAL_UNEMPLOYMENT",nullable=true,precision=9,scale=2)
	public java.lang.Double getPersonalUnemployment(){
		return this.personalUnemployment;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  失业保险（个人）
	 */
	public void setPersonalUnemployment(java.lang.Double personalUnemployment){
		this.personalUnemployment = personalUnemployment;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  工伤（企业）
	 */
	@Column(name ="COMPANY_INJURY",nullable=true,precision=9,scale=2)
	public java.lang.Double getCompanyInjury(){
		return this.companyInjury;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  工伤（企业）
	 */
	public void setCompanyInjury(java.lang.Double companyInjury){
		this.companyInjury = companyInjury;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  生育（企业）
	 */
	@Column(name ="COMPANY_MATERNITY",nullable=true,precision=9,scale=2)
	public java.lang.Double getCompanyMaternity(){
		return this.companyMaternity;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  生育（企业）
	 */
	public void setCompanyMaternity(java.lang.Double companyMaternity){
		this.companyMaternity = companyMaternity;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  住房公积金（企业）
	 */
	@Column(name ="COMPANY_HOUSING_FUND",nullable=true,precision=9,scale=2)
	public java.lang.Double getCompanyHousingFund(){
		return this.companyHousingFund;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  住房公积金（企业）
	 */
	public void setCompanyHousingFund(java.lang.Double companyHousingFund){
		this.companyHousingFund = companyHousingFund;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  住房公积金（个人）
	 */
	@Column(name ="PERSONAL_HOUSING_FUND",nullable=true,precision=9,scale=2)
	public java.lang.Double getPersonalHousingFund(){
		return this.personalHousingFund;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  住房公积金（个人）
	 */
	public void setPersonalHousingFund(java.lang.Double personalHousingFund){
		this.personalHousingFund = personalHousingFund;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  企业合计
	 */
	@Column(name ="COMPANY_SUM",nullable=true,precision=9,scale=2)
	public java.lang.Double getCompanySum(){
		return this.companySum;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  企业合计
	 */
	public void setCompanySum(java.lang.Double companySum){
		this.companySum = companySum;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  个人合计
	 */
	@Column(name ="PERSONAL_SUM",nullable=true,precision=9,scale=2)
	public java.lang.Double getPersonalSum(){
		return this.personalSum;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  个人合计
	 */
	public void setPersonalSum(java.lang.Double personalSum){
		this.personalSum = personalSum;
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer 缴纳月数 缴纳月数 默认是1
	 */
	@Column(name ="num_month",nullable=true,precision=10,scale=0)
	public java.lang.Integer getNumMonth(){
		return this.numMonth;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  缴纳月数 缴纳月数 默认是1
	 */
	public void setNumMonth(java.lang.Integer numMonth){
		this.numMonth = numMonth;
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  生成日期
	 */
	@Column(name ="enter_date",nullable=true)
	public java.util.Date getEnterDate(){
		return this.enterDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  生成日期
	 */
	public void setEnterDate(java.util.Date enterDate){
		this.enterDate = enterDate;
	}

}
