package com.sportsecho.hotdeal.service;

import com.sportsecho.common.exception.GlobalException;
import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.PurchaseHotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.UpdateHotdealInfoRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.hotdeal.dto.response.PurchaseHotdealResponseDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.exception.HotdealErrorCode;
import com.sportsecho.hotdeal.mapper.HotdealMapper;
import com.sportsecho.hotdeal.repository.HotdealRepository;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.exception.ProductErrorCode;
import com.sportsecho.product.repository.ProductRepository;
import com.sportsecho.purchase.entity.Purchase;
import com.sportsecho.purchase.mapper.PurchaseMapper;
import com.sportsecho.purchase.repository.PurchaseRepository;
import com.sportsecho.purchaseProduct.entity.ProductRole;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import com.sportsecho.purchaseProduct.repository.PurchaseProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Qualifier("V1")
@Service
@RequiredArgsConstructor
@Slf4j
public class HotdealServiceImplV1 implements HotdealService {

    private final HotdealRepository hotdealRepository;
    private final ProductRepository productRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final PurchaseRepository purchaseRepository;

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
    public PurchaseHotdealResponseDto purchaseHotdeal(Member member, PurchaseHotdealRequestDto requestDto) {
        Hotdeal hotdeal = hotdealRepository.findByIdWithPessimisticWriteLock(requestDto.getHotdealId())
            .orElseThrow(() -> new GlobalException(HotdealErrorCode.NOT_FOUND_HOTDEAL));

        if (hotdeal.getDealQuantity() == 0) {
            throw new GlobalException(HotdealErrorCode.SOLD_OUT); // 핫딜 재고 0일때
        }

        if (hotdeal.getDealQuantity() < requestDto.getQuantity()) {
            throw new GlobalException(HotdealErrorCode.LACK_DEAL_QUANTITY); // 핫딜 구매시 재고보다 많은 수량 구매 시도
        }

        Product product = hotdeal.getProduct();
        int dicountedPrice = product.getPrice() - (product.getPrice() * hotdeal.getSale() / 100);

        Purchase purchase = PurchaseMapper.INSTANCE.fromPurchaseHotdealReqeustDto(requestDto, dicountedPrice, member);
        purchaseRepository.save(purchase);

        PurchaseProduct purchaseProduct = PurchaseProduct.builder()
            .product(product)
            .productsQuantity(requestDto.getQuantity())
            .role(ProductRole.HOTDEAL)
            .build();
        purchaseProductRepository.save(purchaseProduct);

        hotdeal.updateDealQuantity(hotdeal.getDealQuantity() - requestDto.getQuantity()); // 앞에서 예외처리 완료
        hotdealRepository.save(hotdeal); // 더티 체킹

        return HotdealMapper.INSTANCE.toPurchaseResponseDto(hotdeal);
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
