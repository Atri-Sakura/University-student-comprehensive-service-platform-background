package com.gzu.utils;

import com.gzu.contant.TokenConstants;
import com.gzu.contant.TokenExpiration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.sql.Time;
import java.util.Date;
import java.util.Map;

/**
 * Jwt工具
 */
public class JwtUtil {
    /**
     * 令牌密钥
     */
    public static String secret = TokenConstants.SECRET;

    /**
     * 过期时间
     */
    private static final long EXPIRATION =3600*1000;



    public static String generateToken(String username) {
        String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }
    public static String createToken(Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从JWT中获取声明
     * @param token
     * @return
     */
    public static Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 从Token中获得用户名
     * @param token
     * @return
     */
    public static String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 判断Token
     * @param token
     * @param username
     * @return
     */
    public static boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username));
    }

    /**
     * token过期判断
     * @param token
     * @return
     */
    public static boolean isTokenExpired(String token) {
        final Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }



}
