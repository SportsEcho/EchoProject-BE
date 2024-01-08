package com.sportsecho.member.service;

import com.sportsecho.common.jwt.JwtUtil;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImplV1 implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MemberResponseDto signup(MemberRequestDto request) {

        //email duplicate check
        if(memberRepository.findByEmail(request.getEmail()).isPresent())
            throw new GlobalException(ErrorCode.DUPLICATED_USER_NAME);

        Member member = Member.builder()
                .memberName(request.getMemberName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(MemberRole.CUSTOMER)
                .build();


        memberRepository.save(member);

        return MemberResponseDto.builder()
            .memberName(member.getMemberName())
            .email(member.getEmail())
            .build();
    }

    @Override
    public void login(MemberRequestDto request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Member member = ((MemberDetailsImpl) authentication.getPrincipal()).getMember();

            String token = jwtUtil.generateAccessToken(member.getEmail(), member.getRole());

            response.setHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        } catch(BadCredentialsException e) {
            throw new GlobalException(ErrorCode.INVALID_AUTH);
        }
    }

    @Override
    public void logout(Member member, HttpServletRequest request) {}

    @Override
    public void refresh(HttpServletRequest request) {}

    @Override
    @Transactional
    public MemberResponseDto deleteMember(Member member) {
        memberRepository.delete(member);

        return MemberResponseDto.builder()
            .memberName(member.getMemberName())
            .email(member.getEmail())
            .build();
    }
}
