package com.charge.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.p3.core.utils.common.StringUtils;
import org.springframework.stereotype.Repository;

import com.charge.entity.EmployeeDeclareEntity;
import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.EmployeeSalaryEntity;
import com.charge.entity.EmployeeSalaryExcelEntity;
import com.charge.entity.SalaryEntity;
import com.charge.entity.SalaryExcleEntity;
import com.charge.entity.SixGoldEntity;

/**
 * @author
 * @version v1.0
 * @Title: EmployeeSalaryRepository
 * @Description: 员工薪酬持久层
 * @date 2018年3月26日
 */
@Repository
public class SalaryRepository extends GenericBaseCommonDao<SalaryEntity, Integer> {


    /******************************************     修改区域开始   修改人：文世庭           ******************************************/


    /**
     * 双发地1所有名称
     *
     * @param status
     * @return
     */
    public List<String> getPlaceOneGroup(String departId, Integer status) {
        List<String> placeOneList = null;
        if (StringUtils.isNotBlank(departId)) {
            placeOneList = getSession().createQuery("select t.placeOne from SalaryEntity t where t.deleteFlg = 0 and "
                    + " t.employeeInfo.department.id=:departId and t.state = :status group by t.placeOne")
                    .setString("departId", departId).setInteger("status", status).list();
        } else {
            placeOneList = getSession().createQuery("select t.placeOne from SalaryEntity t where t.deleteFlg = 0 and "
                    + "t.state = :status group by t.placeOne").setInteger("status", status).list();
        }
        return placeOneList;
    }

    /**
     * 双发地1所有名称
     *
     * @param status
     * @return
     */
    public List<String> getPlaceOneGroupNew(String departId) {
        List<String> placeOneList = null;
        if (StringUtils.isNotBlank(departId)) {
            placeOneList = getSession().createQuery("select t.placeOne from SalaryEntity t where t.deleteFlg = 0 and "
                    + " t.employeeInfo.department.id=:departId group by t.placeOne")
                    .setString("departId", departId).list();
        } else {
            placeOneList = getSession().createQuery("select t.placeOne from SalaryEntity t where t.deleteFlg = 0 "
                    + "group by t.placeOne").list();
        }
        return placeOneList;
    }


    /**
     * 双发地1为条件查找数据
     *
     * @param placeOne
     * @return
     */
    public List<SalaryEntity> findByPlaceOne(String departId, String placeOne, Integer status) {
        List<SalaryEntity> salaryList = null;
        if (StringUtils.isNotBlank(departId)) {
            salaryList = getSession().createQuery("from SalaryEntity t where t.deleteFlg = 0 and "
                    + " t.employeeInfo.department.id=:departId and t.state = :status and t.placeOne = :placeOne ")
                    .setString("departId", departId).setInteger("status", status).setString("placeOne", placeOne).list();
        } else {
            salaryList = getSession().createQuery("from SalaryEntity t where t.deleteFlg = 0 and "
                    + " t.state = :status and t.placeOne = :placeOne ")
                    .setInteger("status", status).setString("placeOne", placeOne).list();
        }
        return salaryList;
    }

    /**
     * 双发地1为条件查找数据
     *
     * @param placeOne
     * @return
     */
    public List<SalaryEntity> findByPlaceOneNew(String departId, String placeOne) {
        List<SalaryEntity> salaryList = null;
        if (StringUtils.isNotBlank(departId)) {
            salaryList = getSession().createQuery("from SalaryEntity t where t.deleteFlg = 0 and "
                    + " t.employeeInfo.department.id=:departId and t.placeOne = :placeOne ")
                    .setString("departId", departId).setString("placeOne", placeOne).list();
        } else {
            salaryList = getSession().createQuery("from SalaryEntity t where t.deleteFlg = 0 and "
                    + " t.placeOne = :placeOne ")
                    .setString("placeOne", placeOne).list();
        }
        return salaryList;
    }


    /**
     * 获取所有申报数据
     *
     * @param departID
     * @return
     */
    public List<SalaryEntity> getSameMonthEmployeeSalary(String departId, Integer state) {
        List<SalaryEntity> salaryEntityList = new ArrayList<SalaryEntity>();
        //部门为空查询所有
        if (StringUtils.isNotBlank(departId)) {
            salaryEntityList = getSession().createQuery("from SalaryEntity where "
                    + " state = :state and employeeInfo.department.id = :departId")
                    .setInteger("state", state).setString("departId", departId).list();
        } else {
            salaryEntityList = getSession().createQuery("from SalaryEntity where "
                    + " state = :state").setInteger("state", state).list();
        }
        return salaryEntityList;
    }

    public List<SalaryEntity> getSameMonthEmployeeSalaryNew(String departId, Integer state, Integer employeeFlag) {
        List<SalaryEntity> salaryEntityList = new ArrayList<SalaryEntity>();
        //部门为空查询所有
        if (StringUtils.isNotBlank(departId)) {
            salaryEntityList = getSession().createQuery("from SalaryEntity where employeeFlag = :employeeFlag and"
                    + " state = :state and employeeInfo.department.id = :departId").setInteger("employeeFlag", employeeFlag)
                    .setInteger("state", state).setString("departId", departId).list();
        } else {
            salaryEntityList = getSession().createQuery("from SalaryEntity where employeeFlag = :employeeFlag and"
                    + " state = :state").setInteger("employeeFlag", employeeFlag).setInteger("state", state).list();
        }
        return salaryEntityList;
    }


