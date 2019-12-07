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
                    window.location.href = '/web/placeShipsGrid.html?gp=' + data.gpid;
                    })
                .fail(function(){
                    swal.fire({
                    icon: 'error',
                    text: "How sad, you're not even capable of joining a game.",
                    showConfirmButton: true,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '>:('
                    });
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
                     })
                     .fail(function() {
                         console.log("login failed");
                         swal.fire({
                         icon: 'error',
                         title:"Error.",
                         text: "You shall not pass. Check spelling and upper/lowercase, 'cause you're not getting anywhere whilst failing to log in.",
                         showConfirmButton: true,
                         confirmButtonColor: '#3085d6',
                         confirmButtonText: '>:('
                         });
                     })
         },
        signup(){
               var request = {
                              userName: $("#username").val(),
                              password: $("#password").val()
                              };
                     $.post("/api/players", request)
                         .done(function(){
                             swal.fire({
                             icon:'success',
                             title: "Success.",
                             text:"Hell yeah! Account created. Time to destroy some ships.",
                             showConfirmButton: false,
                             timer: 1500
                             });
                         })
                         .then(function(){
                          $.post("/api/login", {username: request.userName, password: request.password})
                               .done(function() {
                                location.reload();
                               })
                          })
                         .fail(function(){
                             swal.fire({
                             icon:'error',
                             title:"Error.",
                             text: "Try something else so you can join our cult.",
                             showConfirmButton: true,
                             confirmButtonColor: '#3085d6',
                             confirmButtonText: '>:('
                             });
                         })
             },
         logout(){
                  $.post("/api/logout")
                      .done(function(){
                      swal.fire({
                      icon:'success',
                      title:"You're now logged out. Come back soon!",
                      showConfirmButton: false,
                      timer: 1500
                      });
                      location.reload();
                      $("#username").val("");
                      $("#password").val("");
                      })
                      .fail(function(){
                        swal.fire({
                        icon: 'error',
                        title:"Error.",
                        text: "Just to let you know, you're failing even at logging out.",
                        showConfirmButton: true,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '>:('
                        });
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
      swal.fire({
      icon: 'error',
      title:'Failed',
      text: textStatus,
      showConfirmButton: true,
      confirmButtonText: '>:(',
      confirmButtonColor: '#3085d6'
      });
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
