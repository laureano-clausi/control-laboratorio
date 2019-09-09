/* global Vue, axios*/
Vue.component('errores', {
    data: function () {
        return {
            errores: []
        };
    },
    methods: {
        agregar: function () {
            var self = this;
            axios.get('/api/rooms/' + url.searchParams.get("id") + '/error').then(response => (self.errores = response.data));
        }
    },
    created: function () {

    },
    template: document.getElementById("errores").innerHTML
});