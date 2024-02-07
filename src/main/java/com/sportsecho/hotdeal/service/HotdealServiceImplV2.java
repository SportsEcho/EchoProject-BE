package com.sportsecho.hotdeal.service;

import com.sportsecho.common.exception.GlobalException;
import com.sportsecho.common.redis.RedisUtil;
import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.PurchaseHotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.SetUpHotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.UpdateHotdealInfoRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.hotdeal.dto.response.HotdealWaitResponse;
import com.sportsecho.hotdeal.dto.response.PurchaseHotdealResponseDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.exception.HotdealErrorCode;
import com.sportsecho.hotdeal.mapper.HotdealMapper;
import com.sportsecho.hotdeal.repository.HotdealRepository;
import com.sportsecho.member.entity.Member;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.exception.ProductErrorCode;
import com.sportsecho.product.repository.ProductRepository;
import com.sportsecho.purchase.entity.Purchase;
import com.sportsecho.purchase.mapper.PurchaseMapper;
import com.sportsecho.purchase.repository.PurchaseRepository;
import com.sportsecho.purchaseProduct.entity.ProductRole;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import com.sportsecho.purchaseProduct.repository.PurchaseProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * V2 업데이트 내용 SortedSet을 이용한 대기열 구현 구매 완료후 해당 유저를 대기열에서 삭제 0개의 재고가 남았을 때 대기열 삭제
 */
@Qualifier("V2")
@Service
@RequiredArgsConstructor
@Slf4j
public class HotdealServiceImplV2 implements HotdealService {

    private final HotdealRepository hotdealRepository;
    private final ProductRepository productRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final PurchaseRepository purchaseRepository;
    private final RedisUtil redisUtil;

    @Override
    @Transactional
    public HotdealResponseDto createHotdeal(Long productId,
        HotdealRequestDto requestDto) {

        Product product = findProduct(productId);
        Hotdeal hotdeal = HotdealMapper.INSTANCE.toEntity(requestDto, product);
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
    public PurchaseHotdealResponseDto purchaseHotdeal(Member member,
        PurchaseHotdealRequestDto requestDto) {

        Hotdeal hotdeal = hotdealRepository.findByIdWithPessimisticWriteLock(
                requestDto.getHotdealId())
            .orElseThrow(() -> new GlobalException(HotdealErrorCode.NOT_FOUND_HOTDEAL));

        if (hotdeal.getDealQuantity() == 0) {
            throw new GlobalException(HotdealErrorCode.SOLD_OUT); // 핫딜 재고 0일때
        }

        if (hotdeal.getDealQuantity() < requestDto.getQuantity()) {
            throw new GlobalException(
                HotdealErrorCode.LACK_DEAL_QUANTITY); // 핫딜 구매시 재고보다 많은 수량 구매 시도
        }

        Long hotdealId = hotdeal.getId();
        if (!redisUtil.getOldHotdealWaitSet(String.valueOf(hotdealId),
            redisUtil.getQueueSize(String.valueOf(hotdealId))).contains(member.getEmail())) {
            throw new GlobalException(HotdealErrorCode.NOT_IN_WAIT_QUEUE); // 대기열에 없는 유저의 구매 시도 방지
        }

        Product product = hotdeal.getProduct();
        int dicountedPrice = product.getPrice() - (product.getPrice() * hotdeal.getSale() / 100);

        Purchase purchase = PurchaseMapper.INSTANCE.fromPurchaseHotdealReqeustDto(requestDto,
            dicountedPrice, member);
        purchaseRepository.save(purchase);
        purchase.setCreatedAt(LocalDateTime.now());

        PurchaseProduct purchaseProduct = PurchaseProduct.builder()
            .purchase(purchase)
            .product(product)
            .productsQuantity(requestDto.getQuantity())
            .role(ProductRole.HOTDEAL)
            .build();
        purchaseProductRepository.save(purchaseProduct);

        hotdeal.updateDealQuantity(
            hotdeal.getDealQuantity() - requestDto.getQuantity()); // 앞에서 예외처리 완료
        hotdealRepository.save(hotdeal); // 더티 체킹

        redisUtil.removePurchaseRequestFromQueue(String.valueOf(hotdeal.getId()),
            member.getEmail());

        if (hotdeal.getDealQuantity() == 0) {
            redisUtil.deleteOldHotdealWaitSet(String.valueOf(hotdeal.getId()));
        }

        return HotdealMapper.INSTANCE.toPurchaseResponseDto(hotdeal);
    }

    @Override
    @Transactional
    public HotdealWaitResponse waitHotdeal(String hotdealId, Member member) {

        Hotdeal hotdeal = findHotdeal(Long.parseLong(hotdealId));
        int waitCount = redisUtil.getQueueSize(hotdealId);

        if (hotdeal.getDealQuantity() < waitCount * 10) { // 발급 전에 재고 확인후 대기열 추가
            return new HotdealWaitResponse("현재 구매 요청이 많아 대기열에 추가할 수 없습니다", waitCount);
        }

        redisUtil.addPurchaseHotdealMemberToQueueString(hotdealId, member.getEmail(),
            System.currentTimeMillis());

        return new HotdealWaitResponse("success", waitCount + 1);
    }

    @Override
    @Transactional
    public void deleteHotdealWaitingMember(Member member, String hotdealId) {
        redisUtil.removePurchaseRequestFromQueue(hotdealId, member.getEmail());
    }

    @Override
    @Transactional
    public boolean isMyHotdealTurn(Member member, String hotdealId) {
        Set<String> oldHotdealWaitSet = redisUtil.getOldHotdealWaitSet(hotdealId,
            10); // 대기열에서 10명씩 허가
        if (oldHotdealWaitSet == null) {
            throw new GlobalException(HotdealErrorCode.SOLD_OUT);
        }
        return oldHotdealWaitSet.contains(member.getEmail());
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

    @Override
    public void purchaseHotdealV3(Member member, PurchaseHotdealRequestDto requestDto) {

    }

    @Override
    public void setUpHotdeal(Long hotdealId, SetUpHotdealRequestDto requestDto) {

    }
}
