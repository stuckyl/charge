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
 * @Description: 六金比例表
 * @author wenst
 * @date 2018-04-21 12:18:33
 * @version V1.0
 *
 */
@Entity
@Table(name = "c_six_gold_scale", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class SixGoldScaleEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**六金地点*/
	private java.lang.String sixGoldPlace;
	/**养老保险（企业）*/
	private java.lang.Double companyEndowment;
	/**养老保险（个人）*/
	private java.lang.Double personalEndowment;
	/**养老保险最高*/
	private java.lang.Double endowmentMax;
	/**养老保险最低*/
	private java.lang.Double endowmentMin;
	/**医疗保险（企业）*/
	private java.lang.Double companyMedical;
	/**医疗保险（个人）*/
	private java.lang.Double personalMedical;
	/**医疗保险最高*/
	private java.lang.Double medicalMax;
	/**医疗保险最低*/
	private java.lang.Double medicalMin;
	/**失业保险（企业）*/
	private java.lang.Double companyUnemployment;
	/**失业保险（个人）*/
	private java.lang.Double personalUnemployment;
	/**失业保险最高*/
	private java.lang.Double unemploymentMax;
	/**失业保险最低*/
	private java.lang.Double unemploymentMin;
	/**工伤（企业）*/
	private java.lang.Double companyInjury;
	/**工伤最高*/
	private java.lang.Double injuryMax;
	/**工伤最低*/
	private java.lang.Double injuryMin;
	/**生育（企业）*/
	private java.lang.Double companyMaternity;
	/**生育最高*/
	private java.lang.Double maternityMax;
	/**生育最低*/
	private java.lang.Double maternityMin;
	/**住房公积金（企业）*/
	private java.lang.Double companyHousingFund;
	/**住房公积金（个人）*/
	private java.lang.Double personalHousingFund;
	/**住房公积金最高*/
	private java.lang.Double housingFundMax;
	/**住房公积金最低*/
	private java.lang.Double housingFundMin;
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
	@Column(name ="COMPANY_ENDOWMENT",nullable=true,precision=6,scale=3)
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
	@Column(name ="PERSONAL_ENDOWMENT",nullable=true,precision=6,scale=3)
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
	 *@return: java.lang.Double  养老保险最高
	 */
	@Column(name ="ENDOWMENT_MAX",nullable=true,precision=9,scale=2)
	public java.lang.Double getEndowmentMax(){
		return this.endowmentMax;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  养老保险最高
	 */
	public void setEndowmentMax(java.lang.Double endowmentMax){
		this.endowmentMax = endowmentMax;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  养老保险最低
	 */
	@Column(name ="ENDOWMENT_MIN",nullable=true,precision=9,scale=2)
	public java.lang.Double getEndowmentMin(){
		return this.endowmentMin;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  养老保险最低
	 */
	public void setEndowmentMin(java.lang.Double endowmentMin){
		this.endowmentMin = endowmentMin;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  医疗保险（企业）
	 */
	@Column(name ="COMPANY_MEDICAL",nullable=true,precision=6,scale=3)
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
	@Column(name ="PERSONAL_MEDICAL",nullable=true,precision=6,scale=3)
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
	 *@return: java.lang.Double  医疗保险最高
	 */
	@Column(name ="MEDICAL_MAX",nullable=true,precision=9,scale=2)
	public java.lang.Double getMedicalMax(){
		return this.medicalMax;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  医疗保险最高
	 */
	public void setMedicalMax(java.lang.Double medicalMax){
		this.medicalMax = medicalMax;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  医疗保险最低
	 */
	@Column(name ="MEDICAL_MIN",nullable=true,precision=9,scale=2)
	public java.lang.Double getMedicalMin(){
		return this.medicalMin;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  医疗保险最低
	 */
	public void setMedicalMin(java.lang.Double medicalMin){
		this.medicalMin = medicalMin;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  失业保险（企业）
	 */
	@Column(name ="COMPANY_UNEMPLOYMENT",nullable=true,precision=6,scale=3)
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
	@Column(name ="PERSONAL_UNEMPLOYMENT",nullable=true,precision=6,scale=3)
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
	 *@return: java.lang.Double  失业保险最高
	 */
	@Column(name ="UNEMPLOYMENT_MAX",nullable=true,precision=9,scale=2)
	public java.lang.Double getUnemploymentMax(){
		return this.unemploymentMax;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  失业保险最高
	 */
	public void setUnemploymentMax(java.lang.Double unemploymentMax){
		this.unemploymentMax = unemploymentMax;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  失业保险最低
	 */
	@Column(name ="UNEMPLOYMENT_MIN",nullable=true,precision=9,scale=2)
	public java.lang.Double getUnemploymentMin(){
		return this.unemploymentMin;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  失业保险最低
	 */
	public void setUnemploymentMin(java.lang.Double unemploymentMin){
		this.unemploymentMin = unemploymentMin;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  工伤（企业）
	 */
	@Column(name ="COMPANY_INJURY",nullable=true,precision=6,scale=3)
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
	 *@return: java.lang.Double  工伤最高
	 */
	@Column(name ="INJURY_MAX",nullable=true,precision=9,scale=2)
	public java.lang.Double getInjuryMax(){
		return this.injuryMax;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  工伤最高
	 */
	public void setInjuryMax(java.lang.Double injuryMax){
		this.injuryMax = injuryMax;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  工伤最低
	 */
	@Column(name ="INJURY_MIN",nullable=true,precision=9,scale=2)
	public java.lang.Double getInjuryMin(){
		return this.injuryMin;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  工伤最低
	 */
	public void setInjuryMin(java.lang.Double injuryMin){
		this.injuryMin = injuryMin;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  生育（企业）
	 */
	@Column(name ="COMPANY_MATERNITY",nullable=true,precision=6,scale=3)
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
	 *@return: java.lang.Double  生育最高
	 */
	@Column(name ="MATERNITY_MAX",nullable=true,precision=9,scale=2)
	public java.lang.Double getMaternityMax(){
		return this.maternityMax;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  生育最高
	 */
	public void setMaternityMax(java.lang.Double maternityMax){
		this.maternityMax = maternityMax;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  生育最低
	 */
	@Column(name ="MATERNITY_MIN",nullable=true,precision=9,scale=2)
	public java.lang.Double getMaternityMin(){
		return this.maternityMin;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  生育最低
	 */
	public void setMaternityMin(java.lang.Double maternityMin){
		this.maternityMin = maternityMin;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  住房公积金（企业）
	 */
	@Column(name ="COMPANY_HOUSING_FUND",nullable=true,precision=6,scale=3)
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
	@Column(name ="PERSONAL_HOUSING_FUND",nullable=true,precision=6,scale=3)
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
	 *@return: java.lang.Double  住房公积金最高
	 */
	@Column(name ="HOUSING_FUND_MAX",nullable=true,precision=9,scale=2)
	public java.lang.Double getHousingFundMax(){
		return this.housingFundMax;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  住房公积金最高
	 */
	public void setHousingFundMax(java.lang.Double housingFundMax){
		this.housingFundMax = housingFundMax;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  住房公积金最低
	 */
	@Column(name ="HOUSING_FUND_MIN",nullable=true,precision=9,scale=2)
	public java.lang.Double getHousingFundMin(){
		return this.housingFundMin;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  住房公积金最低
	 */
	public void setHousingFundMin(java.lang.Double housingFundMin){
		this.housingFundMin = housingFundMin;
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
}
