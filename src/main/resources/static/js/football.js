// API 키와 호스트는 변수로 저장하여 재사용
const apiKey = 'd789e7aa74msh95a2867cc80a6d0p11239ajsna2c01db4ee85';
const apiHostFootball = 'api-football-v1.p.rapidapi.com';
const apiHostBasketball = 'api-basketball.p.rapidapi.com';
const apiHostBaseball = 'api-baseball.p.rapidapi.com';

// 경기 정보를 가져오는 함수
function fetchGames(sportType) {
  let apiUrl, apiHost;

  switch (sportType) {
    case 'football':
      apiUrl = 'https://api-football-v1.p.rapidapi.com/v3/timezone';
      apiHost = apiHostFootball;
      break;
    case 'basketball':
      apiUrl = 'https://api-basketball.p.rapidapi.com/timezone';
      apiHost = apiHostBasketball;
      break;
    case 'baseball':
      apiUrl = 'https://api-baseball.p.rapidapi.com/timezone';
      apiHost = apiHostBaseball;
      break;
    default:
      return;
  }

  $.ajax({
    url: apiUrl,
    method: 'GET',
    headers: {
      'X-RapidAPI-Key': apiKey,
      'X-RapidAPI-Host': apiHost
    }
  }).done(function(response) {
    // 여기에서 response 데이터를 사용하여 경기 일정을 페이지에 표시
    console.log(response);
  });
}

// 축구 경기 정보 가져오기
fetchGames('football').then(response => {
  // 여기에서 response 데이터를 사용하여 축구 경기 일정을 페이지에 표시
  // 예: document.getElementById('football-games-schedule').innerHTML = ...
});