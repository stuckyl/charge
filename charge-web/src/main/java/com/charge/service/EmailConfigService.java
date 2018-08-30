package com.charge.service;

import static org.hamcrest.Matchers.stringContainsInOrder;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.constant.Globals;
import org.springframework.core.task.TaskExecutor;
import org.jeecgframework.p3.core.utils.common.StringUtils;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.charge.entity.EmailConfigEntity;
import com.charge.repository.CommonRepository;
import com.charge.repository.EmailConfigRepository;

/**
* @Title: EmailConfigService
* @Description: 邮箱配置业务层
* @author wenst
* @date 2018年4月20日
* @version v1.0
*/
@Service
@Transactional
public class EmailConfigService{
	private static final Logger log = Logger.getLogger(EmailConfigService.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private DictCategoryService dictCategoryService;
	@Autowired
	private CommonRepository commonRepository;
	@Autowired
	private TaskExecutor taskExecutor;


	/**
	 * 邮件通知指定部门和角色下的账号
	 * @param departId 部门ID
	 * @param dictCategory 字典分类
	 * @param typename 字典类型名称
	 * @param tplContent 邮件内容
	 */
	public void departEmailNotice(String departId,String dictCategory,String typename,Map<String,String> tplContent){
		Map<String,String> dictMap = dictCategoryService.getRoleNameCode(dictCategory);
		String hql = "select tsu from TSUser tsu,TSUserOrg tsuo,TSRoleUser tsru where tsu.id = tsuo.tsUser.id and tsu.id = tsru.TSUser.id and tsuo.tsDepart.id=? and tsru.TSRole.roleCode=? ";
		List<TSUser> userList = systemService.findHql(hql, new Object[]{departId,dictMap.get(typename)});//获取指定字典类型账号
		for(int i = 0;i<userList.size();i++){
			TSUser tSUser = userList.get(i);
			//发送邮件通知
			if(StringUtils.isNotBlank(tSUser.getEmail())){
				mailSend(tSUser.getEmail(), tplContent);
			}
		}
	}


	/**
	 * 指定通知字典表分类下的账号
	 * @param dictCategory 字典分类
	 * @param typename 字典类型名称
	 * @param tplContent 邮件内容
	 */
	public void dictEmailNotice(String dictCategory,Map<String,String> tplContent){
		List<String> dictList = dictCategoryService.getRoleCode(dictCategory);
		for(int i = 0;i<dictList.size();i++){
			TSUser tSUser = commonService.findUniqueByProperty(TSUser.class, "userName", dictList.get(i));
			if(null!=tSUser&&StringUtils.isNotBlank(tSUser.getEmail())){
				//发送邮件通知
				mailSend(tSUser.getEmail(), tplContent);
			}
		}
	}

	/**
	 * 邮箱发送
	 * @param toEmail
	 * @param subject
	 * @param msg
	 */
	@Transactional(readOnly=false)
	public void mailSend(final String toEmail,final Map<String,String> tplContent){taskExecutor.execute(new Runnable(){
		String host =commonRepository.getSystemEmailHost();
		String systemEmailAddress =commonRepository.getSystemEmailAccount();
		String systemEmailAccount =systemEmailAddress;
		String systemEmailPass =commonRepository.getSystemEmailPass();
        public void run(){
       try {
    	   MailUtil.sendEmail(host,toEmail, tplContent.get("subject"), tplContent.get("content"),systemEmailAddress,systemEmailAccount,systemEmailPass);
            } catch (Exception e) {
    			log.info("邮件发送错误");
    			e.printStackTrace();
           }
        }
     });
   }

/*	@Transactional(readOnly=false)
	public boolean mailSend(String toEmail,Map<String,String> tplContent) {
		boolean resualt=true;
		try {
			MailUtil.sendEmail(commonRepository.getSystemEmailHost(),toEmail, tplContent.get("subject"), tplContent.get("content"),commonRepository.getSystemEmailAddress(),commonRepository.getSystemEmailAccount(),commonRepository.getSystemEmailPass());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			resualt=false;
			log.info("邮件发送错误");
			e.printStackTrace();
		}
		return resualt;
	}*/
	/**
	 * 邮箱发送,发送至默认配置地址
	 * @param toEmail
	 * @param subject
	 * @param msg
	 */
	@Transactional(readOnly=false)
	public void mailSend(final Map<String,String> tplContent){
		taskExecutor.execute(new Runnable(){
		String host =commonRepository.getSystemEmailHost();
		String systemEmailAddress =commonRepository.getSystemEmailAccount();
		String systemEmailAccount =systemEmailAddress;
		String systemEmailPass =commonRepository.getSystemEmailPass();
		String toEmail = commonRepository.getSystemConstant("c_email_group");
		public void run(){
       try {
    	   MailUtil.sendEmail(host,toEmail, tplContent.get("subject"), tplContent.get("content"),systemEmailAddress,systemEmailAccount,systemEmailPass);
            } catch (Exception e) {
    			log.info("邮件发送错误");
    			e.printStackTrace();
           }
        }
     });
	}


	/**
	 * 邮箱发送,发送至默认配置地址,抄送至人
	 * @param toEmail
	 * @param subject
	 * @param msg
	 */
	@Transactional(readOnly=false)
	public void mailSendCc(final String toEmails,final Map<String,String> tplContent){
		taskExecutor.execute(new Runnable(){
		String host =commonRepository.getSystemEmailHost();
		String systemEmailAddress =commonRepository.getSystemEmailAccount();
		String systemEmailAccount =systemEmailAddress;
		String systemEmailPass =commonRepository.getSystemEmailPass();
		String toEmail = commonRepository.getSystemConstant("c_email_group");
		public void run(){
       try {
    	   MailUtil.sendEmail(host,toEmail,toEmails, tplContent.get("subject"), tplContent.get("content"),systemEmailAddress,systemEmailAccount,systemEmailPass);
            } catch (Exception e) {
    			log.info("邮件发送错误");
    			e.printStackTrace();
           }
        }
     });
	}
	/*public boolean mailSend(Map<String,String> tplContent) {
		boolean resualt=true;
		String toEmail = commonRepository.getSystemConstant("c_email_group");
		try {
			MailUtil.sendEmail(commonRepository.getSystemEmailHost(),toEmail, tplContent.get("subject"), tplContent.get("content"),commonRepository.getSystemEmailAddress(),commonRepository.getSystemEmailAccount(),commonRepository.getSystemEmailPass());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			resualt=false;
			log.info("邮件发送错误");
			e.printStackTrace();
		}
		return resualt;
	}*/
	/**
	 * 邮箱发送,实现多人发送不同的邮件
	 * @param toEmail
	 * @param subject
	 * @param msg
	 */
	@Transactional(readOnly=false)
	public void mailSendAll(final List<String> toEmails,final Map<String,Map<String, String>> tplContent){taskExecutor.execute(new Runnable(){
		String host =commonRepository.getSystemEmailHost();
		String systemEmailAddress =commonRepository.getSystemEmailAccount();
		String systemEmailAccount =systemEmailAddress;
		String systemEmailPass =commonRepository.getSystemEmailPass();
		public void run(){
			for(String toEmail:toEmails){
				Map<String, String> massage =tplContent.get(toEmail);
				try {
					MailUtil.sendEmail(host,toEmail,massage.get("subject"),massage.get("content") ,systemEmailAddress, systemEmailAccount,systemEmailPass);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					log.info("邮件发送错误");
					e.printStackTrace();
				}
			}
        }
     });
   }
	/**
	 * 邮箱发送,实现多人发送不同的邮件
	 * @param toEmail
	 * @param subject
	 * @param msg
	 */
	@Transactional(readOnly=false)
	public void mailSendAll2(final Map<String,Map<String, String>> tplContent){taskExecutor.execute(new Runnable(){
		String host =commonRepository.getSystemEmailHost();
		String systemEmailAddress =commonRepository.getSystemEmailAccount();
		String systemEmailAccount =systemEmailAddress;
		String systemEmailPass =commonRepository.getSystemEmailPass();
		public void run(){
			for(String toEmail:tplContent.keySet()){
				Map<String, String> massage =tplContent.get(toEmail);
				try {
					MailUtil.sendEmail(host,toEmail,massage.get("subject"),massage.get("content") ,systemEmailAddress, systemEmailAccount,systemEmailPass);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					log.info("邮件发送错误");
					e.printStackTrace();
				}
			}
        }
     });
   }
}