package com.charge.service;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.MailUtil;
import org.omg.CORBA.ObjectHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.Else;
import com.charge.entity.CustomerInfoEntity;
import com.charge.entity.EmailConfigEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.repository.CommonRepository;
import com.charge.repository.EmailConfigRepository;
import com.charge.repository.EmployeeInfoRepository;

import jodd.mail.Email;

@Service
@Transactional
public class EmployeeInfoService {
	private static final Logger log = Logger.getLogger(EmployeeDeclareService.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private EmployeeInfoRepository employeeInfoRepo;

	@Autowired
	private EmailConfigRepository emailConfigRepo;

	@Autowired
	private EmailConfigService emailConfigService;

	@Autowired
	private CommonRepository commonRepository;

	@Autowired
	private SystemService systemService;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}
	@Autowired
	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}


	/**
	 * 去掉空格、回车、换行符、制表符
	 * @return
	 */
	public static String replaceAllBlank(String str) {
			String dest = "";
		        if (str!=null) {
		        	java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\s*|\t|\r|\n");
		            java.util.regex.Matcher m = p.matcher(str);
		            dest = m.replaceAll("");
		        }
		        dest = dest.replace("\n", "").replace("\t","").replace("\r","").replace(" ", "");
		        return dest;
		    }
	/**
	 * 去掉收尾空格、回车、换行符
	 * @return
	 */
	public static String replaceBlank(String str) {
		str = str.trim();
		str = str.replace("\n", "").replace("\t","").replace("\r","");
		while (str.startsWith("　")) {//这里判断是不是全角空格
			str = str.substring(1, str.length()).trim();
			}
			while (str.endsWith("　")) {
				str = str.substring(0, str.length() - 1).trim();
			}
		return str;
	}
	/**
	 * 根据业务部门OP的所有同级部门id
	 * @return
	 */
	public List<TSDepart> getEquativeDepart(){
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSDepart depart = systemService.findUniqueByProperty(TSDepart.class,"id",user.getDepartid());
		List<TSDepart> departlist = systemService.findHql("from TSDepart t where t.TSPDepart.id=?", new Object[]{depart.getTSPDepart().getId()});
		return departlist;
	}

	/**
	 * 得到权限
	 * 加逻辑
	 * 得到权限名 getdeclareStatus or getUserRoleCode
	 * @return
	 */
	public Integer getUserRoleCold() {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Integer cord = null;
		if(null!=user){
			List<TSRoleUser> tsRoleList =commonService.findHql("from TSRoleUser t where t.TSUser.id=?", new Object[]{user.getId()});//id,得到TSrole对象
			for(TSRoleUser ts:tsRoleList ){
				if(ts.getTSRole().getRoleCode().equals("t_input")){
					cord = 1;
				}else if (ts.getTSRole().getRoleCode().equals("t_report")) {
					cord = 2;
				}else if (ts.getTSRole().getRoleCode().equals("t_check")) {
					cord = 3;
				}else if (ts.getTSRole().getRoleCode().equals("t_control")){
					cord = 4;
				}else if(ts.getTSRole().getRoleCode().equals("t_input_m")){
					cord = 2;
				}else {
					cord = 1;
				}
			}
		}
		return cord;
	}
	public Integer getNameRoleCold(TSUser user) {
		Integer cord = null;
		if(null!=user){
			List<TSRoleUser> tsRoleList =commonService.findHql("from TSRoleUser t where t.TSUser.id=?", new Object[]{user.getId()});//id,得到TSrole对象
			for(TSRoleUser ts:tsRoleList ){
				if(ts.getTSRole().getRoleCode().equals("t_input")){
					cord = 1;
				}else if (ts.getTSRole().getRoleCode().equals("t_report")) {
					cord = 2;
				}else if (ts.getTSRole().getRoleCode().equals("t_check")) {
					cord = 3;
				}else if (ts.getTSRole().getRoleCode().equals("t_control")){
					cord = 4;
				}else if (ts.getTSRole().getRoleCode().equals("t_input_m")){
					cord = 5;
				}else {
					cord = 1;
				}
			}
		}
		return cord;
	}
	/**
	 * 得到录入者 IntputerName
	 * @return
	 */
	public String getIntputerName(EmployeeInfoEntity employeeInfo ) {
		TSBaseUser input = systemService.findUniqueByProperty(TSBaseUser.class,"userName", employeeInfo.getInputerId());
		if(input == null||input.getRealName()==null){
			return null;
		}
		return input.getRealName();
	}
	/**
	 * 得到申报者 reporterName
	 * @return
	 */
	public String getReporterName(EmployeeInfoEntity employeeInfo ) {
		TSBaseUser report = systemService.getEntity(TSBaseUser.class, employeeInfo.getReporterId());
		if(report == null||report.getRealName()==null){
			return null;
		}
		return report.getRealName();
	}

	/**
	 * 得到当前用户处理中状态码
	 * @return
	 */
	public Integer getStatus(TSUser user) {
		Integer declareStatus = 0;
		Integer role = getNameRoleCold(user);
		Integer addRole = 0;
		TSDepart depart = commonService.getEntity(TSDepart.class, user.getDepartid());
		if(role == 1)
			addRole = 2;
		Integer lv = commonRepository.getDepartGread(depart);
		if (lv == 1) {
			declareStatus = 4;
		}else{
			declareStatus = lv*2+addRole;
		}
		return declareStatus;
	}

	/**
	 * 批量申报
	 * @return
	 */
