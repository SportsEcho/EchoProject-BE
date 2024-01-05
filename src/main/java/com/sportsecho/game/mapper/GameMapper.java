package com.sportsecho.game.mapper;

import com.sportsecho.game.dto.GameResponseDto;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface GameMapper {
    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    @Mapping(target = "sportType", source = "gameData.sportType")
    @Mapping(target = "teamA", source = "gameData.teamA")
    @Mapping(target = "teamB", source = "gameData.teamB")
    @Mapping(target = "gameDateTime", expression = "java(LocalDateTime.parse(gameData.get(\"gameDateTime\")))")
    @Mapping(target = "location", source = "gameData.location")
    GameResponseDto mapToDto(Map<String, Object> gameData);
}

