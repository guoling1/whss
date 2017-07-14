<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>Title</title>
    <link rel="stylesheet" href="../../css/style.1.0.0.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="record">
    <div class="weui-cells" id="weui-cells">
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <p id="title">会员卡充值</p>
                <p id="date" class="time">2017-02-14 16：38</p>
            </div>
            <div id="context" class="weui-cell__ft">￥<span class="price">55.21</span></div>
        </div>
    </div>
</div>
<script src="http://static.jinkaimen.cn/hsy/js/vendor.2.0.1.3.min.js"></script>
<script>
    const message = _require('message');
    const http = _require('http');
    const tools = _require('tools');
    // 定义ajax事件
    let query = tools.GetUrlArg();
    let title = document.getElementById('title');
    let date = document.getElementById('date');
    let context = document.getElementById('context');
    let pathName = window.location.pathname;
    let search = window.location.search;
    let weuiCell = document.getElementById('weui-cells');
    http.get(pathName, {
        id: query.noticeId
    }, function (data) {

        for (let i = 0; i < data.list.length; i++) {
            weuiCell.append(`
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <p id="title">${data.list[i].shopname}</p>
                        <p id="date" class="time">${data.list[i].createTime}</p>
                    </div>
                    <div id="context" class="weui-cell__ft">￥<span class="price">${data.list[i].amount}</span></div>
                </div>`)
        }
    });
</script>
</body>
</html>
