package com.sportsecho.hotdeal.mapper;

import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HotdealMapper {

    HotdealMapper INSTANCE = Mappers.getMapper(HotdealMapper.class);

    @Mapping(target = "product", ignore = true) // request에 product는 pathVariable로 받고 있기에 ignore
    Hotdeal toEntity(HotdealRequestDto requestDto);

    HotdealRequestDto toRequestDto(Hotdeal entity);

    @Mapping(source = "product.title", target = "title")
    @Mapping(source = "product.content", target = "content")
    @Mapping(source = "product.imageUrl", target = "imageUrl")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "sale", target = "sale")
    @Mapping(source = "startDay", target = "startDay")
    @Mapping(source = "dueDay", target = "dueDay")
    HotdealResponseDto toResponseDto(Hotdeal entity);

}
