package com.charge.service;

import com.charge.entity.EmployeeInfoEntity;
import com.charge.entity.SixGoldEntity;
import com.charge.repository.SixGoldRepository;
import com.charge.utils.CalendarUtil;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

@Service
@Transactional
public class SixGoldService {

    @Autowired
    private SixGoldRepository sixGoldRepository;

    @Autowired
    private CommonService commonService;

    /**
     *
     * @param employeeCode
     * @return
     */
    public SixGoldEntity findByEmployeeCode(String employeeCode, Date date) {

        return sixGoldRepository.findByEmployeeCode(employeeCode, date);
    }


    /**
     * 获取员工最后一个月
     *
     * @param 员工code
     * @param date
     * @return
     */
    public SixGoldEntity saveOneByLastMouth(String code, Date date, TSUser user) {
        SixGoldEntity sixGoldEntity = new SixGoldEntity();
        SixGoldEntity entity = sixGoldRepository.findLastEnterDateByEmployee(code);

        if (entity != null) {
            sixGoldEntity.setEmployeeCode(code);
            sixGoldEntity.setSixGoldPlace(entity.getSixGoldPlace());
            /**录入日期*/
            sixGoldEntity.setEnterDate(date);
            /**删除标记*/
            sixGoldEntity.setDelFlg(0);
            /**创建人*/
            sixGoldEntity.setCreatedBy(user.getId());
            /**创建日期*/
            java.util.Date currentDate = new java.util.Date();
            sixGoldEntity.setCreatedDate(currentDate);
            /**最后修改人*/
            sixGoldEntity.setLastModifiedBy(user.getId());
            /**最后修改时间*/
            sixGoldEntity.setLastModifiedDate(currentDate);
            sixGoldEntity.setNumMonth(1);

            Integer num = entity.getNumMonth()==null?1:entity.getNumMonth();

            if (num <= 1 ) {
                /**养老保险（企业）*/
                sixGoldEntity.setCompanyEndowment(entity.getCompanyEndowment());
                /**养老保险（个人）*/
                sixGoldEntity.setPersonalEndowment(entity.getPersonalEndowment());
                /**医疗保险（企业）*/
                sixGoldEntity.setCompanyMedical(entity.getCompanyMedical());
                /**医疗保险（个人）*/
                sixGoldEntity.setPersonalMedical(entity.getPersonalMedical());
                /**失业保险（企业）*/
                sixGoldEntity.setCompanyUnemployment(entity.getCompanyUnemployment());
                /**失业保险（个人）*/
                sixGoldEntity.setPersonalUnemployment(entity.getPersonalUnemployment());
                /**工伤（企业）*/
                sixGoldEntity.setCompanyInjury(entity.getCompanyInjury());
                /**生育（企业）*/
                sixGoldEntity.setCompanyMaternity(entity.getCompanyMaternity());
                /**住房公积金（企业）*/
                sixGoldEntity.setCompanyHousingFund(entity.getCompanyHousingFund());
                /**住房公积金（个人）*/
                sixGoldEntity.setPersonalHousingFund(entity.getPersonalHousingFund());
            } else {
/**养老保险（企业）*/
                sixGoldEntity.setCompanyEndowment(new BigDecimal(entity.getCompanyEndowment()/num).setScale(2, RoundingMode.UP).doubleValue());
                /**养老保险（个人）*/
                sixGoldEntity.setPersonalEndowment(new BigDecimal(entity.getPersonalEndowment()/num).setScale(2, RoundingMode.UP).doubleValue());
                /**医疗保险（企业）*/
                sixGoldEntity.setCompanyMedical(new BigDecimal(entity.getCompanyMedical()/num).setScale(2, RoundingMode.UP).doubleValue());
                /**医疗保险（个人）*/
                sixGoldEntity.setPersonalMedical(new BigDecimal(entity.getPersonalMedical()/num).setScale(2, RoundingMode.UP).doubleValue());
                /**失业保险（企业）*/
                sixGoldEntity.setCompanyUnemployment(new BigDecimal(entity.getCompanyUnemployment()/num).setScale(2, RoundingMode.UP).doubleValue());
                /**失业保险（个人）*/
                sixGoldEntity.setPersonalUnemployment(new BigDecimal(entity.getPersonalUnemployment()/num).setScale(2, RoundingMode.UP).doubleValue());
                /**工伤（企业）*/
                sixGoldEntity.setCompanyInjury(new BigDecimal(entity.getCompanyInjury()/num).setScale(2, RoundingMode.UP).doubleValue());
                /**生育（企业）*/
                sixGoldEntity.setCompanyMaternity(new BigDecimal(entity.getCompanyMaternity()/num).setScale(2, RoundingMode.UP).doubleValue());
                /**住房公积金（企业）*/
                sixGoldEntity.setCompanyHousingFund(new BigDecimal(entity.getCompanyHousingFund()/num).setScale(2, RoundingMode.UP).doubleValue());
                /**住房公积金（个人）*/
                sixGoldEntity.setPersonalHousingFund(new BigDecimal(entity.getPersonalHousingFund()/num).setScale(2, RoundingMode.UP).doubleValue());
            }

            /**企业合计*/
            sixGoldEntity.setCompanySum(new BigDecimal(sixGoldEntity.getCompanyEndowment() + sixGoldEntity.getCompanyMedical() + sixGoldEntity.getCompanyUnemployment() + sixGoldEntity.getCompanyInjury() + sixGoldEntity.getCompanyHousingFund() + sixGoldEntity.getCompanyMaternity()).setScale(2, RoundingMode.UP).doubleValue());
            /**个人合计*/
            sixGoldEntity.setPersonalSum(new BigDecimal(sixGoldEntity.getPersonalEndowment() + sixGoldEntity.getPersonalMedical() + sixGoldEntity.getPersonalUnemployment() + sixGoldEntity.getPersonalHousingFund()).setScale(2, RoundingMode.UP).doubleValue());

            sixGoldRepository.save(sixGoldEntity);
        }

        return sixGoldEntity;
    }

