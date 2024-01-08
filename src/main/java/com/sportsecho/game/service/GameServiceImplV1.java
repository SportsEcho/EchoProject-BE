package com.sportsecho.game.service;

import com.sportsecho.game.dto.GameResponseDto;
import com.sportsecho.game.exception.GameErrorCode;
import com.sportsecho.global.exception.GlobalException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service("gameServiceImplV1")
@RequiredArgsConstructor
public class GameServiceImplV1 implements GameService {
    private final RestTemplate restTemplate;

    @Override
    public List<GameResponseDto> getGamesBySport(String sportType) {
        String apiUrl = determineApiUrl(sportType);
        ResponseEntity<List<GameResponseDto>> response = restTemplate.exchange(
            apiUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<GameResponseDto>>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new GlobalException(GameErrorCode.EXTERNAL_API_ERROR);
        }
    }

    private String determineApiUrl(String sportType) {
        switch (sportType.toLowerCase()) {
            case "football":
                return "https://api-football-v1.p.rapidapi.com/v3/timezone";
            case "basketball":
                return "https://api-basketball.p.rapidapi.com/timezone";
            case "baseball":
                return "https://api-baseball.p.rapidapi.com/timezone";
            default:
                throw new IllegalArgumentException("Invalid sport type");
        }
    }
}
