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
					
					<form action="achievement/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ACHIEVEMENT_ID" id="ACHIEVEMENT_ID" value="${pd.ACHIEVEMENT_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">标题:</td>
								<td><input type="text" name="TITLE" id="TITLE" value="${pd.TITLE}" maxlength="255" placeholder="这里输入标题" title="标题" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">完成单位:</td>
								<td><input type="text" name="COMPLETE_ORG" id="COMPLETE_ORG" value="${pd.COMPLETE_ORG}" maxlength="255" placeholder="这里输入完成单位" title="完成单位" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">完成人:</td>
								<td><input type="text" name="COMPLETE_PER" id="COMPLETE_PER" value="${pd.COMPLETE_PER}" maxlength="255" placeholder="这里输入完成人" title="完成人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">奖励类型:</td>
								<td><input type="text" name="ENCOURAGEMENT_CLASS" id="ENCOURAGEMENT_CLASS" value="${pd.ENCOURAGEMENT_CLASS}" maxlength="255" placeholder="这里输入奖励类型" title="奖励类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">奖励等级:</td>
								<td><input type="text" name="ENCOURAGEMENT_GRADE" id="ENCOURAGEMENT_GRADE" value="${pd.ENCOURAGEMENT_GRADE}" maxlength="255" placeholder="这里输入奖励等级" title="奖励等级" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">获奖时间:</td>
								<td><input type="text" name="ENCOURAGEMENT_DATE" id="ENCOURAGEMENT_DATE" value="${pd.ENCOURAGEMENT_DATE}" maxlength="255" placeholder="这里输入获奖时间" title="获奖时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">获奖级别:</td>
								<td><input type="text" name="ENCOURAGEMENT_LEVEL" id="ENCOURAGEMENT_LEVEL" value="${pd.ENCOURAGEMENT_LEVEL}" maxlength="255" placeholder="这里输入获奖级别" title="获奖级别" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">描述:</td>
								<td><input type="text" name="DESCRIPTION" id="DESCRIPTION" value="${pd.DESCRIPTION}" maxlength="255" placeholder="这里输入描述" title="描述" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">联系单位名称:</td>
								<td><input type="text" name="ORGANIZATION_NAME" id="ORGANIZATION_NAME" value="${pd.ORGANIZATION_NAME}" maxlength="255" placeholder="这里输入联系单位名称" title="联系单位名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">联系单位地址:</td>
								<td><input type="text" name="ORGANIZATION_ADDRESS" id="ORGANIZATION_ADDRESS" value="${pd.ORGANIZATION_ADDRESS}" maxlength="255" placeholder="这里输入联系单位地址" title="联系单位地址" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">联系单位邮编:</td>
								<td><input type="text" name="ORGANIZATION_POSTCODE" id="ORGANIZATION_POSTCODE" value="${pd.ORGANIZATION_POSTCODE}" maxlength="255" placeholder="这里输入联系单位邮编" title="联系单位邮编" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">联系单位电话:</td>
								<td><input type="text" name="ORGANIZATION_PHONE" id="ORGANIZATION_PHONE" value="${pd.ORGANIZATION_PHONE}" maxlength="255" placeholder="这里输入联系单位电话" title="联系单位电话" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">联系人:</td>
								<td><input type="text" name="ORGANIZATION_LINKMAN" id="ORGANIZATION_LINKMAN" value="${pd.ORGANIZATION_LINKMAN}" maxlength="255" placeholder="这里输入联系人" title="联系人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">奖励来源:</td>
								<td><input type="text" name="SOURCE" id="SOURCE" value="${pd.SOURCE}" maxlength="255" placeholder="这里输入奖励来源" title="奖励来源" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">奖励发布单位:</td>
								<td><input type="text" name="DATA_PUBLIC_NAME" id="DATA_PUBLIC_NAME" value="${pd.DATA_PUBLIC_NAME}" maxlength="255" placeholder="这里输入奖励发布单位" title="奖励发布单位" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">获奖时间:</td>
								<td><input type="text" name="DATA_PUBLIC_DATE" id="DATA_PUBLIC_DATE" value="${pd.DATA_PUBLIC_DATE}" maxlength="255" placeholder="这里输入获奖时间" title="获奖时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">关键字:</td>
								<td><input type="text" name="KEYWORDS" id="KEYWORDS" value="${pd.KEYWORDS}" maxlength="255" placeholder="这里输入关键字" title="关键字" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">认证时间:</td>
								<td><input type="text" name="IDENTIFY_DATE" id="IDENTIFY_DATE" value="${pd.IDENTIFY_DATE}" maxlength="255" placeholder="这里输入认证时间" title="认证时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">认证单位:</td>
								<td><input type="text" name="IDENTIFY_ORG" id="IDENTIFY_ORG" value="${pd.IDENTIFY_ORG}" maxlength="255" placeholder="这里输入认证单位" title="认证单位" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">注册标志:</td>
								<td><input type="text" name="REGISTRATION_MARK" id="REGISTRATION_MARK" value="${pd.REGISTRATION_MARK}" maxlength="255" placeholder="这里输入注册标志" title="注册标志" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">公共机构:</td>
								<td><input type="text" name="PUBLIC_ORGANIZATION" id="PUBLIC_ORGANIZATION" value="${pd.PUBLIC_ORGANIZATION}" maxlength="255" placeholder="这里输入公共机构" title="公共机构" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">发布时间:</td>
								<td><input type="text" name="PUBLIC_DATE" id="PUBLIC_DATE" value="${pd.PUBLIC_DATE}" maxlength="255" placeholder="这里输入发布时间" title="发布时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td><input class="span10 date-picker" name="CREATE_TIME" id="CREATE_TIME" value="${pd.CREATE_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="创建时间" title="创建时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">点击量:</td>
								<td><input type="number" name="HIT" id="HIT" value="${pd.HIT}" maxlength="32" placeholder="这里输入点击量" title="点击量" style="width:98%;"/></td>
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
		            msg:'请输入标题',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TITLE").focus();
			return false;
			}
			if($("#COMPLETE_ORG").val()==""){
				$("#COMPLETE_ORG").tips({
					side:3,
		            msg:'请输入完成单位',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COMPLETE_ORG").focus();
			return false;
			}
			if($("#COMPLETE_PER").val()==""){
				$("#COMPLETE_PER").tips({
					side:3,
		            msg:'请输入完成人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COMPLETE_PER").focus();
			return false;
			}
			if($("#ENCOURAGEMENT_CLASS").val()==""){
				$("#ENCOURAGEMENT_CLASS").tips({
					side:3,
		            msg:'请输入奖励类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ENCOURAGEMENT_CLASS").focus();
			return false;
			}
			if($("#ENCOURAGEMENT_GRADE").val()==""){
				$("#ENCOURAGEMENT_GRADE").tips({
					side:3,
		            msg:'请输入奖励等级',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ENCOURAGEMENT_GRADE").focus();
			return false;
			}
			if($("#ENCOURAGEMENT_DATE").val()==""){
				$("#ENCOURAGEMENT_DATE").tips({
					side:3,
		            msg:'请输入获奖时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ENCOURAGEMENT_DATE").focus();
			return false;
			}
			if($("#ENCOURAGEMENT_LEVEL").val()==""){
				$("#ENCOURAGEMENT_LEVEL").tips({
					side:3,
		            msg:'请输入获奖级别',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ENCOURAGEMENT_LEVEL").focus();
			return false;
			}
			if($("#DESCRIPTION").val()==""){
				$("#DESCRIPTION").tips({
					side:3,
		            msg:'请输入描述',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DESCRIPTION").focus();
			return false;
			}
			if($("#ORGANIZATION_NAME").val()==""){
				$("#ORGANIZATION_NAME").tips({
					side:3,
		            msg:'请输入联系单位名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORGANIZATION_NAME").focus();
			return false;
			}
			if($("#ORGANIZATION_ADDRESS").val()==""){
				$("#ORGANIZATION_ADDRESS").tips({
					side:3,
		            msg:'请输入联系单位地址',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORGANIZATION_ADDRESS").focus();
			return false;
			}
			if($("#ORGANIZATION_POSTCODE").val()==""){
				$("#ORGANIZATION_POSTCODE").tips({
					side:3,
		            msg:'请输入联系单位邮编',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORGANIZATION_POSTCODE").focus();
			return false;
			}
			if($("#ORGANIZATION_PHONE").val()==""){
				$("#ORGANIZATION_PHONE").tips({
					side:3,
		            msg:'请输入联系单位电话',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORGANIZATION_PHONE").focus();
			return false;
			}
			if($("#ORGANIZATION_LINKMAN").val()==""){
				$("#ORGANIZATION_LINKMAN").tips({
					side:3,
		            msg:'请输入联系人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORGANIZATION_LINKMAN").focus();
			return false;
			}
			if($("#SOURCE").val()==""){
				$("#SOURCE").tips({
					side:3,
		            msg:'请输入奖励来源',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SOURCE").focus();
			return false;
			}
			if($("#DATA_PUBLIC_NAME").val()==""){
				$("#DATA_PUBLIC_NAME").tips({
					side:3,
		            msg:'请输入奖励发布单位',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DATA_PUBLIC_NAME").focus();
			return false;
			}
			if($("#DATA_PUBLIC_DATE").val()==""){
				$("#DATA_PUBLIC_DATE").tips({
					side:3,
		            msg:'请输入获奖时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DATA_PUBLIC_DATE").focus();
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
			if($("#IDENTIFY_DATE").val()==""){
				$("#IDENTIFY_DATE").tips({
					side:3,
		            msg:'请输入认证时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IDENTIFY_DATE").focus();
			return false;
			}
			if($("#IDENTIFY_ORG").val()==""){
				$("#IDENTIFY_ORG").tips({
					side:3,
		            msg:'请输入认证单位',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IDENTIFY_ORG").focus();
			return false;
			}
			if($("#REGISTRATION_MARK").val()==""){
				$("#REGISTRATION_MARK").tips({
					side:3,
		            msg:'请输入注册标志',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REGISTRATION_MARK").focus();
			return false;
			}
			if($("#PUBLIC_ORGANIZATION").val()==""){
				$("#PUBLIC_ORGANIZATION").tips({
					side:3,
		            msg:'请输入公共机构',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PUBLIC_ORGANIZATION").focus();
			return false;
			}
			if($("#PUBLIC_DATE").val()==""){
				$("#PUBLIC_DATE").tips({
					side:3,
		            msg:'请输入发布时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PUBLIC_DATE").focus();
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
		            msg:'请输入点击量',
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