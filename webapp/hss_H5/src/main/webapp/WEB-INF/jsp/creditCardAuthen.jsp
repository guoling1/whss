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
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.2.css">
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

    .layer {
      display: none;
      position: fixed;
      top: 0;
      left: 0;
      z-index: 99;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
    }

    .space {
      width: 95%;
      height: auto;
      border-radius: 3px;
      background-color: #FFF;
      position: absolute;
      top: 45%;
      left: 50%;
      padding: 0;
      transform: translate3d(-50%, -50%, 0);
      overflow: hidden;
    }

    .space-title {
      width: 100%;
      height: 65px;
      line-height: 65px;
      background-color: #f4f5f8;
      text-align: left;
      padding-left: 15px;
    }

    .xx {
      float: right;
      width: 65px;
      height: 65px;
      background: url("../assets/xx.png") no-repeat center;
      background-size: 18px 18px;
    }

    .space-cont {
      padding: 0 15px 10px;
    }

    .cont-detail {
      padding: 20px 0;
      line-height: 40px;
    }

    .operation {
      width: 100%;
      height: 50px;
    }

    .layer-submit {
      margin: auto;
      width: 45%;
      height: 50px;
      line-height: 50px;
      background-color: #7086bd;
      font-size: 16px;
      color: #FFF;
      border-radius: 5px;
    }
  </style>
</head>
<body>

<div id="branchInfo">
  <div class="ipt-space">
    <div class="name">信用卡号</div>
    <input id="ipt" class="ipt" type="text" placeholder="输入本人名下信用卡号">
  </div>

  <div class="small">
    部分通道的使用必须进行该认证
  </div>

  <div class="submit-space">
    <div id="submit" class="submit">确定</div>
  </div>

  <div class="message-space" id="layer">
    <div class="message-box">
      <div class="message-box-head">提示</div>
      <div class="message-box-body">
        该通道正在支付公司注册中<br>请明天再使用该通道
      </div>
      <div class="message-box-foot">
        <div class="message-enter" id="cancel">
          确定
        </div>
      </div>
    </div>
  </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.7.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.2/creditCardAuthen.min.js"></script>
</html>
