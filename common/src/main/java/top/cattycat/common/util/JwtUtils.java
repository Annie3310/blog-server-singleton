package top.cattycat.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import top.cattycat.common.pojo.oauth.github.response.GitHubUserInfoResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 王金义
 * @date 2022/1/10
 */
public class JwtUtils {
    private final static String SECRET = "aee1afc825e4a2cf3b6cc90fd0ff8bc8c34038aa04163bb1ef6e4519e3d8e023";
    private final static Long EXPIRE_TIME = TimeUnit.DAYS.toMillis(7);

    public static String getToken(GitHubUserInfoResponse userInfo) {
        final String login = userInfo.getLogin();

        // jwt生成时间 当前时间
        final long nowMillis = System.currentTimeMillis();
        final Date date = new Date(nowMillis);

        final HashMap<String, Object> claims = new HashMap(2) {
            {
                put("id", userInfo.getId());
                put("login", login);
                put("avatar", userInfo.getAvatarUrl());
            }
        };
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(date)
                .setSubject(login)
                .setExpiration(new Date(nowMillis + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean verify(String token) {
        try {
            parseToken(token);
        } catch (Exception e) {
            // Usually SignatureException
            return false;
        }
        return true;
    }
}
