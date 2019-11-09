var app = new Vue({
    el: "#app",
    data:{
        players:[],
        games:[],
        myGames: [],
        user: ""
    },
    methods:{
        joinGame(gameId){
            $.post("/api/games/" + gameId + "/players")
                .done(function(){
                    swal("Success: You're in. Good luck. Mercy has no place in this battle.",
                    {
                    timer: 3000
                    });
                    window.setTimeout(function(){
                    window.location.href = '/web/game.html?gp='+data.gpid;}, 3000)
                    })
                },
         login(){
             var request = {
                          username: $("#username").val(),
                          password: $("#password").val()
                          };
                 $.post("/api/login", request)
                     .done(function() {
                      swal("Oh, it's you. good to see you again, I guess.");
                         location.reload();
                     })
                     .fail(function() {
                         console.log("login failed");
                         swal("Error. You shall not pass. Check spelling and upper/lowercase, 'cause you're not getting anywhere whilst failing to log in.");
                     })
         },
        signup(){
               var request = {
                              userName: $("#username").val(),
                              password: $("#password").val()
                              };
                     $.post("/api/players", request)
                         .done(function(){
                             location.reload();
                             swal("Hell yeah! Account created. Time to destroy some ships.");
                         })
                         .fail(function(){
                             swal("Error. Try something else so you can join our cult.");
                         })
             },
         logout(){
                  $.post("/api/logout")
                      .done(function(){
                      swal("You're now logged out. Come back soon!");
                      location.reload();
                      $("#username").val("");
                      $("#password").val("");
                      })
                      .fail(function(){
                        swal("Error: just to let you know, you're failing even at logging out.");
                      })
        },
        createGame(){
            $.post("/api/games")
                .done(function(data){
                window.location.href = 'web/game.html?gp=' + data.gpid;
                })
        }
        },
    created: function(){
        getGames()
    }
})
function getLeaderboard() {
  $.get('/api/leaderboard')
    .done(function (data) {
    var ranking = data.sort(function(a, b){
    return b.total - a.total;
    })
    app.players = ranking;
    })
    .fail(function (jqXHR, textStatus) {
      swal('Failed: ' + textStatus);
    });
}

getLeaderboard();

function getGames(){
 $.get('/api/games')
.done(function(data){
    var games = data.games;
    app.games = games;
    app.user = data.player;
    var misJuegos = games.filter(juego => app.user.email == juego.gamePlayers[0].player.email || app.user.email == juego.gamePlayers[1].player.email);
    app.myGames = misJuegos;
    })
}
