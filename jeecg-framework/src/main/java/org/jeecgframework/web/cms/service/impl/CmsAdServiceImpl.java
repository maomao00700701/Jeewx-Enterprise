package org.jeecgframework.web.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.cms.dao.CmsAdDao;
import org.jeecgframework.web.cms.entity.CmsAdEntity;
import org.jeecgframework.web.cms.service.CmsAdServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("cmsAdService")
@Transactional
public class CmsAdServiceImpl extends CommonServiceImpl implements CmsAdServiceI {
	@Autowired
	private CmsAdDao cmsAdDao;

	@Override
	public List<CmsAdEntity> list(Map params, int page, int rows) {
		return cmsAdDao.listByMap(params, page, rows);
	}

	@Override
	public List<CmsAdEntity> list(Map params) {
		return cmsAdDao.listByMap(params);
	}

	@Override
	public List<CmsAdEntity> list(CmsAdEntity cmsAdEntity) {
		return cmsAdDao.list(cmsAdEntity);
	}

	@Override
	public List<CmsAdEntity> list(CmsAdEntity cmsAdEntity, int page, int rows) {
		// TODO Auto-generated method stub
		return cmsAdDao.list(cmsAdEntity);
	}
}