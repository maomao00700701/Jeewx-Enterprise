package org.jeecgframework.web.cms.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.cgdynamgraph.entity.core.CgDynamGraphConfigHeadEntity;
import org.jeecgframework.web.cgdynamgraph.entity.core.CgDynamGraphConfigParamEntity;
import org.jeecgframework.web.cgform.entity.upload.CgUploadEntity;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.cms.entity.CmsArticleEntity;
import org.jeecgframework.web.cms.entity.CmsMenuEntity;
import org.jeecgframework.web.cms.service.CmsArticleServiceI;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



/**   
 * @Title: Controller
 * @Description: 后台管理：文章
 * @author zhangdaihao
 * @date 2014-06-10i 20:07:00
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/cmsArticleController")
public class CmsArticleController extends BaseController {
	@Autowired
	private CmsArticleServiceI cmsArticleService;
	@Autowired
	private SystemService systemService;
	private String message;
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("system/cms/cmsArticleList");
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
	public void datagrid(CmsArticleEntity cmsArticle,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CmsArticleEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, cmsArticle, request.getParameterMap());
		this.cmsArticleService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * @param cmsArticle
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridwx")
	public void datagridwx(CmsArticleEntity cmsArticle,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CmsArticleEntity.class, dataGrid);
		cq.eq("columnId", cmsArticle.getColumnId());
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, cmsArticle, request.getParameterMap());
		this.cmsArticleService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CmsArticleEntity cmsArticle, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		cmsArticle = systemService.getEntity(CmsArticleEntity.class, cmsArticle.getId());
		message = "信息删除成功";
		cmsArticleService.delete(cmsArticle);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CmsArticleEntity cmsArticle, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(cmsArticle.getId())) {
			message = "信息更新成功";
			CmsArticleEntity t = cmsArticleService.get(CmsArticleEntity.class, cmsArticle.getId());
			String publishFlag = cmsArticle.getPublish();
			try {
				MyBeanUtils.copyBeanNotNull2Bean(cmsArticle, t);
				if (publishFlag.equalsIgnoreCase("Y")) {
					if (t.getPublishDate() == null) {//没发布过,则没有发布时间
						t.setPublishDate(new Date());
					}
				}
				cmsArticleService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "信息更新失败";
			}
		} else {
			message = "信息添加成功";
			String publishFlag = cmsArticle.getPublish();
			if (publishFlag.equalsIgnoreCase("Y")) {
				cmsArticle.setPublishDate(new Date());
			}else {
				cmsArticle.setPublishDate(null);
			}
			cmsArticleService.save(cmsArticle);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		j.setObj(cmsArticle);
		return j;
	}
	
	/**
	 * 信息发布
	 * 
	 * @return
	 */
	@RequestMapping(params = "publish")
	@ResponseBody
	public AjaxJson publish(CmsArticleEntity cmsArticle, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		cmsArticle = systemService.getEntity(CmsArticleEntity.class, cmsArticle.getId());
		String publishFlag = cmsArticle.getPublish();
		if (publishFlag.equalsIgnoreCase("N")) {
			message = "信息发布成功";
			if (cmsArticle.getPublishDate() == null) {//没发布过,则没有发布时间
				cmsArticle.setPublish("Y");
				cmsArticle.setPublishDate(new Date());
			}else{
				message = "信息已发布,无需重复发布!";
			}
			cmsArticleService.saveOrUpdate(cmsArticle);
		}else{
			message = "信息已发布,无需重复发布!";
		}
		systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}
	
	@RequestMapping(params = "popmenulink")
	public ModelAndView popmenulink(ModelMap modelMap, @RequestParam String id, HttpServletRequest request) {
		request.setAttribute("id",id); 
		return new ModelAndView("system/cms/popmenulink");
	}
	
	/**
	 * 获取文件附件信息
	 * 
	 */
	@RequestMapping(params = "getFiles")
	@ResponseBody
	public AjaxJson getFiles(String id){
		List<CgUploadEntity> uploadBeans = cgFormFieldService.findByProperty(CgUploadEntity.class, "cgformId", id);
		List<Map<String,Object>> files = new ArrayList<Map<String,Object>>(0);
		for(CgUploadEntity b:uploadBeans){
			String title = b.getAttachmenttitle();//附件名
			String fileKey = b.getId();//附件主键
			String path = b.getRealpath();//附件路径
			String field = b.getCgformField();//表单中作为附件控件的字段
			Map<String, Object> file = new HashMap<String, Object>();
			file.put("title", title);
			file.put("fileKey", fileKey);
			file.put("path", path);
			file.put("field", field==null?"":field);
			files.add(file);
		}
		AjaxJson j = new AjaxJson();
		j.setObj(files);
		return j;
	}

	/**
	 * 信息列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CmsArticleEntity cmsArticle, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(cmsArticle.getId())) {
			cmsArticle = cmsArticleService.getEntity(CmsArticleEntity.class, cmsArticle.getId());
			req.setAttribute("cmsArticlePage", cmsArticle);
		}
		return new ModelAndView("system/cms/cmsArticle");
	}
	
	/**
	 * 信息列表(前台) 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "cmsArticleListShow")
	public ModelAndView cmsArticleListShow(HttpServletRequest request) {
		if (StringUtil.isNotEmpty(request.getParameter("columnId"))) {
			request.setAttribute("column", cmsArticleService.getEntity(CmsMenuEntity.class,request.getParameter("columnId")));
		}
		return new ModelAndView("system/cms/cmsArticleListShow");
	}

	/**
	 * 信息展示(前台) 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "cmsArticleShow")
	public ModelAndView cmsArticleShow(HttpServletRequest request) {
		if (StringUtil.isNotEmpty(request.getParameter("articleId"))) {
			CmsArticleEntity cmsArticle = cmsArticleService.getEntity(CmsArticleEntity.class, request.getParameter("articleId"));
			request.setAttribute("cmsArticlePage", cmsArticle);
		}
		return new ModelAndView("system/cms/cmsArticleShow");
	}
}
