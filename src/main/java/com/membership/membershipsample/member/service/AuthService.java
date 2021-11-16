package com.membership.membershipsample.member.service;

import com.membership.membershipsample.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Value("${auth.token.key}")
    private String secretKey;

    private final CookieService cookieService;

    @Autowired
    public AuthService(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    public String issueToken(String userEmail) throws UnsupportedEncodingException {

        LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(20);
        String jwt = Jwts.builder()
                .setSubject(this.secretKey)
                .setExpiration(new Date(
                    expiredDate.getYear(),
                    expiredDate.getMonthValue(), expiredDate.getDayOfMonth(),
                    expiredDate.getHour(), expiredDate.getMinute(), 59))
                .claim("name", userEmail)
                .claim("scope", "membership/member")
                .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                .compact();
        return jwt;
    }

    public Map<String, String> validateToken(String token) {

        Map<String, String> result = new HashMap<String, String>();

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("secret".getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();

            String name = claims.get("name", String.class);
            String scope = claims.get("scope", String.class);

            result.put("name", name);
            result.put("scope", scope);

            return result;
        }
        catch (Exception e) {
            result.put("name", "");
            result.put("scope", "");
            return result;
        }
    }

    public String getUserIdFromJwtCookie(HttpServletRequest request) {
        String userJwt = CookieService.getToken(request);
        Map<String, String> userInfo = this.validateToken(userJwt);
        return userInfo.get("name");
    }

    public String getJwtValueFromHeader(HttpServletRequest request) {
        String userJwt = request.getHeader("Authorization");
        String userId = userJwt.trim().replace("Bearer", "");

        return userId;
    }

    public Member getUserDetails(HttpServletRequest request) {
        String userId = this.getJwtValueFromHeader(request);
        return Member.builder()
            .email(userId)
            .build();
    }
}
