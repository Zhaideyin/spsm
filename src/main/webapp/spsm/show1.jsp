<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/static/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
 <style>
        *{  margin: 0px;
            padding: 0px;}
        .head{
            background-color:#272727;
            width: 100%;
            height: 50px;
             }
        .head p{
            color: #ffffff;
        }
        .logo{
            background-image: url("./img/head.png");
            background-repeat: no-repeat;
            width: 293px;
            height: 46px;
            position: relative;
            left: 100px;
            top:3px;
        }
        .first{
            position: absolute;
            left: 500px;
            top:15px;

        }
        .second{
            position: absolute;
            left: 650px;
            top:15px;


        }
        .third{
            position: absolute;
            left: 800px;
            top:15px;


        }
        .fourth{
            position: absolute;
            left: 1000px;
            top:15px;

        }
        .logo,.first,.second,.third,.fourth{
            height: 53px;
        }
         .main{
             background-color: azure;
            min-height: 820px;
             width: 100%;
        }
         .top{
             background-color: #12525e;
             width: 100%;
             height: 100px;
             position: absolute;


         }
        .foot{
            background-color: #878784;
            width: 100%;
            height: 80px;
        }
        .footLogo{
            background-image: url("./img/foot.png");
            position:relative ;
            left: 10px;
            top:10px;
            height: 57px;
            width: 59px;
        }
        .footText{
            position: relative;
            left: 79px;
            top:-40px;
            height: 57px;
        }
        .footText b{
            color: #ffffff;
        }
        .topFirst ,.topSecond{
           color: #ffffff;
        }
        .topFirst{
            position: relative;
            left:40px;
            top:20px;
        }
        .topSecond{
            font-size: 30px;
            position: relative;
            left:30px;
            top:30px;
        }
        .tcdPageCode{
            position: relative;
            left: 750px;

        }
        .tcdPageCode{padding: 15px 20px;text-align: left;color: #ccc;}
        .tcdPageCode a{display: inline-block;color: #428bca;display: inline-block;height: 25px;	line-height: 25px;	padding: 0 10px;border: 1px solid #ddd;	margin: 0 2px;border-radius: 4px;vertical-align: middle;}
        .tcdPageCode a:hover{text-decoration: none;border: 1px solid #428bca;}
        .tcdPageCode span.current{display: inline-block;height: 25px;line-height: 25px;padding: 0 10px;margin: 0 2px;color: #fff;background-color: #428bca;	border: 1px solid #428bca;border-radius: 4px;vertical-align: middle;}
        .tcdPageCode span.disabled{	display: inline-block;height: 25px;line-height: 25px;padding: 0 10px;margin: 0 2px;	color: #bfbfbf;background: #f2f2f2;border: 1px solid #bfbfbf;border-radius: 4px;vertical-align: middle;}

        .content{
            padding-top: 140px;
            padding-left: 40px;
            padding-right: 40px;
            padding-bottom: 40px;
            border-bottom-color: #272727;
            border-bottom-style: inset;
            border-bottom-width: 2px;

        }


    </style>
</head>
<body>
<div class="container">
<div class="head">
    <div class="logo">
    </div>
    <div class="first">
      <p>育种创新数据库</p>
    </div>
    <div class="second">
        <p>种业产业数据库</p>
    </div>
    <div class="third">
        <p >成果推广与转化数据库</p>
    </div>
    <div class="fourth">
        <p>育种专家智库</p>
    </div>
</div>
<div class="main">
<div class="top">
    <p class="topFirst">首页/育种创新数据库 Breeding Innovation Database/育种知识服务 Breeding Research Service </p>
    <p class="topSecond">育种知识服务</p>
</div>
    <div class="content">
    </div>
    <div class="tcdPageCode">
        
    </div>

</div>
<div class="foot">
    <div class="footLogo">
    </div>
    <div class="footText">
        <b>网站地图</b>&nbsp;&nbsp;<b>关于我们</b>&nbsp;&nbsp;<b>友情链接</b>&nbsp;&nbsp;<b>四川省农研所</b><br>
        <b>四川省农研所版权所有，四川省农业科学研究所科研网编辑部维护，电子信箱：news@sichuan.edu.cn</b><br>
        <b> Copyright 2001-2016 news.sichuan.edu.cn All right reserved</b>
    </div>
</div>
</div>
<script type="text/javascript" src="./js/jquery-3.1.1.min.js"></script>
<script src="http://www.lanrenzhijia.com/ajaxjs/jquery.min.js"></script>
<script src="http://www.lanrenzhijia.com/ajaxjs/jquery.page.js"></script>
<script>
    var str = "";
    var totalPage=0;
    var pageNum=1;
    
    $.ajax({
        type: "get",
        url: "localhost:8080/api/spider/list",
        data: "page=1&count=3",
        dataType:"json",
        success: function(msg){
            totalPage=msg.totalPage;
            $(".tcdPageCode").createPage({
                pageCount:totalPage,
                current:pageNum,
                backFn:function(p){
                        pageNum=p;
                    $.ajax({
                        type: "get",
                        url:"localhost:8080/spsm/api/spider/list",
                        data: "page="+pageNum+"&count=3",
                        dataType:"json",
                        success:function (msg) {
                            str="";
                            for (var i =0;i<msg.data.length;i++){
                                str += msg.data[i].CONTENT+"<hr>"
                            }
                            $(".content").html(str)
                        }
                    })

                }
            });
            for (var i =0;i<msg.data.length;i++){
                str += msg.data[i].CONTENT
            }
            $(".content").html(str)+"<hr>"
        }
    });

    
</script>
</body>
</html>