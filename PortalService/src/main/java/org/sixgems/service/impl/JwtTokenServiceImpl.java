package org.sixgems.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.sixgems.model.api.SsoUserDetails;
import org.sixgems.service.api.JwtTokenService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Julian on 30.06.2015.
 */
public class JwtTokenServiceImpl implements JwtTokenService {

    private Key key;

    private String issuer;

    private SignatureAlgorithm signatureAlgorithm;

    //Default validityPeriod of 10 minutes
    private int validityPeriodInMin = 10;

    public JwtTokenServiceImpl() {
        key = MacProvider.generateKey();
        try{
            issuer = InetAddress.getLocalHost().getHostName();
        }
        catch(UnknownHostException e){
            issuer = "";
        }
    }

    @Override
    public String convertToJwt(SsoUserDetails userDetails) {

        Date issuedAt = new Date();
        Date expirationAt = getExpDate(issuedAt);

        Claims claims = Jwts.claims(generateClaimsFromUserDetails(userDetails));
        claims.setSubject(userDetails.getSsoUserName());
        claims.setIssuer(issuer);
        claims.setIssuedAt(issuedAt);
        claims.setExpiration(expirationAt);

        String jwtString = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setClaims(claims)
                .signWith(signatureAlgorithm, key)
                .compact();
        return jwtString;
    }

    @Override
    public boolean isValid(String jwt) {
        try{
            return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody().getExpiration().after(new Date());
        }catch(Exception e){
            return false;
        }
    }

    private Map<String, Object> generateClaimsFromUserDetails(SsoUserDetails userDetails){

        Map<String, Object> claims = new HashMap<>();
        Map<String, Object> attributes = userDetails.getSsoUserAttributes();
        Collection<String> groups = userDetails.getSsoUserGroups();

        //put all additional user attributes in the claims map, which should be passed to the backend-services
        if (attributes!=null && !attributes.isEmpty()){
            claims.putAll(attributes);
        }

        //put List of groups in the claims-payload of the JWT
        if (!groups.isEmpty()){
            claims.put("groups", groups);
        }

        return claims;
    }

    private Date getExpDate(Date issuedAt){

        Calendar cal = Calendar.getInstance();
        cal.setTime(issuedAt);
        cal.add(Calendar.MINUTE, validityPeriodInMin);
        return cal.getTime();
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public int getValidityPeriodInMin() {
        return validityPeriodInMin;
    }

    public void setValidityPeriodInMin(int validityPeriodInMin) {
        this.validityPeriodInMin = validityPeriodInMin;
    }
}
