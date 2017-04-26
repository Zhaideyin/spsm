package com.ronxuntech.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;


public class JwtsUtil {
	public static final JwtsUtil single=new JwtsUtil();
	private JwtsUtil() {

	}
	public static JwtsUtil newInstance(){
		return single;
	}
	public Map<String,Object> generateToken(String storeKey,Object storeValue,String keyString){
		Map<String,Object> data=new HashMap<String,Object>();
		Key key=generateKey(keyString);
		JwtBuilder jb=Jwts.builder();
		jb.claim(storeKey, storeValue);
		//5天过期
		int exp=1000*60*60*24*5;
		long now=System.currentTimeMillis();

		jb.setExpiration(new Date(now+exp));
		
		String token = jb.signWith(SignatureAlgorithm.HS256, key).compact();
		data.put("accessToken", token);
		data.put("expiresAt", now+exp);
		return data;
	}
	
	public Map<String,Object> validateToken(String storeKey,String keyString,String token){
		if(null==token||"".equalsIgnoreCase(token)){
			return null;
		}
		Key key=generateKey(keyString);
		try {		
			Jws<Claims> jws=Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			
			Claims claims=jws.getBody();
			Map<String,Object> data=new HashMap<String,Object>();
			data.put(storeKey, claims.get(storeKey));
			data.put("expiresAt",claims.getExpiration().getTime());
			return data;
		} catch (SignatureException e) {
		    //don't trust the JWT!
		}catch(MalformedJwtException e){
			//token不正确
		}catch(ExpiredJwtException e){
			//token过期
		}catch(ArrayIndexOutOfBoundsException e){
			//token长度不对
		}
		return null;
	}
	private Key generateKey(String keyString) {
		Base64 base64 = new Base64();
		byte[] encodedKey;
		encodedKey = base64.encode(keyString.getBytes());
		Key key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "DES");
		return key;
	}
}
