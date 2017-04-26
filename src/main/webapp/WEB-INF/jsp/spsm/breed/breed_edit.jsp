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
					
					<form action="breed/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="BREED_ID" id="BREED_ID" value="${pd.BREED_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">品种名:</td>
								<td><input type="text" name="TITLE" id="TITLE" value="${pd.TITLE}" maxlength="255" placeholder="这里输入品种名" title="品种名" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">选育单位:</td>
								<td><input type="text" name="BREEDING_ORG" id="BREEDING_ORG" value="${pd.BREEDING_ORG}" maxlength="255" placeholder="这里输入选育单位" title="选育单位" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">审定时间:</td>
								<td><input type="text" name="AUTHORISE_DATE" id="AUTHORISE_DATE" value="${pd.AUTHORISE_DATE}" maxlength="255" placeholder="这里输入审定时间" title="审定时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">品种源:</td>
								<td><input type="text" name="BREED_SOURCE" id="BREED_SOURCE" value="${pd.BREED_SOURCE}" maxlength="255" placeholder="这里输入品种源" title="品种源" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">特征:</td>
								<td><input type="text" name="FEATURE" id="FEATURE" value="${pd.FEATURE}" maxlength="255" placeholder="这里输入特征" title="特征" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">产量表现:</td>
								<td><input type="text" name="OUTPUT" id="OUTPUT" value="${pd.OUTPUT}" maxlength="255" placeholder="这里输入产量表现" title="产量表现" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">栽培要点:</td>
								<td><input type="text" name="GROW_POINT" id="GROW_POINT" value="${pd.GROW_POINT}" maxlength="255" placeholder="这里输入栽培要点" title="栽培要点" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">适宜地区:</td>
								<td><input type="text" name="AREA" id="AREA" value="${pd.AREA}" maxlength="255" placeholder="这里输入适宜地区" title="适宜地区" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">关键字:</td>
								<td><input type="text" name="KEYWORDS" id="KEYWORDS" value="${pd.KEYWORDS}" maxlength="255" placeholder="这里输入关键字" title="关键字" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td><input class="span10 date-picker" name="CREATE_TIME" id="CREATE_TIME" value="${pd.CREATE_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="创建时间" title="创建时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">点击次数:</td>
								<td><input type="text" name="HIT" id="HIT" value="${pd.HIT}" maxlength="255" placeholder="这里输入点击次数" title="点击次数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">认证编号:</td>
								<td><input type="text" name="AUTHORISE_NUM" id="AUTHORISE_NUM" value="${pd.AUTHORISE_NUM}" maxlength="255" placeholder="这里输入认证编号" title="认证编号" style="width:98%;"/></td>
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
			if($("#TITLE").val()==""){
				$("#TITLE").tips({
					side:3,
		            msg:'请输入品种名',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TITLE").focus();
			return false;
			}
			if($("#BREEDING_ORG").val()==""){
				$("#BREEDING_ORG").tips({
					side:3,
		            msg:'请输入选育单位',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BREEDING_ORG").focus();
			return false;
			}
			if($("#AUTHORISE_DATE").val()==""){
				$("#AUTHORISE_DATE").tips({
					side:3,
		            msg:'请输入审定时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AUTHORISE_DATE").focus();
			return false;
			}
			if($("#BREED_SOURCE").val()==""){
				$("#BREED_SOURCE").tips({
					side:3,
		            msg:'请输入品种源',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BREED_SOURCE").focus();
			return false;
			}
			if($("#FEATURE").val()==""){
				$("#FEATURE").tips({
					side:3,
		            msg:'请输入特征',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#FEATURE").focus();
			return false;
			}
			if($("#OUTPUT").val()==""){
				$("#OUTPUT").tips({
					side:3,
		            msg:'请输入产量表现',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OUTPUT").focus();
			return false;
			}
			if($("#GROW_POINT").val()==""){
				$("#GROW_POINT").tips({
					side:3,
		            msg:'请输入栽培要点',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GROW_POINT").focus();
			return false;
			}
			if($("#AREA").val()==""){
				$("#AREA").tips({
					side:3,
		            msg:'请输入适宜地区',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AREA").focus();
			return false;
			}
			if($("#KEYWORDS").val()==""){
				$("#KEYWORDS").tips({
					side:3,
		            msg:'请输入关键字',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#KEYWORDS").focus();
			return false;
			}
			if($("#CREATE_TIME").val()==""){
				$("#CREATE_TIME").tips({
					side:3,
		            msg:'请输入创建时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CREATE_TIME").focus();
			return false;
			}
			if($("#HIT").val()==""){
				$("#HIT").tips({
					side:3,
		            msg:'请输入点击次数',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#HIT").focus();
			return false;
			}
			if($("#AUTHORISE_NUM").val()==""){
				$("#AUTHORISE_NUM").tips({
					side:3,
		            msg:'请输入认证编号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AUTHORISE_NUM").focus();
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