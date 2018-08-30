package com.charge.repository;

import com.charge.entity.EmployeeInfoEntity;
import org.hibernate.SQLQuery;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.charge.entity.SixGoldEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
* @Title: SixGoldRepository
* @Description: 社保六金持久层
* @author wenst
* @date 2018年3月26日
* @version v1.0
*/
@Repository
public class SixGoldRepository extends GenericBaseCommonDao<SixGoldEntity, Integer>{


    /**
     * 旧版（新版去掉月份）
     * 根据员工编号获取六金数据
     *
     * @param employeeCode
     * @return
     */
    public SixGoldEntity findByEmployeeCode(String employeeCode, Date enterDate) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    	String date = sdf.format(enterDate);
    	try {
			Date month = sdf.parse(date);
	        List<SixGoldEntity> list = getSession().createQuery("from SixGoldEntity s where s.employeeCode=:employeeCode and s.enterDate=:enterDate and s.delFlg=0").setString("employeeCode", employeeCode).setDate("enterDate", month).list();
	//        List<SixGoldEntity> list =(List<SixGoldEntity>) getSession().createSQLQuery("select * from c_six_gold where employee_code=:employeeCode and  DATE_FORMAT(enter_date,'%Y-%M') =  DATE_FORMAT(:enterDate,'%Y-%M') and del_flg=0").setString("employeeCode", employeeCode).setDate("enterDate", enterDate).list();

	        if (list != null && !list.isEmpty()) {
	            return list.get(0);
	        }
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    /**
     * 新版（不需要传入月份）
     * 根据员工编号获取六金数据
     *
     * @param employeeCode
     * @return
     */
    public SixGoldEntity findByEmployeeCode(String employeeCode) {
        List<SixGoldEntity> list = getSession().createQuery("from SixGoldEntity s where s.employeeCode=:employeeCode and s.delFlg=0").setString("employeeCode", employeeCode).list();

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    /**
     * 判断是否生成过
     *
     */
    public  boolean isExist(String employeeCode, Date enterDate) {
        Long sum = (Long) getSession().createQuery("select count(*) from SixGoldEntity s where s.employeeCode=:employeeCode and s.enterDate=:enterDate and s.delFlg=0").setString("employeeCode", employeeCode).setDate("enterDate", enterDate).uniqueResult();
        if (sum > 0) {
            return true;
        }

        return false;
    }

    /**
     * 获取员工最后一次的六金
     *
     * @param employeeCode
     * @return
     */
    public SixGoldEntity findLastEnterDateByEmployee(String employeeCode) {
        List<SixGoldEntity> list = getSession().createQuery("from SixGoldEntity s where s.employeeCode=:employeeCode and s.delFlg=0 order by enterDate DESC").setString("employeeCode", employeeCode).list();

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }
	/**
	 * 清空表SixGold
	 *  2018-07-11
	 */
	public void emptyAllSixGold(){
		getSession().createQuery("delete SixGoldEntity").executeUpdate();
	}
}
