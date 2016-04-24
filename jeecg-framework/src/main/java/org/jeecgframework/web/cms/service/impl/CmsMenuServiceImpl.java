package org.jeecgframework.web.cms.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.cms.dao.CmsMenuDao;
import org.jeecgframework.web.cms.entity.CmsMenuEntity;
import org.jeecgframework.web.cms.service.CmsMenuServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service("cmsMenuService")
@Transactional
public class CmsMenuServiceImpl extends CommonServiceImpl implements
		CmsMenuServiceI {
	@Autowired
	private CmsMenuDao cmsArticleDao;

	@Override
	public <T> Serializable save(T entity) {
		Serializable t = super.save(entity);
		return t;
	}

	@Override
	public List<CmsMenuEntity> list(Map params, int page, int rows) {
		return cmsArticleDao.listByMap(params, page, rows);
	}

	@Override
	public List<CmsMenuEntity> list(Map params) {
		return cmsArticleDao.listByMap(params);
	}

	@Override
	public List<CmsMenuEntity> list(CmsMenuEntity cmsMenuEntity) {
		return cmsArticleDao.list(cmsMenuEntity);
	}

	@Override
	public List<CmsMenuEntity> list(CmsMenuEntity cmsMenuEntity, int page,
			int rows) {
		// TODO Auto-generated method stub
		return cmsArticleDao.list(cmsMenuEntity);
	}

}