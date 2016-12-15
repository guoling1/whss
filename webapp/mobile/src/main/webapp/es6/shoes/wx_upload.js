/**
 * Created by administrator on 2016/12/9.
 */

_require.register("upload", (module, exports, _require, global)=> {

  /* 图片上传类
   * 默认已经做好 config 校验
   * 仅针对单张图片*/
  class Upload {
    constructor(btnId, fun) {
      this.localId = '';
      this.serverId = '';
      this.btn = document.getElementById(btnId);
      this.callback = fun;
      wx.ready(()=> {
        console.log('upload 构造成功...');

        this.btn.addEventListener('click', ()=> {
          wx.chooseImage({
            count: 1,
            sizeType: ['original', 'compressed'],
            sourceType: ['album', 'camera'],
            success: (res)=> {
              this.localId = res.localIds[0];
              // 图片选择完成后 延迟100ms调用上传 (解决安卓6.2以下的版本会出现的bug)
              setTimeout(()=> {
                wx.uploadImage({
                  localId: this.localId, // 需要上传的图片的本地ID，由chooseImage接口获得
                  isShowProgressTips: 1, // 默认为1，显示进度提示
                  success: (res)=> {
                    this.serverId = res.serverId; // 返回图片的服务器端ID
                    this.callback(this.localId, this.serverId);
                  }
                });
              }, 100);
            }
          });
        });
      });
    }

    getServerId() {
      return this.serverId;
    }
  }
  module.exports = Upload;
});