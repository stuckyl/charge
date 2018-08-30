package com.charge.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;

import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.SixGoldScaleEntity;
import com.charge.repository.SixGoldScaleRepository;
import com.charge.service.EmployeeInfoService;

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
import java.util.Map.Entry;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Title: Controller
 * @Description: 六金比例表
 * @author wenst
 * @date 2018-04-21 12:18:33
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/sixGoldScaleController")
public class SixGoldScaleController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(SixGoldScaleController.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CommonService commonService;
	@Autowired
	private SixGoldScaleRepository sixGoldScaleRepo;



	/**
	 * 获取六金城市组
	 * @return
	 */
	@RequestMapping(params = "sixGoldCitys")
	@ResponseBody
	public List<String> getSixGoldCityGroup(){
		List<String> sixGoldCityList = sixGoldScaleRepo.findSixGoldCityGroup();
		return sixGoldCityList;
	}

	/**
	 * 获取六金城市的最低基数
	 * @return
	 */
	@RequestMapping(params = "sixGoldBase")
	@ResponseBody
	public List<Double> sixGoldBase(String sixGoldCity){
		SixGoldScaleEntity sixGold = sixGoldScaleRepo.findByCity(sixGoldCity);
		List<Double> sixGoldMinimum = new ArrayList<>();
		if(StringUtil.isNotEmpty(sixGold)){
			sixGoldMinimum.add(sixGold.getEndowmentMin());
			sixGoldMinimum.add(sixGold.getHousingFundMin());
			sixGoldMinimum.add(sixGold.getInjuryMin());
			sixGoldMinimum.add(sixGold.getMaternityMin());
			sixGoldMinimum.add(sixGold.getMedicalMin());
			sixGoldMinimum.add(sixGold.getUnemploymentMin());
		}else{
			sixGoldMinimum.add((double)0);
		}
		return sixGoldMinimum;
	}
	/**
	 * Ajax获取城市的六金信息
	 * @return
	 */
	@RequestMapping(params = "getSixGoldScaleInfo")
	@ResponseBody
	public Map<String,Object> getSixGoldScaleInfo(String sixGoldCity){
		SixGoldScaleEntity sixGold = sixGoldScaleRepo.findByCity(sixGoldCity);
		Map<String,Object> cityInfo = new HashMap<>();
		if(StringUtil.isNotEmpty(sixGold)){
			//公司养老比例
			cityInfo.put("companyEndowment", sixGold.getCompanyEndowment());
			//个人养老比例
			cityInfo.put("personalEndowment",sixGold.getPersonalEndowment());
			cityInfo.put("companyHousingFund", sixGold.getCompanyHousingFund());
			cityInfo.put("personalHousingFund", sixGold.getPersonalHousingFund());
			cityInfo.put("companyInjury", sixGold.getCompanyInjury());
			cityInfo.put("companyMaternity", sixGold.getCompanyMaternity());
			cityInfo.put("companyMedical", sixGold.getCompanyMedical());
			cityInfo.put("personalMedical", sixGold.getPersonalMedical());
			cityInfo.put("companyUnemployment", sixGold.getCompanyUnemployment());
			cityInfo.put("personalUnemployment", sixGold.getPersonalUnemployment());
			List minList = new ArrayList();
			minList.add(sixGold.getEndowmentMin());
			minList.add(sixGold.getHousingFundMin());
			minList.add(sixGold.getInjuryMin());
			minList.add(sixGold.getMaternityMin());
			minList.add(sixGold.getMedicalMin());
			minList.add(sixGold.getUnemploymentMin());
			cityInfo.put("sixGoldMin",Collections.max(minList));
		}
		return cityInfo;
	}

	/**
	 * 获取发薪地城市组
	 * @return
	 */
	@RequestMapping(params = "getPayCityGroup")
	@ResponseBody
	public List<String> getPayCityGroup(){
		List<String> CityList = sixGoldScaleRepo.findPayArea();
		return CityList;
	}

	/**
	 * 六金比例表列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/charge/sixGoldScaleList");
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
	public void datagrid(SixGoldScaleEntity sixGoldScale,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SixGoldScaleEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sixGoldScale, request.getParameterMap());
		commonService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除六金比例表
	 *
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(SixGoldScaleEntity sixGoldScale, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sixGoldScale = systemService.getEntity(SixGoldScaleEntity.class, sixGoldScale.getId());
		message = "六金比例表删除成功";
		commonService.delete(sixGoldScale);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}


	/**
	 * 添加六金比例表
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(SixGoldScaleEntity sixGoldScale, HttpServletRequest request) {
		TSUser tsUser = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(sixGoldScale.getId())) {
			message = "六金比例表更新成功";
			SixGoldScaleEntity t = commonService.get(SixGoldScaleEntity.class, sixGoldScale.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(sixGoldScale, t);
				t.setLastModifiedBy(tsUser.getUserName());
				t.setLastModifiedDate(new Date());
				commonService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "六金比例表更新失败";
			}
		} else {
			//查看六金地点是否存在，存在更新旧记录
			List<SixGoldScaleEntity> sixGoldPlaceList = commonService.findByProperty(SixGoldScaleEntity.class, "sixGoldPlace", sixGoldScale.getSixGoldPlace());
			if(!sixGoldPlaceList.isEmpty()){
				try {
					MyBeanUtils.copyBeanNotNull2Bean(sixGoldScale,sixGoldPlaceList.get(0));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sixGoldPlaceList.get(0).setLastModifiedBy(tsUser.getUserName());
				sixGoldPlaceList.get(0).setLastModifiedDate(new Date());
				commonService.saveOrUpdate(sixGoldPlaceList.get(0));
				message = "六金比例表更新成功";
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			}else {
				commonService.save(sixGoldScale);
				message = "六金比例表添加成功";
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 六金比例导入
	 * @param empFile
	 * @param response
	 * @return
	 */
	@RequestMapping(params="sixGoldScaleImport")
	public ResponseEntity<Map<String,Object>> sixGoldScaleImport(@RequestParam("sgFile") MultipartFile sgFile,
			HttpServletRequest request,
			HttpServletResponse response){
		Map<String,Object> result = new HashMap<String,Object>();
		Workbook wb = null;
		boolean flag = true;
		int success = 0;
		int error = 0;
		List<String> minMaxError = new ArrayList<String>();
		Map<Integer,String> errReason = new HashMap<Integer,String>();
		try {
			if(!sgFile.isEmpty()){
				wb = WorkbookFactory.create(sgFile.getInputStream());
				Sheet sheet = wb.getSheetAt(0);




				int lastRowNum = sheet.getLastRowNum(); //从0 开始
				Row row = null;
				Iterator<Row> rows = sheet.iterator();
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
							Cell cell1 = row.getCell(beginColum);
							if(cell1==null||cell1.getStringCellValue()==null||"".equals(cell1.getStringCellValue())) {
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
				String stringCellValue = row.getCell(beginColum).getStringCellValue();
				int physicalNumberOfCells = row.getPhysicalNumberOfCells();
				if(row.getCell(beginColum)==null||!"城市".equals(stringCellValue)||physicalNumberOfCells!=23){
					result.put("errCode", -1);
					result.put("errMsg", "请导入六金信息的正确模板：表头共有23列，第一列应为 城市");
					return new ResponseEntity<>(result,HttpStatus.OK);
				}
				//跳过第二行
				row = rows.next();

				List<String> CityList = new ArrayList<String>();
				List<String> repeatCityList = new ArrayList<String>();
				//判断身份证是否有重复的
				while(rows.hasNext()){
					row = rows.next();
					//身份证在 第二列
					int idNum = beginColum;
					Cell cell = row.getCell(idNum);
					if(cell==null||cell.getStringCellValue()==null||"".equals(cell.getStringCellValue())) continue;
					String cityName = cell.getStringCellValue();
					if(CityList.indexOf(cityName)==-1){ //不包含 此 身份证号
						CityList.add(cityName);
					}else {
						repeatCityList.add(""+(row.getRowNum()+1));
					}
				}
				if(repeatCityList.size()!=0) {
					result.put("errCode", -1);
					result.put("errMsg", "表中存在重复城市，重复行数："+repeatCityList.toString()+"<br>请先去除重复城市的六金信息再导入");
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
				list.add("城市");
				list.add("养老最低");
				list.add("养老最高");
				list.add("养老企业");
				list.add("养老个人");
				list.add("医疗最低");
				list.add("医疗最高");
				list.add("医疗企业");
				list.add("医疗个人");
				list.add("失业最低");
				list.add("失业最高");
				list.add("失业企业");
				list.add("失业个人");
				list.add("生育最低");
				list.add("生育最高");
				list.add("生育企业");
				list.add("工伤最低");
				list.add("工伤最高");
				list.add("工伤企业");
				list.add("公积金最低");
				list.add("公积金最高");
				list.add("公积金企业");
				list.add("公积金个人");

				while(rows.hasNext()){
					row = rows.next();
					int rowNum = row.getRowNum()+1;
					int num = beginColum;

					Cell cell2 = row.getCell(beginColum);
					if(cell2==null||cell2.getStringCellValue()==null||"".equals(cell2.getStringCellValue())) continue;

					try {
						String cityName = EmployeeInfoService.replaceAllBlank(row.getCell(num).getStringCellValue());num++;
						Double endowmentMin = row.getCell(num).getNumericCellValue();num++;
						Double endowmentMax = row.getCell(num).getNumericCellValue();num++;
						if(endowmentMin>endowmentMax) {
							minMaxError.add(cityName+"养老"); continue;
						}
						Double endowmentCompany = formatDouble(row.getCell(num).getNumericCellValue());num++;
						Double endowmentPersonal = formatDouble(row.getCell(num).getNumericCellValue());num++;
						Double medicalMin = row.getCell(num).getNumericCellValue();num++;
						Double medicalMax = row.getCell(num).getNumericCellValue();num++;
						if(medicalMin>medicalMax) {
							minMaxError.add(cityName+"医疗"); continue;
						}
						Double medicalCompany = formatDouble(row.getCell(num).getNumericCellValue());num++;
						Double medicalPersonal = formatDouble(row.getCell(num).getNumericCellValue());num++;
						Double unemploymentMin = row.getCell(num).getNumericCellValue();num++;
						Double unemploymentMax = row.getCell(num).getNumericCellValue();num++;
						if(unemploymentMin>unemploymentMax) {
							minMaxError.add(cityName+"失业"); continue;
						}
						Double unemploymentCompany = formatDouble(row.getCell(num).getNumericCellValue());num++;
						Double unemploymentPersonal = formatDouble(row.getCell(num).getNumericCellValue());num++;
						Double maternityMin = row.getCell(num).getNumericCellValue();num++;
						Double maternityMax = row.getCell(num).getNumericCellValue();num++;
						if(maternityMin>maternityMax) {
							minMaxError.add(cityName+"生育"); continue;
						}
						Double maternityCompany = formatDouble(row.getCell(num).getNumericCellValue());num++;
						Double injuryMin = row.getCell(num).getNumericCellValue();num++;
						Double injuryMax = row.getCell(num).getNumericCellValue();num++;
						if(injuryMin>injuryMax) {
							minMaxError.add(cityName+"工伤"); continue;
						}
						Double injuryCompany = formatDouble(row.getCell(num).getNumericCellValue());num++;
						Double housingFundMin = row.getCell(num).getNumericCellValue();num++;
						Double housingFundMax = row.getCell(num).getNumericCellValue();num++;
						if(housingFundMin>housingFundMax) {
							minMaxError.add(cityName+"住房公积金"); continue;
						}
						Double housingFundCompany = formatDouble(row.getCell(num).getNumericCellValue());num++;
						Double housingFundPersonal = formatDouble(row.getCell(num).getNumericCellValue());num++;
						SixGoldScaleEntity sixGoldScale = new SixGoldScaleEntity();
						//六金地点
						sixGoldScale.setSixGoldPlace(cityName);
						//养老
						sixGoldScale.setEndowmentMin(endowmentMin);
						sixGoldScale.setEndowmentMax(endowmentMax);
						sixGoldScale.setCompanyEndowment(endowmentCompany);
						sixGoldScale.setPersonalEndowment(endowmentPersonal);
						//医疗
						sixGoldScale.setMedicalMin(medicalMin);
						sixGoldScale.setMedicalMax(medicalMax);
						sixGoldScale.setCompanyMedical(medicalCompany);
						sixGoldScale.setPersonalMedical(medicalPersonal);
						//失业
						sixGoldScale.setUnemploymentMin(unemploymentMin);
						sixGoldScale.setUnemploymentMax(unemploymentMax);
						sixGoldScale.setCompanyUnemployment(unemploymentCompany);
						sixGoldScale.setPersonalUnemployment(unemploymentPersonal);
						//生育
						sixGoldScale.setMaternityMin(maternityMin);
						sixGoldScale.setMaternityMax(maternityMax);
						sixGoldScale.setCompanyMaternity(maternityCompany);
						//工伤
						sixGoldScale.setInjuryMin(injuryMin);
						sixGoldScale.setInjuryMax(injuryMax);
						sixGoldScale.setCompanyInjury(injuryCompany);
						//公积金
						sixGoldScale.setHousingFundMin(housingFundMin);
						sixGoldScale.setHousingFundMax(housingFundMax);
						sixGoldScale.setCompanyHousingFund(housingFundCompany);
						sixGoldScale.setPersonalHousingFund(housingFundPersonal);
						//保存
						try{
							AjaxJson aj =  save(sixGoldScale,request);
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

					} catch (Exception e) {
						log.error(e.getMessage(),e);
						error++;
						errReason.put(rowNum, list.get(num-beginColum));
					}

				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			flag = false;
		}
		if(flag){
			result.put("errCode", 0);
			StringBuffer mme = new StringBuffer();
			for(String str : minMaxError) {
				mme.append(str+",");
			}
			mme.append("最高最低数据异常。请修正后再导入");
			if(errReason.size()==0) {
				result.put("errMsg","成功导入："+success+"条。"+(minMaxError.size()==0?"":mme.toString()));
			}else {
				StringBuffer sb = new StringBuffer();
				sb.append("单元格格式存在错误。错误详情：\n");
				Set<Entry<Integer, String>> entrySet = errReason.entrySet();
				Iterator<Entry<Integer, String>> iterator = entrySet.iterator();
				int count=0;
				while(iterator.hasNext()) {
					Entry<Integer, String> next = iterator.next();
//					sb.append("第"+next.getKey()+"行,"+"第"+next.getValue()+"列;");
					sb.append("第"+next.getKey()+"行,"+next.getValue()+"列;");
					count++;
					if(count>9) {
						sb.append("......");
						break;
					}
				}
				result.put("errMsg","成功导入："+success+"条，失败："+error+"条。\n"+sb.toString()+(minMaxError.size()==0?"":mme.toString()));
			}
		}else{
			result.put("errCode", -1);
			result.put("errMsg", "请检查您的模板格式是否已经混乱");
		}
		return new ResponseEntity<>(result,HttpStatus.OK);
	}


	/**
	 * 格式化百分比
	 * @param num
	 * @return
	 */
	public double formatDouble(double num){
		return num * 100.0;
	}


	/**
	 * 六金比例表列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(SixGoldScaleEntity sixGoldScale, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sixGoldScale.getId())) {
			sixGoldScale = commonService.getEntity(SixGoldScaleEntity.class, sixGoldScale.getId());
			req.setAttribute("sixGoldScalePage", sixGoldScale);
		}
		return new ModelAndView("com/charge/sixGoldScale");
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SixGoldScaleEntity> list() {
		List<SixGoldScaleEntity> listSixGoldScales=commonService.getList(SixGoldScaleEntity.class);
		return listSixGoldScales;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SixGoldScaleEntity task = commonService.get(SixGoldScaleEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SixGoldScaleEntity sixGoldScale, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SixGoldScaleEntity>> failures = validator.validate(sixGoldScale);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.save(sixGoldScale);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		Integer id = sixGoldScale.getId();
		URI uri = uriBuilder.path("/rest/sixGoldScaleController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SixGoldScaleEntity sixGoldScale) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SixGoldScaleEntity>> failures = validator.validate(sixGoldScale);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		commonService.saveOrUpdate(sixGoldScale);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		commonService.deleteEntityById(SixGoldScaleEntity.class, id);
	}
}
