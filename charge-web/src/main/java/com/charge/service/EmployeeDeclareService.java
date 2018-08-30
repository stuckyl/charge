package com.charge.service;

import com.charge.entity.CorporateInfoEntity;
import com.charge.entity.CustomerInfoEntity;
import com.charge.entity.EmployeeDeclareCopyEntity;
import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.ProjectCopyEntity;
import com.charge.entity.ProjectEntity;
import com.charge.entity.SixGoldEntity;
import com.charge.entity.SixGoldScaleEntity;
import com.charge.repository.AttendanceCalendarRepository;
import com.charge.repository.EmployeeDeclareRepository;
import com.charge.repository.EmployeeInfoRepository;
import com.charge.repository.CommonRepository;
import com.charge.repository.CorporateInfoRepository;
import com.charge.repository.SixGoldScaleRepository;
import com.charge.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wenst
 * @version v1.0
 * @Title: EmployeeDeclareService
 * @Description: 申报业务层
 * @date 2018年3月22日
 */
@Service
@Transactional
public class EmployeeDeclareService {
    private static final Logger log = Logger.getLogger(EmployeeDeclareService.class);

    @Autowired
    private EmployeeDeclareRepository employeeDeclareRepo;

    @Autowired
    private CommonService commonService;

    @Autowired
    private DictCategoryService dictCategoryService;

    @Autowired
    private AttendanceCalendarRepository attendanceCalendarRepo;

    @Autowired
    private SixGoldService sixGoldService;

    @Autowired
	private SystemService systemService;

	@Autowired
	private SixGoldScaleRepository sixGoldScaleRepository;

	@Autowired
	private CommonRepository jeecgRepo;

	@Autowired
	private CorporateInfoRepository corporateInfoRepository;

	@Autowired
	private EmailConfigService emailConfigService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private EmployeeDeclareCopyService employeeDeclareCopyService;

    /**
     * 根据月份生成数据
     *
     * @param departId
     * @param month
     */
    public void initDefalutData(String departId, String month, TSUser user) throws Exception {
        //获取当月可以结算工资的员工
    	List<EmployeeInfoEntity> employeeInfoList = null;
    	//将指定部门下 员工信息录入状态完成的员工查找出来。
    	employeeInfoList = employeeDeclareRepo.findByDeclareStatusAndByInputer(departId,new Integer[]{0,1},month,user.getId());
    	if(employeeInfoList==null || employeeInfoList.size()==0) {
    		ContextHolderUtils.getRequest().setAttribute("nullMsg", "本月目前没有可以生成收支报表的员工");
    		return;
    	}

    	//年月 转换为 date
        SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date te = sdft.parse(month);
        String str1 = sdf.format(te);
        Date date = sdf.parse(str1);

        List<SixGoldEntity> list = new ArrayList<SixGoldEntity>();
        List<String> warnMsg = (List<String>) ContextHolderUtils.getRequest().getAttribute("warnMsg");
        //先 获取 员工六金信息
    	for (int i = 0; i < employeeInfoList.size(); i++) {
    		EmployeeInfoEntity entity = employeeInfoList.get(i);
    		SixGoldEntity sixGoldEntity = sixGoldService.findByEmployeeCode(entity.getCode(), date);
    		if(sixGoldEntity == null || sixGoldEntity.getId() == null) {  //通知 inputer哪几个 员工六金未导入
    			//根据  地区六金基本表 生成 缺省六金
    			String sixGoldCity = entity.getSixGoldCity();
    			SixGoldScaleEntity sixGoldScale = sixGoldScaleRepository.findByCity(sixGoldCity);
    			if(sixGoldScale==null || sixGoldScale.getId() == null) {  // 地区六金信息也没有，报错
    				warnMsg.add(entity.getName());
    			}else {
    				sixGoldEntity = setUpDefautSixGold(sixGoldScale,0);
    			}
    		}
    		list.add(sixGoldEntity);
    	}

    	int success = 0;
    	//int repeat = 0;
        // 获取员工收入信息
        // 循环初始员工收入数据

    	//个税起征点
    	double perTaxBase = Double.parseDouble("3500");
    	//生成初始申报状态码
    	int initStatus = jeecgRepo.getDeclareInitStatusNumber(user);
        for (int i = 0; i < employeeInfoList.size(); i++) {
            EmployeeInfoEntity entity = employeeInfoList.get(i);
            //查询当前员工是否在员工收入中已经存在数据，没有新增
            //通过员工id和生成记录的月份 查询是否重复，而不是仅通过员工id
            EmployeeDeclareEntity employeeDeclare = employeeDeclareRepo.findByMonthAndEmployeeId(entity.getId(),month);

            boolean	 isCreate = false;
            //c_employee_declare 员工申报表中 无记录, 插入新的记录
            if (employeeDeclare == null) {
                isCreate = true;
            }else {
            	Integer backUpFlag = employeeDeclare.getBaceUpFlag();
            	if(backUpFlag!=null&&backUpFlag==1) { //有备份标记 永不再生
            	}else {
            		if(employeeDeclare.getInputerId().equals(user.getUserName())) {
                		//防止出现  加薪未审批通过前 生成收支，此时收支为未加薪，加薪后再生成会出现两条同时存在的情况
                		if(employeeDeclare.getEmployeeASalary().doubleValue()!=entity.getAStandardSalary().doubleValue()) {
                			employeeDeclareRepo.delete(employeeDeclare);
                			isCreate = true;
                		}
                	}else{//如果 已经生成的数据的 录入者不是 当前用户 并且 处于未录入 和录入完成中
                		if(employeeDeclare.getDeclareStatus()<=initStatus&&employeeDeclare.getDeclareStatus()>initStatus-3) {
                			employeeDeclareRepo.delete(employeeDeclare);
                			isCreate = true;
                		}
                	}
            	}
            }
            if(isCreate) {
            	employeeDeclare = new EmployeeDeclareEntity();
                employeeDeclare.setSalaryDate(date);//申报 月份
                employeeDeclare.setEmployeeInfo(entity);//员工信息
                employeeDeclare.setEmployeeBasePay(entity.getBasePay());
                employeeDeclare.setEmployeeASalary(entity.getAStandardSalary());
                employeeDeclare.setEmployeeType(entity.getEmployeeFlag());
                employeeDeclare.setEmployeeDepartment(entity.getDepartment());
                employeeDeclare.setDelFlg(0);//删除标记  0正常  1删除
                employeeDeclare.setDeclareStatus(initStatus);

                //带出顾客  单价  和 单价方式
                //根据客户id获取客户名字
                CustomerInfoEntity customer = commonService.findUniqueByProperty(CustomerInfoEntity.class, "id", entity.getCustomerId());
                if(customer!=null) {
                	employeeDeclare.setCustomerName(customer.getCode());
                	employeeDeclare.setCustomerInfo(customer.getId());
                	employeeDeclare.setCorporateId(customer.getSignCorporate());
                }
                employeeDeclare.setUnitPriceType(entity.getUnitPriceType());
                employeeDeclare.setUnitPrice(entity.getUnitPrice());

                //导入 员工六金表信息
                Double  sixCompanyBurdenOne = list.get(i)==null?0:list.get(i).getCompanySum();
//                int numMonth = list.get(i)==null?1:list.get(i).getNumMonth();
                employeeDeclare.setSixCompanyBurdenOne(sixCompanyBurdenOne);
                Double  sixPersonalBurden = (list.get(i)==null?0:list.get(i).getPersonalSum());
                employeeDeclare.setSixPersonalBurdenOne(sixPersonalBurden);

                //计算个人所得税
                //个税一   工资-六金
                double a1 = entity.getA1Payment()-sixPersonalBurden;  //发薪1 的个人税
                double perToneTaxOne = Utils.calculateIndividualIncomeTax(a1,perTaxBase);
                employeeDeclare.setPerToneTaxOne(perToneTaxOne);
                //个税二
                double a2 = entity.getA2Payment();
                double perToneTaxTwo = Utils.calculateIndividualIncomeTax(a2,perTaxBase);
                employeeDeclare.setPerToneTaxTwo(perToneTaxTwo);

                //员工折扣带入到B折扣
                if(entity.getDiscount()!=null) {
                	employeeDeclare.setBDiscount(entity.getDiscount());
                }

                //法定信息
                String[] ym = month.split("-");
                int year = Integer.parseInt(ym[0]);
                int mon = Integer.parseInt(ym[1]);
                try{
                	employeeDeclare.setLegalAttendanceDay(attendanceCalendarRepo.getYearMonthDays(year, mon));
                }catch(NullPointerException e) {
                	throw e;
                }

                Date currentDate = new Date();
                employeeDeclare.setCreatedDate(currentDate);
                employeeDeclare.setCreatedBy(user.getId());
                employeeDeclare.setBaceUpFlag(0);
                employeeDeclare.setLastModifiedBy(user.getUserName());
                employeeDeclare.setLastModifiedDate(currentDate);
                employeeDeclare.setInputerId(user.getUserName());
                employeeDeclareRepo.save(employeeDeclare);
                success++;  //新生成的记录数
            }
        }
        //返回成功几条的信息
        List<Integer> nums = (List<Integer>) ContextHolderUtils.getRequest().getAttribute("nums");
        nums.add(success);
    }

