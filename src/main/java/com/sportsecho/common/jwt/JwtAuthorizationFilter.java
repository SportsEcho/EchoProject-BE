package com.sportsecho.common.jwt;

import com.sportsecho.member.service.MemberDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JwtAuthorizationFilter")
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberDetailsServiceImpl memberDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        //Bearer를 포함한 tokenValue
        String tokenValue = jwtUtil.getAccessToken(request);

        if(StringUtils.hasText(tokenValue)) {
            String token = jwtUtil.substringToken(tokenValue);

            //token 검증 - 토큰이 유효하지 않으면 false 반환
            if(!jwtUtil.validateToken(token)) return;

            String email = jwtUtil.getSubject(token);

            try {
                setAuthentication(email);
            } catch(Exception e) {
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * authentication이 세팅돼있어야 authorization이 가능하다.
     * publicEndPoint가 아닌 경우에 header값을 넣지 않으면 doFilter() 메서드가 실행되는데
     * 이때, authentication이 세팅되어있지 않기 때문에 SpringSecurity에 걸리게 되어 403을 응답받는다.
     * */
    public void setAuthentication(String email) {
        Authentication authentication = createAuthentication(email);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public Authentication createAuthentication(String email) {
        UserDetails memberDetails = memberDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
    }
}
