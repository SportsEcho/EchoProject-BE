package com.sportsecho.memberProduct.controller;

import com.sportsecho.memberProduct.service.MemberProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberProductController {

    private final MemberProductService MemberProductService;

}