/*	public Map<String, Object> batchDeclare() {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		//将当前用户部门下的所有本月待申报的数据加载出来
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(null!=user){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});//用户信息
        	String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			if(null==departId){
				result.put("errMsg", "当前登录用户："+user.getUserName()+"所在部门为空。");
				result.put("errCode", -1);
			}else{
				//加载录入未申报数据
				List<EmployeeInfoEntity> employeeInfoList = employeeInfoRepo.findByDeclareStatusTsUserId(departId,new Integer[]{1,5},user.getId());
				if(!employeeInfoList.isEmpty()){
					updateDeclareStatus(user,employeeInfoList, 2);
					Map<String,String> tplContent = new HashMap<String,String>();
					tplContent.put("subject", "申报通过通知");
					tplContent.put("content", "员工：【"+extractEmployeeNameInfo(employeeInfoList)+"】申报已通过，请及时处理");
					//发送邮件
					emailConfigService.dictEmailNotice("commonAppr", tplContent);
				}else{
					result.put("errMsg", "没有需要申报的数据。");
					result.put("errCode", -1);
				}
			}
		}else{
			String errMsg = "当前用户未登录，无法进行申报操作。";
			log.info(errMsg);
			result.put("errMsg", errMsg);
			result.put("errCode", -1);
		}
		return result;
	}*/

	public Map<String, Object> batchDeclare() {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		//将当前用户部门下的所有本月待申报的数据加载出来
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(null!=user){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});//用户信息
        	String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			if(null==departId){
				result.put("errMsg", "当前登录用户："+user.getUserName()+"所在部门为空");
				result.put("errCode", -1);
			}else{
				//加载录入未申报数据
				List<EmployeeInfoEntity> employeeInfoList = employeeInfoRepo.findByDeclareStatusTsUserId(departId,new Integer[]{1,5},user.getId());
				if(!employeeInfoList.isEmpty()){
					updateDeclareStatus(user,employeeInfoList, 2);
					Map<String,String> tplContent = new HashMap<String,String>();
					tplContent.put("subject", "申报通过通知");
					tplContent.put("content", "员工：【"+extractEmployeeNameInfo(employeeInfoList)+"】申报已通过，请及时处理。");
					//发送邮件
					emailConfigService.dictEmailNotice("commonAppr", tplContent);
				}else{
					result.put("errMsg", "没有需要申报的数据");
					result.put("errCode", -1);
				}
			}
		}else{
			String errMsg = "当前用户未登录，无法进行申报操作";
			log.info(errMsg);
			result.put("errMsg", errMsg);
			result.put("errCode", -1);
		}
		return result;
	}
	/**
	 * 入保退保工作
	 * j-8-1
	 * @return
	 */
	public Map<String, Object> setInsuranceChange(List<Integer> id,String isId) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		result.put("errMsg", "操作成功");
		if(getUserRoleCold()==3){
			List<EmployeeInfoEntity> employeeInfoList = employeeInfoRepo.findByDeclareStatusId(id);
			if("1".equals(isId)){
				for(EmployeeInfoEntity emp:employeeInfoList){
					emp.setInsurance(1);
					commonService.saveOrUpdate(emp);
				}
			}else if("2".equals(isId)){
				for(EmployeeInfoEntity emp:employeeInfoList){
					emp.setInsurance(3);
					commonService.saveOrUpdate(emp);
				}
			}else{
				result.put("errCode", -1);
				result.put("errMsg", "无效操作，请重试");
			}
		}else{
			result.put("errCode", -1);
			result.put("errMsg", "当前用户没有进行此操作的权利");
		}
		return result;
	}


	/**
	 * 申报和通过ID id,部门 departId，离职状态 delFlg
	 * j-5-8
	 * @return
	 */

	public Map<String, Object> declareAndPass(List<Integer> id) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		StringBuffer name = new StringBuffer();
		//将当前用户部门下的所有本月待申报的数据加载出来
		List<EmployeeInfoEntity> employeeInfoList = null;
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(null!=user){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});//用户信息
        	String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			if(null==departId){
				result.put("errMsg", "当前登录用户："+user.getUserName()+"所在部门为空");
				result.put("errCode", -1);
			}else{
				employeeInfoList = employeeInfoRepo.findByDeclareStatusId(id);
				if(!employeeInfoList.isEmpty()){
					name = updateDeclareStatus(user,employeeInfoList);
					//Map<String,String> tplContent = new HashMap<String,String>();
					//tplContent.put("subject", "申报通过通知");
					//tplContent.put("content", "员工：【"+extractEmployeeNameInfo(employeeInfoList)+"】申报已通过，请及时处理");
					//发送邮件
					//emailConfigService.dictEmailNotice("commonAppr", tplContent);
				}else{
					result.put("errMsg", "没有需要的数据");
					result.put("errCode", -1);
				}
			}
		}else{
			String errMsg = "当前用户未登录，无法进行此操作";
			log.info(errMsg);
			result.put("errMsg", errMsg);
			result.put("errCode", -1);
		}
		if(0<name.length()){
			result.put("errMsg", "员工"+name+"在当前状态无法进行此操作");
			result.put("errCode", -1);
		}
		return result;
	}

	/**
	 * 新-退回-选中
	 * @param departId
	 * @param departName
	 * @param returnReason
	 * @return
	 */

	public Map<String, Object> newBatchReturn(List<Integer> id,String returnReason) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		//当前登录人员信息
		StringBuffer name= new StringBuffer();
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(null!=user){
			//Map<String,String> tplContent = new HashMap<String,String>();
			//加载申报未通过的数据
			List<EmployeeInfoEntity> employeeInfoList = employeeInfoRepo.findByDeclareStatusId(id);
			if(!employeeInfoList.isEmpty()){
				name =returnDeclareStatus(user,employeeInfoList,returnReason);
				/*for(TSDepart depart:departSet){
					tplContent.put("subject", "审批退回通知");
					tplContent.put("content", "员工：【"+extractEmployeeNameInfo(employeeInfoList)+"】审批未通过，退回理由："+returnReason);
					//发送邮件
					emailConfigService.departEmailNotice(depart.getId(), "empSyFlg", "2", tplContent);
				}*/
			}else{
				result.put("errMsg", "当前部门："+id.toString()+"没有需要退回的申报数据");
				result.put("errCode", -1);
			}
		}else{
			String errMsg = "当前用户未登录，无法进行退回操作";
			log.info(errMsg);
			result.put("errMsg", errMsg);
			result.put("errCode", -1);
		}
		if (0 < name.length()) {
			result.put("errMsg", "员工"+name+"在当前状态无法进行此操作");
			result.put("errCode", -1);
		}
		return result;
	}

	/**
	 * 批量退回
	 * @param departId
	 * @param departName
	 * @param returnReason
	 * @return
	 */
	public Map<String, Object> batchReturn(String departId,String returnReason) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		//当前登录人员信息
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(null!=user){
			Map<String,String> tplContent = new HashMap<String,String>();
			//加载申报未通过的数据
			List<EmployeeInfoEntity> employeeInfoList = employeeInfoRepo.findByDeclareStatus(departId,new Integer[]{2});
			if(!employeeInfoList.isEmpty()){
				updateDeclareStatus(user,employeeInfoList,5);
				Set<String> departSet = new HashSet<String>();
				for(int i = 0;i<employeeInfoList.size();i++){
					departSet.add(employeeInfoList.get(i).getDepartment());
				}
				for(String depart:departSet){
					tplContent.put("subject", "审批退回通知");
					tplContent.put("content", "员工：【"+extractEmployeeNameInfo(employeeInfoList)+"】审批未通过，退回理由："+returnReason);
					//发送邮件
					emailConfigService.departEmailNotice(depart, "empSyFlg", "2", tplContent);
				}
			}else{
				result.put("errMsg", "当前部门：【"+(StringUtils.isBlank(departId)?"所有部门":departId)+"】下没有需要退回的申报数据");
				result.put("errCode", -1);
			}
		}else{
			String errMsg = "当前用户未登录，无法进行退回操作";
			log.info(errMsg);
			result.put("errMsg", errMsg);
			result.put("errCode", -1);
		}
		return result;
	}

	/**
	 * 导出
	 * 修改j-5-11
	 * @param status
	 * @param delFlg
	 * @param declareStartDate
	 * @param declareEndDate
	 * @param departId
	 * @return
	 */
	public Map<String,Object> exportExcelDatas(List<EmployeeInfoEntity> employeeInfoList){
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Map<String,Object> result = new HashMap<String,Object>();
		//读取excel模板
		File excelFile=new File(EmployeeDeclareService.class.getResource("/")
				.getPath()+"excel-template/employee2.xlsx");
		if(!excelFile.exists()){
			return null;
		}
		InputStream is=null;
		Workbook wb=null;
		try {
			is = new FileInputStream(excelFile);
			wb=new XSSFWorkbook(is);
			Sheet sheet=wb.getSheetAt(0);
			if(!employeeInfoList.isEmpty()){
				for(int i = 0;i<employeeInfoList.size();i++){
					Row oldRow = sheet.getRow(i + 1);
                    Row row = sheet.getRow(i + 2);
					if(null==row){
						row=sheet.createRow(i+2);
					}
					row.setRowStyle(oldRow.getRowStyle());
					short lastCellNum = oldRow.getLastCellNum();
                    for(int j = 1 ;j < lastCellNum;j++) {
                    	Cell createCell = row.createCell(j);
                    	Cell cell = oldRow.getCell(j);
                    	CellStyle cellStyle = wb.createCellStyle();
                    	cellStyle.cloneStyleFrom(cell.getCellStyle());
//                    	CellStyle cellStyle = cell.getCellStyle();
                    	DataFormat format = wb.createDataFormat();
//                    	//poi 设置excel单元格样式封装 样式"($#,##0.00_)"
                    	cellStyle.setDataFormat(format.getFormat("#,##0.00"));
                    	if(j==8||j==9||j==10||j==13||j==15||j==17){
                    		cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    	}
                    	createCell.setCellStyle(cellStyle);
                    }
					EmployeeInfoEntity employeeInfo = employeeInfoList.get(i);
					row.getCell(1).setCellValue(objConvertString(employeeInfo.getCode()));
					row.getCell(2).setCellValue(objConvertString(employeeInfo.getName()));
					TSDepart department = systemService.getEntity(TSDepart.class, employeeInfo.getDepartment());
					row.getCell(3).setCellValue(objConvertString(department.getDepartname()));
					String empFlgStr = "";
					if(null!=employeeInfo.getEmployeeFlag()){
						if("0".equals(employeeInfo.getEmployeeFlag().toString())){
							empFlgStr = "TECH";
						}else if("1".equals(employeeInfo.getEmployeeFlag().toString())){
							empFlgStr = "OP";
						}
					}
					row.getCell(4).setCellValue(empFlgStr);
					TSBaseUser inputer = systemService.findUniqueByProperty(TSBaseUser.class,"userName",employeeInfo.getInputerId());
					row.getCell(5).setCellValue(objConvertString(inputer.getRealName()));
					if(!(getNameRoleCold(user)==3&&employeeInfo.getEmployeeFlag()==1)){
						row.getCell(6).setCellValue(objConvertString(employeeInfo.getCmbAccount()));
						row.getCell(7).setCellValue(objConvertString(employeeInfo.getIcbcAccount()));
						row.getCell(8).setCellValue(formatPrice(employeeInfo.getAStandardSalary()));
						row.getCell(9).setCellValue(formatPrice(employeeInfo.getBasePay()));
						if(StringUtil.isNotEmpty(employeeInfo.getMeritPay())&&employeeInfo.getMeritPay()>0){
							row.getCell(10).setCellValue(formatPrice(employeeInfo.getMeritPay()));
						}
						row.getCell(11).setCellValue(employeeInfo.getDiscount()+"%");
						row.getCell(14).setCellValue(objConvertString(employeeInfo.getA1Place()));
						row.getCell(15).setCellValue(formatPrice(employeeInfo.getA1Payment()));
						if(StringUtil.isNotEmpty(employeeInfo.getA2Payment())&&employeeInfo.getA2Payment()!=0){
							row.getCell(16).setCellValue(objConvertString(employeeInfo.getA2Place()));
							row.getCell(17).setCellValue(formatPrice(employeeInfo.getA2Payment()));
						}
					}
					row.getCell(12).setCellValue(objConvertString(employeeInfo.getSixGoldCity()));
					row.getCell(13).setCellValue(formatPrice(employeeInfo.getSixGoldBase()));
					row.getCell(18).setCellValue(objConvertString(employeeInfo.getContactWay()));
					String houseStr = "";
					if(null!=employeeInfo.getHouseholdRegistration()){
						if("0".equals(employeeInfo.getHouseholdRegistration().toString())){
							houseStr = "本市农业";
						}else if("1".equals(employeeInfo.getHouseholdRegistration().toString())){
							houseStr = "本市城镇";
						}else if("2".equals(employeeInfo.getHouseholdRegistration().toString())){
							houseStr = "外埠农业";
						}else if("3".equals(employeeInfo.getHouseholdRegistration().toString())){
							houseStr = "外埠城镇";
						}
					}
					row.getCell(19).setCellValue(houseStr);
					String sexStr = "";
					if(null!=employeeInfo.getGender()){
						if("0".equals(employeeInfo.getGender().toString())){
							sexStr = "男";
						}else if("1".equals(employeeInfo.getGender().toString())){
							sexStr = "女";
						}
					}
					row.getCell(20).setCellValue(sexStr);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					if(null!=employeeInfo.getEntryDate()){
						row.getCell(21).setCellValue(sdf.format(employeeInfo.getEntryDate()));
					}
					if(StringUtil.isNotEmpty(employeeInfo.getQuitDate())){
						row.getCell(22).setCellValue(sdf.format(employeeInfo.getQuitDate()));
					}
					//row.getCell(18).setCellValue(objConvertString(employeeInfo.getQuitReason()));
				}
			}
		} catch (Exception e) {
			log.info(e.getMessage(),e);
		} finally {
			if(null!=is){
				try {
					is.close();
				} catch (IOException e) {
					log.error(e.getMessage(),e);
				}
			}
		}
		result.put("wb", wb);
		result.put("fileName", "员工信息表"+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
		return result;
	}


	/**
	 * 格式化价格
	 * @return
	 */
	public String formatPrice(Double price){
		String pStr = "";
		if(null!=price){
			pStr = price+"";
			if(pStr.substring(pStr.indexOf(".")+1).length()==1){
				pStr = pStr+"0";
			}
		}
		return pStr;
	}

	/**
	 * 格式化日期
	 * @return
	 */
	public String formatDate(Date date,String fmt){
		String dateStr = "";
		if(null!=date){
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			dateStr = sdf.format(date);
		}
		return dateStr;
	}

	/**
	 * 对象转字符串
	 * @param obj
	 * @return
	 */
	public String objConvertString(Object obj){
		if(null!=obj){
			return obj.toString();
		}
		return "";
	}
	/**
	 * 更新申报状态
	 * @param user
	 * @param declareStatus
	 */
	@Transactional(readOnly=false)
	public void updateDeclareStatus(TSUser user,List<EmployeeInfoEntity> employeeInfoList,Integer changeStatus) {
		//批量更新状态
		for(int i = 0;i<employeeInfoList.size();i++){
			EmployeeInfoEntity employeeInfo = employeeInfoList.get(i);
			employeeInfo.setDeclareStatus(changeStatus);
			employeeInfo.setLastModifiedDate(new Date());
			employeeInfoRepo.update(employeeInfo);
		}
	}
	/**
	 * 判断操作状态
	 * @param user
	 * @param declareStatus
	 */
	public boolean getUpdate(EmployeeInfoEntity employeeInfo) {
		Integer role = getUserRoleCold();
		if((employeeInfo.getDeclareStatus()==1||employeeInfo.getDeclareStatus()==2)&&role==1){
			return true;
		}else if((employeeInfo.getDeclareStatus()==3||employeeInfo.getDeclareStatus()==4)&&role==2){
			return true;
		}else if(employeeInfo.getDeclareStatus()==5&&role==4){
			return true;
		}else if((employeeInfo.getDeclareStatus()==1||employeeInfo.getDeclareStatus()==2)&&role==2&&employeeInfo.getEmployeeFlag()==1){
			return true;
		}
		return false;
	}

	/**
	 * 更新申报状态
	 * 5-9 j
	 * 1+2 / 2+1
	 * @param user
	 * @param declareStatus
	 */
	@Transactional(readOnly=false)
	public StringBuffer updateDeclareStatus(TSUser user,List<EmployeeInfoEntity> employeeInfoList) {
		//批量更新状态
		StringBuffer errorMassage = new StringBuffer();
		String insuranceInputer = new String();
		Integer newchangeStatus = null;
		List<String> email = new ArrayList<>(); //员工入职报告email
		List<String> email2 = new ArrayList<>(); //员工薪资调整email
		List<EmployeeInfoEntity> insuranceMassage = new ArrayList<>(); //入保员工信息
		Boolean insurance = false;
		for(int i = 0;i<employeeInfoList.size();i++){
			EmployeeInfoEntity employeeInfo = employeeInfoList.get(i);
//			if(getUpdate(employeeInfo)){
			if(((employeeInfo.getDeclareStatus())%2)!=0 ){
				newchangeStatus= employeeInfo.getDeclareStatus()-1;
			}else{
				newchangeStatus= employeeInfo.getDeclareStatus()-2;
			}
			if(newchangeStatus==2&&employeeInfo.getQuitStatus()==0){
				employeeInfo.setQuitStatus(1);
				employeeInfo.setChangeFlag(0);
				if(null==employeeInfo.getInsurance()||!StringUtil.isNotEmpty(employeeInfo.getInsurance())){
					employeeInfo.setInsurance(0);
					insurance = true;
				}
			}
			employeeInfo.setDeclareStatus(newchangeStatus);
			employeeInfo.setLastModifiedDate(new Date());
			if(getUserRoleCold().equals(2)){
				employeeInfo.setReporterId(user.getUserName());
			}else if(getUserRoleCold().equals(4)){
				employeeInfo.setControllerId(user.getUserName());
			}
			employeeInfo.setLastModifiedBy(user.getUserName());
			employeeInfo.setLastModifiedDate(new Date());
			employeeInfo.setLoseReason(null);
			employeeInfoRepo.update(employeeInfo);
			if(newchangeStatus==2){
				if(employeeInfo.getChangeFlag()==1){
					addEmployeeInfo(employeeInfo);
				}else if (employeeInfo.getChangeFlag()==3) {
					changeEmployeeInfo(employeeInfo);
				}else if(employeeInfo.getChangeFlag()==0&&null!=employeeInfo.getChangeDate()){
					addEmployeeInfo(employeeInfo);
				}
			}
			if(insurance){
				insuranceMassage.add(employeeInfo);
				TSUser user2 = commonService.findUniqueByProperty(TSUser.class, "userName",employeeInfo.getInputerId());
				if(insuranceInputer.indexOf(user2.getEmail())==-1){
					if(insuranceInputer.length()>0)
						insuranceInputer=insuranceInputer+",";
					insuranceInputer=insuranceInputer+user2.getEmail();
				}
			}
			if(newchangeStatus!=2)	{
				TSUser inputNow = new TSUser();
				if(newchangeStatus!=4){
					inputNow = commonRepository.getManagerNow((newchangeStatus+1)/2, employeeInfo.getDepartment(), employeeInfo.getInputerId());
					if(employeeInfo.getQuitStatus()==1){
						email2.add(inputNow.getEmail());
					}else{
						email.add(inputNow.getEmail());
					}
				}else{
					List<TSRoleUser> controller = commonService.findHql("from TSRoleUser t where t.TSRole.id=?", new Object[]{"2c934f26633da93b01633dadbdd80009"});
					String checkerEmail = new String();
					for(TSRoleUser ru:controller){
						if(ru.getTSUser().getDeleteFlag()==0&&ru.getTSUser().getStatus()==1){
							inputNow = ru.getTSUser();
							if(employeeInfo.getQuitStatus()==1){
								email2.add(inputNow.getEmail());
							}else{
								email.add(inputNow.getEmail());
							}
						}
					}
				}
			}
		}
		if(!email.isEmpty()){
			email = new ArrayList<>(new HashSet<>(email));
			Map<String, String>oneMsg = new HashMap<>();
			oneMsg.put("subject", "员工入职报告");
			oneMsg.put("content", "<br>员工入职已上报，请及时处理。<br><br>< 收支运营 SaaS >");
			for(String em:email){
				emailConfigService.mailSend(em,oneMsg);
			}
		}
		if(!email2.isEmpty()){
			email2 = new ArrayList<>(new HashSet<>(email2));
			Map<String, String>oneMsg = new HashMap<>();
			oneMsg.put("subject", "员工薪资调整申报");
			oneMsg.put("content", "<br>员工薪资调整已申报，请及时处理。<br><br>< 收支运营 SaaS >");
			for(String em:email2){
				emailConfigService.mailSend(em,oneMsg);
			}
		}
		if(!insuranceMassage.isEmpty()){
			Map<String,String> dictMap2 = new HashMap<>();
			String info = "<br>请办理入保手续。<br>";
			for(EmployeeInfoEntity emp:insuranceMassage ){
				if(info.indexOf(commonRepository.placeToCompany(emp.getA1Place()))==-1){
					info =info+"<br>公司："+commonRepository.placeToCompany(emp.getA1Place());
				}
			}
			dictMap2.put("subject", "员工入保通知");
			dictMap2.put("content", info+"<br><br>< 收支运营 SaaS >");
			emailConfigService.mailSend(dictMap2);
		}
		return errorMassage;
	}


	/**
	 * 编辑修改后申报补充逻辑
	 * j 7-12
	 * @return
	 */
	public void addEmployeeInfo(EmployeeInfoEntity employeeInfo){
		if(employeeInfo.getChangeDate()!=null&&StringUtil.isNotEmpty(employeeInfo.getChangeFlag())){
			if(employeeInfo.getChangeDate().after(employeeInfo.getEffectiveDate())){
				EmployeeInfoEntity t = new EmployeeInfoEntity();
				employeeInfo.setExpiryDate(employeeInfo.getChangeDate());
				employeeInfo.setChangeFlag(2);
				systemService.saveOrUpdate(employeeInfo);
				employeeInfo.setId(null);
				t = copyEmployeeInfo(employeeInfo, t);
				t.setChangeFlag(0);
				systemService.save(t);
			}else{
				employeeInfo = copyEmployeeInfo(employeeInfo, employeeInfo);
				employeeInfo.setChangeFlag(0);
				systemService.saveOrUpdate(employeeInfo);
			}
		}else{
			employeeInfo.setChangeFlag(0);
			systemService.saveOrUpdate(employeeInfo);
		}
	}
	public void changeEmployeeInfo(EmployeeInfoEntity employeeInfo){
		if(StringUtil.isNotEmpty(employeeInfo.getChangeDate())){
			if(employeeInfo.getChangeDate().after(employeeInfo.getEffectiveDate())){
				List<EmployeeInfoEntity> allEntity = systemService.findByProperty(EmployeeInfoEntity.class, "code", employeeInfo.getCode());
				EmployeeInfoEntity t = new EmployeeInfoEntity();
				for(EmployeeInfoEntity emp:allEntity){
					if(emp.getEffectiveDate().equals(employeeInfo.getExpiryDate()))
						t = emp;
				}
				if(StringUtil.isNotEmpty(t)){
					employeeInfo.setExpiryDate(employeeInfo.getChangeDate());
					employeeInfo.setChangeFlag(2);
					systemService.saveOrUpdate(employeeInfo);
					Integer a = employeeInfo.getId();
					employeeInfo.setId(null);
					t = copyEmployeeInfo(employeeInfo, t);
					employeeInfo.setId(a);
					t.setChangeFlag(0);
					employeeInfoRepo.update(t);
				}
			}else{
				List<EmployeeInfoEntity> allEntity = systemService.findByProperty(EmployeeInfoEntity.class, "code", employeeInfo.getCode());
				EmployeeInfoEntity t = new EmployeeInfoEntity();
				for(EmployeeInfoEntity emp:allEntity){
					if(emp.getEffectiveDate().equals(employeeInfo.getExpiryDate()))
						t = emp;
				}
				if(StringUtil.isNotEmpty(t)){
					systemService.delete(t);
				}
				employeeInfo = copyEmployeeInfo(employeeInfo, employeeInfo);
				employeeInfo.setChangeFlag(0);
				systemService.saveOrUpdate(employeeInfo);
			}
		}else {
			List<EmployeeInfoEntity> allEntity = systemService.findByProperty(EmployeeInfoEntity.class, "code", employeeInfo.getCode());
			for(EmployeeInfoEntity emp:allEntity){
				if(emp.getEffectiveDate().equals(employeeInfo.getExpiryDate()))
					systemService.delete(emp);
			}
			GregorianCalendar gc=new GregorianCalendar();
			gc.setTime(employeeInfo.getEffectiveDate());
			gc.add(1, 15);
			gc.set(GregorianCalendar.DAY_OF_MONTH, 0);
			employeeInfo.setExpiryDate(gc.getTime());
			employeeInfo.setChangeFlag(0);
			systemService.saveOrUpdate(employeeInfo);
		}
	}
	/**
	 * 拷贝一条新员工信息数据
	 * j 7-12
	 * @return
	 */
	public EmployeeInfoEntity copyEmployeeInfo(EmployeeInfoEntity oldEntity,EmployeeInfoEntity newEntity){
		try {
			MyBeanUtils.copyBeanNotNull2Bean(oldEntity,newEntity);
			newEntity.setA2Payment(Math.round((oldEntity.getAStandardSalaryCh()-oldEntity.getA1PaymentCh())*100)/100.0);
			newEntity.setA2PaymentCh(null);
			newEntity.setAStandardSalary(oldEntity.getAStandardSalaryCh());
			newEntity.setAStandardSalaryCh(null);
			newEntity.setA1Payment(oldEntity.getA1PaymentCh());
			newEntity.setA1PaymentCh(null);
			newEntity.setA1Place(oldEntity.getA1PlaceCh());
			newEntity.setA1PlaceCh(null);
			newEntity.setA2Place(oldEntity.getA2PlaceCh());
			newEntity.setA2PlaceCh(null);
			Double b = Double.parseDouble(commonRepository.getSystemBasePay());
			newEntity.setBasePay(b);
			newEntity.setMeritPay((newEntity.getAStandardSalary()-b)*100/100.0);
			if(newEntity.getMeritPay()<0) {newEntity.setMeritPay((double)0);}
			newEntity.setSixGoldCity(oldEntity.getSixGoldCityCh());
			newEntity.setSixGoldCityCh(null);
			newEntity.setSixGoldBase(oldEntity.getSixGoldBaseCh());
			newEntity.setSixGoldBaseCh(null);
			newEntity.setEffectiveDate(oldEntity.getChangeDate());
			GregorianCalendar gc=new GregorianCalendar();
			gc.setTime(oldEntity.getChangeDate());
			gc.add(1, 15);
			gc.set(GregorianCalendar.DAY_OF_MONTH, 0);
			newEntity.setExpiryDate(gc.getTime());
			newEntity.setChangeDate(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newEntity;
	}

	/**
	 * 清空员工薪酬变动
	 * j 7-12
	 * @return
	 */
	public void changeInfoEmpty(EmployeeInfoEntity employeeInfo){
		employeeInfo.setA2PaymentCh(null);
		employeeInfo.setA2PlaceCh(null);
		employeeInfo.setAStandardSalaryCh(null);
		employeeInfo.setA1PaymentCh(null);
		employeeInfo.setA1PlaceCh(null);
		employeeInfo.setSixGoldBaseCh(null);
		employeeInfo.setSixGoldCityCh(null);
		employeeInfo.setChangeDate(null);
		if(StringUtil.isNotEmpty(employeeInfo.getChangeFlag())){
			changeEmployeeInfo(employeeInfo);
		}
	}


	/**
	 * 更新入职状态
	 * 5-9 j
	 * 1+2 / 2+1
	 * @param user
	 * @param declareStatus
	 *
	 */
	public Map<String, Object> updateSucced(List<EmployeeInfoEntity> employeeInfoList){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("errCode", 0);
		StringBuffer name = new StringBuffer();
		if(!employeeInfoList.isEmpty()){
			for (int i = 0; i < employeeInfoList.size(); i++) {
				EmployeeInfoEntity employeeInfo = employeeInfoList.get(i);
				if (employeeInfo.getDeclareStatus().equals(7)){
					employeeInfo.setDeclareStatus(8);
					employeeInfo.setQuitStatus(1);
					employeeInfoRepo.update(employeeInfo);
				}else{
					name.append(employeeInfo.getName()+"，");
				}
			}
		}else {
			result.put("errMsg", "没有需要入职的数据");
			result.put("errCode", -1);
		}
		if (0 < name.length()) {
			result.put("errMsg", "员工"+name+"在当前状态无法进行此操作");
			result.put("errCode", -1);
		}
		return result;
	}

	/**
	 * 更新退回状态
	 * 5-9 j
	 * 3-1 / 4-2
	 * @param user
	 * @param declareStatus
	 *
	 */
	@Transactional(readOnly=false)
	public StringBuffer returnDeclareStatus(TSUser user,List<EmployeeInfoEntity> employeeInfoList,String returnReason) {
		//批量更新状态
		StringBuffer errorMassage = new StringBuffer();
		String succeedName = new String();
		Integer newchangeStatus = null;
		List<TSUser> email = new ArrayList<>();
		Map<String, String> nameToEmail =new HashMap<>();
		for(int i = 0;i<employeeInfoList.size();i++){
				EmployeeInfoEntity employeeInfo = employeeInfoList.get(i);
				TSUser input = systemService.findUniqueByProperty(TSUser.class, "userName", employeeInfo.getInputerId());
				//if(getUpdate(employeeInfo)){
					if(user.getUserName().equals(employeeInfo.getInputerId())){
					}else{
						if(!employeeInfo.getDeclareStatus().equals(2)){
							if(((employeeInfo.getDeclareStatus())%2)==0 ){
								newchangeStatus= employeeInfo.getDeclareStatus()+1;
							}else{
								newchangeStatus= employeeInfo.getDeclareStatus()+2;
							}
							employeeInfo.setDeclareStatus(newchangeStatus);
							employeeInfo.setLoseReason(returnReason);
							employeeInfo.setLastModifiedDate(new Date());
							if(user.getUserName().equals(employeeInfo.getInputerId()))
								employeeInfo.setDeclareStatus(getStatus(user)-1);
							employeeInfoRepo.update(employeeInfo);
						}
					}
			TSUser inputNow = commonRepository.getManagerNow((newchangeStatus+1)/2, employeeInfo.getDepartment(), employeeInfo.getInputerId());
			email.add(inputNow);
			succeedName=nameToEmail.get(inputNow.getEmail());
			String custname = "";
			if(StringUtil.isNotEmpty(employeeInfo.getCustomerId())){
				CustomerInfoEntity cust = commonService.getEntity(CustomerInfoEntity.class, employeeInfo.getCustomerId());
				if(StringUtil.isNotEmpty(cust)){
					custname = cust.getCode();
				}
			}
			if(succeedName!=null&&StringUtil.isNotEmpty(succeedName)){
				succeedName = succeedName+"姓名："+employeeInfo.getName()+"<br>";
				succeedName = succeedName +"顾客："+custname+"<br>";
				succeedName = succeedName+"拒绝理由："+returnReason+"<br><br>";
			}else {
				succeedName = "<br>员工入职已被拒绝，请及时处理。<br><br>";
				succeedName = succeedName+"姓名："+employeeInfo.getName()+"<br>";
				succeedName = succeedName+"顾客："+custname+"<br>";
				succeedName = succeedName+"拒绝理由："+returnReason+"<br><br>";
			}
			nameToEmail.put(inputNow.getEmail(), succeedName);
		}
		email = new ArrayList<>(new HashSet<>(email));
		Map<String,Map<String, String>> dictMap = new HashMap<>();
		for(TSUser em:email){
			Map<String, String>oneMsg = new HashMap<>();
			String toName = nameToEmail.get(em.getEmail());
			oneMsg.put("subject", "员工信息拒绝通知");
			oneMsg.put("content", nameToEmail.get(em.getEmail())+"< 收支运营 SaaS >");
			dictMap.put(em.getEmail(), oneMsg);
		}
		emailConfigService.mailSendAll2(dictMap);
		return errorMassage;
	}

	/**
	 * 离职定时器，每日0点
	 * 8-6 j
	 * @return
	 * @param returnReason
	 */
	@Scheduled(cron="0 0 0 1/1 * ? ")
	public void leaveTimes(){
		org.jeecgframework.core.util.LogUtil.info("===========定时任务离职状态变更处理=============");
		String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Date nowDate = new Date();
		try {
			nowDate =new SimpleDateFormat("yyyy-MM-dd").parse(now);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<EmployeeInfoEntity> employeeInfos = commonService.findByProperty(EmployeeInfoEntity.class, "quitDate",nowDate);
		for(EmployeeInfoEntity emp:employeeInfos){
			emp.setDeclareStatus(1);
			emp.setQuitStatus(1);
			commonService.saveOrUpdate(emp);
		}
		org.jeecgframework.core.util.LogUtil.info("===========定时任务离职状态变更处理完成=============");
	}
	/**
	 * 如退保提醒邮件定时器，每周1-5，每天7点
	 * 8-6 j
	 * @return
	 * @param returnReason
	 */
	//@Scheduled(cron="0 0 7 ? * 1-5")
	public void dailyReminder(){
		org.jeecgframework.core.util.LogUtil.info("===========每日提醒任务定时器=============");

		org.jeecgframework.core.util.LogUtil.info("===========每日提醒任务定时器=============");
	}

	/**
	 * 提取员工名字信息
	 * @param employeeDeclareList
	 * @return
	 */
	public String extractEmployeeNameInfo(List<EmployeeInfoEntity> employeeInfoList){
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<employeeInfoList.size();i++){
			if(i == 0){
				sb.append(employeeInfoList.get(i).getName());
			}else{
				sb.append(",").append(employeeInfoList.get(i).getName());
			}
		}
		return sb.toString();
	}


	/**
	 * 邮箱发送
	 * @param toEmail
	 * @param subject
	 * @param msg
	 */
/*	@Transactional(readOnly=false)
	public void mailSend(String toEmail,String subject,String msg) {
		String hql = "from EmailConfigEntity t where t.sendCount < t.maxCount";
		//加载邮箱配置信息
		List<EmailConfigEntity> emailConfigList = commonService.findHql(hql, new Object[]{});
		if(!emailConfigList.isEmpty()){
			EmailConfigEntity emailConfig = emailConfigList.get(0);
			//创建邮件发送服务器
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost(emailConfig.getHost());
			mailSender.setPort(emailConfig.getPort());
			mailSender.setUsername(emailConfig.getAccount());
			mailSender.setPassword(emailConfig.getPassword());
			//创建邮件内容
			SimpleMailMessage message=new SimpleMailMessage();
			message.setFrom(emailConfig.getMailFrom());
			message.setTo(toEmail);
			message.setSubject(subject);
			message.setText(msg);
			//发送邮件
			mailSender.send(message);
			//更新邮箱发送次数+1
			emailConfig.setSendCount(emailConfig.getSendCount()+1);
			emailConfigRepo.saveOrUpdate(emailConfig);
		}else{
			log.info("未找到邮箱服务器配置信息");
		}
	}
*/

	/**
	 * 获取全部数据
	 * j 5-8
	 * @param employeeDeclare
	 * @param dataGrid
	 * @param parameterMap
	 */
	public void setDataGridAll(EmployeeInfoEntity employeeInfo, Map<String,String[]> parameterMap,DataGrid dataGrid,Integer declareStatus){
		CriteriaQuery cq = new CriteriaQuery(EmployeeInfoEntity.class,dataGrid);
		if(StringUtil.isNotEmpty(declareStatus)){
			cq.ge("declareStatus", declareStatus);
		}
		cq.eq("delFlg", 0);
		cq.notEq("quitStatus", 2);
		cq.gt("expiryDate", new Date());
		cq.le("effectiveDate", new Date());
		if(StringUtil.isNotEmpty(employeeInfo.getDepartment())){
			String depatrId = employeeInfo.getDepartment();
			employeeInfo.setDepartment(null);
			TSDepart depart = commonService.getEntity(TSDepart.class,depatrId);
			List<TSDepart > departs = commonRepository.subdivision(depart);
			String[] departIds = new String[departs.size()];
			int i = 0;
			for(TSDepart dp : departs){
				departIds[i] = dp.getId();
				i++;
			}
			cq.in("department",departIds);
		}
		//cq.addOrder("declareStatus", SortDirection.asc);
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, employeeInfo, parameterMap);
		commonService.getDataGridReturn(cq, true);
	}
	/**
	 * 获取全部外派数据
	 * j 5-11
	 * @param employeeDeclare
	 * @param dataGrid
	 * @param parameterMap
	 */
	public void setDataEmpFlag(EmployeeInfoEntity employeeInfo, Map<String,String[]> parameterMap,DataGrid dataGrid,Object[] declareStatus,Integer flag){
		CriteriaQuery cq = new CriteriaQuery(EmployeeInfoEntity.class,dataGrid);
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		cq.in("declareStatus", declareStatus);
		cq.eq("delFlg", 0);
		cq.eq("employeeFlag", flag);
		cq.gt("expiryDate", new Date());
		cq.le("effectiveDate", new Date());
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, employeeInfo, parameterMap);
		commonService.getDataGridReturn(cq, true);
	}


	/**
	 * 获取inputerId的数据
	 * j 5-11
	 * @param employeeDeclare
	 * @param dataGrid
	 * @param parameterMap
	 */
	public void setDataGridFlag(EmployeeInfoEntity employeeInfo, Map<String,String[]> parameterMap,DataGrid dataGrid,Integer flag,Integer declareStatus,Integer lv){
		CriteriaQuery cq = new CriteriaQuery(EmployeeInfoEntity.class,dataGrid);
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(StringUtil.isNotEmpty(declareStatus)){
			if(declareStatus>lv){
				cq.ge("declareStatus", declareStatus);
			}else if (declareStatus<lv){
				cq.between("declareStatus",5,declareStatus);
			}
		}
		cq.eq("delFlg", 0);
		//cq.eq("employeeFlag", flag);
		cq.eq("inputerId", user.getUserName());
		cq.eq("department", user.getDepartid());
		cq.gt("expiryDate", new Date());
		cq.le("effectiveDate", new Date());
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, employeeInfo, parameterMap);
		commonService.getDataGridReturn(cq, true);
	}
	/**
	 * 获取部门下的数据
	 * j 5-11
	 * @param employeeDeclare
	 * @param dataGrid
	 * @param parameterMap
	 */
	public void setDataGriddepart(EmployeeInfoEntity employeeInfo, Map<String,String[]> parameterMap,DataGrid dataGrid,Integer flag,Integer declareStatus,Integer lv){
		CriteriaQuery cq = new CriteriaQuery(EmployeeInfoEntity.class,dataGrid);
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(StringUtil.isNotEmpty(declareStatus)){
			if(declareStatus>lv){
				cq.ge("declareStatus", declareStatus);
			}else if(declareStatus<lv){
				cq.between("declareStatus",4,declareStatus);
			}
		}
		String depatrId = null;
		if(StringUtil.isNotEmpty(employeeInfo.getDepartment())){
			depatrId = employeeInfo.getDepartment();
			employeeInfo.setDepartment(null);
		}else{
			depatrId = user.getDepartid();
		}
		TSDepart depart = commonService.getEntity(TSDepart.class,depatrId);
		List<TSDepart > departs = commonRepository.subdivision(depart);
		String[] departIds = new String[departs.size()];
		int i = 0;
		for(TSDepart dp : departs){
			departIds[i] = dp.getId();
			i++;
		}
		cq.eq("delFlg", 0);
		cq.in("department",departIds);
		cq.gt("expiryDate", new Date());
		cq.le("effectiveDate", new Date());
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, employeeInfo, parameterMap);
		commonService.getDataGridReturn(cq, true);
	}

	/**
	 * 设置datagrid数据
	 * @param employeeDeclare
	 * @param parameterMap
	 * @param dataGrid
	 * @param orgIdList
	 * @param declareStatus
	 * @param delFlg
	 */


	public void setDataGrid(EmployeeInfoEntity employeeInfo, Map<String,String[]> parameterMap, DataGrid dataGrid,
			List<String> orgIdList,Object[] declareStatus,Integer delFlg) {
		CriteriaQuery cq = new CriteriaQuery(EmployeeInfoEntity.class, dataGrid);
		//cq.eq("delFlg", delFlg);
		cq.in("declareStatus", declareStatus);
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		//超级管理员、顶层管理员可以看到所有数据
		List<String> topManager = getTopManager();
		if(!"admin".equals(user.getUserName())&&!topManager.contains(user.getUserName())){
			List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        	String departId = currTSUserOrgList.size()>0?currTSUserOrgList.get(0).getTsDepart().getId():null;//只支持单部门
			if(null!=departId){
				orgIdList.add(departId);
			}else{
				log.info("当前登录用户："+user.getUserName()+"所在部门为空");
			}
		}
		if(!orgIdList.isEmpty()){
			cq.in("department", orgIdList.toArray(new String[orgIdList.size()]));

//			List<EmployeeInfoEntity> employeeList = employeeInfoRepo.findByDepartIds(orgIdList.toArray(new String[orgIdList.size()]));
//			if(!employeeList.isEmpty()){
//				Integer[] eids = new Integer[employeeList.size()];
//				for(int i = 0;i<employeeList.size();i++){
//					eids[i]=employeeList.get(i).getId();
//				}
//				cq.in("id",eids);
//			}else{
//				cq.eq("id",-99999);
//			}
		}
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, employeeInfo, parameterMap);
		commonService.getDataGridReturn(cq, true);
	}


	/**
	 * 获取顶层管理员
	 * @return
	 */
	public List<String> getTopManager(){
		List<String> topManagerList = new ArrayList<String>();
		List<TSTypegroup> tsTypeGroupList = commonService.findByProperty(TSTypegroup.class, "typegroupcode", "topManager");
		if(!tsTypeGroupList.isEmpty()&&!tsTypeGroupList.get(0).getTSTypes().isEmpty()){
			for(int i =0;i<tsTypeGroupList.get(0).getTSTypes().size();i++){
				TSType tsType = tsTypeGroupList.get(0).getTSTypes().get(i);
				//获取人员信息
				List<TSUser> tsuserList = commonService.findByProperty(TSUser.class,"userName",tsType.getTypecode());
				if(!tsuserList.isEmpty()){
					topManagerList.add(tsuserList.get(0).getUserName());
				}
			}
		}
		return topManagerList;
	}
}