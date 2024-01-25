package com.sportsecho.game.mapper;

import com.sportsecho.game.dto.GameResponseDto;
import com.sportsecho.game.entity.Game;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    GameResponseDto gameToGameResponseDto(Game game);

}