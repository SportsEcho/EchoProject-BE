package com.sportsecho.game.service;

import com.sportsecho.game.dto.GameResponseDto;
import com.sportsecho.game.entity.Game;
import com.sportsecho.game.exception.GameErrorCode;
import com.sportsecho.game.mapper.GameMapper;
import com.sportsecho.game.repository.GameRepository;
import com.sportsecho.common.exception.GlobalException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("V2")
@RequiredArgsConstructor
@Slf4j
public class GameServiceImplV2 implements GameService{

    private final GameRepository gameRepository;

    @Override
    public List<GameResponseDto> getGamesByDateAndLeague(String date) {
        List<Game> gameList = gameRepository.findByDate(date);
        List<GameResponseDto>  gameResponseDtoList = new ArrayList<>();

        gameList.forEach(game -> {
            gameResponseDtoList.add(GameMapper.INSTANCE.gameToGameResponseDto(game));
        });

        return gameResponseDtoList;
    }

    @Override
    public GameResponseDto getGameDetails(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(
            () -> new GlobalException(GameErrorCode.GAME_NOT_FOUND)
        );

        return GameMapper.INSTANCE.gameToGameResponseDto(game);
    }
}
