document.addEventListener("DOMContentLoaded", function(){
        var pwd = document.getElementById("pwd");
        pwd.type = "password";
    });
    function mostra(){
        if(pwd.type == "password"){
            pwd.type = "text";
        } else {
            pwd.type = "password";
        }
    }