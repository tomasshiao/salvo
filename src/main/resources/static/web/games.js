function loadData(){
    $.get("/api/games")
    .done(function(data){
    app.games = data;
    })
    .fail(function(jqXHR, textStatus){
        showOutput("Failed: " + textStatus);
    })
}
var app = new Vue({
    el: "#app",
    data:{
        games: [],
    }
});
loadData();
