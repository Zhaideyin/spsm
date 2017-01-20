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
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
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
					
					<form action="webinfo/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="WEBINFO_ID" id="WEBINFO_ID" value="${pd.WEBINFO_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">种子地址:</td>
								<td><input type="text" name="SEED" id="SEED" value="${pd.SEED}" maxlength="255" placeholder="这里输入种子地址" title="种子地址" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">url过滤正则表达式:</td>
								<td><input type="text" name="URLREX" id="URLREX" value="${pd.URLREX}" maxlength="255" placeholder="这里输入url过滤正则表达式" title="url过滤正则表达式" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">标题所在标签:</td>
								<td><input type="text" name="TITLETAG" id="TITLETAG" value="${pd.TITLETAG}" maxlength="255" placeholder="这里输入标题标签" title="标题标签" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">内容所在标签:</td>
								<td><input type="text" name="CONTENTTAG" id="CONTENTTAG" value="${pd.CONTENTTAG}" maxlength="255" placeholder="这里输入是抓取内容标签" title="内容标签" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">是否含图片:</td>
								<td><input type="text" name="HASIMG" id="HASIMG" value="${pd.HASIMG}" maxlength="255" placeholder="这里输入是否含图片，输入‘true’ or ‘false’" title="是否含图片" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">图片过滤正则表达式:</td>
								<td><input type="text" name="IMGREGEX" id="IMGREGEX" value="${pd.IMGREGEX}" maxlength="255" placeholder="这里输入图片过滤正则表达式" title="图片过滤正则表达式" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">图片所在标签:</td>
								<td><input type="text" name="IMGTAG" id="IMGTAG" value="${pd.IMGTAG}" maxlength="255" placeholder="这里输入图片标签" title="图片标签" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">是否含有文件:</td>
								<td><input type="text" name="HASDOC" id="HASDOC" value="${pd.HASDOC}" maxlength="255" placeholder="这里输入是否含有文件" title="是否含有文件" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">文件过滤正则表达式:</td>
								<td><input type="text" name="DOCREGEX" id="DOCREGEX" value="${pd.DOCREGEX}" maxlength="255" placeholder="这里输入文件过滤正则表达式" title="文件过滤正则表达式" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">文件所在标签:</td>
								<td><input type="text" name="DOCTAG" id="DOCTAG" value="${pd.DOCTAG}" maxlength="255" placeholder="这里输入文件标签" title="文件标签" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">网站总页数:</td>
								<td><input type="text" name="TOTALPAGE" id="TOTALPAGE" value="${pd.TOTALPAGE}" maxlength="255" placeholder="这里输入网站总页数" title="网站总页数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">ajax分页标签:</td>
								<td><input type="text" name="PAGEAJAXTAG" id="PAGEAJAXTAG" value="${pd.PAGEAJAXTAG}" maxlength="255" placeholder="这里输入网站分页是ajax分页设置" title="网站分页是ajax分页设置" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">get分页标签:</td>
								<td><input type="text" name="PAGEGETTAG" id="PAGEGETTAG" value="${pd.PAGEGETTAG}" maxlength="255" placeholder="这里输入网站分页是get的" title="网站分页是get的" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">网站分页类型:</td>
								<td><input type="text" name="PAGEMETHOD" id="PAGEMETHOD" value="${pd.PAGEMETHOD}" maxlength="255" placeholder="这里输入网站分页类型" title="网站分页类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">网页编码格式:</td>
								<td><input type="text" name="PAGEENCODING" id="PAGEENCODING" value="${pd.PAGEENCODING}" maxlength="255" placeholder="这里输入网页编码格式" title="网页编码格式" style="width:98%;"/></td>
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
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//保存
		function save(){
			if($("#SEED").val()==""){
				$("#SEED").tips({
					side:3,
		            msg:'请输入种子地址',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SEED").focus();
			return false;
			}
			if($("#URLREX").val()==""){
				$("#URLREX").tips({
					side:3,
		            msg:'请输入url过滤正则表达式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#URLREX").focus();
			return false;
			}
			if($("#TITLETAG").val()==""){
				$("#TITLETAG").tips({
					side:3,
		            msg:'请输入标题标签',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TITLETAG").focus();
			return false;
			}
			if($("#CONTENTTAG").val()==""){
				$("#CONTENTTAG").tips({
					side:3,
		            msg:'请输入内容标签',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONTENTTAG").focus();
			return false;
			}
			if($("#HASIMG").val()==""){
				$("#HASIMG").tips({
					side:3,
		            msg:'请输入是否含图片',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#HASIMG").focus();
			return false;
			}
			if($("#IMGREGEX").val()==""){
				$("#IMGREGEX").tips({
					side:3,
		            msg:'请输入图片过滤正则表达式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IMGREGEX").focus();
			return false;
			}
			if($("#IMGTAG").val()==""){
				$("#IMGTAG").tips({
					side:3,
		            msg:'请输入图片标签',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IMGTAG").focus();
			return false;
			}
			if($("#HASDOC").val()==""){
				$("#HASDOC").tips({
					side:3,
		            msg:'请输入是否含有文件',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#HASDOC").focus();
			return false;
			}
			if($("#DOCREGEX").val()==""){
				$("#DOCREGEX").tips({
					side:3,
		            msg:'请输入文件过滤正则表达式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DOCREGEX").focus();
			return false;
			}
			if($("#DOCTAG").val()==""){
				$("#DOCTAG").tips({
					side:3,
		            msg:'请输入文件标签',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DOCTAG").focus();
			return false;
			}
			if($("#TOTALPAGE").val()==""){
				$("#TOTALPAGE").tips({
					side:3,
		            msg:'请输入网站总页数',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TOTALPAGE").focus();
			return false;
			}
			if($("#PAGEAJAXTAG").val()==""){
				$("#PAGEAJAXTAG").tips({
					side:3,
		            msg:'请输入网站分页是ajax分页设置',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PAGEAJAXTAG").focus();
			return false;
			}
			if($("#PAGEGETTAG").val()==""){
				$("#PAGEGETTAG").tips({
					side:3,
		            msg:'请输入网站分页是get的',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PAGEGETTAG").focus();
			return false;
			}
			if($("#PAGEMETHOD").val()==""){
				$("#PAGEMETHOD").tips({
					side:3,
		            msg:'请输入网站分页类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PAGEMETHOD").focus();
			return false;
			}
			if($("#PAGEENCODING").val()==""){
				$("#PAGEENCODING").tips({
					side:3,
		            msg:'请输入网页编码格式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PAGEENCODING").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
		<script type="text/javascript" src="plugins/webuploader/webuploader.js"></script>
		<script type="text/javascript" src="domoplugins/uploader.js"></script>
		<script>
			$(".filepicker").each(function(index){$(this).imguploader({id:"filepicker"+index})});
		</script>
</body>
</html>