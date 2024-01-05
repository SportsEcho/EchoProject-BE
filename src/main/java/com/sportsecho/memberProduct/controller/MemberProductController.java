package com.sportsecho.memberProduct.controller;

import com.sportsecho.memberProduct.service.MemberProductServiceImplV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberProductController {

    private final MemberProductServiceImplV1 MemberProductService;

}
