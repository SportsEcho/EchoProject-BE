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
        MemberProduct memberProduct = MemberProductMapper.INSTANCE.toEntity(requestDto, member,
            product);

        memberProductRepository.save(memberProduct);
        member.getMemberProductList().add(memberProduct);
        product.getMemberProductList().add(memberProduct);

        return MemberProductMapper.INSTANCE.toResponseDto(memberProduct);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
            new NullPointerException("상품이 존재하지 않습니다.")
        );
    }
}
