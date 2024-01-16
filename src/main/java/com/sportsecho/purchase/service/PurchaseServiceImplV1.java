package com.sportsecho.purchase.service;

import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.entity.Member;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.memberProduct.repository.MemberProductRepository;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import com.sportsecho.purchase.entity.Purchase;
import com.sportsecho.purchase.exception.PurchaseErrorCode;
import com.sportsecho.purchase.mapper.PurchaseMapper;
import com.sportsecho.purchase.repository.PurchaseRepository;
import com.sportsecho.purchaseProduct.entity.PurchaseProduct;
import com.sportsecho.purchaseProduct.repository.PurchaseProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Qualifier("V1")
@Service
@RequiredArgsConstructor
public class PurchaseServiceImplV1 implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseProductRepository purchaseProductRepository;
    private final MemberProductRepository memberProductRepository;

    @Override
    @Transactional
    public PurchaseResponseDto purchase(PurchaseRequestDto requestDto, Member member) {

        // 장바구니 목록 찾아오기
        List<MemberProduct> memberProductList = memberProductRepository.findByMemberId(
            member.getId());
        if (memberProductList.isEmpty()) {
            throw new GlobalException(PurchaseErrorCode.EMPTY_CART);
        }

        // 재고 확인
        checkStock(memberProductList);

        // 구매 인스턴스 생성
        Purchase purchase = PurchaseMapper.INSTANCE.toEntity(requestDto, member);
        purchaseRepository.save(purchase);

        // 구매 정보와 장바구니 상품 리스트로 purchaseProduct 엔티티 리스트 생성
        List<PurchaseProduct> purchaseProductList = createPList(memberProductList, purchase);
        purchaseProductRepository.saveAll(purchaseProductList);
        purchase.getPurchaseProductList().addAll(purchaseProductList);

        // 총 금액 업데이트
        purchase.updateTotalPrice(calTotalPrice(purchaseProductList));
        purchaseRepository.save(purchase);

        // 장바구니 비우기
        memberProductRepository.deleteAllByMemberId(member.getId());

        return purchase.createResponseDto();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponseDto> getPurchaseList(Member member) {

        List<Purchase> purchaseList = purchaseRepository.findByMemberId(member.getId());
        if (purchaseList.isEmpty()) {
            throw new GlobalException(PurchaseErrorCode.EMPTY_PURCHASE_LIST);
        }

        return purchaseList.stream()
            .map(Purchase::createResponseDto)
            .toList();
    }

    private int calTotalPrice(List<PurchaseProduct> purchaseProductList) {
        return purchaseProductList.stream()
            .mapToInt(purchaseProduct -> purchaseProduct.getProduct().getPrice()
                * purchaseProduct.getProductsQuantity())
            .sum();
    }

    private List<PurchaseProduct> createPList(List<MemberProduct> memberProductList,
        Purchase purchase) {
        return memberProductList.stream()
            .map(memberProduct -> PurchaseProduct.builder()
                .purchase(purchase)
                .product(memberProduct.getProduct())
                .productsQuantity(memberProduct.getProductsQuantity())
                .build())
            .toList();
    }

    private void checkStock(List<MemberProduct> memberProductList) {
        memberProductList.stream()
            .filter(memberProduct -> memberProduct.getProductsQuantity() >
                memberProduct.getProduct().getQuantity())
            .findFirst()
            .ifPresent(memberProduct -> {
                throw new GlobalException(PurchaseErrorCode.OUT_OF_STOCK);
            });
    }
}
