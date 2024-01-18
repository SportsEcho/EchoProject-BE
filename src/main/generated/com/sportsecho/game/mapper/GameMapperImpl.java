package com.sportsecho.game.mapper;

import com.sportsecho.game.dto.GameResponseDto;
import com.sportsecho.game.dto.GameResponseDto.GameResponseDtoBuilder;
import com.sportsecho.game.entity.Game;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-18T19:57:43+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class GameMapperImpl implements GameMapper {

    @Override
    public GameResponseDto gameToGameResponseDto(Game game) {
        if ( game == null ) {
            return null;
        }

        GameResponseDtoBuilder gameResponseDto = GameResponseDto.builder();

        gameResponseDto.sportType( game.getSportType() );
        gameResponseDto.teamA( game.getTeamA() );
        gameResponseDto.teamB( game.getTeamB() );
        gameResponseDto.gameDateTime( convertToDateTime( game.getGameDateTime() ) );
        gameResponseDto.location( game.getLocation() );

        return gameResponseDto.build();
    }

    @Override
    public GameResponseDto mapToDto(Map<String, Object> gameData) {
        if ( gameData == null ) {
            return null;
        }

        GameResponseDtoBuilder gameResponseDto = GameResponseDto.builder();

        gameResponseDto.sportType( getString(gameData, "sportType") );
        gameResponseDto.teamA( getString(gameData, "teamA") );
        gameResponseDto.teamB( getString(gameData, "teamB") );
        gameResponseDto.location( getString(gameData, "location") );
        gameResponseDto.gameDateTime( convertToDateTime(gameData.get("gameDateTime")) );

        return gameResponseDto.build();
    }
}
