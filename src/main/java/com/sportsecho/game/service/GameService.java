package com.sportsecho.game.service;

import com.sportsecho.game.dto.GameResponseDto;
import java.util.List;

public interface GameService {
    List<GameResponseDto> getGamesByDateAndLeague(String date);
}