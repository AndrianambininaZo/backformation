package com.streaming.arosaina.service;

import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private ApplicationUserRepository userRepository;


    public String generateJwt(Authentication authentication){
        Instant now=Instant.now();
        ApplicationUser user=userRepository.findByEmail(authentication.getName());
        String scope=authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claimsSet=JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .claim("string",user.getId())
                .expiresAt(now.plus(120, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("roles",scope)
                .claim("iss","is_formation_v1_v123412345")
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
