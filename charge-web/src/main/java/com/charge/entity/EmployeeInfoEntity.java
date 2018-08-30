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
 * @Description: 员工信息实体类
 * @author zhangdaihao
 * @date 2018-07-12 11:02:17
 * @version V1.0
 *
 */
@Entity
@Table(name = "c_employee_info", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class EmployeeInfoEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**员工身份证号*/
	private java.lang.String code;
	/**员工姓名*/
	private java.lang.String name;
	/**所属部门*/
	private java.lang.String department;
	/**员工标志（TECH外派 0，OP总部 1）*/
	private java.lang.Integer employeeFlag;
	/**联系方式（手机）*/
	private java.lang.String contactWay;
	/**邮箱*/
	private java.lang.String email;
	/**户口性质*/
	private java.lang.Integer householdRegistration;
	/**性别*/
	private java.lang.Integer gender;
	/**招商银行账户*/
	private java.lang.String cmbAccount;
	/**工商银行账户*/
	private java.lang.String icbcAccount;
	/**入职日*/
	private java.util.Date entryDate;
	/**A（标准）*/
	private java.lang.Double aStandardSalary;
	/**基本工资*/
	private java.lang.Double basePay;
	/**绩效工资*/
	private java.lang.Double meritPay;
	/**试用折扣率*/
	private java.lang.Integer discount;
	/**六金城市*/
	private java.lang.String sixGoldCity;
	/**录入者id*/
	private java.lang.String inputerId;
	/**申报者id*/
	private java.lang.String reporterId;
	/**审批者id*/
	private java.lang.String controllerId;
	/**六金基数*/
	private java.lang.Double sixGoldBase;
	/**发薪地1*/
	private java.lang.String a1Place;
	/**A1（工资） 发薪金额1*/
	private java.lang.Double a1Payment;
	/**发薪地点2  如：北京、上海、江苏、昆山、江苏、广州、深圳、智蓝*/
	private java.lang.String a2Place;
	/**发薪金额2 A1+A2=A*/
	private java.lang.Double a2Payment;
	/**申报状态   2入职成功 1离职*/
	private java.lang.Integer declareStatus;
	/**离职状态 0-未入职，1-在职，2-离职*/
	private java.lang.Integer quitStatus;
	/**离职日*/
	private java.util.Date quitDate;
	/**离职理由*/
	private java.lang.String quitReason;
	/**离职详细信息*/
	private java.lang.String quitReasonDetails;
	/**签约法人主体*/
	private java.lang.Integer signCorporate;
	/**客户名称*/
	private java.lang.Integer customerId;
	/**单价方式*/
	private java.lang.Integer unitPriceType;
	/**单价*/
	private java.lang.Double unitPrice;
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
	/**申请失败理由*/
	private java.lang.String loseReason;
	/**生效日期*/
	private java.util.Date effectiveDate;
	/**失效日期*/
	private java.util.Date expiryDate;
	/**入保情况 0 未入保 1已入保 2需退保 3已退保*/
	private java.lang.Integer insurance;
	/**变动通过情况 0未变动，1变动未通过，2通过，3再次变动未通过*/
	private java.lang.Integer changeFlag;
	/**六金城市 变动*/
	private java.lang.String sixGoldCityCh;
	/**六金基数 变动*/
	private java.lang.Double sixGoldBaseCh;
	/**A（标准）变动*/
	private java.lang.Double aStandardSalaryCh;
	/**发薪地1 变动*/
	private java.lang.String a1PlaceCh;
	/**A1(工资)发薪金额1 变动*/
	private java.lang.Double a1PaymentCh;
	/**变动生效日期*/
	private java.util.Date changeDate;
	/**编辑限制，限制同时编辑*/
	private java.lang.String editors;
	/**发薪地2 变动*/
	private java.lang.String a2PlaceCh;
	/**A1(工资)发薪金额2 变动*/
	private java.lang.Double a2PaymentCh;

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	 *@return: java.lang.String  员工身份证号
	 */
	@Column(name ="CODE",nullable=true,length=50)
	public java.lang.String getCode(){
		return this.code;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  员工身份证号
	 */
	public void setCode(java.lang.String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  员工姓名
	 */
	@Column(name ="NAME",nullable=true,length=30)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  员工姓名
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属部门
	 */
	@Column(name ="DEPARTMENT",nullable=true,length=60)
	public java.lang.String getDepartment(){
		return this.department;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属部门
	 */
	public void setDepartment(java.lang.String department){
		this.department = department;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  员工标志（TECH外派 0，OP总部 1）
	 */
	@Column(name ="EMPLOYEE_FLAG",nullable=true,precision=10,scale=0)
	public java.lang.Integer getEmployeeFlag(){
		return this.employeeFlag;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  员工标志（TECH外派 0，OP总部 1）
	 */
	public void setEmployeeFlag(java.lang.Integer employeeFlag){
		this.employeeFlag = employeeFlag;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  联系方式（手机）
	 */
	@Column(name ="CONTACT_WAY",nullable=true,length=100)
	public java.lang.String getContactWay(){
		return this.contactWay;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  联系方式（手机）
	 */
	public void setContactWay(java.lang.String contactWay){
		this.contactWay = contactWay;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  邮箱
	 */
	@Column(name ="EMAIL",nullable=true,length=100)
	public java.lang.String getEmail(){
		return this.email;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  邮箱
	 */
	public void setEmail(java.lang.String email){
		this.email = email;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  户口性质
	 */
	@Column(name ="HOUSEHOLD_REGISTRATION",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHouseholdRegistration(){
		return this.householdRegistration;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  户口性质
	 */
	public void setHouseholdRegistration(java.lang.Integer householdRegistration){
		this.householdRegistration = householdRegistration;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  性别
	 */
	@Column(name ="GENDER",nullable=true,precision=10,scale=0)
	public java.lang.Integer getGender(){
		return this.gender;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  性别
	 */
	public void setGender(java.lang.Integer gender){
		this.gender = gender;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  招商银行账户
	 */
	@Column(name ="CMB_ACCOUNT",nullable=true,length=50)
	public java.lang.String getCmbAccount(){
		return this.cmbAccount;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  招商银行账户
	 */
	public void setCmbAccount(java.lang.String cmbAccount){
		this.cmbAccount = cmbAccount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  工商银行账户
	 */
	@Column(name ="ICBC_ACCOUNT",nullable=true,length=50)
	public java.lang.String getIcbcAccount(){
		return this.icbcAccount;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工商银行账户
	 */
	public void setIcbcAccount(java.lang.String icbcAccount){
		this.icbcAccount = icbcAccount;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  入职日
	 */
	@Column(name ="ENTRY_DATE",nullable=true)
	public java.util.Date getEntryDate(){
		return this.entryDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  入职日
	 */
	public void setEntryDate(java.util.Date entryDate){
		this.entryDate = entryDate;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  A（标准）
	 */
	@Column(name ="A_STANDARD_SALARY",nullable=true,precision=11,scale=2)
	public java.lang.Double getAStandardSalary(){
		return this.aStandardSalary;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  A（标准）
	 */
	public void setAStandardSalary(java.lang.Double aStandardSalary){
		this.aStandardSalary = aStandardSalary;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  基本工资
	 */
	@Column(name ="BASE_PAY",nullable=true,precision=11,scale=2)
	public java.lang.Double getBasePay(){
		return this.basePay;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  基本工资
	 */
	public void setBasePay(java.lang.Double basePay){
		this.basePay = basePay;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  绩效工资
	 */
	@Column(name ="MERIT_PAY",nullable=true,precision=11,scale=2)
	public java.lang.Double getMeritPay(){
		return this.meritPay;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  绩效工资
	 */
	public void setMeritPay(java.lang.Double meritPay){
		this.meritPay = meritPay;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  试用折扣率
	 */
	@Column(name ="DISCOUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDiscount(){
		return this.discount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  试用折扣率
	 */
	public void setDiscount(java.lang.Integer discount){
		this.discount = discount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  六金城市
	 */
	@Column(name ="SIX_GOLD_CITY",nullable=true,length=60)
	public java.lang.String getSixGoldCity(){
		return this.sixGoldCity;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  六金城市
	 */
	public void setSixGoldCity(java.lang.String sixGoldCity){
		this.sixGoldCity = sixGoldCity;
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
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  六金基数
	 */
	@Column(name ="SIX_GOLD_BASE",nullable=true,precision=11,scale=2)
	public java.lang.Double getSixGoldBase(){
		return this.sixGoldBase;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  六金基数
	 */
	public void setSixGoldBase(java.lang.Double sixGoldBase){
		this.sixGoldBase = sixGoldBase;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发薪地1
	 */
	@Column(name ="A1_PLACE",nullable=true,length=30)
	public java.lang.String getA1Place(){
		return this.a1Place;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发薪地1
	 */
	public void setA1Place(java.lang.String a1Place){
		this.a1Place = a1Place;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  A1（工资） 发薪金额1
	 */
	@Column(name ="A1_PAYMENT",nullable=true,precision=11,scale=2)
	public java.lang.Double getA1Payment(){
		return this.a1Payment;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  A1（工资） 发薪金额1
	 */
	public void setA1Payment(java.lang.Double a1Payment){
		this.a1Payment = a1Payment;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发薪地点2  如：北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
	 */
	@Column(name ="A2_PLACE",nullable=true,length=30)
	public java.lang.String getA2Place(){
		return this.a2Place;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发薪地点2  如：北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
	 */
	public void setA2Place(java.lang.String a2Place){
		this.a2Place = a2Place;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  发薪金额2 A1+A2=A
	 */
	@Column(name ="A2_PAYMENT",nullable=true,precision=11,scale=2)
	public java.lang.Double getA2Payment(){
		return this.a2Payment;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  发薪金额2 A1+A2=A
	 */
	public void setA2Payment(java.lang.Double a2Payment){
		this.a2Payment = a2Payment;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  申报状态   1录入完成，2申报失败，3申报处理，4审批失败，5审批处理，7待入职，8入职成功
	 */
	@Column(name ="DECLARE_STATUS",nullable=true,precision=10,scale=0)
	public java.lang.Integer getDeclareStatus(){
		return this.declareStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  申报状态   1录入完成，2申报失败，3申报处理，4审批失败，5审批处理，7待入职，8入职成功
	 */
	public void setDeclareStatus(java.lang.Integer declareStatus){
		this.declareStatus = declareStatus;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  离职状态 0-未入职，1-在职，2-离职
	 */
	@Column(name ="QUIT_STATUS",nullable=true,precision=10,scale=0)
	public java.lang.Integer getQuitStatus(){
		return this.quitStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  离职状态 0-未入职，1-在职，2-离职
	 */
	public void setQuitStatus(java.lang.Integer quitStatus){
		this.quitStatus = quitStatus;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  离职日
	 */
	@Column(name ="QUIT_DATE",nullable=true)
	public java.util.Date getQuitDate(){
		return this.quitDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  离职日
	 */
	public void setQuitDate(java.util.Date quitDate){
		this.quitDate = quitDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  离职理由
	 */
	@Column(name ="QUIT_REASON",nullable=true,length=500)
	public java.lang.String getQuitReason(){
		return this.quitReason;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  离职理由
	 */
	public void setQuitReason(java.lang.String quitReason){
		this.quitReason = quitReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  离职详细信息
	 */
	@Column(name ="QUIT_REASON_DETAILS",nullable=true,length=500)
	public java.lang.String getQuitReasonDetails(){
		return this.quitReasonDetails;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  离职详细信息
	 */
	public void setQuitReasonDetails(java.lang.String quitReasonDetails){
		this.quitReasonDetails = quitReasonDetails;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  签约法人主体
	 */
	@Column(name ="SIGN_CORPORATE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getSignCorporate(){
		return this.signCorporate;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  签约法人主体
	 */
	public void setSignCorporate(java.lang.Integer signCorporate){
		this.signCorporate = signCorporate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客户名称
	 */
	@Column(name ="CUSTOMER_NAME",nullable=true,length=30)
	public java.lang.Integer getCustomerId(){
		return this.customerId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客户名称
	 */
	public void setCustomerId(java.lang.Integer customerId){
		this.customerId = customerId;
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
	@Column(name ="UNIT_PRICE",nullable=true,precision=11,scale=2)
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
	 *@return: java.lang.String  申请失败理由
	 */
	@Column(name ="LOSE_REASON",nullable=true,length=255)
	public java.lang.String getLoseReason(){
		return this.loseReason;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  申请失败理由
	 */
	public void setLoseReason(java.lang.String loseReason){
		this.loseReason = loseReason;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  生效日期
	 */
	@Column(name ="EFFECTIVE_DATE",nullable=true)
	public java.util.Date getEffectiveDate(){
		return this.effectiveDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  生效日期
	 */
	public void setEffectiveDate(java.util.Date effectiveDate){
		this.effectiveDate = effectiveDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  失效日期
	 */
	@Column(name ="EXPIRY_DATE",nullable=true)
	public java.util.Date getExpiryDate(){
		return this.expiryDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  失效日期
	 */
	public void setExpiryDate(java.util.Date expiryDate){
		this.expiryDate = expiryDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  入保情况 0 未入保 1已入保 2需退保 3已退保
	 */
	@Column(name ="INSURANCE",nullable=true,precision=10,scale=0)
	public java.lang.Integer getInsurance(){
		return this.insurance;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  入保情况 0 未入保 1已入保 2需退保 3已退保
	 */
	public void setInsurance(java.lang.Integer insurance){
		this.insurance = insurance;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  变动通过情况 0未变动，1变动未通过，2通过，3再次变动未通过
	 */
	@Column(name ="CHANGE_FLAG",nullable=true,precision=10,scale=0)
	public java.lang.Integer getChangeFlag(){
		return this.changeFlag;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  变动通过情况 0未变动，1变动未通过，2通过，3再次变动未通过
	 */
	public void setChangeFlag(java.lang.Integer changeFlag){
		this.changeFlag = changeFlag;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  六金城市 变动
	 */
	@Column(name ="SIX_GOLD_CITY_CH",nullable=true,length=30)
	public java.lang.String getSixGoldCityCh(){
		return this.sixGoldCityCh;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  六金城市 变动
	 */
	public void setSixGoldCityCh(java.lang.String sixGoldCityCh){
		this.sixGoldCityCh = sixGoldCityCh;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  六金基数 变动
	 */
	@Column(name ="SIX_GOLD_BASE_CH",nullable=true,precision=11,scale=2)
	public java.lang.Double getSixGoldBaseCh(){
		return this.sixGoldBaseCh;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  六金基数 变动
	 */
	public void setSixGoldBaseCh(java.lang.Double sixGoldBaseCh){
		this.sixGoldBaseCh = sixGoldBaseCh;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  A（标准）变动
	 */
	@Column(name ="A_STANDARD_SALARY_CH",nullable=true,precision=11,scale=2)
	public java.lang.Double getAStandardSalaryCh(){
		return this.aStandardSalaryCh;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  A（标准）变动
	 */
	public void setAStandardSalaryCh(java.lang.Double aStandardSalaryCh){
		this.aStandardSalaryCh = aStandardSalaryCh;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发薪地1 变动
	 */
	@Column(name ="A1_PLACE_CH",nullable=true,length=30)
	public java.lang.String getA1PlaceCh(){
		return this.a1PlaceCh;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发薪地1 变动
	 */
	public void setA1PlaceCh(java.lang.String a1PlaceCh){
		this.a1PlaceCh = a1PlaceCh;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  A1(工资)发薪金额1 变动
	 */
	@Column(name ="A1_PAYMENT_CH",nullable=true,precision=11,scale=2)
	public java.lang.Double getA1PaymentCh(){
		return this.a1PaymentCh;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  A1(工资)发薪金额1 变动
	 */
	public void setA1PaymentCh(java.lang.Double a1PaymentCh){
		this.a1PaymentCh = a1PaymentCh;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发薪地2 变动
	 */
	@Column(name ="A2_PLACE_CH",nullable=true,length=30)
	public java.lang.String getA2PlaceCh(){
		return this.a2PlaceCh;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发薪地1 变动
	 */
	public void setA2PlaceCh(java.lang.String a2PlaceCh){
		this.a2PlaceCh = a2PlaceCh;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  A1(工资)发薪金额2 变动
	 */
	@Column(name ="A2_PAYMENT_CH",nullable=true,precision=11,scale=2)
	public java.lang.Double getA2PaymentCh(){
		return this.a2PaymentCh;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  A1(工资)发薪金额2 变动
	 */
	public void setA2PaymentCh(java.lang.Double a2PaymentCh){
		this.a2PaymentCh = a2PaymentCh;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  变动日期
	 */
	@Column(name ="CHANGE_DATE",nullable=true)
	public java.util.Date getChangeDate(){
		return this.changeDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  变动日期
	 */
	public void setChangeDate(java.util.Date changeDate){
		this.changeDate = changeDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  编辑限制，限制同时编辑
	 */
	@Column(name ="EDITORS",nullable=true,length=50)
	public java.lang.String getEditors(){
		return this.editors;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  编辑限制，限制同时编辑
	 */
	public void setEditors(java.lang.String editors){
		this.editors = editors;
	}
}
