function loadData(){
    $.get("/api/games")
    .done(function(data){
    //formatData(JSON.stringify(data, null, 2));
    formatData(data);
    })
    .fail(function(jqXHR, textStatus){
        showOutput("Failed: " + textStatus);
    })
}
function formatData(data){
    data.map(x -> )
}