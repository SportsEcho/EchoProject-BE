package com.sportsecho.purchase.service;

import com.sportsecho.member.entity.Member;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.memberProduct.repository.MemberProductRepository;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import com.sportsecho.purchase.entity.Purchase;
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
        Purchase purchase = PurchaseMapper.INSTANCE.toEntity(requestDto, member);
        List<MemberProduct> memberProductList = memberProductRepository.findByMemberId(
            member.getId());

        // 총 금액 업데이트
        updateTotalPrice(memberProductList, purchase);
        purchaseRepository.save(purchase);

        // 구매 정보와 장바구니 상품 리스트로 purchaseProduct 엔티티 리스트 생성
        List<PurchaseProduct> purchaseProductList = createPList(memberProductList, purchase);
        purchaseProductRepository.saveAll(purchaseProductList);
        purchase.getPurchaseProductList().addAll(purchaseProductList);

        // 장바구니 비우기
        memberProductRepository.deleteByMemberId(member.getId());

        return purchase.createResponseDto(memberProductList);
    }

    private void updateTotalPrice(List<MemberProduct> memberProductList, Purchase purchase) {
        int totalPrice = memberProductList.stream()
            .mapToInt(memberProduct -> memberProduct.getProduct().getPrice()
                * memberProduct.getProductsQuantity())
            .sum();

        purchase.updateTotalPrice(totalPrice);
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
}
