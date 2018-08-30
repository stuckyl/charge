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
 * @Description: 法定工作日
 * @author zhangdaihao
 * @date 2018-06-29 13:35:38
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_attendance_calendar", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class AttendanceCalendarEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**年度*/
	private java.lang.Integer year;
	/**创建人*/
	private java.lang.String createdBy;
	/**创建日期*/
	private java.util.Date createdDate;
	/**最后修改人*/
	private java.lang.String lastModifiedBy;
	/**最后修改时间*/
	private java.util.Date lastModifiedDate;
	/**1月法定出勤日*/
	private java.lang.Integer month1;
	/**1月总天数*/
	private java.lang.Integer totaldays1;
	/**1月双休日天数*/
	private java.lang.Integer weekends1;
	/**1月法定假期*/
	private java.lang.Integer holidays1;
	/**2月法定出勤日*/
	private java.lang.Integer month2;
	/**2月总天数*/
	private java.lang.Integer totaldays2;
	/**2月双休日天数*/
	private java.lang.Integer weekends2;
	/**2月法定假期*/
	private java.lang.Integer holidays2;
	/**3月法定出勤日*/
	private java.lang.Integer month3;
	/**3月总天数*/
	private java.lang.Integer totaldays3;
	/**3月双休日天数*/
	private java.lang.Integer weekends3;
	/**3月法定假期*/
	private java.lang.Integer holidays3;
	/**4月法定出勤日*/
	private java.lang.Integer month4;
	/**4月总天数*/
	private java.lang.Integer totaldays4;
	/**4月双休日天数*/
	private java.lang.Integer weekends4;
	/**4月法定假期*/
	private java.lang.Integer holidays4;
	/**5月法定出勤日*/
	private java.lang.Integer month5;
	/**5月总天数*/
	private java.lang.Integer totaldays5;
	/**5月双休日天数*/
	private java.lang.Integer weekends5;
	/**5月法定假期*/
	private java.lang.Integer holidays5;
	/**6月法定出勤日*/
	private java.lang.Integer month6;
	/**6月总天数*/
	private java.lang.Integer totaldays6;
	/**6月双休日天数*/
	private java.lang.Integer weekends6;
	/**6月法定假期*/
	private java.lang.Integer holidays6;
	/**7月法定出勤日*/
	private java.lang.Integer month7;
	/**7月总天数*/
	private java.lang.Integer totaldays7;
	/**7月双休日天数*/
	private java.lang.Integer weekends7;
	/**7月法定假期*/
	private java.lang.Integer holidays7;
	/**8月法定出勤日*/
	private java.lang.Integer month8;
	/**8月总天数*/
	private java.lang.Integer totaldays8;
	/**8月双休日天数*/
	private java.lang.Integer weekends8;
	/**8月法定假期*/
	private java.lang.Integer holidays8;
	/**9月法定出勤日*/
	private java.lang.Integer month9;
	/**9月总天数*/
	private java.lang.Integer totaldays9;
	/**9月双休日天数*/
	private java.lang.Integer weekends9;
	/**9月法定假期*/
	private java.lang.Integer holidays9;
	/**10月法定出勤日*/
	private java.lang.Integer month10;
	/**10月总天数*/
	private java.lang.Integer totaldays10;
	/**10月双休日天数*/
	private java.lang.Integer weekends10;
	/**10月法定假期*/
	private java.lang.Integer holidays10;
	/**11月法定出勤日*/
	private java.lang.Integer month11;
	/**11月总天数*/
	private java.lang.Integer totaldays11;
	/**11月双休日天数*/
	private java.lang.Integer weekends11;
	/**11月法定假期*/
	private java.lang.Integer holidays11;
	/**12月法定出勤日*/
	private java.lang.Integer month12;
	/**12月总天数*/
	private java.lang.Integer totaldays12;
	/**12月双休日天数*/
	private java.lang.Integer weekends12;
	/**12月法定假期*/
	private java.lang.Integer holidays12;
	
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
	 *@return: java.lang.Integer  年度
	 */
	@Column(name ="YEAR",nullable=true,precision=10,scale=0)
	public java.lang.Integer getYear(){
		return this.year;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  年度
	 */
	public void setYear(java.lang.Integer year){
		this.year = year;
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  1月法定出勤日
	 */
	@Column(name ="MONTH1",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth1(){
		return this.month1;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  1月法定出勤日
	 */
	public void setMonth1(java.lang.Integer month1){
		this.month1 = month1;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  1月总天数
	 */
	@Column(name ="TOTALDAYS1",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays1(){
		return this.totaldays1;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  1月总天数
	 */
	public void setTotaldays1(java.lang.Integer totaldays1){
		this.totaldays1 = totaldays1;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  1月双休日天数
	 */
	@Column(name ="WEEKENDS1",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends1(){
		return this.weekends1;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  1月双休日天数
	 */
	public void setWeekends1(java.lang.Integer weekends1){
		this.weekends1 = weekends1;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  1月法定假期
	 */
	@Column(name ="HOLIDAYS1",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays1(){
		return this.holidays1;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  1月法定假期
	 */
	public void setHolidays1(java.lang.Integer holidays1){
		this.holidays1 = holidays1;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  2月法定出勤日
	 */
	@Column(name ="MONTH2",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth2(){
		return this.month2;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  2月法定出勤日
	 */
	public void setMonth2(java.lang.Integer month2){
		this.month2 = month2;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  2月总天数
	 */
	@Column(name ="TOTALDAYS2",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays2(){
		return this.totaldays2;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  2月总天数
	 */
	public void setTotaldays2(java.lang.Integer totaldays2){
		this.totaldays2 = totaldays2;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  2月双休日天数
	 */
	@Column(name ="WEEKENDS2",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends2(){
		return this.weekends2;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  2月双休日天数
	 */
	public void setWeekends2(java.lang.Integer weekends2){
		this.weekends2 = weekends2;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  2月法定假期
	 */
	@Column(name ="HOLIDAYS2",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays2(){
		return this.holidays2;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  2月法定假期
	 */
	public void setHolidays2(java.lang.Integer holidays2){
		this.holidays2 = holidays2;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  3月法定出勤日
	 */
	@Column(name ="MONTH3",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth3(){
		return this.month3;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  3月法定出勤日
	 */
	public void setMonth3(java.lang.Integer month3){
		this.month3 = month3;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  3月总天数
	 */
	@Column(name ="TOTALDAYS3",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays3(){
		return this.totaldays3;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  3月总天数
	 */
	public void setTotaldays3(java.lang.Integer totaldays3){
		this.totaldays3 = totaldays3;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  3月双休日天数
	 */
	@Column(name ="WEEKENDS3",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends3(){
		return this.weekends3;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  3月双休日天数
	 */
	public void setWeekends3(java.lang.Integer weekends3){
		this.weekends3 = weekends3;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  3月法定假期
	 */
	@Column(name ="HOLIDAYS3",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays3(){
		return this.holidays3;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  3月法定假期
	 */
	public void setHolidays3(java.lang.Integer holidays3){
		this.holidays3 = holidays3;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  4月法定出勤日
	 */
	@Column(name ="MONTH4",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth4(){
		return this.month4;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  4月法定出勤日
	 */
	public void setMonth4(java.lang.Integer month4){
		this.month4 = month4;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  4月总天数
	 */
	@Column(name ="TOTALDAYS4",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays4(){
		return this.totaldays4;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  4月总天数
	 */
	public void setTotaldays4(java.lang.Integer totaldays4){
		this.totaldays4 = totaldays4;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  4月双休日天数
	 */
	@Column(name ="WEEKENDS4",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends4(){
		return this.weekends4;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  4月双休日天数
	 */
	public void setWeekends4(java.lang.Integer weekends4){
		this.weekends4 = weekends4;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  4月法定假期
	 */
	@Column(name ="HOLIDAYS4",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays4(){
		return this.holidays4;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  4月法定假期
	 */
	public void setHolidays4(java.lang.Integer holidays4){
		this.holidays4 = holidays4;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  5月法定出勤日
	 */
	@Column(name ="MONTH5",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth5(){
		return this.month5;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  5月法定出勤日
	 */
	public void setMonth5(java.lang.Integer month5){
		this.month5 = month5;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  5月总天数
	 */
	@Column(name ="TOTALDAYS5",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays5(){
		return this.totaldays5;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  5月总天数
	 */
	public void setTotaldays5(java.lang.Integer totaldays5){
		this.totaldays5 = totaldays5;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  5月双休日天数
	 */
	@Column(name ="WEEKENDS5",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends5(){
		return this.weekends5;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  5月双休日天数
	 */
	public void setWeekends5(java.lang.Integer weekends5){
		this.weekends5 = weekends5;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  5月法定假期
	 */
	@Column(name ="HOLIDAYS5",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays5(){
		return this.holidays5;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  5月法定假期
	 */
	public void setHolidays5(java.lang.Integer holidays5){
		this.holidays5 = holidays5;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  6月法定出勤日
	 */
	@Column(name ="MONTH6",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth6(){
		return this.month6;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  6月法定出勤日
	 */
	public void setMonth6(java.lang.Integer month6){
		this.month6 = month6;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  6月总天数
	 */
	@Column(name ="TOTALDAYS6",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays6(){
		return this.totaldays6;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  6月总天数
	 */
	public void setTotaldays6(java.lang.Integer totaldays6){
		this.totaldays6 = totaldays6;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  6月双休日天数
	 */
	@Column(name ="WEEKENDS6",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends6(){
		return this.weekends6;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  6月双休日天数
	 */
	public void setWeekends6(java.lang.Integer weekends6){
		this.weekends6 = weekends6;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  6月法定假期
	 */
	@Column(name ="HOLIDAYS6",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays6(){
		return this.holidays6;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  6月法定假期
	 */
	public void setHolidays6(java.lang.Integer holidays6){
		this.holidays6 = holidays6;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  7月法定出勤日
	 */
	@Column(name ="MONTH7",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth7(){
		return this.month7;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  7月法定出勤日
	 */
	public void setMonth7(java.lang.Integer month7){
		this.month7 = month7;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  7月总天数
	 */
	@Column(name ="TOTALDAYS7",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays7(){
		return this.totaldays7;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  7月总天数
	 */
	public void setTotaldays7(java.lang.Integer totaldays7){
		this.totaldays7 = totaldays7;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  7月双休日天数
	 */
	@Column(name ="WEEKENDS7",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends7(){
		return this.weekends7;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  7月双休日天数
	 */
	public void setWeekends7(java.lang.Integer weekends7){
		this.weekends7 = weekends7;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  7月法定假期
	 */
	@Column(name ="HOLIDAYS7",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays7(){
		return this.holidays7;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  7月法定假期
	 */
	public void setHolidays7(java.lang.Integer holidays7){
		this.holidays7 = holidays7;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  8月法定出勤日
	 */
	@Column(name ="MONTH8",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth8(){
		return this.month8;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  8月法定出勤日
	 */
	public void setMonth8(java.lang.Integer month8){
		this.month8 = month8;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  8月总天数
	 */
	@Column(name ="TOTALDAYS8",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays8(){
		return this.totaldays8;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  8月总天数
	 */
	public void setTotaldays8(java.lang.Integer totaldays8){
		this.totaldays8 = totaldays8;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  8月双休日天数
	 */
	@Column(name ="WEEKENDS8",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends8(){
		return this.weekends8;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  8月双休日天数
	 */
	public void setWeekends8(java.lang.Integer weekends8){
		this.weekends8 = weekends8;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  8月法定假期
	 */
	@Column(name ="HOLIDAYS8",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays8(){
		return this.holidays8;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  8月法定假期
	 */
	public void setHolidays8(java.lang.Integer holidays8){
		this.holidays8 = holidays8;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  9月法定出勤日
	 */
	@Column(name ="MONTH9",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth9(){
		return this.month9;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  9月法定出勤日
	 */
	public void setMonth9(java.lang.Integer month9){
		this.month9 = month9;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  9月总天数
	 */
	@Column(name ="TOTALDAYS9",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays9(){
		return this.totaldays9;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  9月总天数
	 */
	public void setTotaldays9(java.lang.Integer totaldays9){
		this.totaldays9 = totaldays9;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  9月双休日天数
	 */
	@Column(name ="WEEKENDS9",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends9(){
		return this.weekends9;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  9月双休日天数
	 */
	public void setWeekends9(java.lang.Integer weekends9){
		this.weekends9 = weekends9;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  9月法定假期
	 */
	@Column(name ="HOLIDAYS9",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays9(){
		return this.holidays9;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  9月法定假期
	 */
	public void setHolidays9(java.lang.Integer holidays9){
		this.holidays9 = holidays9;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  10月法定出勤日
	 */
	@Column(name ="MONTH10",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth10(){
		return this.month10;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  10月法定出勤日
	 */
	public void setMonth10(java.lang.Integer month10){
		this.month10 = month10;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  10月总天数
	 */
	@Column(name ="TOTALDAYS10",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays10(){
		return this.totaldays10;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  10月总天数
	 */
	public void setTotaldays10(java.lang.Integer totaldays10){
		this.totaldays10 = totaldays10;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  10月双休日天数
	 */
	@Column(name ="WEEKENDS10",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends10(){
		return this.weekends10;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  10月双休日天数
	 */
	public void setWeekends10(java.lang.Integer weekends10){
		this.weekends10 = weekends10;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  10月法定假期
	 */
	@Column(name ="HOLIDAYS10",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays10(){
		return this.holidays10;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  10月法定假期
	 */
	public void setHolidays10(java.lang.Integer holidays10){
		this.holidays10 = holidays10;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  11月法定出勤日
	 */
	@Column(name ="MONTH11",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth11(){
		return this.month11;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  11月法定出勤日
	 */
	public void setMonth11(java.lang.Integer month11){
		this.month11 = month11;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  11月总天数
	 */
	@Column(name ="TOTALDAYS11",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays11(){
		return this.totaldays11;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  11月总天数
	 */
	public void setTotaldays11(java.lang.Integer totaldays11){
		this.totaldays11 = totaldays11;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  11月双休日天数
	 */
	@Column(name ="WEEKENDS11",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends11(){
		return this.weekends11;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  11月双休日天数
	 */
	public void setWeekends11(java.lang.Integer weekends11){
		this.weekends11 = weekends11;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  11月法定假期
	 */
	@Column(name ="HOLIDAYS11",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays11(){
		return this.holidays11;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  11月法定假期
	 */
	public void setHolidays11(java.lang.Integer holidays11){
		this.holidays11 = holidays11;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  12月法定出勤日
	 */
	@Column(name ="MONTH12",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMonth12(){
		return this.month12;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  12月法定出勤日
	 */
	public void setMonth12(java.lang.Integer month12){
		this.month12 = month12;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  12月总天数
	 */
	@Column(name ="TOTALDAYS12",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotaldays12(){
		return this.totaldays12;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  12月总天数
	 */
	public void setTotaldays12(java.lang.Integer totaldays12){
		this.totaldays12 = totaldays12;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  12月双休日天数
	 */
	@Column(name ="WEEKENDS12",nullable=true,precision=10,scale=0)
	public java.lang.Integer getWeekends12(){
		return this.weekends12;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  12月双休日天数
	 */
	public void setWeekends12(java.lang.Integer weekends12){
		this.weekends12 = weekends12;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  12月法定假期
	 */
	@Column(name ="HOLIDAYS12",nullable=true,precision=10,scale=0)
	public java.lang.Integer getHolidays12(){
		return this.holidays12;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  12月法定假期
	 */
	public void setHolidays12(java.lang.Integer holidays12){
		this.holidays12 = holidays12;
	}
}
