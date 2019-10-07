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

function login(evt) {
  evt.preventDefault();
  var form = evt.target.form;
  $.post("/app/login",
         { name: form["username"].value,
           pwd: form["password"].value })
   .done(function() { console.log("logged in!");})
   .fail(function(){console.log("Username and/or password invalid");});
}

function logout(evt) {
  evt.preventDefault();
  $.post("/app/logout")
   .done(function() { console.log("logged out"); })
   .fail(function() { console.log("Unable to log out"); });
}