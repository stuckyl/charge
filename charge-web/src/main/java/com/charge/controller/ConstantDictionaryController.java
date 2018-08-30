package com.charge.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import com.charge.entity.ConstantDictionaryEntity;
import com.charge.repository.CommonRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Title: Controller
 * @Description: 系统常量字典
 * @author zhangdaihao
 * @date 2018-07-25 11:12:39
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/constantDictionaryController")
public class ConstantDictionaryController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ConstantDictionaryController.class);

	@Autowired
	private CommonService commonService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CommonRepository jeecgRepo;



	@RequestMapping(params = "getTurAndPerTax")
	@ResponseBody
	public List<String> getTurAndPerTax() {
		String systemTurnoverTax = jeecgRepo.getSystemTurnoverTax();
		String systemPerTax = "3500";
		List<String> result = new ArrayList<String>();
		result.add(systemPerTax);
		result.add(systemTurnoverTax);
		return result;
	}

	/**
	 * 系统常量字典列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/charge/constantDictionaryList");
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
	public void datagrid(ConstantDictionaryEntity constantDictionary,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ConstantDictionaryEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, constantDictionary, request.getParameterMap());
		this.commonService.getDataGridReturn(cq, true);
		List<ConstantDictionaryEntity> constant = dataGrid.getResults();
		for(ConstantDictionaryEntity con:constant){
			if("c_basePay".equals(con.getConstantKey())){
				con.setConstantValue(addComma(con.getConstantValue()));
			}
			if("c_perTax".equals(con.getConstantKey())){
				con.setConstantValue(addComma(con.getConstantValue()));
			}
			if("c_turnoverTax".equals(con.getConstantKey())){
				con.setConstantValue(addComma(con.getConstantValue()));
			}
		}
		dataGrid.setResults(constant);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 字符型数字加千分位和两位小数
	 *
	 */
	public static String addComma(String str){
		boolean neg = false;
		if (str.startsWith("-")){  //处理负数
			str = str.substring(1);
			neg = true;
		}
		String tail = null;
		if (str.indexOf('.') != -1){ //处理小数点
			tail = str.substring(str.indexOf('.'));
			str = str.substring(0, str.indexOf('.'));
		}
		StringBuilder sb = new StringBuilder(str);
		sb.reverse();
		for (int i = 3; i < sb.length(); i += 4){
			sb.insert(i, ',');
		}
		sb.reverse();
		if (neg){
			sb.insert(0, '-');
		}
		if (tail != null){
			if(tail.length()==1)
				tail = tail+"0";
			if(tail.length()==2)
				tail = tail+"0";
			sb.append(tail);
		}else{
			sb.append(".00");
		}
		return sb.toString();
	}
	/**
	 * 删除系统常量字典
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(ConstantDictionaryEntity constantDictionary, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		constantDictionary = systemService.getEntity(ConstantDictionaryEntity.class, constantDictionary.getId());
		message = "系统参数字典删除成功";
		commonService.delete(constantDictionary);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}


	/**
	 * 添加系统常量字典
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ConstantDictionaryEntity constantDictionary, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(constantDictionary.getId())) {
			message = "系统参数更新成功";
			ConstantDictionaryEntity t = commonService.get(ConstantDictionaryEntity.class, constantDictionary.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(constantDictionary, t);
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "系统参数更新失败";
			}
		} else {
			message = "系统参数添加成功";
			commonService.saveOrUpdate(constantDictionary);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 系统常量字典列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(ConstantDictionaryEntity constantDictionary, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(constantDictionary.getId())) {
			constantDictionary = commonService.getEntity(ConstantDictionaryEntity.class, constantDictionary.getId());
			req.setAttribute("constantDictionaryPage", constantDictionary);
		}
		return new ModelAndView("com/charge/constantDictionary");
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<ConstantDictionaryEntity> list() {
		List<ConstantDictionaryEntity> listConstantDictionarys=commonService.getList(ConstantDictionaryEntity.class);
		return listConstantDictionarys;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		ConstantDictionaryEntity task = commonService.get(ConstantDictionaryEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody ConstantDictionaryEntity constantDictionary, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<ConstantDictionaryEntity>> failures = validator.validate(constantDictionary);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.save(constantDictionary);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		Integer id = constantDictionary.getId();
		URI uri = uriBuilder.path("/rest/constantDictionaryController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody ConstantDictionaryEntity constantDictionary) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<ConstantDictionaryEntity>> failures = validator.validate(constantDictionary);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.saveOrUpdate(constantDictionary);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		commonService.deleteEntityById(ConstantDictionaryEntity.class, id);
	}
}
