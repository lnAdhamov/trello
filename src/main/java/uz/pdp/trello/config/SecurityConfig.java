package uz.pdp.trello.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import uz.pdp.trello.service.UserService;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    private final CustomAuthenticationErrorHandler customAuthenticationErrorHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(manager -> {
            manager
                    .requestMatchers("/register", "/login", "/error", "/css/**").permitAll()
                    .requestMatchers(
                            "/task/add/",
                            "/task/removefile/",
                            "/task/addmember/",
                            "/task/applydeadline",
                            "/task/applyname/",
                            "/task/removemember/",
                            "/task/changetasklist/",
                            "/task/delete/",
                            "/tasklist/delete/",
                            "/tasklist/edit/",
                            "/tasklist/add/",
                            "/board/add/"
                    ).hasRole("ADMIN")
                    .anyRequest().authenticated();
        });

        http.formLogin(manager -> {
            manager.loginPage("/login")
                    .usernameParameter("phone")
                    .passwordParameter("password")
                    .failureHandler(customAuthenticationErrorHandler)
                    .defaultSuccessUrl("/");
        });

        http.logout(manager -> {
            manager.logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"));
        });

        http.rememberMe(manager -> {
            manager
                    .alwaysRemember(true)
                    .tokenValiditySeconds(60)
                    .rememberMeParameter("rem-me");
        });

        http.userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
