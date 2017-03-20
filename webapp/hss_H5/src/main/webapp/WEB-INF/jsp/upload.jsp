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
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.2.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="upload" class="upload">
  <div class="process">
    <div class="steps">
      <span class="right"></span>填写资料
    </div>
    <div class="steps active">
      <span>2</span>上传资料
    </div>
    <div class="steps">
      <span>3</span>开始使用
    </div>
  </div>
  <div class="main flexBox flex-box-column">
    <div class="space">
      <div class="left">
        <a href="javascript:void(0);" class="file" id="identityFacePic">
          <div class="file-title">身份证正面照片</div>
          <div class="file-logo">
            <img id="identityFacePic_src">
          </div>
          <div class="btn"></div>
        </a>
      </div>
      <div class="right">
        <div class="sample-title">示例</div>
        <div class="sample-logo"><img src="http://static.jinkaimen.cn/hss/assets/sample1.png"></div>
      </div>
    </div>
    <div class="space">
      <div class="left">
        <a href="javascript:void(0);" class="file" id="identityOppositePic">
          <div class="file-title">身份证背面照片</div>
          <div class="file-logo">
            <img src="" id="identityOppositePic_src">
          </div>
          <div class="btn"></div>
        </a>
      </div>
      <div class="right">
        <div class="sample-title">示例</div>
        <div class="sample-logo"><img src="http://static.jinkaimen.cn/hss/assets/sample2.png"></div>
      </div>
    </div>
    <div class="space">
      <div class="left">
        <a href="javascript:void(0);" class="file" id="identityHandPic">
          <div class="file-title">手持身份证正面照片</div>
          <div class="file-logo">
            <img id="identityHandPic_src">
          </div>
          <div class="btn"></div>
        </a>
      </div>
      <div class="right">
        <div class="sample-title">示例</div>
        <div class="sample-logo"><img src="http://static.jinkaimen.cn/hss/assets/sample3.png"></div>
      </div>
    </div>
    <div class="space">
      <div class="left">
        <a href="javascript:void(0);" class="file" id="bankHandPic">
          <div class="file-title">手持结算卡正面照片</div>
          <div class="file-logo">
            <img src="" id="bankHandPic_src">
          </div>
          <div class="btn"></div>
        </a>
      </div>
      <div class="right">
        <div class="sample-title">示例</div>
        <div class="sample-logo"><img src="http://static.jinkaimen.cn/hss/assets/sample4.png"></div>
      </div>
    </div>
    <div class="next">
      <div id="submit">提交</div>
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
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.2.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.4/upload.min.js"></script>
</html>