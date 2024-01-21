package com.sportsecho.purchase.service;

import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.entity.Member;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.memberProduct.repository.MemberProductRepository;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.repository.ProductRepository;
import com.sportsecho.purchase.dto.PurchaseRequestDto;
import com.sportsecho.purchase.dto.PurchaseResponseDto;
import com.sportsecho.purchase.entity.Purchase;
import com.sportsecho.purchase.exception.PurchaseErrorCode;
import com.sportsecho.purchase.mapper.PurchaseMapper;
import com.sportsecho.purchase.repository.PurchaseRepository;
import com.sportsecho.purchaseProduct.entity.ProductRole;
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

    private final ProductRepository productRepository;
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

        // 재고 확인 후 차감
        updateStock(memberProductList);

        // 구매 인스턴스 생성
        Purchase purchase = PurchaseMapper.INSTANCE.toEntity(requestDto, member);
        purchase = purchaseRepository.save(purchase);

        // 구매 정보와 장바구니 상품 리스트로 purchaseProduct 엔티티 리스트 생성
        List<PurchaseProduct> purchaseProductList = createPList(memberProductList, purchase);

        // 총 금액 업데이트
        purchase.updateTotalPrice(calTotalPrice(purchaseProductList));
        purchaseRepository.save(purchase);

        // 장바구니 비우기
        memberProductRepository.deleteAllByMemberId(member.getId());

        return PurchaseMapper.INSTANCE.toResponseDto(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponseDto> getPurchaseList(Member member) {

        List<Purchase> purchaseList = purchaseRepository.findByMemberId(member.getId());
        if (purchaseList.isEmpty()) {
            throw new GlobalException(PurchaseErrorCode.NOT_FOUND_PURCHASE);
        }

        return purchaseList.stream()
            .map(PurchaseMapper.INSTANCE::toResponseDto)
            .toList();
    }

    @Override
    @Transactional
    public void cancelPurchase(Long purchaseId, Member member) {

        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(
            () -> new GlobalException(PurchaseErrorCode.NOT_FOUND_PURCHASE)
        );
        checkMember(purchase, member);
        updateStock(purchase);

        purchaseProductRepository.deleteByPurchaseId(purchaseId);
        purchaseRepository.deleteById(purchaseId);
    }

    private int calTotalPrice(List<PurchaseProduct> purchaseProductList) {
        return purchaseProductList.stream()
            .mapToInt(purchaseProduct -> purchaseProduct.getProduct().getPrice()
                * purchaseProduct.getProductsQuantity())
            .sum();
    }

    private List<PurchaseProduct> createPList(List<MemberProduct> memberProductList,
        Purchase purchase) {

        List<PurchaseProduct> pList = memberProductList.stream()
            .map(memberProduct -> PurchaseProduct.builder()
                .purchase(purchase)
                .product(memberProduct.getProduct())
                .productsQuantity(memberProduct.getProductsQuantity())
                .role(ProductRole.PRODUCT)
                .build())
            .toList();

        purchaseProductRepository.saveAll(pList);
        purchase.getPurchaseProductList().addAll(pList);

        return pList;
    }

    private void updateStock(Purchase purchase) {
        List<PurchaseProduct> purchaseProductList = purchase.getPurchaseProductList();

        for (PurchaseProduct purchaseProduct : purchaseProductList) {
            Product product = purchaseProduct.getProduct();
            product.increaseQuantity(purchaseProduct.getProductsQuantity());
            productRepository.save(product);
        }
    }

    private void updateStock(List<MemberProduct> memberProductList) {

        for (MemberProduct memberProduct : memberProductList) {
            Product product = memberProduct.getProduct();

            if (memberProduct.getProductsQuantity() > product.getQuantity()) {
                throw new GlobalException(PurchaseErrorCode.OUT_OF_STOCK);
            } else {
                product.decreaseQuantity(memberProduct.getProductsQuantity());
                productRepository.save(product);
            }
        }
    }

    private void checkMember(Purchase purchase, Member member) {
        if (!purchase.getMember().getId().equals(member.getId())) {
            throw new GlobalException(PurchaseErrorCode.ACCESS_DENIED);
        }
    }
}
