package com.team01.realestate.security.config;

import com.team01.realestate.security.jwt.AuthEntryPointJwt;
import com.team01.realestate.security.jwt.AuthTokenFilter;
import com.team01.realestate.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt authEntryPointJwt;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf ->csrf.disable())
                .exceptionHandling(exceptionHandling->
                        exceptionHandling.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize->
                        authorize.requestMatchers(AUTH_WHITE_LIST).permitAll()
                                .requestMatchers(HttpMethod.GET, "/adverts").permitAll()  // /adverts için GET izinli
                                .anyRequest().authenticated());
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(jwt ->
//                                jwt.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//                        ));

        http.headers( headers-> headers.
                frameOptions(frameOptions -> frameOptions.sameOrigin()));
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                // uygulamamıza gelen her isteğe bu ayarlar uygulanır.
                registry.addMapping("/**")
                        // tüm kaynaklara (farklı sunuculara veya domainlere) isteğe
                        // izin verildiğini belirtir. Yani, başka bir sunucudan gelen
                        // isteklere izin verilir.
                        .allowedOrigins("*",
                                "http://localhost:3000")
                        // gelen isteklerdeki başlıklar (örneğin, Authorization
                        // veya Content-Type) herhangi bir sınırlama olmadan kabul edilir.
                        .allowedHeaders("*")
                        // tüm HTTP metotlarına (GET, POST, PUT, DELETE vb.) isteğe izin verilir.
                        .allowedMethods("*");
            }
        };
    }


    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "swagger-ui.html",
            "/swagger-ui/**",
            "index.html",
            "/",
            "/images/**",
            "/css/**",
            "/js/**",
            "/contactMessages/save",
            "/login",
            "/google-login",
            "/register",
            "/forgot-password",
            "/reset-password",
            "/contact-messages/create",
            "/categories",
            "/cities",
            "/countries",
            "/districts",
            "/advert-types",
            "/adverts/cities",
            "/adverts/popular",
            "/adverts/categories",
            "/adverts/{slug}",
            "/categories/list",
    };
}