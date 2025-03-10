package ua.ithillel.expensetracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.ithillel.expensetracker.auth.GoogleAuthErrorHandler;
import ua.ithillel.expensetracker.auth.GoogleAuthSuccessHandler;
import ua.ithillel.expensetracker.auth.HillelBasicAuthEntryPoint;
import ua.ithillel.expensetracker.auth.JwtAuthEntryPoint;
import ua.ithillel.expensetracker.client.GoogleAccessResponseClient;
import ua.ithillel.expensetracker.filter.HillelJwtFilter;
import ua.ithillel.expensetracker.repo.UserRepo;
import ua.ithillel.expensetracker.service.GoogleOAuth2UserService;
import ua.ithillel.expensetracker.service.GoogleOidcUserService;
import ua.ithillel.expensetracker.service.HillelUserDetailService;
import ua.ithillel.expensetracker.util.JwtUtil;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private GoogleAuthSuccessHandler googleAuthSuccessHandler;
    @Autowired
    private GoogleAuthErrorHandler googleAuthErrorHandler;
    @Autowired
    private GoogleAccessResponseClient googleAccessResponseClient;
    @Autowired
    private GoogleOidcUserService googleOidcUserService;
    @Autowired
    private GoogleOAuth2UserService googleOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HillelBasicAuthEntryPoint basicAuthEntryPoint, HillelJwtFilter jwtFilter) throws Exception {
        // basic auth
//        http.
//                csrf(conf -> conf.disable())
//                .authorizeHttpRequests(conf ->
//                        conf
//                                .requestMatchers(antMatcher("/v1/auth/**")).permitAll()
////                                .requestMatchers(antMatcher("/v1/user/{userId}/**")).access(new WebExpressionAuthorizationManager("#userId == authentication.principal.id"))
//                                .requestMatchers(antMatcher("/v1/health")).permitAll()
//                                .requestMatchers(antMatcher("/v1/users/**")).hasAuthority("ROLE_ADMIN")
//                                .anyRequest().authenticated())
//                .httpBasic(conf -> conf.authenticationEntryPoint(basicAuthEntryPoint));

        // jwt auth
//        http
//                .csrf(conf -> conf.disable())
//                .authorizeHttpRequests(conf ->
//                        conf
//                                .requestMatchers(antMatcher("/v1/auth/**")).permitAll()
////                                .requestMatchers(antMatcher("/v1/user/{userId}/**")).access(new WebExpressionAuthorizationManager("#userId == authentication.principal.id"))
//                                .requestMatchers(antMatcher("/v1/health")).permitAll()
//                                .requestMatchers(antMatcher("/v1/users/**")).hasAuthority("ROLE_ADMIN")
//                                .anyRequest().authenticated())
//                .exceptionHandling(conf ->
//                        conf.authenticationEntryPoint(new JwtAuthEntryPoint()))
//                .sessionManagement(conf ->
//                        conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(conf ->
                        conf
                                .requestMatchers(antMatcher("/v1/auth/**")).permitAll()
//                                .requestMatchers(antMatcher("/v1/user/{userId}/**")).access(new WebExpressionAuthorizationManager("#userId == authentication.principal.id"))
                                .requestMatchers(antMatcher("/v1/health")).permitAll()
                                .requestMatchers(antMatcher("/v1/users/**")).hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated())
                .exceptionHandling(conf ->
                        conf.authenticationEntryPoint(new JwtAuthEntryPoint()))
                .sessionManagement(conf ->
                        conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(conf ->
                        conf
                                .authorizationEndpoint(end ->
                                        end.baseUri("/v1/auth/oauth2"))
                                .redirectionEndpoint(redirect ->
                                        redirect.baseUri("/v1/auth/google/callback"))

                                .successHandler(googleAuthSuccessHandler)
                                .failureHandler(googleAuthErrorHandler)

                                .tokenEndpoint(token ->
                                        token.accessTokenResponseClient(googleAccessResponseClient))

                                .userInfoEndpoint(user ->
                                        user
                                                .userService(googleOAuth2UserService)
                                                .oidcUserService(googleOidcUserService))

                );


        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public HillelJwtFilter jwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        return new HillelJwtFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepo userRepo) {
        return new HillelUserDetailService(userRepo);
    }
}
