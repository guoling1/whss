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
    <!--<link rel="stylesheet" href="http://img.jinkaimen.cn/hsy/css/reset.css">-->
    <!--<link rel="stylesheet" href="http://img.jinkaimen.cn/hsy/css/style.css">-->
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
                分配个数
                <input type="text" name="count" id="number1" placeholder="请输入分配个数">
            </label>
            <p>剩余可分配个数：${data}个
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
                分配个数
                <input type="text" name="count" id="number2" placeholder="请输入分配个数">
            </label>
            <p>剩余可分配个数: ${data}个
                <span><a href="/dealer/distributeRecords">分配记录</a></span>
                <input type="hidden" id="data" value="${data}" />
            </p>
            <input type="hidden"  name="dealerId2"  value="0" />
            <input type="hidden" name="isSelf" value="1" />
            <input class="submit" id = "submit2" type="submit" value="提交" />
        </form>
        </div>
    </div>
</div>
<%@include file="../message.jsp" %>
</body>
<script src="http://img.jinkaimen.cn/hsy/js/qrcode.min.js"></script>
<script src="/js/jQuery-2.1.4.min.js"></script>
<script src="/js/utility.js"></script>
<script>
    $('#issueHead span').click(function () {
        $(this).addClass('active').siblings('span').removeClass('active');
        $('.box div').eq($(this).index()).addClass('show').siblings('div').removeClass('show');
    })
</script>
<script>
    $("#mobile").keyup( function () {
        var mobi1e = $("#mobile").val();
        $.post("/dealer/find", { condition: mobi1e},
                                function(data){
                                    for(var i = 0; i < data.result.length; i++){
                                            var oLi='<li>'+
                                                    '<span>'+data.result[i].name+'</span>'+
                                                    '<span>'+data.result[i].mobile+'</span>'+
                                                    '</li>'
                                            $('.list').append(oLi)
                                        }
                                     var aLi = document.getElementById('listId').getElementsByTagName('li');
                                        for(var i=0; i<aLi.length;i++){
                                            if(aLi.length!=0){
                                            console.log(2222)
                                                $('.list').show()
                                            }
                                            aLi[i].index = i
                                            aLi[i].onclick=function () {
                                                $('.content input')[0].value=data.result[this.index].name + " " + data.result[this.index].mobile;
                                                $('.list').hide();
                                                $("#dealerId1").val(data.result[this.index].dealerId);
                                            }
                                        }
          });}
           );
    $("#submit1").click( function(){
        var number1 = $("#number1").val();
        var data = $("#data").val();
        var dealerId1 = $("#dealerId1").val();
        if (parseInt(dealerId1)==0){
             prompt_show("请输入二级代理");
               return false;
        }
        if(parseInt(data)>=parseInt(number1) && parseInt(number1) > 0)
          {
            return true;
          }else{
          prompt_show("剩余可分配个数:" + data +"个");
            return false;
          }
    } );
    $("#submit2").click( function(){
            var number2 = $("#number2").val();
            var data = $("#data").val();
            if(parseInt(data)>=parseInt(number2) && parseInt(number2) > 0 )
              {
                return true;
              }else{
              prompt_show("剩余可分配个数:" + data +"个");
                return false;
              }
        } );
</script>
</html>