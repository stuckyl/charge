package com.charge.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.controller.core.LoginController;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;

import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.SixGoldEntity;
import com.charge.entity.SixGoldScaleEntity;
import com.charge.repository.CommonRepository;
import com.charge.repository.EmployeeDeclareRepository;
import com.charge.service.EmployeeDeclareCopyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Title: Controller
 * @Description: 社保六金
 * @author wenst
 * @date 2018-03-20 09:31:32
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/sixGoldController")
public class SixGoldController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SixGoldController.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EmployeeDeclareRepository employeeDeclareRepo;
	@Autowired
	private EmployeeDeclareCopyService employeeDeclareCopyService;
	@Autowired
	private CommonRepository commonRepository;

	/**
	 * 社保六金列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/charge/sixGoldList");
	}

	/**
	 * easyui AJAX请求数据
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(SixGoldEntity sixGold,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String month;
		if(request.getParameter("month")!=null&&!"".equals(request.getParameter("month"))) {
			month = request.getParameter("month");
		}else {
			Date date = timeCondition==null?new Date():timeCondition;
			month = sdf.format(date);
		}
		try {
			Date date = sdf.parse(month);
			sixGold.setEnterDate(date);
			timeCondition=date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//加入姓名查询 0824
		if(sixGold.getEmployeeName()!=null) {
			sixGold.setEmployeeName("*"+sixGold.getEmployeeName()+"*");
		}

		CriteriaQuery cq = new CriteriaQuery(SixGoldEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sixGold, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除社保六金
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(SixGoldEntity sixGold, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sixGold = systemService.getEntity(SixGoldEntity.class, sixGold.getId());
		message = "社保六金删除成功";
		commonService.delete(sixGold);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}


	/**
	 * 添加社保六金
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(SixGoldEntity sixGold, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "社保六金更新成功";
		List<SixGoldEntity> list = commonService.findHql("from SixGoldEntity t where t.enterDate=? and t.employeeCode=?",sixGold.getEnterDate(), sixGold.getEmployeeCode());
		SixGoldEntity t = (list.size()==0?null:list.get(0));
		if(t != null) {
			try {
				if(!sixGold.getCompanySum().equals(t.getCompanySum())||!sixGold.getPersonalSum().equals(t.getPersonalSum())) {
					MyBeanUtils.copyBeanNotNull2Bean(sixGold, t);
					commonService.saveOrUpdate(t);
					systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
					modifyEmployeeDeclare(sixGold);
				}
			} catch (Exception e) {
				e.printStackTrace();
				message = "社保六金更新失败";
			}
		}else {
			commonService.save(sixGold);
			modifyEmployeeDeclare(sixGold);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * 导入六金后 更新 员工申报时的六金信息
	 * @param sixGold
	 */
	private void modifyEmployeeDeclare(SixGoldEntity sixGold) {
		String employeeCode = sixGold.getEmployeeCode();
		Date enterDate = sixGold.getEnterDate();
		List<EmployeeDeclareEntity> list = employeeDeclareRepo.findByEmployeeCodeAndMoth(employeeCode,enterDate);
		//防止 预先生成 未调整薪资的收支申报
		EmployeeDeclareEntity employeeDeclare = list.size()==0?null:list.get(list.size()-1);
		if(employeeDeclare != null && employeeDeclare.getId()!=null) {

			Double companySum = sixGold.getCompanySum();
			Double sixCompanyBurdenOne = employeeDeclare.getSixCompanyBurdenOne();
			Double personalSum = sixGold.getPersonalSum();
			Double sixPersonalBurdenOne = employeeDeclare.getSixPersonalBurdenOne();
			if(companySum.equals(sixCompanyBurdenOne)&&personalSum.equals(sixPersonalBurdenOne)) {
			}else { //替换并重新计算
				employeeDeclare.setSixCompanyBurdenOne(companySum);
				employeeDeclare.setSixPersonalBurdenOne(personalSum);
				//重新计算收支
				commonRepository.calEmployeeDeclare(employeeDeclare);
				if(employeeDeclare.getBaceUpFlag()!=null){
					if(employeeDeclare.getBaceUpFlag()==1)
					employeeDeclareCopyService.employeeBackUp(employeeDeclare);
				}
//				employeeDeclare.setDeclareReturnreason("实缴六金变动");
				commonService.saveOrUpdate(employeeDeclare);
			}
		}
	}
	/**
	 * 社保六金列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(SixGoldEntity sixGold, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sixGold.getId())) {
			sixGold = commonService.getEntity(SixGoldEntity.class, sixGold.getId());
			req.setAttribute("sixGoldPage", sixGold);
		}
		return new ModelAndView("com/charge/sixGold");
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SixGoldEntity> list() {
		List<SixGoldEntity> listSixGolds=commonService.getList(SixGoldEntity.class);
//		LoginController dd =null;
		return listSixGolds;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SixGoldEntity task = commonService.get(SixGoldEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SixGoldEntity sixGold, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SixGoldEntity>> failures = validator.validate(sixGold);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.save(sixGold);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		Integer id = sixGold.getId();
		URI uri = uriBuilder.path("/rest/sixGoldController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SixGoldEntity sixGold) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SixGoldEntity>> failures = validator.validate(sixGold);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.saveOrUpdate(sixGold);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		commonService.deleteEntityById(SixGoldEntity.class, id);
	}

	/**
	 * 六金excel导入
	 * @param empFile
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(params="sixGoldImport")
	public ResponseEntity<Map<String,Object>> sixGoldImport(
			@RequestParam("sgFile") MultipartFile sgFile,
			HttpServletRequest request,
			HttpServletResponse response,
			String month
			){

		Map<String,Object> result = new LinkedHashMap<String,Object>();
		Workbook wb = null;
		boolean flag = true;
		int success = 0;
		int error = 0;

		Map<Integer,String> errReason = new LinkedHashMap<Integer,String>();
		List<String> notMyEmployee = new ArrayList<String>();
		List<Integer> errID = new ArrayList<Integer>();

		try {
			if(!sgFile.isEmpty()){

				wb = WorkbookFactory.create(sgFile.getInputStream());
				Sheet sheet = wb.getSheetAt(0);

				int lastRowNum = sheet.getLastRowNum(); //从0 开始
				Row row = null;
				Iterator<Row> rows = sheet.iterator();
//				row = rows.next();
				int beginRow = 0;
				short lastCellNum = 0;
				int beginColum = 0;
				boolean mark = false;
				//判断第几行开始有数据
				while(beginRow<=lastRowNum) {
					if(rows.hasNext()){
						row = rows.next();
						//判断第几列开始有数据
						lastCellNum = row.getLastCellNum();
						beginColum = 0;
						while(beginColum<=lastCellNum) {
							try {
								Cell cell1 = row.getCell(beginColum);
								if(cell1!=null) cell1.setCellType(Cell.CELL_TYPE_STRING);
								if(cell1==null||cell1.getStringCellValue()==null||"".equals(cell1.getStringCellValue())) {
									beginColum++;
								}else {
									mark = true;
									break;
								}
							}catch(Exception e) {
								result.put("errCode", -1);
								result.put("errMsg", "第"+beginRow+"行第"+beginColum+"列单元格格式有误");
								return new ResponseEntity<>(result,HttpStatus.OK);
							}
						}
						if(mark) {
							break;
						}
						beginRow++;
					}else if(!rows.hasNext()&&beginRow==0){ //没有一行数据，表格为空
						result.put("errCode", -1);
						result.put("errMsg", "您导入的表格为空");
						return new ResponseEntity<>(result,HttpStatus.OK);
					}else {
						break;
					}
				}
				try{
					row.getCell(beginColum+1).setCellType(Cell.CELL_TYPE_STRING);
					String stringCellValue = row.getCell(beginColum+1).getStringCellValue();
					int physicalNumberOfCells = row.getPhysicalNumberOfCells();
					if(row.getCell(beginColum)==null||!"ID".equals(stringCellValue)||physicalNumberOfCells!=15){
						result.put("errCode", -1);
						result.put("errMsg", "请导入员工六金信息的正确模板：表头共有15列，第一列为缴纳月数，第二列为员工ID");
						return new ResponseEntity<>(result,HttpStatus.OK);
					}
				}catch(Exception e) {
					result.put("errCode", -1);
					result.put("errMsg", "第"+beginRow+"行第"+beginColum+"列单元格格式有误");
					return new ResponseEntity<>(result,HttpStatus.OK);
				}

				//跳过第二行
				row = rows.next();

				List<String> employeeCodeList = new ArrayList<String>();
				List<String> repeatCodeList = new ArrayList<String>();
				//判断身份证是否有重复的
				while(rows.hasNext()){
					row = rows.next();
					//身份证在 第二列
					int idNum = beginColum;
					try{
						Cell cell = row.getCell(idNum+1);
						cell.setCellType(Cell.CELL_TYPE_STRING);
						if(cell==null||cell.getStringCellValue()==null||"".equals(cell.getStringCellValue())) continue;
						String employeeCode = cell.getStringCellValue();
						if(employeeCodeList.indexOf(employeeCode)==-1){ //不包含 此 身份证号
							employeeCodeList.add(employeeCode);
						}else {
							repeatCodeList.add(""+(row.getRowNum()+1));
						}
					}catch(Exception e) {
						result.put("errCode", -1);
						result.put("errMsg", "第"+beginRow+"行第"+beginColum+"列单元格格式有误");
						return new ResponseEntity<>(result,HttpStatus.OK);
					}
				}
				if(repeatCodeList.size()!=0) {
					result.put("errCode", -1);
					result.put("errMsg", "表中存在重复身份证，重复行数："+repeatCodeList.toString()+"<br>请先去除重复身份证的员工信息再导入");
					return new ResponseEntity<>(result,HttpStatus.OK);
				}

				//第二次 读取 插入数据库
				rows = wb.getSheetAt(0).iterator();
				int i=0;
				while(rows.hasNext()&&i<beginRow) {
					rows.next();
					i++;
				}
				//跳过前两行
				row = rows.next();
				row = rows.next();

				List<String> list = new ArrayList<String>();
				list.add("缴纳月数");
				list.add("ID");
				list.add("员工");
				list.add("养老保险企业");
				list.add("养老保险个人");
				list.add("医疗保险企业");
				list.add("医疗保险个人");
				list.add("失业保险企业");
				list.add("失业保险个人");
				list.add("工伤企业");
				list.add("生育个人");
				list.add("住房公积金企业");
				list.add("住房公积金个人");
				list.add("企业合计");
				list.add("个人合计");

				while(rows.hasNext()){
					row = rows.next();
					int rowNum = row.getRowNum()+1;
					int num = beginColum;
					try {
						Cell cell2 = row.getCell(beginColum);
						Cell cell3 = row.getCell(beginColum+1);
						if(cell2!=null) {
							if(cell2.getCellType()!=0) {
								if(cell2.getStringCellValue()==null||"".equals(cell2.getStringCellValue())) continue;
							}else  {
								if(cell2.getNumericCellValue()==0)continue;
							}
						}else {
							continue;
						}
					}catch (Exception e) {
						result.put("errCode", -1);
						result.put("errMsg", "第"+rowNum+"行第"+num+"列单元格格式有误");
						return new ResponseEntity<>(result,HttpStatus.OK);
					}
					try {
						//缴纳月数
						Cell cell = row.getCell(num);
						Double numMonth;
						if(cell.getCellType()==0) { //数字格式
							numMonth = row.getCell(num).getNumericCellValue();num++;
						}else {
							numMonth = Double.parseDouble(row.getCell(num).getStringCellValue());num++;
						}
						cell =row.getCell(num);
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String employeeCode = cell.getStringCellValue();
						if(!CommonRepository.isValidIdNo(employeeCode)) {
							errID.add(row.getRowNum());
							continue;
						}
						num++;
						String employeeName = row.getCell(num).getStringCellValue();
						List<EmployeeInfoEntity> employeeInfo = commonService.findByProperty(EmployeeInfoEntity.class, "code", employeeCode);
						//不再系统中 或者 未入保 或者 已退保的人 无法导入  （已退保也可导入）
						if(employeeInfo.size()==0||employeeInfo.get(employeeInfo.size()-1).getInsurance()==0) {
							notMyEmployee.add(employeeName+",");
							error++;
							continue;
						}
						num++;
						//养老保险（企业/个人）
						Double companyEndowment = row.getCell(num).getNumericCellValue();num++;
						Double personalEndowment = row.getCell(num).getNumericCellValue();num++;
						//医疗保险
						Double companyMedical = row.getCell(num).getNumericCellValue();num++;
						Double personalMedical = row.getCell(num).getNumericCellValue();num++;
						//失业保险
						Double companyUnemployment = row.getCell(num).getNumericCellValue();num++;
						Double personalUnemployment = row.getCell(num).getNumericCellValue();num++;

						//工伤
						Double companyInjury = row.getCell(num).getNumericCellValue();num++;
						//生育
						Double companyMaternity = row.getCell(num).getNumericCellValue();num++;

						//住房 公积金
						Double companyHousingFund = row.getCell(num).getNumericCellValue();num++;
						Double personalHousingFund = row.getCell(num).getNumericCellValue();num++;

						//总和
						Double companySum = row.getCell(num).getNumericCellValue();num++;
						DecimalFormat df= new DecimalFormat("######0.00");
						Double SUM1 = Double.parseDouble(df.format(companySum));
						Double personalSum = row.getCell(num).getNumericCellValue();num++;
						Double SUM2 = Double.parseDouble(df.format(personalSum));

						SixGoldEntity sixGold = new SixGoldEntity();

						sixGold.setNumMonth(numMonth.intValue());
						sixGold.setEmployeeCode(employeeCode);
						sixGold.setEmployeeName(employeeName);
						/*
						 * SixGoldEntity 没有 员工姓名 属性 ，暂 未插入
						 * excel表格第二个 ID 初步 认为是 employeeCode
						 */
						sixGold.setCompanyEndowment(companyEndowment);
						sixGold.setPersonalEndowment(personalEndowment);
						sixGold.setCompanyMedical(companyMedical);
						sixGold.setPersonalMedical(personalMedical);
						sixGold.setCompanyUnemployment(companyUnemployment);
						sixGold.setPersonalUnemployment(personalUnemployment);
						sixGold.setCompanyInjury(companyInjury);
						sixGold.setCompanyMaternity(companyMaternity);
						sixGold.setCompanyHousingFund(companyHousingFund);
						sixGold.setPersonalHousingFund(personalHousingFund);
						sixGold.setCompanySum(SUM1);
						sixGold.setPersonalSum(SUM2);
						SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM");
						sixGold.setEnterDate(sdft.parse(month));
						//删除标记
						sixGold.setDelFlg(0);
						//创建更新信息
						TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
						sixGold.setCreatedBy(user.getUserName());
						sixGold.setCreatedDate(new Date());
						sixGold.setLastModifiedBy(user.getUserName());
						sixGold.setLastModifiedDate(new Date());
						//保存
							AjaxJson aj =  save(sixGold,request);
							if(aj.isSuccess()){
								success ++;
							}else{
								error++;
							}

					} catch (Exception e) {
						logger.error(e.getMessage(),e);
						error++;
						errReason.put(rowNum, list.get(num-beginColum));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			flag = false;
		}
		if(flag){
			result.put("errCode", 0);
			StringBuffer nme = new StringBuffer();
			StringBuffer eId = new StringBuffer();
			eId.append("第");
			for(Integer rnum : errID) eId.append(rnum+"行"); eId.append("的身份证号错误;");
//			nme.append("员工库中不存在");
			for(String str : notMyEmployee) {
				nme.append(str);
			}
			nme.append("未入保或不在系统中，禁止导入；");
			if(errReason.size()==0) {
				result.put("errMsg","成功导入："+success+"条。"+(notMyEmployee.size()==0?"":nme.toString())+(errID.size()==0?"":eId.toString()));
			}else {
				StringBuffer sb = new StringBuffer();
				sb.append("单元格格式存在错误。错误详情：\n");
				Set<Entry<Integer, String>> entrySet = errReason.entrySet();
				Iterator<Entry<Integer, String>> iterator = entrySet.iterator();
				int count=0;
				while(iterator.hasNext()) {
					Entry<Integer, String> next = iterator.next();
					count++;
					sb.append("第"+next.getKey()+"行,"+next.getValue()+"列;");
					if(count>9) {
						sb.append("......");
						break;
					}
				}
				result.put("errMsg","失败："+error+"条。\n"+(notMyEmployee.size()==0?"":nme.toString())+(errID.size()==0?"":eId.toString())+sb.toString()+"\n成功导入："+success+"条");
			}
		}else{
			result.put("errCode", -1);
			result.put("errMsg", "数据库异常，请联系管理员");
		}
		return new ResponseEntity<>(result,HttpStatus.OK);
	}


	@RequestMapping(params = "clearMonth")
	public void clearMonth() {
		this.timeCondition=null;
	}

	private Date timeCondition;
	/**
	 * 根据月份返回当月六金信息
	 *
	 */
	@RequestMapping(params = "sixGoldFindbyMonth")
	public void sixGoldFindbyMonth(SixGoldEntity sixGold,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String month;
		if(request.getParameter("month")!=null&&!"".equals(request.getParameter("month"))) {
			month = request.getParameter("month");
		}else {
			Date date = timeCondition==null?new Date():timeCondition;
			month = sdf.format(date);
		}
		try {
			Date date = sdf.parse(month);
			sixGold.setEnterDate(date);
			timeCondition=date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(sixGold.getEmployeeName()!=null) {
			sixGold.setEmployeeName("*"+sixGold.getEmployeeName()+"*");
		}
		dataGrid.setField("id,numMonth,employeeCode,employeeName,enterDate,companyEndowment,personalEndowment,companyMedical,personalMedical,companyUnemployment,personalUnemployment,companyInjury,companyMaternity,companyHousingFund,personalHousingFund,companySum,personalSum,");
		CriteriaQuery cq = new CriteriaQuery(SixGoldEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sixGold, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

}
