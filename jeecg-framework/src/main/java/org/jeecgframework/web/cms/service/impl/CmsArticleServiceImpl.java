package org.jeecgframework.web.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.cms.dao.CmsArticleDao;
import org.jeecgframework.web.cms.entity.CmsArticleEntity;
import org.jeecgframework.web.cms.service.CmsArticleServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service("cmsArticleService")
@Transactional
public class CmsArticleServiceImpl extends CommonServiceImpl implements CmsArticleServiceI {
	@Autowired
	private CmsArticleDao cmsArticleDao;

	@Override
	public List<CmsArticleEntity> listByMap(Map params, int page, int rows) {
		return cmsArticleDao.listByMap(params, page, rows);
	}

	@Override
	public List<CmsArticleEntity> listByMap(Map params) {
		return cmsArticleDao.listByMap(params);
	}

	@Override
	public List<CmsArticleEntity> list(CmsArticleEntity cmsArticleEntity) {
		return cmsArticleDao.list(cmsArticleEntity);
	}

	@Override
	public List<CmsArticleEntity> list(CmsArticleEntity cmsArticleEntity, int page, int rows) {
		// TODO Auto-generated method stub
		return cmsArticleDao.list(cmsArticleEntity);
	}

	@Override
	public int getCount(Map params) {
		return cmsArticleDao.getCount(params);
	}

	@Override
	public CmsArticleEntity getCmsArticleEntity(String id) {
		return this.getEntity(CmsArticleEntity.class, id);
	}
}