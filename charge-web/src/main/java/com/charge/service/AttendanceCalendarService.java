package com.charge.service;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.charge.entity.AttendanceCalendarEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.repository.AttendanceCalendarRepository;

@Service
@Transactional
public class AttendanceCalendarService{

	@Autowired
	 AttendanceCalendarRepository  attendanceCalendarRepo ;
	/**
	 * 根据传来的年份查询数据库中相同的年份
	 * */
	public List watchYears(String year){
		List list = new LinkedList<>();
		list = attendanceCalendarRepo.finYearsByyear(year);
		return list;

	}

}