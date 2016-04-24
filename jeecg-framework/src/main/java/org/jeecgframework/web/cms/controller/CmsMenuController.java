package org.jeecgframework.web.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.cms.entity.CmsAdEntity;
import org.jeecgframework.web.cms.entity.CmsArticleEntity;
import org.jeecgframework.web.cms.entity.CmsMenuEntity;
import org.jeecgframework.web.cms.service.CmsMenuServiceI;
import org.jeecgframework.web.system.pojo.base.TSCategoryEntity;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;



/**
 * @Title: Controller
 * @Description: 后台管理：文章标题栏目
 * @author zhangdaihao
 * @date 2014-06-10 20:07:00
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/cmsMenuController")
public class CmsMenuController extends BaseController {

	@Autowired
	private CmsMenuServiceI cmsMenuService;
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
	 * cms首页
	 * 
	 * @return
	 */
	@RequestMapping(params = "index")
	public ModelAndView index(HttpServletRequest request,String userid) {
		List<CmsMenuEntity> columnList = cmsMenuService.getList(CmsMenuEntity.class);
		List<CmsAdEntity> adList = systemService.getList(CmsAdEntity.class);
		request.setAttribute("columnList", columnList);
		request.setAttribute("adList", adList);
		request.setAttribute("userid", userid);
		return new ModelAndView("system/cms/index");
	}

	/**
	 * 栏目管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("system/cms/cmsMenuList");
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
	@ResponseBody
	public Object datagrid(CmsMenuEntity cmsMenu, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CmsMenuEntity.class, dataGrid);
		if (cmsMenu.getId() == null || StringUtils.isEmpty(cmsMenu.getId())) {
			cq.isNull("parent");
		} else {
			cq.eq("parent.id", cmsMenu.getId());
			cmsMenu.setId(null);
		}
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, cmsMenu, request.getParameterMap());
		List<TSCategoryEntity> list = this.cmsMenuService.getListByCriteriaQuery(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setIdField("id");
		treeGridModel.setSrc("id");
		treeGridModel.setTextField("name");
		treeGridModel.setParentText("parent_name");
		treeGridModel.setParentId("parent_id");
		treeGridModel.setChildList("list");
		Map<String,Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("type", "type");
        fieldMap.put("href", "href");
        treeGridModel.setFieldMap(fieldMap);
		treeGrids = systemService.treegrid(list, treeGridModel);
		
		JSONArray jsonArray = new JSONArray();
        for (TreeGrid treeGrid : treeGrids) {
            jsonArray.add(JSON.parse(treeGrid.toJson()));
        }
        return jsonArray;
	}
	
	@RequestMapping(params = "tree")
	@ResponseBody
	public List<ComboTree> tree(String selfCode,ComboTree comboTree, boolean isNew) {
		CriteriaQuery cq = new CriteriaQuery(CmsMenuEntity.class);
		cq.isNull("parent");
		cq.add();
		List<CmsMenuEntity> categoryList = systemService.getListByCriteriaQuery(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		for (int i = 0; i < categoryList.size(); i++) {
			comboTrees.add(cmsMenuEntityConvertToTree(categoryList.get(i)));
		}
		return comboTrees;
	}

	private ComboTree cmsMenuEntityConvertToTree(CmsMenuEntity entity) {
		ComboTree tree = new ComboTree();
		tree.setId(entity.getId());
		tree.setText(entity.getName());
		if (entity.getList() != null && entity.getList().size() > 0) {
			List<ComboTree> comboTrees = new ArrayList<ComboTree>();
			for (int i = 0; i < entity.getList().size(); i++) {
				comboTrees.add(cmsMenuEntityConvertToTree(entity.getList().get(i)));
			}
			tree.setChildren(comboTrees);
		}
		return tree;
	}
	

	/**
	 * 删除栏目管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CmsMenuEntity menu, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		
		List<CmsArticleEntity> cmsArticleEntities = systemService.findByProperty(CmsArticleEntity.class, "columnId", menu.getId());
		
		if (cmsArticleEntities.size() > 0) {
			message = "栏目下有文章,请删除文章后再删除栏目";
		}else{
			menu = systemService.getEntity(CmsMenuEntity.class, menu.getId());
			message = "栏目管理删除成功";
			cmsMenuService.delete(menu);
		}
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加栏目管理
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CmsMenuEntity menu, HttpServletRequest request) {
		boolean flag = StringUtil.isEmpty(menu.getParent().getId());
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(menu.getId())) {
			message = "栏目管理更新成功";
			CmsMenuEntity t = cmsMenuService.get(CmsMenuEntity.class, menu.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(menu, t);
				if (flag) {
					t.setParent(null);
				}
				cmsMenuService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "栏目管理更新失败";
			}
		} else {
			message = "栏目管理添加成功";
			if (flag) {
				menu.setParent(null);
			}
			cmsMenuService.save(menu);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 栏目管理列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CmsMenuEntity menu, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(menu.getId())) {
			menu = cmsMenuService.getEntity(CmsMenuEntity.class, menu.getId());
			req.setAttribute("columnPage", menu);
		}
		return new ModelAndView("system/cms/cmsMenu");
	}
	
	@RequestMapping(params = "add")
	public ModelAndView add(CmsMenuEntity menu, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(menu.getId())) {
			menu = cmsMenuService.getEntity(CmsMenuEntity.class, menu.getId());
			req.setAttribute("columnPage", menu);
		}
		return new ModelAndView("system/cms/cmsMenu-add");
	}
}
