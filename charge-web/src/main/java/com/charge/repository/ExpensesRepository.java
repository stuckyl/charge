package com.charge.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.p3.core.utils.common.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.ExpensesEntity;

/**
* @Title: EmployeeDeclareRepository
* @Description: 员工申报表持久层
* @author wenst
* @date 2018年3月22日
* @version v1.0
*/
@Repository
public class ExpensesRepository extends GenericBaseCommonDao<EmployeeDeclareEntity, Integer>{

	@Autowired
	private CommonRepository commonRepository;

	/**
	 * 180725 新增 批量 by 员工申报表主键（id） 查找
	 * @param lids
	 * @author gc
	 */
	public List<ExpensesEntity> findAllByExpensesId(List<Integer> lids) {
		List<ExpensesEntity> expensesList = new ArrayList<ExpensesEntity>();
		expensesList = getSession().createQuery("from ExpensesEntity t where t.id in (:ids) ").setParameterList("ids", lids).list();
		return expensesList;
	}

	/**
	 * 更新
	 * @param employeeDeclareEntity
	 */
	public void update(ExpensesEntity ExpensesEntity){
		getSession().update(ExpensesEntity);
		getSession().flush();
	}
	/**
	 * 通过ID查找申报经费数据
	 * @param id
	 * @return
	 */
	public ExpensesEntity findByExpensesId(Integer id){
		ExpensesEntity expensesDeclareEntity = (ExpensesEntity) getSession().createQuery("from ExpensesEntity t "
				+ "where t.id = :id").setInteger("id", id).uniqueResult();
		return expensesDeclareEntity;
	}
}
