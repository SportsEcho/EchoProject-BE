package com.sportsecho.common.configuration;

import com.sportsecho.common.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(sess ->
            sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        httpSecurity.authorizeHttpRequests(auth -> auth
            .requestMatchers(publicEndPoints()).permitAll()
            .anyRequest().authenticated());

        //JwtFilter 설정
        httpSecurity.addFilterBefore(jwtAuthorizationFilter,
            UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    public RequestMatcher publicEndPoints() {
        return new OrRequestMatcher(
            new AntPathRequestMatcher("/api/members/login"),
            new AntPathRequestMatcher("/api/members/signup/**"),
            new AntPathRequestMatcher("/api/members/**/callback"),

            //game data load
            new AntPathRequestMatcher("/api/games/football"),
            new AntPathRequestMatcher("/api/games/baseball"),
            new AntPathRequestMatcher("/api/games/basketball"),

            //웹소켓 endpoint
            new AntPathRequestMatcher("/websocket"),
            new AntPathRequestMatcher("/v3/**"),
            new AntPathRequestMatcher("/swagger-ui/**")
        );
    }
}
