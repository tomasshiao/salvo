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
