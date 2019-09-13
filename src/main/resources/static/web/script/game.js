

$(function () {
  loadData();
});

function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function loadData() {
  $.get('/api/game_view/' + getParameterByName('id'))
    .done(function (game) {
      console.log(game);
      var playerInfo;
      if (game.gamePlayers[0].id == getParameterByName('id'))
        playerInfo = [game.gamePlayers[0].player, game.gamePlayers[1].player];
      else
        playerInfo = [game.gamePlayers[1].player, game.gamePlayers[0].player];

      $('#playerInfo').text(playerInfo[0].email + ' (you) vs ' + playerInfo[1].email);

      game.ships.forEach(function (shipPiece) {
        shipPiece.locations.forEach(function (shipLocation) {
          let turnHitted = isHit(shipLocation,game.salvoes,playerInfo[0].id)
          if(turnHitted >0){
            $('#B_' + shipLocation).addClass('ship-piece-hited');
            $('#B_' + shipLocation).text(turnHitted);
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