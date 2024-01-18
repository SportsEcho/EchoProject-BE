package com.sportsecho.gamechat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {
    private Long id;
    private String type;
    private String value;
}
