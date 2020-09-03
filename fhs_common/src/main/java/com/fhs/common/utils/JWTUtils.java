package com.fhs.common.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;

/**
 * @Author: Jun
 * @Description: JWT签发与解析
 * @Date: 2020-08-13 14:33
 */
public class JWTUtils {

    /**
     * 签发JWT
     * @param claims
     * @param secret
     * @param ttlMillis
     * @return  String
     */
    public static String createJWT(Map claims, String secretKey, long ttlMillis) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuer("XHB")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(SignatureAlgorithm.HS512, secretKey); // 签名算法以及密匙
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate); // 过期时间
        }
        return builder.compact();
    }


    /**
     *
     * 解析JWT字符串
     * @param jwt
     * @return
     */
    public static Claims parseJWT(String jwt,String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
    }

}
