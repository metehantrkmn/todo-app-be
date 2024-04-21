package com.metehan.config;

import com.metehan.authenticationApi.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtservice;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException
    {


        Cookie cookies[] = request.getCookies();

        if(cookies == null){
            System.out.println("this is control block");
            filterChain.doFilter(request,response);
            return;
        }

        if(Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("token")).collect(Collectors.toList()).size() == 0 || Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("token")).collect(Collectors.toList()).get(0).getValue().equals("")){
            System.out.println("token doesnt exist!!!");
            filterChain.doFilter(request,response);
            return;
        }

        String token = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("token")).collect(Collectors.toList()).get(0).getValue();
        System.out.println("this is outer block");
        System.out.println(token);
/*
        String authHeader = request.getHeader("Authorization");


        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            //if jwt doesnt exist do filters but dont go on rest of the method
            filterChain.doFilter(request,response);
            return;
        }

        //gets token string from the bearer header
        String token = authHeader.substring(7);*/

        //Extract username from token
        String username = jwtservice.extractUsername(token);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails user = userDetailsService.loadUserByUsername(username);
            //check database if the specified token already revoked or expired
            boolean isTokenValid = tokenRepository.findByToken(token).map(t ->
               !t.isExpired() && !t.isRevoked()
            ).orElse(false);
            System.out.println(user.getUsername());
            if(jwtservice.isTokenValid(token,user) && isTokenValid){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                        null,
                        user.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
