package com.charge.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import com.charge.entity.CorporateInfoEntity;

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
 * @Description: 法人主体信息表
 * @author wenst
 * @date 2018-03-20 08:51:04
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/corporateInfoController")
public class CorporateInfoController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CorporateInfoController.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CommonService commonService;



	/**
	 * 法人主体信息表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/charge/corporateInfoList");
	}

	/**
	 * Ajax 判断库中是否录入过签约法人
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "isNull")
	@ResponseBody
	public String isNull(HttpServletRequest request) {
		List<CorporateInfoEntity> list = commonService.findHql("select corp from CorporateInfoEntity corp");
		if(list.size()==0) {
			return "0";
		}else {
			return "1";
		}
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
	public void datagrid(CorporateInfoEntity corporateInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CorporateInfoEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, corporateInfo, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除法人主体信息表
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CorporateInfoEntity corporateInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		corporateInfo = systemService.getEntity(CorporateInfoEntity.class, corporateInfo.getId());
		message = "法人主体信息表删除成功";
		commonService.delete(corporateInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 获取所有法人数据
	 * @return
	 */
	@RequestMapping(params = "getComboTreeData")
	@ResponseBody
	public List<Map<String,Object>> getComboTreeData(){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		List<CorporateInfoEntity> corporList = commonService.loadAll(CorporateInfoEntity.class);
		for(int i = 0;i<corporList.size();i++){
			Map<String,Object> obj = new HashMap<String,Object>();
			obj.put("id", corporList.get(i).getId());
			obj.put("text", corporList.get(i).getCode());
			result.add(obj);
		}
		return result;
	}

	@RequestMapping(params = "getComboTreeData2")
	@ResponseBody
	public List<String> getComboTreeData2(){
		List<String> result = new ArrayList<String>();
		List<CorporateInfoEntity> corporList = commonService.loadAll(CorporateInfoEntity.class);
		for(int i = 0;i<corporList.size();i++){
			result.add(corporList.get(i).getId().toString());
			result.add(corporList.get(i).getCode());
		}
		return result;
	}


	/**
	 * 添加法人主体信息表
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CorporateInfoEntity corporateInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(corporateInfo.getId())) {
			message = "法人主体信息表更新成功";
			CorporateInfoEntity t = commonService.get(CorporateInfoEntity.class, corporateInfo.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(corporateInfo, t);
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "法人主体信息表更新失败";
			}
		} else {
			message = "法人主体信息表添加成功";
			commonService.save(corporateInfo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 法人主体信息表列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CorporateInfoEntity corporateInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(corporateInfo.getId())) {
			corporateInfo = commonService.getEntity(CorporateInfoEntity.class, corporateInfo.getId());
			req.setAttribute("corporateInfoPage", corporateInfo);
		}
		return new ModelAndView("com/charge/corporateInfo");
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<CorporateInfoEntity> list() {
		List<CorporateInfoEntity> listCorporateInfos=commonService.getList(CorporateInfoEntity.class);
		return listCorporateInfos;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		CorporateInfoEntity task = commonService.get(CorporateInfoEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody CorporateInfoEntity corporateInfo, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<CorporateInfoEntity>> failures = validator.validate(corporateInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.save(corporateInfo);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		Integer id = corporateInfo.getId();
		URI uri = uriBuilder.path("/rest/corporateInfoController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody CorporateInfoEntity corporateInfo) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<CorporateInfoEntity>> failures = validator.validate(corporateInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.saveOrUpdate(corporateInfo);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		commonService.deleteEntityById(CorporateInfoEntity.class, id);
	}
}
