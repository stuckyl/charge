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
 * @Description: 邮箱配置
 * @author wenst
 * @date 2018-03-20 14:22:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "c_email_config", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class EmailConfigEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**主机  例：smtp.qq.com*/
	private java.lang.String host;
	/**账号  例：test@qq.com*/
	private java.lang.String account;
	/**密码*/
	private java.lang.String password;
	/**最后发送时间*/
	private java.util.Date lastSendTime;
	/**发送次数*/
	private java.lang.Integer sendCount;
	/**最大可发送次数*/
	private java.lang.Integer maxCount;
	/**发送人 例：test@qq.com*/
	private java.lang.String mailFrom;
	/**端口  默认 25*/
	private java.lang.Integer port;
	/**开启ssl  0 - 不开启  1 - 开启*/
	private java.lang.Integer openSsl;
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
	 *@return: java.lang.String  主机  例：smtp.qq.com
	 */
	@Column(name ="HOST",nullable=true,length=50)
	public java.lang.String getHost(){
		return this.host;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主机  例：smtp.qq.com
	 */
	public void setHost(java.lang.String host){
		this.host = host;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号  例：test@qq.com
	 */
	@Column(name ="ACCOUNT",nullable=true,length=255)
	public java.lang.String getAccount(){
		return this.account;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号  例：test@qq.com
	 */
	public void setAccount(java.lang.String account){
		this.account = account;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  密码
	 */
	@Column(name ="PASSWORD",nullable=true,length=255)
	public java.lang.String getPassword(){
		return this.password;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  密码
	 */
	public void setPassword(java.lang.String password){
		this.password = password;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后发送时间
	 */
	@Column(name ="LAST_SEND_TIME",nullable=true)
	public java.util.Date getLastSendTime(){
		return this.lastSendTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后发送时间
	 */
	public void setLastSendTime(java.util.Date lastSendTime){
		this.lastSendTime = lastSendTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  发送次数
	 */
	@Column(name ="SEND_COUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getSendCount(){
		return this.sendCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  发送次数
	 */
	public void setSendCount(java.lang.Integer sendCount){
		this.sendCount = sendCount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  最大可发送次数
	 */
	@Column(name ="MAX_COUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMaxCount(){
		return this.maxCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  最大可发送次数
	 */
	public void setMaxCount(java.lang.Integer maxCount){
		this.maxCount = maxCount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发送人 例：test@qq.com
	 */
	@Column(name ="MAIL_FROM",nullable=true,length=255)
	public java.lang.String getMailFrom(){
		return this.mailFrom;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发送人 例：test@qq.com
	 */
	public void setMailFrom(java.lang.String mailFrom){
		this.mailFrom = mailFrom;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  端口  默认 25
	 */
	@Column(name ="PORT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getPort(){
		return this.port;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  端口  默认 25
	 */
	public void setPort(java.lang.Integer port){
		this.port = port;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  开启ssl  0 - 不开启  1 - 开启
	 */
	@Column(name ="OPEN_SSL",nullable=true,precision=10,scale=0)
	public java.lang.Integer getOpenSsl(){
		return this.openSsl;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  开启ssl  0 - 不开启  1 - 开启
	 */
	public void setOpenSsl(java.lang.Integer openSsl){
		this.openSsl = openSsl;
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
