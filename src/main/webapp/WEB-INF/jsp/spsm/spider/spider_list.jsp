<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
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
						<div class="col-xs-12">
						<!-- 检索  -->
						<form action="spider/list.do" method="post" name="Form" id="Form">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="nav-search-input" id="nav-search-input" autocomplete="off" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
								</td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="lastStart" id="lastStart"  value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="开始日期"/></td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="lastEnd" name="lastEnd"  value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="结束日期" title="结束日期"/></td>
								<td style="vertical-align:top;padding-left:2px;">

								 <!-- 数据库 -->
								<select id="DATABASETYPE_ID" name="DATABASETYPE_ID" onchange="getSecondSelect()">
									<option value="" >--请选择库--</option>
									<c:forEach items="${pd.databasetype}" var="r" varStatus="vs">
										<option value="${r.DATABASETYPE_ID}" <c:if test="${r.DATABASETYPE_ID == pd.DATABASETYPE_ID}">selected</c:if>>${r.DATABASETYPENAME }</option>
									</c:forEach>
								</select> 
								<!-- 导航栏 -->
								<select id="NAVBARTYPE_ID" name="NAVBARTYPE_ID"  onchange="getThirdSelect()">
									<option value="" >--请选择库--</option>
									<c:forEach items="${pd.navbartype}" var="r" varStatus="vs">
										<option value="${r.NAVBARTYPE_ID}" <c:if test="${r.NAVBARTYPE_ID == pd.NAVBARTYPE_ID}">selected</c:if>>${r.NAVBARTYPENAME }</option>
									</c:forEach>
										
								</select>
								
								<!-- 列表 -->
								<select id="LISTTYPE_ID" name="LISTTYPE_ID" onchange="getForthSelect()">
									<option value="" >--请选择库--</option>
									<c:forEach items="${pd.listtype}" var="r" varStatus="vs">
										<option value="${r.LISTTYPE_ID}" <c:if test="${r.LISTTYPE_ID == pd.LISTTYPE_ID}">selected</c:if>>${r.LISTTYPENAME }</option>
									</c:forEach>
								</select> 
								<!-- 子列表 -->
								<select id="SUBLISTTYPE_ID" name="SUBLISTTYPE_ID"><option value="" >--请选择库--</option>
									<c:forEach items="${pd.sublisttype}" var="r" varStatus="vs">
										<option value="${r.SUBLISTTYPE_ID}" <c:if test="${r.SUBLISTTYPE_ID == pd.SUBLISTTYPE_ID}">selected</c:if>>${r.SUBLISTTYPENAME }</option>
									</c:forEach>
								</select> 
								
								</td>
								<c:if test="${QX.cha == 1 }">
								<td style="vertical-align:top;padding-left:2px"><a class="btn btn-light btn-xs" onclick="tosearch();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								</c:if>
								<c:if test="${QX.toExcel == 1 }"><td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i></a></td></c:if>
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;table-layout:fixed;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width: 50px;">序号</th>
									<th class="center" style="width: 10%;">标题</th>
									<th class="center" style="width: 55%;">内容</th>
									<th class="center" style="width: 10%;">类型</th>
									<th class="center" style="width: 10%;">爬取时间</th>
									<th class="center" style="width: 10%;">操作</th>
								</tr>
							</thead>
													
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty varList}">
									<c:if test="${QX.cha == 1 }">
									<c:forEach items="${varList}" var="var" varStatus="vs">
										<tr>
											<td class='center'>
												<label class="pos-rel"><input type='checkbox' name='ids' value="${var.SPIDER_ID}" class="ace" /><span class="lbl"></span></label>
											</td>
											<td class='center' >${vs.index+1}</td>
											<td class='center'>${var.TITLE}</td>
											<td class='center'style="width: auto ;word-wrap:break-word;">${var.CONTENT}</td>
											<td class='center'>
												<!-- 选择最小的类型作为显示-->
												<c:choose>
													<c:when test="${var.SUBLISTTYPENAME!=null}">
														${var.SUBLISTTYPENAME }
													</c:when>
													<c:when test="${var.LISTTYPENAME!=null}">
														${var.LISTTYPENAME }
													</c:when>
													<c:when test="${var.NAVBARTYPENAME!=null }">
														${var.NAVBARTYPENAME }
													</c:when>
													<c:otherwise>
														${var.DATABASETYPENAME }
													</c:otherwise>
												</c:choose>
											</td>
											<%-- <td class='center'>${var.PUBLISH_TIME}</td> --%>
											<td class='center' >${var.CREATE_TIME}</td>
											<td class="center" >
												<c:if test="${QX.edit != 1 && QX.del != 1 }">
												<span class="label label-large label-grey arrowed-in-right arrowed-in"><i class="ace-icon fa fa-lock" title="无权限"></i></span>
												</c:if>
												<div class="hidden-sm hidden-xs btn-group">
													<c:if test="${QX.edit == 1 }">
													<a class="btn btn-xs btn-success" title="编辑" onclick="edit('${var.SPIDER_ID}');">
														<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
													</a>
													</c:if>
													<c:if test="${QX.del == 1 }">
													<a class="btn btn-xs btn-danger" onclick="del('${var.SPIDER_ID}');">
														<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
													</a>
													</c:if>
												</div>
												<div class="hidden-md hidden-lg">
													<div class="inline pos-rel">
														<button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
															<i class="ace-icon fa fa-cog icon-only bigger-110"></i>
														</button>
			
														<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
															<c:if test="${QX.edit == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="edit('${var.SPIDER_ID}');" class="tooltip-success" data-rel="tooltip" title="修改">
																	<span class="green">
																		<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
															<c:if test="${QX.del == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="del('${var.SPIDER_ID}');" class="tooltip-error" data-rel="tooltip" title="删除">
																	<span class="red">
																		<i class="ace-icon fa fa-trash-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
														</ul>
													</div>
												</div>
											</td>
										</tr>
									
									</c:forEach>
									</c:if>
									<c:if test="${QX.cha == 0 }">
										<tr>
											<td colspan="100" class="center">您无权查看</td>
										</tr>
									</c:if>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="100" class="center" >没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						<div class="page-header position-relative">
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;">
									<c:if test="${QX.add == 1 }">
									<a class="btn btn-sm btn-success" onclick="add();">新增</a>
									</c:if>
									<c:if test="${QX.del == 1 }">
									<a class="btn btn-sm btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" title="批量删除" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a>
									</c:if>
								</td>
								<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
							</tr>
						</table>
						</div>
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
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
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
		//检索
		function tosearch(){
			top.jzts();
			$("#Form").submit();
		}
		$(function() {
		
			//日期框
			$('.date-picker').datepicker({
				autoclose: true,
				todayHighlight: true
			});
			
			//下拉框
			if(!ace.vars['touch']) {
				$('.chosen-select').chosen({allow_single_deselect:true}); 
				$(window)
				.off('resize.chosen')
				.on('resize.chosen', function() {
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				}).trigger('resize.chosen');
				$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
					if(event_name != 'sidebar_collapsed') return;
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				});
				$('#chosen-multiple-style .btn').on('click', function(e){
					var target = $(this).find('input[type=radio]');
					var which = parseInt(target.val());
					if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
					 else $('#form-field-select-4').removeClass('tag-input-style');
				});
			}
			
			
			//复选框全选控制
			var active_class = 'active';
			$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
				var th_checked = this.checked;//checkbox inside "TH" table header
				$(this).closest('table').find('tbody > tr').each(function(){
					var row = this;
					if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
					else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
				});
			});
		});
		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>spider/goAdd.do';
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location=self.location",100);
					 }else{
						 nextPage(${page.currentPage});
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//删除
		function del(Id){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>spider/delete.do?SPIDER_ID="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
		
		//修改
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>spider/goEdit.do?SPIDER_ID='+Id;
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//批量操作
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
					  if(document.getElementsByName('ids')[i].checked){
					  	if(str=='') str += document.getElementsByName('ids')[i].value;
					  	else str += ',' + document.getElementsByName('ids')[i].value;
					  }
					}
					if(str==''){
						bootbox.dialog({
							message: "<span class='bigger-110'>您没有选择任何内容!</span>",
							buttons: 			
							{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
						});
						$("#zcheckbox").tips({
							side:1,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;
					}else{
						if(msg == '确定要删除选中的数据吗?'){
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>spider/deleteAll.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									 $.each(data.list, function(i, list){
											nextPage(${page.currentPage});
									 });
								}
							});
						}
					}
				}
			});
		};
		
		//导出excel
		function toExcel(){
			window.location.href='<%=basePath%>spider/excel.do';
		}
		
		<%-- 
		function select1() {
			var rid = $("#DATABASETYPE_ID").val();
			$.ajax(
			{
				type: "post",
				url: "<%=basePath%>spider/chooseNextSelect.do",
				data: {"DATABASETYPE_ID":rid,"flag":1},
				success: function (data) {
					var obj = $.parseJSON(data);
					for (var i = 0; i < obj.length; i++) {
						if(proid==obj[i].id){
							$("#NAVBARTYPE_ID").append("<option selected value=" + obj[i].NAVBARTYPE_ID +">" + obj[i].NAVBARTYPENAME + "</option>");
							continue;
						}
						$("#NAVBARTYPE_ID").append("<option value=" + obj[i].NAVBARTYPE_ID + ">" + obj[i].NAVBARTYPENAME + "</option>");
					}
				
				}
			})
		}; --%>
		
		
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
						var option ="<option value=\""+username+"\">"+name+"</option>";
						$("#SUBLISTTYPE_ID").append(option);
					}
				},"json");
			}
			
		} 
		
		
	/* 	$(function () {
			$('#DATABASETYPE_ID').bind("change", select1);
			
		}); */
	
		</script>


</body>
</html>