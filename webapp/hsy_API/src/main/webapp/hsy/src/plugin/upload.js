import wx from "weixin-js-sdk"
import store from '../store'
export default{
  install(Vue){
    // 获取微信配置的方法,监测路由变更,仅支持全局调用
    Vue.__init = function (route) {
      Vue.http.post('/wx/getWxConfig', {
        url: window.location.href
      }).then(function (res) {
        wx.config({
          debug: false,
          appId: res.data.appid,
          timestamp: res.data.timestamp,
          nonceStr: res.data.nonceStr,
          signature: res.data.signature,
          jsApiList: [
            'chooseImage',
            'uploadImage'
          ]
        });
      }, function (err) {
        console.log(err);
      });
    };

    const images = {
      localId: {},
      serverId: {}
    };

    Vue.directive('upload', {
      inserted: function (el, binding) {
        let arg = binding.arg;
        el.onclick = function () {
          wx.chooseImage({
            count: 1,
            sizeType: ['original', 'compressed'],
            sourceType: ['album', 'camera'],
            success: function (res) {
              images.localId[arg] = res.localIds[0];
              store.commit('UPLOAD_CHANGE', {
                localId: images.localId
              });
              // 图片选择完成后 延迟100ms调用上传 (解决安卓6.2以下的版本会出现的bug)
              setTimeout(function () {
                wx.uploadImage({
                  localId: images.localId[arg], // 需要上传的图片的本地ID，由chooseImage接口获得
                  isShowProgressTips: 1, // 默认为1，显示进度提示
                  success: function (res) {
                    images.serverId[arg] = res.serverId; // 返回图片的服务器端ID
                    store.commit('UPLOAD_CHANGE', {
                      serverId: images.serverId
                    });
                  }
                });
              }, 100)
            }
          });
        }
      }
    });
  }
}
