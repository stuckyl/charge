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
import javax.persistence.SequenceGenerator;

/**
 * @Title: Entity
 * @Description: 供应商实体类
 * @author zhangdaihao
 * @date 2018-08-10 09:40:26
 * @version V1.0
 *
 */
@Entity
@Table(name = "c_suppliers", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class SuppliersEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**供应商简称*/
	private java.lang.String code;
	/**供应商全称*/
	private java.lang.String name;
	/**签约法人*/
	private java.lang.Integer signCorporate;
	/**企业类型 0-国企 1-外企 2-民企*/
	private java.lang.Integer corporateType;
	/**冻结标记  1- 冻结   0-正常*/
	private java.lang.Integer activeFlg;
	/**删除标记 1 - 删除  0 - 正常*/
	private java.lang.Integer delFlg;
	/**创建人*/
	private java.lang.String createdBy;
	/**创建日期*/
	private java.util.Date createdDate;
	/**最后修改人*/
	private java.lang.String lastModifiedBy;
	/**最后修改时间*/
	private java.util.Date lastModifiedDate;
	/**开户行*/
	private java.lang.String openBank;
	/**银行账号*/
	private String bankAccount;
	/**所在省份*/
	private java.lang.String province;
	/**所在市*/
	private java.lang.String city;

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
	 *@return: java.lang.String  供应商简称
	 */
	@Column(name ="CODE",nullable=true,length=50)
	public java.lang.String getCode(){
		return this.code;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  供应商简称
	 */
	public void setCode(java.lang.String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  供应商全称
	 */
	@Column(name ="NAME",nullable=true,length=50)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  供应商全称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  签约法人
	 */
	@Column(name ="SIGN_CORPORATE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getSignCorporate(){
		return this.signCorporate;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  签约法人
	 */
	public void setSignCorporate(java.lang.Integer signCorporate){
		this.signCorporate = signCorporate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  企业类型 0-国企 1-外企 2-民企
	 */
	@Column(name ="CORPORATE_TYPE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getCorporateType(){
		return this.corporateType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  企业类型 0-国企 1-外企 2-民企
	 */
	public void setCorporateType(java.lang.Integer corporateType){
		this.corporateType = corporateType;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  冻结标记  1- 冻结   0-正常
	 */
	@Column(name ="ACTIVE_FLG",nullable=true,precision=10,scale=0)
	public java.lang.Integer getActiveFlg(){
		return this.activeFlg;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  冻结标记  1- 冻结   0-正常
	 */
	public void setActiveFlg(java.lang.Integer activeFlg){
		this.activeFlg = activeFlg;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  删除标记 1 - 删除  0 - 正常
	 */
	@Column(name ="DEL_FLG",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDelFlg(){
		return this.delFlg;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  删除标记 1 - 删除  0 - 正常
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
	 *方法: 取得java.util.String
	 *@return: java.util.String 开户行
	 */
	@Column(name ="OPENBANK",nullable=true,length=60)
	public java.lang.String getOpenBank() {
		return openBank;
	}

	/**
	 *方法: 设置java.util.String
	 *@param: java.util.String  开户行
	 */
	public void setOpenBank(java.lang.String openBank) {
		this.openBank = openBank;
	}

	/**
	 *方法: 取得java.util.String
	 *@return: java.util.String 银行账户
	 */
	@Column(name ="BANKACCOUNT",nullable=true,length=50)
	public java.lang.String getBankAccount() {
		return bankAccount;
	}

	/**
	 *方法: 设置java.util.String
	 *@param: java.util.String  银行账户
	 */
	public void setBankAccount(java.lang.String bankAccount) {
		this.bankAccount = bankAccount;
	}

	/**
	 *方法: 取得java.util.String
	 *@return: java.util.String 所在省
	 */
	@Column(name ="PROVINCE",nullable=true,length=50)
	public java.lang.String getProvince() {
		return province;
	}

	/**
	 *方法: 设置java.util.String
	 *@param: java.util.String  所在省
	 */
	public void setProvince(java.lang.String province) {
		this.province = province;
	}

	/**
	 *方法: 取得java.util.String
	 *@return: java.util.String 所在市
	 */
	@Column(name ="CITY",nullable=true,length=50)
	public java.lang.String getCity() {
		return city;
	}

	/**
	 *方法: 设置java.util.String
	 *@param: java.util.String  所在市
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}
}
