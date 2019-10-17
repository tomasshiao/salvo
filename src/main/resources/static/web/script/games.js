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

var login = new Vue({
    el:"#login",
    data:{
        currentUser: ""
    }
})
function login() {
  var form = document.getElementById("login-form");
  $.post("/app/login",
         { name: form["username"].value,
           password: form["password"].value })
   .done(function(data) {
   login.currentUser = data.name;
   })
   .fail(function(){console.log("Username and/or password invalid");});
}

function logout() {
  $.post("/app/logout")
   .done(function() { console.log("logged out"); })
   .fail(function() { console.log("Unable to log out"); });
}