    /**
     * 根据 城市六金配置员工缺省六金配置
     * @param sixGoldScale	:六金城市最低最高信息
     * @param basepay :员工录入时 手动配置的六金基数金额
     */
    private SixGoldEntity setUpDefautSixGold(SixGoldScaleEntity sixGoldScale,double basePay) {
    	SixGoldEntity sixGoldEntity = new SixGoldEntity();

    	//公司 个人 养老金
    	Double companyEndowment = sixGoldScale.getCompanyEndowment();
    	Double personalEndowment = sixGoldScale.getPersonalEndowment();
    	Double endowmentMax = sixGoldScale.getEndowmentMax();
    	Double endowmentMin = sixGoldScale.getEndowmentMin();
    	if(basePay < endowmentMin) {
    		companyEndowment = endowmentMin * companyEndowment/100;
    		personalEndowment = endowmentMin * personalEndowment/100;
    	}else if(basePay > endowmentMax) {
    		companyEndowment = endowmentMax * companyEndowment/100;
    		personalEndowment = endowmentMax * personalEndowment/100;
    	}else {
    		companyEndowment = basePay * companyEndowment/100;
    		personalEndowment = basePay * personalEndowment/100;
    	}

    	//公司 个人 住房公积金
    	Double companyHousingFund = sixGoldScale.getCompanyHousingFund();
    	Double personalHousingFund = sixGoldScale.getPersonalHousingFund();
    	Double housingFundMax = sixGoldScale.getHousingFundMax();
    	Double housingFundMin = sixGoldScale.getHousingFundMin();
    	if(basePay < housingFundMin) {
    		companyHousingFund = housingFundMin * companyHousingFund/100;
    		personalHousingFund = housingFundMin * personalHousingFund/100;
    	}else if(basePay > housingFundMax) {
    		companyHousingFund = housingFundMax * companyHousingFund/100;
    		personalHousingFund = housingFundMax * personalHousingFund/100;
    	}else {
    		companyHousingFund = basePay * companyHousingFund/100;
    		personalHousingFund = basePay * personalHousingFund/100;
    	}

    	//公司个人 医疗保险
    	Double companyMedical = sixGoldScale.getCompanyMedical();
    	Double personalMedical = sixGoldScale.getPersonalMedical();
    	Double medicalMax = sixGoldScale.getMedicalMax();
    	Double medicalMin = sixGoldScale.getMedicalMin();
    	if(basePay < medicalMin) {
    		companyMedical = medicalMin * companyMedical/100;
    		personalMedical = medicalMin * personalMedical/100;
    	}else if(basePay > medicalMax) {
    		companyMedical = medicalMax * companyMedical/100;
    		personalMedical = medicalMax * personalMedical/100;
    	}else {
    		companyMedical = basePay * companyMedical/100;
    		personalMedical = basePay * personalMedical/100;
    	}

    	//公司 个人 失业保险
    	Double companyUnemployment = sixGoldScale.getCompanyUnemployment();
    	Double personalUnemployment = sixGoldScale.getPersonalUnemployment();
    	Double unemploymentMax = sixGoldScale.getUnemploymentMax();
    	Double unemploymentMin = sixGoldScale.getUnemploymentMin();
    	if(basePay < unemploymentMin) {
    		companyUnemployment = unemploymentMin * companyUnemployment/100;
    		personalUnemployment = unemploymentMin * personalUnemployment/100;
    	}else if(basePay > unemploymentMax) {
    		companyUnemployment = unemploymentMax * companyUnemployment/100;
    		personalUnemployment = unemploymentMax * personalUnemployment/100;
    	}else {
    		companyUnemployment = basePay * companyUnemployment/100;
    		personalUnemployment = basePay * personalUnemployment/100;
    	}

    	//工伤 保险
    	Double companyInjury = sixGoldScale.getCompanyInjury();
    	Double injuryMax = sixGoldScale.getInjuryMax();
    	Double injuryMin = sixGoldScale.getInjuryMin();
    	if(basePay < injuryMin) {
    		companyInjury = unemploymentMin * companyInjury/100;
    	}else if(basePay > injuryMax) {
    		companyInjury = unemploymentMax * companyInjury/100;
    	}else {
    		companyInjury = basePay * companyInjury/100;
    	}

    	//生育保险
    	Double companyMaternity = sixGoldScale.getCompanyMaternity();
    	Double maternityMax = sixGoldScale.getMaternityMax();
    	Double maternityMin = sixGoldScale.getMaternityMin();
    	if(basePay < maternityMin) {
    		companyMaternity = maternityMin * companyMaternity/100;
    	}else if(basePay > maternityMax) {
    		companyMaternity = maternityMax * companyMaternity/100;
    	}else {
    		companyMaternity = basePay * companyMaternity/100;
    	}

    	sixGoldEntity.setCompanyEndowment(companyEndowment);
    	sixGoldEntity.setPersonalEndowment(personalEndowment);
    	sixGoldEntity.setCompanyHousingFund(companyHousingFund);
    	sixGoldEntity.setPersonalHousingFund(personalHousingFund);
    	sixGoldEntity.setCompanyInjury(companyInjury);
    	sixGoldEntity.setCompanyMaternity(companyMaternity);
    	sixGoldEntity.setCompanyMedical(companyMedical);
    	sixGoldEntity.setPersonalMedical(personalMedical);
    	sixGoldEntity.setCompanyUnemployment(companyUnemployment);
    	sixGoldEntity.setPersonalUnemployment(personalUnemployment);
    	//重点六金 合计
    	sixGoldEntity.setCompanySum(companyEndowment+companyHousingFund+companyInjury+companyMaternity+companyMedical+companyUnemployment);
    	sixGoldEntity.setPersonalSum(personalEndowment+personalHousingFund+personalMedical+personalUnemployment);
    	sixGoldEntity.setNumMonth(1);

    	return sixGoldEntity;
    }

    /**
     * 0507: 选中数据 批量 通过.
     * @param status: 通过后的状态
     * @author gc
     */
	public Map<String, Object> batchPass(List<Integer> lids) {

		Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        //通过 主键 加载数据
        List<EmployeeDeclareEntity> employeeDeclareList = employeeDeclareRepo.findAllByEmployeeDeclareId(lids);

        if (!employeeDeclareList.isEmpty()) {
        	TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        	Map<String,Map<String, String>> tplContent = new HashMap<>();// 收件人邮箱   发送内容Map<title:value;content:value>
    		for (int i = 0; i < employeeDeclareList.size(); i++) {
    			EmployeeDeclareEntity employee = employeeDeclareList.get(i);
    			int declareStatus = employee.getDeclareStatus();
    			if(employee.getEmployeeType()==1) { //跳过 checker
    				if(declareStatus==((3+1)*3-1)||declareStatus == ((3+1)*3-2)) { //3层级 的部门 待申报 和 申报拒绝
    					declareStatus=(1+1)*3-1; //跳过二层审计  变为 一层待审批
    				}else{
    					//待申报的申报状态统一在原基础上-3
            			//申报拒绝的申报状态 统一在原基础上 -1
        				if(employee.getDeclareStatus()%3==2) { // 待申报
        					declareStatus-=3;
            			}else if(employee.getDeclareStatus()%3==1) { // 申报拒绝
            				declareStatus-=2;
            			}else {		//employee.getDeclareStatus()%3==0  待录入
            			}
    				}
    			}else {
    				//待申报的申报状态统一在原基础上-3
        			//申报拒绝的申报状态 统一在原基础上 -1
    				if(employee.getDeclareStatus()%3==2) { // 待申报
    					declareStatus-=3;
        			}else if(employee.getDeclareStatus()%3==1) { // 申报拒绝
        				declareStatus-=2;
        			}else {		//employee.getDeclareStatus()%3==0  待录入
        			}
    			}
    			//更新申报状态
        		updateDeclareStatus(user, employee, declareStatus);

        		//计算更新后的信息由谁处理 并获取邮箱
        		int empLv = (declareStatus+1)/3-1;
				List<TSUser> list = new ArrayList<TSUser>();
				if(empLv==2) { //多个审计账户统统发送
					list = jeecgRepo.findUserByRoleName("t_check");
				}else if(empLv==1){ //多个控制账户统统发送
					list = jeecgRepo.findUserByRoleName("t_control");
				}else if(empLv==0){ // 审批通过  不发送邮件，备份审批通过的数据
					employee.setBaceUpFlag(1);
					employeeDeclareCopyService.employeeBackUp(employee);
					continue;
				}else{ //部门只存在一个总监
					TSUser toSend = jeecgRepo.getManagerNow(empLv, employee.getEmployeeDepartment(), employee.getInputerId());
					list.add(toSend);
				}
				if(list.size()!=0) {
					for(TSUser managerNow : list) {
						String mail = managerNow.getEmail();
		            	if(tplContent.containsKey(mail)) { //包含此 email  //取消掉申报员工姓名
//		            		StringBuffer sb =new StringBuffer(tplContent.get(mail).get("content"));
//		            		sb.append("，"+employee.getEmployeeInfo().getName());
//		            		tplContent.get(mail).put("content", sb.toString());
		            	} else {  //第一次添加邮件
		            		Map<String, String> map = new HashMap<String,String>();
		            		map.put("subject", "收支申报通知");
//		            		map.put("content",managerNow.getRealName()+"，<br><br>"+jeecgRepo.toChinese(employee.getSalaryDate().getMonth())+"月份"+employee.getEmployeeInfo().getName());
		            		map.put("content", "收支信息已申报，请及时处理。<br><br>月份："+(employee.getSalaryDate().getMonth()+1)
		            				+"月<br>申报："+user.getRealName()+"<br><br>< 收支运营 SaaS >");
		            		tplContent.put(mail, map);
		            	}
					}
				}
    		}
    		if(tplContent.size()!=0) {
    			//补全 content
//    			for(String toSend : tplContent.keySet()) {
//    				String string = tplContent.get(toSend).get("content");
//    				String content = string+"收支信息已申报，请及时处理。<br><br>"+"收支运营系统";
//    				tplContent.get(toSend).put("content", content);
//    			}
    			//发送邮件
    			emailConfigService.mailSendAll2(tplContent);
    		}
        } else {
            result.put("errMsg", "无选中数据申报通过");
            result.put("errCode", -1);
        }
        return result;
	}


