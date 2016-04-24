package com.jeecg.qywx.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jeecg.qywx.account.dao.QywxAccountDao;
import com.jeecg.qywx.account.entity.QywxAccount;
import com.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeecg.qywx.api.core.common.AccessToken;
import com.jeecg.qywx.util.SystemUtil;
/**
 * 
 * 微信企业号
 *
 */
@Service
public class AccountService{
	@Autowired
	private QywxAccountDao qywxAccountDao;
	/**
	 * 获取微信企业号 AccessToken
	 * @return
	 */
	public AccessToken getAccessToken(){
		 QywxAccount qywxAccount = qywxAccountDao.get(SystemUtil.QYWX_ACCOUNT_ID);
		 AccessToken accessToken = JwAccessTokenAPI.getAccessToken(qywxAccount.getCorpid(),qywxAccount.getSecret());
		return accessToken;
	}
}
