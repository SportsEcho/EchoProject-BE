package com.sportsecho.member.service;

import com.sportsecho.common.jwt.JwtUtil;
import com.sportsecho.global.exception.ErrorCode;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.repository.MemberRepository;
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
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Member signup(MemberRequestDto request) {

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

        return member;
    }

    @Override
    public void login(MemberRequestDto request, HttpServletResponse response) {
        Authentication authentication;

        //ID, PW 검증
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()
                )
            );
        } catch(BadCredentialsException e) {
            throw new GlobalException(ErrorCode.INVALID_AUTH);
        }

        Member member = ((MemberDetailsImpl) authentication.getPrincipal()).getMember();

        String token = jwtUtil.generateToken(member.getEmail(), member.getRole());

        response.setHeader("Authorization", token);
    }

    @Override
    public Member deleteMember(Member member) {
        memberRepository.delete(member);
        return member;
    }
}
