package com.sportsecho.game.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsecho.game.dto.GameResponseDto;
import com.sportsecho.game.exception.GameErrorCode;
import com.sportsecho.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service("gameServiceImplV1")
@RequiredArgsConstructor
public class GameServiceImplV1{
    private final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Override
    public List<GameResponseDto> getGamesByDateAndLeague(String date, String league) {
        try {
            String apiUrl = determineApiUrl(league, date);
            CompletableFuture<Response> future = asyncHttpClient.prepareGet(apiUrl)
                .setHeader("X-RapidAPI-Key", "your-api-key")
                .setHeader("X-RapidAPI-Host", "your-api-host")
                .execute()
                .toCompletableFuture();

            Response response = future.join();
            if (response.getStatusCode() == HttpStatus.OK.value()) {
                // JSON 응답 처리 및 GameResponseDto로 변환
                List<GameResponseDto> games = objectMapper.readValue(
                    response.getResponseBody(),
                    new TypeReference<List<GameResponseDto>>() {}
                );
                return games;
            } else {
                // 오류 처리
                throw new GlobalException(GameErrorCode.EXTERNAL_API_ERROR);
            }
        } catch (Exception e) {
            // 예외 처리
            throw new GlobalException(GameErrorCode.INVALID_API_RESPONSE, e.getMessage());
        }
    }
    private String determineApiUrl(String league, String date) {
        switch (league.toLowerCase()) {
            case "epl":
                return "https://api-football-v1.p.rapidapi.com/v3/fixtures?date=" + date;
            case "nba":
                return "https://api-basketball.p.rapidapi.com/games?date=" + date;
            case "mlb":
                return "https://api-baseball.p.rapidapi.com/games?id=1&date=" + date;
            default:
                throw new IllegalArgumentException("Invalid league type");
        }
    }
}
