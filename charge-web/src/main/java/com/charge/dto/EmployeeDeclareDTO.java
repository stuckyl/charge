package com.charge.dto;

/**  
* @Title: EmployeeDeclareDTO
* @Description: 申报查询数据传输对象  
* @author wenst  
* @date 2018年3月30日
* @version v1.0  
*/  
public class EmployeeDeclareDTO{
	private String departId;//部门ID
	private String name;//姓名
	private String directSignCustomer;//直签客户
	private String indirectSignCustomer;//转签客户
	private String billingStart;//开票开始日期
	private String billingEnd;//开票结束日期
	private Integer invoiceStatus;//发票状态
	private String declareStart;//申报开始日期
	private String declareEnd;//申报结束日期
	public String getDepartId() {
		return departId;
	}
	public void setDepartId(String departId) {
		this.departId = departId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDirectSignCustomer() {
		return directSignCustomer;
	}
	public void setDirectSignCustomer(String directSignCustomer) {
		this.directSignCustomer = directSignCustomer;
	}
	public String getIndirectSignCustomer() {
		return indirectSignCustomer;
	}
	public void setIndirectSignCustomer(String indirectSignCustomer) {
		this.indirectSignCustomer = indirectSignCustomer;
	}
	public String getBillingStart() {
		return billingStart;
	}
	public void setBillingStart(String billingStart) {
		this.billingStart = billingStart;
	}
	public String getBillingEnd() {
		return billingEnd;
	}
	public void setBillingEnd(String billingEnd) {
		this.billingEnd = billingEnd;
	}
	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public String getDeclareStart() {
		return declareStart;
	}
	public void setDeclareStart(String declareStart) {
		this.declareStart = declareStart;
	}
	public String getDeclareEnd() {
		return declareEnd;
	}
	public void setDeclareEnd(String declareEnd) {
		this.declareEnd = declareEnd;
	}
	
}
