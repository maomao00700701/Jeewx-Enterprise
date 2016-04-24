package org.jeecgframework.web.cms.cmsdata.impl;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.web.cms.cmsdata.CmsDataCollectI;
import org.jeecgframework.web.cms.common.CmsConstant;
import org.jeecgframework.web.cms.common.CmsDataContent;
import org.jeecgframework.web.cms.entity.CmsAdEntity;
import org.jeecgframework.web.cms.entity.CmsMenuEntity;
import org.jeecgframework.web.cms.service.CmsAdServiceI;
import org.jeecgframework.web.cms.service.CmsMenuServiceI;



/**
 * CMS首页数据收集器
 * @author zhangdaihao
 *
 */
public class CmsIndexDataCollect implements CmsDataCollectI {

	@Override
	public void collect(Map<String, String> params) {
		//注意其他方法调用只能写在里面
		CmsAdServiceI adService = (CmsAdServiceI) ApplicationContextUtil.getContext().getBean("cmsAdService");
		CmsMenuServiceI cmsMenuService = (CmsMenuServiceI) ApplicationContextUtil.getContext().getBean("cmsMenuService");
		
		List<CmsAdEntity> adList = adService.list(params);
		CmsDataContent.put(CmsConstant.CMS_DATA_LIST_AD, adList);
		List<CmsMenuEntity> menuList = cmsMenuService.list(params);
		CmsDataContent.put(CmsConstant.CMS_DATA_LIST_MENU, menuList);
		String res = CmsConstant.CMS_ROOT_PATH + "/" + params.get(CmsConstant.CMS_STYLE_NAME);
		//资源路径
		CmsDataContent.put(CmsConstant.BASE, res);
	}

}
