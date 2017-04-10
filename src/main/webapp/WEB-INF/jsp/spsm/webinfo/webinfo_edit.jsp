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
								<td style="width:75px;text-align: right;padding-top: 13px;">url所在标签:</td>
								<td><input type="text" name="URLTAG" id="URLTAG" value="${pd.URLTAG}" maxlength="255" placeholder="这里输入url所在标签" title="url所在标签" style="width:98%;"/></td>
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
								<td>
									<select name="HASIMG" id="HASIMG">
										<option name="false" value="0" <c:if test="${pd.HASIMG==0}"> selected</c:if>>false</option>
										<option name="true" value="1" <c:if test="${pd.HASIMG==1}"> selected</c:if>>true</option>
									</select>
								</td>
								<%--<td><input type="text" name="HASIMG" id="HASIMG" value="${pd.HASIMG}" maxlength="255" placeholder="这里输入是否含图片，输入‘true’ or ‘false’" title="是否含图片" style="width:98%;"/></td>--%>
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
								<td>

									<select name="HASDOC" id="HASDOC">
										<option name="false" value="0" <c:if test="${pd.HASDOC==0}">selected</c:if>>false</option>
										<option name="true" value="1" <c:if test="${pd.HASDOC==1}">selected</c:if>>true</option>
									</select>
								</td>
								<%--<td><input type="text" name="HASDOC" id="HASDOC" value="${pd.HASDOC}" maxlength="255" placeholder="这里输入是否含有文件" title="是否含有文件" style="width:98%;"/></td>--%>
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
								<td><input type="text" name="TOTALPAGE" id="TOTALPAGE" value="${pd.TOTALPAGE}" maxlength="255" placeholder="这里输入网站总页数所在的xpath" title="网站总页数" style="width:98%;"/></td>
							</tr>
							<%--<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">ajax分页标签:</td>
								<td><input type="text" name="PAGEAJAXTAG" id="PAGEAJAXTAG" value="${pd.PAGEAJAXTAG}" maxlength="255" placeholder="这里输入网站分页是ajax分页设置" title="网站分页是ajax分页设置" style="width:98%;"/></td>
							</tr>--%>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">get分页标签:</td>
								<td><input type="text" name="PAGEGETTAG" id="PAGEGETTAG" value="${pd.PAGEGETTAG}" maxlength="255" placeholder="这里输入网站分页是get的" title="网站分页是get的" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">网站分页类型:</td>
								<td>
									<select name="PAGEMETHOD" id="PAGEMETHOD">
										<option name="get" value="get">get</option>
										<option name="post" value="post">post</option>
										<option name="other" value="other">other</option>
									</select>
								</td>
								<%--<td><input type="text" name="PAGEMETHOD" id="PAGEMETHOD" value="${pd.PAGEMETHOD}" maxlength="255" placeholder="这里输入网站分页类型" title="网站分页类型" style="width:98%;"/></td>--%>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">网页编码格式:</td>
								<td>
									<input type="text" name="PAGEENCODING" id="PAGEENCODING" value="${pd.PAGEENCODING}" maxlength="255" placeholder="这里输入网页编码格式,如：utf-8或gb2312等" title="网页编码格式" style="width:98%;"/></td>
							</tr>

							<tr><td>库类型</td>
								<td>
									<select id="DATABASETYPE_ID" name="DATABASETYPEID" onchange="getSecondSelect()">

										<c:forEach items="${pd.datatypeList}" var="r" varStatus="vs">
											<option value="${r.DATABASETYPE_ID}" <c:if test="${r.DATABASETYPE_ID == pd.DATABASETYPEID}">selected</c:if>>${r.DATABASETYPENAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>

							<tr><td>导航</td>
								<td>
									<select id="NAVBARTYPE_ID" name="NAVBARTYPEID"  onchange="getThirdSelect()">

										<c:forEach items="${pd.navbartypeList}" var="r" varStatus="vs">
												<option value="${r.NAVBARTYPE_ID}" <c:if test="${r.NAVBARTYPE_ID == pd.NAVBARTYPEID}">selected</c:if>>${r.NAVBARTYPENAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>

							<tr><td>列表</td>
								<td>
									<select id="LISTTYPE_ID" name="LISTTYPEID" onchange="getForthSelect()">
										<c:forEach items="${pd.listypeList}" var="r" varStatus="vs">
											<option value="${r.LISTTYPE_ID}" <c:if test="${r.LISTTYPE_ID == pd.LISTTYPEID}">selected</c:if>>${r.LISTTYPENAME }</option>
										</c:forEach>
									</select>

								</td>
							</tr>

							<tr><td>子列表</td>
								<td><select id="SUBLISTTYPE_ID" name="SUBLISTTYPEID"><option value="" >
									<c:forEach items="${pd.sublistypeList}" var="r" varStatus="vs">
										<option value="${r.SUBLISTTYPE_ID}" <c:if test="${r.SUBLISTTYPE_ID == pd.SUBLISTTYPEID}">selected</c:if>>${r.SUBLISTTYPENAME }</option>
									</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">爬取时间:</td>
								<td><input class="span10 date-picker" name="CREATETIME" id="CREATETIME" value="${pd.CREATETIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="添加时间" title="添加时间" style="width:98%;"/></td>
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

			/*if(($("#HASIMG").val()=="true" && $("#IMGREGEX").val()==""){
				$("#IMGREGEX").tips({
					side:3,
		            msg:'请输入图片过滤正则表达式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IMGREGEX").focus();
			return false;
			}*/
			/*if(($("#HASIMG").val()=="true" && $("#IMGTAG").val()==""){
				$("#IMGTAG").tips({
					side:3,
		            msg:'请输入图片标签',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IMGTAG").focus();
			return false;
			}*/
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
			/*if(($("#HASDOC").val()=="true"  && $("#DOCREGEX").val()==""){
				$("#DOCREGEX").tips({
					side:3,
		            msg:'请输入文件过滤正则表达式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DOCREGEX").focus();
			return false;
			}*/
			/*if(($("#HASDOC").val()=="true"  && $("#DOCTAG").val()==""){
				$("#DOCTAG").tips({
					side:3,
		            msg:'请输入文件标签',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DOCTAG").focus();
			return false;
			}*/
			/*if($("#TOTALPAGE").val()==""){
				$("#TOTALPAGE").tips({
					side:3,
		            msg:'请输入网站总页数,如只有一页，请输入1',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TOTALPAGE").focus();
			return false;
			}*/
			/*if($("#PAGEAJAXTAG").val()==""){
				$("#PAGEAJAXTAG").tips({
					side:3,
		            msg:'请输入网站分页是ajax分页设置',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PAGEAJAXTAG").focus();
			return false;
			}*/
			/*if($("#PAGEGETTAG").val()==""){
				$("#PAGEGETTAG").tips({
					side:3,
		            msg:'请输入网站分页是get的',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PAGEGETTAG").focus();
			return false;
			}*/
			/*if($("#PAGEMETHOD").val()==""){
				$("#PAGEMETHOD").tips({
					side:3,
		            msg:'请输入网站分页类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PAGEMETHOD").focus();
			return false;
			}*/
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