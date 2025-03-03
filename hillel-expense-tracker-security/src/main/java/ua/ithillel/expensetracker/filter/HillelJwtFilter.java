package ua.ithillel.expensetracker.filter;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.ithillel.expensetracker.model.security.HillelUserDetails;
import ua.ithillel.expensetracker.util.JwtUtil;

import java.io.IOException;


@RequiredArgsConstructor
public class HillelJwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            Claims claims = jwtUtil.parseToken(token);

            String username = claims.getSubject();

            SecurityContext context = SecurityContextHolder.getContext();

            if (username != null && context.getAuthentication() == null) {
                HillelUserDetails userDetails = (HillelUserDetails) userDetailsService.loadUserByUsername(username);

                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }


        filterChain.doFilter(request, response);
    }
}
