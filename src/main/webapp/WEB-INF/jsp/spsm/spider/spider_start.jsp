<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-md-12" style="padding-top:20px; text-align: center">
							<!-- 检索  -->
							<form action="spider/start.do" method="post" name="Form" id="Form">
								<select id="seedUrl" name="seedUrl" style="width: 400px">
									<c:forEach items="${urlList}" var="url" varStatus="vs">
										<option value="${url.seed}">${ url.seed}</option>
									</c:forEach>
								</select>
								<!-- 数据库 -->
								<select id="DATABASETYPE_ID" name="DATABASETYPE_ID" onchange="getSecondSelect()">
									<option value="" >--请选择库--</option>
									<c:forEach items="${list}" var="r" varStatus="vs">
										<option value="${r.DATABASETYPE_ID}">${r.DATABASETYPENAME }</option>
									</c:forEach>
								</select> 
								<!-- 导航栏 -->
								<select id="NAVBARTYPE_ID" name="NAVBARTYPE_ID" onchange="getThirdSelect()">
										<option value="${r.NAVBARTYPE_ID}">${r.NAVBARTYPENAME }</option>
								</select>
								
								<!-- 列表 -->
								<select id="LISTTYPE_ID" name="LISTTYPE_ID" onchange="getForthSelect()">
										<option value="${r.LISTTYPE_ID}">${r.LISTTYPENAME }</option>
								</select> 
								<!-- 子列表 -->
								<select id="SUBLISTTYPE_ID" name="SUBLISTTYPE_ID">
										<option value="${r.SUBLISTTYPE_ID}">${r.SUBLISTTYPENAME }</option>
								</select> 
								
								<input type="submit" value="爬取">
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

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up"
			class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript">
		$(top.hangge());//关闭加载状态
      	//加载第二个下拉框的数据
		function getSecondSelect(){
			var rid = $("#DATABASETYPE_ID").val();
			if(null!=rid && rid!=""){
				$.get("spider/chooseNextSelect.do",{"DATABASETYPE_ID":rid,"flag":1},function(datas){
					$("#NAVBARTYPE_ID").html("<option value=\"\" >--请选择导航--</option>");
					if(datas.length<1){
						$("#NAVBARTYPE_ID").html("<option value=\"\" >--无--</option>");
					}
					for ( var i = 0; i < datas.length; i++) {
						var json = datas[i];
						//获取JSON对象的属性
						var username = json.NAVBARTYPE_ID;
						var name = json.NAVBARTYPENAME;
						var option = "<option value=\""+username+"\">"+name+"</option>";
						$("#NAVBARTYPE_ID").append(option);
					}
				},"json");
			}else{
				$("#NAVBARTYPE_ID").html("<option value=\"\" >--请选择导航--</option>");
				$("#LISTTYPE_ID").html("<option value=\"\" >--请选择列表--</option>");
				$("#SUBLISTTYPE_ID").html("<option value=\"\" >--请选择子列表--</option>");
			}
			
	}
		// 加载第三个下拉框的数据
		function getThirdSelect(){
			var rid = $("#NAVBARTYPE_ID").val();
			if(null!=rid && rid!=""){
				$.get("spider/chooseNextSelect.do",{"NAVBARTYPE_ID":rid,"flag":2},function(datas){
					$("#LISTTYPE_ID").html("<option value=\"\" >--请选择列表--</option>");
					if(datas.length<1){
						$("#LISTTYPE_ID").html("<option value=\"\" >--无--</option>");
					}
					for ( var i = 0; i < datas.length; i++) {
						var json = datas[i];
						//获取JSON对象的属性
						var username = json.LISTTYPE_ID;
						var name = json.LISTTYPENAME;
						var option = "<option value=\""+username+"\">"+name+"</option>";
						$("#LISTTYPE_ID").append(option);
					}
				},"json");
			}else{
				$("#LISTTYPE_ID").html("<option value=\"\" >--请选择列表--</option>");
				$("#SUBLISTTYPE_ID").html("<option value=\"\" >--请子列表--</option>");
			}
			
		}
		
		//加载弟四个下拉框的数据。
		function getForthSelect(){
			var rid = $("#LISTTYPE_ID").val();
			if(null!=rid && rid!=""){
				$.get("spider/chooseNextSelect.do",{"LISTTYPE_ID":rid,"flag":3},function(datas){
					$("#SUBLISTTYPE_ID").html("<option value=\"\" >--请选择子列表--</option>");
					if(datas.length<1){
						$("#SUBLISTTYPE_ID").html("<option value=\"\" >--无--</option>");
					}
					for ( var i = 0; i < datas.length; i++) {
						var json = datas[i];
						//获取JSON对象的属性
						var username = json.SUBLISTTYPE_ID;
						var name = json.SUBLISTTYPENAME;
						var option = "<option value=\""+username+"\">"+name+"</option>";
						$("#SUBLISTTYPE_ID").append(option);
					}
				},"json");
			}
			
		}
	
	</script>

</body>
</html>