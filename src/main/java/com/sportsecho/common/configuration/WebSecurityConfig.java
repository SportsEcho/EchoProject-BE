package com.sportsecho.common.configuration;

import com.sportsecho.common.jwt.JwtAuthorizationFilter;
import com.sportsecho.common.jwt.JwtExceptionFilter;
import com.sportsecho.member.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    //@Bean 내부에서 사용할 변수이기 때문에 static final로 선언
    private static final String GET = HttpMethod.GET.name();
    private static final String POST = HttpMethod.POST.name();
    private static final String PATCH = HttpMethod.PATCH.name();
    private static final String DELETE = HttpMethod.DELETE.name();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(Customizer.withDefaults())// CORS 활성화
            .csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(sess ->
            sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        httpSecurity.authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(publicEndPoints()).permitAll()
            .requestMatchers(adminEndPoints()).hasAuthority(MemberRole.ADMIN.name())
            .anyRequest().authenticated());

        //JwtFilter 설정
        httpSecurity.addFilterBefore(
            jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class
        );

        //Authorization 에서 발생하는 Exception에 대한 GlobalException 처리
        httpSecurity.addFilterBefore(
            jwtExceptionFilter, JwtAuthorizationFilter.class
        );

        return httpSecurity.build();
    }

    private RequestMatcher publicEndPoints() {
        return new OrRequestMatcher(
            //사용자(관리자) 로그인,회원가입 및 소셜로그인
            new AntPathRequestMatcher("/api/members/login", POST),
            new AntPathRequestMatcher("/api/members/signup/**", GET),
            new AntPathRequestMatcher("/api/members/**/callback", GET),

            //게임 전체 조회와 게임 단건조회
            new AntPathRequestMatcher("/api/games", GET),
            new AntPathRequestMatcher("/api/games/details/{gameId}", GET),

            //상품 페이지 조회와 상품 단건조회
            new AntPathRequestMatcher("/api/products", GET),
            new AntPathRequestMatcher("/api/products/{productId}", GET),

            //핫딜 페이지 조회와 핫딜 단건조회
            new AntPathRequestMatcher("/api/hotdeals", GET),
            new AntPathRequestMatcher("/api/hotdeals/{hotdealId}", GET),

            //STOMP Connection
            new AntPathRequestMatcher("/websocket"),

            //Swagger
            new AntPathRequestMatcher("/v3/**"),
            new AntPathRequestMatcher("/swagger-ui/**")
        );
    }

    private RequestMatcher adminEndPoints() {
        return new OrRequestMatcher(
            //상품 생성, 수정, 삭제
            new AntPathRequestMatcher("/api/products", POST),
            new AntPathRequestMatcher("/api/products/{productId}", PATCH),
            new AntPathRequestMatcher("/api/products/{productId}", DELETE)
        );
    }
}
