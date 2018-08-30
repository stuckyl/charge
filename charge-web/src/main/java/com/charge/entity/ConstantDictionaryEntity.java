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
 * @Description: 系统常量实体
 * @author zhangdaihao
 * @date 2018-07-26 09:51:22
 * @version V1.0
 *
 */
@Entity
@Table(name = "c_constant_dic", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class ConstantDictionaryEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**程序查询时所用code*/
	private java.lang.String constantKey;
	/**常量名*/
	private java.lang.String constantName;
	/**常量值*/
	private java.lang.String constantValue;
	/**updateBy*/
	private java.lang.String updateBy;

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
	 *@return: java.lang.String  程序查询时所用code
	 */
	@Column(name ="CONSTANT_KEY",nullable=false,length=50)
	public java.lang.String getConstantKey(){
		return this.constantKey;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  程序查询时所用code
	 */
	public void setConstantKey(java.lang.String constantKey){
		this.constantKey = constantKey;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  常量名
	 */
	@Column(name ="CONSTANT_NAME",nullable=false,length=50)
	public java.lang.String getConstantName(){
		return this.constantName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  常量名
	 */
	public void setConstantName(java.lang.String constantName){
		this.constantName = constantName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  常量值
	 */
	@Column(name ="CONSTANT_VALUE",nullable=false,length=50)
	public java.lang.String getConstantValue(){
		return this.constantValue;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  常量值
	 */
	public void setConstantValue(java.lang.String constantValue){
		this.constantValue = constantValue;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  updateBy
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  updateBy
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
}
