package org.jeecgframework.web.cms.cmsdata.impl;

import java.util.Map;

import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.web.cms.cmsdata.CmsDataCollectI;
import org.jeecgframework.web.cms.common.CmsConstant;
import org.jeecgframework.web.cms.common.CmsDataContent;
import org.jeecgframework.web.cms.entity.CmsArticleEntity;
import org.jeecgframework.web.cms.service.CmsArticleServiceI;



public class CmsArticleDataCollect implements CmsDataCollectI {
	
	@Override
	public void collect(Map<String, String> params) {
		CmsArticleServiceI cmsArticleService = (CmsArticleServiceI) ApplicationContextUtil.getContext().getBean("cmsArticleService");
		
		String articleid = params.get("articleid") != null ? params.get("articleid").toString() : "-";
		CmsArticleEntity cmsArticleEntity = cmsArticleService.getCmsArticleEntity(articleid);
		if (cmsArticleEntity != null) {
			CmsDataContent.put(CmsConstant.CMS_DATA_MAP_ARTICLE, cmsArticleEntity);
			CmsDataContent.put(CmsConstant.CMS_DATA_STR_TITLE, cmsArticleEntity.getTitle());
		}else{
			CmsDataContent.put(CmsConstant.CMS_DATA_MAP_ARTICLE, new CmsArticleEntity());
			CmsDataContent.put(CmsConstant.CMS_DATA_STR_TITLE, "文章明细");
		}
		String res = CmsConstant.CMS_ROOT_PATH + "/" + params.get(CmsConstant.CMS_STYLE_NAME);
		//资源路径
		CmsDataContent.put(CmsConstant.BASE, res);
	}

}
