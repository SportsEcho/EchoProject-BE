package com.sportsecho.game.scheduler;


import com.sportsecho.game.entity.Game;
import com.sportsecho.game.repository.GameRepository;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameScheduler {

    private final GameRepository gameRepository;

    public static String calculateSeason(String sport, LocalDate date) {
        // 스포츠별 시즌 시작 월 정의
        int seasonStartMonth;
        switch (sport) {
            case "EPL":
                seasonStartMonth = Month.AUGUST.getValue(); // 8월
                break;
            case "NBA":
                seasonStartMonth = Month.OCTOBER.getValue(); // 10월
                break;
            case "MLB":
                seasonStartMonth = Month.MARCH.getValue(); // 3월
                break;
            default:
                throw new IllegalArgumentException("Unknown sport: " + sport);
        }

        int currentYear = date.getYear();
        // 현재 날짜가 시즌 시작 월 이전인 경우, 시즌은 이전 연도에 시작함
        return String.valueOf(
            (date.getMonthValue() < seasonStartMonth) ? currentYear - 1 : currentYear);
    }

//    @Scheduled(fixedRate = 900000) // 매 15분마다 실행 (900000ms = 30분) = 하루 96번 호출
    @Scheduled(cron = "0 0 * * * ?") // 과금 이슈로 최종 베포 전까지 한 시간 마다 수행
    public void updateTodayGame() throws IOException, InterruptedException, JSONException {

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        String todayString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String season = calculateSeason("EPL", today);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(
                "https://api-football-v1.p.rapidapi.com/v3/fixtures?date=" + todayString
                    + "&league=39&season=" + season + "&timezone=timezone%3DAsia%252FSeoul"))
            .header("X-RapidAPI-Key", "d789e7aa74msh95a2867cc80a6d0p11239ajsna2c01db4ee85")
            .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());

        String jsonData = response.body();
        JSONObject jsonObject = new JSONObject(jsonData);

        if (jsonObject.getInt("results") == 0) {
            log.info("경기 일정이 없습니다");
            return;
        }

        JSONArray gameList = jsonObject.getJSONArray("response");

        for (int i = 0; i < gameList.length(); i++) {
            JSONObject fixture = gameList.getJSONObject(i).getJSONObject("fixture");
            JSONObject teams = gameList.getJSONObject(i).getJSONObject("teams");
            JSONObject league = gameList.getJSONObject(i).getJSONObject("league");
            JSONObject venue = fixture.getJSONObject("venue");
            JSONObject goals = gameList.getJSONObject(i).getJSONObject("goals");

            String dateString = fixture.getString("date");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);

            // 이미 시작하지 않은 경기는 null값을 주고 있으므로 0으로 처리
            int homeScore = goals.isNull("home") ? 0 : goals.getInt("home");
            int awayScore = goals.isNull("away") ? 0 : goals.getInt("away");

            Game game = Game.createGame(
                teams.getJSONObject("home").getString("name"),
                teams.getJSONObject("home").getString("logo"),
                teams.getJSONObject("away").getString("name"),
                teams.getJSONObject("away").getString("logo"),
                league.getString("logo"),
                localDateTime,
                venue.getString("name"),
                homeScore,
                awayScore
            );
            if (!gameRepository.existsGameByDate(localDateTime)) {
                gameRepository.save(game);
            }

        }

    }

    @Scheduled(cron = "0 0 0 20 * ?") // 월 단위로 진행 자정에 실행
    public void fetchUpcomingGames() throws IOException, InterruptedException, JSONException {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate oneMonthLater = now.plusMonths(1);
        String season = calculateSeason("EPL", now);

        String from = now.format(DateTimeFormatter.ISO_DATE);
        String to = oneMonthLater.format(DateTimeFormatter.ISO_DATE);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(
                "https://api-football-v1.p.rapidapi.com/v3/fixtures?&league=39" +
                    "&season=" + season +
                    "&from=" + from + "&to=" + to +
                    "&timezone=timezone%3DAsia%252FSeoul"))
            .header("X-RapidAPI-Key", "d789e7aa74msh95a2867cc80a6d0p11239ajsna2c01db4ee85")
            .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());

        String jsonData = response.body();
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray gameList = jsonObject.getJSONArray("response");
        log.info(gameList.toString());

        for (int i = 0; i < gameList.length(); i++) {
            JSONObject fixture = gameList.getJSONObject(i).getJSONObject("fixture");
            JSONObject teams = gameList.getJSONObject(i).getJSONObject("teams");
            JSONObject league = gameList.getJSONObject(i).getJSONObject("league");
            JSONObject venue = fixture.getJSONObject("venue");
            JSONObject goals = gameList.getJSONObject(i).getJSONObject("goals");

            String dateString = fixture.getString("date");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);

            // 이미 시작하지 않은 경기는 null값을 주고 있으므로 0으로 처리
            int homeScore = goals.isNull("home") ? 0 : goals.getInt("home");
            int awayScore = goals.isNull("away") ? 0 : goals.getInt("away");

            Game game = Game.createGame(
                teams.getJSONObject("home").getString("name"),
                teams.getJSONObject("home").getString("logo"),
                teams.getJSONObject("away").getString("name"),
                teams.getJSONObject("away").getString("logo"),
                league.getString("logo"),
                localDateTime,
                venue.getString("name"),
                homeScore,
                awayScore
            );
            log.info(game.toString());
            gameRepository.save(game);

        }
    }
}