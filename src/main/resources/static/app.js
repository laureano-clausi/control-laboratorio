
Vue.filter("formatDate", function (value) {
    if (value) {
        var d = new Date(value);
        return d.getHours() + ":" + (d.getMinutes() > 9 ? d.getMinutes() : "0" + d.getMinutes()) + " " + d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear();
    } else {
        return 'error';
    }
});

var app = new Vue({
    el: '#app',
    data: {
        message: 'Hello Vue!!!'
    }
});
