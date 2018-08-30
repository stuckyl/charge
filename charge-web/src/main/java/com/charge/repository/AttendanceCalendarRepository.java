package com.charge.repository;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.charge.entity.AttendanceCalendarEntity;

/**
* @Title: AttendanceCalendarRepository
* @Description: 出勤日历持久层
* @author wenst
* @date 2018年3月26日
* @version v1.0
*/
@Repository
public class AttendanceCalendarRepository extends GenericBaseCommonDao<AttendanceCalendarEntity, Integer>{

	/**
	 * 根据年月查询出勤天数
	 * @return
	 */
	public Double getYearMonthDays(Integer year,Integer month)throws NullPointerException{

		String mon = getMonthField(month);
		Integer c = (Integer) getSession().createQuery("select "+mon+" from AttendanceCalendarEntity t where "
				+ "t.year=:year").setInteger("year", year).uniqueResult();
		Double doubleValue = null;
		doubleValue = c.doubleValue();
		return doubleValue;
	}
	private String getMonthField(Integer month) {
		String mon = "month1";
		switch(month) {
		case 1: mon = "month1"; break;
		case 2: mon="month2";break;
		case 3: mon = "month3";break;
		case 4: mon = "month4";break;
		case 5: mon = "month5";break;
		case 6: mon = "month6";break;
		case 7: mon = "month7";break;
		case 8: mon = "month8";break;
		case 9: mon = "month9";break;
		case 10: mon = "month10";break;
		case 11: mon = "month11";break;
		case 12: mon = "month12";break;
		}
		return mon;
	}
	/**
	 *根据传来的年份查询实体类
	 * */
	public List finYearsByyear(String year) {
		// TODO Auto-generated method stub
		List list = new LinkedList<>();
		int parseInt = Integer.parseInt(year);
		list = getSession().createQuery("select year from AttendanceCalendarEntity t where "
				+ "t.year=:year").setInteger("year",parseInt).list();
		return list;
	}
}
