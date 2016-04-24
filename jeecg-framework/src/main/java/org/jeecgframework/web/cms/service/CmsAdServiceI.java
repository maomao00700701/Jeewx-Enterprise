package org.jeecgframework.web.cms.service;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.cms.entity.CmsAdEntity;


public interface CmsAdServiceI extends CommonService {

	public List<CmsAdEntity> list(CmsAdEntity cmsAdEntity);

	public List<CmsAdEntity> list(CmsAdEntity cmsAdEntity, int page, int rows);

	public List<CmsAdEntity> list(Map params, int page, int rows);

	public List<CmsAdEntity> list(Map params);

}
