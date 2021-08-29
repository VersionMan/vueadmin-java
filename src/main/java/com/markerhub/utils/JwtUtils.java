package com.markerhub.utils;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")
public class JwtUtils {

	private long expire;
	private String secret; //密钥
	private String header;

	// 生成jwt
	public String generateToken(String username) {

		Date nowDate = new Date();
		Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

		return Jwts.builder()
				.setHeaderParam("typ", "JWT") //type为jwt
				.setSubject(username) //所面对的主体
				.setIssuedAt(nowDate)//创建时间
				.setExpiration(expireDate)// 7天過期
				.signWith(SignatureAlgorithm.HS512, secret) //加密算法
				.compact(); //合成
	}

	// 解析jwt
	public Claims getClaimByToken(String jwt) {  //Claims类
		try {
			return Jwts.parser()
					.setSigningKey(secret) //放密钥
					.parseClaimsJws(jwt)//解析
					.getBody(); //返回body
		} catch (Exception e) {
			return null;
		}
	}

	// jwt是否过期
	public boolean isTokenExpired(Claims claims) {
		return claims.getExpiration().before(new Date());
	}

}
