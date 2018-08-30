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
import com.charge.entity.SuppliersEntity;
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

@Controller
@RequestMapping("/suppliersController")
public class SuppliersController extends BaseController  {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SuppliersEntity.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CommonService commonService;

	@Autowired
	private CorporateInfoRepository corporateInfoRepository;



	/**
	 * 获取所有客户数据----获取供应商
	 * @return
	 */
	@RequestMapping(params = "getComboTreeData")
	@ResponseBody
	public List<Map<String,Object>> getComboTreeData(){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
//		List<CustomerInfoEntity> customerList = commonService.loadAll(CustomerInfoEntity.class);
		List<SuppliersEntity> suppliersEntity = commonService.findByProperty(SuppliersEntity.class, "activeFlg", 0);
		for(int i = 0;i<suppliersEntity.size();i++){

				Map<String,Object> obj = new HashMap<String,Object>();
				obj.put("id", suppliersEntity.get(i).getId());
				obj.put("text", suppliersEntity.get(i).getCode());
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
	 * 删除供应商信息表
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(SuppliersEntity suppliersEntity, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		suppliersEntity = systemService.getEntity(SuppliersEntity.class, suppliersEntity.getId());
		message = "客户信息表删除成功";
		commonService.delete(suppliersEntity);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}
	/**
	 * 冻结供应商信息表
	 *
	 * @return
	 */
	@RequestMapping(params = "Frozen")
	@ResponseBody
	public AjaxJson Frozen(SuppliersEntity suppliersEntity, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		suppliersEntity = systemService.getEntity(SuppliersEntity.class, suppliersEntity.getId());
		message = "客户冻结成功";
		suppliersEntity.setActiveFlg(1);
		commonService.saveOrUpdate(suppliersEntity);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}
	/**
	 * 激活供应商信息表
	 *
	 * @return
	 */
	@RequestMapping(params = "activation")
	@ResponseBody
	public AjaxJson activation(SuppliersEntity suppliersEntity, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		suppliersEntity = systemService.getEntity(SuppliersEntity.class, suppliersEntity.getId());
		message = "客户激活成功";
		suppliersEntity.setActiveFlg(0);
		commonService.saveOrUpdate(suppliersEntity);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 供应商信息表列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(SuppliersEntity suppliersEntity, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(suppliersEntity.getId())) {
			suppliersEntity = commonService.getEntity(SuppliersEntity.class, suppliersEntity.getId());
			req.setAttribute("suppliersEntityPage", suppliersEntity);
		}
			return new ModelAndView("com/charge/suppliersEntity");
	}

	/**
	 * 供应商信息表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/charge/suppliersEntityList");
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
	public void datagrid(SuppliersEntity suppliers,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SuppliersEntity.class, dataGrid);
		//查询条件组装器
//		dataGrid.setField("id,code,name,corporateType,address,tel,signCorporate,workDays,accountDelay,");
		if("signCorporate.Code".equals(dataGrid.getSort())) {
			dataGrid.setSort("signCorporate");
		}
		//加入姓名模糊查询
			if(suppliers.getCode()!=null) {
				suppliers.setCode("*"+suppliers.getCode()+"*");
			}

//		int customerFlage = Integer.parseInt(request.getParameter("customerFlage"));
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, suppliers, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
		List<SuppliersEntity> results = dataGrid.getResults();
		for(SuppliersEntity result : results) {
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
	 * 添加客户信息表
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(SuppliersEntity suppliersEntity, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(suppliersEntity.getId())) {
			message = "供应商信息表更新成功";
			SuppliersEntity t = commonService.get(SuppliersEntity.class, suppliersEntity.getId());
			try {
				Boolean flag1 = (t.getSignCorporate()).equals(request.getParameter("signCorporateId"));
				MyBeanUtils.copyBeanNotNull2Bean(suppliersEntity, t);
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
				message = "供应商信息表更新失败";
			}
		}else {
			CorporateInfoEntity corporateInfo = commonService.getEntity(CorporateInfoEntity.class, Integer.valueOf(request.getParameter("signCorporateId")));
			if(null!=corporateInfo){
				suppliersEntity.setSignCorporate(corporateInfo.getId());
			}
			message = "供应商信息表添加成功";
			suppliersEntity.setActiveFlg(0);
			commonService.save(suppliersEntity);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}
}
