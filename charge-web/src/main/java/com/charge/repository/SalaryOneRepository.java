package com.charge.repository;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.charge.entity.SalaryOneEntity;

/**  
* @Title: EmployeeSalaryRepository
* @Description: 员工薪酬持久层
* @author   
* @date 2018年3月26日
* @version v1.0  
*/  
@Repository
public class SalaryOneRepository extends GenericBaseCommonDao<SalaryOneEntity, Integer>{

	/**
	 * 查询员工薪酬的数据（list）
	 * @return
	 */
	public List<SalaryOneEntity> findByDepartIds(){
		List<SalaryOneEntity> esList = getSession().createQuery("from SalaryOneEntity").list();
		return esList;
	}

}
