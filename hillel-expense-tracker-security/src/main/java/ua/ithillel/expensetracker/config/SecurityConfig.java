package ua.ithillel.expensetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ua.ithillel.expensetracker.auth.HillelBasicAuthEntryPoint;
import ua.ithillel.expensetracker.repo.UserRepo;
import ua.ithillel.expensetracker.service.HillelUserDetailService;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HillelBasicAuthEntryPoint basicAuthEntryPoint) throws Exception {
        http.
                csrf(conf -> conf.disable())
                .authorizeHttpRequests(conf ->
                        conf
                                .requestMatchers(antMatcher("/v1/auth/**")).permitAll()
                                .requestMatchers(antMatcher("/v1/health")).permitAll()
                                .anyRequest().authenticated())
                .httpBasic(conf -> conf.authenticationEntryPoint(basicAuthEntryPoint));


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepo userRepo) {
        return new HillelUserDetailService(userRepo);
    }

//    @Bean
//    public UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) {
//        UserDetails defaultUser = User.builder()
//                .username("hillel")
//                .password(passwordEncoder.encode("hillel134"))
//                .build();
//
//        return new InMemoryUserDetailsManager(defaultUser);
//    }
}
