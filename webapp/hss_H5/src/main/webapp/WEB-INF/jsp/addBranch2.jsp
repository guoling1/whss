<%--
  Created by IntelliJ IDEA.
  User: Thinkpad
  Date: 2017/7/7
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>添加开户行</title>
    <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.23.css">
    <%--<link rel="stylesheet" href="../css/hss/style.2.2.22.css">--%>
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="branch1">
    <div class="branch">
        <div class="list">
            <div class="name">支行全称</div>
            <input id="branch" class="ipt" type="text" placeholder="输入开户支行全称">

            <div class="layer" id="layer-b">
                <div class="match">
                    <div class="match_name">支行名称</div>
                    <input id="match_ipt" class="match_ipt" type="text" placeholder="输入开户支行全称">
                </div>
                <div class="search">
                    <div class="search-list" id="layer-b-list">
                        <!--动态添加-->
                    </div>
                </div>
            </div>
        </div>
        <div class="small">
            支行信息填写错误可能会导致结算失败<br>
            如不确定，请联系您的银行查询
        </div>
        <div class="submit-space">
            <div id="submit" class="submit">确定</div>
        </div>
    </div>
</div>

</body>
<script>
    var pageData = {
        oemNo: '${oemNo}',
        source: '${source}'
    }
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.12.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.33/addBranch2.min.js"></script>
<%--<script src="../js/hss/2.2.32/addBranch2.min.js"></script>--%>
</html>