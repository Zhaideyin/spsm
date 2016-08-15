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

						<form action="processDefinition/newDeployment.do" name="Form" id="Form" method="post" enctype="multipart/form-data">

							<div id="zhongxin" style="padding-top: 13px;">
								<table id="table_report" class="table table-striped table-bordered table-hover">
									<tr>
										<td style="width:175px;text-align: right;padding-top: 13px;">流程规则名称:</td>
										<td><input type="text" name="processName" id="processName" value="${pd.processName}" maxlength="255" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
									</tr>
									<tr>
										<td style="width:175px;text-align: right;padding-top: 13px;">流程规则文件:<p>(zip格式，包含bpmn和png文件)</p></td>
										<td><input type="file" name="processFile" id="processFile" value="${pd.processFile}"  type="text"  placeholder="上传规则文件" title="文件" style="width:98%;"/></td>
									</tr>

									<tr>
										<td style="text-align: center;" colspan="10">
											<a class="btn btn-mini btn-primary" onclick="save();">部署</a>
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


<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript">
	$(top.hangge());
	//保存
	function save(){
		if($("#processName").val()==""){
			$("#processName").tips({
				side:3,
				msg:'请输入规则名称',
				bg:'#AE81FF',
				time:2
			});
			$("#processName").focus();
			return false;
		}
		if($("#processFile").val()==""){
			$("#processFile").tips({
				side:3,
				msg:'请上传规则文件',
				bg:'#AE81FF',
				time:2
			});
			$("#processFile").focus();
			return false;
		}

		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();

	}


</script>
<script type="text/javascript" src="plugins/webuploader/webuploader.js"></script>
<script type="text/javascript" src="domoplugins/uploader.js"></script>
<script>
	$(".filepicker").each(function(index){$(this).imguploader({id:"filepicker"+index})});
</script>

<script type="text/javascript">
	$(top.hangge());	//关闭加载状态
</script>
</body>
</html>