    /******************************************     修改区域结束          ******************************************/


    /**
     * 更新
     *
     * @param employeeDeclareEntity
     */
    public void update(SalaryEntity salaryEntity) {
        getSession().update(salaryEntity);
//        getSession().flush();
    }

    /**
     * 获取所有申报数据
     *
     * @param departID
     * @return
     */
    public List<SalaryEntity> getSameMonthDeclare(String departId, Integer state) {
        List<SalaryEntity> salaryEntityList = getSession().createQuery("from SalaryEntity t where t.employeeInfo.department.id=:departId and"
                + " t.deleteFlg = 0 and state = :state and date_format(t.createdDate,'%Y-%m')=date_format(now(),'%Y-%m')")
                .setString("departId", departId).setInteger("state", state).list();
        return salaryEntityList;
    }

    /**
     * 获取所有申报数据(外派)
     *
     * @param departID
     * @return
     */
    public List<SalaryEntity> getSameMonthDeclareTech(String departId, Integer state) {
        String sql = "select * from c_salary t inner join c_employee_info e on t.employee_id = e.id INNER JOIN"
                + " t_s_depart d on e.department = d.ID where e.employee_flag = 0 and e.department = '" + departId + "' and t.state = " + state + " and "
                + "t.delete_flg = 0 and date_format(t.created_date,'%Y-%m')=date_format(now(),'%Y-%m')";
        SQLQuery q = getSession().createSQLQuery(sql)
                .addEntity(SalaryEntity.class);
        List<SalaryEntity> salaryEntityList = q.list();
        return salaryEntityList;
    }

    /**
     * 获取所有申报数据（本社）
     *
     * @param departID
     * @return
     */
    public List<SalaryEntity> getSameMonthDeclareOp(String departId, Integer state) {
        String sql = "select * from c_salary t inner join c_employee_info e on t.employee_id = e.id INNER JOIN"
                + " t_s_depart d on e.department = d.ID where e.employee_flag = 1 and e.department = '" + departId + "' and t.state = " + state + " and "
                + "t.delete_flg = 0 and date_format(t.created_date,'%Y-%m')=date_format(now(),'%Y-%m')";
        SQLQuery q = getSession().createSQLQuery(sql)
                .addEntity(SalaryEntity.class);
        List<SalaryEntity> salaryEntityList = q.list();
        return salaryEntityList;
    }

    /**
     * 统计当月记录数
     *
     * @return
     */
    public Long countBySameMonth(String departId) {
        Long sum = (Long) getSession().createQuery("select count(*) from SalaryEntity t where t.employeeInfo.department.id=:departId and"
                + " t.deleteFlg = 0 and date_format(t.createdDate,'%Y-%m')<date_format(now(),'%Y-%m')").setString("departId", departId).uniqueResult();
        return sum;
    }

    /**
     * 获取所有薪酬数据
     *
     * @param departID
     * @return
     */
    public List<SalaryEntity> getSameMonthDeclareAdd(String departId) {
        List<SalaryEntity> saEntityList = getSession().createQuery("from SalaryEntity t where t.employeeInfo.department.id=:departId and "
                + "t.deleteFlg = 0").setString("departId", departId).list();
        return saEntityList;
    }

    /**
     * 获取薪酬状态
     *
     * @return
     */
    public Integer getStatusCount(Long idp) {
        Integer st = null;
        st = (Integer) getSession().createQuery("select state from SalaryEntity s where "
                + "s.deleteFlg=0 and s.id = :idp").setLong("idp", idp).uniqueResult();
        return st;
    }

    /**
     * 查询员工薪酬的数据（list）
     *
     * @return
     */
    public List<SalaryEntity> findByDepartIds() {
        List<SalaryEntity> esList = getSession().createQuery("from SalaryEntity").list();
        return esList;
    }

    public List<SalaryEntity> findInsert(SalaryEntity salaryEntity) {
        /*getSession().update(salaryEntity);*/
        List<SalaryEntity> sEntityList = getSession().createSQLQuery("insert into SalaryEntity(salaryDate, salaryOne) select declareDate,"
                + "a1Salary from EmployeeDeclareEntity  WHERE   declareStatus = 1").list();
        return sEntityList;
    }

    /**
     * 判断是否生成过
     *
     * @param salaryDate
     * @param code
     * @param departId
     * @return
     */
    public SalaryEntity findByCodeAndDate(Date salaryDate, String code) {
        List<SalaryEntity> list = getSession().createQuery("from SalaryEntity s where s.employeeInfo.code=:code and s.salaryDate=:salaryDate and s.deleteFlg=0").setString("code", code).setDate("salaryDate", salaryDate).list();

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }
}
