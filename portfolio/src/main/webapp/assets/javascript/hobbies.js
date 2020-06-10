// execute when page is rendered
$(document).ready(function(){
    get_data();
    // generate quote when rendered
    get_quote();
});

// call dataservlet
function get_data() {

    fetch('/data').then(response => {
        return response.json();
    }).then( data_object => {
        $(".content div").text(data_object["intro"]);
    })
}

// get quote from quote servlet 
function get_quote() {
        
    fetch('/quote').then((response) => {
        return response.text();
    }).then(quote => {
        $(".quote").text(quote);
    })
}