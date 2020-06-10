// execute when page is rendered
$(document).ready(function(){
    get_data();
});

// call dataservlet
function get_data() {

    fetch('/data').then(response => {
        return response.json();
    }).then( data_object => {
        console.log(data_object);
    })
}