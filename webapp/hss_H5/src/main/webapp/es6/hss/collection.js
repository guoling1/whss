/**
 * Created by administrator on 2016/12/6.
 */

// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 引入http message
const message = _require('message');
const Keyboard = _require('keyboard');
new Keyboard({
  spanId: 'key-span',
  inputId: 'key-input',
  keyboardId: 'keyboard'
});

// 定义变量
let layerB = document.getElementById('layerB');
let toB = document.getElementById('toB');
let cancelB = document.getElementById('cancelB');

cancelB.addEventListener('click', function () {
  layerB.style.display = 'none';
});

toB.addEventListener('click', function () {
  window.location.href = '/sqb/bankBranch/' + pageData.bankId + '?branch=true';
});

let layerBK = document.getElementById('layerBK');
let toBK = document.getElementById('toBK');
let cancelBK = document.getElementById('cancelBK');

cancelBK.addEventListener('click', function () {
  layerBK.style.display = 'none';
});

toBK.addEventListener('click', function () {
  window.location.href = '/sqb/bankBranch/' + pageData.bankId + '?branch=true&card=true';
});

let layerC = document.getElementById('layerC');
let toC = document.getElementById('toC');
let cancelC = document.getElementById('cancelC');

cancelC.addEventListener('click', function () {
  layerC.style.display = 'none';
});

toC.addEventListener('click', function () {
  window.location.href = '/sqb/creditCardAuthen?card=true';
});

let layer = document.getElementById('layer');
let cancel = document.getElementById('cancel');

cancel.addEventListener('click', function () {
  layer.style.display = 'none';
});

let layerE = document.getElementById('layerE');
let cancelE = document.getElementById('cancelE');

cancelE.addEventListener('click', function () {
  layerE.style.display = 'none';
});