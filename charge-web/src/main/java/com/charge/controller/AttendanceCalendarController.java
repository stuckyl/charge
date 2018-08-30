package com.charge.controller;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.Else;
import com.charge.entity.AttendanceCalendarEntity;
import com.charge.entity.CustomerInfoEntity;
import com.charge.repository.AttendanceCalendarRepository;
import com.charge.service.AttendanceCalendarService;

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

import static org.hamcrest.Matchers.stringContainsInOrder;

import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Title: Controller
 * @Description: 出勤日历表
 * @author wenst
 * @date 2018-03-20 14:04:44
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/attendanceCalendarController")
public class AttendanceCalendarController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AttendanceCalendarController.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CommonService commonService;
	@Autowired
	private AttendanceCalendarService attendanceCalendarService;

	/**
	 * 出勤日历表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/charge/attendanceCalendarList");
	}
	/**
	 * 查看出勤日历已有的年份
	 *
	 * */
	@RequestMapping(params = "watch")
	@ResponseBody
	@Transactional(readOnly=false)
	public Map<String, Object> watch(@RequestParam("year") String year){

		Map<String,Object> result = new HashMap<String,Object>();
		List list = new LinkedList<>();
		 result.put("watchY", -1);
		 list = attendanceCalendarService.watchYears(year);
		Integer j = -1;
		if((list.size())>0){
			 result.put("watchY", 1);

		}
		return result;

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
	public void datagrid(AttendanceCalendarEntity attendanceCalendar,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AttendanceCalendarEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, attendanceCalendar, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除出勤日历表
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(AttendanceCalendarEntity attendanceCalendar, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		attendanceCalendar = systemService.getEntity(AttendanceCalendarEntity.class, attendanceCalendar.getId());
		message = "出勤日历表删除成功";
		commonService.delete(attendanceCalendar);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}


	/**
	 * 添加出勤日历表
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(AttendanceCalendarEntity attendanceCalendar, HttpServletRequest request) {
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(attendanceCalendar.getId())) {
			message = "出勤日历表更新成功";
			AttendanceCalendarEntity t = commonService.get(AttendanceCalendarEntity.class, attendanceCalendar.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(attendanceCalendar, t);
				if(null!=user){
					t.setLastModifiedBy(user.getUserName());
				}
				t.setLastModifiedDate(new Date());
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "出勤日历表更新失败";
			}
		} else {
			message = "出勤日历表添加成功";
			if(null!=user){
				attendanceCalendar.setCreatedBy(user.getUserName());
			}
			attendanceCalendar.setCreatedDate(new Date());
			commonService.save(attendanceCalendar);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 导入出勤日历表
	 *
	 * @return
	 */
	@RequestMapping(params="AttendanceCalendarImport")
	public ResponseEntity<Map<String,Object>> AttendanceCalendarImport(@RequestParam("attFile") MultipartFile attFile,
	HttpServletRequest request,
	HttpServletResponse response){
		Map<String,Object> result = new HashMap<String,Object>();
		Workbook wb = null;
		boolean flag = true;
		int success = 0;
		int error = 0;
		int beginColum = 0;
		StringBuffer errormsg = new StringBuffer();
		String rowNum = new String();
		int num = 0;
		List<String> minMaxError = new ArrayList<String>();
		Map<Integer,String> errReason = new HashMap<Integer,String>();
		try {
			if(!attFile.isEmpty()){
				wb = WorkbookFactory.create(attFile.getInputStream());
				Sheet sheet = wb.getSheetAt(0);
				int lastRowNum = sheet.getLastRowNum(); //从0 开始
				Row row = null;
				Iterator<Row> rows = sheet.iterator();
				int beginRow = 0;
				short lastCellNum = 0;
				boolean mark = false;
				//判断第几行开始有数据
				while(beginRow<=lastRowNum) {
					if(rows.hasNext()){
						row = rows.next();
						//判断第几列开始有数据
						lastCellNum = row.getLastCellNum();
						beginColum = 0;
						while(beginColum<=lastCellNum) {
							Cell cell1 = row.getCell(beginColum);
							if(cell1==null||cell1.getNumericCellValue()==0||"".equals(cell1.getNumericCellValue())) {
								beginColum++;
							}else {
								mark = true;
								break;
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
				int physicalNumberOfCells = row.getPhysicalNumberOfCells();
				int type = row.getCell(beginColum).getCellType();
				if(type != 0 || row.getCell(beginColum)==null||physicalNumberOfCells!=13){
					result.put("errCode", -1);
					result.put("errMsg", "请导入正确模板：表头共有13列，第一格应为导入的年份");
					return new ResponseEntity<>(result,HttpStatus.OK);
				}
				try{
					Double year0 =row.getCell(beginColum).getNumericCellValue();
					Integer year = year0.intValue();
					if(year0 != year.doubleValue()|| year == 0 || year > 2050){
						throw new Exception();
					}
					row = rows.next();
					num = beginColum;
					rowNum = "自然天数";
					Double[] totlaDays = new Double[12];
					for(int i=0;i<12;i++){
						num++;totlaDays[i] = row.getCell(num).getNumericCellValue();
					}
					row = rows.next();
					num = beginColum;
					rowNum = "双休日数";
					Double[] weekends = new Double[12];
					for(int i=0;i<12;i++){
						num++;weekends[i] = row.getCell(num).getNumericCellValue();
					}
					row = rows.next();
					num = beginColum;
					rowNum = "公休日数";
					Double[] holidays = new Double[12];
					for(int i=0;i<12;i++){
						num++;holidays[i] = row.getCell(num).getNumericCellValue();
					}
					row = rows.next();
					num = beginColum;
					rowNum = "工作日数";
					Double[] month = new Double[12];
					for(int i=0;i<12;i++){
						num++;month[i] = row.getCell(num).getNumericCellValue();
					}
					for(int i=0;i<12;i++){
						Calendar a = Calendar.getInstance();
						a.set(Calendar.YEAR, year);
						a.set(Calendar.MONTH, i);
						a.set(Calendar.DATE, 1);
						a.roll(Calendar.DATE, -1);
						int maxDate = a.get(Calendar.DATE);
						int q = daysCorrect(totlaDays[i],weekends[i],holidays[i],month[i],maxDate);
						if(q == 1){
							errormsg.append("第"+(i+1)+"月数据格式错误，");
							}else if(q == 4){
							errormsg.append("第"+(i+1)+"月自然日数错误，");
							}else if(q ==2){
							errormsg.append("第"+(i+1)+"月双休日大于10天，");
							}else if(q ==3){
							errormsg.append("第"+(i+1)+"月假期与工作日数之和不等于当月天数，");
							}
					}
					if(errormsg.length() == 0){
						List<AttendanceCalendarEntity> attendanceCalendars = systemService.findByProperty(AttendanceCalendarEntity.class, "year", year);
						AttendanceCalendarEntity attendanceCalendar = new AttendanceCalendarEntity();
						if(!(attendanceCalendars.size() ==0 || null == attendanceCalendars)){
							attendanceCalendar = attendanceCalendars.get(0);
						}
						attendanceCalendar.setYear(year);
						//设定月总天数
						attendanceCalendar.setTotaldays1(totlaDays[0].intValue());
						attendanceCalendar.setTotaldays2(totlaDays[1].intValue());
						attendanceCalendar.setTotaldays3(totlaDays[2].intValue());
						attendanceCalendar.setTotaldays4(totlaDays[3].intValue());
						attendanceCalendar.setTotaldays5(totlaDays[4].intValue());
						attendanceCalendar.setTotaldays6(totlaDays[5].intValue());
						attendanceCalendar.setTotaldays7(totlaDays[6].intValue());
						attendanceCalendar.setTotaldays8(totlaDays[7].intValue());
						attendanceCalendar.setTotaldays9(totlaDays[8].intValue());
						attendanceCalendar.setTotaldays10(totlaDays[9].intValue());
						attendanceCalendar.setTotaldays11(totlaDays[10].intValue());
						attendanceCalendar.setTotaldays12(totlaDays[11].intValue());
						//设定法定双休日
						attendanceCalendar.setWeekends1(weekends[0].intValue());
						attendanceCalendar.setWeekends2(weekends[1].intValue());
						attendanceCalendar.setWeekends3(weekends[2].intValue());
						attendanceCalendar.setWeekends4(weekends[3].intValue());
						attendanceCalendar.setWeekends5(weekends[4].intValue());
						attendanceCalendar.setWeekends6(weekends[5].intValue());
						attendanceCalendar.setWeekends7(weekends[6].intValue());
						attendanceCalendar.setWeekends8(weekends[7].intValue());
						attendanceCalendar.setWeekends9(weekends[8].intValue());
						attendanceCalendar.setWeekends10(weekends[9].intValue());
						attendanceCalendar.setWeekends11(weekends[10].intValue());
						attendanceCalendar.setWeekends12(weekends[11].intValue());
						//设定法定假期
						attendanceCalendar.setHolidays1(holidays[0].intValue());
						attendanceCalendar.setHolidays2(holidays[1].intValue());
						attendanceCalendar.setHolidays3(holidays[2].intValue());
						attendanceCalendar.setHolidays4(holidays[3].intValue());
						attendanceCalendar.setHolidays5(holidays[4].intValue());
						attendanceCalendar.setHolidays6(holidays[5].intValue());
						attendanceCalendar.setHolidays7(holidays[6].intValue());
						attendanceCalendar.setHolidays8(holidays[7].intValue());
						attendanceCalendar.setHolidays9(holidays[8].intValue());
						attendanceCalendar.setHolidays10(holidays[9].intValue());
						attendanceCalendar.setHolidays11(holidays[10].intValue());
						attendanceCalendar.setHolidays12(holidays[11].intValue());
						//设定法定出勤日
						attendanceCalendar.setMonth1(month[0].intValue());
						attendanceCalendar.setMonth2(month[1].intValue());
						attendanceCalendar.setMonth3(month[2].intValue());
						attendanceCalendar.setMonth4(month[3].intValue());
						attendanceCalendar.setMonth5(month[4].intValue());
						attendanceCalendar.setMonth6(month[5].intValue());
						attendanceCalendar.setMonth7(month[6].intValue());
						attendanceCalendar.setMonth8(month[7].intValue());
						attendanceCalendar.setMonth9(month[8].intValue());
						attendanceCalendar.setMonth10(month[9].intValue());
						attendanceCalendar.setMonth11(month[10].intValue());
						attendanceCalendar.setMonth12(month[11].intValue());
						try{
							AjaxJson aj =  save(attendanceCalendar,request);
							if(aj.isSuccess()){
								success ++;
							}else{
								error++;
							}
						}catch(Exception e) {
							result.put("errCode", -1);
							result.put("errMsg", "数据库异常，请联系管理员");
							return new ResponseEntity<>(result,HttpStatus.OK);
						}
					}
				}catch (Exception e) {
					logger.error(e.getMessage(),e);
					error++;
					if(rowNum.length() == 0){
						errormsg.append("年份格式错误");
					}else{
						errormsg.append("第"+(num-beginColum)+"月"+rowNum+"单元格格式错误");
					}
				}
			}
		}catch (Exception e) {
				logger.error(e.getMessage(),e);
				flag = false;
			}
		if(flag){
			if(errormsg.length() == 0){
				result.put("errMsg","法定工作日导入成功");
			}else{
				result.put("errMsg",errormsg);
			}
		}else{
			result.put("errCode", -1);
			result.put("errMsg", "请检查您的模板格式是否已经混乱");
		}
		return new ResponseEntity<>(result,HttpStatus.OK);
	}
	/**
	 * 出勤日历导入判断1
	 *
	 * @return
	 */
	public int daysCorrect (Double totlaDays,Double weekends,Double holidays,Double month,int day ){
		Integer totlaint = totlaDays.intValue();
		Integer weekendint = weekends.intValue();
		Integer holidayint = holidays.intValue();
		Integer monthint = month.intValue();
		if(totlaDays != totlaint.doubleValue()||weekends != weekendint.doubleValue()||holidays!=holidayint.doubleValue()||month!=monthint.doubleValue()){
			return 1;
		}
		if(weekendint > 10){
			return 2;
		}
		if(totlaint !=(weekendint+holidayint+monthint)){
			return 3;
		}
		if(totlaint != day){
			return 4;
		}
		return 0;
	}

	/**
	 * 出勤日历表列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(AttendanceCalendarEntity attendanceCalendar, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(attendanceCalendar.getId())) {
			attendanceCalendar = commonService.getEntity(AttendanceCalendarEntity.class, attendanceCalendar.getId());
			req.setAttribute("attendanceCalendarPage", attendanceCalendar);
		}
		return new ModelAndView("com/charge/attendanceCalendar");
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<AttendanceCalendarEntity> list() {
		List<AttendanceCalendarEntity> listAttendanceCalendars=commonService.getList(AttendanceCalendarEntity.class);
		return listAttendanceCalendars;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AttendanceCalendarEntity task = commonService.get(AttendanceCalendarEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody AttendanceCalendarEntity attendanceCalendar, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<AttendanceCalendarEntity>> failures = validator.validate(attendanceCalendar);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.save(attendanceCalendar);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		Integer id = attendanceCalendar.getId();
		URI uri = uriBuilder.path("/rest/attendanceCalendarController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody AttendanceCalendarEntity attendanceCalendar) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<AttendanceCalendarEntity>> failures = validator.validate(attendanceCalendar);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.saveOrUpdate(attendanceCalendar);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		commonService.deleteEntityById(AttendanceCalendarEntity.class, id);
	}
}
