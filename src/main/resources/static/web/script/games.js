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
    var request = {
                 username: $("#username").val(),
                 password: $("#password").val()
                 };
        $.post("/api/players", request)
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
    var request = {
                 username: $("#username").val(),
                 password: $("#password").val()
                 };
        $.post("/api/login", request)
            .done(function() {
             alert("Success");
                console.log("login ok");
                $('#loginSuccess').show( "slow" ).delay(2000).hide( "slow" );
                $("#username").val("");
                $("#password").val("");
                $.get("/api/games")
                    .done(function(data){
                    var player = data.players.email;
                    $("#formularioLogIn").hide();
                    $("#logOut").show();
                    $("#playerLoggueado").text("User: " + player);
                    })
            })
            .fail(function() {
                console.log("login failed");
                alert("Error");
                $('#loginFailed').show( "slow" ).delay(2000).hide( "slow" );
                $("#username").val("");
                $("#password").val("");
                $("#username").focus();
            })
    }
});
function logout(){
          $.post("/api/logout")
              .done(function(){
              alert("Logged out");
              $("#formularioLogIn").show();
              $("#logOut").hide();
              })
      }