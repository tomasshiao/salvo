$(function () {
  loadData();
});

function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function loadData() {
  $.get('/api/game_view/' + getParameterByName('gp'))
    .done(function (game) {
      var playerInfo;
      if(game.gamePlayers.length == 2){
      if (game.gamePlayers[0].gpid == getParameterByName('gp'))
        playerInfo = [game.gamePlayers[0].player, game.gamePlayers[1].player];
      else
        playerInfo = [game.gamePlayers[1].player, game.gamePlayers[0].player];

      $('#playerInfo').text(playerInfo[0].email + ' (you) vs ' + playerInfo[1].email);
      } else {
        playerInfo = [game.gamePlayers[0].player];
        $('#playerInfo').text(playerInfo[0].email + ' (you) vs ' + "An incognito player whose identity is yet to be revealed");
      }
      game.ships.forEach(function (shipPiece) {
        shipPiece.locations.forEach(function (shipLocation) {
          let turnHit = isHit(shipLocation,game.salvoes,playerInfo[0].id)
          if(turnHit >0){
            $('#B_' + shipLocation).addClass('ship-piece-hit');
            $('#B_' + shipLocation).text(turnHit);
          }
          else
            $('#B_' + shipLocation).addClass('ship-piece');
        });
      });
      game.salvoes.forEach(function (salvo) {
        console.log(salvo);
        if (playerInfo[0].id === salvo.player) {
          salvo.locations.forEach(function (location) {
            $('#S_' + location).addClass('salvo');
          });
        } else {
          salvo.locations.forEach(function (location) {
            $('#_' + location).addClass('salvo');
          });
        }
      });
    })
    .fail(function (jqXHR, textStatus) {
      alert('Failed: ' + textStatus);
    });
}

function isHit(shipLocation,salvoes,playerId) {
  var hit = 0;
  salvoes.forEach(function (salvo, ship) {
    if(salvo.player != playerId)
      salvo.locations.forEach(function (location) {
        if(ship.locations === location)
          hit = salvo.turn;
      });
  });
  return hit;
}

function backToMenu(){
    window.location.href = 'games.html';
}