<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Demotest</title>
</head>
<body>
<h1>一般</h1>
<div id="container" style="height: 400px;width: 400px"></div>
<script src="/spsm/static/js/myjs/js2wordcloud.js"></script>
<script>
    var option = {
        imageShape: '/spsm/static/images/种植技术.png',
        tooltip: {
            show: true,
            formatter: function(item) {
                return item[0] + ': 价值¥' + item[1] + '<br>' + '词云图'
            }
        },
        list: ${pd.keyWordMap},
        color: '#FA4259',
        shape: 'circle',
        ellipticity: 1
    }
    var wc = new Js2WordCloud(document.getElementById('container'))
    wc.showLoading({
        backgroundColor: '#fff',
        text: '看见了福建省的附件里是卡洛斯的家乐福就爱上了克拉家乐福及爱丽丝垃圾堆里发简历到复健科了会计师两地分居阿里拉客积分的垃圾费浪费据了解乐山大佛',
        effect: 'spin'
    })
    setTimeout(function() {
        wc.hideLoading()
        wc.setOption(option)
    }, 2000)

</script>
</body>
</html>