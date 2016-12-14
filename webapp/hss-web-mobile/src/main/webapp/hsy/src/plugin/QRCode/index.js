/**
 * Created by administrator on 16/12/1.
 */

export default{
  install(Vue){
    Vue.directive('qr', {
      inserted: function (el, binding) {
        // 获取二维码生成url
        let codeUrl = binding.value.url;

        // 生成二维码
        let qrcode = new QRCode(el, {
          text: codeUrl,
          width: 180,
          height: 180,
          colorDark: "#000000",
          colorLight: "#ffffff",
          correctLevel: QRCode.CorrectLevel.H
        });

        // 清除二维码
        Vue.prototype._$qrClear = function () {
          qrcode.clear();
        }
      }
    });
  }
}
