<%--
  Created by IntelliJ IDEA.
  User: xiang.yu
  Date: 2016/12/5
  Time: 20:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>好收银</title>
    <link rel="stylesheet" href="../css/reset.css">
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
<div id="issueCord">
    <div class="header" id="issueHead">
        <span class="active">分配给二级代理</span>
        <span>分配给自己</span>
    </div>
    <div class="box">
        <!--分配给二级代理-->
        <div class="content show" id="content">
            <form action="/dealer/distributeQRCode" method="post">
                <label for="">
                    二级代理
                    <input type="text" name="name" id="mobile" placeholder="输入代理手机号或名称">
                    <ul class="list" id="listId"></ul>
                </label>
                <label for="">
                    起始码
                    <input type="number" name="startCode" id="number1" placeholder="请输入起始二维码">
                </label>
                <label for="">
                    结束码
                    <input type="number" name="endCode" id="number2" placeholder="请输入结束二维码">
                </label>
                <p>本次分配个数：<span id="count1" style="float:none"></span>个
                    <span><a href="/dealer/distributeRecords">分配记录</a></span>
                </p>
                <input type="hidden"  name="dealerId" id="dealerId1" value="0" />
                <input type="hidden" name="isSelf" value="0" />
                <input class="submit" id = "submit1" type="submit" value="提交" />
            </form>
        </div>
        <!--分配给自己-->
        <div class="ownContent" id="ownContent">
            <form action="/dealer/distributeQRCode" method="post">
                <label for="">
                    起始码
                    <input type="number" name="startCode" id="number3" placeholder="请输入起始二维码">
                </label>
                <label for="">
                    结束码
                    <input type="number" name="endCode" id="number4" placeholder="请输入结束二维码">
                </label>
                <p>本次分配个数:<span id="count3" style="float:none"></span> 个
                    <span><a href="/dealer/distributeRecords?self=1">分配记录</a></span>
                    <input type="hidden" id="data" value="" />
                </p>
                <input type="hidden"  name="dealerId2"  value="0" />
                <input type="hidden" name="isSelf" value="1" />
                <input class="submit" id = "submit2" type="submit" value="提交" />
            </form>
        </div>
    </div>
</div>
</body>
<script src="/js/jQuery-2.1.4.min.js"></script>
<script src="/js/utility.js"></script>
<%--<script src="/js/issue.js"></script>--%>

<script src="/js/shoes.js"></script>
<script src="/js/issue1.js"></script>
</html>