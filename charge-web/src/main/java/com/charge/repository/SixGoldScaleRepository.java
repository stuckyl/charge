package com.charge.repository;

import static org.hamcrest.Matchers.stringContainsInOrder;

import java.util.ArrayList;
import java.util.List;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.springframework.stereotype.Repository;

import com.charge.entity.SixGoldScaleEntity;

/**
* @Title: SixGoldScaleRepository
* @Description: 社保六金比例
* @author wenst
* @date 2018年4月21日
* @version v1.0
*/
@Repository
public class SixGoldScaleRepository extends GenericBaseCommonDao<SixGoldScaleEntity, Integer>{


	/**
	 * 查询六金城市分组
	 * @return
	 */
	public List<String> findSixGoldCityGroup(){
		List<String> sixGoldCityGroupList = getSession().createQuery("select t.sixGoldPlace from "
				+ "SixGoldScaleEntity t group by t.sixGoldPlace").list();
		return sixGoldCityGroupList;
	}

	public List<String> findPayArea(){
		List<TSType> tsTypes = getSession().createQuery("select t.TSTypes from "
				+ "TSTypegroup t where t.typegroupcode = :type").setString("type", "payArea").list();
		List<String> citys = new ArrayList<>();
		for(TSType city : tsTypes)
			citys.add(city.getTypename());
		return citys;
	}

	/**
	 * 根据 城市名 返回六金信息
	 * @param city
	 * @return
	 */
	public SixGoldScaleEntity findByCity(String city) {
		SixGoldScaleEntity sixGoldScale = (SixGoldScaleEntity) getSession().createQuery("from SixGoldScaleEntity s where s.sixGoldPlace = :city").setString("city", city).uniqueResult();
		return sixGoldScale;
	}
	/**
	 * 清空表SixCity
	 *  2018-07-11
	 */
	public void emptyAllSixGoldScale(){
		getSession().createQuery("delete SixGoldScaleEntity").executeUpdate();
	}

}