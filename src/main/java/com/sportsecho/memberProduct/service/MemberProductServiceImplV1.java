package com.sportsecho.memberProduct.service;

import com.sportsecho.common.exception.GlobalException;
import com.sportsecho.member.entity.Member;
import com.sportsecho.memberProduct.dto.MemberProductRequestDto;
import com.sportsecho.memberProduct.dto.MemberProductResponseDto;
import com.sportsecho.memberProduct.entity.MemberProduct;
import com.sportsecho.memberProduct.exception.MemberProductErrorCode;
import com.sportsecho.memberProduct.mapper.MemberProductMapper;
import com.sportsecho.memberProduct.repository.MemberProductRepository;
import com.sportsecho.product.entity.Product;
import com.sportsecho.product.repository.ProductRepository;
import java.util.List;
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
    @Transactional(readOnly = true)
    public List<MemberProductResponseDto> getCart(Member member) {

        List<MemberProduct> memberProductList = memberProductRepository.findByMemberId(
            member.getId());

        return memberProductList.stream()
            .map(MemberProductMapper.INSTANCE::toResponseDto)
            .toList();
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId, Member member) {
        MemberProduct memberProduct = findMemberProduct(cartId);
        checkMember(member.getId(), memberProduct);
        memberProductRepository.delete(memberProduct);
    }

    @Override
    @Transactional
    public void deleteAllCart(Member member) {
        memberProductRepository.deleteAllByMemberId(member.getId());
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new GlobalException(MemberProductErrorCode.NOT_FOUND_PRODUCT)
            );
    }

    private MemberProduct findMemberProduct(Long cartId) {
        return memberProductRepository.findById(cartId)
            .orElseThrow(() -> new GlobalException(MemberProductErrorCode.NOT_FOUND_PRODUCT_IN_CART)
            );
    }

    private void checkMember(Long memberId, MemberProduct memberProduct) {
        if (!memberId.equals(memberProduct.getMember().getId())) {
            throw new GlobalException(MemberProductErrorCode.ACCESS_DENIED);
        }
    }
}
