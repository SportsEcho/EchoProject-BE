package com.sportsecho.game.service;

import com.sportsecho.common.dto.ApiResponse;
import com.sportsecho.common.dto.ResponseCode;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.game.dto.GameResponseDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.sportsecho.global.exception.ErrorCode;
@Service
public class GameService {

    private final RestTemplate restTemplate;

    @Autowired
    public GameService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ApiResponse<List<GameResponseDto>> getGamesBySport(String sportType) {
        String apiUrl = determineApiUrl(sportType);
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity(apiUrl, ApiResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<GameResponseDto> matches = convertToGamesponseDtoList(response.getBody().getData());
            return ApiResponse.of(ResponseCode.OK, matches);
        } else {
            throw new GlobalException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    private List<GameResponseDto> convertToGamesponseDtoList(Object apiResponseData) {
        if (!(apiResponseData instanceof List<?>)) {
            throw new GlobalException(ErrorCode.INVALID_API_RESPONSE);
        }

        List<?> responseDataList = (List<?>) apiResponseData;
        List<GameResponseDto> gameResponseDtos = new ArrayList<>();

        for (Object responseData : responseDataList) {
            // JSON 객체를 Map으로 캐스팅할 수 있도록 처리해야 함
            Map<String, Object> gameData = (Map<String, Object>) responseData;

            GameResponseDto gameResponseDto = new GameResponseDto();
            gameResponseDto.setTeamA((String) gameData.get("teamA"));
            gameResponseDto.setTeamB((String) gameData.get("teamB"));
            gameResponseDto.setGameDateTime(LocalDateTime.parse((String) gameData.get("gameDateTime")));
            gameResponseDto.setLocation((String) gameData.get("location"));

            gameResponseDtos.add(gameResponseDto);
        }

        return gameResponseDtos;
    }


    private String determineApiUrl(String sportType) {
        // sportType에 따라 다른 API URL 반환
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