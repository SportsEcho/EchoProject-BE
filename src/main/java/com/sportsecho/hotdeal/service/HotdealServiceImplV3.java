package com.sportsecho.hotdeal.service;

import com.sportsecho.common.exception.GlobalException;
import com.sportsecho.common.redis.RedisUtil;
import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.PurchaseHotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.SetUpHotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.UpdateHotdealInfoRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.event.HotdealPermissionEventListener;
import com.sportsecho.hotdeal.exception.HotdealErrorCode;
import com.sportsecho.hotdeal.mapper.HotdealMapper;
import com.sportsecho.hotdeal.repository.HotdealRepository;
import com.sportsecho.hotdeal.scheduler.HotdealScheduler;
import com.sportsecho.member.entity.Member;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.exception.ProductErrorCode;
import com.sportsecho.product.repository.ProductRepository;
import com.sportsecho.purchase.repository.PurchaseRepository;
import com.sportsecho.purchaseProduct.repository.PurchaseProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Qualifier("V3")
@Service
@RequiredArgsConstructor
@Slf4j
public class HotdealServiceImplV3 implements HotdealService {

    private final HotdealRepository hotdealRepository;
    private final ProductRepository productRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final PurchaseRepository purchaseRepository;
    private final RedisUtil redisUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final HotdealPermissionEventListener eventListener;
    private final HotdealScheduler hotdealScheduler;

    @Override
    @Transactional
    public HotdealResponseDto createHotdeal(Long productId,
        HotdealRequestDto requestDto) {

        Product product = findProduct(productId);
        Hotdeal hotdeal = Hotdeal.of(requestDto.getStartDay(), requestDto.getDueDay(),
            requestDto.getDealQuantity(), requestDto.getSale(), product);
        hotdealRepository.save(hotdeal);

        return HotdealMapper.INSTANCE.toResponseDto(hotdeal);
    }

    @Override
    @Transactional
    public HotdealResponseDto getHotdeal(Long hotdealId) {
        Hotdeal hotdeal = findHotdeal(hotdealId);

        return HotdealMapper.INSTANCE.toResponseDto(hotdeal);
    }

    @Override
    @Transactional
    public List<HotdealResponseDto> getHotdealList(Pageable pageable) {
        Page<Hotdeal> hotdealPage = hotdealRepository.findAll(pageable);

        return hotdealPage.stream()
            .map(HotdealMapper.INSTANCE::toResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HotdealResponseDto updateHotdeal(Long hotdealId,
        UpdateHotdealInfoRequestDto requestDto) {
        Hotdeal hotdeal = findHotdeal(hotdealId);

        hotdeal.updateHotdealInfo(requestDto.getStartDay(), requestDto.getDueDay(),
            requestDto.getSale(), requestDto.getDealQuantity());

        return HotdealMapper.INSTANCE.toResponseDto(hotdeal);
    }

    @Override
    @Transactional
    public void purchaseHotdeal(Member member, PurchaseHotdealRequestDto requestDto) {

        Long hotdealId = requestDto.getHotdealId();

        Hotdeal hotdeal = hotdealRepository.findByIdWithPessimisticWriteLock(hotdealId)
            .orElseThrow(() -> new GlobalException(HotdealErrorCode.NOT_FOUND_HOTDEAL));

        redisUtil.addQueue(hotdeal, member, requestDto);
    }

    @Override
    @Transactional
    public void setUpHotdeal(Long hotdealId, SetUpHotdealRequestDto requestDto) {
        Hotdeal hotdeal = hotdealRepository.findByIdWithPessimisticWriteLock(hotdealId)
            .orElseThrow(() -> new GlobalException(HotdealErrorCode.NOT_FOUND_HOTDEAL));

        redisUtil.clearQueue(hotdealId);
        redisUtil.setPublishedSize(requestDto.getPublishedSize());
        hotdealScheduler.setHotdeal(hotdeal);
    }

    @Override
    @Transactional
    public void deleteHotdeal(Long hotdealId) {
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

}
