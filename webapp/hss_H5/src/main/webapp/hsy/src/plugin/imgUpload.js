import wx from "weixin-js-sdk"
export default{
  install(Vue){
    Vue.directive('imgUpload',{
      inserted: function (el,binding){
        let arg = binding.arg;
        //创建左侧ul
        let leftUl = document.createElement('ul');
        if(arg && arg.leftUl){
          leftUl.className=arg.leftUl
        } else{
          leftUl.style.float="left";
          leftUl.style.width="46%";
          leftUl.style.marginLeft='10px'
        }
        //循环创建四个上传图片框
        for (let i=0;i<4;i++){
          let li = document.createElement('li');
          if(arg && arg.li){
            li.className=arg.li
          } else{
            li.style.width="100%";
            li.style.height='173px';
            li.style.background="#fff";
            li.style.marginBottom="20px"
          }
          //图片上方文字
          let p=document.createElement('p');
          p.innerHTML="上传图片"
          if(arg && arg.p){
            p.className=arg.p
          } else{
            p.style.width="93%";
            p.style.height='28px';
            p.style.lineHeight='28px';
            p.style.fontSize='14px'
            p.style.color="#aab2d2";
            p.style.margin="0 auto"
          }
          li.appendChild(p)
          //选择图片框
          let div = document.createElement('div');
          if(arg && arg.div){
            div.className=arg.div
          } else{
            div.style.width="93%";
            div.style.height='133px';
            div.style.background="#f4f5f8";
            div.style.margin="0 auto"
          }
          li.appendChild(div)

          let img = document.createElement('img');
          //img.src='../assets/camera.png'
          div.appendChild(img)
          div.onclick=function(){
            console.log(1);
            wx.chooseImage({
                count:1,
                sizeType: ['original', 'compressed'], //原图还是压缩图
                sourceType: ['album', 'camera'], //指定来源相册还是相机
                success: function (res) {
                  var localIds = res.localIds; //返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片

                  //预览图片接口
                  wx.previewImage({
                    current: '', //当前显示图片的http链接
                    urls: [] //需要预览的图片http链接列表
                  });
                }
            })
          }
          leftUl.appendChild(li)
        }
        el.appendChild(leftUl)
        //设置每个p的内容
        var aP=leftUl.getElementsByTagName('p');
        aP[0].innerHTML="身份证正面照"
        aP[1].innerHTML="身份证背面照片"
        aP[2].innerHTML="手持身份证照片"
        aP[3].innerHTML="手持结算卡正面"

        //创建右侧ul
        let rightUl = document.createElement('ul');
        if(arg && arg.rightUl){
          rightUl.className=arg.rightUl
        } else{
          rightUl.style.float="right";
          rightUl.style.width="46%";
          rightUl.style.marginRight='10px'
        }
        //循环创建四个上传图片框
        for (let i=0;i<4;i++){
          let li = document.createElement('li');
          if(arg && arg.li){
            li.className=arg.li
          } else{
            li.style.width="100%";
            li.style.height='173px';
            li.style.background="#fff";
            li.style.marginBottom="20px"
          }
          //图片上方文字
          let p=document.createElement('p');
          p.innerHTML="示例"
          if(arg && arg.p){
            p.className=arg.p
          } else{
            p.style.width="93%";
            p.style.height='28px';
            p.style.lineHeight='28px';
            p.style.fontSize='14px'
            p.style.color="#aab2d2";
            p.style.margin="0 auto"
          }
          li.appendChild(p)
          //选择图片框
          let div = document.createElement('div');
          if(arg && arg.div){
            div.className=arg.div
          } else{
            div.style.width="93%";
            div.style.height='133px';
            div.style.background="#f4f5f8";
            div.style.margin="0 auto"
          }
          li.appendChild(div)
          div.onclick=function(){
            console.log(1);
            wx.chooseImage({
                count:1,
                sizeType: ['original', 'compressed'], // 原图还是压缩图
                sourceType: ['album', 'camera'], // 指定来源相册还是相机
                success: function (res) {
                  var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片

                  //预览图片接口
                  wx.previewImage({
                    current: '', //当前显示图片的http链接
                    urls: [] //需要预览的图片http链接列表
                  });
                }
            })
          }
          rightUl.appendChild(li)
        }
        el.appendChild(rightUl)

        //创建提交按钮
        let input=document.createElement('div');
        if(arg && arg.input){
          input.className=arg.input
        } else {
          input.style.overflow="hidden";
          //input.style.border='1px solid #000';
          input.style.width="94%";
          input.style.height='50px'
          input.style.lineHeight='50px'
          input.style.background='#7086bd'
          input.style.fontSize='16px';
          input.style.color='#fff'
          input.style.borderRadius='4px'
          input.style.margin='0 auto 18px'
          input.style.textAlign='center'
          input.innerHTML="提交"
        }

        input.onclick=function () {
          //上传图片接口
          wx.uploadImage({
            localId: '', //需要上传的图片的本地ID，由chooseImage接口获得
            isShowProgressTips: 1, // 默认为1，显示进度提示
            success: function (res) {
              var serverId = res.serverId; // 返回图片的服务器端ID
            }
          })
        }
        el.appendChild(input)
     }
   })
  }
}
