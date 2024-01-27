package com.sportsecho.hotdeal.controller;

import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.PurchaseHotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.UpdateHotdealInfoRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.hotdeal.service.HotdealService;
import com.sportsecho.member.entity.MemberDetailsImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HotdealController {

    @Autowired
    private @Qualifier("V2") HotdealService hotdealService;

    @PostMapping("/products/{productId}/hotdeals")
    public ResponseEntity<HotdealResponseDto> createHotdeal(
        @PathVariable Long productId,
        @RequestBody HotdealRequestDto requestDto
    ) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(hotdealService.createHotdeal(productId, requestDto));
    }

    @GetMapping("/hotdeals")
    public ResponseEntity<List<HotdealResponseDto>> getHotdealList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        // 마감 임박한 상품 우선 정렬 기본값
        @RequestParam(defaultValue = "dueDay,asc") String sort
    ) {

        String[] sortParams = sort.split(",");
        Sort sortObj = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        List<HotdealResponseDto> responseDtoList = hotdealService.getHotdealList(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);

    }

    @GetMapping("/hotdeals/{hotdealId}")
    public ResponseEntity<HotdealResponseDto> getHotdeal(
        @PathVariable Long hotdealId
    ) {

        HotdealResponseDto responseDto = hotdealService.getHotdeal(hotdealId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/hotdeals/{hotdealId}")
    public ResponseEntity<HotdealResponseDto> updateHotdeal(
        @PathVariable Long hotdealId,
        @RequestBody UpdateHotdealInfoRequestDto requestdto
    ) {

        HotdealResponseDto responseDto = hotdealService.updateHotdeal(hotdealId, requestdto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/hotdeals/{hotdealId}")
    public ResponseEntity<Void> deleteHotdeal(@PathVariable Long hotdealId) {
        hotdealService.deleteHotdeal(hotdealId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/hotdeals/purchase")
    public ResponseEntity<Void> purchaseHotdeal(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @RequestBody PurchaseHotdealRequestDto requestDto
    ) {
        hotdealService.purchaseHotdeal(memberDetails.getMember(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/hotdeals/{hotdealId}/set")
    public ResponseEntity<Void> settingHotdeal(
        @PathVariable Long hotdealId
    ) {
        hotdealService.hotdealSetting(hotdealId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
