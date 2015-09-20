package io.oasp.gastronomy.restaurant.general.common.impl.security;

import java.security.Key;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.oasp.gastronomy.restaurant.general.common.api.UserProfile;
import io.oasp.gastronomy.restaurant.general.common.api.security.UserData;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public final class MyTokenHandler {

    private final Key secret;

    private KeyHandler keyHandler;

    public MyTokenHandler(String secret) {
    	keyHandler = new KeyHandler();
    	try{
    		this.secret = keyHandler.getPublicKey("public_key.der");
    	}
    	catch (Exception e){
    		throw new IllegalArgumentException("no valid Public Key present");
    	}

    }

    public UserData parseUserFromToken(String token) {
        Claims claims = parseClaimsFromToken(token);
        String username = claims.getSubject();

        HashMap<String, ArrayList<String>> apps = (HashMap<String, ArrayList<String>>) claims.get("groups");
        ArrayList<String> authorityList = new ArrayList<String>();
        if(apps.containsKey("restaurant")) {
          authorityList.addAll(apps.get("restaurant"));
        }
        if(apps.containsKey("general")) {
          authorityList.addAll(apps.get("general"));
        }

        HashSet<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
        for(String authorityString : authorityList) {
          SimpleGrantedAuthority authority = new SimpleGrantedAuthority(authorityString);
          authorities.add(authority);
        }
        UserData user = new UserData(username, "", authorities);

        return user;
    }

    public UserProfile parseUserProfileFromToken(String token){
    	Claims claims = parseClaimsFromToken(token);
    	String username = claims.getSubject();
    	String commonName = (String) claims.get("cn");

      HashMap<String, ArrayList<String>> apps = (HashMap<String, ArrayList<String>>) claims.get("groups");
      ArrayList<String> groupList = new ArrayList<String>();
      if(apps.containsKey("restaurant")) {
        groupList.addAll(apps.get("restaurant"));
      }
      if(apps.containsKey("general")) {
        groupList.addAll(apps.get("general"));
      }

    	return new TokenUserProfile(username, getFirstNameFromCN(commonName), getLastNameFromCN(commonName), groupList);
    }

    private Claims parseClaimsFromToken(String token){
    	Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    	return claims;
    }

    private String getFirstNameFromCN(String commonName){
    	return commonName.trim().split("\\s+")[0];
    }

    private String getLastNameFromCN(String commonName){
    	return commonName.trim().split("\\s+")[1];
    }

}
