package com.charge.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
* @Title: DictCategoryService
* @Description: 字典分类业务层
* @author wenst
* @date 2018年4月20日
* @version v1.0
*/
@Controller
public class DictCategoryService {

	@Autowired
	private CommonService commonService;


	/**
	 * 获取顶层管理员
	 * @return
	 */
	public List<String> getTopManager(String dictCategory){
		List<String> topManagerList = new ArrayList<String>();
		List<TSTypegroup> tsTypeGroupList = commonService.findByProperty(TSTypegroup.class, "typegroupcode", dictCategory);
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


	/**
	 * 获取字典内容信息
	 * @return
	 */
	public String getRoleCode(String dictCategory,String typename){
		String rs = null;
		if(StringUtils.isNotBlank(dictCategory)&&StringUtils.isNotBlank(typename)){
			List<TSTypegroup> tsTypeGroupList = commonService.findByProperty(TSTypegroup.class, "typegroupcode", dictCategory);
			if(!tsTypeGroupList.isEmpty()&&!tsTypeGroupList.get(0).getTSTypes().isEmpty()){
				for(int i =0;i<tsTypeGroupList.get(0).getTSTypes().size();i++){
					TSType tsType = tsTypeGroupList.get(0).getTSTypes().get(i);
					if(typename.equals(tsType.getTypename())){
						rs = tsType.getTypecode();
					}
				}
			}
		}
		return rs;
	}


	/**
	 * 获取字典内容信息
	 * @return
	 */
	public Map<String,String> getRoleNameCode(String dictCategory){
		Map<String,String> result = new HashMap<String,String>();
		if(StringUtils.isNotBlank(dictCategory)){
			List<TSTypegroup> tsTypeGroupList = commonService.findByProperty(TSTypegroup.class, "typegroupcode", dictCategory);
			if(!tsTypeGroupList.isEmpty()&&!tsTypeGroupList.get(0).getTSTypes().isEmpty()){
				for(int i =0;i<tsTypeGroupList.get(0).getTSTypes().size();i++){
					TSType tsType = tsTypeGroupList.get(0).getTSTypes().get(i);
					result.put(tsType.getTypename(), tsType.getTypecode());
				}
			}
		}
		return result;
	}


	/**
	 * 获取字典内容信息
	 * @return
	 */
	public List<String> getRoleCode(String dictCategory){
		List<String> result = new ArrayList<String>();
		if(StringUtils.isNotBlank(dictCategory)){
			List<TSTypegroup> tsTypeGroupList = commonService.findByProperty(TSTypegroup.class, "typegroupcode", dictCategory);
			if(!tsTypeGroupList.isEmpty()&&!tsTypeGroupList.get(0).getTSTypes().isEmpty()){
				for(int i =0;i<tsTypeGroupList.get(0).getTSTypes().size();i++){
					TSType tsType = tsTypeGroupList.get(0).getTSTypes().get(i);
					result.add(tsType.getTypecode());
				}
			}
		}
		return result;
	}

}
