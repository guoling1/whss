<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
  <style>
    {
      width: 100%;
      height: 100%;
      overflow: auto;
    }

    .main {
      padding-bottom: 20px;
    }

    .process {
      height: 47px;
      padding: 0 15px;
      background-color: #61729c;
    }

    .steps {
      float: left;
      width: 33.33%;
      height: 47px;
      line-height: 47px;
      font-size: 16px;
      color: #a7b0d3;
      text-align: center;
    }

    .steps span {
      display: inline-block;
      width: 16px;
      height: 16px;
      line-height: 16px;
      font-size: 11px;
      color: #a7b0d3;
      text-align: center;
      border: 1px solid #a7b0d3;
      border-radius: 50%;
      margin-right: 9px;
    }

    .steps.active {
      color: #FFF;
    }

    .steps.active span {
      background-color: #FFF;
      color: #61729c;
      border: none;
    }

    .top {
      width: 100%;
      height: auto;
      background-color: #FFF;
      padding-top: 15px;
    }

    .bottom {
      width: 100%;
      border-top: 8px solid #f4f5f8;
    }

    .group {
      height: 50px;
      line-height: 50px;
      border-bottom: 1px solid #edeef5;
      padding: 0 15px;
    }

    .group .name {
      display: inline-block;
      width: 50px;
      line-height: 50px;
      margin-right: 15px;
      font-size: 15px;
      color: #4a5171;
      text-align: left;
    }

    .group .file {
      width: 1px;
      line-height: 50px;
      text-align: left;
      font-size: 15px;
      color: #7079a0;
      position: relative;
    }

    .group .file div {
      width: 100%;
      height: 100%;
      opacity: 0;
      position: absolute;
      left: 0;
      top: 0;
    }

    .group .ipt {
      border: none;
      font-size: 16px;
      color: #4a5171;
    }

    .group ::-webkit-input-placeholder {
      color: #aab2d2;
    }

    .group :-moz-placeholder {
      color: #aab2d2;
    }

    .group ::-moz-placeholder {
      color: #aab2d2;
    }

    .group :-ms-input-placeholder {
      color: #aab2d2;
    }

    .group .btn {
      display: inline-block;
      float: right;
      line-height: 50px;
      font-size: 12px;
      color: #7079a0;
    }

    .next {
      padding: 26px 15px 0;
    }

    .next div {
      width: 100%;
      height: 50px;
      line-height: 50px;
      font-size: 16px;
      color: #FFF;
      text-align: center;
      background-color: #7086bd;
      border-radius: 5px;
    }

    .sample {
      display: none;
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
      z-index: 89;
    }

    .sample img {
      position: absolute;
      top: 45%;
      left: 50%;
      width: 90%;
      transform: translate3d(-50%, -50%, 0);
    }
  </style>
</head>
<body>

<div id="material">
  <div class="process">
    <div class="steps active">
      <span>1</span>填写资料
    </div>
    <div class="steps">
      <span>2</span>上传资料
    </div>
    <div class="steps">
      <span>3</span>开始使用
    </div>
  </div>
  <div class="main">
    <div class="top">
      <div class="group">
        <div class="name">店铺名</div>
        <input type="text" class="ipt" placeholder="例如某某小卖部" id="merchantName">
      </div>
      <div class="group">
        <div class="name">地址</div>
        <input type="text" class="ipt" placeholder="输入店铺地址" id="address">
      </div>
    </div>
    <div class="bottom">
      <div class="group">
        <div class="name">结算卡</div>
        <input type="number" class="ipt" placeholder="输入本人名下借记卡号" id="bankNo">
      </div>
      <div class="group">
        <div class="name">上传</div>
        <a href="javascript:void(0);" class="file" id="chooseImage">
          <span id="bankText">点击上传结算卡正面照</span>

          <div></div>
        </a>

        <div class="btn" id="sampleShow">查看示例</div>
      </div>
      <div class="group">
        <div class="name">开户名</div>
        <input type="text" class="ipt" placeholder="本人银行卡开户姓名" id="username">
      </div>
      <div class="group">
        <div class="name">身份证</div>
        <input type="text" class="ipt" placeholder="输入身份证号" id="identity">
      </div>
      <div class="group">
        <div class="name">手机号</div>
        <input type="number" class="ipt" placeholder="开户银行预留手机号" id="reserveMobile">

        <div class="btn" id="sendCode">发送验证码</div>
      </div>
      <div class="group">
        <div class="name">验证码</div>
        <input type="number" class="ipt" placeholder="输入短信验证码" id="code">
      </div>
      <div class="next" id="submit">
        <div>下一步</div>
      </div>
      <input type="hidden" id="bankPic"/>
    </div>
  </div>

  <div class="sample" id="sampleHide">
    <img src="http://static.jinkaimen.cn/hss/assets/card-sample.png" alt="">
  </div>
</div>
</div>
</body>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
  /*
   * 注意：
   * 1. 所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
   * 2. 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
   * 3. 常见问题及完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
   *
   * 开发中遇到问题详见文档“附录5-常见错误及解决办法”解决，如仍未能解决可通过以下渠道反馈：
   * 邮箱地址：weixin-open@qq.com
   * 邮件主题：【微信JS-SDK反馈】具体问题
   * 邮件内容说明：用简明的语言描述问题所在，并交代清楚遇到该问题的场景，可附上截屏图片，微信团队会尽快处理你的反馈。
   */
  wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: '${config.appid}', // 必填，公众号的唯一标识
    timestamp:${config.timestamp},// 必填，生成签名的时间戳
    nonceStr: '${config.nonceStr}', // 必填，生成签名的随机串
    signature: '${config.signature}',// 必填，签名，见附录1
    jsApiList: [
      'chooseImage',
      'uploadImage'
    ]
  });
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.3.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.1.4/material.min.js"></script>

</html>