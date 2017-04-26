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
					
					<form action="patent/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="PATENT_ID" id="PATENT_ID" value="${pd.PATENT_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">专利名:</td>
								<td><input type="text" name="TITLE" id="TITLE" value="${pd.TITLE}" maxlength="255" placeholder="这里输入专利名" title="专利名" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">申请人:</td>
								<td><input type="text" name="APPLICANT" id="APPLICANT" value="${pd.APPLICANT}" maxlength="255" placeholder="这里输入申请人" title="申请人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">申请时间:</td>
								<td><input type="text" name="APPLY_DATE" id="APPLY_DATE" value="${pd.APPLY_DATE}" maxlength="255" placeholder="这里输入申请时间" title="申请时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">申请编号:</td>
								<td><input type="text" name="APPLY_NUM" id="APPLY_NUM" value="${pd.APPLY_NUM}" maxlength="255" placeholder="这里输入申请编号" title="申请编号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">认证时间:</td>
								<td><input type="text" name="AUTHORIZE_DATE" id="AUTHORIZE_DATE" value="${pd.AUTHORIZE_DATE}" maxlength="255" placeholder="这里输入认证时间" title="认证时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">类型:</td>
								<td><input type="text" name="TYPE" id="TYPE" value="${pd.TYPE}" maxlength="255" placeholder="这里输入类型" title="类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">发明人:</td>
								<td><input type="text" name="INVENTOR" id="INVENTOR" value="${pd.INVENTOR}" maxlength="255" placeholder="这里输入发明人" title="发明人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">摘要:</td>
								<td><input type="text" name="SUMMARY" id="SUMMARY" value="${pd.SUMMARY}" maxlength="255" placeholder="这里输入摘要" title="摘要" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">关键词:</td>
								<td><input type="text" name="KEYWORDS" id="KEYWORDS" value="${pd.KEYWORDS}" maxlength="255" placeholder="这里输入关键词" title="关键词" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td><input class="span10 date-picker" name="CREATE_TIME" id="CREATE_TIME" value="${pd.CREATE_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="创建时间" title="创建时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">点击次数:</td>
								<td><input type="number" name="HIT" id="HIT" value="${pd.HIT}" maxlength="32" placeholder="这里输入点击次数" title="点击次数" style="width:98%;"/></td>
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
		            msg:'请输入专利名',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TITLE").focus();
			return false;
			}
			if($("#APPLICANT").val()==""){
				$("#APPLICANT").tips({
					side:3,
		            msg:'请输入申请人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#APPLICANT").focus();
			return false;
			}
			if($("#APPLY_DATE").val()==""){
				$("#APPLY_DATE").tips({
					side:3,
		            msg:'请输入申请时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#APPLY_DATE").focus();
			return false;
			}
			if($("#APPLY_NUM").val()==""){
				$("#APPLY_NUM").tips({
					side:3,
		            msg:'请输入申请编号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#APPLY_NUM").focus();
			return false;
			}
			if($("#AUTHORIZE_DATE").val()==""){
				$("#AUTHORIZE_DATE").tips({
					side:3,
		            msg:'请输入认证时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AUTHORIZE_DATE").focus();
			return false;
			}
			if($("#TYPE").val()==""){
				$("#TYPE").tips({
					side:3,
		            msg:'请输入类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TYPE").focus();
			return false;
			}
			if($("#INVENTOR").val()==""){
				$("#INVENTOR").tips({
					side:3,
		            msg:'请输入发明人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INVENTOR").focus();
			return false;
			}
			if($("#SUMMARY").val()==""){
				$("#SUMMARY").tips({
					side:3,
		            msg:'请输入摘要',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUMMARY").focus();
			return false;
			}
			if($("#KEYWORDS").val()==""){
				$("#KEYWORDS").tips({
					side:3,
		            msg:'请输入关键词',
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