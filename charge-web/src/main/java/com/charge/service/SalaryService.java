package com.charge.service;

import com.charge.entity.*;
import com.charge.repository.EmailConfigRepository;
import com.charge.repository.EmployeeDeclareRepository;
import com.charge.repository.EmployeeInfoRepository;
import com.charge.repository.SalaryRepository;
import com.charge.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.*;
import org.jeecgframework.web.system.service.SystemService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
public class SalaryService {
    private static final Logger logger = Logger.getLogger(SalaryService.class);

    @Autowired
    private SalaryRepository salaryRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private EmailConfigRepository emailConfigRepo;
    @Autowired
    private EmployeeDeclareRepository employeeDeclareRepository;

    @Autowired
    private EmployeeInfoRepository employeeInfoRepo;

    @Autowired
    private SixGoldService sixGoldService;

    @Autowired
    private EmailConfigService emailConfigService;

	private SystemService systemService;

	private SalaryService salaryService;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}


    /******************************************   修改区域开始    修改人：文世庭    ******************************************/

    /**
     * 申报*批量通过
     *
     * @return
     */
    public Map<String, Object> salarBybatchDeclare() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        //将当前用户部门下的所有本月待申报的数据加载出来
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        if (null != user) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            String departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
            if (null == departId) {
                result.put("errMsg", "当前登录用户：" + user.getUserName() + "所在部门为空");
                result.put("errCode", -1);
            } else {
                //加载录入未申报数据
                List<SalaryEntity> salaryEntityList = salaryRepository.getSameMonthDeclareTech(departId, 1);
                List<SalaryEntity> salaryEntityOpList = salaryRepository.getSameMonthDeclareOp(departId, 1);
                System.out.println("salaryEntityList .size :" + salaryEntityList.size());
                System.out.println("salaryEntityList  :" + salaryEntityList);
                if (!salaryEntityList.isEmpty() || !salaryEntityOpList.isEmpty()) {
                    updateDeclareStatus(user, salaryEntityList, 2);
                    updateDeclareStatus(user, salaryEntityOpList, 4);
                    //发送邮件通知给管理员
                    if (!salaryEntityList.isEmpty()) {
                        Map<String, String> tplContent = new HashMap<String, String>();
                        tplContent.put("subject", "申报通知");
                        tplContent.put("content", "员工：" + extractEmployeeNameInfo(salaryEntityList) + "申报通过，请及时处理");
                        logger.info(extractEmployeeNameInfo(salaryEntityList));
                        emailConfigService.departEmailNotice(departId, "empSyFlg", "3", tplContent);
                    }
                    if (!salaryEntityOpList.isEmpty()) {
                        Map<String, String> tplContentOp = new HashMap<String, String>();
                        tplContentOp.put("subject", "申报通知");
                        tplContentOp.put("content", "员工：" + extractEmployeeNameInfo(salaryEntityOpList) + "申报通过，请及时处理");
                        logger.info(extractEmployeeNameInfo(salaryEntityOpList));
                        emailConfigService.departEmailNotice(departId, "empSyFlg", "4", tplContentOp);
                    }
                } else {
                    result.put("errMsg", "没有需要申报的数据");
                    result.put("errCode", -1);
                }
            }
        } else {
            String errMsg = "当前用户未登录，无法进行申报操作";
            //log.info(errMsg);
            result.put("errMsg", errMsg);
            result.put("errCode", -1);
        }
        return result;
    }

    /**
     * 申报*批量退回
     *
     * @param departId
     * @param departName
     * @param returnReason
     * @return
     */
    public Map<String, Object> declareReturn(String returnReason) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        //将当前用户部门下的所有本月待申报的数据加载出来
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        if (null != user) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            String departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
            if (null == departId) {
                result.put("errMsg", "当前登录用户：" + user.getUserName() + "所在部门为空");
                result.put("errCode", -1);
            } else {
                //加载录入未申报数据
                List<SalaryEntity> salaryEntityList = salaryRepository.getSameMonthDeclare(departId, 1);
                System.out.println("salaryEntityList .size :" + salaryEntityList.size());
                System.out.println("salaryEntityList  :" + salaryEntityList);
                if (!salaryEntityList.isEmpty()) {
                    updateDeclareStatus(user, salaryEntityList, 3);
                    //发送邮件通知给管理员
                    Map<String, String> tplContent = new HashMap<String, String>();
                    tplContent.put("subject", "审核通知");
                    tplContent.put("content", "员工：" + extractEmployeeNameInfo(salaryEntityList) + "审核通过，请及时处理。");
                    logger.info(extractEmployeeNameInfo(salaryEntityList));
                    emailConfigService.departEmailNotice(departId, "empSyFlg", "1", tplContent);
                } else {
                    result.put("errMsg", "没有需要退回的数据");
                    result.put("errCode", -1);
                }
            }
        } else {
            String errMsg = "当前用户未登录，无法进行退回操作";
            //log.info(errMsg);
            result.put("errMsg", errMsg);
            result.put("errCode", -1);
        }
        return result;
    }

    /**
     * 审核* 批量通过
     *
     * @param departId
     * @param departName
     * @return
     */
    public Map<String, Object> auditPass() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        //将当前用户部门下的所有本月待申报的数据加载出来
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        if (null != user) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            String departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
            //加载录入未申报数据
            List<SalaryEntity> salaryList = salaryRepository.getSameMonthEmployeeSalary(departId, 2);
            if (!salaryList.isEmpty()) {
                updateDeclareStatus(user, salaryList, 4);
                Set<String> departSet = new HashSet<String>();
                for (int i = 0; i < salaryList.size(); i++) {
                    departSet.add(salaryList.get(i).getEmployeeInfo().getDepartment());
                }
                Map<String, String> tplContent = new HashMap<String, String>();
                tplContent.put("subject", "审核通知");
                tplContent.put("content", "员工：" + extractEmployeeNameInfo(salaryList) + "审核通过，请及时处理。");
                logger.info(extractEmployeeNameInfo(salaryList));
                emailConfigService.departEmailNotice(departId, "empSyFlg", "4", tplContent);
            } else {
                result.put("errMsg", "当前部门：【" + (StringUtils.isBlank(departId) ? "所有部门" : departId) + "】下没有需要申报通过的数据");
                result.put("errCode", -1);
            }
        } else {
            String errMsg = "当前用户未登录，无法进行申报操作";
            logger.info(errMsg);
            result.put("errMsg", errMsg);
            result.put("errCode", -1);
        }
        return result;
    }

    /**
     * 审核*批量退回
     *
     * @param departId
     * @param departName
     * @param returnReason
     * @return
     */
    public Map<String, Object> auditReturn(String returnReason) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        //当前登录人员信息
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        if (null != user) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            String departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
            //加载申报未通过的数据
            List<SalaryEntity> salaryList = salaryRepository.getSameMonthEmployeeSalary(departId, 2);
            if (!salaryList.isEmpty()) {
                updateDeclareStatus(user, salaryList, 5);
                Set<String> departSet = new HashSet<String>();
                for (int i = 0; i < salaryList.size(); i++) {
                    departSet.add(salaryList.get(i).getEmployeeInfo().getDepartment());
                }
                Map<String, String> tplContent = new HashMap<String, String>();
                tplContent.put("subject", "审核通知");
                tplContent.put("content", "员工：" + extractEmployeeNameInfo(salaryList) + "审核通过，请及时处理。");
                emailConfigService.departEmailNotice(departId, "empSyFlg", "2", tplContent);
            } else {
                result.put("errMsg", "当前部门：【" + (StringUtils.isBlank(departId) ? "所有部门" : departId) + "】下没有需要退回的申报数据");
                result.put("errCode", -1);
            }
        } else {
            String errMsg = "当前用户未登录，无法进行申报操作";
            logger.info(errMsg);
            result.put("errMsg", errMsg);
            result.put("errCode", -1);
        }
        return result;
    }

    /**
     * 审批* 批量通过
     *
     * @param departId
     * @param departName
     * @return
     */
    public Map<String, Object> batchPass() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        //将当前用户部门下的所有本月待申报的数据加载出来
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        if (null != user) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            String departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
            //加载录入未申报数据
            List<SalaryEntity> salaryList = salaryRepository.getSameMonthEmployeeSalary(departId, 4);
            if (!salaryList.isEmpty()) {
                updateDeclareStatus(user, salaryList, 6);
                Set<String> departSet = new HashSet<String>();
                for (int i = 0; i < salaryList.size(); i++) {
                    departSet.add(salaryList.get(i).getEmployeeInfo().getDepartment());
                }
            } else {
                result.put("errMsg", "当前部门：【" + (StringUtils.isBlank(departId) ? "所有部门" : departId) + "】下没有需要申报通过的数据");
                result.put("errCode", -1);
            }
        } else {
            String errMsg = "当前用户未登录，无法进行申报操作";
            logger.info(errMsg);
            result.put("errMsg", errMsg);
            result.put("errCode", -1);
        }
        return result;
    }

    /**
     * 审批*批量退回
     *
     * @param departId
     * @param departName
     * @param returnReason
     * @return
     */
    public Map<String, Object> batchReturn(String returnReason) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        //当前登录人员信息
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        if (null != user) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            String departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
            //加载申报未通过的数据
            List<SalaryEntity> salaryList = salaryRepository.getSameMonthEmployeeSalaryNew(departId, 4, 0);
            List<SalaryEntity> salaryListOp = salaryRepository.getSameMonthEmployeeSalaryNew(departId, 4, 1);
            if (!salaryList.isEmpty() || !salaryListOp.isEmpty()) {
                updateDeclareStatus(user, salaryList, 7);
                updateDeclareStatus(user, salaryListOp, 7);
                Set<String> departSet = new HashSet<String>();
                for (int i = 0; i < salaryList.size(); i++) {
                    departSet.add(salaryList.get(i).getEmployeeInfo().getDepartment());
                }
                //（外派人员退回发送邮件）
                Map<String, String> tplContent = new HashMap<String, String>();
                tplContent.put("subject", "审核通知");
                tplContent.put("content", "员工：" + extractEmployeeNameInfo(salaryList) + "审核退回，请及时处理。");
                emailConfigService.departEmailNotice(departId, "empSyFlg", "3", tplContent);
                //（本社人员退回发送邮件）
                Map<String, String> tplContentOp = new HashMap<String, String>();
                tplContentOp.put("subject", "审核通知");
                tplContentOp.put("content", "员工：" + extractEmployeeNameInfo(salaryListOp) + "审核退回，请及时处理。");
                emailConfigService.departEmailNotice(departId, "empSyFlg", "2", tplContentOp);

            } else {
                result.put("errMsg", "当前部门：【" + (StringUtils.isBlank(departId) ? "所有部门" : departId) + "】下没有需要退回的申报数据");
                result.put("errCode", -1);
            }
        } else {
            String errMsg = "当前用户未登录，无法进行申报操作";
            logger.info(errMsg);
            result.put("errMsg", errMsg);
            result.put("errCode", -1);
        }
        return result;
    }

    /**
     * 录入完成发送邮件
     *
     * @param departId
     * @return
     */
    public Map<String, Object> complete() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        //当前登录人员信息
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        if (null != user) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            String departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
            //加载录入完成的数据
            List<SalaryEntity> salaryList = salaryRepository.getSameMonthEmployeeSalary(departId, 0);
            if (!salaryList.isEmpty()) {
                updateDeclareStatus(user, salaryList, 1);
                Set<String> departSet = new HashSet<String>();
                for (int i = 0; i < salaryList.size(); i++) {
                    departSet.add(salaryList.get(i).getEmployeeInfo().getDepartment());
                }
                Map<String, String> tplContent = new HashMap<String, String>();
                tplContent.put("subject", "录入完成通知");
                tplContent.put("content", "员工：" + extractEmployeeNameInfo(salaryList) + "录入完成，请及时处理。");
                logger.info(extractEmployeeNameInfo(salaryList));
                emailConfigService.departEmailNotice(departId, "empSyFlg", "2", tplContent);
            } else {
                result.put("errMsg", "当前部门：【" + (StringUtils.isBlank(departId) ? "所有部门" : departId) + "】下没有录入完成的数据");
                result.put("errCode", -1);
            }
        } else {
            String errMsg = "当前用户未登录，无法进行申报操作";
            logger.info(errMsg);
            result.put("errMsg", errMsg);
            result.put("errCode", -1);
        }
        return result;
    }

    /**
     * 部门管理员邮件通知
     *
     * @param departId
     * @param salaryList
     * @return
     */
    private Map<String, Object> departManagerEmailNotice(String departId, List<SalaryEntity> salaryList, String returnReason, int contentType) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        TSDepart depart = commonService.get(TSDepart.class, departId);
        if (null != depart) {
            TSDepartAuthGroupEntity tsdAuthGroupList = commonService.findUniqueByProperty(TSDepartAuthGroupEntity.class, "deptCode", depart.getOrgCode());
            String groupId = "-1";
            if (null != tsdAuthGroupList) {
                groupId = tsdAuthGroupList.getId();
            }
            List<TSDepartAuthgManagerEntity> tsDepartAuthMangerList = commonService.findByProperty(
                    TSDepartAuthgManagerEntity.class, "groupId", groupId);
            for (int i = 0; i < tsDepartAuthMangerList.size(); i++) {
                List<TSUser> tsuserList = commonService.findByProperty(TSUser.class, "userName", tsDepartAuthMangerList.get(i).getUserId());
                if (!tsuserList.isEmpty()) {
                    //发送邮件 1-退回  2-通过
                    if (1 == contentType) {
                        mailSend(tsuserList.get(0).getEmail(), "申报退回通知", "员工：【" + extractEmployeeNameInfo(salaryList) + "】申报未通过，退回理由：" + returnReason);
                        logger.info("邮件通知发送成功，内容：员工：【" + extractEmployeeNameInfo(salaryList) + "】申报未通过，退回理由：" + returnReason);
                    } else if (2 == contentType) {
                        mailSend(tsuserList.get(0).getEmail(), "申报通过通知", "员工：【" + extractEmployeeNameInfo(salaryList) + "】申报已通过。");
                        logger.info("邮件通知发送成功，内容：员工：【" + extractEmployeeNameInfo(salaryList) + "】申报已通过。");
                    } else if (3 == contentType) {
                        mailSend(tsuserList.get(0).getEmail(), "录入完成通知", "员工：【" + extractEmployeeNameInfo(salaryList) + "】信息已经录入完成。");
                        logger.info("邮件通知发送成功，内容：员工：【" + extractEmployeeNameInfo(salaryList) + "】申报已通过。");
                    } else {
                        logger.info("没有对应内容类型，contentType=" + contentType);
                    }
                } else {
                    result.put("errCode", -1);
                    result.put("errMsg", "部门管理员：【" + tsDepartAuthMangerList.get(i).getUserId() + "】在系统中不存在");
                    logger.info("用户账号：" + tsDepartAuthMangerList.get(i).getUserId() + "在系统中不存在");
                }
            }
        } else {
            logger.info("部门：【" + departId + "】，在系统中不存在");
            result.put("errMsg", "当前部门：【" + departId + "】在系统中不存在");
            result.put("errCode", -1);
        }
        return result;
    }


    /**
     * 获取数据
     *
     * @param salaryEntity
     * @param parameterMap
     * @param dataGrid
     * @param orgIdList
     * @param status
     * @param delFlg
     */
    public void setDataGrid(SalaryEntity salaryEntity, Map<String, String[]> parameterMap, DataGrid dataGrid,
                            List<String> orgIdList, Object[] status, Integer delFlg) {
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        CriteriaQuery cq = new CriteriaQuery(SalaryEntity.class, dataGrid);
        cq.eq("deleteFlg", delFlg);
        cq.in("state", status);
        //超级管理员、顶层管理员可以看到所有数据
        List<String> topManager = getTopManager();
        if (!"admin".equals(user.getUserName()) && !topManager.contains(user.getUserName())) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            String departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
            if (null != departId) {
                orgIdList.add(departId);
            } else {
                logger.info("当前登录用户：" + user.getUserName() + "所在部门为空");
            }
        }
        if (!orgIdList.isEmpty()) {
            List<EmployeeInfoEntity> employeeList = employeeInfoRepo.findByDepartIdsAll(orgIdList.toArray(new String[orgIdList.size()]));
            if (!employeeList.isEmpty()) {
                Integer[] eids = new Integer[employeeList.size()];
                for (int i = 0; i < employeeList.size(); i++) {
                    eids[i] = employeeList.get(i).getId();
                }
                cq.in("employeeInfo.id", eids);
            } else {
                cq.eq("employeeInfo.id", -99999);
            }
        }
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, salaryEntity, parameterMap);
        commonService.getDataGridReturn(cq, true);
    }

    /**
     * 获取数据(外派审核)
     *
     * @param salaryEntity
     * @param parameterMap
     * @param dataGrid
     * @param orgIdList
     * @param status
     * @param delFlg
     */
    public void setAuditDataGrid(SalaryEntity salaryEntity, Map<String, String[]> parameterMap, DataGrid dataGrid,
                                 List<String> orgIdList, Object[] status, Integer delFlg) {
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        CriteriaQuery cq = new CriteriaQuery(SalaryEntity.class, dataGrid);
        cq.eq("deleteFlg", delFlg);
        cq.in("state", status);
        //超级管理员、顶层管理员可以看到所有数据
        List<String> topManager = getTopManager();
        if (!"admin".equals(user.getUserName()) && !topManager.contains(user.getUserName())) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            String departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
            if (null != departId) {
                orgIdList.add(departId);
            } else {
                logger.info("当前登录用户：" + user.getUserName() + "所在部门为空");
            }
        }
        if (!orgIdList.isEmpty()) {
            List<EmployeeInfoEntity> employeeList = employeeInfoRepo.findByDepartIds(orgIdList.toArray(new String[orgIdList.size()]));
            if (!employeeList.isEmpty()) {
                Integer[] eids = new Integer[employeeList.size()];
                for (int i = 0; i < employeeList.size(); i++) {
                    eids[i] = employeeList.get(i).getId();
                }
                cq.in("employeeInfo.id", eids);
            } else {
                cq.eq("employeeInfo.id", -99999);
            }
        }
        //查询条件组装器
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, salaryEntity, parameterMap);
        commonService.getDataGridReturn(cq, true);
    }


    /**
     * 获取顶层管理员
     *
     * @return
     */
    public List<String> getTopManager() {
        List<String> topManagerList = new ArrayList<String>();
        List<TSTypegroup> tsTypeGroupList = commonService.findByProperty(TSTypegroup.class, "typegroupcode", "topManager");
        if (!tsTypeGroupList.isEmpty() && !tsTypeGroupList.get(0).getTSTypes().isEmpty()) {
            for (int i = 0; i < tsTypeGroupList.get(0).getTSTypes().size(); i++) {
                TSType tsType = tsTypeGroupList.get(0).getTSTypes().get(i);
                //获取人员信息
                List<TSUser> tsuserList = commonService.findByProperty(TSUser.class, "userName", tsType.getTypecode());
                if (!tsuserList.isEmpty()) {
                    topManagerList.add(tsuserList.get(0).getUserName());
                }
            }
        }
        return topManagerList;
    }


    /**
     * @param declareStartDate
     * @param declareEndDate
     * @param departId
     * @return
     * @throws IOException
     */
    public Map<String, Object> exportDatas(String departId, Integer delFlg) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> links = new ArrayList<String>();
        String baseSavePath = ResourceUtil.getConfigByName("excelGeneratePath") + "/" + formatDate(new Date(), "yyyyMMdd");
        File baseSavePathFile = new File(baseSavePath);
        if (!baseSavePathFile.exists()) {
            baseSavePathFile.mkdirs();
        }
        Long timestamp = System.currentTimeMillis();
        String txtUrl = "";
        //加载双发地1数据
        List<String> placeOneList = salaryRepository.getPlaceOneGroup(departId, 6);//审批完成的数据
        //读取excel模板
        File salaryPaymentExcel = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/salary-payment.xlsx");
        File salaryBankExcel = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/salary-bank.xlsx");
        if (!salaryPaymentExcel.exists() || !salaryBankExcel.exists()) {
            return null;
        }
        InputStream salaryPaymentIs = null;
        FileOutputStream salaryPaymentFos = null;
        Workbook salaryPaymentWb = null;
        InputStream salaryBankIs = null;
        FileOutputStream salaryBankFos = null;
        Workbook salaryBankWb = null;
        try {
            if (!placeOneList.isEmpty()) {
                for (int i = 0; i < placeOneList.size(); i++) {
                    //工资
                    salaryPaymentIs = new FileInputStream(salaryPaymentExcel);
                    salaryPaymentWb = new XSSFWorkbook(salaryPaymentIs);
                    Sheet salaryPaymentSheet = salaryPaymentWb.getSheetAt(0);
                    DataFormat salaryPaymentDataFormat = salaryPaymentWb.createDataFormat();
                    CellStyle salaryPaymentDefaultStyle = getCellStyle(salaryPaymentWb);
                    CellStyle salaryPaymentMoneyCellStyle = getCellStyle(salaryPaymentWb);
                    salaryPaymentMoneyCellStyle.setDataFormat(salaryPaymentDataFormat.getFormat("#,##null"));
                    //银行
                    salaryBankIs = new FileInputStream(salaryBankExcel);
                    salaryBankWb = new XSSFWorkbook(salaryBankIs);
                    Sheet salaryBankSheet = salaryBankWb.getSheetAt(0);
                    DataFormat salaryBankDataFormat = salaryBankWb.createDataFormat();
                    CellStyle salaryBankDefaultStyle = getCellStyle(salaryBankWb);
                    CellStyle salaryBankMoneyCellStyle = getCellStyle(salaryBankWb);
                    salaryBankMoneyCellStyle.setDataFormat(salaryBankDataFormat.getFormat("#,##null"));
                    //加载指定双发地员工薪酬数据
                    List<SalaryEntity> salaryList = salaryRepository.findByPlaceOne(departId, placeOneList.get(i), 6);//审批完成的数据
                    for (int j = 0; j < salaryList.size(); j++) {
                        SalaryEntity salaryEntity = salaryList.get(j);
                        //银行部分
                        Row salaryBankRow = salaryBankSheet.getRow(j + 2);
                        if (null == salaryBankRow) {
                            salaryBankRow = salaryBankSheet.createRow(j + 2);
                        }
                        //设置单元格样式
                        for (int k = 1; k < 5; k++) {
                            if (null == salaryBankRow.getCell(k)) {
                                salaryBankRow.createCell(k).setCellStyle(salaryBankDefaultStyle);
                            } else {
                                salaryBankRow.getCell(k).setCellStyle(salaryBankDefaultStyle);
                            }
                        }
                        //员工信息
                        if (null != salaryEntity.getEmployeeInfo()) {
                            salaryBankRow.getCell(1).setCellValue(objConvertString(salaryEntity.getEmployeeInfo().getCode()));
                            salaryBankRow.getCell(2).setCellValue(objConvertString(salaryEntity.getEmployeeInfo().getCmbAccount()));
                            salaryBankRow.getCell(3).setCellValue(objConvertString(salaryEntity.getEmployeeInfo().getName()));
                        }
                        salaryBankRow.getCell(4).setCellStyle(salaryBankMoneyCellStyle);
                        salaryBankRow.getCell(4).setCellValue(objConvertString(salaryEntity.getTransferSalaryOne()));
                        //工资部分
                        Row salaryPaymentRow = salaryPaymentSheet.getRow(j + 3);
                        if (null == salaryPaymentRow) {
                            salaryPaymentRow = salaryPaymentSheet.createRow(j + 3);
                        }
                        //设置单元格样式
                        for (int k = 1; k < 12; k++) {
                            if (null == salaryPaymentRow.getCell(k)) {
                                salaryPaymentRow.createCell(k).setCellStyle(salaryPaymentDefaultStyle);
                            } else {
                                salaryPaymentRow.getCell(k).setCellStyle(salaryPaymentDefaultStyle);
                            }
                        }
                        //员工信息
                        if (null != salaryEntity.getEmployeeInfo()) {
                            salaryPaymentRow.getCell(1).setCellValue(objConvertString(salaryEntity.getEmployeeInfo().getCode()));
                            salaryPaymentRow.getCell(2).setCellValue(objConvertString(salaryEntity.getEmployeeInfo().getName()));
                            //部门信息
                            if (null != salaryEntity.getEmployeeInfo().getDepartment()) {
                            	TSDepart department = systemService.getEntity(TSDepart.class, salaryEntity.getEmployeeInfo().getDepartment());
                                salaryPaymentRow.getCell(3).setCellValue(objConvertString(department.getDepartname()));
                            }
                        }
                        salaryPaymentRow.getCell(4).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(5).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(6).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(7).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(8).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(9).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(10).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(11).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(4).setCellValue(objConvertString(salaryEntity.getSalaryOne()));
                        salaryPaymentRow.getCell(5).setCellValue(objConvertString(salaryEntity.getSixPersonalBurdenOne()));
                        salaryPaymentRow.getCell(6).setCellValue(objConvertString(salaryEntity.getPerToneTaxOne()));
                        salaryPaymentRow.getCell(7).setCellValue(objConvertString(salaryEntity.getYearEndBonusOne()));
                        salaryPaymentRow.getCell(8).setCellValue(objConvertString(salaryEntity.getYearTaxPersonalOne()));
                        salaryPaymentRow.getCell(9).setCellValue(objConvertString(salaryEntity.getTransferSalaryOne()));
                        salaryPaymentRow.getCell(10).setCellValue(objConvertString(salaryEntity.getSixCompanyBurdenOne()));
//						salaryPaymentRow.getCell(11).setCellType(Cell.CELL_TYPE_FORMULA);
                        salaryPaymentRow.getCell(11).setCellFormula("E" + (j + 4) + "+H" + (j + 4) + "+K" + (j + 4));
                    }
                    String savePrefixPath = baseSavePath + "/" + timestamp + "_" + placeOneList.get(i);
                    String viewPrefixPath = ResourceUtil.getConfigByName("domain") + "/" +
                            ResourceUtil.getConfigByName("uploadpath") + "/excel/" +
                            formatDate(new Date(), "yyyyMMdd") + "/" +
                            timestamp + "_" + placeOneList.get(i);
                    //工资部分
                    salaryPaymentSheet.setForceFormulaRecalculation(true);
                    links.add(viewPrefixPath + "工资审批通过.xlsx");
                    salaryPaymentFos = new FileOutputStream(savePrefixPath + "工资审批通过.xlsx");
                    salaryPaymentWb.write(salaryPaymentFos);
                    salaryPaymentFos.flush();
                    salaryPaymentFos.close();
                    salaryPaymentIs.close();
                    //银行部分
                    salaryBankSheet.setForceFormulaRecalculation(true);
                    links.add(viewPrefixPath + "银行审批通过.xlsx");
                    salaryBankFos = new FileOutputStream(savePrefixPath + "银行审批通过.xlsx");
                    salaryBankWb.write(salaryBankFos);
                    salaryBankFos.flush();
                    salaryBankFos.close();
                    salaryBankIs.close();
                }
            }
            //超链接写入文本
            txtUrl = writeTxtLink(links, timestamp + ".txt");
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        } finally {
            try {
                if (null != salaryPaymentIs) {
                    salaryPaymentIs.close();
                }
                if (null != salaryPaymentFos) {
                    salaryPaymentFos.close();
                }
                if (null != salaryBankIs) {
                    salaryBankIs.close();
                }
                if (null != salaryBankFos) {
                    salaryBankFos.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        result.put("txtUrl", txtUrl);
        result.put("fileName", timestamp + ".txt");
        return result;
    }

    /**
     * @param declareStartDate
     * @param declareEndDate
     * @param departId
     * @return
     * @throws IOException
     */
    public Map<String, Object> exportDatasNew(String departId, Integer delFlg) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> links = new ArrayList<String>();
        String baseSavePath = ResourceUtil.getConfigByName("excelGeneratePath") + "/" + formatDate(new Date(), "yyyyMMdd");
        File baseSavePathFile = new File(baseSavePath);
        if (!baseSavePathFile.exists()) {
            baseSavePathFile.mkdirs();
        }
        Long timestamp = System.currentTimeMillis();
        String txtUrl = "";
        //加载双发地1数据
        List<String> placeOneList = salaryRepository.getPlaceOneGroupNew(departId);//数据
        //读取excel模板
        File salaryPaymentExcel = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/salary-payment.xlsx");
        File salaryBankExcel = new File(EmployeeDeclareService.class.getResource("/")
                .getPath() + "excel-template/salary-bank.xlsx");
        if (!salaryPaymentExcel.exists() || !salaryBankExcel.exists()) {
            return null;
        }
        InputStream salaryPaymentIs = null;
        FileOutputStream salaryPaymentFos = null;
        Workbook salaryPaymentWb = null;
        InputStream salaryBankIs = null;
        FileOutputStream salaryBankFos = null;
        Workbook salaryBankWb = null;
        try {
            if (!placeOneList.isEmpty()) {
                for (int i = 0; i < placeOneList.size(); i++) {
                    //工资
                    salaryPaymentIs = new FileInputStream(salaryPaymentExcel);
                    salaryPaymentWb = new XSSFWorkbook(salaryPaymentIs);
                    Sheet salaryPaymentSheet = salaryPaymentWb.getSheetAt(0);
                    DataFormat salaryPaymentDataFormat = salaryPaymentWb.createDataFormat();
                    CellStyle salaryPaymentDefaultStyle = getCellStyle(salaryPaymentWb);
                    CellStyle salaryPaymentMoneyCellStyle = getCellStyle(salaryPaymentWb);
                    salaryPaymentMoneyCellStyle.setDataFormat(salaryPaymentDataFormat.getFormat("#,##null"));
                    //银行
                    salaryBankIs = new FileInputStream(salaryBankExcel);
                    salaryBankWb = new XSSFWorkbook(salaryBankIs);
                    Sheet salaryBankSheet = salaryBankWb.getSheetAt(0);
                    DataFormat salaryBankDataFormat = salaryBankWb.createDataFormat();
                    CellStyle salaryBankDefaultStyle = getCellStyle(salaryBankWb);
                    CellStyle salaryBankMoneyCellStyle = getCellStyle(salaryBankWb);
                    salaryBankMoneyCellStyle.setDataFormat(salaryBankDataFormat.getFormat("#,##null"));
                    //加载指定双发地员工薪酬数据
                    List<SalaryEntity> salaryList = salaryRepository.findByPlaceOneNew(departId, placeOneList.get(i));//数据
                    for (int j = 0; j < salaryList.size(); j++) {
                        SalaryEntity salaryEntity = salaryList.get(j);
                        //银行部分
                        Row salaryBankRow = salaryBankSheet.getRow(j + 2);
                        if (null == salaryBankRow) {
                            salaryBankRow = salaryBankSheet.createRow(j + 2);
                        }
                        //设置单元格样式
                        for (int k = 1; k < 5; k++) {
                            if (null == salaryBankRow.getCell(k)) {
                                salaryBankRow.createCell(k).setCellStyle(salaryBankDefaultStyle);
                            } else {
                                salaryBankRow.getCell(k).setCellStyle(salaryBankDefaultStyle);
                            }
                        }
                        //员工信息
                        if (null != salaryEntity.getEmployeeInfo()) {
                            salaryBankRow.getCell(1).setCellValue(objConvertString(salaryEntity.getEmployeeInfo().getCode()));
                            salaryBankRow.getCell(2).setCellValue(objConvertString(salaryEntity.getEmployeeInfo().getCmbAccount()));
                            salaryBankRow.getCell(3).setCellValue(objConvertString(salaryEntity.getEmployeeInfo().getName()));
                        }
                        salaryBankRow.getCell(4).setCellStyle(salaryBankMoneyCellStyle);
                        salaryBankRow.getCell(4).setCellValue(objConvertString(salaryEntity.getTransferSalaryOne()));
                        //工资部分
                        Row salaryPaymentRow = salaryPaymentSheet.getRow(j + 3);
                        if (null == salaryPaymentRow) {
                            salaryPaymentRow = salaryPaymentSheet.createRow(j + 3);
                        }
                        //设置单元格样式
                        for (int k = 1; k < 12; k++) {
                            if (null == salaryPaymentRow.getCell(k)) {
                                salaryPaymentRow.createCell(k).setCellStyle(salaryPaymentDefaultStyle);
                            } else {
                                salaryPaymentRow.getCell(k).setCellStyle(salaryPaymentDefaultStyle);
                            }
                        }
                        //员工信息
                        if (null != salaryEntity.getEmployeeInfo()) {
                            salaryPaymentRow.getCell(1).setCellValue(objConvertString(salaryEntity.getEmployeeInfo().getCode()));
                            salaryPaymentRow.getCell(2).setCellValue(objConvertString(salaryEntity.getEmployeeInfo().getName()));
                            //部门信息
                            if (null != salaryEntity.getEmployeeInfo().getDepartment()) {
                            	TSDepart department = systemService.getEntity(TSDepart.class, salaryEntity.getEmployeeInfo().getDepartment());
                                salaryPaymentRow.getCell(3).setCellValue(objConvertString(department.getDepartname()));
                            }
                        }
                        salaryPaymentRow.getCell(4).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(5).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(6).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(7).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(8).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(9).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(10).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(11).setCellStyle(salaryPaymentMoneyCellStyle);
                        salaryPaymentRow.getCell(4).setCellValue(objConvertString(salaryEntity.getSalaryOne()));
                        salaryPaymentRow.getCell(5).setCellValue(objConvertString(salaryEntity.getSixPersonalBurdenOne()));
                        salaryPaymentRow.getCell(6).setCellValue(objConvertString(salaryEntity.getPerToneTaxOne()));
                        salaryPaymentRow.getCell(7).setCellValue(objConvertString(salaryEntity.getYearEndBonusOne()));
                        salaryPaymentRow.getCell(8).setCellValue(objConvertString(salaryEntity.getYearTaxPersonalOne()));
                        salaryPaymentRow.getCell(9).setCellValue(objConvertString(salaryEntity.getTransferSalaryOne()));
                        salaryPaymentRow.getCell(10).setCellValue(objConvertString(salaryEntity.getSixCompanyBurdenOne()));
//						salaryPaymentRow.getCell(11).setCellType(Cell.CELL_TYPE_FORMULA);
                        salaryPaymentRow.getCell(11).setCellFormula("E" + (j + 4) + "+H" + (j + 4) + "+K" + (j + 4));
                    }
                    String savePrefixPath = baseSavePath + "/" + timestamp + "_" + placeOneList.get(i);
                    String viewPrefixPath = ResourceUtil.getConfigByName("domain") + "/" +
                            ResourceUtil.getConfigByName("uploadpath") + "/excel/" +
                            formatDate(new Date(), "yyyyMMdd") + "/" +
                            timestamp + "_" + placeOneList.get(i);
                    //工资部分
                    salaryPaymentSheet.setForceFormulaRecalculation(true);
                    links.add(viewPrefixPath + "工资.xlsx");
                    salaryPaymentFos = new FileOutputStream(savePrefixPath + "工资.xlsx");
                    salaryPaymentWb.write(salaryPaymentFos);
                    salaryPaymentFos.flush();
                    salaryPaymentFos.close();
                    salaryPaymentIs.close();
                    //银行部分
                    salaryBankSheet.setForceFormulaRecalculation(true);
                    links.add(viewPrefixPath + "银行.xlsx");
                    salaryBankFos = new FileOutputStream(savePrefixPath + "银行.xlsx");
                    salaryBankWb.write(salaryBankFos);
                    salaryBankFos.flush();
                    salaryBankFos.close();
                    salaryBankIs.close();
                }
            }
            //超链接写入文本
            txtUrl = writeTxtLink(links, timestamp + ".txt");
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        } finally {
            try {
                if (null != salaryPaymentIs) {
                    salaryPaymentIs.close();
                }
                if (null != salaryPaymentFos) {
                    salaryPaymentFos.close();
                }
                if (null != salaryBankIs) {
                    salaryBankIs.close();
                }
                if (null != salaryBankFos) {
                    salaryBankFos.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        result.put("txtUrl", txtUrl);
        result.put("fileName", timestamp + ".txt");
        return result;
    }

    /**
     * 对象转字符串
     *
     * @param obj
     * @return
     */
    public String objConvertString(Object obj) {
        if (null != obj) {
            return obj.toString();
        }
        return "";
    }


    /**
     * 写入超链接到文本
     *
     * @return
     */
    public String writeTxtLink(List<String> links, String fileName) {
        String baseSavePath = ResourceUtil.getConfigByName("excelGeneratePath") + "/" + formatDate(new Date(), "yyyyMMdd");
        File baseSaveFile = new File(baseSavePath);
        if (!baseSaveFile.exists()) {
            baseSaveFile.mkdirs();
        }
        BufferedWriter writer = null;
        try {
            FileWriter fileWriter = new FileWriter(new File(baseSavePath + "/" + fileName));
            writer = new BufferedWriter(fileWriter);
            for (int i = 0; i < links.size(); i++) {
                writer.write(links.get(i));
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return baseSavePath + "/" + fileName;
    }

    /**
     * 获取单元格样式
     *
     * @param wb
     * @return
     */
    private CellStyle getCellStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        //添加上边边框
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        //添加右边边框
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        //添加下边边框
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        //添加左边边框
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        //居中
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //字体
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 10);//字体大小
        style.setFont(font);
        return style;
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public String formatDate(Date date, String fmt) {
        String dateStr = "";
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }


    /******************************************  修改区域结束    ******************************************/


    public void updateStatus(TSUser user, List<SalaryEntity> salaryEntity, Integer state) {
        //批量更新状态
        for (int i = 0; i < salaryEntity.size(); i++) {
            SalaryEntity salary = salaryEntity.get(i);
            salary.setCreatedDate(new Date());
            salary.setState(state);
            salaryRepository.update(salary);
        }
    }

    /**
     * 更新申报状态
     *
     * @param user
     * @param declareStatus
     */
    public void updateDeclareStatus(TSUser user, List<SalaryEntity> saleryList, Integer state) {
        //批量更新状态
        for (int i = 0; i < saleryList.size(); i++) {
            SalaryEntity salery = saleryList.get(i);
            salery.setState(state);
            salaryRepository.update(salery);
        }
    }

    /**
     * 提取员工名字信息
     *
     * @param salaryEntityList
     * @return
     */
    public String extractEmployeeNameInfo(List<SalaryEntity> salaryEntityList) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < salaryEntityList.size(); i++) {
            if (i == 0) {
                sb.append(salaryEntityList.get(i).getEmployeeInfo().getName());
            } else {
                sb.append(",").append(salaryEntityList.get(i).getEmployeeInfo().getName());
            }
        }
        return sb.toString();
    }

    @Test
    public void sendTest(){
    	salaryService.mailSend("3272124733@qq.com","主题测试","内容测试");
    }
    /**
     * 邮箱发送
     *
     * @param toEmail
     * @param subject
     * @param msg
     */
    @Transactional(readOnly = false)
    public void mailSend(String toEmail, String subject, String msg) {
        String hql = "from EmailConfigEntity t where t.sendCount < t.maxCount";
        //加载邮箱配置信息
        List<EmailConfigEntity> emailConfigList = commonService.findHql(hql, new Object[]{});
        if (!emailConfigList.isEmpty()) {
            EmailConfigEntity emailConfig = emailConfigList.get(1);
            //创建邮件发送服务器
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(emailConfig.getHost());
            mailSender.setPort(emailConfig.getPort());
            mailSender.setUsername(emailConfig.getAccount());
            mailSender.setPassword(emailConfig.getPassword());
            //创建邮件内容
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailConfig.getMailFrom());
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(msg);
            //发送邮件
            mailSender.send(message);
            //更新邮箱发送次数+1
            emailConfig.setSendCount(emailConfig.getSendCount() + 1);
            emailConfigRepo.saveOrUpdate(emailConfig);
        } else {
            //log.info("未找到邮箱服务器配置信息");
        }
    }

    public Map<String, Object> getTableHeaderCalc(String departId, Integer... status) {
        Map<String, Object> result = new HashMap<String, Object>();

        return result;
    }


    /**
     * 获取申报确认表数据
     * return
     */
    public Map<String, Object> findAllSalaryByDepartId() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errCode", 0);
        //将当前用户部门下的所有本月待申报的数据加载出来
        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        if (null != user) {
            List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
            String departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null;//只支持单部门
            if (null == departId) {
                result.put("errMsg", "当前登录用户：" + user.getUserName() + "所在部门为空");
                result.put("errCode", -1);
            } else {
                //获取申报确认表数据
                List<EmployeeDeclareEntity> salaryEntityList = employeeDeclareRepository.findAllSalaryByDepartId(departId);
                if (!salaryEntityList.isEmpty()) {

                } else {
                    result.put("errMsg", "没有需要申报的数据");
                    result.put("errCode", -1);
                }
            }
        } else {
            String errMsg = "当前用户未登录，无法进行申报操作";
            //log.info(errMsg);
            result.put("errMsg", errMsg);
            result.put("errCode", -1);
        }
        return result;
    }


/*	new add*/

    /**
     * 外派生成部门上一个月数据
     *
     * @return
     */
    public void add(String dateString) throws Exception {
        logger.info("add==================== start:" + dateString);

        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        // 非超级管理员添加部门条件
        String departId = null;
        // 超级管理员、顶层管理员可以看到所有数据
        List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null; //只支持单部门

        List<EmployeeInfoEntity> employeeList = employeeInfoRepo.findByDepartIds(new String[]{departId});
        SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date te = sdft.parse(dateString);

        String str1 = sdf.format(te);
        Date date = sdf.parse(str1);

        // 获取上个月
       /* SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date date = null;
        try {
            date = sdf.parse(CalendarUtil.getLastMonthDate("yyyyMM"));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        if (employeeList != null && !employeeList.isEmpty()) {
            for (int i = 0; i < employeeList.size(); i++) {
                EmployeeInfoEntity entity = employeeList.get(i);
                SixGoldEntity sixGoldEntity = sixGoldService.findByEmployeeCode(entity.getCode(), date);
                if (sixGoldEntity != null) {
                    // 判断是否生成过上一个数据
                    if (salaryRepository.findByCodeAndDate(date, entity.getCode()) == null) {
                        // 添加一条数据
                        SalaryEntity salaryEntity = new SalaryEntity();
                        // 薪酬年月
                        salaryEntity.setSalaryDate(date);
                        // 员工ID
                        salaryEntity.setEmployeeInfo(entity);
                        // B折扣率 注意：这里是【折扣率】，不是【试用期折扣率】
                        salaryEntity.setBtDiscountRate(100);
                        // C1电脑补贴 单位
                        salaryEntity.setC1ComputerSubsidy(null);
                        // C2加班费 单位
                        salaryEntity.setC2OvertimePay(null);
                        salaryEntity.setC1OtherSubsidy(null);
                        salaryEntity.setC1Note(salaryEntity.getC1Note());
                        salaryEntity.setC2OtherSubsidy(null);
                        salaryEntity.setC2Note(salaryEntity.getC2Note());
                        // C3其他补贴
                        salaryEntity.setC3OtherSubsidy(null);
                        salaryEntity.setC3Note(salaryEntity.getC3Note());
                        // 特别扣减
                        salaryEntity.setSpecialDeduction(null);
                        // C补贴 -  计算公式 C=C1+C2+C3-特别扣减
                        salaryEntity.setCnumSubsidy(null);
                        // D年终奖
                        salaryEntity.setDnumYearEndBonus(null);
                        // 年终奖1
                        salaryEntity.setYearEndBonusOne(null);
                        // 年个税1
                        salaryEntity.setYearTaxPersonalOne(null);

                        // 双发地点1 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
                        salaryEntity.setPlaceOne(entity.getSixGoldCity());
                        // 工资1
                        salaryEntity.setSalaryOne(entity.getA1Payment());

                        // 工资2
                        salaryEntity.setSalaryTwo(entity.getA1Payment());
                        // 年终奖2
                        salaryEntity.setYearEndBonusTwo(null);
                        // 年个税2
                        salaryEntity.setYearTaxPersonalTwo(null);
                        // 双发地点2 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
//                        salaryEntity.setPlaceTwo(entity.getPerformancePalce());

                        // 判断那个地方发六金
                        if (!StringUtil.isEmpty(entity.getSixGoldCity()) && entity.getSixGoldCity().equals(sixGoldEntity.getSixGoldPlace())) {
                            // 六金（个人负担）1
                            salaryEntity.setSixPersonalBurdenOne(sixGoldEntity.getPersonalSum());
                            // 个调税1
                            salaryEntity.setPerToneTaxOne(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryOne() - salaryEntity.getSixPersonalBurdenOne()));
                            // 六金（公司负担）1
                            salaryEntity.setSixCompanyBurdenOne(sixGoldEntity.getCompanySum());

                            // 六金（个人负担）2
                            salaryEntity.setSixPersonalBurdenTwo(null);
                            // 个调税2
                            salaryEntity.setPerToneTaxTwo(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryTwo()));
                            // 六金（公司负担）2
                            salaryEntity.setSixCompanyBurdenTwo(null);
                        } else {
                            // 六金（个人负担）1
                            salaryEntity.setSixPersonalBurdenOne(null);
                            // 个调税1
                            salaryEntity.setPerToneTaxOne(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryOne()));
                            // 六金（公司负担）1
                            salaryEntity.setSixCompanyBurdenOne(null);

                            // 六金（个人负担）2
                            salaryEntity.setSixPersonalBurdenTwo(sixGoldEntity.getPersonalSum());
                            // 个调税2
                            salaryEntity.setPerToneTaxTwo(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryTwo() - salaryEntity.getSixPersonalBurdenTwo()));
                            // 六金（公司负担）2
                            salaryEntity.setSixCompanyBurdenTwo(sixGoldEntity.getCompanySum());
                        }

                        // 打卡金额1 【薪酬表】的【打卡金额1】=【工资1】-【六金（个人负担）1】-【个调税1】+【年终奖1」-【年个税1】
                        salaryEntity.setTransferSalaryOne(salaryEntity.getSalaryOne() - salaryEntity.getSixPersonalBurdenOne() - salaryEntity.getPerToneTaxOne() + salaryEntity.getYearEndBonusOne() - salaryEntity.getYearTaxPersonalOne());

                        // 公司成本1 【薪酬表】的【公司成本1】=【工资1】+【年终奖1】+【六金（公司负担）1】
                        salaryEntity.setCompanyCostOne(salaryEntity.getSalaryOne() + salaryEntity.getYearEndBonusOne() + salaryEntity.getSixCompanyBurdenOne());

                        // 打卡金额2
                        salaryEntity.setTransferSalaryTwo(salaryEntity.getSalaryTwo() - salaryEntity.getSixPersonalBurdenTwo() - salaryEntity.getPerToneTaxTwo() + salaryEntity.getYearEndBonusTwo() - salaryEntity.getYearTaxPersonalTwo());

                        // 公司成本2
                        salaryEntity.setCompanyCostTwo(salaryEntity.getSalaryTwo() + salaryEntity.getYearEndBonusTwo() + salaryEntity.getSixCompanyBurdenTwo());

                        // 状态0:录入中、1:申报中、2:申报完成
                        salaryEntity.setState(0);

                        salaryEntity.setCreatedBy(user.getId());
                        Date currentDate = new Date();
                        salaryEntity.setCreatedDate(currentDate);
                        salaryEntity.setLastModifiedDate(currentDate);
                        salaryEntity.setLastModifiedBy(user.getId());
                        salaryEntity.setDeleteFlg(0);

                        salaryRepository.save(salaryEntity);

                        logger.info("SalaryEntity : " + salaryEntity);
                    }
                }
            }
        }

        logger.info("add==================== end");
    }


    /**
     * 生成一个员工的薪酬支付
     *
     * @param
     * @return
     */
    public SalaryEntity addOne(EmployeeInfoEntity entity, Date date, TSUser user) {
        // 添加一条数据
        SalaryEntity salaryEntity = new SalaryEntity();
        // 从 c_six_gold 中获取 员工的 六金信息
        SixGoldEntity sixGoldEntity = sixGoldService.findByEmployeeCode(entity.getCode(), date);


        SalaryEntity oldSalaryEntity = salaryRepository.findByCodeAndDate(date, entity.getCode());



        // 如果生成过 进行更新
        logger.info("SalaryEntity 新增 : " + salaryEntity);

        salaryEntity.setId(Utils.generateId());
        // 薪酬年月
        salaryEntity.setSalaryDate(date);
        // 员工ID
        salaryEntity.setEmployeeInfo(entity);
        // B折扣率 注意：这里是【折扣率】，不是【试用期折扣率】
        salaryEntity.setBtDiscountRate(null);
        // C1电脑补贴 单位
        salaryEntity.setC1ComputerSubsidy(null);
        // C2加班费 单位
        salaryEntity.setC2OvertimePay(null);
        // C3其他补贴
        salaryEntity.setC1OtherSubsidy(null);
        salaryEntity.setC1Note(null);
        salaryEntity.setC2OtherSubsidy(null);
        salaryEntity.setC2Note(null);
        salaryEntity.setC3OtherSubsidy(null);
        salaryEntity.setC3Note(null);
        // 特别扣减
        salaryEntity.setSpecialDeduction(null);
        // C补贴 -  计算公式 C=C1+C2+C3-特别扣减
        salaryEntity.setCnumSubsidy(null);
        // D年终奖
        salaryEntity.setDnumYearEndBonus(null);
        // 年终奖1
        salaryEntity.setYearEndBonusOne(null);
        // 年个税1
        salaryEntity.setYearTaxPersonalOne(null);
        // 双发地点1 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
        salaryEntity.setPlaceOne(entity.getSixGoldCity());
        // 工资1
        salaryEntity.setSalaryOne(entity.getA1Payment());
        // 工资2
//        salaryEntity.setSalaryTwo(entity.getPerformancePayment());
        // 年终奖2
        salaryEntity.setYearEndBonusTwo(null);
        // 年个税2
        salaryEntity.setYearTaxPersonalTwo(null);
        // 双发地点2 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
//        salaryEntity.setPlaceTwo(entity.getPerformancePalce());
        // 判断那个地方发六金
        if (!StringUtil.isEmpty(entity.getSixGoldCity()) && entity.getSixGoldCity().equals(sixGoldEntity.getSixGoldPlace())) {
            // 六金（个人负担）1
            salaryEntity.setSixPersonalBurdenOne(sixGoldEntity.getPersonalSum());
            // 个调税1
//            salaryEntity.setPerToneTaxOne(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryOne() - salaryEntity.getSixPersonalBurdenOne()));
            // 六金（公司负担）1
            salaryEntity.setSixCompanyBurdenOne(sixGoldEntity.getCompanySum());
            // 六金（个人负担）2
            salaryEntity.setSixPersonalBurdenTwo(null);
            // 个调税2
            salaryEntity.setPerToneTaxTwo(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryTwo()));
            // 六金（公司负担）2
            salaryEntity.setSixCompanyBurdenTwo(null);
        } else {
            // 六金（个人负担）1
            salaryEntity.setSixPersonalBurdenOne(null);
            // 个调税1
            salaryEntity.setPerToneTaxOne(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryOne()));
            // 六金（公司负担）1
            salaryEntity.setSixCompanyBurdenOne(null);
            // 六金（个人负担）2
            salaryEntity.setSixPersonalBurdenTwo(sixGoldEntity.getPersonalSum());
            // 个调税2
//            salaryEntity.setPerToneTaxTwo(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryTwo() - salaryEntity.getSixPersonalBurdenTwo()));
            salaryEntity.setPerToneTaxTwo(null);

            // 六金（公司负担）2
            salaryEntity.setSixCompanyBurdenTwo(sixGoldEntity.getCompanySum());
        }
        // 打卡金额1 【薪酬表】的【打卡金额1】=【工资1】-【六金（个人负担）1】-【个调税1】+【年终奖1」-【年个税1】
//        salaryEntity.setTransferSalaryOne(salaryEntity.getSalaryOne() - salaryEntity.getSixPersonalBurdenOne() - salaryEntity.getPerToneTaxOne() + salaryEntity.getYearEndBonusOne() - salaryEntity.getYearTaxPersonalOne());
        salaryEntity.setTransferSalaryOne(null);
        // 公司成本1 【薪酬表】的【公司成本1】=【工资1】+【年终奖1】+【六金（公司负担）1】
//        salaryEntity.setCompanyCostOne(salaryEntity.getSalaryOne() + salaryEntity.getYearEndBonusOne() + salaryEntity.getSixCompanyBurdenOne());
        salaryEntity.setCompanyCostOne(null);
        // 打卡金额2
//        salaryEntity.setTransferSalaryTwo(salaryEntity.getSalaryTwo() - salaryEntity.getSixPersonalBurdenTwo() - salaryEntity.getPerToneTaxTwo() + salaryEntity.getYearEndBonusTwo() - salaryEntity.getYearTaxPersonalTwo());
        salaryEntity.setTransferSalaryTwo(null);
        // 公司成本2
//        salaryEntity.setCompanyCostTwo(salaryEntity.getSalaryTwo() + salaryEntity.getYearEndBonusTwo() + salaryEntity.getSixCompanyBurdenTwo());
        salaryEntity.setCompanyCostTwo(null);
        // 状态0:录入中、1:申报中、2:申报完成
        salaryEntity.setState(0);
        salaryEntity.setCreatedBy(user.getId());
        Date currentDate = new Date();
        salaryEntity.setCreatedDate(currentDate);
        salaryEntity.setLastModifiedDate(currentDate);
        salaryEntity.setLastModifiedBy(user.getId());
        salaryEntity.setDeleteFlg(0);
        if (oldSalaryEntity != null && oldSalaryEntity.getId() != null) {
            salaryRepository.update(salaryEntity);
        }else {
            salaryRepository.save(salaryEntity);
        }
        return salaryEntity;
    }

    /**
     * 生成部门上一个月数据
     *
     * @return
     */
    public void addAll(String dateString) throws Exception {
        logger.info("add==================== start");

        TSUser user = (TSUser) ContextHolderUtils.getSession().getAttribute(ResourceUtil.LOCAL_CLINET_USER);
        // 非超级管理员添加部门条件
        String departId = null;
        String departname = null;
        // 超级管理员、顶层管理员可以看到所有数据
        List<TSUserOrg> currTSUserOrgList = commonService.findHql("from TSUserOrg t where t.tsUser.id=?", new Object[]{user.getId()});
        departId = currTSUserOrgList.size() > 0 ? currTSUserOrgList.get(0).getTsDepart().getId() : null; //只支持单部门
        logger.info("---" + departId);
        List<EmployeeInfoEntity> employeeList = employeeInfoRepo.findByDepartIdsAll(new String[]{departId});

        SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Date te = sdft.parse(dateString);

        String str1 = sdf.format(te);
        Date date = sdf.parse(str1);

        logger.info(date);

        if (employeeList != null && !employeeList.isEmpty()) {
            for (int i = 0; i < employeeList.size(); i++) {
                EmployeeInfoEntity entity = employeeList.get(i);
                SixGoldEntity sixGoldEntity = sixGoldService.findByEmployeeCode(entity.getCode(), date);
                if (sixGoldEntity != null) {
                    // 判断是否生成过上一个数据
                    if (salaryRepository.findByCodeAndDate(date, entity.getCode()) == null) {
                        // 添加一条数据
                        SalaryEntity salaryEntity = new SalaryEntity();
                        // 薪酬年月
                        salaryEntity.setSalaryDate(date);
                        // 员工ID
                        salaryEntity.setEmployeeInfo(entity);
                        // B折扣率 注意：这里是【折扣率】，不是【试用期折扣率】
                        salaryEntity.setBtDiscountRate(100);
                        // C1电脑补贴 单位
                        salaryEntity.setC1ComputerSubsidy(null);
                        // C2加班费 单位
                        salaryEntity.setC2OvertimePay(null);
                        salaryEntity.setC1OtherSubsidy(null);
                        salaryEntity.setC1Note(salaryEntity.getC1Note());
                        salaryEntity.setC2OtherSubsidy(null);
                        salaryEntity.setC2Note(salaryEntity.getC2Note());
                        // C3其他补贴
                        salaryEntity.setC3OtherSubsidy(null);
                        salaryEntity.setC3Note(salaryEntity.getC3Note());
                        // 特别扣减
                        salaryEntity.setSpecialDeduction(null);
                        // C补贴 -  计算公式 C=C1+C2+C3-特别扣减
                        salaryEntity.setCnumSubsidy(null);
                        // D年终奖
                        salaryEntity.setDnumYearEndBonus(null);
                        // 年终奖1
                        salaryEntity.setYearEndBonusOne(null);
                        // 年个税1
                        salaryEntity.setYearTaxPersonalOne(null);
                        // 双发地点1 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
                        salaryEntity.setPlaceOne(entity.getSixGoldCity());
                        // 工资1
                        salaryEntity.setSalaryOne(entity.getA1Payment());
                        // 工资2
//                        salaryEntity.setSalaryTwo(entity.getPerformancePayment());
                        // 年终奖2
                        salaryEntity.setYearEndBonusTwo(null);
                        // 年个税2
                        salaryEntity.setYearTaxPersonalTwo(null);
                        // 双发地点2 - 北京、上海、江苏、昆山、江苏、广州、深圳、智蓝
//                        salaryEntity.setPlaceTwo(entity.getPerformancePalce());
                        // 判断那个地方发六金
                        if (!StringUtil.isEmpty(entity.getSixGoldCity()) && entity.getSixGoldCity().equals(sixGoldEntity.getSixGoldPlace())) {
                            // 六金（个人负担）1
                            salaryEntity.setSixPersonalBurdenOne(sixGoldEntity.getPersonalSum());
                            // 个调税1
                            salaryEntity.setPerToneTaxOne(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryOne() - salaryEntity.getSixPersonalBurdenOne()));
                            // 六金（公司负担）1
                            salaryEntity.setSixCompanyBurdenOne(sixGoldEntity.getCompanySum());
                            // 六金（个人负担）2
                            salaryEntity.setSixPersonalBurdenTwo(null);
                            // 个调税2
                            salaryEntity.setPerToneTaxTwo(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryTwo()));
                            // 六金（公司负担）2
                            salaryEntity.setSixCompanyBurdenTwo(null);
                        } else {
                            // 六金（个人负担）1
                            salaryEntity.setSixPersonalBurdenOne(null);
                            // 个调税1
                            salaryEntity.setPerToneTaxOne(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryOne()));
                            // 六金（公司负担）1
                            salaryEntity.setSixCompanyBurdenOne(null);
                            // 六金（个人负担）2
                            salaryEntity.setSixPersonalBurdenTwo(sixGoldEntity.getPersonalSum());
                            // 个调税2
                            salaryEntity.setPerToneTaxTwo(Utils.calculateIndividualIncomeTax(salaryEntity.getSalaryTwo() - salaryEntity.getSixPersonalBurdenTwo()));
                            // 六金（公司负担）2
                            salaryEntity.setSixCompanyBurdenTwo(sixGoldEntity.getCompanySum());
                        }
                        // 打卡金额1 【薪酬表】的【打卡金额1】=【工资1】-【六金（个人负担）1】-【个调税1】+【年终奖1」-【年个税1】
                        salaryEntity.setTransferSalaryOne(salaryEntity.getSalaryOne() - salaryEntity.getSixPersonalBurdenOne() - salaryEntity.getPerToneTaxOne() + salaryEntity.getYearEndBonusOne() - salaryEntity.getYearTaxPersonalOne());
                        // 公司成本1 【薪酬表】的【公司成本1】=【工资1】+【年终奖1】+【六金（公司负担）1】
                        salaryEntity.setCompanyCostOne(salaryEntity.getSalaryOne() + salaryEntity.getYearEndBonusOne() + salaryEntity.getSixCompanyBurdenOne());
                        // 打卡金额2
                        salaryEntity.setTransferSalaryTwo(salaryEntity.getSalaryTwo() - salaryEntity.getSixPersonalBurdenTwo() - salaryEntity.getPerToneTaxTwo() + salaryEntity.getYearEndBonusTwo() - salaryEntity.getYearTaxPersonalTwo());
                        // 公司成本2
                        salaryEntity.setCompanyCostTwo(salaryEntity.getSalaryTwo() + salaryEntity.getYearEndBonusTwo() + salaryEntity.getSixCompanyBurdenTwo());
                        // 状态0:录入中、1:申报中、2:申报完成
                        salaryEntity.setState(1);
                        salaryEntity.setCreatedBy(user.getId());
                        Date currentDate = new Date();
                        salaryEntity.setCreatedDate(currentDate);
                        salaryEntity.setLastModifiedDate(currentDate);
                        salaryEntity.setLastModifiedBy(user.getId());
                        salaryEntity.setDeleteFlg(0);
                        salaryRepository.save(salaryEntity);
                        logger.info("SalaryEntity : " + salaryEntity);
                    }
                }
            }
        }
    }
}