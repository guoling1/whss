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
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.1.5.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="bankList">
  <div class="group red" id="color">
    <div class="top">
      <div class="logo" id="logo"></div>
      <div class="info">
        <div class="name">${bankName}</div>
        <div class="type">储蓄卡 <c:if test="${branchShortName!=''}">| ${branchShortName}</c:if></div>
      </div>
    </div>
    <div class="bottom">
      <div class="p">
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
      </div>
      <div class="p">
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
      </div>
      <div class="p">
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
      </div>
      <div class="p">
        <div class="word">${bankNo}</div>
      </div>
      <div class="s">${mobile}</div>
    </div>
    <div class="small">
      该卡用于收款后的自动结算与余额提现
    </div>
  </div>
</div>

<div id="branchInfo">
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
            <%-- 动态添加 --%>
          </div>
        </div>
      </div>
    </div>
    <div class="list">
      <div class="name">支行名称</div>
      <input id="branch" class="ipt" type="text" placeholder="支行名称关键字如：国贸" readonly>

      <div class="layer" id="layer-b">
        <div class="match">
          <div class="match_name">支行名称</div>
          <input id="match_ipt" class="match_ipt" type="text" placeholder="支行名称关键字如：国贸">
        </div>
        <div class="search">
          <div class="search-list" id="layer-b-list">
            <%-- 动态添加 --%>
          </div>
        </div>
      </div>
    </div>
    <div class="small">
      支行信息填写错误可能会导致结算失败<br>
      如不确定，请联系您的银行查询
    </div>
    <div class="submit-space">
      <div id="submit" class="submit">确认修改</div>
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
  }
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.2.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.4/bankBranch.min.js"></script>
</html>