	/**
	 * 批量退回
	 * @param ids
	 * @param returnReason
	 * @param type
	 * @return
	 */
    public Map<String, Object> batchReturn(List<Integer> ids, String returnReason, String type) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        //当前登录人员信息
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        //加载数据
        List<EmployeeDeclareEntity> employeeDeclareList = employeeDeclareRepo.findAllByEmployeeDeclareId(ids);
        if (!employeeDeclareList.isEmpty()) {
        	Map<String,Map<String, String>> tplContent = new HashMap<>();// 收件人邮箱   发送内容Map<title:value;content:value>
        	for (int i = 0; i < employeeDeclareList.size(); i++) {
        		EmployeeDeclareEntity employee = employeeDeclareList.get(i);
				int declareStatus = employee.getDeclareStatus();
				if(employee.getEmployeeType()==1) { //内部员工 跳过 二层审计
					if(declareStatus==2 || declareStatus == ((1+1)*3-1)) { //审批通过 和 待审批
						declareStatus=(3+1)*3-2; //跳过二层审计  变为 三层申报拒绝
					}else{
						if(declareStatus%3==2) { //待申报 -> 申报拒绝
			        		declareStatus+=2;
			        	}else if(declareStatus%3==1) {  //申报拒绝 -> 申报拒绝
			        		declareStatus+=3;
			        	}else {        	}
					}
				}else {   //外派员工
					if(declareStatus==2) {  //controller 先审批通过 再拒绝到下一层
						declareStatus=7;
		        	}else if(declareStatus%3==2) { //待申报 -> 申报拒绝
		        		declareStatus+=2;
		        	}else if(declareStatus%3==1) {  //申报拒绝 -> 申报拒绝
		        		declareStatus+=3;
		        	}else {        	}
				}
				updateDeclareStatus(user,employee,declareStatus,returnReason);
				int empLv = (declareStatus+2)/3-1;
				List<TSUser> list = new ArrayList<TSUser>();
				if(empLv==2) { //审计
					list = jeecgRepo.findUserByRoleName("t_check");
				}else {
					TSUser toSend = jeecgRepo.getManagerNow(empLv, employee.getEmployeeDepartment(), employee.getInputerId());
					list.add(toSend);
				}
				if(list.size()!=0) {
					for(TSUser managerNow : list) {
						String mail = managerNow.getEmail();
		            	if(tplContent.containsKey(mail)) { //包含此 email  //去掉人名
		            		StringBuffer sb =new StringBuffer(tplContent.get(mail).get("content"));
		            		sb.append("<br><br>月份："+(employee.getSalaryDate().getMonth()+1)+"月<br>姓名："
		            				+employee.getEmployeeInfo().getName()+"<br>顾客："+employee.getCustomerName()+"<br>拒绝理由："+returnReason);
		            		tplContent.get(mail).put("content", sb.toString());
		            	} else {  //第一次添加邮件
		            		Map<String, String> map = new HashMap<String,String>();
		            		map.put("subject", "收支拒绝通知");
		            		map.put("content","收支信息被拒绝，请及时处理。<br><br>月份："+(employee.getSalaryDate().getMonth()+1)+"月<br>姓名："
		            		+employee.getEmployeeInfo().getName()+"<br>顾客："+employee.getCustomerName()+"<br>拒绝理由："+returnReason);
		            		tplContent.put(mail, map);
		            	}
					}
				}
        	}
        	if(tplContent.size()!=0) {
        		//补全 content
        		for(String toSend : tplContent.keySet()) {
    				String string = tplContent.get(toSend).get("content");
    				String content = string+"<br><br>< 收支运营 SaaS >";
    				tplContent.get(toSend).put("content", content);
    			}
    			//发送邮件
    			emailConfigService.mailSendAll2(tplContent);
        	}
        } else {
            result.put("errMsg", "没有需要退回的申报数据");
            result.put("errCode", -1);
        }
        return result;
    }
    /**
     * 更新申报状态（更新拒绝状态，带拒绝理由）
     * @param user
     * @param employeeDeclare
     * @param statusChange
     * @param returnReason
     */
	private void updateDeclareStatus(TSUser user,EmployeeDeclareEntity employeeDeclare, Integer statusChange,
			String returnReason) {
             EmployeeDeclareEntity employee = employeeDeclare;
             employee.setLastModifiedDate(new Date());
             employee.setLastModifiedBy(user.getUserName());
             employee.setDeclareStatus(statusChange);
             employee.setDeclareReturnreason(returnReason);
             employeeDeclareRepo.update(employee);
	}

	/**
     * 批量更新申报状态（通过）
     *
     * @param user
     * @param declareStatus
     */
    public void updateDeclareStatus(TSUser user, List<EmployeeDeclareEntity> employeeDeclareList, Integer changeStatus) {
        //批量更新状态
        for (int i = 0; i < employeeDeclareList.size(); i++) {
            EmployeeDeclareEntity employee = employeeDeclareList.get(i);
            employee.setLastModifiedDate(new Date());
            employee.setLastModifiedBy(user.getUserName());
            employee.setDeclareStatus(changeStatus);
            employee.setDeclareReturnreason(null);
            employeeDeclareRepo.update(employee);
        }
    }

    /**
     * 更新单个收支申报状态（通过）
     * @param user
     * @param employee
     * @param changeStatus
     */
	public void updateDeclareStatus(TSUser user, EmployeeDeclareEntity employee, int changeStatus) {
		// TODO Auto-generated method stub
        employee.setLastModifiedDate(new Date());
        employee.setLastModifiedBy(user.getUserName());
        employee.setDeclareStatus(changeStatus);
        employee.setDeclareReturnreason(null);
        employeeDeclareRepo.update(employee);
	}


    /**
     * 提取员工名字信息
     *
     * @param employeeDeclareList
     * @return
     */
    public String extractEmployeeNameInfo(List<EmployeeDeclareEntity> employeeDeclareList) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < employeeDeclareList.size(); i++) {
            if (i == 0) {
                sb.append(employeeDeclareList.get(i).getEmployeeInfo().getName());
            } else {
                sb.append(",").append(employeeDeclareList.get(i).getEmployeeInfo().getName());
            }
        }
        return sb.toString();
    }

    private CellStyle getCellStyle(Workbook wb, int fontType) {
        CellStyle style = wb.createCellStyle();
        //添加上边边框
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        //添加右边边框
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        //添加下边边框
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        //添加左边边框
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        //居中
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //字体
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 10);//字体大小
        //字体
        Font fontRed = wb.createFont();
        fontRed.setFontName("黑体");
        fontRed.setFontHeightInPoints((short) 10);//字体大小
        fontRed.setColor(IndexedColors.RED.index);
        if (fontType == 0) {
            style.setFont(font);
        } else {
            style.setFont(fontRed);
        }
        return style;
    }


    /**
     * 对象转字符串
     *
     * @param obj
     * @return
     */
    public String objConvertString(Object obj) {
        if (null != obj) {
            return obj.toString();
        }
        return "";
    }


    /**
     * 格式化价格
     *
     * @return
     */
    public String formatPrice(Double price) {
        String pStr = "";
        if (null != price) {
            pStr = price + "";
            if (pStr.substring(pStr.indexOf(".") + 1).length() == 1) {
                pStr = pStr + "0";
            }
        }
        return pStr;
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public String formatDate(Date date, String fmt) {
        String dateStr = "";
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }

    /**
     * 获取表头数据
     *
     * @param user
     * @return
     */
    public Map<String, Object> getTableHeaderDatas(Integer[] status) {
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        //非超级管理员添加部门条件
        String departId = null;
        //超级管理员、顶层管理员可以看到所有数据
        List<String> topManager = dictCategoryService.getTopManager("topManager");
        if (!"admin".equals(user.getUserName()) && !topManager.contains(user.getUserName())) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
        }
        Map<String, Object> result = getTableHeaderCalc(departId, status);
        return result;
    }


    /**
     * 获取表头计算数据
     *
     * @return
     */
    public Map<String, Object> getTableHeaderCalc(String departId, Integer... declareStatus) {
        Map<String, Object> result = new HashMap<String, Object>();
        Long t1 = employeeDeclareRepo.getNotEmptyEmployeeNameCount(departId, declareStatus);//员工姓名非空记录数
        Double t20 = employeeDeclareRepo.getUnitPriceAvg(departId, 0, declareStatus);//月单价
        Double t22 = employeeDeclareRepo.getUnitPriceAvg(departId, 2, declareStatus);//日单价
        Double t23 = employeeDeclareRepo.getUnitPriceAvg(departId, 3, declareStatus);//时单价
        Double t3 = employeeDeclareRepo.getAppointmentAttendanceDayAvg(departId, declareStatus);//约定出勤日平均数
        Long t4 = employeeDeclareRepo.getNotEmptyMonthAdjustmentCount(departId, declareStatus);//月间调整不为空记录数
        Double t5 = employeeDeclareRepo.getIncomeSum(departId, declareStatus);//收入总和
        Double t6 = employeeDeclareRepo.getMonthIncomeProportion(departId, declareStatus);//月间调整和收入百分比
        Double t7 = employeeDeclareRepo.getNoPerformanceAttendanceDayProportion(departId, declareStatus);//无绩效和有绩效、有薪年假百分比
        Double t8 = employeeDeclareRepo.getAstandardSalarySum(departId, declareStatus);//a标准总和
        Double t9 = employeeDeclareRepo.getBdiscountAvg(departId, declareStatus);//试用期折扣 平均值
        Double t10 = employeeDeclareRepo.getPayablePerformanceProportion(departId, declareStatus);//应付绩效和应付绩效、工资百分比
        Double t11 = employeeDeclareRepo.getC1ComputerSubsidySum(departId, declareStatus);//电脑补贴总和
        Double t12 = employeeDeclareRepo.getC2OvertimeSalarySum(departId, declareStatus);//加班费总和
        Double t13 = employeeDeclareRepo.getC3OtherSubsidySum(departId, declareStatus);//其它补贴总和
        Double t14 = employeeDeclareRepo.getCsubsidySum(departId, declareStatus);//补贴总和
        Double t15 = employeeDeclareRepo.getPayableTotalSum(departId, declareStatus);//应发合计总和
        Long t16 = employeeDeclareRepo.getPayableTotalGtZeroSum(departId, declareStatus);//应发合计大于0记录数
        result.put("t1", t1 == null ? 0 : t1);
        result.put("t20", (t20 == null || t20 == 0) ? " " : formatNum2(t20));
        result.put("t22", ((t22 == null || t22 == 0) ? " " : formatNum2(t22)));
        result.put("t23", ((t23 == null || t23 == 0) ? " " : formatNum2(t23)));
        result.put("t3", ((t3 == null || t3 == 0) ? " " : formatNum2(t3)));
        result.put("t4", ((t4 == null || t4 == 0) ? " " : t4));
        result.put("t5", ((t5 == null || t5 == 0) ? " " : formatNum2(t5)));
        result.put("t6", (t6 == null ? 0.0 : formatNum1(t6 * 100.0)) + "%");
        result.put("t7", (t7 == null ? 0.0 : formatNum1(t7 * 100.0)) + "%");
        result.put("t8", ((t8 == null || t8 == 0) ? " " : formatNum2(t8)));
        result.put("t9", (t9 == null ? 0.0 : formatNum1(t9)) + "%");
        result.put("t10", (t10 == null ? 0.0 : formatNum1(t10 * 100.0)) + "%");
        result.put("t11", ((t11 == null || t11 == 0) ? " " : formatNum2(t11)));
        result.put("t12", ((t12 == null || t12 == 0) ? " " : formatNum2(t12)));
        result.put("t13", ((t13 == null || t13 == 0) ? " " : formatNum2(t13)));
        result.put("t14", ((t14 == null || t14 == 0) ? " " : formatNum2(t14)));
        result.put("t15", ((t15 == null || t15 == 0) ? " " : formatNum2(t15)));
        result.put("t16", ((t16 == null || t16 == 0) ? " " : t16));
        return result;
    }




    public Double formatNum1(Double num) {
        String str = String.valueOf(num);
        if (str.substring(str.lastIndexOf(".") + 1).length() > 1) {
            BigDecimal bigDecimal = new BigDecimal(num);
            return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return num;
    }

    public Double formatNum2(Double num) {
        String str = String.valueOf(num);
        if (str.substring(str.lastIndexOf(".") + 1).length() > 2) {
            BigDecimal bigDecimal = new BigDecimal(num);
            return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return num;
    }

    /**
     * dataGird查询函数
     * @param employeeDeclare
     * @param parameterMap
     * @param dataGrid
     * @param declareStatus
     * @param delFlg
     * @param statusQueryCondition 状态码查询条件  0: in  1: <= less   2:>= greater
     */
    public void setDataGrid(EmployeeDeclareEntity employeeDeclare,Map<String, String[]> parameterMap, DataGrid dataGrid,Integer[] declareStatus, Integer delFlg,int statusQueryCondition) {
    	//通过 session 获取 当前 登陆用户
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        //EmployeeDeclareEntity
        CriteriaQuery cq = new CriteriaQuery(EmployeeDeclareEntity.class, dataGrid);
        cq.eq("delFlg", delFlg);
        switch(statusQueryCondition) {
	        case 1: cq.le("declareStatus", declareStatus[0]);cq.ge("declareStatus", 3);break;
	        case 2: cq.ge("declareStatus", declareStatus[0]);break;
	        default: cq.in("declareStatus", declareStatus);
        }
        //部门查询在这里处理
        if(StringUtil.isNotEmpty(employeeDeclare.getEmployeeDepartment())){
        	String depatrId = employeeDeclare.getEmployeeDepartment();
        	employeeDeclare.setEmployeeDepartment(null);
			TSDepart depart = commonService.getEntity(TSDepart.class,depatrId);
			List<TSDepart> departs = jeecgRepo.findSubDepart(depart);
			String[] departIds = new String[departs.size()];
			int i = 0;
			for(TSDepart dp : departs){
				departIds[i] = dp.getId();
				i++;
			}
			cq.in("employeeDepartment",departIds);
        }
        //如果是 controller和checker 就可以查看全部 c_employee_declare
        String myRoleName = employeeDeclareRepo.findMyRole(user.getId());
        if("t_control".equals(myRoleName)) {
        }else if("t_access".equals(myRoleName)) {  //访客不能查看审批和内部员工的业绩考核
        	employeeDeclare.setEmployeeType(0);
        	employeeDeclare.setDeclareStatus(2);
        }else if("t_check".equals(myRoleName)) {  //审计部门不能查看 内部员工的 业绩考核
        	employeeDeclare.setEmployeeType(0);
        }else {//
        	if("t_report".equals(myRoleName)) {  //总监情况特殊，录入界面只能看到自己录入的，申报界面 只能看到 部门下客户经理申报的
        		TSDepart currentDepart = jeecgRepo.getCurrentDepart(user);
        		List<TSUser> list = jeecgRepo.getDepartAllUser(currentDepart);
    			if(list.size() == 0) { //部门下 没有 客户经理 账号，则 显示空
    				cq.eq("employeeInfo.id", -99999);
        		} else {
        			int size = list.size();
        			String[] list1 = new String[size];
        			for(int i =0;i<size;i++) {
        				list1[i]=list.get(i).getUserName();
        			}
        			cq.in("inputerId", list1);
        		}
        	}else {
        		employeeDeclare.setInputerId(user.getUserName());
        	}
        }
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, employeeDeclare, parameterMap);
        commonService.getDataGridReturn(cq, true);
    }
    /**
     * dataGird查询函数   访客专用
     * @param employeeDeclare
     * @param parameterMap
     * @param dataGrid
     * @param declareStatus
     * @param delFlg
     * @param statusQueryCondition 状态码查询条件  0: in  1: <= less   2:>= greater
     */
    public void setDataGridforAccess(EmployeeDeclareCopyEntity employeeDeclareCopy,Map<String, String[]> parameterMap, DataGrid dataGrid) {
    	//EmployeeDeclareCopyEntity
    	CriteriaQuery cq = new CriteriaQuery(EmployeeDeclareCopyEntity.class, dataGrid);
    	//访客只能查看外派
    	employeeDeclareCopy.setEmployeeType(0);
    	cq.eq("employeeType", 0);
    	//部门查询在这里处理
    	if(StringUtil.isNotEmpty(employeeDeclareCopy.getEmployeeDepartment())){
    		String depatrId = employeeDeclareCopy.getEmployeeDepartment();
    		employeeDeclareCopy.setEmployeeDepartment(null);
    		TSDepart depart = commonService.getEntity(TSDepart.class,depatrId);
    		List<TSDepart> departs = jeecgRepo.findSubDepart(depart);
    		String[] departIds = new String[departs.size()];
    		int i = 0;
    		for(TSDepart dp : departs){
    			departIds[i] = dp.getId();
    			i++;
    		}
    		cq.in("employeeDepartment",departIds);
    	}
    	//查询条件组装器
    	org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, employeeDeclareCopy, parameterMap);
    	commonService.getDataGridReturn(cq, true);
    }

	/**
	 * 导出excel函数（利用datagrid查询条件）
	 * @date 0809
	 * @param list
	 * @param date
	 * @return
	 */
	public Map<String, Object> exportDeclareData(List<EmployeeDeclareEntity> list, Date date) {
		// TODO Auto-generated method stub
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = mo.toString()+"月人力收支总计";
		Map<String, Object> result = new HashMap<String, Object>();
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/employee-declare3.xlsx");
        if (!excelFile.exists()) {
            return null;
        }
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(excelFile);
            wb = new XSSFWorkbook(is);
            Sheet sheet = wb.getSheetAt(0);
            empDeclareExportDemo(list, sheet);
            //让sheet的求和函数生效
            sheet.setForceFormulaRecalculation(true);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        result.put("wb", wb);
        result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
		return result;
	}
	/**
	 * 访客-导出excel函数（利用datagrid查询条件）
	 * @date 0809
	 * @param list
	 * @param date
	 * @return
	 */
	public Map<String, Object> exportDeclareDataforAccess(List<EmployeeDeclareCopyEntity> list, Date date) {
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = mo.toString()+"月人力收支总计";
		Map<String, Object> result = new HashMap<String, Object>();
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/employee-declare3.xlsx");
        if (!excelFile.exists()) {
            return null;
        }
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(excelFile);
            wb = new XSSFWorkbook(is);
            Sheet sheet = wb.getSheetAt(0);
            empDeclareExportDemoforAccess(list, sheet);
            //让sheet的求和函数生效
            sheet.setForceFormulaRecalculation(true);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        result.put("wb", wb);
        result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
		return result;
	}
	/**
	 * EmployeeDeclareCopyEntity导出模板
	 * 访客专用
	 * @param list
	 * @param sheet
	 */
	public void empDeclareExportDemoforAccess(List<EmployeeDeclareCopyEntity> list, Sheet sheet) {
		// TODO Auto-generated method stub
		if (!list.isEmpty()) {
			//设置求和函数
			Row temp = sheet.getRow(1);
			//收入  求和
			temp.getCell(15).setCellFormula("SUM(P4:P"+(4+list.size()-1)+")");
			temp.getCell(30).setCellFormula("SUM(AE4:AE"+(4+list.size()-1)+")");
		    for (int i = 0; i < list.size();
		    		i++) {
		        Row oldRow = sheet.getRow(i + 2);
		        Row row = sheet.createRow(i + 3);
		        //设置新增行的单元格风格与上一行一样
		        row.setRowStyle(oldRow.getRowStyle());
		        short lastCellNum = oldRow.getLastCellNum();
		        for(int j = 1 ;j < lastCellNum;j++) {
		        	Cell createCell = row.createCell(j);
		        	Cell oldcell = oldRow.getCell(j);
		        	CellStyle cellStyle = oldcell.getCellStyle();
		        	CellStyle copy = sheet.getWorkbook().createCellStyle();
		        	copy.cloneStyleFrom(cellStyle);
		        	if((j>=7&&j<=9)||(j>=12&&j<=15)||(j>=20&&j<=22)||(j>=24&&j<=30)) {
		        		copy.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		        	}
		        	createCell.setCellStyle(copy);
		        }

		        EmployeeDeclareCopyEntity employeeDeclare =list.get(i);
		        //员工信息
		        EmployeeInfoEntity employeeInfoEntity = commonService.get(EmployeeInfoEntity.class, employeeDeclare.getEmployeeId());
		        if (null != employeeInfoEntity) {
		            row.getCell(1).setCellValue(objConvertString(employeeInfoEntity.getCode()));
		            row.getCell(2).setCellValue(objConvertString(employeeInfoEntity.getName()));
		            //部门信息
		            if (null != employeeDeclare.getEmployeeDepartment()) {
		            	TSDepart department = systemService.getEntity(TSDepart.class, employeeDeclare.getEmployeeDepartment());
		                row.getCell(3).setCellValue(objConvertString(department.getDepartname()));
		            }
		        }
//                 //客户简称
//                 row.getCell(4).setCellValue(employeeDeclare.getCustomerName());
		       //法人简称
//                 row.getCell(5).setCellValue(corporateInfo==null?null:corporateInfo.getCode());
		       Integer customerId = employeeDeclare.getCustomerId()==null?null:employeeDeclare.getCustomerId();
		       //根据客户ID获取法人信息
		       Integer corporateId = null;
		       if(employeeDeclare.getCorporateId()==null) {
		    	   corporateId = jeecgRepo.findCorporateIdByCustomerId(customerId);
		       }else {
		    	   corporateId = employeeDeclare.getCorporateId();
		       }
		       CorporateInfoEntity corporateInfo = corporateInfoRepository.findUniqueBy(CorporateInfoEntity.class, "id", corporateId);

		       if(corporateInfo!=null) {
		    	   if("北京云信".equals(employeeDeclare.getCorporateId())&&"北京云信".equals(corporateInfo.getCode())) {
		    		   row.getCell(4).setCellValue(employeeDeclare.getCustomerName());
		    	   }
		    	   if("江苏智蓝".equals(corporateInfo.getCode())) {
		    		   row.getCell(5).setCellValue(employeeDeclare.getCustomerName());
		    	   }
		    	   if("北京智蓝".equals(corporateInfo.getCode())) {
		    		   row.getCell(6).setCellValue(employeeDeclare.getCustomerName());
		    	   }
		       }

		        if(employeeDeclare.getUnitPriceType()!=null) {
		        	 //月单价
		            Integer unitPriceType = employeeDeclare.getUnitPriceType();
		            switch(unitPriceType) {
		            //月单价
		            	case 0: row.getCell(7).setCellValue(employeeDeclare.getUnitPrice());break;
		            	case 2: row.getCell(8).setCellValue(employeeDeclare.getUnitPrice());break;
		            	case 3: row.getCell(9).setCellValue(employeeDeclare.getUnitPrice());break;
		            }
		        }
		        //约定出勤日
		        if(employeeDeclare.getAppointedAttendanceDay()!=null)
		        row.getCell(10).setCellValue(employeeDeclare.getAppointedAttendanceDay());
		        //验收出勤日
		        if(employeeDeclare.getAcceptedAttendanceDay()!=null)
		        row.getCell(11).setCellValue(employeeDeclare.getAcceptedAttendanceDay());
		        //当月其他
		        if(employeeDeclare.getMonthOther()!=null)
		        row.getCell(12).setCellValue(employeeDeclare.getMonthOther());
		        //验收加算
		        if(employeeDeclare.getAcceptanceAdd()!=null)
		        row.getCell(13).setCellValue(employeeDeclare.getAcceptanceAdd());
		        //月间调整
		        if(employeeDeclare.getMonthAdjustment()!=null)
		        row.getCell(14).setCellValue(employeeDeclare.getMonthAdjustment());
		        //收入
		        if(employeeDeclare.getIncome()!=null)
		        row.getCell(15).setCellValue(employeeDeclare.getIncome());
		        if(employeeDeclare.getPoCode()!=null)
				row.getCell(16).setCellValue(employeeDeclare.getPoCode());

				//法定出勤日
		        if(employeeDeclare.getLegalAttendanceDay()!=null)
				row.getCell(17).setCellValue(employeeDeclare.getLegalAttendanceDay());

				//有绩效出勤日
		        if(employeeDeclare.getPerformanceAttendanceDay()!=null)
				row.getCell(18).setCellValue(employeeDeclare.getPerformanceAttendanceDay());
				//无绩效 出勤日数
		        if(employeeDeclare.getNoPerformanceAttendanceDay()!=null)
		        row.getCell(19).setCellValue(employeeDeclare.getNoPerformanceAttendanceDay());
				//基本工资（A1）
				if(employeeDeclare.getEmployeeBasepay()!=null)
				row.getCell(20).setCellValue(employeeDeclare.getEmployeeBasepay());
				//A标准工资
				Double aStandardSalary = employeeDeclare.getEmployeeAsalary()==null?0:employeeDeclare.getEmployeeAsalary();
				//绩效工资（A2）
				Double meritPay = aStandardSalary-(employeeDeclare.getEmployeeBasepay()==null?0:employeeDeclare.getEmployeeBasepay());
				row.getCell(21).setCellValue(meritPay);
				//标准工资（A）
				Double basePay = employeeDeclare.getEmployeeBasepay()==null?0:employeeDeclare.getEmployeeBasepay();
				row.getCell(22).setCellValue(aStandardSalary);
				//B折扣率
				Integer bDiscount = employeeDeclare.getBDiscount()==null?0:employeeDeclare.getBDiscount();
//				java.text.DecimalFormat percentFormat =new java.text.DecimalFormat();
				double decimal = bDiscount/100.0;
				row.getCell(23).setCellValue(decimal);

				//应付工资
				Double legalAttendanceDay = employeeDeclare.getLegalAttendanceDay()==null?0:employeeDeclare.getLegalAttendanceDay();
				if(legalAttendanceDay!=0) {
					Double performanceDays = employeeDeclare.getPerformanceAttendanceDay()==null?0:employeeDeclare.getPerformanceAttendanceDay();
					Double noPerformanceDays = employeeDeclare.getNoPerformanceAttendanceDay()==null?0:employeeDeclare.getNoPerformanceAttendanceDay();

					//应付 基本工资  （有效+无效）/法定*基本工资*折扣率
					Double payableBaseSalary = (performanceDays+noPerformanceDays)/legalAttendanceDay*basePay*bDiscount/100;
					row.getCell(24).setCellValue(payableBaseSalary);
					//应付 绩效    有效/法定*基本工资*折扣率
					Double payablePerformance = performanceDays/legalAttendanceDay*meritPay*bDiscount/100;
					row.getCell(25).setCellValue(payablePerformance);
				}
				//C1电脑补贴
				Double cComputerSubsidy = employeeDeclare.getCComputerSubsidy()==null?0:employeeDeclare.getCComputerSubsidy();
				row.getCell(26).setCellValue(cComputerSubsidy);
				//C2加班费
				Double cOvertimeSalary = employeeDeclare.getCOvertimeSalary()==null?0:employeeDeclare.getCOvertimeSalary();
				row.getCell(27).setCellValue(cOvertimeSalary);

				//C3其他补贴
				Double c1OtherSubsidy = employeeDeclare.getC1OtherSubsidy()==null?0:employeeDeclare.getC1OtherSubsidy();
				Double c2OtherSubsidy = employeeDeclare.getC2OtherSubsidy()==null?0:employeeDeclare.getC2OtherSubsidy();
				Double c3OtherSubsidy = employeeDeclare.getC3OtherSubsidy()==null?0:employeeDeclare.getC3OtherSubsidy();
				Double cOtherSubsidy = c1OtherSubsidy + c2OtherSubsidy + c3OtherSubsidy;
				row.getCell(28).setCellValue(cOtherSubsidy);
				//C总补贴
				Double cTotalSubsidy = cComputerSubsidy + cOvertimeSalary + cOtherSubsidy;
				row.getCell(29).setCellValue(cTotalSubsidy);
				//应发合计  payableSalary
				if(employeeDeclare.getPayableSalary()!=null)
				row.getCell(30).setCellValue(employeeDeclare.getPayableSalary());

				String c1OtherSubsidyRemark = employeeDeclare.getC1OtherSubsidyRemark();
				String c2OtherSubsidyRemark = employeeDeclare.getC2OtherSubsidyRemark();
				String c3OtherSubsidyRemark = employeeDeclare.getC3OtherSubsidyRemark();
				StringBuffer sb = new StringBuffer();
				sb.append(c1OtherSubsidyRemark==null?"":c1OtherSubsidyRemark+" ");
				sb.append(c2OtherSubsidyRemark==null?"":c2OtherSubsidyRemark+" ");
				sb.append(c3OtherSubsidyRemark==null?"":c3OtherSubsidyRemark+" ");
				row.getCell(31).setCellValue(sb.toString());
		    }
		}
	}

	/**
	 * 导出excel函数（利用datagrid查询条件） -工资单导出专用
	 * @date 0815
	 * @param list
	 * @param date
	 * @return
	 */
	public Map<String, Object> exportDeclareSalaryIdsData(List<EmployeeDeclareEntity> list, Date date) {
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = mo.toString()+"月工资单";
		Map<String, Object> result = new HashMap<String, Object>();
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
				.getPath() + "excel-template/employee-salary.xlsx");
		if (!excelFile.exists()) {
			return null;
		}
		InputStream is = null;
		Workbook wb = null;
		try {
			is = new FileInputStream(excelFile);
			wb = new XSSFWorkbook(is);
			Sheet sheet = wb.getSheetAt(0);
            empDeclareSalaryExportSelectedIdsDemo(list, sheet);
			//让sheet的求和函数生效
			sheet.setForceFormulaRecalculation(true);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		result.put("wb", wb);
		result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
		return result;
	}
	/**
	 * 导出excel函数（利用datagrid查询条件）-工资单导出专用
	 * @date 0814
	 * @param list
	 * @param date
	 * @return
	 */
	public Map<String, Object> exportDeclareSalaryData(List<EmployeeDeclareEntity> list, Date date) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = mo.toString()+"月工资单";
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
				.getPath() + "excel-template/employee-salary.xlsx");
		if (!excelFile.exists()) {
			return null;
		}
		InputStream is = null;
		Workbook wb = null;
		try {
			is = new FileInputStream(excelFile);
			wb = new XSSFWorkbook(is);
			Sheet sheet = wb.getSheetAt(0);
			empDeclareSalaryExportDemo(list, sheet);
			//让sheet的求和函数生效
			sheet.setForceFormulaRecalculation(true);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		result.put("wb", wb);
		result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
		return result;
	}
	/**
	 * 导出 部门 总收支
	 * @param request
	 * @param declareStatus
	 * @param employeeDeclare
	 * @param dataGrid
	 * @return
	 */
	public Map<String, Object> exportDepartData(HttpServletRequest request, String declareStatus,
			EmployeeDeclareEntity employeeDeclare, DataGrid dataGrid,Date date) {
		Map<String, Object> result = new HashMap<String, Object>();
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = mo.toString()+"月部门总收支";
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/depart-declare.xlsx");
        if (!excelFile.exists()) {
            return null;
        }
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(excelFile);
            wb = new XSSFWorkbook(is);

            //项目的查询条件  因为执行完dataGrid查询后 会清空查询对象的查询条件  所以该段代码提前
            ProjectEntity projectCondition = new ProjectEntity();
            projectCondition.setProjectMonth(employeeDeclare.getSalaryDate());
            if(employeeDeclare.getEmployeeDepartment()!=null)  projectCondition.setProjectDepartment(employeeDeclare.getEmployeeDepartment());
            if(employeeDeclare.getCustomerInfo()!=null)  projectCondition.setProjectCustomer1(employeeDeclare.getCustomerInfo()+"");

            //sheet1 - 人力收支 （带 查询条件）
            Sheet sheet1 = wb.getSheetAt(1);
            //查询
    		setDataGridByStatus(request, declareStatus, employeeDeclare, dataGrid);
    		List<EmployeeDeclareEntity> list = dataGrid.getResults();
    		//导出
            empDeclareExportDemo(list, sheet1);
            //让sheet的求和函数生效
            sheet1.setForceFormulaRecalculation(true);


            //sheet2 - 项目收支 （带查询条件）
            Sheet sheet2 = wb.getSheetAt(2);
            List<ProjectEntity> projectList = new ArrayList<ProjectEntity>();
            String projectStatus = "";
            if(declareStatus==null||"".equals(declareStatus)||"undefined".equals(declareStatus)){
            }else {
            	 Integer ps = jeecgRepo.changeStatusEmployeeDeclareToProject(Integer.parseInt(declareStatus));
            	 projectStatus = ""+ps;
            }
            projectService.setDataGridByStatus(request, projectStatus, projectCondition, dataGrid);
            projectList = dataGrid.getResults();
            projectService.projectDeclareExportDemo(projectList,sheet2);


            //sheet0 - 部门合计
            Sheet sheet0 = wb.getSheetAt(0);
            Row row = sheet0.getRow(1);
            //总收入  总毛利  总人数（人数+项目数）
            double incomeTotal = 0;
            double profitTotal = 0;
            double num = list.size()+projectList.size();
            for(EmployeeDeclareEntity declare : list) {
            	incomeTotal += declare.getIncome()==null?0:declare.getIncome();
            	profitTotal += declare.getCompanyProfit()==null?0:declare.getCompanyProfit();
            }
            for(ProjectEntity project : projectList) {
            	incomeTotal += project.getProjectIncome()==null?0:project.getProjectIncome();
            	profitTotal += project.getProjectProfit()==null?0:project.getProjectProfit();
            }
            row.getCell(1).setCellValue(incomeTotal);
            //人数
            row.getCell(2).setCellValue(list.size());
            //项目数
            row.getCell(3).setCellValue(projectList.size());
            //总毛利
            row.getCell(4).setCellValue(profitTotal);
            //平均毛利
            row.getCell(5).setCellValue(num==0?0:profitTotal/num);

        } catch (Exception e) {
            log.info(e.getMessage(), e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        result.put("wb", wb);
        result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
		return result;
	}
	/**
	 * 访客-导出部门总收支
	 * @param request
	 * @param declareStatus
	 * @param employeeDeclare
	 * @param dataGrid
	 * @param date
	 * @return
	 */
	public Map<String, Object> exportDepartDataforAccess(HttpServletRequest request,EmployeeDeclareCopyEntity employeeDeclare, DataGrid dataGrid, Date date) {
		Calendar gc=Calendar.getInstance();
		gc.setTime(date);
		Integer mo =gc.get(Calendar.MONTH)+1;
		String head = mo.toString()+"月部门总收支";
		Map<String, Object> result = new HashMap<String, Object>();
		File excelFile = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/depart-declare.xlsx");
        if (!excelFile.exists()) {
            return null;
        }
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(excelFile);
            wb = new XSSFWorkbook(is);

            //项目的查询条件  因为执行完dataGrid查询后 会清空查询对象的查询条件  所以该段代码提前
            ProjectCopyEntity projectCondition = new ProjectCopyEntity();
            projectCondition.setProjectMonth(employeeDeclare.getSalaryDate());
            if(employeeDeclare.getEmployeeDepartment()!=null)  projectCondition.setProjectDepartment(employeeDeclare.getEmployeeDepartment());
            if(employeeDeclare.getCustomerId()!=null)  projectCondition.setProjectCustomer1(employeeDeclare.getCustomerId()+"");

            //sheet1 - 人力收支 （带 查询条件）
            Sheet sheet1 = wb.getSheetAt(1);
            //查询
    		setDataGridforAccess(employeeDeclare, request.getParameterMap(), dataGrid);
    		List<EmployeeDeclareCopyEntity> list = dataGrid.getResults();
    		//导出
            empDeclareExportDemoforAccess(list, sheet1);
            //让sheet的求和函数生效
            sheet1.setForceFormulaRecalculation(true);


            //sheet2 - 项目收支 （带查询条件）
            Sheet sheet2 = wb.getSheetAt(2);
            List<ProjectCopyEntity> projectList = new ArrayList<ProjectCopyEntity>();
            projectService.setDataGridforAccess(projectCondition, request.getParameterMap(), dataGrid);
            projectList = dataGrid.getResults();
            projectService.projectDeclareExportDemoforAccess(projectList,sheet2);


          //sheet0 - 部门合计
            Sheet sheet0 = wb.getSheetAt(0);
            Row row = sheet0.getRow(1);
            //总收入  总毛利  总人数（人数+项目数）
            double incomeTotal = 0;
            double profitTotal = 0;
            double num = list.size()+projectList.size();
            for(EmployeeDeclareCopyEntity declare : list) {
            	incomeTotal += declare.getIncome()==null?0:declare.getIncome();
            	profitTotal += declare.getCompanyProfit()==null?0:declare.getCompanyProfit();
            }
            for(ProjectCopyEntity project : projectList) {
            	incomeTotal += project.getProjectIncome()==null?0:project.getProjectIncome();
            	profitTotal += project.getProjectProfit()==null?0:project.getProjectProfit();
            }
            row.getCell(1).setCellValue(incomeTotal);
            //人数
            row.getCell(2).setCellValue(list.size());
            //项目数
            row.getCell(3).setCellValue(projectList.size());
            //总毛利
            row.getCell(4).setCellValue(profitTotal);
            //平均毛利
            row.getCell(5).setCellValue(num==0?0:profitTotal/num);

        } catch (Exception e) {
            log.info(e.getMessage(), e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        result.put("wb", wb);
        result.put("fileName", head+formatDate(new Date(),"yyyy-MM-dd")+".xlsx");
		return result;
	}
	/**
	 * 收支申报导出模板
	 * @param list
	 * @param sheet
	 */
	public void empDeclareExportDemo(List<EmployeeDeclareEntity> list, Sheet sheet) {
		if (!list.isEmpty()) {
			//设置求和函数
			Row temp = sheet.getRow(1);
			//收入  求和
			temp.getCell(15).setCellFormula("SUM(P4:P"+(4+list.size()-1)+")");
			temp.getCell(30).setCellFormula("SUM(AE4:AE"+(4+list.size()-1)+")");
		    for (int i = 0; i < list.size();
		    		i++) {
		        Row oldRow = sheet.getRow(i + 2);
		        Row row = sheet.createRow(i + 3);
		        //设置新增行的单元格风格与上一行一样
		        row.setRowStyle(oldRow.getRowStyle());
		        short lastCellNum = oldRow.getLastCellNum();

		        for(int j = 1 ;j < lastCellNum;j++) {
		        	Cell createCell = row.createCell(j);
		        	Cell oldcell = oldRow.getCell(j);
		        	CellStyle cellStyle = oldcell.getCellStyle();
		        	CellStyle copy = sheet.getWorkbook().createCellStyle();
		        	copy.cloneStyleFrom(cellStyle);
		        	if((j>=7&&j<=9)||(j>=12&&j<=15)||(j>=20&&j<=22)||(j>=24&&j<=30)) {
		        		copy.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		        	}
		        	createCell.setCellStyle(copy);
		        }

		        EmployeeDeclareEntity employeeDeclare =list.get(i);
		        //员工信息
		        if (null != employeeDeclare.getEmployeeInfo()) {
		            row.getCell(1).setCellValue(objConvertString(employeeDeclare.getEmployeeInfo().getCode()));
		            row.getCell(2).setCellValue(objConvertString(employeeDeclare.getEmployeeInfo().getName()));
		            //部门信息
		            if (null != employeeDeclare.getEmployeeDepartment()) {
		            	TSDepart department = systemService.getEntity(TSDepart.class, employeeDeclare.getEmployeeDepartment());
		                row.getCell(3).setCellValue(objConvertString(department.getDepartname()));
		            }
		        }
//                 //客户简称
//                 row.getCell(4).setCellValue(employeeDeclare.getCustomerName());
		       //法人简称
//                 row.getCell(5).setCellValue(corporateInfo==null?null:corporateInfo.getCode());
		       Integer customerId = employeeDeclare.getCustomerInfo()==null?null:employeeDeclare.getCustomerInfo();
		       //根据客户ID获取法人信息
		       Integer corporateId = null;
		       if(employeeDeclare.getCorporateId()==null) {
		    	   corporateId = jeecgRepo.findCorporateIdByCustomerId(customerId);
		       }else {
		    	   corporateId = employeeDeclare.getCorporateId();
		       }
		       CorporateInfoEntity corporateInfo = corporateInfoRepository.findUniqueBy(CorporateInfoEntity.class, "id", corporateId);

		       if(corporateInfo!=null) {
		    	   if("北京云信".equals(corporateInfo.getCode())) {
		    		   row.getCell(4).setCellValue(employeeDeclare.getCustomerName());
		    	   }
		    	   if("江苏智蓝".equals(corporateInfo.getCode())) {
		    		   row.getCell(5).setCellValue(employeeDeclare.getCustomerName());
		    	   }
		    	   if("北京智蓝".equals(corporateInfo.getCode())) {
		    		   row.getCell(6).setCellValue(employeeDeclare.getCustomerName());
		    	   }
		       }

		        if(employeeDeclare.getUnitPriceType()!=null) {
		        	 //月单价
		            Integer unitPriceType = employeeDeclare.getUnitPriceType();
		            switch(unitPriceType) {
		            //月单价
		            	case 0: row.getCell(7).setCellValue(employeeDeclare.getUnitPrice());break;
		            	case 2: row.getCell(8).setCellValue(employeeDeclare.getUnitPrice());break;
		            	case 3: row.getCell(9).setCellValue(employeeDeclare.getUnitPrice());break;
		            }
		        }
		        //约定出勤日
		        if(employeeDeclare.getAppointedAttendanceDay()!=null)
		        row.getCell(10).setCellValue(employeeDeclare.getAppointedAttendanceDay());
		        //验收出勤日
		        if(employeeDeclare.getAcceptedAttendanceDay()!=null)
		        row.getCell(11).setCellValue(employeeDeclare.getAcceptedAttendanceDay());
		        //当月其他
		        if(employeeDeclare.getMonthOther()!=null)
		        row.getCell(12).setCellValue(employeeDeclare.getMonthOther());
		        //验收加算
		        if(employeeDeclare.getAcceptanceAdd()!=null)
		        row.getCell(13).setCellValue(employeeDeclare.getAcceptanceAdd());
		        //月间调整
		        if(employeeDeclare.getMonthAdjustment()!=null)
		        row.getCell(14).setCellValue(employeeDeclare.getMonthAdjustment());
		        //收入
		        if(employeeDeclare.getIncome()!=null)
		        row.getCell(15).setCellValue(employeeDeclare.getIncome());
		        if(employeeDeclare.getPoCode()!=null)
				row.getCell(16).setCellValue(employeeDeclare.getPoCode());

				//法定出勤日
		        if(employeeDeclare.getLegalAttendanceDay()!=null)
				row.getCell(17).setCellValue(employeeDeclare.getLegalAttendanceDay());

				//有绩效出勤日
		        if(employeeDeclare.getPerformanceAttendanceDay()!=null)
				row.getCell(18).setCellValue(employeeDeclare.getPerformanceAttendanceDay());
				//无绩效 出勤日数
		        if(employeeDeclare.getNoPerformanceAttendanceDay()!=null)
		        row.getCell(19).setCellValue(employeeDeclare.getNoPerformanceAttendanceDay());
				//基本工资（A1）
				if(employeeDeclare.getEmployeeBasePay()!=null)
				row.getCell(20).setCellValue(employeeDeclare.getEmployeeBasePay());
				//A标准工资
				Double aStandardSalary = employeeDeclare.getEmployeeASalary()==null?0:employeeDeclare.getEmployeeASalary();
				//绩效工资（A2）
				Double meritPay = aStandardSalary-(employeeDeclare.getEmployeeBasePay()==null?0:employeeDeclare.getEmployeeBasePay());
				row.getCell(21).setCellValue(meritPay);
				//标准工资（A）
				Double basePay = employeeDeclare.getEmployeeBasePay()==null?0:employeeDeclare.getEmployeeBasePay();
				row.getCell(22).setCellValue(aStandardSalary);
				//B折扣率
				Integer bDiscount = employeeDeclare.getBDiscount()==null?0:employeeDeclare.getBDiscount();
//				java.text.DecimalFormat percentFormat =new java.text.DecimalFormat();
				double decimal = bDiscount/100.0;
				row.getCell(23).setCellValue(decimal);;

				//应付工资
				Double legalAttendanceDay = employeeDeclare.getLegalAttendanceDay()==null?0:employeeDeclare.getLegalAttendanceDay();
				if(legalAttendanceDay!=0) {
					Double performanceDays = employeeDeclare.getPerformanceAttendanceDay()==null?0:employeeDeclare.getPerformanceAttendanceDay();
					Double noPerformanceDays = employeeDeclare.getNoPerformanceAttendanceDay()==null?0:employeeDeclare.getNoPerformanceAttendanceDay();

					//应付 基本工资  （有效+无效）/法定*基本工资*折扣率
					Double payableBaseSalary = (performanceDays+noPerformanceDays)/legalAttendanceDay*basePay*bDiscount/100;
					row.getCell(24).setCellValue(payableBaseSalary);
					//应付 绩效    有效/法定*基本工资*折扣率
					Double payablePerformance = performanceDays/legalAttendanceDay*meritPay*bDiscount/100;
					row.getCell(25).setCellValue(payablePerformance);
				}
				//C1电脑补贴
				Double cComputerSubsidy = employeeDeclare.getCComputerSubsidy()==null?0:employeeDeclare.getCComputerSubsidy();
				row.getCell(26).setCellValue(cComputerSubsidy);
				//C2加班费
				Double cOvertimeSalary = employeeDeclare.getCOvertimeSalary()==null?0:employeeDeclare.getCOvertimeSalary();
				row.getCell(27).setCellValue(cOvertimeSalary);

				//C3其他补贴
				Double c1OtherSubsidy = employeeDeclare.getC1OtherSubsidy()==null?0:employeeDeclare.getC1OtherSubsidy();
				Double c2OtherSubsidy = employeeDeclare.getC2OtherSubsidy()==null?0:employeeDeclare.getC2OtherSubsidy();
				Double c3OtherSubsidy = employeeDeclare.getC3OtherSubsidy()==null?0:employeeDeclare.getC3OtherSubsidy();
				Double cOtherSubsidy = c1OtherSubsidy + c2OtherSubsidy + c3OtherSubsidy;
				row.getCell(28).setCellValue(cOtherSubsidy);
				//C总补贴
				Double cTotalSubsidy = cComputerSubsidy + cOvertimeSalary + cOtherSubsidy;
				row.getCell(29).setCellValue(cTotalSubsidy);
				//应发合计  payableSalary
				if(employeeDeclare.getPayableSalary()!=null)
				row.getCell(30).setCellValue(employeeDeclare.getPayableSalary());

				String c1OtherSubsidyRemark = employeeDeclare.getC1OtherSubsidyRemark();
				String c2OtherSubsidyRemark = employeeDeclare.getC2OtherSubsidyRemark();
				String c3OtherSubsidyRemark = employeeDeclare.getC3OtherSubsidyRemark();
				StringBuffer sb = new StringBuffer();
				sb.append(c1OtherSubsidyRemark==null?"":c1OtherSubsidyRemark+" ");
				sb.append(c2OtherSubsidyRemark==null?"":c2OtherSubsidyRemark+" ");
				sb.append(c3OtherSubsidyRemark==null?"":c3OtherSubsidyRemark+" ");
				row.getCell(31).setCellValue(sb.toString());
		    }
		}
	}


	/**
	 * 收支申报导出模板	-工资单导出专用
	 * @param list
	 * @param sheet
	 */
	public void empDeclareSalaryExportSelectedIdsDemo(List<EmployeeDeclareEntity> list, Sheet sheet) {
		if (!list.isEmpty()) {
			//设置求和函数
//			Row temp = sheet.getRow(1);
			//收入  求和
//			temp.getCell(15).setCellFormula("SUM(P4:P"+(4+list.size()-1)+")");
			for (int i = 0; i < list.size(); i++) {
				Row oldRow = sheet.getRow(i + 1);
				Row row = sheet.createRow(i + 2);
				//设置新增行的单元格风格与上一行一样
				row.setRowStyle(oldRow.getRowStyle());
				short lastCellNum = oldRow.getLastCellNum();
				for(int j = 1 ;j < lastCellNum;j++) {
					Cell createCell = row.createCell(j);
		        	Cell oldcell = oldRow.getCell(j);
		        	CellStyle cellStyle = oldcell.getCellStyle();
		        	CellStyle copy = sheet.getWorkbook().createCellStyle();
		        	copy.cloneStyleFrom(cellStyle);
		        	if(j>=5&&j<=9) {
		        		copy.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		        	}
		        	createCell.setCellStyle(copy);
				}

				EmployeeDeclareEntity employeeDeclare =list.get(i);
				//员工信息
				if (null != employeeDeclare.getEmployeeInfo()) {
					row.getCell(1).setCellValue(objConvertString(employeeDeclare.getEmployeeInfo().getName()));
					row.getCell(2).setCellValue(objConvertString(employeeDeclare.getEmployeeInfo().getCode()));
					//部门信息
					if (null != employeeDeclare.getEmployeeDepartment()) {
						TSDepart department = systemService.getEntity(TSDepart.class, employeeDeclare.getEmployeeDepartment());
						row.getCell(3).setCellValue(objConvertString(department.getDepartname()));
					}
						row.getCell(4).setCellValue(objConvertString(employeeDeclare.getEmployeeInfo().getSixGoldCity()));
					if (null != employeeDeclare.getSixPersonalBurdenOne()) {
						row.getCell(5).setCellValue(objConvertString(employeeDeclare.getSixPersonalBurdenOne()));
					}
				}
				Double perToneTaxOne=employeeDeclare.getPerToneTaxOne()==null?0:employeeDeclare.getPerToneTaxOne();
				Double perToneTaxTwo=employeeDeclare.getPerToneTaxTwo()==null?0:employeeDeclare.getPerToneTaxTwo();
				Double perToneTax=perToneTaxOne+perToneTaxTwo;
				row.getCell(6).setCellValue(perToneTax);
				//C1电脑补贴
				Double cComputerSubsidy = employeeDeclare.getCComputerSubsidy()==null?0:employeeDeclare.getCComputerSubsidy();
				//C2加班费
				Double cOvertimeSalary = employeeDeclare.getCOvertimeSalary()==null?0:employeeDeclare.getCOvertimeSalary();

				//C3其他补贴
				Double c1OtherSubsidy = employeeDeclare.getC1OtherSubsidy()==null?0:employeeDeclare.getC1OtherSubsidy();
				Double c2OtherSubsidy = employeeDeclare.getC2OtherSubsidy()==null?0:employeeDeclare.getC2OtherSubsidy();
				Double c3OtherSubsidy = employeeDeclare.getC3OtherSubsidy()==null?0:employeeDeclare.getC3OtherSubsidy();
				Double cOtherSubsidy = c1OtherSubsidy + c2OtherSubsidy + c3OtherSubsidy;
				//C总补贴
				Double cTotalSubsidy = cComputerSubsidy + cOvertimeSalary + cOtherSubsidy;
				row.getCell(7).setCellValue(cTotalSubsidy);
				//月到手
				if(employeeDeclare.getEmployeeRealSalary()!=null){
					row.getCell(8).setCellValue(employeeDeclare.getEmployeeRealSalary());
				}
			}
		}
	}
	/**
	 * 收支申报导出模板 -工资单导出专用
	 * @param list
	 * @param sheet
	 */
	public void empDeclareSalaryExportDemo(List<EmployeeDeclareEntity> list, Sheet sheet) {
		if (!list.isEmpty()) {
			//设置求和函数
//			Row temp = sheet.getRow(1);
			//收入  求和
//			temp.getCell(15).setCellFormula("SUM(P4:P"+(4+list.size()-1)+")");
			for (int i = 0; i < list.size(); i++) {
				Row oldRow = sheet.getRow(i + 1);
				Row row = sheet.createRow(i + 2);
				//设置新增行的单元格风格与上一行一样
				row.setRowStyle(oldRow.getRowStyle());
				short lastCellNum = oldRow.getLastCellNum();
				for(int j = 1 ;j < lastCellNum;j++) {
					Cell createCell = row.createCell(j);
		        	Cell oldcell = oldRow.getCell(j);
		        	CellStyle cellStyle = oldcell.getCellStyle();
		        	CellStyle copy = sheet.getWorkbook().createCellStyle();
		        	copy.cloneStyleFrom(cellStyle);
		        	if(j>=5&&j<=9) {
		        		copy.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		        	}
		        	createCell.setCellStyle(copy);
				}

				EmployeeDeclareEntity employeeDeclare =list.get(i);
				//员工信息
				if (null != employeeDeclare.getEmployeeInfo().getId()) {
					EmployeeInfoEntity employeeInfo = commonService.getEntity(EmployeeInfoEntity.class, employeeDeclare.getEmployeeInfo().getId());
					row.getCell(1).setCellValue(objConvertString(employeeInfo.getName()));
					row.getCell(2).setCellValue(objConvertString(employeeInfo.getCode()));
					//部门信息
					if (null != employeeDeclare.getEmployeeDepartment()) {
						TSDepart department = systemService.getEntity(TSDepart.class, employeeDeclare.getEmployeeDepartment());
						row.getCell(3).setCellValue(objConvertString(department.getDepartname()));
					}
					if (null != employeeInfo.getSixGoldCity()) {
						row.getCell(4).setCellValue(objConvertString(employeeInfo.getSixGoldCity()));
					}
					if (null != employeeDeclare.getSixPersonalBurdenOne()) {
						row.getCell(5).setCellValue(employeeDeclare.getSixPersonalBurdenOne());
					}
				}
				Double perToneTaxOne=employeeDeclare.getPerToneTaxOne()==null?0:employeeDeclare.getPerToneTaxOne();
				Double perToneTaxTwo=employeeDeclare.getPerToneTaxTwo()==null?0:employeeDeclare.getPerToneTaxTwo();
				Double perToneTax=perToneTaxOne+perToneTaxTwo;
				row.getCell(6).setCellValue(perToneTax);
				//C1电脑补贴
				Double cComputerSubsidy = employeeDeclare.getCComputerSubsidy()==null?0:employeeDeclare.getCComputerSubsidy();
				//C2加班费
				System.out.println(employeeDeclare.getCOvertimeSalary());
				Double cOvertimeSalary = employeeDeclare.getCOvertimeSalary()==null?0:employeeDeclare.getCOvertimeSalary();

				//C3其他补贴
				Double c1OtherSubsidy = employeeDeclare.getC1OtherSubsidy()==null?0:employeeDeclare.getC1OtherSubsidy();
				Double c2OtherSubsidy = employeeDeclare.getC2OtherSubsidy()==null?0:employeeDeclare.getC2OtherSubsidy();
				Double c3OtherSubsidy = employeeDeclare.getC3OtherSubsidy()==null?0:employeeDeclare.getC3OtherSubsidy();
				Double cOtherSubsidy = c1OtherSubsidy + c2OtherSubsidy + c3OtherSubsidy;
				//C总补贴
				Double cTotalSubsidy = cComputerSubsidy + cOvertimeSalary + cOtherSubsidy;
				row.getCell(7).setCellValue(cTotalSubsidy);
				//月到手
				if(employeeDeclare.getEmployeeRealSalary()!=null){
					row.getCell(8).setCellValue(employeeDeclare.getEmployeeRealSalary());
				}

			}
		}
	}
	/**
	 * 统计待处理消息
	 * @param user
	 * @return
	 */
	public int getMyMessageCount(TSUser user) {
		// TODO Auto-generated method stub
		String role = employeeDeclareRepo.findMyRole(user.getId());
		int count = 0;
		if(role.contains("check")) {
			count = employeeDeclareRepo.getSpecialRoleMessageCount(true);
		}else if(role.contains("control")){
			count = employeeDeclareRepo.getSpecialRoleMessageCount(false);
		}else if("t_report".equals(role)){
			TSDepart currentDepart = jeecgRepo.getCurrentDepart(user);
			Integer initNum = jeecgRepo.getDeclareInitStatusNumber(user);
			count = employeeDeclareRepo.getMyMessageCount(currentDepart.getOrgCode(),initNum);
		}else {
			TSDepart currentDepart = jeecgRepo.getCurrentDepart(user);
			Integer initNum = jeecgRepo.getDeclareInitStatusNumber(user);
			count = employeeDeclareRepo.getMyMessageCount(currentDepart.getOrgCode(),initNum,user.getUserName());
		}
		return count;
	}

	/**
	 * 一键统计-人力收支
	 * @param request
	 * @param declareStatus  查询条件-申报状态
	 * @param employeeDeclare 查询条件集合-employeeDeclare
	 * @param dataGrid	返回结果
	 */
	public Map<String, Double> oneKeyTotalRL(HttpServletRequest request, String declareStatus, EmployeeDeclareEntity employeeDeclare,
			DataGrid dataGrid) {
		setDataGridByStatus(request, declareStatus, employeeDeclare, dataGrid);
		Map<String,Double> map = new HashMap<String,Double>();
		List<EmployeeDeclareEntity> list = dataGrid.getResults();
		if(list ==null || list.size() == 0) {
        	map.put("incomeTotal", 0.0);
            map.put("profitTotal", 0.0);
            map.put("peopleTotal", 0.0);
        }else {
        	double incomeTotal = 0.0;
        	double profitTotal = 0.0;
        	for(EmployeeDeclareEntity declare : list) {
        		double income = declare.getIncome()==null?0.0:declare.getIncome();
        		double profit = declare.getCompanyProfit()==null?0.0:declare.getCompanyProfit();
        		incomeTotal+=income;
        		profitTotal+=profit;
        	}
        	double peopleTotal = list.size();
            map.put("incomeTotal", incomeTotal);
            map.put("profitTotal", profitTotal);
            map.put("peopleTotal", peopleTotal);
        }
		return map;
	}

	/**
	 * 一键统计-人力全部 -访客专属
	 * @param request
	 * @param employeeDeclare
	 * @param dataGrid
	 * @return
	 */
	public Map<String, Double> oneKeyTotalRLByVisitor(HttpServletRequest request, EmployeeDeclareCopyEntity employeeDeclareCopy,
			DataGrid dataGrid) {
		Map<String,Double> map = new HashMap<String,Double>();
		setDataGridforAccess(employeeDeclareCopy, request.getParameterMap(), dataGrid);
		List<EmployeeDeclareCopyEntity> list = dataGrid.getResults();
		if(list ==null || list.size() == 0) {
        	map.put("incomeTotal", 0.0);
            map.put("profitTotal", 0.0);
            map.put("peopleTotal", 0.0);
        }else {
        	double incomeTotal = 0.0;
        	double profitTotal = 0.0;
        	for(EmployeeDeclareCopyEntity declare : list) {
        		double income = declare.getIncome()==null?0.0:declare.getIncome();
        		double profit = declare.getCompanyProfit()==null?0.0:declare.getCompanyProfit();
        		incomeTotal+=income;
        		profitTotal+=profit;
        	}
        	double peopleTotal = list.size();
            map.put("incomeTotal", incomeTotal);
            map.put("profitTotal", profitTotal);
            map.put("peopleTotal", peopleTotal);
        }
		return map;
	}
	/**
	 * 访客 - 一键统计人力全部
	 * 供访客项目页面一键统计部门调用
	 * @param date
	 * @param department
	 * @param customerId
	 * @return
	 */
	public Map<String, Double> oneKeyTotalRLByVisitor(Date date, String departId, int customerId) {
		// TODO Auto-generated method stub
		Map<String,Double> map = new HashMap<String,Double>();

		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSDepart curDepart = jeecgRepo.getCurrentDepart(user);
		String orgCode = curDepart.getOrgCode();
		Integer[] empFlags = new Integer[2];
		if(departId == null || "".equals(departId)||"undefined".equals(departId)) {  // 部门查询框未选中
			orgCode = orgCode.substring(0,3);
			empFlags[0]=0;
		}else {	//部门查询框选中  访客只能看外派
			TSDepart findDepart = commonService.findUniqueByProperty(TSDepart.class, "id", departId);
			orgCode = findDepart.getOrgCode();
			empFlags[0]=0;
		}
		Object oneKeyTotal = employeeDeclareRepo.oneKeyTotalforAccess(date, orgCode, empFlags,customerId);
		Object[] obj = (Object[]) oneKeyTotal;
		if(obj[0]==null&&obj[2]==null) {
			map.put("incomeTotal", 0.0);
            map.put("profitTotal", 0.0);
            map.put("peopleTotal", 0.0);
		}else{
			map.put("incomeTotal", obj[0]==null?0:Double.parseDouble(obj[0].toString()));
            map.put("profitTotal", obj[1]==null?0:Double.parseDouble(obj[1].toString()));
            map.put("peopleTotal", Double.parseDouble(obj[2].toString()));
		}
		return map;
	}
	/**
	 * datagrid查询 - 加入申报状态包装查询
	 * @param request
	 * @param declareStatus
	 * @param employeeDeclare
	 * @param dataGrid
	 */
	public void setDataGridByStatus(HttpServletRequest request, String declareStatus,
			EmployeeDeclareEntity employeeDeclare, DataGrid dataGrid) {
		if(declareStatus==null||"".equals(declareStatus)||"undefined".equals(declareStatus)) {
			setDataGrid(employeeDeclare, request.getParameterMap(), dataGrid, null, 0,0);
		} else { //有选择申报状态
			TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
			Integer initStatus = jeecgRepo.getDeclareInitStatusNumber(user);
			int selectStatus = Integer.parseInt(declareStatus);
			if(selectStatus==initStatus+1) { //查询 未上报
				setDataGrid(employeeDeclare, request.getParameterMap(), dataGrid, new Integer[]{initStatus+1}, 0,2);
	        }else if(selectStatus==initStatus-3){//查询已上报
				setDataGrid(employeeDeclare, request.getParameterMap(), dataGrid, new Integer[]{initStatus-3}, 0,1);
	        }else {
	        	employeeDeclare.setDeclareStatus(selectStatus);
	        	setDataGrid(employeeDeclare, request.getParameterMap(), dataGrid, null, 0,0);
	        }
		}
	}
	/**
	 * 查询当前用户所在部门下所有人力收支（包括子部门）
	 * @param user
	 * @return
	 */
	public List<EmployeeDeclareEntity> findAllByCurrentDept(TSUser user) {
		List<EmployeeDeclareEntity> result = new ArrayList<EmployeeDeclareEntity>();
		TSDepart currentDepart = jeecgRepo.getCurrentDepart(user);
		List<TSDepart> subdivision = jeecgRepo.findSubDepart(currentDepart);
		for(TSDepart depart : subdivision) {
			List<EmployeeDeclareEntity> list = commonService.findByProperty(EmployeeDeclareEntity.class, "employeeDepartment", depart.getId());
			result.addAll(list);
		}
		return result;
	}
	/**
	 * 通过部门ID 查询该部门下的所有人力收支（包括子部门）
	 * @param projectDepartment
	 * @return
	 */
	public List<EmployeeDeclareEntity> findAllByCurrentDept(String departId) {
		List<EmployeeDeclareEntity> result = new ArrayList<EmployeeDeclareEntity>();
		TSDepart currentDepart = commonService.getEntity(TSDepart.class, departId);
		List<TSDepart> subdivision = jeecgRepo.findSubDepart(currentDepart);
		for(TSDepart depart : subdivision) {
			List<EmployeeDeclareEntity> list = commonService.findByProperty(EmployeeDeclareEntity.class, "employeeDepartment", depart.getId());
			result.addAll(list);
		}
		return result;
	}
}