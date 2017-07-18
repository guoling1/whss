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
<%--<div id="branchInfo">
    <div class="branch">
        <div class="list">
            <div class="name">开户行地区</div>
            <input id="world" class="ipt" type="text" placeholder="点击选择" readonly>

            <div class="layer" id="layer-w">
                <div class="name">开户行地区</div>
                <div class="search top">
                    <div class="search-result">
                        <div id="p" style="display:none;">请选择</div>
                        <div id="c" style="display:none;">请选择</div>
                        <div id="ct" style="display:none;">请选择</div>
                    </div>
                    <div class="search-list" id="layer-w-list">
                        &lt;%&ndash; 动态添加 &ndash;%&gt;
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>--%>
<div id="branch1">
    <div class="branch">
        <div class="list">
            <div class="name">开户行地区</div>
            <input id="world" class="ipt" type="text" placeholder="点击选择" readonly>

            <div class="layer" id="layer-w">
                <div class="name">开户行地区</div>
                <div class="search top">
                    <div class="search-result">
                        <div id="p" style="display:none;">请选择</div>
                        <div id="c" style="display:none;">请选择</div>
                        <div id="ct" style="display:none;">请选择</div>
                    </div>
                    <div class="search-list" id="layer-w-list">
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
            <div id="submit" class="submit">下一步</div>
        </div>
    </div>
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
<script src="../js/hss/2.2.32/district.min.js"></script>
</html>