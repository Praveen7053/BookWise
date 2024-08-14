package com.bookWise.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class BookWiseSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean(name = "authenticationManagerBean")
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().ignoringAntMatchers("/userSignupNLogin/loginRegisteredUser", "/userSignupNLogin/registerNewUser") // Disable CSRF for these endpoints
               // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Enable CSRF with tokens in cookies
                .and()
                .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .antMatchers("/home").hasRole("USER")
                    .antMatchers("/sellerHome").hasRole("ADMIN")
                    .antMatchers("/UserSignUp.jsp").permitAll()
                    .antMatchers("/userSignUpPage").permitAll()
                    .antMatchers("/userSignupNLogin/loginRegisteredUser").permitAll()
                    .antMatchers("/userSignupNLogin/registerNewUser").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .permitAll()
            )
            .logout(logout ->
                logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login") // Redirect to login page after logout
                    .permitAll()
                    .invalidateHttpSession(true) // Invalidate session
                    .deleteCookies("JSESSIONID") // Delete cookies
            );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/resources/**");
    }

}