    /**
     * 生成一条默认的六金
     *
     * @param employeeInfo
     * @param id
     */
    public void saveOne(EmployeeInfoEntity employeeInfo, String id) {
        // 获取这个月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        java.util.Date date = null;
        try {
            date = sdf.parse(CalendarUtil.getFirstMonthDay("yyyyMM"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String code = employeeInfo.getCode();
        Double baseMoney = employeeInfo.getSixGoldBase();
        String place = employeeInfo.getSixGoldCity();

        if (!sixGoldRepository.isExist(code, date)) {
            if (baseMoney > 0) {
                SixGoldEntity sixGoldEntity = new SixGoldEntity();
                sixGoldEntity.setEmployeeCode(code);
                sixGoldEntity.setSixGoldPlace(employeeInfo.getSixGoldCity());
                /**录入日期*/
                sixGoldEntity.setEnterDate(date);
                /**删除标记*/
                sixGoldEntity.setDelFlg(0);
                /**创建人*/
                sixGoldEntity.setCreatedBy(id);
                /**创建日期*/
                java.util.Date currentDate = new java.util.Date();
                sixGoldEntity.setCreatedDate(currentDate);
                /**最后修改人*/
                sixGoldEntity.setLastModifiedBy(id);
                /**最后修改时间*/
                sixGoldEntity.setLastModifiedDate(currentDate);

                if (place.equals("上海")) {
                    /**养老保险（企业）*/
                    sixGoldEntity.setCompanyEndowment(new BigDecimal(baseMoney * 20 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**养老保险（个人）*/
                    sixGoldEntity.setPersonalEndowment(new BigDecimal(baseMoney * 8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（企业）*/
                    sixGoldEntity.setCompanyMedical(new BigDecimal(baseMoney * 9.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（个人）*/
                    sixGoldEntity.setPersonalMedical(new BigDecimal(baseMoney * 2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（企业）*/
                    sixGoldEntity.setCompanyUnemployment(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（个人）*/
                    sixGoldEntity.setPersonalUnemployment(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**工伤（企业）*/
                    sixGoldEntity.setCompanyInjury(new BigDecimal(baseMoney * 0.2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**生育（企业）*/
                    sixGoldEntity.setCompanyMaternity(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（企业）*/
                    sixGoldEntity.setCompanyHousingFund(new BigDecimal(baseMoney * 7 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（个人）*/
                    sixGoldEntity.setPersonalHousingFund(new BigDecimal(baseMoney * 7 / 100).setScale(2, RoundingMode.UP).doubleValue());

                } else if (place.equals("昆山")) {
                    /**养老保险（企业）*/
                    sixGoldEntity.setCompanyEndowment(new BigDecimal(baseMoney * 19 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**养老保险（个人）*/
                    sixGoldEntity.setPersonalEndowment(new BigDecimal(baseMoney * 8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（企业）*/
                    sixGoldEntity.setCompanyMedical(new BigDecimal(baseMoney * 8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（个人）*/
                    sixGoldEntity.setPersonalMedical(new BigDecimal(baseMoney * 2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（企业）*/
                    sixGoldEntity.setCompanyUnemployment(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（个人）*/
                    sixGoldEntity.setPersonalUnemployment(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**工伤（企业）*/
                    sixGoldEntity.setCompanyInjury(new BigDecimal(baseMoney * 1.6 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**生育（企业）*/
                    sixGoldEntity.setCompanyMaternity(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（企业）*/
                    sixGoldEntity.setCompanyHousingFund(new BigDecimal(baseMoney * 8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（个人）*/
                    sixGoldEntity.setPersonalHousingFund(new BigDecimal(baseMoney * 8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                } else if (place.equals("北京")) {
                    /**养老保险（企业）*/
                    sixGoldEntity.setCompanyEndowment(new BigDecimal(baseMoney * 19 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**养老保险（个人）*/
                    sixGoldEntity.setPersonalEndowment(new BigDecimal(baseMoney * 8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（企业）*/
                    sixGoldEntity.setCompanyMedical(new BigDecimal(baseMoney * 10 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（个人）*/
                    sixGoldEntity.setPersonalMedical(new BigDecimal(baseMoney * 2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（企业）*/
                    sixGoldEntity.setCompanyUnemployment(new BigDecimal(baseMoney * 0.8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（个人）*/
                    sixGoldEntity.setPersonalUnemployment(new BigDecimal(baseMoney * 0.2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**工伤（企业）*/
                    sixGoldEntity.setCompanyInjury(new BigDecimal(baseMoney * 0.2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**生育（企业）*/
                    sixGoldEntity.setCompanyMaternity(new BigDecimal(baseMoney * 0.8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（企业）*/
                    sixGoldEntity.setCompanyHousingFund(new BigDecimal(baseMoney * 12 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（个人）*/
                    sixGoldEntity.setPersonalHousingFund(new BigDecimal(baseMoney * 12 / 100).setScale(2, RoundingMode.UP).doubleValue());
                } else if (place.equals("深圳")) {
                    /**养老保险（企业）*/
                    sixGoldEntity.setCompanyEndowment(new BigDecimal(baseMoney * 13 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**养老保险（个人）*/
                    sixGoldEntity.setPersonalEndowment(new BigDecimal(baseMoney * 8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（企业）*/
                    sixGoldEntity.setCompanyMedical(new BigDecimal(baseMoney * 6.2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（个人）*/
                    sixGoldEntity.setPersonalMedical(new BigDecimal(baseMoney * 2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（企业）*/
                    sixGoldEntity.setCompanyUnemployment(new BigDecimal(baseMoney * 1 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（个人）*/
                    sixGoldEntity.setPersonalUnemployment(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**工伤（企业）*/
                    sixGoldEntity.setCompanyInjury(new BigDecimal(baseMoney * 0.28 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**生育（企业）*/
                    sixGoldEntity.setCompanyMaternity(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（企业）*/
                    sixGoldEntity.setCompanyHousingFund(new BigDecimal(baseMoney * 5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（个人）*/
                    sixGoldEntity.setPersonalHousingFund(new BigDecimal(baseMoney * 5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                } else if (place.equals("广州")) {
                    /**养老保险（企业）*/
                    sixGoldEntity.setCompanyEndowment(new BigDecimal(baseMoney * 14 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**养老保险（个人）*/
                    sixGoldEntity.setPersonalEndowment(new BigDecimal(baseMoney * 8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（企业）*/
                    sixGoldEntity.setCompanyMedical(new BigDecimal(baseMoney * 6.2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（个人）*/
                    sixGoldEntity.setPersonalMedical(new BigDecimal(baseMoney * 2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（企业）*/
                    sixGoldEntity.setCompanyUnemployment(new BigDecimal(baseMoney * 0.64 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（个人）*/
                    sixGoldEntity.setPersonalUnemployment(new BigDecimal(baseMoney * 0.2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**工伤（企业）*/
                    sixGoldEntity.setCompanyInjury(new BigDecimal(baseMoney * 0.2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**生育（企业）*/
                    sixGoldEntity.setCompanyMaternity(new BigDecimal(baseMoney * 0.85 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（企业）*/
                    sixGoldEntity.setCompanyHousingFund(new BigDecimal(baseMoney * 5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（个人）*/
                    sixGoldEntity.setPersonalHousingFund(new BigDecimal(baseMoney * 5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                } else {
                    /**养老保险（企业）*/
                    sixGoldEntity.setCompanyEndowment(new BigDecimal(baseMoney * 20 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**养老保险（个人）*/
                    sixGoldEntity.setPersonalEndowment(new BigDecimal(baseMoney * 8 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（企业）*/
                    sixGoldEntity.setCompanyMedical(new BigDecimal(baseMoney * 9.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**医疗保险（个人）*/
                    sixGoldEntity.setPersonalMedical(new BigDecimal(baseMoney * 2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（企业）*/
                    sixGoldEntity.setCompanyUnemployment(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**失业保险（个人）*/
                    sixGoldEntity.setPersonalUnemployment(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**工伤（企业）*/
                    sixGoldEntity.setCompanyInjury(new BigDecimal(baseMoney * 0.2 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**生育（企业）*/
                    sixGoldEntity.setCompanyMaternity(new BigDecimal(baseMoney * 0.5 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（企业）*/
                    sixGoldEntity.setCompanyHousingFund(new BigDecimal(baseMoney * 7 / 100).setScale(2, RoundingMode.UP).doubleValue());
                    /**住房公积金（个人）*/
                    sixGoldEntity.setPersonalHousingFund(new BigDecimal(baseMoney * 7 / 100).setScale(2, RoundingMode.UP).doubleValue());
                }

                /**企业合计*/
                sixGoldEntity.setCompanySum(new BigDecimal(sixGoldEntity.getCompanyEndowment() + sixGoldEntity.getCompanyMedical() + sixGoldEntity.getCompanyUnemployment() + sixGoldEntity.getCompanyInjury() + sixGoldEntity.getCompanyHousingFund() + sixGoldEntity.getCompanyMaternity()).setScale(2, RoundingMode.UP).doubleValue());
                /**个人合计*/
                sixGoldEntity.setPersonalSum(new BigDecimal(sixGoldEntity.getPersonalEndowment() + sixGoldEntity.getPersonalMedical() + sixGoldEntity.getPersonalUnemployment() + sixGoldEntity.getPersonalHousingFund()).setScale(2, RoundingMode.UP).doubleValue());

                sixGoldRepository.save(sixGoldEntity);
            }
        }
    }

}