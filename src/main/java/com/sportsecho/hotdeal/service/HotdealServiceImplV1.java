package com.sportsecho.hotdeal.service;

import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.UpdateHotdealInfoRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.exception.HotdealErrorCode;
import com.sportsecho.hotdeal.exception.HotdealException;
import com.sportsecho.hotdeal.mapper.HotdealMapper;
import com.sportsecho.hotdeal.repository.HotdealRepository;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.exception.ProductErrorCode;
import com.sportsecho.product.repository.ProductRepository;
import com.sportsecho.product.service.ProductService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Qualifier("V1")
@Service
@RequiredArgsConstructor
public class HotdealServiceImplV1 implements HotdealService {

    private final HotdealRepository hotdealRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public HotdealResponseDto createHotdeal(Member member, Long productId,
        HotdealRequestDto requestDto) {
        if (!isAuthorized(member)) {
            throw new GlobalException(HotdealErrorCode.NO_AUTHORIZATION);
        }

        Product product = findProduct(productId);
        Hotdeal hotdeal = Hotdeal.of(requestDto.getStartDay(), requestDto.getDueDay(),
            requestDto.getDealQuantity(), requestDto.getSale(), product);
        hotdealRepository.save(hotdeal);

        return HotdealMapper.INSTANCE.toResponseDto(hotdeal);
    }

    @Override
    public HotdealResponseDto getHotdeal(Long hotdealId) {
        Hotdeal hotdeal = findHotdeal(hotdealId);

        return HotdealMapper.INSTANCE.toResponseDto(hotdeal);
    }

    @Override
    public List<HotdealResponseDto> getHotdealList(Pageable pageable) {
        Page<Hotdeal> hotdealPage = hotdealRepository.findAll(pageable);

        return hotdealPage.stream()
            .map(HotdealMapper.INSTANCE::toResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HotdealResponseDto updateHotdeal(Member member, Long hotdealId,
        UpdateHotdealInfoRequestDto requestDto) {
        if (!isAuthorized(member)) {
            throw new GlobalException(HotdealErrorCode.NO_AUTHORIZATION);
        }
        Hotdeal hotdeal = findHotdeal(hotdealId);

        hotdeal.updateHotdealInfo(requestDto.getStartDay(), requestDto.getDueDay(),
            requestDto.getSale(), requestDto.getDealQuantity());

        return HotdealMapper.INSTANCE.toResponseDto(hotdeal);
    }

    @Override
    @Transactional
    public void decreaseHotdealDealQuantity(Member member, Long hotdealId ,int quantity) {
        if (!isAuthorized(member)) {
            throw new GlobalException(HotdealErrorCode.NO_AUTHORIZATION);
        }
        Hotdeal hotdeal = findHotdeal(hotdealId);

        if (hotdeal.getDealQuantity() < quantity) {
            throw new GlobalException(HotdealErrorCode.LACK_DEAL_QUANTITY); // 에러 코드 이름 피드백 받아요
        }

        hotdeal.updateDealQuantity(hotdeal.getDealQuantity() - quantity);
    }

    @Override
    @Transactional
    public void deleteHotdeal(Member member, Long hotdealId) {
        if (!isAuthorized(member)) {
            throw new GlobalException(HotdealErrorCode.NO_AUTHORIZATION);
        }
        Hotdeal hotdeal = findHotdeal(hotdealId);

        hotdealRepository.delete(hotdeal);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new GlobalException(ProductErrorCode.NOT_FOUND_PRODUCT));
    }

    private Hotdeal findHotdeal(Long hotdealId) {
        return hotdealRepository.findById(hotdealId)
            .orElseThrow(() -> new GlobalException(HotdealErrorCode.NOT_FOUND_HOTDEAL));
    }

    private Boolean isAuthorized(Member member) {
        return member.getRole().equals(MemberRole.ADMIN);
    }

    //GlobalException(GameErrorCode.EXTERNAL_API_ERROR)
}
