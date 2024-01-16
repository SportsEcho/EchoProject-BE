package com.sportsecho.game.mapper;

import com.sportsecho.game.dto.GameResponseDto;
import com.sportsecho.game.entity.Game;
import java.time.LocalDateTime;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    GameResponseDto gameToGameResponseDto(Game game);

    @Mapping(target = "sportType", expression = "java(getString(gameData, \"sportType\"))")
    @Mapping(target = "teamA", expression = "java(getString(gameData, \"teamA\"))")
    @Mapping(target = "teamB", expression = "java(getString(gameData, \"teamB\"))")
    @Mapping(target = "location", expression = "java(getString(gameData, \"location\"))")
    @Mapping(target = "gameDateTime", expression = "java(convertToDateTime(gameData.get(\"gameDateTime\")))")
    GameResponseDto mapToDto(Map<String, Object> gameData);

    default String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    default LocalDateTime convertToDateTime(Object gameDateTime) {
        return gameDateTime == null ? null : LocalDateTime.parse(gameDateTime.toString());
    }
}