package com.sportsecho.hotdeal.event;

import com.sportsecho.common.exception.GlobalException;
import com.sportsecho.hotdeal.dto.response.PurchaseHotdealResponseDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.exception.HotdealErrorCode;
import com.sportsecho.hotdeal.mapper.HotdealMapper;
import com.sportsecho.hotdeal.repository.HotdealRepository;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.product.entity.Product;
import com.sportsecho.purchase.entity.Purchase;
import com.sportsecho.purchase.repository.PurchaseRepository;
import com.sportsecho.purchaseProduct.entity.ProductRole;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import com.sportsecho.purchaseProduct.repository.PurchaseProductRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class HotdealPermissionEventListener {

    private final MemberRepository memberRepository;
    private final PurchaseRepository purchaseRepository;
    private final HotdealRepository hotdealRepository;
    private final PurchaseProductRepository purchaseProductRepository;

    @EventListener
    @Transactional
    public PurchaseHotdealResponseDto handleUserPermissionGrantedEvent(
        HotdealPermissionEvent event) {

        Hotdeal hotdeal = event.getHotdeal();
        Long memberId = event.getMemberId();
        int quantity = event.getQuantity();

        if (hotdeal.getDealQuantity() == 0) {
            throw new GlobalException(HotdealErrorCode.SOLD_OUT); // 핫딜 재고 0일때
        }

        if (hotdeal.getDealQuantity() < quantity) {
            throw new GlobalException(
                HotdealErrorCode.LACK_DEAL_QUANTITY); // 핫딜 구매시 재고보다 많은 수량 구매 시도
        }

        Product product = hotdeal.getProduct();
        int discountedPrice = product.getPrice() - (product.getPrice() * hotdeal.getSale() / 100);

        Member member = memberRepository.findById(memberId).orElse(null);
        Purchase purchase = Purchase.createEntity(event.getAddress(), event.getPhone(), member);
        purchase = purchaseRepository.save(purchase);
        purchase.setCreatedAt(LocalDateTime.now());

        PurchaseProduct purchaseProduct = PurchaseProduct.builder()
            .product(product)
            .productsQuantity(quantity)
            .role(ProductRole.HOTDEAL)
            .build();
        purchaseProductRepository.save(purchaseProduct);

        hotdeal.updateDealQuantity(hotdeal.getDealQuantity() - quantity);
        hotdealRepository.save(hotdeal);

        log.info("{}님 핫딜 상품 구매가 완료되었습니다.", memberId);
        log.info("남은 핫딜 수량 : {}", hotdeal.getDealQuantity());

        return HotdealMapper.INSTANCE.toPurchaseResponseDto(hotdeal);
    }
}
