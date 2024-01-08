package com.sportsecho.member.service;

import com.sportsecho.common.jwt.JwtUtil;
import com.sportsecho.common.jwt.exception.JwtErrorCode;
import com.sportsecho.common.redis.RedisUtil;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.member.exception.MemberErrorCode;
import com.sportsecho.member.mapper.MemberMapper;
import com.sportsecho.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * V2 업데이트 내용
 * - V1에서 builder로 생성하던 MemberEntity를 MemberMapper를 통해 생성
 * - V1에서 builder로 생성해 반환하던 MemberResponseDto를 MemberMapper를 통해 생성
 * */
@Service("V2")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImplV2 implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MemberResponseDto signup(MemberRequestDto request) {

        //email duplicate check
        if(memberRepository.findByEmail(request.getEmail()).isPresent())
            throw new GlobalException(MemberErrorCode.DUPLICATED_EMAIL);

        //MemberMapper를 이용한 Entity 생성
        Member member = MemberMapper.MAPPER.toEntity(request);
        member.passwordEncode(passwordEncoder);

        memberRepository.save(member);


        return MemberMapper.MAPPER.toResponseDto(member);
    }

    @Override
    public void login(MemberRequestDto request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Member member = ((MemberDetailsImpl) authentication.getPrincipal()).getMember();

            String accessToken = jwtUtil.generateAccessToken(member.getEmail(), member.getRole());
            String refreshToken = jwtUtil.generateRefreshToken();

            //ResponseHeader에 토큰 추가
            setTokenHeader(response, accessToken, refreshToken);

            redisUtil.saveRefreshToken(refreshToken, member.getEmail());

        } catch(BadCredentialsException e) {
            throw new GlobalException(MemberErrorCode.INVALID_AUTH);
        }
    }

    @Override
    public void logout(Member member, HttpServletRequest request) {
        String refreshToken = jwtUtil.getRefreshToken(request);

        //redis에 refreshToken이 존재하는 경우
        if(redisUtil.isExist(refreshToken)) {

            //현재 접속중인 사용자가 로그아웃을 요청한 사용자인지 확인
            if(member.getEmail().equals(redisUtil.getEmail(refreshToken))) {
                redisUtil.removeToken(refreshToken);
            } else {
                throw new GlobalException(MemberErrorCode.INVALID_REQUEST);
            }
        } else {
            throw new GlobalException(JwtErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
    }

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.getRefreshToken(request);

        //redis에 refreshToken이 존재하는 경우
        if(redisUtil.isExist(refreshToken)) {
            Member member = memberRepository.findByEmail(redisUtil.getEmail(refreshToken))
                .orElseThrow(() -> new GlobalException(MemberErrorCode.USER_NOT_FOUND));

            //accessToken 발급
            String accessToken = jwtUtil.generateAccessToken(member.getEmail(), member.getRole());

            setTokenHeader(response, accessToken, refreshToken);
        } else {
            throw new GlobalException(JwtErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public MemberResponseDto deleteMember(Member member) {
        memberRepository.delete(member);
        return MemberMapper.MAPPER.toResponseDto(member);
    }

    private void setTokenHeader(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        response.setHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER, refreshToken);
    }
}
