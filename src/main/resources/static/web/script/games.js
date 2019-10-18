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
$('#login-form').on('submit', function (event) {
    event.preventDefault();
    if (submitButton == "login") {
        $.post("/api/login",
            { username: $("#username").val(),
                password: $("#password").val() })
            .done(function() {
                console.log("login ok");
                $('#loginSuccess').show( "slow" ).delay(2000).hide( "slow" );
                // $("#username").val("");
                $("#password").val("");
                updateJson();
            })
            .fail(function() {
                console.log("login failed");
                $('#loginFailed').show( "slow" ).delay(2000).hide( "slow" );
                $("#username").val("");
                $("#password").val("");
                $("#username").focus();
                // $('#loginFailed').hide( "slow" );
            })
            .always(function() {
            });
    }

 $(function() {
     $('.submitbutton').click(function () {
         submitButton = $(this).attr('name')
     });
 });