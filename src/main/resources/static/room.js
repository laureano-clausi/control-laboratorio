/* global Vue, axios*/
Vue.component('room', {
    data: function () {
        return {
            room: {}
        };
    },
    created: function () {
        var self = this;
        var url = new URL(window.location);

        axios.get('/api/rooms/' + url.searchParams.get("id")).then(response => (self.room = response.data));

    },
    template: document.getElementById("template-room").innerHTML
});