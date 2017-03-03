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
let xxB = document.getElementById('xxB');
let toB = document.getElementById('toB');
let cancelB = document.getElementById('cancelB');

xxB.addEventListener('click', function () {
  layerB.style.display = 'none';
});

cancelB.addEventListener('click', function () {
  layerB.style.display = 'none';
});

toB.addEventListener('click', function () {
  window.location.href = '/sqb/bankBranch?branch=true';
});

let layerBK = document.getElementById('layerBK');
let xxBK = document.getElementById('xxBK');
let toBK = document.getElementById('toBK');
let cancelBK = document.getElementById('cancelBK');

xxBK.addEventListener('click', function () {
  layerBK.style.display = 'none';
});

cancelBK.addEventListener('click', function () {
  layerBK.style.display = 'none';
});

toBK.addEventListener('click', function () {
  window.location.href = '/sqb/bankBranch?branch=true&card=true';
});

let layerC = document.getElementById('layerC');
let xxC = document.getElementById('xxC');
let toC = document.getElementById('toC');
let cancelC = document.getElementById('cancelC');

xxC.addEventListener('click', function () {
  layerC.style.display = 'none';
});

cancelC.addEventListener('click', function () {
  layerC.style.display = 'none';
});

toC.addEventListener('click', function () {
  window.location.href = '/sqb/creditCardAuthen?card=true';
});

let layer = document.getElementById('layer');
let xx = document.getElementById('xx');
let cancel = document.getElementById('cancel');

xx.addEventListener('click', function () {
  layer.style.display = 'none';
});

cancel.addEventListener('click', function () {
  layer.style.display = 'none';
});

let layerE = document.getElementById('layerE');
let xxE = document.getElementById('xxE');
let cancelE = document.getElementById('cancelE');

xxE.addEventListener('click', function () {
  layerE.style.display = 'none';
});

cancelE.addEventListener('click', function () {
  layerE.style.display = 'none';
});