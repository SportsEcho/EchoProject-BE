package com.sportsecho.memberProduct.service;

import com.sportsecho.memberProduct.repository.MemberProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProductServiceImplV1 implements MemberProductService {

    private final MemberProductRepository memberProductRepository;

}
