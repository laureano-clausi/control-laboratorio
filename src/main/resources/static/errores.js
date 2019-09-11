/* global Vue, axios*/
Vue.component('errores', {
    data: function () {
        return {
            errores: []
        };
    },
    created: function () {
        var self = this;
        var url = new URL(window.location);
        
        axios.get('/api/rooms/' + url.searchParams.get("id") + '/error').then(response => (self.errores = response.data));

    },
    template: document.getElementById("errores").innerHTML
});