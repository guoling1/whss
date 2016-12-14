// var validate = validate();

var images = {
    localId: {},
    serverId: {}
};
wx.ready(function () {
    document.querySelector('#identityFacePic').onclick = function () {
        wx.chooseImage({
            count: 1,
            sizeType: ['original', 'compressed'],
            sourceType: ['album', 'camera'],
            success: function (res) {
                images.localId[0] = res.localIds[0];
                $("#identityFacePic_src").attr("src",images.localId[0]);
                // 图片选择完成后 延迟100ms调用上传 (解决安卓6.2以下的版本会出现的bug)
                setTimeout(function () {
                    wx.uploadImage({
                        localId: images.localId[0], // 需要上传的图片的本地ID，由chooseImage接口获得
                        isShowProgressTips: 1, // 默认为1，显示进度提示
                        success: function (res) {
                            images.serverId[0] = res.serverId; // 返回图片的服务器端ID
                        }
                    });
                }, 100)
            }
        });
    };

    document.querySelector('#identityOppositePic').onclick = function () {
        wx.chooseImage({
            count: 1,
            sizeType: ['original', 'compressed'],
            sourceType: ['album', 'camera'],
            success: function (res) {
                images.localId[1] = res.localIds[0];
                $("#identityOppositePic_src").attr("src",images.localId[1]);
                // 图片选择完成后 延迟100ms调用上传 (解决安卓6.2以下的版本会出现的bug)
                setTimeout(function () {
                    wx.uploadImage({
                        localId: images.localId[1], // 需要上传的图片的本地ID，由chooseImage接口获得
                        isShowProgressTips: 1, // 默认为1，显示进度提示
                        success: function (res) {
                            images.serverId[1] = res.serverId; // 返回图片的服务器端ID
                        }
                    });
                }, 100)
            }
        });
    };

    document.querySelector('#identityHandPic').onclick = function () {
        wx.chooseImage({
            count: 1,
            sizeType: ['original', 'compressed'],
            sourceType: ['album', 'camera'],
            success: function (res) {
                images.localId[2] = res.localIds[0];
                $("#identityHandPic_src").attr("src",images.localId[2]);
                // 图片选择完成后 延迟100ms调用上传 (解决安卓6.2以下的版本会出现的bug)
                setTimeout(function () {
                    wx.uploadImage({
                        localId: images.localId[2], // 需要上传的图片的本地ID，由chooseImage接口获得
                        isShowProgressTips: 1, // 默认为1，显示进度提示
                        success: function (res) {
                            images.serverId[2] = res.serverId; // 返回图片的服务器端ID
                        }
                    });
                }, 100)
            }
        });
    };

    document.querySelector('#bankHandPic').onclick = function () {
        wx.chooseImage({
            count: 1,
            sizeType: ['original', 'compressed'],
            sourceType: ['album', 'camera'],
            success: function (res) {
                images.localId[3] = res.localIds[0];
                $("#bankHandPic_src").attr("src",images.localId[3]);
                // 图片选择完成后 延迟100ms调用上传 (解决安卓6.2以下的版本会出现的bug)
                setTimeout(function () {
                    wx.uploadImage({
                        localId: images.localId[3], // 需要上传的图片的本地ID，由chooseImage接口获得
                        isShowProgressTips: 1, // 默认为1，显示进度提示
                        success: function (res) {
                            images.serverId[3] = res.serverId; // 返回图片的服务器端ID
                        }
                    });
                }, 100)
            }
        });
    };

});



function submit() {
        var identityFacePic = images.serverId[0];
        var identityOppositePic = images.serverId[1];
        var identityHandPic = images.serverId[2];
        var bankHandPic = images.serverId[3];

        if (identityFacePic == null || identityFacePic == ""){
            prompt_show(identityFacePic+" 输入值为空！");
            return false;
        }
        if (identityOppositePic == null || identityOppositePic == ""){
            prompt_show(identityOppositePic+" 输入值为空！");
            return false;
        }
        if (identityHandPic == null || identityHandPic == ""){
            prompt_show(identityHandPic+" 输入值为空！");
            return false;
        }
        if (bankHandPic == null || bankHandPic == ""){
            prompt_show(bankHandPic+" 输入值为空！");
            return false;
        }

        var param = {"identityFacePic":identityFacePic,"identityOppositePic":identityOppositePic,
            "identityHandPic":identityHandPic,"bankHandPic":bankHandPic};
        var newParam=JSON.stringify(param);
        $.ajax({
            url : "/merchantInfo/savePic",
            type : "post",
            contentType:"application/json",
            dataType:"json",
            data :newParam,
            success : function(r) {
                if(r.code==1){
                    window.location.href="/sqb/follow";
                }else{
                    prompt_show(r.msg);
                }
            },
            error : function(){
                prompt_show("系统异常");
            }
        });


}

