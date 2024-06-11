package com.fenrir.simplebookdatabasesite.security;

import com.fenrir.simplebookdatabasesite.security.jwt.JwtAuthenticationEntryPoint;
import com.fenrir.simplebookdatabasesite.security.jwt.JwtTokenFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    private JwtTokenFilter jwtTokenFilter;
    private static final String[] SWAGGER = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .cors()
                .and()
                    .csrf().disable()
                    .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers(SWAGGER).permitAll()
                    .antMatchers("/actuator/**").permitAll()
                    .antMatchers("/api/auth/**").permitAll()
                    .antMatchers("/api/users/{username}/info").access("@userSecurity.isResourceOwner(authentication, #username)")
                    .antMatchers(
                            HttpMethod.GET,
                            "/api/authors/**",
                            "/api/books/**",
                            "/api/genres/**",
                            "/api/shelves/**",
                            "/api/users/**"
                    ).permitAll()
                    .antMatchers(
                            HttpMethod.POST, "/api/shelves/{username}/**"
                    ).access("@userSecurity.isResourceOwner(authentication, #username)")
                    .antMatchers(
                            "/api/shelves/{username}/**"
                    ).access("hasAnyRole('ADMIN', 'MODERATOR') or @userSecurity.isResourceOwner(authentication, #username)")
                    .antMatchers(
                            "/api/users/{username}/**"
                    ).access("@userSecurity.isResourceOwner(authentication, #username)")
                    .antMatchers(
                            "/api/authors/**",
                            "/api/books/**",
                            "/api/genres/**"
                    ).hasAnyRole("ADMIN", "MODERATOR")
                    .antMatchers("/api/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();
    }
}
