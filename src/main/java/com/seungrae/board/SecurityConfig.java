package com.seungrae.board;

import com.seungrae.board.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 동작
public class SecurityConfig {

    @Bean
        // 외부 라이브러리를 디펜던시인젝션으로 사용하고 싶을 때.
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        // 세션 데이터 생성하지 X
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterBefore(new JwtFilter(), ExceptionTranslationFilter.class);

        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/**").permitAll()
        );
        // 폼으로 자동 로그인. 실패하면 ?error
//        http.formLogin((formLogin) -> formLogin.loginPage("/login")
//                .defaultSuccessUrl("/")
//        );

        // @PreAuthorize("isAuthenticated()")등 인증 안된 사용자 접근 시 컨트롤 진입 전 로그인 안내 출력
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> // 인증 X
                        res.sendRedirect("/login?needAuth=true"))
                .accessDeniedHandler((req, res, e) -> // 권한이 없을 때
                        res.sendRedirect("/?denied=true"))
        );
        // 로그아웃 jwt
        http.logout(logout -> logout
                .logoutUrl("/logout")                 // 기본 POST
                .deleteCookies("jwt")                 // 또는 addLogoutHandler(new CookieClearingLogoutHandler("jwt"))
                .logoutSuccessUrl("/login")                // 끝난 뒤 이동
                .permitAll()
        );
        //http.logout(logout -> logout.logoutUrl("/logout"));
        return http.build();

    }
}