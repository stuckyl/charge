package com.charge.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.controller.core.UserController;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.charge.entity.EmployeeInfoEntity;
import com.charge.repository.CommonRepository;
import com.charge.repository.EmployeeDeclareRepository;
import com.charge.repository.EmployeeInfoRepository;
import com.charge.repository.SixGoldRepository;
import com.charge.repository.SixGoldScaleRepository;
import com.charge.service.EmployeeInfoService;

/**
 * @Title: Controller
 * @Description: 我的部门
 * @author J
 * @date 2018-06-17
 */

@Controller
@RequestMapping("/myDepartController")
public class MyDepartController {
	private final static Logger log = Logger.getLogger(MyDepartController.class);

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MyDepartController.class);

	private SystemService systemService;
	private UserController userController;
	private CommonRepository commonRepository;
	private UserService userService;
	private EmployeeInfoService employeeInfoService;
	@Autowired
	private EmployeeInfoRepository employeeInfoRepo;
	@Autowired
	private EmployeeDeclareRepository employeeDeclare;
	@Autowired
	private SixGoldRepository sixGold;
	@Autowired
	private SixGoldScaleRepository sixGoldScale;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}
	@Autowired
	public void setUserController(UserController userController) {
		this.userController = userController;
	}
	@Autowired
	public void setCommonRepository(CommonRepository commonRepository) {
		this.commonRepository = commonRepository;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setEmployeeInfoService(EmployeeInfoService employeeInfoService) {
		this.employeeInfoService = employeeInfoService;
	}


	@RequestMapping(params="departUserDel")
	@ResponseBody
	@Transactional(readOnly=false)
	public AjaxJson departUserDel(TSUser user, HttpServletRequest req){
		AjaxJson result =new AjaxJson();
		user = systemService.getEntity(TSUser.class, user.getId());
		Long userCount = systemService.getCountForJdbc("select count(1) from c_employee_info where inputer_id='" + user.getUserName()+"'and del_flg = 0 ");
		TSUser myUser = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if (myUser.getUserName().equals(user.getUserName())) {
			 result.setSuccess(false);
			 result.setMsg("不可删除自己正在使用的账户");
		}else if(userCount == 0){
			 result = userController.trueDel(user, req);
		}else{
			 result.setSuccess(false);
			 result.setMsg("该用户下存在员工信息不可删除");
		}
		return result;
	}
	//校验新增是否可以新增部门总监
	@RequestMapping(params="checkSize")
	@ResponseBody
	public Map<String,Object> checkSize(String departid){
		Map<String,Object> result = new HashMap<String,Object>();
		String role2 = "t_input_m";
		List<TSRoleUser> roleUsers2 = systemService.findHql("from TSRoleUser t where t.TSUser.departid = ? and t.TSRole.roleCode = ? ",departid, role2);
		result.put("errCode", roleUsers2.size());
		return result;
	}

	//业务部门子部门部门总监变更
	@RequestMapping(params="changeDepartManager")
	@ResponseBody
	public Map<String,Object> changeDepartManager(String id, HttpServletRequest req){
		Map<String,Object> result = new HashMap<String,Object>();
		TSUser user =systemService.getEntity(TSUser.class, id);
		Short logType=Globals.Log_Type_UPDATE;
		result.put("errCode", 0);
		result.put("errMsg","权限更改成功，请注意分配原部门总监的员工信息");
		try{
			user = systemService.getEntity(TSUser.class, user.getId());
			if(employeeInfoService.getNameRoleCold(user)==2){
				result.put("errCode", 1);
				result.put("errMsg","此人已经是部门总监权限，请选择客户经理进行权限转移");
			}
			TSUser oldManager = commonRepository.getDepartChief(user.getDepartid());
			String[] depart = new String[]{user.getDepartid()};
			String[] roleidRep = new String[]{"2c934f26633da93b01633dace53b0005"};
			String[] roleidInp = new String[]{"2c934f26633da93b01633dac749f0003"};
			if(StringUtil.isNotEmpty(oldManager)){
				List<EmployeeInfoEntity> employeeInfos =systemService.findByProperty(EmployeeInfoEntity.class, "inputerId", oldManager.getUserName()) ;
				for(EmployeeInfoEntity tmp:employeeInfos){
					tmp.setInputerId(user.getUserName());
					systemService.saveOrUpdate(tmp);
				}
				this.userService.saveOrUpdate(oldManager,depart, roleidInp);
			}
			this.userService.saveOrUpdate(user,depart, roleidRep);
		}catch (Exception e) {
			// TODO: handle exception
			result.put("errCode", 1);
			result.put("errMsg","权限转移失败");
		}
		return result;
	}

	@RequestMapping(params="emptyAll")
	@ResponseBody
	public Map<String,Object> emptyAll(){
		Map<String,Object> result = new HashMap<String,Object>();
		TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		if(employeeInfoService.getNameRoleCold(user)!=4){
			result.put("errCode",1);
			result.put("errMsg","该账户没有进行此操作的权限");
			return result;
		}
		try{
			employeeInfoRepo.emptyAllEmployeeInfo();
			employeeDeclare.emptyAllEmployeeDeclare();
			sixGold.emptyAllSixGold();
			sixGoldScale.emptyAllSixGoldScale();
			result.put("errCode",0);
			result.put("errMsg","清空成功");
		}catch (Exception e) {
			// TODO: handle exception
			result.put("errCode",1);
			result.put("errMsg"," 清空失败");
		}
		return result;
	}
}
