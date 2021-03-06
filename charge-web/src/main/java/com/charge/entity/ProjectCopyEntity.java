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
 * @Description: 项目备份类
 * @author zhangdaihao
 * @date 2018-08-11 14:56:17
 * @version V1.0
 *
 */
@Entity
@Table(name = "c_project_copy", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class ProjectCopyEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**备份原id*/
	private java.lang.Integer batId;
	/**立项月份*/
	private java.util.Date projectMonth;
	/**立项日期*/
	private java.util.Date projectDate;
	/**项目所属部门*/
	private java.lang.String projectDepartment;
	/**项目上游客户*/
	private java.lang.String projectCustomer1;
	/**项目下游顾客*/
	private java.lang.String projectCustomer2;
	/**项目签约法人*/
	private java.lang.String projectConstant;
	/**项目收入*/
	private java.lang.Double projectIncome;
	/**项目支出*/
	private java.lang.Double projectPay;
	/**项目毛利*/
	private java.lang.Double projectProfit;
	/**项目毛利率*/
	private java.lang.Double projectProfitRate;
	/**申报状态*/
	private java.lang.Integer projectStatus;
	/**拒绝理由*/
	private java.lang.String projectReturnreason;
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
	/**审核者id*/
	private java.lang.String checkerId;
	/**审批者id*/
	private java.lang.String controllerId;
	/**删除标记*/
	private java.lang.Integer delFlage;
	/**修改标记*/
	private java.lang.String editors;
	/**c1类型   null_空    0_内部   1_外部*/
	private java.lang.Integer c1Other;
	/**c1备注*/
	private java.lang.String c1OtherRemarks;
	/**c1供应商id*/
	private java.lang.Integer c1OtherSupplier;
	/**c1供应商—金额*/
	private java.lang.Double c1OtherMoney;
	/**c2类型   null_空    0_内部   1_外部*/
	private java.lang.Integer c2Other;
	/**c2备注*/
	private java.lang.String c2OtherRemarks;
	/**c2供应商id*/
	private java.lang.Integer c2OtherSupplier;
	/**c2供应商—金额*/
	private java.lang.Double c2OtherMoney;
	/**c3类型   null_空    0_内部   1_外部*/
	private java.lang.Integer c3Other;
	/**c3备注*/
	private java.lang.String c3OtherRemarks;
	/**c3供应商id*/
	private java.lang.Integer c3OtherSupplier;
	/**c3供应商—金额*/
	private java.lang.Double c3OtherMoney;
	/**c4类型   null_空    0_内部   1_外部*/
	private java.lang.Integer c4Other;
	/**c4备注*/
	private java.lang.String c4OtherRemarks;
	/**c4供应商id*/
	private java.lang.Integer c4OtherSupplier;
	/**c4供应商—金额*/
	private java.lang.Double c4OtherMoney;
	/**c5类型   null_空    0_内部   1_外部*/
	private java.lang.Integer c5Other;
	/**c5备注*/
	private java.lang.String c5OtherRemarks;
	/**c5供应商id*/
	private java.lang.Integer c5OtherSupplier;
	/**c5供应商—金额*/
	private java.lang.Double c5OtherMoney;
	/**流转税  0-无    1-有流转税*/
	private java.lang.Integer isturnovertax;

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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  备份原id
	 */
	@Column(name ="BAT_ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getBatId(){
		return this.batId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  备份原id
	 */
	public void setBatId(java.lang.Integer batId){
		this.batId = batId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  立项月份
	 */
	@Column(name ="PROJECT_MONTH",nullable=true)
	public java.util.Date getProjectMonth(){
		return this.projectMonth;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  立项月份
	 */
	public void setProjectMonth(java.util.Date projectMonth){
		this.projectMonth = projectMonth;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  立项日期
	 */
	@Column(name ="PROJECT_DATE",nullable=true)
	public java.util.Date getProjectDate(){
		return this.projectDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  立项日期
	 */
	public void setProjectDate(java.util.Date projectDate){
		this.projectDate = projectDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目所属部门
	 */
	@Column(name ="PROJECT_DEPARTMENT",nullable=true,length=60)
	public java.lang.String getProjectDepartment(){
		return this.projectDepartment;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目所属部门
	 */
	public void setProjectDepartment(java.lang.String projectDepartment){
		this.projectDepartment = projectDepartment;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目上游客户
	 */
	@Column(name ="PROJECT_CUSTOMER1",nullable=true,length=60)
	public java.lang.String getProjectCustomer1(){
		return this.projectCustomer1;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目上游客户
	 */
	public void setProjectCustomer1(java.lang.String projectCustomer1){
		this.projectCustomer1 = projectCustomer1;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目下游顾客
	 */
	@Column(name ="PROJECT_CUSTOMER2",nullable=true,length=60)
	public java.lang.String getProjectCustomer2(){
		return this.projectCustomer2;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目下游顾客
	 */
	public void setProjectCustomer2(java.lang.String projectCustomer2){
		this.projectCustomer2 = projectCustomer2;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目签约法人
	 */
	@Column(name ="PROJECT_CONSTANT",nullable=true,length=60)
	public java.lang.String getProjectConstant(){
		return this.projectConstant;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目签约法人
	 */
	public void setProjectConstant(java.lang.String projectConstant){
		this.projectConstant = projectConstant;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  项目收入
	 */
	@Column(name ="PROJECT_INCOME",nullable=true,precision=13,scale=2)
	public java.lang.Double getProjectIncome(){
		return this.projectIncome;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  项目收入
	 */
	public void setProjectIncome(java.lang.Double projectIncome){
		this.projectIncome = projectIncome;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  项目支出
	 */
	@Column(name ="PROJECT_PAY",nullable=true,precision=13,scale=2)
	public java.lang.Double getProjectPay(){
		return this.projectPay;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  项目支出
	 */
	public void setProjectPay(java.lang.Double projectPay){
		this.projectPay = projectPay;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  项目毛利
	 */
	@Column(name ="PROJECT_PROFIT",nullable=true,precision=13,scale=2)
	public java.lang.Double getProjectProfit(){
		return this.projectProfit;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  项目毛利
	 */
	public void setProjectProfit(java.lang.Double projectProfit){
		this.projectProfit = projectProfit;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  项目毛利率
	 */
	@Column(name ="PROJECT_PROFIT_RATE",nullable=true,precision=13,scale=2)
	public java.lang.Double getProjectProfitRate(){
		return this.projectProfitRate;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  项目毛利率
	 */
	public void setProjectProfitRate(java.lang.Double projectProfitRate){
		this.projectProfitRate = projectProfitRate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  申报状态
	 */
	@Column(name ="PROJECT_STATUS",nullable=true,precision=10,scale=0)
	public java.lang.Integer getProjectStatus(){
		return this.projectStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  申报状态
	 */
	public void setProjectStatus(java.lang.Integer projectStatus){
		this.projectStatus = projectStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  拒绝理由
	 */
	@Column(name ="PROJECT_RETURNREASON",nullable=true,length=255)
	public java.lang.String getProjectReturnreason(){
		return this.projectReturnreason;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  拒绝理由
	 */
	public void setProjectReturnreason(java.lang.String projectReturnreason){
		this.projectReturnreason = projectReturnreason;
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
	 *@return: java.lang.String  审核者id
	 */
	@Column(name ="CHECKER_ID",nullable=true,length=32)
	public java.lang.String getCheckerId(){
		return this.checkerId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审核者id
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
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  删除标记
	 */
	@Column(name ="DEL_FLAGE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDelFlage(){
		return this.delFlage;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  删除标记
	 */
	public void setDelFlage(java.lang.Integer delFlage){
		this.delFlage = delFlage;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  修改标记
	 */
	@Column(name ="EDITORS",nullable=true,length=32)
	public java.lang.String getEditors(){
		return this.editors;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  修改标记
	 */
	public void setEditors(java.lang.String editors){
		this.editors = editors;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  c1类型   null_空    0_内部   1_外部
	 */
	@Column(name ="C1_OTHER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getC1Other(){
		return this.c1Other;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  c1类型   null_空    0_内部   1_外部
	 */
	public void setC1Other(java.lang.Integer c1Other){
		this.c1Other = c1Other;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  c1备注
	 */
	@Column(name ="C1_OTHER_REMARKS",nullable=true,length=255)
	public java.lang.String getC1OtherRemarks(){
		return this.c1OtherRemarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  c1备注
	 */
	public void setC1OtherRemarks(java.lang.String c1OtherRemarks){
		this.c1OtherRemarks = c1OtherRemarks;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  c1供应商id
	 */
	@Column(name ="C1_OTHER_SUPPLIER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getC1OtherSupplier(){
		return this.c1OtherSupplier;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  c1供应商id
	 */
	public void setC1OtherSupplier(java.lang.Integer c1OtherSupplier){
		this.c1OtherSupplier = c1OtherSupplier;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  c1供应商—金额
	 */
	@Column(name ="C1_OTHER_MONEY",nullable=true,precision=13,scale=2)
	public java.lang.Double getC1OtherMoney(){
		return this.c1OtherMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  c1供应商—金额
	 */
	public void setC1OtherMoney(java.lang.Double c1OtherMoney){
		this.c1OtherMoney = c1OtherMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  c2类型   null_空    0_内部   1_外部
	 */
	@Column(name ="C2_OTHER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getC2Other(){
		return this.c2Other;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  c2类型   null_空    0_内部   1_外部
	 */
	public void setC2Other(java.lang.Integer c2Other){
		this.c2Other = c2Other;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  c2备注
	 */
	@Column(name ="C2_OTHER_REMARKS",nullable=true,length=255)
	public java.lang.String getC2OtherRemarks(){
		return this.c2OtherRemarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  c2备注
	 */
	public void setC2OtherRemarks(java.lang.String c2OtherRemarks){
		this.c2OtherRemarks = c2OtherRemarks;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  c2供应商id
	 */
	@Column(name ="C2_OTHER_SUPPLIER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getC2OtherSupplier(){
		return this.c2OtherSupplier;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  c2供应商id
	 */
	public void setC2OtherSupplier(java.lang.Integer c2OtherSupplier){
		this.c2OtherSupplier = c2OtherSupplier;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  c2供应商—金额
	 */
	@Column(name ="C2_OTHER_MONEY",nullable=true,precision=13,scale=2)
	public java.lang.Double getC2OtherMoney(){
		return this.c2OtherMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  c2供应商—金额
	 */
	public void setC2OtherMoney(java.lang.Double c2OtherMoney){
		this.c2OtherMoney = c2OtherMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  c3类型   null_空    0_内部   1_外部
	 */
	@Column(name ="C3_OTHER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getC3Other(){
		return this.c3Other;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  c3类型   null_空    0_内部   1_外部
	 */
	public void setC3Other(java.lang.Integer c3Other){
		this.c3Other = c3Other;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  c3备注
	 */
	@Column(name ="C3_OTHER_REMARKS",nullable=true,length=255)
	public java.lang.String getC3OtherRemarks(){
		return this.c3OtherRemarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  c3备注
	 */
	public void setC3OtherRemarks(java.lang.String c3OtherRemarks){
		this.c3OtherRemarks = c3OtherRemarks;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  c3供应商id
	 */
	@Column(name ="C3_OTHER_SUPPLIER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getC3OtherSupplier(){
		return this.c3OtherSupplier;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  c3供应商id
	 */
	public void setC3OtherSupplier(java.lang.Integer c3OtherSupplier){
		this.c3OtherSupplier = c3OtherSupplier;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  c3供应商—金额
	 */
	@Column(name ="C3_OTHER_MONEY",nullable=true,precision=13,scale=2)
	public java.lang.Double getC3OtherMoney(){
		return this.c3OtherMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  c3供应商—金额
	 */
	public void setC3OtherMoney(java.lang.Double c3OtherMoney){
		this.c3OtherMoney = c3OtherMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  c4类型   null_空    0_内部   1_外部
	 */
	@Column(name ="C4_OTHER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getC4Other(){
		return this.c4Other;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  c4类型   null_空    0_内部   1_外部
	 */
	public void setC4Other(java.lang.Integer c4Other){
		this.c4Other = c4Other;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  c4备注
	 */
	@Column(name ="C4_OTHER_REMARKS",nullable=true,length=255)
	public java.lang.String getC4OtherRemarks(){
		return this.c4OtherRemarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  c4备注
	 */
	public void setC4OtherRemarks(java.lang.String c4OtherRemarks){
		this.c4OtherRemarks = c4OtherRemarks;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  c4供应商id
	 */
	@Column(name ="C4_OTHER_SUPPLIER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getC4OtherSupplier(){
		return this.c4OtherSupplier;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  c4供应商id
	 */
	public void setC4OtherSupplier(java.lang.Integer c4OtherSupplier){
		this.c4OtherSupplier = c4OtherSupplier;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  c4供应商—金额
	 */
	@Column(name ="C4_OTHER_MONEY",nullable=true,precision=13,scale=2)
	public java.lang.Double getC4OtherMoney(){
		return this.c4OtherMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  c4供应商—金额
	 */
	public void setC4OtherMoney(java.lang.Double c4OtherMoney){
		this.c4OtherMoney = c4OtherMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  c5类型   null_空    0_内部   1_外部
	 */
	@Column(name ="C5_OTHER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getC5Other(){
		return this.c5Other;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  c5类型   null_空    0_内部   1_外部
	 */
	public void setC5Other(java.lang.Integer c5Other){
		this.c5Other = c5Other;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  c5备注
	 */
	@Column(name ="C5_OTHER_REMARKS",nullable=true,length=255)
	public java.lang.String getC5OtherRemarks(){
		return this.c5OtherRemarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  c5备注
	 */
	public void setC5OtherRemarks(java.lang.String c5OtherRemarks){
		this.c5OtherRemarks = c5OtherRemarks;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  c5供应商id
	 */
	@Column(name ="C5_OTHER_SUPPLIER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getC5OtherSupplier(){
		return this.c5OtherSupplier;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  c5供应商id
	 */
	public void setC5OtherSupplier(java.lang.Integer c5OtherSupplier){
		this.c5OtherSupplier = c5OtherSupplier;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  c5供应商—金额
	 */
	@Column(name ="C5_OTHER_MONEY",nullable=true,precision=13,scale=2)
	public java.lang.Double getC5OtherMoney(){
		return this.c5OtherMoney;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  c5供应商—金额
	 */
	public void setC5OtherMoney(java.lang.Double c5OtherMoney){
		this.c5OtherMoney = c5OtherMoney;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  流转税标记 0-无   1-有
	 */
	@Column(name ="ISTURNOVERTAX")
	public java.lang.Integer getIsturnovertax() {
		return isturnovertax;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  流转税标记 0-无   1-有
	 */
	public void setIsturnovertax(java.lang.Integer isturnovertax) {
		this.isturnovertax = isturnovertax;
	}
}
