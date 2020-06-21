// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

$(document).ready(function(){

    // jQuery methods go here...
    // animate profile section
    $(".intro").hover(function() {
        $(".intro span:nth-child(1).border").addClass("animate1");
        $(".intro span:nth-child(2).border").addClass("animate2");
        $(".intro span:nth-child(3).border").addClass("animate3");
        $(".intro span:nth-child(4).border").addClass("animate4");
    }, function() {
        $(".intro span:nth-child(1).border").removeClass("animate1");
        $(".intro span:nth-child(2).border").removeClass("animate2");
        $(".intro span:nth-child(3).border").removeClass("animate3");
        $(".intro span:nth-child(4).border").removeClass("animate4");
    });

    displayContact();
});

// get login status
function getLoginStatus() {

    return fetch('/loginStatus').then(response => {
        return response.json();
    }).then(dataObject => {
        return dataObject["loginStatus"]
    })
}

function displayContact() {

    console.log("displayContact function");
    getLoginStatus().then(loginStatus => {
        if (loginStatus) {
            $('.nav-bar').append('<div class="link"><a href="/login">LOGOUT</a></div>')
            $('.nav-bar').append('<div class="contact"><a href="./contact.html">CONTACT</a></div>');
        } else {
            $('.nav-bar').append('<div class="link"><a href="/login">LOGIN</a></div>')
        }
    });
}