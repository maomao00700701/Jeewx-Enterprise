SELECT count(*) FROM cms_article WHERE 1=1
<#if params.columnid ?exists && params.columnid?length gt 0>
	and column_id = :params.columnid
</#if>
