// API 키와 호스트 설정
const apiKey = process.env.API_KEY;
const apiHostBasketball = 'api-basketball.p.rapidapi.com';

// 농구 경기 정보를 가져오는 함수
function fetchBasketballGames() {
  let apiUrl = 'https://api-basketball.p.rapidapi.com/timezone';

  $.ajax({
    url: apiUrl,
    method: 'GET',
    headers: {
      'X-RapidAPI-Key': apiKey,
      'X-RapidAPI-Host': apiHostBasketball
    }
  }).done(function(response) {
    // 여기에서 response 데이터를 사용하여 농구 경기 일정을 페이지에 표시
    console.log(response);
  });
}

// 농구 댓글을 불러오는 함수
function fetchCommentsForBasketball(gameId) {
  fetch(`/api/games/${gameId}/comments`)
  .then(response => response.json())
  .then(comments => {
    const commentsContainer = document.getElementById('basketball-comments-container');
    commentsContainer.innerHTML = ''; // 기존 내용 초기화

    comments.forEach(comment => {
      const commentElement = document.createElement('div');
      commentElement.className = 'comment-item';
      commentElement.innerHTML = `
            <p>${comment.memberName} <span>(${formatDate(comment.createdDate)})</span>: ${comment.content}</p>
          `;
      commentsContainer.appendChild(commentElement);
    });
  })
  .catch(error => console.error('Error:', error));
}

document.addEventListener('DOMContentLoaded', function() {
  const gameId = '특정 농구 게임 ID'; // 실제 게임 ID로 대체
  fetchBasketballGames();
  fetchCommentsForBasketball(gameId);
});
