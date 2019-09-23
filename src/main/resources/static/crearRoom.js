/* global Vue, axios*/
Vue.component('crear-room', {
    data: function () {
        return {
            room: {nombre: "un nombre", ip: "una ip"}
        };
    },
    methods: {
        agregar: function () {
            var self = this;
            axios.post('/api/rooms', self.room).then(response => (location.reload()));
        }
    },
    created: function () {

    },
    template: document.getElementById("crear-room").innerHTML
});