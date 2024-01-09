// API 키와 호스트 설정
const apiKey = process.env.API_KEY;
const apiHostFootball = 'api-football-v1.p.rapidapi.com';

// 축구 경기 정보를 가져오는 함수
function fetchFootballGames() {
  let apiUrl = 'https://api-football-v1.p.rapidapi.com/v3/timezone';

  $.ajax({
    url: apiUrl,
    method: 'GET',
    headers: {
      'X-RapidAPI-Key': apiKey,
      'X-RapidAPI-Host': apiHostFootball
    }
  }).done(function(response) {
    // 여기에서 response 데이터를 사용하여 축구 경기 일정을 페이지에 표시
    console.log(response);
  });
}

// 축구 댓글을 불러오는 함수
function fetchCommentsForFootball(gameId) {
  fetch(`/api/games/${gameId}/comments`)
  .then(response => response.json())
  .then(comments => {
    const commentsContainer = document.getElementById('comments-container');
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

// 날짜 포맷 함수 (예: YYYY-MM-DD HH:mm 형식)
function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleString();
}

// 페이지 로드 시 축구 경기 정보 및 댓글 불러오기
document.addEventListener('DOMContentLoaded', function() {
  const gameId = '특정 게임 ID'; // 실제 게임 ID로 대체
  fetchFootballGames();
  fetchCommentsForFootball(gameId);
});
