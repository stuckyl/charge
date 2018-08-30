package com.charge.repository;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDataRule;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.stereotype.Repository;

import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.ProjectCopyEntity;
import com.charge.entity.ProjectEntity;
import com.charge.utils.CalendarUtil;
import com.fasterxml.jackson.core.io.SegmentedStringWriter;

/**
* @Title: ProjectRepository
* @Description: 项目收支持久层
* @author sunyanjie
* @date 2018年8月08日
* @version v1.0
*/
@Repository
public class ProjectRepository extends GenericBaseCommonDao<ProjectEntity, Integer>{
	private static final Logger log = Logger.getLogger(ProjectRepository.class);

	/**
	 * 查找固定项目信息
	 * j 5-9
	 *
	 * @param id
	 *
	 */

	public List<ProjectEntity> findByDeclareStatusId(List<Integer> id){
		List<ProjectEntity> projectList = null;
		projectList = getSession().createQuery("from ProjectEntity t where "
				+ "t.delFlage = 0 and t.id in (:id) ").setParameterList("id", id).list();
		return projectList;
	}

	/**
	 * 更新项目信息
	 * @param project
	 */
	public void update(ProjectEntity project){
		getSession().update(project);
		getSession().flush();
	}

	/**
	 * 通过登陆用户ID 找到对应角色名 (t_reporter可以录入本部员工)
	 * @author gc
	 * 0517
	 */
	public String findMyRole(String id) {
		// TODO Auto-generated method stub
		String sql = "select tsr.rolecode from t_s_role tsr LEFT JOIN t_s_role_user tsru on tsr.id=tsru.roleid where userid=?";
		String roleName = (String) getSession().createSQLQuery(sql).setString(0, id).uniqueResult();
		return roleName;
	}


	/**
	 * 获取层级待处理消息数量
	 * @param lv
	 * @param orgCode
	 * @return
	 */
	public int getMessageCount(int lv,String orgCode,boolean isManager) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from c_project where project_status=? and project_month<=?";
		BigInteger obj = new BigInteger("0");
		Date now = new Date();
		if(lv==1) {
			obj =  (BigInteger) getSession().createSQLQuery(sql).setInteger(0, 2).setDate(1, now).uniqueResult();
		}else if(lv==2){
			sql = "select count(*) from c_project where project_status>=? and project_status<=? and project_month<=?";
			obj =  (BigInteger) getSession().createSQLQuery(sql).setInteger(0, 3).setInteger(1,4).setDate(2, now).uniqueResult();
		}else {
			sql = "select count(*) from c_project where project_status>=? and project_status<=? and project_department in (select id from t_s_depart where org_code like ?) and project_month<=?";
			if(isManager) {
				obj = (BigInteger) getSession().createSQLQuery(sql).setInteger(0, (lv+1)*2-1).setInteger(1,(lv+1)*2).setString(2, orgCode+"%").setDate(3, now).uniqueResult();
			}else {
				obj = (BigInteger) getSession().createSQLQuery(sql).setInteger(0, lv*2-1).setInteger(1,lv*2).setString(2, orgCode+"%").setDate(3, now).uniqueResult();
			}
		}
		return obj.intValue();
	}

	/**
	 * 一键统计项目收支 - 从备份表
	 * @param date
	 * @param orgCode
	 * @return
	 */
	public Object oneKeyTotalforAccess(Date date, String orgCode,Integer customerId) {
		Object uniqueResult = null;
		if(customerId == -1||customerId == null) { // 没有顾客查询
			String sql ="select SUM(project_income) as '总收入',SUM(project_profit) as '总毛利',COUNT(*) as '总人数' from c_project_copy "
					+ "where DATE_FORMAT(project_month,'%Y-%m')=DATE_FORMAT(?,'%Y-%m') "
					+ "and project_department in (select id from t_s_depart where org_code like ?)";
			uniqueResult = getSession().createSQLQuery(sql).setDate(0, date).setString(1, orgCode+"%").uniqueResult();
		}else {
			String sql ="select SUM(project_income) as '总收入',SUM(project_profit) as '总毛利',COUNT(*) as '总人数' from c_project_copy "
					+ "where DATE_FORMAT(project_month,'%Y-%m')=DATE_FORMAT(?,'%Y-%m') "
					+ "and project_department in (select id from t_s_depart where org_code like ?) "
					+ "and project_customer1 = ?";
			uniqueResult = getSession().createSQLQuery(sql).setDate(0, date).setString(1, orgCode+"%").setInteger(2, customerId).uniqueResult();
		}
		return uniqueResult;
	}
	/**
	 *
	 * @param date
	 * @param orgCode
	 * @param customerId
	 * @return
	 */
	public Object oneKeyTotalGC(Date date, String orgCode, Integer customerId) {
		// TODO Auto-generated method stub
		Object uniqueResult = null;
		if(customerId == -1||customerId == null) { // 没有顾客查询
			String sql ="select SUM(project_income) as '总收入',SUM(project_profit) as '总毛利',COUNT(*) as '总人数' from c_project "
					+ "where DATE_FORMAT(project_month,'%Y-%m')=DATE_FORMAT(?,'%Y-%m') "
					+ "and project_department in (select id from t_s_depart where org_code like ?)";
			uniqueResult = getSession().createSQLQuery(sql).setDate(0, date).setString(1, orgCode+"%").uniqueResult();
		}else {
			String sql ="select SUM(project_income) as '总收入',SUM(project_profit) as '总毛利',COUNT(*) as '总人数' from c_project "
					+ "where DATE_FORMAT(project_month,'%Y-%m')=DATE_FORMAT(?,'%Y-%m') "
					+ "and project_department in (select id from t_s_depart where org_code like ?) "
					+ "and project_customer1 = ?";
			uniqueResult = getSession().createSQLQuery(sql).setDate(0, date).setString(1, orgCode+"%").setInteger(2, customerId).uniqueResult();
		}
		return uniqueResult;
	}

	/**
	 * 根据主键集合查询
	 * @param lids
	 * @return
	 */
	public List<ProjectEntity> findAllByProjectId(List<Integer> lids) {
		// TODO Auto-generated method stub
		List<ProjectEntity> projectList = new ArrayList<ProjectEntity>();
		projectList = getSession().createQuery("from ProjectEntity t where t.id in (:ids) ").setParameterList("ids", lids).list();
		return projectList;
	}
	/**
	 * 根据主键集合查询-访客专用
	 * @param lids
	 * @return
	 */
	public List<ProjectCopyEntity> findAllByProjectCopyId(List<Integer> lids) {
		// TODO Auto-generated method stub
		List<ProjectCopyEntity> projectCopyList = new ArrayList<ProjectCopyEntity>();
		projectCopyList = getSession().createQuery("from ProjectCopyEntity t where t.id in (:ids) ").setParameterList("ids", lids).list();
		return projectCopyList;
	}




}