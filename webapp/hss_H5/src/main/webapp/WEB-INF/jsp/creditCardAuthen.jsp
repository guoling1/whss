<%--
  Created by IntelliJ IDEA.
  User: Thinkpad
  Date: 2016/12/29
  Time: 21:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.1.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
  <style>
    #branchInfo {
      width: 100%;
      height: 100%;
      background-color: #f4f5f8;
      overflow: hidden;
    }

    .ipt-space {
      width: 100%;
      height: 50px;
      line-height: 50px;
      background-color: #FFF;
      border-bottom: 1px solid #eaecf3;
      padding: 0 15px;
      margin: 8px 0;
    }

    .name {
      float: left;
      display: inline-block;
      font-size: 15px;
      color: #4a5171;
      margin-right: 20px;
    }

    .ipt {
      height: 49px;
      line-height: 49px;
      float: left;
      font-size: 15px;
      color: #aab2d2;
    }

    .small {
      text-align: left;
      font-size: 12px;
      color: #aab0c6;
      padding-left: 15px;
    }

    .submit-space {
      width: 100%;
      margin-top: 20px;
      padding: 0 15px;
    }

    .submit {
      width: 100%;
      height: 45px;
      line-height: 45px;
      font-size: 16px;
      color: #FFF;
      border-radius: 3px;
      background-color: #4ecea2;
    }
  </style>
</head>
<body>

<div id="branchInfo">
  <div class="ipt-space">
    <div class="name">信用卡号</div>
    <input class="ipt" type="text" placeholder="输入本人名下信用卡号">
  </div>

  <div class="small">
    部分通道的使用必须进行该认证
  </div>

  <div class="submit-space">
    <div class="submit">确定</div>
  </div>

</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.0.1/common.min.js"></script>
</html>
