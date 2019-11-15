var app = new Vue({
    el: "#app",
    data:{
        players:[],
        games:[],
        user: ""
    },
    methods:{
        joinGame(gameId){
            $.post("/api/games/" + gameId + "/players")
                .done(function(data){
                    console.log(gameId);
                    console.log(data.gpid);
                    swal("Success: You're in. Good luck. Mercy has no place in this battle.");
                    window.location.href = '/web/game.html?gp='+data.gpid;
                    })
                .fail(function(){
                    swal("How sad, you're not even capable of joining a game.");
                })
                },
         login(){
             var request = {
                          username: $("#username").val(),
                          password: $("#password").val()
                          };
                 $.post("/api/login", request)
                     .done(function() {
                      location.reload();
                      swal("Oh, it's you. good to see you again, I guess.");
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
                console.log(data);
                console.log(data.gpid);
                window.location.href = '/web/placeShipsGrid.html?gp=' + data.gpid;
                })
        },
        reenter(gamePlayers){
            var gamePlayerId = 0;
            if(gamePlayers[0].player.email == app.user.email){
                gamePlayerId = gamePlayers[0].gpid
            } else {
                gamePlayerId = gamePlayers[1].gpid
            }
            window.location.href = 'game.html?gp=' + gamePlayerId;
        }
        },
    created: function(){
        getGames(),
        getLeaderboard()
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

function getGames(){
 $.get('/api/games')
.done(function(data){
    var games = data.games;
    app.games = games;
    app.user = data.player;
    })
}
