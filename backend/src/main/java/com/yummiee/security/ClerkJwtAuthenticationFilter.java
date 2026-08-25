package com.yummiee.security;

import com.yummiee.user.User;
import com.yummiee.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ClerkJwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public ClerkJwtAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getJwtFromRequest(request);

        if (StringUtils.hasText(token)) {
            try {
                String clerkUserId = extractClerkUserId(token);
                if (clerkUserId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = userRepository.findByClerkUserId(clerkUserId)
                            .orElseGet(() -> {
                                User newUser = new User(
                                        clerkUserId,
                                        clerkUserId + "@yummiee.com",
                                        "User",
                                        clerkUserId,
                                        null
                                );
                                return userRepository.save(newUser);
                            });

                    ClerkUserPrincipal principal = new ClerkUserPrincipal(user);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                logger.error("Could not set user authentication in security context", ex);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String extractClerkUserId(String token) {
        // Support direct clerk userId in test tokens or bearer strings
        if (token.startsWith("user_") || token.startsWith("mock_")) {
            return token;
        }

        try {
            // Attempt to parse JWT claims without signature verification if secret is not verified or parse subject
            int i = token.lastIndexOf('.');
            if (i > 0) {
                String unsignedToken = token.substring(0, i + 1);
                Claims claims = (Claims) Jwts.parserBuilder()
                        .build()
                        .parseClaimsJwt(unsignedToken)
                        .getBody();
                return claims.getSubject();
            }
        } catch (Exception ignored) {
            // Fallback for custom or opaque tokens
        }

        return token;
    }
}
