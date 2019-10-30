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
      swal('Failed: ' + textStatus);
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
    $('.log-in-button').click(function () {
       button = $(this).attr('name')
    });
    $(".sign-up-button").click(function () {
       button = $(this).attr('name')
    });

$('#login-form').on('submit', function (event) {
    event.preventDefault();
    if (button == "login") {
    var request = {
                 username: $("#username").val(),
                 password: $("#password").val()
                 };
        $.post("/api/login", request)
            .done(function() {
             swal("Success");
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
                swal("Error");
                $('#loginFailed').show( "slow" ).delay(2000).hide( "slow" );
                $("#username").val("");
                $("#password").val("");
                $("#username").focus();
            })
    } else if(button == "signup"){
      var request = {
                     userName: $("#username").val(),
                     password: $("#password").val()
                     };
            $.post("/api/players", request)
                .done(function(){
                    location.reload();
                    swal("Success!");
                })
                .fail(function(){
                    swal("Error");
                })
    }
});
});
function logout(){
          $.post("/api/logout")
              .done(function(){
              swal("Logged out");
              $("#formularioLogIn").show();
              $("#logOut").hide();
              })
}
