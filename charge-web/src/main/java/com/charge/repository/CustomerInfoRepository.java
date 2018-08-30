package com.charge.repository;

import java.util.List;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.charge.entity.CustomerInfoEntity;

/**
* @Title: CustomerInfoRepository
* @Description: 客户信息持久层
* @author wenst
* @date 2018年3月26日
* @version v1.0
*/
@Repository
public class CustomerInfoRepository extends GenericBaseCommonDao<CustomerInfoEntity, Integer>{

	public List findAllCustomer() {
		// TODO Auto-generated method stub
		List result = getSession().createQuery("from CustomerInfoEntity").list();
		return result;
	}

}
