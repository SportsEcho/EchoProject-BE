package com.sportsecho.memberProduct.service;

import com.sportsecho.member.entity.Member;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.memberProduct.mapper.MemberProductMapper;
import com.sportsecho.memberProduct.repository.MemberProductRepository;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.repository.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Qualifier("V1")
@Service
@RequiredArgsConstructor
public class MemberProductServiceImplV1 implements MemberProductService {

    private final MemberProductRepository memberProductRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberProductResponseDto addCart(Long productId, MemberProductRequestDto requestDto,
        Member member) {

        Product product = findProduct(productId);
        Optional<MemberProduct> memberProduct = memberProductRepository.findByProductIdAndMemberId(
            productId, member.getId());
        MemberProductResponseDto responseDto;

        if (memberProduct.isPresent()) {
            memberProduct.get().updateQuantity(requestDto.getProductsQuantity());
            responseDto = MemberProductMapper.INSTANCE.toResponseDto(memberProduct.get());
        } else {
            MemberProduct savedMemberProduct = MemberProductMapper.INSTANCE.toEntity(
                requestDto, member, product);
            memberProductRepository.save(savedMemberProduct);
            responseDto = MemberProductMapper.INSTANCE.toResponseDto(savedMemberProduct);
        }

        return responseDto;
    }

    @Override
    @Transactional
    public void deleteCart(Long productId, Member member) {
        MemberProduct memberProduct = findMemberProduct(productId, member.getId());
        checkMember(member.getId(), memberProduct);
        memberProductRepository.delete(memberProduct);
    }

    @Override
    @Transactional
    public void deleteAllCart(Member member) {
        memberProductRepository.deleteByMemberId(member.getId());
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new NullPointerException("상품이 존재하지 않습니다.")
            );
    }

    private MemberProduct findMemberProduct(Long productId, Long memberId) {
        return memberProductRepository.findByProductIdAndMemberId(productId, memberId)
            .orElseThrow(() -> new NullPointerException("장바구니에 상품이 존재하지 않습니다.")
            );
    }

    private void checkMember(Long memberId, MemberProduct memberProduct) {
        if (!memberId.equals(memberProduct.getMember().getId())) {
            throw new IllegalArgumentException("본인만 수정/삭제 가능합니다.");
        }
    }
}
