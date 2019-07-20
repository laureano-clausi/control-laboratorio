/* global Vue, axios*/
Vue.component('listado-room', {
    data: function () {
        return {
            rooms: []
        };
    },
    created: function () {
        var self = this;
        axios.get('/api/rooms')
                .then(response => (self.rooms = response.data));
    },
    template: document.getElementById("template-listado-room").innerHTML
});