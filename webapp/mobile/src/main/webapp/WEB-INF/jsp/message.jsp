<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/5
  Time: 21:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="message" style="display: none">
  <div class="weui-mask"></div>
  <div class="weui-dialog">
    <div class="weui-dialog__hd"><strong class="weui-dialog__title">提示</strong></div>
    <div class="weui-dialog__bd" id="content"></div>
    <div class="weui-dialog__ft">
      <div class="weui-dialog__btn weui-dialog__btn_primary" id="enter">确定</div>
    </div>
  </div>
</div>

<div id="prompt" style="display: none;width: auto;height: auto;font-size: 16px;color: #FFF;
     position: fixed;top: 40%;left: 50%;transform: translate3d(-50%,-50%,0);
     padding: 5px 10px; background-color: #373737;border-radius: 5px;"></div>

<script>
  var timer;
  var prompt = document.getElementById('prompt');
  function prompt_show(msg) {
    clearTimeout(timer);
    prompt.innerHTML = msg;
    prompt.style.display = 'block';
    timer = setTimeout(function () {
      prompt.style.display = 'none';
    }, 1000);
  }
</script>
