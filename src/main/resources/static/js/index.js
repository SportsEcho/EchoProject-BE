// 경기 정보를 가져오는 함수
function fetchGamesBySport(sportType) {
  fetch(`/api/games?sportType=${sportType}`)
  .then(response => response.json())
  .then(data => {
    // 경기 일정을 페이지에 표시하는 로직
  })
  .catch(error => console.error('Error:', error));
}

// 드롭다운 메뉴를 토글하는 함수
function toggleDropdown() {
  var dropdown = document.getElementById("sports-dropdown");
  dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
}

// "경기 일정" 링크에 이벤트 리스너 추가
document.querySelector('nav > ul > li > a[href="#calendar"]').addEventListener('click', function(event) {
  event.preventDefault(); // 기본 링크 동작 방지
  toggleDropdown();
});