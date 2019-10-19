var leaderboard = new Vue({
    el:"#leaderboard",
    data:{
    players: []
    }
})

function getLeaderboard() {
  $.get('/api/leaderboard')
    .done(function (data) {
    console.log(data);
    var ranking = data.sort(function(a, b){
    return b.total - a.total;
    })
    console.log(ranking);
    leaderboard.players = ranking;
    })
    .fail(function (jqXHR, textStatus) {
      alert('Failed: ' + textStatus);
    });
}

getLeaderboard();

var gameList = new Vue({
    el:"#tableGames",
    data:{
    gamesList: []
    }
})

function getGames(){
 $.get('/api/games')
    .done(function(data){
    console.log(data);
    var games = data.games;
    console.log(games);
    gameList.gamesList = games;
    })
}
getGames();
$(function() {
    $('.submitbutton').click(function () {
        submitButton = $(this).attr('name')
    });
    $(".sign-up-button").click(function(){
        var username = $("#username").val();
        var password = $("#password").val();
        $.post("/api/players", {userName:  username, password: password})
        .done(function(){
            alert("Success");
        })
        .fail(function(){
            alert("Error");
        })
    })
});
$('#login-form').on('submit', function (event) {
    event.preventDefault();
    if (submitButton == "login") {
        $.post("/api/login",
            { username: $("#username").val(),
                password: $("#password").val() })
            .done(function() {
             alert("Success");
                console.log("login ok");
                $('#loginSuccess').show( "slow" ).delay(2000).hide( "slow" );
                // $("#username").val("");
                $("#password").val("");
                //updateJson();
            })
            .fail(function() {
                console.log("login failed");
                alert("Error");
                $('#loginFailed').show( "slow" ).delay(2000).hide( "slow" );
                $("#username").val("");
                $("#password").val("");
                $("#username").focus();
                // $('#loginFailed').hide( "slow" );
            })
            .always(function() {
            });
    }
});