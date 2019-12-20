var gamesData;
var actualPlayer;
var opponent={
"email": "An incognito player whose identity is yet to be revealed."}
var hitsConverted;

//Para obtener el id del gamePlayer colocado como query en la url
var gpId = getParameterByName("gp")
console.log(gpId)

function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

fetch("/api/game_view/"+ gpId)
.then(function(response){
	return response.json()
})
.then(function(json){
  gamesData = json
  //Determina el jugador actual y contra quien juega
  WhoIsWho()
  //Cargamos por medio de gridstackla los barcos y chequeamos si ya hay barcos o no para hacer una grilla statica o no
  //(En un futuro se trabajara sin la pagina intermedia de place-ship por lo que de momento esto comparacion esta demas)
  if(gamesData.ships.length > 0){
    //if true, the grid is initialized in static mode, that is, the ships can't be moved
    loadGrid(true)
  } else{
    //On the contrary, the grid is initialized in dynamic mode, allowing the user to move the ships
    loadGrid(false)
     //A futuro para cargar los salvos por medio de gridstack
    //loadGridSalvo()
  }

  createGrid(11, $(".grid-salvoes"), 'salvoes') //carga la matriz que contendra los salvoes pero sin gridstack.js
  setSalvoes() //carga los salvoes ya guardados
    //Una vez cargado los salvoes con createGrid procedemos a establecer una funcion click por cada celda de la siguiente manera
    $('div[id^="salvoes"].grid-cell').click(function(){
        //seguir para codear el disparar una celda, tener en cuenta el tamaño maximo de disparos y que no pueda disparar a una celda ya pintada
        //la diferencia entre una celda pintada y otra que no esta en la clase "salvo" que se le agrega
       //console.log(evt.target)
        //console.log("d")
        if (!$(this).hasClass("salvo")&&!$(this).hasClass("target")&&$(".target").length < 5){
            $(this).addClass("target");
        }else if($(this).hasClass("target")){
            $(this).removeClass("target");
           }
        })
    })
.then(function(){
    console.log(actualPlayer.id);
    var salvoesFired = gamesData.salvoes.sort(function(a, b){return b.turn - a.turn;});
    gameEvents.mySalvoHits = salvoesFired.filter(x => x.player == actualPlayer.id);
    gameEvents.opponentsSalvoHits = salvoesFired.filter(x => x.player != actualPlayer.id);
    var myHits = gameEvents.mySalvoHits.flatMap(salvo => salvo.hits);
    var allMyHits = gameEvents.mySalvoHits.flatMap(salvo => salvo.hits);
    var allMyHitLoc = gameEvents.mySalvoHits.flatMap(salvo => salvo.locations);
    console.log(allMyHits);
    hitsConverted = allMyHits.flatMap(hit => convert(hit[0]) + (parseInt(hit[1]) - 1));
     hitsConverted.forEach(function(ubi){
                $('#salvoes' + ubi).removeClass("salvo").addClass("ship-down");
            })
})
.catch(function(error){
	console.log(error)
})

function WhoIsWho(){
  for(i = 0; i < gamesData.gamePlayers.length; i++){
       if(gamesData.gamePlayers[i].gpid == gpId){
         actualPlayer = gamesData.gamePlayers[i].player
       } else{
         opponent = gamesData.gamePlayers[i].player
       }
     }

  let logger = document.getElementById("logger")
  let wrapper = document.createElement('DIV')
  let p1 = document.createElement('P')
  p1.innerHTML = `Guten Tag, ${actualPlayer.email}!`
  let p2 = document.createElement('P')
  p2.innerHTML = `Now you are playing against: ${opponent.email}`
  wrapper.appendChild(p1)
  wrapper.appendChild(p2)
  logger.appendChild(wrapper)
}

const setSalvoes = function () {
    for (i = 0; i < gamesData.salvoes.length; i++) {
        for (j = 0; j < gamesData.salvoes[i].locations.length; j++) {
            let turn = gamesData.salvoes[i].turn
            let player = gamesData.salvoes[i].player
            let x = +(gamesData.salvoes[i].locations[j].substring(1)) - 1
            let y = stringToInt(gamesData.salvoes[i].locations[j][0].toUpperCase())
            if (player == actualPlayer.id) {
                document.getElementById(`salvoes${y}${x}`).classList.add('salvo')
                document.getElementById(`salvoes${y}${x}`).innerHTML = `<span>${turn}</span>`
            } else {
                if (document.getElementById(`ships${y}${x}`).className.indexOf('busy-cell') != -1) {
                    document.getElementById(`ships${y}${x}`).classList.remove('busy-cell')
                    document.getElementById(`ships${y}${x}`).classList.add('ship-down')
                    document.getElementById(`ships${y}${x}`).innerHTML = `<span>${turn}</span>`
                }
            }
        }
    }
}

//shots sera un modelo de salvo:
//shot{
//  turn: 1,
//  locations:["A1","C2","G4"]
//}
//Disparar los salvos

function shoot(turno,locations){
     var turno = getTurn();
        var locationsToShoot=[];
        $(".target").each(function(){
            let location = $(this).attr("id").substring(7);
            //"00"
            let locationConverted = String.fromCharCode(parseInt(location[0]) + 65) + (parseInt(location[1]) + 1)
            // +65 --> ASCII
            locationsToShoot.push(locationConverted)
        })
    var url = "/api/games/players/" + getParameterByName("gp") + "/salvoes"
    $.post({
        url: url,
        data: JSON.stringify({turnNumber: turno, salvoLocation:locationsToShoot}),
        dataType: "text",
        contentType: "application/json"
    })
    .done(function (response, status, jqXHR) {
        swal.fire({
        icon: 'success',
        title: "SALVO FIRED!",
        showConfirmButton: false,
        timer: 1000
        });
    })
    .then(function(){
        console.log(hitsConverted);
        $('div[id^="salvoes"].grid-cell.target').removeClass("target").addClass("salvo");
        location.reload();
    })
    .fail(function (jqXHR, status, httpError){
        swal.fire({
        icon: 'error',
        title: "Failed to fire salvo",
        text: status + " " + httpError,
        showConfirmButton: true,
        confirmButtonColor: '#3085d6',
        confirmButtonText: '>:('
        });
        })
    }

function backToMenu(){
    window.location.href = '/web/games.html';
}

function getTurn (){
  var arr=[]
  var turn = 0;
  gamesData.salvoes.map(function(salvo){
    if(salvo.player == actualPlayer.id){
      arr.push(salvo.turn);
    }
  })
  turn = Math.max.apply(Math, arr);

  if (turn == -Infinity){
    return 1;
  } else {
    return turn + 1;
  }

}

function convert(letra){
    if(letra == "A"){
        return "0";
    } else if(letra == "B"){
        return "1";
    } else if(letra == "C"){
        return "2";
    } else if(letra == "D"){
        return "3";
    } else if(letra == "E"){
        return "4";
    } else if(letra == "F"){
        return "5";
    } else if(letra == "G"){
        return "6";
    } else if(letra == "H"){
        return "7";
    } else if(letra == "I"){
        return "8";
    } else if(letra == "J"){
        return "9";
    }
}

var gameEvents = new Vue({
    el: '#gameEvents',
    data:{
        mySalvoHits: [],
        opponentsSalvoHits: []
    }
})
