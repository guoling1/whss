<%--
  Created by IntelliJ IDEA.
  User: Thinkpad
  Date: 2017/7/7
  Time: 11:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>填写支行</title>
    <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.21.css">--%>
    <link rel="stylesheet" href="../css/hss/style.2.2.22.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="writeBranch">
    <div class="top">
        <input id="match_ipt" type="text" class="inp" placeholder="输入支行关键字">
        <span id="search">搜索</span>
    </div>
    <div class="tip">如无法选择准确的网点，建议尝试选择同城第一个支行</div>
    <div class="weui-cells" style="margin-top: -1px" id="cellList">
        <%--<div class="weui-cell" id="layer-b-list">
            <div class="weui-cell__bd">
                <p>标题文字标题文字标题文字标题文字标题文字标题文字标题文字</p>
            </div>
        </div>--%>
    </div>
    <div></div>
</div>
</body>
<script>
    var pageData = {
        bin: '${bankBin}',
        provinceCode: '${provinceCode}',
        provinceName: '${provinceName}',
        cityCode: '${cityCode}',
        cityName: '${cityName}',
        countyCode: '${countyCode}',
        countyName: '${countyName}',
        branchCode: '${branchCode}',
        branchName: '${branchName}',
        oemNo:'${oemNo}'
    }
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js"></script>
<%--<script src="http://static.jinkaimen.cn/hss/2.2.32/bankBranch.min.js"></script>--%>
<script src="../js/hss/2.2.32/branch.min.js"></script>
</html>