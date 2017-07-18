var left = document.getElementById('left');
var right = document.getElementById('right');
var leftList = document.getElementById('leftList');
var rightList = document.getElementById('rightList');
left.onclick = function () {
    left.className = 'active';
    right.className = '';
    leftList.className = 'leftList';
    rightList.className = 'rightList hide';
}
right.onclick = function () {
    right.className = 'active';
    left.className = '';
    leftList.className = 'leftList hide';
    rightList.className = 'rightList';
}