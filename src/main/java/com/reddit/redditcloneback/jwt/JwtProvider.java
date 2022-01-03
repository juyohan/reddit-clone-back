package com.reddit.redditcloneback.jwt;

import com.reddit.redditcloneback.dto.UserDto;
import com.reddit.redditcloneback.oauth.dto.CustomOAuth2User;
import com.reddit.redditcloneback.security.dto.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.reddit.redditcloneback.exception.Exceptions.*;

@Component
public class JwtProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
    public static final long tokenValidityInMilliseconds = 30 * 60 * 1000L; // 30분
    private final String secret;
    private Key key;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 토큰을 생성하는 함수이다.
     *
     * @param authentication
     * @return
     */
    public UserDto.Response.LoginDetail create(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String imagePath = "";

        if (authentication.getPrincipal() instanceof CustomUserDetails)
            imagePath = ((CustomUserDetails) authentication.getPrincipal()).getImagePath();
        else if (authentication.getPrincipal() instanceof CustomOAuth2User)
            imagePath = ((CustomOAuth2User) authentication.getPrincipal()).getImagePath();

        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityInMilliseconds);

        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("imagePath", imagePath)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .compact();

        return new UserDto.Response.LoginDetail(authentication.getName(), token, imagePath, now + tokenValidityInMilliseconds);
    }


    /**
     *
     * @param claims
     * @param refreshRange
     * @return
     */
    private boolean canUpdateToken(Claims claims, long refreshRange) {
        long exp = claims.getExpiration().getTime();
        if (exp > 0) {
            long remain = exp - System.currentTimeMillis();
            return remain < refreshRange;
        }
        return false;
    }

    /**
     *
     * @param claims
     * @return
     */
    private String updateToken(Claims claims) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(claims.getSubject())
                .claim(AUTHORITIES_KEY, claims.get("auth"))
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 토큰을 파싱하는 함수이다.
     * @param token
     * @param res
     * @return
     */
    public Authentication parseJws(String token, ServletResponse res) {
        System.out.println("parsing 시작");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 10분보다 적게 남았을 때, 재발급
        if (canUpdateToken(claims, 10 * 60 * 1000L)) {
            String updateToken = updateToken(claims);
            HttpServletResponse response = (HttpServletResponse) res;
            response.addHeader("Authorization", updateToken);
        }

        List<GrantedAuthority> authorities =
                Arrays.asList(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .stream().map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        CustomUserDetails principal = new CustomUserDetails(claims.getSubject(), "", authorities, claims.get("imagePath").toString());
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰의 유효성 검사를 처리하는 함수이다.
     * @param token 토큰의 정보가 담긴 데이터
     * @return 유효성 검사를 제대로 통과를 했다면, true 를 반환한다.
     */
    public boolean validateToken(
            String token,
            HttpServletRequest request
    ) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", JWT_WRONG_TYPE_TOKEN.getCode());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", JWT_EXPIRED_TOKEN.getCode());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", JWT_UNSUPPORTED_TOKEN.getCode());
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", JWT_WRONG_TOKEN.getCode());
        } catch (RuntimeException e) {
            request.setAttribute("exception", JWT_UNKNOWN_ERROR.getCode());
        }
        return false;
    }

}
