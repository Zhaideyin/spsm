<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<%--<link rel="stylesheet" href="static/ace/css/datepicker.css" />--%>
	<link rel="stylesheet" type="text/css" href="plugins/webuploader/webuploader.css" />

</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">

						<form action="worknews/${msg }.do" name="Form" id="Form" method="post">
							<input type="hidden" name="WORKNEWS_ID" id="WORKNEWS_ID" value="${pd.WORKNEWS_ID}"/>
							<div id="zhongxin" style="padding-top: 13px;">
								<table id="table_report" class="table table-striped table-bordered table-hover">
									<tr>
										<td style="width:75px;text-align: right;padding-top: 13px;">标题:</td>
										<td><input type="text" name="TITLE" id="TITLE" value="${pd.TITLE}" maxlength="255" placeholder="这里输入标题" title="标题" style="width:98%;"/></td>
									</tr>
									<tr>
										<td style="width:75px;text-align: right;padding-top: 13px;">日期:</td>
										<td><input class="span10 date-picker" name="NEWSDATE" id="NEWSDATE" value="${pd.NEWSDATE}" onclick="WdatePicker()"  type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="日期" title="日期" style="width:98%;"/></td>
									</tr>
									<tr>
										<td style="width:75px;text-align: right;padding-top: 13px;">类别:</td>
										<td><input type="text" name="CATEGORY" id="CATEGORY" value="${pd.CATEGORY}" maxlength="255" placeholder="这里输入类别" title="类别" style="width:98%;"/></td>
									</tr>
									<tr>
										<td style="width:75px;text-align: right;padding-top: 13px;">新闻内容:</td>
										<%--<td><input type="text" name="CONTENT" id="CONTENT" value="${pd.CONTENT}" maxlength="255" placeholder="这里输入新闻内容" title="新闻内容" style="width:98%;"/></td>--%>
										<c:if test="${not empty pd.CONTENTPATH}">
											<input  style='visibility:hidden' type="text" name="CONTENTPATH" id="CONTENTPATH" value="${pd.CONTENTPATH}" maxlength="255" placeholder="文档路径" title="图片id" />
										</c:if>
										<td style="width:80%;">
											<script id="container" name="CONTENT" type="text/plain">${pd.CONTENT}</script>
										</td>
									</tr>
									<tr>
										<td style="text-align: center;" colspan="10">
											<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
											<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
										</td>
									</tr>
								</table>
							</div>
							<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
						</form>
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.page-content -->
		</div>
	</div>
	<!-- /.main-content -->
</div>
<!-- /.main-container -->


<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp"%>
<!-- 下拉框 -->
<script src="static/ace/js/chosen.jquery.js"></script>
<!-- 日期框 -->
<%--<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>--%>
<script src="plugins/My97DatePicker/WdatePicker.js"></script>

<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript">
	$(top.hangge());
	//保存
	function save(){
		if($("#TITLE").val()==""){
			$("#TITLE").tips({
				side:3,
				msg:'请输入标题',
				bg:'#AE81FF',
				time:2
			});
			$("#TITLE").focus();
			return false;
		}
		if($("#NEWSDATE").val()==""){
			$("#NEWSDATE").tips({
				side:3,
				msg:'请输入日期',
				bg:'#AE81FF',
				time:2
			});
			$("#NEWSDATE").focus();
			return false;
		}
		if($("#CATEGORY").val()==""){
			$("#CATEGORY").tips({
				side:3,
				msg:'请输入类别',
				bg:'#AE81FF',
				time:2
			});
			$("#CATEGORY").focus();
			return false;
		}
		if($("#CONTENT").val()==""){
			$("#CONTENT").tips({
				side:3,
				msg:'请输入新闻内容',
				bg:'#AE81FF',
				time:2
			});
			$("#CONTENT").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}

//	$(function() {
//		//日期框
//		$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
//	});
</script>
<script type="text/javascript" src="plugins/webuploader/webuploader.js"></script>
<script type="text/javascript" src="domoplugins/uploader.js"></script>
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>
<script>
	$(".filepicker").each(function(index){$(this).imguploader({id:"filepicker"+index})});
</script>
<script type="text/javascript">
	var editor = UE.getEditor('container');
</script>
<script type="text/javascript">
	$(top.hangge());	//关闭加载状态
</script>
</body>
</html>