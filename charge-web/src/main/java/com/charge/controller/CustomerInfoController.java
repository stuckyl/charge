package com.charge.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;
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
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import com.charge.entity.CorporateInfoEntity;
import com.charge.entity.CustomerInfoEntity;
import com.charge.repository.CorporateInfoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
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
 * @Description: 客户信息表
 * @author wenst
 * @date 2018-03-20 08:41:04
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/customerInfoController")
public class CustomerInfoController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CustomerInfoController.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CommonService commonService;

	@Autowired
	private CorporateInfoRepository corporateInfoRepository;


	/**
	 * 根据签约客户获取法人
	 * @return
	 */
	@RequestMapping(params = "getcorporate")
	@ResponseBody
	public List<Map<String,Object>> getcorporate(HttpServletRequest request){
		String customerId = request.getParameter("customerId");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		CustomerInfoEntity customerInfo = commonService.getEntity(CustomerInfoEntity.class,Integer.parseInt(customerId));
		List<CorporateInfoEntity> corporList = commonService.loadAll(CorporateInfoEntity.class);
		for(int i = 0;i<corporList.size();i++){
			Map<String,Object> obj = new HashMap<String,Object>();
			obj.put("id", corporList.get(i).getId());
			obj.put("text", corporList.get(i).getCode());
			obj.put("corid", customerInfo.getSignCorporate());
			result.add(obj);
		}
		return result;
	}

	/**
	 * 外包方信息表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/charge/customerInfoList");
	}
	/**
	 * 顾客信息表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list1")
	public ModelAndView list1(HttpServletRequest request) {
		return new ModelAndView("com/charge/customerInfoList1");
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
	public void datagrid(CustomerInfoEntity customerInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CustomerInfoEntity.class, dataGrid);
		//查询条件组装器
//		dataGrid.setField("id,code,name,corporateType,address,tel,signCorporate,workDays,accountDelay,");
		if("signCorporate.Code".equals(dataGrid.getSort())) {
			dataGrid.setSort("signCorporate");
		}
		//加入姓名模糊查询
			if(customerInfo.getCode()!=null) {
				customerInfo.setCode("*"+customerInfo.getCode()+"*");
			}

//		int customerFlage = Integer.parseInt(request.getParameter("customerFlage"));
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, customerInfo, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
		List<CustomerInfoEntity> results = dataGrid.getResults();
		for(CustomerInfoEntity result : results) {
			Map m = new HashMap();
			Integer corporateId = result.getSignCorporate();
			CorporateInfoEntity corporateInfo = corporateInfoRepository.findUniqueBy(CorporateInfoEntity.class, "id", corporateId);
			if(corporateInfo==null||corporateInfo.getCode()==null) {
				m.put("signCorporate.Code", "无");
			}else{
				m.put("signCorporate.Code", corporateInfo.getCode());
			}
			extMap.put(result.getId().toString(), m);
		}

		TagUtil.datagrid(response, dataGrid,extMap);
	}

	/**
	 * 删除客户信息表
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CustomerInfoEntity customerInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		customerInfo = systemService.getEntity(CustomerInfoEntity.class, customerInfo.getId());
		message = "客户信息表删除成功";
		commonService.delete(customerInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}
	/**
	 * 冻结客户信息表
	 *
	 * @return
	 */
	@RequestMapping(params = "Frozen")
	@ResponseBody
	public AjaxJson Frozen(CustomerInfoEntity customerInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		customerInfo = systemService.getEntity(CustomerInfoEntity.class, customerInfo.getId());
		message = "客户冻结成功";
		customerInfo.setActiveFlg(1);
		commonService.saveOrUpdate(customerInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}
	/**
	 * 激活客户信息表
	 *
	 * @return
	 */
	@RequestMapping(params = "activation")
	@ResponseBody
	public AjaxJson activation(CustomerInfoEntity customerInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		customerInfo = systemService.getEntity(CustomerInfoEntity.class, customerInfo.getId());
		message = "客户激活成功";
		customerInfo.setActiveFlg(0);
		commonService.saveOrUpdate(customerInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 获取所有客户数据----获取上游客户
	 * @return
	 */
	@RequestMapping(params = "getComboTreeData")
	@ResponseBody
	public List<Map<String,Object>> getComboTreeData(){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
//		List<CustomerInfoEntity> customerList = commonService.loadAll(CustomerInfoEntity.class);
		List<CustomerInfoEntity> customerList = commonService.findByProperty(CustomerInfoEntity.class, "activeFlg", 0);
		for(int i = 0;i<customerList.size();i++){

				Map<String,Object> obj = new HashMap<String,Object>();
				obj.put("id", customerList.get(i).getId());
				obj.put("text", customerList.get(i).getCode());
				/*
				 * if(null!=customerList.get(i).getSignType()){
				if(customerList.get(i).getSignType().equals(0)){
					obj.put("attr", "直签");
				}else if(customerList.get(i).getSignType().equals(1)){
					obj.put("attr", "转签");
				}
			}
				 */
				result.add(obj);
		}
		return result;
	}


	/**
	 * 添加客户信息表
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CustomerInfoEntity customerInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(customerInfo.getId())) {
			message = "客户信息表更新成功";
			CustomerInfoEntity t = commonService.get(CustomerInfoEntity.class, customerInfo.getId());
			try {
				Boolean flag1 = (t.getSignCorporate()).equals(request.getParameter("signCorporateId"));
				MyBeanUtils.copyBeanNotNull2Bean(customerInfo, t);
				if(StringUtils.isNotBlank(request.getParameter("signCorporateId"))){
					CorporateInfoEntity corporateInfo = null;
					if(!flag1){
						corporateInfo = commonService.getEntity(CorporateInfoEntity.class, Integer.valueOf(request.getParameter("signCorporateId")));
					}
					if(null!=corporateInfo){
						t.setSignCorporate(corporateInfo.getId());
					}
				}
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "客户信息表更新失败";
			}
		}else {
			CorporateInfoEntity corporateInfo = commonService.getEntity(CorporateInfoEntity.class, Integer.valueOf(request.getParameter("signCorporateId")));
			if(null!=corporateInfo){
				customerInfo.setSignCorporate(corporateInfo.getId());
			}
			message = "客户信息表添加成功";
			customerInfo.setActiveFlg(0);
			commonService.save(customerInfo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * 客户信息表列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CustomerInfoEntity customerInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(customerInfo.getId())) {
			customerInfo = commonService.getEntity(CustomerInfoEntity.class, customerInfo.getId());
			req.setAttribute("customerInfoPage", customerInfo);
		}
			return new ModelAndView("com/charge/customerInfo");
	}
	/**
	 * 客户信息表列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate1")
	public ModelAndView addorupdate1(CustomerInfoEntity customerInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(customerInfo.getId())) {
			customerInfo = commonService.getEntity(CustomerInfoEntity.class, customerInfo.getId());
			req.setAttribute("customerInfoPage", customerInfo);
		}
		return new ModelAndView("com/charge/customerInfo1");
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<CustomerInfoEntity> list() {
		List<CustomerInfoEntity> listCustomerInfos=commonService.getList(CustomerInfoEntity.class);
		return listCustomerInfos;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		CustomerInfoEntity task = commonService.get(CustomerInfoEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody CustomerInfoEntity customerInfo, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<CustomerInfoEntity>> failures = validator.validate(customerInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.save(customerInfo);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		Integer id = customerInfo.getId();
		URI uri = uriBuilder.path("/rest/customerInfoController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody CustomerInfoEntity customerInfo) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<CustomerInfoEntity>> failures = validator.validate(customerInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.saveOrUpdate(customerInfo);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		commonService.deleteEntityById(CustomerInfoEntity.class, id);
	}
}
