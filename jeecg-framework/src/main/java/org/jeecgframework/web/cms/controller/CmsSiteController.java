package org.jeecgframework.web.cms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.cms.entity.CmsSiteEntity;
import org.jeecgframework.web.cms.service.CmsSiteServiceI;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



/**
 * @Title: Controller
 * @Description: 微站点信息
 * @author onlineGenerator
 * @date 2014-07-15 21:04:08
 * @version V1.0
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/cmsSiteController")
public class CmsSiteController extends BaseController {
	@Autowired
	private CmsSiteServiceI cmsSiteService;
	@Autowired
	private SystemService systemService;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 微站点信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("system/cms/site/cmsSiteList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(CmsSiteEntity cmsSite, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CmsSiteEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, cmsSite, request.getParameterMap());
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.cmsSiteService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除微站点信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(CmsSiteEntity cmsSite, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		cmsSite = systemService.getEntity(CmsSiteEntity.class, cmsSite.getId());
		message = "微站点信息删除成功";
		try {
			cmsSiteService.delete(cmsSite);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "微站点信息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除微站点信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "站点信息删除成功";
		try {
			for (String id : ids.split(",")) {
				CmsSiteEntity cmsSite = systemService.getEntity(CmsSiteEntity.class, id);
				cmsSiteService.delete(cmsSite);
				systemService.addLog(message, Globals.Log_Type_DEL,	Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "站点信息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加站点信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(CmsSiteEntity cmsSite, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		List<CmsSiteEntity> lst = cmsSiteService.getList(CmsSiteEntity.class);
		if(lst.size()!=0){
			message="每个用户只能添加一个站点";
			j.setSuccess(false);
		}else{
			message = "站点信息添加成功";
			try {
				cmsSiteService.save(cmsSite);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "站点信息添加失败";
				throw new BusinessException(e.getMessage());
			}
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新站点信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(CmsSiteEntity cmsSite, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "站点信息更新成功";
		CmsSiteEntity t = cmsSiteService.get(CmsSiteEntity.class, cmsSite.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(cmsSite, t);
			cmsSiteService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "站点信息更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 微站点信息新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(CmsSiteEntity cmsSite, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(cmsSite.getId())) {
			cmsSite = cmsSiteService.getEntity(CmsSiteEntity.class, cmsSite.getId());
			req.setAttribute("cmsSitePage", cmsSite);
		}
		return new ModelAndView("system/cms/site/cmsSite-add");
	}

	/**
	 * 微站点信息编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(CmsSiteEntity cmsSite, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(cmsSite.getId())) {
			cmsSite = cmsSiteService.getEntity(CmsSiteEntity.class, cmsSite.getId());
			req.setAttribute("cmsSitePage", cmsSite);
		}
		return new ModelAndView("system/cms/site/cmsSite-update");
	}

}
