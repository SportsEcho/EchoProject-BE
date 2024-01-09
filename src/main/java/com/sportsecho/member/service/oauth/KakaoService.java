package com.sportsecho.member.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsecho.common.jwt.JwtUtil;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.dto.KakaoUserInfoDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.exception.MemberErrorCode;
import com.sportsecho.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j(topic = "KakaoService")
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public void kakaoLogin(String code, HttpServletResponse response) {
        try {
            // 1. "인가 코드"로 "엑세스 토큰" 요청
            String accessToken = getToken(code);

            // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
            KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

            // 3. 필요시에 회원가입 -> 미가입된 사용자라면 회원가입 후 로그인 처리 후 JWT발급
            Member kakaoMember = registerKakaoMemberIfNeeded(kakaoUserInfo);

            // 4. JWT 토큰 반환
            String aToken = jwtUtil.generateAccessToken(kakaoMember.getEmail(), kakaoMember.getRole());
            String rToken = jwtUtil.generateRefreshToken();
            jwtUtil.setJwtHeader(response, aToken, rToken);
        } catch(JsonProcessingException e) {
            throw new GlobalException(MemberErrorCode.DUPLICATED_EMAIL);
        }
    }

    private String getToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
            .fromUriString("https://kauth.kakao.com")
            .path("/oauth/token")
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoApiKey);
        body.add("redirect_uri", "http://localhost:8080/api/members/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
            .fromUriString("https://kapi.kakao.com")
            .path("/v2/user/me")
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
            .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
            .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    private Member registerKakaoMemberIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();
        Member kakaoMember = memberRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoMember == null) {
            // 카카오 사용자 email과 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            Member sameEmailMember = memberRepository.findByEmail(kakaoEmail).orElse(null);

            if (sameEmailMember != null) {
                kakaoMember = sameEmailMember;
            } else {
                // email: kakao email & password: random UUID
                String email = kakaoUserInfo.getEmail();
                String encodedPassword = passwordEncoder.encode(UUID.randomUUID().toString());

                kakaoMember = Member.builder()
                    .memberName(kakaoUserInfo.getNickname())
                    .email(email)
                    .password(encodedPassword)
                    .role(MemberRole.CUSTOMER)
                    .build();
            }

            //KakaoId update 및 저장
            kakaoMember = kakaoMember.updateKakaoId(kakaoId);
            memberRepository.save(kakaoMember);
        }

        return kakaoMember;
    }
}
