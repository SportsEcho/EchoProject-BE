package com.sportsecho.hotdeal.mapper;

import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.hotdeal.dto.response.PurchaseHotdealResponseDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.product.entity.ProductImage;
import java.time.Duration;
import java.time.LocalDateTime;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HotdealMapper {

    HotdealMapper INSTANCE = Mappers.getMapper(HotdealMapper.class);

    @Mapping(target = "product", ignore = true)
    Hotdeal toEntity(HotdealRequestDto requestDto);

    @Mapping(source = "product.title", target = "title")
    @Mapping(source = "product.content", target = "content")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "sale", target = "sale")
    @Mapping(source = "startDay", target = "startDay")
    @Mapping(source = "dueDay", target = "dueDay")
    HotdealResponseDto toResponseDto(Hotdeal entity);

    @Mapping(source = "product.title", target = "title")
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "remainQuantity", ignore = true) // remainQuantity는 커스텀 매핑을 사용
    @Mapping(target = "remainTime", ignore = true)
        // remainTime은 커스텀 매핑을 사용
    PurchaseHotdealResponseDto toPurchaseResponseDto(Hotdeal hotdeal);

    @AfterMapping
    default void afterMappingToPurchaseResponseDto(Hotdeal hotdeal,
        @MappingTarget PurchaseHotdealResponseDto.PurchaseHotdealResponseDtoBuilder dtoBuilder) {
        dtoBuilder.price(calculateDiscountPrice(hotdeal.getProduct().getPrice(), hotdeal.getSale()))
            .remainQuantity(hotdeal.getDealQuantity()) // 남은 수량은 모든 로직 이후 update로 처리
            .remainTime(calculateRemainTime(hotdeal.getDueDay()));
    }

    @AfterMapping
    default void afterMappingToHotdealResponseDto(Hotdeal hotdeal,
        @MappingTarget HotdealResponseDto.HotdealResponseDtoBuilder dtoBuilder) {

        dtoBuilder.imageUrlList(
            hotdeal.getProduct().getProductImageList().stream()
                .map(ProductImage::getImageUrl)
                .toList()
        );
    }

    // helper 메서드들을 구현
    private int calculateDiscountPrice(int originalPrice, int sale) {
        return originalPrice - (originalPrice * sale / 100);
    }

    private int calculateRemainQuantity(int dealQuantity, int soldQuantity) {
        return dealQuantity - soldQuantity;
    }

    private String calculateRemainTime(LocalDateTime dueDay) {
        Duration duration = Duration.between(LocalDateTime.now(), dueDay);
        return formatDuration(duration); // 'formatDuration'은 Duration을 원하는 형식의 문자열로 변환
    }

    private String formatDuration(Duration duration) {
        // Duration을 원하는 문자열 형식으로 변환하는 로직 구현
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%dh %dmin", hours, minutes);
    }

}
