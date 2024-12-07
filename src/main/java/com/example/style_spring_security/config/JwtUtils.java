package com.example.style_spring_security.config;

import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Jwt 工具类
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "xiaolinbao.jwt")
public class JwtUtils {

    private long expire;
    private String secret;
    private String header;

    /**
     * 生成 JWT
     *
     * @return token String
     */
    public  String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成JWT
     *
     * @param subject token中要存放的数据 (json格式)
     * @return token String
     */
    public  String createJwt(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID()); // 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jwt,通过指定过期时间
     *
     * @param subject   token中要存放的数据 (json格式)
     * @param ttlMillis token过期时间
     * @return String
     */
    public  String createJwt(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    /**
     * 生成签名
     *
     * @param subject   token中要存放的数据 (json格式)
     * @param ttlMillis 过期时间(毫秒)
     * @param uuid      uuid
     * @return JwtBuilder
     */
    private JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = expire;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        log.info(">> expDate: {}, nowMillis: {} ttlMillis: {}", expDate, nowMillis, ttlMillis);
        return Jwts.builder()
                .setId(uuid) // 唯一ID
                .setSubject(subject)// 主题 可以是json数据
                .setIssuer("sg")// 签发者
                .setIssuedAt(now)// 签发时间
                .signWith(signatureAlgorithm, secretKey)// 使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);

    }

    /**
     * 创建token，指定id，过期时间
     *
     * @param id
     * @param subject   token中要存放的数据 (json格式)
     * @param ttlMillis 过期时间(毫秒)
     * @return token String
     */
    public  String createJwt(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);
        return builder.compact();
    }

    /**
     * 生成加密后的密钥 secret
     *
     * @return SecretKey
     */
    public SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析JWT
     *
     * @param jwt token
     * @return 原码数据
     */
    public Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 解析token，获取payload
     * @param token
     * @return
     */
    public Claims extractAllClaims(String token) {
        Claims claims;
        // 无论是否过期，都返回claims对象
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException e){
            claims = e.getClaims();
        }
        return claims;
    }

    /**
     * JWT token是否过期
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Boolean isTokenExpired(Claims claim){
        return claim.getExpiration().before(new Date());
    }
}
