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

    @Scheduled(fixedRate = 900000) // 매 15분마다 실행 (900000ms = 30분) = 하루 96번 호출
    public void updateTodayGame() throws IOException, InterruptedException, JSONException {

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        String todayString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(
                "https://api-football-v1.p.rapidapi.com/v3/fixtures?date=" + todayString
                    + "&league=39&season=2023"))
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd'T'HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(
                    ZoneId.of("Asia/Seoul"));

            Game game = Game.createGame(
                teams.getJSONObject("home").getString("name"),
                teams.getJSONObject("home").getString("logo"),
                teams.getJSONObject("away").getString("name"),
                teams.getJSONObject("away").getString("logo"),
                league.getString("logo"),
                zonedDateTime.toLocalDateTime(),
                venue.getString("name"),
                goals.getJSONObject("home").getString("score"),
                goals.getJSONObject("away").getString("score")
            );
            log.info(game.toString());
            gameRepository.save(game);

        }

    }

    //    @Scheduled(fixedRate = 86400000) // 매일 자정에 실행 (86400000ms = 24시간)
    @Scheduled(cron = "* * * * * *")
    public void fetchUpcomingGames() throws IOException, InterruptedException, JSONException {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate oneMonthLater = now.plusMonths(1);

        String from = now.format(DateTimeFormatter.ISO_DATE);
        String to = oneMonthLater.format(DateTimeFormatter.ISO_DATE);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(
                "https://api-football-v1.p.rapidapi.com/v3/fixtures?date=2024-01-17&league=39&season=2023&from="
                    + from + "&to=" + to))
            .header("X-RapidAPI-Key", "d789e7aa74msh95a2867cc80a6d0p11239ajsna2c01db4ee85")
            .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());

        String jsonData = response.body();
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray gameList = jsonObject.getJSONArray("response");

        for (int i = 0; i < gameList.length(); i++) {
            JSONObject fixture = gameList.getJSONObject(i).getJSONObject("fixture");
            JSONObject teams = gameList.getJSONObject(i).getJSONObject("teams");
            JSONObject league = gameList.getJSONObject(i).getJSONObject("league");
            JSONObject venue = fixture.getJSONObject("venue");
            JSONObject goals = gameList.getJSONObject(i).getJSONObject("goals");


            String fixtureDateString = fixture.getString("date");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(fixtureDateString, formatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"));

            Game game = Game.createGame(
                teams.getJSONObject("home").getString("name"),
                teams.getJSONObject("home").getString("logo"),
                teams.getJSONObject("away").getString("name"),
                teams.getJSONObject("away").getString("logo"),
                league.getString("logo"),
                zonedDateTime.toLocalDateTime(),
                venue.getString("name"),
                goals.getJSONObject("home").getString("score"),
                goals.getJSONObject("away").getString("score")
            );

            log.info("Fetching game for " + zonedDateTime.toLocalDate() + ": " + game.toString());
            gameRepository.save(game);
        }
    }
}