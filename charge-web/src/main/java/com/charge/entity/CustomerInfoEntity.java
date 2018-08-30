package com.charge.entity;

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

/**
 * @Title: Entity
 * @Description: 客户信息表
 * @author wenst
 * @date 2018-03-20 08:41:04
 * @version V1.0
 *
 */
@Entity
@Table(name = "c_customer_info", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class CustomerInfoEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**客户名称*/
	private java.lang.String name;

	/**客户简称*/
	private String code;
	/**客户地址*/
	private java.lang.String address;
	/**联系人*/
	private java.lang.String contact;
	/**联系电话*/
	private java.lang.String tel;
	/**约定工作日数*/
	private java.lang.Double workDays;
	/**约定帐龄*/
	private Double accountDelay;

	/**企业性质   0-国企 1-外企 2-民企*/
	private Integer corporateType;
	/**签约法人*/
	private Integer signCorporate;
	/**冻结标记 1 - 冻结  0 - 正常*/
	private java.lang.Integer activeFlg;
	/**删除标记 1 - 删除  0 - 正常*/
	private java.lang.Integer delFlg;
	/**客户标记1 - 下游供应商  0 - 上游客户*/
/*	private java.lang.Integer customerFlage;*/
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
	@Column(name ="tel")
	public java.lang.String getTel() {
		return tel;
	}

	public void setTel(java.lang.String tel) {
		this.tel = tel;
	}
	@Column(name ="work_days")
	public Double getWorkDays() {
		return workDays;
	}

	public void setWorkDays(Double workDays) {
		this.workDays = workDays;
	}
	@Column(name ="account_delay")
	public Double getAccountDelay() {
		return accountDelay;
	}

	public void setAccountDelay(Double accountDelay) {
		this.accountDelay = accountDelay;
	}

	@Column(name ="corporate_type")
	public Integer getCorporateType() {
		return corporateType;
	}

	public void setCorporateType(Integer corporateType) {
		this.corporateType = corporateType;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客户名称
	 */
	@Column(name ="NAME",nullable=true,length=30)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客户名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客户地址
	 */
	@Column(name ="ADDRESS",nullable=true,length=300)
	public java.lang.String getAddress(){
		return this.address;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客户地址
	 */
	public void setAddress(java.lang.String address){
		this.address = address;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  联系人
	 */
	@Column(name ="CONTACT",nullable=true,length=30)
	public java.lang.String getContact(){
		return this.contact;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  联系人
	 */
	public void setContact(java.lang.String contact){
		this.contact = contact;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  签约法人主体
	 */
	@Column(name="sign_corporate")
	public Integer getSignCorporate(){
		return this.signCorporate;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  签约法人主体
	 */
	public void setSignCorporate(Integer signCorporate){
		this.signCorporate = signCorporate;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  冻结标记 1 - 冻结  0 - 正常
	 */
	@Column(name ="ACTIVE_FLG",nullable=true,precision=10,scale=0)
	public java.lang.Integer getActiveFlg() {
		return activeFlg;
	}

	public void setActiveFlg(java.lang.Integer activeFlg) {
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  客户标记 1 - 下游顾客  0 - 上游客户
	 */
	/*@Column(name ="CUSTOMER_FLAGE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getCustomerFlage() {
		return customerFlage;
	}*/

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  客户标记 1 - 下游顾客  0 - 上游客户
	 */
	/*public void setCustomerFlage(java.lang.Integer customerFlage) {
		this.customerFlage = customerFlage;
	}*/

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

	@Column(name ="code",nullable=true)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
