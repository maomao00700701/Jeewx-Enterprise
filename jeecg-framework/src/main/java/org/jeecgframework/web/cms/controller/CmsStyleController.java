package org.jeecgframework.web.cms.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.UploadFile;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.ZipUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.cms.entity.CmsStyleEntity;
import org.jeecgframework.web.cms.service.CmsStyleServiceI;
import org.jeecgframework.web.system.pojo.base.TSDocument;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;



/**
 * @Title: Controller
 * @Description: 站点模板
 * @author onlineGenerator
 * @date 2014-07-15 22:20:46
 * @version V1.0
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/cmsStyleController")
public class CmsStyleController extends BaseController {
	@Autowired
	private CmsStyleServiceI cmsStyleService;
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
	 * 站点模板列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("system/cms/style/cmsStyleList");
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
	public void datagrid(CmsStyleEntity cmsStyle, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CmsStyleEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, cmsStyle, request.getParameterMap());
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.cmsStyleService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除站点模板
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(CmsStyleEntity cmsStyle,HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		cmsStyle = systemService.getEntity(CmsStyleEntity.class, cmsStyle.getId());
		message = "站点模板删除成功";
		try {
			cmsStyleService.delete(cmsStyle);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "站点模板删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除站点模板
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "站点模板删除成功";
		try {
			for (String id : ids.split(",")) {
				CmsStyleEntity cmsStyle = systemService.getEntity(CmsStyleEntity.class, id);
				cmsStyleService.delete(cmsStyle);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "站点模板删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加站点模板
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(CmsStyleEntity cmsStyle, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "站点模板添加成功";
		try {
			cmsStyleService.save(cmsStyle);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "站点模板添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		j.setObj(cmsStyle);
		return j;
	}

	/**
	 * 更新站点模板
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(CmsStyleEntity cmsStyle, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "站点模板更新成功";
		CmsStyleEntity t = cmsStyleService.get(CmsStyleEntity.class, cmsStyle.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(cmsStyle, t);
			cmsStyleService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "站点模板更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		j.setObj(cmsStyle);
		return j;
	}

	/**
	 * 站点模板新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(CmsStyleEntity cmsStyle, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(cmsStyle.getId())) {
			cmsStyle = cmsStyleService.getEntity(CmsStyleEntity.class, cmsStyle.getId());
			req.setAttribute("cmsStylePage", cmsStyle);
		}
		return new ModelAndView("system/cms/style/cmsStyle-add");
	}

	/**
	 * 站点模板编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(CmsStyleEntity cmsStyle, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(cmsStyle.getId())) {
			cmsStyle = cmsStyleService.getEntity(CmsStyleEntity.class, cmsStyle.getId());
			req.setAttribute("cmsStylePage", cmsStyle);
		}
		return new ModelAndView("system/cms/style/cmsStyle-update");
	}
	
	@RequestMapping(params = "upload", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson upload(MultipartHttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		// 获取模板ID
		String id = request.getParameter("id");
		Map<String, Object> attributes = new HashMap<String, Object>();
		TSTypegroup tsTypegroup = systemService.getTypeGroup("fieltype", "文档分类");
		TSType tsType = systemService.getType("files", "附件", tsTypegroup);
		String fileKey = oConvertUtils.getString(request.getParameter("fileKey"));// 文件ID
		String documentTitle = oConvertUtils.getString(request.getParameter("documentTitle"));// 文件标题
		TSDocument document = new TSDocument();
		if (StringUtil.isNotEmpty(fileKey)) {
			document.setId(fileKey);
			document = systemService.getEntity(TSDocument.class, fileKey);
			document.setDocumentTitle(documentTitle);
		}
		document.setSubclassname(MyClassLoader.getPackPath(document));
		document.setCreatedate(DateUtils.gettimestamp());
		document.setTSType(tsType);
		UploadFile uploadFile = new UploadFile(request, document);
		uploadFile.setCusPath("files");
		uploadFile.setSwfpath("swfpath");
		document = systemService.uploadFile(uploadFile);
		// 获取持久对象
		CmsStyleEntity cmsStyle = cmsStyleService.getEntity(CmsStyleEntity.class, id);
		// 定义模板路径，规则为：template/cms/{userid}/模板名称
		String requestPath = request.getSession().getServletContext().getRealPath("/template/cms");
		// 硬盘存储路径，即解压路径
		String realpath = requestPath + "/" + cmsStyle.getTemplateUrl()+"/";
		File tempfolder = new File(realpath);
		if (!tempfolder.exists()) {
			// 创建模板文件夹路径
			tempfolder.mkdirs();
		}
		cmsStyleService.updateEntitie(cmsStyle);
		try {
			String path = request.getSession().getServletContext().getRealPath("/")+ document.getRealpath();
			ZipUtil.unZip(path,tempfolder.getAbsolutePath()+"/");
		} catch (Exception e) {
			e.printStackTrace();
		}
		attributes.put("url", document.getRealpath());
		attributes.put("fileKey", document.getId());
		attributes.put("name", document.getAttachmenttitle());
		attributes.put("viewhref", "commonController.do?openViewFile&fileid="+ document.getId());
		attributes.put("delurl", "commonController.do?delObjFile&fileKey="+ document.getId());
		j.setMsg("文件添加成功");
		j.setAttributes(attributes);
		return j;
	}
	/**
	 * 下载自定义模板
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(params = "downloadTemplate")
	public void downloadTemplate(HttpServletRequest request,HttpServletResponse response,String id){
		String url = "default";
		CmsStyleEntity style = cmsStyleService.get(CmsStyleEntity.class, id);
		if(style!=null){
			url = style.getTemplateUrl();
		}
		String path = "template/cms/"+url;
		
		String sourceSrc = request.getSession().getServletContext().getRealPath(path);
		String downloadSrc = sourceSrc + ".zip";
		String fileName = style.getTemplateName()+".zip";
		try {
			// 压缩模版文件夹
			ZipUtil.compress(sourceSrc, downloadSrc);
			response.reset();
			// 设置response的Header
			response.setContentType("APPLICATION/OCTET-STREAM");
			OutputStream out = new BufferedOutputStream(response.getOutputStream());
			// 文件字符集
			response.setHeader("Content-disposition", "attachment;filename="+ new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			InputStream inStream = new FileInputStream(downloadSrc);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.write(buffer);
			out.flush();
			out.close();
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 下载默认模板
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "downloadDefaultTemplate")
	public void downloadDefaultTemplate(HttpServletRequest request,HttpServletResponse response) {
		String path = "template/cms/default/";
		String sourceSrc = request.getSession().getServletContext().getRealPath(path);
		String downloadSrc = sourceSrc + ".zip";
		try {
			// 压缩模版文件夹
			ZipUtil.compress(sourceSrc, downloadSrc);
			String fileName = "jeewx_default.zip";
			response.reset();
			// 设置response的Header
			response.setContentType("APPLICATION/OCTET-STREAM");
			OutputStream out = new BufferedOutputStream(response.getOutputStream());
			// 文件字符集
			response.setHeader("Content-disposition", "attachment;filename="+ new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			InputStream inStream = new FileInputStream(downloadSrc);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.write(buffer);
			out.flush();
			out.close();
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
