package com.sportsecho.game.mapper;

import com.sportsecho.game.dto.GameResponseDto;
import com.sportsecho.game.entity.Game;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    GameResponseDto gameToGameResponseDto(Game game);

    GameResponseDto mapToDto(Map<String, Object> gameData);
}

