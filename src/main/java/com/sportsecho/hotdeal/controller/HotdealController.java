package com.sportsecho.hotdeal.controller;

import com.sportsecho.common.dto.ApiResponse;
import com.sportsecho.hotdeal.dto.request.HotdealRequestDto;
import com.sportsecho.hotdeal.dto.request.UpdateHotdealInfoRequestDto;
import com.sportsecho.hotdeal.dto.response.HotdealResponseDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.service.HotdealService;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberDetailsImpl;
import java.util.List;
import lombok.Getter;
import org.apache.coyote.Request;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.DomainEvents;
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
    private @Qualifier("V1") HotdealService hotdealService;

    @PostMapping("/products/{productId}/hotdeal")
    public ResponseEntity<ApiResponse<HotdealResponseDto>> createHotdeal(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @PathVariable Long productId,
        @RequestBody HotdealRequestDto requestDto
    ) {

        HotdealResponseDto responseDto = hotdealService.createHotdeal(memberDetails.getMember(),
            productId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of("핫딜 생성 성공", 201, responseDto)
        );
    }

    @GetMapping("products/{productId}/hotdeals")
    public ResponseEntity<ApiResponse<List<HotdealResponseDto>>> getHotdealList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        // 마감 임박한 상품 우선 정렬 기본값
        @RequestParam(defaultValue = "dueDay,asc") String sort
    ) {

        String[] sortParams = sort.split(",");
        Sort sortObj = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        List<HotdealResponseDto> responseDtoList = hotdealService.getHotdealList(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of("핫딜 목록 조회 성공", 200, responseDtoList)
        );

    }

    @GetMapping("/products/{productId}/hotdeal/{hotdealId}")
    public ResponseEntity<ApiResponse<HotdealResponseDto>> getHotdeal(
        @PathVariable Long hotdealId
    ) {

        HotdealResponseDto responseDto = hotdealService.getHotdeal(hotdealId);

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of("핫딜 단건 조회 성공", 201, responseDto)
        );
    }

    @PatchMapping("/products/{productId}/hotdeal/{hotdealId}")
    public ResponseEntity<ApiResponse<HotdealResponseDto>> updateHotdeal(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @PathVariable Long hotdealId,
        @RequestBody UpdateHotdealInfoRequestDto requestdto
    ) {

        HotdealResponseDto responseDto = hotdealService.updateHotdeal(memberDetails.getMember(),
            hotdealId, requestdto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse.of("핫딜 정보 수정 성공", 200, responseDto)
        );
    }

    @DeleteMapping("/hotdeal/{hotdealId}")
    public ResponseEntity<ApiResponse<Void>> deleteHotdeal(
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @PathVariable Long hotdealId
    ) {

        hotdealService.deleteHotdeal(memberDetails.getMember(), hotdealId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            ApiResponse.of("핫딜 삭제 성공", 204, null)
        );
    }

}
