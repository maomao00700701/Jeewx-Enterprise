package org.jeecgframework.web.cms.dao;

import java.util.List;
import java.util.Map;

import org.jeecgframework.minidao.annotation.Arguments;
import org.jeecgframework.minidao.annotation.MiniDao;
import org.jeecgframework.minidao.annotation.ResultType;
import org.jeecgframework.web.cms.entity.CmsAdEntity;



@MiniDao
public interface CmsAdDao{

	@Arguments( { "cmsAdEntity"})
	@ResultType(CmsAdEntity.class)
	public List<CmsAdEntity> list(CmsAdEntity cmsAdEntity);

	@Arguments( { "cmsAdEntity", "page", "rows" })
	@ResultType(CmsAdEntity.class)
	public List<CmsAdEntity> list(CmsAdEntity cmsAdEntity, int page, int rows);

	@Arguments( { "params"})
	@ResultType(CmsAdEntity.class)
	public List<CmsAdEntity> listByMap(Map params);

	@Arguments( { "params", "page", "rows" })
	@ResultType(CmsAdEntity.class)
	public List<CmsAdEntity> listByMap(Map params, int page, int rows);
	
}
