const message = _require('message');
const tools = _require('tools');
// 定义ajax事件
let query = tools.GetUrlArg();
let weuiCells = document.getElementById('weui-cells');
let record = document.getElementById('record');
let nowPage = 1;
let more = document.createElement('div');
more.className = 'touch_more';
more.innerHTML = '加载更多';
more.style.display = 'none';
more.style.textAlign = 'center';
more.style.height = '50px';
more.style.lineHeight = '50px';
more.style.fontSize = '16px';
more.style.color = '#999';
more.style.backgroundColor = '#f4f5f8';
let pathName = window.location.pathname;
more.addEventListener('touchstart', function () {
    more.style.backgroundColor = '#d8d7dc';
});
more.addEventListener('touchend', function () {
    more.style.backgroundColor = '#f4f5f8';
});
more.addEventListener('click', function () {
    getData(null, nowPage + 1);
});
record.appendChild(more);
let getData = function (e, page) {
    // 检查page 如果page为1 清空content数据
    nowPage = page || 1;
    if (nowPage == 1) {
        weuiCells.innerHTML = '';
    }
    $.ajax({
        type: "get",
        url: '/membership/rechargeListByPage?mid='+query.mid+'&currentPage='+nowPage,
        dataType: "json",
        error: function () {
            alert("请求失败")
        },
        success: function (data) {
            for (let i = 0; i < data.list.length; i++) {
                let div_list = document.createElement('div');
                div_list.className = 'weui-cell';
                let div_list_left = document.createElement('div');
                div_list_left.className = 'weui-cell__bd';
                let left_title = document.createElement('p');
                left_title.innerHTML = data.list[i].shopname;
                let left_date = document.createElement('p');
                left_date.className = 'time';
                left_date.innerHTML = data.list[i].updateTime;
                div_list_left.appendChild(left_title);
                div_list_left.appendChild(left_date);
                let div_list_right = document.createElement('div');
                div_list_right.className = 'weui-cell__ft';
                let span = document.createElement('span');
                span.innerHTML="￥";
                let span1 = document.createElement('span');
                span1.innerHTML=data.list[i].amount;
                div_list_right.appendChild(span)
                div_list_right.appendChild(span1)
                div_list.appendChild(div_list_left);
                div_list.appendChild(div_list_right);
                weuiCells.insertBefore(div_list, weuiCells.childNodes[0]);
            }
            if (data.page.totalPage != nowPage) {
                more.style.display = 'block';
            } else {
                more.style.display = 'none';
            }
        }
    });
};
// 初始化数据
getData();