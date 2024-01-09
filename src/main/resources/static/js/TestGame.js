function loadGames(sportType) {
  $.ajax({
    url: '/api/games/' + sportType,
    method: 'GET',
    success: function(response) {
      displayGames(response.data);
    },
    error: function(error) {
      console.error("Error loading games: ", error);
      alert("경기 정보를 불러오는데 실패했습니다.");
    }
  });
}

function displayGames(games) {
  var gamesContainer = $('#games');
  gamesContainer.empty();

  games.forEach(function(game) {
    var gameHtml = `
                    <div class="games">
                        <h3>${game.teamA} vs ${game.teamB}</h3>
                        <p>경기 시간: ${game.gameDateTime}</p>
                        <p>장소: ${game.location}</p>
                        <!-- 댓글 기능 추가 가능 -->
                    </div>
                `;
    gamesContainer.append(gameHtml);
  });
}