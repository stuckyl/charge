package com.charge.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Title: Entity
 * @Description: 经费申请表
 * @author sunyj
 * @date 2018-07-10 13:45:40
 * @version V1.0
 *
 */
@Entity
@Table(name = "c_expenses", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class ExpensesEntity {
	/**id*/
	@Excel(name = "ID")
	private java.lang.Integer id;
	/**薪酬年月 - 201802、201803、201804*/
	@Excel(name = "活动年月")
	private java.util.Date startMonth;
	@Excel(name = "活动起始日期")
	private java.util.Date startDate;
	@Excel(name = "活动日数")
	private java.lang.Double numberDate;
	@Excel(name = "活动主题")
	private java.lang.String theme;
	@Excel(name = "活动内容")
	private java.lang.String content;
	@Excel(name = "申请金额")
	private java.lang.Double money;
	@Excel(name = "活动人数")
	private java.lang.Integer numberPeople;
	@Excel(name = "人均消费")
	private java.lang.Double average;
	@Excel(name = "活动参与人姓名")
	private java.lang.String namePeople;
	@Excel(name = "所属部门")
	private java.lang.String departmentId;
	@Excel(name = "申报状态")
	private java.lang.Integer declareStatus;
	@Excel(name = "失败理由")
	private java.lang.String declareReturnreason;
	@Excel(name = "录入者")
	private java.lang.String inputerId;
	@Excel(name = "申报者")
	private java.lang.String reporterId;
	@Excel(name = "审核者")
	private java.lang.String checkerId;
	@Excel(name = "创建者")
	private java.lang.String createdBy;
	@Excel(name = "创建日期")
	private java.util.Date createdDate;

	@Excel(name = "删除标记")
	private java.lang.Integer expensesDelFlg;
	@Excel(name = "最后修改人")
	private java.lang.String lastModifiedBy;
	@Excel(name = "最后修改时间")
	private java.util.Date lastModifiedDate;
	/** 编辑限制 0_编辑中，1_无人编辑 **/
	private java.lang.String editors;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID",nullable=false,precision=10,scale=0)
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	@Column(name ="START_MONTH",nullable=true)
	public java.util.Date getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(java.util.Date startMonth) {
		this.startMonth = startMonth;
	}

	@Column(name ="START_DATE",nullable=true)
	public java.util.Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	@Column(name ="NUMBER_DATE",nullable=true,precision=9,scale=2)
	public java.lang.Double getNumberDate() {
		return numberDate;
	}
	public void setNumberDate(java.lang.Double numberDate) {
		this.numberDate = numberDate;
	}

	@Column(name ="THEME",nullable=true,length=60)
	public java.lang.String getTheme() {
		return theme;
	}
	public void setTheme(java.lang.String theme) {
		this.theme = theme;
	}

	@Column(name ="CONTENT",nullable=true,length=255)
	public java.lang.String getContent() {
		return content;
	}
	public void setContent(java.lang.String content) {
		this.content = content;
	}

	@Column(name ="MONEY",nullable=true,precision=9,scale=2)
	public java.lang.Double getMoney() {
		return money;
	}
	public void setMoney(java.lang.Double money) {
		this.money = money;
	}

	@Column(name ="NUMBER_PEOPLE",nullable=true,precision=9,scale=0)
	public java.lang.Integer getNumberPeople() {
		return numberPeople;
	}
	public void setNumberPeople(java.lang.Integer numberPeople) {
		this.numberPeople = numberPeople;
	}

	/**
	 * 人均消费
	 * */
	@Column(name ="AVERAGE",nullable=true,precision=9,scale=2)
	public java.lang.Double getAverage() {
		return average;
	}
	public void setAverage(java.lang.Double average) {
		this.average = average;
	}

	/**
	 * 活动人员姓名
	 * */
	@Column(name ="NAME_PEOPLE",nullable=true,length=255)
	public java.lang.String getNamePeople() {
		return namePeople;
	}
	public void setNamePeople(java.lang.String namePeople) {
		this.namePeople = namePeople;
	}

	/**
	 * 部门
	 * */
	@Column(name ="DEPARTMENT_ID",nullable=true,length=60)
	public java.lang.String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(java.lang.String departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * 获取申报状态
	 * */
	@Column(name ="DECLARE_STATUS",nullable=true,precision=1,scale=0)
	public java.lang.Integer getDeclareStatus() {
		return declareStatus;
	}
	public void setDeclareStatus(java.lang.Integer declareStatus) {
		this.declareStatus = declareStatus;
	}

	/**
	 * 失败理由
	 * */
	@Column(name ="DECLARE_RETURNREASON",nullable=true,length=255)
	public String getDeclareReturnreason() {
		return declareReturnreason;
	}
	public void setDeclareReturnreason(String declareReturnReason) {
		this.declareReturnreason = declareReturnReason;
	}

	/**
	 * 录入者
	 * */
	@Column(name ="INPUTER_ID",nullable=true,length=32)
	public java.lang.String getInputerId() {
		return inputerId;
	}
	public void setInputerId(java.lang.String inputerId) {
		this.inputerId = inputerId;
	}
	/**
	 * 申报者ID
	 * */
	@Column(name ="REPORTER_ID",nullable=true,length=32)
	public String getReporterId() {
		return reporterId;
	}
	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}

	/**
	 * 审核者ID
	 * */
	@Column(name ="CHECKER_ID",nullable=true,length=32)
	public String getCheckerId() {
		return checkerId;
	}
	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}

	/**
	 * 创建者
	 * */
	@Column(name ="CREATED_BY",nullable=true,length=32)
	public java.lang.String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(java.lang.String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * 创建日期
	 * */
	@Column(name ="CREATED_DATE",nullable=true,length=32)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * 删除标记
	 * */
	@Column(name ="EXPENSES_DEL_FLG",nullable=true,precision=1,scale=0)
	public java.lang.Integer getExpensesDelFlg() {
		return expensesDelFlg;
	}
	public void setExpensesDelFlg(java.lang.Integer expensesDelFlg) {
		this.expensesDelFlg = expensesDelFlg;
	}
	/**
	 *最后修改人
	 * */
	@Column(name ="LAST_MODIFIED_BY",nullable=true,length=60)
	public java.lang.String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(java.lang.String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 *最后修改时间
	 * */
	@Column(name ="LAST_MODIFIED_DATE",nullable=true,length=32)
	public java.util.Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(java.util.Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 *编辑限制
	 * */
	@Column(name ="EDITORS",nullable=true)
	public java.lang.String getEditors() {
		return editors;
	}
	public void setEditors(java.lang.String editors) {
		this.editors = editors;
	}
}
